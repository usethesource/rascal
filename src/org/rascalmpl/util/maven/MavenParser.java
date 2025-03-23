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

import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.maven.model.Model;
import org.apache.maven.model.building.DefaultModelBuilderFactory;
import org.apache.maven.model.building.DefaultModelBuildingRequest;
import org.apache.maven.model.building.ModelBuilder;
import org.apache.maven.model.building.ModelBuildingException;
import org.apache.maven.model.building.ModelBuildingRequest;
import org.apache.maven.model.building.ModelBuildingResult;
import org.apache.maven.model.building.ModelCache;
import org.apache.maven.model.building.ModelProblem;
import org.apache.maven.model.building.ModelSource;
import org.apache.maven.model.building.ModelSource2;
import org.apache.maven.model.resolution.ModelResolver;
import org.apache.maven.model.resolution.UnresolvableModelException;
import org.apache.maven.settings.Settings;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.rascalmpl.library.Messages;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.values.IRascalValueFactory;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import io.usethesource.vallang.IListWriter;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValueFactory;

public class MavenParser {
    private static final IValueFactory VF = IRascalValueFactory.getInstance();

    private final Settings settings;
    private final Path projectPom;
    private final ISourceLocation projectPomLocation;
    private final ModelBuilder builder;
    private final HttpClient httpClient;
    private final ModelCache modelCache;
    private final Path rootMavenRepo;

    public MavenParser(Settings settings, Path projectPom) {
        this(settings, projectPom, Util.mavenRepository(settings));
    }

    /*package*/ MavenParser(Settings settings, Path projectPom, Path rootMavenRepo) {
        this.settings = settings;
        this.projectPom = projectPom;
        this.rootMavenRepo = rootMavenRepo;
        try {
            this.projectPomLocation = URIUtil.createFileLocation(projectPom);
        }
        catch (URISyntaxException e) {
            throw new IllegalArgumentException("Project pom is an illegal path", e);
        }

        builder = new DefaultModelBuilderFactory().newInstance();
        httpClient = HttpClient.newBuilder()
            .version(Version.HTTP_2) // upgrade where possible
            .connectTimeout(Duration.ofSeconds(10)) // don't wait longer than 10s to connect to a repo
            .build();
        modelCache = new CaffeineModelCache();
    }

    public Artifact parseProject() throws ModelResolutionError {
        var request = new DefaultModelBuildingRequest()
            .setPomFile(projectPom.toFile())
            .setValidationLevel(ModelBuildingRequest.VALIDATION_LEVEL_MAVEN_3_0); // TODO: figure out if we need this

        var resolver = new SimpleResolver(settings, rootMavenRepo, builder, httpClient);
        var messages = VF.listWriter();

        var model = getBestModel(projectPomLocation, request, resolver, messages);
        if (model == null) {
            throw new ModelResolutionError(messages);
        }

        var result = Artifact.build(model, true, projectPom, projectPomLocation, "", Collections.emptySet(), messages, resolver);
        if (result == null) {
            return Artifact.unresolved(new ArtifactCoordinate(model.getGroupId(), model.getArtifactId(), model.getVersion(), ""), messages);
        }
        return result;
    }

    /*package*/ @Nullable Artifact parseArtifact(ArtifactCoordinate coordinate, Set<ArtifactCoordinate.WithoutVersion> exclusions, SimpleResolver originalResolver) {
        var messages = VF.listWriter();
        try {
            var modelSource = originalResolver.resolveModel(coordinate);
            var pomLocation = calculateLocation(modelSource);
            var pomPath = Path.of(pomLocation.getURI());

            var resolver = new SimpleResolver(settings, rootMavenRepo, builder, httpClient);
            // we need to use the original resolver to be able to resolve parent poms
            var workspaceResolver = new SimpleWorkspaceResolver(originalResolver, builder, this);
            
            var request = new DefaultModelBuildingRequest()
                .setModelSource(modelSource)
                .setWorkspaceModelResolver(workspaceResolver); // only for repository poms do we setup this extra resolver to help find parent poms

            var model = getBestModel(pomLocation, request, resolver, messages);
            if (model == null) {
                return Artifact.unresolved(coordinate, messages);
            }
            return Artifact.build(model, false, pomPath, pomLocation, coordinate.getClassifier(), exclusions, messages, resolver);
        } catch (UnresolvableModelException e) {
            return Artifact.unresolved(coordinate, messages);
        }
    }

    private static ISourceLocation calculateLocation(ModelSource source) {
        try {
            URI loc;
            if (source instanceof ModelSource2) {
                loc = ((ModelSource2)source).getLocationURI();
            }
            else {
                loc = new URI(source.getLocation());
            }
            return VF.sourceLocation(URIUtil.fixUnicode(loc));
        }
        catch (URISyntaxException e) {
            return URIUtil.unknownLocation();
        }
    }


    private @Nullable Model getBestModel(ISourceLocation pom, ModelBuildingRequest request, ModelResolver resolver, IListWriter messages) {
        try {
            var result = buildModel(request, resolver);
            translateProblems(result.getProblems(), pom, messages);
            return result.getEffectiveModel();
        } catch (ModelBuildingException be) {
            translateProblems(be.getProblems(), pom, messages);
            return be.getModel();
        } 
    }

    private static void translateProblems(List<ModelProblem> problems, ISourceLocation loc, IListWriter messages) {
        for (var problem : problems) {
            // TODO: figure out how we can get correct offset & length from the xml parser (right now it has line & column, but they're always 0)
            var message = problem.getMessage();
            switch (problem.getSeverity()) {
                case ERROR: // fall through
                case FATAL: messages.append(Messages.error(message, loc)); break;
                case WARNING: messages.append(Messages.warning(message, loc)); break;
                default: throw new UnsupportedOperationException("Missing case: " + problem.getSeverity());
            }
        }
    }

    public Model buildEffectiveModel(ModelBuildingRequest request, ModelResolver resolver) throws ModelBuildingException {
        return buildModel(request, resolver).getEffectiveModel();
    }


    private ModelBuildingResult buildModel(ModelBuildingRequest request, ModelResolver resolver) throws ModelBuildingException {
        request.setModelResolver(resolver)
            .setModelCache(modelCache)
            .setSystemProperties(System.getProperties());
        return builder.build(request);
    }


    private static final class CaffeineModelCache implements ModelCache {
        private static final class Key {
            private final String groupId; 
            private final String artifactId;
            private final String version;
            private final String tag;

            public Key(String groupId, String artifactId, String version, String tag) {
                this.groupId = groupId;
                this.artifactId = artifactId;
                this.version = version;
                this.tag = tag;
            }

            @Override
            public int hashCode() {
                return groupId.hashCode()
                    + (artifactId.hashCode() * 7)
                    + (version.hashCode() * 11)
                    + (tag.hashCode() * 13)
                    ;
            }

            @Override
            public boolean equals(Object obj) {
                if (this == obj)
                    return true;
                if (!(obj instanceof Key))
                    return false;
                Key other = (Key) obj;
                return groupId.equals(other.groupId)
                    && artifactId.equals(other.artifactId)
                    && version.equals(other.version)
                    && tag.equals(other.tag);
            }
        }

        private final Cache<Key, Object> modelCache = Caffeine.newBuilder()
            .maximumSize(100)
            .build();


        @Override
        public void put(String groupId, String artifactId, String version, String tag, Object data) {
            modelCache.put(new Key(groupId, artifactId, version, tag), data);
        }

        @Override
        public Object get(String groupId, String artifactId, String version, String tag) {
            return modelCache.getIfPresent(new Key(groupId, artifactId, version, tag));
        }
    }
    

    private static void test(Path target) throws ModelResolutionError {
        var start = System.currentTimeMillis();
        var parser = new MavenParser(new Settings(), target);
        var project = parser.parseProject();
        var stop = System.currentTimeMillis();
        var out = new PrintWriter(System.out);
        project.report(out);
        out.printf("It took %d ms to resolve root artifact%n", stop - start);
        start = System.currentTimeMillis();
        var deps =project.resolveDependencies(Scope.COMPILE, parser);
        stop = System.currentTimeMillis();
        out.println(deps);
        out.printf("It took %d ms to resolve dependencies%n", stop - start);
        out.flush();
    }

    public static void main(String[] args) {
        try {
            test(Path.of("C:/Users/Davy/swat.engineering/rascal/rascal/test/org/rascalmpl/util/maven/poms/multi-module/example-core/pom.xml"));

            System.out.println("******");
            System.out.println("******");
            System.out.println("******");

            test(Path.of("pom.xml").toAbsolutePath());
        } catch (Throwable t) {
            System.err.println("Caught: " +t);
            t.printStackTrace();
        }
    }

}
