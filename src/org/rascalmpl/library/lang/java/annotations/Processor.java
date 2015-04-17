package org.rascalmpl.library.lang.java.annotations;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ServiceLoader;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.AnnotationValueVisitor;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic.Kind;
import javax.tools.JavaFileObject;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.NullRascalMonitor;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.load.StandardLibraryContributor;
import org.rascalmpl.interpreter.result.ICallableValue;
import org.rascalmpl.uri.FileURIResolver;
import org.rascalmpl.values.ValueFactoryFactory;

@SupportedAnnotationTypes(
		{"org.rascalmpl.library.lang.java.annotations.Analysis"
		,"org.rascalmpl.library.lang.java.annotations.Generator"
		,"org.rascalmpl.library.lang.java.annotations.Transformation"
		})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class Processor extends AbstractProcessor {
	private static final String CLASSPATH = "classpath";
	private static final String SOURCEPATH = "sourcepath";
	private final Evaluator eval;
	private final IValueFactory vf;
	private Map<String,IValue> options;
	
	public Processor() {
		vf = ValueFactoryFactory.getValueFactory();
		GlobalEnvironment heap = new GlobalEnvironment();
		eval = new Evaluator(vf, new PrintWriter(System.err), new PrintWriter(System.out), new ModuleEnvironment("***processor***", heap), heap);
		eval.addRascalSearchPathContributor(StandardLibraryContributor.getInstance());
	}
	
	@Override
	public synchronized void init(ProcessingEnvironment processingEnv) {
		super.init(processingEnv);
		this.options = convertOptions(processingEnv.getOptions());
		inferMoreOptions();
		setRascalSearchPath();
	}

	private void setRascalSearchPath() {
		if (options.get(SOURCEPATH) != null) {
			IList sourcePath = (IList) options.get(SOURCEPATH);
			for (IValue elem : sourcePath) {
				eval.addRascalSearchPath(FileURIResolver.constructFileURI(((IString) elem).getValue()));
			}
		}
	}

	private IList filesToList(Iterable<? extends File> files) {
		IListWriter l = vf.listWriter();
		for (File f : files) {
			l.append(vf.string(f.toString()));
		}
		return l.done();
	}
	
	private void inferMoreOptions() {
		ServiceLoader<AnnotationsOptionsProvider> loader = ServiceLoader.load(AnnotationsOptionsProvider.class);
		for (AnnotationsOptionsProvider provider : loader) {
			this.options.putAll(convertOptions(provider.getOptions()));
			addToPath(SOURCEPATH, provider.getClassPath());
			addToPath(CLASSPATH, provider.getSourcePath());
		}
	}

	private void addToPath(String pathName, Iterable<? extends File> path) {
		IList files = filesToList(path);
		if (options.get(pathName) != null) {
			files = ((IList) options.get(pathName)).concat(files);
		}
		options.put(pathName, files);
	}


	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		processAnalyzers(roundEnv);
		processGenerators(roundEnv);
		return true;
	}
	
	public void processAnalyzers(RoundEnvironment roundEnv) {
		for (Element e : roundEnv.getElementsAnnotatedWith(Analysis.class)) {
			try {
				Analysis a = e.getAnnotation(Analysis.class);
				Map<String,IValue> parameters = inferParameters(e);
				ICallableValue func = inferFunction(a.fun());
				IList messages = (IList) func.call(new Type[0], new IValue[0], parameters).getValue();
				processMessages(e, messages);
			}
			catch (Throwable t) {
				info("Exception while processing annotation: " + t.getMessage(), e);
			}
		}
	}
	
	public void processGenerators(RoundEnvironment roundEnv) {
		for (Element e : roundEnv.getElementsAnnotatedWith(Generator.class)) {
			try {
				Generator a = e.getAnnotation(Generator.class);
				Map<String,IValue> parameters = inferParameters(e);
				ICallableValue func = inferFunction(a.fun());
				IMap classes = (IMap) func.call(new Type[0], new IValue[0], parameters).getValue();
				processGeneratedClasses(e, classes);
			}
			catch (Throwable t) {
				info("Exception while processing annotation: " + t.getMessage(), e);
			}
		}
	}

	private void processGeneratedClasses(Element e, IMap classes) throws IOException {
		for (IValue key : classes) {
			String name = ((IString) key).getValue();
			String source = ((IString) classes.get(key)).getValue();
			JavaFileObject file = processingEnv.getFiler().createSourceFile(name, e);
			try (PrintWriter out = new PrintWriter(file.openOutputStream())) {
				out.println(source);
			}
		}
	}

	private void processMessages(Element e, IList messages) {
		for (IValue val : messages) {
			IConstructor message = (IConstructor) val;
			switch (message.getName()) {
			case "info":
				info(getMessage(message), e);
				break;
			case "warning":
				warning(getMessage(message), e);
				break;
			case "error":
				error(getMessage(message), e);
				break;
			default:
				info(getMessage(message), e);
			}
		}
	}

	private ICallableValue inferFunction(String fun) {
		ModuleEnvironment mod = getModule(fun);
		if (mod == null) {
			throw new IllegalArgumentException("no module found for: " + fun);
		}
		
		ICallableValue func = getFunction(fun, mod);
		if (func == null) {
			throw new IllegalArgumentException("no function found for: " + fun);
		}
		return func;
	}
	
	public String getMessage(IConstructor message) {
		return ((IString) message.get("message")).getValue();
	}

	private Map<String, IValue> inferParameters(Element e) {
		Map<String,IValue> parameters = new HashMap<>();
		for (AnnotationMirror a : e.getAnnotationMirrors()) {
			String name = a.getAnnotationType().asElement().getSimpleName().toString();
			Map<String,IValue> values = convertElementValues(a.getElementValues());
			parameters.put(name, vf.node(name, values));
		}

		parameters.putAll(options);
		return parameters;
	}

	private Map<String, IValue> convertElementValues(Map<? extends ExecutableElement, ? extends AnnotationValue> elementValues) {
		Map<String,IValue> result = new HashMap<>();
		
		for (Entry<? extends ExecutableElement,? extends AnnotationValue> e : elementValues.entrySet()) {
			String name = e.getKey().getSimpleName().toString();
			IValue val = e.getValue().accept(new AnnotationValueVisitor<IValue, Void>() {
				@Override
				public IValue visit(AnnotationValue av, Void p) {
					return vf.string(av.toString());
				}

				@Override
				public IValue visit(AnnotationValue av) {
					return vf.string(av.toString());
				}

				@Override
				public IValue visitBoolean(boolean b, Void p) {
					return vf.bool(b);
				}

				@Override
				public IValue visitByte(byte b, Void p) {
					return vf.integer(b);
				}

				@Override
				public IValue visitChar(char c, Void p) {
					return vf.string(c);
				}

				@Override
				public IValue visitDouble(double d, Void p) {
					return vf.real(d);
				}

				@Override
				public IValue visitFloat(float f, Void p) {
					return vf.real(f);
				}

				@Override
				public IValue visitInt(int i, Void p) {
					return vf.integer(i);
				}

				@Override
				public IValue visitLong(long i, Void p) {
					return vf.integer(i);
				}

				@Override
				public IValue visitShort(short s, Void p) {
					return vf.integer(s);
				}

				@Override
				public IValue visitString(String s, Void p) {
					return vf.string(s);
				}

				@Override
				public IValue visitType(TypeMirror t, Void p) {
					return vf.string(t.toString());
				}

				@Override
				public IValue visitEnumConstant(VariableElement c, Void p) {
					return vf.string(c.toString());
				}

				@Override
				public IValue visitAnnotation(AnnotationMirror a, Void p) {
					return vf.string(a.toString());
				}

				@Override
				public IValue visitArray(List<? extends AnnotationValue> vals, Void p) {
					IListWriter w = vf.listWriter();
					for (AnnotationValue v : vals) {
						w.append(v.accept(this, p));
					}
					return w.done();
				}

				@Override
				public IValue visitUnknown(AnnotationValue av, Void p) {
					return vf.string(av.toString());
				}
			}, null);
			
			result.put(name, val);
		}
		
		return result;
	}

	private void warning(String msg, Element e) {
		processingEnv.getMessager().printMessage(Kind.WARNING, msg, e);
	}
	
	private void info(String msg) {
		processingEnv.getMessager().printMessage(Kind.NOTE, msg);
	}
	
	private void info(String msg, Element e) {
		processingEnv.getMessager().printMessage(Kind.NOTE, msg, e);
	}
	
	private void error(String msg, Element e) {
		processingEnv.getMessager().printMessage(Kind.ERROR, msg, e);
	}

	private ICallableValue getFunction(String fun, ModuleEnvironment mod) {
		int ind = fun.lastIndexOf("::");
		if (ind != -1) {
			String name = fun.substring(ind + 2);
			info("looking up function " + name);
			return (ICallableValue) mod.getVariable(name);
		}
		
		return null;
	}

	private ModuleEnvironment getModule(String fun) {
		int ind = fun.lastIndexOf("::");
		if (ind != -1) {
			String name = fun.substring(0, ind);
			info("importing " + name);
			eval.doImport(new NullRascalMonitor(), name);
			return eval.getHeap().getModule(name);
		}
		
		return null; 
	}

	private Map<String,IValue> convertOptions(Map<String, String> options) {
		Map<String,IValue> w = new HashMap<>();
		for (Entry<String,String> e : options.entrySet()) {
			switch (e.getKey()) {
			case SOURCEPATH:
			case CLASSPATH:
				IListWriter l = vf.listWriter();
				for (String elem : e.getValue().split(":")) {
					l.append(vf.string(elem));
				}
				w.put(e.getKey(), l.done());
				break;
			default:
				w.put(e.getKey(), vf.string(e.getValue()));
			}
		}
			
		return w;
	}
}
