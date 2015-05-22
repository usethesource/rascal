@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jouke Stoel - Jouke.Stoel@cwi.nl}
module Load

import Parse;
import AST;
import ParseTree;

FeatureDiagram load(loc l) = implodeFDL(parseFeatureDiagram(l));

FeatureDiagram implodeFDL(Tree fd) = implode(#FeatureDiagram, fd); 