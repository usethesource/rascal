/*
 * Copyright (c) 2018-2025, NWO-I CWI and Swat.engineering
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.rascalmpl.dap.variable;

import io.usethesource.vallang.*;
import io.usethesource.vallang.impl.reference.ValueFactory;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;
import io.usethesource.vallang.visitors.IValueVisitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.rascalmpl.dap.SuspendedState;
import org.rascalmpl.ideservices.IDEServices;
import org.rascalmpl.values.RascalValueFactory;
import org.rascalmpl.values.parsetrees.ITree;
import org.rascalmpl.values.parsetrees.ProductionAdapter;
import org.rascalmpl.values.parsetrees.SymbolAdapter;
import org.rascalmpl.values.parsetrees.TreeAdapter;

/**
 *  Visitor that collects a specific amount of variables from starting index that are subfields of a given variable
 */
public class VariableSubfieldsVisitor implements IValueVisitor<List<RascalVariable>, RuntimeException> {
    private final IDEServices services;
    private final SuspendedState stateManager;
    private final Type visitedType;
    private final int startIndex;
    private final int count;

    public VariableSubfieldsVisitor(SuspendedState stateManager, Type visitedType, int startIndex, int count, IDEServices services) {
        this.services = services;
        this.stateManager = stateManager;
        this.visitedType = visitedType;
        this.startIndex = startIndex;
        this.count = count;
    }

    @Override
    public List<RascalVariable> visitString(IString o) throws RuntimeException {
        return Collections.emptyList();
    }

    @Override
    public List<RascalVariable> visitReal(IReal o) throws RuntimeException {
        return Collections.emptyList();
    }

    @Override
    public List<RascalVariable> visitRational(IRational o) throws RuntimeException {
        return Collections.emptyList();
    }

    @Override
    public List<RascalVariable> visitList(IList o) throws RuntimeException {
        List<RascalVariable> result = new ArrayList<>();
        int max = count == -1 ? o.length() : Math.min(o.length(), startIndex+count);
        for (int i = startIndex; i < max; i++) {
            RascalVariable newVar = new RascalVariable(visitedType.isList() ? visitedType.getElementType() : o.getElementType(),Integer.toString(i), o.get(i), services);
            addVariableToResult(newVar, result);
        }
        return result;
    }

    @Override
    public List<RascalVariable> visitSet(ISet o) throws RuntimeException {
        List<RascalVariable> result = new ArrayList<>();

        AtomicInteger i = new AtomicInteger(startIndex);
        o.stream().skip(startIndex).limit(count == -1 ? o.size()-startIndex : count).forEach(value -> {
            RascalVariable newVar = new RascalVariable(visitedType.isSet() ? visitedType.getElementType() : o.getElementType(),Integer.toString(i.get()), value, services);
            addVariableToResult(newVar, result);
            i.getAndIncrement();
        });

        return result;
    }

    @Override
    public List<RascalVariable> visitSourceLocation(ISourceLocation o) throws RuntimeException {
        return Collections.emptyList();
    }

    @Override
    public List<RascalVariable> visitTuple(ITuple o) throws RuntimeException {
        List<RascalVariable> result = new ArrayList<>();
        Type type = visitedType.isTuple() ? visitedType : o.getType();
        int max = count == -1 ? o.arity() : Math.min(o.arity(), startIndex+count);
        for (int i = startIndex; i < max; i++) {
            RascalVariable newVar = new RascalVariable(type.getFieldType(i), type.hasFieldNames() ? type.getFieldName(i) : Integer.toString(i), o.get(i), services);
            addVariableToResult(newVar, result);
        }
        return result;
    }

    @Override
    public List<RascalVariable> visitNode(INode o) throws RuntimeException {
        List<RascalVariable> result = new ArrayList<>();

        int max = count == -1 ? o.arity() : Math.min(o.arity(), startIndex+count);
        for (int i = startIndex; i < max; i++) {
            RascalVariable newVar = new RascalVariable(o.get(i).getType(), Integer.toString(i), o.get(i), services);
            addVariableToResult(newVar, result);
        }
        return result;
    }

    @Override
    public List<RascalVariable> visitConstructor(IConstructor o) throws RuntimeException {
        List<RascalVariable> result = new ArrayList<>();

        if(o.getType().isSubtypeOf(RascalValueFactory.Tree)) {
            // Special case for trees
            return visitTree((ITree) o);
        }

        if(startIndex<o.arity()) {
            int max = count == -1 ? o.arity() : Math.min(o.arity(), startIndex+count);
            for (int i = startIndex; i < max; i++) {
                RascalVariable newVar = new RascalVariable(o.getConstructorType().getFieldType(i), o.getConstructorType().hasFieldNames() ? o.getConstructorType().getFieldName(i) : Integer.toString(i), o.get(i), services);
                addVariableToResult(newVar, result);
            }
        }

        if (o.mayHaveKeywordParameters() && (count == -1 || (startIndex+count) - o.arity() > 0)) {
            Map<String, IValue> parameters = o.asWithKeywordParameters().getParameters();
            int remaining = count == -1 ? parameters.size() : (startIndex+count) - o.arity();
            int startParametersIndex = startIndex<o.arity() ? 0 : startIndex-o.arity();
            parameters.keySet().stream().skip(startParametersIndex).limit(remaining).forEach(name -> {
                IValue value = parameters.get(name);
                RascalVariable newVar = new RascalVariable(value.getType(), '['+name+']', value, services);
                addVariableToResult(newVar, result);
            });
        }

        return result;
    }

    public List<RascalVariable> visitTree(ITree o) throws RuntimeException {
        List<RascalVariable> result = new ArrayList<>();
        if (SymbolAdapter.isStartSort(TreeAdapter.getType(o))) { // skip the start symbol
            IList skippedStarto = o.getArgs().delete(0).delete(1);
            if(skippedStarto.length()>0 && skippedStarto.get(0) instanceof ITree){
			    o = (ITree) skippedStarto.get(0);
            }
		}

        IList args = TreeAdapter.getASTArgs(o);
        // Only get symbol if production is default production
        IList symbol = ProductionAdapter.isDefault(o.getProduction()) ? ProductionAdapter.getASTSymbols(o.getProduction()) : null;

        int max = count == -1 ? args.length() : Math.min(args.length(), startIndex+count);
        for (int i = startIndex; i < max; i++) {
            IValue arg = args.get(i);
            String name = symbol != null && i < symbol.length() && SymbolAdapter.isLabel((IConstructor) symbol.get(i))
                    ? SymbolAdapter.getLabel((IConstructor) symbol.get(i))
                    : Integer.toString(i);
            RascalVariable newVar = new RascalVariable(arg.getType(), name, arg, services);
            addVariableToResult(newVar, result);
        }
        return result;
    }

    @Override
    public List<RascalVariable> visitInteger(IInteger o) throws RuntimeException {
        return Collections.emptyList();
    }

    @Override
    public List<RascalVariable> visitMap(IMap o) throws RuntimeException {
        List<RascalVariable> result = new ArrayList<>();

        o.stream().skip(startIndex).limit(count == -1 ? o.size()-startIndex : count).forEach(pair -> {
            ITuple tuple = (ITuple) pair;
            RascalVariable newVar = new RascalVariable(visitedType.isMap() ? visitedType.getValueType() : o.getValueType(), RascalVariableUtils.getDisplayString(tuple.get(0), services), tuple.get(1), services);
            addVariableToResult(newVar, result);
        });

        return result;
    }

    @Override
    public List<RascalVariable> visitBoolean(IBool boolValue) throws RuntimeException {
        return Collections.emptyList();
    }

    @Override
    public List<RascalVariable> visitExternal(IExternalValue externalValue) throws RuntimeException {
        return Collections.emptyList();
    }

    @Override
    public List<RascalVariable> visitDateTime(IDateTime o) throws RuntimeException {
        List<RascalVariable> result = new ArrayList<>();

        if(o.isDate() || o.isDateTime()){
            result.add(new RascalVariable(TypeFactory.getInstance().integerType(), "year", ValueFactory.getInstance().integer(o.getYear()), services));
            result.add(new RascalVariable(TypeFactory.getInstance().integerType(), "month", ValueFactory.getInstance().integer(o.getMonthOfYear()), services));
            result.add(new RascalVariable(TypeFactory.getInstance().integerType(), "day", ValueFactory.getInstance().integer(o.getDayOfMonth()), services));
        }

        if(o.isTime() || o.isDateTime()){
            result.add(new RascalVariable(TypeFactory.getInstance().integerType(), "hour", ValueFactory.getInstance().integer(o.getHourOfDay()), services));
            result.add(new RascalVariable(TypeFactory.getInstance().integerType(), "minute", ValueFactory.getInstance().integer(o.getMinuteOfHour()), services));
            result.add(new RascalVariable(TypeFactory.getInstance().integerType(), "second", ValueFactory.getInstance().integer(o.getSecondOfMinute()), services));
            result.add(new RascalVariable(TypeFactory.getInstance().integerType(), "millisecond", ValueFactory.getInstance().integer(o.getMillisecondsOfSecond()), services));
            result.add(new RascalVariable(TypeFactory.getInstance().integerType(), "timezoneOffsetHours", ValueFactory.getInstance().integer(o.getTimezoneOffsetHours()), services));
            result.add(new RascalVariable(TypeFactory.getInstance().integerType(), "timezoneOffsetMinutes", ValueFactory.getInstance().integer(o.getTimezoneOffsetMinutes()), services));
        }

        return result;
    }

    private void addVariableToResult(RascalVariable newVar, List<RascalVariable> resultList){
        if(newVar.hasSubFields()){
            stateManager.addVariable(newVar);
            VariableSubElementsCounter counter = newVar.getValue().accept(new VariableSubElementsCounterVisitor());
            newVar.setIndexedVariables(counter.getIndexedVariables());
            newVar.setNamedVariables(counter.getNamedVariables());
        }
        resultList.add(newVar);
    }
}
