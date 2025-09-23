package org.rascalmpl.util.maven;

import java.io.IOException;

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
import io.usethesource.vallang.type.Type;

/**
 * This class is responsible for mapping dependency pom and position information to an ISourceLocation of the declaration of the dependency.
 */
public class MavenMessageConverter {
	private static final IValueFactory VF = ValueFactoryFactory.getValueFactory();

    private final ColumnMaps columnMaps;
    private boolean errorReadingPom;

    public MavenMessageConverter() {
        columnMaps = new ColumnMaps(l -> {
            try {
                return Prelude.consumeInputStream(URIResolverRegistry.getInstance().getCharacterReader(l.top()));
            }
            catch (IOException e) {
                errorReadingPom = true;
                return "";
            }
        });
    }

    private IValue message(Type type, String message, ISourceLocation pomLocation, int line, int column) {
        errorReadingPom = false;
        LineColumnOffsetMap map = columnMaps.get(pomLocation);

        if (errorReadingPom) {
            // Maybe log some warning here?
            return pomLocation;
        }

        Pair<Integer, Integer> offsets = map.calculateInverseOffsetLength(0, 0, line, column);
        ISourceLocation messageLocation = VF.sourceLocation(pomLocation, offsets.getRight(), 0, line, line, column, column);

        return Messages.message(type, message, messageLocation);
    }

    private IValue message(Type type, String message, Dependency dep) {
        return message(type, message, dep.getPomLocation(), dep.getLine(), dep.getColumn());
    }

    public IValue info(String message, Dependency dep) {
        return message(Messages.Message_info, message, dep);
    }

    public IValue warning(String message, Dependency dep) {
        return message(Messages.Message_warning, message, dep);
    }

    public IValue error(String message, Dependency dep) {
        return message(Messages.Message_error, message, dep);
    }

    public IValue info(String message, Artifact artifact) {
        return info(message, artifact.getOrigin());
    }

    public IValue warning(String message, Artifact artifact) {
        return warning(message, artifact.getOrigin());
    }

    public IValue error(String message, Artifact artifact) {
        return error(message, artifact.getOrigin());
    }

    public IValue error(String message, ISourceLocation pomLocation, int line, int column) {
        return message(Messages.Message_error, message, pomLocation, line, column);
    }

    public IValue warning(String message, ISourceLocation pomLocation, int line, int column) {
        return message(Messages.Message_warning, message, pomLocation, line, column);
    }

}
