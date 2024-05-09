package org.rascalmpl.library.util;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;

import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.exceptions.RuntimeExceptionFactory;
import org.rascalmpl.library.Prelude;

import io.usethesource.vallang.ISet;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValueFactory;

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
            DataFlavor flavor = DataFlavor.getTextPlainUnicodeFlavor();

            try (Reader data = flavor.getReaderForText(cp.getContents(null))) {
                return vf.string(Prelude.consumeInputStream(data));
            }
        }
        catch (UnsupportedFlavorException | IOException e) {
           monitor.warning("Clipboard::paste failed", null);
        }
        
        return vf.string("");
    }

    public IString paste(IString mimetype) {
        try {
            DataFlavor flavor = new DataFlavor(mimetype.getValue());
            
            try (Reader data = flavor.getReaderForText(cp.getContents(null))) {
                return vf.string(Prelude.consumeInputStream(data));
            }
        }
        catch (ClassNotFoundException e) {
           throw RuntimeExceptionFactory.illegalArgument(vf.string("Unsupported clipboard mimetype: " + mimetype));
        }
        catch (UnsupportedFlavorException e) {
            throw RuntimeExceptionFactory.illegalArgument(vf.string("Unsupported clipboard mimetype: " + e.getMessage()));
        }
        catch (IOException e) {
            throw RuntimeExceptionFactory.io(e.getMessage());
        }
    }

    public ISet availableTextMimetypes() {
        return Arrays.stream(cp.getAvailableDataFlavors())
            .filter(flavor -> flavor.isFlavorTextType())
            .map(flavor -> vf.tuple(vf.string(flavor.getHumanPresentableName()), vf.string(flavor.getMimeType())))
            .collect(vf.setWriter());
    }

    public ISet availableTextMimetypesFor(IString shortMimetype) {
        return Arrays.stream(cp.getAvailableDataFlavors())
            .filter(flavor -> flavor.isFlavorTextType())
            .filter(flavor -> flavor.isMimeTypeEqual(shortMimetype.getValue()))
            .map(flavor -> vf.tuple(vf.string(flavor.getHumanPresentableName()), vf.string(flavor.getMimeType())))
            .collect(vf.setWriter());
    }
}
