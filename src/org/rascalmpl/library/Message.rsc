@license{
  Copyright (c) 2009-2013 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Mark Hills - Mark.Hills@cwi.nl (CWI)}
@contributor{Arnold Lankamp - Arnold.Lankamp@cwi.nl}
@doc{
Name: Message

Synopsis: A `Message` datatype that represents messages in the IDE.

Syntax:

Types:
<listing>
data Message = error(str msg, loc at)
             | warning(str msg, loc at)
             | info(str msg, loc at);
</listing>



Function:

Details:

Description:
Messages can be used to communicate information about source texts.
They can be interpreted by IDEs to display type errors and warnings, etc.
`Message`s are, for instance, used as [AlgebraicDataType] annotations.

The `Message` library provides the following:
<toc Rascal/Libraries/Prelude/Message 1>


Examples:

Benefits:

Pitfalls:

Questions:


}
module Message

data Tree;
@doc{
Synopsis: An alias that represents a quick fix.
Description:
It is a function which takes the Parse Tree and problem location as input and returns the potential rewritten tree in text back to the editor. 
When the quick fix is applied the outcome of the function will replace the content of the file 
}
alias QuickFix = str (&T<:Tree input, loc origin);

@doc{
Description:
Messages can be used to communicate information about source texts.
They can be interpreted by IDE's to display type errors and warnings, etc.
If wanted, they can be provided with a list of quick fixes. These will show up in the IDE when hovering over the message. 
}
data Message(lrel[str label, QuickFix fix] quickFixes = []) = 
       error(str msg, loc at)
     | warning(str msg, loc at)
     | info(str msg, loc at);
/*
@doc{
Synopsis: Create an error message.
}
public Message error(loc source, str msg) {
  return error(msg,source);
}

@doc{
Synopsis: Create a warning message.
}
public Message warning(loc source, str msg) {
  return warning(msg,source);
}

@doc{
Synopsis: Create an info message.
},
public Message info(loc source, str msg) {
  return info(msg,source);
}
*/
