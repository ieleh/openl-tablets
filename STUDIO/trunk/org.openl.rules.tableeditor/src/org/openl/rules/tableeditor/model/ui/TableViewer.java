package org.openl.rules.tableeditor.model.ui;

import org.apache.commons.lang.StringUtils;
import org.openl.rules.table.FormattedCell;
import org.openl.rules.table.ICell;
import org.openl.rules.table.ICellComment;
import org.openl.rules.table.IGrid;
import org.openl.rules.table.IGridRegion;
import org.openl.rules.table.IGridTable;
import org.openl.rules.table.ui.ICellStyle;
import org.openl.util.Log;

public class TableViewer {

    private IGrid grid;

    private IGridRegion reg;

    private void setStyle(ICell cell, CellModel cm) {
        ICellStyle style = cell.getStyle();

        if (style == null) {
            return;
        }

        switch (style.getHorizontalAlignment()) {
            case ICellStyle.ALIGN_RIGHT:
                cm.setHalign("right");
                break;
            case ICellStyle.ALIGN_CENTER:
                cm.setHalign("center");
                break;
            case ICellStyle.ALIGN_JUSTIFY:
                cm.setHalign("justify");
                break;
        }

        switch (style.getVerticalAlignment()) {
            case ICellStyle.VERTICAL_BOTTOM:
                cm.setValign("bottom");
                break;
            case ICellStyle.VERTICAL_CENTER:
                cm.setValign("center");
                break;
            case ICellStyle.VERTICAL_TOP:
                cm.setValign("top");
                break;
        }

        if (style.getIdent() > 0) {
            cm.setIdent(style.getIdent());
        }

        short[] rgb = style.getFillForegroundColor();
        cm.setRgbBackground(rgb);

        cm.setFont(cell.getFont());
    }

    /** @deprecated */
    public static String showTable(TableModel tm, boolean showGrid) {
        StringBuilder buf = new StringBuilder(1000);
        tm.toHtmlString(buf, showGrid);
        return buf.toString();
    }

    /**
     * Default constructor
     */
    public TableViewer() {

    }

    /**
     * Two argument constructor
     *
     * @param grid
     * @param reg
     */
    public TableViewer(IGrid grid, IGridRegion reg) {
        super();
        this.grid = grid;
        this.reg = reg;
    }

    CellModel buildCell(ICell cell, CellModel cm) {

        cm.setColspan(getColSpan(cell));
        cm.setRowspan(getRowSpan(cell));

        if (cm.getRow() == 0) {
            cm.setWidth(getWidth(cell));
        }

        String formattedValue = ((FormattedCell) cell).getFormattedValue();
        if (StringUtils.isNotBlank(formattedValue)) {
            cm.setContent(formattedValue);
            if (cell.getFormula() != null) {
                cm.setFormula(cell.getFormula());
            }
        }

        ICellComment cellComment = cell.getComment();
        cm.setComment(cellComment != null ? cellComment.getText() : null);

        setStyle(cell, cm);
        return cm;
    }

    public TableModel buildModel(IGridTable gt) {

        // IGridTable table = new GridTable(g.getTop(), g.getLeft(),
        // g.getBottom(),
        // g.getRight(), t.getGrid());

        int h = IGridRegion.Tool.height(reg);
        int w = IGridRegion.Tool.width(reg);

        TableModel tm = new TableModel(w, h, gt);

        for (int row = reg.getTop(); row <= reg.getBottom(); row++) {
            for (int column = reg.getLeft(); column <= reg.getRight(); column++) {
                int c = column - reg.getLeft();
                int r = row - reg.getTop();
                if (tm.hasCell(r, c)) {
                    continue;
                }
                ICell cell = grid.getCell(column, row);

                CellModel cm = buildCell(cell, new CellModel(r, c));

                tm.addCell(cm, r, c);
                if (cm.getColspan() > 1 || cm.getRowspan() > 1) {
                    CellModelDelegator cmd = new CellModelDelegator(cm);
                    for (int i = 0; i < cm.getRowspan(); i++) {
                        for (int j = 0; j < cm.getColspan(); j++) {
                            if (i == 0 && j == 0) {
                                continue;
                            }
                            tm.addCell(cmd, r + i, c + j);
                        }
                    }
                }

            }

        }

        setGrid(tm);
        return tm;
    }

    BorderStyle getBorderStyle(ICellStyle cs, int side) {

        int xlsStyle;
        short[] rgb;

        short[] bss = cs.getBorderStyle();
        xlsStyle = bss == null ? ICellStyle.BORDER_NONE : bss[side];

        short[][] rgbb = cs.getBorderRGB();
        rgb = rgbb == null ? new short[] { 0, 0, 0 } : rgbb[side];

        BorderStyle bs = new BorderStyle();
        bs.setRgb(rgb);
        switch (xlsStyle) {
            case ICellStyle.BORDER_NONE:
                return BorderStyle.NONE;
            case ICellStyle.BORDER_DASH_DOT_DOT:
            case ICellStyle.BORDER_DASH_DOT:
            case ICellStyle.BORDER_DASHED:
                bs.setWidth(1);
                bs.setStyle("dashed");
                break;

            case ICellStyle.BORDER_DOTTED:
                bs.setWidth(1);
                bs.setStyle("dotted");
                break;
            case ICellStyle.BORDER_DOUBLE:
                bs.setWidth(1);
                bs.setStyle("double");
                break;
            case ICellStyle.BORDER_THIN:
                bs.setWidth(1);
                bs.setStyle("solid");
                break;
            case ICellStyle.BORDER_THICK:
                bs.setWidth(2);
                bs.setStyle("solid");
                break;
            case ICellStyle.BORDER_HAIR:
                bs.setWidth(1);
                bs.setStyle("dotted");
                break;
            case ICellStyle.BORDER_MEDIUM:
                bs.setWidth(2);
                bs.setStyle("solid");
                break;
            case ICellStyle.BORDER_MEDIUM_DASH_DOT:
            case ICellStyle.BORDER_MEDIUM_DASH_DOT_DOT:
            case ICellStyle.BORDER_MEDIUM_DASHED:
                bs.setWidth(2);
                bs.setStyle("dashed");
                break;
            default:
                Log.warn("Unknown border style: " + xlsStyle);
        }
        return bs;
    }

    int getColSpan(ICell cell) {
        IGridRegion gr = cell.getRegion();
        if (gr == null) {
            return 1;
        }
        return IGridRegion.Tool.width(IGridRegion.Tool.intersect(reg, gr));
    }

    int getRowSpan(ICell cell) {
        IGridRegion gr = cell.getRegion();
        if (gr == null) {
            return 1;
        }
        return IGridRegion.Tool.height(IGridRegion.Tool.intersect(reg, gr));
    }

    public int getWidth(ICell cell) {
        IGridRegion gr;
        if ((gr = cell.getRegion()) == null) {
            return grid.getColumnWidth(cell.getColumn());
        }
        int w = 0;

        gr = IGridRegion.Tool.intersect(gr, reg);
        for (int c = gr.getLeft(); c <= gr.getRight(); c++) {
            w += grid.getColumnWidth(c);
        }

        return w;
    }

    short[] rgb(BorderStyle bs1, BorderStyle bs2) {
        if (bs1 == null && bs2 == null) {
            return new short[] { 0, 0, 0 };
        }

        return bs1 == null ? bs2.getRgb()
                : (bs2 == null ? bs1.getRgb()
                        : (bs1 == BorderStyle.NONE ? bs2.getRgb() : bs1.getRgb()));
    }

    void setGrid(TableModel tm) {
        int width = IGridRegion.Tool.width(reg);

        for (int i = 0; i <= width; i++) {
            setVerticalBorder(i, tm);
        }

        int height = IGridRegion.Tool.height(reg);

        for (int i = 0; i <= height; i++) {
            setHorizontalBorder(i, tm);
        }

    }

    void setHorizontalBorder(int row, TableModel tm) {
        int width = IGridRegion.Tool.width(reg);
        int left = reg.getLeft();
        int top = reg.getTop();

        for (int i = 0; i < width; i++) {
            ICellStyle ts = row + top - 1 < 0 ? null : grid.getCell(i + left, row + top - 1).getStyle();
            ICellStyle bs = grid.getCell(i + left, row + top).getStyle();

            CellModel cmTop = ts == null ? null : tm.findCellModel(i, row - 1, ICellStyle.BOTTOM);
            CellModel cmBottom = bs == null ? null : tm.findCellModel(i, row, ICellStyle.TOP);

            if (cmTop == null && cmBottom == null) {
                continue;
            }

            BorderStyle tStyle = ts != null ? getBorderStyle(ts, ICellStyle.BOTTOM) : null;
            BorderStyle bStyle = bs != null ? getBorderStyle(bs, ICellStyle.TOP) : null;

            int W = width(tStyle, bStyle);
            String style = style(tStyle, bStyle);
            short[] rgb = rgb(tStyle, bStyle);

            BorderStyle bstyle = new BorderStyle(W, style, rgb);

            switch (W) {
                case 0:
                    break;
                case 1:
                    if (cmTop == null) {
                        cmBottom.setBorderStyle(bstyle, ICellStyle.TOP);

                    } else {
                        cmTop.setBorderStyle(bstyle, ICellStyle.BOTTOM);
                    }
                    break;
                case 2:
                    if (cmTop == null) {
                        cmBottom.setBorderStyle(bstyle, ICellStyle.TOP);
                    } else if (cmBottom == null) {
                        cmTop.setBorderStyle(bstyle, ICellStyle.BOTTOM);
                    } else {
                        bstyle.setWidth(1);
                        cmBottom.setBorderStyle(bstyle, ICellStyle.TOP);
                        cmTop.setBorderStyle(bstyle, ICellStyle.BOTTOM);
                    }

            }
        }

    }

    void setVerticalBorder(int column, TableModel tm) {
        int height = IGridRegion.Tool.height(reg);
        int left = reg.getLeft();
        int top = reg.getTop();

        for (int i = 0; i < height; i++) {
            ICellStyle ls = column + left - 1 < 0 ? null : grid.getCell(column + left - 1, i + top).getStyle();
            ICellStyle rs = column + left - 1 < 0 ? null : grid.getCell(column + left, i + top).getStyle();

            CellModel cmLeft = ls == null ? null : tm.findCellModel(column - 1, i, ICellStyle.RIGHT);
            CellModel cmRight = rs == null ? null : tm.findCellModel(column, i, ICellStyle.LEFT);

            if (cmLeft == null && cmRight == null) {
                continue;
            }

            BorderStyle lStyle = ls != null ? getBorderStyle(ls, ICellStyle.RIGHT) : null;
            BorderStyle rStyle = rs != null ? getBorderStyle(rs, ICellStyle.LEFT) : null;

            int W = width(lStyle, rStyle);
            String style = style(lStyle, rStyle);
            short[] rgb = rgb(lStyle, rStyle);

            BorderStyle bstyle = new BorderStyle(W, style, rgb);

            switch (W) {
                case 0:
                    break;
                case 1:
                    if (cmLeft == null) {
                        cmRight.setBorderStyle(bstyle, ICellStyle.LEFT);

                    } else {
                        cmLeft.setBorderStyle(bstyle, ICellStyle.RIGHT);
                    }
                    break;
                case 2:
                    if (cmLeft == null) {
                        cmRight.setBorderStyle(bstyle, ICellStyle.LEFT);
                    } else if (cmRight == null) {
                        cmLeft.setBorderStyle(bstyle, ICellStyle.RIGHT);
                    } else {
                        bstyle.setWidth(1);
                        cmRight.setBorderStyle(bstyle, ICellStyle.LEFT);
                        cmLeft.setBorderStyle(bstyle, ICellStyle.RIGHT);
                    }

            }
        }

    }

    String style(BorderStyle bs1, BorderStyle bs2) {
        if (bs1 == null && bs2 == null) {
            return "none";
        }

        return bs1 == null ? bs2.getStyle()
                : (bs2 == null ? bs1.getStyle()
                        : (bs1 == BorderStyle.NONE ? bs2.getStyle() : bs1.getStyle()));
    }

    int width(BorderStyle bs1, BorderStyle bs2) {
        if (bs1 == null && bs2 == null) {
            return 0;
        }

        return bs1 == null ? bs2.getWidth() : (bs2 == null ? bs1.getWidth() : Math.max(bs1.getWidth(), bs2.getWidth()));
    }

}
