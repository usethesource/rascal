@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Mark Hills - Mark.Hills@cwi.nl (CWI)}
@contributor{Arnold Lankamp - Arnold.Lankamp@cwi.nl}
module Message

@doc{
Messages can be used to communicate information about source texts.
They can be interpreted by IDE's to display type errors and warnings, etc.
}
data Message = error(str msg, loc at)
             | warning(str msg, loc at)
             | info(str msg, loc at);

public Message error(loc source, str msg) {
  return error(msg,source);
}

public Message warning(loc source, str msg) {
  return warning(msg,source);
}

public Message info(loc source, str msg) {
  return info(msg,source);
}
