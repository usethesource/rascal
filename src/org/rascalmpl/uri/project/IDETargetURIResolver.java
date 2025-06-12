/*
 * Copyright (c) 2018-2025, NWO-I CWI and Swat.engineering
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
package org.rascalmpl.uri.project;

import java.io.IOException;
import java.util.function.Function;

import org.rascalmpl.uri.ILogicalSourceLocationResolver;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;

import io.usethesource.vallang.ISourceLocation;

public class IDETargetURIResolver implements ILogicalSourceLocationResolver {
    private final Function<ISourceLocation,ISourceLocation> resolver;

    public IDETargetURIResolver(Function<ISourceLocation,ISourceLocation> resolver) {
        this.resolver = resolver;
    }

    @Override
    public ISourceLocation resolve(ISourceLocation input) throws IOException {
        URIResolverRegistry reg = URIResolverRegistry.getInstance();
        ISourceLocation projectLoc = URIUtil.correctLocation("project", input.getAuthority(), "");
        ISourceLocation resolved = resolver.apply(projectLoc);

        ISourceLocation bin = URIUtil.getChildLocation(resolved, "bin");
        ISourceLocation targetClasses = URIUtil.getChildLocation(resolved, "target/classes");

        if (reg.exists(bin)) {
            if (!reg.exists(targetClasses)) {
                return URIUtil.getChildLocation(bin, input.getPath());
            }
        }

        if (reg.exists(targetClasses)) {
            return URIUtil.getChildLocation(targetClasses, input.getPath());
        }

        if (reg.exists(URIUtil.getChildLocation(resolved, "pom.xml"))) {
            return URIUtil.getChildLocation(targetClasses, input.getPath());
        }

        return URIUtil.getChildLocation(bin, input.getPath());
    }

    @Override
    public String scheme() {
        return "target";
    }

    @Override
    public String authority() {
        return "";
    }
}
