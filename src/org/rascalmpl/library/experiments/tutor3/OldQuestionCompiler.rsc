module experiments::tutor3::OldQuestionCompiler

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
import experiments::tutor3::Questions;
import experiments::tutor3::ParseQuestions;
import experiments::tutor3::ValueGenerator;
 
int countInsertCmds(EvalCmd c){
    n = 0;
    visit(c){ case InsertCmd ins: n += 1; }
    return n;
}

str holeMarkup(int n, int len) = "+++\<div class=\"hole\" id=\"hole<n>\" length=\"<len>\"/\>+++";

tuple[str quoted, str execute, bool hasHoles, map[str,str] bindings] preprocess(int questionId, QuestionPart q, str setup, map[str,str] initialEnv, PathConfig pcfg){
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
            default:
             throw "Unhandled insert <ins>";
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
        v = eval(questionId, expr, setup, pcfg);
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

int questionId = 0;

//void process(loc qloc, loc aloc, str questions){
//    qs = parse(questions);
//    questionId = 0;
//    for(q <- qs.questions){
//        println(process(q));
//    }
//}

str removeComments(Intro? intro){
   res = "";
   for(line <- split("\n", "<intro>")){
       if(!startsWith(line, "//")){
          res += line + "\n";
       }
   }
   //println("removeComments: intro = @<intro>@ =\> <res>");
   return res;
}

public str compileQuestions(str qmodule, list[loc] srcs, list[loc] libs, list[loc] courses, loc bin, loc boot) {
    println("compileQuestions: <qmodule>, <srcs>, <libs>, <courses>, <bin>, <boot>");
    pcfg = pathConfig(srcs=[|test-modules:///|]+srcs,libs=libs,bin=bin, boot=boot);
    bn = split("/", qmodule)[-1];
    qloc = courses[0] + ("/" + qmodule + "/" + bn + ".questions");
    println("compileQuestions: qloc=<qloc>");
    return compileQuestions(qloc, pcfg);
}

public str compileQuestions(loc qloc, PathConfig pcfg){
   return process(qloc, pcfg);
}

str process(loc qloc, PathConfig pcfg){
    qs = parse(qloc);
    questionId = 0;
    
    bn = qloc.file;
    if(/^<b:.*>\..*$/ := qloc.file) bn = b;
    
    res = "# <bn>
          '
          '++++
          '\<script src=\"http:///code.jquery.com/jquery-3.1.1.js\"\>\</script\>
          '++++
          '";
    for(q <- qs.questions){
        intro = removeComments(q.intro);
        res += (intro + "\n" + process(q, pcfg) +"\n");
    }
    res += "
           '++++
           '\<script src=\"tutor-prelude.js\"\>\</script\>
           '++++
           '";
    return res;
}

str process(Question q, PathConfig pcfg){
    text = "<q.text>";
    body = q.body;
    prep_quoted = prep_executed = "";
    prep_holes = false;
    env1 = ();
    questionId += 1;
  
    if(body has prep){
        <prep_quoted, prep_executed, prep_holes, env1> = preprocess(questionId, body.prep.text, "", (), pcfg);
    }
    
    expr_quoted = expr_executed = "";
    expr_holes = false;
    env2 = env1;
    if(body has expr){
        <expr_quoted, expr_executed, expr_holes, env2> = preprocess(questionId, body.expr.text, prep_executed, env1, pcfg);
        return questionMarkup(questionId, text, 
                                    "module Question<questionId>
                                    '<prep_quoted><"<prep_quoted>" == "" ? "" : "\n">
                                    'test bool <body.expr.name>() = 
                                    '     <expr_quoted> == <expr_holes ? eval(questionId, expr_executed, prep_executed, pcfg) : holeMarkup(1, 10)>;
                                    '");
    }
    if(prep_holes){
        runTests(questionId, prep_executed, pcfg);
        return questionMarkup(questionId, text, "module Question<questionId>
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

str escape(str code){
    return replaceAll(code, "\"", "&quot;");
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
           '\<div id=\"Question<n>\" 
           '    class=\"codequestion\"
           '    listing=\"<escape(replaceHoles(code))>\"\>
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

loc makeQuestion(int questionId, PathConfig pcfg){
    for(f <- pcfg.bin.ls){
        if(/Question/ := "<f>"){
        //println("remove: <f>");
           remove(f);
        }
    }
    mloc = |test-modules:///| + "Question<questionId>.rsc";
    return mloc;
}

value eval(int questionId, str exp, str setup, PathConfig pcfg) {
    Q = makeQuestion(questionId, pcfg);
    msrc = "module Question<questionId> <setup> value main() {<exp>;}";
    writeFile(Q, msrc);
    try {
       compileAndLink("Question<questionId>", pcfg); 
       return execute(Q, pcfg);
    } catch e:{
       println("*** While evaluating <exp> in
               '    <msrc> 
               '*** the following error occurred: 
               '    <e>");
       throw "Error while evaluating expression <exp>";
    }
}

void runTests(int questionId, str mbody, PathConfig pcfg){
    Q = makeQuestion(questionId, pcfg);
    msrc = "module Question<questionId> <mbody>";
    writeFile(Q, msrc);
    try {
       //compileAndLink("Question<questionId>", pcfg); 
       //res = execute(Q, pcfg, testsuite=true);
       //if(!printTestReport(res, [])){
       if(true !:= rascalTests(["Question<questionId>"], pcfg, recompile=true)){
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
    PathConfig pcfg = 
    pathConfig(srcs=[|test-modules:///|, |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library|], 
               bin=|file:///Users/paulklint/git/rascal/bootstrap/phase2|, 
               boot=|file:///Users/paulklint/git/rascal/bootstrap/phase2|,
               libs=[|home:///git/rascal/bootstrap/phase2/org/rascal/mpl/library|]);
    println(compileQuestions("ADocTest/Questions", pcfg.srcs, pcfg.libs, [|home:///git/rascal/src/org/rascalmpl/courses|], pcfg.bin, pcfg.boot));
    //question[code] Replace _ by an expression and make the test true:
    //   expr[setComprehension,[:answer[n-1] | int n \<- :generate[list[int][1,10]]]]
    //end
    //process("question Replace _ by an expression and make the test true:
    //        'expr setComprehension: [?{n-1} | int n \<- !{list[int][1,10]}]
    //        'end
    //        '
    //        'question Replace _ by a function name and make the test true:
    //        'prep: 
    //        'import List;
    //        'expr listFunction: ?{headTail}(!{list[int][1,10]})
    //        'end 
    //         
    //        'question Replace _ by a value and make the test true:
    //        'expr setIn: ?{A:int} in %{  !{B:set[int]} + !{A} }
    //        'end");         
 //
 //   process("question Replace _ by the result of the intersection and make the test true:
 //           'expr setIntersection: %{ !{A:set[int]} + !{B:set[int]} } & %{ !{C:set[int]} + !{B} }
 //           'end");
 //   
    //process("question Replace _ by the value of the given expression and make the test true:
    //        'prep:
    //        'lrel[str country, int year, int amount] GDP = 
    //        '   [\<\"US\", 2008, 14264600\>, 
    //        '    \<\"Japan\", 2007, 4376705\>];
    //        'expr relSubscript: GDP[\"US\",2008]
    //        'end");
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