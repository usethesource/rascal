package org.rascalmpl.interpreter.load;

import java.util.Queue;

import io.usethesource.vallang.IList;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.type.TypeFactory;

public class SourceLocationListContributor implements IRascalSearchPathContributor {
  private IList locs;
  private String label;

  public SourceLocationListContributor(String label, IList sourceLocations) {
    assert sourceLocations.getElementType().isSubtypeOf(TypeFactory.getInstance().sourceLocationType());
    this.label = label;
    this.locs = sourceLocations;
  }
  
  @Override
  public void contributePaths(Queue<ISourceLocation> path) {
    for (IValue elem : locs) {
      path.add((ISourceLocation) elem);
    }
  }

  @Override
  public String getName() {
    return label;
  }

}
