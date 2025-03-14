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
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import org.apache.maven.settings.Settings;
import org.apache.maven.settings.io.xpp3.SettingsXpp3Reader;
import org.codehaus.plexus.interpolation.EnvarBasedValueSource;
import org.codehaus.plexus.interpolation.InterpolationException;
import org.codehaus.plexus.interpolation.PropertiesBasedValueSource;
import org.codehaus.plexus.interpolation.RegexBasedInterpolator;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

/* package */ class Util {
    static Path mavenRepository() {
        String repoProp = System.getProperty("maven.repo.local");
        if (repoProp != null) {
            return Path.of(repoProp);
        }

        Path userHome = Path.of(System.getProperty("user.home"));
        Path userSettingsPath = userHome.resolve(".m2").resolve("settings.xml");
        Optional<Path> repo = getRepoFromSettings(userSettingsPath);
        if (repo.isPresent()) {
            return repo.get();
        }

        Optional<Path> mavenHome = findMavenHome();
        if (mavenHome.isPresent()) {
            Path globalSettingsPath = mavenHome.get().resolve("conf").resolve("settings.xml");
            repo = getRepoFromSettings(globalSettingsPath);
            if (repo.isPresent()) {
                return repo.get();
            }
        }

        return userHome.resolve(".m2").resolve("repository");
    }

    private static Optional<Path> findMavenHome() {
        // Traverse the PATH environment variable and locate mvn (or mvn.cmd on Windows)
        String path = System.getenv("PATH");
        if (path == null) {
            return Optional.empty();
        }

        for (String dirname : System.getenv("PATH").split(File.pathSeparator)) {
            Path dir = Path.of(dirname);
            Optional<Path> mvnHome = resolveMavenHome(dir.resolve("mvn.cmd"));
            if (mvnHome.isPresent()) {
                return mvnHome;
            }

            mvnHome = resolveMavenHome(dir.resolve("mvn"));
            if (mvnHome.isPresent()) {
                return mvnHome;
            }
        }

        return Optional.empty();
    }

    private static Optional<Path> resolveMavenHome(Path executable) {
        if (Files.exists(executable) && Files.isExecutable(executable)) {
            try {
                return Optional.of(executable.toRealPath().getParent().getParent());
            }
            catch (IOException e) {
                // Ignore
            }
        }

        return Optional.empty();
    }
    
    private static Optional<Settings> readSettings(Path settingsXmlFile) {
        if (Files.exists(settingsXmlFile)) {
            try (FileReader fileReader = new FileReader(settingsXmlFile.toFile())) {
                SettingsXpp3Reader reader = new SettingsXpp3Reader();
                return Optional.of(reader.read(fileReader));
            }
            catch (XmlPullParserException | IOException e) {
                // Could not read settings.xml, fallback to hard-coded path below
            }
        }

        return Optional.empty();
    }

    private static Optional<Path> getRepoFromSettings(Path settingsXmlFile) {
        Optional<Settings> settings = readSettings(settingsXmlFile);
        if (settings.isPresent()) {
            String localRepo = settings.get().getLocalRepository();
            if (localRepo != null) {
                return Optional.of(Path.of(replaceVariables(localRepo)));
            }
        }

        return Optional.empty();
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
