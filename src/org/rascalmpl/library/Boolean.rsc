@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
@doc{
Name: Boolean

Synopsis: Boolean values.

Syntax:
`true`, `false`

Library functions: `import Boolean;`

Types:
`bool`

Function:

Details:

Description:
The Booleans are represented by the type `bool` which has two values: `true` and `false`.

The Boolean operators (to be more precise: operators with a value of type Boolean as result) have _short-circuit_ semantics. 
This means that the operands are evaluated until the outcome of the operator is known.

Most operators are self-explanatory except the match (:=) and no match (!:=) operators that are also the main reason to treat Boolean operator expressions separately. Although we describe patterns in full detail in [Patterns], a preview is useful here. A pattern can

* match (or not match) any arbitrary value (that we will call the _subject value_);

* during the match variables may be bound to subvalues of the subject value.


The _match_ operator
<listing>
$Pat$ := $Exp$
</listing>
is evaluated as follows:

* $Exp$ is evaluated, the result is a subject value;

* the subject value is matched against the pattern $Pat$;

* if the match succeeds, any variables in the pattern are bound to subvalues of the subject value and the match expression yields `true`;

* if the match fails, no variables are bound and the match expression yields `false`.


This looks and _is_ nice and dandy, so why all this fuss about Boolean operators?
The catch is that--as we will see in [Patterns]--a match need not be unique. This means that there may be more than one way of matching the subject value resulting in different variable bindings. 

This behaviour is applicable in the context of all Rascal constructs where a pattern match determines the flow of control of the program, in particular:

* Boolean expressions: when a pattern match fails that is part of a Boolean expression, further solutions are tried in order to try to make the Boolean expression true.

* Tests in [For], [While], [Do] statements.

* Tests in [Any] and [All] expressions.

* Tests and [Enumerator]s in comprehensions.

* Pattern matches in cases of a [Visit].

* Pattern matches in cases of a [Switch].


Examples:
Consider the following match of a list
<listing>
[1, list[int] L, 2, list[int] M] := [1,2,3,2,4]
</listing>
By definition `list[int] L` and `list[int] M` match list elements that are part of the enclosing list in which they occur. If they should match a nested list each should be enclosed in list brackets.

There are two solutions for the above match:

* `L` = `[]` and `M` =` [2, 3, 2, 4]`; and

* `L` = `[2,3]` and `M` =` [4]`.


Depending on the context, only the first solution of a match expression is used, respectively all solutions are used.
If a match expression occurs in a larger Boolean expression, a subsequent subexpression may yield false and -- depending on the actual operator -- evaluation backtracks to a previously evaluated match operator to try a next solution. Let's illustrate this by extending the above example:

<listing>
[1, list[int] L, 2, list[int] M] := [1,2,3,2,4] && size(L) > 0
</listing>
where we are looking for a solution in which L has a non-empty list as value. Evaluation proceeds as follows:

* The left argument of the `&&` operator is evaluated: the match expression is evaluated resulting in the bindings `L = []` and `M = [2, 3, 2, 4]`;

* The right argument of the `&&` operator is evaluated: `size(L) > 0` yields `false`;

* Backtrack to the left argument of the `&&` operator to check for more solutions: indeed there are more solutions resulting in the bindings `L = [2,3]` and `M = [4]`;

* Proceed to the right operator of `&&`: this time `size(L) > 0` yields `true`;

* The result of evaluating the complete expression is `true`.

Benefits:

Pitfalls:

Questions:

}
module Boolean

@doc{
Name: arbBool
Synopsis: Return an arbitrary Boolean value.
Syntax:
Types:
Function:
`bool arbBool( )`

Details:
Description:
Examples:
<screen>
import Boolean;
arbBool();
arbBool();
arbBool();
</screen>

Benefits:
<tt>arbInt</tt> is a convenient generator for arbitrary binary choices.

Pitfalls:
Questions:
}

@javaClass{org.rascalmpl.library.Boolean}
public java bool arbBool();

@deprecated{Useless function that will be removed}
@doc{
Name: fromInt
Synopsis: Convert from integer to Boolean.
Syntax:
Types:
Function:
`bool fromInt(int n)`
Details:
Description:
Convert an integer to a Boolean value: all non-zero integers are mapped to `true`, zero is mapped to `false`.

Examples:
<screen>
import Boolean;
fromInt(13);
fromInt(0);
</screen>

Benefits:
Pitfalls:
Questions:
}
public bool fromInt(int i)
{
  return i != 0;
}

@doc{Convert the strings "true" or "false" to a bool}
public bool fromString(str s)
{ 
  if (s == "true") {
    return true;
  }
  if (s == "false") {
    return false;
  }
//  throw s + " is not \"true\" or \"false\";
}

@doc{
Name: toInt
Synopsis: Convert a Boolean value to integer.
Syntax:
Types:
Function:
int toInt(bool b)

Details:
Description:
Maps `true` to `1` and `false` to 0.

Examples:
<screen>
import Boolean;
toInt(true);
toInt(false);
</screen>

Benefits:
Pitfalls:
Questions:
}
public int toInt(bool b)
{
  return b ? 1 : 0;
}

@doc{
Name: toReal
Synopsis: Convert Boolean value to real.
Syntax:
Types:
Function: `real toReal(bool b)`
Details:

Description:
Maps `true` to `1,0` and `false` to `0.0`.

Examples:
<screen>
import Boolean;
toReal(true);
toReal(false);
</screen>

Benefits:
Pitfalls:
Questions:
}
public real toReal(bool b)
{
  return b ? 1.0 : 0.0;
}

@doc{
Name: toString
Synopsis: Convert Boolean value to string.
Syntax:
Types:
Function: str toString(bool b)`
Details:
Description:
Maps `true` to `"true"` and `false` to `"false"`.

Examples:
<screen>
import Boolean;
toString(true);
toString(false);
</screen>

Benefits:
Pitfalls:
Questions:
}
public str toString(bool b)
{
  return b ? "true" : "false";
}

