package com.alcanl.app.application.ui.view.form;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.stereotype.Component;

import javax.swing.*;

@SwingContainer
@Component("bean.form.main")
@Getter
public class MainForm extends JFrame {
    private JPanel panelMain;
    private JPanel panelBottomBar;
    private JLabel labelVersion;
    private JLabel labelWelcomeUser;
    private JPanel panelTopBar;
    private JPanel buttonExit;
    private JPanel buttonMaximize;
    private JPanel buttonMinimize;
    private JLabel labelNew;
    private JPanel buttonNew;
    private JPanel buttonEdit;
    private JLabel labelEdit;
    private JLabel labelSettings;
    private JPanel buttonSettings;
    private JLabel labelLogout;
    private JPanel buttonLogout;
    private JLabel iconLogo;
    private JPanel panelLogo;
    private JLabel iconExit;
    private JLabel iconMaximize;
    private JLabel iconMinimize;
    private JScrollPane paneMid;
    private JPanel panelContainer;
    private JPanel panelStockState;
    private JPanel panelLesserThan;
    private JPanel panelStockOut;
    private JTable tableLesserThanThreshold;
    private JScrollPane scrollPaneStockOut;
    private JScrollPane scrollPaneLesserThan;
    private JPanel panelRight;
    private JPanel buttonRightBar;
    private JLabel labelCount;
    private JLabel iconBar;
    private JPanel buttonAddStock;
    private JLabel iconAdd;
    private JPanel buttonReleaseStock;
    private JLabel iconRelease;
    private JPanel panelMainContainer;
    private JTable tableStockOut;
    private JTabbedPane tabbedPaneMain;
    private JPanel panelProducts;
    private JPanel panelStockMoves;
    private JPanel panelUserOperations;
    private JPanel panelStockOperations;
    private JTable table1;
    private JTable table2;
    private JTextField textField4;
    private JTextField textField5;
    private JTextField textField6;
    private JTextField textField1;
    private JButton listeleButton;
    private JButton başlangıçButton;
    private JButton bitişButton;
    private JLabel labelDateBegin;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField7;

    @PostConstruct
    private void postConstruct()
    {
        dispose();
    }
}
