package com.alcanl.app.helper.table;

import com.alcanl.app.helper.dialog.DialogHelper;
import com.alcanl.app.helper.popup.PopUpHelper;
import com.alcanl.app.helper.table.search.type.StockMovementSearchType;
import com.alcanl.app.repository.entity.type.StockMovementType;
import com.alcanl.app.service.ApplicationService;
import com.alcanl.app.service.dto.ProductDTO;
import com.alcanl.app.service.dto.StockMovementDTO;
import com.alcanl.app.service.dto.UserDTO;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.time.format.DateTimeFormatter;
import java.util.*;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

@Component
@Getter
@Slf4j
@RequiredArgsConstructor
public final class TableInitializer {

    private final List<Pair<JTable, DefaultTableModel>> m_tablePairsList;
    private final ApplicationContext m_applicationContext;
    private final ApplicationService m_applicationService;
    private final ExecutorService m_threadPool;
    private final PopUpHelper m_popUpHelper;
    private final DialogHelper m_dialogHelper;
    private final DateTimeFormatter m_dateTimeFormatter;

    private static final String TABLE_HEADER_PRODUCT_NAME = "ÜRÜN ADI";
    private static final String TABLE_HEADER_PRODUCT_ORIGINAL_CODE = "ÜRÜN KODU";
    private static final String TABLE_HEADER_STOCK = "STOK";
    private static final String TABLE_HEADER_PRODUCT_THRESHOLD = "EŞİK";
    private static final String TABLE_HEADER_PRODUCT_STOCK_CODE = "STOK KODU";
    private static final String TABLE_HEADER_PRODUCT_STOCK_SHELF_NUMBER = "RAF NUMARASI";
    private static final String TABLE_HEADER_PRODUCT_REGISTER_DATE = "ÜRÜN KAYIT TARİHİ";
    private static final String TABLE_HEADER_STOCK_REGISTER_DATE = "KAYIT TARİHİ";
    private static final String TABLE_HEADER_RECORD_OWNER = "İŞLEM SAHİBİ";
    private static final String TABLE_HEADER_RECORD_AMOUNT = "KAYIT MİKTARI";
    private static final String TABLE_HEADER_USER_USERNAME = "KULLANICI ADI";
    private static final String TABLE_HEADER_USER_FIRST_NAME = "ADI";
    private static final String TABLE_HEADER_USER_LAST_NAME = "SOYADI";
    private static final String TABLE_HEADER_USER_EMAIL = "EMAIL";
    private static final String TABLE_HEADER_USER_REGISTER_DATE = "KAYIT TARİHİ";
    private static final String TABLE_HEADER_USER_DESCRIPTION = "AÇIKLAMA";
    private static final String TABLE_HEADER_STOCK_MOVEMENT_TYPE = "İŞLEM TÜRÜ";

    public static int criticalStockCount = 0;

    public void setTableColumnAlignments()
    {
        m_tablePairsList.getFirst().getFirst().getColumnModel().getColumn(2).setPreferredWidth(1);
        m_tablePairsList.get(1).getFirst().getColumnModel().getColumn(2).setPreferredWidth(2);
        m_tablePairsList.get(1).getFirst().getColumnModel().getColumn(3).setPreferredWidth(2);
    }
    public void doFilterForTableProducts(List<ProductDTO> products)
    {
        if (products == null)
            return;

        initializeProductListTable(products);

        if (products.isEmpty())
            m_dialogHelper.showEmptyFilteredListWarningDialog();
    }
    public void setTables(JTable... tables)
    {
        Arrays.stream(tables).forEach(table -> m_tablePairsList.add(Pair.of(table,
                m_applicationContext.getBean("bean.table.model.default", DefaultTableModel.class))));
    }
    public void reInitStockOutTables()
    {
        criticalStockCount = 0;
        m_tablePairsList.getFirst().getSecond().getDataVector().clear();
        m_tablePairsList.get(1).getSecond().getDataVector().clear();
        initializeStockOutTables();
        m_tablePairsList.forEach(pair -> {
            pair.getFirst().setModel(pair.getSecond());
            setCellsAlignment(pair.getFirst());
        });
    }
    public void initializeTables()
    {
        initializeStockOutTables();
        initializeStockMovementTables(StockMovementSearchType.NONE);
        initializeProductListTable();
        initializeActiveUsersTable();
        initializeLastTwentyStockMovesTable();
        m_tablePairsList.forEach(this::initializeTablesCallback);
        m_tablePairsList.stream().limit(5).forEach(this::initializeStockTablesClickListeners);
        initializeActiveUserTableClickListeners();
        initializeLastTwentyStockMovesTableClickListeners();
        setTableColumnAlignments();
    }
    public void initializeActiveUsersTable()
    {
        try {
            m_tablePairsList.get(5).getSecond().getDataVector().clear();
            initializeUsersListTableModel(m_tablePairsList.get(5).getSecond());
            fillUsersListTable(m_applicationService.findAllUsers(), m_tablePairsList.get(5).getSecond());
            setCellsAlignment(m_tablePairsList.get(5).getFirst());
        } catch (ExecutionException | InterruptedException ex) {
            m_dialogHelper.showUnknownErrorMessageDialog(ex.getMessage());
        }

    }
    private void initializeActiveUserTableClickListeners()
    {
        m_tablePairsList.get(5).getFirst().setComponentPopupMenu((JPopupMenu) m_applicationContext.getBean("bean.table.users.popup.click.right"));
        m_tablePairsList.get(5).getFirst().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                try {
                    Point point = e.getPoint();
                    int currentRow = m_tablePairsList.get(5).getFirst().rowAtPoint(point);

                    if (SwingUtilities.isRightMouseButton(e) || SwingUtilities.isLeftMouseButton(e))
                        m_threadPool.execute(() -> tableUserItemClickedCallback(m_tablePairsList.get(5).getFirst(), currentRow));

                } catch (IllegalArgumentException ignore) {
                    m_tablePairsList.get(5).getFirst().clearSelection();
                }
            }
        });
        m_tablePairsList.get(5).getFirst().addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                var row = m_tablePairsList.get(5).getFirst().rowAtPoint(e.getPoint());
                var col = m_tablePairsList.get(5).getFirst().columnAtPoint(e.getPoint());

                m_tablePairsList.get(5).getFirst().setToolTipText(m_tablePairsList.get(5).getFirst().getValueAt(row, col).toString());
            }
        });
    }
    private void initializeLastTwentyStockMovesTableClickListeners()
    {
        m_tablePairsList.get(6).getFirst().addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                var row = m_tablePairsList.get(6).getFirst().rowAtPoint(e.getPoint());
                var col = m_tablePairsList.get(6).getFirst().columnAtPoint(e.getPoint());

                m_tablePairsList.get(6).getFirst().setToolTipText(m_tablePairsList.get(6).getFirst().getValueAt(row, col).toString());
            }
        });
    }
    private void initializeStockOutTables()
    {
        try {
            initializeStockOutTables(m_tablePairsList.getFirst().getSecond(), m_tablePairsList.get(1).getSecond());
        } catch (ExecutionException | InterruptedException ex) {
            log.error("MainFrameController::initializeTables: {}", ex.getMessage());
        }

    }
    private void initializeStockOutTables(DefaultTableModel tableModelStockOut, DefaultTableModel tableModelLesserThan) throws ExecutionException, InterruptedException {
        initializeStockOutTableModel(tableModelStockOut);
        initializeStockLesserTableModel(tableModelLesserThan);
        fillStockOutTable(m_threadPool.submit(m_applicationService::getAllStockOutProducts).get(),
                tableModelStockOut);
        fillStockThresholdTable(m_threadPool.submit(m_applicationService::getAllLesserThanThresholdStockProducts).get(),
                tableModelLesserThan);
        criticalStockCount += tableModelStockOut.getRowCount() + tableModelLesserThan.getRowCount();
    }
    private void initializeProductListTable(List<ProductDTO> list)
    {
        m_tablePairsList.get(4).getSecond().getDataVector().clear();
        initializeProductListTableModel(m_tablePairsList.get(4).getSecond());
        fillProductListTable(list, m_tablePairsList.get(4).getSecond());
        setCellsAlignment(m_tablePairsList.get(4).getFirst());
    }
    public void initializeLastTwentyStockMovesTable()
    {
        try {
            m_tablePairsList.get(6).getSecond().getDataVector().clear();
            initializeLastTwentyStockMovesTableModel(m_tablePairsList.get(6).getSecond());
            fillLastTwentyStockMovesTable(m_applicationService.findLastStockMovementRecords(),
                    m_tablePairsList.get(6).getSecond());
            setCellsAlignment(m_tablePairsList.get(6).getFirst());
        } catch (ExecutionException | InterruptedException ex) {
            log.error("TableInitializer::initializeLastTwentyStockMovesTable: {}", ex.getMessage());
            m_dialogHelper.showUnknownErrorMessageDialog(ex.getMessage());
        }
    }
    public void initializeProductListTable()
    {
        try {
            initializeProductListTable(m_applicationService.findAllProducts());
        }
        catch (ExecutionException | InterruptedException ex) {
            log.error("TableInitializer::initializeProductListTable: {}", ex.getMessage());
            m_dialogHelper.showUnknownErrorMessageDialog(ex.getMessage());
        }
    }
    public void initializeStockMovementTables(StockMovementSearchType type)
    {
        m_tablePairsList.get(2).getSecond().getDataVector().clear();
        m_tablePairsList.get(3).getSecond().getDataVector().clear();
        initializeStockInputMovementsTableModel(m_tablePairsList.get(2).getSecond());
        initializeStockOutputMovementsTableModel(m_tablePairsList.get(3).getSecond());

        try {
            var list = switch (type) {
                case USER ->
                    m_applicationService.findAllStockMovementsByUser(StockMovementSearchType.getUserName());
                case PRODUCT -> m_applicationService.findAllStockMovementsByProduct(
                        StockMovementSearchType.getProductId());
                case DATE ->
                        m_applicationService.findAllStockMovementsByDateBetween(StockMovementSearchType.getStartDate(), StockMovementSearchType.getEndDate());
                case USER_AND_DATE ->
                        m_applicationService.findAllStockMovementsByUserAndDate(
                                StockMovementSearchType.getUserName(), StockMovementSearchType.getStartDate(), StockMovementSearchType.getEndDate());
                case PRODUCT_AND_DATE ->
                        m_applicationService.findAllStockMovementsByProductAndDate(
                            StockMovementSearchType.getStartDate(), StockMovementSearchType.getEndDate(), StockMovementSearchType.getProductId());
                case PRODUCT_AND_USER ->
                        m_applicationService.findAllStockMovementsByProductAndUser(
                            StockMovementSearchType.getProductId(), StockMovementSearchType.getUserName());
                case ALL ->
                        m_applicationService.findAllStockMovementsByAllCriteria(
                            StockMovementSearchType.getUserName(), StockMovementSearchType.getProductId(),
                            StockMovementSearchType.getStartDate(), StockMovementSearchType.getEndDate());
                case ALL_RECORDS -> m_applicationService.findAllStockMovements();

                default -> new ArrayList<StockMovementDTO>();
            };

            fillStockInputMovementsTable(list, m_tablePairsList.get(2).getSecond());
            fillStockOutputMovementsTable(list, m_tablePairsList.get(3).getSecond());
            setCellsAlignment(m_tablePairsList.get(2).getFirst());
            setCellsAlignment(m_tablePairsList.get(3).getFirst());
        } catch (InterruptedException | ExecutionException ex) {
            log.error("MainFrameController::initializeStockMovementTables: {}", ex.getMessage());
            m_dialogHelper.showUnknownErrorMessageDialog(ex.getMessage());
        }
    }
    private void initializeTablesCallback(Pair<JTable, DefaultTableModel> pair)
    {
        pair.getFirst().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        pair.getFirst().getTableHeader().setReorderingAllowed(false);
        pair.getFirst().getTableHeader().setResizingAllowed(false);
        pair.getFirst().getTableHeader().setUpdateTableInRealTime(false);
        pair.getFirst().getTableHeader().setFont(new Font("calibri", Font.BOLD, 13));
        pair.getFirst().getTableHeader().addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                pair.getFirst().getTableHeader().setToolTipText(
                        pair.getFirst().getTableHeader().getColumnModel().getColumn(
                                pair.getFirst().getTableHeader().getColumnModel().getColumnIndexAtX(e.getX())
                        ).getIdentifier().toString());
            }
        });
        pair.getFirst().setModel(pair.getSecond());
        setCellsAlignment(pair.getFirst());
    }
    private void initializeStockTablesClickListeners(Pair<JTable, DefaultTableModel> pair)
    {
        pair.getFirst().setComponentPopupMenu((JPopupMenu) m_applicationContext.getBean("bean.table.popup.click.right"));
        pair.getFirst().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                try {
                    Point point = e.getPoint();
                    int currentRow = pair.getFirst().rowAtPoint(point);

                    if (SwingUtilities.isRightMouseButton(e) || SwingUtilities.isLeftMouseButton(e))
                        m_threadPool.execute(() -> tableStockItemClickedCallback(pair.getFirst(), currentRow));

                    if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2)
                        m_dialogHelper.showProductCardDialogWithProduct();

                } catch (IllegalArgumentException ignore) {
                    pair.getFirst().clearSelection();
                }
            }
        });
        pair.getFirst().addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                var row = pair.getFirst().rowAtPoint(e.getPoint());
                var col = pair.getFirst().columnAtPoint(e.getPoint());

                pair.getFirst().setToolTipText(pair.getFirst().getValueAt(row, col).toString());
            }
        });
    }
    private void tableStockItemClickedCallback(JTable table, int currentRow)
    {
        var selectedProductDTO = m_applicationService.findProductById(table
                .getValueAt(currentRow, 0).toString()).orElse(null);
        table.setRowSelectionInterval(currentRow, currentRow);
        m_dialogHelper.setSelectedProduct(selectedProductDTO);
    }
    private void tableUserItemClickedCallback(JTable table, int currentRow)
    {
        var selectedUser = m_applicationService.findByUsername(table
                .getValueAt(currentRow, 0).toString()).orElse(null);
        table.setRowSelectionInterval(currentRow, currentRow);
        m_dialogHelper.setSelectedUser(selectedUser);
    }
    private void initializeStockOutTableModel(DefaultTableModel model)
    {
        Object[] tableHeaders = {TABLE_HEADER_PRODUCT_ORIGINAL_CODE, TABLE_HEADER_PRODUCT_NAME, TABLE_HEADER_STOCK};
        model.setColumnIdentifiers(tableHeaders);
    }
    private void initializeStockLesserTableModel(DefaultTableModel model)
    {
        Object[] tableHeaders = {TABLE_HEADER_PRODUCT_ORIGINAL_CODE, TABLE_HEADER_PRODUCT_NAME,
                TABLE_HEADER_PRODUCT_THRESHOLD, TABLE_HEADER_STOCK};
        model.setColumnIdentifiers(tableHeaders);
    }
    private void initializeStockInputMovementsTableModel(DefaultTableModel model)
    {
        Object[] tableHeaders = {TABLE_HEADER_PRODUCT_ORIGINAL_CODE, TABLE_HEADER_PRODUCT_NAME, TABLE_HEADER_PRODUCT_STOCK_CODE,
            TABLE_HEADER_PRODUCT_STOCK_SHELF_NUMBER, TABLE_HEADER_STOCK,
                TABLE_HEADER_STOCK_REGISTER_DATE, TABLE_HEADER_RECORD_AMOUNT, TABLE_HEADER_RECORD_OWNER};

        model.setColumnIdentifiers(tableHeaders);
    }
    private void initializeStockOutputMovementsTableModel(DefaultTableModel model)
    {
        Object[] tableHeaders = {TABLE_HEADER_PRODUCT_ORIGINAL_CODE, TABLE_HEADER_PRODUCT_NAME, TABLE_HEADER_PRODUCT_STOCK_CODE,
                TABLE_HEADER_PRODUCT_STOCK_SHELF_NUMBER, TABLE_HEADER_STOCK,
                TABLE_HEADER_STOCK_REGISTER_DATE, TABLE_HEADER_RECORD_AMOUNT, TABLE_HEADER_RECORD_OWNER};

        model.setColumnIdentifiers(tableHeaders);
    }
    private void initializeProductListTableModel(DefaultTableModel model)
    {
        Object[] tableHeaders = {TABLE_HEADER_PRODUCT_ORIGINAL_CODE, TABLE_HEADER_PRODUCT_NAME, TABLE_HEADER_PRODUCT_REGISTER_DATE,
                TABLE_HEADER_PRODUCT_STOCK_CODE, TABLE_HEADER_STOCK, TABLE_HEADER_PRODUCT_THRESHOLD};

        model.setColumnIdentifiers(tableHeaders);
    }
    private void initializeUsersListTableModel(DefaultTableModel model)
    {
        Object[] tableHeaders = {TABLE_HEADER_USER_USERNAME, TABLE_HEADER_USER_FIRST_NAME, TABLE_HEADER_USER_LAST_NAME,
                TABLE_HEADER_USER_EMAIL, TABLE_HEADER_USER_REGISTER_DATE, TABLE_HEADER_USER_DESCRIPTION};

        model.setColumnIdentifiers(tableHeaders);
    }
    private void initializeLastTwentyStockMovesTableModel(DefaultTableModel model)
    {
        Object[] tableHeaders = {TABLE_HEADER_STOCK_MOVEMENT_TYPE, TABLE_HEADER_USER_USERNAME, TABLE_HEADER_USER_REGISTER_DATE};

        model.setColumnIdentifiers(tableHeaders);
    }
    private void fillStockInputMovementsTable(List<StockMovementDTO> list, DefaultTableModel defaultTableModel)
    {
        list.stream().filter(stockMovementDTO -> stockMovementDTO.getStockMovementType() == StockMovementType.STOCK_INPUT
                || stockMovementDTO.getStockMovementType() == StockMovementType.STOCK_REGISTER)
                .forEach(stockMovementDTO -> fillStockMovementsTableCallback(stockMovementDTO, defaultTableModel));
    }
    private void fillStockOutputMovementsTable(List<StockMovementDTO> list, DefaultTableModel defaultTableModel)
    {
        list.stream().filter(stockMovementDTO -> stockMovementDTO.getStockMovementType() == StockMovementType.STOCK_OUTPUT)
                .forEach(stockMovementDTO -> fillStockMovementsTableCallback(stockMovementDTO, defaultTableModel));
    }
    private void fillProductListTable(List<ProductDTO> list, DefaultTableModel defaultTableModel)
    {
        list.forEach(productDTO -> fillProductListTableCallback(productDTO, defaultTableModel));
    }
    private void fillStockOutTable(List<ProductDTO> list,DefaultTableModel defaultTableModel)
    {
        list.forEach(productDTO -> fillStockOutTableCallback(productDTO, defaultTableModel));
    }
    private void fillStockThresholdTable(List<ProductDTO> list,DefaultTableModel defaultTableModel)
    {
        list.forEach(productDTO -> fillStockLesserTableCallback(productDTO, defaultTableModel));
    }
    private void fillUsersListTable(List<UserDTO> list,DefaultTableModel defaultTableModel)
    {
        list.forEach(userDTO -> fillUsersListTableCallback(userDTO, defaultTableModel));
    }
    private void fillLastTwentyStockMovesTable(List<StockMovementDTO> list,DefaultTableModel defaultTableModel)
    {
        list.forEach(stockMovementDTO -> fillLastTwentyMovesTableCallback(stockMovementDTO, defaultTableModel));
    }
    private void fillStockOutTableCallback(ProductDTO productDTO, DefaultTableModel defaultTableModel)
    {
        Object[] data = {productDTO.getOriginalCode(), productDTO.getProductName(),  productDTO.getStock().getAmount()};
        defaultTableModel.addRow(data);
    }
    private void fillStockLesserTableCallback(ProductDTO productDTO, DefaultTableModel defaultTableModel)
    {
        Object[] data = {productDTO.getOriginalCode(), productDTO.getProductName(), productDTO.getStock().getThreshold(), productDTO.getStock().getAmount()};
        defaultTableModel.addRow(data);
    }
    private void fillStockMovementsTableCallback(StockMovementDTO stockMovementDTO, DefaultTableModel defaultTableModel)
    {
        Object[] data = {stockMovementDTO.getStock().getProduct().getOriginalCode(), stockMovementDTO.getStock().getProduct().getProductName(),
                stockMovementDTO.getStock().getProduct().getStockCode(), stockMovementDTO.getStock().getShelfNumber(),
                stockMovementDTO.getStock().getAmount(), m_dateTimeFormatter.format(stockMovementDTO.getRecordDate()),
                stockMovementDTO.getAmount(), stockMovementDTO.getUser().getUsername()};

        defaultTableModel.addRow(data);
    }
    private void fillProductListTableCallback(ProductDTO productDTO,DefaultTableModel defaultTableModel)
    {
        Object[] data = {productDTO.getOriginalCode(), productDTO.getProductName(),
                m_dateTimeFormatter.format(productDTO.getRegisterDate()),
                productDTO.getStockCode(), productDTO.getStock().getAmount(), productDTO.getStock().getThreshold()};

        defaultTableModel.addRow(data);
    }
    private void fillUsersListTableCallback(UserDTO userDTO, DefaultTableModel defaultTableModel)
    {
        Object[] data = {userDTO.getUsername(), userDTO.getFirstName(), userDTO.getLastName(), userDTO.getEMail(),
                m_dateTimeFormatter.format(userDTO.getDateOfRegister()), userDTO.getDescription()};

        defaultTableModel.addRow(data);
    }
    private void fillLastTwentyMovesTableCallback(StockMovementDTO stockMovementDTO, DefaultTableModel defaultTableModel)
    {
        Object[] data = {stockMovementDTO.getStockMovementType().toString(), stockMovementDTO.getUser().getUsername(),
                m_dateTimeFormatter.format(stockMovementDTO.getRecordDate())};

        defaultTableModel.addRow(data);
    }
    private void setCellsAlignment(JTable table)
    {
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        TableModel tableModel = table.getModel();

        for (int columnIndex = 0; columnIndex < tableModel.getColumnCount(); columnIndex++)
        {
            table.getColumnModel().getColumn(columnIndex).setCellRenderer(rightRenderer);
        }
    }
}
