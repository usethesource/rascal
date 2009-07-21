package test.StandardLibraryTests;

import java.io.File;

import org.junit.Ignore;
import org.junit.Test;

import test.TestFramework;
import static org.junit.Assert.*;

public class ValueIOTests extends TestFramework {

	private boolean binaryWriteRead(String type, String exp){
		boolean success = false;
		try{
			prepare("import ValueIO;");
			prepareMore("writeValueToBinaryFile(\"xxx\", " + exp + ");");
			
			success = runTestInSameEvaluator("{" + type + " N := readValueFromBinaryFile(\"xxx\") && N == " + exp + ";}");
		}finally{
			// Clean up.
			removeTempFile();
		}
		return success;
	}
	
	public void removeTempFile(){
		new File("xxx").delete();
	}
	
	@Test public void binBool() { assertTrue(binaryWriteRead("bool", "true")); }
	
	@Test public void binInt() { assertTrue(binaryWriteRead("int", "1")); }
	
	@Test public void binReal() { assertTrue(binaryWriteRead("real", "2.5")); }
	
	@Test public void binStr1() { assertTrue(binaryWriteRead("str", "\"abc\"")); }
	
	@Test public void binStr2() { assertTrue(binaryWriteRead("str", "\"ab\\nc\"")); }
	
	@Test public void binLoc() { assertTrue(binaryWriteRead("loc",  "loc(file:/home/paulk/pico.trm?offset=0&length=1&begin=2,3&end=4,5)"));	}
	
	@Test public void binList() { assertTrue(binaryWriteRead("list[int]", "[1,2,3]")); }
	
	@Test public void binSet() { assertTrue(binaryWriteRead("set[int]", "{1,2,3}")); }
	
	@Test public void binMap() { assertTrue(binaryWriteRead("map[int,int]", "(1:10,2:20)")); }
	
	@Test public void binTuple() { assertTrue(binaryWriteRead("tuple[int,bool,str]", "<1,true,\"abc\">")); }
	
	@Test public void binADT(){
		try{
			String type = "Bool";
			String exp = "band(bor(btrue,bfalse),band(btrue,btrue))";
			prepare("data Bool = btrue | bfalse | band(Bool left, Bool right) | bor(Bool left, Bool right);");
			prepareMore("import ValueIO;");
			prepareMore("writeValueToBinaryFile(\"xxx\", " + exp + ");");
			assertTrue(runTestInSameEvaluator("{" + type + " N := readValueFromBinaryFile(\"xxx\"); N == " + exp + ";}"));
		}finally{
			// Clean up.
			removeTempFile();
		}
	}
	
	private boolean textWriteRead(String type, String exp){
		boolean success = false;
		try{
			prepare("import ValueIO;");
			prepareMore("writeValueToTextFile(\"xxx\", " + exp + ");");
			
			success = runTestInSameEvaluator("{" + type + " N := readValueFromTextFile(\"xxx\"); N == " + exp + ";}");
		}finally{
			// Clean up.
			removeTempFile();
		}
		return success;
	}
	
	@Test public void textBool() { assertTrue(textWriteRead("bool", "true")); }
	
	@Test public void textInt() { assertTrue(textWriteRead("int", "1")); }
	
	@Test public void textReal() { assertTrue(textWriteRead("real", "2.5")); }
	
	@Ignore @Test public void textStr1() { assertTrue(textWriteRead("str", "\"abc\"")); }
	
	@Ignore @Test public void textStr2() { assertTrue(textWriteRead("str", "\"ab\\nc\"")); }
	
	@Ignore @Test public void textLoc() { assertTrue(textWriteRead("loc",  "loc(file:/home/paulk/pico.trm?offset=0&length=1&begin=2,3&end=4,5)"));	}
	
	@Ignore @Test public void textList() { assertTrue(textWriteRead("list[int]", "[1,2,3]")); }
	
	@Ignore @Test public void textSet() { assertTrue(textWriteRead("set[int]", "{1,2,3}")); }
	
	@Ignore @Test public void textMap() { assertTrue(textWriteRead("map[int,int]", "(1:10,2:20)")); }
	
	@Ignore @Test public void textTuple() { assertTrue(textWriteRead("tuple[int,bool,str]", "<1,true,\"abc\">")); }
	
	@Ignore @Test public void textADT(){
		try{
			String type = "Bool";
			String exp = "band(bor(btrue,bfalse),band(btrue,btrue))";
			prepare("data Bool = btrue | bfalse | band(Bool left, Bool right) | bor(Bool left, Bool right);");
			prepareMore("import ValueIO;");
			prepareMore("writeValueToTextFile(\"xxx\", " + exp + ");");
			assertTrue(runTestInSameEvaluator("{" + type + " N := readValueFromTextFile(\"xxx\"); N == " + exp + ";}"));
		}finally{
			// Clean up.
			removeTempFile();
		}
	}
}
