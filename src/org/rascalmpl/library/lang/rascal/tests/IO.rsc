module lang::rascal::tests::IO

import IO;
import ValueIO;

private loc aFile = |tmp:///rascal-test/wr.txt|;

public test bool writeReadFile(str content) {
  writeFile(aFile, content);
  return readFile(aFile) == content;
}

public test bool writeReadValue(value x) {
  //if (/datetime _ := x) return true; // something is wrong with datetime random generation
  writeTextValueFile(aFile, x);
  return readTextValueFile(aFile) == x;
}

