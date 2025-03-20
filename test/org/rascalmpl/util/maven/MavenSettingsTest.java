/*
 * Copyright (c) 2025, Swat.engineering All rights reserved. Redistribution and use in source and
 * binary forms, with or without modification, are permitted provided that the following conditions
 * are met: 1. Redistributions of source code must retain the above copyright notice, this list of
 * conditions and the following disclaimer. 2. Redistributions in binary form must reproduce the
 * above copyright notice, this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT
 * HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED
 * TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.rascalmpl.util.maven;

import java.nio.file.Path;

import org.junit.After;
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

    @After
    public void cleanupEnv() {
        System.setProperty("user.home", originalHome);
    }    

    @Test
    public void testMavenSettings() throws Exception {
        Path repo = Util.mavenRepository();
        Assert.assertEquals("HelloWorld", repo.toString());
    }

}
