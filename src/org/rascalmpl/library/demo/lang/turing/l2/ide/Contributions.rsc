module demo::lang::turing::l2::ide::Contributions

import ParseTree;
import util::IDE;
import IO;
import lang::box::util::Box2Text;

import demo::lang::turing::l2::cst::Parse;
import demo::lang::turing::l2::ast::Load;
import demo::lang::turing::l2::check::Check;
import demo::lang::turing::l2::format::Format;
import demo::lang::turing::l2::ide::Outline;
import demo::lang::turing::l2::desugar::Desugar;
import demo::lang::turing::l1::ide::Compile;
import demo::lang::turing::l1::vis::TuringVisualisation;


public void registerContributions() {
	registerLanguage("Turing L2", "t_l2", Tree (str s, loc l) {
		return demo::lang::turing::l2::cst::Parse::parse(s,l);
	});
	registerContributions("Turing L2", 
		{annotator(Tree (Tree t) {
		  return t[@messages=check(load(t))];
		}),
		outliner(node (Tree t) {
		  return turing2outline(load(t));
		}),
		popup(menu("Turing", [
				action("Compile", void (Tree t, loc sel) {
					loc target = sel[extension = "ctur"];
					compile(desugar(load(t)), target);
				}),
				action("Emulate", void (Tree t, loc sel) {
					visInterpreter(desugar(load(t)));	
				}),
				edit("Format", str (Tree t, loc s) {
				  return format(turing2box(load(t)));
				})
			]))}
	);
}
