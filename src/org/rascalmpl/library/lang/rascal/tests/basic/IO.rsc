module lang::rascal::tests::basic::IO

import String;

import IO;
import ValueIO;

private loc aFile = |tmp:///rascal-test/wr.txt|;

test bool writeReadFile(str content) {
  if (size(content) == 0 || content[0] == "\a00") return true;
  writeFile(aFile, content);
  return readFile(aFile) == content;
}

test bool writeReadValue(value x) {
  writeTextValueFile(aFile, x);
  y = readTextValueFile(aFile);
  if (x != y) 
    println("<x> != <y> ???");
  
  return x == y;
}

data Encoding = utf8() | utf16le() | utf16be() | utf16() | utf32le() | utf32be() | utf32();
map[Encoding, str] encodingNames = (utf8() : "UTF-8", utf16le() : "UTF-16LE",
  utf16be() : "UTF-16BE", utf16(): "UTF-16", utf32le() : "UTF-32LE",
  utf32be() : "UTF-32BE", utf32(): "UTF-32"
);

test bool correctEncoding(Encoding enc, str content) {
	content = removeZeroIAmbBOM(enc, content);
		  writeFileEnc(aFile, encodingNames[enc], content);
  return readFileEnc(aFile, encodingNames[enc]) == content;
}

test bool correctEncodingImplicit(Encoding enc, str content) {
	content = removeZeroIAmbBOM(enc, content);
		  writeFileEnc(aFile, encodingNames[enc], content);
  return readFile(aFile) == content;
}

public str removeZeroIAmbBOM(Encoding enc, str s) {
	if (size(s)> 0 && s[0] == "\a00" && (enc == utf16() || enc == utf16le())) {
		return "\a01" + (size(s) > 1 ? s[1..] : "");
	}
	return s;
}

test bool appendWorksCorrectly(Encoding enc, str a, str b) {
	a = removeZeroIAmbBOM(enc, a);
	b = removeZeroIAmbBOM(enc, b);
	  writeFileEnc(aFile, encodingNames[enc], a);
	  appendToFileEnc(aFile, encodingNames[enc], b);
	  return readFile(aFile) == a + b;
}

test bool appendWorksCorrectlyImplicit(Encoding enc, str a, str b) {
	a = removeZeroIAmbBOM(enc, a);
	b = removeZeroIAmbBOM(enc, b);
	  writeFileEnc(aFile, encodingNames[enc], a);
	  appendToFile(aFile, b);
	  return readFile(aFile) == a + b;
}

test bool readOffsetStart(str a, str b) {
	if (size(a) + size(b) == size(a + b)) {
		writeFileEnc(aFile, "UTF8", a + b);
		return readFileEnc(aFile[offset=0][length=size(a)], "utf8") == a;
	}
	return true;
}

test bool readOffsetEnd(str a, str b) {
	if (size(a) + size(b) == size(a + b)) {
		writeFileEnc(aFile, "UTF8", a + b);
		return readFileEnc(aFile[offset=size(a)][length=size(b)], "utf8") == b;
	}
	return true;
}

test bool readOffsetMiddle(str a, str b, str c) {
	if (size(a) == 0 || size(b) == 0) return true;
	if (size(a) + size(b) + size(c) == size(a + b + c)) {
		writeFileEnc(aFile, "UTF8", a + b + c);
		return readFileEnc(aFile[offset=size(a)][length=size(b)], "utf8") == b;
	}
	return true;
}

test bool md5Hash(){
	writeFileEnc(|home:///wr.txt|, encodingNames[utf8()], "abc\n123\n!@#$%\n");
	return md5HashFile(|home:///wr.txt|) == "931210fcfae2c4979e5d51a264648b82";
}
