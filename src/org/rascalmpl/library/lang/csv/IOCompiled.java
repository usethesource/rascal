package org.rascalmpl.library.lang.csv;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContext;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Types;

import io.usethesource.vallang.IBool;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;

public class IOCompiled extends IO {

    private final Types typeConverter;

    public IOCompiled(IValueFactory values){
        super(values);
        this.typeConverter = new Types(values);
    }

    /*
     * Read a CSV file
     */
    public IValue readCSV(ISourceLocation loc, IBool header, IString separator, IString encoding, IBool printInferredType, RascalExecutionContext rex){
        return read(null, loc, header, separator, encoding, printInferredType, rex.getStdOut(), () -> null, () -> null);
    }

    public IValue readCSV(IValue result, ISourceLocation loc, IBool header, IString separator, IString encoding, RascalExecutionContext rex){
        return read(result, loc, header, separator, encoding, values.bool(false), rex.getStdOut(), () -> null, () -> null);
    }

    /*
     * Calculate the type of a CSV file, returned as the string 
     */
    public IValue getCSVType(ISourceLocation loc, IBool header, IString separator, IString encoding, IBool printInferredType, RascalExecutionContext rex){
        return computeType(loc, header, separator, encoding, rex.getStdOut(), () -> null, () -> null, (IValue v) -> typeConverter.typeToValue(v.getType(), rex));
    }

    /*
     * Write a CSV file.
     */
    public void writeCSV(IValue rel, ISourceLocation loc, IBool header, IString separator, IString encoding, RascalExecutionContext rex){
        // TODO: pass in the type of the generic type instead of the dynamic type (it used to be: ctx.getCurrentEnvt().getTypeBindings().get(types.parameterType("T"));)
        writeCSV(rel, loc, header, separator, encoding, rel.getType(), () -> null, () -> null);
    }
}
