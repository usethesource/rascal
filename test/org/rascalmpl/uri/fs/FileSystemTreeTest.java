package org.rascalmpl.uri.fs;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.io.FileExistsException;
import org.junit.Before;
import org.junit.Test;

public class FileSystemTreeTest {
    private FileSystemTree<FSEntry> target;
    private long currentTime = 0;

    @Before
    public void initTarget() {
        target = new FileSystemTree<FSEntry>(new FSEntry(0, 0, 0), true);
        currentTime = 0;
    }

    private void addFile(String name) throws IOException {
        addFile(name, currentTime, currentTime, 1);
        currentTime++;
    }
    private void addFile(String name, long created, long lastModified, long size) throws IOException {
        target.addFile(name, new FSEntry(created, lastModified, size), FileSystemTreeTest::dirEntry);
    }
    private void addDirectory(String name) throws IOException {
        addDirectory(name, currentTime, currentTime);
        currentTime++;
    }
    private void addDirectory(String name, long created, long lastModified) throws IOException {
        target.addDirectory(name, dirEntry(created, lastModified), FileSystemTreeTest::dirEntry);
    }

    private static FSEntry dirEntry(long created, long lastModified) {
        return new FSEntry(created, lastModified, 1);
    }


    @Test
    public void emptyDirectoryListing() throws IOException {
        assertArrayEquals(new String[0], target.directChildren("/"));
        addDirectory("a");
        assertArrayEquals(new String[0], target.directChildren("/a"));
    }

    @Test
    public void afterAddingFileItShouldShow() throws IOException {
        addFile("f1.txt");
        assertArrayEquals(new String[] { "f1.txt" }, target.directChildren("/"));
        addFile("a/f2.txt");
        assertArrayEquals(new String[] { "f2.txt" }, target.directChildren("a"));
    }

    @Test
    public void prefixSlashShouldNotMatter() throws IOException {
        addFile("a");
        assertArrayEquals("no slash", new String[] { "a" }, target.directChildren(""));
        assertArrayEquals("single slash", new String[] { "a" }, target.directChildren("/"));
        assertArrayEquals("triple slash", new String[] { "a" }, target.directChildren("///"));

        addFile("b/f1.txt");
        assertArrayEquals(new String[] { "f1.txt" }, target.directChildren("///b"));
    }

    @Test
    public void postfixSlashShouldNotMatter() throws IOException {
        addFile("a/f1.txt");
        assertArrayEquals("single slash should be fine", new String[] { "f1.txt" }, target.directChildren( "a/"));
        assertArrayEquals("triple slash should be fine", new String[] { "f1.txt" }, target.directChildren( "a///"));
        assertArrayEquals("prefix and double slash should be fine", new String[] { "f1.txt" }, target.directChildren("///a///"));
    }

    @Test
    public void makeIntermediateDirectories() throws IOException {
        addFile("a/b/c/d.txt");
        addFile("/a/b/c/d2.txt");
        assertArrayEquals(new String[] { "c" }, target.directChildren("a/b"));
        assertArrayEquals(new String[] { "d.txt", "d2.txt" }, sorted(target.directChildren("a/b/c")));
    }

    @Test
    public void makeNewDirectories() throws IOException {
        addDirectory("/b/c");
        assertArrayEquals(new String[] { "b" }, target.directChildren("/"));
        assertArrayEquals(new String[] { "c" }, target.directChildren("/b"));
        addDirectory("d/e/");
        assertArrayEquals(new String[] { "b", "d" }, sorted(target.directChildren("/")));
        assertArrayEquals(new String[] { "e" }, target.directChildren("/d"));
    }


    @Test
    public void delete() throws IOException {
        addFile("a/b/c.txt");
        assertThrows(IOException.class, () -> target.remove("a"));
        assertThrows(IOException.class, () -> target.remove("a/b"));
        target.remove("a/b/c.txt");
        assertArrayEquals(new String[]{ "b"}, target.directChildren("a"));
        assertArrayEquals(new String[0], target.directChildren("a/b"));
        target.remove("a/b");
        target.remove("a");
        target.remove("c"); // should not throw even though it does not exist
    }

    @Test
    public void filesAreNotDirectories() throws IOException {
        addFile("a");
        assertThrows(IOException.class, () -> addFile("a/b.txt"));
        assertThrows(IOException.class, () -> target.lastModified("a/b.txt"));
        assertThrows(IOException.class, () -> target.directChildren("a/"));

        addFile("b/c");
        assertThrows(IOException.class, () -> addFile("b/c/d.txt"));
        assertThrows(IOException.class, () -> target.lastModified("b/c/d.txt"));
        assertThrows(IOException.class, () -> target.directChildren("b/c"));
    }

    @Test
    public void multiThreadingNothingLost() throws IOException, InterruptedException, BrokenBarrierException {
        var written = new ConcurrentLinkedDeque<String>();
        int threads = Runtime.getRuntime().availableProcessors();
        var anyException = new AtomicReference<IOException>();
        var done = new AtomicBoolean(false);
        var start = new CyclicBarrier(threads + 1);
        var finished = new CyclicBarrier(threads + 1);
        for (int i = 0; i < threads; i++) {
            var r = new Random(i * System.currentTimeMillis());
            var t = new Thread(() -> {
                try {
                    start.await();
                    while (!done.get()) {
                        for (int j = 5; j < 1000; j++) { 
                            var file = String.format("%s/%s.txt", r.nextInt(j / 2), r.nextInt(j));
                            try {
                                target.addFile(file, new FSEntry(j, j, 1), FileSystemTreeTest::dirEntry);
                                written.add(file);
                            } catch (FileExistsException ignored) {
                            }
                            var dir = String.format("%s/%s/%s", r.nextInt(j / 2), r.nextInt(j / 3), r.nextInt(j));
                            try {
                                target.addDirectory(dir, dirEntry(j, j), FileSystemTreeTest::dirEntry);
                                written.add(dir);
                            } catch (FileExistsException ignored) {
                            }
                        }
                    }
                } catch (InterruptedException | BrokenBarrierException e) {
                    return;
                } catch (IOException e) {
                    anyException.compareAndSet(null, e);
                } finally {
                    try {
                        finished.await();
                    }
                    catch (InterruptedException | BrokenBarrierException e) {
                        return;
                    }
                }
            });
            t.start();
        }
        start.await();
        Thread.sleep(2000);
        done.set(true);
        finished.await();
        for (var p : written) {
            assertTrue(p + " should exist", target.exists(p));
        }
    }

    private static String[] sorted(String [] input) {
        Arrays.sort(input);
        return input;
    }
    
}
