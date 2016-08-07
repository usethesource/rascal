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
	        
public class RSFIValueWriter {
    
    private final boolean doCaching = false;

	private final TrackLastWritten<Type> typeCache;
	private final TrackLastWritten<IValue> valueCache;
	private final TrackLastWritten<ISourceLocation> uriCache;
	
	protected static final byte[] header = { 'R', 'V', 1,0,0 };

	private final IInteger minInt;
	private final IInteger maxInt;

	private OutputStream basicOut;
	private RSFWriter writer;

	public RSFIValueWriter(OutputStream out, int typeWindowSize, int valueWindowSize, int uriWindowSize) throws IOException {
		this.basicOut = out;
		
		assert typeWindowSize > 0 && typeWindowSize < 255;
        assert valueWindowSize > 0 && valueWindowSize < 255;
        assert uriWindowSize > 0 && uriWindowSize < 255;
       
        out.write(header);
    	out.write(typeWindowSize);
    	out.write(valueWindowSize);
    	out.write(uriWindowSize);
    	
    	this.writer = new RSFWriter(out);

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
	
	RSFWriter getWriter() {
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
	            writer.startValue(RSF.PREVIOUS_TYPE_ID);
	            writer.writeField(RSF.PREVIOUS_ID, id);
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
	                writeAtomicType(RSF.BOOL_TYPE);
	                break;
	            }
	            case DATETIME:{
	                writeAtomicType(RSF.DATETIME_TYPE);
	                break;
	            }
	            case INT: {
                    writeAtomicType(RSF.INT_TYPE);
                    break;
                }
	            case NODE: {
                    writeAtomicType(RSF.NODE_TYPE);
                    break;
                }
	            case NUMBER: {
                    writeAtomicType(RSF.NUMBER_TYPE);
                    break;
                }
	            case RATIONAL: {
                    writeAtomicType(RSF.RATIONAL_TYPE);
                    break;
                }
	            case REAL: {
                    writeAtomicType(RSF.REAL_TYPE);
                    break;
                }
	            case LOC: {
                    writeAtomicType(RSF.LOC_TYPE);
                    break;
                }
	            case STR: {
                    writeAtomicType(RSF.STR_TYPE);
                    break;
                }
	            case VALUE: {
                    writeAtomicType(RSF.VALUE_TYPE);
                    break;
                }
	            case VOID: {
                    writeAtomicType(RSF.VOID_TYPE);
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
	                    writer.startValue(RSF.ADT_TYPE);

	                    writer.writeField(RSF.ADT_NAME,  adt.getName());

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
                      writer.startValue(RSF.ALIAS_TYPE);
                      writer.writeField(RSF.ALIAS_NAME,  alias.getName());
                      
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
	                    writer.startValue(RSF.CONSTRUCTOR_TYPE);
	                    writer.writeField(RSF.CONSTRUCTOR_NAME,  cons.getName());
	                    
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
                        writer.startValue(RSF.FUNCTION_TYPE);
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
                        writer.startValue(RSF.REIFIED_TYPE);
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
                        writer.startValue(RSF.OVERLOADED_TYPE);

                        Set<FunctionType> alternatives = ((OverloadedFunctionType) ovl).getAlternatives();
                        writer.writeField(RSF.OVERLOADED_SIZE, alternatives.size()); 
                        
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
                        writer.startValue(RSF.NONTERMINAL_TYPE);
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
	                    writer.startValue(RSF.LIST_TYPE);

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
	                    writer.startValue(RSF.MAP_TYPE);
	          
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
	                    writer.startValue(RSF.PARAMETER_TYPE);
	                    writer.writeField(RSF.PARAMETER_NAME, param.getName());
	                   
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
	                    writer.startValue(RSF.SET_TYPE);
	                   
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
	                    writer.startValue(RSF.TUPLE_TYPE);
	                    writer.writeField(RSF.TUPLE_ARITY, tuple.getArity());
	                    String[] fieldNames = tuple.getFieldNames();
	                    if(fieldNames != null){
	                        writeNames(RSF.TUPLE_NAMES, fieldNames);
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
	             writer.startValue(RSF.PREVIOUS_VALUE);
	             writer.writeField(RSF.PREVIOUS_VALUE_ID, id);
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
					writer.startValue(RSF.BOOL_VALUE);
					writer.writeField(RSF.BOOL_CONTENT, b.getValue() ? 1 : 0);
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
				    
					writer.startValue(RSF.CONSTRUCTOR_VALUE);
					writer.writeField(RSF.CONSTRUCTOR_ARITY, cons.arity());
					if(cons.mayHaveKeywordParameters()){
						if(cons.asWithKeywordParameters().hasParameters()){
							writer.writeField(RSF.CONSTRUCTOR_KWPARAMS, cons.asWithKeywordParameters().getParameters().size());
						}
					} else {
						if(cons.asAnnotatable().hasAnnotations()){
							writer.writeField(RSF.CONSTRUCTOR_ANNOS, cons.asAnnotatable().getAnnotations().size());
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
						writer.startValue(RSF.DATETIME_VALUE);
						
						writer.writeField(RSF.DATETIME_YEAR, dateTime.getYear());
						writer.writeField(RSF.DATETIME_MONTH, dateTime.getMonthOfYear());
						writer.writeField(RSF.DATETIME_DAY, dateTime.getDayOfMonth());
						
						writer.writeField(RSF.DATETIME_HOUR, dateTime.getHourOfDay());
						writer.writeField(RSF.DATETIME_MINUTE, dateTime.getMinuteOfHour());
						writer.writeField(RSF.DATETIME_SECOND, dateTime.getSecondOfMinute());
						writer.writeField(RSF.DATETIME_MILLISECOND, dateTime.getMillisecondsOfSecond());
						
						writer.writeField(RSF.DATETIME_TZ_HOUR, dateTime.getTimezoneOffsetHours());
						writer.writeField(RSF.DATETIME_TZ_MINUTE, dateTime.getTimezoneOffsetMinutes());
					} else if(dateTime.isDate()){
						writer.startValue(RSF.DATE_VALUE);
						
						writer.writeField(RSF.DATE_YEAR, dateTime.getYear());
						writer.writeField(RSF.DATE_MONTH, dateTime.getMonthOfYear());
						writer.writeField(RSF.DATE_DAY, dateTime.getDayOfMonth());
					} else {
						writer.startValue(RSF.TIME_VALUE);
						
						writer.writeField(RSF.TIME_HOUR, dateTime.getHourOfDay());
						writer.writeField(RSF.TIME_MINUTE, dateTime.getMinuteOfHour());
						writer.writeField(RSF.TIME_SECOND, dateTime.getSecondOfMinute());
						writer.writeField(RSF.TIME_MILLISECOND, dateTime.getMillisecondsOfSecond());
						
						writer.writeField(RSF.TIME_TZ_HOUR, dateTime.getTimezoneOffsetHours());
						writer.writeField(RSF.TIME_TZ_MINUTE, dateTime.getTimezoneOffsetMinutes());
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
						writer.startValue(RSF.INT_VALUE);
						writer.writeField(RSF.INT_CONTENT, n);
					} else {
						writer.startValue(RSF.BIGINT_VALUE);
						byte[] valueData = ii.getTwosComplementRepresentation();
						writer.writeField(RSF.BIGINT_CONTENT, valueData);
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
					writer.startValue(RSF.LIST_VALUE);
					writer.writeField(RSF.LIST_SIZE, lst.length());
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
					writer.startValue(RSF.MAP_VALUE);
					writer.writeField(RSF.MAP_SIZE, map.size());
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
					writer.startValue(RSF.NODE_VALUE);
					writer.writeField(RSF.NODE_NAME,  node.getName());
					writer.writeField(RSF.NODE_ARITY, node.arity());
					if(node.mayHaveKeywordParameters()){
						if(node.asWithKeywordParameters().hasParameters()){
							writer.writeField(RSF.NODE_KWPARAMS, node.asWithKeywordParameters().getParameters().size());
						}
					} else {
						if(node.asAnnotatable().hasAnnotations()){
							writer.writeField(RSF.NODE_ANNOS, node.asAnnotatable().getAnnotations().size());
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
					writer.startValue(RSF.RATIONAL_VALUE);
					endAndCacheValue(rat);
				}
				break;
			}
				
			case REAL: {
				assert it.atBeginning();

				IReal real = (IReal) it.getValue();
				if(!inCache(real)){
					writer.startValue(RSF.REAL_VALUE);
					byte[] valueData = real.unscaled().getTwosComplementRepresentation();
					writer.writeField(RSF.REAL_CONTENT, valueData);
					writer.writeField(RSF.REAL_SCALE, real.scale());
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
					writer.startValue(RSF.SET_VALUE);
					writer.writeField(RSF.SET_SIZE, set.size());
					endAndCacheValue(set);
				}
				break;
			}

			case LOC: {
				assert it.atBeginning();
				
				ISourceLocation loc = (ISourceLocation) it.getValue();
				if(!inCache(loc)){
					writer.startValue(RSF.LOC_VALUE);
					ISourceLocation uriPart = loc.top();
					int alreadyWritten = uriCache.howLongAgo(uriPart);
					if (alreadyWritten == -1) {
					    writer.writeField(RSF.LOC_SCHEME, uriPart.getScheme());
					    if (uriPart.hasAuthority()) {
					        writer.writeField(RSF.LOC_AUTHORITY, uriPart.getAuthority());
					    }
					    if (uriPart.hasPath()) {
					        writer.writeField(RSF.LOC_PATH, uriPart.getPath());
					    }
					    if (uriPart.hasQuery()) {
					        writer.writeField(RSF.LOC_QUERY,  uriPart.getQuery());
					    }
					    if (uriPart.hasFragment()) {
					        writer.writeField(RSF.LOC_FRAGMENT,  uriPart.getFragment());
					    }
					    uriCache.write(uriPart);
					}
					else {
					    writer.writeField(RSF.LOC_PREVIOUS_URI, alreadyWritten);
					}
					
					if(loc.hasOffsetLength()){
						writer.writeField(RSF.LOC_OFFSET, loc.getOffset());
						writer.writeField(RSF.LOC_LENGTH, loc.getLength());
					} 
					if(loc.hasLineColumn()){
						writer.writeField(RSF.LOC_BEGINLINE, loc.getBeginLine());
						writer.writeField(RSF.LOC_ENDLINE, loc.getEndLine());
						writer.writeField(RSF.LOC_BEGINCOLUMN, loc.getBeginColumn());
						writer.writeField(RSF.LOC_ENDCOLUMN, loc.getEndColumn());
					}
					endAndCacheValue(loc);
				}
				break;
			}
				
			case STR: {
				assert it.atBeginning();
				
				IString str = (IString) it.getValue();
				writer.startValue(RSF.STR_VALUE);
				writer.writeField(RSF.STR_CONTENT, str.getValue());
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
					writer.startValue(RSF.TUPLE_VALUE);
					writer.writeField(RSF.TUPLE_SIZE, tuple.arity());
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
    		 RSFIValueWriter ser = new RSFIValueWriter(out, 10, 10, 10);
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
                 RSFIValueReader reader = new RSFIValueReader(in, vf, ts);
                 System.out.println(reader.readValue());
             }
    		 
    	 }
    }
}
