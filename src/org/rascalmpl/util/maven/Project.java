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
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;
import org.apache.maven.model.building.DefaultModelBuilderFactory;
import org.apache.maven.model.building.DefaultModelBuildingRequest;
import org.apache.maven.model.building.ModelBuildingException;
import org.apache.maven.model.building.ModelBuildingRequest;
import org.apache.maven.model.building.ModelBuildingResult;
import org.apache.maven.model.building.ModelProblem;
import org.apache.maven.model.building.ModelSource;
import org.apache.maven.model.building.ModelSource2;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.rascalmpl.library.Messages;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.values.IRascalValueFactory;

import io.usethesource.vallang.IList;
import io.usethesource.vallang.IListWriter;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValueFactory;

// fix: maybe project should not exist, and it's always an artifact?
/**
 * Represents a maven project identified with a pom.xml
 */
public class Project {
    private static final IValueFactory VF = IRascalValueFactory.getInstance();

    private final ArtifactCoordinate coordinate;
    private final @Nullable ArtifactCoordinate parentCoordinate;
    private final IList messages;
    private final boolean errors;
    private final List<Dependency> dependencies;
    private final CurrentResolution resolutionCache;
    

    public ArtifactCoordinate getCoordinate() {
        return coordinate;
    }

    public @Nullable ArtifactCoordinate getParentCoordinate() {
        return parentCoordinate;
    }

    /**
     * During the parsing of the project pom, we found some errors. We still tried to build as good of a model as we could
     * @return
     */
    public boolean hasErrors() {
        return errors;
    }

    public IList getMessages() {
        return messages;
    }

    private Project(ArtifactCoordinate coordinate, @Nullable ArtifactCoordinate parentCoordinate,
        List<Dependency> dependencies, IList messages, boolean errors, CurrentResolution context) {
        this.coordinate = coordinate;
        this.parentCoordinate = parentCoordinate;
        this.dependencies = dependencies;
        this.messages = messages;
        this.errors = errors;
        this.resolutionCache = context;
    }

    /*package for testing*/ static Project parseProjectPom(Path pomFile, Path mavenRoot) {
        var modelBuilder = new DefaultModelBuilderFactory().newInstance();
        var request = new DefaultModelBuildingRequest()
            .setPomFile(pomFile.toFile())
            .setValidationLevel(ModelBuildingRequest.VALIDATION_LEVEL_MAVEN_3_0); // TODO: figure out if we need this
        var httpClient = HttpClient.newBuilder()
            .version(Version.HTTP_2) // upgrade where possible
            .connectTimeout(Duration.ofSeconds(10)) // don't wait longer than 10s to connect to a repo
            .build();
        var context = new CurrentResolution(modelBuilder, new HashMap<>(), makeLocation(pomFile), httpClient, mavenRoot);
        return build(request, context, true);
    }

    /**
     * We have to have a file name to a pom.xml in a project, it does not work for .pom files in a maven repository.
     */
    public static Project parseProjectPom(Path pomFile) {
        return parseProjectPom(pomFile, Util.mavenRepository());
    }

    /*package*/ static ModelBuildingResult buildModel(ModelBuildingRequest request, CurrentResolution context) throws ModelBuildingException {
        request.setModelResolver(context.resolver)
            .setModelCache(context.modelCache)
            .setSystemProperties(System.getProperties());
        return context.builder.build(request);
    }

    private static Project build(ModelBuildingRequest request, CurrentResolution context, boolean isRoot) {
        try {
            return translate(buildModel(request, context), context, isRoot);
        } catch (ModelBuildingException be) {
            return buildError(be, context, isRoot);
        }
    }


    /*package*/ static @Nullable Model parseRepositoryPom(ModelSource resolvedEntry, CurrentResolution context) {
        var request = new DefaultModelBuildingRequest()
            .setModelSource(resolvedEntry)
            .setWorkspaceModelResolver(context.workspaceResolver); // only for repository poms do we setup this extra resolver to help find parent poms
        URI loc;
        if (resolvedEntry instanceof ModelSource2) {
            loc = ((ModelSource2)resolvedEntry).getLocationURI();
        }
        else {
            try {
                loc = new URI(resolvedEntry.getLocation());
            }
            catch (URISyntaxException e) {
                loc = URIUtil.unknownLocation().getURI();
            }
        }
        try {
            return buildModel(request, context).getEffectiveModel();
        }
        catch (ModelBuildingException be) {
            return be.getModel();
        }
    }

    private static ISourceLocation makeLocation(Path pomFile) {
        try {
            return URIUtil.createFileLocation(pomFile.toAbsolutePath());
        }
        catch (URISyntaxException e) {
            return URIUtil.unknownLocation();
        }
    }

    private static Project translate(ModelBuildingResult success, CurrentResolution context, boolean isRoot) {
        var messages = translateProblems(success.getProblems(), context.pom);
        return translate(success.getEffectiveModel(), messages, false, context, isRoot);
    }

    private static Project translate(Model m, IListWriter messages, boolean errors, CurrentResolution context, boolean isRoot) {
        return new Project(
            translateCoordinate(m),
            translateCoordinate(m.getParent()),
            translateDependencies(m, messages, context, isRoot),
            messages.done(),
            errors, 
            context
        );
    }

    private static IListWriter translateProblems(List<ModelProblem> problems, ISourceLocation loc) {
        var messages = VF.listWriter();
        for (var problem : problems) {
            var pos = loc;
            if (problem.getLineNumber() >= 0) {
                pos = VF.sourceLocation(pos, 0,0, problem.getLineNumber(),  problem.getLineNumber(), problem.getColumnNumber(), problem.getColumnNumber() + 1);
            }
            var message = problem.getMessage();
            switch (problem.getSeverity()) {
                case ERROR: // fall through
                case FATAL: messages.append(Messages.error(message, pos)); break;
                case WARNING: messages.append(Messages.warning(message, pos)); break;
                default: throw new UnsupportedOperationException("Missing case: " + problem.getSeverity());
            }
        }
        return messages;
    }

    private static Project buildError(ModelBuildingException be, CurrentResolution context,  boolean isRoot) {
        var messages = translateProblems(be.getProblems(), context.pom);
        var incompleteModel = be.getModel();
        if (incompleteModel == null) {
            messages.append(Messages.error("Could not build an intermediate model", context.pom));
            return new Project(ArtifactCoordinate.UNKNOWN, null, Collections.emptyList(), messages.done(), true, context);
        }
        return translate(incompleteModel, messages, true, context, isRoot);
    }

    private static ArtifactCoordinate translateCoordinate(Model model) {
        return new ArtifactCoordinate(model.getGroupId(), model.getArtifactId(), model.getVersion(), "");
    }

    private static ArtifactCoordinate translateCoordinate(Parent model) {
        if (model == null || model.getArtifactId() == null || model.getArtifactId().isEmpty()) {
            return null;
        }
        return new ArtifactCoordinate(model.getGroupId(), model.getArtifactId(), model.getVersion(), "");
    }

    private static List<Dependency> translateDependencies(Model model, IListWriter messages, CurrentResolution context, boolean isRoot) {
        return model.getDependencies()
            .stream()
            .filter(d -> isRoot || !"test".equals(d.getScope())) // unless we're the root, we don't care about downstream test dependencies
            .map(d -> Dependency.build(d, messages, context))
            .collect(Collectors.toUnmodifiableList())
            ;
    }

    @Override
    public String toString() {
        var result = new StringWriter();
        try (var printer = new PrintWriter(result)) {
            write(printer);
        }
        return result.toString();
    }

    public void write(PrintWriter target) {
        target.print("Maven project: ");
        target.println(coordinate);
        target.print("Parent: ");
        target.println(parentCoordinate);
        target.println("Dependencies:");
        for (var d : dependencies) {
            target.print("- ");
            target.println(d);
        }
        target.println("Messages:");
        Messages.write(messages, target);
    }

    /**
     * Resolve maven classpath based on the algorithm that maven uses. This will take the dependancies, 
     * resolves them and calculates their dependencies. Untill we've resolved all dependencies related for this scope.
     * @see {@link https://maven.apache.org/guides/introduction/introduction-to-dependency-mechanism.html} 
     * @param forScope for which scope, note that interpreter shouldn't use {@link Scope#RUNTIME} but use {@link Scope#COMPILE}.
     * @return a list of class path entries of the dependencies of this project
     */
    public List<Artifact> resolveArtifacts(Scope forScope) {
        var alreadyIncluded = new HashSet<ArtifactCoordinate.WithoutVersion>();
        var result = new ArrayList<Artifact>(dependencies.size());
        calculateClassPath(forScope, alreadyIncluded, result, dependencies);
        return result;
    }

    /**
     * First add all dependencies at the current level, and then for all added, go through their dependencies
     */
    private void calculateClassPath(Scope forScope, Set<ArtifactCoordinate.WithoutVersion> alreadyIncluded, List<Artifact> result,
        List<Dependency> currentLevel) {
        var nextLevel = new ArrayList<Artifact>(currentLevel.size());
        for (var d : currentLevel) {
            var withoutVersion = d.getCoordinate().versionLess();
            if (alreadyIncluded.contains(withoutVersion) || !d.shouldInclude(forScope)) {
                continue;
            }
            var art = d.resolve(resolutionCache);
            if (art != null) {
                result.add(art);
                nextLevel.add(art);
            }
            alreadyIncluded.add(withoutVersion);
        }
        if (forScope == Scope.TEST) {
            // only do test scope for top level, switch to compile after that
            forScope = Scope.COMPILE;
        }
        // now we go through the new artifacts and make sure we add their dependencies if needed 
        for (var a: nextLevel) {
            calculateClassPath(forScope, alreadyIncluded, result, a.getDependencies(forScope));
        }
    }



    public static void main(String[] args) throws InterruptedException {
        var start = System.currentTimeMillis();
        var exampleCore = parseProjectPom(Path.of("C:/Users/Davy/swat.engineering/rascal/rascal/test/org/rascalmpl/util/maven/poms/multi-module/example-core/pom.xml"));
        System.out.println(exampleCore);
        var stop = System.currentTimeMillis();
        System.out.printf("It took %d ms to calculate\n", stop - start);
        start = System.currentTimeMillis();
        System.out.println(exampleCore.resolveArtifacts(Scope.COMPILE));
        stop = System.currentTimeMillis();
        System.out.printf("It took %d ms to calculate\n", stop - start);

        System.out.println("******");
        System.out.println("******");
        System.out.println("******");

        start = System.currentTimeMillis();
        var rascal = parseProjectPom(Path.of("pom.xml"));
        System.out.println(rascal);
        stop = System.currentTimeMillis();
        System.out.printf("It took %d ms to calculate\n", stop - start);
        start = System.currentTimeMillis();
        System.out.println(rascal.resolveArtifacts(Scope.COMPILE));
        stop = System.currentTimeMillis();
        System.out.printf("It took %d ms to calculate\n", stop - start);
    }
}
