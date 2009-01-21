package org.meta_environment.rascal.interpreter;

import java.util.Iterator;
import java.util.List;

import org.eclipse.imp.pdb.facts.INode;
import org.meta_environment.rascal.ast.IASTVisitor;
import org.meta_environment.rascal.ast.Name;
import org.meta_environment.rascal.ast.QualifiedName;

public class Names {

	static public Name lastName(QualifiedName qname) {
		List<Name> names = qname.getNames();
		return names.get(names.size() - 1);
	}
	
	static public String moduleName(QualifiedName qname) {
		List<Name> names = qname.getNames();
		java.util.List<Name> prefix = names.subList(0, names.size() - 1);

		if (prefix.size() == 0) {
			return null;
		}
		
		StringBuilder tmp = new StringBuilder();
		Iterator<Name> iter = prefix.iterator();

		while (iter.hasNext()) {
			Name part = iter.next();
			tmp.append(part.toString());
			if (iter.hasNext()) {
				tmp.append("::");
			}
		}

		return tmp.toString();
	}
	
	static public String name(Name name) {
		String s = name.toString();
		if (s.startsWith("\\")) {
			s = s.substring(1);
		}
		
		return s;
	}
	
	static public String consName(QualifiedName qname) {
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
	
	static class InventedName extends Name {
		private final String fName;
		
		public InventedName(String name) {
			fName = name;
		}

		public <T> T accept(IASTVisitor<T> v) {
			throw new RascalBug("Can not visit invented name");
		}
		
		@Override
		public String toString() {
			return fName;
		}
		
		@Override
		public INode getTree() {
			throw new RascalBug("Invented name does not have a node");
		}
	}
}
