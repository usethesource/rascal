package org.rascalmpl.library.vis.util;

import static org.rascalmpl.library.vis.properties.Properties.ID;

import java.util.HashMap;
import java.util.Set;

import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.library.vis.figure.Figure;

public class NameResolver {
	
	private final NameResolver parent;
	private final HashMap<String, Object> localFigures;
	private final HashMap<String,NameResolver> children;
	private final IEvaluatorContext ctx;
	
	public NameResolver(NameResolver parent, IEvaluatorContext ctx){
		this.parent = parent;
		this.ctx = ctx;
		localFigures = new HashMap<String, Object>();
		children = new HashMap<String, NameResolver>();
	}
	
	public NameResolver(IEvaluatorContext ctx){
		this(null,ctx);
	}
	
	public void register(Figure fig){
		String id = fig.prop.getStr(ID);
		if(id != null && !id.equals("")){
			localFigures.put(id, fig);
		}
	}
	
	public void register(String name, Object fig){
		localFigures.put(name, fig);
	}
	
	public NameResolver newChild(String name){
		NameResolver child = new NameResolver(this, ctx);
		children.put(name, child);
		return child;
	}
	
	public Object resolve(String path){
		if(path.startsWith("../")){
			if(isRoot()){
				throw RuntimeExceptionFactory.figureException("Could not resolve " + path + ":no such parent", ctx.getValueFactory().string(""), ctx.getCurrentAST(),
						ctx.getStackTrace());
			} else {
				return parent.resolve(path.substring("../".length()));
			}
		} else if(path.startsWith("/")){
			return root().resolve(path);
		} else if(path.contains("/")){
			int nameSpaceEnd = path.indexOf("/");
			String nameSpace = path.substring(0,nameSpaceEnd);
			if(children.containsKey(nameSpace)){
				return children.get(nameSpace).resolve(path.substring(nameSpaceEnd+1));
			} else {
				throw RuntimeExceptionFactory.figureException("Could not resolve " + path + ":no such child namespace", ctx.getValueFactory().string(""), ctx.getCurrentAST(),
						ctx.getStackTrace());
			}
		} else {
			if(localFigures.containsKey(path)){
				return localFigures.get(path);
			} else if(!isRoot()){
				return parent.resolve(path);
			} else {
				return null;
			}
		}
	}
	
	public Figure resolveFigure(String path){
		Object res = resolve(path);
		if(res instanceof Figure){
			return (Figure)res;
		} else {
			return null;
		}
	}
	
	public MaxFontAscent resolveMaxFontAscent(String path){
		Object res = resolve(path);
		if(res instanceof MaxFontAscent){
			return (MaxFontAscent)res;
		} else {
			return null;
		}
	}
	
	public boolean isRoot() {
		return parent == null;
	}
	
	public NameResolver root(){
		if(isRoot()) {
			return this;
		} else {
			return parent.root();
		}
	}


	
	

}
