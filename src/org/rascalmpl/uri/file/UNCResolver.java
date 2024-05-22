package org.rascalmpl.uri.file;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.regex.Pattern;

import io.usethesource.vallang.ISourceLocation;

/**
 * Implements the UNC-available network shares on Windows systems.
 */
public class UNCResolver extends FileURIResolver {
    private boolean onWindows = System.getProperty("os.name").toLowerCase().startsWith("win");

    public UNCResolver() throws IOException {
        super();
    }

    @Override
    protected String getPath(ISourceLocation uri) {
        if (!onWindows) {
            throw new RuntimeException(new FileNotFoundException(uri.toString() + "; UNC is only available on Windows"));
        }
        
        if (uri.hasAuthority()) {
            String path = uri.getPath();
            
            if (path.startsWith("/")) {
                // that will be the backslash added before the path later
                path = path.substring(1);
            }

            if (path.endsWith(":")) {
                // current folder on drive not supported in UNC notation, this becomes the root of the drive
                path = path + "\\";
            }
            
			return "\\\\" + uri.getAuthority() + "\\" + path;
		}
		else {
			// just a normal absolute path
			return uri.getPath();
		}
    }
    
    @Override
    public String scheme() {
        return "unc";
    }
}
