module experiments::vis2::sandbox::Pico
import Prelude;
import ParseTree;
import demo::lang::Pico::Load;
import demo::lang::Pico::Abstract;
import experiments::vis2::sandbox::FigureServer;
import experiments::vis2::sandbox::Figure; 
import ParseTree;

map[str, tuple[int, int]] id2pos = ();
int idx = 0;

str input = 
"begin 
     declare input : natural,  
             output : natural,           
             repnr : natural,
             rep : natural;
     input := 14;
     output := 1;
     while input - 1 do 
          rep := output;
          repnr := input;
          while repnr - 1 do
            output := output + rep;
            repnr := repnr - 1
          od;
          input := input - 1
     od
end"
;


str input2 = "begin
  declare
     x : natural,
     n : natural;
  x := 4;
  n := 10;
  while n do n := n - 1; x := x + x od
end";

void bezoek(PROGRAM n) {
    visit(n) {
        case node v:  println(v);
        }
    }
    
Figure cell(str s, str fontWeight="normal", str fontColor = "black", str fontStyle = "normal", str id ="") = 
         box(size=<80,40>, id = id, fig = text(s
                  ,fontWeight = fontWeight, fontColor = fontColor, fontStyle = fontStyle
       ),fillColor = "antiquewhite", rounded=<10,10>, lineWidth = 0,
         event=on("click", 
          void(str ev, str id, str v){
            if (id2pos[id]?) textProperty("panel", html=
            "\<pre\><substring(input, id2pos[id][0],  id2pos[id][0]+id2pos[id][1])>\</pre\>");
            else 
               textProperty("panel", html="not found:<id>");
            }
          ));
       
Figure nonTerm(str s, str id = "") = cell(s, fontWeight="bold",  fontColor="darkblue", id = id);
      
       
       
Figure decl(str typ, str id, DECL d) = tree(nonTerm("Decl", id = updateNode(d)), [ cell(typ), cell(id, fontColor="darkmagenta", fontStyle="italic")]
);

Figure asg(str id, Figure f, STATEMENT s) = tree(nonTerm("Assign", id = updateNode(s)), [ cell(id, fontColor="darkmagenta",fontStyle = "italic"), f ]
);
    
Figure compileExp(natCon(int N)) = cell("<N>");  /*2*/

Figure compileExp(strCon(str S)) = cell("<S>");

Figure compileExp(id(PicoId Id)) =  cell(Id,fontStyle = "italic", fontColor="darkmagenta" );

Figure compileExp(add(EXP E1, EXP E2)) =    /*3*/
  tree(nonTerm("add"), [compileExp(E1), compileExp(E2)]);

Figure compileExp(sub(EXP E1, EXP E2)) =
   tree(nonTerm("sub"), [compileExp(E1), compileExp(E2)]);

Figure compileExp(conc(EXP E1, EXP E2)) =
  tree(nonTerm("conc"), [compileExp(E1), compileExp(E2)]);
  


// Compile a statement



Figure compileStat(s:asgStat(PicoId Id, EXP Exp)) = asg(Id, compileExp(Exp), s);
	
Figure compileStat(s:ifElseStat(EXP Exp,              /*5*/
                              list[STATEMENT] Stats1,
                              list[STATEMENT] Stats2)){
  return tree(nonterm("ifthenelse", id = updateNode(s)), [compileExp(Exp),  
          compileStats(Stats1),  
          compileStats(Stats2)
         ]);
}

Figure compileStat(s:whileStat(EXP Exp, 
                             list[STATEMENT] Stats1)) {
        return tree(nonTerm("while", id = updateNode(s)), [compileExp(Exp),  
          compileStats(Stats1)       
         ]);
 
}

// Compile a list of statements
Figure compileStats(list[STATEMENT] Stats1) {     /*6*/
  return tree(nonTerm("stats"), [ compileStat(S) | S <- Stats1 ]);
  }
  
// Compile declarations

Figure compileDecls(list[DECL] Decls) =
  tree(nonTerm("Decls"), [ ((d.tp == natural()) ? decl("NAT", d.name, d) : decl("STR", d.name, d))  |      
    d <- Decls
  ]);

// Compile a Pico program

str updateNode(PROGRAM p) {
   str r = "n<idx>";
   idx= idx+1;
   id2pos[r] = <p@location.offset, p@location.length>;
   return r;
   }
   
str updateNode(DECL p) {
   str r = "n<idx>";
   idx= idx+1;
   id2pos[r] = <p@location.offset, p@location.length>;
   return r;
   }
   
str updateNode(EXP p) {
   str r = "n<idx>";
   idx= idx+1;
   id2pos[r] = <p@location.offset, p@location.length>;
   return r;
   }
   
str updateNode(STATEMENT p) {
   str r = "n<idx>";
   idx= idx+1;
   id2pos[r] = <p@location.offset, p@location.length>;
   return r;
   }

public Figure compileProgram(PROGRAM P){
  id2pos = (); idx = 0;
  if(program(list[DECL] Decls, list[STATEMENT] Series) := P){
     return box(fig=tree(nonTerm("Program", id = updateNode(P)), [compileDecls(Decls), compileStats(Series)]
     , pathColor= "green", cityblock = false, fillColor = "none", grow = 0.8, rasterHeight = 1000, 
     orientation = topDown()));
     idx = idx+1;
  } else
    throw "Cannot happen";
}

// public Instrs compileProgram(str txt) = compileProgram(load(txt));
    



public void main() {
    // println(input);
    PROGRAM p = load(input);
    Figure t = compileProgram(p);
    Figure g  = htmlText(
    "\<pre\>\<code\><input>\</pre\>\</code\>" 
    , size=<400, 400>, nl2br = false, id="panel");
    Figure f = hcat(lineWidth = 1, align = topLeft, figs= [g, t], borderWidth =-1, borderStyle="groove", borderColor="");
    // writeFile(|file:///ufs/bertl/html/v.html|, toHtmlString(f));
    // println(id2pos);
    render(f, lineWidth = 0);
   }