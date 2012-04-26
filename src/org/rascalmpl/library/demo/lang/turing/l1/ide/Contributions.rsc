module demo::lang::turing::l1::ide::Contributions

import util::IDE;
import util::SyntaxHighlightingTemplates;
import demo::lang::turing::l1::cst::Parse;


public void registerContributions() {
	registerLanguage("Turing L1", "tur", parse);
}