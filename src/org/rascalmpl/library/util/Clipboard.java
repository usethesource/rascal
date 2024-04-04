package org.rascalmpl.library.util;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.io.StringWriter;

import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.values.RascalValueFactory;
import org.rascalmpl.values.parsetrees.SymbolAdapter;
import org.rascalmpl.values.parsetrees.TreeAdapter;

import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.io.StandardTextWriter;

public class Clipboard {
    private final IValueFactory vf;
    private final java.awt.datatransfer.Clipboard cp;
    private IRascalMonitor monitor;

    public Clipboard(IValueFactory vf, IRascalMonitor monitor) {
        this.vf = vf;
        this.cp = java.awt.Toolkit.getDefaultToolkit().getSystemClipboard();
        this.monitor = monitor;
    }

    public void copy(IString arg) {
        var selection = new StringSelection(arg.getValue());
		cp.setContents(selection, selection);
    }

    public IString paste() {
        try {
            if (cp.isDataFlavorAvailable(DataFlavor.stringFlavor)) {
                return vf.string(cp.getData(DataFlavor.stringFlavor).toString());
            }
        }
        catch (UnsupportedFlavorException | IOException e) {
           monitor.warning("Clipboard::paste failed", null);
        }
        
        return vf.string("");
    }
}
