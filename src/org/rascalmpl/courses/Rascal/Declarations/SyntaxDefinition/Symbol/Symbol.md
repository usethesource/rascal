# Symbol

.Synopsis
The symbols that can occur in a syntax definition.

.Index
" ' [ ] ! - || && ( ) ? + * { } | () ^ @ >> !>> << !<< \

.Syntax

Nonterminal symbols are identifier names that _start with an uppercase letter_.


| Symbol                      | Description |
| --- | --- |
| `_Symbol_ _fieldName_`      | Any symbol can be labeled with a field name that _starts with a lowercase letter_ |



The following literal symbols and character classes are defined:



| Symbol                        | Description |
| --- | --- |
|`"_stringliteral_"`            | Literal string |
|`'_stringliteral_'`            | Case-insensitive literal string |
|`[_range~1~_ _range~2~_ ... ]` | Character class |




The following operations on character classes can be composed arbitrarily:


| Class                        | Description  |
| --- | --- |
|`!_Class_`                    | Complement of `_Class_` with respect to the UTF8 universe of characters |
| `_Class~1~_ - _Class~2~_`    | Difference of character classes `_Class~1~_` and `_Class~2~_`              |
| `_Class~1~_ \|\| _Class~2~_` | Union of character classes `_Class~1~_` and `_Class~2~_`                   |
| `_Class~1~_ && _Class~2~_`   | Intersection of character classes `_Class~1~_` and `_Class~2~_`            |
| `(_Class_)`                  | Brackets for defining application order of class operators               |




The following regular expressions can be constructed over ((SyntaxDefinition-Symbol))s:


| Symbol                                 | Description                                                          |
| --- | --- |
| `_Symbol_?`                            | Optional _Symbol_                                                    |
| `_Symbol_+`                            | Non-empty list of _Symbol_s                                          |
| `_Symbol_*`                            | Possibly empty list of _Symbol_s.                                    |
| `{_Symbol~1~_ _Symbol~2~_}+`           | Non-empty list of _Symbol~1~_ separated by _Symbol~2~_                 |
| `{_Symbol~1~_ _Symbol~2~_}*`           | Possibly empty list of _Symbol~1~_ separated by _Symbol~2~_.           |
| `(_Symbol~1~_ _Symbol~2~_ ... )`       | Embedded sequence of symbols                                         |
| `(_Symbol~1~_ \| _Symbol~2~_ \| ... )` | Embedded choice of alternative symbols                               |
| `()`                                   | The anonymous non-terminal for the language with the empty string   |




Inline conditions (((Disambiguation))s) can be added to symbols to constrain their acceptability:


| Disambiguation                             | Description                                                 |
| --- | --- |
| `_Symbol_ _`                 | _Symbol_ ends at end of line or end of file                     |
| `^_Symbol_`                  | _Symbol_ starts at begin of line                                |
| `_Symbol_ @ _ColumnIndex_`   | _Symbol_ starts at certain column index.                        |
| `_Symbol~1~_ >> _Symbol~2~_`   | _Symbol~1~_ must be (directly) followed by _Symbol~2~_            |
| `_Symbol~1~_ !>> _Symbol~2~_`  | _Symbol~1~_ must _not_ be (directly) followed by _Symbol~2~_      |
| `_Symbol~1~_ << _Symbol~2~_`   | _Symbol~2~_ must be (directly) preceded by _Symbol~1~_            |
| `_Symbol~1~_ !<< _Symbol~2~_`  | _Symbol~2~_ must _not_ be (directly) preceded by _Symbol~1~_      |
| `_Symbol~1~_ \ _Symbol~2~_`   | _Symbol~1~_ must not be in the language defined by _Symbol~2~_    |




Symbols can be composed arbitrarily.

.Types
Every non-terminal symbol is a type.

.Function

.Details

.Description
The basic symbols are the non-terminal name and the labeled non-terminal name. 
These refer to the names defined by ((Syntax Definition)). 
You can use any defined non-terminal name in any other definition (lexical in syntax, syntax in lexical, etc). 

Then we have literals and character classes to define the _terminals_ of a grammar. 
When you use a literal such as `"begin"`, Rascal will produce a definition for it down to the character level before generating a parser: `syntax "begin" = [b][e][g][i][n];`. This effect will be visible in the ((Parse Trees)) produced by the parser. For case insensitive literals you will see a similar effect; the use of `'begin'` produces `syntax 'begin' = [bB][eE][gG][iI][nN]`.

Character classes have the same escaping conventions as characters in a ((Values-String)) literal, but spaces and newlines are meaningless and have to be escaped and the `[` and `]` brackets as well as the dash `-` need escaping. For example, one writes `[\[ \] \ \n\-]` for a class that includes the open and close square brackets and a space, a newline and a dash. Character classes support ranges as in `[a-zA-Z0-9]`. Please note about character classes that:

*  the operations on character classes are executed before parser generation time. You will not find explicit representation of these operations in [ParseTrees], but rather their net effect as resulting character classes. 
*  Character classes are also ordered by Rascal and overlapping ranges are merged before parsers are generated. Equality between character classes is checked after this canonicalization.
*  Although all ((Symbol))s are type constructors, the character class operators are not allowed in types.


The other symbols either _generate_ for you parts of the construction of a grammar, or they _constrain_ the rules of the grammar to generate a smaller set of trees as ((Disambiguation))s.

The _generative symbols_ are referred to as the _regular symbols_. These are like named non-terminals, except that they are defined implicitly and interpreted by the parser generator to produce a parser that can recognize a symbol optionally, iteratively, alternatively, sequentially, etc. You also need to know this about the regular symbols:

*  In ((Parse Trees)) you will find special nodes for the regular expression symbols that hide _how_ these were recognized. 
*  ((Patterns)) using ((Concrete Syntax)) have special semantics for the regular symbols (list matching, separator handling, ignoring layout, etc.).
*  Regular symbols are not allowed in _keyword_ ((Syntax Definition))s
*  Depending on their occurrence in a _lexical_, _syntax_ or _layout_ ((Syntax Definition)) 
   the semantics of regular symbols changes. In the _syntax_ context, layout non-terminals will be woven 
   into the regular symbol, but not in the _lexical_ and _layout_ contexts. 
   For example, a `_Symbol_\*` in a _syntax_ definition such as `syntax X = A*;` will be processed to `syntax X = `{A Layout}*`. Similarly, `syntax X = {A B}+;` will be processed to `syntax X = {A (Layout B Layout)}+;`. 


The _constraint_ symbols are specially there to deal with the fact that Rascal does not generate a scanner. There are no a priori disambiguation rules such as prefer keywords or longest match. Instead, you should use the constraint symbols to define the effect of keyword reservation and longest match. 

*  It is important to note that these constraints work on a character-by-character level in the input stream. So, a follow constraint such as `A >> [a-z]` means that the character immediately following a recognized A must be in the range `[a-z]`.
*  Read more on the constraint symbols via ((Disambiguation))s.

.Examples


A character class that defines all alphanumeric characters:
```rascal
lexical AlphaNumeric = [a-zA-Z0-9];
```
A character class that defines anything except quotes:
```rascal
lexical AnythingExceptQuote = ![\"];
```
An identifier class with longest match (can not be followed immediately by [a-z]):
```rascal
lexical Id = [a-z]+ !>> [a-z];
```
An identifier class with longest match and first match (can not be preceded or followed by [a-z]):
```rascal-shell,continue
lexical Id = [a-z] !<< [a-z]+ !>> [a-z];
```
An identifier class with some reserved keywords and longest match:
```rascal
lexical Id = [a-z]+ !>> [a-z] \ "if" \ "else" \ "fi";
```
An optional else branch coded using sequence and optional symbols:
```rascal
syntax Statement = "if" Expression "then" Statement ("else" Statement)? "fi";
```
A block of statements separated by semicolons:
```rascal
syntax Statement = "{" {Statement ";"}* "}";
```
A declaration with an embedded list of alternative modifiers and a list of typed parameters:
```rascal
syntax Declaration = ("public" | "private" | "static" | "final")* Type Id "(" {(Type Id) ","}* ")" Statement;
```

.Benefits

*  The symbol language is very expressive and can lead to short definitions of complex syntactic constructs.
*  There is no built-in longest match for iterators, which makes syntax definitions open to languages that do not have longest match.
*  There is no built-in keyword preference or reservation, which makes syntax definitions open to language composition and legacy languages.

.Pitfalls

*  By nesting too many symbols definitions can be become hard to understand. 
*  By nesting too many symbols pattern matching and term construction becomes more complex. Extra non-terminals and rules with meaningful names can make a language specification more manageable. 
*  The lack of automatic longest match and prefer keyword heuristics (you have to define it yourself), sometimes leads to unexpected ambiguity. See [Disambiguation].

