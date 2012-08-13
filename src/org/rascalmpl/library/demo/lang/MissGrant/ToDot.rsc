module demo::lang::MissGrant::ToDot
import demo::lang::MissGrant::ToRelation;
import demo::lang::MissGrant::AST;
import lang::dot::Dot;
import IO;
import Set;

str nodeLabel(ActionRel s, str n) {
  str r="{<n><for (c<-s[n]) {> |<c><}>}";
  return r;
}

DotGraph toDot(Controller c, str top) {
    TransRel r = transRel(c);
    ActionRel s = commands(c);
    Stms nodes = [N(n, [<"shape", "record">,<"label","\"<nodeLabel(s,n)>\"">]) | n<-definedStates(c), !isEmpty(s[n])];
    Stms edges =   [ E(f, t, [<"label", a>]) | <str f, str a, str t> <-r]; 
     return digraph("missgrant", 
      [NODE( [<"style","filled">, <"fillcolor","cornsilk">,<"fontcolor","blue">,<"shape","ellipse">])] 
         +[S([N(top), A("rank","source")])]+nodes+edges); 
}


public void toDot(Controller c, str top, loc lc) {
    DotGraph d = toDot(c, top);
    writeFile(lc, toString(d));
    }


