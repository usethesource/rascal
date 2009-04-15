package test.StandardLibraryTests;

import org.junit.Test;

import test.TestFramework;
import static org.junit.Assert.*;

public class ValueIOTests extends TestFramework {

	@Test
	public void binary(){
		prepare("import ValueIO;");
		prepareMore("writeValueToBinaryFile(\"xxx\", 1);");
		assertTrue(runTestInSameEvaluator("{int N := readValueFromBinaryFile(\"xxx\"); N == 1;}"));
	}
}
