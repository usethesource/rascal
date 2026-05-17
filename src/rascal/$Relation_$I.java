package rascal;
import io.usethesource.vallang.*;
import org.rascalmpl.runtime.function.*;

@SuppressWarnings("unused")
public interface $Relation_$I  {
    IValue carrier(IValue $0);
    IValue carrierR(IValue $0, IValue $1);
    IValue carrierX(IValue $0, IValue $1);
    IValue complement(IValue $0);
    IValue domain(IValue $0);
    IValue domainR(IValue $0, IValue $1);
    IValue domainX(IValue $0, IValue $1);
    IValue groupDomainByRange(IValue $0);
    IValue groupRangeByDomain(IValue $0);
    IValue ident(IValue $0);
    IValue index(IValue $0);
    IValue invert(IValue $0);
    IValue range(IValue $0);
    IValue rangeR(IValue $0, IValue $1);
    IValue rangeX(IValue $0, IValue $1);
}