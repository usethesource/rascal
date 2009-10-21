module experiments::Processing::Graph

alias Graph = node;

@doc{create a graph}
@reflect{Needs calling context when calling argument function}
@javaClass{org.meta_environment.rascal.library.experiments.Processing.Graph}
public Graph java graph(rel[str,str] m,
			            void (int x, int y, str label, int count) drawNode,
			            void (int fx, int fy, int tx, int ty) drawEdge);
			           
@doc{draw a graph}
@reflect{Needs calling context for error exceptions}
@javaClass{org.meta_environment.rascal.library.experiments.Processing.Graph}	                     
public void java draw(Graph g);
			                     
			                     