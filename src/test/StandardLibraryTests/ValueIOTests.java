package test.StandardLibraryTests;

import org.junit.Test;

import test.TestFramework;
import static org.junit.Assert.*;

public class ValueIOTests extends TestFramework {

	private boolean binaryWriteRead(String type, String exp){
		prepare("import ValueIO;");
		prepareMore("writeValueToBinaryFile(\"xxx\", " + exp + ");");
		return runTestInSameEvaluator("{" + type + " N := readValueFromBinaryFile(\"xxx\"); N == " + exp + ";}");
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
		String type = "Bool";
		String exp = "band(bor(btrue,bfalse),band(btrue,btrue))";
		prepare("data Bool = btrue | bfalse | band(Bool left, Bool right) | bor(Bool left, Bool right);");
		prepareMore("import ValueIO;");
		prepareMore("writeValueToBinaryFile(\"xxx\", " + exp + ");");
		assertTrue(runTestInSameEvaluator("{" + type + " N := readValueFromBinaryFile(\"xxx\"); N == " + exp + ";}"));
	}
	
	private boolean textWriteRead(String type, String exp){
		prepare("import ValueIO;");
		prepareMore("writeValueToTextFile(\"xxx\", " + exp + ");");
		return runTestInSameEvaluator("{" + type + " N := readValueFromTextFile(\"xxx\"); N == " + exp + ";}");
	}
	
	@Test public void textBool() { assertTrue(textWriteRead("bool", "true")); }
	
	@Test public void textInt() { assertTrue(textWriteRead("int", "1")); }
	
	@Test public void textReal() { assertTrue(textWriteRead("real", "2.5")); }
	
	@Test public void textStr1() { assertTrue(textWriteRead("str", "\"abc\"")); }
	
	@Test public void textStr2() { assertTrue(textWriteRead("str", "\"ab\\nc\"")); }
	
	@Test public void textLoc() { assertTrue(textWriteRead("loc",  "loc(file:/home/paulk/pico.trm?offset=0&length=1&begin=2,3&end=4,5)"));	}
	
	@Test public void textList() { assertTrue(textWriteRead("list[int]", "[1,2,3]")); }
	
	@Test public void textSet() { assertTrue(textWriteRead("set[int]", "{1,2,3}")); }
	
	@Test public void textMap() { assertTrue(textWriteRead("map[int,int]", "(1:10,2:20)")); }
	
	@Test public void textTuple() { assertTrue(textWriteRead("tuple[int,bool,str]", "<1,true,\"abc\">")); }
	
	@Test public void textADT(){
		String type = "Bool";
		String exp = "band(bor(btrue,bfalse),band(btrue,btrue))";
		prepare("data Bool = btrue | bfalse | band(Bool left, Bool right) | bor(Bool left, Bool right);");
		prepareMore("import ValueIO;");
		prepareMore("writeValueToTextFile(\"xxx\", " + exp + ");");
		assertTrue(runTestInSameEvaluator("{" + type + " N := readValueFromTextFile(\"xxx\"); N == " + exp + ";}"));
	}
}
