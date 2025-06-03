package rascal.analysis.graphs;
import io.usethesource.vallang.*;
import org.rascalmpl.runtime.function.*;

@SuppressWarnings("unused")
public interface $Graph_$I  {
    IValue bottom(IValue $0);
    IValue connectedComponents(IValue $0);
    IValue order(IValue $0);
    IValue predecessors(IValue $0, IValue $1);
    IValue reach(IValue $0, IValue $1);
    IValue reachR(IValue $0, IValue $1, IValue $2);
    IValue reachX(IValue $0, IValue $1, IValue $2);
    IValue shortestPathPair(IValue $0, IValue $1, IValue $2);
    IValue stronglyConnectedComponents(IValue $0);
    IValue stronglyConnectedComponentsAndTopSort(IValue $0);
    IValue successors(IValue $0, IValue $1);
    IValue top(IValue $0);
    IValue transitiveEdges(IValue $0);
    IValue transitiveReduction(IValue $0);
}