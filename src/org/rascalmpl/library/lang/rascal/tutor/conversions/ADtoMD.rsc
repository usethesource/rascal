@synopsis{Temporary utility conversions for evolving the tutor syntax from AsciiDoc to Docusaurus Markdown}
module lang::rascal::tutor::conversions::ADtoMD

import util::FileSystem;
import IO;
import String;

void ad2md() {
    ad2md();
    ad2md();
}

void ad2md(loc root) {
    for (f <- find(root, isSourceFile))
      convertFile(f);
}

bool isSourceFile(loc f) = f.extension in {"md", "rsc"};

void convertFile(loc file) {
    println("converting: <file>");
    result=for (l <- readFileLines(file)) {
        append convertLine(l);
    }

    writeFileLines(file, result);
}

// [map functions]((Library:Map))
str convertLine(/<prefix:.*>link:\/<course:[A-Za-z0-9]+>#<concept:[A-Za-z0-9\-]+>\[<title:[^\]]*>\]<postfix:.*$>/) 
  = convertLine("<prefix>[<title>]((<trim(course)>:<trim(concept)>))<postfix>");

// [Why Rascal]((WhyRascal))
str convertLine(/<prefix:.*>link:\/<course:[A-Za-z0-9]+>\[<title:[^\]]*>\]<postfix:.*$>/)
  = convertLine("<prefix>[<title>]((<trim(course)>))<postfix>");

// ((Hello))
str convertLine(/<prefix:.*>\<\<<concept:[A-Za-z0-9\-]+>\>\><postfix:.*$>/)
  = convertLine("<prefix>((<trim(concept)>))<postfix>");

// [[Extraction-Workflow]]
// ![Extraction Workflow]((define-extraction.png))
// statement-parts.png[width="500px" style="float: right;" ,alt="Statement Types"]
str convertLine(/^<prefix:.*>image:[:]*<filename:[A-Za-z\-0-9]+>\.<ext:png|jpeg|jpg|svg>\[<properties:[^\]]*>\]<postfix:.*$>/)
  = convertLine("<prefix>![<extractTitle(properties)>]((<filename>.<ext>))<postfix>");

// ((String-GreaterThan))
str convertLine(/^<prefix:.*>\<\<<concept:[A-Za-z\-0-9\ ]+>,<title:[A-Za-z\-0-9\ ]+>\>\><postfix:.*$>/)
  = convertLine("<prefix>((<concept>))<postfix>");

// ((Pattern Matching))
str convertLine(/^<prefix:.*>\<\<<concept:[A-Za-z\-0-9\ ]+>\>\><postfix:.*$>/)
  = convertLine("<prefix>((<concept>))<postfix>");

str convertLine(/^<prefix:.*>loctoc::\[[0-9]+\]<postfix:.*$>/)
  = convertLine("<prefix>(((TOC)))<postfix>");

str convertLine(/^<prefix:.*>kbd:\[<keys:.*?>\]<postfix:.*$>/)
  = convertLine("<prefix>`<keys>`<postfix>");

str convertLine(/^\ \ \ \ \*\*<postfix:.*$>/)
  = convertLine("    *<postfix>");

// ((Library:Libraries
str convertLine(/^<prefix:.*>\(\(Libraries:<postfix:.*$>/)
  = convertLine("<prefix>((Library:<postfix>");

str convertLine(/^<prefix:.*>\(\(Library:Libraries-<postfix:.*$>/)
  = convertLine("<prefix>((Library:<postfix>");

// Library:Prelude-
str convertLine(/^<prefix:.*>\(\(Library:Prelude-<postfix:.*$>/)
  = convertLine("<prefix>((Library:<postfix>");

str convertLine(/^<prefix:.*>\(\(<pre:[^\)]+>-Prelude-<lst:[^\)\-]+>\)\)<postfix:.*>$/)
  = convertLine("<prefix>((<pre>-<lst>))<postfix>");

str convertLine(/^<prefix:.*>\(\(<pre:[^\)]+>-<fst:[^\)\-]+>-<lst:[^\)\-]+>\)\)<postfix:.*>$/)
  = convertLine("<prefix>((<pre>-<fst>))<postfix>") when lst == fst, fst != "Prelude";

// Rascal:Concepts-
str convertLine(/^<prefix:.*>\(\(Rascal:Concepts-<rest:[^)]+>\)\)<postfix:.*>$/)
  = convertLine("<prefix>((RascalConcepts:<rest>))<postfix>");

// italics within backquotes is not supported anymore. removing underscores for readability's sake
str convertLine(/^<prefix:.*>`<prequote:[^`]*>_<italics:[A-Za-z0-9~]+>_<postquote:[^`]*>`<postfix:.*$>/)
  = convertLine("<prefix>`<prequote><italics><postquote>`<postfix>");

// http://www.schemers.org/Documents/Standards/R5RS/HTML/r5rs-Z-H-7.html#%_sec_4.1.3[procedure call]
str convertLine(/^<prefix:.*>http\:\/\/<url:[^\[\(\)]+>\[<label:[^\]\(\)]+>\]<postfix:.*$>/)
  = convertLine("<prefix>[<label>](http://<url>)<postfix>");

default str convertLine(str line) = line;

str extractTitle(/title=\"<t:[^\"]+>\"/) = t;
default str extractTitle(str x) = "";
