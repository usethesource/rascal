module lang::rascal::tutor::Convert

import IO;
import List;
import String;
import util::FileSystem;
import ParseTree;
import util::Reflective;
import lang::rascal::\syntax::Rascal;
/*

Name:               =>  # the_name
the_name           

Synopsis:           => .Synopsis
enz.

<listing>           => ```rascal            PM from file
</listing>          => ```

<screen>            => ```rascal-shell      PM errors en continue
</screen>           => ```

$VarName$           => _VarName_
$VarName_index$     => _VarName~index~_
$VarName^index$     => _VarName^index^_

<warning>           => WARNING:
</warning>          => ""

[ConceptName]       => 
[$ConceptName]      => 
$OtherCourse:ConceptName] =>


In code:

*/

str getAnchor(loc src){
    parts = split("/", src.path);
    return "<parts[-3]>-<parts[-2]>";
}

str convert(loc src){    
    return convert(getAnchor(src), readFileLines(src));
}

str hashes(int n) = "<for(int _ <-[0..n]){>#<}>";

str convert(str _, list[str] lines){
  result = ""; //"[[<anchor>]]\n";
  int i = 0;
  
  while (i < size(lines)){
    switch(lines[i]){
  
    case /^<eqs:[=]+><label:[^=]*>[=]+/: {
      result += "<hashes(size(eqs))> <label>\n\n";
      i += 1;
    }
    
    case /^\s*<marker:[\*\#]+><entry:.*>/: {
        if(/^[A-Za-z]+/ := lines[i - 1]){
           result += "\n";
        }
        result += "<marker> <entry>\n";
        i += 1;
    }
  
    case /^Name:\s*<name:.*>$/: {
        result += "# <name>\n";
        i += 1;
    }
    case /^Details:/: {
        result += ".Details\n";
        i += 1;
    }
    case /^Synopsis:\s*<text:.*>$/: {
        result += ".Synopsis
                  '<text>
                  '";
        i += 1;
    }
    case /^Usage:/: {
        result += ".Usage\n";
        i += 1;
    }
    case /^Types:/: {
        result += ".Types\n";
        i += 1;
    }
    case /^Function:/: {
        result += ".Function\n";
        i += 1;
    }
    case /^Syntax:/: {
        result += ".Syntax\n";
        i += 1;
    }
    case /^Description:/: {
        result += ".Description\n";
        i += 1;
    }
    case /^Examples:/: {
        result += ".Examples\n";
        i += 1;
    }
    case /^Benefits:/: {
        result += ".Benefits\n";
        i += 1;
    }
    case /^Pitfalls:/: {
        result += ".Pitfalls\n";
        i += 1;
    }
    
    case /^Questions:/: {
        return result;
    }
    
    // table
    case /^\|/: {
       result += "|====
                 '<lines[i]>
                 '";
       i += 1;
       while(/^\|/ := lines[i]){
        result += lines[i] + "\n";
        i += 1;
       }
       result += "|====\n";
    }
    
    // screen
    case /^\<screen\s*<error:.*>\>\s*.*$/: {
      result += "[source,rascal-shell<error == "" ? "" : ",error">]\n----\n";
      i += 1;
      incode = true;
      while(/^\<\/screen\>/ !:= lines[i]) {
         while(/^\/\/\s*<rest:.*$>/ := lines[i] && /^\<\/screen\>/ !:= lines[i]){
            //println(lines[i]);
            if(incode){
               incode = false;
               result += "----\n";
            }
            result += "<rest>\n";
            i += 1;
         }
         if(!incode){
            result += "[source,rascal-shell,continue<error == "" ? "" : ",error">]\n----\n";
            incode = true;
            if(/^\<\/screen\>/ := lines[i]){
                break;
            }
         }
         result += "<lines[i]>\n";
         i += 1;
      }
      result += "----\n";
      i += 1;
    }
    
    // listing from file
    case /^\<listing\s*<name:.+>\>$/: {
      result += "[source,rascal]
                '----
                'include::{LibDir}<name>[]
                '----
                '
                ";
      i += 1;
    }
    
    // inline listing  
    case /^\<listing\>\s*.*$/: {
      result += "[source,rascal]
                '----
                '";
      i += 1;

      while(/^\<\/listing\>/ !:= lines[i]){
         result += "<lines[i]>\n";
         i += 1;
      }
      result += "----
                '";
       i += 1;         
    }
     
    // render a figure
    case /^\<figure\s*<file:.*>\>$/: {
      str width = "";
      str height = "";
      //println("file = <file>");
      if(/<name:.*>\s+<w:[0-9]+>x<h:[0-9]+>$/ := file){
        file = name;
        width = w;
        height = h;
      }
      result += "[source,rascal-figure,width=<width>,height=<height>,file=<file>]
                '----
                ";
      i += 1;
    }
    case /^\<\/figure\>/:{
         result += "----\n";
         i += 1;
    }
    
    // table of contents
    
    case /^\<toc\s*<concept:[A-Za-z0-9\/]*>\s*<level:[0-9]*>/:{
      result += "subtoc[<concept>,<level>]\n";
      i += 1;
    }
    
    case /^\<warning\><txt:.*>\<\/warning\><rest:.*>$/:{
      result += "Warning: <txt>\n<convertRestLine(rest)>";
      i += 1;
    }
    
    // anything else
    default: {
      result += convertRestLine(lines[i]) + "\n";
      i += 1;
    }
    }
  }
  return result;
}

// Take care of other markup in a line

private str convertRestLine(str line){
  return visit(line){
    
    case /^<op1:__?>+<text:[^_]*><op2:__?>/: {
       if(op1 != op2)
          fail;
       insert (size(op1) == 1) ? i(text) : b(text);
    }
    
    case /^`<c:[^`]*>`/ => (size(c) == 0) ? "`" : code(markupCode(c))
    
    case /^\$<var:[A-Za-z]*><ext:[_\^\+\-A-Za-z0-9]*>\$/ => i(var) + markupSubs(ext)              
    
    case /^\[<text:[^\]]*>\]\(<url:[:\/0-9-a-zA-Z"$\-_.\+!?*'(),~#%=]+>\)/ => link(url, text)
    
    case /^\[\$?<concept:[A-Za-z0-9\/]+>\]/ => "\<\<<slash2dash(concept)>\>\>"
    
    case /^\[\$?<course:[A-Za-z0-9\/]+>\s*:\s*<concept:[A-Za-z0-9\/]+>\]/ =>
         "link::{<course>}#<slash2dash(concept)>[]"
    
    case /^\/\*<dig:[0-9][0-9]?>\*\//  => "\<<dig>\>"
    
    case /^!\[<alt:[^\]]*>\]\(<file:[A-Za-z0-9\-\_\.\/]+\.[a-z0-9]+><opts:[^\)]*>\)/: { 
        imOpts = getImgOpts(opts,alt);
        if(imOpts != ""){
           imOpts += ",";
        }
        insert "\nimage:<file>[<imOpts>alt=\"<alt>\"]\n";
    }
    
   };
}

str slash2dash(str txt) = replaceAll(txt, "/", "-");

public str b(str txt){
  return "*<txt>*";
}

public str i(str txt){
  return "_<txt>_";
}

public str code(str txt){
  return "`<txt>`";
}

private str markupSubs(str txt){
  return visit(txt){
    case /^_<subsup:[\+\-]?[A-Za-z0-9]+>/  => sub(subsup) 
    case /^\^<subsup:[\+\-]?[A-Za-z0-9]+>/ => sup(subsup)   
  }
}

public str sub(str txt){
    return "~<txt>~";
}

public str sup(str txt){
    return "^<txt>^";
}

// Do the markup for a code fragment

private str markupCode(str text){
  return visit(text){
    case /^\$<var:[A-Za-z]*><ext:[_\^\+\-A-Za-z0-9]*>\$/ => i(var) + markupSubs(ext)
    case /^\/\*<dig:[0-9][0-9]?>\*\// => "//\<<dig>\>"
  }
}

// Refer to an external link

public str link(str url, str text){
  return "<url>[<text>]";
}

private str getImgOpts(str txt, str _){
  opts = "";
  visit(txt){
    case /^\s*\|\s*left/: {opts += "style=\"float: left;\" "; insert "";}
    case /^\s*\|\s*right/: {opts += "style=\"float: right;\" "; insert "";}
    case /^\s*\|\s*center/: {opts += "style=\"float: center;\" "; insert "";}
    case /^\s*\|\s*<N:[0-9]+>\s*px/: {opts += "width=\"<N>px\" "; insert ""; }
    case /^\s*\|\s*border\s*<N:[0-9]+>\s*px/: {opts += "border=\"<N>px\" "; insert ""; }
    case /^\s*\|\s*border/: {opts += "border=\"1px\" "; insert ""; }
    case /^\s*\|\s*space\s*<N:[0-9]+>\s*px/: {opts += "hspace=\"<N>px\" vspace=\"<N>px\" "; insert ""; }
  }
  //println("getImgOpts(<txt>) returns <opts>");
  return opts;
}

tuple[bool, str] convertLib(loc L){
    Module M = parseModuleWithSpaces(L).top;
    
    M1 = visit(M){
        case Tag t:
            if("<t.name>" == "doc") {
               c = trim(convert("XXX", split("\n", "<t.contents>")));
               
               if(!endsWith(c, "}")){
                  c += "\n}";
               }
               try { 
                  cp = parse(#TagString,c);
                  t.contents = cp;
                  insert t;
                } catch value e: {	// TODO: type was added for new (experimental) type checker; otherwise no type info known about e
                  println("Could not parse: <c>");
                  throw e;
                }
            }
    };
    
    return <M1 != M, "<M1>">;  
}

value main(){

    for(conceptFile <- find(|file:///Users/paulklint/git/rascal/src/org/rascalmpl/courses/Tutor|, "concept")){
        println("conceptFile: <conceptFile>");
        lines = readFileLines(conceptFile);
        println(conceptFile[extension = "concept2"]);
        writeFile(conceptFile[extension = "concept2"], convert(conceptFile));
    }
    
    return true;
}

value convertLibs(){
    int nfiles = 0, nconverted = 0;
    for(libFile <- find(|file:///Users/paulklint/git/rascal/src/org/rascalmpl/library|, "rsc")){
        nfiles += 1;
        if(libFile == |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Boolean.rsc|){
            continue;
        }
        lines = readFileLines(libFile);
        <converted, libText> = convertLib(libFile);
       if(converted){
           nconverted += 1;
           println("converted : <libFile>");
           writeFile(libFile, libText);
        }
    }
    println("<nfiles> files, <nconverted> converted");
    return true;
}