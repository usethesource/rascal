module viz::VLRender

import experiments::VL::FigureCore;

/*
 * Library functions for rendering a figure.
 */

@doc{Render a figure}
@reflect{Needs calling context when calling argument function}
@javaClass{org.rascalmpl.library.experiments.VL.FigureLibrary}
public void java render(Figure elem);