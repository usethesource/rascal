module lang::rascal::tests::basic::IO
 
import String;

import IO;
import util::UUID;

private loc aFile = |test-temp:///basic-io-<"<uuidi()>">.txt|;

test bool writeReadFile(str content) {
  if (size(content) == 0 || any(c <- [0..size(content)], content[c] == "\a00")) return true;
  writeFile(aFile, content);
  return readFile(aFile) == content;
}

@ignore{not all values can be read in back (such as constructors that turn into nodes)}
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
	if (a != "", b != "", size(a) + size(b) == size(a + b)) {
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
	writeFileEnc(|test-temp:///basic-io-md5.txt|, encodingNames[utf8()], "abc\n123\n!@#$%\n");
	return md5HashFile(|test-temp:///basic-io-md5.txt|) == "931210fcfae2c4979e5d51a264648b82";
}

data Compression 
	= gzip()
	//| Z() // read-only
	| xz()
	| bzip2()
	| zstd()
	//| lzma() //read-only
	;

map[Compression, str] comprExtension
	= (
		gzip() : "gz",
		//Z() : "Z",
		xz() : "xz",
		bzip2() : "bz2",//,
		zstd(): "zst"
		//lzma() : "lzma"	
	);

@tries{100}
test bool compressionWorks(str a, Compression comp) {
	targetFile = aFile[extension = aFile.extension + "." + comprExtension[comp]];
	targetFile = targetFile[scheme = "compressed+" + targetFile.scheme];
	writeFile(targetFile, a);
	return readFile(targetFile) == a;
}

@tries{100}
test bool compressionWorksWithEncoding(str a, Compression comp, Encoding enc) {
	targetFile = aFile[extension = aFile.extension + "." + comprExtension[comp]];
	targetFile = targetFile[scheme = "compressed+" + targetFile.scheme];
	writeFileEnc(targetFile, encodingNames[enc], a);
	return readFileEnc(targetFile, encodingNames[enc]) == a;
}

@expected{PathNotFound}
test bool writeFileOffsetNonExistingFile() {
	writeFile(aFile[file=aFile.file + "invald"][offset=0][length=10], "Foobar");
	return false;
}


@expected{PathNotFound}
test bool writeFileOffsetNonExistingFile2() {
	writeFile(aFile[file=aFile.file + "invald"][offset=200][length=10], "Foobar");
	return false;
}


test bool writeFileOffsetEnd(Encoding enc, str a, str b) {
	a = removeZeroIAmbBOM(enc, a);
	b = removeZeroIAmbBOM(enc, b);
	if (a == "" || b == "") {
		return true;
	}
	writeFileEnc(aFile, encodingNames[enc], a);
	l2 = aFile[offset=size(a)][length=0];
	writeFileEnc(l2, encodingNames[enc], b);
	return readFileEnc(aFile, encodingNames[enc]) == a + b;
}
test bool writeFileOffsetEndInvalidLength(Encoding enc, str a, str b) {
	a = removeZeroIAmbBOM(enc, a);
	b = removeZeroIAmbBOM(enc, b);
	if (a == "" || b == "") {
		return true;
	}
	writeFileEnc(aFile, encodingNames[enc], a);
	l2 = aFile[offset=size(a)][length=10];
	writeFileEnc(l2, encodingNames[enc], b);
	return readFileEnc(aFile, encodingNames[enc]) == a + b;
}

test bool writeFileOffsetEnd2(Encoding enc, str a, str b) {
	a = removeZeroIAmbBOM(enc, a);
	b = removeZeroIAmbBOM(enc, b);
	if (a == "" || b == "") {
		return true;
	}
	writeFileEnc(aFile, encodingNames[enc], a + b);
	l2 = aFile[offset=size(a)][length=size(b)];
	writeFileEnc(l2, encodingNames[enc], b);
	return readFileEnc(aFile, encodingNames[enc]) == a + b;
}

test bool writeFileOffsetMiddle(Encoding enc, str a, str b) {
	a = removeZeroIAmbBOM(enc, a);
	b = removeZeroIAmbBOM(enc, b);
	if (a == "" || b == "") {
		return true;
	}
	writeFileEnc(aFile, encodingNames[enc], a+a);
	l2 = aFile[offset=size(a)][length=size(a)];
	writeFileEnc(l2, encodingNames[enc], b);
	return readFileEnc(aFile, encodingNames[enc]) == a + b;
}

test bool writeFileOffsetMiddle2(Encoding enc, str a, str b) {
	a = removeZeroIAmbBOM(enc, a);
	b = removeZeroIAmbBOM(enc, b);
	if (a == "" || b == "") {
		return true;
	}
	writeFileEnc(aFile, encodingNames[enc], a+a);
	l2 = aFile[offset=size(a)][length=0];
	writeFileEnc(l2, encodingNames[enc], b);
	return readFileEnc(aFile, encodingNames[enc]) == a + b + a;
}


test bool writeFileOffsetStart(Encoding enc, str a, str b) {
	a = removeZeroIAmbBOM(enc, a);
	b = removeZeroIAmbBOM(enc, b);
	if (a == "" || b == "") {
		return true;
	}
	writeFileEnc(aFile, encodingNames[enc], a+a);
	l2 = aFile[offset=0][length=size(a)];
	writeFileEnc(l2, encodingNames[enc], b);
	return readFileEnc(aFile, encodingNames[enc]) == b + a;
}


test bool md5ValueTest() = md5Hash("test") == "303b5c8988601647873b4ffd247d83cb"; // "test" as md5sum (yes, nested quotes)

test bool md5FileTest() {
	writeFile(aFile, "test");
	return md5HashFile(aFile) == "098f6bcd4621d373cade4e832627b4f6"; // test as md5sum
}

