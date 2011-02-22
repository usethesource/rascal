package org.rascalmpl.interpreter.utils;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.imp.pdb.facts.INode;
import org.rascalmpl.ast.ASTFactoryFactory;
import org.rascalmpl.ast.IASTVisitor;
import org.rascalmpl.ast.Name;
import org.rascalmpl.ast.QualifiedName;
import org.rascalmpl.interpreter.asserts.ImplementationError;


public class Names {

	static public Name lastName(QualifiedName qname) {
		List<Name> names = qname.getNames();
		return names.get(names.size() - 1);
	}
	
	static public String unescape(String s) {
		s = s.replace('\\', ' ');
		return s.replaceAll(" ","");
	}
	
	static public boolean isQualified(QualifiedName name) {
		return name.getNames().size() > 1;
	}

	static public String fullName(QualifiedName qname) {
		List<Name> names = qname.getNames();
		java.util.List<Name> prefix = names.subList(0, names.size() - 1);

		if (prefix.size() == 0) {
			return name(names.get(0));
		}
		
		StringBuilder tmp = new StringBuilder(names.size() * 20);
		Iterator<Name> iter = prefix.iterator();

		while (iter.hasNext()) {
			tmp.append(name(iter.next()));
			if (iter.hasNext()) {
				tmp.append("::");
			}
		}
		
		tmp.append("::");
		tmp.append(name(names.get(names.size() - 1)));
		
		return tmp.toString();
	}

	/**
	 * Get the module name part of a qualified name
	 * @return a string containing all but the last part of the given qualified name
	 */
	static public String moduleName(QualifiedName qname) {
		List<Name> names = qname.getNames();
		java.util.List<Name> prefix = names.subList(0, names.size() - 1);

		if (prefix.size() == 0) {
			return null;
		}
		
		StringBuilder tmp = new StringBuilder(names.size() * 20);
		Iterator<Name> iter = prefix.iterator();

		while (iter.hasNext()) {
			tmp.append(name(iter.next()));
			if (iter.hasNext()) {
				tmp.append("::");
			}
		}
		
		return tmp.toString();
	}
	
	static public String name(Name name) {
		String s;
		if (name instanceof Name.Lexical) {
			s = ((Name.Lexical) name).getString();
		}
		else if (name instanceof InventedName) {
			s = ((InventedName) name).toString();
		}
		else {
			throw new ImplementationError("unexpected type of name found: " + name);
		}
		return unescape(s);
	}
	
	static public String consName(QualifiedName qname) {
		return name(lastName(qname));
	}
	
	static public String typeName(QualifiedName qname) {
		return name(lastName(qname));
	}
	
	static public String sortName(QualifiedName qname) {
		List<Name> names = qname.getNames();
		if (names.size() >= 2) {
			return name(names.get(names.size() - 2));
		}
		return null;
	}
	
	static public Name toName(String name) {
		return new InventedName(name);
	}
	
	static public QualifiedName toQualifiedName(String name) {
		List<Name> list = new LinkedList<Name>();
		list.add(toName(name));
		return ASTFactoryFactory.getASTFactory().makeQualifiedNameDefault(null, list);
	}
	
	static class InventedName extends Name {
		private final String fName;
		
		public InventedName(String name) {
			super(null);
			fName = name;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			throw new ImplementationError("Can not visit invented name");
		}
		
		@Override
		public String toString() {
			return fName;
		}
		
		@Override
		public INode getTree() {
			throw new ImplementationError("Invented name does not have a node");
		}
	}
}
