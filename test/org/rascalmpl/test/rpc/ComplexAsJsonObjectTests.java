package org.rascalmpl.test.rpc;

import java.io.IOException;

import org.junit.BeforeClass;
import org.rascalmpl.ideservices.GsonUtils;
import org.rascalmpl.values.RascalValueFactory;

import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IDateTime;
import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.IMapWriter;
import io.usethesource.vallang.IReal;

public class ComplexAsJsonObjectTests extends IValueOverJsonTestBase {
    @BeforeClass
    public static void setup() throws IOException {
        startTestServerAndClient(GsonUtils.complexAsJsonObject());
    }

    @Override
    public void testSendBool() {
        expectSuccessful("IBool", () -> (IBool) prelude.arbBool(), testServer::sendBool);
    }

    @Override
    public void testSendConstructor() {
        expectUnsuccessful("IConstructor", () -> (IConstructor) RascalValueFactory.Attribute_Assoc_Left, testServer::sendConstructor);
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
        expectUnsuccessful("INode", () -> prelude.arbNode(), testServer::sendNode);
    }

    @Override
    public void testSendRational() {
        expectSuccessful("IRational", () -> arbRational(), testServer::sendRational);
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
        expectUnsuccessful("IList", () -> vf.list(vf.string(""), vf.integer(0)), testServer::sendList);
    }

    @Override
    public void testSendMap() {
        IMapWriter writer = vf.mapWriter();
        writer.put(vf.integer(0), vf.string("zero"));
        writer.put(vf.integer(1), vf.string("one"));
        expectUnsuccessful("IMap", () -> writer.done(), testServer::sendMap);
    }

    @Override
    public void testSendSet() {
        expectUnsuccessful("ISet", () -> vf.set(vf.integer(0), vf.integer(1), vf.integer(2)), testServer::sendSet);
    }

    @Override
    public void testSendTuple() {
        expectUnsuccessful("ITuple", () -> vf.tuple(vf.integer(0), vf.integer(1)), testServer::sendTuple);
    }
}
