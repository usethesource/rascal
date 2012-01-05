@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
module util::SourceHierarchy

@doc{Labels are used to refer to source code entities in the UI of the IDE}
data Label = string(str description)
           | icon(loc image)
           | composite(set[Label] labels);

@doc{
Source hierarchies are tree shaped abstract models of the source code of programs.
This model offers a generic representation format, for use in communication with IDE
features such as "outline".
}
data SourceHierarchy = group(Label label, loc ref, list[SourceHierarchy] members)
                     | group(Label label, loc ref, set[SourceHierarchy] elements)
                     | item(Label label, loc ref);
