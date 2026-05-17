package rascal.lang.rascal.grammar.definition;
import io.usethesource.vallang.*;
import org.rascalmpl.runtime.function.*;

@SuppressWarnings("unused")
public interface $Characters_$I  {
    IValue cc2ranges(IValue $0);
    IValue charToInt(IValue $0);
    IValue complement(IValue $0);
    IValue difference(IValue $0, IValue $1);
    IValue intersect(IValue $0, IValue $1);
    IValue intersection(IValue $0, IValue $1);
    IValue lessThan(IValue $0, IValue $1);
    IValue new_char_class(IValue $0);
    IValue new_range(IValue $0, IValue $1);
    IValue range(IValue $0);
    IValue union(IValue $0, IValue $1);
}