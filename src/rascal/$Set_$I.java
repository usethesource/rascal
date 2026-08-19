package rascal;
import io.usethesource.vallang.*;
import org.rascalmpl.runtime.function.*;

@SuppressWarnings("unused")
public interface $Set_$I  {
    IValue classify(IValue $0, IValue $1);
    IValue getFirstFrom(IValue $0);
    IValue getOneFrom(IValue $0);
    IValue getSingleFrom(IValue $0);
    IValue group(IValue $0, IValue $1);
    IValue index(IValue $0);
    IValue isEmpty(IValue $0);
    IValue itoString(IValue $0);
    IValue jaccard(IValue $0, IValue $1);
    IValue mapper(IValue $0, IValue $1);
    IValue max(IValue $0);
    IValue min(IValue $0);
    IValue power(IValue $0);
    IValue power1(IValue $0);
    IValue reducer(IValue $0, IValue $1, IValue $2);
    IValue reducer(IValue $0);
    IValue size(IValue $0);
    IValue sort(IValue $0);
    IValue sort(IValue $0, IValue $1);
    IValue sum(IValue $0);
    IValue takeFirstFrom(IValue $0);
    IValue takeOneFrom(IValue $0);
    IValue toList(IValue $0);
    IValue toMap(IValue $0);
    IValue toMapUnique(IValue $0);
    IValue toString(IValue $0);
    IValue top(IValue $0, IValue $1);
    IValue top(IValue $0, IValue $1, IValue $2);
    IValue union(IValue $0);
}