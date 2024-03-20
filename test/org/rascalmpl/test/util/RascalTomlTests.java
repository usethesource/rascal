package org.rascalmpl.test.util;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringReader;
import java.util.Collections;

import org.junit.Test;
import org.rascalmpl.interpreter.utils.RascalToml;

public class RascalTomlTests {
    
    private static RascalToml parse(String s) throws IOException {
        System.out.println(s);
        return RascalToml.parse(new StringReader(s));
    }

    @Test
    public void parserWorks() throws IOException {
        var toml = parse(
            "version='1.0' \n" +

            "[project]\n" +
            "name = 'test-project'\n" +
            "sources = [ 'src/main/rascal']\n" +
            "libraries = [ '|lib://typepal|']\n" +

            "[main]\n" +
            "module = 'lang::testing::Mod'\n" +
            "function = 'main'\n"
        );
        assertEquals("1.0", toml.getVersion());
        assertEquals("test-project", toml.getProjectName());
        assertEquals(Collections.singletonList("src/main/rascal"), toml.getSource());
        assertEquals(Collections.singletonList("|lib://typepal|"), toml.getRequireLibraries());
    }
}
