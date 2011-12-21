@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Mark Hills - Mark.Hills@cwi.nl (CWI)}
module experiments::resource::Resource

import List;

data Option[&T] = none() | some(&T v);
    
data Nullable[&T] = null() | notnull(&T v);

@javaClass{org.rascalmpl.library.experiments.resource.Resource}
@reflect{Uses Context for access to the current class loader}
public java void registerResource(str javaClass);

@javaClass{org.rascalmpl.library.experiments.resource.Resource}
@reflect{Uses Context for Creating Values}
public java &T getTypedResource(loc uri, type[&T] t);

@javaClass{org.rascalmpl.library.experiments.resource.Resource}
@reflect{Uses Context for Creating Values}
private java void generateTypedInterfaceInternal(str tagname, loc uri);

public void generateTypedInterface(str tagname, loc uri, str params...) {
    int idx = 0;
    while (idx < size(params)) {
        str paramName = params[idx]; idx += 1;
        str paramValue = params[idx]; idx += 1;
        if (idx > 2) uri = uri[query = uri.query + "&"];
        uri = uri[query = uri.query + "<paramName>=<paramValue>"];
    }
    generateTypedInterfaceInternal(tagname,uri);
}
