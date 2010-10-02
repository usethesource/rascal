module viz::Figure::Render

import viz::Figure::Core;

/*
 * Library functions for rendering a figure.
 */

@doc{Render a figure}
@reflect{Needs calling context when calling argument function}
@javaClass{org.rascalmpl.library.viz.Figure.FigureLibrary}
public void java render(Figure fig);

@doc{Render a figure and write it to file}
@reflect{Needs calling context when calling argument function}
@javaClass{org.rascalmpl.library.viz.Figure.FigureLibrary}
public void java renderSave(Figure fig, loc file);