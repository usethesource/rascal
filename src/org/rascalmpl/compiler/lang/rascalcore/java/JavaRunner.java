package org.rascalmpl.compiler.lang.rascalcore.java;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.rascalmpl.exceptions.RuntimeExceptionFactory;
import org.rascalmpl.uri.classloaders.SourceLocationClassLoader;

import io.usethesource.vallang.IList;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValueFactory;

public class JavaRunner {
    private final IValueFactory vf;

    public JavaRunner(IValueFactory vf) {
        this.vf = vf;
    }
    
    public void runJavaMain(IString qname, IList args, IList classpath) {
        var loader = new SourceLocationClassLoader(classpath, getClass().getClassLoader());

        try {
            Class<?> clz = loader.loadClass(qname.getValue());
            Method main = clz.getMethod("main", String[].class);
            String[] mainArgs = args.stream().map(a -> ((IString) a).getValue()).toArray(String[]::new);
            Object[] invokeArgs = new Object[] { mainArgs};
            main.invoke(null, invokeArgs);
        }
        catch (ClassNotFoundException e) {
            throw RuntimeExceptionFactory.illegalArgument(vf.string("Class not found: " + e.getMessage()));
        }
        catch (NoSuchMethodException e) {
            throw RuntimeExceptionFactory.illegalArgument(vf.string("Method not found: " + e.getMessage()));
        }
        catch (SecurityException e) {
            throw RuntimeExceptionFactory.illegalArgument(vf.string("Security problem: " + e.getMessage()));
        }
        catch (IllegalAccessException e) {
            throw RuntimeExceptionFactory.illegalArgument(vf.string("Access not allowed: " + e.getMessage()));
        }
        catch (IllegalArgumentException e) {
            throw RuntimeExceptionFactory.illegalArgument(vf.string(e.getMessage()));
        }
        catch (InvocationTargetException e) {
            throw RuntimeExceptionFactory.javaException(e, null, null);
        }
    }
}
