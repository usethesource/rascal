module experiments::RascalTutor::HTMLUtils

// HTML utilities

import List;
import experiments::RascalTutor::CourseModel;

public str html(str head, str body) {
	return "\<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\"\n
            \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"\>\n
            \<html xmlns=\"http://www.w3.org/1999/xhtml\"\>\n
             <head>\n<body>\n\</html\>"
           ;
}

public str head(str txt) {
  return "\n\<head\><txt>\n\</head\>";
}
public str title(str txt) {
  return "\n\<title\><txt>\</title\>\n";
}

public str body(str txt) {
  return "\<body\>\n<txt>\n\</body\>";
}

public str h(int level, str txt) {
  return "\<h<level>\><txt>\</h<level>\>\n";
}

public str h1(str txt) {
  return h(1,txt);
}

public str h2(str txt) {
  return h(2, txt);
}

public str h3(str txt) {
  return h(3, txt);
}

public str hr(){
  return "\<hr\>\n";
}

public str p(str txt){
  return "\<p\><txt>\</p\>\n";
}

public str b(str txt){
  return "\<b\><txt>\</b\>";
}

public str i(str txt){
  return "\<i\><txt>\</i\>";
}

public str tt(str txt){
  return "\<tt\><txt>\</tt\>";
}

public str code(str txt){
  return "\<code\><txt>\</code\>";
}

public str blockquote(str txt){
  return "\<blockquote\><txt>\</blockquote\>";
}

public str br(){
  return "\<br/\>\n";
}

public str font(str color, str txt){
  return "\<font color=\"<color>\"\><txt>\</font\>";
}

public str li(str txt){
  return "\<li\><txt>\</li\>\n";
}

public str sub(str txt){
return "\<sub\><txt>\</sub\>";
}

public str sup(str txt){
return "\<sup\><txt>\</sup\>";
}

public str ul(str txt){
  return "\<ul\><txt>\</ul\>";
}

public str ol(str txt){
  return "\<ol\><txt>\</ol\>";
}

public str td(str txt, str align){
  return "\<td align=\"<align>\"\><txt>\</td\>";
}

public str tr(str txt){
  return "\<tr\><txt>\</tr\>";
}

public str table(str txt){
  return "\<table\><txt>\</table\>";
}

public str th(str txt, str align){
  return "\<th align=\"<align>\"\><txt>\</th\>";
}

public str col(str align){
  return "\<col align=\"<align>\" /\>";
}

public str pre(str class, str txt){
  return "\<pre class=\"<class>\"\><txt>\</pre\>";
}

public str sectionHead(str txt){
  return "\<span class=\"sectionHead\"\><txt>\</span\>";
}

public str escapeForRascal(str input){
  return 
    visit(input){
      case /^\</ => "\\\<"
      case /^\>/ => "\\\>"
      case /^"/  => "\\\""
      case /^'/  => "\\\'"
      case /^\\/ => "\\\\"
    };
}

public str escapeForHtml(str txt){
  return
    visit(txt){
      case /^\</ => "&lt;"
      case /^\>/ => "&gt;"
      case /^"/ => "&quot;"
      case /^&/ => "&amp;"
    }
}

public str escapeForJavascript(str txt){
  return
    visit(txt){
      case /^"/ => "\\\""
      case /^'/ => "\\\'"
      case /^\\/ => "\\\\"
    };
}

public str showConceptURL(ConceptName c, str name){
   return "\<a href=\"show?concept=<c>\"\><name>\</a\>";
}

public str showConceptPath(ConceptName cn){
  names = basenames(cn);
  return "<for(int i <- [0 .. size(names)-1]){><(i==0)?"":"/"><showConceptURL(compose(names, 0, i), names[i])><}>";
}



