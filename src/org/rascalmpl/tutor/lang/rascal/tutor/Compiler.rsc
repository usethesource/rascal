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
  bin=|target://rascal-tutor/docs|,
  libs=[|lib://rascal|],
  srcs=[
    |project://rascal-tutor/src/lang/rascal/tutor/examples/Test|
  ]);

public list[Message] lastErrors = [];

public void defaultCompile(bool clean=false) {
  if (clean) {
    remove(defaultConfig.bin, recursive=true);
  }
  errors = compile(defaultConfig);

  for (e <- errors) {
    println("<e.at>: <e.msg><if (e.cause?) {>
            '    <e.cause><}>");
  }

  lastErrors = errors;
}

@synopsis{compiles each pcfg.srcs folder as a course root}
list[Message] compile(PathConfig pcfg, CommandExecutor exec = createExecutor(pcfg)) {
  ind = createConceptIndex(pcfg);
  
  if (pcfg.isPackageCourse) {
    generatePackageIndex(pcfg);
  }

  return [*compileCourse(src, pcfg[currentRoot=src], exec, ind) | src <- pcfg.srcs];
}

void generatePackageIndex(PathConfig pcfg) {
  targetFile = pcfg.bin + "Packages" + pcfg.packageName + "index.md";

  if (pcfg.license?) {
    writeFile(targetFile.parent + "License.md", 
      "---
      'title: <pcfg.packageName> open-source license
      '---
      '
      '<readFile(pcfg.license)>");
  }

  writeFile(targetFile,
    "---
    'title: <pcfg.packageName>
    '---
    '
    'This is the documentation for version <pcfg.packageVersion> of <pcfg.packageName>.
    '
    '<if (src <- pcfg.srcs, src.file in {"src", "rascal", "api"}) {>* [API documentation](../../Packages/<pcfg.packageName>/API)<}>
    '<for (src <- pcfg.srcs, src.file notin {"src", "rascal", "api"}) {>* [<capitalize(src.file)>](../../Packages/<pcfg.packageName>/<capitalize(src.file)>)
    '<}>* [Stackoverflow questions](https://stackoverflow.com/questions/tagged/rascal+<pcfg.packageName>)
    '<if (pcfg.license?) {>* [Open-source license](../../Packages/<pcfg.packageName>/License.md)<}>
    '<if (pcfg.sources?) {>* [Source code](<"<pcfg.sources>"[1..-1]>)<}>
    '<if (pcfg.issues?) {>* [Issue tracker](<"<pcfg.issues>"[1..-1]>)<}>
    '
    '#### Installation
    '
    'To use <pcfg.packageName> in a maven-based Rascal project, include the following dependency in the `pom.xml` file:
    '
    '```xml
    '\<dependencies\>
    '    \<dependency\>  
    '        \<groupId\><pcfg.packageGroup>\</groupId\>
    '        \<artifactId\><pcfg.packageName>\</artifactId\>
    '        \<version\><pcfg.packageVersion>\</version\>
    '    \</dependency\>
    '\</dependencies\> 
    '```
    '**and** change the `Require-Libraries` field in `/path/to/yourProjectName/META-INF/RASCAL.MF` like so:
    '
    '```MF
    'Manifest-Version: 0.0.1
    'Project-Name: yourProjectName
    'Source: path/to/src
    'Require-Libraries: |lib://<pcfg.packageName>|
    '
    '
    '```
    ':::info
    'dot.MF files _must_ end with an empty line.
    ':::
    ");
}

list[Message] compileCourse(loc root, PathConfig pcfg, CommandExecutor exec, Index ind) 
  = compileDirectory(root, pcfg[currentRoot=root], exec, ind);
  
list[Message] compile(loc src, PathConfig pcfg, CommandExecutor exec, Index ind, int sidebar_position=-1) {
    if (src in pcfg.ignores) {
      return [info("skipped ignored location: <src>", src)];
    }

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
          println("copying   <src> [Asset]");
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
    if (d in pcfg.ignores) {
      return [info("skipped ignored location: <d>", d)];
    }

    indexFiles = {(d + "<d.file>")[extension="md"], (d + "index.md")};

    if (!exists(d)) {
      return [warning("Course folder does not exist on disk: <d>", d)];
    }

    output = [];
    errors = [];
    nestedDtls = [];

    if (i <- indexFiles && exists(i)) {
      // this can only be a markdown file (see above)
      j=i;
      j.file = (j.file == j.parent[extension="md"].file) ? "index.md" : j.file;

      targetFile = pcfg.bin 
        + (pcfg.isPackageCourse ? "Packages/<pcfg.packageName>" : "")
        + ((pcfg.isPackageCourse && pcfg.currentRoot.file in {"src","rascal","api"}) ? "API" : capitalize(pcfg.currentRoot.file))
        + relativize(pcfg.currentRoot, j)[extension="md"].path;
      
      if (!exists(targetFile) || lastModified(i) > lastModified(targetFile)) {
        println("compiling <i> [Index Markdown]");
        output = compileMarkdown(i, pcfg[currentFile=i], exec, ind, sidebar_position=sidebar_position);
      
        writeFile(targetFile,
            "<for (line(x) <- output) {><x>
            '<}>"
        );

        if (details(list[str] xxx) <- output) {
          // here we give the details list declared in `details` header
          // on to compute the right sidebar_positions down for the nested
          // concepts
          nestedDtls = xxx;
        }

        errors = [e | err(e) <- output];
        if (errors != []) {
          writeBinaryValueFile(targetFile[extension="errors"], errors);    
        }
        else {
          remove(targetFile[extension="errors"]);
        }
      }
      else {
        println("reusing   <i>");
        if (exists(targetFile[extension="errors"])) {
          errors = readBinaryValueFile(#list[Message], targetFile[extension="errors"]);
        }
      }
    }
    else {
      generateIndexFile(d, pcfg, sidebar_position=sidebar_position);
    }

    return [
      *errors,
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
    p2r = pathToRoot(pcfg.currentRoot, d, pcfg.isPackageCourse);
    title = (d == pcfg.currentRoot && d.file in {"src","rascal","api"}) ? "API" : d.file;

    targetFile = pcfg.bin 
      + (pcfg.isPackageCourse ? "Packages/<pcfg.packageName>" : "")
      + ((pcfg.isPackageCourse && pcfg.currentRoot.file in {"src","rascal","api"}) ? "API" : capitalize(pcfg.currentRoot.file))
      + relativize(pcfg.currentRoot, d).path
      + "index.md"
      ;

    writeFile(targetFile,
      "---
      'title: <title>
      '<if (sidebar_position != -1) {>sidebar_position: <sidebar_position>
      '<}>---
      '
      '<for (e <- d.ls, isDirectory(e) || e.extension in {"rsc", "md"}, e.file != "internal") {>
      '* [<e[extension=""].file>](<p2r>/<if (pcfg.isPackageCourse) {>Packages/<pcfg.packageName>/<}><if (pcfg.isPackageCourse && pcfg.currentRoot.file in {"src","rascal","api"}) {>API<} else {><capitalize(pcfg.currentRoot.file)><}><relativize(pcfg.currentRoot, e)[extension=isDirectory(e)?"":"md"].path>)<}>");
    return [];
  } catch IO(msg): {
    return [error(msg, d)];
  } 
}

@synopsis{Translates Rascal source files to docusaurus markdown.} 
list[Message] compileRascalFile(loc m, PathConfig pcfg, CommandExecutor exec, Index ind) {
  loc targetFile = pcfg.bin 
        + (pcfg.isPackageCourse ? "Packages/<pcfg.packageName>" : "")
        + ((pcfg.isPackageCourse && pcfg.currentRoot.file in {"src","rascal","api"}) ? "API" : capitalize(pcfg.currentRoot.file))
        + relativize(pcfg.currentRoot, m)[extension="md"].path;
  errors = [];

  if (!exists(targetFile) || lastModified(targetFile) < lastModified(m)) {
    println("compiling <m> [Rascal Source File]");
    list[Output] output = generateAPIMarkdown(relativize(pcfg.currentRoot, m).parent.path, m, pcfg, exec, ind);

    writeFile(targetFile,
      "<for (line(x) <- output) {><x>
      '<}>"
    );

    errors = [e | err(e) <- output];
    if (errors != []) {
      writeBinaryValueFile(targetFile[extension="errors"], errors);
    }
    else {
      remove(targetFile[extension="errors"]);
    }
  }
  else {
    println("reusing   <m>");
    if (exists(targetFile[extension="errors"])) {
      errors = readBinaryValueFile(#list[Message], targetFile[extension=""]);
    }
  }

  return errors;
}

@synopsis{This uses another nested directory listing to construct information for the TOC embedded in the current document.}
list[str] createDetailsList(loc m, PathConfig pcfg) 
  = sort([ "<capitalize(pcfg.currentRoot.file)>:<if (isDirectory(d), !exists(d + "index.md"), !exists((d + d.file)[extension="md"])) {>package:<}><if (d.extension == "rsc") {>module:<}><replaceAll(relativize(pcfg.currentRoot, d)[extension=""].path[1..], "/", "-")>" | d <- m.parent.ls, m != d, d.file != "index.md", isDirectory(d) || d.extension in {"rsc", "md"}]);

list[Message] compileMarkdownFile(loc m, PathConfig pcfg, CommandExecutor exec, Index ind, int sidebar_position=-1) {
  order = createDetailsList(m, pcfg);

  // turn A/B/B.md into A/B/index.md for better URLs in the end result (`A/B/`` is better than `A/B/B.html`)
  m.file = (m.file == m.parent[extension="md"].file) ? "index.md" : m.file;

  loc targetFile = pcfg.bin 
        + (pcfg.isPackageCourse ? "Packages/<pcfg.packageName>" : "")
        + ((pcfg.isPackageCourse && pcfg.currentRoot.file in {"src","rascal","api"}) ? "API" : capitalize(pcfg.currentRoot.file))
        + relativize(pcfg.currentRoot, m)[extension="md"].path;

  errors = [];

  if (!exists(targetFile) || lastModified(m) > lastModified(targetFile)) {
    println("compiling <m> [Normal Markdown]");
    list[Output] output = compileMarkdown(m, pcfg[currentFile=m], exec, ind, order, sidebar_position=sidebar_position) + [Output::empty()];
   
    writeFile(targetFile,
        "<for (line(x) <- output) {><x>
        '<}>"
    );

    errors = [e | err(e) <- output];
    if (errors != []) {
      writeBinaryValueFile(targetFile[extension="errors"], errors);
    }
    return errors;
  }
  else {
    println("reusing   <m>");
    if (exists(targetFile[extension="errors"])) {
      // keep reporting the errors of the previous run, for clarity's sake
      return readBinaryValueFile(#list[Message], targetFile[extension="errors"]);
    }
  }

  return [];
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

@synopsis{Include Rascal REPL commands literally and execute them as side-effects in the REPL without reporting output unless there are unexpected errors.}
list[Output] compileMarkdown([str first:/^\s*```rascal-commands<rest1:.*>$/, *str block, /^\s*```/, *str rest2], int line, int offset, PathConfig pcfg, CommandExecutor exec, Index ind, list[str] dtls, int sidebar_position=-1) {
  str code = "<for (l <- block) {><l>
             '<}>";
  
  try {
    commands = ([start[Commands]] code).top.commands;
 
    if (/continue/ !:= rest1) {
      exec.reset();
    }

    stderr = "";

    for (EvalCommand c <- commands) {
      output = exec.eval("<c>");
      stderr += output["application/rascal+stderr"]?"";
    }

    return [ 
        Output::empty(), // must have an empty line
        out("```rascal <rest1>"),
        *[out(l) | l <- block],
        Output::empty(),
        out("```"),
        *[
          out(":::danger"),
          *[out(errLine) | errLine <- split("\n", stderr)],
          out(":::") 
          | /errors/ !:= rest1, filterErrors(stderr) != ""
        ], 
        *[err(error("rascal-declare block failed: <stderr>", pcfg.currentFile(offset, 1, <line, 0>, <line, 1>))) | filterErrors(stderr) != ""],
        *compileMarkdown(rest2, line + 1 + size(block) + 1, offset + length(first) + length(block), pcfg, exec, ind, dtls, sidebar_position=sidebar_position)
      ];
  }
  catch ParseError(x): {
    return [err(error("parse error in rascal-commands block: <x>", pcfg.currentFile(offset, 1, <line, 0>, <line, 1>)))];
  }
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
list[Output] compileMarkdown([/^<prefix:.*>\[<title:[^\]]*>\]\(\(<link:[A-Za-z0-9\-\ \t\.\:]+>\)\)<postfix:.*>$/, *str rest], int line, int offset, PathConfig pcfg, CommandExecutor exec, Index ind, list[str] dtls, int sidebar_position=-1) {
  resolution = ind[removeSpaces(link)];
  p2r = pathToRoot(pcfg.currentRoot, pcfg.currentFile, pcfg.isPackageCourse);

  if (trim(title) == "") {
    title = link;
  }
  
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
  p2r = pathToRoot(pcfg.currentRoot, pcfg.currentFile, pcfg.isPackageCourse);

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
      out("\<div class=\"theme-doc-version-badge\"\>rascal-<getRascalVersion()><if (pcfg.isPackageCourse) {>, <pcfg.packageName>-<pcfg.packageVersion><}>\</div\>"),
      *compileMarkdown(rest, line + 2 + size(header), offset + size(a) + size(b) + length(header), pcfg, exec, ind, dtls, sidebar_position=sidebar_position)
    ];
  } 
  catch value e: {
    switch(e) {
      //  case IllegalTypeArgument(str x, str y)     : e = "<x>, <y>";
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

@synopsis{Divide the work over sections to avoid stackoverflows}
list[Output] compileMarkdown([str first:/^\s*#+\s+<title:.*>$/, *str body, nextSection:/^\s*#+\s+.*$/, *str rest], int line, int offset, PathConfig pcfg, CommandExecutor exec, Index ind, list[str] dtls, int sidebar_position=-1) 
  = [
    *compileMarkdown([first, *body], line + 1, offset + length(first) + 1, pcfg, exec, ind, dtls, sidebar_position=sidebar_position),
    *compileMarkdown([nextSection, *rest], line + 1 + size(body), offset + length(first) + 1 + length(body), pcfg, exec, ind, dtls, sidebar_position=sidebar_position)
  ] when /\S/ <- body;

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
    shot   = output["application/rascal+screenshot"]?"";
    png    = output["image/png"]?"";

    if (filterErrors(stderr) != "" && /cancelled/ !:= stderr) {
      for (allowErrors, str errLine <- split("\n", stderr)) {
        append OUT : out(errLine);
      }

      if (!allowErrors) {
        append OUT : err(error("Code execution failed:
                               '    <stderr>", pcfg.currentFile(offset, 1, <lineOffset + lineOffsetHere, 0>, <lineOffset + lineOffsetHere, 1>), cause=stderr)); 
        append OUT : out("```");      
        append OUT : out(":::danger");
        append OUT : out("Rascal code execution failed (unexpectedly) during compilation of this documentation.");
        append OUT : out(":::");
        append OUT : out("```rascal-shell");
        for (errLine <- split("\n", stderr)) {
           append OUT : out(errLine);
        }
        append OUT : out("```");
      }
    }

    if (stdout != "") {
      for (outLine <- split("\n", stdout)[..500]) {
        append OUT : out("<outLine>");
      }
    }

    if (shot != "") {
      loc targetFile = pcfg.bin + "assets" + capitalize(pcfg.currentRoot.file) + relativize(pcfg.currentRoot, pcfg.currentFile)[extension=""].path;
      targetFile.file = targetFile.file + "_screenshot_<lineOffsetHere+lineOffset>.png";
      println("screenshot <targetFile>");
      writeBase64(targetFile, shot);
      append OUT: out("```");
      append OUT: out("![image](<relativize(pcfg.bin, targetFile).path>)");
      append OUT: out("```rascal-shell");
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
      append OUT : err(error("Code execution failed in prepare block:
                             '    <stderr>", pcfg.currentFile(offset, 1, <lineOffset + lineOffsetHere, 0>, <lineOffset + lineOffsetHere, 1>), cause=stderr)); 
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
  