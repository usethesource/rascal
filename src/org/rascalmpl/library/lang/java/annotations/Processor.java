package org.rascalmpl.library.lang.java.annotations;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;

import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IMapWriter;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.NullRascalMonitor;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.load.StandardLibraryContributor;
import org.rascalmpl.values.ValueFactoryFactory;

@SupportedAnnotationTypes(
		{"org.rascalmpl.library.lang.java.annotations.Analysis"
		,"org.rascalmpl.library.lang.java.annotations.Generator"
		,"org.rascalmpl.library.lang.java.annotations.Transformation"
		})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class Processor extends AbstractProcessor {
	private final Evaluator eval;
	private final IValueFactory vf;
	
	public Processor() {
		vf = ValueFactoryFactory.getValueFactory();
		GlobalEnvironment heap = new GlobalEnvironment();
		eval = new Evaluator(vf, new PrintWriter(System.err), new PrintWriter(System.out), new ModuleEnvironment("***processor***", heap), heap);
		eval.addRascalSearchPathContributor(StandardLibraryContributor.getInstance());
	}
	
	@Override
	public boolean process(Set<? extends TypeElement> annotations,
			RoundEnvironment roundEnv) {
		IMap options = convertOptions(processingEnv.getOptions());
		
		for (TypeElement t : annotations) {
			processingEnv.getMessager().printMessage(Kind.ERROR, "Hallo! " + options, t);
			
			if (t.getKind() != ElementKind.CLASS) {
				processingEnv.getMessager().printMessage(Kind.ERROR, "Ignoring annotation", t);
				continue;
			}
			
			for (Analysis a : t.getAnnotationsByType(Analysis.class)) {
				String module = getModule(a.fun());
				
				if (module != null && !module.isEmpty()) {
					eval.doImport(new NullRascalMonitor(), module);
				}
				
				String func = getFunction();
				
				if (func != null && !func.isEmpty()) {
					eval.getHeap().getModule(module);
				}
			}
		}
		
		return true;
	}

	private String getFunction() {
		return "main";
	}

	private String getModule(String fun) {
		return "Processors";
	}

	private IMap convertOptions(Map<String, String> options) {
		IMapWriter w = vf.mapWriter();
		for (Entry<String,String> e : options.entrySet()) {
			w.put(vf.string(e.getKey()), vf.string(e.getValue()));
		}
		return w.done();
	}
}
