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

import lang::xml::IO;
import lang::rascal::tutor::repl::TutorCommandExecutor;
import lang::rascal::tutor::apidoc::ExtractDoc;

data PathConfig(loc currentRoot = |unknown:///|, loc currentFile = |unknown:///|);

@synopsis{compiles each pcfg.srcs folder as a course root}
list[Message] compile(PathConfig pcfg, CommandExecutor exec = createExecutor(pcfg)) {
  // TODO: create index for concept linking <<Concept-Name>> <<course:Concept-Name>> <<Parent-Concept-Name>> 
  return [*compileCourse(src, pcfg[currentRoot=src], exec) | src <- pcfg.srcs];
} 

list[Message] compileCourse(loc root, PathConfig pcfg, CommandExecutor exec) {
  output = compileDirectory(root, pcfg, exec);
  
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

list[Output] compile(loc src, PathConfig pcfg, CommandExecutor exec) {
    if (isDirectory(src)) {
        return compileDirectory(src, pcfg, exec);
    }
    else if (src.extension == "rsc") {
        return compileRascal(src, pcfg, exec);
    }
    else if (src.extension in {"md", "concept"}) {
        return compileMarkdown(src, pcfg, exec);
    }
    else if (src.extension in {"png","jpg","svg","jpeg", "html", "js"}) {
        try {
            copy(src, pcfg.bin + relativize(pcfg.currentRoot, src).path);
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

list[Output] compileDirectory(loc d, PathConfig pcfg, CommandExecutor exec) {
    indexFiles = {(d + "<d.file>")[extension="concept"], (d + "<d.file>")[extension="md"]};

    if (i <- indexFiles, exists(i)) {
      output = compile(i, pcfg, exec);
      order = [*x | details(x) <- output];

      if (order != []) {
        return output + [*compile(d + s, pcfg, exec) | s <- order]
             + [err(warning("Concept <c> is missing from .Details", i)) | c <- d.ls, c.file notin order, isDirectory(c)];
      }
      else {
        return output + [*compile(s, pcfg, exec) | s <- d.ls, s != i];
      }
    }
    else {
      return [*compile(s, pcfg, exec) | s <- d.ls];
    }
}

@synopsis{Translates Rascal source files to docusaurus markdown.} 
list[Output] compileRascal(loc m, PathConfig pcfg, CommandExecutor exec) {
    parent = relativize(pcfg.currentRoot, m).parent.path;
 
    // This is where error locations break. Have to wire the line
    // and offset and the Output model through the old `extractDoc` function
    // to fix that.
    <tmp, i> = extractDoc(parent, m);

    return compileMarkdown(split("\n", tmp), 1, 0, pcfg, exec);
}


list[Output] compileMarkdown(loc m, PathConfig pcfg, CommandExecutor exec) 
  = compileMarkdown(readFileLines(m), 1, 0, pcfg[currentFile=m], exec);

// [source,rascal-shell] --- block --- legacy syntax still supported for backward compatibility
list[Output] compileMarkdown([str first:/^\s*\[source,rascal-shell<rest1:.*>$/, /---/, *block, /---/, *str rest2], int line, int offset, PathConfig pcfg, CommandExecutor exec)
  = [
      out("```rascal-shell"),
      *compileRascalShell(block, /errors/ := rest1, /continue/ := rest1, line+1, offset + size(first) + 1, pcfg, exec),
      out("```"),
      *compileMarkdown(rest2, line + 1 + size(block) + 1, offset + size(first) + (0 | it + size(b) | b <- block), pcfg, exec)
    ];

list[Output] compileMarkdown([str first:/^\s*```rascal-shell<rest1:.*>$/, *block, /^\s*```/, *str rest2], int line, int offset, PathConfig pcfg, CommandExecutor exec)
  = [
      out("```rascal-shell"),
      *compileRascalShell(block, /errors/ := rest1, /continue/ := rest1, line+1, offset + size(first) + 1, pcfg, exec),
      out("```"),
      *compileMarkdown(rest2, line + 1 + size(block) + 1, offset + size(first) + (0 | it + size(b) | b <- block), pcfg, exec)
    ];

// this supports legacy headers like .Description and .Synopsis to help in the transition to docusaurus
list[Output] compileMarkdown([str first:/^\s*\.<title:[A-Z][a-z]*><rest:.*>/, *str rest2], int line, int offset, PathConfig pcfg, CommandExecutor exec) 
  = [
      *[out("## <title> <rest>") | skipEmpty(rest2) != [] && [/^\s*\.[A-Z][a-z]*.*/, *_] !:= skipEmpty(rest2)],
      *compileMarkdown(rest2, line + 1, offset + size(first), pcfg, exec)
    ]
    +
    [details(split(" ", trim(l))) | title == ".Details", [*str lines, /^\s*\.[A-Z][a-z]*/, *_] := rest2, l <- lines];

list[Output] compileMarkdown([], int _/*line*/, int _/*offset*/, PathConfig _, CommandExecutor _) = [];

default list[Output] compileMarkdown([str head, *str tail], int line, int offset, PathConfig pcfg, CommandExecutor exec) 
  = [
      out(head),
      *compileMarkdown(tail, line + 1, offset + size(head) + 1, pcfg, exec)
    ];

list[Output] compileRascalShell(list[str] block, bool allowErrors, bool isContinued, int lineOffset, int offset, PathConfig pcfg, CommandExecutor exec) {
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
