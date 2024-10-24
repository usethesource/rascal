package org.rascalmpl.shell;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.Queue;

import org.rascalmpl.library.Prelude;
import org.rascalmpl.library.lang.rascal.syntax.RascalParser;
import org.rascalmpl.parser.Parser;
import org.rascalmpl.parser.gtd.result.out.DefaultNodeFlattener;
import org.rascalmpl.parser.uptr.UPTRNodeFactory;
import org.rascalmpl.parser.uptr.action.NoActionExecutor;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.values.parsetrees.ITree;
import org.rascalmpl.values.parsetrees.TreeAdapter;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.ISourceLocation;

public class ParseTest {

    private static ITree parseFile(ISourceLocation f, char[] data) {
        return new RascalParser().parse(Parser.START_MODULE, f.getURI(), data, new NoActionExecutor(),
            new DefaultNodeFlattener<IConstructor, ITree, ISourceLocation>(), new UPTRNodeFactory(true));
    }

    private static void progress(String s, Object... args) {
        System.err.println(String.format(s, args));
    }

    private static List<Entry<ISourceLocation, char[]>> findFiles() throws URISyntaxException, IOException {
        final var reg = URIResolverRegistry.getInstance();
        Queue<ISourceLocation> worklist = new LinkedList<>();
        worklist.add(URIUtil.correctLocation("cwd", null, "/src"));
        // URIUtil.createFileLocation("d:\\swat.engineering\\rascal\\rascal\\src"));
        List<Entry<ISourceLocation, char[]>> result = new ArrayList<>();
        ISourceLocation next;
        while ((next = worklist.poll()) != null) {
            var children = reg.list(next);
            for (var c: children) {
                if (reg.isDirectory(c)) {
                    worklist.add(c);
                }
                else if (c.getPath().endsWith(".rsc")) {
                    try (var r = reg.getCharacterReader(c)) {
                        result.add(new AbstractMap.SimpleEntry<>(c, Prelude.consumeInputStream(r).toCharArray()));
                    }
                }
            }
        }
        return result;
        

    }

    static final int parsingRounds = 10;
    static final int warmupRounds = 2;

    public static void main(String[] args) throws URISyntaxException, IOException {
        progress("Collecting rascal files");
        var targets = findFiles();
        progress("Found: %d files, press enter to start parsing", targets.size());
        System.in.read();
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        progress("Start parsing");
        
        long count = 0;
        long totalTime = 0;

        for (int i = 0; i < warmupRounds + parsingRounds; i++) {
            var start = System.nanoTime();
            for (var t : targets) {
                var tr = parseFile(t.getKey(), t.getValue());
                count += TreeAdapter.getLocation(TreeAdapter.getStartTop(tr)).getLength();
            }
            var stop = System.nanoTime();
            progress("%s took: %d ms", i < warmupRounds ? "Warmup" : "Parsing", TimeUnit.NANOSECONDS.toMillis(stop - start));
            if (i >= warmupRounds) {
                totalTime += stop - start;
            }
        }
        progress("Done: %d, average time: %d ms", count, TimeUnit.NANOSECONDS.toMillis(totalTime / parsingRounds));
    }
}
