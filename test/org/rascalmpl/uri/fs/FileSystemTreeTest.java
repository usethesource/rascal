package org.rascalmpl.uri.fs;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

public class FileSystemTreeTest {
    private FileSystemTree<FSEntry> target;
    private long currentTime = 0;

    @Before
    public void initTarget() throws IOException {
        target = new FileSystemTree<FSEntry>(new FSEntry(0, 0));
        currentTime = 0;
    }

    private void addFile(String name) throws IOException {
        addFile(name, currentTime, currentTime);
        currentTime++;
    }
    private void addFile(String name, long created, long lastModified) throws IOException {
        target.addFile(name, new FSEntry(created, lastModified), FSEntry::new);
    }
    private void addDirectory(String name) throws IOException {
        addDirectory(name, currentTime, currentTime);
        currentTime++;
    }
    private void addDirectory(String name, long created, long lastModified) throws IOException {
        target.addDirectory(name, new FSEntry(created, lastModified), FSEntry::new);
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

    private static String[] sorted(String [] input) {
        Arrays.sort(input);
        return input;
    }
    
}
