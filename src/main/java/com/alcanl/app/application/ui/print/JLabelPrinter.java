package com.alcanl.app.application.ui.print;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;

@Component("bean.print.printer")
@RequiredArgsConstructor
public class JLabelPrinter implements Printable {

    @Override
    public int print(Graphics g, PageFormat pf, int pageIndex) {
        if (pageIndex > 0) {
            return Printable.NO_SUCH_PAGE;
        }

        //

        return Printable.PAGE_EXISTS;
    }

    public static PageFormat customizePageFormat(PageFormat pageFormat) {
        Paper paper = new Paper();
        double margin = 20.0;
        paper.setImageableArea(margin, margin, pageFormat.getWidth() - 2 * margin, pageFormat.getHeight() - 2 * margin);
        paper.setSize(pageFormat.getWidth(), pageFormat.getHeight());
        pageFormat.setPaper(paper);
        return pageFormat;
    }
}
