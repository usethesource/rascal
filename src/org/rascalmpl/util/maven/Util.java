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
package org.rascalmpl.util.maven;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.maven.settings.Settings;
import org.apache.maven.settings.io.xpp3.SettingsXpp3Reader;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.codehaus.plexus.interpolation.EnvarBasedValueSource;
import org.codehaus.plexus.interpolation.InterpolationException;
import org.codehaus.plexus.interpolation.PropertiesBasedValueSource;
import org.codehaus.plexus.interpolation.RegexBasedInterpolator;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

public class Util {
    public static Path mavenRepository() {
        String repoProp = System.getProperty("maven.repo.local");
        if (repoProp != null) {
            return Path.of(repoProp);
        }

        Path userHome = Path.of(System.getProperty("user.home"));
        Path userSettingsPath = userHome.resolve(".m2").resolve("settings.xml");
        Path repo = getRepoFromSettings(userSettingsPath);
        if (repo != null) {
            return repo;
        }

        Path mavenHome = findMavenHome();
        if (mavenHome != null) {
            Path globalSettingsPath = mavenHome.resolve("conf").resolve("settings.xml");
            repo = getRepoFromSettings(globalSettingsPath);
            if (repo != null) {
                return repo;
            }
        }

        return userHome.resolve(".m2").resolve("repository");
    }

    private static @Nullable Path findMavenHome() {
        // Traverse the PATH environment variable and locate mvn (or mvn.cmd on Windows)
        String path = System.getenv("PATH");
        if (path == null) {
            return null;
        }

        for (String dirname : path.split(File.pathSeparator)) {
            Path dir = Path.of(dirname);
            Path mvnHome = resolveMavenHome(dir.resolve("mvn.cmd"));
            if (mvnHome != null) {
                return mvnHome;
            }

            mvnHome = resolveMavenHome(dir.resolve("mvn"));
            if (mvnHome != null) {
                return mvnHome;
            }
        }

        return null;
    }

    private static @Nullable Path resolveMavenHome(Path executable) {
        if (Files.isExecutable(executable)) {
            try {
                // Navigate from bin/mvn to the root of the Maven installation
                return executable.toRealPath().getParent().getParent();
            }
            catch (IOException e) {
                // Ignore
            }
        }

        return null;
    }
    
    private static @Nullable Settings readSettings(Path settingsXmlFile) {
        if (Files.exists(settingsXmlFile)) {
            try (var input = Files.newInputStream(settingsXmlFile)) {
                return new SettingsXpp3Reader().read(input);
            }
            catch (XmlPullParserException | IOException e) {
                // Could not read settings.xml, settings will not be used
            }
        }

        return null;
    }

    private static @Nullable Path getRepoFromSettings(Path settingsXmlFile) {
        Settings settings = readSettings(settingsXmlFile);
        if (settings != null) {
            String localRepo = settings.getLocalRepository();
            if (localRepo != null) {
                return Path.of(replaceVariables(localRepo));
            }
        }

        return null;
    }

    private static String replaceVariables(String input) {
        RegexBasedInterpolator interpolator = new RegexBasedInterpolator();
 
        interpolator.addValueSource(new PropertiesBasedValueSource(System.getProperties()));
        try {
            interpolator.addValueSource(new EnvarBasedValueSource(false));
        } catch (IOException e) {
            // No environment variables if this fails
        }
        
        try {
            return interpolator.interpolate(input);
        }
        catch (InterpolationException e) {
            return input;
        }
    }


    private Util() {}
}
