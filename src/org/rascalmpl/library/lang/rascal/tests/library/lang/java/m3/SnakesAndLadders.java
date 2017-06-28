package org.rascalmpl.library.lang.rascal.tests.library.lang.java.m3;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.Queue;

import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;

import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValueFactory;

public class SnakesAndLadders {
    private final IValueFactory vf;

    public SnakesAndLadders(IValueFactory vf) {
        this.vf = vf;
    }
    
    public ISourceLocation getSnakesAndLaddersPath() {
        try {
            URIResolverRegistry reg = URIResolverRegistry.getInstance();
            ISourceLocation tempRoot = URIUtil.correctLocation("tmp", "", "/snakes-ladders/");
            File expandedTempFolder = new File(reg.logicalToPhysical(tempRoot).getPath());
            if (new File(expandedTempFolder, ".project").exists()) {
                expandedTempFolder.deleteOnExit();
                return tempRoot;
            }

            ISourceLocation sourceRoot = vf.sourceLocation("testdata", "", "example-project/p2-SnakesAndLadders/");
            Queue<ISourceLocation> toCopy = new LinkedList<>();
            toCopy.add(sourceRoot);
            ISourceLocation current;
            while ((current = toCopy.poll()) != null) {
                if (reg.isDirectory(current)) {
                    for (ISourceLocation ent : reg.list(current)) {
                        toCopy.add(ent);
                    }
                }
                else {
                    copyFile(reg, sourceRoot, current, expandedTempFolder);
                }
            }
            expandedTempFolder.deleteOnExit();
            return tempRoot;
        }
        catch (Throwable e) {
            // null pointers etc all result in the same, invalid locations
            return URIUtil.invalidLocation();
        }
    }

    private void copyFile(URIResolverRegistry reg, ISourceLocation sourceRoot, ISourceLocation sourcePath, File targetRoot) throws IOException {
        File targetPath = new File(targetRoot, sourcePath.getPath().replace(sourceRoot.getPath(), ""));
        if (targetPath.getAbsolutePath().endsWith(".jv")) {
            String targetPathName = targetPath.getAbsolutePath();
            targetPath = new File(targetPathName.substring(0, targetPathName.length() - 3) + ".java");
        }
        targetPath.getParentFile().mkdirs();
        try (InputStream from = reg.getInputStream(sourcePath)) {
            try (OutputStream to = new FileOutputStream(targetPath)) {
                byte[] buffer = new byte[8*1024];
                int read;
                while ((read= from.read(buffer)) > 0 ) {
                    to.write(buffer, 0, read);
                }
            }
            finally {
                targetPath.deleteOnExit();
            }
        }
    }
}
