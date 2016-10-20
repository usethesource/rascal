module experiments::tutor3::QuestionCompiler

import ParseTree;
import IO;
import String;
import util::Math;
import List;
import Set;
import String;
import experiments::Compiler::Compile;
import experiments::Compiler::Execute;
import util::SystemAPI;
import IO;
import util::Reflective;
import DateTime;

import Ambiguity;

// Syntax of the Question language

lexical LAYOUT = [\ \t \n];

layout Layout = LAYOUT* !>> LAYOUT;

keyword Reserved
    = "question"
    | "prep"
    | "expr"
    | "end"
    ;

keyword TypeNames
    = "bool"
    | "int"
    | "real"
    | "num"
    | "str"
    | "loc"
    | "datetime"
    | "list"
    | "set"
    | "map"
    | "tuple"
    | "rel"
    | "lrel"
    | "value"
    | "void"
    | "arb"
    ;

lexical InsName = ([A-Z a-z] [A-Z a-z 0-9 _]* !>> [A-Z a-z 0-9 _]) \ TypeNames;

lexical Name = [A-Z a-z] [A-Z a-z 0-9 _]* !>> [A-Z a-z 0-9 _];

lexical IntCon = "-"?[0-9]+ !>> [0-9];

lexical HoleContents
    = "\\" !<< "{" ( ![{}] | ("\\" [{}]) | HoleContents)* parts "\\" !<< "}";
    
lexical HoleCmd 
    = "?" HoleContents contents
    ;
    
lexical NonInsertText
    = ![!{} \ \n \t]
    ;

syntax EvalCmd
    = "%{" EvalElem+ elements "}"
    ;

syntax EvalElem
    = insert_cmd: InsertCmd insertCmd
    | non_insert_cmd: NonInsertText
    ;
    
syntax InsertCmd = "!{" Insert insertDetails "}";

syntax Insert =
         InsName name
       | InsName name ":" Type type
       | Type type
       ;
       
syntax Range = "[" IntCon min "," IntCon max "]" range;

syntax Type 
        = "bool" 
        | "int"
        | "int" Range range
        | "real"
        | "real" Range range
        | "num"
        | "num" Range range
        | "str"
        | "loc"
        | "datetime"
        | "list" "[" Type elemType "]"
        | "list" "[" Type elemType "]" Range range
        | "set" "[" Type elemType "]"
        | "set" "[" Type elemType "]" Range range
        | "map" "[" Type keyType "," Type valType "]"
        | "map" "[" Type keyType "," Type valType "]" Range range
        | "tuple" "[" {Type ","}+ elemTypes "]"
        | "tuple" "[" {Type ","}+ elemTypes "]" Range range
        | "rel" "[" {Type ","}+ elemTypes "]"
        | "lrel" "[" {Type ","}+ elemTypes "]"
        | "value"
        | "void"
        | "arb" "[" IntCon depth ","  {Type ","}+ elemTypes  "]"
        ;

lexical Op = [? % !];

lexical AlphaNum = [a-z A-Z 0-9 _];

lexical NonOp = !([? % !] || [a-z A-Z 0-9 _] || [\ \t \n]);

lexical AnyText
    = NonOp+ !>> !([? % !] || [a-z A-Z 0-9 _] || [\ \t \n])
   // = NonOp+ !>> NonOp    // Should be the same but does not work.
    | Op !>> "{"
    | AlphaNum+ !>> [a-z A-Z 0-9 _]
    ;
 
lexical AnyButReserved = AnyText \ Reserved; 

syntax QuestionPart = (HoleCmd | InsertCmd | EvalCmd | AnyButReserved)+;

syntax Question = "question" QuestionPart text Body body "end";

syntax Body 
    = Prep prep
    | Prep prep Expr expr
    | Expr expr
    ;

syntax Text = "text" ":" QuestionPart text;

syntax Prep = "prep" ":" QuestionPart text;

syntax Expr = "expr" Name name ":" QuestionPart text;

value main1(){
    q = parse(#Question, "question Replace _ by the value of the given expression and make the test true:
            'prep: lrel[str country, int year, int amount] GDP = [ ];
            'expr relSubscript: GDP[\"US\",2008]
            'end"); 
    //println(diagnose(parse(#QuestionPart, "[]]", allowAmbiguity=true)));
    //q = parse(#QuestionPart, "= [ ];"); 
    //println(q.body is expr);
   return true;
}

// Type generation

Type generateType(Type tp){
    return
        visit(tp){
           case (Type) `arb[<IntCon depth>,<{Type ","}+ elemTypes>]` =>
            generateArbType(toInt("<depth>"), elemTypes)
        };
}

Type generateArbType(int n, {Type ","}+ prefs){
   //println("generateArbType: <n>, <prefs>");
   if(n <= 0){
       lprefs = [pref | pref <- prefs];
       return getOneFrom(lprefs);
      // We want: return getOneFrom(prefs);
   }
     
   switch(arbInt(6)){
     case 0: { elemType = generateArbType(n-1, prefs); return (Type) `list[<Type elemType>]`; }
     case 1: { elemType = generateArbType(n-1, prefs); return (Type) `set[<Type elemType>]`; }
     case 2: { keyType = generateArbType(n-1, prefs);
               valType = generateArbType(n-1, prefs); 
               return (Type) `map[<Type keyType>,<Type valType>]`;
               }
     case 3: return generateArbTupleType(n-1, prefs);
     case 4: return generateArbRelType(n-1, prefs);
     case 5: return generateArbLRelType(n-1, prefs);
   }
} 

int size({Type ","}+ ets) = size([et | et <- ets]);

Type makeTupleType({Type ","}+ ets) =  [Type] "tuple[<intercalate(",", [et | et <- ets])>]";

Type generateArbTupleType(int n, {Type ","}+ prefs){
   arity = 1 + arbInt(5);
   return [Type] "tuple[<intercalate(",", [generateArbType(n-1, prefs) | int i <- [0 .. 1+arbInt(5)] ])>]";
}

Type generateArbRelType(int n, {Type ","}+ prefs){
   return [Type] "rel[<intercalate(",", [generateArbType(n - 1, prefs) | int i <- [0 .. 1+arbInt(5)] ])>]";
}

Type generateArbLRelType(int n, {Type ","}+ prefs){
   return [Type] "lrel[<intercalate(",", [generateArbType(n - 1, prefs) | int i <- [0 .. 1+arbInt(5)] ])>]";
}

// Value generation

public str generateValue(Type tp){
     //println("generateValue(<tp>, <env>)");
     switch(tp){
        case (Type) `bool`:          
            return generateBool();
            
        case (Type) `int`:      
            return generateInt(-100,100);
            
        case (Type) `int[<IntCon f>,<IntCon t>]`:      
            return generateInt(toInt("<f>"), toInt("<t>"));
            
        case (Type) `real`:      
            return generateReal(-100,100);
            
        case (Type) `real[<IntCon f>,<IntCon t>]`:      
            return generateReal(toInt("<f>"), toInt("<t>"));
            
        case (Type) `num`:      
            return generateNum(-100,100);
            
        case (Type) `num[<IntCon f>,<IntCon t>]`:      
            return generateNum(toInt("<f>"), toInt("<t>"));
       
       case (Type) `str`:
            return generateString();
            
       case (Type) `loc`:
            return generateLoc();
            
       case (Type) `datetime`:            
            return generateDateTime();
            
       case (Type) `list[<Type et>]`: 
            return generateList(et);
            
       case (Type) `list[<Type et>][<IntCon f>,<IntCon t>]`: 
            return generateList(et, from=toInt("<f>"), to=toInt("<t>"));
            
       case (Type) `set[<Type et>]`: 
            return generateSet(et);
            
       case (Type) `set[<Type et>] [<IntCon f>,<IntCon t>]`: 
            return generateSet(et, from=toInt("<f>"), to=toInt("<t>"));
            
       case (Type) `map[<Type kt>,<Type vt>]`:           
            return generateMap(kt, vt);
       
       case (Type) `map[<Type kt>,<Type vt>][<IntCon f>,<IntCon t>]`:           
            return generateMap(kt, vt, from=toInt("<f>"), to=toInt("<t>"));
       
       case (Type) `tuple[<{Type ","}+ ets>]`:   
            return generateTuple(ets);
            
       case (Type) `tuple[<{Type ","}+ ets>][<IntCon f>,<IntCon t>]`:   
            return generateTuple(ets, from=toInt("<f>"), to=toInt("<t>"));
            
       case (Type) `rel[<{Type ","}+ ets>]`: 
            return generateSet(makeTupleType(ets));
            
       case (Type) `lrel[<{Type ","}+ ets>]`: 
            return generateList(makeTupleType(ets));
            
       case (Type) `value`:               
            return generateArb(0, baseTypes);
     }
     throw "Unknown type: <tp>";
}

set[set[str]] vocabularies =
{
// Letters:
{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P",
"Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"},

// Sesame Street:
{"Bert", "Ernie", "Big Bird", "Cookie Monster", "Grover", "Elmo",
"Slimey the Worm", "Telly Monster", "Count Von Count", "Countess Darling Von Darling",
"Countess Von Backwards", "Guy Smiley", "Barkley the Dog", "Little Bird",
"Forgetful Jones", "Bruno", "Hoots The Owl", "Prince Charming",
"Mumford the Magician", "Two-Headed Monster"},

// Star trek

// Dr Who

// Star Wars:
{"Jar Jar Binks", "C-3PO", "E-3PO", "Boba Fett", "Isolder", "Jabba the Hut",
"Luke Skywalker", "Obi-Wan Kenobi", "Darth Sidious", "Pincess Leia",
"Emperor Palpatine", "R2-D2", "Admiral Sarn", "Boba Fett", 
"Anakin Skywalker", "Sy Snootles", "Han Solo", "Tibor", "Darth Vader",
"Ailyn Vel", "Yoda", "Zekk", "Joh Yowza"},

// Game of Thrones:
{"Tyrion", "Cersei", "Daenerys", "Petyr", "Jorah", "Sansa", "Arya",
 "Theon", "Bran", "Sandor", "Joffrey", "Catelyn", "Robb", "Ned",
 "Viserys", "Khal", "Varys", "Samwell", "Bronn", "Tywin", "Shae",
 "Jeor", "Gendry", "Tommen", "Jaqen", "Davos", "Melisandre",
 "Margaery", "Stannis", "Ygritte", "Talisa", "Brienne", "Gilly",
 "Roose", "Tormund", "Ramsay", "Daario", "Missandei", "Eilaria",
 "The High Sparrow"},
 
 // World of Warcraft:

 {"Archimonde", "Kil\'jaeden", "Mannoroth", "Ner\'zhul", "Sargeras",
 "Balnazzar", "Magtheridon", "Mal\'Ganis", "Tichondrius", "Varimathras",
 "Azgalor", "Hakkar", "Kazzak", "Detheroc", "Akama", "Velen", 
 "Alexstrasza", "Malygos", "Neltharion", "Nozdormu", "Ysera",
 "Korialstrasz", "Kalecgos", "Brann", "Muradin"},
 
// Fruits:
{"Blackcurrant", "Redcurrant", "Gooseberry", "Eggplant", "Guava",
"Pomegranate", "Kiwifruit", "Grape", "Cranberry", "Blueberry",  "Pumpkin",
"Melon", "Orange", "Lemon", "Lime", "Grapefruit", "Blackberry",
 "Raspberry", "Pineapple", "Fig", "Apple", "Strawberry",
 "Mandarin", "Coconut", "Date", "Prune", "Lychee", "Mango", "Papaya",
 "Watermelon"},

// Herbs and spices:
{"Anise", "Basil", "Musterd", "Nutmeg", "Cardamon", "Cayenne", 
"Celery", "Pepper", "Cinnamon", "Coriander", "Cumin",
"Curry", "Dill", "Garlic", "Ginger", "Jasmine",
"Lavender", "Mint", "Oregano", "Parsley", "Peppermint",
"Rosemary", "Saffron", "Sage", "Sesame", "Thyme",
"Vanilla", "Wasabi"}

};      
       
public str generateBool(){
   return toString(arbBool());
}
 
 public str generateInt(int from, int to){
   return toString(from + arbInt(to - from));
}
 
 public str generateReal(int from, int to){
   return toString(from + arbReal() * (to - from));
}

public str generateNumber(int from, int to){
   return (arbBool()) ? generateInt(from, to) : generateReal(from, to);
}

public str generateLoc(){
  return "|file:///home/paulk/pico.trm|(0,1,\<2,3\>,\<4,5\>)";
   /*
   scheme = getOneFrom(["http", "file", "stdlib", "cwd"]);
   authority = "auth";
   host = "www.cwi.nl";
   port = "80";
   path = "/a/b";

uri: the URI of the location. Also subfields of the URI can be accessed:

scheme: the scheme (or protocol) like http or file. Also supported is cwd: for current working directory (the directory from which Rascal was started).

authority: the domain where the data are located.

host: the host where the URI is hosted (part of authority).

port: port on host (part ofauthority).

path: path name of file on host.

extension: file name extension.

query: query data

fragment: the fragment name following the path name and query data.

user: user info (only present in schemes like mailto).

offset: start of text area.

length: length of text area

begin.line, begin.column: begin line and column of text area.

end.line, end.column end line and column of text area.
*/
}

public str generateDateTime(){
   year = 1990 + arbInt(150);
   month = 1 + arbInt(11);
   day = 1 + arbInt(6);
   dt1 = createDate(year, month, day);
   if(arbBool())
      return "<dt1>";
   hour = arbInt(24);
   minute = arbInt(60);
   second = arbInt(60);
   milli = arbInt(1000);
   dt2 = createTime(hour, minute, second, milli);
  return "<joinDateAndTime(dt1, dt2)>";
}

public str generateString(){
  vocabulary = getOneFrom(vocabularies);
  return "\"<getOneFrom(vocabulary)>\"";
}

public str generateList(Type et, int from=0, int to=5){
   n = from;
   if(from < to)
      n = from + arbInt(to - from + 1);
   elms = [];
   while(size(elms) < n){
        elms += generateValue(et);
   }
   return "[<intercalate(", ", elms)>]";
}

public str generateSet(Type et, int from=0, int to=5){
   n = from;
   if(from < to)
      n = from + arbInt(to - from + 1);
   elms = [];
   attempt = 0;
   while(size(elms) < n && attempt < 100){
     attempt += 1;
     elm = generateValue(et);
     if(elm notin elms)
        elms += elm;
   }
   return "{<intercalate(", ", elms)>}";
}

public str generateMap(Type kt, Type vt){
   keys = { generateValue(kt, env) | int i <- [0 .. arbInt(5)] }; // ensures unique keys
   keyList = toList(keys);
   return "(<for(i <- index(keyList)){><(i==0)?"":", "><keyList[i]>: <generateValue(vt)><}>)";
}

public str generateTuple({Type ","}+ ets){
   return intercalate(", ", [generateValue(et) | et <-ets]);
   //return "\<<for(int i <- [0 .. size(ets)]){><(i==0)?"":", "><generateValue(ets[i])><}>\>";
}

public str generateRel({Type ","}+ ets){
   return "\<<for(int i <- [0 .. size(ets)]){><(i==0)?"":", "><generateValue(ets[i])><}>\>";
}

public str generateLRel({Type ","}+ ets){
   return "\<<for(int i <- [0 .. size(ets)]){><(i==0)?"":", "><generateValue(ets[i])><}>\>";
}

public str generateArb(int n, {Type ","}+ prefs){
   if(n <= 0)
      return generateValue(getOneFrom(prefs));
      
   switch(arbInt(5)){
     case 0: return generateList(generateArbType(n-1, prefs), minSize, maxSize);
     case 1: return generateSet(generateArbType(n-1, prefs), minSize, maxSize);
     case 2: return generateMap(generateArbType(n-1, prefs), generateArbType(n-1, prefs));
     case 3: return generateTuple(prefs);
     case 4: return generateRel(prefs);
   }
} 

int countInsertCmds(EvalCmd c){
    n = 0;
    visit(c){ case InsertCmd ins: n += 1; }
    return n;
}

str holeMarkup(int n, int len) = "+++\<div class=\"hole\" id=\"hole<n>\" length=\"<len>\"/\>+++";

tuple[str quoted, str execute, bool hasHoles, map[str,str] bindings] preprocess(QuestionPart q, str setup, map[str,str] initialEnv){
    quoted = "";
    executed = "";
    nholes = 0;
    map[str,str] env = initialEnv;
    
    str handle(InsertCmd ins){
        switch(ins.insertDetails){  
            case (Insert) `<InsName name>:<Type tp>`: {
                str v = generateValue(generateType(tp));
                if(env["<name>"]?){
                    throw "Double declaration for <name> in <ins>";
                }
                env["<name>"] = v;
                //println("added <name> : <v>");
                return v;
            }
            case (Insert) `<Type tp>`: {
                Type tp1 = generateType(tp);
                return generateValue(generateType(tp));
            }
            case (Insert) `<InsName name>`: {
                return env["<name>"];
            }
        }
    }
    
    str handle(EvalCmd c){
        expr = "";
        for(elem <- c.elements){
            if(elem is insert_cmd){
                expr += handle(elem.insertCmd);
           } else {
                expr += "<elem>";
           }
        }
        //println("Eval: <expr>");
        v = eval(expr, setup);
        return "<v>";
    }
    
    top-down-break visit(q){
        case InsertCmd ins: {
            v = handle(ins);
            quoted += v; executed += v;
        }   
        case HoleCmd h: {
            nholes += 1;
            txt = "<h.contents.parts>";
            quoted += holeMarkup(nholes, size(txt));
            try {
                ins = [InsertCmd] "!{<txt>}";
                v = handle(ins);
                executed += v;
                //println("hole: InsertCmd: <v>");
            } catch: {
                try {
                    c = [EvalCmd] "%{<txt>}";
                    if(countInsertCmds(c) == 0) throw "";
                    v = handle(c);
                    executed += v;
                    //println("hole: EvalCmd: <v>");
                } catch : {
                    executed += "<txt>";
                    //println("hole: plain: <txt>");
                }
            }
         }
         
         case EvalCmd c: {
            v = handle(c);
            quoted += v; executed += v;
         }
        
        case AnyText a: { 
            quoted += "<a>";  
            executed += "<a>";
        }
        case LAYOUT l: {
            quoted += "<l>";
            executed += "<l>";
        }
    }
    
    return <quoted, executed, nholes > 0, env>;
}

void process(str question){
    res = process(parse(#Question, question));
    //println("<question>\n==\>\n<res>");
    println(res);
}

int nquestions = 0;

str process(Question q){
    text = "<q.text>";
    body = q.body;
    prep_quoted = prep_executed = "";
    prep_holes = false;
    env1 = ();
    nquestions += 1;
  
    if(body has prep){
        <prep_quoted, prep_executed, prep_holes, env1> = preprocess(body.prep.text, "", ());
    }
    
    expr_quoted = expr_executed = "";
    expr_holes = false;
    env2 = env1;
    if(body has expr){
        <expr_quoted, expr_executed, expr_holes, env2> = preprocess(body.expr.text, prep_executed, env1);
        return questionMarkup(nquestions, text, "module Question
                                    '<prep_quoted><"<prep_quoted>" == "" ? "" : "\n">
                                    'test bool <body.expr.name>() = <expr_quoted> == <expr_holes ? eval(expr_executed, prep_executed) : holeMarkup(1, 10)>;");
    }
    if(prep_holes){
        runTests(prep_executed);
        return questionMarkup(nquestions, text, "module Question
                                    '<prep_quoted>
                                    ");
    }
    throw "Incorrect question: no expr given and no holes in prep code";
      
}

str replaceHoles(str code){
    return replaceAll(visit(code){
        case /^\+\+\+[^\+]+\+\+\+/ => "_"
    }, "\n", "\\n");
}

str removeSpacesAroundHoles(str code){
    return visit(code){
        case /^[ ]+\+\+\+/ => "+++"
        case /^\+\+\+[ ]+/ => "+++"
    };
}

str questionMarkup(int n, str text, str code){
    return ".Question <n>
           '<text>
           '++++
           '\<div id=\"question<n>\" 
           '    class=\"codequestion\"
           '    listing=\"<replaceHoles(code)>\"\>
           '++++
           '[source,rascal,subs=\"normal\"]
           '----
           '<removeSpacesAroundHoles(code)>
           '----
           '++++
           '\</div\>
           '\<br\>
           '++++";
}

PathConfig pcfg = 
    pathConfig(srcs=[|test-modules:///|, |home:///git/rascal/src/org/rascalmpl/library|], 
               bin=|home:///bin|, 
               boot=|home:///git/rascal/bootstrap/phase2|,
               libs=[|home:///bin|, |home:///git/rascal/bootstrap/phase2/org/rascal/mpl/library|]);

loc makeQuestion(){
    for(f <- pcfg.bin.ls){
        if(/Question/ := "<f>"){
        //println("remove: <f>");
           remove(f);
        }
    }
    mloc = |test-modules:///| + "Question.rsc";
    return mloc;
}

value eval(str exp, str setup) {
    Q = makeQuestion();
    msrc = "module Question <setup> value main() {<exp>;}";
    writeFile(Q, msrc);
    try {
       compileAndLink("Question", pcfg); 
       return execute(Q, pcfg);
    } catch e:{
       println("*** While evaluating <exp> in
               '    <msrc> 
               '*** the following error occurred: 
               '    <e>");
       throw "Error while evaluating expression <exp>";
    }
}

void runTests(str mbody){
    Q = makeQuestion();
    msrc = "module Question <mbody>";
    writeFile(Q, msrc);
    try {
       compileAndLink("Question", pcfg); 
       res = execute(Q, pcfg, testsuite=true);
       if(!printTestReport(res, [])){
          throw "Errors while executing testsuite for question";
       }
    } catch e: {
       println("*** While running tests for 
               '    <msrc> 
               '*** the following error occurred: 
               '    <e>");
       throw "Error while running tests";
    }
} 

value main(){

    process("question Replace _ by an expression and make the test true:
            'expr setComprehension: [?{n-1} | int n \<- !{list[int]}]
            'end");
            
    process("question Replace _ by a function name and make the test true:
            'prep: 
            'import List;
            'expr listFunction: ?{headTail}(!{list[int]})
            'end"); 
 //            
 //   process("question Replace _ by a value and make the test true:
 //           'expr setIn: ?{A:int} in %{  !{B:set[int]} + !{A} }
 //           'end");         
 //
 //   process("question Replace _ by the result of the intersection and make the test true:
 //           'expr setIntersection: %{ !{A:set[int]} + !{B:set[int]} } & %{ !{C:set[int]} + !{B} }
 //           'end");
//    
//    process("question Replace _ by the value of the given expression and make the test true:
//            'prep:
//            'lrel[str country, int year, int amount] GDP = 
//            '   [\<\"US\", 2008, 14264600\>, 
//            '    \<\"EU\", 2008, 18394115\>,
//            '    \<\"Japan\", 2008, 4923761\>, 
//            '    \<\"US\", 2007, 13811200\>, 
//            '    \<\"EU\", 2007, 13811200\>, 
//            '    \<\"Japan\", 2007, 4376705\>];
//            'expr relSubscript: GDP[\"US\",2008]
//            'end");
//    
//    process("question Replace _ by the size of the list and make the test true:
//            'prep:
//            'import List;
//            'expr sizeList: size(!{list[int]})
//            'end");
//            
//    process("question Replace _ (two times!) and make the test true:
//            'prep:
//            'import ?{List};
//            'expr sizeList: size(!{list[int]})
//            'end");
//   
//    process("question Complete the body of the function palindrome and let all tests pass:
//            'prep:
//            'import String;
//            '
//            'bool isPalindrome(str words) = ?{size(words) \<= 1 || words[0] == words[-1] && isPalindrome(words[1..-1])};
//            '
//            'test bool palindrome1() = isPalindrome(\"\");
//            'test bool palindrome2() = isPalindrome(\"a\");
//            'test bool palindrome3() = isPalindrome(\"aa\");
//            'test bool palindrome4() = isPalindrome(\"abcba\");
//            'test bool palindrome5() = !isPalindrome(\"abc\");
//            'end");
//     
//     process("question Given the data type `ColoredTree`, complete the definition of the function `flipRedChildren` 
//             'that exchanges the children of all red nodes.
//             'prep:
//             'data ColoredTree = leaf(int N)      
//             '        | red(ColoredTree left, ColoredTree right) 
//             '        | black(ColoredTree left, ColoredTree right);
//             '
//             'ColoredTree rb = red(black(leaf(1), red(leaf(2),leaf(3))), black(leaf(3), leaf(4)));
//
//             'ColoredTree flipRedChildren(ColoredTree t){
//             '   return visit(t){
//             '      case red(l,r) =\> ?{red(r,l)}
//             '   };
//             '}
//
//             'test bool ct1() = flipRedChildren(rb) == red( black(leaf(3), leaf(4)), black(leaf(1), red(leaf(3),leaf(2))));
//             'end");
     
    return true;
}