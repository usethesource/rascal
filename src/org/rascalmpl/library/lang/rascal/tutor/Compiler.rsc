@synopsis{compiles .rsc and .md files to markdown by executing Rascal-specific code and inlining its output}
@description{
  This compiler collects .rsc files and .md files from a PathConfig's srcs folders.
  
  Every .rsc file is compiled to a .md file with an outline of the declarations contained
  in the file and the contents of the @synopsis, @description, @pitfalls, @benefits, @examples
  tags with those declarations. @doc is also supported for backward compatibility's purposes.
  The resulting markdown is processed by the rest of the compiler, as if written by hand.

  Every .md file is scanned for ```rascal-shell elements. The contents between the ``` are
  executed by a private Rascal REPL and the output is captured in different ways. Normal IO
  via stderr and stdout is literally printed back and HTML or image output is inlined into 
  the document.

  For (nested) folders in the srcs folders, which do not contain an `index.md` file, or
  a <name>.md file where the name is equal to the name of the current folder, a fresh index.md
  file is generated which ${includes} all the (generated) .md files of the current folder and the index.md
  file of every nested folder.
}
module lang::rascal::tutor::Compiler

// THIS FILE IS UNDER CONSTRUCTION (FIRST VERSION)

import Message;
import Exception;
import IO;
import String;
import List;
import Location;
import ParseTree;
import util::Reflective;
import util::FileSystem;

import lang::xml::IO;
import lang::rascal::tutor::repl::TutorCommandExecutor;
import lang::rascal::tutor::apidoc::ExtractDoc;
import lang::rascal::tutor::apidoc::ExtractInfo;
import lang::rascal::tutor::apidoc::DeclarationInfo;
import lang::rascal::tutor::Indexer;
import lang::rascal::tutor::Names;

data PathConfig(loc currentRoot = |unknown:///|, loc currentFile = |unknown:///|);
data Message(str cause="");


public PathConfig defaultConfig
  = pathConfig(
  bin=|target://rascal/doc|,
  srcs=[
    |project://rascal/src/org/rascalmpl/courses/Rascalopedia|,
    |project://rascal/src/org/rascalmpl/courses/CompileTimeErrors|,
    |project://rascal/src/org/rascalmpl/courses/Test|,
    |project://rascal/src/org/rascalmpl/courses/RascalConcepts|,
    |project://rascal/src/org/rascalmpl/courses/TypePal|,
    |project://rascal/src/org/rascalmpl/courses/Recipes|,
    |project://rascal/src/org/rascalmpl/courses/Tutor|,
    |project://rascal/src/org/rascalmpl/courses/GettingStarted|,
    |project://rascal/src/org/rascalmpl/courses/WhyRascal|,
    |project://rascal/src/org/rascalmpl/courses/Rascal|,
    |project://rascal/src/org/rascalmpl/courses/TutorHome|,
    |project://rascal/src/org/rascalmpl/courses/RascalTests|,
    |project://rascal/src/org/rascalmpl/courses/RascalShell|,
    |project://rascal/src/org/rascalmpl/courses/RunTimeErrors|,
    |project://rascal/src/org/rascalmpl/courses/Developers|,
    |project://rascal/src/org/rascalmpl/library|
  ]);
public list[Message] lastErrors = [];

public void defaultCompile() {
  errors = compile(defaultConfig);

  for (e <- errors) {
    println("<e.at>: <e.msg>");
    if (e.cause?)
        println("
                '    <e.cause>"[1..]);
  }

  lastErrors = errors;
}

@synopsis{compiles each pcfg.srcs folder as a course root}
list[Message] compile(PathConfig pcfg, CommandExecutor exec = createExecutor(pcfg)) {
  ind = createConceptIndex(pcfg);
  
  return [*compileCourse(src, pcfg[currentRoot=src], exec, ind) | src <- pcfg.srcs];
} 

list[Message] compileCourse(loc root, PathConfig pcfg, CommandExecutor exec, Index ind) {
  output = compileDirectory(root, pcfg, exec, ind);
  issues = [m | err(Message m) <- output];

  // write the output lines to disk (filtering errors)
  writeFile(
    (pcfg.bin + capitalize(root.file))[extension="md"], 
    "<if (issues != []) {>## Document preparation issues
    '
    'The following issues have been detected while preparing this draft document. It is not ready for publication.
    '
    '<for (str severity(str msg, loc at) <- issues) {>1. [<severity>] <at.top>:<if (at.begin?) {><at.begin.line>,<at.begin.column><}><msg>
    '<}>
    '<}><for (out(l) <- output) {><l>
    '<}>"
  );

  // return only the errors
  return issues;
}

data Output 
  = out(str content)
  | err(Message message)
  | details(list[str] order)
  | search(list[str] contents, str fragment)
  ;

alias Index = rel[str reference, str url];

list[Output] compile(loc src, PathConfig pcfg, CommandExecutor exec, Index ind) {
    println("\rcompiling <src>");

    // new concept, new execution environment:
    exec.reset();

    if (isDirectory(src)) {
        return compileDirectory(src, pcfg, exec, ind);
    }
    else if (src.extension == "rsc") {
        return compileRascal(src, pcfg[currentFile=src], exec, ind);
    }
    else if (src.extension in {"md"}) {
        return compileMarkdown(src, pcfg, exec, ind);
    }
    else if (src.extension in {"png","jpg","svg","jpeg", "html", "js"}) {
        try {
            if (str path <- ind["<src.parent.file>-<src.file>"]) {
              copy(src, pcfg.bin + path);
            }
            return [];
        }
        catch IO(str message): {
            return [err(error(message, src))];
        }
    }
    else {
        return [];
    }
}

list[Output] compileDirectory(loc d, PathConfig pcfg, CommandExecutor exec, Index ind) {
    indexFiles = {(d + "<d.file>")[extension="concept"], (d + "<d.file>")[extension="md"]};

    if (i <- indexFiles, exists(i)) {
      output = compile(i, pcfg, exec, ind);
      order = [*x | details(x) <- output];

      if (order != []) {
        return output + [*compile(d + s, pcfg, exec, ind) | s <- order]
             + [err(warning("Concept <c> is missing from .Details: <order>", i)) | loc c <- d.ls, c.file notin order, isDirectory(c)]
             + [err(warning("Concept <c> from .Details is missing from file system: <files>", i)) | str c <- order, files := [e.file | loc e <- d.ls, isDirectory(e)], c notin files]
             ;
      }
      else {
        return output + [*compile(s, pcfg, exec, ind) | s <- d.ls, s != i];
      }
    }
    else {
      return [*compile(s, pcfg, exec, ind) | isDirectory(d), s <- d.ls];
    }
}

@synopsis{Translates Rascal source files to docusaurus markdown.} 
list[Output] compileRascal(loc m, PathConfig pcfg, CommandExecutor exec, Index ind) {
    parent = relativize(pcfg.currentRoot, m).parent.path;
 
    // This is where error locations break. Have to wire the line
    // and offset and the Output model through the old `extractDoc` function
    // to fix that.
    <tmp, inf> = extractDoc(parent, m);

    return compileMarkdown(split("\n", tmp), 1, 0, pcfg, exec, ind, ["<i.moduleName>::<i.name>" | DeclarationInfo i <- inf, i has name]);
}

list[Output] compileMarkdown(loc m, PathConfig pcfg, CommandExecutor exec, Index ind) 
  = compileMarkdown(readFileLines(m), 1, 0, pcfg[currentFile=m], exec, ind, 
    // this uses another nested directory listing to construct information for the (((TOC))) embedded in the current document:
    [ "<pcfg.currentRoot.file>:<fragment(pcfg.currentRoot, d)[1..]>" | d <- m.parent.ls, isDirectory(d), exists((d + d.file)[extension="md"])]) + [out("")];

@synopsis{make sure to tag all section headers with the right fragment id for concept linking}
list[Output] compileMarkdown([str first:/^\s*#\s*<title:[^#].*>$/, *str rest], int line, int offset, PathConfig pcfg, CommandExecutor exec, Index ind, list[str] dtls)
  = [ out("## <title> {<fragment(pcfg.currentRoot, pcfg.currentFile)>}"),
      *compileMarkdown(rest, line + 1, offset + size(first), pcfg, exec, ind, dtls)
    ];

@synopsis{execute _rascal-shell_ blocks on the REPL}
list[Output] compileMarkdown([str first:/^\s*```rascal-shell<rest1:.*>$/, *block, /^\s*```/, *str rest2], int line, int offset, PathConfig pcfg, CommandExecutor exec, Index ind, list[str] dtls)
  = [ out(""), // must have an empty line
      out("```rascal-shell"),
      *compileRascalShell(block, /error/ := rest1, /continue/ := rest1, line+1, offset + size(first) + 1, pcfg, exec, ind),
      out("```"),
      *compileMarkdown(rest2, line + 1 + size(block) + 1, offset + size(first) + (0 | it + size(b) | b <- block), pcfg, exec, ind, dtls)
    ];

@synopsis{inline an itemized list of details (collected from the .Details section)}
list[Output] compileMarkdown([str first:/^\s*\(\(\(\s*TOC\s*\)\)\)\s*$/, *str rest], int line, int offset, PathConfig pcfg, CommandExecutor exec, Index ind, list[str] dtls)
  = [
     *[*compileMarkdown(["* ((<d>))"], line, offset, pcfg, exec, ind, []) | d <- dtls],
     *compileMarkdown(rest, line + 1, offset + size(first), pcfg, exec, ind, [])
    ]
    +
    [
      err(warning("TOC is empty. .Details section is missing?", pcfg.currentFile(offset, 1, <line, 0>, <line, 1>)))
      | dtls == [] 
    ];

@synopsis{implement subscript syntax for letters and numbers and dashes and underscores}
list[Output] compileMarkdown([/^<prefix:.*>~<words:[A-Z0-9\-_0-9]+>~<postfix:.*>$/, *str rest], int line, int offset, PathConfig pcfg, CommandExecutor exec, Index ind, list[str] dtls) 
  = compileMarkdown(["<prefix>\<sub\><words>\</sub\><postfix>", *rest], line, offset, pcfg, exec, ind, dtls);

@synopsis{implement superscript syntax for letters and numbers and dashes and underscores}
list[Output] compileMarkdown([/^<prefix:.*>^<words:[A-Z0-9\-_0-9]+>^<postfix:.*>$/, *str rest], int line, int offset, PathConfig pcfg, CommandExecutor exec, Index ind, list[str] dtls) 
  = compileMarkdown(["<prefix>\<sup\><words>\</sup\><postfix>", *rest], line, offset, pcfg, exec, ind, dtls);

        
@synopsis{resolve ((links)) and [labeled]((links))}
list[Output] compileMarkdown([/^<prefix:.*>\(\(<link:[A-Za-z0-9\-\ \t\.\:]+>\)\)<postfix:.*>$/, *str rest], int line, int offset, PathConfig pcfg, CommandExecutor exec, Index ind, list[str] dtls) {
  resolution = ind[removeSpaces(link)];

  switch (resolution) {
      case {u}: 
        if (/\[<title:[A-Za-z-0-9\ ]*>\]$/ := prefix) {
          return compileMarkdown(["<prefix[..-(size(title)+2)]>[<title>](<u>)<postfix>", *rest], line, offset, pcfg, exec, ind, dtls);
        }
        else {
          return compileMarkdown(["<prefix>[<addSpaces(link)>](<u>)<postfix>", *rest], line, offset, pcfg, exec, ind, dtls);
        }
      case { }: {
        if (/^<firstWord:[A-Za-z0-9\-\.\:]+>\s+<secondWord:[A-Za-z0-9\-\.\:]+>/ := link) {
            // give this a second chance, in reverse
            return compileMarkdown(["<prefix>((<secondWord>-<firstWord>))<postfix>", *rest], line, offset, pcfg, exec, ind, dtls);
        }

        return [
                  err(warning("Broken concept link: <link>", pcfg.currentFile(offset, 1, <line,0>,<line,1>))),
                  *compileMarkdown(["<prefix>_<link> (broken link)_<postfix>", *rest], line, offset, pcfg, exec, ind, dtls)
              ];
      }
      case {a:/^#<link:.*$>/, b:/^\/[^#]+#<link2:.*$>/}:
         if (link == link2) {
            return compileMarkdown(["<prefix>[<addSpaces(link2)>](<b>)<postfix>", *rest], line, offset, pcfg, exec, ind, dtls); 
         }  else fail;
      case {_, _, *_}:
        return [
                  err(warning("Ambiguous concept link: <link> resolves to all of <resolution>", pcfg.currentFile(offset, 1, <line,0>,<line,1>))),
                  *compileMarkdown(["<prefix>_broken:<link> (ambiguous link)_<postfix>", *rest], line, offset, pcfg, exec, ind, dtls)
              ];
  }

  return [err(warning("Unexpected state of link resolution for <link>: <resolution>", pcfg.currentFile(offset, 1, <line,0>,<line,1>)))];
}

@synopsis{This supports legacy headers like .Description and .Synopsis to help in the transition to docusaurus}
list[Output] compileMarkdown([str first:/^\s*\.<title:[A-Z][a-z]*><rest:.*>/, *str rest2], int line, int offset, PathConfig pcfg, CommandExecutor exec, Index ind, list[str] dtls) {
  if (title == "Details" || /^##+\s*Details/ := title) {
    // details need to be collected and then skipped in the output
    if ([*str lines, str nextHeader:/^\s*\.[A-Z][a-z]*/, *str rest3] := rest2) {
       return [
        *[details([trim(word) | word <- split(" ", trim(l)), trim(word) != ""]) | l <- lines],
        *compileMarkdown([nextHeader, *rest3], line + 1 + size(lines), offset + size(first) + (0 | 1 + it | _ <- lines), pcfg, exec, ind, dtls)
       ];
    }
    else {
      return [err(error("Parsing input around .Details section failed somehow.", pcfg.currentFile(offset, 0, <line, 0>, <line, 1>)))];
    }
  }
  else if (title == "Index" || /^##+\sIndex/ := title) {
    if ([*str lines, str nextHeader:/^\s*\.[A-Z][a-z]*/, *str rest3] := rest2) {
       return [
        search(lines, fragment(pcfg.currentRoot, pcfg.currentFile)),
        *compileMarkdown([nextHeader, *rest3], line + 1 + size(lines), offset + size(first) + (0 | 1 + it | _ <- lines), pcfg, exec, ind, dtls)
       ];
    } else {
       return [err(error("Parsing input around .Index section failed somehow.", pcfg.currentFile(offset, 0, <line, 0>, <line, 1>)))];
    }
  }
  else {
    // just a normal section header 
    return [
      *[out("### <title> <rest>") | skipEmpty(rest2) != [] && [/^\s*\.[A-Z][a-z]*.*/, *_] !:= skipEmpty(rest2)],
      *compileMarkdown(rest2, line + 1, offset + size(first), pcfg, exec, ind, dtls)
    ];
  }
}

@synopsis{this is when we have processed all the input lines}
list[Output] compileMarkdown([], int _/*line*/, int _/*offset*/, PathConfig _, CommandExecutor _, Index _, list[str] _) = [];

@synopsis{all other lines are simply copied to the output stream}
default list[Output] compileMarkdown([str head, *str tail], int line, int offset, PathConfig pcfg, CommandExecutor exec, Index ind, list[str] dtls) 
  = [
      out(head),
      *compileMarkdown(tail, line + 1, offset + size(head) + 1, pcfg, exec, ind, dtls)
    ];

list[Output] compileRascalShell(list[str] block, bool allowErrors, bool isContinued, int lineOffset, int offset, PathConfig pcfg, CommandExecutor exec, Index _) {
  if (!isContinued) {
    exec.reset();
  }

  return OUT:for (str line <- block) {
    if (/^\s*\/\/<comment:.*>$/ := line) { // comment line
      append OUT : out("```");
      append OUT : out(trim(comment));
      append OUT : out("```rascal-shell");
      continue OUT;
    }
    append out("<exec.prompt()><line>");
    
    output = exec.eval(line);
    result = output["text/plain"]?"";
    stderr = output["application/rascal+stderr"]?"";
    stdout = output["application/rascal+stdout"]?"";
    html   = output["text/html"]?"";

    if (filterErrors(stderr) != "" && /cancelled/ !:= stderr) {
      for (allowErrors, errLine <- split("\n", stderr)) {
        append OUT : out(errLine);
      }

      if (!allowErrors) {
        append OUT : out("```");      
        append out(":::danger");
        append OUT : out("Rascal code execution failed (unexpectedly) during compilation of this documentation.");
        append OUT : out("\<pre\>");
        for (errLine <- split("\n", stderr)) {
           append OUT : out(errLine);
        }
        append OUT : out("\</pre\>");
        append OUT : err(error("Code execution failed", pcfg.currentFile(offset, 1, <lineOffset, 0>, <lineOffset, 1>), cause=stderr)); 
        append OUT : out(":::");
        append OUT : out("");
        append OUT : out("```rascal-shell");
      }
    }

    if (stdout != "") {
      append OUT : out(line);
      line = "";

      for (outLine <- split("\n", stdout)) {
        append OUT : out("<outLine>");
      }
    }

    if (html != "") {
      // unwrap an iframe if this is an iframe
      xml = readXML(html);

      if ("iframe"(_, src=/\"http:\/\/localhost:<port:[0-9]+>\"/) := xml) {
        html = readFile(|http://localhost:<port>/index.html|);
      }

      // otherwise just inline the html
      append(out("\<div class=\"rascal-html-output\"\>"));
      for (htmlLine <- split("\n", html)) {
        append OUT : out("\> <trim(htmlLine)>");
      }
      append OUT : out("\</div\>");
    }
    else if (result != "") {
      for (str resultLine <- split("\n", result)) {
        append OUT : out(trim(resultLine));
      }
    } 
  }
}

list[str] skipEmpty([/^s*$/, *str rest]) = skipEmpty(rest);
default list[str] skipEmpty(list[str] lst) = lst;



private str filterErrors(str errorStream) = intercalate("\n", filterErrors(split("\n", errorStream)));

private list[str] filterErrors([/^warning, ambiguity/, *str rest]) = filterErrors(rest);
private list[str] filterErrors([/^Generating parser/, *str rest]) = filterErrors(rest);
private default list[str] filterErrors([str head, *str tail]) = [head, *filterErrors(tail)];
private list[str] filterErrors([]) = [];



