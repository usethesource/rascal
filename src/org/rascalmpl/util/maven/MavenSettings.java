package org.rascalmpl.util.maven;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import org.apache.maven.settings.Mirror;
import org.apache.maven.settings.Settings;
import org.apache.maven.settings.io.xpp3.SettingsXpp3Reader;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.codehaus.plexus.interpolation.EnvarBasedValueSource;
import org.codehaus.plexus.interpolation.InterpolationException;
import org.codehaus.plexus.interpolation.PropertiesBasedValueSource;
import org.codehaus.plexus.interpolation.RegexBasedInterpolator;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

/**
 *  A wrapper around Maven's Setting class to only expose the properties we need and to properly implement defaulting:
 * - First try a system property if one is associated with the setting property (e.g. maven.repo.local for localRepository)
 * - If not found, try the user settings
 * - If not found, try system settings
 * - If not found, return a hardcoded default
 */
public class MavenSettings {
    public static Path mavenRepository() {
        return readSettings().getLocalRepository();
    }

    static MavenSettings readSettings() {
        Path userHome = Path.of(System.getProperty("user.home"));
        Path userSettings = userHome.resolve(".m2").resolve("settings.xml");

        Path globalSettings = null;
        Path mavenHome = findMavenHome();
        if (mavenHome != null) {
            globalSettings = mavenHome.resolve("conf").resolve("settings.xml");
        }

        return new MavenSettings(readSettingsFile(userSettings), readSettingsFile(globalSettings));
    }

    private final @Nullable Settings userSettings;
    private final @Nullable Settings systemSettings;

    MavenSettings(@Nullable Settings userSettings, @Nullable Settings systemSettings) {
        this.userSettings = userSettings;
        this.systemSettings = systemSettings;
    }

    /**
     * Empty settings for testing
     */
    MavenSettings() {
        this(null, null);
    }

    Path getLocalRepository() {
        String repoProp = System.getProperty("maven.repo.local");
        if (repoProp != null) {
            return Path.of(repoProp);
        }

        if (userSettings != null && userSettings.getLocalRepository() != null) {
            return Path.of(replaceVariables(userSettings.getLocalRepository()));
         }

         if (systemSettings != null && systemSettings.getLocalRepository() != null) {
             return Path.of(replaceVariables(systemSettings.getLocalRepository()));
         }

        Path userHome = Path.of(System.getProperty("user.home"));
        return userHome.resolve(".m2").resolve("repository");
    }

    Map<String, Mirror> getMirrors() {
        Map<String, Mirror> mirrors = new HashMap<>();;

        if (systemSettings != null) {
            for (Mirror mirror : systemSettings.getMirrors()) {
                mirrors.put(mirror.getMirrorOf(), mirror);
            }
        }

        // Mirrors in userSettings override ones defined in systemSettings
        if (userSettings != null) {
            for (Mirror mirror : userSettings.getMirrors()) {
                mirrors.put(mirror.getMirrorOf(), mirror);
            }
        }

        return mirrors;
    }

    private static String replaceVariables(String input) {
        var interpolator = new RegexBasedInterpolator();
 
        interpolator.addValueSource(new PropertiesBasedValueSource(System.getProperties()));
        try {
            interpolator.addValueSource(new EnvarBasedValueSource(false));
        } catch (IOException ignored) {
            // No environment variables if this fails
        }
        
        try {
            return interpolator.interpolate(input);
        }
        catch (InterpolationException e) {
            return input;
        }
    }

    private static @Nullable Settings readSettingsFile(Path settingsXmlFile) {
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


}
