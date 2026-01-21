package org.rascalmpl.test.rpc;

import java.io.IOException;

import org.junit.BeforeClass;
import org.rascalmpl.ideservices.GsonUtils;

import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IDateTime;
import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.IMapWriter;
import io.usethesource.vallang.IReal;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;
import io.usethesource.vallang.type.TypeStore;

public class ComplexAsStringTests extends IValueOverJsonTestBase {
    private static TypeFactory tf = TypeFactory.getInstance();
    private static TypeStore ts = new TypeStore();
    private static final Type TestAdt = tf.abstractDataType(ts, "TestAdt");
    private static final Type TestAdt_testCons = tf.constructor(ts, TestAdt, "testCons", tf.stringType(), "id", tf.integerType(), "nr");

    @BeforeClass
    public static void setup() throws IOException {
        startTestServerAndClient(GsonUtils.complexAsString(ts));
    }

    @Override
    public void testSendBool() {
        expectSuccessful("IBool", () -> (IBool) prelude.arbBool(), testServer::sendBool);
    }

    @Override
    public void testSendConstructor() {
        // expectSuccessful("IConstructor", () -> vf.constructor(TestAdt_testCons, vf.string("hi"), vf.integer(38)), testServer::sendConstructor);
    }

    @Override
    public void testSendDateTime() {
        expectSuccessful("IDateTime", () -> (IDateTime) prelude.arbDateTime(), testServer::sendDateTime);
    }

    @Override
    public void testSendInteger() {
        expectSuccessful("IInteger", () -> (IInteger) math.arbInt(), testServer::sendInteger);
    }

    @Override
    public void testSendNode() {
        // expectSuccessful("INode", () -> prelude.arbNode(), testServer::sendNode);
    }

    @Override
    public void testSendRational() {
        // expectSuccessful("IRational", () -> arbRational(), testServer::sendRational);
    }

    @Override
    public void testSendReal() {
        expectSuccessful("IReal", () -> (IReal) math.arbReal(), testServer::sendReal);
    }

    @Override
    public void testSendLocation() {
        expectSuccessful("ISourceLocation", () -> prelude.arbLoc(), testServer::sendLocation);
    }

    @Override
    public void testSendString() {
        expectSuccessful("IString", () -> prelude.arbString(vf.integer(1024)), testServer::sendString);
    }

    @Override
    public void testSendIntAsNumber() {
        expectSuccessful("INumber", () -> (IInteger) math.arbInt(), testServer::sendNumber);
    }

    @Override
    public void testSendRealAsNumber() {
        expectSuccessful("INumber", () -> (IReal) math.arbReal(), testServer::sendNumber);
    }

    @Override
    public void testSendRealAsValue() {
        expectSuccessful("IValue", () -> (IReal) math.arbReal(), testServer::sendReal);
    }

    @Override
    public void testSendList() {
        // expectSuccessful("IList", () -> vf.list(vf.string(""), vf.integer(0)), testServer::sendList);
    }

    @Override
    public void testSendMap() {
        IMapWriter writer = vf.mapWriter();
        writer.put(vf.integer(0), vf.string("zero"));
        writer.put(vf.integer(1), vf.string("one"));
        // expectSuccessful("IMap", () -> writer.done(), testServer::sendMap);
    }

    @Override
    public void testSendSet() {
        // expectSuccessful("ISet", () -> vf.set(vf.integer(0), vf.integer(1), vf.integer(2)), testServer::sendSet);
    }

    @Override
    public void testSendTuple() {
        // expectSuccessful("ITuple", () -> vf.tuple(vf.integer(0), vf.integer(1)), testServer::sendTuple);
    }
}
