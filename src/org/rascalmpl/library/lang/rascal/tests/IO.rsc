module lang::rascal::tests::IO

import IO;
import ValueIO;

private loc aFile = |home:///rascal-test/wr.txt|;

public test bool writeReadFile(str content) {
  writeFile(aFile, content);
  return readFile(aFile) == content;
}

public test bool writeReadValue(value x) {
  writeTextValueFile(aFile, x);
  y = readTextValueFile(aFile);
  if (x != y) 
    println("<x> != <y> ???");
  
  return x == y;
}

