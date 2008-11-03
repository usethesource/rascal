package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class StructuredType extends AbstractAST {
	static public class Ambiguity extends StructuredType {
		private final java.util.List<org.meta_environment.rascal.ast.StructuredType> alternatives;

		public Ambiguity(
				java.util.List<org.meta_environment.rascal.ast.StructuredType> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
		}

		public java.util.List<org.meta_environment.rascal.ast.StructuredType> getAlternatives() {
			return alternatives;
		}
	}

	static public class List extends StructuredType {
		private org.meta_environment.rascal.ast.TypeArg typeArg;

		/* "list" "[" typeArg:TypeArg "]" -> StructuredType {cons("List")} */
		private List() {
		}

		/* package */List(ITree tree,
				org.meta_environment.rascal.ast.TypeArg typeArg) {
			this.tree = tree;
			this.typeArg = typeArg;
		}

		private void $setTypeArg(org.meta_environment.rascal.ast.TypeArg x) {
			this.typeArg = x;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitStructuredTypeList(this);
		}

		@Override
		public org.meta_environment.rascal.ast.TypeArg getTypeArg() {
			return typeArg;
		}

		public List setTypeArg(org.meta_environment.rascal.ast.TypeArg x) {
			List z = new List();
			z.$setTypeArg(x);
			return z;
		}
	}

	static public class Map extends StructuredType {
		private org.meta_environment.rascal.ast.TypeArg first;
		private org.meta_environment.rascal.ast.TypeArg second;

		/*
		 * "map" "[" first:TypeArg "," second:TypeArg "]" -> StructuredType
		 * {cons("Map")}
		 */
		private Map() {
		}

		/* package */Map(ITree tree,
				org.meta_environment.rascal.ast.TypeArg first,
				org.meta_environment.rascal.ast.TypeArg second) {
			this.tree = tree;
			this.first = first;
			this.second = second;
		}

		private void $setFirst(org.meta_environment.rascal.ast.TypeArg x) {
			this.first = x;
		}

		private void $setSecond(org.meta_environment.rascal.ast.TypeArg x) {
			this.second = x;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitStructuredTypeMap(this);
		}

		@Override
		public org.meta_environment.rascal.ast.TypeArg getFirst() {
			return first;
		}

		@Override
		public org.meta_environment.rascal.ast.TypeArg getSecond() {
			return second;
		}

		public Map setFirst(org.meta_environment.rascal.ast.TypeArg x) {
			Map z = new Map();
			z.$setFirst(x);
			return z;
		}

		public Map setSecond(org.meta_environment.rascal.ast.TypeArg x) {
			Map z = new Map();
			z.$setSecond(x);
			return z;
		}
	}

	static public class Relation extends StructuredType {
		private org.meta_environment.rascal.ast.TypeArg first;
		private java.util.List<org.meta_environment.rascal.ast.TypeArg> rest;

		/*
		 * "rel" "[" first:TypeArg "," rest:{TypeArg ","}+ "]" -> StructuredType
		 * {cons("Relation")}
		 */
		private Relation() {
		}

		/* package */Relation(ITree tree,
				org.meta_environment.rascal.ast.TypeArg first,
				java.util.List<org.meta_environment.rascal.ast.TypeArg> rest) {
			this.tree = tree;
			this.first = first;
			this.rest = rest;
		}

		private void $setFirst(org.meta_environment.rascal.ast.TypeArg x) {
			this.first = x;
		}

		private void $setRest(
				java.util.List<org.meta_environment.rascal.ast.TypeArg> x) {
			this.rest = x;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitStructuredTypeRelation(this);
		}

		@Override
		public org.meta_environment.rascal.ast.TypeArg getFirst() {
			return first;
		}

		@Override
		public java.util.List<org.meta_environment.rascal.ast.TypeArg> getRest() {
			return rest;
		}

		public Relation setFirst(org.meta_environment.rascal.ast.TypeArg x) {
			Relation z = new Relation();
			z.$setFirst(x);
			return z;
		}

		public Relation setRest(
				java.util.List<org.meta_environment.rascal.ast.TypeArg> x) {
			Relation z = new Relation();
			z.$setRest(x);
			return z;
		}
	}

	static public class Set extends StructuredType {
		private org.meta_environment.rascal.ast.TypeArg typeArg;

		/* "set" "[" typeArg:TypeArg "]" -> StructuredType {cons("Set")} */
		private Set() {
		}

		/* package */Set(ITree tree,
				org.meta_environment.rascal.ast.TypeArg typeArg) {
			this.tree = tree;
			this.typeArg = typeArg;
		}

		private void $setTypeArg(org.meta_environment.rascal.ast.TypeArg x) {
			this.typeArg = x;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitStructuredTypeSet(this);
		}

		@Override
		public org.meta_environment.rascal.ast.TypeArg getTypeArg() {
			return typeArg;
		}

		public Set setTypeArg(org.meta_environment.rascal.ast.TypeArg x) {
			Set z = new Set();
			z.$setTypeArg(x);
			return z;
		}
	}

	static public class Tuple extends StructuredType {
		private org.meta_environment.rascal.ast.TypeArg first;
		private java.util.List<org.meta_environment.rascal.ast.TypeArg> rest;

		/*
		 * "tuple" "[" first:TypeArg "," rest:{TypeArg ","}+ "]" ->
		 * StructuredType {cons("Tuple")}
		 */
		private Tuple() {
		}

		/* package */Tuple(ITree tree,
				org.meta_environment.rascal.ast.TypeArg first,
				java.util.List<org.meta_environment.rascal.ast.TypeArg> rest) {
			this.tree = tree;
			this.first = first;
			this.rest = rest;
		}

		private void $setFirst(org.meta_environment.rascal.ast.TypeArg x) {
			this.first = x;
		}

		private void $setRest(
				java.util.List<org.meta_environment.rascal.ast.TypeArg> x) {
			this.rest = x;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitStructuredTypeTuple(this);
		}

		@Override
		public org.meta_environment.rascal.ast.TypeArg getFirst() {
			return first;
		}

		@Override
		public java.util.List<org.meta_environment.rascal.ast.TypeArg> getRest() {
			return rest;
		}

		public Tuple setFirst(org.meta_environment.rascal.ast.TypeArg x) {
			Tuple z = new Tuple();
			z.$setFirst(x);
			return z;
		}

		public Tuple setRest(
				java.util.List<org.meta_environment.rascal.ast.TypeArg> x) {
			Tuple z = new Tuple();
			z.$setRest(x);
			return z;
		}
	}

	public org.meta_environment.rascal.ast.TypeArg getFirst() {
		throw new UnsupportedOperationException();
	}

	public java.util.List<org.meta_environment.rascal.ast.TypeArg> getRest() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.TypeArg getSecond() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.TypeArg getTypeArg() {
		throw new UnsupportedOperationException();
	}
}
