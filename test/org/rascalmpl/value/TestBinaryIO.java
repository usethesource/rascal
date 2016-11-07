/*******************************************************************************
* Copyright (c) 2009 Centrum Wiskunde en Informatica (CWI)
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*    Arnold Lankamp - interfaces and implementation
*******************************************************************************/
package org.rascalmpl.value;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

import org.rascalmpl.value.impl.fast.ValueFactory;
import org.rascalmpl.value.io.binary.message.IValueReader;
import org.rascalmpl.value.io.binary.message.IValueWriter;
import org.rascalmpl.value.io.binary.message.IValueWriter.CompressionRate;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeStore;
import org.rascalmpl.value.util.RandomValues;

import junit.framework.TestCase;

/**
 * @author Arnold Lankamp
 */
public class TestBinaryIO extends TestCase {
	private static IValueFactory vf = ValueFactory.getInstance();
	public void testBinaryIO() {
	    TypeStore ts = new TypeStore();
	    RandomValues.addNameType(ts);
	    for (IValue value: RandomValues.getTestValues(vf)) {
	        ioRoundTrip(ts, value);
	    }
	}
	
	public void testRandomBinaryIO() {
	    TypeStore ts = new TypeStore();
	    Type name = RandomValues.addNameType(ts);
	    Random r = new Random(42);
	    for (int i = 0; i < 20; i++) {
	        IValue value = RandomValues.generate(name, ts, vf, r, 10);
	        ioRoundTrip(ts, value);
	    }
	}

    private void ioRoundTrip(TypeStore ts, IValue value) {
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            try (IValueWriter w = new IValueWriter(buffer,  CompressionRate.Normal)) {
                w.write(value);
            }
            try (IValueReader read = new IValueReader(new ByteArrayInputStream(buffer.toByteArray()), vf, ts)) {
                IValue result = read.read();
                if(!value.isEqual(result)){
                    String message = "Not equal: \n\t"+value+" : "+value.getType()+"\n\t"+result+" : "+result.getType();
                    System.err.println(message);
                    fail(message);
                }
            }
		}
	    catch(IOException ioex){
			ioex.printStackTrace();
			fail(ioex.getMessage());
		}
    }
}
