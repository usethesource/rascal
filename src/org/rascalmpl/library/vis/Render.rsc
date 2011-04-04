@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Bert Lisser - Bert.Lisser@cwi.nl (CWI)}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
@contributor{Arnold Lankamp - Arnold.Lankamp@cwi.nl}
module vis::Render

import vis::Figure;

/*
 * Library functions for rendering a figure.
 */

@doc{Render a figure in standard tab}
@reflect{Needs calling context when calling argument function}
@javaClass{org.rascalmpl.library.vis.FigureLibrary}
public void java render(Figure fig);

@doc{Render a figure, in named tab}
@reflect{Needs calling context when calling argument function}
@javaClass{org.rascalmpl.library.vis.FigureLibrary}
public void java render(str name, Figure fig);

@doc{Render a figure and write it to file}
@reflect{Needs calling context when calling argument function}
@javaClass{org.rascalmpl.library.vis.FigureLibrary}
public void java renderSave(Figure fig, loc file);

@doc{Set custom colors for errors}
@javaClass{org.rascalmpl.library.vis.FigureColorUtils}
public void java setErrorColors(list[Color] colors);

@doc{Set custom colors for editor highlights}
@javaClass{org.rascalmpl.library.vis.FigureColorUtils}
public void java setHighlightColors(list[Color] colors);
