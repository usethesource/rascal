module experiments::RascalTutor::HTMLGenerator

import experiments::RascalTutor::HTMLUtils;
import experiments::RascalTutor::CourseModel;

import String;
import ToString;
import IO;
import List;
import Scripting;

// Collect related concepts that occur in links.

private set[ConceptName] relatedConcepts = {};

private void addRelated(ConceptName cn){
  relatedConcepts += cn;
}

public set[ConceptName] getAndClearRelated(){
  r = relatedConcepts;
  relatedConcepts = {};
  return r;
}

// Allow for warnings in concepts

private list[str] warnings = [];
public list[str] getAndClearWarnings(){
  w = warnings;
  warnings = [];
  return w;
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
    case /^\<screen\>\s*<codeLines:.*>$/: {
      i += 1;
      start = i;
      while((i < n) && /^\<\/screen\>/ !:= lines[i]){
         codeLines += lines[i] + "\n";
         i += 1;
      }
      end = i - start;
      return <markupScreen(slice(lines, start, end)), skipOneNL(lines, i+1, n)>;
    }
    
    // listing from file
    case /^\<listing\s*<name:.+>\>$/: {
      loc L = |stdlib:///|[path = name];
      try {
      	codeLines = readFileLines(L);
      	return < markupListing(codeLines), skipOneNL(lines, i+1, n) >;
      } catch: return <"\<warning\>File <name> not found.\</warning\>", i + 1>;
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
      i += 1;
      codeLines = [];
      while((i < n) && /^\<\/figure\>/ !:= lines[i]){
         codeLines += lines[i];
         i += 1;
      }
      return < markupFigure(codeLines, file), skipOneNL(lines, i+1, n)  >;
    }
    
    // warning
    case /^\<warning\><txt:.*>\<\/warning\><rest:.*>$/:{
      warnings += txt;
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
      case /^\|<entry:[^\|]+>/: {entries += td(markupRestLine(entry), alignments[k] ? "left"); k += 1; insert "";}
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
    
    case /^`<c:[^`]*>`/ => code(markupCode(c))
    
    case /^\$<var:[A-Za-z]*><ext:[_\^A-Za-z0-9]*>\$/ => code(i(var) + markupSubs(ext))              
    
    case /^\[<text:[^\]]*>\]\(<url:[^)]+>\)/ => link(url, text)
    
    case /^\[<concept:[A-Za-z0-9\/]+>\]/: {addRelated(concept); insert show(concept); }
    
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
    
    case /^\/\*<dig:[0-9]>\*\//  => "\<img src=\"images/<dig>.png\"\>"
    
    case /^!\[<alt:[^\]]*>\]\(<file:[A-Za-z0-9\-\_\.\/]+\.png><opts:[^\)]*>\)/ => "\<img <getImgOpts(opts)> alt=\"<alt>\" src=\"<conceptPath>/<file>\"\>"
    
   };
}

// Subscripts and superscripts

private str markupSubs(str txt){
  return visit(txt){
    case /^_<subsup:[A-Za-z0-9]+>/  => sub(subsup) 
    case /^\^<subsup:[A-Za-z0-9]+>/ => sup(subsup)   
  }
}

// HTML to show a concept

private str show(str cn){
  return "\<a href=\"/show?concept=<cn>\"\><cn>\</a\>";
}

// HTML for an external link

public str link(str url, str text){
  return "\<a href=\"<url>\"\><(text=="")?url:text>\</a\>";
}

// Get options for image

private str getImgOpts(str txt){
  opts = "";
  visit(txt){
    case /^\s*\|\s*left/: {opts += "style=\"float: left;\" "; insert "";}
    case /^\s*\|\s*right/: {opts += "style=\"float: right;\" "; insert "";}
    case /^\s*\|\s*center/: {opts += "style=\"float: center;\" "; insert "";}
    case /^\s*\|\s*<N:[0-9]+>\s*px/: {opts += "width=\"<N>px\" height=\"<N>px\" "; insert ""; }
  }
  println("getImgOpts(<txt>) returns <opts>");
  return opts;
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
    case /^&/    => "&amp;"
    case /^\$\$/ => "$"
    case /^\$<var:[A-Za-z]*><ext:[_\^A-Za-z0-9]*>\$/ => i(var) + markupSubs(ext)
    case /^\/\*<dig:[0-9]>\*\// => "\<img src=\"images/<dig>.png\"\>"
  };
}

private str markupFigure(list[str] lines, str file){
  n = size(lines);
  str renderCall = lines[n-1];
  errors = "";
  if(/\s*render\(<arg:.*>\);/ := renderCall){
      // replace the render call by a call to renderSave
	  absPath = courseRoot.path + "../<conceptPath>/<file>";
	  println("absPath = <absPath>; conceptPath=<conceptPath>; file=<file>");
	  lines[n-1] = "renderSave(<arg>, |cwd://<absPath>|);";
	  out = shell(["import viz::Figure::Core;", 
	               "import viz::Figure::Render;"] + 
	              lines, 
	              5000);
	  println("**** shell output ****\n<out>");
	  errors = "";
	  for(line <- out)
	     if(/.*[Ee]rror/ := line)
	       errors += line;
	  println("errors = <errors>");
	  // Restore original call for listing
	  lines[n-1] = renderCall;
  } else
    errors = "Last line should be a call to \"render\"";
    
  if(errors != "")
    errors = "\<warning\>" + errors + "\</warning\>";
  return errors + markupListing(lines);
}

private str markupRascalPrompt(list[str] lines){
  return  "<for(str line <- lines){><visit(line){ case /^rascal\>/ => b("rascal\>") }>\n<}>";
}

// Do screen markup

private str markupScreen(list[str] lines){
   stripped_code = "<for(line <- lines){><(startsWith(line, "//")) ? "" : (line + "\n")><}>";
   result_lines = shell(stripped_code);
   
   int i = 0; int upbi = size(lines);
   int j = 0; int upbj = size(result_lines);
   pre_open = "\<pre class=\"screen\"\>";
   codeLines = pre_open;
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
           codeLines += "\</pre\>\n<markup(slice(lines, start, i - start))>\n<pre_open>";
         }
         if(i <upbi) {
         	codeLines += b(prompt) + limitWidth(lines[i], 80) + "\n";
         	i += 1; j += 1;
         }
         while(j < upbj && !startsWith(result_lines[j], prompt)){
           codeLines += limitWidth(result_lines[j], 80) + "\n";
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
           codeLines += "\</pre\>\n<markup(slice(lines, start, i - start))>\n<pre_open>";
         }

   }
   codeLines += "\</pre\>";
   return codeLines;
}

public str limitWidth(str txt, int limit){
  if(size(txt) < limit)
    return escapeForHtml(txt);
  return escapeForHtml(substring(txt, 0, limit)) + "&raquo;\n" + limitWidth(substring(txt, limit), limit);
}

// Extract serach terms from a code fragment

private set[str] searchTermsCode(str line){
  set[str] terms = {};
  visit(line){
    case /^\s+/: insert "";
    case /^\$[^\$]*\$/: insert "";
    case /^<kw:\w+>/: {terms += kw; println("kw = <kw>"); insert ""; }
    case /^<op:[^a-zA-Z\$\ \t]+>/: { terms += op; println("op = <op>"); insert ""; }
  }
  return terms;
}

// Collect search terms from the Synopsis-related entries in concept description

public set[str] searchTermsSynopsis(list[str] syn, list[str] tp, list[str] fn, list[str] synop){
  
  return (searchTerms(syn) - {"...", "...,"}) + 
         {t | str t <- searchTerms(tp), /\{\}\[\]\(\)[,]?/ !:= t, t notin {"...", "...,", ",...", ",...,"}};
         // TODO what do we do with searchTerms(fn), searchTerms(synop)?
}

private set[str]  searchTerms(list[str] lines){
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
         };
       }
       k += 1;
    };
    return terms;
}
