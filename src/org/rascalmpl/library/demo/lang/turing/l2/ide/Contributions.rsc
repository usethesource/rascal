module demo::lang::turing::l2::ide::Contributions

import ParseTree;
import util::IDE;
import IO;
import demo::lang::turing::l2::cst::Parse;
import demo::lang::turing::l2::ast::Load;
import demo::lang::turing::l2::desugar::Desugar;
import demo::lang::turing::l1::ide::Compile;


public void registerContributions() {
	registerLanguage("Turing L2", "t_l1", Tree (str s, loc l) {
		return demo::lang::turing::l2::cst::Parse::parse(s,l);
	});
	registerContributions("Turing L2", 
		{popup(menu("Turing", [action("Compile", void (Tree t, loc sel) {
			loc target = sel[extension = "ctur"];
			compile(desugar(load(t)), target);
		})]))}
	);
}
