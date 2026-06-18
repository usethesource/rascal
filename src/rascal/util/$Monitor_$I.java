package rascal.util;
import io.usethesource.vallang.*;
import org.rascalmpl.runtime.function.*;

@SuppressWarnings("unused")
public interface $Monitor_$I  {
    IValue horseRaceTest();
    IValue job(IValue $0, IValue $1, java.util.Map<java.lang.String,IValue> $kwpActuals);
    IValue jobEnd(IValue $0, java.util.Map<java.lang.String,IValue> $kwpActuals);
    void jobIsCancelled(IValue $0);
    void jobStart(IValue $0, java.util.Map<java.lang.String,IValue> $kwpActuals);
    void jobStep(IValue $0, IValue $1, java.util.Map<java.lang.String,IValue> $kwpActuals);
    void jobTodo(IValue $0, java.util.Map<java.lang.String,IValue> $kwpActuals);
    void jobWarning(IValue $0, IValue $1);
    IValue printLongUnfinishedLine();
    IValue simpleAsyncPrintTest();
    IValue unfinishedInputTest();
    IValue unfinishedLinesAtTheEndTest();
}