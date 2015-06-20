@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jouke Stoel - Jouke.Stoel@cwi.nl}
module Parse

import Syntax;
import ParseTree;

public start[FeatureDiagram] parseFeatureDiagram(str src, loc file) = 
  parse(#start[FeatureDiagram], src, file);
  
public start[FeatureDiagram] parseFeatureDiagram(loc file) = 
  parse(#start[FeatureDiagram], file);