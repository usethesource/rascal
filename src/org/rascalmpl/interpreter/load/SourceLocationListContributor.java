package org.rascalmpl.interpreter.load;

import java.util.List;

import org.rascalmpl.value.IList;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.type.TypeFactory;

public class SourceLocationListContributor implements IRascalSearchPathContributor {
  private IList locs;
  private String label;

  public SourceLocationListContributor(String label, IList sourceLocations) {
    assert sourceLocations.getElementType().isSubtypeOf(TypeFactory.getInstance().sourceLocationType());
    this.label = label;
    this.locs = sourceLocations;
  }
  
  @Override
  public void contributePaths(List<ISourceLocation> path) {
    for (IValue elem : locs) {
      path.add(((ISourceLocation) elem));
    }
  }

  @Override
  public String getName() {
    return label;
  }

}
