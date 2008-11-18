package org.meta_environment.rascal.errors;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.imp.pdb.facts.ITree;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.impl.hash.ValueFactory;
import org.eclipse.imp.pdb.facts.type.FactTypeError;
import org.eclipse.imp.pdb.facts.type.TreeNodeType;
import org.eclipse.imp.pdb.facts.type.NamedTreeType;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.meta_environment.rascal.io.ATermReader;

public class Factory {
	private static TypeFactory tf = TypeFactory.getInstance();
	private static IValueFactory vf = ValueFactory.getInstance();

	public static final NamedTreeType Summary = tf.namedTreeType("Summary");
	public static final NamedTreeType Error = tf.namedTreeType("Error");
	public static final NamedTreeType Subject = tf.namedTreeType("Subject");

	public static final TreeNodeType Summary_Summary = tf.treeNodeType(Summary, "summary", tf.stringType(), "producer", tf.stringType(), "id", tf.listType(Error), "errors");
	public static final TreeNodeType Error_Info = tf.treeNodeType(Error, "info", tf.stringType(), "description", tf.listType(Subject), "subjects");
	public static final TreeNodeType Error_Warning = tf.treeNodeType(Error, "warning", tf.stringType(), "description", tf.listType(Subject), "subjects");
	public static final TreeNodeType Error_Error = tf.treeNodeType(Error, "error", tf.stringType(), "description", tf.listType(Subject), "subjects");
	public static final TreeNodeType Error_Fatal = tf.treeNodeType(Error, "fatal", tf.stringType(), "description", tf.listType(Subject), "subjects");
	
	public static final TreeNodeType Subject_Subject = tf.treeNodeType(Subject, "subject", tf.stringType(), "description");
	public static final TreeNodeType Subject_Localized = tf.treeNodeType(Subject, "localized", tf.stringType(), "description", org.meta_environment.rascal.locations.Factory.Location, "location");
	
	private static final class InstanceHolder {
		public final static Factory factory = new Factory();
	}
	  
	public static Factory getInstance() {
		return InstanceHolder.factory;
	}
	
	private Factory() {}
	
	public ITree readSummary(InputStream stream) throws FactTypeError, IOException {
		ATermReader reader = new ATermReader();
		return (ITree) reader.read(vf, Summary, stream);
	}
}
