@doc{
#### Synopsis

AST model for HTML5 including pretty printer
}
@contributor{Tijs van der Storm - storm@cwi.nl (CWI)}
module lang::html5::DOM

import List;

data HTML5Node = html5node(str name, list[value] kids);
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

HTML5Attr abbr(value val) = html5attr("abbr", val);
HTML5Attr about(value val) = html5attr("about", val);
HTML5Attr accept(value val) = html5attr("accept", val);
HTML5Attr accesskey(value val) = html5attr("accesskey", val);
HTML5Attr action(value val) = html5attr("action", val);
HTML5Attr align(value val) = html5attr("align", val);
HTML5Attr allowfullscreen(value val) = html5attr("allowfullscreen", val);
HTML5Attr alt(value val) = html5attr("alt", val);
HTML5Attr aria(value val) = html5attr("aria", val);
HTML5Attr async(value val) = html5attr("async", val);
HTML5Attr autocomplete(value val) = html5attr("autocomplete", val);
HTML5Attr autofocus(value val) = html5attr("autofocus", val);
HTML5Attr autoplay(value val) = html5attr("autoplay", val);
HTML5Attr border(value val) = html5attr("border", val);
HTML5Attr challenge(value val) = html5attr("challenge", val);
HTML5Attr char(value val) = html5attr("char", val);
HTML5Attr charset(value val) = html5attr("charset", val);
HTML5Attr checked(value val) = html5attr("checked", val);
HTML5Attr cite(value val) = html5attr("cite", val);
HTML5Attr class(value val) = html5attr("class", val);
HTML5Attr cols(value val) = html5attr("cols", val);
HTML5Attr colspan(value val) = html5attr("colspan", val);
HTML5Attr command(value val) = html5attr("command", val);
HTML5Attr content(value val) = html5attr("content", val);
HTML5Attr contenteditable(value val) = html5attr("contenteditable", val);
HTML5Attr contextmenu(value val) = html5attr("contextmenu", val);
HTML5Attr controls(value val) = html5attr("controls", val);
HTML5Attr coords(value val) = html5attr("coords", val);
HTML5Attr \data(value val) = html5attr("data", val);
HTML5Attr datatype(value val) = html5attr("datatype", val);
HTML5Attr \datetime(value val) = html5attr("datetime", val);
HTML5Attr \default(value val) = html5attr("default", val);
HTML5Attr defer(value val) = html5attr("defer", val);
HTML5Attr dir(value val) = html5attr("dir", val);
HTML5Attr dirname(value val) = html5attr("dirname", val);
HTML5Attr disabled(value val) = html5attr("disabled", val);
HTML5Attr download(value val) = html5attr("download", val);
HTML5Attr draggable(value val) = html5attr("draggable", val);
HTML5Attr dropzone(value val) = html5attr("dropzone", val);
HTML5Attr enctype(value val) = html5attr("enctype", val);
HTML5Attr \for(value val) = html5attr("for", val);
HTML5Attr form(value val) = html5attr("form", val);
HTML5Attr formaction(value val) = html5attr("formaction", val);
HTML5Attr formenctype(value val) = html5attr("formenctype", val);
HTML5Attr formmethod(value val) = html5attr("formmethod", val);
HTML5Attr formnovalidate(value val) = html5attr("formnovalidate", val);
HTML5Attr formtarget(value val) = html5attr("formtarget", val);
HTML5Attr headers(value val) = html5attr("headers", val);
HTML5Attr height(value val) = html5attr("height", val);
HTML5Attr hidden(value val) = html5attr("hidden", val);
HTML5Attr high(value val) = html5attr("high", val);
HTML5Attr href(value val) = html5attr("href", val);
HTML5Attr hreflang(value val) = html5attr("hreflang", val);
HTML5Attr http(value val) = html5attr("http", val);
HTML5Attr icon(value val) = html5attr("icon", val);
HTML5Attr id(value val) = html5attr("id", val);
HTML5Attr inlist(value val) = html5attr("inlist", val);
HTML5Attr ismap(value val) = html5attr("ismap", val);
HTML5Attr itemid(value val) = html5attr("itemid", val);
HTML5Attr itemprop(value val) = html5attr("itemprop", val);
HTML5Attr itemref(value val) = html5attr("itemref", val);
HTML5Attr itemscope(value val) = html5attr("itemscope", val);
HTML5Attr itemtype(value val) = html5attr("itemtype", val);
HTML5Attr keytype(value val) = html5attr("keytype", val);
HTML5Attr kind(value val) = html5attr("kind", val);
HTML5Attr label(value val) = html5attr("label", val);
HTML5Attr lang(value val) = html5attr("lang", val);
HTML5Attr language(value val) = html5attr("language", val);
HTML5Attr \list(value val) = html5attr("list", val);
HTML5Attr local_(value val) = html5attr("local_", val);
HTML5Attr loop(value val) = html5attr("loop", val);
HTML5Attr low(value val) = html5attr("low", val);
HTML5Attr manifest(value val) = html5attr("manifest", val);
HTML5Attr max(value val) = html5attr("max", val);
HTML5Attr maxlength(value val) = html5attr("maxlength", val);
HTML5Attr media(value val) = html5attr("media", val);
HTML5Attr mediagroup(value val) = html5attr("mediagroup", val);
HTML5Attr method(value val) = html5attr("method", val);
HTML5Attr min(value val) = html5attr("min", val);
HTML5Attr multiple(value val) = html5attr("multiple", val);
HTML5Attr muted(value val) = html5attr("muted", val);
HTML5Attr name(value val) = html5attr("name", val);
HTML5Attr novalidate(value val) = html5attr("novalidate", val);
HTML5Attr onabort(value val) = html5attr("onabort", val);
HTML5Attr onafterprint(value val) = html5attr("onafterprint", val);
HTML5Attr onbeforeprint(value val) = html5attr("onbeforeprint", val);
HTML5Attr onbeforeunload(value val) = html5attr("onbeforeunload", val);
HTML5Attr onblur(value val) = html5attr("onblur", val);
HTML5Attr oncanplay(value val) = html5attr("oncanplay", val);
HTML5Attr oncanplaythrough(value val) = html5attr("oncanplaythrough", val);
HTML5Attr onchange(value val) = html5attr("onchange", val);
HTML5Attr onclick(value val) = html5attr("onclick", val);
HTML5Attr oncontextmenu(value val) = html5attr("oncontextmenu", val);
HTML5Attr ondblclick(value val) = html5attr("ondblclick", val);
HTML5Attr ondrag(value val) = html5attr("ondrag", val);
HTML5Attr ondragend(value val) = html5attr("ondragend", val);
HTML5Attr ondragenter(value val) = html5attr("ondragenter", val);
HTML5Attr ondragleave(value val) = html5attr("ondragleave", val);
HTML5Attr ondragover(value val) = html5attr("ondragover", val);
HTML5Attr ondragstart(value val) = html5attr("ondragstart", val);
HTML5Attr ondrop(value val) = html5attr("ondrop", val);
HTML5Attr ondurationchange(value val) = html5attr("ondurationchange", val);
HTML5Attr onemptied(value val) = html5attr("onemptied", val);
HTML5Attr onended(value val) = html5attr("onended", val);
HTML5Attr onerror(value val) = html5attr("onerror", val);
HTML5Attr onfocus(value val) = html5attr("onfocus", val);
HTML5Attr onformchange(value val) = html5attr("onformchange", val);
HTML5Attr onforminput(value val) = html5attr("onforminput", val);
HTML5Attr onhashchange(value val) = html5attr("onhashchange", val);
HTML5Attr oninput(value val) = html5attr("oninput", val);
HTML5Attr oninvalid(value val) = html5attr("oninvalid", val);
HTML5Attr onkeydown(value val) = html5attr("onkeydown", val);
HTML5Attr onkeypress(value val) = html5attr("onkeypress", val);
HTML5Attr onkeyup(value val) = html5attr("onkeyup", val);
HTML5Attr onload(value val) = html5attr("onload", val);
HTML5Attr onloadeddata(value val) = html5attr("onloadeddata", val);
HTML5Attr onloadedmetadata(value val) = html5attr("onloadedmetadata", val);
HTML5Attr onloadstart(value val) = html5attr("onloadstart", val);
HTML5Attr onmessage(value val) = html5attr("onmessage", val);
HTML5Attr onmousedown(value val) = html5attr("onmousedown", val);
HTML5Attr onmousemove(value val) = html5attr("onmousemove", val);
HTML5Attr onmouseout(value val) = html5attr("onmouseout", val);
HTML5Attr onmouseover(value val) = html5attr("onmouseover", val);
HTML5Attr onmouseup(value val) = html5attr("onmouseup", val);
HTML5Attr onmousewheel(value val) = html5attr("onmousewheel", val);
HTML5Attr onoffline(value val) = html5attr("onoffline", val);
HTML5Attr ononline(value val) = html5attr("ononline", val);
HTML5Attr onpagehide(value val) = html5attr("onpagehide", val);
HTML5Attr onpageshow(value val) = html5attr("onpageshow", val);
HTML5Attr onpause(value val) = html5attr("onpause", val);
HTML5Attr onplay(value val) = html5attr("onplay", val);
HTML5Attr onplaying(value val) = html5attr("onplaying", val);
HTML5Attr onpopstate(value val) = html5attr("onpopstate", val);
HTML5Attr onprogress(value val) = html5attr("onprogress", val);
HTML5Attr onratechange(value val) = html5attr("onratechange", val);
HTML5Attr onredo(value val) = html5attr("onredo", val);
HTML5Attr onreset(value val) = html5attr("onreset", val);
HTML5Attr onresize(value val) = html5attr("onresize", val);
HTML5Attr onscroll(value val) = html5attr("onscroll", val);
HTML5Attr onseeked(value val) = html5attr("onseeked", val);
HTML5Attr onseeking(value val) = html5attr("onseeking", val);
HTML5Attr onselect(value val) = html5attr("onselect", val);
HTML5Attr onshow(value val) = html5attr("onshow", val);
HTML5Attr onstalled(value val) = html5attr("onstalled", val);
HTML5Attr onstorage(value val) = html5attr("onstorage", val);
HTML5Attr onsubmit(value val) = html5attr("onsubmit", val);
HTML5Attr onsuspend(value val) = html5attr("onsuspend", val);
HTML5Attr ontimeupdate(value val) = html5attr("ontimeupdate", val);
HTML5Attr onundo(value val) = html5attr("onundo", val);
HTML5Attr onunload(value val) = html5attr("onunload", val);
HTML5Attr onvolumechange(value val) = html5attr("onvolumechange", val);
HTML5Attr onwaiting(value val) = html5attr("onwaiting", val);
HTML5Attr open(value val) = html5attr("open", val);
HTML5Attr optimum(value val) = html5attr("optimum", val);
HTML5Attr pattern(value val) = html5attr("pattern", val);
HTML5Attr ping(value val) = html5attr("ping", val);
HTML5Attr placeholder(value val) = html5attr("placeholder", val);
HTML5Attr poster(value val) = html5attr("poster", val);
HTML5Attr prefix(value val) = html5attr("prefix", val);
HTML5Attr preload(value val) = html5attr("preload", val);
HTML5Attr property(value val) = html5attr("property", val);
HTML5Attr radiogroup(value val) = html5attr("radiogroup", val);
HTML5Attr readonly(value val) = html5attr("readonly", val);
HTML5Attr \rel(value val) = html5attr("rel", val);
HTML5Attr required(value val) = html5attr("required", val);
HTML5Attr resource(value val) = html5attr("resource", val);
HTML5Attr rev(value val) = html5attr("rev", val);
HTML5Attr reversed(value val) = html5attr("reversed", val);
HTML5Attr role(value val) = html5attr("role", val);
HTML5Attr rows(value val) = html5attr("rows", val);
HTML5Attr rowspan(value val) = html5attr("rowspan", val);
HTML5Attr sandbox(value val) = html5attr("sandbox", val);
HTML5Attr scope(value val) = html5attr("scope", val);
HTML5Attr scoped(value val) = html5attr("scoped", val);
HTML5Attr seamless(value val) = html5attr("seamless", val);
HTML5Attr selected(value val) = html5attr("selected", val);
HTML5Attr shape(value val) = html5attr("shape", val);
HTML5Attr size(value val) = html5attr("size", val);
HTML5Attr sizes(value val) = html5attr("sizes", val);
HTML5Attr span(value val) = html5attr("span", val);
HTML5Attr spellcheck(value val) = html5attr("spellcheck", val);
HTML5Attr src(value val) = html5attr("src", val);
HTML5Attr srcdoc(value val) = html5attr("srcdoc", val);
HTML5Attr srclang(value val) = html5attr("srclang", val);
HTML5Attr \start(value val) = html5attr("start", val);
HTML5Attr step(value val) = html5attr("step", val);
HTML5Attr style(value val) = html5attr("style", val);
HTML5Attr tabindex(value val) = html5attr("tabindex", val);
HTML5Attr target(value val) = html5attr("target", val);
HTML5Attr template(value val) = html5attr("template", val);
//HTML5Attr title(value val) = html5attr("title", val);  <== overlaps with title(value kids...) defined above
HTML5Attr translate(value val) = html5attr("translate", val);
HTML5Attr \type(value val) = html5attr("type", val);
HTML5Attr typeof(value val) = html5attr("typeof", val);
HTML5Attr usemap(value val) = html5attr("usemap", val);
HTML5Attr valign(value val) = html5attr("valign", val);
HTML5Attr \value(value val) = html5attr("value", val);
HTML5Attr vocab(value val) = html5attr("vocab", val);
HTML5Attr width(value val) = html5attr("width", val);
HTML5Attr wrap(value val) = html5attr("wrap", val);
HTML5Attr xml_base(value val) = html5attr("xml_base", val);
HTML5Attr xml_id(value val) = html5attr("xml_id", val);
HTML5Attr xml_lang(value val) = html5attr("xml_lang", val);
HTML5Attr xml_space(value val) = html5attr("xml_space", val);


  
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
  
public HTML5Node example 
  = html(head(title("something")), body(
      ul(li("bla"), 
          li("foo", 3, img(href("someref"))))));
  
str toString(HTML5Node x) {
  attrs = { k | HTML5Attr k <- x.kids };
  kids = [ k | value k <- x.kids, !(HTML5Attr _ := k) ];
  return nodeToString(x.name, attrs, kids); 
}
