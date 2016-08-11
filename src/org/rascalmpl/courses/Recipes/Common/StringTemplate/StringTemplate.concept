# String Template

.Synopsis
Using string templates to generate code.

.Syntax

.Types

.Function

.Details

.Description
Many websites and code generators use template-based code generation. They start from a text template that contains embedded variables and code. The template is "executed" by replacing the embedded variables and code by their string value. Languages like PHP and Ruby are popular for this feature. Let's see how we can do this in Rascal. 

Rascal provides string templates that rival what is provided in
http://www.ruby-doc.org/stdlib/libdoc/erb/rdoc/ERB.html[Ruby], http://www.php.net/[PHP] or http://www.stringtemplate.org/[ANTLR].
They are fully described in link:/Rascal#Values-String[string values].

.Examples
The problem we want to solve is as follows: 
given a number of fields (with a name and a type)
how can we generate a Java class with getters and setters for those fields?

Here is a solution:
[source,rascal]
----
include::{LibDir}demo/common/StringTemplate.rsc[tags=module]
----

                
<1> An auxiliary function `capitalize` is defined to capitalize the first character of a string.

<2> Here is the heavy lifting done: `genClass` is defined that takes as arguments:

*  the `name` of the class, and
*  a map `fields` that associates field names with their type (both string values).


Function `genClass` returns a string that contains several _string interpolations_ delimited by `<` and `>`.
Let's discuss some of them:

*  In each line, only the text following `'` is contributed to the output. The text before (and including) `'` can be used to properly indent
   the string.
*  The output of each interpolated call, like to `genMethod` is auto-indented.
*  `public class <name>`: insert the desired class name in the result.
*  `<for x<-fields){>` ... `<}>`: loops over the fields and contributes the text produced by its body to the result.
*  `private <fields[x]> <x>;`: finds for the current field `x` its type and produces an appropriate private field declaration.
*  `public void set<capitalize(x)>(<fields[x]> <x>)`: method header for the setter for field `x`.

Let's see how this works out on actual data:
[source,rascal-shell]
----
import demo::common::StringTemplate;
import IO;
fields = (
     "name" : "String",
     "age" : "Integer",
     "address" : "String"
  );
println(genClass("Person", fields));
----

.Benefits

*  String templates are ideal to generate arbitrary output. In particular, no grammar is needed to describe this output.
*  Auto-indent helps to be able to compose templates from reusable parts.

.Pitfalls

*  Since no grammar is used to control output, errors in generated code can only be detected by a downstream processor such as a compiler for the generated code.

*  In more complex cases, it can be better to introduce an abstract datatype to represent the desired code and to use string templates to
produce the actual textual representation of that code.

