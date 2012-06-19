module demo::lang::Pico::Pico

import Prelude;
import util::IDE;

import vis::Figure;
import vis::Render;

import demo::lang::Pico::Abstract;
import demo::lang::Pico::Syntax;
import demo::lang::Pico::Typecheck;
import demo::lang::Pico::Eval;
import demo::lang::Pico::Compile;
import demo::lang::Pico::ControlFlow;
import demo::lang::Pico::Uninit;
import demo::lang::Pico::Visualize;


private str Pico_NAME = "Pico";
private str Pico_EXT = "pico";

// Parsing
Tree parser(str x, loc l) {
    return parse(#Program, x, l);
}

// Type checking

public Program checkPicoProgram(Program x) {
	p = implode(#PROGRAM, x);
	env = checkProgram(p);
	errors = { error(s, l) | <l, s> <- env.errors };
	if(!isEmpty(errors))
		return x[@messages = errors];
    ids = uninitProgram(p);
	warnings = { warning("Variable <v> maybe initialized", l) | <loc l, str v> <- ids };
	return x[@messages = warnings];
}

// Compiling

public void compilePicoProgram(Program x, loc l){
    p = implode(#PROGRAM, x);
    asm = compileProgram(p);
	text(asm);
}

// Evaluating

public void evalPicoProgram(Program x, loc selection) {
	m = implode(#PROGRAM, x); 
	text(evalProgram(m));
}

// Visualize control flow graph

public void visualizePicoProgram(Program x, loc selection) {
	m = implode(#PROGRAM, x); 
	CFG = cflowProgram(m);
	render(visCFG(CFG.graph));
}
	
// Contributions to IDE

public set[Contribution] Pico_CONTRIBS = {
	popup(
		menu("Pico",[
		    action("Evaluate Pico program", evalPicoProgram),
    		action("Compile Pico to ASM", compilePicoProgram),
    		action("Show Control flow graph", visualizePicoProgram)
	    ])
  	)
};

// Register the Pico tools

public void registerPico() {
  registerLanguage(Pico_NAME, Pico_EXT, parser);
  registerAnnotator(Pico_NAME, checkPicoProgram);
  
  registerContributions(Pico_NAME, Pico_CONTRIBS);
}   







