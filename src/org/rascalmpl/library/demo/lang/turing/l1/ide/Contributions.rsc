module demo::lang::turing::l1::ide::Contributions

import ParseTree;
import util::IDE;
import IO;
import demo::lang::turing::l1::cst::Parse;
import demo::lang::turing::l1::ast::Load;
import demo::lang::turing::l1::ide::Compile;


public void registerContributions() {
	registerLanguage("Turing L1", "tur", Tree (str s, loc l) {
		return demo::lang::turing::l1::cst::Parse::parse(s,l);
	});
	registerContributions("Turing L1", 
		{popup(menu("Turing", [action("Compile", void (Tree t, loc sel) {
			loc target = sel[extension = sel.extension + ".ctur"];
			compile(load(t), target);
		})]))}
	);
}
