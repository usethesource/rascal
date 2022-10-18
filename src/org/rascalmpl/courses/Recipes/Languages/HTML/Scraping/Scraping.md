---
title: HTML Scraping
keywords:
    - scraping
    - "pattern matching"
    - recursion
    - html
---

#### Synopsis

Scraping HTML is recovering data from HTML documents

#### Description

In this example we see HTML as just another language that may contain relevant information. In the case
of HTML it is smart to reuse existing parsers, so we use an ((AbstractSyntax)) format for HTML. It is
described in ((lang::html::AST)) and its IO interface is described in ((lang::html::IO)).

In this demo we extract information from the website of the [Centraal Bureau voor Statistiek (CBS)](https://www.cbs.nl), the Dutch national centre for statistics. 

We found an interesting [page](https://longreads.cbs.nl/nederland-in-cijfers-2022/hoeveel-fietsen-we-gemiddeld-per-week/) that lists how much biking the Dutchies do on average weekly:

```rascal-shell
import lang::html::IO;
import IO;

page = readHTMLFile(|https://longreads.cbs.nl/nederland-in-cijfers-2022/hoeveel-fietsen-we-gemiddeld-per-week/|);
```

As you can see the output is truncated with `...`, to see more we can use ((IO-iprintln)):

```rascal-shell,continue
iprintln(page)
```

We used Chrome's "Inspect" feature to figure out that the div class `datatable-container` is of interest.
So let's select that using a deep match operator and bind that div to the `tab` variable:

```rascal-shell,continue
if (/tab:div(_,class=/datatable-container/) := page)
  iprintln(tab);
```

We used a deep match pattern and then a regular expression pattern to select all `class` attributes that have `datatable-container` somewhere in the string.

Every row in the table contains data, except the header row. Let's convert this entire table 
to a relation of type `rel[str persoonskenmerken, real fietskilometers]`. 

We create the match pattern by step-wise refinement. First let's just 
list all the rows:

```rascal-shell,continue
if (/tab:div(rows,class=/datatable-container/) := page) { // <1>
    for (/r:tr(_) := rows) { // <2>
        println(r);
    }
}
```

* <1> binds the children of the div to `rows`;
* <2> uses deep match `/` to quickly jump to all the nested `tr` nodes;

Now we refined the pattern to filter out the non-header rows:

```rascal-shell,continue
if (/tab:div(rows,class=/datatable-container/) := page) { 
    for (/r:tr([th(_,scope="row"), td(_)]) := rows) { // <3>
        println(r);
    }
}
```

* <3> we matching only those `tr` that have two children, one `th` and one `td`. To be sure we also limit the first `th` to have the `scope` attribute equal to `"row"`.

Now it's time to get the final data out. The category is in the first column and the numbers are in the second.
We could make the query deeper and more complex, but we choose to add another nesting level for the sake of clarity:

```rascal-shell,continue
if (/tab:div(rows,class=/datatable-container/) := page) { 
    for (/r:tr([category:th(_,scope="row"), number:td(_)]) := rows) { 
        if (/text(str c) := category, /text(str n) := number) {
            println("<c> --- <n>");
        }
    }
}
```

Now we have scraped the data out of the HTML syntax tree, we have to convert it to 
raw data. But the Dutch use comma's as decimal separators:

```rascal-shell,continue,error
import String;
toReal("18,79");
toReal("18.79");
replaceAll("18,79", ",", ".")
```

```rascal-shell,continue
rel[str persoonskenmerken, real fietskilometers] myData = {};
if (/tab:div(rows,class=/datatable-container/) := page) { 
    for (/r:tr([category:th(_,scope="row"), number:td(_)]) := rows) { 
        if (/text(str c) := category, /text(str n) := number) {
            println("<c> --- <n>");
            myData += <c, toReal(replaceAll(n, ",", "."))>;
        }
    }
}
myData;
```

Now we have the data in a format that we can compute with:
```rascal-shell
myData<persoonskenmerken>
import Set;
theSum = sum(myData<fietskilometers>);
relativeData = { <pk, round(avg / theSum * 100.0, 0.1) > | <pk, avg> <- myData};

To keep this analysis for the future, for example when new data is published on the site, we
can store the query in a function. It is also ready to be rewritten from structured programming
style into a functional comprehension. Let's do that first:

```rascal-shell,continue
{ <c, toReal(replaceAll(n, ",", "."))>                      
| /tab:div(rows,class=/datatable-container/) := page        
, /r:tr([category:th(_,scope="row"), number:td(_)]) := rows 
, /text(str c) := category, /text(str n) := number          
}
```

The patterns have _not_ changed, only they have been copied to the generator/filter side
of a ((Set-Comprehension)):
* <1> here we have the resulting tuple that uses `c` and `n` which have been selected by pattern matching
* <2> this is the first selector that finds the table in the page
* <3> here we iterate over the rows that are not the header
* <4> finally we project out the text from the two cells.

Now we wrap it all up in a reusable function:
```rascal-shell,continue
rel[str persoonskenmerken, real fietskilometers] scrapeFietskilometers(loc address=|https://longreads.cbs.nl/nederland-in-cijfers-2022/hoeveel-fietsen-we-gemiddeld-per-week/|) 
    = { <c, toReal(replaceAll(n, ",", "."))>                      
        | /tab:div(rows,class=/datatable-container/)        := readHTMLFile(address)        
        , /r:tr([category:th(_,scope="row"), number:td(_)]) := rows 
        , /text(str c) := category, /text(str n)            := number          
    };
scrapeFietsKilometers()
```

Every time the function is called, the HTML is retrieved again from the site. We coded the
URL in a default parameter, just in case a similar page exists that we might try our analysis
on. 

#### Benefits

* Rascal has a lot of powerful ((PatternMatching)) operators to dissect a HTML page with;
* Skills used in the analysis of programming languages, like traversal and pattern matching, are equally useful for HTML scraping;
* Deep matching and ((Statements-Visit)) skip over all uninteresting content without depending on it. The more you use these "structure shy" primitives, the more robust the scraper will be against sudden changes in the HTML.

#### Pitfalls

* HTML scraping is a *brittle* business. If the page changes, then it's likely the query will not work
anymore. The function will start returning empty sets of tuples in that case, most likely. If we look at the
structural dependencies then the word `datatable-container` is very important. Also this query matches only
tables with two columns, and the first cell is always a `th` and the second cell is `td`. Finally the actual
data is stored in a single text cell under the `th` and `td`. If any of these properties change, this scraper 
breaks. However, if _anything else_ changes, the scraper keeps working;
* The HTML parser skips SVG elements;