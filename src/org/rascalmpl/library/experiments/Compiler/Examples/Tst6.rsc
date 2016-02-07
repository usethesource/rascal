module experiments::Compiler::Examples::Tst6
  
import String;

import IO;
import ValueIO;

private loc aFile = |test-temp:///basic-io.txt|;

value main(){
  str content = "Z8UiuN";
  if (size(content) == 0 || content[0] == "\a00") return true;
  writeFile(aFile, content);
  return readFile(aFile) == content;
}
