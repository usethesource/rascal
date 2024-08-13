package org.rascalmpl.core.library.lang.rascalcore.compile.runtime;

import static org.rascalmpl.values.RascalValueFactory.TYPE_STORE_SUPPLIER;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.rascalmpl.core.library.lang.rascalcore.compile.runtime.traverse.Traverse;
import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.exceptions.JavaMethodLink;
import org.rascalmpl.exceptions.RuntimeExceptionFactory;
import org.rascalmpl.ideservices.IDEServices;
import org.rascalmpl.interpreter.utils.IResourceLocationProvider;
import org.rascalmpl.library.util.ToplevelType;
import org.rascalmpl.types.DefaultRascalTypeVisitor;
import org.rascalmpl.types.NonTerminalType;
import org.rascalmpl.types.RascalType;
import org.rascalmpl.types.RascalTypeFactory;
import org.rascalmpl.types.ReifiedType;
import org.rascalmpl.uri.SourceLocationURICompare;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.values.IRascalValueFactory;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.functions.IFunction;
import org.rascalmpl.values.parsetrees.ITree;
import org.rascalmpl.values.parsetrees.ProductionAdapter;
import org.rascalmpl.values.parsetrees.SymbolAdapter;
import org.rascalmpl.values.parsetrees.TreeAdapter;
import org.rascalmpl.values.parsetrees.TreeAdapter.FieldResult;

import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IDateTime;
import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IListWriter;
import io.usethesource.vallang.IMap;
import io.usethesource.vallang.IMapWriter;
import io.usethesource.vallang.INode;
import io.usethesource.vallang.INumber;
import io.usethesource.vallang.IRational;
import io.usethesource.vallang.IReal;
import io.usethesource.vallang.ISet;
import io.usethesource.vallang.ISetWriter;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.ITuple;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.exceptions.FactTypeUseException;
import io.usethesource.vallang.exceptions.InvalidDateTimeException;
import io.usethesource.vallang.io.StandardTextReader;
import io.usethesource.vallang.io.binary.stream.IValueInputStream;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;
import io.usethesource.vallang.type.TypeStore;


public abstract class $RascalModule /*extends ATypeFactory*/ {

	/*************************************************************************/
	/*		Utilities for generated code									 */
	/*************************************************************************/
  
    // ---- value factory for creating functions, reified types and parsers unique to the Rascal runtime 
     final protected IRascalValueFactory $RVF;
    
    // ---- library helper methods and fields  -------------------------------------------
  
    /*package*/  final PrintStream $OUT; 
    /*package*/  final PrintWriter $OUTWRITER;
    /*package*/  final PrintStream $ERR;
    /*package*/  final PrintWriter $ERRWRITER;
    /*package*/  final InputStream $IN;
    
    /*package*/  final IRascalMonitor $MONITOR;

    protected final RascalExecutionContext rex;
	
    protected final IValueFactory $VF;
    public final TypeFactory $TF;
    protected final RascalTypeFactory $RTF;
    public final TypeStore $TS;
	protected final Traverse $TRAVERSE;
	
	public final IBool Rascal_TRUE;
	public final IBool Rascal_FALSE;

	protected final FailReturnFromVoidException $failReturnFromVoidException;
    
    public $RascalModule(RascalExecutionContext rex){
    	this.rex = rex;
    	$IN = rex.getInStream();
    	$OUT = rex.getOutStream();
    	$OUTWRITER = rex.getOutWriter();
    	$ERR = rex.getErrStream();
    	$ERRWRITER = rex.getErrWriter();
    	$MONITOR = rex;
    	$VF = rex.getIValueFactory();
    	$RVF = rex.getRascalRuntimeValueFactory();
    	$TF = rex.getTypeFactory();
    	$TS = rex.getTypeStore();
    	$RTF = rex.getRascalTypeFactory();
    	rex.setModule(this);
    	$TRAVERSE = rex.getTraverse();
    	
    	Rascal_TRUE =  $VF.bool(true);
    	Rascal_FALSE =  $VF.bool(false);
    	$failReturnFromVoidException = new FailReturnFromVoidException();
    }
    
    public final IConstructor $reifiedAType(IConstructor t, IMap definitions) {
        return $RVF.reifiedType(t, definitions);
    }
    
    @SuppressWarnings("unchecked")
    protected <T> T $initLibrary(String className) {
        PrintWriter[] outputs = new PrintWriter[] { $OUTWRITER, $ERRWRITER };
        int writers = 0;

        OutputStream[] rawOutputs = new OutputStream[] { $OUT, $ERR };
        int rawWriters = 0;

        try{
            Class<?> clazz = getClass().getClassLoader().loadClass(className);

            if (clazz.getConstructors().length > 1) {
                throw new IllegalArgumentException("Rascal JavaBridge can only deal with one constructor. This class has multiple: " + clazz);
            }

			Constructor<?>[] constructors = clazz.getConstructors();

			if (constructors.length < 1) {
				throw new JavaMethodLink(className, "no public constructors found", new IllegalArgumentException(className));
			}
			else if (constructors.length != 1) {
				throw new JavaMethodLink(className, "more than one public constructor found", new IllegalArgumentException(className));
			}

            Constructor<?> constructor = constructors[0];

            Object[] args = new Object[constructor.getParameterCount()];
            Class<?>[] formals = constructor.getParameterTypes();

            for (int i = 0; i < constructor.getParameterCount(); i++) {
                if (formals[i].isAssignableFrom(IValueFactory.class)) {
                    args[i] = $VF;
                }
                else if (formals[i].isAssignableFrom(TypeStore.class)) {
                    args[i] = $TS;
                }
                else if (formals[i].isAssignableFrom(TypeFactory.class)) {
                    args[i] = TypeFactory.getInstance();
                }
                else if (formals[i].isAssignableFrom(PrintWriter.class)) {
                    args[i] = outputs[writers++ % 2];
                }
                else if (formals[i].isAssignableFrom(OutputStream.class)) {
                    args[i] = rawOutputs[rawWriters++ %2];
                }
                else if (formals[i].isAssignableFrom(InputStream.class)) {
                    args[i] = $IN;
                }
                else if (formals[i].isAssignableFrom(IRascalMonitor.class)) {
                    args[i] = $MONITOR;
                }
                else if (formals[i].isAssignableFrom(ClassLoader.class)) {
					// TODO: the classloaders have to become configurable later
                    args[i] = getClass().getClassLoader();
                }
                else if (formals[i].isAssignableFrom(IRascalValueFactory.class)) {
                    args[i] = new RascalRuntimeValueFactory(rex);
                }
                else if (formals[i].isAssignableFrom($RascalModule.class)) {
                    args[i] = this;
                }
				else if (formals[i].isAssignableFrom(IDEServices.class)) {
					if ($MONITOR instanceof IDEServices) {
						args[i] = (IDEServices) $MONITOR;
					}
					else {
						throw new IllegalArgumentException("No IDE services are available in this environment");
					}
				}
				else if (formals[i].isAssignableFrom(IResourceLocationProvider.class)) {
					// We provide resources directly from the run-time classpath of the current module.
					// This means that test-resources must be copied to the test target or the target folder 
					// before we run this code.
					args[i] = new IResourceLocationProvider() {
						@Override
						public Set<ISourceLocation> findResources(String fileName) {
							Set<ISourceLocation> result = new HashSet<>();

							try {
								for (URL found : Collections.list(getClass().getClassLoader().getResources(fileName))) {
									try {
										result.add($VF.sourceLocation(found.toURI()));
									} catch (URISyntaxException e) {
										$MONITOR.warning("WARNING: skipping " + found + " due to URI syntax exception", URIUtil.rootLocation("module-init"));
									}
								}
							}
							catch (IOException e) {
								// then we don't have anything. it could happens if the folder or jar of the currently running code has 
								// dissappeared while running the code in it.
							}
								
							return result;
						}
					};
				}
                else {
                    throw new IllegalArgumentException(constructor + " has unknown arguments. Only IValueFactory, TypeStore, ClassLoader, PrintWriter, OutputStream, InputStream, &T extends $RascalModule, IRascalValueFactory, TypeFactory and IResourceLocationProvider are supported");
                }
            }

            return (T) constructor.newInstance(args);
        }
        catch (ClassNotFoundException | NoClassDefFoundError | IllegalArgumentException | InstantiationException | IllegalAccessException | InvocationTargetException | SecurityException e) {
            throw new JavaMethodLink(className, e.getMessage(), e);
        }
    }
    
    private static void $usage(String module, String error, Type kwargs) {
        PrintWriter $ERR = new PrintWriter(System.err);
        
        if (!error.isEmpty() && !error.equals("help")) {
            $ERR.println(error);
        }
        
        $ERR.println("Usage: ");
        $ERR.println("java -cp ... " + module + " <options>");

        if (kwargs.getArity() > 0) {
            $ERR.println(" [options]\n\nOptions:\n");

            for (String param : kwargs.getFieldNames()) {
                $ERR.print("\t-");
                $ERR.print(param);
                if (kwargs.getFieldType(param).isSubtypeOf(TypeFactory.getInstance().boolType())) {
                    $ERR.println("\t[arg]: one of nothing (true), \'1\', \'0\', \'true\' or \'false\';");
                }
                else {
                    $ERR.println("\t[arg]: " + kwargs.getFieldType(param) + " argument;");
                }
            }
        }
        else {
            $ERR.println('\n');
        }
        
        $ERR.flush();
        
        if (!error.equals("help")) {
            throw new IllegalArgumentException();
        }
    }

    protected static Map<String, IValue> $parseCommandlineParameters(String module, String[] commandline, Type kwTypes) {
        IValueFactory $VF = ValueFactoryFactory.getValueFactory();
        TypeFactory $TF = TypeFactory.getInstance();
        
        Map<String, Type> expectedTypes = new HashMap<>();

        for (String kwp : kwTypes.getFieldNames()) {
            expectedTypes.put(kwp, kwTypes.getFieldType(kwp));
        }

        Map<String, IValue> params = new HashMap<>();

        for (int i = 0; i < commandline.length; i++) {
            if (commandline[i].equals("-help")) {
                $usage(module, "help", kwTypes);
            }
            else if (commandline[i].startsWith("-")) {
                String label = commandline[i].replaceFirst("^-+", "");
                Type expected = expectedTypes.get(label);

                if (expected == null) {
                   $usage(module, "unknown argument: " + label, kwTypes);
                }

                if (expected.isSubtypeOf(TypeFactory.getInstance().boolType())) {
                    if (i == commandline.length - 1 || commandline[i+1].startsWith("-")) {
                        params.put(label, $VF.bool(true));
                    }
                    else if (i < commandline.length - 1) {
                        String arg = commandline[++i].trim();
                        if (arg.equals("1") || arg.equals("true")) {
                            params.put(label, $VF.bool(true));
                        }
                        else {
                            params.put(label, $VF.bool(false));
                        }
                    }

                    continue;
                }
                else if (i == commandline.length - 1 || commandline[i+1].startsWith("-")) {
                    $usage(module, "expected option for " + label, kwTypes);
                }
                else if (expected.isSubtypeOf($TF.listType($TF.valueType()))) {
                    IListWriter writer = $VF.listWriter();

                    while (i + 1 < commandline.length && !commandline[i+1].startsWith("-")) {
                        writer.append($parseCommandlineOption(module, kwTypes, expected.getElementType(), commandline[++i]));
                    }

                    params.put(label, writer.done());
                }
                else if (expected.isSubtypeOf($TF.setType($TF.valueType()))) {
                    ISetWriter writer = $VF.setWriter();

                    while (i + 1 < commandline.length && !commandline[i+1].startsWith("-")) {
                        writer.insert($parseCommandlineOption(module, kwTypes, expected.getElementType(), commandline[++i]));
                    }

                    params.put(label, writer.done());
                }
                else {
                    params.put(label, $parseCommandlineOption(module, kwTypes, expected, commandline[++i]));
                }
            }
        }

        return params;
    }    

    private static IValue $parseCommandlineOption(String module, Type kwTypes, Type expected, String option) {
        TypeFactory $TF = TypeFactory.getInstance();
        IValueFactory $VF = ValueFactoryFactory.getValueFactory();
        
        if (expected.isSubtypeOf($TF.stringType())) {
            return $VF.string(option);
        }
        else {
            StringReader reader = new StringReader(option);
            try {
                return new StandardTextReader().read($VF, expected, reader);
            } catch (FactTypeUseException e) {
                $usage(module, "expected " + expected + " but got " + option + " (" + e.getMessage() + ")", kwTypes);
            } catch (IOException e) {
                $usage(module, "unexpected problem while parsing commandline:" + e.getMessage(), kwTypes);
            }
            
            throw new IllegalArgumentException();
        }
    }
    
	// ---- utility methods ---------------------------------------------------

	public final IMap $buildMap(final IValue...values){
		IMapWriter w = $VF.mapWriter();
		if(values.length % 2 != 0) throw new InternalCompilerError("$RascalModule: buildMap should have even number of arguments");
		for(int i = 0; i < values.length; i += 2) {
			w.put(values[i], values[i+1]);
		}
		return w.done();
	}
	
	public final boolean $intersectsType(Type t1, Type t2) {
		return t1.intersects(t2);
	}
	
	public final boolean $isComparable(Type t1, Type t2) {
		return $isSubtypeOf(t1, t2) || $isSubtypeOf(t2, t1);
	}
	
	private final boolean checkRightValueOrParam(Type left, Type right) {
		if(right.isTop()) {
			return true;
		}
		if(right.isParameter()) {
			return $isSubtypeOf(left, right.getBound());
		}
		return false;
	}
	
	//TODO: consider caching this method
	
	public final boolean $isSubtypeOf(Type left, Type right) {
		return left.isSubtypeOf(right);
	}
	
	public boolean $isTreeProductionEqual(IValue tree, IConstructor production) {
	    return (tree instanceof ITree) && ((org.rascalmpl.values.parsetrees.ITree) tree).isAppl() && (production).equals(((org.rascalmpl.values.parsetrees.ITree) tree).getProduction());
	}
	
	public boolean $isNonTerminal(Type treeType, IConstructor expected) {
		return treeType instanceof NonTerminalType && (((NonTerminalType) treeType).getSymbol().equals(expected));
	}
	
	public boolean $isNonTerminal(Type treeType, Type expected) {
	    // TODO: this is an inefficient test, but it does unify parameterized ADTs, sorts and lexes.
		if(treeType == expected) return true;
		if(treeType instanceof NonTerminalType) {
			NonTerminalType givenNT = (NonTerminalType) treeType;
			NonTerminalType expectedNT = (NonTerminalType)  expected;
		    return givenNT.getSymbol().equals(expectedNT.getSymbol()); // && givenNT.getArity() == expectedNT.getArity();
		}
		return false;

		//		return treeType instanceof NonTerminalType && (((NonTerminalType) treeType).toString()).equals(expected.toString());
	}
	
	 public IList readBinaryConstantsFile(Class<?> c, String path, int expected_length, String expected_md5Hash) {
		// The constants file has the structure: <int nconstants, str md5Hash, list[value] constants>
    	Type constantsFileType = $TF.tupleType($TF.integerType(), $TF.stringType(), $TF.listType($TF.valueType()));
    	
		ISourceLocation loc = null; 
		try {
			URL url = c.getClassLoader().getResource(path);
			if(url == null) {
				throw RuntimeExceptionFactory.io($VF.string("Cannot find resource " + path));
			}
			loc = $VF.sourceLocation(url.toURI());
		} catch (URISyntaxException e) {
			System.err.println("readBinaryConstantsFile: " + path + " throws " + e.getMessage());
		}

    	try (IValueInputStream in = constructValueReader(loc)) {
    		IValue constantsFile = in.read();;
    		if(constantsFile.getType().isSubtypeOf(constantsFileType)){
    			ITuple tup = (ITuple)constantsFile;
    			int found_length = ((IInteger)tup.get(0)).intValue();
    			if(found_length != expected_length) {
    				throw RuntimeExceptionFactory.io($VF.string("Expected " + expected_length + " constants, but only " + found_length + " found in " + path));
    			}
    			String found_hash = ((IString)tup.get(1)).getValue();
    			if(!found_hash.equals(expected_md5Hash)) {
    				throw RuntimeExceptionFactory.io($VF.string("Expected md5Hash " + expected_md5Hash + ", but got " + found_hash + " for " + path));
    			}
    			
    			return (IList) tup.get(2);
    		} else {
    			throw RuntimeExceptionFactory.io($VF.string("Requested type " + constantsFileType + ", but found " + constantsFile.getType()));
    		}
    	}
		catch (IOException e) {
			System.err.println("readBinaryConstantsFile: " + loc + " throws " + e.getMessage());
			throw RuntimeExceptionFactory.io($VF.string(e.getMessage()));
		}
		catch (Exception e) {
			System.err.println("readBinaryConstantsFile: " + loc + " throws " + e.getMessage());
			throw RuntimeExceptionFactory.io($VF.string(e.getMessage()));
		}
	}

    private IValueInputStream constructValueReader(ISourceLocation loc) throws IOException {
        URIResolverRegistry registry = URIResolverRegistry.getInstance();
        if (registry.supportsReadableFileChannel(loc)) {
            FileChannel channel = registry.getReadableFileChannel(loc);
            if (channel != null) {
                return new IValueInputStream(channel, $VF, TYPE_STORE_SUPPLIER);
            }
        }
        return new IValueInputStream(registry.getInputStream(loc), $VF, TYPE_STORE_SUPPLIER);
    }
	
	/*************************************************************************/
	/*		Rascal primitives called by generated code						 */
	/*************************************************************************/

	
	
	public final IInteger $aint_add_aint(final IInteger lhs, final IInteger rhs) {
		return lhs.add(rhs);
	}
	
	public final IReal $aint_add_areal(final IInteger lhs, final IReal rhs) {
		return lhs.add(rhs);
	}
	
	public final INumber $aint_add_arat(final IInteger lhs, final IRational rhs) {
		return lhs.add(rhs);
	}
	
	public final INumber $aint_add_anum(final IInteger lhs, final INumber rhs) {
		return lhs.add(rhs);
	}
	
	public final INumber $areal_add_aint(final IReal lhs, final IInteger rhs) {
		return lhs.add(rhs);
	}
	
	public final IReal $areal_add_areal(final IReal lhs, final IReal rhs) {
		return lhs.add(rhs);
	}
	
	public final INumber $areal_add_arat(final IReal lhs, final IRational rhs) {
		return lhs.add(rhs);
	}
	
	public final INumber $areal_add_anum(final IReal lhs, final INumber rhs) {
		return lhs.add(rhs);
	}
	
	public final INumber $arat_add_aint(final IRational lhs, final IInteger rhs) {
		return lhs.add(rhs);
	}
	
	public final INumber $arat_add_areal(final IRational lhs, final IReal rhs) {
		return lhs.add(rhs);
	}
	
	public final IRational $arat_add_arat(final IRational lhs, final IRational rhs) {
		return lhs.add(rhs);
	}
	
	public final INumber $arat_add_anum(final IRational lhs, final INumber rhs) {
		return lhs.add(rhs);
	}
	
	public final INumber $anum_add_aint(final INumber lhs, final IInteger rhs) {
		return lhs.add(rhs);
	}
	
	public final INumber $anum_add_areal(final INumber lhs, final IReal rhs) {
		return lhs.add(rhs);
	}
	
	public final INumber $anum_add_arat(final INumber lhs, final IRational rhs) {
		return lhs.add(rhs);
	}
	
	public final INumber $anum_add_anum(final INumber lhs, final INumber rhs) {
		return lhs.add(rhs);
	}
	
	public final IString $astr_add_astr(final IString lhs, final IString rhs) {
		return lhs.concat(rhs);
	}
	
	public final ISourceLocation $aloc_add_astr(final ISourceLocation sloc, final IString s) {
		String path = sloc.hasPath() ? sloc.getPath() : "";
		if(!path.endsWith(URIUtil.URI_PATH_SEPARATOR)){
			path = path + URIUtil.URI_PATH_SEPARATOR;
		}
		path = path.concat(s.getValue());
		return $aloc_field_update("path", $VF.string(path), sloc);
	}
	
	public final ITuple $atuple_add_atuple(final ITuple t1, final ITuple t2) {
		int len1 = t1.arity();
		int len2 = t2.arity();
		IValue elems[] = new IValue[len1 + len2];
		for(int i = 0; i < len1; i++)
			elems[i] = t1.get(i);
		for(int i = 0; i < len2; i++)
			elems[len1 + i] = t2.get(i);
		return $VF.tuple(elems);
	}
	
	public final IList $alist_add_alist(final IList lhs, final IList rhs) {
		return lhs.concat(rhs);
	}
	
	public final IList $alist_add_elm(final IList lhs, final IValue rhs) {
		return lhs.append(rhs);
	}
	
	public final IList $elm_add_alist(final IValue lhs, final IList rhs) {
		return rhs.insert(lhs);
	}
	
	public final ISet $aset_add_aset(final ISet lhs, final ISet rhs) {
		return lhs.union(rhs);
	}
	
	public final ISet $aset_add_elm(final ISet lhs, final IValue rhs) {
		return lhs.insert(rhs);
	}
	
	public final ISet $elm_add_aset(final IValue lhs, final ISet rhs) {
		return rhs.insert(lhs);
	}
	
	public final IMap $amap_add_amap(final IMap lhs, final IMap rhs) {
		return lhs.join(rhs);
	}

	// ---- annotation_get ----------------------------------------------------

	public final IValue $annotation_get(final IConstructor cons, final String fieldName) {
		if(cons.asWithKeywordParameters().hasParameter(fieldName)) {
			return cons.asWithKeywordParameters().getParameter(fieldName);
		}
		throw  RuntimeExceptionFactory.noSuchAnnotation(fieldName);
	}
	
	public final IValue $annotation_get(final INode cons, final String fieldName) {
		if(cons.asWithKeywordParameters().hasParameter(fieldName)) {
			return cons.asWithKeywordParameters().getParameter(fieldName);
		}
		throw  RuntimeExceptionFactory.noSuchAnnotation(fieldName);
	}

	public final GuardedIValue $guarded_annotation_get(final IConstructor cons, final String fieldName) {
		if(cons.asWithKeywordParameters().hasParameter(fieldName)) {
			return new GuardedIValue(cons.asWithKeywordParameters().getParameter(fieldName));
		}
		
//		Type consType = cons.getType();
//		Map<String, Type> kwps = $TS.getKeywordParameters(consType);

		if(TreeAdapter.isTree(cons) && TreeAdapter.isAppl((ITree) cons)) {
			// TODO: keyword parameter of Tree
			IConstructor prod = ((ITree) cons).getProduction();

			for(IValue elem : ProductionAdapter.getSymbols(prod)) {
				IConstructor arg = (IConstructor) elem;
				if (SymbolAdapter.isLabel(arg) && SymbolAdapter.getLabel(arg).equals(fieldName)) {
					return new GuardedIValue(arg);			        
				}
			}
		}
		return UNDEFINED;
	}
	
	public final GuardedIValue $guarded_annotation_get(final INode cons, final String fieldName) {
		if(cons.asWithKeywordParameters().hasParameter(fieldName)) {
			return new GuardedIValue(cons.asWithKeywordParameters().getParameter(fieldName));
		}
		return UNDEFINED;
	}

	// ---- assert_fails ------------------------------------------------------

	public final IBool $assert_fails(final IString message) {
		throw RuntimeExceptionFactory.assertionFailed(message);
	}

	// ---- create ------------------------------------------------------------

	public final ISourceLocation $create_aloc(final IString uri) {
		try {
			return URIUtil.createFromURI(uri.getValue());
		} 
		catch (URISyntaxException e) {
			// this is actually an unexpected run-time exception since Rascal prevents you from 
			// creating non-encoded 
			throw RuntimeExceptionFactory.malformedURI(uri.getValue());
		}
		catch (UnsupportedOperationException e) {
			throw RuntimeExceptionFactory.malformedURI(uri.getValue() + ":" + e.getMessage());
		}
	}

	/**
	 * Create a loc with given offsets and length
	 */
	public final ISourceLocation $create_aloc_with_offset(final ISourceLocation loc, final IInteger offset, final IInteger length) {
		return $VF.sourceLocation(loc, offset.intValue(), length.intValue());
	}
	
	public final ISourceLocation $create_aloc_with_offset_and_begin_end(final ISourceLocation loc, final IInteger offset, final IInteger length, final ITuple begin, final ITuple end) {
		int beginLine = ((IInteger) begin.get(0)).intValue();
		int beginCol = ((IInteger) begin.get(1)).intValue();

		int endLine = ((IInteger) end.get(0)).intValue();
		int endCol = ((IInteger)  end.get(1)).intValue();
		return $VF.sourceLocation(loc, offset.intValue(), length.intValue(), beginLine, endLine, beginCol, endCol);
	}

	

	public final IInteger $aint_divide_aint(final IInteger a, final IInteger b) {
		try {
			return a.divide(b);
		} catch(ArithmeticException e) {
			throw RuntimeExceptionFactory.arithmeticException("divide by zero");
		}
	}

	public final INumber $aint_divide_areal(final IInteger a, final IReal b) {
		try {
			return a.multiply($VF.real(1.0)).divide(b,  $VF.getPrecision());
		} catch(ArithmeticException e) {
			throw RuntimeExceptionFactory.arithmeticException("divide by zero");
		}
	}

	public final IRational $aint_divide_arat(final IInteger a, final IRational b) {
		try {
			return a.toRational().divide(b);
		} catch(ArithmeticException e) {
			throw RuntimeExceptionFactory.arithmeticException("divide by zero");
		}
	}

	public final INumber $aint_divide_anum(final IInteger a, final INumber b) {
		try {
			return a.multiply($VF.real(1.0)).divide(b, $VF.getPrecision());
		} catch(ArithmeticException e) {
			throw RuntimeExceptionFactory.arithmeticException("divide by zero");
		}
	}

	public final IReal $areal_divide_aint(final IReal a, final IInteger b) {
		try {
			return (IReal) a.divide(b, $VF.getPrecision());
		} catch(ArithmeticException e) {
			throw RuntimeExceptionFactory.arithmeticException("divide by zero");
		}
	}

	public final IReal $areal_divide_areal(final IReal a, final IReal b) {
		try {
			return a.divide(b, $VF.getPrecision());
		} catch(ArithmeticException e) {
			throw RuntimeExceptionFactory.arithmeticException("divide by zero");
		}
	}

	public final IReal $areal_divide_arat(IReal a, IRational b) {
		try {
			return (IReal) a.divide(b, $VF.getPrecision());
		} catch(ArithmeticException e) {
			throw RuntimeExceptionFactory.arithmeticException("divide by zero");
		}
	}

	public final INumber $areal_divide_anum(final IReal a, final INumber b) {
		try {
			return a.divide(b, $VF.getPrecision());
		} catch(ArithmeticException e) {
			throw RuntimeExceptionFactory.arithmeticException("divide by zero");
		}
	}

	public final IRational $arat_divide_aint(final IRational a, final IInteger b) {
		try {
			return a.divide(b);
		} catch(ArithmeticException e) {
			throw RuntimeExceptionFactory.arithmeticException("divide by zero");
		}
	}

	public final IReal $arat_divide_areal(final IRational a, final IReal b) {
		try {
			return a.multiply($VF.real(1.0)).divide(b,  $VF.getPrecision());
		} catch(ArithmeticException e) {
			throw RuntimeExceptionFactory.arithmeticException("divide by zero");
		}
	}

	public final IRational $arat_divide_arat(final IRational a, final IRational b) {
		try {
			return a.toRational().divide(b);
		} catch(ArithmeticException e) {
			throw RuntimeExceptionFactory.arithmeticException("divide by zero");
		}
	}

	public final INumber $arat_divide_anum(final IRational a, final INumber b) {
		try {
			return a.multiply($VF.real(1.0)).divide(b, $VF.getPrecision());
		} catch(ArithmeticException e) {
			throw RuntimeExceptionFactory.arithmeticException("divide by zero");
		}
	}

	public final INumber $anum_divide_aint(final INumber a, final IInteger b) {
		try {
			return a.divide(b, $VF.getPrecision());
		} catch(ArithmeticException e) {
			throw RuntimeExceptionFactory.arithmeticException("divide by zero");
		}
	}

	public final INumber $anum_divide_areal(final INumber a, final IReal b) {
		try {
			return a.divide(b, $VF.getPrecision());
		} catch(ArithmeticException e) {
			throw RuntimeExceptionFactory.arithmeticException("divide by zero");
		}
	}
	public final INumber $anum_divide_arat(final INumber a, final IRational b) {
		try {
			return a.divide(b, $VF.getPrecision());
		} catch(ArithmeticException e) {
			throw RuntimeExceptionFactory.arithmeticException("divide by zero");
		}
	}

	public final INumber $anum_divide_anum(final INumber a, final INumber b) {
		try {
			return a.divide(b, $VF.getPrecision());
		} catch(ArithmeticException e) {
			throw RuntimeExceptionFactory.arithmeticException("divide by zero");
		}
	}

	// ---- equal -------------------------------------------------------------

	public final IBool $equal(final IValue left, final IValue right) {
		Type leftType = left.getType();
		Type rightType = right.getType();
		if (leftType.isSubtypeOf($TF.numberType()) && rightType.isSubtypeOf($TF.numberType())) {
			return ((INumber)left).equal((INumber)right);
		} else if(leftType.isNode() && rightType.isNode()){
			return ((INode) left).equals((INode) right) ? Rascal_TRUE : Rascal_FALSE;
		} else if(left instanceof ITree && right instanceof ITree) {
			return $VF.bool(left.equals(right)); // use match to ignore "src" keyword parameters in trees
		} else {
			return $VF.bool(left.equals(right));
		}
	}
	
	// ---- get name ----------------------------------------------------------
	
	public final IString $anode_get_name(final INode nd) {
		return $VF.string(nd.getName());
	}

	// ---- get_field ---------------------------------------------------------

	public final IValue $anode_get_field(final INode nd, final String fieldName) {
		IValue res = nd.asWithKeywordParameters().getParameter(fieldName);
		if(res != null) {
			return res;
		}
		if(nd instanceof IConstructor) {
			IConstructor c = (IConstructor) nd;
			if(c.has(fieldName)) {
				return c.get(fieldName);
			}
			
			IValue res1 = c.asWithKeywordParameters().getParameter(fieldName);
			if(res1 != null) {
				return res1;
			}

		}
		throw RuntimeExceptionFactory.noSuchField(fieldName);
	}
	
	public final GuardedIValue $guarded_anode_get_field(final INode nd, final String fieldName) {
		try {
			IValue result = $anode_get_field(nd, fieldName);
			return new GuardedIValue(result);
		} catch (RuntimeException e) {
			return UNDEFINED;
		}
	}
	
	public final IValue $aadt_get_field(final IConstructor cons, final String fieldName) {
		Type consType = cons.getConstructorType();
		
		if(TreeAdapter.isTree(cons) && TreeAdapter.isAppl((ITree) cons)) {				
			FieldResult fldres = TreeAdapter.getLabeledField((ITree) cons, fieldName);
			if(fldres != null) {
				ITree res = TreeAdapter.getLabeledField((ITree) cons, fieldName).tree;
				if(res != null) {
					return res;
				}
			}
		}

		// Does fieldName exist as positional field?
		if(consType.hasField(fieldName)){
			IValue res = cons.get(fieldName);
			return res;
		}

		IValue result = cons.asWithKeywordParameters().getParameter(fieldName);
		if(result != null) {
			return result;
		}

		throw RuntimeExceptionFactory.noSuchField(fieldName);
	}
	
	public final GuardedIValue $guarded_aadt_get_field(final IConstructor cons, final String fieldName) {
		try {
			IValue result = $aadt_get_field(cons, fieldName);
			return new GuardedIValue(result);
		} catch (RuntimeException e) {
			return UNDEFINED;
		}
	}

	public final IValue $aloc_get_field(final ISourceLocation sloc, final String field) {
		IValue v;
		switch (field) {

		case "scheme":
			String s = sloc.getScheme();
			v = $VF.string(s == null ? "" : s);
			break;

		case "authority":
			v = $VF.string(sloc.hasAuthority() ? sloc.getAuthority() : "");
			break;

		case "host":
			if (!URIResolverRegistry.getInstance().supportsHost(sloc)) {
				throw RuntimeExceptionFactory.noSuchField("The scheme " + sloc.getScheme() + " does not support the host field, use authority instead.");
			}
			s = sloc.getURI().getHost();
			v = $VF.string(s == null ? "" : s);
			break;

		case "path":
			v = $VF.string(sloc.hasPath() ? sloc.getPath() : URIUtil.URI_PATH_SEPARATOR);
			break;

		case "parent":
			String path = sloc.getPath();
			if (path.equals("") || path.equals(URIUtil.URI_PATH_SEPARATOR)) {
				throw RuntimeExceptionFactory.noParent(sloc);
			}
			// remove one or more /'s at the end
			while (path.endsWith(URIUtil.URI_PATH_SEPARATOR)) {
				path = path.substring(0, path.length() - URIUtil.URI_PATH_SEPARATOR.length());
			}
			int i = path.lastIndexOf(URIUtil.URI_PATH_SEPARATOR);

			if (i != -1) {
				path = path.substring(0, i);
				if (sloc.getScheme().equalsIgnoreCase("file")) {
					// there is a special case for file references to windows paths.
					// the root path should end with a / (c:/ not c:)
					if (path.lastIndexOf(URIUtil.URI_PATH_SEPARATOR) == 0 && path.endsWith(":")) {
						path += URIUtil.URI_PATH_SEPARATOR;
					}
				}
				v = $aloc_field_update("path", $VF.string(path), sloc);
			} else {
				throw RuntimeExceptionFactory.noParent(sloc);
			}
			break;	

		case "file": 
			path = sloc.hasPath() ? sloc.getPath() : "";
			
			while(path.endsWith(URIUtil.URI_PATH_SEPARATOR)) {
				path = path.substring(0, path.length() - URIUtil.URI_PATH_SEPARATOR.length());
			}

			i = path.lastIndexOf(URIUtil.URI_PATH_SEPARATOR);

			if (i != -1) {
				path = path.substring(i+URIUtil.URI_PATH_SEPARATOR.length());
			}
			v = $VF.string(path);	
			break;

		case "ls":
			ISourceLocation resolved = sloc;
			if(URIResolverRegistry.getInstance().exists(resolved) && URIResolverRegistry.getInstance().isDirectory(resolved)){
				IListWriter w = $VF.listWriter();

				try {
					for (ISourceLocation elem : URIResolverRegistry.getInstance().list(resolved)) {
						w.append(elem);
					}
				}
				catch (FactTypeUseException | IOException e) {
					throw RuntimeExceptionFactory.io($VF.string(e.getMessage()));
				}

				v = w.done();
				break;
			} else {
				throw RuntimeExceptionFactory.io($VF.string("You can only access ls on a directory, or a container."));
			}

		case "extension":
			path = sloc.hasPath() ? sloc.getPath() : "";
			while(path.endsWith(URIUtil.URI_PATH_SEPARATOR)) {
				path = path.substring(0, path.length() - 1);
			}
			int slashIndex = path.lastIndexOf(URIUtil.URI_PATH_SEPARATOR);
			
			if (slashIndex == -1) {
				// empty path
				v = $VF.string("");
			}
			else {
				int j = path.substring(slashIndex).lastIndexOf((int)'.');

				if (j != -1) {
					v = $VF.string(path.substring(slashIndex + j + 1));
				}
				else {
					v = $VF.string("");
				}
			}
			
			break;

		case "fragment":
			v = $VF.string(sloc.hasFragment() ? sloc.getFragment() : "");
			break;

		case "query":
			v = $VF.string(sloc.hasQuery() ? sloc.getQuery() : "");
			break;

		case "params":
			String query = sloc.hasQuery() ? sloc.getQuery() : "";
			IMapWriter res = $VF.mapWriter();

			if (query.length() > 0) {
				String[] params = query.split("&");
				for (String param : params) {
					String[] keyValue = param.split("=");
					res.put($VF.string(keyValue[0]), $VF.string(keyValue[1]));
				}
			}
			v = res.done();
			break;

		case "user":
			if (!URIResolverRegistry.getInstance().supportsHost(sloc)) {
				throw RuntimeExceptionFactory.noSuchField("The scheme " + sloc.getScheme() + " does not support the user field, use authority instead.");
			}
			s = sloc.getURI().getUserInfo();
			v = $VF.string(s == null ? "" : s);
			break;

		case "port":
			if (!URIResolverRegistry.getInstance().supportsHost(sloc)) {
				throw RuntimeExceptionFactory.noSuchField("The scheme " + sloc.getScheme() + " does not support the port field, use authority instead.");
			}
			int n = sloc.getURI().getPort();
			v = $VF.integer(n);
			break;	

		case "length":
			if(sloc.hasOffsetLength()){
				v = $VF.integer(sloc.getLength());
				break;
			} else {
				throw RuntimeExceptionFactory.unavailableInformation(/*"length",*/ null, null);
			}

		case "offset":
			if(sloc.hasOffsetLength()){
				v = $VF.integer(sloc.getOffset());
				break;
			} else {
				throw RuntimeExceptionFactory.unavailableInformation(/*"offset",*/ null, null);
			}

		case "begin":
			if(sloc.hasLineColumn()){
				v = $VF.tuple($VF.integer(sloc.getBeginLine()), $VF.integer(sloc.getBeginColumn()));
				break;
			} else {
				throw RuntimeExceptionFactory.unavailableInformation(/*"begin",*/ null, null);
			}
		case "end":
			if(sloc.hasLineColumn()){
				v = $VF.tuple($VF.integer(sloc.getEndLine()), $VF.integer(sloc.getEndColumn()));
				break;
			} else {
				throw RuntimeExceptionFactory.unavailableInformation(/*"end",*/ null, null);
			}

		case "uri":
			v = $VF.string(sloc.getURI().toString());
			break;

		case "top":
			v = sloc.top();
			break;

		default:
			throw RuntimeExceptionFactory.noSuchField(field);
		}

		return v;
	}

	public final GuardedIValue $guarded_aloc_get_field(final ISourceLocation sloc, final String field) {
		try {
			IValue result = $aloc_get_field(sloc, field);
			return new GuardedIValue(result);
		} catch (RuntimeException e) {
			return UNDEFINED;
		}
	}

	public final IValue $adatetime_get_field(final IDateTime dt, final String field) {
		IValue v;
		try {
			switch (field) {
			case "isDate":
				v = $VF.bool(dt.isDate());
				break;
			case "isTime":
				v = $VF.bool(dt.isTime());
				break;
			case "isDateTime":
				v = $VF.bool(dt.isDateTime());
				break;
			case "century":
				if (!dt.isTime()) {
					v = $VF.integer(dt.getCentury());
					break;
				}
				throw RuntimeExceptionFactory.unavailableInformation(/*"Can not retrieve the century on a time value",*/ null, null);
			case "year":
				if (!dt.isTime()) {
					v = $VF.integer(dt.getYear());
					break;
				}
				throw RuntimeExceptionFactory.unavailableInformation(/*"Can not retrieve the year on a time value",*/ null, null);

			case "month":
				if (!dt.isTime()) {
					v = $VF.integer(dt.getMonthOfYear());
					break;
				}
				throw RuntimeExceptionFactory.unavailableInformation(/*"Can not retrieve the month on a time value",*/ null, null);
			case "day":
				if (!dt.isTime()) {
					v = $VF.integer(dt.getDayOfMonth());
					break;
				}
				throw RuntimeExceptionFactory.unavailableInformation(/*"Can not retrieve the day on a time value",*/ null, null);
			case "hour":
				if (!dt.isDate()) {
					v = $VF.integer(dt.getHourOfDay());
					break;
				}
				throw RuntimeExceptionFactory.unavailableInformation(/*"Can not retrieve the hour on a date value",*/ null, null);
			case "minute":
				if (!dt.isDate()) {
					v = $VF.integer(dt.getMinuteOfHour());
					break;
				}
				throw RuntimeExceptionFactory.unavailableInformation(/*"Can not retrieve the minute on a date value",*/ null, null);
			case "second":
				if (!dt.isDate()) {
					v = $VF.integer(dt.getSecondOfMinute());
					break;
				}
				throw RuntimeExceptionFactory.unavailableInformation(/*"Can not retrieve the second on a date value",*/ null, null);
			case "millisecond":
				if (!dt.isDate()) {
					v = $VF.integer(dt.getMillisecondsOfSecond());
					break;
				}
				throw RuntimeExceptionFactory.unavailableInformation(/*"Can not retrieve the millisecond on a date value",*/ null, null);
			case "timezoneOffsetHours":
				if (!dt.isDate()) {
					v = $VF.integer(dt.getTimezoneOffsetHours());
					break;
				}
				throw RuntimeExceptionFactory.unavailableInformation(/*"Can not retrieve the timezone offset hours on a date value",*/ null, null);
			case "timezoneOffsetMinutes":
				if (!dt.isDate()) {
					v = $VF.integer(dt.getTimezoneOffsetMinutes());
					break;
				}
				throw RuntimeExceptionFactory.unavailableInformation(/*"Can not retrieve the timezone offset minutes on a date value",*/ null, null);

			case "justDate":
				if (!dt.isTime()) {
					v = $VF.date(dt.getYear(), dt.getMonthOfYear(), dt.getDayOfMonth());
					break;
				}
				throw RuntimeExceptionFactory.unavailableInformation(/*"Can not retrieve the date component of a time value",*/ null, null);
			case "justTime":
				if (!dt.isDate()) {
					v = $VF.time(dt.getHourOfDay(), dt.getMinuteOfHour(), dt.getSecondOfMinute(), 
							dt.getMillisecondsOfSecond(), dt.getTimezoneOffsetHours(),
							dt.getTimezoneOffsetMinutes());
					break;
				}
				throw RuntimeExceptionFactory.unavailableInformation(/*"Can not retrieve the time component of a date value",*/ null, null);
			default:
				throw RuntimeExceptionFactory.noSuchField(field);
			}
			return v;

		} catch (InvalidDateTimeException e) {
			throw RuntimeExceptionFactory.illegalArgument(dt);
		}
	}
	
	public final GuardedIValue $guarded_datetime_get_field(final IDateTime dt, final String field) {
		try {
			IValue result = $adatetime_get_field(dt, field);
			return new GuardedIValue(result);
		} catch (RuntimeException e) {
			return UNDEFINED;
		}
	}
	
	public final IValue $atuple_get_field(final ITuple tup, final String fieldName) {
		IValue result = tup.get(fieldName);
		if(result == null) {
			throw RuntimeExceptionFactory.noSuchField(fieldName);
		}
		return result;
	}
	
	public final IValue $atuple_get_field_by_index(final ITuple tup, final int index) {
		IValue result = tup.get(index);
//		if(result == null) {
//			throw RuntimeExceptionFactory.noSuchField(index);
//		}
		return result;
	}
	
	public final GuardedIValue $guarded_atuple_get_field(final ITuple tup, final String fieldName) {
		try {
			IValue result = $atuple_get_field(tup, fieldName);
			return new GuardedIValue(result);
		} catch (RuntimeException e) {
			return UNDEFINED;
		}
	}
	
	public final IValue $areified_get_field(final IConstructor rt, final String field) {
		return rt.get(field);
	}

	// ---- field_project -----------------------------------------------------

	@SuppressWarnings("deprecation")
	public final IValue $atuple_field_project(final ITuple tup, final IValue... fields) {
		int n = fields.length;
		IValue [] newFields = new IValue[n];
		for(int i = 0; i < n; i++){
			IValue field = fields[i];
			newFields[i] = field.getType().isInteger() ? tup.get(((IInteger) field).intValue())
					: tup.get(((IString) field).getValue());
		}
		return (n - 1 > 1) ? $VF.tuple(newFields) : newFields[0];
	}
	
	public final GuardedIValue $guarded_atuple_field_project(final ITuple tup, final IValue... fields) {
		try {
			return new GuardedIValue($atuple_field_project(tup, fields));
		} catch (Exception e) {
			return UNDEFINED;
		}
	}

	public final ISet $amap_field_project (final IMap map, final IValue... fields) {
		ISetWriter w = $VF.setWriter();
		int indexArity = fields.length;
		int intFields[] = new int[indexArity];
		for(int i = 0; i < indexArity; i++){
			intFields[i]  = ((IInteger) fields[i]).intValue();
		}
		IValue[] elems = new IValue[indexArity];
		Iterator<Entry<IValue,IValue>> iter = map.entryIterator();
		while (iter.hasNext()) {
			Entry<IValue,IValue> entry = iter.next();
			for(int j = 0; j < fields.length; j++){
				elems[j] = intFields[j] == 0 ? entry.getKey() : entry.getValue();
			}
			w.insert((indexArity > 1) ? $VF.tuple(elems) : elems[0]);
		}
		return w.done();
	}
	
	public final GuardedIValue $guarded_amap_field_project(final IMap map, final IValue... fields) {
		try {
			return new GuardedIValue($amap_field_project(map, fields));
		} catch (Exception e) {
			return UNDEFINED;
		}
	}

	public final ISet $arel_field_project(final ISet set, final IValue... fields) {
		int indexArity = fields.length;
		int intFields[] = new int[indexArity];
		for(int i = 0; i < indexArity; i++){
			intFields[i]  = ((IInteger) fields[i]).intValue();
		}
		return set.asRelation().project(intFields);
	}
	
	public final GuardedIValue $guarded_arel_field_project(final ISet set, final IValue... fields) {
		try {
			return new GuardedIValue($arel_field_project(set, fields));
		} catch (Exception e) {
			return UNDEFINED;
		}
	}

	public final IList $alrel_field_project(final IList lrel, final IValue... fields) {
		int indexArity = fields.length;
		int intFields[] = new int[indexArity];
		for(int i = 0; i < indexArity; i++){
			intFields[i]  = ((IInteger) fields[i]).intValue();
		}
		IListWriter w = $VF.listWriter();
		IValue[] elems = new IValue[indexArity];
		for(IValue vtup : lrel){
			ITuple tup = (ITuple) vtup;
			for(int j = 0; j < fields.length; j++){
				elems[j] = tup.get(intFields[j]);
			}
			w.append((indexArity > 1) ? $VF.tuple(elems) : elems[0]);
		}
		return w.done();
	}
	
	public final GuardedIValue $guarded_alrel_field_project(final IList lrel, final IValue... fields) {
		try {
			return new GuardedIValue($alrel_field_project(lrel, fields));
		} catch (Exception e) {
			return UNDEFINED;
		}
	}

	// ---- field_update ------------------------------------------------------

	public final ISourceLocation $aloc_field_update(final String field, final IValue repl, final ISourceLocation sloc) {		
		Type replType = repl.getType();

		int iLength = sloc.hasOffsetLength() ? sloc.getLength() : -1;
		int iOffset = sloc.hasOffsetLength() ? sloc.getOffset() : -1;
		int iBeginLine = sloc.hasLineColumn() ? sloc.getBeginLine() : -1;
		int iBeginColumn = sloc.hasLineColumn() ? sloc.getBeginColumn() : -1;
		int iEndLine = sloc.hasLineColumn() ? sloc.getEndLine() : -1;
		int iEndColumn = sloc.hasLineColumn() ? sloc.getEndColumn() : -1;
		URI uri;
		boolean uriPartChanged = false;
		String scheme = sloc.getScheme();
		String authority = sloc.hasAuthority() ? sloc.getAuthority() : "";
		String path = sloc.hasPath() ? sloc.getPath() : null;
		String query = sloc.hasQuery() ? sloc.getQuery() : null;
		String fragment = sloc.hasFragment() ? sloc.getFragment() : null;

		try {
			String newStringValue = null;
			if(replType.isString()){
				newStringValue = ((IString)repl).getValue();
			}

			switch (field) {

			case "uri":
				uri = URIUtil.createFromEncoded(newStringValue);
				// now destruct it again
				scheme = uri.getScheme();
				authority = uri.getAuthority();
				path = uri.getPath();
				query = uri.getQuery();
				fragment = uri.getFragment();
				uriPartChanged = true;
				break;

			case "scheme":
				scheme = newStringValue;
				uriPartChanged = true;
				break;

			case "authority":
				authority = newStringValue;
				uriPartChanged = true;
				break;

			case "host":
				if (!URIResolverRegistry.getInstance().supportsHost(sloc)) {
					throw RuntimeExceptionFactory.noSuchField("The scheme " + sloc.getScheme() + " does not support the host field, use authority instead.");
				}
				uri = URIUtil.changeHost(sloc.getURI(), newStringValue);
				authority = uri.getAuthority();
				uriPartChanged = true;
				break;

			case "path":
				path = newStringValue;
				uriPartChanged = true;
				break;

			case "file": 
				
				boolean endsWithSlash = path.endsWith(URIUtil.URI_PATH_SEPARATOR);
				path = endsWithSlash ? path.substring(0, path.length() - URIUtil.URI_PATH_SEPARATOR.length()) : path;

				int i = path.lastIndexOf(URIUtil.URI_PATH_SEPARATOR);

				if (i != -1) {
					path = path.substring(0, i) + URIUtil.URI_PATH_SEPARATOR + newStringValue;
				}
				else {
					path = path + URIUtil.URI_PATH_SEPARATOR + newStringValue;	
				}	
				if (endsWithSlash) {
					path += URIUtil.URI_PATH_SEPARATOR;
				}
				uriPartChanged = true;
				break;

			case "parent":
				i = path.lastIndexOf(URIUtil.URI_PATH_SEPARATOR);
				String parent = newStringValue;
				if (!parent.startsWith(URIUtil.URI_PATH_SEPARATOR)) {
					parent = URIUtil.URI_PATH_SEPARATOR + parent;
				}
				if (i != -1) {
					path = parent + path.substring(i);
				}
				else {
					path = parent;	
				}
				uriPartChanged = true;
				break;	

			case "ls":
				throw RuntimeExceptionFactory.noSuchField("Cannot update the children of a location");

			case "extension":
				String ext = newStringValue;
				
				endsWithSlash = path.endsWith(URIUtil.URI_PATH_SEPARATOR);
				if (endsWithSlash) {
					path = path.substring(0, path.length() - 1);
				}
			
				if (path.length() > 1) {
					int slashIndex = path.lastIndexOf(URIUtil.URI_PATH_SEPARATOR);
					int index = path.substring(slashIndex).lastIndexOf('.');

					if (index == -1 && !ext.isEmpty()) {
						path = path + (!ext.startsWith(".") ? "." : "") + ext;
					}
					else if (!ext.isEmpty()) {
						path = path.substring(0, slashIndex + index) + (!ext.startsWith(".") ? "." : "") + ext;
					}
					else if (index != -1) {
						path = path.substring(0, slashIndex + index);
					}

					if (endsWithSlash) {
						path = path + URIUtil.URI_PATH_SEPARATOR;
					}
				}
				uriPartChanged = true;
				break;

			case "top":
				if (replType.isString()) {
					uri = URIUtil.assumeCorrect(newStringValue);
					scheme = uri.getScheme();
					authority = uri.getAuthority();
					path = uri.getPath();
					query = uri.getQuery();
					fragment = uri.getFragment();
				}
				else if (replType.isSourceLocation()) {
					ISourceLocation rep = (ISourceLocation) repl;
					scheme = rep.getScheme();
					authority = rep.hasAuthority() ? rep.getAuthority() : null;
					path = rep.hasPath() ? rep.getPath() : null;
					query = rep.hasQuery() ? rep.getQuery() : null;
					fragment = rep.hasFragment() ? rep.getFragment() : null;
				}
				uriPartChanged = true;
				break;

			case "fragment":
				fragment = newStringValue;
				uriPartChanged = true;
				break;

			case "query":
				query = newStringValue;
				uriPartChanged = true;
				break;

			case "user":
				if (!URIResolverRegistry.getInstance().supportsHost(sloc)) {
					throw RuntimeExceptionFactory.noSuchField("The scheme " + sloc.getScheme() + " does not support the user field, use authority instead.");
				}
				uri = sloc.getURI();
				if (uri.getHost() != null) {
					uri = URIUtil.changeUserInformation(uri, newStringValue);
				}

				authority = uri.getAuthority();
				uriPartChanged = true;
				break;

			case "port":
				if (!URIResolverRegistry.getInstance().supportsHost(sloc)) {
					throw RuntimeExceptionFactory.noSuchField("The scheme " + sloc.getScheme() + " does not support the port field, use authority instead.");
				}
				if (sloc.getURI().getHost() != null) {
					int port = Integer.parseInt(((IInteger) repl).getStringRepresentation());
					uri = URIUtil.changePort(sloc.getURI(), port);
				}
				authority = sloc.getURI().getAuthority();
				uriPartChanged = true;
				break;	

			case "length":
				iLength = ((IInteger) repl).intValue();
				if (iLength < 0) {
					throw RuntimeExceptionFactory.illegalArgument(repl);
				}
				break;

			case "offset":
				iOffset = ((IInteger) repl).intValue();
				if (iOffset < 0) {
					throw RuntimeExceptionFactory.illegalArgument(repl);
				}
				break;

			case "begin":
				iBeginLine = ((IInteger) ((ITuple) repl).get(0)).intValue();
				iBeginColumn = ((IInteger) ((ITuple) repl).get(1)).intValue();

				if (iBeginColumn < 0 || iBeginLine < 0) {
					throw RuntimeExceptionFactory.illegalArgument(repl);
				}
				break;
			case "end":
				iEndLine = ((IInteger) ((ITuple) repl).get(0)).intValue();
				iEndColumn = ((IInteger) ((ITuple) repl).get(1)).intValue();

				if (iEndColumn < 0 || iEndLine < 0) {
					throw RuntimeExceptionFactory.illegalArgument(repl);
				}
				break;			

			default:
				throw RuntimeExceptionFactory.noSuchField("Modification of field " + field + " in location not allowed");
			}

			ISourceLocation newLoc = sloc;
			if (uriPartChanged) {
				newLoc = $VF.sourceLocation(scheme, authority, path, query, fragment);
			}

			if (sloc.hasLineColumn()) {
				// was a complete loc, and thus will be now
				return $VF.sourceLocation(newLoc, iOffset, iLength, iBeginLine, iEndLine, iBeginColumn, iEndColumn);
			}

			if (sloc.hasOffsetLength()) {
				// was a partial loc

				if (iBeginLine != -1 || iBeginColumn != -1) {
					//will be complete now.
					iEndLine = iBeginLine;
					iEndColumn = iBeginColumn;
					return $VF.sourceLocation(newLoc, iOffset, iLength, iBeginLine, iEndLine, iBeginColumn, iEndColumn);
				}
				else if (iEndLine != -1 || iEndColumn != -1) {
					// will be complete now.
					iBeginLine = iEndLine;
					iBeginColumn = iEndColumn;
					return $VF.sourceLocation(newLoc, iOffset, iLength, iBeginLine, iEndLine, iBeginColumn, iEndColumn);
				}
				else {
					// remains a partial loc
					return $VF.sourceLocation(newLoc, iOffset, iLength);
				}
			}

			// used to have no offset/length or line/column info, if we are here

			if (iBeginColumn != -1 || iEndColumn != -1 || iBeginLine != -1 || iBeginColumn != -1) {
				// trying to add line/column info to a uri that has no offset length
				throw RuntimeExceptionFactory.invalidUseOfLocation("Can not add line/column information without offset/length");
			}

			// trying to set offset that was not there before, adding length automatically
			if (iOffset != -1 ) {
				if (iLength == -1) {
					iLength = 0;
				}
			}

			// trying to set length that was not there before, adding offset automatically
			if (iLength != -1) {
				if (iOffset == -1) {
					iOffset = 0;
				}
			}

			if (iOffset != -1 || iLength != -1) {
				// used not to no offset/length, but do now
				return $VF.sourceLocation(newLoc, iOffset, iLength);
			}

			// no updates to offset/length or line/column, and did not used to have any either:
			return newLoc;

		} catch (IllegalArgumentException e) {
			throw RuntimeExceptionFactory.illegalArgument(sloc);
		} catch (URISyntaxException e) {
			throw RuntimeExceptionFactory.malformedURI(e.getMessage());
		}
	}

	public final IDateTime $adatetime_field_update(final String field, final IValue repl, final IDateTime dt) {
		// Individual fields
		int year = dt.getYear();
		int month = dt.getMonthOfYear();
		int day = dt.getDayOfMonth();
		int hour = dt.getHourOfDay();
		int minute = dt.getMinuteOfHour();
		int second = dt.getSecondOfMinute();
		int milli = dt.getMillisecondsOfSecond();
		int tzOffsetHour = dt.getTimezoneOffsetHours();
		int tzOffsetMin = dt.getTimezoneOffsetMinutes();

		try {
			switch (field) {

			case "year":
				if (dt.isTime()) {
					throw RuntimeExceptionFactory.invalidUseOfTimeException("Can not update the year on a time value");
				}
				year = ((IInteger)repl).intValue();
				break;

			case "month":
				if (dt.isTime()) {
					throw RuntimeExceptionFactory.invalidUseOfTimeException("Can not update the month on a time value");
				}
				month = ((IInteger)repl).intValue();
				break;

			case "day":
				if (dt.isTime()) {
					throw RuntimeExceptionFactory.invalidUseOfTimeException("Can not update the day on a time value");
				}	
				day = ((IInteger)repl).intValue();
				break;

			case "hour":
				if (dt.isDate()) {
					throw RuntimeExceptionFactory.invalidUseOfDateException("Can not update the hour on a date value");
				}	
				hour = ((IInteger)repl).intValue();
				break;

			case "minute":
				if (dt.isDate()) {
					throw RuntimeExceptionFactory.invalidUseOfDateException("Can not update the minute on a date value");
				}
				minute = ((IInteger)repl).intValue();
				break;

			case "second":
				if (dt.isDate()) {
					throw RuntimeExceptionFactory.invalidUseOfDateException("Can not update the second on a date value");
				}
				second = ((IInteger)repl).intValue();
				break;

			case "millisecond":
				if (dt.isDate()) {
					throw RuntimeExceptionFactory.invalidUseOfDateException("Can not update the millisecond on a date value");
				}
				milli = ((IInteger)repl).intValue();
				break;

			case "timezoneOffsetHours":
				if (dt.isDate()) {
					throw RuntimeExceptionFactory.invalidUseOfDateException("Can not update the timezone offset hours on a date value");
				}
				tzOffsetHour = ((IInteger)repl).intValue();
				break;

			case "timezoneOffsetMinutes":
				if (dt.isDate()) {
					throw RuntimeExceptionFactory.invalidUseOfDateException("Can not update the timezone offset minutes on a date value");
				}
				tzOffsetMin = ((IInteger)repl).intValue();
				break;			

			default:
				throw RuntimeExceptionFactory.noSuchField(field);
			}
			IDateTime newdt = null;
			if (dt.isDate()) {
				newdt = $VF.date(year, month, day);
			} else if (dt.isTime()) {
				newdt = $VF.time(hour, minute, second, milli, tzOffsetHour, tzOffsetMin);
			} else {
				newdt = $VF.datetime(year, month, day, hour, minute, second, milli, tzOffsetHour, tzOffsetMin);
			}
			return newdt;
		}
		catch (IllegalArgumentException e) {
			throw RuntimeExceptionFactory.illegalArgument(repl, /*"Cannot update field " + field + ", this would generate an invalid datetime value",*/ null, null);
		}
		catch (InvalidDateTimeException e) {
			throw RuntimeExceptionFactory.illegalArgument(dt, /*e.getMessage(),*/ null, null);
		}
	}
	
	public final INode $anode_field_update(final String fieldName, IValue repl, final INode nd) {
		if(nd.getType().isAbstractData()) {
			return $aadt_field_update(fieldName, repl, (IConstructor) nd);
		}
		if ((nd.mayHaveKeywordParameters() && nd.asWithKeywordParameters().getParameter(fieldName) != null)){
			return nd.asWithKeywordParameters().setParameter(fieldName, repl);
		} else {
//			if(nd.isAnnotatable()){
//				return nd.asAnnotatable().setAnnotation(fieldName, repl);
//			} else {
				throw RuntimeExceptionFactory.illegalArgument(nd, /*fieldName,*/ null, null);
//			}
		}
	}
	
	public final IConstructor $aadt_field_update(final String fieldName, IValue repl, final IConstructor cons) {

		Type consType = cons.getConstructorType();

		// Does fieldName exist as field in a parse tree?
		if(TreeAdapter.isTree(cons)) {
			if(TreeAdapter.isAppl((ITree) cons) && TreeAdapter.getLabeledField((ITree)cons, fieldName) != null) {
				return TreeAdapter.setArg((ITree)cons, fieldName, (IConstructor) repl);
			} else {
				return cons.asWithKeywordParameters().setParameter(fieldName, repl);
			}
		}

		// Does fieldName exist as positional field?
		if(consType.hasField(fieldName)){
			return cons.set(fieldName, repl);
		}

		// fieldName must be a keyword field
		return cons.asWithKeywordParameters().setParameter(fieldName, repl);
	}
	
	// ---- has ---------------------------------------------------------------
	
	/**
	 * Runtime check whether a node has a named field
	 * 
	 */
	public final boolean $anode_has_field(final INode nd, final String fieldName) {
		if(nd.getType().isAbstractData()) {
			if($aadt_has_field((IConstructor) nd, fieldName)) {
				return true;
			}
		}
		if ((nd.mayHaveKeywordParameters() && nd.asWithKeywordParameters().getParameter(fieldName) != null)){
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Runtime check whether given constructor has a named field (positional or keyword).
	*/

	public final boolean $aadt_has_field(final IConstructor cons, final String fieldName, Type... consesWithField) {
		
		if(TreeAdapter.isTree(cons) && TreeAdapter.isAppl((ITree) cons)) {
			FieldResult fldres = TreeAdapter.getLabeledField((ITree) cons, fieldName);
			if(fldres != null) {
				return true;
			}
			if(cons.asWithKeywordParameters().getParameter(fieldName) != null) {
				return true;
			}
		}
		
		Type consType = cons.getConstructorType();
		for(Type ct : consesWithField) {
			if(consType.equals(ct)) return true;
		}

		// Does fieldName exist as positional field?
		if(consType.hasField(fieldName)){
			return true;
		}
		//TODO: enable this when annotations are gone
		//if($TS.hasKeywordParameter(consType, fieldName)) {
			return cons.asWithKeywordParameters().getParameter(fieldName) != null;
		//}
		//return false;
	}
	
	// ---- has_name_and_arity
	
	public final boolean $nonterminal_has_name_and_arity(final IValue v, final String name, final int arity) {
		if(v instanceof IConstructor &&TreeAdapter.isTree((IConstructor)v)) {
			ITree tree = (ITree) v;
			// Count the non-literal symbols in the argument list
			IList args = org.rascalmpl.values.parsetrees.TreeAdapter.getArgs(tree);
			int prod_arity = 0;
			for(IValue varg : args) {
				if(org.rascalmpl.values.parsetrees.TreeAdapter.isLexical((ITree)varg) ||
						org.rascalmpl.values.parsetrees.TreeAdapter.isSort((ITree)varg) ||
						org.rascalmpl.values.parsetrees.TreeAdapter.isList((ITree)varg)
						) {
					prod_arity++;
				}
			}
			if(prod_arity != arity)
				return false;
			IConstructor prod = org.rascalmpl.values.parsetrees.TreeAdapter.getProduction(tree);
			IValue def = prod.get("def");
			if(def == null) return false;
			if(((IConstructor)def).has("name")) {
				return ((IString)((IConstructor)def).get("name")).getValue().equals(name);
			}
		}
		return false;
	}
	
	public final boolean $has_type_and_arity(final IValue v, final Type type, final int arity) {
		if(v instanceof IConstructor) {
			Type consType = ((IConstructor)v).getConstructorType();
			Map<Type,Type> m = new HashMap<>();
			if(consType.match(type, m)) {
				if(TreeAdapter.isTree((IConstructor)v)) {
					ITree tree = (ITree) v;

					if(TreeAdapter.isChar(tree)) return arity == 1;
					if(TreeAdapter.isAmb(tree)) return arity == 1;

					IList args = org.rascalmpl.values.parsetrees.TreeAdapter.getArgs(tree);
					if(args == null && arity != 0) return false;

					if(TreeAdapter.isLiteral(tree)) {
						IConstructor p = org.rascalmpl.values.parsetrees.TreeAdapter.getProduction(tree);
						IList symbols = ProductionAdapter.getSymbols(p);
						return args.size() == symbols.size();
					}
					if(!TreeAdapter.isAppl(tree)) return false;
					return arity == 2;
				}
//				if(consType.getArity() == arity) {
//					System.err.println(true + " 3");
					return true;
//				}
			}
		}
		return false;
	}
	
	public final IValue $nonterminal_get_arg(final ITree tree, final int idx) {
		// Find idx-th nonterminal symbol in the argument list
		IList args = org.rascalmpl.values.parsetrees.TreeAdapter.getArgs(tree);
		int i = 0;
		for(IValue varg : args) {
			if(!org.rascalmpl.values.parsetrees.TreeAdapter.isLayout((ITree)varg)) {
				if(org.rascalmpl.values.parsetrees.TreeAdapter.isLexical((ITree)varg) ||
				    org.rascalmpl.values.parsetrees.TreeAdapter.isSort((ITree)varg) ||
				   org.rascalmpl.values.parsetrees.TreeAdapter.isList((ITree)varg)
				  ) {
					if(i == idx) {
						return varg;
					}
					i++;
				}
			}
		}
		throw new InternalCompilerError("nonterminal does not have argument #" + idx);
	}
	
	// ---- is -----------------------------------------------------------------
	
	public final boolean $is(final IValue val, final IString sname) {
		Type tp = val.getType();
		String name = sname.getValue();
		if(tp.isAbstractData()){
			if(tp.getName().equals("Tree")){
				IConstructor cons = (IConstructor) val;
				if(cons.getName().equals("appl")){
					IConstructor prod = (IConstructor) cons.get(0);
					IConstructor def = (IConstructor) prod.get(0);
					if(def.getName().equals("label")){
						return ((IString) def.get(0)).getValue().equals(name);
					}
				}
			} else {
				String consName = ((IConstructor)val).getConstructorType().getName();
				if(consName.startsWith("\\")){
					consName = consName.substring(1);
				}
				return consName.equals(name);
			}
		} else if(tp.isNode()){
			String nodeName = ((INode) val).getName();
			if(nodeName.startsWith("\\")){
				nodeName = nodeName.substring(1);
			}
			return nodeName.equals(name);
		} 
		return false;
	}

	// ---- is_defined_value and get_defined_value -----------------------------

	private final GuardedIValue UNDEFINED = new GuardedIValue();

	public final boolean $is_defined_value(final GuardedIValue val) {
		return val.defined;
	}

	public final IValue $get_defined_value(final GuardedIValue val) {
		return val.value;
	}

	// ---- join --------------------------------------------------------------

	public final IList $alist_join_alrel(final IList left, final IList right){
		if(left.length() == 0){
			return left;
		}
		if(right.length() == 0){
			return right;
		}
		Type rightType = right.get(0).getType();
		assert rightType.isTuple();

		int rarity = rightType.getArity();
		IValue fieldValues[] = new IValue[1 + rarity];
		IListWriter w = $VF.listWriter();

		for (IValue lval : left){
			fieldValues[0] = lval;
			for (IValue rtuple: right) {
				for (int i = 0; i < rarity; i++) {
					fieldValues[i + 1] = ((ITuple)rtuple).get(i);
				}
				w.append($VF.tuple(fieldValues));
			}
		}
		return w.done();
	}

	public final IList $alrel_join_alrel(final IList left, final IList right){
		if(left.length() == 0){
			return left;
		}
		if(right.length() == 0){
			return right;
		}
		Type leftType = left.get(0).getType();
		Type rightType = right.get(0).getType();
		assert leftType.isTuple();
		assert rightType.isTuple();

		int larity = leftType.getArity();
		int rarity = rightType.getArity();
		IValue fieldValues[] = new IValue[larity + rarity];
		IListWriter w = $VF.listWriter();

		for (IValue ltuple : left){
			for (IValue rtuple: right) {
				for (int i = 0; i < larity; i++) {
					fieldValues[i] = ((ITuple)ltuple).get(i);
				}
				for (int i = larity; i < larity + rarity; i++) {
					fieldValues[i] = ((ITuple)rtuple).get(i - larity);
				}
				w.append($VF.tuple(fieldValues));
			}
		}
		return w.done();
	}

	public final IList $alrel_join_alist(final IList left, final IList right){
		if(left.length() == 0){
			return left;
		}
		if(right.length() == 0){
			return right;
		}
		Type leftType = left.get(0).getType();
		assert leftType.isTuple();

		int larity = leftType.getArity();
		IValue fieldValues[] = new IValue[larity + 1];
		IListWriter w = $VF.listWriter();

		for (IValue ltuple : left){
			for (IValue rval: right) {
				for (int i = 0; i < larity; i++) {
					fieldValues[i] = ((ITuple)ltuple).get(i);
				}
				fieldValues[larity] = rval;
				w.append($VF.tuple(fieldValues));
			}
		}
		return w.done();
	}

	public final ISet $aset_join_arel(final ISet left, final ISet right){
		if(left.size() == 0){
			return left;
		}
		if(right.size() == 0){
			return right;
		}
		Type rightType = right.getElementType();
		assert rightType.isTuple();

		int rarity = rightType.getArity();
		IValue fieldValues[] = new IValue[1 + rarity];
		ISetWriter w = $VF.setWriter();

		for (IValue lval : left){
			for (IValue rtuple: right) {
				fieldValues[0] = lval;
				for (int i = 0; i <  rarity; i++) {
					fieldValues[i + 1] = ((ITuple)rtuple).get(i);
				}
				w.insert($VF.tuple(fieldValues));
			}
		}
		return w.done();
	}

	public final ISet $arel_join_arel(final ISet left, final ISet right){
		if(left.size() == 0){
			return left;
		}
		if(right.size() == 0){
			return right;
		}
		Type leftType = left.getElementType();
		Type rightType = right.getElementType();
		assert leftType.isTuple();
		assert rightType.isTuple();

		int larity = leftType.getArity();
		int rarity = rightType.getArity();
		IValue fieldValues[] = new IValue[larity + rarity];
		ISetWriter w = $VF.setWriter();

		for (IValue ltuple : left){
			for (IValue rtuple: right) {
				for (int i = 0; i < larity; i++) {
					fieldValues[i] = ((ITuple)ltuple).get(i);
				}
				for (int i = larity; i < larity + rarity; i++) {
					fieldValues[i] = ((ITuple)rtuple).get(i - larity);
				}
				w.insert($VF.tuple(fieldValues));
			}
		}
		return w.done();
	}

	public final ISet $arel_join_aset(final ISet left, final ISet right){

		if(left.size() == 0){
			return left;
		}
		if(right.size() == 0){
			return right;
		}
		Type leftType = left.getElementType();
		assert leftType.isTuple();

		int larity = leftType.getArity();
		IValue fieldValues[] = new IValue[larity + 1];
		ISetWriter w = $VF.setWriter();

		for (IValue ltuple : left){
			for (IValue rval: right) {
				for (int i = 0; i < larity; i++) {
					fieldValues[i] = ((ITuple)ltuple).get(i);
				}
				fieldValues[larity] = rval;
				w.insert($VF.tuple(fieldValues));
			}
		}
		return w.done();
	}

	// ---- less --------------------------------------------------------------

	public final IBool $less(final IValue left, final IValue right){

		Type leftType = left.getType();
		Type rightType = right.getType();

		if (leftType.isSubtypeOf($TF.numberType()) && rightType.isSubtypeOf($TF.numberType())) {
			return ((INumber)left).less((INumber)right);
		}

		if(!leftType.comparable(rightType)){
			return Rascal_FALSE;
		}
		
		return leftType.accept(new DefaultRascalTypeVisitor<IBool,RuntimeException>(Rascal_FALSE) {
			@Override
			public IBool visitList(Type type) throws RuntimeException {
				if(rightType.isList()) {
					return $alist_less_alist((IList)left, (IList)right);
				}
				throw new InternalCompilerError("less: unexpected arguments `" + leftType + "` and `" + rightType + "`");
			}

			@Override
			public IBool visitMap(Type type) throws RuntimeException {
				if(rightType.isMap()) {
					return $amap_less_amap((IMap)left, (IMap)right);
				}
				throw new InternalCompilerError("less: unexpected arguments `" + leftType + "` and `" + rightType + "`");
			}

			@Override
			public IBool visitSet(Type type) throws RuntimeException {
				if(rightType.isSet()) {
					return $aset_less_aset((ISet)left, (ISet)right);
				}
				throw new InternalCompilerError("less: unexpected arguments `" + leftType + "` and `" + rightType + "`");
			}

			@Override
			public IBool visitSourceLocation(Type type) throws RuntimeException {
				if(rightType.isSourceLocation()) {
					return $aloc_less_aloc((ISourceLocation)left, (ISourceLocation)right);		
				}
				throw new InternalCompilerError("less: unexpected arguments `" + leftType + "` and `" + rightType + "`");
			}

			@Override
			public IBool visitString(Type type) throws RuntimeException {
				if(rightType.isString()) {
					return $astr_less_astr((IString)left, (IString)right);
				}
				throw new InternalCompilerError("less: unexpected arguments `" + leftType + "` and `" + rightType + "`");
			}

			@Override
			public IBool visitNode(Type type) throws RuntimeException {
				if(rightType.isNode()){
					return $anode_less_anode((INode)left, (INode)right);	
				}
				throw new InternalCompilerError("less: unexpected arguments `" + leftType + "` and `" + rightType + "`");
			}

			@Override
			public IBool visitConstructor(Type type) throws RuntimeException {
				return visitNode(type);
			}

			@Override
			public IBool visitAbstractData(Type type)
					throws RuntimeException {
				return visitNode(type);
			}

			@Override
			public IBool visitTuple(Type type) throws RuntimeException {
				if(rightType.isTuple()) {
					return $atuple_less_atuple((ITuple)left, (ITuple)right);
				}
				throw new InternalCompilerError("less: unexpected arguments `" + leftType + "` and `" + rightType + "`");
			}

			@Override
			public IBool visitBool(Type type) throws RuntimeException {
				if(rightType.isBool()) {
					return $abool_less_abool((IBool)left, (IBool)right);
				}
				throw new InternalCompilerError("less: unexpected arguments `" + leftType + "` and `" + rightType + "`");
			}
		
			@Override
			public IBool visitDateTime(Type type) throws RuntimeException {
				if(rightType.isDateTime()) {
					return $adatetime_less_adatetime((IDateTime)left, (IDateTime)right);
				}
				throw new InternalCompilerError("less: unexpected arguments `" + leftType + "` and `" + rightType + "`");
			}
			});
	}
	
	public final IBool $aint_less_aint(final IInteger a, final IInteger b) {
		return a.less(b);
	}

	public final IBool $aint_less_areal(final IInteger a, final IReal b) {
		return a.less(b);
	}

	public final IBool $aint_less_arat(final IInteger a, final IRational b) {
		return a.toRational().less(b);
	}

	public final IBool $aint_less_anum(final IInteger a, final INumber b) {
		return a.less(b);
	}

	public final IBool $areal_less_aint(final IReal a, final IInteger b) {
		return a.less(b);
	}

	public final IBool $areal_less_areal(final IReal a, final IReal b) {
		return a.less(b);
	}

	public final IBool $areal_less_arat(IReal a, IRational b) {
		return a.less(b);
	}

	public final IBool $areal_less_anum(final IReal a, final INumber b) {
		return a.less(b);
	}

	public final IBool $arat_less_aint(final IRational a, final IInteger b) {
		return a.less(b);
	}

	public final IBool $arat_less_areal(final IRational a, final IReal b) {
		return a.less(b);
	}

	public final IBool $arat_less_arat(final IRational a, final IRational b) {
		return a.toRational().less(b);
	}

	public final IBool $arat_less_anum(final IRational a, final INumber b) {
		return a.less(b);
	}

	public final IBool $anum_less_aint(final INumber a, final IInteger b) {
		return a.less(b);
	}

	public final IBool $anum_less_areal(final INumber a, final IReal b) {
		return a.less(b);
	}
	public final IBool $anum_less_arat(final INumber a, final IRational b) {
		return a.less(b);
	}

	public final IBool $anum_less_anum(final INumber a, final INumber b) {
		return a.less(b);
	}

	public final IBool $abool_less_abool(final IBool left, final IBool right) {
		return  $VF.bool(!left.getValue() && right.getValue());
	}

	public final IBool $astr_less_astr(final IString left, final IString right) {
		return $VF.bool(left.compare(right) == -1);
	}

	public final IBool $adatetime_less_adatetime(final IDateTime left, final IDateTime right) {
		return $VF.bool(left.compareTo(right) == -1);
	}

	public final IBool $aloc_less_aloc(final ISourceLocation left, final ISourceLocation right) {
		int compare = SourceLocationURICompare.compare(left, right);
		if (compare < 0) {
			return Rascal_TRUE;
		}
		else if (compare > 0) {
			return Rascal_FALSE;
		}

		// but the uri's are the same
		// note that line/column information is superfluous and does not matter for ordering

		if (left.hasOffsetLength()) {
			if (!right.hasOffsetLength()) {
				return Rascal_FALSE;
			}

			int roffset = right.getOffset();
			int rlen = right.getLength();
			int loffset = left.getOffset();
			int llen = left.getLength();

			if (loffset == roffset) {
				return $VF.bool(llen < rlen);
			}
			return $VF.bool(roffset < loffset && roffset + rlen >= loffset + llen);
		}
		else if (compare == 0) {
			return Rascal_FALSE;
		}

		if (!right.hasOffsetLength()) {
			throw new InternalCompilerError("offset length missing");
		}
		return Rascal_FALSE;
	}

	public final IBool $atuple_less_atuple(final ITuple left, final ITuple right) {
		int leftArity = left.arity();
		int rightArity = right.arity();

		for (int i = 0; i < Math.min(leftArity, rightArity); i++) {
			Object result;
			if(leftArity < rightArity || i < leftArity - 1)
				result = $equal(left.get(i), right.get(i));
			else
				result = $less(left.get(i), right.get(i));

			if(!((IBool)result).getValue()){
				return Rascal_FALSE;
			}
		}

		return $VF.bool(leftArity <= rightArity);
	}

	public final IBool $anode_less_anode(final INode left, final INode right) {
		int compare = left.getName().compareTo(right.getName());

		if (compare <= -1) {
			return Rascal_TRUE;
		}

		if (compare >= 1){
			return Rascal_FALSE;
		}

		// if the names are not ordered, then we order lexicographically on the arguments:

		int leftArity = left.arity();
		int rightArity = right.arity();

		Object result =  Rascal_FALSE;
		for (int i = 0; i < Math.min(leftArity, rightArity); i++) {

			if(leftArity < rightArity || i < leftArity - 1)
				result = $lessequal(left.get(i), right.get(i));
			else
				result = $less(left.get(i), right.get(i));

			if(!((IBool)result).getValue()){
				return Rascal_FALSE;
			}
		}

		if (!left.mayHaveKeywordParameters() && !right.mayHaveKeywordParameters()) {
//			if (left.asAnnotatable().hasAnnotations() || right.asAnnotatable().hasAnnotations()) {
//				// bail out 
//				return Rascal_FALSE;
//			}
		}

		if (!left.asWithKeywordParameters().hasParameters() && right.asWithKeywordParameters().hasParameters()) {
			return Rascal_TRUE;
		}

		if (left.asWithKeywordParameters().hasParameters() && !right.asWithKeywordParameters().hasParameters()) {
			return Rascal_FALSE;
		}

		if (left.asWithKeywordParameters().hasParameters() && right.asWithKeywordParameters().hasParameters()) {
			Map<String, IValue> paramsLeft = left.asWithKeywordParameters().getParameters();
			Map<String, IValue> paramsRight = right.asWithKeywordParameters().getParameters();
			if (paramsLeft.size() < paramsRight.size()) {
				return Rascal_TRUE;
			}
			if (paramsLeft.size() > paramsRight.size()) {
				return Rascal_FALSE;
			}
			if (paramsRight.keySet().containsAll(paramsLeft.keySet()) && !paramsRight.keySet().equals(paramsLeft.keySet())) {
				return Rascal_TRUE;
			}
			if (paramsLeft.keySet().containsAll(paramsLeft.keySet()) && !paramsRight.keySet().equals(paramsLeft.keySet())) {
				return Rascal_FALSE;
			}
			//assert paramsLeft.keySet().equals(paramsRight.keySet());
			for (String k: paramsLeft.keySet()) {
				result = $less(paramsLeft.get(k), paramsRight.get(k));

				if(!((IBool)result).getValue()){
					return Rascal_FALSE;
				}
			}
		}

		return $VF.bool((leftArity < rightArity) || ((IBool)result).getValue());
	}

	public final IBool $alist_less_alist(final IList left, final IList right) {
		if(left.length() > right.length()){
			return Rascal_FALSE;
		}
		OUTER:for (int l = 0, r = 0; l < left.length(); l++) {
			for (r = Math.max(l, r) ; r < right.length(); r++) {
				if (left.get(l).equals(right.get(r))) {
					r++;
					continue OUTER;
				}
			}
			return Rascal_FALSE;
		}
		return $VF.bool(left.length() != right.length());
	}

	public final IBool $aset_less_aset(final ISet left, final ISet right) {
		return $VF.bool(!left.equals(right) && left.isSubsetOf(right));
	}

	public final IBool $amap_less_amap(final IMap left, final IMap right) {
		return $VF.bool(left.isSubMap(right) && !right.isSubMap(left));
	}

	// ---- lessequal ---------------------------------------------------------

	public final IBool $lessequal(final IValue left, final IValue right){

		Type leftType = left.getType();
		Type rightType = right.getType();

		if (leftType.isSubtypeOf($TF.numberType()) && rightType.isSubtypeOf($TF.numberType())) {
			return ((INumber)left).lessEqual((INumber)right);
		}

		if(!leftType.comparable(rightType)){
			return Rascal_FALSE;
		}
		
		return leftType.accept(new DefaultRascalTypeVisitor<IBool,RuntimeException>(Rascal_FALSE) {
			@Override
			public IBool visitList(Type type) throws RuntimeException {
				if(rightType.isList()) {
					return $alist_lessequal_alist((IList)left, (IList)right);
				}
				throw new InternalCompilerError("lessequal: unexpected arguments `" + leftType + "` and `" + rightType + "`");
			}

			@Override
			public IBool visitMap(Type type) throws RuntimeException {
				if(rightType.isMap()) {
					return $amap_lessequal_amap((IMap)left, (IMap)right);
				}
				throw new InternalCompilerError("lessequal: unexpected arguments `" + leftType + "` and `" + rightType + "`");
			}

			@Override
			public IBool visitSet(Type type) throws RuntimeException {
				if(rightType.isSet()) {
					return $aset_lessequal_aset((ISet)left, (ISet)right);
				}
				throw new InternalCompilerError("lessequal: unexpected arguments `" + leftType + "` and `" + rightType + "`");
			}

			@Override
			public IBool visitSourceLocation(Type type) throws RuntimeException {
				if(rightType.isSourceLocation()) {
					return $aloc_lessequal_aloc((ISourceLocation)left, (ISourceLocation)right);		
				}
				throw new InternalCompilerError("lessequal: unexpected arguments `" + leftType + "` and `" + rightType + "`");
			}

			@Override
			public IBool visitString(Type type) throws RuntimeException {
				if(rightType.isString()) {
					return $astr_lessequal_astr((IString)left, (IString)right);
				}
				throw new InternalCompilerError("lessequal: unexpected arguments `" + leftType + "` and `" + rightType + "`");
			}

			@Override
			public IBool visitNode(Type type) throws RuntimeException {
				if(rightType.isNode()){
					return $anode_lessequal_anode((INode)left, (INode)right);	
				}
				throw new InternalCompilerError("lessequal: unexpected arguments `" + leftType + "` and `" + rightType + "`");
			}

			@Override
			public IBool visitConstructor(Type type) throws RuntimeException {
				return visitNode(type);
			}

			@Override
			public IBool visitAbstractData(Type type)
					throws RuntimeException {
				return visitNode(type);
			}

			@Override
			public IBool visitTuple(Type type) throws RuntimeException {
				if(rightType.isTuple()) {
					return $atuple_lessequal_atuple((ITuple)left, (ITuple)right);
				}
				throw new InternalCompilerError("lessequal: unexpected arguments `" + leftType + "` and `" + rightType + "`");
			}

			@Override
			public IBool visitBool(Type type) throws RuntimeException {
				if(rightType.isBool()) {
					return $abool_lessequal_abool((IBool)left, (IBool)right);
				}
				throw new InternalCompilerError("lessequal: unexpected arguments `" + leftType + "` and `" + rightType + "`");
			}
		
			@Override
			public IBool visitDateTime(Type type) throws RuntimeException {
				if(rightType.isDateTime()) {
					return $adatetime_lessequal_adatetime((IDateTime)left, (IDateTime)right);
				}
				throw new InternalCompilerError("lessequal: unexpected arguments `" + leftType + "` and `" + rightType + "`");
			}
			});


//		switch (ToplevelType.getToplevelType(leftType)) {
//		// TODO: is this really faster than a TypeVisitor?? No because getTopLevelType includes a TypeVisitor itself.
//		// TODO: check type of right
//		case BOOL:
//			return $abool_lessequal_abool((IBool)left, (IBool)right);
//		case STR:
//			return $astr_lessequal_astr((IString)left, (IString)right);
//		case DATETIME:
//			return $adatetime_lessequal_adatetime((IDateTime)left, (IDateTime)right);
//		case LOC:
//			return $aloc_lessequal_aloc((ISourceLocation)left, (ISourceLocation)right);
//		case LIST:
//		case LREL:
//			return $alist_lessequal_alist((IList)left, (IList)right);
//		case SET:
//		case REL:
//			return $aset_lessequal_aset((ISet)left, (ISet)right);
//		case MAP:
//			return $amap_lessequal_amap((IMap)left, (IMap)right);
//		case CONSTRUCTOR:
//		case NODE:
//			return $anode_lessequal_anode((INode)left, (INode)right);
//		case ADT:
//			return aadt_lessequal_aadt((IAbstractDataType)left, right);
//		case TUPLE:
//			return $atuple_lessequal_atuple((ITuple)left, (ITuple)right);
//		default:
//			throw new InternalCompilerError("less: unexpected type " + leftType);
//		}
	}
	
	public final IBool $aint_lessequal_aint(final IInteger a, final IInteger b) {
		return a.lessEqual(b);
	}

	public final IBool $aint_lessequal_areal(final IInteger a, final IReal b) {
		return a.lessEqual(b);
	}

	public final IBool $aint_lessequal_arat(final IInteger a, final IRational b) {
		return a.lessEqual(b);
	}

	public final IBool $aint_lessequal_anum(final IInteger a, final INumber b) {
		return a.lessEqual(b);
	}

	public final IBool $areal_lessequal_aint(final IReal a, final IInteger b) {
		return a.lessEqual(b);
	}

	public final IBool $areal_lessequal_areal(final IReal a, final IReal b) {
		return a.lessEqual(b);
	}

	public final IBool $areal_lessequal_arat(IReal a, IRational b) {
		return a.lessEqual(b);
	}

	public final IBool $areal_lessequal_anum(final IReal a, final INumber b) {
		return a.lessEqual(b);
	}

	public final IBool $arat_lessequal_aint(final IRational a, final IInteger b) {
		return a.lessEqual(b);
	}

	public final IBool $arat_lessequal_areal(final IRational a, final IReal b) {
		return a.lessEqual(b);
	}

	public final IBool $arat_lessequal_arat(final IRational a, final IRational b) {
		return a.lessEqual(b);
	}

	public final IBool $arat_lessequal_anum(final IRational a, final INumber b) {
		return a.lessEqual(b);
	}

	public final IBool $anum_lessequal_aint(final INumber a, final IInteger b) {
		return a.lessEqual(b);
	}

	public final IBool $anum_lessequal_areal(final INumber a, final IReal b) {
		return a.lessEqual(b);
	}
	public final IBool $anum_lessequal_arat(final INumber a, final IRational b) {
		return a.lessEqual(b);
	}

	public final IBool $anum_lessequal_anum(final INumber a, final INumber b) {
		return a.lessEqual(b);
	}

	public final IBool $abool_lessequal_abool(final IBool left, final IBool right) {
		boolean l = left.getValue();
		boolean r = right.getValue();
		return $VF.bool((!l && r) || (l == r));
	}

	public final IBool $astr_lessequal_astr(final IString left, final IString right) {
		int c = left.compare(right);
		return $VF.bool(c == -1 || c == 0);
	}

	public final IBool $adatetime_lessequal_adatetime(final IDateTime left, final IDateTime right) {
		int c = left.compareTo(right);
		return $VF.bool(c== -1 || c == 0);
	}

	public final IBool $aloc_lessequal_aloc(final ISourceLocation left, final ISourceLocation right) {
		int compare = SourceLocationURICompare.compare(left, right);
		if (compare < 0) {
			return Rascal_TRUE;
		}
		else if (compare > 0) {
			return Rascal_FALSE;
		}

		// but the uri's are the same
		// note that line/column information is superfluous and does not matter for ordering

		if (left.hasOffsetLength()) {
			if (!right.hasOffsetLength()) {
				return Rascal_FALSE;
			}

			int roffset = right.getOffset();
			int rlen = right.getLength();
			int loffset = left.getOffset();
			int llen = left.getLength();

			if (loffset == roffset) {
				return $VF.bool(llen <= rlen);
			}
			return $VF.bool(roffset < loffset && roffset + rlen >= loffset + llen);
		}
		else if (compare == 0) {
			return Rascal_TRUE;
		}

		if (!right.hasOffsetLength()) {
			throw new InternalCompilerError("missing offset length");
		}
		return Rascal_FALSE;
	}

	public final IBool $anode_lessequal_anode(final INode left, final INode right) {
		int compare = left.getName().compareTo(right.getName());

		if (compare <= -1) {
			return Rascal_TRUE;
		}

		if (compare >= 1){
			return Rascal_FALSE;
		}

		// if the names are not ordered, then we order lexicographically on the arguments:

		int leftArity = left.arity();
		int rightArity = right.arity();

		for (int i = 0; i < Math.min(leftArity, rightArity); i++) {
			if(!$lessequal(left.get(i), right.get(i)).getValue()){
				return Rascal_FALSE;
			}
		}
		return $VF.bool(leftArity <= rightArity);
	}

	public final IBool $atuple_lessequal_atuple(final ITuple left, final ITuple right) {
		int leftArity = left.arity();
		int rightArity = right.arity();

		for (int i = 0; i < Math.min(leftArity, rightArity); i++) {			
			if(!$lessequal(left.get(i), right.get(i)).getValue()){
				return Rascal_FALSE;
			}
		}

		return $VF.bool(leftArity <= rightArity);
	}

	public final IBool $alist_lessequal_alist(final IList left, final IList right) {
		if (left.length() == 0) {
			return Rascal_TRUE;
		}
		else if (left.length() > right.length()) {
			return Rascal_FALSE;
		}

		OUTER:for (int l = 0, r = 0; l < left.length(); l++) {
			for (r = Math.max(l, r) ; r < right.length(); r++) {
				if (left.get(l).equals(right.get(r))) {
					continue OUTER;
				}
			}
			return Rascal_FALSE;
		}

		return $VF.bool(left.length() <= right.length());
	}

	public final IBool $aset_lessequal_aset(final ISet left, final ISet right) {
		return $VF.bool(left.size() == 0 || left.equals(right) || left.isSubsetOf(right));
	}

	public final IBool $amap_lessequal_amap(final IMap left, final IMap right) {
		return $VF.bool(left.isSubMap(right));
	}
	
	// ---- parse -------------------------------------------------------------
	
	public final IValue $parse(final IValue reified, IString inputText, ISourceLocation inputLocation) {
		IFunction parser = $RVF.parser(reified, $VF.bool(true),$VF.bool(false), $VF.bool(false), $VF.set());
		return parser.call(inputText, inputLocation);
	}

	

	public final IInteger $aint_product_aint(final IInteger a, final IInteger b) {
		return a.multiply(b);
	}

	public final IReal $aint_product_areal(final IInteger a, final IReal b) {
		return a.multiply(b);
	}

	public final IRational $aint_product_arat(final IInteger a, final IRational b) {
		return a.toRational().multiply(b);
	}

	public final INumber $aint_product_anum(final IInteger a, final INumber b) {
		return a.multiply(b);
	}

	public final IReal $areal_product_aint(final IReal a, final IInteger b) {
		return (IReal) a.multiply(b);
	}

	public final IReal $areal_product_areal(final IReal a, final IReal b) {
		return a.multiply(b);
	}

	public final IReal $areal_product_arat(IReal a, IRational b) {
		return (IReal) a.multiply(b);
	}

	public final INumber $areal_product_anum(final IReal a, final INumber b) {
		return a.multiply(b);
	}

	public final INumber $arat_product_aint(final IRational a, final IInteger b) {
		return a.multiply(b);
	}

	public final IReal $arat_product_areal(final IRational a, final IReal b) {
		return a.multiply(b);
	}

	public final IRational $arat_product_arat(final IRational a, final IRational b) {
		return a.toRational().multiply(b);
	}

	public final INumber $arat_product_anum(final IRational a, final INumber b) {
		return a.multiply(b);
	}

	public final INumber $anum_product_aint(final INumber a, final IInteger b) {
		return a.multiply(b);
	}

	public final INumber $anum_product_areal(final INumber a, final IReal b) {
		return a.multiply(b);
	}
	public final INumber $anum_product_arat(final INumber a, final IRational b) {
		return a.multiply(b);
	}

	public final INumber $anum_product_anum(final INumber a, final INumber b) {
		return a.multiply(b);
	}

	public final IList $alist_product_alist(final IList left, final IList right) {
		IListWriter w = $VF.listWriter();
		for(IValue l : left){
			for(IValue r : right){
				w.append($VF.tuple(l,r));
			}
		}
		return w.done();
	}

	public final ISet $aset_product_aset(final ISet left, final ISet right) {
		ISetWriter w = $VF.setWriter();
		for(IValue l : left){
			for(IValue r : right){
				w.insert($VF.tuple(l,r));
			}
		}
		return w.done();
	}
	
	// ---- regexp ------------------------------------------------------------
	
	public final Matcher $regExpCompile(String pat, String subject) {
		//pat = pat.replaceAll("\\\\", "\\\\\\\\");
		try {
			Pattern p = Pattern.compile(pat);
			return p.matcher(subject);
		} catch (java.util.regex.PatternSyntaxException e) {
			throw RuntimeExceptionFactory.regExpSyntaxError(e.getMessage());
		}
	}
	
	public final IString $str_escape_for_regexp(IString insert) {
		StringBuilder sw = new StringBuilder();
		for(int i : insert) {
			char c = (char) i;
			switch(c){
				case '.': sw.append("\\."); break;
				case '(': sw.append("\\("); break;
				case ')': sw.append("\\)"); break;
				case '*': sw.append("\\*"); break;
				default: sw.append(c);
			}
		}
		return $VF.string(sw.toString());
	}
	
	public final IString $str_escape_for_regexp(String insert) {
		return $str_escape_for_regexp($VF.string(insert));
	}

	// ---- slice -------------------------------------------------------------

	public final IString $astr_slice(final IString str,  final Integer first,final  Integer second,final Integer end){
		SliceDescriptor sd = makeSliceDescriptor(first, second, end, str.length());
		StringBuilder buffer = new StringBuilder();
		int increment = sd.second - sd.first;
		if(sd.first == sd.end || increment == 0){
			// nothing to be done
		} else
			if(sd.first <= sd.end){
				for(int i = sd.first; i >= 0 && i < sd.end; i += increment){
					buffer.appendCodePoint(str.charAt(i));
				}
			} else {
				for(int j = sd.first; j >= 0 && j > sd.end && j < str.length(); j += increment){
					buffer.appendCodePoint(str.charAt(j));
				}
			}
		return $VF.string(buffer.toString());
	}

	public final IList $anode_slice(final INode node,  final Integer first, final Integer second, final Integer end){
		SliceDescriptor sd = makeSliceDescriptor(first, second, end, node.arity());
		IListWriter w = $VF.listWriter();
		int increment = sd.second - sd.first;
		if(sd.first == sd.end || increment == 0){
			// nothing to be done
		} else
			if(sd.first <= sd.end){
				for(int i = sd.first; i >= 0 && i < sd.end; i += increment){
					w.append(node.get(i));
				}
			} else {
				for(int j = sd.first; j >= 0 && j > sd.end && j < node.arity(); j += increment){
					w.append(node.get(j));
				}
			}

		return w.done();
	}
	
	public final IList $alist_slice(IList lst, Integer first, Integer second, Integer end){
		SliceDescriptor sd = makeSliceDescriptor(first, second, end, lst.length());
		IListWriter w = $VF.listWriter();
		int increment = sd.second - sd.first;
		if(sd.first == sd.end || increment == 0){
			// nothing to be done
		} else
			if(sd.first <= sd.end){
				for(int i = sd.first; i >= 0 && i < sd.end; i += increment){
					w.append(lst.get(i));
				}
			} else {
				for(int j = sd.first; j >= 0 && j > sd.end && j < lst.length(); j += increment){
					w.append(lst.get(j));
				}
			}
		return w.done();
	}
	
	public final ITree $lexical_slice(final ITree tree, final Integer first, final Integer second, final Integer end){
		return $syntactic_slice(tree, first, second, end, 1);
	}
	
	public final ITree $concrete_slice(final ITree tree, final Integer first, final Integer second, final Integer end){
		return $syntactic_slice(tree, first, second, end, 2);
	}
	
	public final ITree $lexical_slice_seps(final ITree tree, final Integer first, final Integer second, final Integer end){
		IConstructor nt = ProductionAdapter.getType(TreeAdapter.getProduction(tree));
		int delta = SymbolAdapter.getSeparators(nt).size() + 1;
		return $syntactic_slice(tree, first, second, end, delta);
	}
	
	public final ITree $concrete_slice_seps(final ITree tree, final Integer first, final Integer second, final Integer end) {
		IConstructor nt = ProductionAdapter.getType(TreeAdapter.getProduction(tree));
		int delta = SymbolAdapter.getSeparators(nt).size() + 1;
		return $syntactic_slice(tree, first, second, end, delta);
	}
	
	private final ITree $syntactic_slice(final ITree tree, final Integer first, final Integer second, final Integer end, final int delta){
		IList args = tree.getArgs();
		int nargs = args.size();
		SliceDescriptor sd = makeSliceDescriptor(first, second, end, nargs);
		int sd_first = sd.first * delta;
		int sd_second = sd.second * delta;
		int sd_end = Math.min(sd.end * delta, nargs);
		IListWriter w = $VF.listWriter();
		int increment = (sd_second - sd_first);
		if(sd_first == sd_end || increment == 0){
			// nothing to be done
		} else {
			if(sd_first <= sd_end){
				for(int i = sd_first; i >= 0 && i < sd_end; i += increment){
					w.append(args.get(i));
					if(i < (sd_end - increment)) {
						for(int j = i + 1; j < i + delta; j++) {
							w.append(args.get(j));
						}
					}
				}
			} else {
				for(int i = sd_first; i >= 0 && i > sd_end && i < nargs; i += increment){
					w.append(args.get(i));
					if(i > 0) {
						for(int j = i - 1; j > i - delta; j--) {
							w.append(args.get(j));
						}
					}
				}
			}
		}
		
		IList newArgs = w.done();
		String opname = ((IConstructor)org.rascalmpl.values.parsetrees.TreeAdapter.getProduction(tree).get(0)).getName();
		boolean isPlusIter = opname == "iter" || opname == "iter-seps";
		if(isPlusIter && newArgs.size() == 0) {
			throw RuntimeExceptionFactory.illegalArgument(newArgs, "Slice should not create empty list of elements for lexical or nonterminal with one or more repetitions");
		}
		
		return org.rascalmpl.values.parsetrees.TreeAdapter.setArgs(tree,newArgs);
	}
	
	public final IList $makeSlice(final INode node, final Integer first, final Integer second,final Integer end){
		SliceDescriptor sd = makeSliceDescriptor(first, second, end, node.arity());
		IListWriter w = $VF.listWriter();
		int increment = sd.second - sd.first;
		if(sd.first == sd.end || increment == 0){
			// nothing to be done
		} else
			if(sd.first <= sd.end){
				for(int i = sd.first; i >= 0 && i < sd.end; i += increment){
					w.append(node.get(i));
				}
			} else {
				for(int j = sd.first; j >= 0 && j > sd.end && j < node.arity(); j += increment){
					w.append(node.get(j));
				}
			}

		return w.done();
	}

	private SliceDescriptor makeSliceDescriptor(final Integer first, final Integer second, final Integer end, final int len) {
		int firstIndex = 0;
		int secondIndex = 1;
		int endIndex = len;

		if(first != null){
			firstIndex = first;
			if(firstIndex < 0)
				firstIndex += len;
		}
		if(end != null){
			endIndex = end;
			if(endIndex < 0){
				endIndex += len;
			}
		}

		if(second == null){
			secondIndex = firstIndex + ((firstIndex <= endIndex) ? 1 : -1);
		} else {
			secondIndex = second;
			if(secondIndex < 0)
				secondIndex += len;
			if(!(first == null && end == null)){
				if(first == null && secondIndex > endIndex)
					firstIndex = len - 1;
				if(end == null && secondIndex < firstIndex)
					endIndex = -1;
			}
		}

		if(len == 0 || firstIndex >= len){
			firstIndex = secondIndex = endIndex = 0;
		} else if(endIndex > len){
			endIndex = len;
		} 
		//		else if(endIndex == -1){
		//			endIndex = 0;
		//		}

		return new SliceDescriptor(firstIndex, secondIndex, endIndex);
	}

	public final IString $astr_slice_replace(final IString str, final Integer first, final Integer second, final Integer end, final IString repl) {
		SliceDescriptor sd = makeSliceDescriptor(first, second, end, str.length());
		return  str.replace(sd.first, sd.second, sd.end, repl);
	}

	public final INode $anode_slice_replace(final INode node, final Integer first, final Integer second, final Integer end, final IList repl) {
		SliceDescriptor sd = makeSliceDescriptor(first, second, end, node.arity());
		return  node.replace(sd.first, sd.second, sd.end, repl);
	}

	public final IList $alist_slice_replace(final IList lst, final Integer first, final Integer second, final Integer end, final IList repl) {
		SliceDescriptor sd = makeSliceDescriptor(first, second, end, lst.length());
		return  $updateListSlice(lst, sd, SliceOperator.replace, repl);
	}

	public final IList $alist_slice_add(final IList lst, final Integer first, final Integer second, final Integer end, final IList repl) {
		SliceDescriptor sd = makeSliceDescriptor(first, second, end, lst.length());
		return  $updateListSlice(lst, sd, SliceOperator.add, repl);
	}

	public final IList $alist_slice_subtract(final IList lst, final Integer first, final Integer second, final Integer end, final IList repl) {
		SliceDescriptor sd = makeSliceDescriptor(first, second, end, lst.length());
		return  $updateListSlice(lst, sd, SliceOperator.subtract, repl);
	}

	public final IList $alist_slice_product(final IList lst, final Integer first, final Integer second, final Integer end, final IList repl) {
		SliceDescriptor sd = makeSliceDescriptor(first, second, end, lst.length());
		return  $updateListSlice(lst, sd, SliceOperator.product, repl);
	}

	public final IList $alist_slice_divide(final IList lst, final Integer first, final Integer second, final Integer end, final IList repl) {
		SliceDescriptor sd = makeSliceDescriptor(first, second, end, lst.length());
		return  $updateListSlice(lst, sd, SliceOperator.divide, repl);
	}

	public final IList $updateListSlice(final IList lst, final SliceDescriptor sd, final SliceOperator op, final IList repl){
		IListWriter w = $VF.listWriter();
		int increment = sd.second - sd.first;
		int replIndex = 0;
		int rlen = repl.length();
		boolean wrapped = false;
		if(sd.first == sd.end || increment == 0){
			// nothing to be done
		} else
			if(sd.first <= sd.end){
				assert increment > 0;
				int listIndex = 0;
				while(listIndex < sd.first){
					w.append(lst.get(listIndex++));
				}
				while(listIndex >= 0 && listIndex < sd.end){
					w.append(op.execute(lst.get(listIndex), repl.get(replIndex++), this));
					if(replIndex == rlen){
						replIndex = 0;
						wrapped = true;
					}
					for(int q = 1; q < increment && listIndex + q < sd.end; q++){
						w.append(lst.get(listIndex + q));
					}
					listIndex += increment;
				}
				listIndex = sd.end;
				if(!wrapped){
					while(replIndex < rlen){
						w.append(repl.get(replIndex++));
					}
				}
				while(listIndex < lst.length()){
					w.append(lst.get(listIndex++));
				}
			} else {
				assert increment < 0;
				int j = lst.length() - 1;
				while(j > sd.first){
					w.insert(lst.get(j--));
				}
				while(j >= 0 && j > sd.end && j < lst.length()){
					w.insert(op.execute(lst.get(j), repl.get(replIndex++), this));
					if(replIndex == rlen){
						replIndex = 0;
						wrapped = true;
					}
					for(int q = -1; q > increment && j + q > sd.end; q--){
						w.insert(lst.get(j + q));
					}
					j += increment;
				}
				j = sd.end;
				if(!wrapped){
					while(replIndex < rlen){
						w.insert(repl.get(replIndex++));
					}
				}

				while(j >= 0){
					w.insert(lst.get(j--));
				}

			}
		return w.done();
	}

	// ---- splice ------------------------------------------------------------
	/**
	 * Splice elements in a list writer
	 * 
	 * IListWriter w, IListOrISet val  => w with val's elements spliced in
	 */
	public final IListWriter $listwriter_splice(final IListWriter writer, final IValue val) {
		if(val instanceof IList){
			IList lst = (IList) val;
			for(IValue v : lst){
				writer.append(v);
			}
		} else if(val instanceof ISet){
			ISet set = (ISet) val;
			for(IValue v : set){
				writer.append(v);
			}
		} else {
			writer.append((IValue) val);
		}
		return writer;
	}

	/**
	 * Splice elements in a set writer
	 * 
	 * ISetWriter w, IListOrISet val => w with val's elements spliced in
	 */

	public final ISetWriter $setwriter_splice(final ISetWriter writer, final IValue val) {
		if(val instanceof IList){
			IList lst = (IList) val;
			for(IValue v : lst){
				writer.insert(v);
			}
		} else if(val instanceof ISet){
			ISet set = (ISet) val;
			for(IValue v : set){
				writer.insert(v);
			}
		} else {
			writer.insert((IValue) val);
		}
		return writer;
	}

	// ---- subscript ---------------------------------------------------------

	public final IString $astr_subscript_int(final IString str, final int idx) {
		try {
			return (idx >= 0) ? str.substring(idx, idx+1)
					: str.substring(str.length() + idx, str.length() + idx + 1);
		} catch(IndexOutOfBoundsException e) {
			throw RuntimeExceptionFactory.indexOutOfBounds($VF.integer(idx));
		}
	}

	public final GuardedIValue $guarded_astr_subscript_int(final IString str, final int idx){
		try {
			IString res = (idx >= 0) ? str.substring(idx, idx+1)
					: str.substring(str.length() + idx, str.length() + idx + 1);
			return new GuardedIValue(res);
		} catch(IndexOutOfBoundsException e) {
			return UNDEFINED;
		}
	}
	
	public final IValue $alist_subscript_int(final IList lst, final int idx) {
		try {
			return lst.get((idx >= 0) ? idx : (lst.length() + idx));
		} catch(IndexOutOfBoundsException e) {
			throw RuntimeExceptionFactory.indexOutOfBounds($VF.integer(idx));
		}
	}

	public final GuardedIValue $guarded_list_subscript(final IList lst, final int idx) {
		try {
			return new GuardedIValue(lst.get((idx >= 0) ? idx : (lst.length() + idx)));
		} catch(IndexOutOfBoundsException e) {
			return UNDEFINED;
		}
	}
	
	public final IValue $amap_subscript(String name, final IMap map, final IValue idx) {
		IValue v = map.get(idx);
		if(v == null) {				
			throw RuntimeExceptionFactory.noSuchKey(idx);
		}
		return v;
	}
	
	public final IValue $amap_subscript(final IMap map, final IValue idx) {
		IValue v = map.get(idx);
		if(v == null) {
			throw RuntimeExceptionFactory.noSuchKey(idx);
		}
		return v;
	}

	public final GuardedIValue $guarded_map_subscript(final IMap map, final IValue idx) {
		IValue v = map.get(idx);
		return v == null? UNDEFINED : new GuardedIValue(v);
	}

	public final IValue $atuple_subscript_int(final ITuple tup, final int idx) {
		try {
			return tup.get((idx >= 0) ? idx : tup.arity() + idx);
		} catch(IndexOutOfBoundsException e) {
			throw RuntimeExceptionFactory.indexOutOfBounds($VF.integer(idx));
		}
	}

	public final GuardedIValue $guarded_atuple_subscript_int(final ITuple tup, final int idx) {
		try {
			IValue res = tup.get((idx >= 0) ? idx : tup.arity() + idx);
			return new GuardedIValue(res);
		} catch(IndexOutOfBoundsException e) {
			return UNDEFINED;
		}
	}

	public final IValue $anode_subscript_int(final INode node, int idx) {
		try {
			if(idx < 0){
				idx =  node.arity() + idx;
			}
			return node.get(idx);  
		} catch(IndexOutOfBoundsException e) {
			throw RuntimeExceptionFactory.indexOutOfBounds($VF.integer(idx));
		}
	}

	public final GuardedIValue $guarded_anode_subscript_int(final INode node, int idx) {
		try {
			if(idx < 0){
				idx =  node.arity() + idx;
			}
			IValue res = node.get(idx);  
			return new GuardedIValue(res);
		} catch(IndexOutOfBoundsException e) {
			return UNDEFINED;
		}
	}

	public final IValue $aadt_subscript_int(final IConstructor cons, final int idx) {
		try {
			return cons.get((idx >= 0) ? idx : (cons.arity() + idx));
		} catch(IndexOutOfBoundsException e) {
			throw RuntimeExceptionFactory.indexOutOfBounds($VF.integer(idx));
		}
	}

	public final GuardedIValue $guarded_aadt_subscript_int(final IConstructor cons, final int idx) {
		try {
			IValue res = cons.get((idx >= 0) ? idx : (cons.arity() + idx));
			return new GuardedIValue(res);
		} catch(IndexOutOfBoundsException e) {
			return UNDEFINED;
		}
	}
	
	// ---- arel_subscript ----------------------------------------------------

	/**
	 * Subscript of a n-ary rel with a single subscript (no set and unequal to _)
	 */
	
	public final ISet $arel_subscript1_noset(final ISet rel, final IValue idx) {
		if(rel.isEmpty()){
			return rel;
		}
		return rel.asRelation().index(idx);
	}
	
	public final GuardedIValue $guarded_arel_subscript1_noset(final ISet rel, final IValue idx) {
		try {
			return  new GuardedIValue($arel_subscript1_noset(rel, idx));
		} catch (Exception e) {
			return UNDEFINED;
		}
	}

	/**
	 * Subscript of a binary rel with a single subscript (a set but unequal to _)
	 */
	public final ISet $arel2_subscript1_aset(final ISet rel, final ISet idx) {
		if(rel.isEmpty()){
			return rel;
		}
		ISetWriter wset = $VF.setWriter();

		for (IValue v : rel) {
			ITuple tup = (ITuple)v;
			
			IValue tup0 = tup.get(0);
			if(tup0.getType().isSet() && idx.equals(tup0) || idx.contains(tup0)){
				wset.insert(tup.get(1));
			} 
		}
		return wset.done();
	}
	
	public final GuardedIValue $guarded_arel2_subscript1_aset(final ISet rel, final ISet idx) {
		try {
			return  new GuardedIValue($arel2_subscript1_aset(rel, idx));
		} catch (Exception e) {
			return UNDEFINED;
		}
	}

	/**
	 * Subscript of an n-ary (n > 2) rel with a single subscript (a set and unequal to _)
	 */
	public final ISet $arel_subscript1_aset(final ISet rel, final ISet idx) {
		if(rel.isEmpty()){
			return rel;
		}
		int relArity = rel.getElementType().getArity();		

		ISetWriter wset = $VF.setWriter();
		IValue args[] = new IValue[relArity - 1];

		for (IValue v : rel) {
			ITuple tup = (ITuple)v;
			IValue tup0 = tup.get(0);
			if(tup0.getType().isSet() && idx.equals(tup0) || idx.contains(tup0)){
				for (int i = 1; i < relArity; i++) {
					args[i - 1] = tup.get(i);
				}
				wset.insert($VF.tuple(args));
			} 
		}
		return wset.done();
	}
	
	public final GuardedIValue $guarded_arel_subscript1_aset(final ISet rel, final ISet idx) {
		try {
			return  new GuardedIValue($arel_subscript1_aset(rel, idx));
		} catch (Exception e) {
			return UNDEFINED;
		}
	}

	/**
	 * Subscript of rel, general case
	 * subsDesc is a subscript descriptor: an array with integers: 0: noset, 1: set, 2: wildcard
	 */

	public final ISet $arel_subscript (final ISet rel, final IValue[] idx, final int[] subsDesc) {
		if(rel.isEmpty()){
			return rel;
		}
		int indexArity = idx.length;
		int relArity = rel.getElementType().getArity();

		ISetWriter wset = $VF.setWriter();

		if(relArity - indexArity == 1){	// Return a set
			allValues:
				for (IValue v : rel) {
					ITuple tup = (ITuple)v;
					for(int k = 0; k < indexArity; k++){
						switch(subsDesc[k]){
						case 0: 
							if(!tup.get(k).equals(idx[k])) continue allValues; 
							continue;
						case 1: {
							IValue tup_k = tup.get(k);
							if(!(tup_k.getType().isSet() && idx[k].equals(tup_k) ||((ISet)idx[k]).contains(tup_k))) 
								continue allValues;
							}
						}
					}
					wset.insert(tup.get(indexArity));
				}
		} else {						// Return a relation
			IValue args[] = new IValue[relArity - indexArity];
			allValues:
				for (IValue v : rel) {
					ITuple tup = (ITuple)v;
					for(int k = 0; k < indexArity; k++){
						switch(subsDesc[k]){
						case 0: 
							if(!tup.get(k).equals(idx[k])) continue allValues; 
							continue;
						case 1: {
							IValue tup_k = tup.get(k);
							if(!(tup_k.getType().isSet() && idx[k].equals(tup_k) ||((ISet)idx[k]).contains(tup_k))) 
								continue allValues;
							}
						}
					}

					for (int i = indexArity; i < relArity; i++) {
						args[i - indexArity] = tup.get(i);
					}
					wset.insert($VF.tuple(args));
				}
		}

		return wset.done();
	}
	
	public final GuardedIValue $guarded_arel_subscript (final ISet rel, final IValue[] idx, final int[] subsDesc) {
		try {
			return  new GuardedIValue($arel_subscript(rel, idx, subsDesc));
		} catch (Exception e) {
			return UNDEFINED;
		}
	}
	
	// ---- alrel_subscript ---------------------------------------------------

	/**
	 * Subscript of a n-ary lrel with a single subscript (no set and unequal to _)
	 */

	public final IValue $alrel_subscript1_noset(final IList lrel, final IValue idx) {
		if(lrel.isEmpty()){
			return lrel;
		}
		if(idx instanceof IInteger) {
			return lrel.get(((IInteger) idx).intValue());
		}
		return lrel.asRelation().index(idx);
	}
		
	public final GuardedIValue $guarded_alrel_subscript1_noset(final IList lrel, final IValue idx) {
		try {
			return  new GuardedIValue($alrel_subscript1_noset(lrel, idx));
		} catch (Exception e) {
			return UNDEFINED;
		}
	}

	/**
	 * Subscript of a binary lrel with a single subscript (a set but unequal to _)
	 */
	public final IList $alrel2_subscript1_aset(final IList lrel, final ISet idx) {
		if(lrel.isEmpty()){
			return lrel;
		}
		IListWriter wlist = $VF.listWriter();

		for (IValue v : lrel) {
			ITuple tup = (ITuple)v;

			if(idx.contains(tup.get(0))){
				wlist.append(tup.get(1));
			} 
		}
		return wlist.done();
	}
		
	public final GuardedIValue $guarded_alrel2_subscript1_aset(final IList lrel, final ISet idx) {
		try {
			return  new GuardedIValue($alrel2_subscript1_aset(lrel, idx));
		} catch (Exception e) {
			return UNDEFINED;
		}
	}

	/**
	 * Subscript of an n-ary (n > 2) lrel with a single subscript (a set and unequal to _)
	 */
	public final IList $alrel_subscript1_aset(final IList lrel, final ISet index) {
		if(lrel.isEmpty()){
			return lrel;
		}
		int lrelArity = lrel.getElementType().getArity();		

		IListWriter wlist = $VF.listWriter();
		IValue args[] = new IValue[lrelArity - 1];

		for (IValue v : lrel) {
			ITuple tup = (ITuple)v;

			if(index.contains(tup.get(0))){
				for (int i = 1; i < lrelArity; i++) {
					args[i - 1] = tup.get(i);
				}
				wlist.append($VF.tuple(args));
			} 
		}
		return wlist.done();
	}
		
	public final GuardedIValue $guarded_alrel_subscript1_aset(final IList lrel, final ISet index) {
		try {
			return  new GuardedIValue($alrel_subscript1_aset(lrel, index));
		} catch (Exception e) {
			return UNDEFINED;
		}
	}

	/**
	 * Subscript of lrel, general case
	 * subsDesc is a subscript descriptor: an array with integers: 0: noset, 1: set, 2: wildcard
	 */

	public final IList $alrel_subscript (final IList lrel, final IValue[] idx, final int[] subsDesc) {
		if(lrel.isEmpty()){
			return lrel;
		}
		int indexArity = idx.length;
		int lrelArity = lrel.getElementType().getArity();

		IListWriter wlist = $VF.listWriter();

		if(lrelArity - indexArity == 1){	// Return a set
			allValues:
				for (IValue v : lrel) {
					ITuple tup = (ITuple)v;
					for(int k = 0; k < indexArity; k++){
						switch(subsDesc[k]){
						case 0: 
							if(!tup.get(k).equals(idx[k])) continue allValues; 
							continue;
						case 1: 
							if(!(((ISet)idx[k]).contains(tup.get(k)))) continue allValues;
						}
					}
					wlist.append(tup.get(indexArity));
				}
		} else {						// Return a relation
			IValue args[] = new IValue[lrelArity - indexArity];
			allValues:
				for (IValue v : lrel) {
					ITuple tup = (ITuple)v;
					for(int k = 0; k < indexArity; k++){
						switch(subsDesc[k]){
						case 0: 
							if(!tup.get(k).equals(idx[k])) continue allValues; 
							continue;
						case 1: 
							if(!((ISet)idx[k]).contains(tup.get(k))) continue allValues;
						}
					}

					for (int i = indexArity; i < lrelArity; i++) {
						args[i - indexArity] = tup.get(i);
					}
					wlist.append($VF.tuple(args));
				}
		}

		return wlist.done();
	}
		
	public final GuardedIValue $guarded_alrel_subscript (final IList lrel, final IValue[] idx, final int[] subsDesc) {
		try {
			return  new GuardedIValue($alrel_subscript(lrel, idx, subsDesc));
		} catch (Exception e) {
			return UNDEFINED;
		}
	}

	public final IValue $iter_subscript(final ITree subject, final int idx) {
		IList lst = org.rascalmpl.values.parsetrees.TreeAdapter.getArgs((ITree)subject);
		return lst.get(idx);
	}
	
	public final IValue $subscript_int(final ITuple tup, final int idx) {
		return tup.get(idx);
	}
	
	public final IValue $subscript_int(final INode nd, final int idx) {
		return nd.get(idx);
	}
	
	public final IString $subscript_int(final IString str, final int idx) {
		return str.substring(idx, 1);
	}
	
	public final IValue $subscript_int(final IValue subject, final int idx) {
		Type subjectType = subject.getType();
		if(subjectType.isList()) {
			return ((IList) subject).get(idx);
		}
		if(subjectType.isTuple()) {
			return ((ITuple) subject).get(idx);
		}
		if(subjectType.isString()) {
			return ((IString) subject).substring(idx, 1);
		}
		if(subjectType.isAbstractData()) {
//			if(subject instanceof ITree){
//				if(org.rascalmpl.values.parsetrees.TreeAdapter.isChar((ITree)subject)) {
//					return $VF.integer(org.rascalmpl.values.parsetrees.TreeAdapter.getCharacter((ITree)subject));
//				}
//				if(org.rascalmpl.values.parsetrees.TreeAdapter.isAppl((ITree)subject)) {
//					IConstructor prod = org.rascalmpl.values.parsetrees.TreeAdapter.getProduction((ITree)subject);
//					if(ProductionAdapter.isList(prod) || ProductionAdapter.isOpt(prod)) {
//						IList args = ((ITree)subject).getArgs();
//					    return args.get(idx);
//					}
//					if(idx == 0) {
//						return prod;
//					} else if(idx == 1){
//						return org.rascalmpl.values.parsetrees.TreeAdapter.getArgs((ITree)subject);
//					} else
//						throw new RuntimeException("Valid index on appl node is 0 or 1, found " + idx + " on " + subject);
//				}
//				return org.rascalmpl.values.parsetrees.TreeAdapter.getArgs((ITree)subject).get(idx);
//			}
			return ((IConstructor) subject).get(idx);
		}
		if(subjectType.isNode()) {
			return ((INode) subject).get(idx);
		}
		throw new RuntimeException("Unsupported subscript of type " + subject.getType());
	}
	
	// lexical subscript
	
	public final IValue $lexical_subscript(final org.rascalmpl.values.parsetrees.ITree subject, final int idx) {
		IList args = org.rascalmpl.values.parsetrees.TreeAdapter.getArgs(subject);
		try {
			return args.get((idx >= 0) ? idx : (args.length() + idx));
		} catch(IndexOutOfBoundsException e) {
			throw RuntimeExceptionFactory.indexOutOfBounds($VF.integer(idx));
		}
	}
	
	public final IValue $lexical_subscript_seps(final org.rascalmpl.values.parsetrees.ITree subject, final int idx) {
		IList args = org.rascalmpl.values.parsetrees.TreeAdapter.getArgs(subject);
		try {
			return args.get((idx >= 0) ? 2 * idx : (args.length() + 1 + 2 * idx));
		} catch(IndexOutOfBoundsException e) {
			throw RuntimeExceptionFactory.indexOutOfBounds($VF.integer(idx));
		}
	}
	
	// concrete subscript
	
	public final IValue $concrete_subscript(final org.rascalmpl.values.parsetrees.ITree subject, final int idx) {
		IList args = org.rascalmpl.values.parsetrees.TreeAdapter.getArgs(subject);
		
		try {
			return args.get((idx >= 0) ? 2 * idx : (args.length() + 1 + 2 * idx));
		} catch(IndexOutOfBoundsException e) {
			throw RuntimeExceptionFactory.indexOutOfBounds($VF.integer(idx));
		}
	}
	
	public final IValue $concrete_physical_subscript(final org.rascalmpl.values.parsetrees.ITree subject, final int idx) {
		IList args = org.rascalmpl.values.parsetrees.TreeAdapter.getArgs(subject);
		
		try {
			return args.get(idx);
		} catch(IndexOutOfBoundsException e) {
			throw RuntimeExceptionFactory.indexOutOfBounds($VF.integer(idx));
		}
	}
	
	public final IValue $concrete_subscript_seps(final org.rascalmpl.values.parsetrees.ITree subject, final int idx) {
		IList args = org.rascalmpl.values.parsetrees.TreeAdapter.getArgs(subject);
		IConstructor nt = ProductionAdapter.getType(TreeAdapter.getProduction(subject));
		int delta = SymbolAdapter.getSeparators(nt).size() + 1;
		
		int n = (idx >= 0) ? (delta * idx) : (args.length() - 1 + delta * (idx + 1));
		if(n >= 0 && n < args.length()) {
			return args.get(n);
		}
		throw RuntimeExceptionFactory.indexOutOfBounds($VF.integer(idx));
	}
	
	// ---- concreteSubList ---------------------------------------------------
	
	public final ITree $concreteSubList(final ITree tree, final int from, final int len, final int delta){
		if(org.rascalmpl.values.parsetrees.TreeAdapter.isList(tree)) {
			int adjusted_len = len;
			if(len > 0) {
				adjusted_len = len - delta + 1;
			}
			int actual_len = tree.getArgs().length();
			if(from >= actual_len) {
				return org.rascalmpl.values.parsetrees.TreeAdapter.setArgs(tree, $VF.list());
			}
			return org.rascalmpl.values.parsetrees.TreeAdapter.setArgs(tree, org.rascalmpl.values.parsetrees.TreeAdapter.getArgs(tree).sublist(from, adjusted_len));
		}
		throw RuntimeExceptionFactory.illegalArgument(tree);
	}

	
	
	// ---- typeOf ----------------------------------------------------------
	
//	public IConstructor $typeOf(IValue v) {
//		return reify2atype(v.getType(), empty);
//	}

	// ---- update ------------------------------------------------------------

	/**
	 * Update list element
	 * 
	 */

	public final IList $alist_update(int n, final IValue v, final IList lst) {
		if(n < 0){
			n = lst.length() + n;
		}
		try {
			return lst.put(n, v);
		} catch (IndexOutOfBoundsException e){
			throw RuntimeExceptionFactory.indexOutOfBounds($VF.integer(n));

		}
	}

	/**
	 * Update map element
	 * 
	 */
	
	public final IMap $amap_update(final IValue key, final IValue v, final IMap map) {	
		IMap res = map.put(key, v);
		return res;
	}

	/**
	 * Update tuple element
	 * 
	 */

	public final ITuple $atuple_update(final int n, final IValue v, final ITuple tup) {
		try {
			return tup.set(n, v);
		} catch (IndexOutOfBoundsException e){
			throw RuntimeExceptionFactory.indexOutOfBounds($VF.integer(n));

		}
	}

	/**
	 * Update argument of adt constructor by its field name
	 * 
	 */

	public final IConstructor $aadt_update(final IConstructor cons, final IString field, final IValue v) {
		return cons.set(field.getValue(), v);
	}

	public String $displayTestArgs(IValue[] args) {
		StringBuilder res = new StringBuilder("\n");
		for(int i = 0; i < args.length; i++) { res.append("arg #").append(i).append(": ").append(args[i]).append("\n");}
		return res.toString();
	}
	
	// Private methods for Slice Operator
	
	// ---- add ---------------------------------------------------------------
	// The following package private functions $add, $product, $multiply, $divide, $intersect are never
	// called from generated code, but are used by the SliceOperator that needs these generic versions.

	final IValue $add(final IValue lhs, final IValue rhs) {
			ToplevelType lhsType = ToplevelType.getToplevelType(lhs.getType());
			ToplevelType rhsType = ToplevelType.getToplevelType(rhs.getType());
			switch (lhsType) {
			case INT:
				switch (rhsType) {
				case INT:
					return ((IInteger) lhs).add((IInteger) rhs);
				case NUM:
					return ((IInteger) lhs).add((INumber) rhs);
				case REAL:
					return ((IInteger) lhs).add((IReal) rhs);
				case RAT:
					return ((IInteger) lhs).add((IRational) rhs);
				case LIST:
					return ((IList) rhs).insert(lhs);
				case SET:
					return ((ISet) rhs).insert(lhs);
				default:
					throw new InternalCompilerError("$RascalModule add: Illegal type combination: " + lhsType + " and " + rhsType);
				}
			case NUM:
				switch (rhsType) {
				case INT:
					return ((INumber) lhs).add((IInteger) rhs);
				case NUM:
					return ((INumber) lhs).add((INumber) rhs);
				case REAL:
					return ((INumber) lhs).add((IReal) rhs);
				case RAT:
					return ((INumber) lhs).add((IRational) rhs);
				case LIST:
					return ((IList) rhs).insert(lhs);
				case SET:
					return ((ISet) rhs).insert(lhs);
				default:
					throw new InternalCompilerError("$RascalModule add: Illegal type combination: " + lhsType + " and " + rhsType);
				}
			case REAL:
				switch (rhsType) {
				case INT:
					return ((IReal) lhs).add((IInteger) rhs);
				case NUM:
					return ((IReal) lhs).add((INumber) rhs);
				case REAL:
					return ((IReal) lhs).add((IReal) rhs);
				case RAT:
					return ((IReal) lhs).add((IRational) rhs);
				case LIST:
					return ((IList) rhs).insert(lhs);
				case SET:
					return ((ISet) rhs).insert(lhs);
				default:
					throw new InternalCompilerError("$RascalModule add: Illegal type combination: " + lhsType + " and " + rhsType);
				}
			case RAT:
				switch (rhsType) {
				case INT:
					return  ((IRational) lhs).add((IInteger) rhs);
				case NUM:
					return ((IRational) lhs).add((INumber) rhs);
				case REAL:
					return ((IRational) lhs).add((IReal) rhs);
				case RAT:
					return ((IRational) lhs).add((IRational) rhs);
				case LIST:
					return ((IList) rhs).insert(lhs);
				case SET:
					return ((ISet) rhs).insert(lhs);
				default:
					throw new InternalCompilerError("$RascalModule add: Illegal type combination: " + lhsType + " and " + rhsType);
				}
			case SET:
				return ((ISet) lhs).insert(rhs);

			case LIST:
				return ((IList) lhs).append(rhs);

			case LOC:
				switch (rhsType) {
				case STR:
					return $aloc_add_astr((ISourceLocation) lhs, (IString) rhs);
				default:
					throw new InternalCompilerError("$RascalModule add: Illegal type combination: " + lhsType + " and " + rhsType);
				}
			case LREL:
				switch (rhsType) {
				case LIST:
				case LREL:
					return ((IList) lhs).concat((IList)rhs);
				default:
					throw new InternalCompilerError("$RascalModule add: Illegal type combination: " + lhsType + " and " + rhsType);
				}
			case MAP:
				switch (rhsType) {
				case MAP:
					return ((IMap) lhs).compose((IMap) rhs);
				default:
					throw new InternalCompilerError("$RascalModule add: Illegal type combination: " + lhsType + " and " + rhsType);
				}
			case REL:
				switch (rhsType) {
				case SET:
				case REL:
					return ((ISet) lhs).union((ISet) rhs);
				default:
					throw new InternalCompilerError("$RascalModule add: Illegal type combination: " + lhsType + " and " + rhsType);
				}
			case STR:
				switch (rhsType) {
				case STR:
					return ((IString) lhs).concat((IString) rhs);
				default:
					throw new InternalCompilerError("$RascalModule add: Illegal type combination: " + lhsType + " and " + rhsType);
				}
			case TUPLE:
				switch (rhsType) {
				case TUPLE:
					return $atuple_add_atuple((ITuple) lhs, (ITuple) rhs);
				default:
					throw new InternalCompilerError("$RascalModule add: Illegal type combination: " + lhsType + " and " + rhsType);
				}
			default:
				switch (rhsType) {
				case LIST:
					return ((IList) rhs).insert(lhs);
				case SET:
					return ((ISet) rhs).insert(lhs);
				default:
					throw new InternalCompilerError("$RascalModule add: Illegal type combination: " + lhsType + " and " + rhsType);
				}
			}
		}
	
	// ---- subtract ----------------------------------------------------------

	 final IValue $subtract(final IValue lhs, final IValue rhs) {
			ToplevelType lhsType = ToplevelType.getToplevelType(lhs.getType());
			ToplevelType rhsType = ToplevelType.getToplevelType(rhs.getType());
			switch (lhsType) {
			case INT:
				switch (rhsType) {
				case INT:
					return ((IInteger) lhs).subtract((IInteger)rhs);
				case NUM:
					return ((IInteger) lhs).subtract((INumber)rhs);
				case REAL:
					return ((IInteger) lhs).subtract((IReal)rhs);
				case RAT:
					return ((IInteger) lhs).subtract((IRational)rhs);
				default:
					throw new InternalCompilerError("Illegal type combination: " + lhsType + " and " + rhsType);
				}
			case NUM:
				switch (rhsType) {
				case INT:
					return ((INumber) lhs).subtract((IInteger)rhs);
				case NUM:
					return ((INumber) lhs).subtract((INumber)rhs);
				case REAL:
					return ((INumber) lhs).subtract((IReal)rhs);
				case RAT:
					return ((INumber) lhs).subtract((IRational)rhs);
				default:
					throw new InternalCompilerError("Illegal type combination: " + lhsType + " and " + rhsType);
				}
			case REAL:
				switch (rhsType) {
				case INT:
					return ((IReal) lhs).subtract((IInteger)rhs);
				case NUM:
					return ((IReal) lhs).subtract((INumber)rhs);
				case REAL:
					return ((IReal) lhs).subtract((IReal)rhs);
				case RAT:
					return ((IReal) lhs).subtract((IRational)rhs);
				default:
					throw new InternalCompilerError("Illegal type combination: " + lhsType + " and " + rhsType);
				}
			case RAT:
				switch (rhsType) {
				case INT:
					return ((IRational) lhs).subtract((IInteger)rhs);
				case NUM:
					return ((IRational) lhs).subtract((INumber)rhs);
				case REAL:
					return ((IRational) lhs).subtract((IReal)rhs);
				case RAT:
					return ((IRational) lhs).subtract((IRational)rhs);
				default:
					throw new InternalCompilerError("Illegal type combination: " + lhsType + " and " + rhsType);
				}
			default:
				throw new InternalCompilerError("Illegal type combination: " + lhsType + " and " + rhsType);
			}
		}
	 
	// ---- product -----------------------------------------------------------

		public final IValue $product(final IValue lhs, final IValue rhs) {
			ToplevelType lhsType = ToplevelType.getToplevelType(lhs.getType());
			ToplevelType rhsType = ToplevelType.getToplevelType(rhs.getType());
			switch (lhsType) {
			case INT:
				switch (rhsType) {
				case INT:
					return ((IInteger) lhs).multiply((IInteger) rhs);
				case NUM:
					return ((IInteger) lhs).multiply((INumber) rhs);
				case REAL:
					return ((IInteger) lhs).multiply((IReal) rhs);
				case RAT:
					return ((IInteger) lhs).multiply((IRational) rhs);
				default:
					throw new InternalCompilerError("Illegal type combination: " + lhsType + " and " + rhsType);
				}
			case NUM:
				switch (rhsType) {
				case INT:
					return ((INumber) lhs).multiply((IInteger) rhs);
				case NUM:
					return ((INumber) lhs).multiply((INumber) rhs);
				case REAL:
					return ((INumber) lhs).multiply((IReal) rhs);
				case RAT:
					return ((INumber) lhs).multiply((IRational) rhs);
				default:
					throw new InternalCompilerError("Illegal type combination: " + lhsType + " and " + rhsType);
				}
			case REAL:
				switch (rhsType) {
				case INT:
					return ((IReal) lhs).multiply((IInteger) rhs);
				case NUM:
					return ((IReal) lhs).multiply((INumber) rhs);
				case REAL:
					return ((IReal) lhs).multiply((IReal) rhs);
				case RAT:
					return ((IReal) lhs).multiply((IRational) rhs);
				default:
					throw new InternalCompilerError("Illegal type combination: " + lhsType + " and " + rhsType);
				}
			case RAT:
				switch (rhsType) {
				case INT:
					return ((IRational) lhs).multiply((IInteger) rhs);
				case NUM:
					return ((IRational) lhs).multiply((INumber) rhs);
				case REAL:
					return ((IRational) lhs).multiply((IReal) rhs);
				case RAT:
					return ((IRational) lhs).multiply((IRational) rhs);
				default:
					throw new InternalCompilerError("Illegal type combination: " + lhsType + " and " + rhsType);
				}
			default:
				throw new InternalCompilerError("Illegal type combination: " + lhsType + " and " + rhsType);
			}
		}
		
		// ---- divide ------------------------------------------------------------

		final IValue $divide(final IValue lhs, final IValue rhs) {
			ToplevelType lhsType = ToplevelType.getToplevelType(lhs.getType());
			ToplevelType rhsType = ToplevelType.getToplevelType(rhs.getType());
			switch (lhsType) {
			case INT:
				switch (rhsType) {
				case INT:
					return $aint_divide_aint((IInteger) lhs, (IInteger) rhs);
				case NUM:
					return $aint_divide_anum((IInteger) lhs, (INumber) rhs);
				case REAL:
					return $aint_divide_areal((IInteger) lhs, (IReal) rhs);
				case RAT:
					return $aint_divide_arat((IInteger) lhs, (IRational) rhs);
				default:
					throw new InternalCompilerError("$RascalModule divide: Illegal type combination: " + lhsType + " and " + rhsType);
				}
			case NUM:
				switch (rhsType) {
				case INT:
					return $anum_divide_aint((INumber) lhs, (IInteger) rhs);
				case NUM:
					return $anum_divide_anum((INumber) lhs, (INumber) rhs);
				case REAL:
					return $anum_divide_areal((INumber) lhs, (IReal) rhs);
				case RAT:
					return $anum_divide_arat((INumber) lhs,  (IRational) rhs);
				default:
					throw new InternalCompilerError("$RascalModule divide: Illegal type combination: " + lhsType + " and " + rhsType);
				}
			case REAL:
				switch (rhsType) {
				case INT:
					return $areal_divide_aint((IReal) lhs, (IInteger) rhs);
				case NUM:
					return $areal_divide_anum((IReal) lhs, (INumber) rhs);
				case REAL:
					return $areal_divide_areal((IReal) lhs, (IReal) rhs);
				case RAT:
					return $areal_divide_arat((IReal) lhs, (IRational) rhs);
				default:
					throw new InternalCompilerError("$RascalModule divide: Illegal type combination: " + lhsType + " and " + rhsType);
				}
			case RAT:
				switch (rhsType) {
				case INT:
					return $arat_divide_aint((IRational) lhs, (IInteger) rhs);
				case NUM:
					return $arat_divide_anum((IRational) lhs, (INumber) rhs);
				case REAL:
					return $arat_divide_areal((IRational) lhs, (IReal) rhs);
				case RAT:
					return $arat_divide_arat((IRational) lhs, (IRational) rhs);
				default:
					throw new InternalCompilerError("$RascalModule divide: Illegal type combination: " + lhsType + " and " + rhsType);
				}
			default:
				throw new InternalCompilerError("$RascalModule divide: Illegal type combination: " + lhsType + " and " + rhsType);
			}
		}
		
		// ---- intersect ---------------------------------------------------------

		final IValue $intersect(final IValue left, final IValue right) {
			Type leftType = left.getType();
			Type rightType = right.getType();

			switch (ToplevelType.getToplevelType(leftType)) {
			case LIST:
				switch (ToplevelType.getToplevelType(rightType)) {
				case LIST:
				case LREL:
					return ((IList) left).intersect((IList) right);
				default:
					throw new InternalCompilerError("intersect: illegal combination " + leftType + " and " + rightType);
				}
			case SET:
				switch (ToplevelType.getToplevelType(rightType)) {
				case SET:
				case REL:
					return ((ISet) left).intersect((ISet) right);
				default:
					throw new InternalCompilerError("intersect: illegal combination " + leftType + " and " + rightType);
				}
			case MAP:
				return ((IMap) left).common((IMap) right);

			default:
				throw new InternalCompilerError("intersect: illegal combination " + leftType + " and " + rightType);
			}
		}
		
		protected IString $toIString(IValue v) { 
			if(v instanceof IString) {
					return (IString) v;
			} else if(v instanceof ITree) {
				return $VF.string(TreeAdapter.yield((ITree)v));
			} else  {
				return $VF.string(v.toString());
			}
		}
		
		
}

enum SliceOperator {
	
	replace(0) {
		@Override
		public IValue execute(final IValue left, final IValue right, $RascalModule rascalModule) {
			return right;
		}
	},
	add(1) {
		@Override
		public IValue execute(final IValue left, final IValue right, $RascalModule rascalModule) {
			return rascalModule.$add(left, right);
		}
	},
	subtract(2){
		@Override
		public IValue execute(final IValue left, final IValue right, $RascalModule rascalModule) {
			return rascalModule.$subtract(left, right);
		}
	}, 
	product(3){
		@Override
		public IValue execute(final IValue left, final IValue right, $RascalModule rascalModule) {
			return rascalModule.$product(left, right);
		}
	}, 

	divide(4){
		@Override
		public IValue execute(final IValue left, final IValue right, $RascalModule rascalModule) {
			return rascalModule.$divide(left, right);
		}
	}, 

	intersect(5){
		@Override
		public IValue execute(final IValue left, final IValue right, $RascalModule rascalModule) {
			return rascalModule.$intersect(left, right);
		}
	};

	final int operator;
	public static final SliceOperator[] values;
	
	static {
		values = values();
	}

	public final SliceOperator fromInteger(int n) {
		return values[n];
	}

	public abstract IValue execute(final IValue left, final IValue right, $RascalModule rascalModule);

	public final SliceOperator replace() {
		return values[0];
	}

	public final SliceOperator add() {
		return values[1];
	}

	public final SliceOperator subtract() {
		return values[2];
	}

	public final SliceOperator product() {
		return values[3];
	}

	public final SliceOperator divide() {
		return values[4];
	}

	public final SliceOperator intersect() {
		return values[5];
	}

	SliceOperator(int op) {
		this.operator = op;
	}
}
