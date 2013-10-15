/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.values.locations;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.exceptions.UnexpectedConstructorTypeException;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.rascalmpl.values.IRascalValueFactory;

public class Factory {
	private static TypeFactory tf = TypeFactory.getInstance();
	private static TypeStore ts = new TypeStore();

	public static final Type Location = tf.abstractDataType(ts, "Location");
	public static final Type Area = tf.abstractDataType(ts, "Area");

	public static final Type Location_File = tf.constructor(ts, Location,
			"file", tf.stringType(), "filename");
	public static final Type Location_Area = tf.constructor(ts, Location,
			"area", Area, "area");
	public static final Type Location_AreaInFile = tf.constructor(ts, Location,
			"area-in-file", tf.stringType(), "filename", Area, "area");

	public static final Type Area_Area = tf.constructor(ts, Area, "area", tf
			.integerType(), "beginLine", tf.integerType(), "beginColumn", tf
			.integerType(), "endLine", tf.integerType(), "endColumn", tf
			.integerType(), "offset", tf.integerType(), "length");

	private static final class InstanceHolder {
		public final static Factory factory = new Factory();
	}

	public static Factory getInstance() {
		return InstanceHolder.factory;
	}

	public static TypeStore getStore() {
		return ts;
	}

	private Factory() {
		super();
	}

	public ISourceLocation toSourceLocation(IRascalValueFactory factory,
			IConstructor loc) {
		Type type = loc.getConstructorType();

		if (type == Location_File) {
			String filename = ((IString) loc.get("filename")).getValue();
			return factory.sourceLocation(filename, 0, 0,
					0, 0, 0, 0);
		} else if (type == Location_Area) {
			String filename = "/";
			IConstructor area = (IConstructor) loc.get("area");
			if (area.getConstructorType() == Area_Area) {
				int offset = ((IInteger) area.get("offset")).intValue();
				int startLine = ((IInteger) area.get("beginLine")).intValue();
				int endLine = ((IInteger) area.get("endLine")).intValue();
				int startCol = ((IInteger) area.get("beginColumn")).intValue();
				int endCol = ((IInteger) area.get("endColumn")).intValue();
				int length = ((IInteger) area.get("length")).intValue();

				return factory.sourceLocation(filename,
						offset, length, startLine, endLine, startCol, endCol);
			}
			throw new UnexpectedConstructorTypeException(Area, area.getType());
		} else if (type == Location_AreaInFile) {
			String filename = ((IString) loc.get("filename")).getValue();
			IConstructor area = (IConstructor) loc.get("area");
			if (area.getConstructorType() == Area_Area) {
				int offset = ((IInteger) area.get("offset")).intValue();
				int startLine = ((IInteger) area.get("beginLine")).intValue();
				int endLine = ((IInteger) area.get("endLine")).intValue();
				int startCol = ((IInteger) area.get("beginColumn")).intValue();
				int endCol = ((IInteger) area.get("endColumn")).intValue();
				int length = ((IInteger) area.get("length")).intValue();

				return factory.sourceLocation(filename,
						offset, length, startLine, endLine, startCol, endCol);
			}

			throw new UnexpectedConstructorTypeException(Area, area.getType());

		}

		throw new UnexpectedConstructorTypeException(Location, type);
	}
}
