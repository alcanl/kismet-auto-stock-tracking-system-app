package com.alcanl.app.helper.table;

import com.alcanl.app.helper.DialogHelper;
import com.alcanl.app.helper.PopUpHelper;
import com.alcanl.app.helper.table.search.type.StockMovementSearchType;
import com.alcanl.app.repository.entity.type.StockMovementType;
import com.alcanl.app.service.ApplicationService;
import com.alcanl.app.service.dto.ProductDTO;
import com.alcanl.app.service.dto.StockMovementDTO;
import lombok.Getter;
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
import java.time.format.DateTimeFormatter;
import java.util.*;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

@Component
@Getter
@Slf4j
public final class TableInitializer {

    private final List<Pair<JTable, DefaultTableModel>> m_tablePairsList;
    private final ApplicationContext m_applicationContext;
    private final ApplicationService m_applicationService;
    private final ExecutorService m_threadPool;
    private final PopUpHelper m_popUpHelper;
    private final DialogHelper m_dialogHelper;

    private static final String TABLE_HEADER_PRODUCT_NAME = "ÜRÜN ADI";
    private static final String TABLE_HEADER_PRODUCT_ORIGINAL_CODE = "ÜRÜN KODU";
    private static final String TABLE_HEADER_STOCK = "AKTİF STOK";
    private static final String TABLE_HEADER_PRODUCT_THRESHOLD = "EŞİK STOK";
    private static final String TABLE_HEADER_PRODUCT_STOCK_CODE = "STOK KODU";
    private static final String TABLE_HEADER_PRODUCT_STOCK_SHELF_NUMBER = "RAF NUMARASI";
    private static final String TABLE_HEADER_PRODUCT_REGISTER_DATE = "ÜRÜN KAYIT TARİHİ";
    private static final String TABLE_HEADER_STOCK_REGISTER_DATE = "KAYIT TARİHİ";
    private static final String TABLE_HEADER_RECORD_OWNER = "İŞLEM SAHİBİ";
    private static final String TABLE_HEADER_RECORD_AMOUNT = "KAYIT MİKTARI";

    public static int criticalStockCount = 0;
    public TableInitializer(ApplicationService applicationService, ExecutorService threadPool,
                            ApplicationContext applicationContext, DialogHelper dialogHelper, PopUpHelper popUpHelper)
    {
        m_tablePairsList = new ArrayList<>();
        m_applicationService = applicationService;
        m_threadPool = threadPool;
        m_dialogHelper = dialogHelper;
        m_applicationContext = applicationContext;
        m_popUpHelper = popUpHelper;
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
        try {
            criticalStockCount = 0;
            m_tablePairsList.getFirst().getSecond().getDataVector().clear();
            m_tablePairsList.get(1).getSecond().getDataVector().clear();
            initializeStockOutTables(m_tablePairsList.getFirst().getSecond(), m_tablePairsList.get(1).getSecond());
            m_tablePairsList.forEach(pair -> {
                pair.getFirst().setModel(pair.getSecond());
                setCellsAlignment(pair.getFirst());
            });

        } catch (ExecutionException | InterruptedException ex) {
            log.error("MainFrameController::reInitializeTables: {}", ex.getMessage());
        }
    }
    public void initializeTables()
    {
        initializeStockOutTables();
        initializeStockMovementTables(StockMovementSearchType.NONE);
        initializeProductListTable();
        m_tablePairsList.forEach(this::initializeTablesCallback);
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
    public void initializeProductListTable()
    {
        try {
            initializeProductListTable(m_applicationService.findAllProducts());
        }
        catch (ExecutionException | InterruptedException ex) {
            log.error("MainFrameController::initializeProductListTable: {}", ex.getMessage());
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
        pair.getFirst().setComponentPopupMenu((JPopupMenu) m_applicationContext.getBean("bean.table.popup.click.right"));
        pair.getFirst().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        pair.getFirst().getTableHeader().setReorderingAllowed(false);
        pair.getFirst().getTableHeader().setResizingAllowed(false);
        pair.getFirst().getTableHeader().setUpdateTableInRealTime(false);
        pair.getFirst().getTableHeader().setFont(new Font("calibri", Font.BOLD, 13));
        pair.getFirst().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                try {
                    Point point = e.getPoint();
                    int currentRow = pair.getFirst().rowAtPoint(point);

                    if (SwingUtilities.isRightMouseButton(e) || SwingUtilities.isLeftMouseButton(e))
                        m_threadPool.execute(() -> tableItemClickedCallback(pair.getFirst(), currentRow));

                    if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2)
                        m_dialogHelper.showProductCardDialogWithProduct();

                } catch (IllegalArgumentException ignore) {
                        pair.getFirst().clearSelection();
                    }
                }
            });
        pair.getFirst().setModel(pair.getSecond());
        setCellsAlignment(pair.getFirst());
    }
    private void tableItemClickedCallback(JTable table, int currentRow)
    {
        var selectedProductDTO = m_applicationService.findProductById(table
                .getValueAt(currentRow, 0).toString()).orElse(null);
        table.setRowSelectionInterval(currentRow, currentRow);
        m_dialogHelper.setSelectedProduct(selectedProductDTO);
        m_popUpHelper.setSelectedProduct(selectedProductDTO);
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
        list.forEach(productDTO -> fillTableStockOutCallback(productDTO, defaultTableModel));
    }
    private void fillStockThresholdTable(List<ProductDTO> list,DefaultTableModel defaultTableModel)
    {
        list.forEach(productDTO -> fillTableLesserCallback(productDTO, defaultTableModel));
    }

    private void fillTableStockOutCallback(ProductDTO productDTO,DefaultTableModel defaultTableModel)
    {
        Object[] data = {productDTO.getOriginalCode(), productDTO.getProductName(),  productDTO.getStock().getAmount()};
        defaultTableModel.addRow(data);
    }
    private void fillTableLesserCallback(ProductDTO productDTO,DefaultTableModel defaultTableModel)
    {
        Object[] data = {productDTO.getOriginalCode(), productDTO.getProductName(), productDTO.getStock().getThreshold(), productDTO.getStock().getAmount()};
        defaultTableModel.addRow(data);
    }
    private void fillStockMovementsTableCallback(StockMovementDTO stockMovementDTO, DefaultTableModel defaultTableModel)
    {
        Object[] data = {stockMovementDTO.getStock().getProduct().getOriginalCode(), stockMovementDTO.getStock().getProduct().getProductName(),
                stockMovementDTO.getStock().getProduct().getStockCode(), stockMovementDTO.getStock().getShelfNumber(), stockMovementDTO.getStock().getAmount(),
                stockMovementDTO.getRecordDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                stockMovementDTO.getAmount(), stockMovementDTO.getUser().getUsername()};

        defaultTableModel.addRow(data);
    }
    private void fillProductListTableCallback(ProductDTO productDTO,DefaultTableModel defaultTableModel)
    {
        Object[] data = {productDTO.getOriginalCode(), productDTO.getProductName(),
                productDTO.getRegisterDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                productDTO.getStockCode(), productDTO.getStock().getAmount(), productDTO.getStock().getThreshold()};

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
