package org.rascalmpl.library;

import org.rascalmpl.values.IRascalValueFactory;

import io.usethesource.vallang.ISourceLocation;
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
}
