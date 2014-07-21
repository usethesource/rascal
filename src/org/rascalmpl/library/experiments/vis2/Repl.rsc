module experiments::vis2::Repl

import experiments::vis2::Figure;
import experiments::vis2::FigureServer;
import String;

import lang::json::IO;
import IO;

data Cell = code(str txt) | output(Figure fig);

data Repl = repl(list[Cell] cells, str command);

Figure visCell(code(str txt)) = text(txt);
Figure visCell(output(Figure fig)) = fig;

Figure visRepl(Repl r) =
	vcat(align=left, figs= [visCell(c) | c <- r.cells] + 
						   [strInput(event=on("submit", bind(r.command)), size=<215,25>)]);

default Figure visRepl(Repl r) = text("Cannot handle <r>");

Figure eval(str txt){
	switch(txt){
	case /^box <size:[0-9]+> <color:[a-z]+>/: {
			sz = toInt(size);
			return box(size=<sz,sz>, fillColor=color);
			}
	case /^<lhs:[0-9]+>\+<rhs:[0-9]+>/:
			return text(toInt(lhs) + toInt(rhs));
	}
	return text("Cannot evaluate: <txt>");
}

Repl transform(Repl r) = repl(r.cells + [text(r.command), eval(r.command)], "");

void runRepl(){
	r =  repl([output(text("Welcome to the Awesome Rascal REPL"))], "");
	
	render("repl", #Repl, r, visRepl,  transform);
}

void main(list[value] args){
	r1 =  repl([output(text("Welcome to the Awesome Rascal REPL"))], "");
	
	v1 = visRepl(r1);
	
	jj = toJSON(r1);
	
	Repl r2 = fromJSON(#Repl, jj);
	
	v2 = visRepl(r2);
	
	println("r1 = <r1>");
	println("r2 = <r2>");
	println("jj = <jj>");
	println("r1 == r2: <r1 == r2>");
	
	//println("v1 = <v1>");
	//println("v2 = <v2>");
	//println("v1 == v2: <v1 == v2>");
}
