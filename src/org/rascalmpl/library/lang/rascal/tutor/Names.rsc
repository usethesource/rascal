module lang::rascal::tutor::Names

import String;
import Location;
import List;
import IO;

data PathConfig(loc currentRoot = |unknown:///|, loc currentFile = |unknown:///|);
data Message(str cause="");

str localLink(loc root, loc concept) = (concept.file == "index.md" || concept.parent.file == concept[extension=""].file) 
  ? "/<capitalize(root.file)><relativize(root, concept.parent).path>"
  : "/<capitalize(root.file)><relativize(root, concept)[extension="md"]>"
  ;

str localDirLink(loc root, loc dir) 
  = "/<capitalize(root.file)><(relativize(root, dir) + "index.md").path>";  


str fragment(loc concept) = stripDoubleEnd("/<capitalize(concept[extension=""].path[1..])>");
str fragment(loc root, loc concept) = stripDoubleEnd(fragment(relativize(root, concept)));
str moduleFragment(str moduleName) = "<replaceAll(moduleName, "::", "/")>";

str stripDoubleEnd(/<prefix:.*>\/<a:[^\/]+>\/<b:[^\-]+>$/) = "<prefix>/<b>" when a == b;
default str stripDoubleEnd(str x) = x;

str removeSpaces(/^<prefix:.*><spaces:\s+><postfix:.*>$/) 
  = removeSpaces("<prefix><capitalize(postfix)>");

default str removeSpaces(str s) = s;

str addSpaces(/^<prefix:.*[a-z0-9]><postfix:[A-Z].+>/) =
  addSpaces("<prefix> <uncapitalize(postfix)>");

default str addSpaces(str s) = split("-", s)[-1];

@synopsis{produces `"../../.."` for pathToRoot(|aap:///a/b|, |aap:///a/b/c/d|)  }
str pathToRoot(loc root, loc src) 
  = "..<for (e <- split("/", relativize(root, src).path), e != "") {>/..<}>"
  when isDirectory(src);

str pathToRoot(loc root, loc src) 
  = pathToRoot(root, src.parent)
  when isFile(src);  