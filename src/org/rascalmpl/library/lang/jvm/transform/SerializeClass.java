/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jeroen van den Bos - Jeroen.van.den.Bos@cwi.nl (CWI)
 *   * Atze van der Ploeg - Atze.van.der.Ploeg@cwi.nl (CWI)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.library.lang.jvm.transform;

import static org.objectweb.asm.ClassWriter.COMPUTE_FRAMES;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IReal;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.IincInsnNode;
import org.objectweb.asm.tree.InnerClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.LookupSwitchInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.MultiANewArrayInsnNode;
import org.objectweb.asm.tree.TableSwitchInsnNode;
import org.objectweb.asm.tree.TryCatchBlockNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.values.ValueFactoryFactory;

public class SerializeClass {
	
	public SerializeClass(IValueFactory values) {
		super();
	}
	
	public void serialize(IConstructor c, ISourceLocation path,IEvaluatorContext ctx){
		try {
			OutputStream output = URIResolverRegistry.getInstance().getOutputStream(path, false);
			new SerializeClassImplementation().serialize(c, output);
		} catch (FileNotFoundException e) {
			throw RuntimeExceptionFactory.pathNotFound(path, null, null);
		} catch (IOException e) {
			throw RuntimeExceptionFactory.io(ValueFactoryFactory.getValueFactory().string(e.getMessage()), null, null);
		} catch(Exception e){
			String msg = e.getMessage();
			ctx.getStdErr().print(msg);
			throw RuntimeExceptionFactory.io(ValueFactoryFactory.getValueFactory().string(msg != null ? msg : e.getClass().getSimpleName()), null, null);
		}
		
	}
	
	
	private class SerializeClassImplementation {
		private HashMap<Integer, LabelNode> _labels;
		
		SerializeClassImplementation(){
			_labels = new  HashMap<Integer, LabelNode>();
		}
		
		void serialize(IConstructor c,OutputStream output) throws IOException {
			ClassNode cn = buildClass(c);
			ClassWriter cw = new ClassWriter(COMPUTE_FRAMES);
			cn.accept(cw);
			output.write(cw.toByteArray());
			output.close();
		}

		ClassNode buildClass(IConstructor c) {
			ClassNode classNode = new ClassNode();
	
			classNode.version = ((IInteger)c.get(0)).intValue();
			classNode.access = ((IInteger)c.get(1)).intValue();
			classNode.name = ((IString)c.get(2)).getValue();
			classNode.signature = emptyIsNull(((IString)c.get(3)).getValue());
			classNode.superName = ((IString)c.get(4)).getValue();
	
			ArrayList<String> interfaces = new ArrayList<String>();
			for (IValue v : ((IList)c.get(5))) {
				interfaces.add(((IString)v).getValue());
			}
			classNode.interfaces = interfaces;
			
			classNode.sourceFile = emptyIsNull(((IString)c.get(6)).getValue());
			classNode.sourceDebug = emptyIsNull(((IString)c.get(7)).getValue());
			classNode.outerClass = emptyIsNull(((IString)c.get(8)).getValue());
			classNode.outerMethod = emptyIsNull(((IString)c.get(9)).getValue());
			classNode.outerMethodDesc = emptyIsNull(((IString)c.get(10)).getValue());
			
			classNode.innerClasses = buildInnerClasses(((IList)c.get(11)));
			
			classNode.fields = buildFields(((IList)c.get(12)));
			
			classNode.methods = buildMethods(((IList)c.get(13)));
			
			return classNode;
		}
	
		List<InnerClassNode> buildInnerClasses(IList iList) {
			ArrayList<InnerClassNode> al = new ArrayList<InnerClassNode>();
			for (IValue v : iList) {
				al.add(new InnerClassNode(((IString)((IConstructor)v).get(0)).getValue(),
										  emptyIsNull(((IString)((IConstructor)v).get(1)).getValue()),
										  emptyIsNull(((IString)((IConstructor)v).get(2)).getValue()),
										  ((IInteger)((IConstructor)v).get(3)).intValue()));
			}
			return al;
		}
	
		List<FieldNode> buildFields(IList iList) {
			ArrayList<FieldNode> al = new ArrayList<FieldNode>();
			for (IValue v : iList) {
				String desc = ((IString)((IConstructor)v).get(2)).getValue();
				Object initialValue = null;
				if (((IConstructor)v).arity() > 4) {
					if (desc.equals("I")) {
						initialValue = new Integer(((IInteger)((IConstructor)v).get(4)).intValue());
					} else if (desc.equals("J")) {
						initialValue = new Long(((IInteger)((IConstructor)v).get(4)).longValue());
					} else if (desc.equals("F")) {
						initialValue = new Float(((IReal)((IConstructor)v).get(4)).floatValue());
					} else if (desc.equals("D")) {
						initialValue = new Double(((IReal)((IConstructor)v).get(4)).doubleValue());
					} else if (desc.equals("Ljava/lang/String;")) {
						initialValue = ((IString)((IConstructor)v).get(4)).getValue();
					}
				}
				al.add(new FieldNode(((IInteger)((IConstructor)v).get(0)).intValue(),
									 ((IString)((IConstructor)v).get(1)).getValue(),
									 ((IString)((IConstructor)v).get(2)).getValue(),
									 emptyIsNull(((IString)((IConstructor)v).get(3)).getValue()),
									 initialValue));
			}
			return al;
		}
	
		List<MethodNode> buildMethods(IList iList) {
			ArrayList<MethodNode> ml = new ArrayList<MethodNode>();
			for (IValue v : iList) {
				IList eil = ((IList)((IConstructor)v).get(4));
				String ea[] = new String[eil.length()];
				for (int i = 0; i < eil.length(); i++) {
					ea[i] = ((IString)eil.get(i)).getValue();
				}
				MethodNode mn = new MethodNode(((IInteger)((IConstructor)v).get(0)).intValue(),
											   ((IString)((IConstructor)v).get(1)).getValue(),
											   ((IString)((IConstructor)v).get(2)).getValue(),
											   emptyIsNull(((IString)((IConstructor)v).get(3)).getValue()),
											   ea);
				
				mn.instructions = buildInstructions((IList)((IConstructor)v).get(5));
				mn.tryCatchBlocks = buildTryCatchBlocks((IList)((IConstructor)v).get(6));
				mn.localVariables = buildLocalVariables((IList)((IConstructor)v).get(7));
				ml.add(mn);
			}
			return ml;
		}
	
		InsnList buildInstructions(IList iList) {
			InsnList il = new InsnList();
			for (IValue v : iList) {
				if (((IConstructor)v).getName().equals("fieldRef")) {
					il.add(new FieldInsnNode(((IInteger)((IConstructor)v).get(0)).intValue(),
											 ((IString)((IConstructor)v).get(1)).getValue(),
											 ((IString)((IConstructor)v).get(2)).getValue(),
											 ((IString)((IConstructor)v).get(3)).getValue()));
				} else if (((IConstructor)v).getName().equals("increment")) {
					il.add(new IincInsnNode(((IInteger)((IConstructor)v).get(0)).intValue(),
											((IInteger)((IConstructor)v).get(1)).intValue()));
				} else if (((IConstructor)v).getName().equals("instruction")) {
					il.add(new InsnNode(((IInteger)((IConstructor)v).get(0)).intValue()));
				} else if (((IConstructor)v).getName().equals("integer")) {
					il.add(new IntInsnNode(((IInteger)((IConstructor)v).get(0)).intValue(),
										   ((IInteger)((IConstructor)v).get(1)).intValue()));
				} else if (((IConstructor)v).getName().equals("jump")) {
					il.add(new JumpInsnNode(((IInteger)((IConstructor)v).get(0)).intValue(),
											getLabel(((IInteger)((IConstructor)v).get(1)).intValue())));
				} else if (((IConstructor)v).getName().equals("label")) {
					il.add(getLabel(((IInteger)((IConstructor)v).get(0)).intValue()));
				} else if (((IConstructor)v).getName().equals("lineNumber")) {
					il.add(new LineNumberNode(((IInteger)((IConstructor)v).get(0)).intValue(),
											  getLabel(((IInteger)((IConstructor)v).get(1)).intValue())));
				} else if (((IConstructor)v).getName().equals("localVariable")) {
					il.add(new VarInsnNode(((IInteger)((IConstructor)v).get(0)).intValue(),
										   ((IInteger)((IConstructor)v).get(1)).intValue()));
				} else if (((IConstructor)v).getName().equals("loadConstantString")) {
					il.add(new LdcInsnNode(((IString)((IConstructor)v).get(0)).getValue()));
				} else if (((IConstructor)v).getName().equals("loadConstantInteger")) {
					il.add(new LdcInsnNode(((IInteger)((IConstructor)v).get(0)).intValue()));
				} else if (((IConstructor)v).getName().equals("loadConstantLong")) {
					il.add(new LdcInsnNode(((IInteger)((IConstructor)v).get(0)).longValue()));
				} else if (((IConstructor)v).getName().equals("loadConstantFloat")) {
					il.add(new LdcInsnNode(((IReal)((IConstructor)v).get(0)).floatValue()));
				} else if (((IConstructor)v).getName().equals("loadConstantDouble")) {
					il.add(new LdcInsnNode(((IReal)((IConstructor)v).get(0)).doubleValue()));
				} else if (((IConstructor)v).getName().equals("lookupSwitch")) {
					IList kl = (IList)((IConstructor)v).get(1);
					int ka[] = new int[kl.length()];
					for (int i = 0; i < kl.length(); i++) {
						ka[i] = ((IInteger)kl.get(i)).intValue();
					}
					IList ll = (IList)((IConstructor)v).get(2);
					LabelNode la[] = new LabelNode[ll.length()];
					for (int i = 0; i < ll.length(); i++) {
						la[i] = getLabel(((IInteger)ll.get(i)).intValue());
					}
					il.add(new LookupSwitchInsnNode(getLabel(((IInteger)((IConstructor)v).get(0)).intValue()),
													ka,
													la));
				} else if (((IConstructor)v).getName().equals("method")) {
					il.add(new MethodInsnNode(((IInteger)((IConstructor)v).get(0)).intValue(),
											  ((IString)((IConstructor)v).get(1)).getValue(),
											  ((IString)((IConstructor)v).get(2)).getValue(),
											  ((IString)((IConstructor)v).get(3)).getValue()));
				} else if (((IConstructor)v).getName().equals("multiANewArray")) {
					il.add(new MultiANewArrayInsnNode(((IString)((IConstructor)v).get(0)).getValue(),
													  ((IInteger)((IConstructor)v).get(1)).intValue()));
				} else if (((IConstructor)v).getName().equals("tableSwitch")) {
					IList ll = (IList)((IConstructor)v).get(3);
					LabelNode la[] = new LabelNode[ll.length()];
					for (int i = 0; i < ll.length(); i++) {
						la[i] = getLabel(((IInteger)ll.get(i)).intValue());
					}
					il.add(new TableSwitchInsnNode(((IInteger)((IConstructor)v).get(0)).intValue(),
												   ((IInteger)((IConstructor)v).get(1)).intValue(),
												   getLabel(((IInteger)((IConstructor)v).get(2)).intValue()),
												   la));
				} else if (((IConstructor)v).getName().equals("type")) {
					il.add(new TypeInsnNode(((IInteger)((IConstructor)v).get(0)).intValue(),
							  				  ((IString)((IConstructor)v).get(1)).getValue()));
				}
			}
			return il;
		}
	
		List<TryCatchBlockNode> buildTryCatchBlocks(IList iList) {
			ArrayList<TryCatchBlockNode> al = new ArrayList<TryCatchBlockNode>();
			for (IValue v : iList) {
				String type = null;
				if (iList.length() > 3) {
					type = ((IString)((IConstructor)v).get(3)).getValue();
				}
				al.add(new TryCatchBlockNode(getLabel(((IInteger)((IConstructor)v).get(0)).intValue()),
											 getLabel(((IInteger)((IConstructor)v).get(1)).intValue()),
											 getLabel(((IInteger)((IConstructor)v).get(2)).intValue()),
											 type));
			}
			return al;
		}
	
		LabelNode getLabel(int intValue) {
			if (!_labels.containsKey(intValue)) {
				_labels.put(intValue, new LabelNode());
			}
			return _labels.get(intValue);
		}
	
		List<LocalVariableNode> buildLocalVariables(IList iList) {
			ArrayList<LocalVariableNode> al = new ArrayList<LocalVariableNode>();
			for (IValue v : iList) {
				al.add(new LocalVariableNode(((IString)((IConstructor)v).get(0)).getValue(),
											 ((IString)((IConstructor)v).get(1)).getValue(),
											 emptyIsNull(((IString)((IConstructor)v).get(2)).getValue()),
											 getLabel(((IInteger)((IConstructor)v).get(3)).intValue()),
											 getLabel(((IInteger)((IConstructor)v).get(4)).intValue()),
											 ((IInteger)((IConstructor)v).get(5)).intValue()));
			}
			return al;
		}
		
		String emptyIsNull(String s) {
			if (s.equals("")) {
				return null;
			}
			
			return s;
		}
	}

}
