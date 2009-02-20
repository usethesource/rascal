package org.meta_environment.rascal.errors;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.io.ATermReader;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.meta_environment.rascal.ValueFactoryFactory;

public class Factory {
	private static TypeStore errors = new TypeStore(org.meta_environment.rascal.locations.Factory.getStore());
	private static TypeFactory tf = TypeFactory.getInstance();
	private static IValueFactory vf = ValueFactoryFactory.getValueFactory();

	public static final Type Summary = errors.abstractDataType("Summary");
	public static final Type Error = errors.abstractDataType("Error");
	public static final Type Subject = errors.abstractDataType("Subject");

	public static final Type Summary_Summary = errors.constructor(Summary, "summary", tf.stringType(), "producer", tf.stringType(), "id", tf.listType(Error), "errors");
	public static final Type Error_Info = errors.constructor(Error, "info", tf.stringType(), "description", tf.listType(Subject), "subjects");
	public static final Type Error_Warning = errors.constructor(Error, "warning", tf.stringType(), "description", tf.listType(Subject), "subjects");
	public static final Type Error_Error = errors.constructor(Error, "error", tf.stringType(), "description", tf.listType(Subject), "subjects");
	public static final Type Error_Fatal = errors.constructor(Error, "fatal", tf.stringType(), "description", tf.listType(Subject), "subjects");
	
	public static final Type Subject_Subject = errors.constructor(Subject, "subject", tf.stringType(), "description");
	public static final Type Subject_Localized = errors.constructor(Subject, "localized", tf.stringType(), "description", org.meta_environment.rascal.locations.Factory.Location, "location");
	
	private static final class InstanceHolder {
		public final static Factory factory = new Factory();
	}
	  
	public static Factory getInstance() {
		return InstanceHolder.factory;
	}
	
	private Factory() {}
	
	public static TypeStore getStore() {
		return errors;
	}
	
	public INode readSummary(InputStream stream) throws FactTypeUseException, IOException {
		ATermReader reader = new ATermReader();
		return (INode) reader.read(vf, Summary, stream);
	}
}
