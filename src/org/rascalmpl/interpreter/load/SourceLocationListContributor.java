package org.rascalmpl.interpreter.load;

import java.net.URI;
import java.util.List;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.TypeFactory;

public class SourceLocationListContributor implements IRascalSearchPathContributor {
  private IList locs;
  private String label;

  public SourceLocationListContributor(String label, IList sourceLocations) {
    assert sourceLocations.getElementType().isSubtypeOf(TypeFactory.getInstance().sourceLocationType());
    this.label = label;
    this.locs = sourceLocations;
  }
  
  @Override
  public void contributePaths(List<URI> path) {
    for (IValue elem : locs) {
      path.add(((ISourceLocation) elem).getURI());
    }
  }

  @Override
  public String getName() {
    return label;
  }

}
