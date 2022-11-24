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
import util::Math;
import util::Sampling;
import Content;
import IO;
import ValueIO;
import List;
import Set;
import Map;

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
        b([text("<type(typeOf(l), ())> (<sampled(l, 100)>)")]), 
        ul([
            li([toHTML(e)])
            | e <- sample(l, 100)
        ])
    ]);

default HTMLElement toHTML(set[value] l)
    = div([
        b([text("<type(typeOf(l), ())> (<sampled(l, 100)>)")]), 
        ul([
            li([toHTML(e)])
            | e <- sample(l, 100)
        ])
    ]);


HTMLElement toHTML(rel[value,value] r)
    = div([
        b([text("<type(typeOf(r), ())> <sampled(r, 100)>")]), 
        table([
            tr([
                td([toHTML(a)]),
                td([toHTML(b)])
            ])
            | <a,b> <- sample(r, 100)
        ], border="1")
    ]);

HTMLElement toHTML(rel[value,value,value] r)
    = div([
        b([text("<type(typeOf(r), ())> <sampled(r, 100)>")]), 
        table([
            tr([
                td([toHTML(a)]),
                td([toHTML(b)]),
                td([toHTML(c)])
            ])
            | <a,b,c> <- sample(r, 100)
        ], border="1")
    ]);

HTMLElement toHTML(rel[value,value,value,value] r)
    = div([
        b([text("<type(typeOf(r), ())> <sampled(r, 100)>")]), 
        table([
            tr([
                td([toHTML(a)]),
                td([toHTML(b)]),
                td([toHTML(c)]),
                td([toHTML(d)])
            ])
            | <a,b,c,d> <- sample(r, 100)
        ], border="1")
    ]);

HTMLElement toHTML(map[value,value] m)
    = div([
        b([text("<type(typeOf(m), ())> <sampled(m, 100)>")]), 
        table([
            tr([
                td([toHTML(k)]),
                td([toHTML(m[k])])
            ])
            | k <- sample(m, 100)
        ], border="1")
    ]);

HTMLElement toHTML(t:<value a, value bb>) 
    = div([
            b([text("<type(typeOf(t), ())>")]), 
            table([
                tr([
                    td([toHTML(a)]),
                    td([toHTML(bb)])
                ])
            ], border="1")
    ]);

HTMLElement toHTML(t:<value a, value bb, value c>) 
    = div([
            b([text("<type(typeOf(t), ())>")]), 
            table([
                tr([
                    td([toHTML(a)]),
                    td([toHTML(bb)]),
                    td([toHTML(c)])
                ])
            ], border="1")
    ]);

HTMLElement toHTML(t:<value a, value bb, value c, value d>) 
    = div([
            b([text("<type(typeOf(t), ())>")]), 
            table([
                tr([
                    td([toHTML(a)]),
                    td([toHTML(bb)]),
                    td([toHTML(c)]),
                    td([toHTML(d)])
                ])
            ], border="1")
    ]);

HTMLElement toHTML(Tree t:appl(Production p, list[Tree] args)) 
    = div([
        text(topProd2rascal(p)),
        *(t@\loc? ? [toHTML(t@\loc)] : []),
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
                    td([toHTML(k)], style="vertical-align:top"),
                    td([toHTML(kws[k])], style="vertical-align:top")
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

private str sampled(list[value] s, int count) 
    = size(s) > count ? "head <count>/<size(s)>" : "";

private str sampled(set[value] s, int count) 
    = size(s) > count ? "sampled <count>/<size(s)>" : "";

private str sampled(map[value,value] s, int count) 
    = size(s) > count ? "sampled <count>/<size(s)>" : "";
