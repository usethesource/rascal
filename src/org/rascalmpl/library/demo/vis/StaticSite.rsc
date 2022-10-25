@license{
Copyright (c) 2022 CWI
All rights reserved. This program and the accompanying materials
are made available under the terms of the Eclipse Public License v1.0
which accompanies this distribution, and is available at
http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen Vinju - Jurgen.Vinju@cwi.nl}
@synopsis{Demonstrates HTML generation and viewing in Rascal}
@description{
In the current module we have a function `table` which transforms
any binary relation into an HTML table:

```rascal-include
demo::vis::StaticSite
```

When we try this out on the commandline, the REPL will pop-up
a browser window such that we can visualize the result:

```rascal-shell
import demo::vis::StaticSite;
characters = {"Sneezy", "Sleepy", "Dopey", "Doc", "Happy", "Bashful", "Grumpy"};
serve(characters * characters);
```

To get this effect we used the following library modules:
* ((lang::html::AST)) contains the HTML abstract syntax tree definition
* ((lang::html::IO)) knows how to pretty-print HTML
* ((Rascal:module:Content)) provides access to the builtin application server of the Rascal REPL
}
module demo::vis::StaticSite

import lang::html::IO;

@synopsis{Translates a binary relation to an HTML table element}
HTMLElement table(rel[&T, &U] r)
    = table([
        tr([
            td([text("<a>")]),
            td([text("<b>")])
        ])
    | <a, b> <- r    
    ]);


