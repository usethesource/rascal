module experiments::Concept::Examples
import experiments::Concept::Types;

public property_table vb = readCxt(|file:///ufs/bertl/concept/tealady.cxt|);
 
public rel[concept_t, concept_t] lat = createLattice(vb);

public int q = writeDot(|file:///ufs/bertl/aap.dot|, lat);