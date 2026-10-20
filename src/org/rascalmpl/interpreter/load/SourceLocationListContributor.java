package org.rascalmpl.interpreter.load;

import java.util.Collection;
import java.util.stream.Collectors;

import io.usethesource.vallang.IList;
import io.usethesource.vallang.ISourceLocation;
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
  public Collection<ISourceLocation> contributePaths() {
    return locs.stream().map(ISourceLocation.class::cast).collect(Collectors.toList());
  }

  @Override
  public String getName() {
    return label;
  }

}
