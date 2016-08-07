package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.serialize;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;

import org.rascalmpl.interpreter.types.FunctionType;
import org.rascalmpl.interpreter.types.NonTerminalType;
import org.rascalmpl.interpreter.types.OverloadedFunctionType;
import org.rascalmpl.interpreter.types.RascalTypeFactory;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.serialize.util.MapLastWritten;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.serialize.util.TrackLastWritten;
import org.rascalmpl.value.IBool;
import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.IDateTime;
import org.rascalmpl.value.IInteger;
import org.rascalmpl.value.IList;
import org.rascalmpl.value.IMap;
import org.rascalmpl.value.INode;
import org.rascalmpl.value.IRational;
import org.rascalmpl.value.IReal;
import org.rascalmpl.value.ISet;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IString;
import org.rascalmpl.value.ITuple;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeFactory;
import org.rascalmpl.value.type.TypeStore;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.RascalValueFactory;
        	
/**
 * RVMIValueWriter is a binary serializer for IValues and Types. The main public functions are:
 * - writeType
 * - writeValue
 */
	        
public class NewRVMIValueWriter {
    
    private final boolean doCaching = false;

	private final TrackLastWritten<Type> typeCache;
	private final TrackLastWritten<IValue> valueCache;
	private final TrackLastWritten<ISourceLocation> uriCache;
	
	protected static final byte[] header = { 'R', 'V', 1,0,0 };

	private final IInteger minInt;
	private final IInteger maxInt;

	private OutputStream basicOut;
	private NewStyleWriter writer;

	public NewRVMIValueWriter(OutputStream out, int typeWindowSize, int valueWindowSize, int uriWindowSize) throws IOException {
		this.basicOut = out;
		
		assert typeWindowSize > 0 && typeWindowSize < 255;
        assert valueWindowSize > 0 && valueWindowSize < 255;
        assert uriWindowSize > 0 && uriWindowSize < 255;
       
        out.write(header);
    	out.write(typeWindowSize);
    	out.write(valueWindowSize);
    	out.write(uriWindowSize);
    	
    	this.writer = new NewStyleWriter(out);

//		store = ts;
//		store.extendStore(RascalValueFactory.getStore());
		
		typeCache = new MapLastWritten<>(typeWindowSize * 1024);
		valueCache = new MapLastWritten<>(valueWindowSize * 1024);
		uriCache = new MapLastWritten<>(uriWindowSize * 1024);
		 
		IValueFactory vf = ValueFactoryFactory.getValueFactory();
		minInt = vf.integer(Integer.MIN_VALUE);
		maxInt = vf.integer(Integer.MAX_VALUE);
	}
	
	public void close() throws IOException {
		writer.flush();
		basicOut.close();
	}
	
	NewStyleWriter getWriter() {
		return writer;
	}
	
	private void writeAtomicType(int valId) throws IOException{
		writer.startValue(valId);
		writer.endValue();
	}
	
	void writeName(int fieldId, String name) throws IOException{
			writer.writeField(fieldId, name);
	}
	
	void writeNames(int fieldId, String[] names) throws IOException{
		int n = names.length;
		writer.writeField(fieldId, n);
		for(int i = 0; i < n; i++){
			writeName(fieldId, names[i]);
		}
	}
	
	boolean inCache(Type type) throws IOException{
	    if(doCaching){
	        int id = typeCache.howLongAgo(type);
	        if(id > -1){
	            System.out.println("inCache: " + type + ", " + id);
	            writer.startValue(Ser.PREVIOUS_TYPE_ID);
	            writer.writeField(Ser.PREVIOUS_ID, id);
	            writer.endValue();
	            return true;
	        }
	    }
	    return false;
	}
	
	void endAndCacheType(Type type) throws IOException{
        writer.endValue();
        typeCache.write(type);
    }
	
	 void writeType(Type t) throws IOException {
	    PrePostTypeIterator it = new PrePostTypeIterator(t);
	    
	    while(it.hasNext()){
	        TypeIteratorKind kind = it.next();
	        switch(kind){

			// Atomic types
			 
	            case BOOL: {
	                writeAtomicType(Ser.BOOL_TYPE);
	                break;
	            }
	            case DATETIME:{
	                writeAtomicType(Ser.DATETIME_TYPE);
	                break;
	            }
	            case INT: {
                    writeAtomicType(Ser.INT_TYPE);
                    break;
                }
	            case NODE: {
                    writeAtomicType(Ser.NODE_TYPE);
                    break;
                }
	            case NUMBER: {
                    writeAtomicType(Ser.NUMBER_TYPE);
                    break;
                }
	            case RATIONAL: {
                    writeAtomicType(Ser.RATIONAL_TYPE);
                    break;
                }
	            case REAL: {
                    writeAtomicType(Ser.REAL_TYPE);
                    break;
                }
	            case LOC: {
                    writeAtomicType(Ser.LOC_TYPE);
                    break;
                }
	            case STR: {
                    writeAtomicType(Ser.STR_TYPE);
                    break;
                }
	            case VALUE: {
                    writeAtomicType(Ser.VALUE_TYPE);
                    break;
                }
	            case VOID: {
                    writeAtomicType(Ser.VOID_TYPE);
                    break;
                }
	            
			// Composite types
	            
	            case ADT: {
	                Type adt = it.getValue();

	                if(it.atBeginning()){
	                    if(inCache(adt)){
	                        it.skipValue();
	                    }
	                } else {
	                    writer.startValue(Ser.ADT_TYPE);

	                    writer.writeField(Ser.ADT_NAME,  adt.getName());

	                    endAndCacheType(adt);
	                }
	                break;

	            }
	            case ALIAS: {
	                Type alias = it.getValue();
	                if(it.atBeginning()){
                        if(inCache(alias)){
                            it.skipValue();
                        }
                    } else {
                      writer.startValue(Ser.ALIAS_TYPE);
                      writer.writeField(Ser.ALIAS_NAME,  alias.getName());
                      
                      endAndCacheType(alias);
                    }
	                break;
	            }
	            case CONSTRUCTOR : {
	                Type cons = it.getValue();
	                if(it.atBeginning()){
	                    if(inCache(cons)){
	                        it.skipValue();
	                    }
	                } else {
	                    writer.startValue(Ser.CONSTRUCTOR_TYPE);
	                    writer.writeField(Ser.CONSTRUCTOR_NAME,  cons.getName());
	                    
	                    endAndCacheType(cons);
	                }
	                break;
	            }
	            case FUNCTION: {
	                Type ft = it.getValue();
                    if(it.atBeginning()){
                        if(inCache(ft)){
                            it.skipValue();
                        }
                    } else {
                        writer.startValue(Ser.FUNCTION_TYPE);
                        endAndCacheType(ft); 
                    }
                    break;
	                
	            }
	            
	            case REIFIED: {
	                Type reif = it.getValue();
                    if(it.atBeginning()){
                        if(inCache(reif)){
                            it.skipValue();
                        }
                    } else {
                        writer.startValue(Ser.REIFIED_TYPE);
                        endAndCacheType(reif); 
                    }
                    break;
	            }
	            
	            case OVERLOADED: {
	                Type ovl = it.getValue();
                    if(it.atBeginning()){
                        if(inCache(ovl)){
                            it.skipValue();
                        }
                    } else {
                        writer.startValue(Ser.OVERLOADED_TYPE);

                        Set<FunctionType> alternatives = ((OverloadedFunctionType) ovl).getAlternatives();
                        writer.writeField(Ser.OVERLOADED_SIZE, alternatives.size()); 
                        
                        endAndCacheType(ovl); 
                    }
                    break;
	            }
	            
	            case NONTERMINAL: {
	                NonTerminalType nt = (NonTerminalType) it.getValue();
	                if(it.atBeginning()){
                        if(inCache(nt)){
                            it.skipValue();
                        }
                    } else {
                        IConstructor cons = nt.getSymbol();
                        writeValue(cons);       // Constructor value comes before NONTERMINAL_TYPE
                        writer.startValue(Ser.NONTERMINAL_TYPE);
                        endAndCacheType(nt); 
                    }
	                break;
	            }
	            
	            case LIST: {
	                Type lst = it.getValue();
	                if(it.atBeginning()){
	                    if(inCache(lst)){
	                        it.skipValue();
	                    }
	                } else {
	                    writer.startValue(Ser.LIST_TYPE);

	                    endAndCacheType(lst);
	                }
	                break;
	            }
	            case MAP: {
	                Type map = it.getValue();
	                if(it.atBeginning()){
	                    if(inCache(map)){
	                        it.skipValue();
	                    }
	                } else {
	                    writer.startValue(Ser.MAP_TYPE);
	          
	                    endAndCacheType(map);
	                }
	                break;
	            }
	            case PARAMETER: {
	                Type param = it.getValue();
	                if(it.atBeginning()){
	                    if(inCache(param)){
	                        it.skipValue();
	                    }
	                } else {
	                    writer.startValue(Ser.PARAMETER_TYPE);
	                    writer.writeField(Ser.PARAMETER_NAME, param.getName());
	                   
	                    endAndCacheType(param);
	                }
	                break;
	            }

	            case SET: {
	                Type set = it.getValue();
	                if(it.atBeginning()){
	                    if(inCache(set)){
	                        it.skipValue();
	                    }
	                } else {
	                    writer.startValue(Ser.SET_TYPE);
	                   
	                    endAndCacheType(set);
	                }
	                break;
	            }
	            case TUPLE: {
	                Type tuple = it.getValue();
	                if(it.atBeginning()){
	                    if(inCache(tuple)){
	                        it.skipValue();
	                    }
	                } else {
	                    writer.startValue(Ser.TUPLE_TYPE);
	                    writer.writeField(Ser.TUPLE_ARITY, tuple.getArity());
	                    String[] fieldNames = tuple.getFieldNames();
	                    if(fieldNames != null){
	                        writeNames(Ser.TUPLE_NAMES, fieldNames);
	                    }
	                    
	                    endAndCacheType(tuple);
	                }
	                break;
	            }
	        }
	    }
	}
	
	 boolean inCache(IValue v) throws IOException{
	     if(doCaching){
	         int id = valueCache.howLongAgo(v);
	         if(id > -1){
	             writer.startValue(Ser.PREVIOUS_VALUE);
	             writer.writeField(Ser.PREVIOUS_VALUE_ID, id);
	             writer.endValue();
	             return true;
	         }
	     }
	     return false;
	 }
	
	void endAndCacheValue(IValue v) throws IOException{
		writer.endValue();
		valueCache.write(v);
	}
	
	/**
	 * Write value v to the output stream.
	 * @param v
	 * @throws IOException
	 */
	public void writeValue(IValue v) throws IOException{
		PrePostIValueIterator it = new PrePostIValueIterator(v);
		
		while(it.hasNext()){
			ValueIteratorKind kind = it.next();
			switch(kind){
			case BOOL: {
				assert it.atBeginning();
				IBool b = (IBool) it.getValue();
				if(!inCache(b)){
					writer.startValue(Ser.BOOL_VALUE);
					writer.writeField(Ser.BOOL_BOOL, b.getValue() ? 1 : 0);
					endAndCacheValue(b);
				}
				break;
			}

			case CONSTRUCTOR: {
				IConstructor cons = (IConstructor) it.getValue();
				if(it.atBeginning()){
					if(inCache(cons)){
						it.skipValue();
					}
				} else {
				    writeType(cons.getUninstantiatedConstructorType());
				    
					writer.startValue(Ser.CONSTRUCTOR_VALUE);
					writer.writeField(Ser.CONSTRUCTOR_ARITY, cons.arity());
					if(cons.mayHaveKeywordParameters()){
						if(cons.asWithKeywordParameters().hasParameters()){
							writer.writeField(Ser.CONSTRUCTOR_KWPARAMS, cons.asWithKeywordParameters().getParameters().size());
						}
					} else {
						if(cons.asAnnotatable().hasAnnotations()){
							writer.writeField(Ser.CONSTRUCTOR_ANNOS, cons.asAnnotatable().getAnnotations().size());
						}
					}
					endAndCacheValue(cons);
				}
				break;
			}
			
			case DATETIME: {
				assert it.atBeginning();
				
				IDateTime dateTime = (IDateTime) it.getValue();
				if(!inCache(dateTime)){
						if(dateTime.isDateTime()){
						writer.startValue(Ser.DATETIME_VALUE);
						
						writer.writeField(Ser.DATETIME_YEAR, dateTime.getYear());
						writer.writeField(Ser.DATETIME_MONTH, dateTime.getMonthOfYear());
						writer.writeField(Ser.DATETIME_DAY, dateTime.getDayOfMonth());
						
						writer.writeField(Ser.DATETIME_HOUR, dateTime.getHourOfDay());
						writer.writeField(Ser.DATETIME_MINUTE, dateTime.getMinuteOfHour());
						writer.writeField(Ser.DATETIME_SECOND, dateTime.getSecondOfMinute());
						writer.writeField(Ser.DATETIME_MILLISECOND, dateTime.getMillisecondsOfSecond());
						
						writer.writeField(Ser.DATETIME_TZ_HOUR, dateTime.getTimezoneOffsetHours());
						writer.writeField(Ser.DATETIME_TZ_MINUTE, dateTime.getTimezoneOffsetMinutes());
					} else if(dateTime.isDate()){
						writer.startValue(Ser.DATE_VALUE);
						
						writer.writeField(Ser.DATE_YEAR, dateTime.getYear());
						writer.writeField(Ser.DATE_MONTH, dateTime.getMonthOfYear());
						writer.writeField(Ser.DATE_DAY, dateTime.getDayOfMonth());
					} else {
						writer.startValue(Ser.TIME_VALUE);
						
						writer.writeField(Ser.TIME_HOUR, dateTime.getHourOfDay());
						writer.writeField(Ser.TIME_MINUTE, dateTime.getMinuteOfHour());
						writer.writeField(Ser.TIME_SECOND, dateTime.getSecondOfMinute());
						writer.writeField(Ser.TIME_MILLISECOND, dateTime.getMillisecondsOfSecond());
						
						writer.writeField(Ser.TIME_TZ_HOUR, dateTime.getTimezoneOffsetHours());
						writer.writeField(Ser.TIME_TZ_MINUTE, dateTime.getTimezoneOffsetMinutes());
					}
					endAndCacheValue(dateTime);
				}
				break;
			}

			case INT: {
				assert it.atBeginning();
				IInteger ii = (IInteger) it.getValue();
				if(!inCache(ii)){
					if(ii.greaterEqual(minInt).getValue() && ii.lessEqual(maxInt).getValue()){
						int n = ii.intValue();
						writer.startValue(Ser.INT_VALUE);
						writer.writeField(Ser.INT_INT, n);
					} else {
						writer.startValue(Ser.BIGINT_VALUE);
						byte[] valueData = ii.getTwosComplementRepresentation();
						writer.writeField(Ser.BIGINT_BIGINT, valueData);
					}
					endAndCacheValue(ii);
				}
				break;
			}
			
			case LIST: {
				IList lst = (IList) it.getValue();
				if(it.atBeginning()){
					if(inCache(lst)){
						it.skipValue();
					}
				} else {
					writer.startValue(Ser.LIST_VALUE);
					writer.writeField(Ser.LIST_SIZE, lst.length());
					endAndCacheValue(lst);
				}
				break;
			}
			
			case MAP: {
				IMap  map = (IMap) it.getValue();
				if(it.atBeginning()){
					if(inCache(map)){
						it.skipValue();
					}
				} else {
					writer.startValue(Ser.MAP_VALUE);
					writer.writeField(Ser.MAP_SIZE, map.size());
					endAndCacheValue(map);
				}
				break;
			}

			case NODE: {
				INode node = (INode) it.getValue();
				if(it.atBeginning()){
					if(inCache(node)){
						it.skipValue();
					}
				} else {
					writer.startValue(Ser.NODE_VALUE);
					writer.writeField(Ser.NODE_NAME,  node.getName());
					writer.writeField(Ser.NODE_ARITY, node.arity());
					if(node.mayHaveKeywordParameters()){
						if(node.asWithKeywordParameters().hasParameters()){
							writer.writeField(Ser.NODE_KWPARAMS, node.asWithKeywordParameters().getParameters().size());
						}
					} else {
						if(node.asAnnotatable().hasAnnotations()){
							writer.writeField(Ser.NODE_ANNOS, node.asAnnotatable().getAnnotations().size());
						}
					}
					endAndCacheValue(node);
				}
				break;
			}
					
			case RATIONAL: {
			    IRational rat = (IRational) it.getValue();
			    if(it.atBeginning()){
			        if(inCache(rat)){
			            it.skipValue();
			        }
			    } else {
					writer.startValue(Ser.RATIONAL_VALUE);
					endAndCacheValue(rat);
				}
				break;
			}
				
			case REAL: {
				assert it.atBeginning();

				IReal real = (IReal) it.getValue();
				if(!inCache(real)){
					writer.startValue(Ser.REAL_VALUE);
					byte[] valueData = real.unscaled().getTwosComplementRepresentation();
					writer.writeField(Ser.REAL_REAL, valueData);
					writer.writeField(Ser.REAL_SCALE, real.scale());
					endAndCacheValue(real);
				}
				break;
			}
			
			case SET: {
				ISet set = (ISet) it.getValue();
				if(it.atBeginning()){
					if(inCache(set)){
						it.skipValue();
					}
				} else {
					writer.startValue(Ser.SET_VALUE);
					writer.writeField(Ser.SET_SIZE, set.size());
					endAndCacheValue(set);
				}
				break;
			}

			case LOC: {
				assert it.atBeginning();
				
				ISourceLocation loc = (ISourceLocation) it.getValue();
				if(!inCache(loc)){
					writer.startValue(Ser.LOC_VALUE);
					ISourceLocation uriPart = loc.top();
					int alreadyWritten = uriCache.howLongAgo(uriPart);
					if (alreadyWritten == -1) {
					    writer.writeField(Ser.LOC_SCHEME, uriPart.getScheme());
					    if (uriPart.hasAuthority()) {
					        writer.writeField(Ser.LOC_AUTHORITY, uriPart.getAuthority());
					    }
					    if (uriPart.hasPath()) {
					        writer.writeField(Ser.LOC_PATH, uriPart.getPath());
					    }
					    if (uriPart.hasQuery()) {
					        writer.writeField(Ser.LOC_QUERY,  uriPart.getQuery());
					    }
					    if (uriPart.hasFragment()) {
					        writer.writeField(Ser.LOC_FRAGMENT,  uriPart.getFragment());
					    }
					    uriCache.write(uriPart);
					}
					else {
					    writer.writeField(Ser.LOC_PREVIOUS_URI, alreadyWritten);
					}
					
					if(loc.hasOffsetLength()){
						writer.writeField(Ser.LOC_OFFSET, loc.getOffset());
						writer.writeField(Ser.LOC_LENGTH, loc.getLength());
					} 
					if(loc.hasLineColumn()){
						writer.writeField(Ser.LOC_BEGINLINE, loc.getBeginLine());
						writer.writeField(Ser.LOC_ENDLINE, loc.getEndLine());
						writer.writeField(Ser.LOC_BEGINCOLUMN, loc.getBeginColumn());
						writer.writeField(Ser.LOC_ENDCOLUMN, loc.getEndColumn());
					}
					endAndCacheValue(loc);
				}
				break;
			}
				
			case STR: {
				assert it.atBeginning();
				
				IString str = (IString) it.getValue();
				writer.startValue(Ser.STR_VALUE);
				writer.writeField(Ser.STR_STR, str.getValue());
				writer.endValue();
				// Already cached at wire level
				break;
			}

			case TUPLE: {
				ITuple tuple = (ITuple) it.getValue();
				if(it.atBeginning()){
					if(inCache(tuple)){
						it.skipValue();
					}
				} else {
					writer.startValue(Ser.TUPLE_VALUE);
					writer.writeField(Ser.TUPLE_SIZE, tuple.arity());
					endAndCacheValue(tuple);
				}
				break;
			}
			default:
				 throw new RuntimeException("writeValue: unexpected kind of value " + kind);
			}
		}
//		writer.startValue(SValue.END_OF_VALUE);
//		writer.endValue();
	}
	
  // Test code
    
    public static void main(String[] args) throws Exception {
    	TypeFactory tf = TypeFactory.getInstance();
    	RascalTypeFactory rtf = RascalTypeFactory.getInstance();
    	IValueFactory vf = ValueFactoryFactory.getValueFactory();
    	TypeStore ts = RascalValueFactory.getStore();
    	 try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
    		 NewRVMIValueWriter ser = new NewRVMIValueWriter(out, 10, 10, 10);
    		 Type ct = tf.constructor(ts, tf.abstractDataType(ts, "D"), "f", tf.integerType(), tf.stringType());
    		 IConstructor nd = vf.constructor(ct, vf.integer(42));
    		 nd = nd.asWithKeywordParameters().setParameter("a", vf.integer(1));
    		 nd = nd.asWithKeywordParameters().setParameter("b", vf.string("xyz"));
    		 
    		 Type param = tf.parameterType("T");
    		 
    		 Type maybe = tf.abstractDataType(ts, "Maybe");
    		 
    		 Type none = tf.constructor(ts, maybe, "none");
    		 
    		 Type Bool = tf.abstractDataType(ts, "Bool");
    		 Type btrue = tf.constructor(ts, Bool, "btrue");
    		 Type bfalse = tf.constructor(ts, Bool, "bfalse");
    		 Type band = tf.constructor(ts, Bool, "band", Bool, Bool);
    		 Type bor = tf.constructor(ts, Bool, "bor", Bool, Bool);
    		 
    		 IValue trueval = vf.constructor(btrue);
    		 IValue falseval = vf.constructor(bfalse);
    		 
    		 IValue andval = vf.constructor(band, trueval, falseval);
    		 
    		 
    		 Type t = rtf.functionType(tf.integerType(), tf.tupleType(tf.stringType(), tf.boolType()), tf.voidType());
    		 IValue v = andval;
    		 System.out.println(v);
    		 ser.writeValue(v);
    		 ser.close();
    		 try (ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray())) {
                 NewRVMIValueReader reader = new NewRVMIValueReader(in, vf, ts);
                 System.out.println(reader.readValue());
             }
    		 
    	 }
    }
}
