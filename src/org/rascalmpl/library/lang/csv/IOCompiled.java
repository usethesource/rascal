package org.rascalmpl.library.lang.csv;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import org.rascalmpl.interpreter.TypeReifier;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.library.Prelude;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContext;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Types;
import org.rascalmpl.unicode.UnicodeOutputStreamWriter;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IListWriter;
import io.usethesource.vallang.ISet;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.ITuple;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.IWriter;
import io.usethesource.vallang.exceptions.FactParseError;
import io.usethesource.vallang.exceptions.UnexpectedTypeException;
import io.usethesource.vallang.io.StandardTextReader;
import io.usethesource.vallang.type.DefaultTypeVisitor;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;
import io.usethesource.vallang.type.TypeStore;

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
        String sep = separator != null ? separator.getValue() : ",";
        Boolean head = header != null ? header.getValue() : true;
        Writer out = null;

        //Type paramType = ctx.getCurrentEnvt().getTypeBindings().get(types.parameterType("T"));

        Type paramType = rel.getType();
        if(!paramType.isRelation() && !paramType.isListRelation()){
            throw RuntimeExceptionFactory.illegalTypeArgument("A relation type is required instead of " + paramType, null, null);
        }

        try{
            boolean isListRel = rel instanceof IList;
            out = new UnicodeOutputStreamWriter(URIResolverRegistry.getInstance().getOutputStream(loc, false), encoding.getValue(), false);
            out = new BufferedWriter(out); // performance
            ISet irel = null;
            IList lrel = null;
            if (isListRel) {
                lrel = (IList)rel;
            }
            else {
                irel = (ISet) rel;
            }

            int nfields = isListRel ? lrel.asRelation().arity() : irel.asRelation().arity();
            if(head){
                for(int i = 0; i < nfields; i++){
                    if(i > 0)
                        out.write(sep);
                    String label = paramType.getFieldName(i);
                    if(label == null || label.isEmpty())
                        label = "field" + i;
                    out.write(label);
                }
                out.write('\n');
            }

            Pattern escapingNeeded = Pattern.compile("[\\n\\r\"\\x" + Integer.toHexString(separator.charAt(0)) + "]");

            for(IValue v : (isListRel ? lrel : irel)){
                ITuple tup = (ITuple) v;
                boolean firstTime = true;
                for(IValue w : tup){
                    if(firstTime)
                        firstTime = false;
                    else
                        out.write(sep);

                    String s;
                    if(w.getType().isString()){
                        s = ((IString)w).getValue();
                    }
                    else {
                        s = w.toString();
                    }
                    if(escapingNeeded.matcher(s).find()){
                        s = s.replaceAll("\"", "\"\"");
                        out.write('"');
                        out.write(s);
                        out.write('"');
                    } else
                        out.write(s);
                }
                out.write('\n');
            }
        }
        catch(IOException e){
            throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
        }finally{
            if(out != null){
                try{
                    out.flush();
                    out.close();
                }catch(IOException ioex){
                    throw RuntimeExceptionFactory.io(values.string(ioex.getMessage()), null, null);
                }
            }
        }
    }

    /**
     * Normalize a label in the header for use in the relation type.
     *
     * @param label the string found in the header
     * @param pos position in the header
     * @return the label (with non-fieldname characters removed) or "field<pos>" when empty
     */
    private String normalizeLabel(final String label, final int pos) {
        final String normalizedLabel = label.replaceAll("[^a-zA-Z0-9]+", "");

        if (!normalizedLabel.isEmpty()) {
            return normalizedLabel;
        } else {
            return "field" + pos;
        }
    }
}
