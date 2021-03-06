package org.openl.rules.lang.xls;

import java.util.LinkedList;
import java.util.Queue;

import org.openl.rules.calc.CellsHeaderExtractor;
import org.openl.rules.lang.xls.syntax.SpreadsheetHeaderNode;
import org.openl.rules.lang.xls.syntax.TableSyntaxNode;
import org.openl.rules.lang.xls.syntax.TableSyntaxNodeHelper;
import org.openl.rules.table.ILogicalTable;
import org.openl.util.StringUtils;

/**
 * Compares spreadsheets. Spreadsheets with dependencies in cells on other
 * Custom Spreadsheet results are considered to be greater.
 * 
 * @author DLiauchuk
 * 
 */
public class SpreadsheetNodeSorter {

    private static boolean check(TableSyntaxNode table1, TableSyntaxNode table2) {
        if (isSpreadsheet(table1) && isSpreadsheet(table2)) {
            // Compare both spreadsheets table syntax nodes.
            // Check if there are usages of custom spreadsheet type
            //
            CellsHeaderExtractor extractor1 = extractNames(table1);

            String methodName2 = TableSyntaxNodeHelper.getTableName(table2);
            if (StringUtils.isNotBlank(methodName2)) {
                if (extractor1.getDependentSpreadsheetTypes().contains(methodName2)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static TableSyntaxNode[] sort(TableSyntaxNode[] tableSyntaxNodes) {
        TableSyntaxNode[] result = new TableSyntaxNode[tableSyntaxNodes.length];
        boolean[][] matrix = new boolean[tableSyntaxNodes.length][tableSyntaxNodes.length];
        int[] c = new int[tableSyntaxNodes.length];
        for (int i = 0; i < tableSyntaxNodes.length; i++) {
            for (int j = 0; j < tableSyntaxNodes.length; j++) {
                if (i != j && check(tableSyntaxNodes[i], tableSyntaxNodes[j])) {
                    matrix[j][i] = true;
                    c[i]++;
                }
            }
        }
        int n = 0;
        Queue<Integer> q = new LinkedList<Integer>();
        for (int i = 0; i < tableSyntaxNodes.length; i++) {
            if (c[i] == 0) {
                q.add(i);
            }
        }
        while (!q.isEmpty()) {
            int t = q.poll();
            result[n++] = tableSyntaxNodes[t];
            for (int i = 0; i < tableSyntaxNodes.length; i++) {
                if (matrix[t][i]) {
                    c[i]--;
                    if (c[i] == 0) {
                        q.add(i);
                    }
                }
            }
        }
        if (n < tableSyntaxNodes.length) {
            for (int i = 0; i < tableSyntaxNodes.length; i++) {
                if (c[i] > 0) {
                    result[n++] = tableSyntaxNodes[i];
                }
            }
        }
        return result;
    }

    private static boolean isSpreadsheet(TableSyntaxNode o1) {
        return XlsNodeTypes.XLS_SPREADSHEET.equals(o1.getNodeType());
    }

    private static CellsHeaderExtractor extractNames(TableSyntaxNode tableSyntaxNode) {
        CellsHeaderExtractor extractor = null;

        // try to get previously stored extractor
        //
        SpreadsheetHeaderNode header = (SpreadsheetHeaderNode) tableSyntaxNode.getHeader();
        extractor = header.getCellHeadersExtractor();

        if (extractor == null) {
            ILogicalTable body = tableSyntaxNode.getTableBody();
            extractor = new CellsHeaderExtractor(TableSyntaxNodeHelper.getSignature(tableSyntaxNode),
                body.getRow(0).getColumns(1),
                body.getColumn(0).getRows(1));
            extractor.extract();

            // set cells header extractor to the table syntax node, to avoid
            // extracting several times
            //
            header.setCellHeadersExtractor(extractor);
        }

        return extractor;
    }

}
