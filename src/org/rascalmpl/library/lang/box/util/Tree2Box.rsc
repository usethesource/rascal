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
    | toCapitalized()
    | asIs()
    ;

private Symbol notNl = #![\n].symbol;

@synopsis{This is the generic default formatter}
@description{
This generic formatter is to be overridden by someone constructing a formatter tools
for a specific language. The goal is that this `toBox` default rule maps 
syntax trees to plausible Box expressions, and that only a minimal amount of specialization
by the user is necessary.
}
default Box toBox(t:appl(Production p, list[Tree] args), FO opts = fo()) {
    // the big workhorse switch identifies all kinds of special cases for shapes of
    // grammar rules, and accidental instances (emptiness, only whitespace, etc.)
    
    
    switch (<delabel(p), args>) {
        // nothing should not produce additional spaces
        case <_, []>: 
            return NULL();

        // literals are printed as-is
        case <prod(lit(_), _, _), _>: {
            str yield =  "<t>";
            return yield != "" ? L(yield) : NULL();
        }
        
        // case-insensitive literals are optionally normalized
        case <prod(cilit(_), _, _), _>: {
            str yield =  "<t>"; 
            return yield != "" ?  L(ci("<t>", opts.ci)) : NULL();
        }
        
        // non-existing content should not generate accidental spaces
        case <regular(opt(_)), []>: 
            return NULL(); 

        case <regular(opt(_)), [Tree present]>: 
            return U([toBox(present)]); 
        
        // non-separated lists should stick without spacing (probably lexical)
        case <regular(iter(_)), list[Tree] elements>:
            return H([toBox(e, opts=opts) | e <- elements], hs=0);

        case <regular(\iter-star(_)), list[Tree] elements>:  
            return H([toBox(e, opts=opts) | e <- elements], hs=0);

        // comma's are usually for parameters separation
        case <regular(\iter-seps(_, [_, lit(","), _])), list[Tree] elements>:
            return HOV([
                H([
                    toBox(elements[i], opts=opts),
                    *[H([toBox(elements[i+2], opts=opts)], hs=1) | i + 2 < size(elements)]
                ], hs=0) | int i <- [0,4..size(elements)]
            ]);

        // comma's are usually for parameters separation
        case <regular(\iter-star-seps(_, [_, lit(","), _])), list[Tree] elements>:
            return HOV([
                H([
                    toBox(elements[i], opts=opts),
                    *[H([toBox(elements[i+2], opts=opts)], hs=1) | i + 2 < size(elements)]
                ], hs=0) | int i <- [0,4..size(elements)]
            ]);

         // semi-colons are usually for statement separation
        case <regular(\iter-seps(_, [_, lit(";"), _])), list[Tree] elements>:
            return V([
                H([
                    toBox(elements[i], opts=opts),
                    *[H([toBox(elements[i+2], opts=opts)], hs=1) | i + 2 < size(elements)]
                ], hs=0) | int i <- [0,4..size(elements)]
            ]);

        // optional semi-colons also happen often
        case <regular(\iter-seps(_, [_, opt(lit(";")), _])), list[Tree] elements>:
            return V([
                H([
                    toBox(elements[i], opts=opts),
                    *[H([toBox(elements[i+2], opts=opts)], hs=1) | i + 2 < size(elements)]
                ], hs=0) | int i <- [0,4..size(elements)]
            ]);

        // semi-colons are usually for parameters separation
        case <regular(\iter-star-seps(_, [_, lit(";"), _])), list[Tree] elements>:
            return V([
                H([
                    toBox(elements[i], opts=opts),
                    *[H([toBox(elements[i+2], opts=opts)], hs=1) | i + 2 < size(elements)]
                ], hs=0) | int i <- [0,4..size(elements)]
            ]);

        // optional semi-colons also happen often
        case <regular(\iter-star-seps(_, [_, opt(lit(";")), _])), list[Tree] elements>:
            return V([
                H([
                    toBox(elements[i], opts=opts),
                    *[H([toBox(elements[i+2], opts=opts)], hs=1) | i + 2 < size(elements)]
                ], hs=0) | int i <- [0,4..size(elements)]
            ]);

        case <regular(\iter-seps(_, [_, lit(_), _])), list[Tree] elements>:
            return V([G([toBox(e, opts=opts) | e <- elements], gs=4, op=H([], hs=0))], hs=1);

        case <regular(\iter-star-seps(_, [_, lit(_), _])), list[Tree] elements>:
            return V([G([toBox(e, opts=opts) | e <- elements], gs=4, op=H([], hs=0))], hs=1);
          
        // with only one separator it's probably a lexical
        case <regular(\iter-seps(_, [_])), list[Tree] elements>:
            return V([G([toBox(e, opts=opts) | e <- elements], gs=2, op=H([], hs=0))], hs=0);

        case <regular(\iter-star-seps(_, [_])), list[Tree] elements>:
            return V([G([toBox(e, opts=opts) | e <- elements], gs=2, op=H([], hs=0))], hs=0);

        // We remove all layout node positions to make the number of children predictable
        // Comments can be recovered by `layoutDiff`. By not recursing into layout
        // positions `toBox` becomes more than twice as fast.
        case <prod(layouts(_), _, _), list[Tree] _>:
            return NULL();

        // if we are given a comment node, then we can format it here for use by layoutDiff
        case <prod(_, [lit(_), *_, lit("\n")], {*_, /\tag("category"(/^[Cc]omment$/))}), list[Tree] elements>:
            return V([
                    H([toBox(elements[0], opts=opts), 
                        H([L(e) | e <- words("<elements[..-1]>")], hs=1)
                    ], hs=1)
                ]);

        // if we are given a comment node, then we can pretty print it here for use by layoutDiff     
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
        case <prod(lex(_), _, _), _> : {
            str yield = "<t>";
            return yield != "" ? L(yield) : NULL();
        }

        // Now we will deal with a lot of cases for expressions and block-structured statements.
        // Those kinds of structures appear again and again as many languages share inspiration
        // from their pre-decessors. Watching out not to loose any comments...

        case <prod(sort(x),[sort(x),_,lit(_),_,sort(x)], _), list[Tree] elements>:
            return HOV([toBox(elements[0], opts=opts), H([toBox(e, opts=opts) | e <- elements[1..]])]);

        // postfix operators stick
        case <prod(sort(x),[sort(x),_,lit(_)], _), list[Tree] elements>:
            return H([toBox(e, opts=opts) | e <- elements], hs=0);

        // prefix operators stick
        case <prod(sort(x),[lit(_), _, sort(x)], _), list[Tree] elements>:
            return H([toBox(e, opts=opts) | e <- elements], hs=0);

        // brackets stick
        case <prod(sort(x),[lit("("), _, sort(x), _, lit(")")], _), list[Tree] elements>:
            return H([toBox(e, opts=opts) | e <- elements], hs=0);

        case <prod(_,[_],_), [Tree single]>:
            return toBox(single);

        // if the sort name is statement-like and the structure block-like, we go for 
        // vertical with indentation
        // program: "begin" Declarations decls {Statement  ";"}* body "end" ;
        case <prod(sort(/[stm]/), [*Symbol pre, lit(_), *Symbol _, lit(_)], _), list[Tree] elements>:
            return V([
                H([*[toBox(p, opts=opts) | Tree p <- elements[0..size(pre)]], toBox(elements[size(pre)], opts=opts)]),
                I([V([toBox(e, opts=opts) | Tree e <- elements[size(pre)+1..-1]])]),
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
private Production delabel(prod(Symbol s, list[Symbol] syms, set[Attr] attrs))
    = prod(delabel(s), [delabel(x) | x <- syms], attrs);

private Production delabel(regular(Symbol s)) = regular(delabel(s));

private Symbol delabel(label(_, Symbol s)) = delabel(s);
private Symbol delabel(conditional(Symbol s, _)) = delabel(s);
private default Symbol delabel(Symbol s) = s;

@synopsis{This is a short-hand for legibility's sake}
private FO fo() = formatOptions();

@synopsis{Implements normalization of case-insensitive literals}
private str ci(str word, toLower()) = toLowerCase(word);
private str ci(str word, toUpper()) = toUpperCase(word);
private str ci(str word, toCapitalized()) = capitalize(word);
private str ci(str word, asIs())    = word;

@synopsis{Split a text by the supported whitespace characters}
private list[str] words(str text)
    = [ x | /<x:[^\ \t\n]+>/ := text];