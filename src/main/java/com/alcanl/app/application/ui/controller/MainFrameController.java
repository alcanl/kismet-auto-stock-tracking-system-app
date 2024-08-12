package com.alcanl.app.application.ui.controller;

import com.alcanl.app.application.ui.event.DisposeEvent;
import com.alcanl.app.application.ui.event.ShowLoginFormEvent;
import com.alcanl.app.application.ui.event.UpdateTablesEvent;
import com.alcanl.app.application.ui.event.UserLoginEvent;
import com.alcanl.app.application.ui.view.form.MainForm;
import com.alcanl.app.application.ui.view.popup.notification.CriticalStockNotificationPopUp;
import com.alcanl.app.helper.types.UserType;
import com.alcanl.app.helper.table.TableInitializer;
import com.alcanl.app.helper.dialog.DialogHelper;
import com.alcanl.app.configuration.CurrentUserConfig;
import com.alcanl.app.helper.table.search.type.StockMovementSearchType;
import com.alcanl.app.repository.exception.EmailAlreadyInUseException;
import com.alcanl.app.repository.exception.UsernameAlreadyInUseException;
import com.alcanl.app.service.ApplicationService;
import com.alcanl.app.service.dto.ProductDTO;
import com.alcanl.app.service.dto.UserDTO;
import com.formdev.flatlaf.FlatClientProperties;
import com.github.lgooddatepicker.components.DatePicker;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.print.PrinterException;
import java.time.chrono.ChronoLocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

import static com.alcanl.app.helper.Resources.EMPTY_STRING;

@Slf4j
@Controller("bean.frame.controller.main")
@RequiredArgsConstructor
public class MainFrameController extends JFrame {

    @Value("${kismet.auto.stock.tracking.system.app.frame.main.dimension.x}")
    private int m_mainFrameStartDimensionX;
    @Value("${kismet.auto.stock.tracking.system.app.frame.main.dimension.y}")
    private int m_mainFrameStartDimensionY;
    private final HashMap<Component, String> m_componentMap;
    private final MainForm m_mainForm;
    private final ExecutorService m_threadPool;
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
        initializeProductListTab();
        m_mainForm.getButtonNotification().putClientProperty( FlatClientProperties.STYLE, "arc: 10" );
        m_mainForm.getButtonAddStock().putClientProperty( FlatClientProperties.STYLE, "arc: 10" );
        m_mainForm.getButtonReleaseStock().putClientProperty( FlatClientProperties.STYLE, "arc: 10" );
        setMinimumSize(new Dimension(m_mainFrameStartDimensionX, m_mainFrameStartDimensionY));
        setSize(new Dimension(m_mainFrameStartDimensionX, m_mainFrameStartDimensionY));
        m_dialogHelper.centerFrame(this);
        setResizable(true);
    }
    @Async
    @EventListener
    public void onTableEventReceived(UpdateTablesEvent ignore)
    {
        reInitTables();
    }
    @Async
    @EventListener
    public void onUserLoginReceived(UserLoginEvent ignore)
    {
        m_mainForm.getButtonClear().doClick();
        m_mainForm.getButtonClearFields().doClick();
        m_dialogHelper.clearFields(m_mainForm.getPanelEditUser(), m_mainForm.getPanelSaveNewUser());
        m_mainForm.getIconEditUserOldPassword().setIcon(
                m_applicationContext.getBean("bean.image.icon.password.field.hidden", ImageIcon.class));
        m_mainForm.getIconEditUserNewPassword().setIcon(
                m_applicationContext.getBean("bean.image.icon.password.field.hidden", ImageIcon.class));
        initializeUserOperationsTab();
        m_mainForm.getTabbedPaneMain().setSelectedIndex(0);
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

        m_threadPool.execute(() -> addWheelListenerCallback(m_mainForm.getPanelMain()));
    }
    private void addWheelListenerCallback(JComponent jComponent)
    {
        if (jComponent instanceof JTable || jComponent instanceof JComboBox)
            return;

        if (jComponent.getComponentCount() > 0)
            Arrays.stream(jComponent.getComponents()).forEach(component -> addWheelListenerCallback ((JComponent)component));

        jComponent.addMouseWheelListener(e -> {
            if (e.isControlDown())
                if (e.getWheelRotation() < 0)
                    m_threadPool.execute(m_dialogHelper::setFontLarger);
                else
                    m_threadPool.execute(m_dialogHelper::setFontSmaller);
        });
    }

    private void initializeFrame()
    {
        setContentPane(m_mainForm.getPanelMain());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initializeWindowListener();
        setUndecorated(true);
        m_dialogHelper.setLayout();
        m_dialogHelper.initializeLogo(this);
    }

    private void initializeExitButton()
    {
        m_mainForm.getButtonExit().addMouseListener(exitButtonClickedCallback());
        m_mainForm.getIconExit().addMouseListener(exitButtonClickedCallback());
    }
    private MouseAdapter exitButtonClickedCallback()
    {
        return new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                m_mainForm.getButtonExit().setBackground((Color) m_applicationContext.getBean("bean.color.button.exit"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                m_mainForm.getButtonExit().setBackground((Color) m_applicationContext.getBean("bean.color.default"));
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e) &&
                        m_dialogHelper.showEnsureExitMessageDialog() == JOptionPane.YES_OPTION) {
                    MainFrameController.this.dispose();
                    m_applicationEventPublisher.publishEvent(new DisposeEvent(this));
                }
            }
        };
    }

    private void initializeMinimizeButton()
    {
        m_mainForm.getButtonMinimize().addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (SwingUtilities.isLeftMouseButton(e))
                            MainFrameController.this.setState(Frame.ICONIFIED);
                    }
                });

        m_mainForm.getIconMinimize().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e))
                    MainFrameController.this.setState(Frame.ICONIFIED);
            }
        });
    }

    private void initializeMaximizeButton()
    {
        m_mainForm.getButtonMaximize().addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                maximizeButtonOnClickedCallback(e);
            }
        });
        m_mainForm.getIconMaximize().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
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
            public void mousePressed(MouseEvent e) {
                consumer.accept(e);
            }
        });

        Arrays.stream(panel.getComponents()).forEach(
                component -> component.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        consumer.accept(e);
                    }
                }));
    }

    private void initializeButtonLogout()
    {
        setOnPanelButtonClickListener(m_mainForm.getButtonLogout(), event -> {
           if (m_dialogHelper.showEnsureLogoutMessageDialog(m_currentUserConfig.getUser().toString()) == JOptionPane.YES_OPTION) {
               m_mainForm.getButtonLogout().setBackground(m_applicationContext.getBean("bean.color.default", Color.class));
               MainFrameController.this.dispose();
               m_applicationEventPublisher.publishEvent(new ShowLoginFormEvent(this));
           }
        });
    }

    private void initializeBars()
    {
        initializeTopBar();
        initializeBottomBar();
        m_mainForm.getTabbedPaneMain().setFont(new Font("calibri", Font.PLAIN, 18));
        m_mainForm.getCheckBoxIncludeContainsStock().setFont(new Font("calibri", Font.PLAIN, 10));
        m_mainForm.getCheckBoxIncludeContainsProduct().setFont(new Font("calibri", Font.PLAIN, 10));
        m_dialogHelper.setStableFonts(m_mainForm);
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
        Arrays.stream(jPanel.getComponents()).forEach(
                component -> component.addMouseListener(new MouseAdapter() {

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        jPanel.setBackground(Color.LIGHT_GRAY);
                    }
                    @Override
                    public void mouseExited(MouseEvent e) {
                        jPanel.setBackground((Color)m_applicationContext.getBean("bean.color.default"));
                    }
                }));

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
        m_tableInitializer.reInitStockOutTables();
        m_mainForm.getLabelCount().setText("%d".formatted(TableInitializer.criticalStockCount));
        m_tableInitializer.initializeStockMovementTables(StockMovementSearchType.ALL_RECORDS);
        m_tableInitializer.initializeProductListTable();
        m_tableInitializer.initializeActiveUsersTable();
    }

    private void initializeTables()
    {
        m_tableInitializer.setTables(m_mainForm.getTableStockOut(), m_mainForm.getTableLesserThanThreshold(),
                m_mainForm.getTableStockInput(), m_mainForm.getTableStockOutput(), m_mainForm.getTableProductList(),
                m_mainForm.getTableActiveUsers());
        m_tableInitializer.initializeTables();
        m_mainForm.getLabelCount().setText("%d".formatted(TableInitializer.criticalStockCount));
    }
    private void initializeBottomBar()
    {
        initializeBars(m_mainForm.getButtonNotification());
        initializeBars(m_mainForm.getButtonAddStock());
        initializeBars(m_mainForm.getButtonReleaseStock());

        m_mainForm.getButtonNotification().addMouseListener(buttonNotificationClickedCallback());
        m_mainForm.getIconNotification().addMouseListener(buttonNotificationClickedCallback());
        m_mainForm.getLabelCount().addMouseListener(buttonNotificationClickedCallback());
        m_mainForm.getButtonAddStock().addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                m_dialogHelper.showAdditionFastStockDialog();
            }
        });
        m_mainForm.getButtonReleaseStock().addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                m_dialogHelper.showReleaseFastStockDialog();
            }
        });
        m_mainForm.getIconAdd().addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                m_dialogHelper.showAdditionFastStockDialog();
            }
        });
        m_mainForm.getIconRelease().addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                m_dialogHelper.showReleaseFastStockDialog();
            }
        });
    }
    private MouseAdapter buttonNotificationClickedCallback()
    {
        return new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    var notificationPopUp = m_applicationContext.getBean(
                            "bean.notification.critical.stock", CriticalStockNotificationPopUp.class);

                    if (!notificationPopUp.isNotificationPopUpActive()) {
                        notificationPopUp.getLabelMessage().setText(notificationPopUp.getLabelMessage()
                                .getText().formatted(TableInitializer.criticalStockCount));
                        notificationPopUp.setLocation((int) e.getLocationOnScreen().getX(),
                                (int) (e.getLocationOnScreen().getY() - notificationPopUp.getHeight()));
                        notificationPopUp.setVisible(true);
                        notificationPopUp.setNotificationPopUpActive(true);
                    }
                    else {
                        notificationPopUp.setVisible(false);
                        notificationPopUp.setNotificationPopUpActive(false);
                    }
                }
            }
        };
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

        m_mainForm.getButtonGetAllStockMovementRecords().addActionListener(e ->
                m_tableInitializer.initializeStockMovementTables(StockMovementSearchType.ALL_RECORDS));

        m_mainForm.getButtonClearFields().addActionListener(e -> {
            m_mainForm.getTextFieldUserName().setText(EMPTY_STRING);
            m_mainForm.getTextFieldOriginalCode().setText(EMPTY_STRING);
            m_mainForm.getTextFieldStockCode().setText(EMPTY_STRING);
            m_mainForm.getButtonStartDate().setText(EMPTY_STRING);
            m_mainForm.getButtonEndDate().setText(EMPTY_STRING);
            m_mainForm.getCheckBoxByUser().setSelected(false);
            m_mainForm.getCheckBoxByDate().setSelected(false);
            m_mainForm.getCheckBoxByProduct().setSelected(false);
        });

        m_mainForm.getButtonPrintStockInputList().addActionListener(e -> printTableCallback(m_mainForm.getTableStockInput()));
        m_mainForm.getButtonPrintStockOutPutList().addActionListener(e -> printTableCallback(m_mainForm.getTableStockOutput()));
    }
    private void printTableCallback(JTable jTable)
    {
        try {
            jTable.print();
        } catch (PrinterException ex) {
            log.error("MainFrameController::printCallback : {}", ex.getMessage());
            m_dialogHelper.showUnknownErrorMessageDialog(ex.getMessage());
        }
    }
    private void buttonSearchProductAndUserSelected()
    {
        var originalCode = m_mainForm.getTextFieldOriginalCode().getText().trim();
        var stockCode = m_mainForm.getTextFieldStockCode().getText().trim();
        var userName = m_mainForm.getTextFieldUserName().getText().trim();
        if (originalCode.isEmpty() && stockCode.isEmpty() || userName.isEmpty()) {
            m_dialogHelper.showEmptyFieldsWarningDialog();
            return;
        }

        if (originalCode.isEmpty())
            StockMovementSearchType.setProductId(
                    m_applicationService.findProductByStockCode(stockCode)
                            .map(ProductDTO::getOriginalCode)
                            .orElse(EMPTY_STRING));
        else
            StockMovementSearchType.setProductId(originalCode);

        StockMovementSearchType.setUserName(userName);
        m_tableInitializer.initializeStockMovementTables(StockMovementSearchType.PRODUCT_AND_USER);
    }
    private void buttonSearchProductSelected()
    {
        var originalCode = m_mainForm.getTextFieldOriginalCode().getText().trim();
        var stockCode = m_mainForm.getTextFieldStockCode().getText().trim();
        if (originalCode.isEmpty() && stockCode.isEmpty()) {
            m_dialogHelper.showEmptyFieldsWarningDialog();
            return;
        }

        if (originalCode.isEmpty())
            StockMovementSearchType.setProductId(m_applicationService
                    .findProductByStockCode(stockCode)
                    .map(ProductDTO::getOriginalCode)
                    .orElse(EMPTY_STRING));
        else
            StockMovementSearchType.setProductId(originalCode);

        m_tableInitializer.initializeStockMovementTables(StockMovementSearchType.PRODUCT);
    }
    private void buttonSearchUserSelected()
    {
        var userName = m_mainForm.getTextFieldUserName().getText().trim();
        if (userName.isEmpty()) {
            m_dialogHelper.showEmptyFieldsWarningDialog();
            return;
        }
        StockMovementSearchType.setUserName(userName);
        m_tableInitializer.initializeStockMovementTables(StockMovementSearchType.USER);
    }
    private void buttonSearchDateSelected()
    {
        var startDate = m_mainForm.getButtonStartDate().getDate();
        var endDate = m_mainForm.getButtonEndDate().getDate();
        if (startDate == null || endDate == null) {
            m_dialogHelper.showEmptyFieldsWarningDialog();
            return;
        }

        StockMovementSearchType.setStartDate(startDate);
        StockMovementSearchType.setEndDate(endDate);
        m_tableInitializer.initializeStockMovementTables(StockMovementSearchType.DATE);
    }
    private void buttonSearchDateAndUserSelected()
    {
        var userName = m_mainForm.getTextFieldUserName().getText().trim();
        var startDate = m_mainForm.getButtonStartDate().getDate();
        var endDate = m_mainForm.getButtonEndDate().getDate();

        if (userName.isEmpty() || startDate == null|| endDate == null) {
            m_dialogHelper.showEmptyFieldsWarningDialog();
            return;
        }

        StockMovementSearchType.setStartDate(startDate);
        StockMovementSearchType.setEndDate(endDate);
        StockMovementSearchType.setUserName(userName);
        m_tableInitializer.initializeStockMovementTables(StockMovementSearchType.USER_AND_DATE);
    }
    private void buttonSearchProductAndDateSelected()
    {
        var originalCode = m_mainForm.getTextFieldOriginalCode().getText().trim();
        var stockCode = m_mainForm.getTextFieldStockCode().getText().trim();
        var startDate = m_mainForm.getButtonStartDate().getDate();
        var endDate = m_mainForm.getButtonEndDate().getDate();

        if (originalCode.isEmpty() && stockCode.isEmpty() || startDate == null || endDate == null) {
            m_dialogHelper.showEmptyFieldsWarningDialog();
            return;
        }

        if (originalCode.isEmpty())
            StockMovementSearchType.setProductId(
                    m_applicationService.findProductByStockCode(stockCode)
                            .map(ProductDTO::getOriginalCode)
                            .orElse(EMPTY_STRING));
        else
            StockMovementSearchType.setProductId(m_mainForm.getTextFieldOriginalCode().getText());

        StockMovementSearchType.setStartDate(startDate);
        StockMovementSearchType.setEndDate(endDate);
        m_tableInitializer.initializeStockMovementTables(StockMovementSearchType.PRODUCT_AND_DATE);
    }
    private void buttonSearchAllCheckBoxesSelected()
    {
        var originalCode = m_mainForm.getTextFieldOriginalCode().getText().trim();
        var stockCode = m_mainForm.getTextFieldStockCode().getText().trim();
        var username = m_mainForm.getTextFieldUserName().getText().trim();
        var startDate = m_mainForm.getButtonStartDate().getDate();
        var endDate = m_mainForm.getButtonEndDate().getDate();

        if (originalCode.isEmpty() && stockCode.isEmpty() || username.isEmpty() || startDate == null || endDate == null) {
            m_dialogHelper.showEmptyFieldsWarningDialog();
            return;
        }

        if (originalCode.isEmpty())
            StockMovementSearchType.setProductId(
                    m_applicationService.findProductByStockCode(stockCode)
                            .map(ProductDTO::getOriginalCode)
                            .orElse(EMPTY_STRING));
        else
            StockMovementSearchType.setProductId(originalCode);

        StockMovementSearchType.setUserName(username);
        StockMovementSearchType.setStartDate(startDate);
        StockMovementSearchType.setEndDate(endDate);
        m_tableInitializer.initializeStockMovementTables(StockMovementSearchType.ALL);
    }
    private void initializeCheckBoxes()
    {
        m_mainForm.getCheckBoxByDate().addActionListener(e -> {
            if (m_mainForm.getCheckBoxByDate().isSelected()) {
                m_mainForm.getButtonStartDate().setEnabled(true);
                m_mainForm.getButtonEndDate().setEnabled(true);
            }
            else {
                m_mainForm.getButtonStartDate().setEnabled(false);
                m_mainForm.getButtonEndDate().setEnabled(false);
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
    private void initializeProductListTab()
    {
        initializeProductListTabButtons();
    }
    private void collectTextsCallback(JPanel panel)
    {
        for (var component : panel.getComponents())
            if (component instanceof DatePicker datePicker) {
                if (datePicker.getDate() != null)
                    m_componentMap.put(datePicker, datePicker.getDate().toString());
            }
            else if (component instanceof JTextField jTextField) {
                if (!jTextField.getText().isBlank())
                    m_componentMap.put(jTextField, jTextField.getText());
            }
            else if (component instanceof JPanel jPanel)
                collectTextsCallback(jPanel);

    }
    private void initializeProductListTabButtons()
    {
        m_mainForm.getButtonFilter().addActionListener(e -> m_threadPool.execute(() -> {
                        m_componentMap.clear();
                        collectTextsCallback(m_mainForm.getPanelFields());
                        m_tableInitializer.doFilterForTableProducts(collectData());
                }));

        m_mainForm.getButtonPrintList().addActionListener(e -> printTableCallback(m_mainForm.getTableProductList()));

        m_mainForm.getButtonClear().addActionListener(e -> {
            m_mainForm.getTextFieldStockEquals().setText(EMPTY_STRING);
            m_mainForm.getTextFieldStockGreater().setText(EMPTY_STRING);
            m_mainForm.getTextFieldStockLesser().setText(EMPTY_STRING);
            m_mainForm.getTextFieldPaneProductName().setText(EMPTY_STRING);
            m_mainForm.getTextFieldPaneProductOriginalCode().setText(EMPTY_STRING);
            m_mainForm.getTextFieldPaneStockCode().setText(EMPTY_STRING);
            m_mainForm.getTextFieldPaneShelfNumber().setText(EMPTY_STRING);
            m_mainForm.getDateRecordDateAfter().setText(EMPTY_STRING);
            m_mainForm.getDateRecordDateBefore().setText(EMPTY_STRING);
            m_mainForm.getDateRecordDateEquals().setText(EMPTY_STRING);
            m_mainForm.getTextFieldPaneProductOriginalCode().requestFocus();
        });

        m_mainForm.getButtonAddProduct().addActionListener(e -> m_dialogHelper.showProductRegisterDialog());
        m_mainForm.getButtonDeleteProduct().addActionListener(e -> {
            if (m_dialogHelper.getSelectedProduct() == null) {
                m_dialogHelper.showNoSelectedProductMessage();
                return;
            }
            if (m_dialogHelper.showEnsureDeleteItemWarningDialog() == JOptionPane.YES_OPTION)
                m_applicationService.deleteProduct(m_dialogHelper.getSelectedProduct());
        });
        m_mainForm.getButtonFastStockAdd().addActionListener(e -> {
            if (m_dialogHelper.getSelectedProduct() == null) {
                m_dialogHelper.showNoSelectedProductMessage();
                return;
            }
            m_dialogHelper.showAdditionFastStockDialogWithProduct();
        });
        m_mainForm.getButtonFastReleaseStock().addActionListener(e -> {
            if (m_dialogHelper.getSelectedProduct() == null) {
                m_dialogHelper.showNoSelectedProductMessage();
                return;
            }
            m_dialogHelper.showReleaseFastStockDialogWithProduct();
        });

        m_mainForm.getButtonGetAllProducts().addActionListener(e -> m_tableInitializer.initializeProductListTable());
    }
    private List<ProductDTO> collectData()
    {
        try {
            return checkFilters(m_applicationService.findAllProducts());

        } catch (ExecutionException | InterruptedException ex) {
            log.error("Error while collecting data in mainFrame : {}", ex.getMessage());
            return null;
        }
    }

    private List<ProductDTO> checkFilters(List<ProductDTO> productList)
    {
        var list = productList;

        if (m_componentMap.containsKey(m_mainForm.getTextFieldPaneProductOriginalCode()))
            list = m_mainForm.getCheckBoxIncludeContainsProduct().isSelected() ?
                    list.stream().filter(dto -> dto.getOriginalCode()
                            .contains(m_componentMap.get(m_mainForm.getTextFieldPaneProductOriginalCode()))).toList() :
                    list.stream().filter(dto -> dto.getOriginalCode()
                    .equals(m_componentMap.get(m_mainForm.getTextFieldPaneProductOriginalCode()))).toList();

        if (m_componentMap.containsKey(m_mainForm.getTextFieldPaneProductName()))
            list = m_mainForm.getCheckBoxIncludeContainsProduct().isSelected() ?
                    list.stream().filter(dto -> dto.getProductName()
                            .contains(m_componentMap.get(m_mainForm.getTextFieldPaneProductName()))).toList() :
                    list.stream().filter(dto -> dto.getProductName()
                    .equals(m_componentMap.get(m_mainForm.getTextFieldPaneProductName()))).toList();

        if (m_componentMap.containsKey(m_mainForm.getTextFieldPaneStockCode()))
            list = m_mainForm.getCheckBoxIncludeContainsStock().isSelected() ?
                    list.stream().filter(dto -> dto.getStockCode()
                            .contains(m_componentMap.get(m_mainForm.getTextFieldPaneStockCode()))).toList() :
                    list.stream().filter(dto -> dto.getStockCode()
                            .equals(m_componentMap.get(m_mainForm.getTextFieldPaneStockCode()))).toList();

        if (m_componentMap.containsKey(m_mainForm.getTextFieldPaneShelfNumber()))
            list = m_mainForm.getCheckBoxIncludeContainsStock().isSelected() ?
                    list.stream().filter(dto -> dto.getStock().getShelfNumber()
                            .contains(m_componentMap.get(m_mainForm.getTextFieldPaneShelfNumber()).trim())).toList() :
                    list.stream().filter(dto -> dto.getStock().getShelfNumber()
                            .equals(m_componentMap.get(m_mainForm.getTextFieldPaneShelfNumber()))).toList();


        if (m_componentMap.containsKey(m_mainForm.getDateRecordDateAfter()))
            list = list.stream()
                    .filter(dto -> dto.getRegisterDate().isAfter(
                            ChronoLocalDate.from(m_mainForm.getDateRecordDateAfter().getDate()))).toList();

        if (m_componentMap.containsKey(m_mainForm.getDateRecordDateBefore()))
            list = list.stream()
                    .filter(dto -> dto.getRegisterDate().isBefore(
                            ChronoLocalDate.from(m_mainForm.getDateRecordDateBefore().getDate()))).toList();

        if (m_componentMap.containsKey(m_mainForm.getDateRecordDateEquals()))
            list = list.stream()
                    .filter(dto -> dto.getRegisterDate().isEqual(
                            ChronoLocalDate.from(m_mainForm.getDateRecordDateEquals().getDate()))).toList();

        if (m_componentMap.containsKey(m_mainForm.getTextFieldStockEquals())) {
            try {
                var value = Integer.parseInt(m_mainForm.getTextFieldStockEquals().getText());
                list = list.stream().filter(dto -> dto.getStock().getAmount() == value).toList();
            } catch (NumberFormatException ignore) {
                log.error("Bad Format Stock Amount Equals");
            }
        }

        if (m_componentMap.containsKey(m_mainForm.getTextFieldStockGreater())) {
            try {
                var value = Integer.parseInt(m_mainForm.getTextFieldStockGreater().getText());
                list = list.stream().filter(dto -> dto.getStock().getAmount() > value).toList();
            } catch (NumberFormatException ignore) {
                log.error("Bad Format Stock Amount Greater");
            }
        }

        if (m_componentMap.containsKey(m_mainForm.getTextFieldStockLesser())) {
            try {
                var value = Integer.parseInt(m_mainForm.getTextFieldStockLesser().getText());
                list = list.stream().filter(dto -> dto.getStock().getAmount() < value).toList();
            } catch (NumberFormatException ignore) {
                log.error("Bad Format Stock Amount Lesser");
            }
        }

        if (m_componentMap.isEmpty()) {
            m_dialogHelper.showEmptyFieldsWarningDialog();
            return null;
        }

        return list;
    }
    private void initializeUserOperationsTab()
    {
        m_dialogHelper.disableTextAreaGrowthBehaviour(m_mainForm.getTextAreaEditUserDescription());
        m_dialogHelper.disableTextAreaGrowthBehaviour(m_mainForm.getTextAreaNewUserDescription());
        m_mainForm.getComboBoxUserRole().addItem(UserType.ADMIN);
        m_mainForm.getComboBoxUserRole().addItem(UserType.USER);

        var currentUser = m_currentUserConfig.getUser();

        if (!currentUser.isAdmin()) {
            m_dialogHelper.disableComponents(m_mainForm.getPanelSaveNewUser());
            m_dialogHelper.disableComponents(m_mainForm.getPanelActiveUsers());
            m_mainForm.getTextAreaNewUserDescription().setEnabled(false);
            m_mainForm.getTextAreaNewUserDescription().setEditable(false);
            m_mainForm.getTableActiveUsers().setEnabled(false);
        }
        else {
            m_dialogHelper.enableComponents(m_mainForm.getPanelSaveNewUser());
            m_dialogHelper.enableComponents(m_mainForm.getPanelActiveUsers());
            m_mainForm.getTextAreaNewUserDescription().setEnabled(true);
            m_mainForm.getTextAreaNewUserDescription().setEditable(true);
            m_mainForm.getTableActiveUsers().setEnabled(true);
            m_mainForm.getIconNewUserHideOrShowPassword().addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    m_dialogHelper.setPasswordFieldVisibleOrInvisibleCallback(
                            m_mainForm.getPasswordFieldNewUserPassword(), m_mainForm.getIconNewUserHideOrShowPassword()
                    );
                }
            });
        }
        if (m_mainForm.getIconEditUserNewPassword().getMouseListeners().length == 0)
            m_mainForm.getIconEditUserNewPassword().addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    m_dialogHelper.setPasswordFieldVisibleOrInvisibleCallback(
                            m_mainForm.getPasswordFieldEditUserNewPassword(), m_mainForm.getIconEditUserNewPassword()
                    );
                }
            });
        if (m_mainForm.getIconEditUserOldPassword().getMouseListeners().length == 0)
            m_mainForm.getIconEditUserOldPassword().addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    m_dialogHelper.setPasswordFieldVisibleOrInvisibleCallback(
                            m_mainForm.getPasswordFieldEditUserOldPassword(), m_mainForm.getIconEditUserOldPassword()
                    );
                }
            });
        m_mainForm.getTextFieldEditUserUserName().setText(currentUser.getUsername());
        m_mainForm.getTextFieldEditUserFirstName().setText(currentUser.getFirstName());
        m_mainForm.getTextFieldEditUserLastName().setText(currentUser.getLastName());
        m_mainForm.getTextFieldEditUserEMail().setText(currentUser.getEMail());
        m_mainForm.getTextAreaEditUserDescription().setText(currentUser.getDescription());
        m_mainForm.getButtonNewUserSave().addActionListener(this::saveUserClickedCallback);
        m_mainForm.getButtonEditUserSave().addActionListener(this::editUserClickedCallback);
        m_mainForm.getButtonDeleteUser().addActionListener(e -> m_dialogHelper.deleteUser());
        m_mainForm.getButtonEditUser().addActionListener(e -> m_dialogHelper.showNewEditUserDialog());

    }
    private void editUserClickedCallback(ActionEvent actionEvent)
    {
        var currentUser = m_currentUserConfig.getUser();
        if (!m_applicationService.isPasswordConfirmed(String.valueOf(m_mainForm.getPasswordFieldEditUserOldPassword().getPassword()))) {
            m_dialogHelper.showConfirmPasswordNotEqualsWarningDialog();
            return;
        }

        var firstName = m_mainForm.getTextFieldEditUserFirstName().getText().trim();
        var lastName = m_mainForm.getTextFieldEditUserLastName().getText().trim();
        var eMail = m_mainForm.getTextFieldEditUserEMail().getText().trim();
        var description = m_mainForm.getTextAreaEditUserDescription().getText().trim();
        var password = String.valueOf(m_mainForm.getPasswordFieldEditUserNewPassword().getPassword());

        if (m_dialogHelper.isThereAnyFieldEmpty(firstName, lastName, eMail, password)) {
            m_dialogHelper.showEmptyUserFieldsWarningDialog();
            return;
        }

        if (m_dialogHelper.isInvalidEMail(eMail)) {
            m_dialogHelper.showUnSupportedFormatMessage("%s".formatted(eMail));
            return;
        }

        if (password.length() < 8) {
            m_dialogHelper.showShortLengthPasswordWarningDialog();
            return;
        }

        currentUser.setFirstName(firstName);
        currentUser.setLastName(lastName);
        currentUser.setEMail(eMail);
        currentUser.setPassword(password);
        currentUser.setDescription(description);
        try {
            m_currentUserConfig.setUser(m_applicationService.updateUser(currentUser));
            m_dialogHelper.showSaveUserProcessSuccessInfoDialog();
        }
        catch (EmailAlreadyInUseException | UsernameAlreadyInUseException ex) {
            m_dialogHelper.showCustomErrorMessageDialog(ex.getMessage());
        }
        catch (ServiceException ex) {
            log.error("Error while updating current user : {}", ex.getMessage());
            m_dialogHelper.showUnknownErrorMessageDialog(ex.getMessage());
        }
    }
    private void saveUserClickedCallback(ActionEvent actionEvent)
    {
        var username = m_mainForm.getTextFieldNewUserUsername().getText().trim();
        var firstName = m_mainForm.getTextFieldNewUserFirstName().getText().trim();
        var lastName = m_mainForm.getTextFieldNewUserLastName().getText().trim();
        var eMail = m_mainForm.getTextFieldNewUserEMail().getText().trim();
        var description = m_mainForm.getTextAreaNewUserDescription().getText().trim();
        var password = String.valueOf(m_mainForm.getPasswordFieldNewUserPassword().getPassword());
        var role = m_mainForm.getComboBoxUserRole().getSelectedItem();

        if (m_dialogHelper.isThereAnyFieldEmpty(username, firstName, lastName, eMail, password) || role == null) {
            m_dialogHelper.showEmptyUserFieldsWarningDialog();
            return;
        }

        if (m_dialogHelper.isInvalidEMail(eMail)) {
            m_dialogHelper.showUnSupportedFormatMessage("%s".formatted(eMail));
            return;
        }

        if (password.length() < 8) {
            m_dialogHelper.showShortLengthPasswordWarningDialog();
            return;
        }

        var userDTO = new UserDTO();
        userDTO.setUsername(username);
        userDTO.setFirstName(firstName);
        userDTO.setLastName(lastName);
        userDTO.setEMail(eMail);
        userDTO.setPassword(password);
        userDTO.setDescription(description);
        userDTO.setAdmin(role == UserType.ADMIN);
        try {
            m_applicationService.saveUser(userDTO);
            m_applicationEventPublisher.publishEvent(new UpdateTablesEvent(this));
            m_dialogHelper.showSaveUserProcessSuccessInfoDialog();
        }
        catch (UsernameAlreadyInUseException | EmailAlreadyInUseException ex) {
            m_dialogHelper.showCustomErrorMessageDialog(ex.getMessage());
        }
        catch (ServiceException ex) {
            log.error("Error while saving new user :{}", ex.getMessage());
            m_dialogHelper.showUnknownErrorMessageDialog(ex.getMessage());
        }
    }
}
