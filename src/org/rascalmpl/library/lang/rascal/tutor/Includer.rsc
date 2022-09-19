
@bootstrapParser
module lang::rascal::tutor::Includer

import lang::rascal::\syntax::Rascal;
import lang::rascal::tutor::Output;
import util::Reflective;
import String;
import Message;

list[Output] prepareModuleForInclusion(str moduleName, bool includeHeaders, bool includeTests, PathConfig pcfg) {
    try {
      moduleLoc = getModuleLocation(trim(moduleName), pcfg);
      start[Module] moduleTree = parseModuleWithSpaces(moduleLoc);
      
      if (!includeTests) {
         moduleTree = visit(moduleTree) {
            case (Module) `<Header h> <Body toplevels>` => (Module) `<Header h>
                                                                    '
                                                                    '<Body filteredToplevels>`
            when filteredToplevels:= removeTests(toplevels)
         } 
      }

      if (!includeHeaders) {
        moduleTree = visit(moduleTree) {
          // TODO: this filters tags of everything, not just the top.
            case Tags _ => (Tags) ``
        }
      }
      
      return [out(l) | str l <- split("\n", "<moduleTree>")];
    }
    catch str notFound: {
      return [err(error(notFound, pcfg.currentFile))];
    }
    catch ParseError(loc f): {
      return [err(error("parse error in included module", f))];
    }
}

Body removeTests((Body) `<Toplevel* begin> <Toplevel elem> <Toplevel* end>`) 
  = (Body) `<Toplevel* begin>
           '<Toplevel* end>` when /(FunctionModifier) `test` := elem
  ;

default Body removeTests(Body b) = b;