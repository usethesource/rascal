package rascal;
import io.usethesource.vallang.*;
import org.rascalmpl.runtime.function.*;

@SuppressWarnings("unused")
public interface $ValueIO_$I  {
    IValue getFileLength(IValue $0);
    IValue readBinaryValueFile(IValue $0, IValue $1);
    IValue readBinaryValueFile(IValue $0);
    IValue readTextValueFile(IValue $0, IValue $1);
    IValue readTextValueFile(IValue $0);
    IValue readTextValueFileWithEmbeddedTypes(IValue $0, IValue $1);
    IValue readTextValueString(IValue $0, IValue $1);
    IValue readTextValueString(IValue $0);
    IValue readValueFile(IValue $0);
    void writeBinaryValueFile(IValue $0, IValue $1, java.util.Map<java.lang.String,IValue> $kwpActuals);
    void writeTextValueFile(IValue $0, IValue $1);
}