package org.rascalmpl.library.vis.containers;

import java.util.Vector;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.INumber;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.impl.fast.ValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.library.vis.FigureFactory;
import org.rascalmpl.library.vis.IFigureApplet;
import org.rascalmpl.library.vis.properties.Properties;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.util.Key;
import org.rascalmpl.library.vis.util.NameResolver;

public class IntervalKey extends WithInnerFig implements Key {

	IValue explain;
	IValue interpolate;
	// TODO: can this type be more general? (i.e. Comparable)
	INumber low,high, interval;
	final private IEvaluatorContext ctx;
	private IList childProps;
	String id;
	
	public IntervalKey(IFigureApplet fpa, IValue interpolate, IValue explain, PropertyManager properties,IList childProps,IEvaluatorContext ctx){
		super(fpa,null,properties);
		this.ctx = ctx;
		this.childProps = childProps;
		id = getIdProperty();
		this.explain = explain;
		this.interpolate = interpolate;
	}
	

	public void init(){
		super.init();
		interval = high = low = null;
	}
	
	public void finalize(){
		interval = high.subtract(low);
		//System.out.printf("Nominal finalize bbox start!\n");
		if(innerFig != null){
			innerFig.destroy();
		}
		
		TypeFactory tf = TypeFactory.getInstance();
		Type[] argTypes = {tf.valueType(),tf.valueType()};
		IValue[] args = {low,high};
		IConstructor figureCons = (IConstructor)
			fpa.executeRascalCallBack(explain,argTypes,args).getValue();
		innerFig = FigureFactory.make(fpa, figureCons, properties, childProps, ctx);
		innerFig.init();
		innerFig.computeFiguresAndProperties();
		NameResolver resolver = new NameResolver(fpa, ctx);
		innerFig.registerNames(resolver);
		innerFig.registerValues(resolver);
		innerFig.getLikes(resolver);
		innerFig.finalize();
		properties = innerFig.properties;
	}
	
	public void bbox(){
		innerFig.bbox();
		minSize.set(innerFig.minSize);
		setResizable();
		//System.out.printf("Nominal key bbox done!\n");
	}
	
	@Override
	public void layout() {
		innerFig.size.set(size);
		innerFig.globalLocation.set(globalLocation);
		innerFig.layout();
		System.out.printf("Nominal key bbox done!\n");
	}
	
	public void registerNames(NameResolver resolver) {
		resolver.register(id,this);
	}

	@Override
	public void draw(double left, double top) {
		innerFig.draw(left, top);
		
	}



	@Override
	public void registerValue(Properties prop, IValue val) {
		if(!(val instanceof INumber)) return;
		INumber n = (INumber) val;
		if(low == null){
			low = n;
		} else if(n.less(low).getValue()){
			low = n;
		}
		if(high == null){
			high = n;
		} else if(n.greater(high).getValue()){
			high = n;
		}
	}



	@Override
	public void registerOffset(double offset) {
		return;
		
	}



	@Override
	public IValue scaleValue(IValue val) {
		if(!(val instanceof INumber)) return ValueFactory.getInstance().real(0);
		INumber n = (INumber) val;
		INumber part = n.subtract(low).divide(interval,10);
		TypeFactory tf = TypeFactory.getInstance();
		return 
			fpa.executeRascalCallBackSingleArgument(interpolate, tf.valueType(), part).getValue(); 
	}



	@Override
	public String getId() {
		return id;
	}
	
}
