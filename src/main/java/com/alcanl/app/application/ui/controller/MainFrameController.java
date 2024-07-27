package com.alcanl.app.application.ui.controller;

import com.alcanl.app.application.ui.event.DisposeEvent;
import com.alcanl.app.application.ui.event.ShowFormEvent;
import com.alcanl.app.application.ui.view.dialog.DialogAddNewProduct;
import com.alcanl.app.application.ui.view.form.MainForm;
import com.alcanl.app.configuration.CurrentUserConfig;
import com.alcanl.app.helper.Resources;
import com.alcanl.app.service.ApplicationService;
import com.alcanl.app.service.dto.ProductDTO;
import com.formdev.flatlaf.FlatClientProperties;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

@Slf4j
@Controller
@DependsOn({"bean.form.login","bean.form.main","bean.dialog.add.new.product"})
public class MainFrameController extends JFrame {

    @Value("${kismet.auto.stock.tracking.system.app.frame.main.dimension.x}")
    private int m_mainFrameStartDimensionX;
    @Value("${kismet.auto.stock.tracking.system.app.frame.main.dimension.y}")
    private int m_mainFrameStartDimensionY;
    private DialogAddNewProduct dialogAddNewProduct;
    private final DefaultTableModel m_defaultTableModel;
    private final MainForm m_mainForm;
    private final Resources m_resources;
    private final ExecutorService m_threadPool;
    private final CurrentUserConfig m_currentUserConfig;
    private final ApplicationService m_applicationService;
    private final ApplicationContext m_applicationContext;
    private final ApplicationEventPublisher m_applicationEventPublisher;
    private static final String TABLE_STOCK_OUT_PRODUCT_NAME = "ÜRÜN ADI";
    private static final String TABLE_STOCK_OUT_PRODUCT_ORIGINAL_CODE = "ÜRÜN KODU";
    private static final String TABLE_STOCK_OUT_STOCK = "STOK";

    public MainFrameController(Resources resources, ExecutorService threadPool,
                               ApplicationService applicationService, ApplicationContext applicationContext, MainForm mainForm,
                               CurrentUserConfig currentUserConfig, ApplicationEventPublisher applicationEventPublisher,
                               DefaultTableModel defaultTableModel)
    {
        m_mainForm = mainForm;
        m_resources = resources;
        m_threadPool = threadPool;
        m_applicationService = applicationService;
        m_applicationContext = applicationContext;
        m_currentUserConfig = currentUserConfig;
        m_applicationEventPublisher = applicationEventPublisher;
        m_defaultTableModel = defaultTableModel;
    }

    @PostConstruct
    private void setFrameSize()
    {
        initializeFrame();
        initializeTables();
        initializeBarButtonsHover();
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
    public void onTableEventReceived()
    {

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

    private void initializeLeftTopBarButtonNew()
    {
        setOnPanelButtonClickListener(m_mainForm.getButtonNew(), null);
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

    private void initializeBarButtonsHover()
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

                initializeBarButtonsHover(jpanel);
            }

        initializeExitButton();
        initializeMaximizeButton();
        initializeMinimizeButton();
        initializeLeftTopBarButtonNew();
        initializeTopBarClickListener();
        initializeButtonLogout();
    }

    private void initializeTopBarClickListener()
    {
        setOnPanelButtonClickListener(m_mainForm.getPanelTopBar(), e -> {
            if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2)
                maximizeButtonOnClickedCallback(e);
            });

    }

    private void initializeBarButtonsHover(JPanel jPanel)
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

    private void initializeTables()
    {
        initializeTableModel();
        m_mainForm.getTableStockOut().setModel(m_defaultTableModel);
        m_mainForm.getTableStockOut().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        m_mainForm.getTableStockOut().getTableHeader().setBackground(Color.WHITE);
        m_mainForm.getTableStockOut().getTableHeader().setReorderingAllowed(false);
        m_mainForm.getTableStockOut().getTableHeader().setResizingAllowed(false);
        m_mainForm.getTableStockOut().getTableHeader().setUpdateTableInRealTime(true);
        m_mainForm.getTableStockOut().getTableHeader().setFont(new Font("segoe ui semibold", Font.BOLD, 13));
        m_mainForm.getTableStockOut().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point point = e.getPoint();
                int currentRow = m_mainForm.getTableStockOut().rowAtPoint(point);
                m_mainForm.getTableStockOut().setRowSelectionInterval(currentRow, currentRow);

                if (SwingUtilities.isRightMouseButton(e) || SwingUtilities.isLeftMouseButton(e))
                {

                }

                if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2)
                {

                }
            }
        });

        try {
            fillTable(m_threadPool.submit(m_applicationService::getAllStockOutProducts).get());
        } catch (ExecutionException | InterruptedException ex) {
            log.error("MainFrameController::initializeTables: {}", ex.getMessage());
        }
    }

    private void fillTable(List<ProductDTO> list)
    {
        var testStock = 0;
        var testProduct = "testName";
        var testProductCode = "testCode";
        Object[] dataTest = {testProduct, testProductCode, testStock};
        m_defaultTableModel.addRow(dataTest);
        m_mainForm.getTableStockOut().setModel(m_defaultTableModel);
        list.forEach(this::fillTableCallback);
        m_resources.setCellsAlignment(m_mainForm.getTableStockOut(), SwingConstants.CENTER);

    }

    private void fillTableCallback(ProductDTO productDTO)
    {
        Object[] data = {productDTO.getProductName(), productDTO.getOriginalCode(), productDTO.getStock().amount};
        m_defaultTableModel.addRow(data);
        m_mainForm.getTableStockOut().setModel(m_defaultTableModel);
    }

    private void initializeTableModel()
    {
        Object[] tableHeaders = {TABLE_STOCK_OUT_PRODUCT_NAME, TABLE_STOCK_OUT_PRODUCT_ORIGINAL_CODE, TABLE_STOCK_OUT_STOCK};
        m_defaultTableModel.setColumnIdentifiers(tableHeaders);
    }

    private void initializeRightSideBar()
    {
        m_mainForm.getLabelCount().setText("%d".formatted(m_defaultTableModel.getRowCount()));
        initializeBarButtonsHover(m_mainForm.getButtonRightBar());
        initializeBarButtonsHover(m_mainForm.getButtonAddStock());
        initializeBarButtonsHover(m_mainForm.getButtonReleaseStock());

        m_mainForm.getButtonRightBar().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (m_mainForm.getPanelStockState().getWidth() > 0) {
                    m_mainForm.getPanelStockState().setSize(0, m_mainForm.getPanelStockState().getHeight());
                    m_mainForm.getIconBar().setIcon((Icon)m_applicationContext.getBean("bean.image.icon.right.bar.open"));
                }
                else {
                    m_mainForm.getPanelStockState().setSize(300, m_mainForm.getPanelStockState().getHeight());
                    m_mainForm.getIconBar().setIcon((Icon)m_applicationContext.getBean("bean.image.icon.right.bar.close"));
                }
            }
        });
        m_mainForm.getButtonAddStock().addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                dialogAddNewProduct = (DialogAddNewProduct)m_applicationContext.getBean("bean.dialog.add.new.product");
                dialogAddNewProduct.pack();
                dialogAddNewProduct.setLocationRelativeTo(null);
                dialogAddNewProduct.setVisible(true);
            }
        });
    }
}
