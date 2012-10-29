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
			fileContent = readFile(f);
			hasConcreteSyntax = /`/ := fileContent;
			Tree t;
			if (hasConcreteSyntax) {
				t = parseFullModule(fileContent, f);
			}
			else {
				t = parseModule(fileContent, f);
			}
			if (/amb(_) := t) {
				if (hasConcreteSyntax) {
					// first try to remove ambiguities around concrete syntax
					t = top-down visit(t) {
						case a:appl(p, [_*, amb(_),_*]) => appl(skipped(), [])
							when /layouts("$QUOTES") := p || /layouts("$BACKTICKS") := p
						case amb(abt) => appl(skipped(), [])
							when /layouts("$QUOTES") := abt || /layouts("$BACKTICKS") := abt
					}
					if (/amb(_) := t) {
						println("Ambiguity found while parsing: <f>");	
					}
					else {
						println("Warning, the file <f> was ambiguous, but it seems located inside the concrete syntax of the other language."); 	
					}
				}
				else {
					println("Ambiguity found while parsing: <f>");	
				}
			}
		}
		catch ParseError(_) : println("Parsing failed for: <f>");	
		catch Java("Parse error"): println("Parsing failed for: <f>");
		catch RuntimeException e : println("Parsing failed for: <f> error:(<e>)");
	}
}

