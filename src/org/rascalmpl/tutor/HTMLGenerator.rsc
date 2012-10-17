@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}

@bootstrapParser
module HTMLGenerator

import HTMLUtils;
import CourseModel;

import String;
import ToString;
import IO;
import List;
import Map;
import util::Eval;

// Maintain a list of warnings found for current concept;

private list[str] warnings = [];

// Get previous list of warnings and clear for next job.

public list[str] getAndClearWarnings(){
  w = warnings;
  warnings = [];
  return w;
}

// Add a warning.

public void addWarning(str txt){
  warnings += txt;
}

// Path of current concept, used to get file URLs right.

private str conceptPath = "";

// markup
// - lines: a list of lines
// - cp: the concept path to be used when resolving URLs.

public str markup(list[str] lines, str cp){
  conceptPath = cp;
  int i = 0;
  res = "";
  n = size(lines);
  while(i < n){
    <txt, i> = markup(lines, i, n);
    res += " " + txt;
  }
  return res;
}

private str markup(list[str] lines){
  return markup(lines, conceptPath);
}

// Skip one empty line

public int skipOneNL(list[str] lines, int i, int n){
  if(i < n && /^\s*$/ := lines[i])
     i += 1;
  return i;
}

// markup: internal markup function:
// lines: a list of lines
// i: current index in list
// n: size of list
// Returns:
// - HTML output
// - first unread index in lines

private tuple[str, int] markup(list[str] lines, int i, int n){
  if(i >= n)
    return <"", n>;
  switch(lines[i]){
    // Sections
    case /^=<eqs:[=]+><label:[^=]*>[=]+/: {
      return <h(size(eqs), label), skipOneNL(lines, i + 1, n)>; 
    }
    
    // Unordered/numbered lists:
    // Collect all items at the same indentation level
    case /^\s*<marker:[\*\#]+><entry:.*>/: {
       i += 1;
       entry = markupRestLine(entry);
       entries = "";
       while(i < n) {
         if(/^\s*<marker1:[\*\#]+><entry1:.*>/ := lines[i]){
            if(size(marker1) > size(marker)){
               <txt, i> = markup(lines, i, n);
               entry += " " + txt;
            } else if(size(marker1) < size(marker)) {
               entries += li(entry);
               return <startsWith(marker, "*") ? ul(entries) : ol(entries), i>;
            } else {
              entries += li(entry);
              entry = markupRestLine(entry1);
              i += 1;
            }
         } else if(i + 1 < n && /^\s*$/ := lines[i] && /^\s*$/ := lines[i+1]){
           if(entry != "")
              entries += li(entry);
              inext = (size(marker) == 1) ? i + 2 : i;
              return < startsWith(marker, "*") ? ul(entries) : ol(entries), inext>;
         } else if(i < n && /^\s*$/ := lines[i]){
             if(i + 1 < n && /^\s*[\*\#]/ !:= lines[i+1])
                entry += br() + br();
             i += 1;
         } else {
            <txt, i> = markup(lines, i, n);
            entry += " " + txt;
         }
       }
       if(entry != "")
          entries += li(entry);
       return <startsWith(marker, "*") ? ul(entries) : ol(entries), i>;
    }
   
    // screen
    case /^\<screen\s*<error:.*>\>\s*<codeLines:.*>$/: {
      i += 1;
      startLine = i;
      while((i < n) && /^\<\/screen\>/ !:= lines[i]){
         codeLines += lines[i] + "\n";
         i += 1;
      }
      end = i - startLine;
      return <markupScreen(slice(lines, startLine, end), error != ""), skipOneNL(lines, i+1, n)>;
    }
    
    // listing from file
    case /^\<listing\s*<name:.+>\>$/: {
      loc L = (|std:///|[path = name]).top;
      try {
      	codeLines = readFileLines(L);
      	return < markupListing(stripLicense(codeLines)), skipOneNL(lines, i+1, n) >;
      } catch value x: {
            msg = "<x>: L = <L>, File <name>.";
            addWarning(msg);
      		return <"\<warning\><msg>\</warning\>", i + 1>;
      }
    }
    
    // inline listing  
    case /^\<listing\>\s*<rest:.*>$/: {
      i += 1;
      codeLines = [];
      while((i < n) && /^\<\/listing\>/ !:= lines[i]){
         codeLines += lines[i];
         i += 1;
      }
      return < markupListing(codeLines), skipOneNL(lines, i+1, n)  >;
    }
    
    // render a figure
    case /^\<figure\s*<file:.*>\>$/: {
      int width = -1;
      int height = -1;
      //println("file = <file>");
      if(/<name:.*>\s+<w:[0-9]+>x<h:[0-9]+>$/ := file){
        file = name;
        width = toInt(w);
        height = toInt(h);
      }
      i += 1;
      codeLines = [];
      while((i < n) && /^\<\/figure\>/ !:= lines[i]){
         codeLines += lines[i];
         i += 1;
      }
      return < markupFigure(codeLines, width, height, file), skipOneNL(lines, i+1, n)  >;
    }
    
    // table of contents
    
    case /^\<toc\s*<concept:[A-Za-z0-9\/]*>\s*<level:[0-9]*>/:{
      return < markupToc(concept, level), skipOneNL(lines, i+1, n) >;
    }
    
    // warning
    case /^\<warning\><txt:.*>\<\/warning\><rest:.*>$/:{
      addWarning(txt);
      return <"\<warning\><txt>\</warning\>" + markupRestLine(rest), i + 1>;
    }
    
    // empty line
    case /^\s*$/: {
        if(i + 1 < n && /^\s*[\*\#=\|]/ !:= lines[i+1] &&
           /\<listing/ !:= lines[i+1] &&  /\<screen/ !:= lines[i+1])
           return <br() + br() + "\n", i + 1>;
        else return <"", i + 1>;
    }
    
    // table
    case /^\|/: {
      headings = getHeadings(lines[i]);
      i += 1;
      if(i < n){
        alignments = getAlignments(lines[i]);
        i += 1;
        rows = "";
        while(i < n && startsWith(lines[i], "|")){
          rows += tableRow(lines[i], alignments);
          i += 1;
        }
        return <table(headings, alignments, rows), skipOneNL(lines, i+1, n)>;
      }
    }
    
    // anything else
    default: {
      return < markupRestLine(lines[i]) + "\n", i + 1>;
    }
  }
}

// Get the column alignments of a table

private list[str] getAlignments(str txt){
  alignments = [];
  visit(txt){
    case /^\|:\-+/: { alignments += "left"; insert "";}
    case /^\|\-+:/: { alignments += "right"; insert "";}
    case /^\|\-+/:  { alignments += "center"; insert "";}
  }
  return alignments;
}

// Get the headings of a table

private list[str] getHeadings(str txt){
  headings = [];
  visit(txt){
    case /^\|<h:(`[^`]*`|[^|])+>/: { headings += markupRestLine(h); insert "";}
  }
  return headings;
}

// Format a table

private str table(list[str] headings, list[str] alignments, str rows){
  cols = "";
  for(a <- alignments)
     cols += col(a);
  hds = "";
  for(int i <- index(headings))
     hds += th(headings[i], alignments[i] ? "center");
  hds = tr(hds);
  return table(cols + hds + rows);
}

// Format a table row
// Also add alignment to each table datum, since col does not seems to work properly

private str tableRow(str txt, list[str] alignments){
  entries = "";
  k = 0;
  visit(txt){
      case /^\|<entry:(`[^`]*`|[^\|`])+>/: {entries += td(markupRestLine(entry), alignments[k] ? "left"); k += 1; insert "";}
  }
  return tr(entries);
}

// Take care of other markup in a line

private str markupRestLine(str line){
  return visit(line){
    
    case /^<op1:__?>+<text:[^_]*><op2:__?>/: {
       if(op1 != op2)
          fail;
       insert (size(op1) == 1) ? i(text) : b(text);
    }
    
    case /^`<c:[^`]*>`/ => (size(c) == 0) ? "`" : code(markupCode(c))
    
    case /^\$<var:[A-Za-z]*><ext:[_\^\+\-A-Za-z0-9]*>\$/ => code(i(var) + markupSubs(ext))              
    
    case /^\[<text:[^\]]*>\]\(<url:[:\/0-9-a-zA-Z"$\-_.\+!?*'(),~#%=]+>\)/ => link(url, text)
    
    case /^\[<short:\$?><concept:[A-Za-z0-9\/]+>\]/: {insert refToUnresolvedConcept(rootname(conceptPath), rootname(conceptPath), concept, short == "$"); }
    
    case /^\[<short:\$?><course:[A-Za-z0-9\/]+>\s*:\s*<concept:[A-Za-z0-9\/]+>\]/: 
         {insert refToUnresolvedConcept(rootname(conceptPath), course, concept, short == "$"); }
    
    case /^\\<char:.>/ :         //TODO nested matching is broken, since wrong last match is used!
      if(char == "\\") 	    insert	"\\";
      else if(char ==  "`") insert	"`";
      else if(char == "*")	insert "*";
      else if(char == "_")  insert "_";
      else if(char == "+")	insert "+";
      else if(char == ".")	insert ".";
      else insert char;
    
    case /^<span:\<[^\>]+\>>/ => span
    
    case /^<ent:&[A-Za-z0-9]+;>/ => ent
    
    case /^&/ => "&amp;"
    
    case /^\</ => "&lt;"
    
    case /^\/\*<dig:[0-9][0-9]?>\*\//  => "\<img src=\"/images/<dig>.png\"\>"
    
    case /^!\[<alt:[^\]]*>\]\(<file:[A-Za-z0-9\-\_\.\/]+\.png><opts:[^\)]*>\)/ => "\<img class=\"TutorImg\" <getImgOpts(opts,alt)> alt=\"<alt>\" src=\"/<conceptPath>/<file>\"\>"
    
   };
}

// Subscripts and superscripts

private str markupSubs(str txt){
  return visit(txt){
    case /^_<subsup:[\+\-]?[A-Za-z0-9]+>/  => sub(subsup) 
    case /^\^<subsup:[\+\-]?[A-Za-z0-9]+>/ => sup(subsup)   
  }
}

// Get options for image

private str getImgOpts(str txt, str alt){
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
  return opts + " title=\"<alt>\"";
}

// Remove the start of the file until (and including) a %%START%% marker
// This is intended to strip licenses and contributor listings.

list[str] stripLicense(list[str] lines){
 
  for(int i <- index(lines)){
    if(/\/\/START/ := lines[i])
      return slice(lines, i + 1, size(lines) - (i + 1));
  }
   return lines;
}

// Do the markup for listings

private str markupListing(list[str] lines){
  txt = "";
  for(line <- lines)
    txt += markupCode(line) + "\n";
  return pre("listing", txt);
}

// Do the markup for a code fragment

private str markupCode(str text){
  return visit(text){
    case /^\</   => "&lt;"
//    case /^\\ /  => "&nbsp;"
    case /^&/    => "&amp;"
    case /^\$\$/ => "$"
    case /^\$<var:[A-Za-z]*><ext:[_\^\+\-A-Za-z0-9]*>\$/ => i(var) + markupSubs(ext)
    case /^\/\*<dig:[0-9][0-9]?>\*\// => "\<img src=\"/images/<dig>.png\"\>"
  }
}

/*
private str lookForErrors(list[str] lines){
   errors = "";
   for(line <- lines)
	   if(/.*[Ee]rror/ := line || /.*[Ee]xception/ := line || /.*cancelled/ := line || /.*[Tt]imeout/ := line)
	       errors += line;
   if(errors != "")
      addWarning(errors);
   return errors;
}
*/

private str markupFigure(list[str] lines, int width, int height, str file){
  n = size(lines);
  str renderCall = lines[n-1];
  errors = "";
  if (/\s*render\(<arg:.*>\);/ := renderCall){
      // replace the render call by a call to renderSave
 
	  path = courseDir[path = courseDir.path + "<conceptPath>/<file>"].top;
	  lines[n-1] = (width > 0 && height > 0) ? "renderSave(<arg>, <width>, <height>, <path>);"
	                                         : "renderSave(<arg>, <path>);";
	  
	    //println("shell input: <for (line <- lines) {><line>
	    //        '<}>");

    try {
	     eval(["import vis::Figure;","import vis::Render;"] + ["<for (line <- lines) {><line> <}>"], 100000);
	  }
	  catch value x: 
	    errors += "<x>\n";
	    
	  // Restore original call for listing
	  lines[n-1] = renderCall;
  } 
  else {
    errors = "Last line should be a call to \"render\"";
  }
  
  if (errors != ""){
    errors = "\<warning\>" + errors + "\</warning\>";
    addWarning(errors);
  }
  return errors + markupListing(lines);
}

/*
private str markupRascalPrompt(list[str] lines){
  return  "<for(str line <- lines){><visit(line){ case /^rascal\>/ => b("rascal\>") }>\n<}>";
}
*/

// Do screen markup

private str printShellInput(str input) = printShellInput(split("\n",input));
  
private str printShellInput(list[str] input) = { //println("input was: <input>");
  "\<span class=\"prompt\"\>rascal\>\</span\><escapeForHtml(head(input))>
  '<for (cont <- tail(input)) {>\<span class=\"continuation\"\>\>\>\>\>\>\>\>\</span\><markupCode(cont)>
  '<}>"; };

data ShellException = parseError(str message, loc location) | error(str message);

@javaClass{org.rascalmpl.tutor.HTMLGenerator}
@reflect{to access the evaluator}
public java str shell(str command, int duration) throws Timeout, ShellException, ShellException;

@reflect{to access the evaluator}
@javaClass{org.rascalmpl.tutor.HTMLGenerator}
public java str startShell();

@reflect{to access the evaluator}
@javaClass{org.rascalmpl.tutor.HTMLGenerator}
public java str endShell();

private str markupScreen(list[str] lines, bool generatesError){
   todo = lines;
   
   codeLines = "\<pre class=\"screen\"\>";
 
   startShell();
   
   try while (todo != []) {
     <first,todo> = headTail(todo);
   
     // first collect comment lines and mark them up
     commentMarkup = [];
     while (startsWith(first, "//")) {
       commentMarkup += [substring(first,2)];
       if (todo == []) {
         return "<markup(commentMarkup)>\</pre\>"; // screen ended in comments
       }
       <first,todo> = headTail(todo);
     }

     if (commentMarkup != []) {
        codeLines += "\</pre\>";
        codeLines += markup(commentMarkup);
        codeLines += "\<pre class=\"screen\"\>";
     }
          
     if (first == "") {
       codeLines += printShellInput(first); 
       codeLines += "cancelled\n";
       continue;
     }
     
     // deal with normal command
     try {
       res = shell(first, 100000);
       //println("result: <res>");
       codeLines += printShellInput(first); 
       codeLines += markupCode(res);
     }
     catch parseError(str msg, loc x) : {
        //println("error: <msg>, <x>");
        if (x.offset >= size(first) && todo != []) {
          <next,todo> = headTail(todo);
          if (next == "") {
            codeLines += printShellInput("<first>\n<next>"); 
            codeLines += "\ncancelled\n";
          }
          else {
            todo = "<first>\n<next>" + todo;
          }
        }
        else {
          codeLines += printShellInput(first);
          codeLines += markupCode(msg);
        }
     }
     catch error(str msg) : {
       codeLines += printShellInput(first);
       codeLines += markupCode(msg);
       if (!generatesError) {
         addWarning("screen command failed: \"<first>\"");
       }
     }
     catch value x: {
        addWarning("screen command failed: \"<first>\", with exception: <x>");
     }
   } 
   catch value x: throw x; // only because we have not a try without finally
   finally {
     endShell();
   }

   codeLines += "\</pre\>";

   return replaceAll(codeLines, "\<pre class=\"screen\"\>\</pre\>", "");
}

public str limitWidth(str txt, int limit){
  if(size(txt) < limit)
    return escapeForHtml(txt);
  return escapeForHtml(substring(txt, 0, limit)) + "&raquo;\n" + limitWidth(substring(txt, limit), limit);
}

// ---- handle search terms ----

// Extract search terms from a code fragment

private set[str] searchTermsCode(str line){
  set[str] terms = {};
  visit(line){
    case /^\s+/: insert "";
    case /^\$[^\$]*\$/: insert "";
    case /^<kw:\w+>/: {terms += kw; /* println("kw = <kw>"); */ insert ""; }
    case /^<op:[^a-zA-Z\$\ \t]+>/: { terms += op; /* println("op = <op>");*/ insert ""; }
  }
  return terms;
}

// Collect search terms from the Synopsis-related entries in concept description

public set[str] searchTermsSynopsis(list[str] syn, list[str] tp, list[str] fn, list[str] synop){  
 return (searchTerms(syn) - {"...", "...,"}); // + 
        {t | str t <- searchTerms(tp), /\{\}\[\]\(\)[,]?/ !:= t, t notin {"...", "...,", ",...", ",...,"}};
         // TODO what do we do with searchTerms(fn), searchTerms(synop)?
}

public set[str]  searchTerms(list[str] lines){
   set[str] terms = {};
   n = size(lines);
   if(n == 0)
     return terms;
   k = 0;
   while(k < n){
       if(/\<listing\>/ := lines[k]){
           k += 1;
           while(k < n && /\<\/listing\>/ !:= lines[k]){
             terms += searchTermsCode(lines[k]);
             k += 1;
           }
       } else {
         visit(lines[k]){
           case /`<syn:[^`]*>`/: {terms += searchTermsCode(syn); insert ""; }
         }
       }
       k += 1;
    }
    return terms;
}

// ---- Table of contents (toc) ----

str markupToc(str conceptName, str slevel){
    
    int level = (slevel != "") ? toInt(slevel) : 1000;
    if(conceptName == "")
       conceptName = conceptPath;
    options = resolveConcept(rootname(conceptName), conceptName);
    if(size(options) != 1){
      addWarning("Unkown or ambiguous concept in toc: <conceptName>");
      return inlineError("unknown or ambiguous in toc: <conceptName>");
    }
    return markupToc1(options[0], level);
}

str markupToc1(ConceptName cn, int level){
    if(level <= 0)
       return "";
    res = "";
    for(ch <- children(cn)){
        syn  = getSynopsis(ch);
        res += li("<refToResolvedConcept(ch, true)>: <markupRestLine(syn)>") + markupToc1(ch, level - 1);
    }
    return ul(res);
}

// ---- Refer to a concept ----

private map[tuple[str, str], list[ConceptName]] resolveCache = ();

public list[ConceptName] resolveConcept(ConceptName course, str toConcept){
  try {
      return resolveCache[<course, toConcept>];
   } catch: ;
   
  //println("resolveConcept: <course>, <toConcept>");
  if(!exists(courseDir + course))
  	 return [];
  if(course == toConcept)
      return [course];
     //return [courseDir + course + "<course>.html"];
 
   courseConcepts = getCourseConcepts(course);
   lcToConcept = toLowerCase(toConcept);
   if(lcToConcept[0] != "/" && rootname(toConcept) != course)
      lcToConcept = "/" + lcToConcept; // Enforce match of whole concept name
   options = for(cn <- courseConcepts){
                 if(endsWith(toLowerCase(cn), lcToConcept))
                    append cn;
             }
   resolveCache[<course, toConcept>] = options;
   return options;
}

// Refer to a not yet resolved concept in a course.

public str refToUnresolvedConcept(ConceptName fromCourse, ConceptName toCourse, ConceptName toConcept, bool short){
  //println("refToUnresolvedConcept: <fromCourse>, <toCourse>, <toConcept>, <short>");
  options = resolveConcept(toCourse, toConcept);
  
  if(size(options) == 1){
     cn = options[0];
     courseTxt = (fromCourse == toCourse) ? "" : ((toConcept == toCourse) ? "" : "<toCourse>:");
     conceptTxt = short ? "<basename(toConcept)>" : "<toConcept>";
     //println("txt = <courseTxt><conceptTxt>");
     return  "\<a href=\"/<cn>/<basename(cn)>.html\"\><courseTxt><conceptTxt>\</a\>";  
  }     
  if(size(options) == 0){
     addWarning("Reference to unknown course or concept: <toCourse>:<toConcept>");
     return inlineError("unknown: <toCourse>:<toConcept>");
  }
  if(size(options) > 1){
     addWarning("Ambiguous reference to concept: <toCourse>:<toConcept>; 
                'Resolve with one of {<intercalate(", ", options)>}");
     return inlineError("ambiguous: <toCourse>:<toConcept>");
  }
}

// Refer to a resolved concept

public str refToResolvedConcept(ConceptName toConcept){
  return refToResolvedConcept(toConcept, false);
}

public str refToResolvedConcept(ConceptName toConcept, bool short){
  name = short ? basename(toConcept) : toConcept;
  return "\<a href=\"/<toConcept>/<basename(toConcept)>.html\"\><name>\</a\>";
}

// Refer to an external link

public str link(str url, str text){
  //println("link: <link>, <text>");
  return "\<a href=\"<url>\"\><(text=="")?url:text>\<img src=\"/images/www-icon.png\" with=\"20\" height=\"20\"\>\</a\>";
}

