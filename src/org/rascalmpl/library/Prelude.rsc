@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
@doc{
---
title: Prelude
---
.Synopsis
Definition of the Rascal standard prelude.

.Description

The standard prelude is automatically imported when Rascal is started.
It combines the following modules:

*  `Boolean`,
*  `DateTime`,
*  `Exception`,
*  `IO`,
*  `List`,
*  `Map`,
*  `Node`,
*  `ParseTree`,
*  `Rational`, ???
*  `Relation`,
*  `Set`,
*  `String`,
*  `ToString`,
*  `ValueIO`.
}
module Prelude

extend Boolean;
extend DateTime;
extend Exception;
extend Grammar;
extend IO;
extend List;
extend ListRelation;
extend Map;
extend Node;
extend ParseTree;
extend Relation;
extend Set;
extend String; 
extend Traversal;
extend Type;
extend ValueIO;  
