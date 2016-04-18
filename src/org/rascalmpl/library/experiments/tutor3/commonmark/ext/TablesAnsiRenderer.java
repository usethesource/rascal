package org.rascalmpl.library.experiments.tutor3.commonmark.ext;

import java.util.Collections;
import java.util.Map;

import org.commonmark.ext.gfm.tables.TableBlock;
import org.commonmark.ext.gfm.tables.TableBody;
import org.commonmark.ext.gfm.tables.TableCell;
import org.commonmark.ext.gfm.tables.TableHead;
import org.commonmark.ext.gfm.tables.TableRow;
import org.commonmark.node.Node;
import org.commonmark.node.Visitor;
import org.rascalmpl.library.experiments.tutor3.commonmark.AnsiWriter;
import org.rascalmpl.library.experiments.tutor3.commonmark.CustomAnsiRenderer;

public class TablesAnsiRenderer implements CustomAnsiRenderer {

    @Override
    public boolean render(Node node, AnsiWriter ansiWriter, Visitor visitor) {
        if (node instanceof TableBlock) {
            renderBlock((TableBlock) node, ansiWriter, visitor);
        } else if (node instanceof TableHead) {
            renderHead((TableHead) node, ansiWriter, visitor);
        } else if (node instanceof TableBody) {
            renderBody((TableBody) node, ansiWriter, visitor);
        } else if (node instanceof TableRow) {
            renderRow((TableRow) node, ansiWriter, visitor);
        } else if (node instanceof TableCell) {
            renderCell((TableCell) node, ansiWriter, visitor);
        } else {
            return false;
        }
        return true;
    }

    private void renderBlock(TableBlock tableBlock, AnsiWriter ansiWriter, Visitor visitor) {
        ansiWriter.line();
        // TODO: What about attributes? If we got the renderer instead of the visitor, we could call getAttributes.
        //ansiWriter.append("|");
        visitChildren(tableBlock, visitor);
        //ansiWriter.append("|");
        ansiWriter.line();
    }

    private void renderHead(TableHead tableHead, AnsiWriter ansiWriter, Visitor visitor) {
        //ansiWriter.append("|");
        visitChildren(tableHead, visitor);
        //ansiWriter.append("|");
    }

    private void renderBody(TableBody tableBody, AnsiWriter ansiWriter, Visitor visitor) {
        ansiWriter.line();
        //ansiWriter.append("|");
        visitChildren(tableBody, visitor);
        //ansiWriter.append("|");
        ansiWriter.line();
    }

    private void renderRow(TableRow tableRow, AnsiWriter ansiWriter, Visitor visitor) {
        ansiWriter.line();
        //ansiWriter.append("|");
        visitChildren(tableRow, visitor);
        //ansiWriter.append("|");
        ansiWriter.line();
    }

    private void renderCell(TableCell tableCell, AnsiWriter ansiWriter, Visitor visitor) {
        String tag = tableCell.isHeader() ? "th" : "td";
        ansiWriter.append("|");
        visitChildren(tableCell, visitor);
        ansiWriter.append("|");
    }

    private static Map<String, String> getAttributes(TableCell tableCell) {
        if (tableCell.getAlignment() != null) {
            return Collections.singletonMap("align", getAlignValue(tableCell.getAlignment()));
        } else {
            return Collections.emptyMap();
        }
    }

    private static String getAlignValue(TableCell.Alignment alignment) {
        switch (alignment) {
            case LEFT:
                return "left";
            case CENTER:
                return "center";
            case RIGHT:
                return "right";
        }
        throw new IllegalStateException("Unknown alignment: " + alignment);
    }

    private void visitChildren(Node node, Visitor visitor) {
        Node child = node.getFirstChild();
        while (child != null) {
            child.accept(visitor);
            child = child.getNext();
        }
    }
}