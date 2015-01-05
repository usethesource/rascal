@license{
  Copyright (c) 2009-2015 CWI
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

@doc{
Description:
Messages can be used to communicate information about source texts.
They can be interpreted by IDE's to display type errors and warnings, etc.
}
data Message = 
       error(str msg, loc at)
     | warning(str msg, loc at)
     | info(str msg, loc at);

