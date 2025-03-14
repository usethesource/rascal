package org.rascalmpl.util.maven;

import java.nio.file.Path;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MavenSettingsTest {
    private static final String TEST_SYSTEM_PROP = "TEST_SYSTEM_PROP";
    private static final String TEST_SYSTEM_PROP_VALUE = "HelloWorld";

    private String originalHome;

    @Before
    public void setupEnv() throws Exception {
        // Set "user.home" system property so we can read the test settings.xml
        Path cwd = Path.of(System.getProperty("user.dir"));
        Path testM2 = cwd.resolve(Path.of( "test", "org", "rascalmpl", "util", "maven", "testhomedir"));
        originalHome = System.getProperty("user.home");
        System.setProperty("user.home", testM2.toString());

        // Set a test property so we can check if the settings.xml was read and replacements succeeded
        System.setProperty(TEST_SYSTEM_PROP, TEST_SYSTEM_PROP_VALUE);
    }

    public void cleanupEnv() {
        System.setProperty("user.home", originalHome);
    }    

    @Test
    public void testMavenSettings() throws Exception {
        Path repo = Util.mavenRepository();
        Assert.assertEquals("HelloWorld", repo.toString());
    }

}
