package org.rascalmpl.util.maven;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.rascalmpl.library.Messages;
import org.rascalmpl.library.Prelude;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.util.locations.ColumnMaps;
import org.rascalmpl.util.locations.LineColumnOffsetMap;
import org.rascalmpl.values.ValueFactoryFactory;

import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;

/**
 * This class is responsible for mapping dependency pom and position information to an ISourceLocation of the declaration of the dependency.
 * Note that this requires reading the pom files, which may fail, so the mapper also collects messages.
 */
public class DependencyOriginMapper {
	private static final IValueFactory VF = ValueFactoryFactory.getValueFactory();

    private ColumnMaps columnMaps;
    private List<IValue> messages;

    public DependencyOriginMapper() {
        columnMaps = new ColumnMaps(l -> {
            try {
                return Prelude.consumeInputStream(URIResolverRegistry.getInstance().getCharacterReader(l.top()));
            }
            catch (IOException e) {
                messages.add(Messages.error("Could not read pom: " + e.getMessage(), l));
                return "";
            }
        });
    }

    public ISourceLocation getDependencyLocation(Dependency d) {
        ISourceLocation pom = d.getPomLocation();
        LineColumnOffsetMap map = columnMaps.get(pom);
        int line = d.getLine();
        int column = d.getColumn();
        Pair<Integer,Integer> offsets = map.calculateInverseOffsetLength(0, 0, line, column);
		return VF.sourceLocation(pom, offsets.getRight(), 0, line, line, column, column);
    }

    public List<IValue> getMapperMessages() {
        return messages;
    }
}
