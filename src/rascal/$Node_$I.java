package rascal;
import io.usethesource.vallang.*;
import org.rascalmpl.runtime.function.*;

@SuppressWarnings("unused")
public interface $Node_$I  {
    IValue arbNode();
    IValue arity(IValue $0);
    IValue delAnnotation(IValue $0, IValue $1);
    IValue delAnnotations(IValue $0);
    IValue delAnnotationsRec(IValue $0);
    IValue getAnnotations(IValue $0);
    IValue getChildren(IValue $0);
    IValue getKeywordParameters(IValue $0);
    IValue getName(IValue $0);
    IValue itoString(IValue $0);
    IValue makeNode(IValue $0, IValue $1, java.util.Map<java.lang.String,IValue> $kwpActuals);
    IValue setAnnotations(IValue $0, IValue $1);
    IValue setKeywordParameters(IValue $0, IValue $1);
    IValue toString(IValue $0);
    IValue unset(IValue $0);
    IValue unset(IValue $0, IValue $1);
    IValue unsetRec(IValue $0, IValue $1);
    IValue unsetRec(IValue $0);
}