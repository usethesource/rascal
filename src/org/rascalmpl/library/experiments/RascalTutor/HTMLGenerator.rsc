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

public void tst(){
println(markup(
["The value of 2 + 3 is @@2 + 3@@.",
"A set of integers: ",
"@@{1, 2, 3}@@. The type of this set is \<tt\>set[int]\</tt\> ",
"as can be seen when we type it into the Rascal evaluator:",
"\<screen\>",
"{1, 2, 3}",
"\</screen\>",
"xxxx",
"\<listing\>",
"if(a \> b)",
"   x = 5;",
"\</listing\>",
"yyy",
"and now we first do an import",
"\<screen\>",
"import List",
"and now we apply a function from the List library:",
"size([1,2,3]);",
"\</screen\>",
"xxx"
]));
}

public void tst2(){
println(markup([
"Other examples of sets are:",
"",
"* \<tt\> {1, 2, 3}\</tt\> // A set of integers",
"* \<tt\> {} \</tt\>       // The empty set",
"* \<tt\> {\"abc\"}\</tt\>   // A set containing a single string"
]));
}

public str markup(list[str] lines){
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
    case /^<stars:[\*]+><entry:.*>/: {res += listEntry("ul", size(stars), entry); i += 1; }
  		
    // Ordered lists
    case /^<hashes:[\#]+><entry:.*>/: { res += listEntry("ol", size(hashes), entry); i += 1; }
    
    case /^:\*<entry:.*>/: {
      if(size(listNesting) > 0){
        res += "\</<popList()>\>" + li(markupRestLine(entry));
      } else
           res + markupRestLine(entry);
      i += 1;
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
      
    case /^\<listing\>\s*<code:.*>$/: {
      res += closeLists();
      i += 1;
      while((i < n) && /^\<\/listing\>/ !:= lines[i]){
         code += lines[i] + "\n";
         i += 1;
      }
      res += pre("listing", code);
      i += 1;
      }
      
    default: {
      res += closeLists() + markupRestLine(lines[i]);
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
    
    case /^@@<code:[^\@]*>@@/ => closeLists() + tt(toString(eval(code)))
    
    case /^\$<var:[A-Z][A-Za-z]*><subscript:[0-9]>?\$/ =>
                                i(var) + ((subscript == "") ? "" : sub(subscript))
    
    case /^\[<url:[^\]]>\]\]/ => link(url)
  }
}   

test markupRestLine("The value of 2 + 3 is @@2 + 3@@") == "The value of 2 + 3 is \<tt\>5\</tt\>";


public str markupRascalPrompt(list[str] lines){
  return  "<for(str line <- lines){><visit(line){ case /^rascal\>/ => b("rascal\>") }>\n<}>";
}

public void tst3(){
println(markup([

"\<screen\>",
"import IO;",
"void hello() {",
"   println(\"Hello world, this is my first Rascal program\");",
"}",
"hello();",
"\</screen\>"
]));
}

public str markupScreen(list[str] lines){
   stripped_code = "<for(line <- lines){><(startsWith(line, "//")) ? "" : (line + "\n")><}>";
   result_lines = shell(stripped_code);
   
   println("markupScreen: lines=<lines>; result_lines=<result_lines>");
   int i = 0; int upbi = size(lines);
   int j = 0; int upbj = size(result_lines);
   pre_open = "\<pre class=\"screen\"\>";
   code = pre_open;
   inPre = true;
   prompt =       "rascal\>";
   continuation = "\>\>\>\>\>\>\>";
   while(i < upbi && j < upbj){
         code += b(prompt) + lines[i] + "\n";
         i += 1; j += 1;
         while(j < upbj && !startsWith(result_lines[j], prompt)){
           code += result_lines[j] + "\n";
           if(i < upbi && startsWith(result_lines[j], continuation))
             i += 1;
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
// pre("screen", markupRascalPrompt(shell(code)));


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

test markupSynopsis(["Exp1 + Exp2"])          == "\<tt\>\<i\>Exp\</i\>\<sub\>1\</sub\> + \<i\>Exp\</i\>\<sub\>2\</sub\>\</tt\>";
test markupSynopsis(["Exp1 + Exp2", "  "])    == "\<tt\>\<i\>Exp\</i\>\<sub\>1\</sub\> + \<i\>Exp\</i\>\<sub\>2\</sub\>\</tt\>";
test markupSynopsis(["Exp1 + Exp2", "Exp3"])  == "\<ul\>\<tt\>\<i\>Exp\</i\>\<sub\>1\</sub\> + \<i\>Exp\</i\>\<sub\>2\</sub\>\</tt\>\<tt\>\<i\>Exp\</i\>\<sub\>3\</sub\>\</tt\>\</ul\>";

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

