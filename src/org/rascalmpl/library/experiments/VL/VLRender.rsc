module experiments::VL::VLRender

import experiments::VL::VLCore;

/*
 * Library functions for rendering a visual element.
 */

@doc{Render a visual element}
@reflect{Needs calling context when calling argument function}
@javaClass{org.rascalmpl.library.experiments.VL.VL}
public void java render(VELEM elem);