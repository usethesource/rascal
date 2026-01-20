package org.rascalmpl.test.rpc;

import java.util.concurrent.CompletableFuture;

import org.eclipse.lsp4j.jsonrpc.services.JsonRequest;

import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IDateTime;
import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IMap;
import io.usethesource.vallang.INode;
import io.usethesource.vallang.INumber;
import io.usethesource.vallang.IRational;
import io.usethesource.vallang.IReal;
import io.usethesource.vallang.ISet;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.ITuple;
import io.usethesource.vallang.IValue;

public interface JsonRpcTestInterface {
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
