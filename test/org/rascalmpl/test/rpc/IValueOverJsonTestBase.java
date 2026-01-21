package org.rascalmpl.test.rpc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.eclipse.lsp4j.jsonrpc.Launcher;
import org.eclipse.lsp4j.jsonrpc.messages.ResponseError;
import org.eclipse.lsp4j.jsonrpc.messages.ResponseErrorCode;
import org.junit.AfterClass;
import org.junit.Test;
import org.rascalmpl.library.Prelude;
import org.rascalmpl.library.util.Math;
import org.rascalmpl.values.ValueFactoryFactory;

import com.google.gson.GsonBuilder;

import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.IRational;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;

public abstract class IValueOverJsonTestBase {
    protected static final IValueFactory vf = ValueFactoryFactory.getValueFactory();
    protected static final Prelude prelude = new Prelude(vf, null, null, null, null);
    protected static final Math math = new Math(vf);

    protected static JsonRpcTestInterface testServer;
    protected static final PipedInputStream is0, is1;
    protected static final PipedOutputStream os0, os1;
    
    static {
        try {
            is0 = new PipedInputStream();
            os0 = new PipedOutputStream();
            is1 = new PipedInputStream(os0);
            os1 = new PipedOutputStream(is0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected static void startTestServerAndClient(Consumer<GsonBuilder> gsonConfig) {
        new TestThread(is0, os0, gsonConfig).start();
        new TestClient(is1, os1, gsonConfig);
    }

    @AfterClass
    public static void teardown() throws IOException {
        if (is0 != null) {
            is0.close();
        }
        if (is1 != null) {
            is1.close();
        }
        if (os0 != null) {
            os0.close();
        }
        if (os1 != null) {
            os1.close();
        }
    }

    static class TestClient {
        public TestClient(InputStream is, OutputStream os, Consumer<GsonBuilder> gsonConfig) {
            Launcher<JsonRpcTestInterface> clientLauncher = new Launcher.Builder<JsonRpcTestInterface>()
                .setRemoteInterface(JsonRpcTestInterface.class)
                .setLocalService(this)
                .setInput(is)
                .setOutput(os)
                .configureGson(gsonConfig)
                .setExecutorService(Executors.newCachedThreadPool())
                .create();

            clientLauncher.startListening();
            testServer = clientLauncher.getRemoteProxy();
        }
    }

    static class TestThread extends Thread {
        private final InputStream is;
        private final OutputStream os;
        private final Consumer<GsonBuilder> gsonConfig;
        
        public TestThread(InputStream is, OutputStream os, Consumer<GsonBuilder> gsonConfig) {
            this.is = is;
            this.os = os;
            this.gsonConfig = gsonConfig;
            this.setDaemon(true);
        }

        @Override
        public void run() {
            Launcher<JsonRpcTestInterface> serverLauncher = new Launcher.Builder<JsonRpcTestInterface>()
                .setLocalService(new JsonRpcTestInterface() {}) // `setLocalService` explicitly requires an interface, not a class
                .setRemoteInterface(JsonRpcTestInterface.class)
                .setInput(is)
                .setOutput(os)
                .configureGson(gsonConfig)
                .setExceptionHandler(e -> {
                    System.err.println(e);
                    return new ResponseError(ResponseErrorCode.InternalError, e.getMessage(), e);
                })
                .create();

            serverLauncher.startListening();
        }
    }

    protected static <T extends IValue> void expectSuccessful(String type, Supplier<T> supplier, Function<T, CompletableFuture<T>> function) {
        var value = supplier.get();
        try {
            assertEquals(value, function.apply(value).get());
        } catch (InterruptedException | ExecutionException e) {
            fail("Error occurred while testing " + type + " over jsonrpc: " + e.getMessage());
        }
    }

    protected static <T extends IValue> void expectUnsuccessful(String type, Supplier<T> supplier, Function<T, CompletableFuture<T>> function) {
        var value = supplier.get();
        try {
            function.apply(value).get();
            fail("Error occurred: " + type + " should not have round-tripped");
        } catch (InterruptedException | ExecutionException e) {
            //This is expected
        }
    }

    protected static IRational arbRational() {
        IInteger numerator = (IInteger) math.arbInt();
        IInteger denominator = (IInteger) math.arbInt();
        while (denominator.equals(vf.integer(0))) {
            denominator = (IInteger) math.arbInt();
        }
        return vf.rational(numerator, denominator);
    }    
    
    @Test
    public abstract void testSendBool();
    
    @Test
    public abstract void testSendConstructor();

    @Test
    public abstract void testSendDateTime();

    @Test
    public abstract void testSendInteger();

    @Test
    public abstract void testSendNode();

    @Test
    public abstract void testSendRational();

    @Test
    public abstract void testSendReal();

    @Test
    public abstract void testSendLocation();

    @Test
    public abstract void testSendString();
    
    @Test
    public abstract void testSendIntAsNumber();

    @Test
    public abstract void testSendRealAsNumber();
    
    @Test
    public abstract void testSendRealAsValue();
    
    @Test
    public abstract void testSendList();

    @Test
    public abstract void testSendMap();

    @Test
    public abstract void testSendSet();

    @Test
    public abstract void testSendTuple();
}
