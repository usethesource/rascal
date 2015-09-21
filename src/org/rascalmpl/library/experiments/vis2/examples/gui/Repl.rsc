module experiments::vis2::Repl

import experiments::vis2::Figure;
import experiments::vis2::FigureServer;
import String;

import lang::json::IO;
import IO;
import Type;
import List;

data Cell = code(str txt) | output(Figure fig);

data Repl = repl(list[Cell] cells, str command);

Figure visCell(code(str txt), int i) = hcat(figs=[text("[<i/2>]:"), text(txt, fontStyle="italic")]);
Figure visCell(output(Figure fig), int i) = fig;

list[Figure] visCells(list[Cell] cells) =
	[ box(lineDashing=[1,1,1,1,1], lineColor="lightgray", fillColor="#F6F6F7", align=topLeft, fig=vcat(align=topLeft, figs=[visCell(cells[i], i), visCell(cells[i+1], i+1)])) | i <- [0, 2 .. size(cells)-1]];

Figure visRepl(Repl r) =
	vcat(align=left, gap=<10,10>, 
	     figs= hcat(figs=[image(url=|file:///lib/favicon.ico|), text("Welcome to the Awesome Rascal REPL", fontSize=20) ])
	           + visCells(r.cells) 
	           + hcat(figs=[text("[<size(r.cells)/2>]:"), strInput(event=on("submit", bind(r.command)), size=<250,25>)]));

default Figure visRepl(Repl r) = text("Cannot handle <r>");

Figure eval(str txt){
	switch(txt){
	case /^box\W+<size:[0-9]+>\W+<color:[a-z]+>/: {
			sz = toInt(size);
			return box(size=<sz,sz>, fillColor=color);
			}
	case /^circle\W+<size:[0-9]+>\W+<color:[a-z]+>/: {
			sz = toInt(size);
			return circle(r=sz, cx=sz,cy=sz, fillColor=color);
			}
	case /^<lhs:[0-9]+>\W*\+\W*<rhs:[0-9]+>/:
			return text(toInt(lhs) + toInt(rhs));
	case /^<lhs:[0-9]+>\W*\*\W*<rhs:[0-9]+>/:
			return text(toInt(lhs) * toInt(rhs));
	}
	return text("Cannot evaluate: <txt>", textColor="red");
}

Repl transform(Repl r) {
	println("Enter transform: <r>,");
	println("Enter transform: <r.cells>");
	println("Enter transform: <eval(r.command)>");
	list[Cell] newCells = r.cells + [code(r.command), output(eval(r.command))];
	return repl(newCells, "");
}

void runRepl(){
	r =  repl([], "");
	
	render("repl", #Repl, r, visRepl,  transform);
}

/*
void main(){

	Repl r1 =  repl([output(text("Welcome to the Awesome Rascal REPL"))], "");
	
	jj = toJSON(r1);
	
	Repl r2 = fromJSON(#Repl, jj);
	
	
	println("r1 = <typeOf(r1)> <r1>");
	println("r2 = <typeOf(r2)> <r2>");
	println("jj = <jj>");
	println("r1 == r2: <r1 == r2>");
	println("r1.cells == r2.cells: <r1.cells == r2.cells>");
	println("r1.cells[0] == r2.cells[0]: <r1.cells[0] == r2.cells[0]>");
	c1 = r1.cells[0];
	c2 = r2.cells[0];
	println("c1 := c2: <c1 := c2>");
	println("c1.fig == c2.fig: <c1.fig == c2.fig>");
	fig1 = c1.fig;
	fig2 = c2.fig;
	println("fig1: <typeOf(fig1)> <fig1>");
	println("fig2: <typeOf(fig2)> <fig2>");
	println("typeOf(fig1) == typeOf(fig2):	<typeOf(fig1) == typeOf(fig2)>");
	println("fig1.text == fig2.text: <fig1.text == fig2.text>");
	println("fig1.size == fig2.size: <fig1.size == fig2.size>");
	println("fig1.fontStyle == fig2.fontStyle: <fig1.fontStyle == fig2.fontStyle>");
	println("fig1.align == fig2.align: <fig1.align == fig2.align>");
	println("fig1.fillOpacity == fig2.fillOpacity: <fig1.fillOpacity == fig2.fillOpacity>");
	println("fig1.rounded == fig2.rounded: <fig1.rounded == fig2.rounded>");
	println("fig1.hgap == fig2.hgap: <fig1.hgap == fig2.hgap>");
	println("fig1.lineColor == fig2.lineColor: <fig1.lineColor == fig2.lineColor>");
	println("fig1.fontSize == fig2.fontSize: <fig1.fontSize == fig2.fontSize>");
	println("fig1.lineWidth == fig2.lineWidth: <fig1.lineWidth == fig2.lineWidth>");
	println("fig1.gap == fig2.gap: <fig1.gap == fig2.gap>");
	println("fig1.lineOpacity == fig2.lineOpacity: <fig1.lineOpacity == fig2.lineOpacity>");
	println("fig1.datasets == fig2.datasets: <fig1.datasets == fig2.datasets>");
	println("fig1.lineDashing == fig2.lineDashing: <fig1.lineDashing == fig2.lineDashing>");
	println("fig1.textDecoration == fig2.textDecoration: <fig1.textDecoration == fig2.textDecoration>");
	println("fig1.fillRule == fig2.fillRule: <fig1.fillRule == fig2.fillRule>");
	println("fig1.vgap == fig2.vgap: <fig1.vgap == fig2.vgap>");
	println("fig1.height == fig2.height: <fig1.height == fig2.height>");
	println("fig1.fontWeight == fig2.fontWeight: <fig1.fontWeight == fig2.fontWeight>");
	println("fig1.grow == fig2.grow: <fig1.grow == fig2.grow>");
	
	println("typeOF: fig1.grow == fig2.grow: <typeOf(fig1.grow)>, <typeOf(fig2.grow)>");
	
	println("fig1.fontFamily == fig2.fontFamily: <fig1.fontFamily == fig2.fontFamily>");
	println("fig1.event == fig2.event: <fig1.event == fig2.event>");
	println("fig1.fontName == fig2.fontName: <fig1.fontName == fig2.fontName>");
	
	
	t = text("Welcome to the Awesome Rascal REPL") ==  text("Welcome to the Awesome Rascal REPL");
	println("t: <t>");
	
	println("r1.command == r2.command: <r1.command == r2.command>");
	
	//v1 = visRepl(r1);
	//v2 = visRepl(r2);
	//println("v1 = <v1>");
	//println("v2 = <v2>");
	//println("v1 == v2: <v1 == v2>");
}

*/
