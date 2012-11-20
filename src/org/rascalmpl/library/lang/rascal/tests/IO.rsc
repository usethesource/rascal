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

public test bool correctlyEncodingUTF8(str content) {
		  writeFileEnc(aFile, "UTF8", content);
  return readFile(aFile) == content;
}
public test bool correctlyEncodingUTF16(str content) {
		  writeFileEnc(aFile, "UTF16", content);
  return readFile(aFile) == content;
}
public test bool correctlyEncodingUTF32(str content) {
		  writeFileEnc(aFile, "UTF32", content);
  return readFile(aFile) == content;
}

public test bool largeFileEncodingCorrectly(str content) {
		  largeContent = (content | it + content | i <-[0..8192]);
		  return correctlyEncodingUTF8(largeContent);
}

data Encoding = utf8() | utf16le() | utf16be() | utf16() | utf32le() | utf32be() | utf32();
map[Encoding, str] encodingNames = (utf8() : "UTF-8", utf16le() : "UTF-16LE",
  utf16be() : "UTF-16BE", utf16(): "UTF-16", utf32le() : "UTF-32LE",
  utf32be() : "UTF-32BE", utf32(): "UTF-32"
);

public test bool appendWorksCorrectly(Encoding enc, str a, str b) {
	  writeFileEnc(aFile, encodingNames[enc], a);
	  appendToFileEnc(aFile, encodingNames[enc], b);
	  return readFile(aFile) == a + b;
}

public test bool appendWorksCorrectlyImplicit(Encoding enc, str a, str b) {
	  writeFileEnc(aFile, encodingNames[enc], a);
	  appendToFile(aFile, b);
	  return readFile(aFile) == a + b;
}

