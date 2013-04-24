package org.rascalmpl.interpreter.result;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static org.rascalmpl.interpreter.env.IsomorphicTypes.isAdt;
import static org.rascalmpl.interpreter.env.IsomorphicTypes.isFunctor;
import static org.rascalmpl.interpreter.env.IsomorphicTypes.isIsomorphic;
import static org.rascalmpl.interpreter.env.IsomorphicTypes.getIsomorphicType;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.control_exceptions.MatchFailed;
import org.rascalmpl.interpreter.env.IsomorphicTypes;
import org.rascalmpl.interpreter.types.FunctionType;
import org.rascalmpl.interpreter.types.RascalTypeFactory;

public class FAlgebraFunction extends AbstractFunction {
	
	// May be an f-algebra function
	private Result<IValue> phi;
	
	// May be a pair of attribute functions <f,g>
	private Result<IValue> f;
	private Result<IValue> g;
	
	// May define anamorphism, catamorphism, 
	// or type-preserving in case of attributes as annotations  
	boolean isAna = false;
	boolean isCata = false;
	
	// An f-algebra is defined in terms of a functor that defines an adt
	private Type isomorphicAdt;
	
	private static RascalTypeFactory RTF = RascalTypeFactory.getInstance();
	private static TypeFactory TF = TypeFactory.getInstance(); 
	
	
	public FAlgebraFunction(FunctionType type, Result<IValue> phi, IEvaluatorContext ctx) {	
		super(null, ctx.getEvaluator(), type, false, null, ctx.getCurrentEnvt());
		this.phi = phi;
		// Check if an f-algebra function is anamorphic or catamorphic
		Type adt = type.getArgumentTypes().getFieldType(0);
		if(isAdt(adt)) {
			if(isFunctor(ctx.getCurrentEnvt().lookupAbstractDataType(adt.getName()))) 
				this.isCata = true;
		}
		if(!this.isCata && isAdt(type.getReturnType())) {
			adt = type.getReturnType();
			if(isFunctor(ctx.getCurrentEnvt().lookupAbstractDataType(adt.getName()))) 
				this.isAna = true;
		}
		if(this.isAna || this.isCata) {
			this.isomorphicAdt = getIsomorphicType(ctx.getCurrentEnvt().lookupAbstractDataType(adt.getName()));
			if(this.isomorphicAdt.isParameterized()) {
				assert(type.getArgumentTypes().getArity() == 2);
				this.isomorphicAdt = type.getArgumentTypes().getFieldType(1).getTypeParameters().getFieldType(0);
			}
		}
		if(!(this.isCata || this.isAna) && type.getArgumentTypes().getFieldName(0).equals(type.getReturnType()))
			this.isomorphicAdt = adt;		
	}
	
	public FAlgebraFunction(FunctionType ftype, Result<IValue> f, FunctionType gtype, Result<IValue> g, IEvaluatorContext ctx) {
		super(null, ctx.getEvaluator(), computeType(ftype, gtype, ctx), false, null, ctx.getCurrentEnvt());	
		this.f = f;
		this.g = g;
		Type adt = this.getFunctionType().getArgumentTypes().getFieldType(0);	
		if(isAdt(adt) && isFunctor(ctx.getCurrentEnvt().lookupAbstractDataType(adt.getName()))) {
			this.isCata = true;
			this.isomorphicAdt = ctx.getCurrentEnvt().lookupAbstractDataType(adt.getName());
			if(this.isomorphicAdt.isParameterized()) {
				assert(this.getFunctionType().getArgumentTypes().getArity() == 2);
				this.isomorphicAdt = this.getFunctionType().getArgumentTypes().getFieldType(1).getTypeParameters().getFieldType(0);
			}
		}
		
		if(isAdt(adt) && !(this.isAna || this.isCata))
			this.isomorphicAdt = adt;
	}
	
	@Override
	public Result<IValue> call(Type[] argTypes, IValue[] argValues, Map<String, Result<IValue>> keyArgValues, Result<IValue> self, Map<String, Result<IValue>> openFunctions) throws MatchFailed {
		return null;
//		Environment environment = new Environment(declarationEnvironment, ctx.getCurrentEnvt(), null, null, "Anonymous Function");
//		ctx.setCurrentEnvt(environment);	
//		ctx.getCurrentEnvt().declareAndStoreInferredInnerScopeVariable("e", ResultFactory.makeResult(argTypes[0], argValues[0], ctx));
//		
//		return new AttributeEvaluator(((FunctionType) this.type).getReturnType(), ctx.getEvaluator());
	}
	
	@Override
	public boolean isAna() {
		return this.isAna;
	}
	
	@Override
	public boolean isCata() {
		return this.isCata;
	}
	
	@Override
	public Type getIsomorphicAdt() {
		return this.isomorphicAdt;
	}
	
	@Override
	public FunctionType getTraverseFunctionType() {
		if(this.isAna)
			return (FunctionType) RTF.functionType(this.getIsomorphicAdt(), TF.tupleType(this.getFunctionType().getArgumentTypes().getFieldType(0)));
		else if(this.isCata)
			return (FunctionType) RTF.functionType(this.getFunctionType().getReturnType(), TF.tupleType(this.getIsomorphicAdt()));
		return this.getFunctionType();
	}

	@Override
	public boolean isStatic() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean isDefault() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IEvaluator<Result<IValue>> getEval() {
		// TODO Auto-generated method stub
		return null;
	}
	
	// Constructs a set of overloaded functions
	public static Map<FunctionType, Result<IValue>> computeOverloadedFunctions(List<AbstractFunction> functions) {
		Map<FunctionType, List<AbstractFunction>> overloaded = new HashMap<FunctionType, List<AbstractFunction>>();
		for(AbstractFunction func : functions) {
			boolean isPartial = false;
			for(Entry<FunctionType, List<AbstractFunction>> entry : overloaded.entrySet()) {
				if(entry.getKey().getArgumentTypes().comparable(func.getFunctionType().getArgumentTypes())) {
					isPartial = true;
					overloaded.remove(entry.getKey());
					overloaded.put((FunctionType) RTF.functionType(entry.getKey().getReturnType().lub(func.getFunctionType().getReturnType()), 
										TF.tupleType(entry.getKey().getArgumentTypes().lub(func.getFunctionType().getArgumentTypes()))), 
									entry.getValue());
					break;
				}
			}
			if(!isPartial) {
				List<AbstractFunction> funcs = new LinkedList<AbstractFunction>();
				funcs.add(func);
				overloaded.put(func.getFunctionType(), funcs);
			}
		}
		
		Map<FunctionType, Result<IValue>> result = new HashMap<FunctionType, Result<IValue>>();
		for(Entry<FunctionType, List<AbstractFunction>> entry : overloaded.entrySet())
			result.put(entry.getKey(), new OverloadedFunction(entry.getValue().get(0).getName(), entry.getValue()));

		return result;
	}
	
	private static FunctionType computeType(FunctionType ftype, FunctionType gtype, IEvaluatorContext ctx) {
		// For attribute functions: (1) <y=f(x,E[ys]); xs=g(x,E[ys])>; (2) <y=f(Exp); xs=g(Exp)>, Exp@inh, Exp@syn
		Type inh = ftype.getArgumentTypes().getFieldType(0);
		
		if(isAdt(inh) && isAdt(gtype.getArgumentTypes().getFieldType(0)) && !isFunctor(inh))
			return (FunctionType) RTF.functionType(inh, TF.tupleType(inh));
		
		Type functor = ftype.getArgumentTypes().getFieldType(1);
		Type syns = functor.getTypeParameters();
		Type syn = ftype.getReturnType();
		Type inhs = gtype.getReturnType();
		
		int arity = syns.getArity();
		
		Type returnType = RTF.functionType(syn, TF.tupleType(inh));
		Type[] argumentTypes = new Type[arity];
		
		for(int i = 0; i < arity; i++)
			argumentTypes[i] = RTF.functionType(syns.getFieldType(i), TF.tupleType(inhs.getFieldType(i)));
		
		Type adt = null;
		if(ftype.getArgumentTypes().getArity() == 3)
			adt = ftype.getArgumentTypes().getFieldType(2);
		
		if(adt == null)
			return (FunctionType) RTF.functionType(returnType, TF.tupleType(ctx.getCurrentEnvt().abstractDataType(functor.getName(), argumentTypes)));
		return (FunctionType) RTF.functionType(returnType, TF.tupleType(ctx.getCurrentEnvt().abstractDataType(functor.getName(), argumentTypes), adt));
	}
	
	class AttributeEvaluator extends AbstractFunction {
		
		public AttributeEvaluator(Type type, IEvaluator<Result<IValue>> eval) {
			super(null, eval, (FunctionType) type, false, null, eval.getCurrentEnvt());
			
		}
		
		@Override
		public Result<IValue> call(Type[] argTypes, IValue[] argValues, Map<String, Result<IValue>> keyArgValues, Result<IValue> self, Map<String, Result<IValue>> openFunctions) throws MatchFailed {
			IValue inh = argValues[0];
			Result<IValue> e = ctx.getCurrentEnvt().getVariable("e");
			IConstructor value = (IConstructor) e.getValue();
			Type constr = value.getConstructorType();
			int arity = ((IConstructor) e.getValue()).arity();
			IValue[] syns = new IValue[arity];
			List<Result<IValue>> phis = new LinkedList<Result<IValue>>();
			for(int i = 0; i < arity; i++) {
				Result<IValue> phi = ResultFactory.makeResult(constr.getFieldType(i), value.get(i), ctx);
				phis.add(phi);
				syns[i] = phi.call(new Type[] { inh.getType() }, new IValue[] { inh }, null).getValue(); // initializing ...
			}
			
			boolean changed = true;
			
			while(changed) {
				changed = false;
				value = ctx.getValueFactory().constructor(constr, syns);
				for(int i = 0; i < arity; i++) {
					IValue prev = syns[i];
					Result<IValue> inh_i = g.call(new Type[] { inh.getType(), value.getType() }, new IValue[] { inh, value }, null);
					syns[i] = phis.get(i).call(new Type[] { inh_i.getType() }, new IValue[] { inh_i.getValue() }, null).getValue();
					if(!syns[i].equals(prev)) changed = true;
				}
			}
			
			return f.call(new Type[] { value.getType() }, new IValue[] { value }, null);
		}

		@Override
		public boolean isStatic() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isDefault() {
			// TODO Auto-generated method stub
			return false;
		}
		
	}
	
}
