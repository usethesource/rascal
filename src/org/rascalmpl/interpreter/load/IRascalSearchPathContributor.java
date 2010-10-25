package org.rascalmpl.interpreter.load;

import java.net.URI;
import java.util.List;

public interface IRascalSearchPathContributor {
	void contributePaths(List<URI> path);
}
