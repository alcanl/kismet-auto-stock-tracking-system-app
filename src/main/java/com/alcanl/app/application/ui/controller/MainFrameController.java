package com.alcanl.app.application.ui.controller;

import com.alcanl.app.application.ui.event.DisposeEvent;
import com.alcanl.app.application.ui.event.ShowFormEvent;
import com.alcanl.app.application.ui.event.UpdateTablesEvent;
import com.alcanl.app.helper.table.TableInitializer;
import com.alcanl.app.helper.DialogHelper;
import com.alcanl.app.application.ui.view.form.MainForm;
import com.alcanl.app.configuration.CurrentUserConfig;
import com.alcanl.app.helper.Resources;
import com.alcanl.app.helper.table.search.type.StockMovementSearchType;
import com.alcanl.app.service.ApplicationService;
import com.alcanl.app.service.dto.ProductDTO;
import com.formdev.flatlaf.FlatClientProperties;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.function.Consumer;

import static com.alcanl.app.helper.Resources.EMPTY_STRING;

@Slf4j
@Controller
@DependsOn({"bean.form.login","bean.form.main","bean.dialog.add.new.product"})
@RequiredArgsConstructor
public class MainFrameController extends JFrame {

    @Value("${kismet.auto.stock.tracking.system.app.frame.main.dimension.x}")
    private int m_mainFrameStartDimensionX;
    @Value("${kismet.auto.stock.tracking.system.app.frame.main.dimension.y}")
    private int m_mainFrameStartDimensionY;
    private final MainForm m_mainForm;
    private final Resources m_resources;
    private final CurrentUserConfig m_currentUserConfig;
    private final ApplicationService m_applicationService;
    private final ApplicationContext m_applicationContext;
    private final TableInitializer m_tableInitializer;
    private final ApplicationEventPublisher m_applicationEventPublisher;
    private final DialogHelper m_dialogHelper;

    @PostConstruct
    private void setFrameProperties()
    {
        initializeFrame();
        initializeTables();
        initializeBars();
        initializeStockMovementsTab();
        m_mainForm.getButtonRightBar().putClientProperty( FlatClientProperties.STYLE, "arc: 10" );
        m_mainForm.getButtonAddStock().putClientProperty( FlatClientProperties.STYLE, "arc: 10" );
        m_mainForm.getButtonReleaseStock().putClientProperty( FlatClientProperties.STYLE, "arc: 10" );
        setMinimumSize(new Dimension(m_mainFrameStartDimensionX, m_mainFrameStartDimensionY));
        setSize(new Dimension(m_mainFrameStartDimensionX, m_mainFrameStartDimensionY));
        m_resources.centerFrame(this);
        setResizable(true);
    }
    @Async
    @EventListener
    public void onTableEventReceived(UpdateTablesEvent ignore)
    {
        reInitTables();
    }

    private void initializeWindowListener()
    {
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowOpened(WindowEvent e) {
                var text = m_mainForm.getLabelWelcomeUser().getText();
                m_mainForm.getLabelWelcomeUser().setText(String.format(text, m_currentUserConfig.getUser().toString()));
            }
        });
    }

    private void initializeFrame()
    {
        setContentPane(m_mainForm.getPanelMain());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initializeWindowListener();
        setUndecorated(true);
        m_resources.setLayout();
        m_resources.initializeLogo(this);
    }

    private void initializeExitButton()
    {
        m_mainForm.getButtonExit().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                m_mainForm.getButtonExit().setBackground((Color)m_applicationContext.getBean("bean.color.button.exit"));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                m_mainForm.getButtonExit().setBackground((Color)m_applicationContext.getBean("bean.color.default"));
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e) &&
                        m_resources.showEnsureExitMessageDialog() == JOptionPane.YES_OPTION) {
                    MainFrameController.this.dispose();
                    m_applicationEventPublisher.publishEvent(new DisposeEvent(this));
                }
            }
        });
    }

    private void initializeMinimizeButton()
    {
        m_mainForm.getButtonMinimize().addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e))
                    MainFrameController.this.setState(Frame.ICONIFIED);
            }
        });
    }

    private void initializeMaximizeButton()
    {
        m_mainForm.getButtonMaximize().addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                maximizeButtonOnClickedCallback(e);
            }
        });
    }

    private void maximizeButtonOnClickedCallback(MouseEvent e)
    {
        if (SwingUtilities.isLeftMouseButton(e) && MainFrameController.this.getExtendedState() == Frame.MAXIMIZED_BOTH) {
            MainFrameController.this.setExtendedState(Frame.NORMAL);
            ((JLabel)(m_mainForm.getButtonMaximize().getComponent(0))).setIcon(
                    (Icon) m_applicationContext.getBean("bean.image.icon.maximize.partial.window")
            );
        }
        else if (SwingUtilities.isLeftMouseButton(e) && MainFrameController.this.getExtendedState() == Frame.NORMAL) {
            MainFrameController.this.setExtendedState(Frame.MAXIMIZED_BOTH);
            ((JLabel)(m_mainForm.getButtonMaximize().getComponent(0))).setIcon(
                    (Icon) m_applicationContext.getBean("bean.image.icon.maximize.full.window")
            );
        }
    }

    private void initializeLeftBarButton(JPanel panel, String bean)
    {
        setOnPanelButtonClickListener(panel, e -> {
                    var menu = ((JPopupMenu)m_applicationContext.getBean(bean));
                    menu.pack();
                    menu.show(panel, panel.getComponent(0).getX(),panel.getY() + panel.getHeight());
        });
    }

    private void setOnPanelButtonClickListener(JPanel panel, Consumer<MouseEvent> consumer)
    {
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                consumer.accept(e);
            }
        });
    }

    private void initializeButtonLogout()
    {
        setOnPanelButtonClickListener(m_mainForm.getButtonLogout(), event -> {
           if (m_resources.showEnsureLogoutMessageDialog(m_currentUserConfig.getUser().toString()) == JOptionPane.YES_OPTION) {
               this.dispose();
               m_applicationEventPublisher.publishEvent(new ShowFormEvent(this));
           }
        });
    }

    private void initializeBars()
    {
        initializeTopBar();
        initializeRightSideBar();
    }
    private void initializeTopBar()
    {
        for (Component component: m_mainForm.getPanelTopBar().getComponents())
            if (component instanceof JPanel jpanel) {
                if (jpanel.equals(m_mainForm.getPanelLogo()) || jpanel.equals(m_mainForm.getButtonExit()))
                    continue;

                initializeBars(jpanel);
            }

        initializeExitButton();
        initializeMaximizeButton();
        initializeMinimizeButton();
        initializeTopBarClickListener();
        initializeButtonLogout();
        initializeLeftBarButton(m_mainForm.getButtonNew(),"bean.menu.popup.top.bar.new");
        initializeLeftBarButton(m_mainForm.getButtonEdit(),"bean.menu.popup.top.bar.edit");
        initializeLeftBarButton(m_mainForm.getButtonSettings(),"bean.menu.popup.top.bar.settings");
    }

    private void initializeTopBarClickListener()
    {
        setOnPanelButtonClickListener(m_mainForm.getPanelTopBar(), e -> {
            if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2)
                maximizeButtonOnClickedCallback(e);
            });

    }

    private void initializeBars(JPanel jPanel)
    {
        jPanel.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                jPanel.setBackground(Color.LIGHT_GRAY);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                jPanel.setBackground((Color)m_applicationContext.getBean("bean.color.default"));
            }
        });
    }
    private void reInitTables()
    {
        m_tableInitializer.setTables(m_mainForm.getTableStockOut(), m_mainForm.getTableLesserThanThreshold());
        m_tableInitializer.reInitTables();
        m_mainForm.getLabelCount().setText("%d".formatted(TableInitializer.criticalStockCount));
    }

    private void initializeTables()
    {
        m_tableInitializer.setTables(m_mainForm.getTableStockOut(), m_mainForm.getTableLesserThanThreshold(), m_mainForm.getTableStockInput(), m_mainForm.getTableStockOutput());
        m_tableInitializer.initializeStockOutTables();
        m_tableInitializer.initializeStockMovementTables(StockMovementSearchType.NONE);
        m_mainForm.getLabelCount().setText("%d".formatted(TableInitializer.criticalStockCount));
    }
    private void initializeRightSideBar()
    {
        initializeBars(m_mainForm.getButtonRightBar());
        initializeBars(m_mainForm.getButtonAddStock());
        initializeBars(m_mainForm.getButtonReleaseStock());

        m_mainForm.getButtonRightBar().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (m_mainForm.getPanelStockState().getWidth() > 0) {
                    m_mainForm.getPanelStockState().setSize(0, m_mainForm.getPanelStockState().getHeight());
                    m_mainForm.getPanelMainContainer().setSize(
                            m_mainForm.getPanelMainContainer().getWidth() + 350, m_mainForm.getPanelMainContainer().getHeight());
                    m_mainForm.getIconBar().setIcon((Icon)m_applicationContext.getBean("bean.image.icon.right.bar.open"));
                }
                else {
                    m_mainForm.getPanelStockState().setSize(350, m_mainForm.getPanelStockState().getHeight());
                    m_mainForm.getPanelMainContainer().setSize(
                            m_mainForm.getPanelMainContainer().getWidth() - 350, m_mainForm.getPanelMainContainer().getHeight());
                    m_mainForm.getIconBar().setIcon((Icon)m_applicationContext.getBean("bean.image.icon.right.bar.close"));
                }
            }
        });
        m_mainForm.getButtonAddStock().addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                m_dialogHelper.showAdditionFastStockDialog();
            }
        });
        m_mainForm.getButtonReleaseStock().addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                m_dialogHelper.showReleaseFastStockDialog();
            }
        });
    }
    private void initializeStockMovementsTab()
    {
        initializeCheckBoxes();

        m_mainForm.getButtonSearch().addActionListener(e -> {
            if (m_mainForm.getCheckBoxByProduct().isSelected() && m_mainForm.getCheckBoxByUser().isSelected() && m_mainForm.getCheckBoxByDate().isSelected())
                buttonSearchAllCheckBoxesSelected();
            else if (m_mainForm.getCheckBoxByProduct().isSelected() && m_mainForm.getCheckBoxByUser().isSelected())
                buttonSearchProductAndUserSelected();
            else if (m_mainForm.getCheckBoxByProduct().isSelected() && m_mainForm.getCheckBoxByDate().isSelected())
                buttonSearchProductAndDateSelected();
            else if (m_mainForm.getCheckBoxByDate().isSelected() && m_mainForm.getCheckBoxByUser().isSelected())
                buttonSearchDateAndUserSelected();
            else if (m_mainForm.getCheckBoxByProduct().isSelected())
                buttonSearchProductSelected();
            else if (m_mainForm.getCheckBoxByUser().isSelected())
                buttonSearchUserSelected();
            else if (m_mainForm.getCheckBoxByDate().isSelected())
                buttonSearchDateSelected();
            else
                m_dialogHelper.showNoSelectedSearchMessage();
        });
    }
    private void buttonSearchProductAndUserSelected()
    {
        StockMovementSearchType.setProductId(m_mainForm.getTextFieldOriginalCode().getText());
        StockMovementSearchType.setUserName(m_mainForm.getTextFieldUserName().getText());
        m_tableInitializer.initializeStockMovementTables(StockMovementSearchType.PRODUCT_AND_USER);
    }
    private void buttonSearchProductSelected()
    {
        StockMovementSearchType.setProductId(m_mainForm.getTextFieldOriginalCode().getText());
        m_tableInitializer.initializeStockMovementTables(StockMovementSearchType.PRODUCT);
    }
    private void buttonSearchUserSelected()
    {
        StockMovementSearchType.setUserName(m_mainForm.getTextFieldUserName().getText());
        m_tableInitializer.initializeStockMovementTables(StockMovementSearchType.USER);
    }
    private void buttonSearchDateSelected()
    {
        StockMovementSearchType.setStartDate(m_mainForm.getButtonStartDate().getDate());
        StockMovementSearchType.setEndDate(m_mainForm.getButtonEndDate().getDate());
        m_tableInitializer.initializeStockMovementTables(StockMovementSearchType.DATE);
    }
    private void buttonSearchDateAndUserSelected()
    {
        StockMovementSearchType.setStartDate(m_mainForm.getButtonStartDate().getDate());
        StockMovementSearchType.setEndDate(m_mainForm.getButtonEndDate().getDate());
        StockMovementSearchType.setUserName(m_mainForm.getTextFieldUserName().getText());
        m_tableInitializer.initializeStockMovementTables(StockMovementSearchType.USER_AND_DATE);
    }
    private void buttonSearchProductAndDateSelected()
    {
        StockMovementSearchType.setProductId(m_mainForm.getTextFieldOriginalCode().getText());
        StockMovementSearchType.setStartDate(m_mainForm.getButtonStartDate().getDate());
        StockMovementSearchType.setEndDate(m_mainForm.getButtonEndDate().getDate());
        m_tableInitializer.initializeStockMovementTables(StockMovementSearchType.PRODUCT_AND_DATE);
    }
    private void buttonSearchAllCheckBoxesSelected()
    {
        if (m_mainForm.getTextFieldStockCode().getText().isEmpty() && m_mainForm.getTextFieldOriginalCode().getText().isEmpty()) {
            m_dialogHelper.showEmptyFieldsWarningDialog();
            return;
        }
        else if (!m_mainForm.getTextFieldOriginalCode().getText().isEmpty())
            StockMovementSearchType.setProductId(m_mainForm.getTextFieldOriginalCode().getText());
        else {
            StockMovementSearchType.setProductId(m_applicationService.findProductByStockCode(
                    m_mainForm.getTextFieldStockCode().getText()
            ).map(ProductDTO::getOriginalCode).orElse(EMPTY_STRING));
        }
        if (m_mainForm.getTextFieldUserName().getText().isEmpty()) {
            m_dialogHelper.showEmptyFieldsWarningDialog();
            return;
        }
        if (!m_mainForm.getButtonStartDate().isTextFieldValid() || m_mainForm.getButtonEndDate().isTextFieldValid()) {
            m_dialogHelper.showEmptyFieldsWarningDialog();
            return;
        }
        StockMovementSearchType.setUserName(m_mainForm.getTextFieldUserName().getText());
        StockMovementSearchType.setStartDate(m_mainForm.getButtonStartDate().getDate());
        StockMovementSearchType.setEndDate(m_mainForm.getButtonEndDate().getDate());
        m_tableInitializer.initializeStockMovementTables(StockMovementSearchType.ALL);
    }
    private void initializeCheckBoxes()
    {
        m_mainForm.getCheckBoxByDate().addActionListener(e -> {
            if (m_mainForm.getCheckBoxByDate().isSelected()) {
                m_mainForm.getButtonEndDate().setEnabled(true);
                m_mainForm.getButtonStartDate().setEnabled(true);
            }
            else {
                m_mainForm.getButtonEndDate().setEnabled(false);
                m_mainForm.getButtonStartDate().setEnabled(false);
            }
        });

        m_mainForm.getCheckBoxByUser().addActionListener(e -> {
            if (m_mainForm.getCheckBoxByUser().isSelected()) {
                m_mainForm.getTextFieldUserName().setEnabled(true);
                m_mainForm.getTextFieldUserName().setEditable(true);
            }
            else {
                m_mainForm.getTextFieldUserName().setEnabled(false);
                m_mainForm.getTextFieldUserName().setEditable(false);
            }
        });

        m_mainForm.getCheckBoxByProduct().addActionListener(e -> {
            if (m_mainForm.getCheckBoxByProduct().isSelected()) {
                m_mainForm.getTextFieldOriginalCode().setEnabled(true);
                m_mainForm.getTextFieldOriginalCode().setEditable(true);
                m_mainForm.getTextFieldStockCode().setEnabled(true);
                m_mainForm.getTextFieldStockCode().setEditable(true);
            }
            else {
                m_mainForm.getTextFieldOriginalCode().setEnabled(false);
                m_mainForm.getTextFieldOriginalCode().setEditable(false);
                m_mainForm.getTextFieldStockCode().setEnabled(false);
                m_mainForm.getTextFieldStockCode().setEditable(false);
            }
        });
    }
}
