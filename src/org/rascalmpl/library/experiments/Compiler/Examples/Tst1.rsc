module experiments::Compiler::Examples::Tst1


//import Exception;
//import Message;
//import ParseTree;
import Grammar;
//import IO;

//public Tree getModuleParseTree(str modulePath) {
//    mloc = getModuleLocation(modulePath);
//    return parseModule(mloc);
//}

@javaClass{org.rascalmpl.library.util.Reflective}
@reflect{Uses Evaluator to get back the grammars imported by \mod}
public java Grammar getModuleGrammar(loc \mod);
//
//@javaClass{org.rascalmpl.library.util.Reflective}
//@reflect{Uses Evaluator to get back the parse tree for the given command}
//public java Tree parseCommand(str command, loc location);
//
//@javaClass{org.rascalmpl.library.util.Reflective}
//@reflect{Uses Evaluator to get back the parse tree for the given commands}
//public java Tree parseCommands(str commands, loc location);
//
//@javaClass{org.rascalmpl.library.util.Reflective}
//@reflect{Uses Evaluator to access the Rascal module parser}
//@doc{This parses a module from a string, in its own evaluator context}
//public java Tree parseModule(str moduleContent, loc location);
//
//@javaClass{org.rascalmpl.library.util.Reflective}
//@reflect{Uses Evaluator to access the Rascal module parser}
//@doc{This parses a module on the search path, and loads it into the current evaluator including all of its imported modules}
//public java Tree parseModule(loc location);
//
//@javaClass{org.rascalmpl.library.util.Reflective}
//@reflect{Uses Evaluator to access the Rascal module parser}
//public java Tree parseModule(loc location, list[loc] searchPath);
//
//@javaClass{org.rascalmpl.library.util.Reflective}
//@reflect{Uses Evaluator to resolve a module name into a source location}
//public java loc getModuleLocation(str modulePath);


//import ParseTree;
//
//layout Whitespace = [\ \t\n]*;
//
//start syntax D = "d";
//start syntax DS = D+;
//
//
//value main(list[value] args) { if( (DS)`d <D+ Xs>` := (DS)`d d`) return (DS)`d <D+ Xs>` == (DS)`d d`; }
