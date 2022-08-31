@synopsis{Temporary utility conversions for evolving the tutor syntax from AsciiDoc to Docusaurus Markdown}
module lang::rascal::tutor::conversions::ADtoMD

import util::FileSystem;
import IO;
import String;

void ad2md(loc root) {
    for (f <- find(root, "md"))
      convertFile(f);
}

void convertFile(loc file) {
    println("converting: <file>");
    result=for (l <- readFileLines(file)) {
        append convertLine(l);
    }

    writeFileLines(file, result);
}

// [map functions]((Libraries:Prelude-Map))
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

// italics within backquotes is not supported anymore. removing underscores for readability's sake
str convertLine(/^<prefix:.*>`<prequote:[^`]*>_<italics:[A-Za-z0-9~]+>_<postquote:[^`]*>`<postfix:.*$>/)
  = convertLine("<prefix>`<prequote><italics><postquote>`<postfix>");

default str convertLine(str line) = line;

str extractTitle(/title=\"<t:[^\"]+>\"/) = t;
default str extractTitle(str x) = "";
