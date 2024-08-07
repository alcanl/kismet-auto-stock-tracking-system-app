package com.alcanl.app.application.ui.view.form;

import com.github.lgooddatepicker.components.DatePicker;

import javax.swing.*;

public class SideBar {
    private JTabbedPane tabbedPaneMain;
    private JPanel panelStockMoves;
    private JTable tableStockInput;
    private JTable tableStockOutput;
    private JCheckBox checkBoxByDate;
    private DatePicker buttonStartDate;
    private DatePicker buttonEndDate;
    private JCheckBox checkBoxByUser;
    private JTextField textFieldUserName;
    private JTextField textFieldOriginalCode;
    private JTextField textFieldStockCode;
    private JCheckBox checkBoxByProduct;
    private JButton buttonSearch;
    private JPanel panelProducts;
    private JScrollPane scrollPaneProductList;
    private JPanel panelUserOperations;
    private JTable tableUserOperations;
    private JPanel panelStockOperations;
    private JButton buttonClearFields;
    private JButton buttonGetAllStockMovementRecords;
    private JTable tableProductList;
    private JTextField textFieldPaneProductOriginalCode;
    private JTextField textFieldPaneProductName;
    private JTextField textFieldPaneStockCode;
    private JTextField textFieldPaneShelfNumber;
    private JTextField textFieldStockEquals;
    private JTextField textFieldStockGreater;
    private JTextField textFieldStockLesser;
    private JButton buttonGetAllProducts;
    private JTextField dateRecordDateEquals;
    private JTextField dateRecordDateAfter;
    private JTextField dateRecordDateBefore;
    private JButton filtreleButton;
    private JButton temizleButton;
    private JButton buttonAddProduct;
    private JButton buttonDeleteProduct;
    private JButton buttonFastStockAdd;
    private JButton buttonFastReleaseStock;
}
