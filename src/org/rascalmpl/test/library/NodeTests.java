/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * 
 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.test.library;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import org.junit.Test;
import org.rascalmpl.test.TestFramework;


public class NodeTests extends TestFramework {
	private final static String TMP_DIR = System.getProperty("java.io.tmpdir") + File.separator;

	@Test
	public void arity() {

		prepare("import Node;").prepareMore(
				"data XNODE = xf() | xf(int) | xf(int,int) | xf(int,int,int);");

		assertTrue(runTestInSameEvaluator("arity(xf()) == 0;"));
		assertTrue(runTestInSameEvaluator("arity(xf(1)) == 1;"));
		assertTrue(runTestInSameEvaluator("arity(xf(1,2)) == 2;"));
	}

	@Test
	public void getChildren() {

		prepare("import Node;").prepareMore(
				"data YNODE = yf() | yf(int) | yf(int,int) | yf(int,int,int);");

		assertTrue(runTestInSameEvaluator("getChildren(yf()) == [];"));
		assertTrue(runTestInSameEvaluator("getChildren(yf(1)) == [1];"));
		assertTrue(runTestInSameEvaluator("getChildren(yf(1,2)) == [1,2];"));
	}

	@Test
	public void getName() {

		prepare("import Node;").prepareMore(
				"data ZNODE = zf() | zf(int) | zf(int,int) | zf(int,int,int);");

		assertTrue(runTestInSameEvaluator("getName(zf()) == \"zf\";"));
		assertTrue(runTestInSameEvaluator("getName(zf(1,2,3)) == \"zf\";"));
	}

	@Test
	public void makeNode() {
		prepare("import Node;");

		assertTrue(runTestInSameEvaluator("{node n = makeNode(\"f\"); getName(n) == \"f\" && arity(n) == 0 && getChildren(n) == []; }"));
		assertTrue(runTestInSameEvaluator("{node n = makeNode(\"f\", 1); getName(n) == \"f\" && arity(n) == 1 && getChildren(n) == [1];}"));
		assertTrue(runTestInSameEvaluator("{node n = makeNode(\"f\", 1, 2); getName(n) == \"f\" && arity(n) == 2 && getChildren(n) == [1,2];}"));
		assertTrue(runTestInSameEvaluator("{node n = makeNode(\"f\", 1, 2, 3); getName(n) == \"f\" && arity(n) == 3 && getChildren(n) == [1,2,3];}"));
	}
	
	private boolean atermWriteRead(String type, String atermString, String dataDefs, String atermValue){
		boolean success = false;
		try{
			PrintStream outStream = new PrintStream(new File(TMP_DIR+"xxx"));
			outStream.print(atermString);
			outStream.close();
			prepare("import Node;");
			prepareMore("import lang::aterm::IO;");
			if(!dataDefs.equals(""))
				prepareMore(dataDefs);
			String fileString = TMP_DIR.replace('\\', '/'); // Windows fix. File.toURI doesn't work properly on windows unfortunately.
			success = runTestInSameEvaluator("{ " + type + " N := readTextATermFile(#" + type + ", |file://"+fileString+"xxx|) && N == " + atermValue + ";}");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			// Clean up.
			removeTempFile();
		}
		return success;
	}
	
	public void removeTempFile(){
		new File(TMP_DIR+"xxx").delete();
	}
	
	@Test
	public void readATermFromFileInt1() {
		assertTrue(atermWriteRead("node", "f(1)", "", "makeNode(\"f\", 1)"));
	}
	
	@Test
	public void readATermFromFileInt2() {
		assertTrue(atermWriteRead("node", "f(1)", "", "\"f\"(1)"));
	}
	
	@Test
	public void readATermFromFileStr1() {
		assertTrue(atermWriteRead("node", "f(\"abc\")", "", "makeNode(\"f\", \"abc\")"));
	}
	
	@Test
	public void readATermFromFileStr2() {
		assertTrue(atermWriteRead("node", "f(\"abc\")", "", "\"f\"(\"abc\")"));
	}
	
	@Test
	public void readATermFromFileList1() {
		assertTrue(atermWriteRead("node", "f([1,2,3])", "", "makeNode(\"f\", [[1,2,3]])"));
	}
	
	@Test
	public void readATermFromFileList2() {
		assertTrue(atermWriteRead("node", "f([1,2,3])", "", "\"f\"([1,2,3])"));
	}
	
	@Test
	public void readATermFromFileFun1() {
		assertTrue(atermWriteRead("node", "f()", "", "makeNode(\"f\")"));
	}
	
	@Test
	public void readATermFromFileFun2() {
		assertTrue(atermWriteRead("node", "f()", "", "\"f\"()"));
	}
	
	@Test
	public void readATermFromFileFunWithArgs1() {
		assertTrue(atermWriteRead("node", "f(1,2,3)", "", "makeNode(\"f\",1,2,3)"));
	}
	
	@Test
	public void readATermFromFileFunWithArgs2() {
		assertTrue(atermWriteRead("node", "f(1,2,3)", "", "\"f\"(1,2,3)"));
	}
	
	@Test
	public void readATermFromFileADT1() {
		assertTrue(atermWriteRead("FUN", "f(1,2,3)", "data FUN = f(int A, int B, int C);", "FUN::f(1,2,3)"));
	}
	
	@Test
	public void readATermFromFileADT3() {
		assertTrue(atermWriteRead("FUN", "f(1,2,3)", "data FUN = f(int A, int B, int C);", "f(1,2,3)"));
	}
	
	@Test
	public void toStringTest() {
		prepare("import Node;");
		assertTrue(runTestInSameEvaluator("{node n = \"f\"(1, 2, 3); toString(n) == \"f(1,2,3)\";}"));
	}

}
