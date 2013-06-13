package org.rascalmpl.library.experiments.m3.internal;

import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;

public class Core {
	private static final TypeFactory TF = TypeFactory.getInstance();
	private static final TypeStore store = new TypeStore();
	
	public static final Type ADT_M3 = TF.abstractDataType(store, "M3");
	
	private static final Type locType = TF.sourceLocationType();
	
	public static final Type CONS_M3 = TF.constructor(store, ADT_M3, "M3", 
									   TF.sourceLocationType(), "project", 
									   TF.relType(locType, "from", locType, "to"), "containment",
									   TF.relType(locType, "from", locType, "to"), "inheritance",
									   TF.relType(locType, "from", locType, "to"), "access",
									   TF.relType(locType, "from", locType, "to"), "reference",
									   TF.relType(locType, "from", locType, "to"), "imports",
									   TF.mapType(locType, "definition", locType, "typ"), "types",
									   TF.mapType(locType, "definition", locType, "comments"), "documentation",
									   TF.relType(locType, "definition", locType, "modifiers"), "modifiers"
									   );
}
