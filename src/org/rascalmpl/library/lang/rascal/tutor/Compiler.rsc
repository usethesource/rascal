@bootstrapParser
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
  a `<name>.md` file where the name is equal to the name of the current folder, a fresh index.md
  file is generated.
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
import ValueIO;

import lang::xml::IO;
import lang::yaml::Model;
import lang::rascal::tutor::repl::TutorCommandExecutor;
import lang::rascal::tutor::apidoc::GenerateMarkdown;
import lang::rascal::tutor::apidoc::ExtractInfo;
import lang::rascal::tutor::Indexer;
import lang::rascal::tutor::Names;
import lang::rascal::tutor::Output;
import lang::rascal::tutor::Includer;
import lang::rascal::\syntax::Rascal;




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

public void compileOne(loc root, loc src, PathConfig pcfg) {
  pcfg.currentRoot = root;
  pcfg.currentFile = src;
  inx = readConceptIndex(pcfg);

  errors = compile(src, pcfg, createExecutor(pcfg), inx);

  for (e <- errors) {
    println("<e.at>: <e.msg><if (e.cause?) {>
            '    <e.cause><}>");
  }
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
  
list[Message] compile(loc src, PathConfig pcfg, CommandExecutor exec, Index ind, int sidebar_position=-1) {
    println("\rcompiling <src>");

    // new concept, new execution environment:
    exec.reset();

    if (isDirectory(src), src.file != "internal") {
        return compileDirectory(src, pcfg, exec, ind, sidebar_position=sidebar_position);
    }
    else if (src.extension == "rsc") {
        return compileRascalFile(src, pcfg[currentFile=src], exec, ind);
    }
    else if (src.extension in {"md"}) {
        return compileMarkdownFile(src, pcfg, exec, ind, sidebar_position=sidebar_position);
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

list[Message] compileDirectory(loc d, PathConfig pcfg, CommandExecutor exec, Index ind, int sidebar_position=-1) {
    indexFiles = {(d + "<d.file>")[extension="md"], (d + "index.md")};

    if (!exists(d)) {
      return [error("Course does not exist <d>", d)];
    }

    output = [];
    nestedDtls = [];

    if (i <- indexFiles && exists(i)) {
      // this can only be a markdown file (see above)
      output = compileMarkdown(i, pcfg[currentFile=i], exec, ind, sidebar_position=sidebar_position);

      i.file = (i.file == i.parent[extension="md"].file) ? "index.md" : i.file;

      writeFile(pcfg.bin + capitalize(pcfg.currentRoot.file) + relativize(pcfg.currentRoot, i)[extension="md"].path,
          "<for (out(x) <- output) {><x>
          '<}>"
      );

      if (details(list[str] xxx) <- output) {
        // here we give the details list declared in `details` header
        // on to compute the right sidebar_positions down for the nested
        // concepts
        nestedDtls = xxx;
      }
    }
    else {
      generateIndexFile(d, pcfg, sidebar_position=sidebar_position);
    }

    return [
      *[e | err(e) <- output],
      *[*compile(s, pcfg, exec, ind, sidebar_position=sp) 
        | s <- d.ls
        , !(s in indexFiles)
        , isDirectory(s) || s.extension in {"md","rsc","png","jpg","svg","jpeg", "html", "js"}
        , int sp := indexOf(nestedDtls, capitalize(s[extension=""].file))
      ]
    ];
}

list[Message] generateIndexFile(loc d, PathConfig pcfg, int sidebar_position=-1) {
  try {
    p2r = pathToRoot(pcfg.currentRoot, d);
    title = replaceAll(relativize(pcfg.currentRoot, d).path[1..], "/", "::");
    writeFile(pcfg.bin + capitalize(pcfg.currentRoot.file) + relativize(pcfg.currentRoot, d).path + "index.md",
      "---
      'title: <if (trim(title) == "") {><capitalize(pcfg.currentRoot.file)><} else {><title><}>
      '<if (sidebar_position != -1) {>sidebar_position: <sidebar_position>
      '<}>---
      '
      '<for (e <- d.ls, isDirectory(e) || e.extension in {"rsc", "md"}, e.file != "internal") {>
      '* [<e[extension=""].file>](<p2r>/<capitalize(pcfg.currentRoot.file)><relativize(pcfg.currentRoot, e)[extension=isDirectory(e)?"":"md"].path>)<}>");
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
  = sort([ "<capitalize(pcfg.currentRoot.file)>:<if (isDirectory(d), !exists(d + "index.md"), !exists((d + d.file)[extension="md"])) {>package:<}><if (d.extension == "rsc") {>module:<}><replaceAll(relativize(pcfg.currentRoot, d)[extension=""].path[1..], "/", "-")>" | d <- m.parent.ls, m != d, d.file != "index.md", isDirectory(d) || d.extension in {"rsc", "md"}]);

list[Message] compileMarkdownFile(loc m, PathConfig pcfg, CommandExecutor exec, Index ind, int sidebar_position=-1) {
  order = createDetailsList(m, pcfg);

  list[Output] output = compileMarkdown(m, pcfg[currentFile=m], exec, ind, order, sidebar_position=sidebar_position) + [Output::empty()];

  // turn A/B/B.md into A/B/index.md for better URLs in the end result (`A/B/`` is better than `A/B/B.html`)
  m.file = (m.file == m.parent[extension="md"].file) ? "index.md" : m.file;

  writeFile(pcfg.bin + capitalize(pcfg.currentRoot.file) + relativize(pcfg.currentRoot, m)[extension="md"].path,
      "<for (out(x) <- output) {><x>
      '<}>"
  );

  return [e | err(e) <- output];
}

list[Output] compileMarkdown(loc m, PathConfig pcfg, CommandExecutor exec, Index ind, int sidebar_position=-1) {
  order = createDetailsList(m, pcfg);

  return compileMarkdown(readFileLines(m), 1, 0, pcfg[currentFile=m], exec, ind, order, sidebar_position=sidebar_position) + [Output::empty()];
}

@synopsis{Skip double quoted blocks}
list[Output] compileMarkdown([str first:/^\s*``````/, *block, str second:/^``````/, *str rest], int line, int offset, PathConfig pcfg, CommandExecutor exec, Index ind, list[str] dtls, int sidebar_position=-1)
  = [ 
      out(first), 
      *[out(b) | b <-block], 
      out(second), 
      *compileMarkdown(rest, line, offset, pcfg, exec, ind, dtls, sidebar_position=sidebar_position)
  ];

@synopsis{Include Rascal code from Rascal source files}
list[Output] compileMarkdown([str first:/^\s*```rascal-include<rest1:.*>$/, *str components, /^\s*```/, *str rest2], int line, int offset, PathConfig pcfg, CommandExecutor exec, Index ind, list[str] dtls, int sidebar_position=-1) {
  return[ 
      Output::empty(), // must have an empty line
      out("```rascal <rest1>"),
      *[*prepareModuleForInclusion(item, /includeHeaders/ := rest1, /includeTests/ := rest1, pcfg) | item <- components],
      Output::empty(),
      out("```"),
      *compileMarkdown(rest2, line + 1 + size(components) + 1, offset + length(first) + length(components), pcfg, exec, ind, dtls, sidebar_position=sidebar_position)
    ];
}

@synopsis{execute _rascal-shell_ blocks on the REPL}
list[Output] compileMarkdown([str first:/^\s*```rascal-shell<rest1:.*>$/, *block, /^\s*```/, *str rest2], int line, int offset, PathConfig pcfg, CommandExecutor exec, Index ind, list[str] dtls, int sidebar_position=-1)
  = [ Output::empty(), // must have an empty line
      out("```rascal-shell <rest1>"),
      *compileRascalShell(block, /error/ := rest1, /continue/ := rest1, line+1, offset + size(first) + 1, pcfg, exec, ind),
      out("```"),
      *compileMarkdown(rest2, line + 1 + size(block) + 1, offset + size(first) + length(block), pcfg, exec, ind, dtls, sidebar_position=sidebar_position)
    ];

@synopsis{execute _rascal-shell-prepare_ blocks on the REPL}
list[Output] compileMarkdown([str first:/^\s*```rascal-prepare<rest1:.*>$/, *block, /^\s*```/, *str rest2], int line, int offset, PathConfig pcfg, CommandExecutor exec, Index ind, list[str] dtls, int sidebar_position=-1)
  = [
      *compileRascalShellPrepare(block, /continue/ := rest1, line+1, offset + size(first) + 1, pcfg, exec, ind),
      *compileMarkdown(rest2, line + 1 + size(block) + 1, offset + size(first) + length(block), pcfg, exec, ind, dtls, sidebar_position=sidebar_position)
    ];

@synopsis{inline an itemized list of details (collected from the details YAML section in the header)}
list[Output] compileMarkdown([str first:/^\s*\(\(\(\s*TOC\s*\)\)\)\s*$/, *str rest], int line, int offset, PathConfig pcfg, CommandExecutor exec, Index ind, list[str] dtls, int sidebar_position=-1)
  = [
     *[*compileMarkdown(["* ((<d>))"], line, offset, pcfg, exec, ind, []) | d <- dtls],
     *compileMarkdown(rest, line + 1, offset + size(first), pcfg, exec, ind, [], sidebar_position=sidebar_position)
    ]
    +
    [
      err(warning("TOC is empty. details section is missing from header?", pcfg.currentFile(offset, 1, <line, 0>, <line, 1>)))
      | dtls == [] 
    ];

@synopsis{inline an itemized list of details (collected from the details YAML section in the header)}
list[Output] compileMarkdown([str first:/^\s*\(\(\(\s*TODO<msg:[^\)]*>\s*\)\)\)\s*$/, *str rest], int line, int offset, PathConfig pcfg, CommandExecutor exec, Index ind, list[str] dtls, int sidebar_position=-1)
  = [
     out(":::caution"),
     out("There is a \"TODO\" in the documentation source:"),
     out("\t<msg>"),
     out(first),
     out(":::"),
     err(warning("TODO: <trim(msg)>", pcfg.currentFile(offset, 1, <line, 0>, <line, 1>))),
     *compileMarkdown(rest, line + 1, offset + size(first), pcfg, exec, ind, [], sidebar_position=sidebar_position)
    ];

@synopsis{Inline example files literally, in Rascal loc notation, but do not compile further from there. Works only if positioned on a line by itself.}
list[Output] compileMarkdown([str first:/^\s*\(\(\|<url:[^\|]+>\|\)\)\s*$/, *str rest], int line, int offset, PathConfig pcfg, CommandExecutor exec, Index ind, list[str] dtls, int sidebar_position=-1) {
  try {
    return [
      *[out(l) | str l <- split("\n", readFile(readTextValueString(#loc, "|<url>|")))],
      *compileMarkdown(rest, line + 1, offset + size(first), pcfg, exec, ind, [], sidebar_position=sidebar_position)
    ];
  }
  catch value x: {
    return [
      err(error("Could not read <url> for inclusion: <x>", pcfg.currentFile(offset, 1, <line, 1>, <line, 2>))),
      *compileMarkdown(rest, line + 1, offset + size(first), pcfg, exec, ind, [], sidebar_position=sidebar_position)
    ];
  }
}

@synopsis{implement subscript syntax for [aeh-pr-vx] (the subscript alphabet is incomplete in unicode)}
list[Output] compileMarkdown([/^<prefix:.*>~<digits:[aeh-pr-vx0-9\(\)+\-]+>~<postfix:.*>$/, *str rest], int line, int offset, PathConfig pcfg, CommandExecutor exec, Index ind, list[str] dtls, int sidebar_position=-1) 
  = compileMarkdown(["<prefix><for (ch <- chars(digits)) {><subscripts["<char(ch)>"]><}><postfix>", *rest], line, offset, pcfg, exec, ind, dtls, sidebar_position=sidebar_position);

@synopsis{detect unsupported subscripts}
list[Output] compileMarkdown([/^<prefix:.*>~<digits:[^~]*[^aeh-pr-vx0-9]+[^~]*>~<postfix:.*>$/, *str rest], int line, int offset, PathConfig pcfg, CommandExecutor exec, Index ind, list[str] dtls, int sidebar_position=-1) 
  = [
    err(error("Unsupported subscript character in <digits>", pcfg.currentFile(offset, 1, <line, 1>, <line, 2>))),
    *compileMarkdown(["<prefix><digits><postfix>", *rest], line, offset, pcfg, exec, ind, dtls, sidebar_position=sidebar_position)
  ];

@synopsis{Resolve [labeled]((links))}
list[Output] compileMarkdown([/^<prefix:.*>\[<title:[^\]]+>\]\(\(<link:[A-Za-z0-9\-\ \t\.\:]+>\)\)<postfix:.*>$/, *str rest], int line, int offset, PathConfig pcfg, CommandExecutor exec, Index ind, list[str] dtls, int sidebar_position=-1) {
  resolution = ind[removeSpaces(link)];
  p2r = pathToRoot(pcfg.currentRoot, pcfg.currentFile);

  switch (resolution) {
      case {str u}: {
        u = /^\/assets/ := u ? u : "<p2r><u>";
        return compileMarkdown(["<prefix>[<title>](<u>)<postfix>", *rest], line, offset, pcfg, exec, ind, dtls, sidebar_position=sidebar_position);
      }
      case { }: {
        if (/^<firstWord:[A-Za-z0-9\-\.\:]+>\s+<secondWord:[A-Za-z0-9\-\.\:]+>/ := link) {
            // give this a second chance, in reverse
            return compileMarkdown(["<prefix>[<title>]((<secondWord>-<firstWord>))<postfix>", *rest], line, offset, pcfg, exec, ind, dtls, sidebar_position=sidebar_position);
        }

        return [
            err(error("Broken concept link: <link>", pcfg.currentFile(offset, 1, <line,0>,<line,1>))),
            *compileMarkdown(["<prefix>_(<title>) <link> (broken link)_<postfix>", *rest], line, offset, pcfg, exec, ind, dtls, sidebar_position=sidebar_position)
        ];
      }
      case {_, _, *_}: {
        // ambiguous resolution, first try and resolve within the current course:
        if ({str u} := ind["<capitalize(pcfg.currentRoot.file)>:<removeSpaces(link)>"]) {
          u = /^\/assets/ := u ? u : "<p2r><u>";
          return compileMarkdown(["<prefix>[<title>](<u>)<postfix>", *rest], line, offset, pcfg, exec, ind, dtls, sidebar_position=sidebar_position);
        }
        else if ({str u} := ind["<capitalize(pcfg.currentRoot.file)>-<removeSpaces(link)>"]) {
          u = /^\/assets/ := u ? u : "<p2r><u>";
          return compileMarkdown(["<prefix>[<title>](<u>)<postfix>", *rest], line, offset, pcfg, exec, ind, dtls, sidebar_position=sidebar_position);
        }
        // or we check if its one of the details of the current concept
        else if ({str u} := ind["<capitalize(pcfg.currentRoot.file)>:<fragment(pcfg.currentRoot, pcfg.currentFile)>-<removeSpaces(link)>"]) {
          u = /^\/assets/ := u ? u : "<p2r><u>";
          return compileMarkdown(["<prefix>[<title>](<u>)<postfix>", *rest], line, offset, pcfg, exec, ind, dtls, sidebar_position=sidebar_position);
        }

        return [
                  err(error("Ambiguous concept link: <removeSpaces(link)> resolves to all of these: <for (r <- resolution) {><r> <}>", pcfg.currentFile(offset, 1, <line,0>,<line,1>),
                              cause="Please choose from the following options to disambiguate: <for (<str k, str v> <- rangeR(ind, ind[removeSpaces(link)]), {_} := ind[k]) {>
                                    '    <k> resolves to <v><}>")),
                  *compileMarkdown(["<prefix> **broken:<link> (ambiguous)** <postfix>", *rest], line, offset, pcfg, exec, ind, dtls, sidebar_position=sidebar_position)
              ];
      }
  }

  return [err(error("Unexpected state of link resolution for <link>: <resolution>", pcfg.currentFile(offset, 1, <line,0>,<line,1>)))];
}

@synopsis{Resolve unlabeled links}
default list[Output] compileMarkdown([/^<prefix:.*>\(\(<link:[A-Za-z0-9\-\ \t\.\:]+>\)\)<postfix:.*>$/, *str rest], int line, int offset, PathConfig pcfg, CommandExecutor exec, Index ind, list[str] dtls, int sidebar_position=-1) {
  resolution = ind[removeSpaces(link)];
  p2r = pathToRoot(pcfg.currentRoot, pcfg.currentFile);

  switch (resolution) {
      case {u}: {
        u = /^\/assets/ := u ? u : "<p2r><u>";
        return compileMarkdown(["<prefix>[<addSpaces(link)>](<u>)<postfix>", *rest], line, offset, pcfg, exec, ind, dtls, sidebar_position=sidebar_position);
      }
      case { }: {
        if (/^<firstWord:[A-Za-z0-9\-\.\:]+>\s+<secondWord:[A-Za-z0-9\-\.\:]+>/ := link) {
            // give this a second chance, in reverse
            return compileMarkdown(["<prefix>((<secondWord>-<firstWord>))<postfix>", *rest], line, offset, pcfg, exec, ind, dtls, sidebar_position=sidebar_position);
        }

        return [
          err(error("Broken concept link: <link>", pcfg.currentFile(offset, 1, <line,0>,<line,1>))),
          *compileMarkdown(["<prefix>_<link> (broken link)_<postfix>", *rest], line, offset, pcfg, exec, ind, dtls, sidebar_position=sidebar_position)
        ];
      }
      case {str plink, /<qlink:.*>\/index\.md/}:
        if (plink == qlink) {
          return compileMarkdown(["<prefix>[<addSpaces(link)>](<p2r><plink>/)<postfix>", *rest], line, offset, pcfg, exec, ind, dtls, sidebar_position=sidebar_position); 
        }  
        else {
          fail;
        }
     
      case {_, _, *_}: {
        // ambiguous resolution, first try and resolve within the current course:
        if ({u} := ind["<capitalize(pcfg.currentRoot.file)>:<removeSpaces(link)>"]) {
          u = /^\/assets/ := u ? u : "<p2r><u>";
          return compileMarkdown(["<prefix>[./<addSpaces(link)>](<u>)<postfix>", *rest], line, offset, pcfg, exec, ind, dtls, sidebar_position=sidebar_position);
        }
        else if ({u} := ind["<capitalize(pcfg.currentRoot.file)>-<removeSpaces(link)>"]) {
          u = /^\/assets/ := u ? u : "<p2r><u>";
          return compileMarkdown(["<prefix>[<addSpaces(link)>](<u>)<postfix>", *rest], line, offset, pcfg, exec, ind, dtls, sidebar_position=sidebar_position);
        }
        // or we check if its one of the details of the current concept
        else if ({u} := ind["<capitalize(pcfg.currentRoot.file)>:<capitalize(pcfg.currentFile[extension=""].file)>-<removeSpaces(link)>"]) {
          u = /^\/assets/ := u ? u : "<p2r><u>";
          return compileMarkdown(["<prefix>[<addSpaces(link)>](<u>)<postfix>", *rest], line, offset, pcfg, exec, ind, dtls, sidebar_position=sidebar_position);
        }

        return [
            err(error("Ambiguous concept link: <removeSpaces(link)> resolves to all of these: <for (r <- resolution) {><r> <}>", pcfg.currentFile(offset, 1, <line,0>,<line,1>),
                      cause="Please choose from the following options to disambiguate: <for (<str k, str v> <- rangeR(ind, ind[removeSpaces(link)]), {_} := ind[k]) {>
                            '    <k> resolves to <v><}>")),
            *compileMarkdown(["<prefix> **broken:<link> (ambiguous)** <postfix>", *rest], line, offset, pcfg, exec, ind, dtls, sidebar_position=sidebar_position)
        ];
      }
  }

  return [err(error("Unexpected state of link resolution for <link>: <resolution>", pcfg.currentFile(offset, 1, <line,0>,<line,1>)))];
}

@synopsis{extract what's needed from the header and print it back, also set sidebar_position}
list[Output] compileMarkdown([a:/^\-\-\-\s*$/, *str header, b:/^\-\-\-\s*$/, *str rest], int line, int offset, PathConfig pcfg, CommandExecutor exec, Index ind, list[str] dtls, int sidebar_position=-1) {
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
      *[out("sidebar_position: <sidebar_position>") | sidebar_position != -1],
      out("---"),
      *compileMarkdown(rest, line + 2 + size(header), offset + size(a) + size(b) + length(header), pcfg, exec, ind, dtls, sidebar_position=sidebar_position)
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
      *compileMarkdown(rest, line + 2 + size(header), offset + size(a) + size(b) + length(header), pcfg, exec, ind, dtls, sidebar_position=sidebar_position)
    ];
  }
}

@synopsis{Removes empty sections in the middle of a document}
list[Output] compileMarkdown([str first:/^\s*#+\s+<title:.*>$/, *str emptySection, nextSection:/^\s*#+\s+.*$/, *str rest], int line, int offset, PathConfig pcfg, CommandExecutor exec, Index ind, list[str] dtls, int sidebar_position=-1) 
  = compileMarkdown([nextSection, *rest], line + 1 + size(emptySection), offset + size(first) + length(emptySection), pcfg, exec, ind, dtls, sidebar_position=sidebar_position)
    when !(/\S/ <- emptySection);

@synopsis{Removes empty sections at the end of a document}
list[Output] compileMarkdown([str first:/^\s*#+\s+<title:.*>$/, *str emptySection, /^\s*$/], int line, int offset, PathConfig pcfg, CommandExecutor exec, Index ind, list[str] dtls, int sidebar_position=-1) 
  = [] when !(/\S/ <- emptySection);

@synopsis{this is when we have processed all the input lines}
list[Output] compileMarkdown([], int _/*line*/, int _/*offset*/, PathConfig _, CommandExecutor _, Index _, list[str] _) = [];

@synopsis{all other lines are simply copied to the output stream}
default list[Output] compileMarkdown([str head, *str tail], int line, int offset, PathConfig pcfg, CommandExecutor exec, Index ind, list[str] dtls, int sidebar_position=-1) 
  = [
      out(head),
      *compileMarkdown(tail, line + 1, offset + size(head) + 1, pcfg, exec, ind, dtls, sidebar_position=sidebar_position)
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
      for (allowErrors, str errLine <- split("\n", stderr)) {
        append OUT : out(errLine);
      }

      if (!allowErrors) {
        append OUT : err(error("Code execution failed", pcfg.currentFile(offset, 1, <lineOffset + lineOffsetHere, 0>, <lineOffset + lineOffsetHere, 1>), cause=stderr)); 
        append OUT : out("```");      
        append OUT : out(":::danger");
        append OUT : out("Rascal code execution failed (unexpectedly) during compilation of this documentation.");
        append OUT : out(":::");
        append OUT : out("```rascal-shell");
        for (errLine <- split("\n", stderr)) {
           append OUT : out(errLine);
        }
      }
    }

    if (stdout != "") {
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

private int length(list[str] lines) = (0 | it + size(l) | str l <- lines);
private int length(str line) = size(line);

private map[str, str] subscripts 
  =  (  
        "0" : "\u2080",
        "1" : "\u2081",
        "2" : "\u2082",
        "3" : "\u2083",
        "4" : "\u2084",
        "5" : "\u2085",
        "6" : "\u2086",
        "7" : "\u2087",
        "8" : "\u2088",
        "9" : "\u2089",
        "+" : "\u208A",
        "-" : "\u208B",
        "(" : "\u208C",
        ")" : "\u208D",
        "a" : "\u2090",
        "e" : "\u2091",
        "h" : "\u2095",
        "i" : "\u1d62",
        "j" : "\u2c7c",
        "k" : "\u2096",
        "l" : "\u2097",
        "m" : "\u2098",
        "n" : "\u2099",
        "o" : "\u2092",
        "p" : "\u209a",
        "r" : "\u1d63",
        "s" : "\u209b",
        "t" : "\u209c",
        "u" : "\u1d64",
        "v" : "\u1d65",
        "x" : "\u2093",
        "A" : "\u2090",
        "E" : "\u2091",
        "H" : "\u2095",
        "I" : "\u1d62",
        "J" : "\u2c7c",
        "K" : "\u2096",
        "L" : "\u2097",
        "M" : "\u2098",
        "N" : "\u2099",
        "O" : "\u2092",
        "P" : "\u209a",
        "R" : "\u1d63",
        "S" : "\u209b",
        "T" : "\u209c",
        "U" : "\u1d64",
        "V" : "\u1d65",
        "X" : "\u2093"
  );