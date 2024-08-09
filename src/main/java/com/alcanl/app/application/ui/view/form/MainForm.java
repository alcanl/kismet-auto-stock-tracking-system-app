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
    private JPanel buttonNotification;
    private JLabel labelCount;
    private JLabel iconNotification;
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
    private JScrollPane paneQueryTables;
    private JPanel panelQueryTables;
    private JPanel panelStockInput;
    private JTable tableStockInput;
    private JPanel panelStockOutput;
    private JTable tableStockOutput;
    private JButton buttonGetAllStockMovementRecords;
    private JPanel panelStockMoveQuery;
    private JPanel panelByDate;
    private JCheckBox checkBoxByDate;
    private JLabel labelStartDate;
    private DatePicker buttonStartDate;
    private JLabel labelEndDate;
    private DatePicker buttonEndDate;
    private JPanel panelByUser;
    private JCheckBox checkBoxByUser;
    private JLabel labelUsername;
    private JTextField textFieldUserName;
    private JPanel panelByProduct;
    private JLabel labelStockMovesProductOriginalCode;
    private JLabel labelStockMovesStockCode;
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
    private JPanel panelProductSearch;
    private JPanel panelByProductRecords;
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
    private JPanel panelUserAddOrEdit;
    private JPanel panelSaveNewUser;
    private JTextField textFieldNewUserUsername;
    private JTextField textFieldNewUserEMail;
    private JTextField textFieldNewUserFirstName;
    private JTextField textFieldNewUserLastName;
    private JPasswordField passwordFieldNewUserPassword;
    private JTextArea textAreaNewUserDescription;
    private JComboBox<String> comboBoxUserRole;
    private JButton buttonNewUserSave;
    private JLabel iconNewUserHideOrShowPassword;
    private JPanel panelEditUser;
    private JTextField textFieldEditUserUserName;
    private JTextField textFieldEditUserEMail;
    private JTextField textFieldEditUserFirstName;
    private JTextField textFieldEditUserLastName;
    private JPasswordField passwordFieldEditUserOldPassword;
    private JLabel iconEditUserOldPassword;
    private JTextArea textAreEditUserDescription;
    private JButton buttonEditUserSave;
    private JPasswordField passwordFieldEditUserNewPassword;
    private JLabel iconEditUserNewPassword;
    private JScrollPane paneUsersTable;
    private JTable tableActiveUsers;
    private JButton buttonEditUser;
    private JButton buttonDeleteUser;

    @PostConstruct
    private void postConstruct()
    {
        dispose();
    }
}
