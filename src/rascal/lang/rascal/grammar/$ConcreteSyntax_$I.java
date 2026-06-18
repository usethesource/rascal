package rascal.lang.rascal.grammar;
import io.usethesource.vallang.*;
import org.rascalmpl.runtime.function.*;

@SuppressWarnings("unused")
public interface $ConcreteSyntax_$I  {
    IValue addHoles(IValue $0);
    IValue createHole(IValue $0, IValue $1);
    IValue denormalize(IValue $0);
    IValue getTargetSymbol(IValue $0);
    IValue holes(IValue $0);
    IValue quotable(IValue $0);
    IValue removeConditionals(IValue $0);
}