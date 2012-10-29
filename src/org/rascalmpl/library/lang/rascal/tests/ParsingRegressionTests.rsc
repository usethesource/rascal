module lang::rascal::tests::ParsingRegressionTests

import util::Reflective;
import IO;
import util::FileSystem;
import Exception;
import String;
import ParseTree;

public void main() {
	for (/file(f) <- crawl(|std:///|), endsWith(f.path, ".rsc")) {
		try {
			t = parseFullModule(readFile(f), f);
			if (/amb(_) := t) {
				println("Ambiguity found while parsing: <f>");	
			}
		}
		catch ParseError(_) : println("Parsing failed for: <f>");	
		catch Java("Parse error"): println("Parsing failed for: <f>");
		catch RuntimeException e : println("Parsing failed for: <f> error:(<e>)");
	}
}

