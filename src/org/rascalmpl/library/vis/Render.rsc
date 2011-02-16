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

@doc{Set custom colors for editor annotations}
@javaClass{org.rascalmpl.library.vis.FigureLibrary}
public void java setHighlightColors(list[Color] colors);