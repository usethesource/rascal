module experiments::Concept::GetFigure
import experiments::Concept::Fca;
import experiments::Concept::Types;
import IO;
import vis::Figure; 

public Figure getFigure(loc f, str layout_alg) {
   property_table vb = readCxt(f);
   println("getFigure:<layout_alg>");
   return use(createLatticeFigure(vb, layout_alg), [height(600), width(600)]);
   }
