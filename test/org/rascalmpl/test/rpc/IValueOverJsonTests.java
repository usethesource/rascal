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

import org.eclipse.lsp4j.jsonrpc.Launcher;
import org.eclipse.lsp4j.jsonrpc.messages.ResponseError;
import org.eclipse.lsp4j.jsonrpc.messages.ResponseErrorCode;
import org.eclipse.lsp4j.jsonrpc.services.JsonRequest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.rascalmpl.ideservices.GsonUtils;
import org.rascalmpl.library.Prelude;
import org.rascalmpl.library.util.Math;
import org.rascalmpl.values.RascalValueFactory;
import org.rascalmpl.values.ValueFactoryFactory;

import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IDateTime;
import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IMap;
import io.usethesource.vallang.IMapWriter;
import io.usethesource.vallang.INode;
import io.usethesource.vallang.INumber;
import io.usethesource.vallang.IRational;
import io.usethesource.vallang.IReal;
import io.usethesource.vallang.ISet;
import io.usethesource.vallang.ISetWriter;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.ITuple;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;

public abstract class IValueOverJsonTests {
    private static final IValueFactory vf = ValueFactoryFactory.getValueFactory();
    private static final Prelude prelude = new Prelude(vf, null, null, null, null);
    private static final Math math = new Math(vf);

    private static TestInterface server;
    private static PipedInputStream is0 = null, is1 = null;
    private static PipedOutputStream os0 = null, os1 = null;

    @BeforeClass
    public static void setup() throws IOException {
        is0 = new PipedInputStream();
        os0 = new PipedOutputStream();
        is1 = new PipedInputStream(os0);
        os1 = new PipedOutputStream(is0);
        new TestThread(is0, os0).start();
        new TestClient(is1, os1);
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

    static class TestServer implements TestInterface {

        @Override
        public CompletableFuture<IBool> sendBool(IBool value) {
            return CompletableFuture.completedFuture(value);
        }

        @Override
        public CompletableFuture<Void> sendConstructor(IConstructor value) {
            return CompletableFuture.failedFuture(new IllegalStateException("IConstructor should not have been decoded"));
        }

        @Override
        public CompletableFuture<IDateTime> sendDateTime(IDateTime value) {
            return CompletableFuture.completedFuture(value);
        }

        @Override
        public CompletableFuture<IInteger> sendInteger(IInteger value) {
            return CompletableFuture.completedFuture(value);
        }

        @Override
        public CompletableFuture<Void> sendNode(INode value) {
            return CompletableFuture.failedFuture(new IllegalStateException("INode should not have been decoded"));
        }

        @Override
        public CompletableFuture<IRational> sendRational(IRational value) {
            return CompletableFuture.completedFuture(value);
        }

        @Override
        public CompletableFuture<IReal> sendReal(IReal value) {
            return CompletableFuture.completedFuture(value);
        }

        @Override
        public CompletableFuture<ISourceLocation> sendLocation(ISourceLocation value) {
            return CompletableFuture.completedFuture(value);
        }

        @Override
        public CompletableFuture<IString> sendString(IString value) {
            return CompletableFuture.completedFuture(value);
        }

        @Override
        public CompletableFuture<INumber> sendNumber(INumber value) {
            return CompletableFuture.completedFuture(value);
        }

        @Override
        public CompletableFuture<IValue> sendValue(IValue value) {
            return CompletableFuture.completedFuture(value);
        }

        @Override
        public CompletableFuture<Void> sendList(IList list) {
            return CompletableFuture.failedFuture(new IllegalStateException("IList should not have been decoded"));
        }

        @Override
        public CompletableFuture<Void> sendMap(IMap map) {
            return CompletableFuture.failedFuture(new IllegalStateException("IMap should not have been decoded"));
        }

        @Override
        public CompletableFuture<Void> sendSet(ISet set) {
            return CompletableFuture.failedFuture(new IllegalStateException("ISet should not have been decoded"));
        }

        @Override
        public CompletableFuture<Void> sendTuple(ITuple tuple) {
            return CompletableFuture.failedFuture(new IllegalStateException("ITuple should not have been decoded"));
        }
    }

    static class TestClient {
        public TestClient(InputStream is, OutputStream os) {
            Launcher<TestInterface> clientLauncher = new Launcher.Builder<TestInterface>()
                .setRemoteInterface(TestInterface.class)
                .setLocalService(this)
                .setInput(is)
                .setOutput(os)
                .configureGson(GsonUtils::configureGson)
                .setExecutorService(Executors.newCachedThreadPool())
                .create();

                clientLauncher.startListening();
                server = clientLauncher.getRemoteProxy();
        }
    }

    static class TestThread extends Thread {
        private final InputStream is;
        private final OutputStream os;
        
        public TestThread(InputStream is, OutputStream os) {
            this.is = is;
            this.os = os;
            this.setDaemon(true);
        }

        @Override
        public void run() {
            Launcher<TestInterface> serverLauncher = new Launcher.Builder<TestInterface>()
                .setLocalService(new TestServer())
                .setRemoteInterface(TestInterface.class)
                .setInput(is)
                .setOutput(os)
                .configureGson(GsonUtils::configureGson)
                .setExceptionHandler(e -> {
                    System.err.println(e);
                    return new ResponseError(ResponseErrorCode.InternalError, e.getMessage(), e);
                })
                .create();

            serverLauncher.startListening();
        }
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

    private static IRational arbRational() {
        IInteger numerator = (IInteger) math.arbInt();
        IInteger denominator = (IInteger) math.arbInt();
        while (denominator.equals(vf.integer(0))) {
            denominator = (IInteger) math.arbInt();
        }
        return vf.rational(numerator, denominator);
    }
}
