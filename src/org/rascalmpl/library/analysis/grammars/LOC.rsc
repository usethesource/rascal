@license{
  Copyright (c) 2009-2022 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@synopsis{Generic utilities to compute (S)LOC metrics based on grammars}
@description{
We use this definition to separate lines from: <http://en.wikipedia.org/wiki/Newline>:
 
* LF:    Line Feed, U+000A
* VT:    Vertical Tab, U+000B
* FF:    Form Feed, U+000C
* CR:    Carriage Return, U+000D
* CR+LF: CR (U+000D) followed by LF (U+000A)
* NEL:   Next Line, U+0085
* LS:    Line Separator, U+2028
* PS:    Paragraph Separator, U+2029
}
module analysis::grammars::LOC

import ParseTree;
import List;
import util::FileSystem;
import util::Reflective;

alias Stats = tuple[int total, map[loc file, int sloc] dist];

Stats slocStats(file(loc l), Stats stats)
  = <stats.total + n, stats.dist + (l: n)>
  when l.extension == "rsc",
    int n := countSLOC(parseModule(l));
   

Stats slocStats(directory(loc l, kids), Stats stats)
  = <stats.total + stats2.total, stats.dist + stats2.dist + (l: stats2.total)>
  when stats2 := ( <0, ()> | slocStats(k, it) | k <- kids );

default Stats slocStats(FileSystem _, Stats stats) = stats;

/*
 * Abstract domain to map parsetrees to
 */
data Output
  = newline()
  | stuff()
  ;
  
int countSLOC(Tree t) {
  list[Output] output = [];
  
  // Ignore any layout before or after the main meat.
  if (t.prod.def is \start) {
    t = t.args[1];
  }  
  
  void write(Output x) {
    if (!isEmpty(output) && last(output) == x) {
      return;
    }
    output += [x]; 
  }
  
  void writeLayout(Tree t) {
    if (isComment(t)) {
      return;
    }
    switch (t) {
      case char(c): 
        if (isNewLineChar(c)) {
          write(newline());
        }
      case appl(_, as):
        writeKids(as, writeLayout);

      case amb(ts): 
        // all alts have the same yield
        // so just pick an arbitrary one
        if (x <- ts) writeLayout(x);
      
      default: ;        
    } 
  }
  
  void writeTree(Tree t) { 
    if (isComment(t)) {
      return;
    }
    if (isLayout(t)) { 
      return writeLayout(t);
    }
    switch (t) {
      case char(c):
        write(isNewLineChar(c) ? newline() : stuff());
      
      case appl(_, as):
        writeKids(as, writeTree);
              
      case amb(ts): 
        if (x <- ts) writeTree(x);
      
      default: ;
    }
  }
  
  void writeKids(list[Tree] as, void(Tree) f) {
    i = 0;
    while (i < size(as)) {
      // Interpret CR LF as a single newline
      if (char(a) := as[i], i+1 < size(as),
          char(b) := as[i+1], 
          isCR(a), isLF(b)) {
        write(newline());
      }
       else {
        f(as[i]);
      } 
      i += 1;
    }
  }
 
  writeTree(t);
  return size([ n | n:newline() <- output]) + 1;
}

bool isLayout(appl(prod(\layouts(_), _, _), _)) = true;
bool isLayout(amb({*_, appl(prod(\layouts(_), _, _), _)})) = true;
default bool isLayout(Tree t) = false;

bool isComment(appl(p:prod(_, _, {*_, \tag("category"("Comment"))}), _)) = true;
bool isComment(appl(p:prod(_, _, {*_, \tag("category"("comment"))}), _)) = true;
default bool isComment(Tree _) = false;


 /*
 
 */
 
bool isLF(int c) =  c == 0x000A;
bool isVT(int c) =  c == 0x000B;
bool isFF(int c) =  c == 0x000C;
bool isCR(int c) =  c == 0x000D;
bool isNEL(int c) = c == 0x0085; 
bool isLS(int c) =  c == 0x2028;
bool isPS(int c) =  c == 0x2029;
 
bool isNewLineChar(int c) = any(i <- [isLF, isVT, isFF, isCR, isNEL, isLS, isPS], i(c));
