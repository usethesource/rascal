package org.rascalmpl.uri.fs;

import static org.junit.Assert.assertArrayEquals;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

public class FileSystemTreeTest {
    private FileSystemTree<FSEntry> target;

    @Before
    public void initTarget() throws IOException {
        target = new FileSystemTree<FSEntry>(new FSEntry(0, 0));
    }

    @Test
    public void emptyDirectoryListing() throws IOException {
        assertArrayEquals(new String[0], target.directChildren("/"));
        target.addDirectory("a", new FSEntry(1, 1), FSEntry::new);
        assertArrayEquals(new String[0], target.directChildren("/a"));
    }

    @Test
    public void afterAddingFileItShouldShow() throws IOException {
        target.addFile("f1.txt", new FSEntry(2, 2), FSEntry::new);
        assertArrayEquals(new String[] { "f1.txt" }, target.directChildren("/"));
        target.addFile("a/f2.txt", new FSEntry(2, 2), FSEntry::new);
        assertArrayEquals(new String[] { "f2.txt" }, target.directChildren("a"));
    }

    @Test
    public void prefixSlashShouldNotMatter() throws IOException {
        target.addFile("a", new FSEntry(0, 0), FSEntry::new);
        assertArrayEquals("no slash", new String[] { "a" }, target.directChildren(""));
        assertArrayEquals("single slash", new String[] { "a" }, target.directChildren("/"));
        assertArrayEquals("triple slash", new String[] { "a" }, target.directChildren("///"));

        target.addFile("b/f1.txt", new FSEntry(2, 2), FSEntry::new);
        assertArrayEquals(new String[] { "f1.txt" }, target.directChildren("///b"));
    }

    @Test
    public void postfixSlashShouldNotMatter() throws IOException {
        target.addFile( "a/f1.txt", new FSEntry(2, 2), FSEntry::new);
        assertArrayEquals("single slash should be fine", new String[] { "f1.txt" }, target.directChildren( "a/"));
        assertArrayEquals("triple slash should be fine", new String[] { "f1.txt" }, target.directChildren( "a///"));
        assertArrayEquals("prefix and double slash should be fine", new String[] { "f1.txt" }, target.directChildren("///a///"));
    }

    @Test
    public void makeIntermediateDirectories() throws IOException {
        target.addFile("a/b/c/d.txt", new FSEntry(2, 2), FSEntry::new);
        target.addFile("/a/b/c/d2.txt", new FSEntry(3, 3), FSEntry::new);
        assertArrayEquals(new String[] { "c" }, target.directChildren("a/b"));
        assertArrayEquals(new String[] { "d.txt", "d2.txt" }, sorted(target.directChildren("a/b/c")));
    }

    @Test
    public void makeNewDirectories() throws IOException {
        target.addDirectory("/b/c", new FSEntry(2, 2), FSEntry::new);
        assertArrayEquals(new String[] { "b" }, target.directChildren("/"));
        assertArrayEquals(new String[] { "c" }, target.directChildren("/b"));
        target.addDirectory("d/e/", new FSEntry(2, 2), FSEntry::new);
        assertArrayEquals(new String[] { "b", "d" }, sorted(target.directChildren("/")));
        assertArrayEquals(new String[] { "e" }, target.directChildren("/d"));
    }

    private static String[] sorted(String [] input) {
        Arrays.sort(input);
        return input;
    }
    
}
