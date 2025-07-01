/*
 * Copyright (c) 2025, Swat.engineering
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

package org.rascalmpl.test.functionality;

import static org.junit.Assert.fail;

import java.io.PrintWriter;
import java.io.Reader;
import java.time.Duration;
import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.shell.ShellEvaluatorFactory;
import org.rascalmpl.test.infrastructure.RascalJunitConsoleMonitor;
import org.rascalmpl.uri.URIUtil;

import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IValue;


@RunWith(Parameterized.class)
public class WatchTests {

    private static final Duration FILE_CREATION_DELAY;
    
    static {
        var os = System.getProperty("os.name", "?").toLowerCase();
        if (os.contains("mac")) {
            // macOS tends to be a bit slower with dispatching events
            FILE_CREATION_DELAY = Duration.ofMillis(100);
        } else {
            FILE_CREATION_DELAY = Duration.ofMillis(0);
        }
    }

    @Parameters(name="{0}")
    public static Object[] locations() { 
        return new Object[] {"|tmp:///watch-tests/", "|memory:///watch-tests/"};
    }

    private final String locationPrefix;
    private final Random rand;

    public WatchTests(String locationPrefix) {
        this.locationPrefix = locationPrefix;
        rand = new Random();
    }

    private IEvaluator<Result<IValue>> setupWatchEvaluator() {
        return setupWatchEvaluator(true);
    }
    private final IEvaluator<Result<IValue>> setupWatchEvaluator(boolean debug) {
        var evaluator = ShellEvaluatorFactory.getBasicEvaluator(Reader.nullReader(), new PrintWriter(System.err, true), new PrintWriter(System.out, false), RascalJunitConsoleMonitor.getInstance());
        
        executeCommand(evaluator, "import IO;");
        executeCommand(evaluator, "int trig = 0;");
        executeCommand(evaluator, "int trig2 = 0;");
        executeCommand(evaluator, "void triggerWatch(FileSystemChange tp) { trig = trig + 1; " + (debug? "println(tp);": "") + " }");
        executeCommand(evaluator, "void triggerWatch2(FileSystemChange tp) { trig2 = trig2 + 1; " + (debug? "println(tp);": "") + " }");
        return evaluator;
    }

    private static IValue executeCommand(IEvaluator<Result<IValue>> eval, String command) {
        synchronized (eval) {
            var result = eval.eval(null, command, URIUtil.rootLocation("stdin"));
            if (result.getStaticType().isBottom()) {
                return null;
            }
            return result.getValue();
        }
    }

    private static boolean executeBooleanExpression(IEvaluator<Result<IValue>> eval, String expr) {
        synchronized (eval) {
            var result = executeCommand(eval, expr);
            if (result instanceof IBool) {
                return ((IBool)result).getValue();
            }
            return false;
        }
    }


    private final void waitForTrue(String message, IEvaluator<Result<IValue>> eval, String expr) throws InterruptedException {
        int sleep = 10;
        for (int i = 0; i < 40; i++) {
            Thread.sleep(sleep);
            if (executeBooleanExpression(eval, expr)) {
                return;
            }
            sleep += sleep / 2;
            sleep = Math.min(100, sleep);
        }
        fail(message);
    }

    private final void holdTrue(String message, IEvaluator<Result<IValue>> eval, String expr) throws InterruptedException {
        int sleep = 10;
        for (int i = 0; i < 20; i++) {
            Thread.sleep(sleep);
            if (!executeBooleanExpression(eval, expr)) {
                fail(message);
            }
            sleep += sleep / 2;
            sleep = Math.min(100, sleep);
        }
    }

    private final void createFile(IEvaluator<Result<IValue>> eval, String loc, String content) throws InterruptedException {
        executeCommand(eval, "writeFile(" + loc + ", \"" + content + "\");");
        // Wait a bit until the file creation is done and events are dispatched
        Thread.sleep(FILE_CREATION_DELAY.toMillis());
    }

    @Test
    public void recursiveWatch() throws InterruptedException {
        var evalTest = setupWatchEvaluator();
        var ourDir = locationPrefix + "rec-" + rand.nextInt(1000);
        createFile(evalTest, ourDir + "/make-dir.txt|", "hi"); 
        executeCommand(evalTest, "watch(" + ourDir + "|, true, triggerWatch);");
        createFile(evalTest, ourDir + "/a/b/c/test-watch.txt|", "hi");
        waitForTrue("Watch should have been triggered", evalTest, "trig > 0");
    }

    @Test
    public void nonRecursive() throws InterruptedException {
        var evalTest = setupWatchEvaluator();
        var ourFile = locationPrefix + "test-non-recursive.txt|";
        createFile(evalTest, ourFile, "hi");
        executeCommand(evalTest, "watch(" + ourFile + ", false, triggerWatch);");
        executeCommand(evalTest, "writeFile(" + ourFile + ", \"hi\");");
        waitForTrue("Watch should have been triggered", evalTest, "trig > 0");
    }

    @Test
    public void delete() throws InterruptedException {
        var evalTest = setupWatchEvaluator();
        createFile(evalTest, locationPrefix + "test-delete.txt|", "hi");
        executeCommand(evalTest, "watch(" + locationPrefix + "|, true, triggerWatch);");
        executeCommand(evalTest, "remove(" + locationPrefix + "test-delete.txt|);");
        waitForTrue("Watch should have been triggered for delete", evalTest, "trig > 0");
    }
    

    @Test
    public void singleFile() throws InterruptedException {
        var evalTest = setupWatchEvaluator();
        var file1 = locationPrefix + "test-watch-a-" + rand.nextInt(100) +  ".txt|";
        var file2 = locationPrefix + "test-watch-" + rand.nextInt(100) +  ".txt|";
        createFile(evalTest, file1, "making it exist");
        executeCommand(evalTest, "watch(" + file1 + ", false, triggerWatch);");
        createFile(evalTest, file2, "bye");
        executeCommand(evalTest, "remove(" + file2 + ");");
        holdTrue("Watch should not have been triggered", evalTest, "trig == 0");
    }

    @Test
    public void combinedFileAndDirectoryWatchesAreFine() throws InterruptedException {
        var evalTest = setupWatchEvaluator();
        var ourDirectory = locationPrefix + "/combined" + rand.nextInt(100);
        createFile(evalTest, ourDirectory + "/test-watch-a5.txt|", "making it exist");
        executeCommand(evalTest, "watch(" + ourDirectory + "/test-watch-a5.txt|, false, triggerWatch);");
        executeCommand(evalTest, "watch(" + ourDirectory + "|, false, triggerWatch2);");
        createFile(evalTest, ourDirectory + "/test-watch.txt|", "bye");
        holdTrue("Watch should not have been triggered for file watch", evalTest, "trig == 0");
        waitForTrue("Watch should have been triggered due to directory watch", evalTest, "trig2 > 0");
    }

    @Test
    public void unwatchStopsEvents() throws InterruptedException {
        var evalTest = setupWatchEvaluator();
        var ourDirectory = locationPrefix + "abc" + rand.nextInt(100);
        createFile(evalTest, ourDirectory + "/testing123.txt|", "");
        executeCommand(evalTest, "watch(" + ourDirectory + "|, true, triggerWatch);");
        Thread.sleep(10); 
        executeCommand(evalTest, "unwatch(" + ourDirectory + "|, true, triggerWatch);");
        Thread.sleep(10); // give it some time to trigger the watch callback
        createFile(evalTest, ourDirectory + "/test-watch.txt|", "hi");
        executeCommand(evalTest, "remove(" + ourDirectory + "/test-watch.txt|);");
        holdTrue("Watch should not have been triggered", evalTest, "trig == 0");
    }

    @Test
    public void unwatchStopsEventsUnrecursive() throws InterruptedException {
        var evalTest = setupWatchEvaluator();
        var ourFile = locationPrefix + "test-watch-" + rand.nextInt(2000) + ".txt|"; // make sure other watches might not be still active for this file
        createFile(evalTest, ourFile, "hi");
        executeCommand(evalTest, "watch(" + ourFile + ", false, triggerWatch);");
        executeCommand(evalTest, "watch(" + ourFile + ", false, triggerWatch2);");
        Thread.sleep(10); // it can take a while before a watch is activated
        executeCommand(evalTest, "unwatch(" + ourFile + ", false, triggerWatch);");
        Thread.sleep(10); // it can take a while before a watch is deactivated
        executeCommand(evalTest, "trig = 0;");
        executeCommand(evalTest, "writeFile(" + ourFile + ", \"hi\");");
        executeCommand(evalTest, "remove(" + ourFile + ");");
        waitForTrue("Watch should have been triggered for open watch", evalTest, "trig2 > 0");
        holdTrue("Watch should not have been triggered for closed watch", evalTest, "trig == 0");
    }
}
