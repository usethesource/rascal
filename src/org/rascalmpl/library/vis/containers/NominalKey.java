package org.rascalmpl.library.vis.containers;

import java.util.Vector;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.impl.fast.ValueFactory;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.library.vis.FigureFactory;
import org.rascalmpl.library.vis.IFigureApplet;
import org.rascalmpl.library.vis.properties.Properties;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.util.Key;
import org.rascalmpl.library.vis.util.NameResolver;


@SuppressWarnings("rawtypes")
public class NominalKey extends WithInnerFig implements Key{

	IValue whole;
	IList possibilities;
	Vector<IValue> originals;
	final private IEvaluatorContext ctx;
	private IList childProps;
	IValue[] tmpArray ;
	String id;
	
	public NominalKey(IFigureApplet fpa, IList possibilties, IValue whole, PropertyManager properties,IList childProps,IEvaluatorContext ctx){
		super(fpa,null,properties);
		this.ctx = ctx;
		this.childProps = childProps;
		this.whole = whole;
		this.possibilities = possibilties;
		this.originals = new Vector<IValue>(possibilties.length());
		tmpArray = new IValue[originals.size()];
		id = getIdProperty();
	}
	
	public void init(){
		super.init();
		originals.clear();
	}
	
	public void finalize(){
		super.computeFiguresAndProperties();
		if(innerFig != null){
			innerFig.destroy();
		}
		
		TypeFactory tf = TypeFactory.getInstance();
		
		IConstructor figureCons = (IConstructor) fpa.executeRascalCallBackSingleArgument(whole,tf.listType(tf.valueType()), ValueFactory.getInstance().list(originals.toArray(tmpArray))).getValue();
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
	}
	
	@Override
	public void layout() {
		innerFig.size.set(size);
		innerFig.globalLocation.set(globalLocation);
		innerFig.layout();
	}
	
	public void registerNames(NameResolver resolver) {
		resolver.register(id,this);
	}

	@Override
	public void draw(double left, double top) {
		innerFig.draw(left, top);
		
	}



	@Override
	public void registerValue(Properties prop, Object val) {
		System.out.printf("Registered!!!\n");
		for(int i = 0 ; i < originals.size() ; i++ ){
			if(originals.get(i).isEqual((IValue)val)){
				return ;
			}
		}
		if(originals.size()  < possibilities.length()){
			originals.add((IValue)val);
		} 
		
	}



	@Override
	public void registerOffset(double offset) {
		return;
		
	}



	@Override
	public Object scaleValue(Object val) {
		for(int i = 0 ; i < originals.size()  ; i++){
			if(originals.get(i).isEqual((IValue)val)){
				return possibilities.get(i);
			}
		}
		return possibilities.get(0);
	}



	@Override
	public String getId() {
		return id;
	}

}
