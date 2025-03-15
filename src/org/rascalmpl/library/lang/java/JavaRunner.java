package org.rascalmpl.library.lang.java;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.JUnit4;
import org.junit.runners.model.InitializationError;
import org.rascalmpl.exceptions.RuntimeExceptionFactory;
import org.rascalmpl.library.Messages;
import org.rascalmpl.uri.classloaders.SourceLocationClassLoader;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IListWriter;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValueFactory;

public class JavaRunner {
    private final IValueFactory vf;

    public JavaRunner(IValueFactory vf) {
        this.vf = vf;
    }

    /**
     * This is to translate JUnit test results to Rascal messages
     */
    private final class TestRunListener extends RunListener {
        private final Class<?> clz;
        private final IListWriter messages;

        private TestRunListener(Class<?> clz, IListWriter messages) {
            this.clz = clz;
            this.messages = messages;
        }

        @Override
        public void testFinished(Description description) throws Exception {
            messages.append(Messages.info(description.getDisplayName() + " succeeded", locForClass(clz)));
        }

        @Override
        public void testFailure(Failure failure) throws Exception {
            messages.append(Messages.error(failure.getDescription().getDisplayName() + " failed", locForClass(clz)));
        }

        @Override
        public void testIgnored(Description description) throws Exception {
            messages.append(Messages.info(description.getDisplayName() + " ignored", locForClass(clz)));
        }
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

    private ISourceLocation locForClass(Class<?> clz) {
        try {
            return vf.sourceLocation(clz.getProtectionDomain().getCodeSource().getLocation().toURI());
        }
        catch (URISyntaxException e) {
           throw RuntimeExceptionFactory.illegalArgument(vf.string(e.getMessage()));
        }
    }

    public IList runJUnitTestClass(IString qname, IList classpath, IConstructor version) {
        if (!version.getName().equals("junit4")) {
            throw RuntimeExceptionFactory.illegalArgument(version);
        }
        
        var loader = new SourceLocationClassLoader(classpath, getClass().getClassLoader());

        try {
            Class<?> clz = loader.loadClass(qname.getValue());
            IListWriter messages = vf.listWriter();
            
            RunListener listener = new TestRunListener(clz, messages);
            RunNotifier notifier = new RunNotifier();
            notifier.addFirstListener(listener);
            
            new JUnit4(clz).run(notifier);

            return messages.done();
        }
        catch (ClassNotFoundException e) {
            throw RuntimeExceptionFactory.illegalArgument(vf.string("Class not found: " + e.getMessage()));
        }
        catch (SecurityException e) {
            throw RuntimeExceptionFactory.illegalArgument(vf.string("Security problem: " + e.getMessage()));
        }
        catch (IllegalArgumentException e) {
            throw RuntimeExceptionFactory.illegalArgument(vf.string(e.getMessage()));
        }
        catch (InitializationError e) {
            throw RuntimeExceptionFactory.illegalArgument(vf.string("JUnit initialization error: " + e.getMessage()));
        }
    }
}
