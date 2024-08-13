package com.alcanl.app.application.ui.print;

import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.awt.*;
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

    @Override
    public int print(Graphics g, PageFormat pf, int pageIndex) {
        if (pageIndex > 0) {
            return Printable.NO_SUCH_PAGE;
        }

        Graphics2D g2d = (Graphics2D) g;

        g2d.setFont(new Font("default", Font.BOLD, 24));
        g2d.drawString(productOriginalCode.toUpperCase(), (int)pf.getImageableWidth() / 2, 30);

        g2d.setFont(new Font("default", Font.PLAIN, 24));
        g2d.drawString(productName.toUpperCase(), (int)pf.getImageableWidth() / 2, 70);

        g2d.setFont(new Font("default", Font.BOLD, 24));
        g2d.drawString(productStockCode.toUpperCase(), (int)pf.getImageableWidth() / 2, 110);

        return Printable.PAGE_EXISTS;
    }

    public static PageFormat customizePageFormat(PageFormat pageFormat) {
        Paper paper = new Paper();
        paper.setSize(80D, 40D);
        double margin = 20.0;
        paper.setImageableArea(margin, margin, pageFormat.getWidth() - 2 * margin, pageFormat.getHeight() - 2 * margin);
        pageFormat.setPaper(paper);
        return pageFormat;
    }
}
