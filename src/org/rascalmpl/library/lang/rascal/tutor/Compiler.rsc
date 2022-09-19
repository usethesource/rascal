@synopsis{compiles .rsc and .md files to markdown by executing Rascal-specific code and inlining its output}
@description{
  This compiler collects .rsc files and .md files from a PathConfig's srcs folders.
  
  Every .rsc file is compiled to a .md file with an outline of the declarations contained
  in the file and the contents of the @synopsis, @description, @pitfalls, @benefits, @examples
  tags with those declarations. @doc is also supported for backward compatibility's purposes.
  The resulting markdown is processed by the rest of the compiler, as if written by hand.

  Every .md file is scanned for rascal-shell between triple backticks elements. The contents between the backticks are
  executed by a private Rascal REPL and the output is captured in different ways. Normal IO
  via stderr and stdout is literally printed back and HTML or image output is inlined into 
  the document.

  For (nested) folders in the srcs folders, which do not contain an `index.md` file, or
  a <name>.md file where the name is equal to the name of the current folder, a fresh index.md
  file is generated which ${includes} all the (generated) .md files of the current folder and the index.md
  file of every nested folder.
}
module lang::rascal::tutor::Compiler

import Message;
import Exception;
import IO;
import String;
import Node;
import List;
import Relation;
import Location;
import ParseTree;
import util::Reflective;
import util::FileSystem;

import lang::xml::IO;
import lang::yaml::Model;
import lang::rascal::tutor::repl::TutorCommandExecutor;
import lang::rascal::tutor::apidoc::GenerateMarkdown;
import lang::rascal::tutor::apidoc::ExtractInfo;
import lang::rascal::tutor::Indexer;
import lang::rascal::tutor::Names;
import lang::rascal::tutor::Output;

data PathConfig(loc currentRoot = |unknown:///|, loc currentFile = |unknown:///|);
data Message(str cause="");


public PathConfig defaultConfig
  = pathConfig(
  bin=|target://rascal/doc|,
  srcs=[
    |project://rascal/src/org/rascalmpl/courses/Rascalopedia|,
    |project://rascal/src/org/rascalmpl/courses/CompileTimeErrors|,
    |project://rascal/src/org/rascalmpl/courses/RascalConcepts|,
    |project://rascal/src/org/rascalmpl/courses/Recipes|,
    |project://rascal/src/org/rascalmpl/courses/Tutor|,
    |project://rascal/src/org/rascalmpl/courses/GettingStarted|,
    |project://rascal/src/org/rascalmpl/courses/GettingHelp|,
    |project://rascal/src/org/rascalmpl/courses/WhyRascal|,
    |project://rascal/src/org/rascalmpl/courses/Rascal|,
    |project://rascal/src/org/rascalmpl/courses/TutorHome|,
    |project://rascal/src/org/rascalmpl/courses/RascalTests|,
    |project://rascal/src/org/rascalmpl/courses/RascalShell|,
    |project://rascal/src/org/rascalmpl/courses/RunTimeErrors|,
    |project://rascal/src/org/rascalmpl/courses/Developers|,
    |project://rascal/src/org/rascalmpl/library|
  ]);

public PathConfig onlyAPIconfig
  = defaultConfig[srcs=[|project://rascal/src/org/rascalmpl/library|]];

public list[Message] lastErrors = [];

public void defaultCompile() {
  remove(defaultConfig.bin, recursive=true);
  errors = compile(defaultConfig);

  for (e <- errors) {
    println("<e.at>: <e.msg><if (e.cause?) {>
            '    <e.cause><}>");
  }

  lastErrors = errors;
}

public void onlyAPICompile() {
 errors = compile(onlyAPIconfig);

  for (e <- errors) {
    println("<e.at>: <e.msg><if (e.cause?) {>
            '    <e.cause><}>");
  }

  lastErrors = errors;
}

@synopsis{compiles each pcfg.srcs folder as a course root}
list[Message] compile(PathConfig pcfg, CommandExecutor exec = createExecutor(pcfg)) {
  ind = createConceptIndex(pcfg);
  
  return [*compileCourse(src, pcfg[currentRoot=src], exec, ind) | src <- pcfg.srcs];
} 

list[Message] compileCourse(loc root, PathConfig pcfg, CommandExecutor exec, Index ind) 
  = compileDirectory(root, pcfg[currentRoot=root], exec, ind);
  
list[Message] compile(loc src, PathConfig pcfg, CommandExecutor exec, Index ind) {
    println("\rcompiling <src>");

    // new concept, new execution environment:
    exec.reset();

    if (isDirectory(src)) {
        return compileDirectory(src, pcfg, exec, ind);
    }
    else if (src.extension == "rsc") {
        return compileRascalFile(src, pcfg[currentFile=src], exec, ind);
    }
    else if (src.extension in {"md"}) {
        return compileMarkdownFile(src, pcfg, exec, ind);
    }
    else if (src.extension in {"png","jpg","svg","jpeg", "html", "js"}) {
        try {  
          copy(src, pcfg.bin + "assets" + capitalize(pcfg.currentRoot.file) + relativize(pcfg.currentRoot, src).path);
          
          return [];
        }
        catch IO(str message): {
            return [error(message, src)];
        }
    }
    else {
        return [];
    }
}

list[Message] compileDirectory(loc d, PathConfig pcfg, CommandExecutor exec, Index ind) {
    indexFiles = {(d + "<d.file>")[extension="md"], (d + "index.md")};

    return [
      *(((i <- indexFiles) && exists(i)) ? compile(i, pcfg, exec, ind) : generateIndexFile(d, pcfg)), 
      *[*compile(s, pcfg, exec, ind) | s <- d.ls, !(s in indexFiles), isDirectory(s) || s.extension in {"md","rsc"}]
    ];
}

list[Message] generateIndexFile(loc d, PathConfig pcfg) {
  try {
    writeFile(pcfg.bin + capitalize(pcfg.currentRoot.file) + relativize(pcfg.currentRoot, d).path + "index.md",
      "# <replaceAll(relativize(pcfg.currentRoot, d).path[1..], "/", "::")>
      '
      '<for (e <- d.ls, isDirectory(e) || e.extension in {"rsc", "md"}, e.file != "internal") {>
      '   * [<e[extension=""].file>](<capitalize(pcfg.currentRoot.file)><relativize(pcfg.currentRoot, e)[extension=isDirectory(e)?"":"md"].path>)<}>");
    return [];
  } catch IO(msg): {
    return [error(msg, d)];
  }
}

@synopsis{Translates Rascal source files to docusaurus markdown.} 
list[Message] compileRascalFile(loc m, PathConfig pcfg, CommandExecutor exec, Index ind) {
   list[Output] output = generateAPIMarkdown(relativize(pcfg.currentRoot, m).parent.path, m, pcfg, exec, ind);

   writeFile(pcfg.bin + capitalize(pcfg.currentRoot.file) + relativize(pcfg.currentRoot, m)[extension="md"].path,
      "<for (out(x) <- output) {><x>
      '<}>"
   );

   return [e | err(e) <- output];
}

@synopsis{This uses another nested directory listing to construct information for the TOC embedded in the current document.}
list[str] createDetailsList(loc m, PathConfig pcfg) 
  = sort([ "<pcfg.currentRoot.file>:<fragment(pcfg.currentRoot, d)[1..]>" | d <- m.parent.ls, isDirectory(d) || d.extension in {"rsc", "md"}]);

list[Message] compileMarkdownFile(loc m, PathConfig pcfg, CommandExecutor exec, Index ind) {
  order = createDetailsList(m, pcfg);

  list[Output] output = compileMarkdown(readFileLines(m), 1, 0, pcfg[currentFile=m], exec, ind, order) + [Output::empty()];

  // turn A/B/B.md into A/B/index.md for better URLs in the end result (`A/B/`` is better than `A/B/B.html`)
  m.file = (m.file == m.parent[extension="md"].file) ? "index.md" : m.file;

  writeFile(pcfg.bin + capitalize(pcfg.currentRoot.file) + relativize(pcfg.currentRoot, m)[extension="md"].path,
      "<for (out(x) <- output) {><x>
      '<}>"
  );

  return [e | err(e) <- output];
}

@synopsis{make sure to tag all section headers with the right fragment id for concept linking}
list[Output] compileMarkdown([str first:/^\s*#\s*<title:[^#].*>$/, *str rest], int line, int offset, PathConfig pcfg, CommandExecutor exec, Index ind, list[str] dtls)
  = [ out("## <title> {<fragment(pcfg.currentRoot, pcfg.currentFile)>}"),
      *compileMarkdown(rest, line + 1, offset + size(first), pcfg, exec, ind, dtls)
    ];

@synopsis{skip double quoted blocks}
list[Output] compileMarkdown([str first:/^\s*``````/, *block, str second:/^``````/, *str rest], int line, int offset, PathConfig pcfg, CommandExecutor exec, Index ind, list[str] dtls)
  = [ 
      out(first), 
      *[out(b) | b <-block], 
      out(second), 
      *compileMarkdown(rest, line, offset, pcfg, exec, ind, dtls)
  ];

@synopsis{execute _rascal-shell_ blocks on the REPL}
list[Output] compileMarkdown([str first:/^\s*```rascal-shell<rest1:.*>$/, *block, /^\s*```/, *str rest2], int line, int offset, PathConfig pcfg, CommandExecutor exec, Index ind, list[str] dtls)
  = [ Output::empty(), // must have an empty line
      out("```rascal-shell"),
      *compileRascalShell(block, /error/ := rest1, /continue/ := rest1, line+1, offset + size(first) + 1, pcfg, exec, ind),
      out("```"),
      *compileMarkdown(rest2, line + 1 + size(block) + 1, offset + size(first) + (0 | it + size(b) | b <- block), pcfg, exec, ind, dtls)
    ];

@synopsis{execute _rascal-shell-prepare_ blocks on the REPL}
list[Output] compileMarkdown([str first:/^\s*```rascal-prepare<rest1:.*>$/, *block, /^\s*```/, *str rest2], int line, int offset, PathConfig pcfg, CommandExecutor exec, Index ind, list[str] dtls)
  = [
      *compileRascalShellPrepare(block, /continue/ := rest1, line+1, offset + size(first) + 1, pcfg, exec, ind),
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

// @synopsis{inline files literally, in Rascal loc notation, but do not compile further from there. Works only if positioned on a line by itself.}
// list[Output] compileMarkdown([str first:/^\s*\(\(include\:\s+\|<url:[^\|]+>|<post:\(?[^\)]*?\)?>\)\)\s*$/, *str rest], int line, int offset, PathConfig pcfg, CommandExecutor exec, Index ind, list[str] dtls) {
//   try {
//     return [
//       *[out(l) | str l <- split("\n", readFile(readTextValueString(#loc, "|<url>|<post>")))],
//       *compileMarkdown(rest, line + 1, offset + size(first), pcfg, exec, ind, [])
//     ];
//   }
//   catch value x: {
//     return [
//       err(error("Could not read <url> for inclusion: <x>", pcfg.currentFile(offset, 1, <line, 1>, <line, 2>))),
//       *compileMarkdown(rest, line + 1, offset + size(first), pcfg, exec, ind, [])
//     ];
//   }

// }

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
      case {str plink, b:/<qlink:.*>\/index\.md/}:
         if (plink == qlink) {
            return compileMarkdown(["<prefix>[<addSpaces(link)>](<plink>)<postfix>", *rest], line, offset, pcfg, exec, ind, dtls); 
         }  else fail;
     
      case {_, _, *_}: {
        // ambiguous resolution, first try and resolve within the current course:
        if ({u} := ind["<capitalize(pcfg.currentRoot.file)>:<removeSpaces(link)>"]) {
          return compileMarkdown(["<prefix>[<addSpaces(link)>](<u>)<postfix>", *rest], line, offset, pcfg, exec, ind, dtls);
        }
        else if ({u} := ind["<capitalize(pcfg.currentRoot.file)>-<removeSpaces(link)>"]) {
          return compileMarkdown(["<prefix>[<addSpaces(link)>](<u>)<postfix>", *rest], line, offset, pcfg, exec, ind, dtls);
        }
        // or we check if its one of the details of the current concept
        else if ({u} := ind["<capitalize(pcfg.currentRoot.file)>:<fragment(pcfg.currentRoot, pcfg.currentFile)>-<removeSpaces(link)>"]) {
          return compileMarkdown(["<prefix>[<addSpaces(link)>](<u>)<postfix>", *rest], line, offset, pcfg, exec, ind, dtls);
        }

        return [
                  err(warning("Ambiguous concept link: <removeSpaces(link)> resolves to all of these: <for (r <- resolution) {><r> <}>", pcfg.currentFile(offset, 1, <line,0>,<line,1>),
                              cause="Please choose from the following options to disambiguate: <for (<str k, str v> <- rangeR(ind, ind[removeSpaces(link)]), {_} := ind[k]) {>
                                    '    <k> resolves to <v><}>")),
                  *compileMarkdown(["<prefix> **broken:<link> (ambiguous)** <postfix>", *rest], line, offset, pcfg, exec, ind, dtls)
              ];
      }
  }

  return [err(warning("Unexpected state of link resolution for <link>: <resolution>", pcfg.currentFile(offset, 1, <line,0>,<line,1>)))];
}

@synopsis{extract what's needed from the header and print it back}
list[Output] compileMarkdown([a:/^\-\-\-\s*$/, *str header, b:/^\-\-\-\s*$/, *str rest], int line, int offset, PathConfig pcfg, CommandExecutor exec, Index ind, list[str] dtls) {
  try {
    model = unsetRec(loadYAML(trim(intercalate("\n", header))));
    dtls = [dtl | mapping(m) := model, scalar(str dtl) <- (m[scalar("details")]?sequence([])).\list];

    if (dtls == []) {
      dtls = createDetailsList(pcfg.currentFile, pcfg);
    }

    return [
      details(dtls),
      out("---"),
      *[out(l) | l <- header],
      out("---"),
      *compileMarkdown(rest, line + 2 + size(header), offset + size(a) + size(b) + (0 | it + size(x) | x <- header), pcfg, exec, ind, dtls)
    ];
  } 
  catch value e: {
    switch(e) {
       case IllegalTypeArgument(str x, str y)     : e = "<x>, <y>";
       case IllegalArgument(value i)              : e = "<i>";
       case IO(str msg)                           : e = "<msg>";
       case Java(str class, str msg)              : e = "<class>: <msg>";
       case Java(str class, str msg, value cause) : e = "<class>: <msg>, caused by: <cause>";
    }

    return [
      err(error("Could not process YAML header: <e>", pcfg.currentFile)),
      out("---"),
      *[out(l) | l <- header],
      out("---"),
      *compileMarkdown(rest, line + 2 + size(header), offset + size(a) + size(b) + (0 | it + size(x) | x <- header), pcfg, exec, ind, dtls)
    ];
  }
}

@synopsis{Removes empty sections in the middle of a document}
list[Output] compileMarkdown([str first:/^\s*#+\s+<title:.*>$/, *str emptySection, nextSection:/^\s*#\s+.*$/, *str rest], int line, int offset, PathConfig pcfg, CommandExecutor exec, Index ind, list[str] dtls) 
  = compileMarkdown([nextSection, *rest], line + 1 + size(emptySection), offset + size(first) + (0 | it + size(l) | l <- emptySection), pcfg, exec, ind, dtls)
    when !(/\S/ <- emptySection);

@synopsis{Removes empty sections at the end of a document}
list[Output] compileMarkdown([str first:/^\s*#+\s+<title:.*>$/, *str emptySection, /^\s*$/], int line, int offset, PathConfig pcfg, CommandExecutor exec, Index ind, list[str] dtls) 
  = [] when !(/\S/ <- emptySection);


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

  lineOffsetHere = 0;
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
        append OUT : err(error("Code execution failed", pcfg.currentFile(offset, 1, <lineOffset + lineOffsetHere, 0>, <lineOffset + lineOffsetHere, 1>), cause=stderr)); 
        append OUT : out(":::");
        append OUT : Output::empty();
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
        append OUT : out("  <htmlLine>");
      }
      append OUT : out("\</div\>");
    }
    else if (result != "") {
      for (str resultLine <- split("\n", result)) {
        append OUT : out(resultLine);
      }
    } 

    lineOffsetHere +=1;
  }
}

@synopsis{Prepare blocks run the REPL but show no input or output}
list[Output] compileRascalShellPrepare(list[str] block, bool isContinued, int lineOffset, int offset, PathConfig pcfg, CommandExecutor exec, Index _) {
  if (!isContinued) {
    exec.reset();
  }

  lineOffsetHere = 0;
  return OUT:for (str line <- block) {
    output = exec.eval(line);
    result = output["text/plain"]?"";
    stderr = output["application/rascal+stderr"]?"";
    stdout = output["application/rascal+stdout"]?"";
    html   = output["text/html"]?"";

    if (filterErrors(stderr) != "" && /cancelled/ !:= stderr) {
      for (errLine <- split("\n", stderr)) {
        append OUT : out(errLine);
      }

      append out(":::danger");
      append OUT : out("Rascal code execution failed (unexpectedly) during compilation of this documentation.");
      append OUT : out("\<pre\>");
      for (errLine <- split("\n", stderr)) {
          append OUT : out(errLine);
      }
      append OUT : out("\</pre\>");
      append OUT : err(error("Code execution failed in prepare block", pcfg.currentFile(offset, 1, <lineOffset + lineOffsetHere, 0>, <lineOffset + lineOffsetHere, 1>), cause=stderr)); 
    }
     
    lineOffsetHere +=1;
  }
}

list[str] skipEmpty([/^s*$/, *str rest]) = skipEmpty(rest);
default list[str] skipEmpty(list[str] lst) = lst;



private str filterErrors(str errorStream) = intercalate("\n", filterErrors(split("\n", errorStream)));

private list[str] filterErrors([/^warning, ambiguity/, *str rest]) = filterErrors(rest);
private list[str] filterErrors([/^Generating parser/, *str rest]) = filterErrors(rest);
private default list[str] filterErrors([str head, *str tail]) = [head, *filterErrors(tail)];
private list[str] filterErrors([]) = [];



