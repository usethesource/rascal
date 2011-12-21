@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Bert Lisser - Bert.Lisser@cwi.nl (CWI)}
module experiments::Concept::Examples
import experiments::Concept::Fca;
import experiments::Concept::Types;
import vis::Render; 
import vis::Figure; 

str base = "tealady";
//aap

// public property_table vb = readCxt(|testdata:///fca/<base>.cxt|);

public property_table vb = readCxt(|project://aap/src/<base>.cxt|);
 
Figure lat = createLatticeFigure(vb, "spring");


public void r() {render(use(lat, [height(600), width(600)]));}

// public void r() {render(lat, [height(600), width(600)]);}

