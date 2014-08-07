module lang::java::style::CheckStyleRegressionTest

import lang::xml::DOM;
import IO;
import String;

rel[loc, int, str] getMessages(loc checkStyleXmlOutput) {
   txt = readFile(checkStyleXmlOutput);
   dom = parseXMLDOM(txt);
   r =  { <|file:///<fname>|, toInt(l), ch> 
        | /element(_, "file", cs:[*_,attribute(_,"name", fname),*_]) := dom
        , /e:element(_,"error",as) := cs
        , {*_,attribute(_,"source", /^.*\.<ch:[A-Za-z]*>Check$/),attribute(_,"line", l)} := {*as}};
   return r;
}
