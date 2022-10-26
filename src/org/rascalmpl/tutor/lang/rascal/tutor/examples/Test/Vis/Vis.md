---
title: Visuals Test
---

```rascal-shell
import lang::html::IO;
serve(p([text("This is"), b([text("bold")]), text("!")]))
```

```rascal-shell
import lang::html::IO;
HTMLElement table(rel[&T, &U] r)
    = table([
        tr([
            td([text("<a>")]),
            td([text("<b>")])
        ])
    | <a, b> <- r    
    ]);
characters = {"Sneezy", "Sleepy", "Dopey", "Doc", "Happy", "Bashful", "Grumpy"};
serve(table(characters * characters))
```

