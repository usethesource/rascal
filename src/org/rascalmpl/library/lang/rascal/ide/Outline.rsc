@bootstrapParser
module lang::rascal::ide::Outline

import ParseTree;
import lang::rascal::\syntax::Rascal;
import Map;
import List;
import String;
 
anno str node@label;
anno loc node@\loc;
 
data FunctionDeclaration(loc src = |unknown:///|);
 
data Declaration(loc src = |unknown:///|);
 
data Name(loc src = |unknown:///|);
 
data QualifiedName(loc src = |unknown:///|);
 
data Signature(loc src = |unknown:///|);
 
data Prod(loc src = |unknown:///|);

node outline(start[Module] m) = outline(m.top);
 
node outline(Module m) {
   n = "<m.header.name>";
   aliases = [];
   annotations = [];
   functions = ();
   imports = [];
   map[str,list[node]] grammars = ();
   tags = [];
   tests = ();
   map[str,list[node]] adts = ();
   variables = []; 
   list[node] e = [];
   
   top-down-break visit (m) {
     case (Declaration) `<Tags _> <Visibility _> <Type t> <{Variable ","}+ vars>;`:
       variables   += [clean("<v.name> <t>")()[src=v.src] | v <- vars]; 
     case (Declaration) `<Tags _> <Visibility _> anno <Type t> <Type ot>@<Name name>;`:  
       annotations += [clean("<name> <t> <ot>@<name>")()[src=name.src]];
     case (Declaration) `<Tags _> <Visibility _> alias <UserType u> = <Type _>;`:
       aliases += [clean("<u.name>")()[src=u.name.src]];  
     case (Declaration) `<Tags _> <Visibility _> tag <Kind _> <Name name> on <{Type ","}+ _>;`:
       tags += [clean("<name>")()[src=name.src]];
       
     case (Declaration) `<Tags _> <Visibility _> data <UserType u> <CommonKeywordParameters kws>;`: {
       f = "<u.name>";
       c = adts["<u.name>"]?e;
       
       if (kws is present) {
         c += [ ".<k.name> <k.\type>"()[src=k.src] | KeywordFormal k <- kws.keywordFormalList];
       }
       
       adts[f] = c;
     }
     
     case (Declaration) `<Tags _> <Visibility _> data <UserType u> <CommonKeywordParameters kws> = <{Variant "|"}+ variants>;` : {
       f = "<u.name>";
       c = adts[f]?e;
       
       if (kws is present) {
         c += [ ".<k.name> <k.\type>"()[src=k.src] | k <- kws.keywordFormalList];
       }
       
       c += [ clean("<v>")()[src=v.src] | v <- variants];
       
       adts[f] = c;
     }
     
     case FunctionDeclaration func : {
       f = clean("<func.signature.name>")()[label="<func.signature.name> <func.signature.parameters>"][src=func.signature.src];
       
       if (/(FunctionModifier) `test` := func.signature) {
         tests[clean("<func.signature.name>")]?e += [f];
       }
       else {
         functions[clean("<func.signature.name>")]?e += [f];
       }
     }
     
     case (Import) `extend <ImportedModule mm>;` :
       imports += ["<mm.name>"()[src=mm.src]];
       
     case (Import) `import <ImportedModule mm>;` :
       imports += ["<mm.name>"()[src=mm.src]];
       
     case (Import) `import <QualifiedName m2> = <LocationLiteral _>;` :
       imports += ["<m2>"()[src=m2.src]];
       
     case SyntaxDefinition def : {
       f = "<def.defined>";
       c = grammars[f]?e;
       c += ["<p>"()[label="<prefix><p.syms>"][src=p.src] 
                    | /Prod p := def.production, p is labeled || p is unlabeled,
                      str prefix := (p is labeled ? "<p.name>: " : "")
                    ];
       grammars[f] = c;
     }    
   }

   map[node,list[node]] count(map[str,list[node]] m)
     = ((!isEmpty(m[k]) ? "<k> (<size(m[k])>)"()[src=(m[k][0]).src] : "<k> (<size(m[k])>)"()) : m[k] | k <- m);
     
   return n(
      "Functions"(count(functions))[label="Functions (<size(functions)>)"],
      "Tests"(count(tests))[label="Tests (<size(tests)>)"],
      "Variables"(variables)[label="Variables (<size(variables)>)"],
      "Aliases"(aliases)[label="Aliases (<size(aliases)>)"],
      "Data"(count(adts))[label="Data (<size(adts)>)"],
      "Annotations"(annotations)[label="Annotations (<size(annotations)>)"],
      "Tags"(tags)[label="Tags (<size(tags)>)"],
      "Imports"(imports)[label="Imports (<size(imports)>)"],
      "Syntax"(count(grammars))[label="Syntax (<size(grammars)>)"]
   );    
}

// remove leading backslash
str clean(/\\<rest:.*>/) = clean(rest);

// multi-line becomes single line
str clean(str x:/\n/) = clean(visit(x) { case /\n/ => " " });

// cut-off too long
str clean(str x) = clean(x[..239]) when size(x) > 256;

// done
default str clean(str x) = x;
