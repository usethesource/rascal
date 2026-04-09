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

import org.apache.maven.model.Model;
import org.apache.maven.model.building.DefaultModelBuildingRequest;
import org.apache.maven.model.building.ModelBuilder;
import org.apache.maven.model.building.ModelBuildingException;
import org.apache.maven.model.building.ModelBuildingRequest;
import org.apache.maven.model.building.ModelSource2;
import org.apache.maven.model.resolution.UnresolvableModelException;
import org.apache.maven.model.resolution.WorkspaceModelResolver;

/**
 * This class is only used when we're parsing a pom from the repository, and it has a parent pom it needs finding
 */
/*package*/ class SimpleWorkspaceResolver implements WorkspaceModelResolver {

    private final SimpleResolver resolver;
    private final ModelBuilder builder;
    private final MavenParser parser;


    public SimpleWorkspaceResolver(SimpleResolver resolver, ModelBuilder builder, MavenParser parser) {
        this.resolver = resolver;
        this.builder = builder;
        this.parser = parser;
    }

	@Override
    public Model resolveRawModel(String groupId, String artifactId, String versionConstraint)
        throws UnresolvableModelException {
        var location = ((ModelSource2)resolver.resolveModel(groupId, artifactId, versionConstraint)).getLocationURI();
        if (!location.getScheme().equals("file")) {
            throw new UnresolvableModelException("Could not locate model on disk", groupId, artifactId, versionConstraint);
        }
        var result = builder.buildRawModel(new File(location), ModelBuildingRequest.VALIDATION_LEVEL_MINIMAL, false);
        if (result.get() == null) {
            throw new UnresolvableModelException("Could not build raw model", groupId, artifactId, versionConstraint);
        }
        return result.get();
    }

    @Override
    public Model resolveEffectiveModel(String groupId, String artifactId, String versionConstraint)
        throws UnresolvableModelException {
        var location = resolver.resolveModel(groupId, artifactId, versionConstraint);
        var request = new DefaultModelBuildingRequest()
            .setModelSource(location)
            .setWorkspaceModelResolver(this)
            ;
        try {
            return parser.buildEffectiveModel(request, resolver);
        }
        catch (ModelBuildingException e) {
            throw new UnresolvableModelException(e, groupId, artifactId, versionConstraint);
        }
    }

}
