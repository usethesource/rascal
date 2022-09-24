module lang::html::AST

data HTMLElement
    = a(list[HTMLElement] elems)
    | abbr(list[HTMLElement] elems)
    | address(list[HTMLElement] elems)
    | area(list[HTMLElement] elems)
    | article(list[HTMLElement] elems)
    | aside(list[HTMLElement] elems)
    | audio(list[HTMLElement] elems)
    | b(list[HTMLElement] elems)
    | base(list[HTMLElement] elems)
    | bdi(list[HTMLElement] elems)
    | bdo(list[HTMLElement] elems)
    | blockquote(list[HTMLElement] elems)
    | body(list[HTMLElement] elems)
    | br(list[HTMLElement] elems)
    | button(list[HTMLElement] elems)
    | canvas(list[HTMLElement] elems)
    | caption(list[HTMLElement] elems)
    | cite(list[HTMLElement] elems)
    | code(list[HTMLElement] elems)
    | col(list[HTMLElement] elems)
    | colgroup(list[HTMLElement] elems)
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
    | embed(list[HTMLElement] elems)
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
    | hr(list[HTMLElement] elems)
    | html(list[HTMLElement] elems)
    | i(list[HTMLElement] elems)
    | iframe(list[HTMLElement] elems)
    | img(list[HTMLElement] elems)
    | input(list[HTMLElement] elems)
    | ins(list[HTMLElement] elems)
    | kbd(list[HTMLElement] elems)
    | keygen(list[HTMLElement] elems)
    | label(list[HTMLElement] elems)
    | legend(list[HTMLElement] elems)
    | li(list[HTMLElement] elems)
    | link(list[HTMLElement] elems)
    | main(list[HTMLElement] elems)
    | \map(list[HTMLElement] elems)
    | mark(list[HTMLElement] elems)
    | menu(list[HTMLElement] elems)
    | menuitem(list[HTMLElement] elems)
    | meta(list[HTMLElement] elems)
    | meter(list[HTMLElement] elems)
    | nav(list[HTMLElement] elems)
    | noscript(list[HTMLElement] elems)
    | object(list[HTMLElement] elems)
    | ol(list[HTMLElement] elems)
    | optgroup(list[HTMLElement] elems)
    | option(list[HTMLElement] elems)
    | output(list[HTMLElement] elems)
    | p(list[HTMLElement] elems)
    | param(list[HTMLElement] elems)
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
    | source(list[HTMLElement] elems)
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
    | track(list[HTMLElement] elems)
    | u(list[HTMLElement] elems)
    | ul(list[HTMLElement] elems)
    | var(list[HTMLElement] elems)
    | video(list[HTMLElement] elems)
    | wbr(list[HTMLElement] elems)
    ;

data HTMLElement(
    value abbr  = "",
    value about  = "",
    value accept  = "",
    value accesskey  = "",
    value action  = "",
    value align  = "",
    value allowfullscreen  = "",
    value alt  = "",
    value aria  = "",
    value async  = "",
    value autocomplete  = "",
    value autofocus  = "",
    value autoplay  = "",
    value border  = "",
    value challenge  = "",
    value char  = "",
    value charset  = "",
    value checked  = "",
    value cite  = "",
    value class  = "",
    value cols  = "",
    value colspan  = "",
    value command  = "",
    value content  = "",
    value contenteditable  = "",
    value contextmenu  = "",
    value controls  = "",
    value coords  = "",
    value \data  = "",
    value datatype  = "",
    value \datetime  = "",
    value \default  = "",
    value defer  = "",
    value dir  = "",
    value dirname  = "",
    value disabled  = "",
    value download  = "",
    value draggable  = "",
    value dropzone  = "",
    value enctype  = "",
    value \for  = "",
    value form  = "",
    value formaction  = "",
    value formenctype  = "",
    value formmethod  = "",
    value formnovalidate  = "",
    value formtarget  = "",
    value headers  = "",
    value height  = "",
    value hidden  = "",
    value high  = "",
    value href  = "",
    value hreflang  = "",
    value http  = "",
    value icon  = "",
    value id  = "",
    value inlist  = "",
    value ismap  = "",
    value itemid  = "",
    value itemprop  = "",
    value itemref  = "",
    value itemscope  = "",
    value itemtype  = "",
    value keytype  = "",
    value kind  = "",
    value label  = "",
    value lang  = "",
    value language  = "",
    value \list  = "",
    value local_  = "",
    value loop  = "",
    value low  = "",
    value manifest  = "",
    value max  = "",
    value maxlength  = "",
    value media  = "",
    value mediagroup  = "",
    value method  = "",
    value min  = "",
    value multiple  = "",
    value muted  = "",
    value name  = "",
    value novalidate  = "",
    value onabort  = "",
    value onafterprint  = "",
    value onbeforeprint  = "",
    value onbeforeunload  = "",
    value onblur  = "",
    value oncanplay  = "",
    value oncanplaythrough  = "",
    value onchange  = "",
    value onclick  = "",
    value oncontextmenu  = "",
    value ondblclick  = "",
    value ondrag  = "",
    value ondragend  = "",
    value ondragenter  = "",
    value ondragleave  = "",
    value ondragover  = "",
    value ondragstart  = "",
    value ondrop  = "",
    value ondurationchange  = "",
    value onemptied  = "",
    value onended  = "",
    value onerror  = "",
    value onfocus  = "",
    value onformchange  = "",
    value onforminput  = "",
    value onhashchange  = "",
    value oninput  = "",
    value oninvalid  = "",
    value onkeydown  = "",
    value onkeypress  = "",
    value onkeyup  = "",
    value onload  = "",
    value onloadeddata  = "",
    value onloadedmetadata  = "",
    value onloadstart  = "",
    value onmessage  = "",
    value onmousedown  = "",
    value onmousemove  = "",
    value onmouseout  = "",
    value onmouseover  = "",
    value onmouseup  = "",
    value onmousewheel  = "",
    value onoffline  = "",
    value ononline  = "",
    value onpagehide  = "",
    value onpageshow  = "",
    value onpause  = "",
    value onplay  = "",
    value onplaying  = "",
    value onpopstate  = "",
    value onprogress  = "",
    value onratechange  = "",
    value onredo  = "",
    value onreset  = "",
    value onresize  = "",
    value onscroll  = "",
    value onseeked  = "",
    value onseeking  = "",
    value onselect  = "",
    value onshow  = "",
    value onstalled  = "",
    value onstorage  = "",
    value onsubmit  = "",
    value onsuspend  = "",
    value ontimeupdate  = "",
    value onundo  = "",
    value onunload  = "",
    value onvolumechange  = "",
    value onwaiting  = "",
    value open  = "",
    value optimum  = "",
    value pattern  = "",
    value ping  = "",
    value placeholder  = "",
    value poster  = "",
    value prefix  = "",
    value preload  = "",
    value property  = "",
    value radiogroup  = "",
    value readonly  = "",
    value \rel  = "",
    value required  = "",
    value resource  = "",
    value rev  = "",
    value reversed  = "",
    value role  = "",
    value rows  = "",
    value rowspan  = "",
    value sandbox  = "",
    value scope  = "",
    value scoped  = "",
    value seamless  = "",
    value selected  = "",
    value shape  = "",
    value size  = "",
    value sizes  = "",
    value span  = "",
    value spellcheck  = "",
    value src  = "",
    value srcdoc  = "",
    value srclang  = "",
    value \start  = "",
    value step  = "",
    value style  = "",
    value tabindex  = "",
    value target  = "",
    value template  = "",
    value title  = "",
    value translate  = "",
    value \type  = "",
    value typeof  = "",
    value usemap  = "",
    value valign  = "",
    value \value  = "",
    value vocab  = "",
    value width  = "",
    value wrap  = "",
    value xml_base  = "",
    value xml_id  = "",
    value xml_lang  = "",
    value xml_space  = ""
);