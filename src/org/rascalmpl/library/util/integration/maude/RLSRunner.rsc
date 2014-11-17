@license{
  Copyright (c) 2009-2013 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Mark Hills - Mark.Hills@cwi.nl (CWI)}
module util::integration::maude::RLSRunner

import IO;
import List;
import Message;
import ParseTree;
import String;
import util::ShellExec;
import vis::Figure;
import vis::Render;

@doc{RLSResult holds the result of running and RLS task. This type should be extended for more specific results.}
data RLSResult = NoResultHandler(str output) ;

@doc{Start running a Maude process without any specific .maude file.}
public PID startMaude(loc maudeLocation) {
    PID pid = createProcess(maudeLocation.path);
    res = readFrom(pid);
    while (/Maude\>\s*$/ !:= res) res = res + readFrom(pid); // consume any of the initial output before the prompt
    return pid;
}

@doc{Start running a Maude process with a specific .maude file.}
public PID startMaude(loc maudeLocation, loc maudeStartupFile) {
    PID pid = createProcess(maudeLocation.path,[maudeStartupFile.path]);
    res = readFrom(pid);
    while (/Maude\>\s*$/ !:= res) res = res + readFrom(pid); // consume any of the initial output before the prompt
    return pid;
}

@doc{Start running a Maude process with a specific .maude file in the given working directory.}
public PID startMaude(loc maudeLocation, loc maudeStartupFile, loc workingDir) {
    PID pid = createProcess(maudeLocation.path,[maudeStartupFile.path], workingDir);
    res = readFrom(pid);
    while (/Maude\>\s+$/ !:= res) res = res + readFrom(pid); // consume any of the initial output before the prompt 
    return pid;
}

@doc{Stop a running Maude process.}
public void stopMaude(PID pid) {
    killProcess(pid);
}

@doc{Structure to hold the information used to run a specific task.}
data RLSRunner = RLSRun(loc maudeFile, str(str,list[str]) pre, RLSResult(str) post);

@doc{Invoke a Maude task using the information in the RLS Runner.}
public RLSResult runRLSTask(loc maudeLocation, RLSRunner rlsRunner, str input...) {
    // Start up a new instance of Maude; this makes sure the running instance
    // is clean.
    PID pid = startMaude(maudeLocation,rlsRunner.maudeFile);
    
    // Prepare the input, which should be the term to evaluate (position 0)
    // and any added arguments, etc (positions 1..n)
    str inputStr = input[0];
    list[str] inputArgs = [ ];
    if (size(input) > 1) inputArgs = [ input[n] | n <- index(input)-0 ];
    
    // Pre-evaluate the input and arguments, getting back the complete term
    // to run in Maude.
    str toRun = (rlsRunner.pre)(inputStr,inputArgs);
    
    // Evaluate the term, waiting on the result
    writeFile(|file:///tmp/torun.maude|, toRun);
    writeTo(pid, toRun);
    str res = readFrom(pid);
    bool continueReading = true;
    while (continueReading) {
        if (/rewrites:\s*\d+/ !:= res && /Maude\>\s+$/ !:= res) 
            res = res + readFrom(pid);
        else
            continueReading = false;
    }
     
    // Post-evaluate the term, creating the appropriate RLS execution result
    RLSResult rlsRes = (rlsRunner.post)(res);
    
    // Shut down the current instance of Maude
    stopMaude(pid);
    
    return rlsRes;
}

@doc{Invoke a Maude task using the information in the RLS Runner.}
// TODO: Factor out similarities in the code, this is just a copy of the above with the dir added
public RLSResult runRLSTask(loc maudeLocation, RLSRunner rlsRunner, loc workingDir, str input...) {
    // Start up a new instance of Maude; this makes sure the running instance
    // is clean.
    PID pid = startMaude(maudeLocation,rlsRunner.maudeFile,workingDir);
    
    // Prepare the input, which should be the term to evaluate (position 0)
    // and any added arguments, etc (positions 1..n)
    str inputStr = input[0];
    list[str] inputArgs = [ ];
    if (size(input) > 1) inputArgs = [ input[n] | n <- index(input)-0 ];
    
    // Pre-evaluate the input and arguments, getting back the complete term
    // to run in Maude.
    str toRun = (rlsRunner.pre)(inputStr,inputArgs);
    
    // Evaluate the term, waiting on the result
    writeFile(|file:///tmp/torun.maude|, toRun);
    writeTo(pid, toRun);
    str res = readFrom(pid);
    bool continueReading = true;
    while (continueReading) {
        if (/rewrites:\s*\d+/ !:= res && /Maude\>\s+$/ !:= res) 
            res = res + readFrom(pid);
        else
            continueReading = false;
    }
     
    // Post-evaluate the term, creating the appropriate RLS execution result
    RLSResult rlsRes = (rlsRunner.post)(res);
    
    // Shut down the current instance of Maude
    stopMaude(pid);
    
    return rlsRes;
}

@doc{Performance information on an evaluation.}
data RLSPerf = RLSPerf(int rewrites, int cputime, int realtime, int rpers);

@doc{Pull out the string results from a standard Maude result message.}
public RLSResult processRLSResult(str res, RLSResult(RLSPerf,str) handler) {
    if (/rewrites:\s*<n1:\d+>\s*in\s*<n2:\d+>ms\s*cpu\s*[\(]<n3:\d+>ms real[\)]\s*[\(]<n4:\d+>\s*/ := res) {
        if (/result\s*String:\s<rs:.*>\s*Maude[\>]\s.*$/s := res) {
            return handler(RLSPerf(toInt(n1), toInt(n2), toInt(n3), toInt(n4)), rs);
        }
        if (/result\s*Char:\s<rs:.*>\s*Maude[\>]\s.*$/s := res) {
            return handler(RLSPerf(toInt(n1), toInt(n2), toInt(n3), toInt(n4)), rs);
        }
    }
    
    return NoResultHandler(res);
}

@doc{Convert newline sequences coded into strings into actual newlines}
public str unescape(str s) {
    while(/^<pre:.*?>[\\][n]<post:.*>$/s := s) s = "<pre>\n<post>";
    return s;
}

@doc{Display the results in a new tab with the given tab name}
public void displayResultsAsTab(str tabName, str res) {
    render(tabName, 
        vcat([
            space(text("",fontSize(14)),gap(5), vis::Figure::left()),
            space(text(substring(res,1,size(res)-2), fontSize(14), font("Courier New"), vis::Figure::left()), gap(5))
            ], vis::Figure::left()));
}

@doc{Display the results on the console}
public void displayResultsInConsole(str resultTag, str res) {
    println("<resultTag>\n<res>");
}

@doc{Convert a list into a Maude-formatted list}
public str listToMaudeList(list[&T] l, str unit) {
    if (size(l) == 0) return unit;
    return "_`,_(\"<head(l)>\",<listToMaudeList(tail(l), unit)>)";
}

@doc{Given a list of error messages in format:::uri-path::offset::length::start-row::start-col::end-row::end-col::message:::, return a set of Message items.}
public set[Message] createMessages(str info) {
    set[Message] errorMsgs = { };
    while (/\|\|<errorInfo:.+?>\|\|<rest:.*>$/ := info) {
        info = rest;
        if (/<severity:\d+>:::\|<path:.+?>::<offset:\d+>::<length:\d+>::<startRow:\d+>::<startCol:\d+>::<endRow:\d+>::<endCol:\d+>\|:::<errorMsg:.+>$/ := errorInfo) {
            loc errorLoc = |file:///dev/null|;
            errorLoc = errorLoc[uri=path][offset=toInt(offset)][length=toInt(length)];
            errorLoc = errorLoc[begin = <toInt(startRow), toInt(startCol)>][end = <toInt(endRow), toInt(endCol)>];
            if (severity == "1")
                errorMsgs = errorMsgs + error(errorMsg, errorLoc);
            else if (severity == "2")
                errorMsgs = errorMsgs + warning(errorMsg, errorLoc);
            else
                errorMsgs = errorMsgs + Message::info(errorInfo, errorLoc);
        }
    }
    return errorMsgs;
}
