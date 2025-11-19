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
package org.rascalmpl.dap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.rascalmpl.dap.variable.RascalVariable;
import org.rascalmpl.dap.variable.VariableSubElementsCounter;
import org.rascalmpl.dap.variable.VariableSubElementsCounterVisitor;
import org.rascalmpl.dap.variable.VariableSubfieldsVisitor;
import org.rascalmpl.debug.IRascalFrame;
import org.rascalmpl.ideservices.IDEServices;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.IRascalResult;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.result.ResultFactory;
import org.rascalmpl.interpreter.staticErrors.UndeclaredVariable;
import org.rascalmpl.uri.URIUtil;

import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValue;

/**
 * Class used to store the state of the Rascal Evaluator when it is suspended
 */
public class SuspendedState {
    private final Evaluator evaluator;
    private final IDEServices services;
    private volatile IRascalFrame[] currentStackFrames;
    private final Map<Integer, RascalVariable> variables;
    private final Map<Integer, IRascalFrame> scopes;
    private volatile int referenceIDCounter;
    private volatile boolean isSuspended;


    public SuspendedState(Evaluator evaluator, IDEServices services) {
        this.evaluator = evaluator;
        this.services = services;
        this.variables = new ConcurrentHashMap<>();
        this.scopes = new ConcurrentHashMap<>();
    }

    public void suspended(){
        currentStackFrames = evaluator.getCurrentStack().toArray(IRascalFrame[]::new);
        referenceIDCounter = 0;
        this.variables.clear();
        this.scopes.clear();
        this.isSuspended = true;
    }

    public ISourceLocation getCurrentLocation(){
        return evaluator.getCurrentPointOfExecution() != null ?
            evaluator.getCurrentPointOfExecution()
            : URIUtil.rootLocation("stdin");
    }

    public void resumed(){
        this.isSuspended = false;
    }

    public boolean isSuspended() {
        return isSuspended;
    }

    public IRascalFrame[] getCurrentStackFrames(){
        return currentStackFrames;
    }

    public IRascalFrame getCurrentStackFrame(){
        var stackCopy = currentStackFrames;
        return stackCopy[stackCopy.length - 1];
    }

    public int addScope(IRascalFrame frame){
        int nextReferenceId = ++referenceIDCounter;
        scopes.put(nextReferenceId, frame);
        return nextReferenceId;
    }

    public List<RascalVariable> getVariables(int referenceID, int startIndex, int maxCount){
        if(referenceID < 0){
            return Collections.emptyList();
        }
        List<RascalVariable> variableList = new ArrayList<>();

        // referenceID is a stack frame reference id
        if(scopes.containsKey(referenceID)){
            IRascalFrame frame = scopes.get(referenceID);
            List<String> frameVariables = new ArrayList<>(frame.getFrameVariables());
            frameVariables.sort(String::compareTo);
            int endIndex = maxCount == -1 ? frameVariables.size() : Math.min(frameVariables.size(), startIndex + maxCount);
            for (String varname : frameVariables.subList(startIndex, endIndex)) {
                IRascalResult result = frame.getFrameVariable(varname);
                RascalVariable refResult = new RascalVariable(result.getDynamicType(), varname, result.getValue(), services);
                if(refResult.hasSubFields()){
                    addVariable(refResult);
                    VariableSubElementsCounter counter = result.getValue().accept(new VariableSubElementsCounterVisitor());
                    refResult.setIndexedVariables(counter.getIndexedVariables());
                    refResult.setNamedVariables(counter.getNamedVariables());
                }
                variableList.add(refResult);
            }
            return variableList;
        }

        if(!variables.containsKey(referenceID)) {
            return variableList;
        }

        // referenceID is a variable ID
        RascalVariable var = variables.get(referenceID);
        return var.getValue().accept(new VariableSubfieldsVisitor(this, var.getType(), startIndex, maxCount, services));
    }

    public void addVariable(RascalVariable variable){
        int nextReferenceID = ++referenceIDCounter;
        variable.setReferenceID(nextReferenceID);
        variables.put(nextReferenceID, variable);
    }

    public RascalVariable setVariable(int referenceID, String name, String valueStr) {
        if(!scopes.containsKey(referenceID)){
            // raise undeclared variable error
            throw new UndeclaredVariable(name, null);
        }
        IRascalFrame frame = scopes.get(referenceID);

        // First: check that this variable exists in the suspended frame. If it
        // doesn't, we don't create it silently; signal failure so the UI can
        // report the error and avoid temporarily showing a value that will be
        // rolled back later.
        if (!frame.getFrameVariables().contains(name)) {
            return null;
        }

        // Evaluate the new value in a fork to avoid touching the suspended evaluator's
        // execution state. Then wrap the raw IValue into a Result bound to the
        // suspended evaluator so it can be stored in its Environment.
        Result<IValue> forkResult = evaluator.fork().eval(evaluator.getMonitor(), valueStr, URIUtil.rootLocation("internal"));
        Result<IValue> newValue = ResultFactory.makeResult(forkResult.getDynamicType(), forkResult.getValue(), evaluator);

        // Try to update the actual frame environment so the mutation affects
        // the suspended evaluator. Many IRascalFrame implementations are backed
        // by Environment (or ModuleEnvironment) which expose storeLocalVariable
        // and declareVariable. If available, declare and store the new value.
        boolean stored = false;
        try {
            if (frame instanceof Environment) {
                Environment env = (Environment) frame;
                // declare variable if necessary (some envs require declaration)
                try {
                    env.declareVariable(newValue.getDynamicType(), name);
                } catch (Throwable t) {
                    // ignore if not supported
                }

                try {
                    env.storeLocalVariable(name, newValue);
                    stored = true;
                } catch (Throwable t) {
                    // fallback: try generic storeVariable if available
                    try {
                        env.storeVariable(name, newValue);
                        stored = true;
                    } catch (Throwable t2) {
                        stored = false;
                    }
                }
            }
        } catch (Throwable t) {
            stored = false;
        }

        // If storing into the runtime environment failed, signal failure so the
        // UI doesn't optimistically display a value that won't persist.
        if (!stored) {
            return null;
        }

        // Update any cached RascalVariable (used for expanded complex variables)
        // by replacing the old entry with a new one that preserves the reference id
        // so the UI keeps working with the same reference.
        for (Map.Entry<Integer, RascalVariable> entry : variables.entrySet()) {
            RascalVariable rv = entry.getValue();
            if (rv.getName().equals(name)) {
                RascalVariable replacement = new RascalVariable(newValue.getDynamicType(), name, newValue.getValue(), services);
                replacement.setReferenceID(rv.getReferenceID());
                replacement.setIndexedVariables(rv.getIndexedVariables());
                replacement.setNamedVariables(rv.getNamedVariables());
                variables.put(entry.getKey(), replacement);
            }
        }

        return new RascalVariable(
            newValue.getDynamicType(), 
            name, 
            newValue.getValue(),
            services
        );
    }

}
