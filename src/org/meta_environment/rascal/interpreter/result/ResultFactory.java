package org.meta_environment.rascal.interpreter.result;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IReal;
import org.eclipse.imp.pdb.facts.IRelation;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.ITypeVisitor;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;

public class ResultFactory {

	// TODO: do apply rules here and introduce normalizedResult. 
	
	@SuppressWarnings("unchecked")
	public static <T extends IValue> Result<T> makeResult(Type declaredType, IValue value) {
		return (Result<T>) declaredType.accept(new Visitor(declaredType, value));
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends IValue> Result<T> nothing() {
		Type type = TypeFactory.getInstance().voidType();
		return (Result<T>) type.accept(new Visitor(type, null));
	}

	@SuppressWarnings("unchecked")
	public static <T extends IValue> Result<T> bool(boolean b) {
		return (Result<T>) new BoolResult(b);
	}
	
	private static class Visitor implements ITypeVisitor<Result<? extends IValue>> {
		private IValue value;
		private Type declaredType;

		public Visitor(Type type, IValue value) {
			this.declaredType = type;
			this.value = value;
		}

		public ElementResult<? extends IValue> visitAbstractData(Type type) {
			// TODO: rename constructor result to AbstractData
			return new ConstructorResult(declaredType, (IConstructor)value);
		}

		public Result<? extends IValue> visitAlias(Type type) {
			return type.getAliased().accept(this);
		}

		public BoolResult visitBool(Type boolType) {
			return new BoolResult(declaredType, (IBool)value);
		}

		public ElementResult<? extends IValue> visitConstructor(Type type) {
			return new ConstructorResult(declaredType, (IConstructor)value);
		}

		public RealResult visitReal(Type type) {
			return new RealResult(declaredType, (IReal)value);
		}

		public IntegerResult visitInteger(Type type) {
			return new IntegerResult(declaredType, (IInteger)value);
		}

		public ListResult visitList(Type type) {
			return new ListResult(declaredType, (IList)value);
		}

		public MapResult visitMap(Type type) {
			return new MapResult(declaredType, (IMap)value);
		}

		public ElementResult<? extends IValue> visitNode(Type type) {
			return new NodeResult(declaredType, (INode)value);
		}

		public Result<? extends IValue> visitParameter(Type parameterType) {
			return parameterType.getBound().accept(this);
		}

		public RelationResult visitRelationType(Type type) {
			return new RelationResult(declaredType, (IRelation)value);
		}

		public SetOrRelationResult<ISet> visitSet(Type type) {
			return new SetResult(declaredType, (ISet)value);
		}

		public SourceLocationResult visitSourceLocation(Type type) {
			return new SourceLocationResult(declaredType, (ISourceLocation)value);		
		}

		public StringResult visitString(Type type) {
			return new StringResult(declaredType, (IString)value);
		}

		public TupleResult visitTuple(Type type) {
			return new TupleResult(declaredType, (ITuple)value);
		}

		public ValueResult visitValue(Type type) {
			return new ValueResult(declaredType, value);
		}

		public VoidResult visitVoid(Type type) {
			return new VoidResult(declaredType);
		}
	}
}
