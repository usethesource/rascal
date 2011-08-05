package org.rascalmpl.library.vis.figure.keys;

import java.util.Vector;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.impl.fast.ValueFactory;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.library.vis.figure.FigureFactory;
import org.rascalmpl.library.vis.figure.combine.WithInnerFig;
import org.rascalmpl.library.vis.graphics.GraphicsContext;
import org.rascalmpl.library.vis.properties.Properties;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.swt.ICallbackEnv;
import org.rascalmpl.library.vis.swt.IFigureApplet;
import org.rascalmpl.library.vis.swt.IFigureConstructionEnv;
import org.rascalmpl.library.vis.util.Key;
import org.rascalmpl.library.vis.util.NameResolver;


public class NominalKey extends WithInnerFig implements Key{

	IValue whole;
	IList possibilities;
	Vector<IValue> originals;
	private IList childProps;
	IValue[] tmpArray ;
	String id;
	IFigureConstructionEnv env;
	
	public NominalKey(IFigureConstructionEnv env, IList possibilties, IValue whole, PropertyManager properties,IList childProps){
		super(null,properties);
		this.env = env;
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
		if(innerFig != null){
			innerFig.destroy();
		}
		
		TypeFactory tf = TypeFactory.getInstance();
		IList originalsL = ValueFactory.getInstance().list(originals.toArray(tmpArray));
		IConstructor figureCons = (IConstructor) env.getCallBackEnv().executeRascalCallBackSingleArgument(whole,tf.listType(tf.valueType()),originalsL).getValue();
		innerFig = FigureFactory.make(env, figureCons, properties, childProps);
		innerFig.init();
		innerFig.computeFiguresAndProperties(env.getCallBackEnv());
		NameResolver resolver = new NameResolver( env.getRascalContext());
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
	}
	
	public void registerNames(NameResolver resolver) {
		resolver.register(id,this);
	}

	@Override
	public void draw(GraphicsContext gc) {
		innerFig.draw(gc);
		
	}
	
	public void registerValue(Properties prop, IValue val) {
		for(int i = 0 ; i < originals.size() ; i++ ){
			if(originals.get(i).isEqual(val)){
				return ;
			}
		}
		if(originals.size()  < possibilities.length()){
			originals.add(val);
		} 
	}
	
	public void registerOffset(double offset) {
		return;
		
	}
	
	public IValue scaleValue(IValue val) {
		
		for(int i = 0 ; i < originals.size()  ; i++){
			if(originals.get(i).isEqual((IValue)val)){
				return possibilities.get(i);
			}
		}
		return possibilities.get(0);
	}
	
	public String getId() {
		return id;
	}

}
