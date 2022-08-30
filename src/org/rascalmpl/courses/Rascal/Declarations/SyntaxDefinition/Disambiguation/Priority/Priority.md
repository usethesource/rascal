# Priority Declaration

.Synopsis
Declare the priority of operators.

.Index
> |

.Syntax

*  `syntax _Exp_ = _alt~1~_ > _alt~2~_ > _alt~3~_` is the basic syntax for priorities.
*  `syntax _Exp_ = _alt~1~_ | _alt~2~_ > _alt~3~_ | _alt~4~_`, where the `|` signifies groups of equal priority
*  `syntax _Exp_ = _associativity_ ( _alt~1~ | ... ) > _alt~2~`, where an associativity group denotes a group of equal priority

.Types

.Function

.Details

.Description
Priority declarations define a partial ordering between the productions _within a single non-terminal_. The feature is specifically designed to fit with the semantics of expression sub-languages embedded in programming languages. There exist other mechanisms for ((Disambiguation)), if ((Disambiguation-Priority)) does not work for you.

The semantics of a priority relation `A > B` is that B will not be nested under A in the left-most or right-most position.
Any other position of A will allow B fine. Note that the priority relation you define is transitively closed, so if A > B and B > C then A > C.

A finer point is that Rascal restricts the filtering of priority such that it is guaranteed that no parse errors occur at the cause of a priority. The following table defines when and where Rascal forbids a direct nesting between two productions `parent > child`, depending on at which left-most or right-most positions the parent and the child are recursive. 

| If `Parent > Child` | Parent None: `E = "[" E "]"` | Parent Left-most: `E = E "*"` |Parent  Right-most: `E = "*" E` | Parent Both: `E = E "*" E`   |
| --- | --- | --- | --- | --- |
| __Child None:__ `E = "{" E "}"`  | No filter        | No filter                    | No filter                     | No filter               |
| __Child Left-most:__ `E = E "+"` | No filter        | No filter                    | Filter under right            | Filter under right      |
| __Child Right-most:__ `E = "+" E`| No filter        | Filter under left            | No filter                     | Filter under left       |
| __Child Both:__ `E = E "+" E`    | No filter        | Filter under left            | Filter under right            | Filter under left and right  |


.Examples
The following snippet uses all ((Disambiguation-Priority)) features:
```rascal
syntax Exp 
  = A: Id
  | B: Number 
  > C: Exp "[" Exp "]" 
  | D: Exp "!"
  > E: Exp "*" Exp 
  > F: Exp "+" Exp;
  | bracket G: "(" Exp ")"
  ;
```
A short explanation:

*  C and D share a group of equal priority. They are incomparable in the partial ordering. That's fine because `1![2]` is not ambiguous.
*  Similarly A and B share a group; yet they are not recursive and so do not play any role in the priority ordering.
*  C and D both have higher priority then E and F, which means that E and F may not be directly nested under C or D.
*  However: E and F will be allowed under the second argument of C because it is not an outermost position. That's fine because `1 [2 + 3]` is not ambiguous. 


Here a number of strings for this language, with brackets to show how they will be parsed: 

*  "1 + 2 * 3" will be parsed as "1 + (2 * 3)" because E > F.
*  "1 + 2 [ 3 ]" will be parsed as "1 + (2\[3\])" because C > F.
*  "1 * 3!" will be parsed as "1 + (3!)" because D > E.
*  "1 + [2 * 3]" will be parsed as "1 + ([2 * 3])" because priority is only defined for outermost positions.

.Benefits

*  Short notation for common expression grammars
*  Removes ambiguity but can not introduce parse errors
*  Allows the use of less non-terminals for the same expression grammar (typically only one), which makes parse trees simpler as well as the mapping to an abstract syntax tree more direct.

.Pitfalls

*  Please do not assume that Rascal's priorities have the same semantics as SDF's priorities.
*  When a priority does not have a filtering effect, such as in `E = E "+" > E "*"` it is usually better to use normal alternative composition: `E = E "+" | E "*"`. There is no difference in the semantics of parsing, but the latter expression is more intentional.
*  You should not hide right or left recursion behind a nullable non-terminal, since the system will not filter the ambiguity then. Example: 
E = left "a"? E "*" E > E "+" E will remain ambiguous. This should be written as: E = left ("a" E "*" E | E "*" E ) > E "+" E; (unfolding the optional such that E becomes explicitly left-most).

