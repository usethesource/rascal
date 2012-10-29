/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.test.library;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;
import org.rascalmpl.test.infrastructure.TestFramework;


public class ValueIOTests extends TestFramework {

	private boolean binaryWriteRead(String type, String exp){
		boolean success = false;
		try{
			prepare("import ValueIO;");
			prepareMore("writeBinaryValueFile(|file://" + System.getProperty("java.io.tmpdir").replace("\\", "/") + "/xxx|, " + exp + ");");
			
			success = runTestInSameEvaluator("{" + type + " N := readBinaryValueFile(|file://" + System.getProperty("java.io.tmpdir").replace("\\", "/") + "/xxx|) && N == " + exp + ";}");
		}finally{
			// Clean up.
			removeTempFile();
		}
		return success;
	}
	
	public void removeTempFile(){
		new File(System.getProperty("java.io.tmpdir").replace("\\", "/") + "/xxx").delete();
	}
	
	@Test public void binBool() { assertTrue(binaryWriteRead("bool", "true")); }
	
	@Test public void binInt() { assertTrue(binaryWriteRead("int", "1")); }
	
	@Test public void binReal() { assertTrue(binaryWriteRead("real", "2.5")); }
	
	@Test public void binStr1() { assertTrue(binaryWriteRead("str", "\"abc\"")); }
	
	@Test public void binStr2() { assertTrue(binaryWriteRead("str", "\"ab\\nc\"")); }
	
	@Test public void binLoc() { assertTrue(binaryWriteRead("loc",  "|file:///home/paulk/pico.trm|(0,1,<2,3>,<4,5>)"));	}
	
	@Test public void binList() { assertTrue(binaryWriteRead("list[int]", "[1,2,3]")); }
	
	@Test public void binSet() { assertTrue(binaryWriteRead("set[int]", "{1,2,3}")); }
	
	@Test public void binMap() { assertTrue(binaryWriteRead("map[int,int]", "(1:10,2:20)")); }
	
	@Test public void binTuple() { assertTrue(binaryWriteRead("tuple[int,bool,str]", "<1,true,\"abc\">")); }
	
	@Test public void binADT(){
		try{
			String type = "Bool";
			String exp = "band(bor(btrue(),bfalse()),band(btrue(),btrue()))";
			prepare("data Bool = btrue() | bfalse() | band(Bool left, Bool right) | bor(Bool left, Bool right);");
			prepareMore("import ValueIO;");
			prepareMore("writeBinaryValueFile(|file://" + System.getProperty("java.io.tmpdir").replace("\\", "/") + "/xxx|, " + exp + ");");
			assertTrue(runTestInSameEvaluator("{" + type + " N := readBinaryValueFile(|file://" + System.getProperty("java.io.tmpdir").replace("\\", "/") + "/xxx|) && N == " + exp + ";}"));
		}finally{
			// Clean up.
			removeTempFile();
		}
	}
	
	@Test public void binParametrizedADT(){
		try{
			prepare("data Maybe[&T] = none() | some(&T t);");
			prepareMore("import ValueIO;");
			prepareMore("writeBinaryValueFile(|file://" + System.getProperty("java.io.tmpdir").replace("\\", "/") + "/xxx|, none());");
			assertTrue(runTestInSameEvaluator("{ Maybe[void] N := readBinaryValueFile(#Maybe[value],|file://" + System.getProperty("java.io.tmpdir").replace("\\", "/") + "/xxx|) && N == none();}"));
			prepareMore("writeBinaryValueFile(|file://" + System.getProperty("java.io.tmpdir").replace("\\", "/") + "/xxx|, some(1));");
			assertTrue(runTestInSameEvaluator("{ Maybe[int] N := readBinaryValueFile(#Maybe[int], |file://" + System.getProperty("java.io.tmpdir").replace("\\", "/") + "/xxx|) && N == some(1);}"));

		}finally{
			// Clean up.
			removeTempFile();
		}
	}
	
	@Test public void binParamAliasInt() {
		try {
			prepare("alias X[&T] = list[&T];");
			prepareMore("import ValueIO;");
			prepareMore("writeBinaryValueFile(|file://" + System.getProperty("java.io.tmpdir").replace("\\", "/") + "/xxx|, [1]);");
			assertTrue(runTestInSameEvaluator("{X[int] N := readBinaryValueFile(#X[int],|file://" + System.getProperty("java.io.tmpdir").replace("\\", "/") + "/xxx|) && N == [1];}"));
	
		}
		finally {
			removeTempFile();
		}
	}
	
	@Test public void binAliasInt() {
		try {
			prepare("alias X = int;");
			prepareMore("import ValueIO;");
			prepareMore("writeBinaryValueFile(|file://" + System.getProperty("java.io.tmpdir").replace("\\", "/") + "/xxx|, 1);");
			assertTrue(runTestInSameEvaluator("{int N := readBinaryValueFile(#X, |file://" + System.getProperty("java.io.tmpdir").replace("\\", "/") + "/xxx|) && N == 1;}"));
		}
		finally {
			removeTempFile();
		}
	}
	private boolean textWriteRead(String type, String exp){
		boolean success = false;
		try{
			prepare("import ValueIO;");
			prepareMore("writeTextValueFile(|file://" + System.getProperty("java.io.tmpdir").replace("\\", "/") + "/xxx|, " + exp + ");");
			
			success = runTestInSameEvaluator("{" + type + " N := readTextValueFile(#" + type + ", |file://" + System.getProperty("java.io.tmpdir").replace("\\", "/") + "/xxx|) && N == " + exp + ";}");
		}finally{
			// Clean up.
			removeTempFile();
		}
		return success;
	}
	
	@Test public void textBool() { assertTrue(textWriteRead("bool", "true")); }
	
	@Test public void textInt() { assertTrue(textWriteRead("int", "1")); }
	
	@Test public void textReal() { assertTrue(textWriteRead("real", "2.5")); }
	
	@Test public void textStr1() { assertTrue(textWriteRead("str", "\"abc\"")); }
	
	@Test public void textStr2() { assertTrue(textWriteRead("str", "\"ab\\nc\"")); }
	
	@Test public void textLoc() { assertTrue(textWriteRead("loc",  "|file:///home/paulk/pico.trm|(0,1,<2,3>,<4,5>)"));	}
	
	@Test public void textList() { assertTrue(textWriteRead("list[int]", "[1,2,3]")); }
	
	@Test public void textSet() { assertTrue(textWriteRead("set[int]", "{1,2,3}")); }
	
	@Test public void textMap() { assertTrue(textWriteRead("map[int,int]", "(1:10,2:20)")); }
	
	@Test public void textTuple() { assertTrue(textWriteRead("tuple[int,bool,str]", "<1,true,\"abc\">")); }
	
	@Test public void textADT(){
		try{
			String type = "Bool";
			String exp = "band(bor(btrue(),bfalse()),band(btrue(),btrue()))";
			prepare("data Bool = btrue() | bfalse() | band(Bool left, Bool right) | bor(Bool left, Bool right);");
			prepareMore("import ValueIO;");
			prepareMore("writeTextValueFile(|file://" + System.getProperty("java.io.tmpdir").replace("\\", "/") + "/xxx|, " + exp + ");");
			assertTrue(runTestInSameEvaluator("{" + type + " N := readTextValueFile(#" + type + ", |file://" + System.getProperty("java.io.tmpdir").replace("\\", "/") + "/xxx|) && N == " + exp + ";}"));
		}finally{
			// Clean up.
			removeTempFile();
		}
	}
}
