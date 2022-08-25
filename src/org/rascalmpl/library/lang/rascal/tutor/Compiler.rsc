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
import util::Reflective;
import util::FileSystem;
import util::UUID;

import lang::xml::IO;
import lang::rascal::tutor::repl::TutorCommandExecutor;
import lang::rascal::tutor::apidoc::ExtractDoc;

data PathConfig(loc currentRoot = |unknown:///|, loc currentFile = |unknown:///|);

@synopsis{compiles each pcfg.srcs folder as a course root}
list[Message] compile(PathConfig pcfg, CommandExecutor exec = createExecutor(pcfg)) {
  ind = createConceptIndex(pcfg.srcs);
  return [*compileCourse(src, pcfg[currentRoot=src], exec, ind) | src <- pcfg.srcs];
} 

rel[str, str] createConceptIndex(list[loc] srcs) 
  = {*createConceptIndex(src) | src <- srcs};

rel[str, str] createConceptIndex(loc src)
  = { 
      <cf.file, fr>,
      *{<"<f.parent.parent.file>-<cf.file>", fr> | f.parent.path != "/", f.parent.file == cf.file, f.parent != src},
      <"<src.file>:<cf.file>", "/<src.file>.md<fr>">,
      *{<"<src.file>:<f.parent.parent.file>-<cf.file>", "/<src.file>.md<fr>"> | f.parent.path != "/", f.parent.file == cf.file}     
    | loc f <- find(src, isConceptFile), fr := fragment(src, f), cf := f[extension=""]
    }
  +
    { <"<f.parent.file>-<f.file>", "/assets/<unid.authority>.<f.extension>">,
      <f.file, "/assets/<unid.authority>.<f.extension>">
    |  loc f <- find(src, isImageFile), loc unid := uuid()
    }
    ;


list[Message] compileCourse(loc root, PathConfig pcfg, CommandExecutor exec, Index ind) {
  output = compileDirectory(root, pcfg, exec, ind);
  
  // write the output lines to disk (filtering errors)
  writeFile(
    (pcfg.bin + root.file)[extension="md"], 
    "<for (out(l) <- output) {><l>
    '<}>"
  );

  // return only the errors
  return [m | err(Message m) <- output];
}

data Output 
  = out(str content)
  | err(Message message)
  | details(list[str] subConcepts)
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
        return compileRascal(src, pcfg, exec, ind);
    }
    else if (src.extension in {"md", "concept"}) {
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
             + [err(warning("Concept <c> is missing from .Details", i)) | c <- d.ls, c.file notin order, isDirectory(c)];
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
    <tmp, i> = extractDoc(parent, m);

    return compileMarkdown(split("\n", tmp), 1, 0, pcfg, exec, ind);
}


list[Output] compileMarkdown(loc m, PathConfig pcfg, CommandExecutor exec, Index ind) 
  = compileMarkdown(readFileLines(m), 1, 0, pcfg[currentFile=m], exec, ind);

// [source,rascal-shell] --- block --- legacy syntax still supported for backward compatibility
list[Output] compileMarkdown([str first:/^\s*\[source,rascal-shell<rest1:.*>$/, /---/, *block, /---/, *str rest2], int line, int offset, PathConfig pcfg, CommandExecutor exec, Index ind)
  = [
      out("```rascal-shell"),
      *compileRascalShell(block, /,error/ := rest1, /continue/ := rest1, line+1, offset + size(first) + 1, pcfg, exec, ind),
      out("```"),
      *compileMarkdown(rest2, line + 1 + size(block) + 1, offset + size(first) + (0 | it + size(b) | b <- block), pcfg, exec, ind)
    ];

@synopsis{make sure to tag all section headers with the right fragment id for concept linking}
list[Output] compileMarkdown([str first:/^\s*#\s*<title:.*>$/, *str rest], int line, int offset, PathConfig pcfg, CommandExecutor exec, Index ind)
  = [ out("# <title> {<fragment(pcfg.currentRoot, pcfg.currentFile)>}"),
      *compileMarkdown(rest, line + 1, offset + size(first), pcfg, exec, ind)
    ];

list[Output] compileMarkdown([str first:/^\s*```rascal-shell<rest1:.*>$/, *block, /^\s*```/, *str rest2], int line, int offset, PathConfig pcfg, CommandExecutor exec, Index ind)
  = [
      out("```rascal-shell"),
      *compileRascalShell(block, /errors/ := rest1, /continue/ := rest1, line+1, offset + size(first) + 1, pcfg, exec, ind),
      out("```"),
      *compileMarkdown(rest2, line + 1 + size(block) + 1, offset + size(first) + (0 | it + size(b) | b <- block), pcfg, exec, ind)
    ];

list[Output] compileMarkdown([/^<prefix:.*>\(\(<link:[A-Za-z0-9\-\ \t\.]+>\)\)<postfix:.*>$/, *str rest], int line, int offset, PathConfig pcfg, CommandExecutor exec, Index ind) {
  resolution = ind[removeSpaces(link)];

  switch (resolution) {
      case {u}: 
        if (/\[<title:[A-Za-z-0-9\ ]*>\]$/ := prefix) {
          return compileMarkdown(["<prefix[..-(size(title)+2)]>[<title>](<u>)<postfix>", *rest], line, offset, pcfg, exec, ind);
        }
        else {
          return compileMarkdown(["<prefix>[<addSpaces(link)>](<u>)<postfix>", *rest], line, offset, pcfg, exec, ind);
        }
      case { }: 
        return [
                  err(error("Broken concept link: <link>", pcfg.currentFile(offset, 1, <line,0>,<line,1>))),
                  *compileMarkdown([":::caution\nBroken link <link>", "<prefix>_broken:<link>_<postfix>", *rest], line, offset, pcfg, exec, ind)
              ]; 
      case {_, _, *_}:
        return [
                  err(error("Ambiguous concept link: <link> resolves to all of <resolution>", pcfg.currentFile(offset, 1, <line,0>,<line,1>))),
                  *compileMarkdown([":::caution\nAmbiguous link <link>", "<prefix>_broken:<link>_<postfix>", *rest], line, offset, pcfg, exec, ind)
              ];
  }

  return [err(error("Unexpected state of link resolution for <link>: <resolution>", pcfg.currentFile(offset, 1, <line,0>,<line,1>)))];
}

 
   
// }

// this supports legacy headers like .Description and .Synopsis to help in the transition to docusaurus
list[Output] compileMarkdown([str first:/^\s*\.<title:[A-Z][a-z]*><rest:.*>/, *str rest2], int line, int offset, PathConfig pcfg, CommandExecutor exec, Index ind) 
  = [
      *[out("## <title> <rest>") | skipEmpty(rest2) != [] && [/^\s*\.[A-Z][a-z]*.*/, *_] !:= skipEmpty(rest2)],
      *compileMarkdown(rest2, line + 1, offset + size(first), pcfg, exec, ind)
    ]
    +
    [details(split(" ", trim(l))) | title == ".Details", [*str lines, /^\s*\.[A-Z][a-z]*/, *_] := rest2, l <- lines];

list[Output] compileMarkdown([], int _/*line*/, int _/*offset*/, PathConfig _, CommandExecutor _, Index _) = [];

default list[Output] compileMarkdown([str head, *str tail], int line, int offset, PathConfig pcfg, CommandExecutor exec, Index ind) 
  = [
      out(head),
      *compileMarkdown(tail, line + 1, offset + size(head) + 1, pcfg, exec, ind)
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

    if (stderr != "" && /cancelled/ !:= stderr) {
      append out(allowErrors ? ":::caution" : ":::danger");
      for (errLine <- split("\n", stderr)) {
        append OUT : out(trim(errLine));
      }

      if (!allowErrors) {
        append OUT : out("Rascal code execution failed unexpectedly during compilation of this documentation!");
        append OUT : err(error("Code execution failed", pcfg.currentFile(offset, 1, <lineOffset, 0>, <lineOffset, 1>))); 
      }

       append OUT : out(":::");
    }

    if (stdout != "") {
      append OUT : out(line);
      line = "";

      for (outLine <- split("\n", stdout)) {
        append OUT : out("\> <trim(outLine)>");
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

bool isConceptFile(loc f) = f.extension in {"md", "concept"};

bool isImageFile(loc f) = f.extension in {"png", "jpg", "svg", "jpeg"};

str fragment(loc concept) = replaceAll("#<concept[extension=""].path[1..]>", "/", "-");
str fragment(loc root, loc concept) = fragment(relativize(root, concept));

str removeSpaces(/^<prefix:.*><spaces:\s+><postfix:.*>$/) 
  = removeSpaces("<prefix><capitalize(postfix)>");

default str removeSpaces(str s) = s;

str addSpaces(/^<prefix:.*[a-z0-9]><postfix:[A-Z].+>/) =
  addSpaces("<prefix> <uncapitalize(postfix)>");

default str addSpaces(str s) = split("-", s)[-1];