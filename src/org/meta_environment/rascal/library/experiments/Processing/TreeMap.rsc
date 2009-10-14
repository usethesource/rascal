module experiments::Processing::TreeMap

alias TreeMap = node;

@doc{create a treemap}
@reflect{Needs calling context when calling argument function}
@javaClass{org.meta_environment.rascal.std.Processing.TreeMap}
public TreeMap java treemap(map[str,int] m, int x, int y, int width, int height, 
			                     void (int x, int y, int w, int h, str word) drawItem);
			                     
@doc{create a treemap}
@reflect{Needs calling context when calling argument function}
@javaClass{org.meta_environment.rascal.std.Processing.TreeMap}
public TreeMap java treemap(map[str,int] m, real x, real y, real width, real height, 
			                     void (real x, real y, real w, real h, str word) drawItem);
			                     
@doc{draw a treemap}
@reflect{Needs calling context for error exceptions}
@javaClass{org.meta_environment.rascal.std.Processing.TreeMap}	                     
public void java draw(TreeMap tm);