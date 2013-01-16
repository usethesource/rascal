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
package org.rascalmpl.values.errors;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.io.ATermReader;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.rascalmpl.values.ValueFactoryFactory;

public class Factory {
	private static TypeStore errors = new TypeStore(org.rascalmpl.values.locations.Factory.getStore());
	private static TypeFactory tf = TypeFactory.getInstance();
	private static IValueFactory vf = ValueFactoryFactory.getValueFactory();

	public static final Type Summary = tf.abstractDataType(errors, "Summary");
	public static final Type Error = tf.abstractDataType(errors, "Error");
	public static final Type Subject = tf.abstractDataType(errors, "Subject");

	public static final Type Summary_Summary = tf.constructor(errors, Summary, "summary", tf.stringType(), "producer", tf.stringType(), "id", tf.listType(Error), "errors");
	public static final Type Error_Info = tf.constructor(errors, Error, "info", tf.stringType(), "description", tf.listType(Subject), "subjects");
	public static final Type Error_Warning = tf.constructor(errors, Error, "warning", tf.stringType(), "description", tf.listType(Subject), "subjects");
	public static final Type Error_Error = tf.constructor(errors, Error, "error", tf.stringType(), "description", tf.listType(Subject), "subjects");
	public static final Type Error_Fatal = tf.constructor(errors, Error, "fatal", tf.stringType(), "description", tf.listType(Subject), "subjects");
	
	public static final Type Subject_Subject = tf.constructor(errors, Subject, "subject", tf.stringType(), "description");
	public static final Type Subject_Localized = tf.constructor(errors, Subject, "localized", tf.stringType(), "description", org.rascalmpl.values.locations.Factory.Location, "location");
	
	private static final class InstanceHolder {
		public final static Factory factory = new Factory();
	}
	  
	public static Factory getInstance() {
		return InstanceHolder.factory;
	}
	
	private Factory() {
		super();
	}
	
	public static TypeStore getStore() {
		return errors;
	}
	
	public INode readSummary(InputStream stream) throws FactTypeUseException, IOException {
		ATermReader reader = new ATermReader();
		return (INode) reader.read(vf, Summary, stream);
	}
}
