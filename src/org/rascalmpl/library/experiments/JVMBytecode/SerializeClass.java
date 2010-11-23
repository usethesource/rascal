package org.rascalmpl.library.experiments.JVMBytecode;

import static org.objectweb.asm.ClassWriter.COMPUTE_FRAMES;
import static org.objectweb.asm.Opcodes.V1_1;
import static org.objectweb.asm.Opcodes.V1_2;
import static org.objectweb.asm.Opcodes.V1_3;
import static org.objectweb.asm.Opcodes.V1_4;
import static org.objectweb.asm.Opcodes.V1_5;
import static org.objectweb.asm.Opcodes.V1_6;
import static org.objectweb.asm.Opcodes.V1_7;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IReal;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AnnotationNode;
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
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.values.ValueFactoryFactory;

public class SerializeClass {
	
	private final static HashMap<Integer, Integer> _javaMinorVersions;
	private final static HashMap<Integer, LabelNode> _labels;
	
	static {
		_javaMinorVersions = new HashMap<Integer, Integer>();
		_javaMinorVersions.put(1, V1_1);
		_javaMinorVersions.put(2, V1_2);
		_javaMinorVersions.put(3, V1_3);
		_javaMinorVersions.put(4, V1_4);
		_javaMinorVersions.put(5, V1_5);
		_javaMinorVersions.put(6, V1_6);
		_javaMinorVersions.put(7, V1_7);
		
		_labels = new HashMap<Integer, LabelNode>();
	}

	public SerializeClass(IValueFactory values) {
		super();
	}

	public static void serialize(IConstructor c, ISourceLocation path) {
		
		ClassNode cn = buildClass(c);
		
		ClassWriter cw = new ClassWriter(COMPUTE_FRAMES);
		cn.accept(cw);
		try {
			FileOutputStream f = new FileOutputStream(new File(path.getURI()));
			f.write(cw.toByteArray());
		} catch (FileNotFoundException e) {
			RuntimeExceptionFactory.pathNotFound(path, null, null);
		} catch (IOException e) {
			RuntimeExceptionFactory.io(ValueFactoryFactory.getValueFactory().string(e.getMessage()), null, null);
		}
	}

	private static ClassNode buildClass(IConstructor c) {
		ClassNode classNode = new ClassNode();

		int majorVersion = ((IInteger)((IConstructor)c.get(0)).get(0)).intValue();
		int minorVersion = ((IInteger)((IConstructor)c.get(0)).get(1)).intValue();
		if (majorVersion != 1 || !_javaMinorVersions.containsKey(minorVersion)) {
			RuntimeExceptionFactory.javaBytecodeError("Only supported versions are 1.1 to 1.7.", null, null);
		}
		classNode.version = _javaMinorVersions.get(minorVersion);
		classNode.access = ((IInteger)c.get(1)).intValue();
		classNode.name = ((IString)c.get(2)).getValue();
		classNode.signature = ((IString)c.get(3)).getValue();
		classNode.superName = ((IString)c.get(4)).getValue();

		ArrayList<String> interfaces = new ArrayList<String>();
		for (IValue v : ((IList)c.get(5))) {
			interfaces.add(((IString)v).getValue());
		}
		classNode.interfaces = interfaces;
		
		classNode.sourceFile = ((IString)c.get(6)).getValue();
		classNode.sourceDebug = ((IString)c.get(7)).getValue();
		classNode.outerClass = ((IString)c.get(8)).getValue();
		classNode.outerMethod = ((IString)c.get(9)).getValue();
		classNode.outerMethodDesc = ((IString)c.get(10)).getValue();
		
		classNode.visibleAnnotations = buildAnnotations(((IList)c.get(11)));
		classNode.invisibleAnnotations = buildAnnotations(((IList)c.get(12)));
		
		classNode.innerClasses = buildInnerClasses(((IList)c.get(13)));
		
		classNode.fields = buildFields(((IList)c.get(14)));
		
		classNode.methods = buildMethods(((IList)c.get(15)));
		
		return classNode;
	}

	@SuppressWarnings("unchecked")
	private static List<AnnotationNode> buildAnnotations(IList iList) {
		ArrayList<AnnotationNode> al = new ArrayList<AnnotationNode>();
		for (IValue v : iList) {
			AnnotationNode a = new AnnotationNode(((IString)((IConstructor)v).get(0)).getValue());
			for (IValue w : ((IList)((IConstructor)v).get(1))) {
				for (IValue x : ((ISet)w)) {
					a.values.add(x);
				}
			}
			al.add(a);
		}
		return al;
	}

	private static List<InnerClassNode> buildInnerClasses(IList iList) {
		ArrayList<InnerClassNode> al = new ArrayList<InnerClassNode>();
		for (IValue v : iList) {
			al.add(new InnerClassNode(((IString)((IConstructor)v).get(0)).getValue(),
									  ((IString)((IConstructor)v).get(1)).getValue(),
									  ((IString)((IConstructor)v).get(2)).getValue(),
									  ((IInteger)((IConstructor)v).get(3)).intValue()));
		}
		return al;
	}

	private static List<FieldNode> buildFields(IList iList) {
		ArrayList<FieldNode> al = new ArrayList<FieldNode>();
		for (IValue v : iList) {
			String desc = ((IString)((IConstructor)v).get(2)).getValue();
			Object initialValue = null;
			if (iList.length() > 4) {
				if (desc == "I") {
					initialValue = new Integer(((IInteger)((IConstructor)v).get(4)).intValue());
				} else if (desc == "J") {
					initialValue = new Long(((IInteger)((IConstructor)v).get(4)).longValue());
				} else if (desc == "F") {
					initialValue = new Float(((IReal)((IConstructor)v).get(4)).floatValue());
				} else if (desc == "D") {
					initialValue = new Double(((IReal)((IConstructor)v).get(4)).doubleValue());
				} else if (desc == "Ljava/lang/String") {
					initialValue = ((IString)((IConstructor)v).get(4)).getValue();
				}
			}
			al.add(new FieldNode(((IInteger)((IConstructor)v).get(0)).intValue(),
								 ((IString)((IConstructor)v).get(1)).getValue(),
								 ((IString)((IConstructor)v).get(2)).getValue(),
								 ((IString)((IConstructor)v).get(3)).getValue(),
								 initialValue));
		}
		return al;
	}

	private static List<MethodNode> buildMethods(IList iList) {
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
										   ((IString)((IConstructor)v).get(3)).getValue(),
										   ea);
			
			mn.visibleAnnotations = buildAnnotations((IList)((IConstructor)v).get(5));
			mn.invisibleAnnotations = buildAnnotations((IList)((IConstructor)v).get(6));
			mn.instructions = buildInstructions((IList)((IConstructor)v).get(7));
			mn.tryCatchBlocks = buildTryCatchBlocks((IList)((IConstructor)v).get(8));
			mn.localVariables = buildLocalVariables((IList)((IConstructor)v).get(9));
		}
		return ml;
	}

	private static InsnList buildInstructions(IList iList) {
		InsnList il = new InsnList();
		for (IValue v : iList) {
			if (((IConstructor)v).getName() == "field") {
				il.add(new FieldInsnNode(((IInteger)((IConstructor)v).get(0)).intValue(),
										 ((IString)((IConstructor)v).get(1)).getValue(),
										 ((IString)((IConstructor)v).get(2)).getValue(),
										 ((IString)((IConstructor)v).get(3)).getValue()));
			} else if (((IConstructor)v).getName() == "increment") {
				il.add(new IincInsnNode(((IInteger)((IConstructor)v).get(0)).intValue(),
										((IInteger)((IConstructor)v).get(1)).intValue()));
			} else if (((IConstructor)v).getName() == "instruction") {
				il.add(new InsnNode(((IInteger)((IConstructor)v).get(0)).intValue()));
			} else if (((IConstructor)v).getName() == "integer") {
				il.add(new IntInsnNode(((IInteger)((IConstructor)v).get(0)).intValue(),
									   ((IInteger)((IConstructor)v).get(1)).intValue()));
			} else if (((IConstructor)v).getName() == "jump") {
				il.add(new JumpInsnNode(((IInteger)((IConstructor)v).get(0)).intValue(),
										getLabel(((IInteger)((IConstructor)v).get(1)).intValue())));
			} else if (((IConstructor)v).getName() == "label") {
				il.add(getLabel(((IInteger)((IConstructor)v).get(1)).intValue()));
			} else if (((IConstructor)v).getName() == "lineNumber") {
				il.add(new LineNumberNode(((IInteger)((IConstructor)v).get(0)).intValue(),
										  getLabel(((IInteger)((IConstructor)v).get(1)).intValue())));
			} else if (((IConstructor)v).getName() == "localVariable") {
				il.add(new VarInsnNode(((IInteger)((IConstructor)v).get(0)).intValue(),
									   ((IInteger)((IConstructor)v).get(1)).intValue()));
			} else if (((IConstructor)v).getName() == "loadConstantString") {
				il.add(new LdcInsnNode(((IString)((IConstructor)v).get(0)).getValue()));
			} else if (((IConstructor)v).getName() == "loadConstantInteger") {
				il.add(new LdcInsnNode(((IInteger)((IConstructor)v).get(0)).intValue()));
			} else if (((IConstructor)v).getName() == "loadConstantLong") {
				il.add(new LdcInsnNode(((IInteger)((IConstructor)v).get(0)).longValue()));
			} else if (((IConstructor)v).getName() == "loadConstantFloat") {
				il.add(new LdcInsnNode(((IReal)((IConstructor)v).get(0)).floatValue()));
			} else if (((IConstructor)v).getName() == "loadConstantDouble") {
				il.add(new LdcInsnNode(((IReal)((IConstructor)v).get(0)).doubleValue()));
			} else if (((IConstructor)v).getName() == "lookupSwitch") {
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
			} else if (((IConstructor)v).getName() == "method") {
				il.add(new MethodInsnNode(((IInteger)((IConstructor)v).get(0)).intValue(),
										  ((IString)((IConstructor)v).get(1)).getValue(),
										  ((IString)((IConstructor)v).get(2)).getValue(),
										  ((IString)((IConstructor)v).get(3)).getValue()));
			} else if (((IConstructor)v).getName() == "multiANewArray") {
				il.add(new MultiANewArrayInsnNode(((IString)((IConstructor)v).get(0)).getValue(),
												  ((IInteger)((IConstructor)v).get(1)).intValue()));
			} else if (((IConstructor)v).getName() == "tableSwitch") {
				IList ll = (IList)((IConstructor)v).get(3);
				LabelNode la[] = new LabelNode[ll.length()];
				for (int i = 0; i < ll.length(); i++) {
					la[i] = getLabel(((IInteger)ll.get(i)).intValue());
				}
				il.add(new TableSwitchInsnNode(((IInteger)((IConstructor)v).get(0)).intValue(),
											   ((IInteger)((IConstructor)v).get(1)).intValue(),
											   getLabel(((IInteger)((IConstructor)v).get(2)).intValue()),
											   la));
			} else if (((IConstructor)v).getName() == "type") {
				il.add(new TypeInsnNode(((IInteger)((IConstructor)v).get(0)).intValue(),
						  				  ((IString)((IConstructor)v).get(1)).getValue()));
			}
		}
		return il;
	}

	private static List<TryCatchBlockNode> buildTryCatchBlocks(IList iList) {
		ArrayList<TryCatchBlockNode> al = new ArrayList<TryCatchBlockNode>();
		for (IValue v : iList) {
			al.add(new TryCatchBlockNode(getLabel(((IInteger)((IConstructor)v).get(0)).intValue()),
										 getLabel(((IInteger)((IConstructor)v).get(1)).intValue()),
										 getLabel(((IInteger)((IConstructor)v).get(2)).intValue()),
										 ((IString)((IConstructor)v).get(3)).getValue()));
		}
		return al;
	}

	private static LabelNode getLabel(int intValue) {
		if (!_labels.containsKey(intValue)) {
			_labels.put(intValue, new LabelNode());
		}
		return _labels.get(intValue);
	}

	private static List<LocalVariableNode> buildLocalVariables(IList iList) {
		ArrayList<LocalVariableNode> al = new ArrayList<LocalVariableNode>();
		for (IValue v : iList) {
			al.add(new LocalVariableNode(((IString)((IConstructor)v).get(0)).getValue(),
										 ((IString)((IConstructor)v).get(1)).getValue(),
										 ((IString)((IConstructor)v).get(2)).getValue(),
										 getLabel(((IInteger)((IConstructor)v).get(3)).intValue()),
										 getLabel(((IInteger)((IConstructor)v).get(4)).intValue()),
										 ((IInteger)((IConstructor)v).get(5)).intValue()));
		}
		return al;
	}

}
