package org.rascalmpl.test.rpc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.eclipse.lsp4j.jsonrpc.Launcher;
import org.eclipse.lsp4j.jsonrpc.messages.ResponseError;
import org.eclipse.lsp4j.jsonrpc.messages.ResponseErrorCode;
import org.eclipse.lsp4j.jsonrpc.services.JsonRequest;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.rascalmpl.ideservices.GsonUtils;
import org.rascalmpl.ideservices.GsonUtils.ComplexTypeMode;
import org.rascalmpl.library.Prelude;
import org.rascalmpl.library.util.Math;
import org.rascalmpl.values.ValueFactoryFactory;

import com.google.gson.GsonBuilder;

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
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.ITuple;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;
import io.usethesource.vallang.type.TypeStore;

@RunWith(Parameterized.class)
public class IValueOverJsonTests {
    private static final IValueFactory vf = ValueFactoryFactory.getValueFactory();
    private static final Prelude prelude = new Prelude(vf, null, null, null, null);
    private static final Math math = new Math(vf);

    private static TypeFactory tf = TypeFactory.getInstance();
    private static TypeStore ts = new TypeStore();
    private static final Type TestAdt = tf.abstractDataType(ts, "TestAdt");
    private static final Type TestAdt_testCons = tf.constructor(ts, TestAdt, "testCons", tf.stringType(), "id", tf.integerType(), "nr");

    private JsonRpcTestInterface testServer;
    private final PipedInputStream is0, is1;
    private final PipedOutputStream os0, os1;
    private final ExecutorService exec = Executors.newCachedThreadPool();

    @Parameters(name="{0}")
    public static Iterable<Object[]> modesAndConfig() {
        return Arrays.asList(new Object[][] {
            { ComplexTypeMode.ENCODE_AS_JSON_OBJECT, GsonUtils.complexAsJsonObject() },
            { ComplexTypeMode.ENCODE_AS_BASE64_STRING, GsonUtils.complexAsBase64String(ts) },
            { ComplexTypeMode.ENCODE_AS_STRING, GsonUtils.complexAsString(ts) },
            { ComplexTypeMode.NOT_SUPPORTED, GsonUtils.noComplexTypes() }
        });
    }

    private final ComplexTypeMode complexTypeMode;

    public IValueOverJsonTests(ComplexTypeMode complexTypeMode, Consumer<GsonBuilder> gsonConfig) {
        this.complexTypeMode = complexTypeMode;
        try {
            is0 = new PipedInputStream();
            os0 = new PipedOutputStream();
            is1 = new PipedInputStream(os0);
            os1 = new PipedOutputStream(is0);
            new TestThread(is0, os0, gsonConfig, exec).start();
            new TestClient(is1, os1, gsonConfig);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @After
    public void teardown() throws IOException {
        exec.shutdown();
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

    class TestClient {
        public TestClient(InputStream is, OutputStream os, Consumer<GsonBuilder> gsonConfig) {
            Launcher<JsonRpcTestInterface> clientLauncher = new Launcher.Builder<JsonRpcTestInterface>()
                .setLocalService(this)
                .setRemoteInterface(JsonRpcTestInterface.class)
                .setInput(is)
                .setOutput(os)
                .configureGson(gsonConfig)
                .setExceptionHandler(e -> {
                    System.err.println(e);
                    return new ResponseError(ResponseErrorCode.InternalError, e.getMessage(), e);
                })
                .setExecutorService(exec)
                .create();

            clientLauncher.startListening();
            testServer = clientLauncher.getRemoteProxy();
        }
    }

    static class TestThread extends Thread {
        private final InputStream is;
        private final OutputStream os;
        private final Consumer<GsonBuilder> gsonConfig;
        private final ExecutorService exec;
        
        public TestThread(InputStream is, OutputStream os, Consumer<GsonBuilder> gsonConfig, ExecutorService exec) {
            this.is = is;
            this.os = os;
            this.gsonConfig = gsonConfig;
            this.exec = exec;
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
                .setExecutorService(exec)
                .create();

            serverLauncher.startListening();
        }
    }

    private static final Set<ComplexTypeMode> asJsonObjectOrNotSupported = new HashSet<>(Arrays.asList(ComplexTypeMode.ENCODE_AS_JSON_OBJECT, ComplexTypeMode.NOT_SUPPORTED));

    private <T extends IValue> void runTestForPrimitiveType(String type, Supplier<T> supplier, Function<T, CompletableFuture<T>> function) {
        expectSuccessful(type, supplier, function);
    }
    
    private <T extends IValue> void runTestForComplexType(String type, Supplier<T> supplier, Function<T, CompletableFuture<T>> function) {
        if (asJsonObjectOrNotSupported.contains(complexTypeMode)) {
            expectUnsuccessful(type, supplier, function);
        } else {
            expectSuccessful(type, supplier, function);
        }
    }

    private static <T extends IValue> void expectSuccessful(String type, Supplier<T> supplier, Function<T, CompletableFuture<T>> function) {
        var value = supplier.get();
        try {
            assertEquals(value, function.apply(value).get(10, TimeUnit.SECONDS));
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            fail("Error occurred while testing " + type + " over jsonrpc: " + e.getMessage());
        }
    }

    private static <T extends IValue> void expectUnsuccessful(String type, Supplier<T> supplier, Function<T, CompletableFuture<T>> function) {
        try {
            function.apply(supplier.get()).get(10, TimeUnit.SECONDS);
            fail("Error occurred: " + type + " should not have round-tripped");
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            //This is expected
        }
    }

    private static IRational arbRational() {
        IInteger numerator = (IInteger) math.arbInt();
        IInteger denominator = (IInteger) math.arbInt();
        while (denominator.equals(vf.integer(0))) {
            denominator = (IInteger) math.arbInt();
        }
        return vf.rational(numerator, denominator);
    }

    private static interface JsonRpcTestInterface {
        @JsonRequest
        default CompletableFuture<IBool> sendBool(IBool bool) {
            return CompletableFuture.completedFuture(bool);
        }
        
        @JsonRequest
        default CompletableFuture<IConstructor> sendConstructor(IConstructor constructor) {
            return CompletableFuture.completedFuture(constructor);
        }

        @JsonRequest
        default CompletableFuture<IDateTime> sendDateTime(IDateTime dateTime) {
            return CompletableFuture.completedFuture(dateTime);
        }

        @JsonRequest
        default CompletableFuture<IInteger> sendInteger(IInteger integer) {
            return CompletableFuture.completedFuture(integer);
        }

        @JsonRequest
        default CompletableFuture<INode> sendNode(INode node) {
            return CompletableFuture.completedFuture(node);
        }

        @JsonRequest
        default CompletableFuture<IRational> sendRational(IRational rational) {
            return CompletableFuture.completedFuture(rational);
        }

        @JsonRequest
        default CompletableFuture<IReal> sendReal(IReal real) {
            return CompletableFuture.completedFuture(real);
        }

        @JsonRequest
        default CompletableFuture<ISourceLocation> sendLocation(ISourceLocation loc) {
            return CompletableFuture.completedFuture(loc);
        }

        @JsonRequest
        default CompletableFuture<IString> sendString(IString string) {
            return CompletableFuture.completedFuture(string);
        }

        @JsonRequest
        default CompletableFuture<INumber> sendNumber(INumber number) {
            return CompletableFuture.completedFuture(number);
        }

        @JsonRequest
        default CompletableFuture<IValue> sendValue(IValue value) {
            return CompletableFuture.completedFuture(value);
        }

        @JsonRequest
        default CompletableFuture<IList> sendList(IList list) {
            return CompletableFuture.completedFuture(list);
        }
        
        @JsonRequest
        default CompletableFuture<IMap> sendMap(IMap map) {
            return CompletableFuture.completedFuture(map);
        }

        @JsonRequest
        default CompletableFuture<ISet> sendSet(ISet set) {
            return CompletableFuture.completedFuture(set);
        }

        @JsonRequest
        default CompletableFuture<ITuple> sendTuple(ITuple tuple) {
            return CompletableFuture.completedFuture(tuple);
        }
    }

    @Test
    public void testSendBool() {
        runTestForPrimitiveType("IBool", () -> (IBool) prelude.arbBool(), testServer::sendBool);
    }
    
    @Test
    public void testSendConstructor() {
        runTestForComplexType("IConstructor", () -> vf.constructor(TestAdt_testCons, vf.string("hi"), vf.integer(38)), testServer::sendConstructor);
    }

    @Test
    public void testSendDateTime() {
        runTestForPrimitiveType("IDateTime", () -> (IDateTime) prelude.arbDateTime(), testServer::sendDateTime);
    }

    @Test
    public void testSendInteger() {
        runTestForPrimitiveType("IInteger", () -> (IInteger) math.arbInt(), testServer::sendInteger);
    }

    @Test
    public void testSendNode() {
        runTestForComplexType("INode", () -> prelude.arbNode(), testServer::sendNode);
    }

    @Test
    public void testSendRational() {
        runTestForPrimitiveType("IRational", () -> arbRational(), testServer::sendRational);
    }

    @Test
    public void testSendReal() {
        runTestForPrimitiveType("IReal", () -> (IReal) math.arbReal(), testServer::sendReal);
    }

    @Test
    public void testSendLocation() {
        runTestForPrimitiveType("ISourceLocation", () -> prelude.arbLoc(), testServer::sendLocation);
    }

    @Test
    public void testSendString() {
        runTestForPrimitiveType("IString", () -> prelude.arbString(vf.integer(1024)), testServer::sendString);
    }
    
    @Test
    public void testSendIntAsNumber() {
        runTestForPrimitiveType("INumber", () -> (IInteger) math.arbInt(), testServer::sendNumber);
    }

    @Test
    public void testSendRealAsNumber() {
        runTestForPrimitiveType("INumber", () -> (IReal) math.arbReal(), testServer::sendNumber);
    }
    
    @Test
    public void testSendRealAsValue() {
        runTestForPrimitiveType("IValue", () -> (IReal) math.arbReal(), testServer::sendReal);
    }
    
    @Test
    public void testSendList() {
        runTestForComplexType("IList", () -> vf.list(vf.string(""), vf.integer(0)), testServer::sendList);
    }

    @Test
    public void testSendMap() {
        IMapWriter writer = vf.mapWriter();
        writer.put(vf.integer(0), vf.string("zero"));
        writer.put(vf.integer(1), vf.string("one"));
        runTestForComplexType("IMap", () -> writer.done(), testServer::sendMap);
    }

    @Test
    public void testSendSet() {
        runTestForComplexType("ISet", () -> vf.set(vf.integer(0), vf.integer(1), vf.integer(2)), testServer::sendSet);
    }

    @Test
    public void testSendTuple() {
        runTestForComplexType("ITuple", () -> vf.tuple(vf.integer(0), vf.integer(1)), testServer::sendTuple);
    }
}
