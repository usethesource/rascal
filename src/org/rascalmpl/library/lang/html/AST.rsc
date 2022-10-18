module lang::html::AST

@synopsis{Abstract Syntax for HTML}
@description{
This is HTML5 encoded like so:
* element tags are constructor names of type `HTMLElement`
* all tags have a list of HTMLElement as children, except the `void` tags that do not have any parameters
* attributes are keyword parameters of type `str`
* unknown tags (such as SVG) are mapped to `unknownElement` nodes and their children are not included.
}
data HTMLElement
    = a(list[HTMLElement] elems)
    | abbr(list[HTMLElement] elems)
    | address(list[HTMLElement] elems)
    | area()
    | article(list[HTMLElement] elems)
    | aside(list[HTMLElement] elems)
    | audio(list[HTMLElement] elems)
    | b(list[HTMLElement] elems)
    | base()
    | bdi(list[HTMLElement] elems)
    | bdo(list[HTMLElement] elems)
    | blockquote(list[HTMLElement] elems)
    | body(list[HTMLElement] elems)
    | br()
    | button(list[HTMLElement] elems)
    | canvas(list[HTMLElement] elems)
    | caption(list[HTMLElement] elems)
    | cite(list[HTMLElement] elems)
    | code(list[HTMLElement] elems)
    | col()
    | colgroup(list[HTMLElement] elems)
    | command()
    | \data(list[HTMLElement] elems)
    | datalist(list[HTMLElement] elems)
    | dd(list[HTMLElement] elems)
    | del(list[HTMLElement] elems)
    | details(list[HTMLElement] elems)
    | dfn(list[HTMLElement] elems)
    | dialog(list[HTMLElement] elems)
    | div(list[HTMLElement] elems)
    | dl(list[HTMLElement] elems)
    | dt(list[HTMLElement] elems)
    | em(list[HTMLElement] elems)
    | embed()
    | fieldset(list[HTMLElement] elems)
    | figcaption(list[HTMLElement] elems)
    | figure(list[HTMLElement] elems)
    | footer(list[HTMLElement] elems)
    | form(list[HTMLElement] elems)
    | h1(list[HTMLElement] elems)
    | h2(list[HTMLElement] elems)
    | h3(list[HTMLElement] elems)
    | h4(list[HTMLElement] elems)
    | h5(list[HTMLElement] elems)
    | h6(list[HTMLElement] elems)
    | head(list[HTMLElement] elems)
    | header(list[HTMLElement] elems)
    | hgroup(list[HTMLElement] elems)
    | hr()
    | html(list[HTMLElement] elems)
    | i(list[HTMLElement] elems)
    | iframe(list[HTMLElement] elems)
    | img()
    | input()
    | ins(list[HTMLElement] elems)
    | kbd(list[HTMLElement] elems)
    | keygen()
    | label(list[HTMLElement] elems)
    | legend(list[HTMLElement] elems)
    | li(list[HTMLElement] elems)
    | link()
    | main(list[HTMLElement] elems)
    | \map(list[HTMLElement] elems)
    | mark(list[HTMLElement] elems)
    | menu(list[HTMLElement] elems)
    | menuitem(list[HTMLElement] elems)
    | meta()
    | meter(list[HTMLElement] elems)
    | nav(list[HTMLElement] elems)
    | noscript(list[HTMLElement] elems)
    | object(list[HTMLElement] elems)
    | ol(list[HTMLElement] elems)
    | optgroup(list[HTMLElement] elems)
    | option(list[HTMLElement] elems)
    | output(list[HTMLElement] elems)
    | p(list[HTMLElement] elems)
    | param()
    | pre(list[HTMLElement] elems)
    | progress(list[HTMLElement] elems)
    | q(list[HTMLElement] elems)
    | rp(list[HTMLElement] elems)
    | rt(list[HTMLElement] elems)
    | ruby(list[HTMLElement] elems)
    | s(list[HTMLElement] elems)
    | samp(list[HTMLElement] elems)
    | script(list[HTMLElement] elems)
    | section(list[HTMLElement] elems)
    | select(list[HTMLElement] elems)
    | small(list[HTMLElement] elems)
    | source()
    | span(list[HTMLElement] elems)
    | strong(list[HTMLElement] elems)
    | style(list[HTMLElement] elems)
    | sub(list[HTMLElement] elems)
    | summary(list[HTMLElement] elems)
    | sup(list[HTMLElement] elems)
    | table(list[HTMLElement] elems)
    | tbody(list[HTMLElement] elems)
    | td(list[HTMLElement] elems)
    | template(list[HTMLElement] elems)
    | textarea(list[HTMLElement] elems)
    | tfoot(list[HTMLElement] elems)
    | th(list[HTMLElement] elems)
    | thead(list[HTMLElement] elems)
    | time(list[HTMLElement] elems)
    | title(list[HTMLElement] elems)
    | tr(list[HTMLElement] elems)
    | track()
    | u(list[HTMLElement] elems)
    | ul(list[HTMLElement] elems)
    | unknownElement(list[HTMLElement] elems) // <1>
    | var(list[HTMLElement] elems)
    | video(list[HTMLElement] elems)
    | wbr()
    | text(str contents)
    ;

data HTMLElement(
    str abbr  = "",
    str about  = "",
    str accept  = "",
    str accesskey  = "",
    str action  = "",
    str align  = "",
    str allowfullscreen  = "",
    str alt  = "",
    str aria  = "",
    str async  = "",
    str autocomplete  = "",
    str autofocus  = "",
    str autoplay  = "",
    str border  = "",
    str challenge  = "",
    str char  = "",
    str charset  = "",
    str checked  = "",
    str cite  = "",
    str class  = "",
    str cols  = "",
    str colspan  = "",
    str command  = "",
    str content  = "",
    str contenteditable  = "",
    str contextmenu  = "",
    str controls  = "",
    str coords  = "",
    str \data  = "",
    str datatype  = "",
    str \datetime  = "",
    str \default  = "",
    str defer  = "",
    str dir  = "",
    str dirname  = "",
    str disabled  = "",
    str download  = "",
    str draggable  = "",
    str dropzone  = "",
    str enctype  = "",
    str \for  = "",
    str form  = "",
    str formaction  = "",
    str formenctype  = "",
    str formmethod  = "",
    str formnovalidate  = "",
    str formtarget  = "",
    str headers  = "",
    str height  = "",
    str hidden  = "",
    str high  = "",
    str href  = "",
    str hreflang  = "",
    str http  = "",
    str icon  = "",
    str id = "",
    str inlist  = "",
    str ismap  = "",
    str itemid  = "",
    str itemprop  = "",
    str itemref  = "",
    str itemscope  = "",
    str itemtype  = "",
    str keytype  = "",
    str kind  = "",
    str label  = "",
    str lang  = "",
    str language  = "",
    str \list  = "",
    str local_  = "",
    str loop  = "",
    str low  = "",
    str manifest  = "",
    str max  = "",
    str maxlength  = "",
    str media  = "",
    str mediagroup  = "",
    str method  = "",
    str min  = "",
    str multiple  = "",
    str muted  = "",
    str name  = "",
    str novalidate  = "",
    str onabort  = "",
    str onafterprint  = "",
    str onbeforeprint  = "",
    str onbeforeunload  = "",
    str onblur  = "",
    str oncanplay  = "",
    str oncanplaythrough  = "",
    str onchange  = "",
    str onclick  = "",
    str oncontextmenu  = "",
    str ondblclick  = "",
    str ondrag  = "",
    str ondragend  = "",
    str ondragenter  = "",
    str ondragleave  = "",
    str ondragover  = "",
    str ondragstart  = "",
    str ondrop  = "",
    str ondurationchange  = "",
    str onemptied  = "",
    str onended  = "",
    str onerror  = "",
    str onfocus  = "",
    str onformchange  = "",
    str onforminput  = "",
    str onhashchange  = "",
    str oninput  = "",
    str oninvalid  = "",
    str onkeydown  = "",
    str onkeypress  = "",
    str onkeyup  = "",
    str onload  = "",
    str onloadeddata  = "",
    str onloadedmetadata  = "",
    str onloadstart  = "",
    str onmessage  = "",
    str onmousedown  = "",
    str onmousemove  = "",
    str onmouseout  = "",
    str onmouseover  = "",
    str onmouseup  = "",
    str onmousewheel  = "",
    str onoffline  = "",
    str ononline  = "",
    str onpagehide  = "",
    str onpageshow  = "",
    str onpause  = "",
    str onplay  = "",
    str onplaying  = "",
    str onpopstate  = "",
    str onprogress  = "",
    str onratechange  = "",
    str onredo  = "",
    str onreset  = "",
    str onresize  = "",
    str onscroll  = "",
    str onseeked  = "",
    str onseeking  = "",
    str onselect  = "",
    str onshow  = "",
    str onstalled  = "",
    str onstorage  = "",
    str onsubmit  = "",
    str onsuspend  = "",
    str ontimeupdate  = "",
    str onundo  = "",
    str onunload  = "",
    str onvolumechange  = "",
    str onwaiting  = "",
    str open  = "",
    str optimum  = "",
    str pattern  = "",
    str ping  = "",
    str placeholder  = "",
    str poster  = "",
    str prefix  = "",
    str preload  = "",
    str property  = "",
    str radiogroup  = "",
    str readonly  = "",
    str \rel  = "",
    str required  = "",
    str resource  = "",
    str rev  = "",
    str reversed  = "",
    str role  = "",
    str rows  = "",
    str rowspan  = "",
    str sandbox  = "",
    str scope  = "",
    str scoped  = "",
    str seamless  = "",
    str selected  = "",
    str shape  = "",
    str size  = "",
    str sizes  = "",
    str span  = "",
    str spellcheck  = "",
    str src  = "",
    str srcdoc  = "",
    str srclang  = "",
    str \start  = "",
    str step  = "",
    str style  = "",
    str tabindex  = "",
    str target  = "",
    str template  = "",
    str title  = "",
    str translate  = "",
    str \type  = "",
    str typeof  = "",
    str usemap  = "",
    str valign  = "",
    str \value  = "",
    str vocab  = "",
    str width  = "",
    str wrap  = "",
    str xml_base  = "",
    str xml_id  = "",
    str xml_lang  = "",
    str xml_space  = ""
);