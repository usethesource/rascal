package org.meta_environment.rascal.interpreter.result;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IDouble;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IRelation;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.ITypeVisitor;
import org.eclipse.imp.pdb.facts.type.Type;

public class ResultFactory {
	
	@SuppressWarnings("unchecked")
	public static <T extends IValue> AbstractResult<T> makeResult(Type declaredType, IValue value) {
		return (AbstractResult<T>) declaredType.accept(new Visitor(declaredType, value));
	}

	private static class Visitor implements ITypeVisitor<AbstractResult<? extends IValue>> {
		private IValue value;
		private Type declaredType;

		public Visitor(Type type, IValue value) {
			this.declaredType = type;
			this.value = value;
		}

		@Override
		public ConstructorResult visitAbstractData(Type type) {
			// TODO: rename constructor result to AbstractData
			return new ConstructorResult(declaredType, (IConstructor)value);
		}

		@Override
		public AbstractResult<? extends IValue> visitAlias(Type type) {
			return type.getAliased().accept(this);
		}

		@Override
		public BoolResult visitBool(Type boolType) {
			return new BoolResult(declaredType, (IBool)value);
		}

		@Override
		public ConstructorResult visitConstructor(Type type) {
			return new ConstructorResult(declaredType, (IConstructor)value);
		}

		@Override
		public RealResult visitDouble(Type type) {
			return new RealResult(declaredType, (IDouble)value);
		}

		@Override
		public IntegerResult visitInteger(Type type) {
			return new IntegerResult(declaredType, (IInteger)value);
		}

		@Override
		public ListResult visitList(Type type) {
			return new ListResult(declaredType, (IList)value);
		}

		@Override
		public MapResult visitMap(Type type) {
			return new MapResult(declaredType, (IMap)value);
		}

		@Override
		public NodeResult visitNode(Type type) {
			return new NodeResult(declaredType, (INode)value);
		}

		@Override
		public AbstractResult<? extends IValue> visitParameter(Type parameterType) {
			return parameterType.getBound().accept(this);
		}

		@Override
		public RelationResult visitRelationType(Type type) {
			return new RelationResult(declaredType, (IRelation)value);
		}

		@Override
		public SetResult visitSet(Type type) {
			return new SetResult(declaredType, (ISet)value);
		}

		@Override
		public SourceLocationResult visitSourceLocation(Type type) {
			return new SourceLocationResult(declaredType, (ISourceLocation)value);		
		}

		@Override
		public StringResult visitString(Type type) {
			return new StringResult(declaredType, (IString)value);
		}

		@Override
		public TupleResult visitTuple(Type type) {
			return new TupleResult(declaredType, (ITuple)value);
		}

		@Override
		public ValueResult<IValue> visitValue(Type type) {
			return new ValueResult<IValue>(declaredType, value);
		}

		@Override
		public VoidResult visitVoid(Type type) {
			return new VoidResult(declaredType);
		}
	}
}
