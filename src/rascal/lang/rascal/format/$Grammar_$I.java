package rascal.lang.rascal.format;
import io.usethesource.vallang.*;
import org.rascalmpl.runtime.function.*;

@SuppressWarnings("unused")
public interface $Grammar_$I  {
    IValue AttrsAndCons();
    IValue CC();
    IValue Prio();
    IValue alt2r(IValue $0, IValue $1, java.util.Map<java.lang.String,IValue> $kwpActuals);
    IValue alt2rascal(IValue $0);
    IValue associativity(IValue $0);
    IValue attr2mod(IValue $0);
    IValue cc2rascal(IValue $0);
    IValue cleanIdentifiers(IValue $0);
    void definition2disk(IValue $0, IValue $1);
    IValue definition2rascal(IValue $0);
    IValue grammar2rascal(IValue $0);
    IValue grammar2rascal(IValue $0, IValue $1);
    IValue iterseps2rascal(IValue $0, IValue $1, IValue $2);
    IValue layoutname(IValue $0);
    IValue module2rascal(IValue $0);
    IValue noAttrs();
    IValue params2rascal(IValue $0);
    IValue prod2rascal(IValue $0);
    IValue range2rascal(IValue $0);
    IValue reserved(IValue $0);
    IValue same(IValue $0, IValue $1);
    IValue symbol2rascal(IValue $0);
    IValue topProd2rascal(IValue $0);
}