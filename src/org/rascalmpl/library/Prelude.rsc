@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
@doc{
Name: Prelude
Synopsis: Definition of the Rascal standard prelude.

Description:

The standard prelude is automatically imported when Rascal is started.
It combines the following modules:

* `Boolean`,
* `DateTime`,
* `Exception`,
* `IO`,
* `List`,
* `Map`,
* `Node`,
* `ParseTree`,
* `Rational`, ???
* `Relation`,
* `Set`,
* `String`,
* `ToString`,
* `ValueIO`.
}
module Prelude

extend Boolean;
extend DateTime;
extend Exception;
extend IO;
extend List;
extend Map;
extend Node;
extend ParseTree;
// extend Rational;
extend Relation;
extend Set;
extend String;
extend ToString;
extend ValueIO;




