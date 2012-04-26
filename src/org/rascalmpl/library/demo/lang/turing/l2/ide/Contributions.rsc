module demo::lang::turing::l2::ide::Contributions

import util::IDE;
import util::SyntaxHighlightingTemplates;
import demo::lang::turing::l2::cst::Parse;


public void registerContributions() {
	registerLanguage("Turing L2", "tur", parse);
}