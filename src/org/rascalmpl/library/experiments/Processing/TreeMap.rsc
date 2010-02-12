module experiments::Processing::TreeMap

import experiments::Processing::Core;

data Treemap = treemap(int id);

@doc{create a treemap}
@reflect{Needs calling context when calling argument function}
@javaClass{org.rascalmpl.library.experiments.Processing.TreeMap}
public Treemap java treemap(str title, map[str,int] m,  list[CallBack] items, list[CallBack] treemap);
			                    
			                     
@doc{draw a treemap}
@reflect{Needs calling context for error exceptions}
@javaClass{org.rascalmpl.library.experiments.Processing.TreeMap}	                     
public void java draw(Treemap tm);