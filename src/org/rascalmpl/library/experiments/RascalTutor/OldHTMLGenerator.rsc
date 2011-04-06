@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
module experiments::RascalTutor::HTMLGenerator

import experiments::RascalTutor::HTMLUtils;

import String;
import ToString;
import IO;
import List;
import Scripting;

private list[str] listNesting = [];

private void pushList(str listType){
	listNesting = listType + listNesting;
}

private str popList(){
   t = head(listNesting);
   listNesting = tail(listNesting);
   return t;
}

private str listEntry(str listType, int nesting, str entry){
  start = "\<<listType>\>\n";
  
  currentNesting = size(listNesting);
  if(nesting == currentNesting){
  	 return li(markupRestLine(entry));
  } else if(nesting > currentNesting){
     startList = "";
     while(nesting > size(listNesting)){
       startList += start;
       pushList(listType);
     }
     return startList + li(markupRestLine(entry));
  } else {
     endList = "";
     while(nesting < size(listNesting)){
       endList +=  "\n\</<popList()>\>\n";
     }
     return endList + li(markupRestLine(entry));
  }
}

private str closeLists(){
  endList = "";
  while(size(listNesting) > 0){
    endList +=  "\n\</<popList()>\>\n";
  }
  return endList;
}

public void tst2(){
println(markup([
"xxxxx\<tt\>\<if(\'\'Exp\'\'){\> ... \'\'Text\'\' ... \<}\>\</tt\>",
"Other examples of sets are:",
"",
"* \<tt\>\<if(\'\'Exp\'\'){\> ... \'\'Text\'\' ... \<}\>\</tt\>",
"* \<tt\> {1, 2, 3}\</tt\> // A set of integers",
"* \<tt\> {} \</tt\>       // The empty set",
"* \<tt\> {\"abc\"}\</tt\>   // A set containing a single string"
]));
}

private str conceptPath = "";

private str markup(list[str] lines){
  return markup(lines, conceptPath);
}

public str markup(list[str] lines, str cp){
  conceptPath = cp;
  n = size(lines);
  int i = 0;
  str res = "";
  while(i < n){
    switch(lines[i]){
    // Sections
    case /^=<eqs:[=]+><label:[^=]*>[=]+/: { res += h(size(eqs), label); i += 1; }
    
    // Horizontal line
    case /^----/: { res += hr(); i += 1; }
    
    // Unordered lists
    case /^<stars:[\*]+><entry:.*>/: {
       i += 1;
       while(i < n && /^\*\:<more:.*>/ := lines[i]){
         entry += " " + more;
         i += 1;
       }
       res += listEntry("ul", size(stars), entry); 
    }
  		
    // Ordered lists
    case /^<hashes:[\#]+><entry:.*>/: { 
       i += 1;
       while(i < n && /^\#\:<more:.*>/ := lines[i]){
         entry += " " + more;
         i += 1;
       }
      res += listEntry("ol", size(hashes), entry); 
    }
   
    case /^\<screen\>\s*<code:.*>$/: {
      res += closeLists();
      i += 1;
      start = i;
      while((i < n) && /^\<\/screen\>/ !:= lines[i]){
         code += lines[i] + "\n";
         i += 1;
      }
      res += markupScreen(slice(lines, start, i - start));
      i += 1;
      }
      
    case /^\<listing\s*<name:.+>\>$/: {
      loc L = |stdlib:///|[path = name];
      try {
      	code = readFileLines(L);
      	println("code = <code>");
      	res += markupListing(code);
      } catch: res += "\<warning\>File <name> not found.\</warning\>";
      i += 1;
    }
      
    case /^\<listing\>\s*<rest:.*>$/: {
      res += closeLists();
      i += 1;
      code = [];
      while((i < n) && /^\<\/listing\>/ !:= lines[i]){
         code += lines[i];
         i += 1;
      }
      //res += pre("listing", code);
      res += markupListing(code);
      i += 1;
      }
    case /^$/: {
      res += closeLists();
      i += 1;
      if(i < n && size(lines[i]) == 0){
        i += 1;
        res += br() + br();
      } else
        res += "\n";
    }
      
    default: {
      res += closeLists() + markupRestLine(lines[i]) + "\n";
      i += 1;
    }
  }
  }
  res += closeLists();
  //println("markupLine ==\> <res>");
  return res;
}

public str markupRestLine(str line){
  ///println("markupRestLine(<line>)");
  return visit(line){
  
     // '' = Italic; ''' = Bold; ''''' = Bold Italic
    case /^'<apo:[']+><text:[^']+>'+/: {
       n = size(apo);
       insert (n == 1) ? i(text) : ((n == 2) ? b(text) : b(i(text)));
    }
    
    case /^\/\*<dig:[0-9]>\*\// => "\<img src=\"images/<dig>.png\"\>"
    
    case /^~~<code:[^\~]*>~~/ => tt(markupRestLine(code))
    
    case /^\$<var:[A-Z][A-Za-z]*><subscript:[0-9]>?\$/ =>
                                i(var) + ((subscript == "") ? "" : sub(subscript))
                                
    case /^\[\[\[<file:[A-Za-z0-9\-\_]+\.png><opts:[^\]]*>\]\]\]/ => "\<img <getImgOpts(opts)> src=\"<conceptPath>/<file>\"\>"
    
    case /^\[\[http:<url:[^\]]+>\]\]/ => link("http:" + url)
    
    case /^\[\[<concept:[^\]]+>\]\]/  => show(concept)
    
   };
}

public str show(str cn){
  return "\<a href=\"/show?concept=<cn>\"\><cn>\</a\>";
}

public str link(str url){
  return "\<a href=\"<url>\"\><url>\</a\>";
}

public str getImgOpts(str txt){
  opts = "";
  visit(txt){
    case /^\s*\|\s*left/: {opts += "align=\"left\" "; }
    case /^\s*\|\s*right/: {opts += "align=\"right\" "; }
    case /^\s*\|\s*<N:[0-9]+>\s*px/: {opts += "width=\"<N>px\" height=\"<N>px\" "; }
  }
  return opts;
}

//test markupRestLine("The value of 2 + 3 is @@2 + 3@@") == "The value of 2 + 3 is \<tt\>5\</tt\>";

public str markupListing(list[str] lines){
  txt = "";
  for(line <- lines)
    txt += markupListingLine(line) + "\n";
  return pre("listing", txt);
}

public str markupListingLine(str line){
  return visit(line){
    case /^\/\*<dig:[0-9]>\*\// => "\<img src=\"images/<dig>.png\"\>"
  }
}

public str markupRascalPrompt(list[str] lines){
  return  "<for(str line <- lines){><visit(line){ case /^rascal\>/ => b("rascal\>") }>\n<}>";
}

public void tst3(){
println(markup([

"\<screen\>",
"//AAA",
"import IO;",
"//BBB",
"void hello() {",
"   println(\"Hello world, this is my first Rascal program\");",
"}",
"//CCC",
"hello();",
"//DDD",
"\</screen\>"
]));
}

public str markupScreen(list[str] lines){
   stripped_code = "<for(line <- lines){><(startsWith(line, "//")) ? "" : (line + "\n")><}>";
   result_lines = shell(stripped_code);
   
   int i = 0; int upbi = size(lines);
   int j = 0; int upbj = size(result_lines);
   pre_open = "\<pre class=\"screen\"\>";
   code = pre_open;
   inPre = true;
   prompt =       "rascal\>";
   continuation = "\>\>\>\>\>\>\>";
   while(i < upbi && j < upbj){
   		 if(i < upbi && startsWith(lines[i], "//")){
           start = i;
           while(i < upbi && startsWith(lines[i], "//")){
               lines[i] = substring(lines[i], 2);
               i += 1;
           }
           code += "\</pre\>\n<markup(slice(lines, start, i - start))>\n<pre_open>";
         }
         if(i <upbi) {
         	code += b(prompt) + escapeForHtml(lines[i]) + "\n";
         	i += 1; j += 1;
         }
         while(j < upbj && !startsWith(result_lines[j], prompt)){
           code += result_lines[j] + "\n";
           if(i < upbi && startsWith(result_lines[j], continuation)){
              i += 1;
             }
           j += 1;
         }
         
         if(i < upbi && startsWith(lines[i], "//")){
           start = i;
           while(i < upbi && startsWith(lines[i], "//")){
               lines[i] = substring(lines[i], 2);
               i += 1;
           }
           code += "\</pre\>\n<markup(slice(lines, start, i - start))>\n<pre_open>";
         }

   }
   code += "\</pre\>";
   return code;
}

public str markupSynopsis(list[str] lines){
  
  rlines = for(int k <- [0 .. size(lines) - 1])
             if(/\S/ := lines[k])
                append tt(visit(lines[k]){
                          case /^<name:[a-z][A-Za-z0-9]*>/ => name
                          
                          case /^&<name:[A-Za-z0-9]+>/ => "&" + name
      
                          case /^<var:[A-Z][A-Za-z]*><subscript:[0-9]>?/ =>
                                i(var) + ((subscript == "") ? "" : sub(subscript))
                         });
  switch(size(rlines)){
    case 0:
      return "";
    case 1:
      return rlines[0];
    default:
     return ul("<for(line <- rlines){><li(line)><}>");
  }
}

//test markupSynopsis(["Exp1 + Exp2"])          == "\<tt\>\<i\>Exp\</i\>\<sub\>1\</sub\> + \<i\>Exp\</i\>\<sub\>2\</sub\>\</tt\>";
//test markupSynopsis(["Exp1 + Exp2", "  "])    == "\<tt\>\<i\>Exp\</i\>\<sub\>1\</sub\> + \<i\>Exp\</i\>\<sub\>2\</sub\>\</tt\>";
//test markupSynopsis(["Exp1 + Exp2", "Exp3"])  == "\<ul\>\<tt\>\<i\>Exp\</i\>\<sub\>1\</sub\> + \<i\>Exp\</i\>\<sub\>2\</sub\>\</tt\>\<tt\>\<i\>Exp\</i\>\<sub\>3\</sub\>\</tt\>\</ul\>";

public set[str] searchTermsSynopsis(list[str] lines){
   set[str] terms = {};
   for(int k <- [0 .. size(lines) - 1])
       visit(lines[k]){
         case /^<name:[a-z][A-Za-z0-9]*>/: {terms += name; insert "";} // BUG IN VISIT
         
         case /^\&<name:[A-Za-z0-9]+>/: {insert "";}
      
         case /^<var:[A-Z][a-z]*><subscript:[0-9]>?/: {insert "";}
                          
         case /^\s*<op:[^A-Za-z \t\r\n]+>/: {terms += op; insert "";}
       };
    return terms;
}

test markup(["===Level 2==="]) == "\<h2\>Level 2\</h2\>\n";

test markup(["----"]) == "\<hr\>\n";
test markup(["\'\'abc\'\'"]) == "\<i\>abc\</i\>";
test markup(["\'\'\'abc\'\'\'"]) == "\<b\>abc\</b\>";
test markup(["\'\'\'\'\'abc\'\'\'\'\'"]) == "\<b\>\<i\>abc\</i\>\</b\>";

test markup(["* abc"]) == "\<ul\>\n\<li\> abc\</li\>\n\</ul\>\n";
test markup(["* abc"]) == "\<ul\>\n\<li\> abc\</li\>\n\</ul\>\n";
test markup(["* abc", "X"]) == "\<ul\>\n\<li\> abc\</li\>\n\</ul\>\nX";
test markup(["* abc", "* def", "X"]) == "\<ul\>\n\<li\> abc\</li\>\n\<li\> def\</li\>\n\</ul\>\nX";
test markup(["* abc", "** def", "* ghi", "X"]) == "\<ul\>\n\<li\> abc\</li\>\n\<ul\>\n\<li\> def\</li\>\n\n\</ul\>\n\<li\> ghi\</li\>\n\</ul\>\nX";
test markup(["* abc", "## def", "* ghi", "X"]) == "\<ul\>\n\<li\> abc\</li\>\n\<ol\>\n\<li\> def\</li\>\n\n\</ol\>\n\<li\> ghi\</li\>\n\</ul\>\nX";
test markup(["* \'\'abc\'\'"]) == "\<ul\>\n\<li\> \<i\>abc\</i\>\</li\>\n\n\</ul\>\n";
test markup(["* abc", "* def", "\'\'ghi\'\'"]) == "\<ul\>\n\<li\> abc\</li\>\n\<li\> def\</li\>\n\</ul\>\n\<i\>ghi\</i\>";

