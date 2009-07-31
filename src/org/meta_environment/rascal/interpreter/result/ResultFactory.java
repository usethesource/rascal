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
import org.meta_environment.rascal.interpreter.IEvaluatorContext;
import org.meta_environment.uptr.Factory;

public class ResultFactory {

	// TODO: do apply rules here and introduce normalizedResult. 
	
	@SuppressWarnings("unchecked")
	public static <T extends IValue> Result<T> makeResult(Type declaredType, IValue value, IEvaluatorContext ctx) {
		return (Result<T>) declaredType.accept(new Visitor(declaredType, value, ctx));
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends IValue> Result<T> nothing() {
		Type type = TypeFactory.getInstance().voidType();
		return (Result<T>) type.accept(new Visitor(type, null, null));
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends IValue> Result<T> nothing(Type type) {
		return (Result<T>) type.accept(new Visitor(type, null, null));
	}
	

	@SuppressWarnings("unchecked")
	public static <T extends IValue> Result<T> bool(boolean b) {
		return (Result<T>) new BoolResult(b, null, null);
	}
	
	private static class Visitor implements ITypeVisitor<Result<? extends IValue>> {
		private IValue value;
		private Type declaredType;
		private IEvaluatorContext ctx;

		public Visitor(Type type, IValue value, IEvaluatorContext ctx) {
			this.declaredType = type;
			this.value = value;
			this.ctx = ctx;
		}

		public ElementResult<? extends IValue> visitAbstractData(Type type) {
			// TODO: rename constructor result to AbstractData
			if (type.equals(Factory.Tree)) {
				return new ConcreteSyntaxResult(declaredType, (IConstructor)value, ctx);
			}
			return new ConstructorResult(declaredType, (IConstructor)value, ctx);
		}

		public Result<? extends IValue> visitAlias(Type type) {
			return type.getAliased().accept(this);
		}

		public BoolResult visitBool(Type boolType) {
			return new BoolResult(declaredType, (IBool)value, null, ctx);
		}

		public Result<? extends IValue> visitConstructor(Type type) {
			if (type.equals(Factory.Tree)) {
				return new ConcreteSyntaxResult(declaredType, (IConstructor)value, ctx);
			}
			if (type.equals(Lambda.getClosureType())) {
				return (Lambda)value;
			}
			return new ConstructorResult(declaredType.getAbstractDataType(), (IConstructor)value, ctx);
		}

		public RealResult visitReal(Type type) {
			return new RealResult(declaredType, (IReal)value, ctx);
		}

		public IntegerResult visitInteger(Type type) {
			return new IntegerResult(declaredType, (IInteger)value, ctx);
		}

		public ListResult visitList(Type type) {
			return new ListResult(declaredType, (IList)value, ctx);
		}

		public MapResult visitMap(Type type) {
			return new MapResult(declaredType, (IMap)value, ctx);
		}

		public ElementResult<? extends IValue> visitNode(Type type) {
			if (type.equals(Factory.Tree)) {
				return new ConcreteSyntaxResult(declaredType, (IConstructor)value, ctx);
			}
			return new NodeResult(declaredType, (INode)value, ctx);
		}

		public Result<? extends IValue> visitParameter(Type parameterType) {
			return parameterType.getBound().accept(this);
		}

		public RelationResult visitRelationType(Type type) {
			return new RelationResult(declaredType, (IRelation)value, ctx);
		}

		public SetOrRelationResult<ISet> visitSet(Type type) {
			return new SetResult(declaredType, (ISet)value, ctx);
		}

		public SourceLocationResult visitSourceLocation(Type type) {
			return new SourceLocationResult(declaredType, (ISourceLocation)value, ctx);		
		}

		public StringResult visitString(Type type) {
			return new StringResult(declaredType, (IString)value, ctx);
		}

		public TupleResult visitTuple(Type type) {
			return new TupleResult(declaredType, (ITuple)value, ctx);
		}

		public ValueResult visitValue(Type type) {
			return new ValueResult(declaredType, value, ctx);
		}

		public VoidResult visitVoid(Type type) {
			return new VoidResult(declaredType, ctx);
		}
	}
}
