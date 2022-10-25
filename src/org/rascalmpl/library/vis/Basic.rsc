@license{
  Copyright (c) 2022 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@synopsis{Uses static HTML to visualize arbitrary Rascal values}
@description{
This modules provides a simple mapping from hierarchical (nested)
Rascal values to HTML markup. The goal of the mapping is to provide
a more visible and searchable representation of Rascal values than
the standard textual expression format.

This module is quite new and may undergo some tweaks in the coming time.
}
module vis::Basic

import lang::html::IO;
import lang::rascal::format::Grammar;
import ParseTree;
import Node;
import util::IDEServices;
import Content;
import IO;
import ValueIO;

Content showValue(value v) 
    = content(md5Hash(v), valueServer(v));

Response (Request) valueServer(value v) {
    Response reply(get(/^\/editor/, parameters=pms)) {
        if (pms["src"]?) {
            edit(readTextValueString(#loc, pms["src"]));
            return response(writeHTMLString(text("done")));
        }

        return response(writeHTMLString(text("could not edit <pms>")));
    }

    default Response reply(get(_)) {
        return response(writeHTMLString(toHTML(v)));
    }

    return reply;
}

HTMLElement toHTML(num i) = text("<i>");
HTMLElement toHTML(str s) = pre([p([text(s)])]);
HTMLElement toHTML(loc l) = a([text("<l>")], href="#", onclick="fetch(\"/editor?src=<l>\")");

HTMLElement toHTML(list[value] l)
    = div([
        b([text("<type(typeOf(l), ())>")]), 
        ul([
            li([toHTML(e)])
            | e <- l
        ])
    ]);

default HTMLElement toHTML(set[value] l)
    = div([
        b([text("<type(typeOf(l), ())>")]), 
        ul([
            li([toHTML(e)])
            | e <- l
        ])
    ]);

HTMLElement toHTML(rel[value,value] r)
    = div([
        b([text("<type(typeOf(r), ())>")]), 
        table([
            tr([
                td([toHTML(a)]),
                td([toHTML(b)])
            ])
            | <a,b> <- r
        ], border="1")
    ]);

HTMLElement toHTML(rel[value,value,value] r)
    = div([
        b([text("<type(typeOf(r), ())>")]), 
        table([
            tr([
                td([toHTML(a)]),
                td([toHTML(b)]),
                td([toHTML(c)])
            ])
            | <a,b,c> <- r
        ], border="1")
    ]);

HTMLElement toHTML(rel[value,value,value,value] r)
    = div([
        b([text("<type(typeOf(r), ())>")]), 
        table([
            tr([
                td([toHTML(a)]),
                td([toHTML(b)]),
                td([toHTML(c)]),
                td([toHTML(d)])
            ])
            | <a,b,c,d> <- r
        ], border="1")
    ]);

HTMLElement toHTML(map[value,value] m)
    = div([
        b([text("<type(typeOf(m), ())>")]), 
        table([
            tr([
                td([toHTML(k)]),
                td([toHTML(m[k])])
            ])
            | m <- m
        ], border="1")
    ]);

HTMLElement toHTML(t:<value a, value b>) 
    = div([
            b([text("<type(typeOf(t), ())>")]), 
            table([
                tr([
                    td([toHTML(a)]),
                    td([toHTML(b)])
                ])
            ], border="1")
    ]);

HTMLElement toHTML(t:<value a, value b, value c>) 
    = div([
            b([text("<type(typeOf(t), ())>")]), 
            table([
                tr([
                    td([toHTML(a)]),
                    td([toHTML(b)]),
                    td([toHTML(c)])
                ])
            ], border="1")
    ]);

HTMLElement toHTML(t:<value a, value b, value c, value d>) 
    = div([
            b([text("<type(typeOf(t), ())>")]), 
            table([
                tr([
                    td([toHTML(a)]),
                    td([toHTML(b)]),
                    td([toHTML(c)]),
                    td([toHTML(d)])
                ])
            ], border="1")
    ]);

HTMLElement toHTML(Tree t:appl(Production p, list[Tree] args)) 
    = div([
        text(topProd2rascal(p)),
        ul([
            li([toHTML(a)])
            | a <- args
        ])
    ]);

HTMLElement toHTML(Tree t:amb(set[Tree] alts)) 
    = div([
        text(symbol2rascal(typeOf(t))),
        ul([
            li([toHTML(a)])
            | a <- alts
        ])
    ]);

HTMLElement toHTML(char(int c)) 
    = pre([text("<char(c)> - codepoint <c>")]);

HTMLElement toHTML(node n) 
    = div([
        p([b([text("node:")]), text(getName(n))]),
        *[
            table([
                tr([
                    td([toHTML(k)]),
                    td([toHTML(kws[k])])
                ])
                | k <- kws
            ], border="1")
        | kws := getKeywordParameters(n), kws != ()
        ],
        ul([
            li([toHTML(a)])
            | a <- n
        ])
    ]);

default HTMLElement toHTML(value x:!set[value] _) // set is also a default to avoid clashing with rel
    = text("<x>");