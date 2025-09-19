package org.rascalmpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.rascalmpl.library.util.PathConfig;
import org.rascalmpl.values.IRascalValueFactory;

public class PathConfigTest {
    private static final String PCFG_S = "pathConfig("+
        "ignores=[|file:///path|],"+
        "resources=[|file:///path|],"+
        "bin=|file:///c:/path/to/rascal-language-servers/rascal-lsp/target/classes|,"+
        "messages=[warning(\"\'build.plugins.plugin.version\' for org.codehaus.gmavenplus:gmavenplus-plugin is missing.\",|file:///C:/path/to/.m2/repository/org/apache/logging/log4j/log4j-core/2.25.1/log4j-core-2.25.1.pom|)],"+
        "libs=[|jar+file:///C:/path/to/.m2/repository/org/rascalmpl/rascal/0.41.0-RC63-SNAPSHOT/rascal-0.41.0-RC63-SNAPSHOT.jar!/|,"+
              "|file:///C:/path/to/.m2/repository/org/rascalmpl/rascal/0.41.0-RC63-SNAPSHOT/rascal-0.41.0-RC63-SNAPSHOT.jar|,"+
              "|mvn://org.eclipse.lsp4j--org.eclipse.lsp4j--0.24.0|,"+
              "|mvn://org.apache.logging.log4j--log4j-core--2.25.1|,"+
              "|mvn://org.apache.logging.log4j--log4j-api--2.25.1|,"+
              "|mvn://org.apache.logging.log4j--log4j-iostreams--2.25.1|,"+
              "|mvn://org.apache.logging.log4j--log4j-jul--2.25.1|,"+
              "|mvn://org.apache.logging.log4j--log4j-layout-template-json--2.25.1|,"+
              "|mvn://org.eclipse.lsp4j--org.eclipse.lsp4j.debug--0.24.0|,"+
              "|mvn://org.eclipse.lsp4j--org.eclipse.lsp4j.jsonrpc--0.24.0|,"+
              "|mvn://org.eclipse.lsp4j--org.eclipse.lsp4j.jsonrpc.debug--0.24.0|,"+
              "|mvn://com.google.code.gson--gson--2.13.2|,"+
              "|mvn://com.google.errorprone--error_prone_annotations--2.41.0|],"+
        "srcs=[|jar+file:///C:/path/to/.m2/repository/org/rascalmpl/rascal/0.41.0-RC63-SNAPSHOT/rascal-0.41.0-RC63-SNAPSHOT.jar!/org/rascalmpl/compiler|,|jar+file:///C:/path/to/.m2/repository/org/rascalmpl/rascal/0.41.0-RC63-SNAPSHOT/rascal-0.41.0-RC63-SNAPSHOT.jar!/org/rascalmpl/typepal|,|file:///c:/path/to/swat/projects/Rascal/rascal-language-servers/rascal-lsp/src|],"+
        "projectRoot=|file:///c:/path/to/swat/projects/Rascal/rascal-language-servers/rascal-lsp|)";

    private PathConfig pcfg;
    
    @Before
    public void setUp() throws IOException {
        pcfg = PathConfig.parse(PCFG_S);
    }

    @Test
    public void parse() {
        assertEquals(1, pcfg.getIgnores().size());
        assertEquals(1, pcfg.getResources().size());
        assertEquals("file", pcfg.getBin().getScheme());
        assertEquals(1, pcfg.getMessages().size());
        assertEquals(13, pcfg.getLibs().size());
        assertEquals(3, pcfg.getSrcs().size());
        assertEquals("file", pcfg.getProjectRoot().getScheme());
    }

    @Test
    public void instanceEquals() {
        assertEquals(pcfg, pcfg);
    }

    @Test
    public void parsedEquals() throws IOException {
        assertEquals(pcfg, PathConfig.parse(PCFG_S));
    }

    @Test
    public void modifiedNotEquals() throws IOException {
        var modPcfg = PathConfig.parse(PCFG_S);
        modPcfg = modPcfg.addSourceLoc(IRascalValueFactory.getInstance().sourceLocation("unknown:///"));
        assertNotEquals(pcfg, modPcfg);
    }
}
