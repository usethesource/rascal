package rascal.lang.rascal.grammar;
import io.usethesource.vallang.*;
import org.rascalmpl.runtime.function.*;

@SuppressWarnings("unused")
public interface $Lookahead_$I  {
    IValue compileLookaheads(IValue $0);
    IValue computeLookaheads(IValue $0, IValue $1);
    IValue definedSymbols(IValue $0);
    IValue diff(IValue $0, IValue $1);
    IValue first(IValue $0);
    IValue first(IValue $0, IValue $1);
    IValue firstAndFollow(IValue $0);
    IValue follow(IValue $0, IValue $1);
    IValue intersect(IValue $0, IValue $1);
    IValue mergeCC(IValue $0);
    IValue optimizeLookaheads(IValue $0, IValue $1);
    IValue optimizeLookaheadsOld(IValue $0, IValue $1);
    IValue order(IValue $0);
    IValue removeLabel(IValue $0);
    IValue removeLabels(IValue $0);
    IValue terminalSymbols(IValue $0);
    IValue usedSymbols(IValue $0);
}