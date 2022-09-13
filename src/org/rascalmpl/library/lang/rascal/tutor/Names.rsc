module lang::rascal::tutor::Names

import String;
import Location;

str localLink(loc root, loc concept) = (concept.file == "index.md" || concept.parent.file == concept[extension=""].file) 
  ? "/<capitalize(root.file)><relativize(root, concept.parent).path>"
  : "/<capitalize(root.file)><relativize(root, concept)[extension="md"]>"
  ;

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