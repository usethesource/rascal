module lang::html5::DOM

import List;

data HTML5Node
  = a(list[value] kids)
  | abbr(list[value] kids)
  | address(list[value] kids)
  | area(list[value] kids)
  | article(list[value] kids)
  | aside(list[value] kids)
  | audio(list[value] kids)
  | b(list[value] kids)
  | base(list[value] kids)
  | bdi(list[value] kids)
  | bdo(list[value] kids)
  | blockquote(list[value] kids)
  | body(list[value] kids)
  | br(list[value] kids)
  | button(list[value] kids)
  | canvas(list[value] kids)
  | caption(list[value] kids)
  | cite(list[value] kids)
  | code(list[value] kids)
  | col(list[value] kids)
  | colgroup(list[value] kids)
  | \data(list[value] kids)
  | datalist(list[value] kids)
  | dd(list[value] kids)
  | del(list[value] kids)
  | details(list[value] kids)
  | dfn(list[value] kids)
  | dialog(list[value] kids)
  | div(list[value] kids)
  | dl(list[value] kids)
  | dt(list[value] kids)
  | em(list[value] kids)
  | embed(list[value] kids)
  | fieldset(list[value] kids)
  | figcaption(list[value] kids)
  | figure(list[value] kids)
  | footer(list[value] kids)
  | form(list[value] kids)
  | h1(list[value] kids)
  | h2(list[value] kids)
  | h3(list[value] kids)
  | h4(list[value] kids)
  | h5(list[value] kids)
  | h6(list[value] kids)
  | head(list[value] kids)
  | header(list[value] kids)
  | hgroup(list[value] kids)
  | hr(list[value] kids)
  | html(list[value] kids)
  | i(list[value] kids)
  | iframe(list[value] kids)
  | img(list[value] kids)
  | input(list[value] kids)
  | ins(list[value] kids)
  | kbd(list[value] kids)
  | keygen(list[value] kids)
  | label(list[value] kids)
  | legend(list[value] kids)
  | li(list[value] kids)
  | link(list[value] kids)
  | main(list[value] kids)
  | \map(list[value] kids)
  | mark(list[value] kids)
  | menu(list[value] kids)
  | menuitem(list[value] kids)
  | meta(list[value] kids)
  | meter(list[value] kids)
  | nav(list[value] kids)
  | noscript(list[value] kids)
  | object(list[value] kids)
  | ol(list[value] kids)
  | optgroup(list[value] kids)
  | option(list[value] kids)
  | output(list[value] kids)
  | p(list[value] kids)
  | param(list[value] kids)
  | pre(list[value] kids)
  | progress(list[value] kids)
  | q(list[value] kids)
  | rp(list[value] kids)
  | rt(list[value] kids)
  | ruby(list[value] kids)
  | s(list[value] kids)
  | samp(list[value] kids)
  | script(list[value] kids)
  | section(list[value] kids)
  | select(list[value] kids)
  | small(list[value] kids)
  | source(list[value] kids)
  | span(list[value] kids)
  | strong(list[value] kids)
  | style(list[value] kids)
  | sub(list[value] kids)
  | summary(list[value] kids)
  | sup(list[value] kids)
  | table(list[value] kids)
  | tbody(list[value] kids)
  | td(list[value] kids)
  | template(list[value] kids)
  | textarea(list[value] kids)
  | tfoot(list[value] kids)
  | th(list[value] kids)
  | thead(list[value] kids)
  | time(list[value] kids)
  | title(list[value] kids)
  | tr(list[value] kids)
  | track(list[value] kids)
  | u(list[value] kids)
  | ul(list[value] kids)
  | var(list[value] kids)
  | video(list[value] kids)
  | wbr(list[value] kids)
  | a(set[HTML5Attr] attrs, list[value] kids)
  | abbr(set[HTML5Attr] attrs, list[value] kids)
  | address(set[HTML5Attr] attrs, list[value] kids)
  | area(set[HTML5Attr] attrs, list[value] kids)
  | article(set[HTML5Attr] attrs, list[value] kids)
  | aside(set[HTML5Attr] attrs, list[value] kids)
  | audio(set[HTML5Attr] attrs, list[value] kids)
  | b(set[HTML5Attr] attrs, list[value] kids)
  | base(set[HTML5Attr] attrs, list[value] kids)
  | bdi(set[HTML5Attr] attrs, list[value] kids)
  | bdo(set[HTML5Attr] attrs, list[value] kids)
  | blockquote(set[HTML5Attr] attrs, list[value] kids)
  | body(set[HTML5Attr] attrs, list[value] kids)
  | br(set[HTML5Attr] attrs, list[value] kids)
  | button(set[HTML5Attr] attrs, list[value] kids)
  | canvas(set[HTML5Attr] attrs, list[value] kids)
  | caption(set[HTML5Attr] attrs, list[value] kids)
  | cite(set[HTML5Attr] attrs, list[value] kids)
  | code(set[HTML5Attr] attrs, list[value] kids)
  | col(set[HTML5Attr] attrs, list[value] kids)
  | colgroup(set[HTML5Attr] attrs, list[value] kids)
  | \data(set[HTML5Attr] attrs, list[value] kids)
  | datalist(set[HTML5Attr] attrs, list[value] kids)
  | dd(set[HTML5Attr] attrs, list[value] kids)
  | del(set[HTML5Attr] attrs, list[value] kids)
  | details(set[HTML5Attr] attrs, list[value] kids)
  | dfn(set[HTML5Attr] attrs, list[value] kids)
  | dialog(set[HTML5Attr] attrs, list[value] kids)
  | div(set[HTML5Attr] attrs, list[value] kids)
  | dl(set[HTML5Attr] attrs, list[value] kids)
  | dt(set[HTML5Attr] attrs, list[value] kids)
  | em(set[HTML5Attr] attrs, list[value] kids)
  | embed(set[HTML5Attr] attrs, list[value] kids)
  | fieldset(set[HTML5Attr] attrs, list[value] kids)
  | figcaption(set[HTML5Attr] attrs, list[value] kids)
  | figure(set[HTML5Attr] attrs, list[value] kids)
  | footer(set[HTML5Attr] attrs, list[value] kids)
  | form(set[HTML5Attr] attrs, list[value] kids)
  | h1(set[HTML5Attr] attrs, list[value] kids)
  | h2(set[HTML5Attr] attrs, list[value] kids)
  | h3(set[HTML5Attr] attrs, list[value] kids)
  | h4(set[HTML5Attr] attrs, list[value] kids)
  | h5(set[HTML5Attr] attrs, list[value] kids)
  | h6(set[HTML5Attr] attrs, list[value] kids)
  | head(set[HTML5Attr] attrs, list[value] kids)
  | header(set[HTML5Attr] attrs, list[value] kids)
  | hgroup(set[HTML5Attr] attrs, list[value] kids)
  | hr(set[HTML5Attr] attrs, list[value] kids)
  | html(set[HTML5Attr] attrs, list[value] kids)
  | i(set[HTML5Attr] attrs, list[value] kids)
  | iframe(set[HTML5Attr] attrs, list[value] kids)
  | img(set[HTML5Attr] attrs, list[value] kids)
  | input(set[HTML5Attr] attrs, list[value] kids)
  | ins(set[HTML5Attr] attrs, list[value] kids)
  | kbd(set[HTML5Attr] attrs, list[value] kids)
  | keygen(set[HTML5Attr] attrs, list[value] kids)
  | label(set[HTML5Attr] attrs, list[value] kids)
  | legend(set[HTML5Attr] attrs, list[value] kids)
  | li(set[HTML5Attr] attrs, list[value] kids)
  | link(set[HTML5Attr] attrs, list[value] kids)
  | main(set[HTML5Attr] attrs, list[value] kids)
  | \map(set[HTML5Attr] attrs, list[value] kids)
  | mark(set[HTML5Attr] attrs, list[value] kids)
  | menu(set[HTML5Attr] attrs, list[value] kids)
  | menuitem(set[HTML5Attr] attrs, list[value] kids)
  | meta(set[HTML5Attr] attrs, list[value] kids)
  | meter(set[HTML5Attr] attrs, list[value] kids)
  | nav(set[HTML5Attr] attrs, list[value] kids)
  | noscript(set[HTML5Attr] attrs, list[value] kids)
  | object(set[HTML5Attr] attrs, list[value] kids)
  | ol(set[HTML5Attr] attrs, list[value] kids)
  | optgroup(set[HTML5Attr] attrs, list[value] kids)
  | option(set[HTML5Attr] attrs, list[value] kids)
  | output(set[HTML5Attr] attrs, list[value] kids)
  | p(set[HTML5Attr] attrs, list[value] kids)
  | param(set[HTML5Attr] attrs, list[value] kids)
  | pre(set[HTML5Attr] attrs, list[value] kids)
  | progress(set[HTML5Attr] attrs, list[value] kids)
  | q(set[HTML5Attr] attrs, list[value] kids)
  | rp(set[HTML5Attr] attrs, list[value] kids)
  | rt(set[HTML5Attr] attrs, list[value] kids)
  | ruby(set[HTML5Attr] attrs, list[value] kids)
  | s(set[HTML5Attr] attrs, list[value] kids)
  | samp(set[HTML5Attr] attrs, list[value] kids)
  | script(set[HTML5Attr] attrs, list[value] kids)
  | section(set[HTML5Attr] attrs, list[value] kids)
  | select(set[HTML5Attr] attrs, list[value] kids)
  | small(set[HTML5Attr] attrs, list[value] kids)
  | source(set[HTML5Attr] attrs, list[value] kids)
  | span(set[HTML5Attr] attrs, list[value] kids)
  | strong(set[HTML5Attr] attrs, list[value] kids)
  | style(set[HTML5Attr] attrs, list[value] kids)
  | sub(set[HTML5Attr] attrs, list[value] kids)
  | summary(set[HTML5Attr] attrs, list[value] kids)
  | sup(set[HTML5Attr] attrs, list[value] kids)
  | table(set[HTML5Attr] attrs, list[value] kids)
  | tbody(set[HTML5Attr] attrs, list[value] kids)
  | td(set[HTML5Attr] attrs, list[value] kids)
  | template(set[HTML5Attr] attrs, list[value] kids)
  | textarea(set[HTML5Attr] attrs, list[value] kids)
  | tfoot(set[HTML5Attr] attrs, list[value] kids)
  | th(set[HTML5Attr] attrs, list[value] kids)
  | thead(set[HTML5Attr] attrs, list[value] kids)
  | time(set[HTML5Attr] attrs, list[value] kids)
  | title(set[HTML5Attr] attrs, list[value] kids)
  | tr(set[HTML5Attr] attrs, list[value] kids)
  | track(set[HTML5Attr] attrs, list[value] kids)
  | u(set[HTML5Attr] attrs, list[value] kids)
  | ul(set[HTML5Attr] attrs, list[value] kids)
  | var(set[HTML5Attr] attrs, list[value] kids)
  | video(set[HTML5Attr] attrs, list[value] kids)
  | wbr(set[HTML5Attr] attrs, list[value] kids)
  ; 

data HTML5Attr
  = abbr(value val)
  | about(value val)
  | accept(value val)
  | accesskey(value val)
  | action(value val)
  | align(value val)
  | allowfullscreen(value val)
  | alt(value val)
  | aria(value val)
  | async(value val)
  | autocomplete(value val)
  | autofocus(value val)
  | autoplay(value val)
  | border(value val)
  | challenge(value val)
  | char(value val)
  | charset(value val)
  | checked(value val)
  | cite(value val)
  | class(value val)
  | cols(value val)
  | colspan(value val)
  | command(value val)
  | content(value val)
  | contenteditable(value val)
  | contextmenu(value val)
  | controls(value val)
  | coords(value val)
  | \data(value val)
  | datatype(value val)
  | \datetime(value val)
  | \default(value val)
  | defer(value val)
  | dir(value val)
  | dirname(value val)
  | disabled(value val)
  | download(value val)
  | draggable(value val)
  | dropzone(value val)
  | enctype(value val)
  | \for(value val)
  | form(value val)
  | formaction(value val)
  | formenctype(value val)
  | formmethod(value val)
  | formnovalidate(value val)
  | formtarget(value val)
  | headers(value val)
  | height(value val)
  | hidden(value val)
  | high(value val)
  | href(value val)
  | hreflang(value val)
  | http(value val)
  | icon(value val)
  | id(value val)
  | inlist(value val)
  | ismap(value val)
  | itemid(value val)
  | itemprop(value val)
  | itemref(value val)
  | itemscope(value val)
  | itemtype(value val)
  | keytype(value val)
  | kind(value val)
  | label(value val)
  | lang(value val)
  | language(value val)
  | \list(value val)
  | local_(value val)
  | loop(value val)
  | low(value val)
  | manifest(value val)
  | max(value val)
  | maxlength(value val)
  | media(value val)
  | mediagroup(value val)
  | method(value val)
  | min(value val)
  | multiple(value val)
  | muted(value val)
  | name(value val)
  | novalidate(value val)
  | onabort(value val)
  | onafterprint(value val)
  | onbeforeprint(value val)
  | onbeforeunload(value val)
  | onblur(value val)
  | oncanplay(value val)
  | oncanplaythrough(value val)
  | onchange(value val)
  | onclick(value val)
  | oncontextmenu(value val)
  | ondblclick(value val)
  | ondrag(value val)
  | ondragend(value val)
  | ondragenter(value val)
  | ondragleave(value val)
  | ondragover(value val)
  | ondragstart(value val)
  | ondrop(value val)
  | ondurationchange(value val)
  | onemptied(value val)
  | onended(value val)
  | onerror(value val)
  | onfocus(value val)
  | onformchange(value val)
  | onforminput(value val)
  | onhashchange(value val)
  | oninput(value val)
  | oninvalid(value val)
  | onkeydown(value val)
  | onkeypress(value val)
  | onkeyup(value val)
  | onload(value val)
  | onloadeddata(value val)
  | onloadedmetadata(value val)
  | onloadstart(value val)
  | onmessage(value val)
  | onmousedown(value val)
  | onmousemove(value val)
  | onmouseout(value val)
  | onmouseover(value val)
  | onmouseup(value val)
  | onmousewheel(value val)
  | onoffline(value val)
  | ononline(value val)
  | onpagehide(value val)
  | onpageshow(value val)
  | onpause(value val)
  | onplay(value val)
  | onplaying(value val)
  | onpopstate(value val)
  | onprogress(value val)
  | onratechange(value val)
  | onredo(value val)
  | onreset(value val)
  | onresize(value val)
  | onscroll(value val)
  | onseeked(value val)
  | onseeking(value val)
  | onselect(value val)
  | onshow(value val)
  | onstalled(value val)
  | onstorage(value val)
  | onsubmit(value val)
  | onsuspend(value val)
  | ontimeupdate(value val)
  | onundo(value val)
  | onunload(value val)
  | onvolumechange(value val)
  | onwaiting(value val)
  | open(value val)
  | optimum(value val)
  | pattern(value val)
  | ping(value val)
  | placeholder(value val)
  | poster(value val)
  | prefix(value val)
  | preload(value val)
  | property(value val)
  | radiogroup(value val)
  | readonly(value val)
  | \rel(value val)
  | required(value val)
  | resource(value val)
  | rev(value val)
  | reversed(value val)
  | role(value val)
  | rows(value val)
  | rowspan(value val)
  | sandbox(value val)
  | scope(value val)
  | scoped(value val)
  | seamless(value val)
  | selected(value val)
  | shape(value val)
  | size(value val)
  | sizes(value val)
  | span(value val)
  | spellcheck(value val)
  | src(value val)
  | srcdoc(value val)
  | srclang(value val)
  | \start(value val)
  | step(value val)
  | style(value val)
  | tabindex(value val)
  | target(value val)
  | template(value val)
  | title(value val)
  | translate(value val)
  | \type(value val)
  | typeof(value val)
  | usemap(value val)
  | valign(value val)
  | \value(value val)
  | vocab(value val)
  | width(value val)
  | wrap(value val)
  | xml_base(value val)
  | xml_id(value val)
  | xml_lang(value val)
  | xml_space(value val)
  ;
  
/*
# void tags: <img> no closing tag.
# for others: need closing tag. (<x/> is not allowed)
# area, base, br, col, command, embed, hr, img, input,
# keygen, link, meta, param, source, track, wbr


#Raw text: script, style (see below)

#Escapable raw text: textarea, title
# can have charrefs, but no ambiguous ampersand
# http://www.w3.org/TR/html5/syntax.html#syntax-ambiguous-ampersand

#The text in raw text and escapable raw text elements must not contain
#any occurrences of the string "</" (U+003C LESS-THAN SIGN, U+002F
#SOLIDUS) followed by characters that case-insensitively match the tag
#name of the element followed by one of "tab" (U+0009), "LF" (U+000A),
#"FF" (U+000C), "CR" (U+000D), U+0020 SPACE, ">" (U+003E), or "/"
#(U+002F).

*/

bool isVoid(str x) = 
 x in {"area", "base", "br", "col", "command", "embed", "hr", "img", "input",
       "keygen", "link,", "meta", "param", "source", "track", "wbr"};


bool isRawText(str x) = x in {"script", "style"};

bool isEscapableRawText(str x) = x in {"script", "style"};

bool isBlockLevel(str x) =
  x in {"address", "article", "aside", "audio", "blockquote", "canvas", "dd",
"div", "dl", "fieldset", "figcaption", "figure", "footer", "form",
"h1", "h2", "h3", "h4", "h5", "h6", "header", "hgroup", "hr",
"noscript", "ol", "output", "p", "pre", "section", "table", "tfoot",
"ul", "video"};

str startTag(str n, str attrs)
  = "\<<n><attrs>\>";

str endTag(str n) = "\<<n>/\>";

str startTag(str n, {}) = startTag(n, "");
  
default str startTag(str n, set[HTML5Attr] attrs) 
  = startTag(n, " " + attrsToString(attrs));
  
str attrsToString(set[HTML5Attr] attrs) 
  = intercalate(" ", [ attrToString(a) | a <- attrs ] );
  
// TODO: escaping
str attrToString(str x(value v)) = "<x>=\"<v>\"";
  
str rawText(list[value] xs) = ("" | it + "<x>" | x <- xs );

// TODO: escaping
str escapableRawText(list[value] xs) = ("" | it + "<x>" | x <- xs );
  
str kidsToString(list[value] kids)
  = ("" | it + kidToString(k) | k <- kids );

str kidToString(HTML5Node elt)  = toString(elt);

default str kidToString(value x)  = "<x>";
  
str nodeToString(str n, set[HTML5Attr] attrs, list[value] kids) {
      str s = "";
      if (isVoid(n)) {
        // ignore kids...
        s += startTag(n, attrs);
      }
      else if (isRawText(n)) {
        s += startTag(n, attrs);
        s += rawText(kids);
        s += endTag(n);
      }
      else if (isEscapableRawText(n)) {
        s += startTag(n, attrs);
        s += escapableRawText(kids);
        s += endTag(n);      
      }
      else if (isBlockLevel(n)) {
        s += "<startTag(n, attrs)>
             '  <for (k <- kids) {>
             '   <kidToString(k)>
             '  <}>
             '<endTag(n)>";
      }
      else {
        s += startTag(n, attrs);
        s += kidsToString(kids);
        s += endTag(n);      
      }
      return s;
}
  
public HTML5Node example 
  = html([head([title(["something"])]), body([
      ul([li(["bla"]), 
          li(["foo", img({href("someref")}, [])])])])]);
  
str toString(HTML5Node x) {
  switch (x) {
    case str n(set[HTML5Attr] attrs, list[value] kids): 
       return nodeToString(n, attrs, kids);
    case str n(set[HTML5Attr] attrs): 
       return nodeToString(n, attrs, []);
    case str n(list[value] kids):
       return nodeToString(n, {}, kids);   
  }
}