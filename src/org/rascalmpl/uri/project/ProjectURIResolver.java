/** 
 * Copyright (c) 2019, Jurgen J. Vinju, Centrum Wiskunde & Informatica (NWOi - CWI) 
 * All rights reserved. 
 *  
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met: 
 *  
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer. 
 *  
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution. 
 *  
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */ 
package org.rascalmpl.uri.project;

import java.io.IOException;

import org.rascalmpl.uri.ILogicalSourceLocationResolver;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;

import io.usethesource.vallang.ISourceLocation;

/**
 * This class implements the project:// scheme for in situations outside of an IDE.
 * In IDE based situations the IDE bridge must offer the implementation of the project
 * scheme. 
 * 
 * We take the root of the *active* project:
 *   * its parent is the "workspace"
 *   * all folders in the workspace are "projects"
 */
public class ProjectURIResolver implements ILogicalSourceLocationResolver {
    private ISourceLocation workspaceFolder;

    /**
     * @param projectRoot  the root of a project, containing /META-INF/RASCAL.MF and /pom.xml
     * @throws IOException if the parent of the root of a project does not exist
     */
    public ProjectURIResolver(ISourceLocation projectRoot) throws IOException {
        var physical = URIResolverRegistry.getInstance().logicalToPhysical(projectRoot);
        this.workspaceFolder = URIUtil.getParentLocation(physical);
    }
    
    @Override
    public ISourceLocation resolve(ISourceLocation input) throws IOException {
        assert input.getScheme().equals(scheme());
        assert !input.getAuthority().isEmpty();
        
        // the authority becomes the folder name
        var projectRoot = URIUtil.getChildLocation(workspaceFolder, input.getAuthority());

        // the path becomes a child of the project's root
        return URIUtil.getChildLocation(projectRoot, input.getPath());
    }

    @Override
    public String scheme() {
        return "project";
    }

    @Override
    public String authority() {
        return "";
    }
}
