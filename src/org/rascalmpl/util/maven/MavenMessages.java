package org.rascalmpl.util.maven;

import org.apache.commons.lang3.tuple.Pair;
import org.rascalmpl.library.Messages;
import org.rascalmpl.library.Prelude;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.util.locations.ColumnMaps;
import org.rascalmpl.util.locations.LineColumnOffsetMap;
import org.rascalmpl.values.IRascalValueFactory;

import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValue;

/**
 * This class is responsible for mapping dependency pom and position information to an ISourceLocation of the declaration of the dependency.
 */
public class MavenMessages {
	private static final IRascalValueFactory VF = IRascalValueFactory.getInstance();

    private static final ColumnMaps columnMaps = initColumnMaps();

    private static ColumnMaps initColumnMaps() {
        return new ColumnMaps(loc -> {
            return Prelude.consumeInputStream(URIResolverRegistry.getInstance().getCharacterReader(loc));
        });
    }

    private static ISourceLocation calcMessageLocation(ISourceLocation pomLocation, int line, int column) {
        LineColumnOffsetMap map = columnMaps.get(pomLocation);
        if (map != null) {
            int utf32Column = map.translateInverseColumn(line, column, true);
            Pair<Integer, Integer> offsets = map.calculateInverseOffsetLength(0, 0, line, column);
            return VF.sourceLocation(pomLocation, offsets.getRight(), 0, line, line, utf32Column, utf32Column);
        }

        return pomLocation;
    }

    public static IValue info(String message, ISourceLocation pomLocation, int line, int column) {
        return Messages.info(message, calcMessageLocation(pomLocation, line, column));
    }

    public static IValue warning(String message, ISourceLocation pomLocation, int line, int column) {
        return Messages.warning(message, calcMessageLocation(pomLocation, line, column));
    }

    public static IValue error(String message, ISourceLocation pomLocation, int line, int column) {
        return Messages.error(message, calcMessageLocation(pomLocation, line, column));
    }

    public static IValue info(String message, Dependency dep) {
        return info(message, dep.getPomLocation(), dep.getLine(), dep.getColumn());
    }

    public static IValue warning(String message, Dependency dep) {
        return warning(message, dep.getPomLocation(), dep.getLine(), dep.getColumn());
    }

    public static IValue error(String message, Dependency dep) {
        return error( message, dep.getPomLocation(), dep.getLine(), dep.getColumn());
    }

    public static IValue info(String message, Artifact artifact) {
        return info(message, artifact.getOrigin());
    }

    public static IValue warning(String message, Artifact artifact) {
        return warning(message, artifact.getOrigin());
    }

    public static IValue error(String message, Artifact artifact) {
        return error(message, artifact.getOrigin());
    }

}
