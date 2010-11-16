module experiments::Concept::Examples
import experiments::Concept::Fca;
import experiments::Concept::Types;
import vis::Render; 
import vis::Figure; 

str base = "tealady";


public property_table vb = readCxt(|file:///ufs/bertl/concept/<base>.cxt|);
 
Figure lat = createLatticeFigure(vb);


public void r() {render(box(lat, [height(600), width(600)]));}