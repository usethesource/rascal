@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Arnold Lankamp - Arnold.Lankamp@cwi.nl}
module demo::Rules::BoolSyntax

layout Whitespace = [\ \t\n]*;

start syntax Bool = "btrue"
                    | "bfalse"
                    | left Bool "&" Bool
                    | right Bool "|" Bool
                    | "(" Bool ")"
                    ;
