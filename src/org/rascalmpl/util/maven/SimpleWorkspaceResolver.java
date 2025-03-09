package org.rascalmpl.util.maven;

import java.io.File;

import org.apache.maven.model.Model;
import org.apache.maven.model.building.DefaultModelBuildingRequest;
import org.apache.maven.model.building.ModelBuilder;
import org.apache.maven.model.building.ModelBuildingException;
import org.apache.maven.model.building.ModelBuildingRequest;
import org.apache.maven.model.building.ModelBuildingResult;
import org.apache.maven.model.building.ModelCache;
import org.apache.maven.model.building.ModelSource2;
import org.apache.maven.model.resolution.ModelResolver;
import org.apache.maven.model.resolution.UnresolvableModelException;
import org.apache.maven.model.resolution.WorkspaceModelResolver;

/*package*/ class SimpleWorkspaceResolver implements WorkspaceModelResolver {

    private final CurrentResolution context;
    
    public SimpleWorkspaceResolver(CurrentResolution context) {
        this.context = context;
    }

    @Override
    public Model resolveRawModel(String groupId, String artifactId, String versionConstraint)
        throws UnresolvableModelException {
        var location = ((ModelSource2)context.resolver.resolveModel(groupId, artifactId, versionConstraint)).getLocationURI();
        if (!location.getScheme().equals("file")) {
            throw new UnresolvableModelException("Could not locate model on disk", groupId, artifactId, versionConstraint);
        }
        var result = context.builder.buildRawModel(new File(location), ModelBuildingRequest.VALIDATION_LEVEL_MINIMAL, false);
        if (result.get() == null) {
            throw new UnresolvableModelException("Could not build raw model", groupId, artifactId, versionConstraint);
        }
        return result.get();
    }

    @Override
    public Model resolveEffectiveModel(String groupId, String artifactId, String versionConstraint)
        throws UnresolvableModelException {
        var location = context.resolver.resolveModel(groupId, artifactId, versionConstraint);
        var request = new DefaultModelBuildingRequest()
            .setModelSource(location)
            .setWorkspaceModelResolver(this)
            ;
        try {
            var result = Project.buildModel(request, context);
            return result.getEffectiveModel();
        }
        catch (ModelBuildingException e) {
            throw new UnresolvableModelException(e, groupId, artifactId, versionConstraint);
        }
    }

}
