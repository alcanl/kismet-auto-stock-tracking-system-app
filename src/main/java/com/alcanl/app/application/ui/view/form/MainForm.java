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
    private JPanel panelTopBar;
    private JPanel buttonExit;
    private JLabel iconExit;
    private JPanel buttonMaximize;
    private JLabel iconMaximize;
    private JPanel buttonMinimize;
    private JLabel iconMinimize;
    private JPanel buttonNew;
    private JLabel labelNew;
    private JPanel buttonEdit;
    private JLabel labelEdit;
    private JPanel buttonSettings;
    private JLabel labelSettings;
    private JPanel buttonLogout;
    private JLabel labelLogout;
    private JPanel panelLogo;
    private JLabel iconLogo;
    private JPanel panelBottomBar;
    private JLabel labelVersion;
    private JLabel labelWelcomeUser;
    private JPanel buttonReleaseStock;
    private JLabel iconRelease;
    private JPanel buttonRightBar;
    private JLabel labelCount;
    private JLabel iconBar;
    private JPanel buttonAddStock;
    private JLabel iconAdd;
    private JScrollPane paneMain;
    private JPanel panelStockState;
    private JTable tableStockOut;
    private JTable tableLesserThanThreshold;
    private JScrollPane paneMid;
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
    private JPanel panelFields;
    private JTextField textFieldPaneProductOriginalCode;
    private JTextField textFieldPaneProductName;
    private JCheckBox checkBoxIncludeContainsProduct;
    private JTextField textFieldPaneStockCode;
    private JTextField textFieldPaneShelfNumber;
    private JCheckBox checkBoxIncludeContainsStock;
    private JTextField textFieldStockEquals;
    private JTextField textFieldStockGreater;
    private JTextField textFieldStockLesser;
    private DatePicker dateRecordDateEquals;
    private DatePicker dateRecordDateAfter;
    private DatePicker dateRecordDateBefore;
    private JButton buttonFilter;
    private JButton buttonClear;
    private JPanel panelUserOperations;
    private JTable tableUserOperations;

    @PostConstruct
    private void postConstruct()
    {
        dispose();
    }
}
