package org.rascalmpl.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Predicate;

import org.junit.Test;
import org.rascalmpl.debug.NullRascalMonitor;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.shell.ShellEvaluatorFactory;
import org.rascalmpl.uri.URIUtil;

public class ProfilerTests {
    
    @Test 
    public void smokeTestNone() throws Exception {
        smokeTest("none", "txt", String::isEmpty);
    }

    @Test 
    public void smokeTestFolded() throws Exception {
        Predicate<String> isFolded = s -> // Approximation
            s.contains("$ at |unknown:///|");

        smokeTest("folded", "folded", isFolded);
    }

    @Test 
    public void smokeTestCpuprofile() throws Exception {
        Predicate<String> isCpuprofile = s -> // Approximation
            s.contains("\"startTime\":") &&
            s.contains("\"endTime\":") &&
            s.contains("\"samples\":") &&
            s.contains("\"timeDeltas\":");
        
        smokeTest("cpuprofile", "cpuprofile", isCpuprofile);
    }

    private void smokeTest(String key, String extension, Predicate<String> predicate) throws Exception {
        var dir = Files.createTempDirectory(this.getClass().getName());
        assertEquals(0, Files.list(dir).count());
        
        var e = newEvaluator();
        enter(e, ":set profileFmt \"" + key + "\"");
        enter(e, ":set profileDir " + URIUtil.correctLocation("file", "", dir.toString()));
        enter(e, ":set profiling true");
        enter(e, "import IO;");

        assertEquals(2, Files.list(dir).count());
        assertTrue(Files.list(dir).allMatch(p -> p.toString().endsWith("." + extension)));
        assertTrue(Files.list(dir).map(ProfilerTests::readStringUnchecked).allMatch(predicate));
    }

    private static Evaluator newEvaluator() {
        var input = new BufferedReader(new InputStreamReader(System.in));
        var stdout = new PrintWriter(System.out);
        var stderr = new PrintWriter(System.err);
        var monitor = new NullRascalMonitor();
        return ShellEvaluatorFactory.getBasicEvaluator(input, stdout, stderr, monitor);
    }

    private static void enter(Evaluator e, String input) {
        e.eval(e.getMonitor(), input, URIUtil.unknownLocation());
    }

    private static String readStringUnchecked(Path path) {
        try {
            return Files.readString(path);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } 
    }
}
