package org.rascalmpl.interpreter.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Collections;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;

import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.toml.TomlMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

import io.usethesource.vallang.ISourceLocation;

public class RascalToml {
    private final String version;

    private final Project project;
    private final @Nullable Main main;


    private static class Project {
        private final String name;
        private final List<String> sources;
        private final @Nullable List<String> libraries;

        private Project(String name, @Nullable List<String> sources, @Nullable List<String> libraries) {
            this.name = name;
            this.sources = (sources == null || sources.isEmpty()) ? Collections.singletonList("src") : Collections.unmodifiableList(sources);
            this.libraries = (libraries == null || libraries.isEmpty()) ? null : Collections.unmodifiableList(libraries);
        }
    }

    private static class Main {
        private final @Nullable String module;
        private final @Nullable String function;

        private Main(@Nullable String module, @Nullable String function) {
            this.module = module;
            this.function = function;
        }
    }

    
    private RascalToml(String version, Project project, @Nullable Main main) {
        this.version = version;
        this.project = project;
        this.main = main;
    }

    public String getVersion() {
        return version;
    }

    public @Nullable String getProjectName() {
        return project.name;
    }

    public @Nullable String getMainModule() {
        return main.module;
    }

    public @Nullable String getMainFunction() {
        return main.function;
    }

    public List<String> getSource() {
        return project.sources;
    }

    public @Nullable List<String> getRequireLibraries() {
        return project.libraries;
    }

    public static RascalToml parse(JarInputStream from) throws IOException {
        return parse(tomlFile(from));
    }

    public static RascalToml parse(Class<?> from) throws IOException {
        return parse(tomlFile(from));
    }

    public static RascalToml parse(File from) throws IOException {
        return parse(tomlFile(from));
    }
    
    public static RascalToml parse(ISourceLocation from) throws IOException {
        return parse(tomlFile(from));
    }

    private static ObjectReader tomlMapper = new TomlMapper()
        .registerModule(new ParameterNamesModule())
        .readerFor(RascalToml.class);

    public static RascalToml parse(Reader from) throws IOException {
        return tomlMapper.readValue(from);
    }

    public static RascalToml parse(InputStream from) throws IOException {
        return tomlMapper.readValue(from);
    }

    private static final String TOML_LOCATION = "META_INF/rascal.toml";

    private static InputStream tomlFile(JarInputStream stream) throws IOException {
        try {
            JarEntry next = null;
            while ((next = stream.getNextJarEntry()) != null) {
                if (next.getName().equals(TOML_LOCATION)) {
                    return stream; // the stream now behaves as an input stream for this file entry
                }
            }
            throw new IOException("Could not find: " + TOML_LOCATION);
        } catch (IOException e) {
            throw new IOException("Error iterating jar for " + TOML_LOCATION, e);
        }
    }

    private static InputStream tomlFile(File jarFile) throws IOException {
        try (var file = new JarFile(jarFile)) {
            return file.getInputStream(new ZipEntry(TOML_LOCATION));
        }
        catch (IOException e) {
            throw new IOException("Error loading " + TOML_LOCATION +" from " + jarFile, e);
        }
    }

    private static InputStream tomlFile(Class<?> clazz) throws IOException {
        var result = clazz.getResourceAsStream("/" + TOML_LOCATION);
        if (result == null) {
            throw new IOException("Error loading " + TOML_LOCATION + " from " + clazz);
        }
        return result;
    }

    private static Reader tomlFile(ISourceLocation root) throws IOException {
        try {
            return URIResolverRegistry.getInstance()
                .getCharacterReader(URIUtil.getChildLocation(RascalManifest.jarify(root), TOML_LOCATION));
        } catch (IOException e) {
            throw new IOException("Error loading " + TOML_LOCATION +" from " + root, e);
        }
    }

}
