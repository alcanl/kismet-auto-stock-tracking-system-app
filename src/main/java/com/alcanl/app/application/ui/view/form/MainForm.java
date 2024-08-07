package com.alcanl.app.application.ui.view.form;

import com.github.lgooddatepicker.components.DatePicker;
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
    private JPanel panelStockOperations;
    private JPanel panelStockMoves;
    private JTable tableStockInput;
    private JTable tableStockOutput;
    private JButton buttonGetAllStockMovementRecords;
    private JCheckBox checkBoxByDate;
    private DatePicker buttonStartDate;
    private DatePicker buttonEndDate;
    private JCheckBox checkBoxByUser;
    private JTextField textFieldUserName;
    private JTextField textFieldOriginalCode;
    private JTextField textFieldStockCode;
    private JCheckBox checkBoxByProduct;
    private JButton buttonClearFields;
    private JButton buttonSearch;
    private JPanel panelProducts;
    private JScrollPane scrollPaneProductList;
    private JButton buttonGetAllProducts;
    private JTable tableProductList;
    private JButton buttonAddProduct;
    private JButton buttonDeleteProduct;
    private JButton buttonFastStockAdd;
    private JButton buttonFastReleaseStock;
    private JTextField textFieldPaneProductOriginalCode;
    private JTextField textFieldPaneProductName;
    private JTextField textFieldPaneStockCode;
    private JTextField textFieldPaneShelfNumber;
    private JTextField textFieldStockEquals;
    private JTextField textFieldStockGreater;
    private JTextField textFieldStockLesser;
    private JTextField dateRecordDateEquals;
    private JTextField dateRecordDateAfter;
    private JTextField dateRecordDateBefore;
    private JButton filtreleButton;
    private JButton temizleButton;
    private JPanel panelUserOperations;
    private JTable tableUserOperations;

    @PostConstruct
    private void postConstruct()
    {
        dispose();
    }
}
