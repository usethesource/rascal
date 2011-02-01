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

