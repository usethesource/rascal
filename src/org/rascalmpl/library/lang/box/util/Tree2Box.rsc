@synopsis{The default formatting rules for _any_ parsetree.}
@description{
This module is meant to be extended to include rules specific for a language.

The main goal of this module is to minimize the number of necessary specializations
for any specific programming language.

This module is a port of the original default formatting rules, implemented in C + ATerm library + APIgen, 
of the "Pandora" in The ASF+SDF Meta-Environment, as described 
in
> M.G.J. van den Brand, A.T. Kooiker, Jurgen J. Vinju, and N.P. Veerman. A Language Independent Framework for 
> Context-sensitive Formatting. In CSMR '06: Proceedings of the Conference on Software Maintenance and 
> Reengineering, pages 103-112, Washington, DC, USA, 2006. IEEE Computer Society Press.

However, due to the more powerful pattern matching available in Rascal, than in C with the ATerm library,
we can specialize for more cases more easily than in the original paper. For example, single and multi-line
comment styles are automatically recognized.

The current algorithm, not extended, additionally guarantees that no comments are lost as long as their grammar
rules have been tagged with `@category="comment"` or the legacy `@category="Comment"`

Another new feature is the normalization of case-insensitive literals. By providing ((toUpper)) or ((toLower))
the mapping algorithm will change every instance of a case-insensitive literal accordingly before translating
it to an L box expression. In case of ((asIs)), the literal will be printed as it occurred in the source code.
}
@examples{
```rascal-shell
import lang::box::\syntax::Box;
extend lang::box::util::Tree2Box;
// Notice how we used `extend` and not `import`, which will be important in the following.
import lang::pico::\syntax::Main;
// First, let's get an example program text
example = "begin
          '%% this is an example Pico program
          '  declare
          '    a : %inline comment% natural,
          '    b : natural;
          '  a := a + b;
          '  b := a - b;
          '  a := a - b
          'end";
// Now we parse it:
program = [start[Program]] example;
// Then we can convert it to a Box tree:
b = toBox(program);
// Finally, we can format the box tree to get a prettier format:
import lang::box::util::Box2Text;
format(b)
// If you are not happy, then you should produce a specialization:
Box toBox((Program) `begin <Declarations decls> <{Statement ";"}* body> end`, FormatOptions opts=formatOptions())
    = V([
        L("begin"),
        I([
            toBox(decls)
        ], is=2),
        I([
            toBox(body)
        ], is=4),
        L("end")
    ]);
// and we see the result here:
format(toBox(program));
```
}
module lang::box::util::Tree2Box

import ParseTree;
import lang::box::\syntax::Box;
import String;

@synopsis{Configuration options for toBox}
data FormatOptions = formatOptions(
    CaseInsensitivity ci = asIs()
);

@synopsis{Normalization choices for case-insensitive literals.}
data CaseInsensitivity
    = toLower()
    | toUpper()
    | asIs()
    ;

@synopsis{This is the generic default formatter}
@description{
This generic formatter is to be overridden by someone constructig a formatter tools
for a specific language. The goal is that this `toBox` default rule maps 
syntax trees to plausible Box expressions, and that only a minimal amount of specialization
by the user is necessary.
}
default Box toBox(t:appl(Production p, list[Tree] args), FO opts = fo()) {
    // the big workhorse switch identifies all kinds of special cases for shapes of
    // grammar rules, and accidental instances (emptiness, only whitespace, etc.)
    Symbol nl = #[\n].symbol;
    Symbol notNl = #![\n].symbol;
    
    switch (<delabel(p), args>) {
        // nothing should not produce additional spaces
        case <_, []>: 
            return NULL();

        // literals are printed as-is
        case <prod(lit(_), _, _), _>: 
            return L("<t>");
        
        // case-insensitive literals are optionally normalized
        case <prod(cilit(_), _, _), _>: 
            return L(ci("<t>", opts.ci));
        
        // non-existing content should not generate accidental spaces
        case <regular(opt(_)), []>: 
            return NULL();
        
        // non-separated lists should stick without spacing (probably lexical)
        case <regular(iter(_)), list[Tree] elements>:
            return H([toBox(e, opts=opts) | e <- elements], hs=0);

        case <regular(\iter-star(_)), list[Tree] elements>:  
            return H([toBox(e, opts=opts) | e <- elements], hs=0);

        case <regular(\iter-seps(_, [_, lit(","), _])), list[Tree] elements>:
            return HV([G([toBox(e, opts=opts) | e <- elements], gs=4, hs=0, op=H)], hs=1);

        case <regular(\iter-seps(_, [_, lit(_), _])), list[Tree] elements>:
            return V([G([toBox(e, opts=opts) | e <- elements], gs=4, hs=0, op=H)], hs=1);

        case <regular(\iter-star-seps(_, [_, lit(_), _])), list[Tree] elements>:
            return V([G([toBox(e, opts=opts) | e <- elements], gs=4, hs=0, op=H)], hs=1);
          
        // with only one separator it's probably a lexical
        case <regular(\iter-seps(_, [_])), list[Tree] elements>:
            return V([G([toBox(e, opts=opts) | e <- elements], gs=2, hs=0, op=H)], hs=0);

        case <regular(\iter-star-seps(_, [_])), list[Tree] elements>:
            return V([G([toBox(e, opts=opts) | e <- elements], gs=2, hs=0, op=H)], hs=0);

        // if comments are found in layout trees, then we include them here
        // and splice them into our context. If the deep match does not find any
        // comments, then layout positions are reduced to U([]) which dissappears
        // by splicing the empty list.
        case <prod(layouts(_), _, _), list[Tree] content>:
            return U([toBox(u, opts=opts) | /u:appl(prod(_, _, {*_,\tag("category"(/^[Cc]omment$/))}), _) <- content]);

        // single line comments are special, since they have the newline in a literal
        // we must guarantee that the formatter will print the newline, but we don't 
        // want an additional newline due to the formatter. We do remove any unnecessary
        // spaces
        case <prod(_, [lit(_), *_, lit("\n")], {*_, /\tag("category"(/^[Cc]omment$/))}), list[Tree] elements>:
            return V([
                    H([toBox(elements[0], opts=opts), 
                        H([L(e) | e <- words("<elements[..-1]>")], hs=1)
                    ], hs=1)
                ]);

        case <prod(_, [lit(_),conditional(\iter-star(notNl),{\end-of-line()})], {*_, /\tag("category"(/^[Cc]omment$/))}), list[Tree] elements>:
            return V([
                    H([toBox(elements[0], opts=opts), 
                        H([L(w) | e <- elements[1..], w <- words("<e>")], hs=1)
                    ], hs=1)
                ]);

        // multiline comments are rewrapped for the sake of readability and fitting on the page
        case <prod(_, [lit(_), *_, lit(_)], {*_, /\tag("category"(/^[Cc]omment$/))}), list[Tree] elements>:
            return HV([toBox(elements[0], opts=opts),                     // recurse in case its a ci literal 
                      *[L(w) | e <- elements[1..-1], w <- words("<e>")], // wrap a nice paragraph
                      toBox(elements[-1], opts=opts)                     // recurse in case its a ci literal 
                    ], hs=1);

        // lexicals are never split in pieces, unless it's comments but those are handled above.
        case <prod(lex(_), _, _), _> :
            return L("<t>");

        // Now we will deal with a lot of cases for expressions and block-structured statements.
        // Those kinds of structures appear again and again as many languages share inspiration
        // from their pre-decessors. Watching out not to loose any comments...

        // we flatten binary operators into their context for better flow of deeply nested
        // operators. The effect will be somewhat like a separated list of expressions where
        // the operators are the separators.
        case <prod(sort(x),[sort(x),_,lit(_),_,sort(x)], _), list[Tree] elements>:
            return U([toBox(e) | e <- elements]);

        // postfix operators stick
        case <prod(sort(x),[sort(x),_,lit(_)], _), list[Tree] elements>:
            return H([toBox(e) | e <- elements], hs=0);

        // prefix operators stick
        case <prod(sort(x),[lit(_), _, sort(x)], _), list[Tree] elements>:
            return H([toBox(e) | e <- elements], hs=0);

        // brackets stick
        case <prod(sort(x),[lit("("), _, sort(x), _, lit(")")], _), list[Tree] elements>:
            return H([toBox(e) | e <- elements], hs=0);

        // if the sort name is statement-like and the structure block-like, we go for 
        // vertical with indentation
        // program: "begin" Declarations decls {Statement  ";"}* body "end" ;
        case <prod(sort(/[stm]/), [lit(_), *_, lit(_)], _), list[Tree] elements>:
            return V([
                toBox(elements[0], opts=opts),
                I([V([toBox(e, opts=opts) | e <- elements[1..-1]])]),
                toBox(elements[-1], opts=opts)
            ]);
    }

    return HV([toBox(a, opts=opts) | a <- args]);
}

@synopsis{For ambiguity clusters an arbitrary choice is made.}
default Box toBox(amb({Tree t, *Tree _}), FO opts=fo()) = toBox(t);

@synopsis{When we end up here we simply render the unicode codepoint back.}
default Box toBox(c:char(_), FormatOptions opts=fo() ) = L("<c>");

@synopsis{Cycles are invisible and zero length}
default Box toBox(cycle(_, _), FO opts=fo()) = NULL();

@synopsis{Private type alias for legibility's sake}
private alias FO = FormatOptions;

@synopsis{Removing production labels removes similar patterns in the main toBox function.}
private Production delabel(prod(label(_, Symbol s), list[Symbol] syms, set[Attr] attrs))
    = prod(s, delabel(syms), attrs);

private default Production delabel(Production p) = p;

private list[Symbol] delabel(list[Symbol] syms) = [delabel(s) | s <- syms];

private Symbol delabel(label(_, Symbol s)) = s;
private default Symbol delabel(Symbol s) = s;

@synopsis{This is a short-hand for legibility's sake}
private FO fo() = formatOptions();

@synopsis{Implements normalization of case-insensitive literals}
private str ci(str word, toLower()) = toLowerCase(word);
private str ci(str word, toUpper()) = toUpperCase(word);
private str ci(str word, asIs())    = word;

@synopsis{Split a text by the supported whitespace characters}
private list[str] words(str text)
    = [ x | /<x:[^\ \t\n]+>/ := text];