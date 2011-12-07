package org.rascalmpl.library.experiments.resource;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.rascalmpl.ast.Command;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.ResourceResult;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.types.ReifiedType;

public class Resource {

	private static HashMap<String, IResource> resourceHandlers = new HashMap<String, IResource>();
	
	private final IValueFactory vf;

	public Resource(IValueFactory vf) {
		this.vf = vf;
	}

	public static void registerResourceHandler(String provider, IResource handler) {
		resourceHandlers.put(provider, handler);
	}
	
	private static void registerResourceHandler(IResource handler) {
		resourceHandlers.put(handler.getProviderString(), handler);
	}

	public static IResource getResourceHandler(ISourceLocation uri) {
		for (String key : resourceHandlers.keySet()) {
			if (uri.getURI().getHost().equals(key)) return resourceHandlers.get(key);
		}
		return null; // TODO: Throw here!
	}
	
	public void registerResource(IString javaClass, IEvaluatorContext ctx) {
		try {
			String className = javaClass.getValue();
			List<ClassLoader> loaders = ctx.getEvaluator().getClassLoaders();
			Class<?> resourceClass = null;
			for (ClassLoader loader : loaders) {
				try {
					resourceClass = loader.loadClass(className);
				} catch (ClassNotFoundException e) {
					continue;
				}
				break;
			}
			if (resourceClass != null) {
				IResource handler = (IResource)resourceClass.newInstance();
				Resource.registerResourceHandler(handler);
			} else {
				throw new ClassNotFoundException(className);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	public IValue getTypedResource(ISourceLocation uriLoc, IConstructor type, IEvaluatorContext ctx) {
		// TODO: We may not need this here, since we already create the same type internally
		// when we create the resource. Commenting out for now...
		Type resourceType = ((ReifiedType) type.getType()).getTypeParameters().getFieldType(0);
		IResource handler = Resource.getResourceHandler(uriLoc);
		ResourceResult rr = handler.createResource(ctx, uriLoc, resourceType);
		return rr.getValue();
	}
	
	public void generateTypedInterfaceInternal(IString tag, ISourceLocation uriLoc, IEvaluatorContext ctx) {
		IResource handler = Resource.getResourceHandler(uriLoc);
		String tagStr = tag.getValue();
		Type t = handler.getResourceType(ctx, uriLoc);
		PrintWriter currentOutStream = ctx.getStdOut();
		
		// Declare an alias to the type of the resource
		TypeStore ts = ctx.getCurrentEnvt().getStore();
		Type alias2t = TypeFactory.getInstance().aliasType(ts, tagStr + "Type", t);
		
		synchronized(currentOutStream){
			currentOutStream.println("Generated type alias " + alias2t.toString() + ": " + alias2t.getAliased().toString());
			currentOutStream.flush();
		}		
		
		// Declare a function that just uses the given URI. This way, if we provide
		// the complete URI up front, we don't need to keep providing it later.
		StringBuilder sb = new StringBuilder();
		sb.append("public ").append(tagStr).append("Type ").append(tagStr).append("() { ");
		sb.append(" return getTypedResource(").append(uriLoc.toString()).append(",#").append(tagStr).append("Type); }");
		IConstructor declTree = ctx.getEvaluator().parseCommand(ctx.getEvaluator().getMonitor(), sb.toString(), ctx.getCurrentAST().getLocation().getURI());
		Command cmd = ctx.getEvaluator().getBuilder().buildCommand(declTree);
		Environment env = ctx.getCurrentEnvt();
		ctx.setCurrentEnvt(env.getRoot());
		Result<IValue> fun0 = ctx.getEvaluator().eval(ctx.getEvaluator().getMonitor(), cmd);
		ctx.unwind(env);

		synchronized(currentOutStream){
			currentOutStream.println("Generated function " + fun0.toString());
			currentOutStream.flush();
		}		
	}
}
