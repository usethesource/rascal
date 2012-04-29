module demo::lang::Pico::Pico

import Prelude;
import util::IDE;
import demo::lang::Pico::Abstract;
import demo::lang::Pico::Syntax;
import demo::lang::Pico::Typecheck;
import demo::lang::Pico::Eval;
import demo::lang::Pico::Compile;


private str Pico_NAME = "Pico";
private str Pico_EXT = "pico";

// Parsing
Tree parser(str x, loc l) {
    return parse(#demo::lang::pico::Syntax::Program, x, l);
}

// Type checking

public Program checkPicoProgram(Program x) {
	p = implode(#PROGRAM, x);
	env = checkProgram(p);
	errors = { error(s, l) | <l, s> <- env.errors };
	return x[@messages = errors];
}

// Compiling

public void compilePicoProgram(Program x, loc l){
    p = implode(#PROGRAM, x);
    asm = compileProgram(p);
    cfile = l[extension = "asm"];
    
	writeFile(cfile, intercalate("\n", asm));
}

// Evaluating

public void evalPicoProgram(Program x, loc selection) {
	m = implode(#PROGRAM, x); 
	println(eval(m));
}

// Contributions to IDE

public set[Contribution] Pico_CONTRIBS = {
	popup(
		menu("Pico",[
		    action("Evaluate Pico program", evalPicoProgram),
    		action("Compile Pico to ASM", compilePicoProgram)
	    ])
  	)
};

// Register the Pico tools

public void registerPico() {
  registerLanguage(Pico_NAME, Pico_EXT, parser);
  registerAnnotator(Pico_NAME, checkPicoProgram);
  registerContributions(Pico_NAME, Pico_CONTRIBS);
}   







