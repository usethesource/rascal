@synopsis{DOM-based AST model for HTML5 including pretty printer}
@contributor{Tijs van der Storm - storm@cwi.nl (CWI)}
module lang::html5::DOM

import List;
import Content;

@synopsis{Generic representation for all HTML tag types.}
@description{
Every standard HTML5 node  type has a convenience function in 
this module with a name that is equal to the tag name, modulo renamings
for identifier compatibility in Rascal.
}
@examples{
```rascal-shell
title("This is a title node")
```
}
data HTML5Node = html5node(str name, list[value] kids);

@synopsis{Generic representation for all HTML attributes.}
@description{
Every standard HTML5 attribute has a convenience function in 
this module with a name that starts with an "a" for "attribute".
This is to prevent overlaps with node names that are the same.
}
@examples{
```rascal-shell
import lang::html5::DOM;
aabbr("Short")
}
data HTML5Attr = html5attr(str name, value val);


HTML5Node a(value kids...) = html5node("a", kids);
HTML5Node abbr(value kids...) = html5node("abbr", kids);
HTML5Node address(value kids...) = html5node("address", kids);
HTML5Node area(value kids...) = html5node("area", kids);
HTML5Node article(value kids...) = html5node("article", kids);
HTML5Node aside(value kids...) = html5node("aside", kids);
HTML5Node audio(value kids...) = html5node("audio", kids);
HTML5Node b(value kids...) = html5node("b", kids);
HTML5Node base(value kids...) = html5node("base", kids);
HTML5Node bdi(value kids...) = html5node("bdi", kids);
HTML5Node bdo(value kids...) = html5node("bdo", kids);
HTML5Node blockquote(value kids...) = html5node("blockquote", kids);
HTML5Node body(value kids...) = html5node("body", kids);
HTML5Node br(value kids...) = html5node("br", kids);
HTML5Node button(value kids...) = html5node("button", kids);
HTML5Node canvas(value kids...) = html5node("canvas", kids);
HTML5Node caption(value kids...) = html5node("caption", kids);
HTML5Node cite(value kids...) = html5node("cite", kids);
HTML5Node code(value kids...) = html5node("code", kids);
HTML5Node col(value kids...) = html5node("col", kids);
HTML5Node colgroup(value kids...) = html5node("colgroup", kids);
HTML5Node \data(value kids...) = html5node("data", kids);
HTML5Node datalist(value kids...) = html5node("datalist", kids);
HTML5Node dd(value kids...) = html5node("dd", kids);
HTML5Node del(value kids...) = html5node("del", kids);
HTML5Node details(value kids...) = html5node("details", kids);
HTML5Node dfn(value kids...) = html5node("dfn", kids);
HTML5Node dialog(value kids...) = html5node("dialog", kids);
HTML5Node div(value kids...) = html5node("div", kids);
HTML5Node dl(value kids...) = html5node("dl", kids);
HTML5Node dt(value kids...) = html5node("dt", kids);
HTML5Node em(value kids...) = html5node("em", kids);
HTML5Node embed(value kids...) = html5node("embed", kids);
HTML5Node fieldset(value kids...) = html5node("fieldset", kids);
HTML5Node figcaption(value kids...) = html5node("figcaption", kids);
HTML5Node figure(value kids...) = html5node("figure", kids);
HTML5Node footer(value kids...) = html5node("footer", kids);
HTML5Node form(value kids...) = html5node("form", kids);
HTML5Node h1(value kids...) = html5node("h1", kids);
HTML5Node h2(value kids...) = html5node("h2", kids);
HTML5Node h3(value kids...) = html5node("h3", kids);
HTML5Node h4(value kids...) = html5node("h4", kids);
HTML5Node h5(value kids...) = html5node("h5", kids);
HTML5Node h6(value kids...) = html5node("h6", kids);
HTML5Node head(value kids...) = html5node("head", kids);
HTML5Node header(value kids...) = html5node("header", kids);
HTML5Node hgroup(value kids...) = html5node("hgroup", kids);
HTML5Node hr(value kids...) = html5node("hr", kids);
HTML5Node html(value kids...) = html5node("html", kids);
HTML5Node i(value kids...) = html5node("i", kids);
HTML5Node iframe(value kids...) = html5node("iframe", kids);
HTML5Node img(value kids...) = html5node("img", kids);
HTML5Node input(value kids...) = html5node("input", kids);
HTML5Node ins(value kids...) = html5node("ins", kids);
HTML5Node kbd(value kids...) = html5node("kbd", kids);
HTML5Node keygen(value kids...) = html5node("keygen", kids);
HTML5Node label(value kids...) = html5node("label", kids);
HTML5Node legend(value kids...) = html5node("legend", kids);
HTML5Node li(value kids...) = html5node("li", kids);
HTML5Node link(value kids...) = html5node("link", kids);
HTML5Node main(value kids...) = html5node("main", kids);
HTML5Node \map(value kids...) = html5node("map", kids);
HTML5Node mark(value kids...) = html5node("mark", kids);
HTML5Node menu(value kids...) = html5node("menu", kids);
HTML5Node menuitem(value kids...) = html5node("menuitem", kids);
HTML5Node meta(value kids...) = html5node("meta", kids);
HTML5Node meter(value kids...) = html5node("meter", kids);
HTML5Node nav(value kids...) = html5node("nav", kids);
HTML5Node noscript(value kids...) = html5node("noscript", kids);
HTML5Node object(value kids...) = html5node("object", kids);
HTML5Node ol(value kids...) = html5node("ol", kids);
HTML5Node optgroup(value kids...) = html5node("optgroup", kids);
HTML5Node option(value kids...) = html5node("option", kids);
HTML5Node output(value kids...) = html5node("output", kids);
HTML5Node p(value kids...) = html5node("p", kids);
HTML5Node param(value kids...) = html5node("param", kids);
HTML5Node pre(value kids...) = html5node("pre", kids);
HTML5Node progress(value kids...) = html5node("progress", kids);
HTML5Node q(value kids...) = html5node("q", kids);
HTML5Node rp(value kids...) = html5node("rp", kids);
HTML5Node rt(value kids...) = html5node("rt", kids);
HTML5Node ruby(value kids...) = html5node("ruby", kids);
HTML5Node s(value kids...) = html5node("s", kids);
HTML5Node samp(value kids...) = html5node("samp", kids);
HTML5Node script(value kids...) = html5node("script", kids);
HTML5Node section(value kids...) = html5node("section", kids);
HTML5Node select(value kids...) = html5node("select", kids);
HTML5Node small(value kids...) = html5node("small", kids);
HTML5Node source(value kids...) = html5node("source", kids);
HTML5Node span(value kids...) = html5node("span", kids);
HTML5Node strong(value kids...) = html5node("strong", kids);
HTML5Node style(value kids...) = html5node("style", kids);
HTML5Node sub(value kids...) = html5node("sub", kids);
HTML5Node summary(value kids...) = html5node("summary", kids);
HTML5Node sup(value kids...) = html5node("sup", kids);
HTML5Node table(value kids...) = html5node("table", kids);
HTML5Node tbody(value kids...) = html5node("tbody", kids);
HTML5Node td(value kids...) = html5node("td", kids);
HTML5Node template(value kids...) = html5node("template", kids);
HTML5Node textarea(value kids...) = html5node("textarea", kids);
HTML5Node tfoot(value kids...) = html5node("tfoot", kids);
HTML5Node th(value kids...) = html5node("th", kids);
HTML5Node thead(value kids...) = html5node("thead", kids);
HTML5Node time(value kids...) = html5node("time", kids);
HTML5Node title(value kids...) = html5node("title", kids);
HTML5Node tr(value kids...) = html5node("tr", kids);
HTML5Node track(value kids...) = html5node("track", kids);
HTML5Node u(value kids...) = html5node("u", kids);
HTML5Node ul(value kids...) = html5node("ul", kids);
HTML5Node var(value kids...) = html5node("var", kids);
HTML5Node video(value kids...) = html5node("video", kids);
HTML5Node wbr(value kids...) = html5node("wbr", kids);

HTML5Attr aaaliasbbr(value val) = html5attr("abbr", val);
HTML5Attr aabout(value val) = html5attr("about", val);
HTML5Attr aaccept(value val) = html5attr("accept", val);
HTML5Attr aaccesskey(value val) = html5attr("accesskey", val);
HTML5Attr aaction(value val) = html5attr("action", val);
HTML5Attr aalign(value val) = html5attr("align", val);
HTML5Attr aallowfullscreen(value val) = html5attr("allowfullscreen", val);
HTML5Attr aalt(value val) = html5attr("alt", val);
HTML5Attr aaria(value val) = html5attr("aria", val);
HTML5Attr aasync(value val) = html5attr("async", val);
HTML5Attr aautocomplete(value val) = html5attr("autocomplete", val);
HTML5Attr aautofocus(value val) = html5attr("autofocus", val);
HTML5Attr aautoplay(value val) = html5attr("autoplay", val);
HTML5Attr aborder(value val) = html5attr("border", val);
HTML5Attr achallenge(value val) = html5attr("challenge", val);
HTML5Attr achar(value val) = html5attr("char", val);
HTML5Attr acharset(value val) = html5attr("charset", val);
HTML5Attr achecked(value val) = html5attr("checked", val);
HTML5Attr aacite(value val) = html5attr("cite", val);
HTML5Attr aclass(value val) = html5attr("class", val);
HTML5Attr acols(value val) = html5attr("cols", val);
HTML5Attr acolspan(value val) = html5attr("colspan", val);
HTML5Attr acommand(value val) = html5attr("command", val);
HTML5Attr acontent(value val) = html5attr("content", val);
HTML5Attr acontenteditable(value val) = html5attr("contenteditable", val);
HTML5Attr acontextmenu(value val) = html5attr("contextmenu", val);
HTML5Attr acontrols(value val) = html5attr("controls", val);
HTML5Attr acoords(value val) = html5attr("coords", val);
HTML5Attr adata(value val) = html5attr("data", val);
HTML5Attr adatatype(value val) = html5attr("datatype", val);
HTML5Attr adatetime(value val) = html5attr("datetime", val);
HTML5Attr adefault(value val) = html5attr("default", val);
HTML5Attr adefer(value val) = html5attr("defer", val);
HTML5Attr adir(value val) = html5attr("dir", val);
HTML5Attr adirname(value val) = html5attr("dirname", val);
HTML5Attr adisabled(value val) = html5attr("disabled", val);
HTML5Attr adownload(value val) = html5attr("download", val);
HTML5Attr adraggable(value val) = html5attr("draggable", val);
HTML5Attr adropzone(value val) = html5attr("dropzone", val);
HTML5Attr aenctype(value val) = html5attr("enctype", val);
HTML5Attr afor(value val) = html5attr("for", val);
HTML5Attr aform(value val) = html5attr("form", val);
HTML5Attr aformaction(value val) = html5attr("formaction", val);
HTML5Attr aformenctype(value val) = html5attr("formenctype", val);
HTML5Attr aformmethod(value val) = html5attr("formmethod", val);
HTML5Attr aformnovalidate(value val) = html5attr("formnovalidate", val);
HTML5Attr aformtarget(value val) = html5attr("formtarget", val);
HTML5Attr aheaders(value val) = html5attr("headers", val);
HTML5Attr aheight(value val) = html5attr("height", val);
HTML5Attr ahidden(value val) = html5attr("hidden", val);
HTML5Attr ahigh(value val) = html5attr("high", val);
HTML5Attr ahref(value val) = html5attr("href", val);
HTML5Attr ahreflang(value val) = html5attr("hreflang", val);
HTML5Attr ahttp(value val) = html5attr("http", val);
HTML5Attr aicon(value val) = html5attr("icon", val);
HTML5Attr aid(value val) = html5attr("id", val);
HTML5Attr ainlist(value val) = html5attr("inlist", val);
HTML5Attr aismap(value val) = html5attr("ismap", val);
HTML5Attr aitemid(value val) = html5attr("itemid", val);
HTML5Attr aitemprop(value val) = html5attr("itemprop", val);
HTML5Attr aitemref(value val) = html5attr("itemref", val);
HTML5Attr aitemscope(value val) = html5attr("itemscope", val);
HTML5Attr aitemtype(value val) = html5attr("itemtype", val);
HTML5Attr akeytype(value val) = html5attr("keytype", val);
HTML5Attr akind(value val) = html5attr("kind", val);
HTML5Attr alabel(value val) = html5attr("label", val);
HTML5Attr alang(value val) = html5attr("lang", val);
HTML5Attr alanguage(value val) = html5attr("language", val);
HTML5Attr alist(value val) = html5attr("list", val);
HTML5Attr alocal_(value val) = html5attr("local_", val);
HTML5Attr aloop(value val) = html5attr("loop", val);
HTML5Attr alow(value val) = html5attr("low", val);
HTML5Attr amanifest(value val) = html5attr("manifest", val);
HTML5Attr amax(value val) = html5attr("max", val);
HTML5Attr amaxlength(value val) = html5attr("maxlength", val);
HTML5Attr amedia(value val) = html5attr("media", val);
HTML5Attr amediagroup(value val) = html5attr("mediagroup", val);
HTML5Attr amethod(value val) = html5attr("method", val);
HTML5Attr amin(value val) = html5attr("min", val);
HTML5Attr amultiple(value val) = html5attr("multiple", val);
HTML5Attr amuted(value val) = html5attr("muted", val);
HTML5Attr aname(value val) = html5attr("name", val);
HTML5Attr anovalidate(value val) = html5attr("novalidate", val);
HTML5Attr aonabort(value val) = html5attr("onabort", val);
HTML5Attr aonafterprint(value val) = html5attr("onafterprint", val);
HTML5Attr aonbeforeprint(value val) = html5attr("onbeforeprint", val);
HTML5Attr aonbeforeunload(value val) = html5attr("onbeforeunload", val);
HTML5Attr aonblur(value val) = html5attr("onblur", val);
HTML5Attr aoncanplay(value val) = html5attr("oncanplay", val);
HTML5Attr aoncanplaythrough(value val) = html5attr("oncanplaythrough", val);
HTML5Attr aonchange(value val) = html5attr("onchange", val);
HTML5Attr aonclick(value val) = html5attr("onclick", val);
HTML5Attr aoncontextmenu(value val) = html5attr("oncontextmenu", val);
HTML5Attr aondblclick(value val) = html5attr("ondblclick", val);
HTML5Attr aondrag(value val) = html5attr("ondrag", val);
HTML5Attr aondragend(value val) = html5attr("ondragend", val);
HTML5Attr aondragenter(value val) = html5attr("ondragenter", val);
HTML5Attr aondragleave(value val) = html5attr("ondragleave", val);
HTML5Attr aondragover(value val) = html5attr("ondragover", val);
HTML5Attr aondragstart(value val) = html5attr("ondragstart", val);
HTML5Attr aondrop(value val) = html5attr("ondrop", val);
HTML5Attr aondurationchange(value val) = html5attr("ondurationchange", val);
HTML5Attr aonemptied(value val) = html5attr("onemptied", val);
HTML5Attr aonended(value val) = html5attr("onended", val);
HTML5Attr aonerror(value val) = html5attr("onerror", val);
HTML5Attr aonfocus(value val) = html5attr("onfocus", val);
HTML5Attr aonformchange(value val) = html5attr("onformchange", val);
HTML5Attr aonforminput(value val) = html5attr("onforminput", val);
HTML5Attr aonhashchange(value val) = html5attr("onhashchange", val);
HTML5Attr aoninput(value val) = html5attr("oninput", val);
HTML5Attr aoninvalid(value val) = html5attr("oninvalid", val);
HTML5Attr aonkeydown(value val) = html5attr("onkeydown", val);
HTML5Attr aonkeypress(value val) = html5attr("onkeypress", val);
HTML5Attr aonkeyup(value val) = html5attr("onkeyup", val);
HTML5Attr aonload(value val) = html5attr("onload", val);
HTML5Attr aonloadeddata(value val) = html5attr("onloadeddata", val);
HTML5Attr aonloadedmetadata(value val) = html5attr("onloadedmetadata", val);
HTML5Attr aonloadstart(value val) = html5attr("onloadstart", val);
HTML5Attr aonmessage(value val) = html5attr("onmessage", val);
HTML5Attr aonmousedown(value val) = html5attr("onmousedown", val);
HTML5Attr aonmousemove(value val) = html5attr("onmousemove", val);
HTML5Attr aonmouseout(value val) = html5attr("onmouseout", val);
HTML5Attr aonmouseover(value val) = html5attr("onmouseover", val);
HTML5Attr aonmouseup(value val) = html5attr("onmouseup", val);
HTML5Attr aonmousewheel(value val) = html5attr("onmousewheel", val);
HTML5Attr aonoffline(value val) = html5attr("onoffline", val);
HTML5Attr aononline(value val) = html5attr("ononline", val);
HTML5Attr aonpagehide(value val) = html5attr("onpagehide", val);
HTML5Attr aonpageshow(value val) = html5attr("onpageshow", val);
HTML5Attr aonpause(value val) = html5attr("onpause", val);
HTML5Attr aonplay(value val) = html5attr("onplay", val);
HTML5Attr aonplaying(value val) = html5attr("onplaying", val);
HTML5Attr aonpopstate(value val) = html5attr("onpopstate", val);
HTML5Attr aonprogress(value val) = html5attr("onprogress", val);
HTML5Attr aonratechange(value val) = html5attr("onratechange", val);
HTML5Attr aonredo(value val) = html5attr("onredo", val);
HTML5Attr aonreset(value val) = html5attr("onreset", val);
HTML5Attr aonresize(value val) = html5attr("onresize", val);
HTML5Attr aonscroll(value val) = html5attr("onscroll", val);
HTML5Attr aonseeked(value val) = html5attr("onseeked", val);
HTML5Attr aonseeking(value val) = html5attr("onseeking", val);
HTML5Attr aonselect(value val) = html5attr("onselect", val);
HTML5Attr aonshow(value val) = html5attr("onshow", val);
HTML5Attr aonstalled(value val) = html5attr("onstalled", val);
HTML5Attr aonstorage(value val) = html5attr("onstorage", val);
HTML5Attr aonsubmit(value val) = html5attr("onsubmit", val);
HTML5Attr aonsuspend(value val) = html5attr("onsuspend", val);
HTML5Attr aontimeupdate(value val) = html5attr("ontimeupdate", val);
HTML5Attr aonundo(value val) = html5attr("onundo", val);
HTML5Attr aonunload(value val) = html5attr("onunload", val);
HTML5Attr aonvolumechange(value val) = html5attr("onvolumechange", val);
HTML5Attr aonwaiting(value val) = html5attr("onwaiting", val);
HTML5Attr aopen(value val) = html5attr("open", val);
HTML5Attr aoptimum(value val) = html5attr("optimum", val);
HTML5Attr apattern(value val) = html5attr("pattern", val);
HTML5Attr aping(value val) = html5attr("ping", val);
HTML5Attr aplaceholder(value val) = html5attr("placeholder", val);
HTML5Attr aposter(value val) = html5attr("poster", val);
HTML5Attr aprefix(value val) = html5attr("prefix", val);
HTML5Attr apreload(value val) = html5attr("preload", val);
HTML5Attr aproperty(value val) = html5attr("property", val);
HTML5Attr aradiogroup(value val) = html5attr("radiogroup", val);
HTML5Attr areadonly(value val) = html5attr("readonly", val);
HTML5Attr arel(value val) = html5attr("rel", val);
HTML5Attr arequired(value val) = html5attr("required", val);
HTML5Attr aresource(value val) = html5attr("resource", val);
HTML5Attr arev(value val) = html5attr("rev", val);
HTML5Attr areversed(value val) = html5attr("reversed", val);
HTML5Attr arole(value val) = html5attr("role", val);
HTML5Attr arows(value val) = html5attr("rows", val);
HTML5Attr arowspan(value val) = html5attr("rowspan", val);
HTML5Attr asandbox(value val) = html5attr("sandbox", val);
HTML5Attr ascope(value val) = html5attr("scope", val);
HTML5Attr ascoped(value val) = html5attr("scoped", val);
HTML5Attr aseamless(value val) = html5attr("seamless", val);
HTML5Attr aselected(value val) = html5attr("selected", val);
HTML5Attr ashape(value val) = html5attr("shape", val);
HTML5Attr asize(value val) = html5attr("size", val);
HTML5Attr asizes(value val) = html5attr("sizes", val);
HTML5Attr aspan(value val) = html5attr("span", val);
HTML5Attr aspellcheck(value val) = html5attr("spellcheck", val);
HTML5Attr asrc(value val) = html5attr("src", val);
HTML5Attr asrcdoc(value val) = html5attr("srcdoc", val);
HTML5Attr asrclang(value val) = html5attr("srclang", val);
HTML5Attr astart(value val) = html5attr("start", val);
HTML5Attr astep(value val) = html5attr("step", val);
HTML5Attr astyle(value val) = html5attr("style", val);
HTML5Attr atabindex(value val) = html5attr("tabindex", val);
HTML5Attr atarget(value val) = html5attr("target", val);
HTML5Attr atemplate(value val) = html5attr("template", val);
HTML5Attr atitle(value val) = html5attr("title", val);
HTML5Attr atranslate(value val) = html5attr("translate", val);
HTML5Attr atype(value val) = html5attr("type", val);
HTML5Attr atypeof(value val) = html5attr("typeof", val);
HTML5Attr ausemap(value val) = html5attr("usemap", val);
HTML5Attr avalign(value val) = html5attr("valign", val);
HTML5Attr avalue(value val) = html5attr("value", val);
HTML5Attr avocab(value val) = html5attr("vocab", val);
HTML5Attr awidth(value val) = html5attr("width", val);
HTML5Attr awrap(value val) = html5attr("wrap", val);
HTML5Attr axml_base(value val) = html5attr("xml_base", val);
HTML5Attr axml_id(value val) = html5attr("xml_id", val);
HTML5Attr axml_lang(value val) = html5attr("xml_lang", val);
HTML5Attr axml_space(value val) = html5attr("xml_space", val);


  
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

bool isEscapableRawText(str x) = x in {"textarea", "title"};

bool isBlockLevel(str x) =
  x in {"address", "article", "aside", "audio", "blockquote", "canvas", "dd",
"div", "dl", "fieldset", "figcaption", "figure", "footer", "form",
"h1", "h2", "h3", "h4", "h5", "h6", "header", "hgroup", "hr",
"noscript", "ol", "output", "p", "pre", "section", "table", "tfoot",
"ul", "video"};

str startTag(str n, str attrs)
  = "\<<n><attrs>\>";

str endTag(str n) = "\</<n>\>";

str startTag(str n, {}) = startTag(n, "");
  
default str startTag(str n, set[HTML5Attr] attrs) 
  = startTag(n, " " + attrsToString(attrs));
  
str attrsToString(set[HTML5Attr] attrs) 
  = intercalate(" ", [ attrToString(a) | HTML5Attr a <- attrs ] );
  
// TODO: escaping
str attrToString(html5attr(str x, value v)) = "<x>=\"<v>\"";
  
str rawText(list[value] xs) = ("" | it + "<x>" | x <- xs );

// TODO: escaping
str escapableRawText(list[value] xs) = ("" | it + "<x>" | x <- xs );
  
str kidsToString(list[value] kids)
  = ("" | it + kidToString(k) | k <- kids );

str kidToString(HTML5Node elt)  = toString(elt);
str kidToString(HTML5Attr x)  = "";

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
             '  <for (k <- kids) {><kidToString(k)>
             '  <}><endTag(n)>";
      }
      else {
        s += startTag(n, attrs);
        s += kidsToString(kids);
        s += endTag(n);      
      }
      return s;
}
  
private HTML5Node example 
  = html(head(title("something")), body(
      ul(li("bla"), 
          li("foo", 3, img(ahref("someref"))))));

@synopsis{pretty print HTML5Node DOM to a string}
str toString(HTML5Node x) {
  attrs = { k | HTML5Attr k <- x.kids };
  kids = [ k | value k <- x.kids, !(HTML5Attr _ := k) ];
  return nodeToString(x.name, attrs, kids); 
}

@synopsis{convenience function to render the HTML5Node dom tree in the browser}
public Content serve(HTML5Node x) = html(toString(x));
