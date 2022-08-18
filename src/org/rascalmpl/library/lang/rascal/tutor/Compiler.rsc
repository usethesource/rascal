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

import lang::rascal::tutor::repl::TutorCommandExecutor;

data PathConfig(loc currentRoot = |unknown:///|, loc currentFile = |unknown:///|);

list[Message] compile(PathConfig pcfg=pathConfig(), CommandExecutor exec = createExecutor(pcfg))
  = [*compile(src, pcfg=pcfg[currentRoot=src], exec=exec) | src <- pcfg.srcs];

list[Message] compile(loc src, PathConfig pcfg=pathConfig(), CommandExecutor exec = createExecutor(pcfg)) {
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
            copy(src, pcfg.bin + relativize(pcfg.srcs[0], src).path);
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

list[Message] compileDirectory(loc d, PathConfig pcfg, CommandExecutor exec) {
    return [*compile(s, pcfg=pcfg, exec=exec) | s <- d.ls];
}
 
list[Message] compileRascal(loc m, PathConfig pcfg, CommandExecutor exec) {
    return [];
}

data Output 
  = out(str content)
  | err(Message message)
  ;

list[Message] compileMarkdown(loc m, PathConfig pcfg, CommandExecutor exec) {
  list[Output] output = compileMarkdown(readFileLines(m), m.begin.line, m.offset, pcfg[currentFile=m], exec);

  // write the output lines to disk (filtering error)
  writeFile(
    (pcfg.bin + relativize(pcfg.srcs[0], m))[extension="md"], 
    "<for (out(l) <- output) {><l>
    '<}>"
  );

   // return the errors, filtering the output:
   return [m | err(Message m) <- output];
}

list[Output] compileMarkdown([str line:/^\s*```rascal-shell<rest1:.*>$/, *block, /^\s*```/, *str rest2], int line, int offset, PathConfig pcfg, CommandExecutor exec)
  = [
      *compileRascalShell(block, /errors/ := rest1, /continued/ := rest1, line+1, offset + size(line) + 1, pcfg, exec),
      *compileMarkdown(rest2, line + 1 + size(block) + 1, offset + size(line) + (0 | it + size(b) | b <- block), pcfg, exec)
    ];

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
  
  return for (str line <- block) {
    append out("<exec.prompt><line>");
    
    output = exec.eval(line);
    result = output["text/plain"]?"";
    stderr = output["application/rascal+stderr"]?"";
    stdout = output["application/rascal+stdout"]?"";
    html   = output["text/html"]?"";

    if (stderr != "") {
      append out(allowErrors ? ":::caution" : ":::danger");
      for (errLine <- split("\n", stderr)) {
        append out(trim(errLine));
      }

      if (!allowErrors) {
        append out("Rascal code execution failed unexpectedly during compilation of this documentation!");
        append err(error("Code execution failed", pcfg.currentFile(offset, 1, <lineOffset, 0>, <lineOffset, 1>))); // TODO track source file
      }

       append out(":::");
    }

    if (stdout != "") {
      append out(line);
      line = "";

      for (outLine <- split("\n", stdout)) {
        append out("\> <trim(outLine)>");
      }
    }

    if (html != "") {
      // unwrap an iframe if this is an iframe
      if (/^\s*\<iframe\\s+src=\"http://localhost:<str port:[0-9]+>\"\\s*\>.*\</iframe\>\\s*$/ := html) {
        html = readFile(|http://localhost:<port>/index.html|);
      }

      // otherwise just inline the html
      append(out("\<div class=\"rascal-html-output\"\>"));
      for (htmlLineLine <- split("\n", html)) {
        append out("\> <trim(htmlLine)>");
      }
      append(out("\</div\>"));
    }
  }
}
