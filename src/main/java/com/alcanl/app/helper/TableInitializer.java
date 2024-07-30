package com.alcanl.app.helper;

import com.alcanl.app.service.ApplicationService;
import com.alcanl.app.service.dto.ProductDTO;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
    private final Resources m_resources;

    private static final String TABLE_STOCK_OUT_PRODUCT_NAME = "ÜRÜN ADI";
    private static final String TABLE_STOCK_OUT_PRODUCT_ORIGINAL_CODE = "ÜRÜN KODU";
    private static final String TABLE_STOCK_OUT_STOCK = "STOK";
    private static final String TABLE_STOCK_LESSER_PRODUCT_THRESHOLD = "EŞİK STOK";

    public static int criticalStockCount = 0;
    public TableInitializer(ApplicationService applicationService, ExecutorService threadPool,
                            Resources resources, ApplicationContext applicationContext, DialogHelper dialogHelper,
                            PopUpHelper popUpHelper)
    {
        m_tablePairsList = new ArrayList<>();
        m_applicationService = applicationService;
        m_threadPool = threadPool;
        m_resources = resources;
        m_dialogHelper = dialogHelper;
        m_applicationContext = applicationContext;
        m_popUpHelper = popUpHelper;
    }
    public void setTables(JTable... tables)
    {
        m_tablePairsList.clear();
        criticalStockCount = 0;
        Arrays.stream(tables).forEach(table -> m_tablePairsList.add(Pair.of(table,
                (DefaultTableModel)m_applicationContext.getBean("bean.table.model.default"))));
    }
    public void initializeTables()
    {

        try {
            initializeStockOutTableModel(m_tablePairsList.getFirst().getSecond());
            initializeStockLesserTableModel(m_tablePairsList.get(1).getSecond());
            fillStockOutTable(m_threadPool.submit(m_applicationService::getAllStockOutProducts).get(),
                    m_tablePairsList.getFirst().getSecond());
            fillStockThresholdTable(m_threadPool.submit(m_applicationService::getAllLesserThanThresholdStockProducts).get(),
                    m_tablePairsList.get(1).getSecond());

            m_tablePairsList.forEach(this::initializeTablesCallback);

        } catch (ExecutionException | InterruptedException ex) {
            log.error("MainFrameController::initializeTables: {}", ex.getMessage());
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
            public void mouseClicked(MouseEvent e) {
                try {
                    Point point = e.getPoint();
                    int currentRow = pair.getFirst().rowAtPoint(point);

                    if (SwingUtilities.isRightMouseButton(e) || SwingUtilities.isLeftMouseButton(e))
                        m_threadPool.execute(() -> tableItemClickCallback(pair.getFirst(), currentRow));

                    if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2)
                        m_dialogHelper.showProductCardDialog();

                } catch (IllegalArgumentException ignore) {
                        pair.getFirst().clearSelection();
                    }
                }
            });
        pair.getFirst().setModel(pair.getSecond());
        m_resources.setCellsAlignment(pair.getFirst(), SwingConstants.CENTER);
        criticalStockCount += pair.getSecond().getRowCount();
    }
    private void tableItemClickCallback(JTable table, int currentRow)
    {
        var selectedProductDTO = m_applicationService.findProductById(table
                .getValueAt(currentRow, 1).toString()).orElse(null);
        m_dialogHelper.setSelectedProduct(selectedProductDTO);
        m_popUpHelper.setSelectedProduct(selectedProductDTO);
    }
    private void initializeStockOutTableModel(DefaultTableModel model)
    {
        Object[] tableHeaders = {TABLE_STOCK_OUT_PRODUCT_NAME, TABLE_STOCK_OUT_PRODUCT_ORIGINAL_CODE, TABLE_STOCK_OUT_STOCK};
        model.setColumnIdentifiers(tableHeaders);
    }
    private void initializeStockLesserTableModel(DefaultTableModel model)
    {
        Object[] tableHeaders = {TABLE_STOCK_OUT_PRODUCT_NAME, TABLE_STOCK_OUT_PRODUCT_ORIGINAL_CODE,
                TABLE_STOCK_LESSER_PRODUCT_THRESHOLD, TABLE_STOCK_OUT_STOCK};
        model.setColumnIdentifiers(tableHeaders);
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
        Object[] data = {productDTO.getProductName(), productDTO.getOriginalCode(), productDTO.getStock().getAmount()};
        defaultTableModel.addRow(data);
    }
    private void fillTableLesserCallback(ProductDTO productDTO,DefaultTableModel defaultTableModel)
    {
        Object[] data = {productDTO.getProductName(), productDTO.getOriginalCode(), productDTO.getStock().getThreshold(), productDTO.getStock().getAmount()};
        defaultTableModel.addRow(data);
    }
}
