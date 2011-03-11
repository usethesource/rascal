package org.rascalmpl.library.lang.jvm.run;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public class BinaryClassLoader extends ClassLoader {

	public Class<?> defineClass(URI classLocation) throws IOException{
		byte[] classBytes = getBytesFromFile(classLocation);
		return defineClass(null,classBytes,0,classBytes.length);
	}
	
	public static byte[] getBytesFromFile(URI fileLocation) throws IOException {
		File file = new File(fileLocation);
	    InputStream is = new FileInputStream(file);
	    long length = file.length();

	    // You cannot create an array using a long type.
	    // It needs to be an int type.
	    // Before converting to an int type, check
	    // to ensure that file is not larger than Integer.MAX_VALUE.
	    if (length > Integer.MAX_VALUE) {
	        throw new IOException("File is too large to be read into byte array!" + file.getName());
	    }
	    byte[] bytes = new byte[(int)length];

	    int offset = 0;
	    int numRead = 0;
	    while (offset < bytes.length) {
	    	numRead=is.read(bytes, offset, bytes.length-offset);
	    	if(numRead == 0) {
	    		 throw new IOException("Could not completely read file "+file.getName());
	    	}
	        offset += numRead;
	    }
	    
	    is.close();
	    return bytes;
	}
	
}
