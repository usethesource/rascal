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
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Function;
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
 * RSFIValueWriter is a binary serializer for IValues and Types. The main public functions is:
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
	
	public RSFWriter getWriter() {
		return writer;
	}
	
	
	
	private void writeAtomicType(int valId) throws IOException{
		writer.startValue(valId);
		writer.endValue();
	}
	
	private void writeName(int fieldId, String name) throws IOException{
			writer.writeField(fieldId, name);
	}
	
	public void writeNames(int fieldId, String[] names) throws IOException{
		int n = names.length;
		writer.writeField(fieldId, n);
		for(int i = 0; i < n; i++){
			writeName(fieldId, names[i]);
		}
	}
	
	private boolean inCache(Type type) throws IOException{
	    if(doCaching){
	        int id = typeCache.howLongAgo(type);
	        if(id > -1){
	            System.out.println("inCache: " + type + ", " + id);
	            writer.startValue(RSF.PreviousType.ID);
	            writer.writeField(RSF.PreviousType.HOW_LONG_AGO, id);
	            writer.endValue();
	            return true;
	        }
	    }
	    return false;
	}
	
	private void endAndCacheType(Type type) throws IOException{
        writer.endValue();
        typeCache.write(type);
    }
	
	 public void writeType(Type t) throws IOException {
	    PrePostTypeIterator it = new PrePostTypeIterator(t);
	    
	    while(it.hasNext()){
	        TypeIteratorKind kind = it.next();
	        switch(kind){

			// Atomic types
			 
	            case BOOL: {
	                writeAtomicType(RSF.BoolType.ID);
	                break;
	            }
	            case DATETIME:{
	                writeAtomicType(RSF.DateTimeType.ID);
	                break;
	            }
	            case INT: {
                    writeAtomicType(RSF.IntegerType.ID);
                    break;
                }
	            case NODE: {
                    writeAtomicType(RSF.NodeType.ID);
                    break;
                }
	            case NUMBER: {
                    writeAtomicType(RSF.NumberType.ID);
                    break;
                }
	            case RATIONAL: {
                    writeAtomicType(RSF.RationalType.ID);
                    break;
                }
	            case REAL: {
                    writeAtomicType(RSF.RealType.ID);
                    break;
                }
	            case LOC: {
                    writeAtomicType(RSF.SourceLocationType.ID);
                    break;
                }
	            case STR: {
                    writeAtomicType(RSF.StringType.ID);
                    break;
                }
	            case VALUE: {
                    writeAtomicType(RSF.ValueType.ID);
                    break;
                }
	            case VOID: {
                    writeAtomicType(RSF.VoidType.ID);
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
	                    writer.startValue(RSF.ADTType.ID);
	                    writer.writeField(RSF.ADTType.NAME,  adt.getName());
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
                      writer.startValue(RSF.AliasType.ID);
                      writer.writeField(RSF.AliasType.NAME,  alias.getName());
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
	                    writer.startValue(RSF.ConstructorType.ID);
	                    writer.writeField(RSF.ConstructorType.NAME,  cons.getName());
	                    
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
                        writer.startValue(RSF.ConstructorType.ID);
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
                        writer.startValue(RSF.ReifiedType.ID);
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
                        writer.startValue(RSF.OverloadedType.ID);

                        Set<FunctionType> alternatives = ((OverloadedFunctionType) ovl).getAlternatives();
                        writer.writeField(RSF.OverloadedType.SIZE, alternatives.size()); 
                        
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
                        writer.startValue(RSF.NonTerminalType.ID);
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
	                    writer.startValue(RSF.ListType.ID);

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
	                    writer.startValue(RSF.MapType.ID);
	          
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
	                    writer.startValue(RSF.ParameterType.ID);
	                    writer.writeField(RSF.ParameterType.NAME, param.getName());
	                   
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
	                    writer.startValue(RSF.SetType.ID);
	                   
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
	                    writer.startValue(RSF.TupleType.ID);
	                    writer.writeField(RSF.TupleType.ARITY, tuple.getArity());
	                    String[] fieldNames = tuple.getFieldNames();
	                    if(fieldNames != null){
	                        writeNames(RSF.TupleType.NAMES, fieldNames);
	                    }
	                    
	                    endAndCacheType(tuple);
	                }
	                break;
	            }
	        }
	    }
	}
	
	private boolean inCache(IValue v) throws IOException{
	     if(doCaching){
	         int id = valueCache.howLongAgo(v);
	         if(id > -1){
	             writer.startValue(RSF.PreviousValue.ID);
	             writer.writeField(RSF.PreviousValue.HOW_FAR_BACK, id);
	             writer.endValue();
	             return true;
	         }
	     }
	     return false;
	 }
	
	private void endAndCacheValue(IValue v) throws IOException{
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
					writer.startValue(RSF.BoolValue.ID);
					writer.writeField(RSF.BoolValue.VALUE, b.getValue() ? 1 : 0);
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
				    
					writer.startValue(RSF.ConstructorValue.ID);
					writer.writeField(RSF.ConstructorValue.ARITY, cons.arity());
					if(cons.mayHaveKeywordParameters()){
						if(cons.asWithKeywordParameters().hasParameters()){
							writer.writeField(RSF.ConstructorValue.KWPARAMS, cons.asWithKeywordParameters().getParameters().size());
						}
					} else {
						if(cons.asAnnotatable().hasAnnotations()){
							writer.writeField(RSF.ConstructorValue.ANNOS, cons.asAnnotatable().getAnnotations().size());
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
				    writer.startValue(RSF.DateTimeValue.ID);
/*				    writer.writeField(RSF.DATETIME_VARIANT, 
				            dateTime.isDateTime() ? RSF.DATETIME_VARIANT_DATETIME : 
				                (dateTime.isDate() ? RSF.DATETIME_VARIANT_DATE : RSF.DATETIME_VARIANT_TIME));
				                */

				    if (!dateTime.isTime()) {
                        writer.writeField(RSF.DateTimeValue.YEAR, dateTime.getYear());
                        writer.writeField(RSF.DateTimeValue.MONTH, dateTime.getMonthOfYear());
                        writer.writeField(RSF.DateTimeValue.DAY, dateTime.getDayOfMonth());
				    }

				    if (!dateTime.isDate()) {
                        writer.writeField(RSF.DateTimeValue.HOUR, dateTime.getHourOfDay());
                        writer.writeField(RSF.DateTimeValue.MINUTE, dateTime.getMinuteOfHour());
                        writer.writeField(RSF.DateTimeValue.SECOND, dateTime.getSecondOfMinute());
                        writer.writeField(RSF.DateTimeValue.MILLISECOND, dateTime.getMillisecondsOfSecond());

                        writer.writeField(RSF.DateTimeValue.TZ_HOUR, dateTime.getTimezoneOffsetHours());
                        writer.writeField(RSF.DateTimeValue.TZ_MINUTE, dateTime.getTimezoneOffsetMinutes());
				    }
				    endAndCacheValue(dateTime);
				}
				break;
			}

			case INT: {
				assert it.atBeginning();
				IInteger ii = (IInteger) it.getValue();
				if(!inCache(ii)){
				    writer.startValue(RSF.IntegerValue.ID);
					if(ii.greaterEqual(minInt).getValue() && ii.lessEqual(maxInt).getValue()){
						int n = ii.intValue();
						writer.writeField(RSF.IntegerValue.INTVALUE, n);
					} else {
						byte[] valueData = ii.getTwosComplementRepresentation();
						writer.writeField(RSF.IntegerValue.BIGVALUE, valueData);
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
					writer.startValue(RSF.ListValue.ID);
					writer.writeField(RSF.ListValue.SIZE, lst.length());
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
					writer.startValue(RSF.MapValue.ID);
					writer.writeField(RSF.MapValue.SIZE, map.size());
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
					writer.startValue(RSF.NodeValue.ID);
					writer.writeField(RSF.NodeValue.NAME,  node.getName());
					writer.writeField(RSF.NodeValue.ARITY, node.arity());
					if(node.mayHaveKeywordParameters()){
						if(node.asWithKeywordParameters().hasParameters()){
							writer.writeField(RSF.NodeValue.KWPARAMS, node.asWithKeywordParameters().getParameters().size());
						}
					} else {
						if(node.asAnnotatable().hasAnnotations()){
							writer.writeField(RSF.NodeValue.ANNOS, node.asAnnotatable().getAnnotations().size());
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
					writer.startValue(RSF.RationalValue.ID);
					endAndCacheValue(rat);
				}
				break;
			}
				
			case REAL: {
				assert it.atBeginning();

				IReal real = (IReal) it.getValue();
				if(!inCache(real)){
					writer.startValue(RSF.RealValue.ID);
					byte[] valueData = real.unscaled().getTwosComplementRepresentation();
					writer.writeField(RSF.RealValue.CONTENT, valueData);
					writer.writeField(RSF.RealValue.SCALE, real.scale());
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
					writer.startValue(RSF.SetValue.ID);
					writer.writeField(RSF.SetValue.SIZE, set.size());
					endAndCacheValue(set);
				}
				break;
			}

			case LOC: {
				assert it.atBeginning();
				
				ISourceLocation loc = (ISourceLocation) it.getValue();
				if(!inCache(loc)){
					writer.startValue(RSF.SourceLocationValue.ID);
					ISourceLocation uriPart = loc.top();
					int alreadyWritten = uriCache.howLongAgo(uriPart);
					if (alreadyWritten == -1) {
					    writer.writeField(RSF.SourceLocationValue.SCHEME, uriPart.getScheme());
					    if (uriPart.hasAuthority()) {
					        writer.writeField(RSF.SourceLocationValue.AUTHORITY, uriPart.getAuthority());
					    }
					    if (uriPart.hasPath()) {
					        writer.writeField(RSF.SourceLocationValue.PATH, uriPart.getPath());
					    }
					    if (uriPart.hasQuery()) {
					        writer.writeField(RSF.SourceLocationValue.QUERY,  uriPart.getQuery());
					    }
					    if (uriPart.hasFragment()) {
					        writer.writeField(RSF.SourceLocationValue.FRAGMENT,  uriPart.getFragment());
					    }
					    uriCache.write(uriPart);
					}
					else {
					    writer.writeField(RSF.SourceLocationValue.PREVIOUS_URI, alreadyWritten);
					}
					
					if(loc.hasOffsetLength()){
						writer.writeField(RSF.SourceLocationValue.OFFSET, loc.getOffset());
						writer.writeField(RSF.SourceLocationValue.LENGTH, loc.getLength());
					} 
					if(loc.hasLineColumn()){
						writer.writeField(RSF.SourceLocationValue.BEGINLINE, loc.getBeginLine());
						writer.writeField(RSF.SourceLocationValue.ENDLINE, loc.getEndLine());
						writer.writeField(RSF.SourceLocationValue.BEGINCOLUMN, loc.getBeginColumn());
						writer.writeField(RSF.SourceLocationValue.ENDCOLUMN, loc.getEndColumn());
					}
					endAndCacheValue(loc);
				}
				break;
			}
				
			case STR: {
				assert it.atBeginning();
				
				IString str = (IString) it.getValue();
				writer.startValue(RSF.StringValue.ID);
				writer.writeField(RSF.StringValue.CONTENT, str.getValue());
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
					writer.startValue(RSF.TupleValue.ID);
					writer.writeField(RSF.TupleValue.SIZE, tuple.arity());
					endAndCacheValue(tuple);
				}
				break;
			}
			
			default:
				 throw new RuntimeException("writeValue: unexpected kind of value " + kind);
			}
		}
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
