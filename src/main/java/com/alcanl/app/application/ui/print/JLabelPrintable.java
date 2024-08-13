package com.alcanl.app.application.ui.print;

import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;

@Scope("prototype")
@Component("bean.print.label.printable")
@NoArgsConstructor
@Setter
public class JLabelPrintable implements Printable {
    private String productOriginalCode;
    private String productName;
    private String productStockCode;
    private static final int lineCharacterBound = 23;
    private static final int yDimensionRange = 40;

    private static int getStringWidth(String str, int fontStyle)
    {
        var affineTransform = new AffineTransform();
        var fontRenderContext = new FontRenderContext(affineTransform,true,true);
        var font = new Font("default", fontStyle, 24);
        return (int)(font.getStringBounds(str, fontRenderContext).getWidth());

    }

    @Override
    public int print(Graphics g, PageFormat pf, int pageIndex) {
        if (pageIndex > 0) {
            return Printable.NO_SUCH_PAGE;
        }

        Graphics2D g2d = (Graphics2D) g;

        var productNameWidth = getStringWidth(productName.toUpperCase(), Font.BOLD);
        var originalCodeWidth = getStringWidth(productOriginalCode.toUpperCase(), Font.PLAIN);
        var productStockCodeWidth = getStringWidth(productStockCode.toUpperCase(), Font.BOLD);
        var startY = 30;

        g2d.setFont(new Font("default", Font.BOLD, 24));

        if (productName.length() <= lineCharacterBound)
            g2d.drawString(productName.toUpperCase(), (int) pf.getImageableWidth() / 2 - productNameWidth / 2, startY += yDimensionRange);
        else {
            var firstLine = productName.toUpperCase().substring(0, lineCharacterBound + 1);
            var secondLine = productName.toUpperCase().substring(lineCharacterBound);
            var firstLineWidth = getStringWidth(firstLine, Font.BOLD);
            var secondLineWidth = getStringWidth(secondLine, Font.BOLD);
            g2d.drawString(firstLine, (int) pf.getImageableWidth() / 2 - firstLineWidth / 2, startY += yDimensionRange);
            g2d.drawString(secondLine, (int) pf.getImageableWidth() / 2 - secondLineWidth / 2, startY += yDimensionRange);
        }

        g2d.setFont(new Font("default", Font.PLAIN, 24));
        g2d.drawString(productOriginalCode.toUpperCase(), (int)pf.getImageableWidth() / 2 - originalCodeWidth / 2, startY += yDimensionRange);

        g2d.setFont(new Font("default", Font.BOLD, 24));
        g2d.drawString(productStockCode.toUpperCase(), (int)pf.getImageableWidth() / 2 - productStockCodeWidth / 2, startY + yDimensionRange);

        return Printable.PAGE_EXISTS;
    }

    public static PageFormat customizePageFormat(PageFormat pageFormat) {
        Paper paper = new Paper();
        paper.setSize(80D, 40D);
        double margin = 20.0;
        paper.setImageableArea(margin, margin, paper.getWidth() - 2 * margin, paper.getHeight() - 2 * margin);
        pageFormat.setPaper(paper);
        return pageFormat;
    }
}
