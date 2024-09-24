package org.rascalmpl.library;

import java.io.PrintWriter;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.rascalmpl.values.IRascalValueFactory;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.type.TypeFactory;
import io.usethesource.vallang.type.TypeStore;


/**
 * Java API for the messages in the standard library module `Message`
 * 
 * This is the standard format for all error messages in Rascal projects and beyond.
 * Since some low-level core code also produces messages that should end up in UI,
 * we write here a bridge between the Java and Rascal representation. 
 * 
 * TODO Later when the standard library is bootstrapped, this code might be replaced
 * by the generated code from the compiler for the `Message` module.
 */
public class Messages {
    private static final TypeFactory tf = TypeFactory.getInstance();
    private static final IValueFactory vf = IRascalValueFactory.getInstance();
    private static final TypeStore ts = new TypeStore();

    // These declarations mirror the data definition in the `Message` root module of the standard library.
    private static final io.usethesource.vallang.type.Type Message = tf.abstractDataType(ts, "Message");
    private static final io.usethesource.vallang.type.Type Message_info = tf.constructor(ts, Message, "info", tf.stringType(), "msg", tf.sourceLocationType(), "at");
    private static final io.usethesource.vallang.type.Type Message_warning = tf.constructor(ts, Message, "warning", tf.stringType(), "msg", tf.sourceLocationType(), "at");
    private static final io.usethesource.vallang.type.Type Message_error = tf.constructor(ts, Message, "error", tf.stringType(), "msg", tf.sourceLocationType(), "at");

    public static IValue info(String message, ISourceLocation loc) {
        return vf.constructor(Message_info, vf.string(message), loc);
    }

    public static IValue warning(String message, ISourceLocation loc) {
        return vf.constructor(Message_warning, vf.string(message), loc);
    }

    public static IValue error(String message, ISourceLocation loc) {
        return vf.constructor(Message_error, vf.string(message), loc);
    }

    public static void write(IList messages, PrintWriter out) {
        int maxLine = 0;
        int maxColumn = 0;

        for (IValue error : messages) {
            ISourceLocation loc = (ISourceLocation) ((IConstructor) error).get("at");
            if (loc.hasLineColumn()) {
                maxLine = Math.max(loc.getBeginLine(), maxLine);
                maxColumn = Math.max(loc.getBeginColumn(), maxColumn);
            } 
        }

        int lineWidth = (int) Math.log10(maxLine + 1) + 1;
		int colWidth = (int) Math.log10(maxColumn + 1) + 1;

        Stream<IConstructor> sortedStream = messages.stream()
            .map(IConstructor.class::cast)
            .sorted((m1, m2) -> {
                ISourceLocation l1 = (ISourceLocation) m1.get("at");
                ISourceLocation l2 = (ISourceLocation) m2.get("at");
                
                if (!l1.getScheme().equals(l2.getScheme())) {
                    return l1.getScheme().compareTo(l2.getScheme());
                }

                if (!l1.getAuthority().equals(l2.getAuthority())) {
                    return l1.getAuthority().compareTo(l2.getAuthority());
                }
                
                if (!l1.getPath().equals(l2.getPath())) {
                    return l1.getPath().compareTo(l2.getPath());
                }

                if (l1.hasLineColumn() && l2.hasLineColumn()) {
                    if (l1.getBeginLine() == l2.getBeginLine()) {
                        return Integer.compare(l1.getBeginColumn(), l2.getBeginColumn());
                    }
                    else {
                        return Integer.compare(l1.getBeginLine(), l2.getBeginLine());
                    }
                }
                else if (l1.hasOffsetLength() && l2.hasOffsetLength()) {
                    return Integer.compare(l1.getOffset(), l2.getOffset());
                }
                else if (l1.hasOffsetLength()) {
                    return -1;
                }
                else if (l2.hasOffsetLength()) {
                    return 1;
                }

                return 0;
            });

        for (IConstructor msg : sortedStream.collect(Collectors.toList())) {
            String type = msg.getName();
            boolean isError = type.equals("error");
            boolean isWarning = type.equals("warning");

            ISourceLocation loc = (ISourceLocation) msg.get("at");
            int col = 0;
            int line = 0;
            if (loc.hasLineColumn()) {
                col = loc.getBeginColumn();
                line = loc.getBeginLine();
            }

            String output 
                = loc.getPath()
                + ":"
                + String.format("%0" + lineWidth + "d", line)
                + ":"
                + String.format("%0" + colWidth + "d", col)
                + ": "
                + ((IString) msg.get("msg")).getValue()
            ;

            if (isError) {
                out.println("[ERROR]   " + output);
            }
            else if (isWarning) {
                out.println("[WARNING] " + output);
            }
            else {
                out.println("[INFO]    " + output);
            }
        }

        out.flush();
		return;
    }
}
