module lang::rascal::tests::IO

import IO;

public test bool writeReadFile(str content) {
  writeFile(|tmp:///rascal-test/wr.txt|, content);
  return readFile(|tmp:///rascal-test/wr.txt|) == content;
} 