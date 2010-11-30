package org.rascalmpl.library.experiments.JVMBytecode;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.impl.fast.ValueFactory;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.IincInsnNode;
import org.objectweb.asm.tree.InnerClassNode;
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
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.values.ValueFactoryFactory;

public class Rascalify {

	private static final URIResolverRegistry _resolver = new URIResolverRegistry();
	private static final ArrayList<LabelNode> _labels = new ArrayList<LabelNode>();
	
	public Rascalify(ValueFactory values) {
		super();
	}

	@SuppressWarnings("unchecked")
	public static void deserializeToDisk(ISourceLocation source, ISourceLocation destination, IString moduleName) {
		try {
			ClassNode cn = new ClassNode();
			ClassReader cr = new ClassReader(_resolver.getInputStream(source.getURI()));
			cr.accept(cn, 0);
			
			OutputStreamWriter writer = new OutputStreamWriter(_resolver.getOutputStream(destination.getURI(), false));
			writer.write("module " + moduleName.getValue() + "\n\n");
			writer.write("import experiments::JVMBytecode::Opcodes;\n");
			writer.write("import experiments::JVMBytecode::SerializeClass;\n\n");
			writer.write("public Class generate" + moduleName.getValue() + "Class() {\n");
			writer.write("\treturn class(" + cn.version + ", " + cn.access + ", \"" + escape(cn.name) + "\", " + checkNull(cn.signature) + ", " + checkNull(cn.superName) + ",\n\t\t");
			writeStrings(writer, (List<String>)cn.interfaces);
			writer.write(", " + checkNull(cn.sourceFile) + ", " + checkNull(cn.sourceDebug) + ", " + checkNull(cn.outerClass) + ", " + checkNull(cn.outerMethod) + ", " + checkNull(cn.outerMethodDesc) + ",\n\t\t");
			writerInnerClasses(cn, writer);
			writer.write(",\n\t\t");
			writeFields(cn, writer);
			writer.write(",\n\t\t");
			writeMethods(cn, writer);
			writer.write("\n\t);\n");
			writer.write("}");
			writer.close();
		} catch(FileNotFoundException e) {
			throw RuntimeExceptionFactory.pathNotFound(source, null, null);
		} catch(IOException e) {
			throw RuntimeExceptionFactory.io(ValueFactoryFactory.getValueFactory().string(e.getMessage()), null, null);
		}
	}

	@SuppressWarnings("unchecked")
	private static void writeFields(ClassNode cn, OutputStreamWriter writer) throws IOException {
		writer.write("[");
		boolean first = true;
		for (FieldNode fn : (List<FieldNode>)cn.fields) {
			if (first) { first = false; } else { writer.write(", "); }
			writer.write("field(" + fn.access + ", \"" + escape(fn.name) + "\", \"" + escape(fn.desc) + "\", " + checkNull(fn.signature) + getValue(fn.value) + ")");
		}
		writer.write("]");
	}

	private static String getValue(Object value) {
		if (value == null) return "";
		if (value instanceof String) return ", \"" + escape((String)value) + "\"";
		return value.toString();
	}

	@SuppressWarnings("unchecked")
	private static void writerInnerClasses(ClassNode cn, OutputStreamWriter writer) throws IOException {
		writer.write("[");
		boolean first = true;
		for (InnerClassNode icn : (List<InnerClassNode>)cn.innerClasses) {
			if (first) { first = false; } else { writer.write(", "); }
			writer.write("innerClass(\"" + escape(icn.name) + "\", " + checkNull(icn.outerName) + ", " + checkNull(icn.innerName) + ", " + icn.access + ")");
		}
		writer.write("]");
	}

	@SuppressWarnings("unchecked")
	private static void writeMethods(ClassNode cn, OutputStreamWriter writer)
			throws IOException {
		boolean first = true;
		writer.write("[\n");
		for (MethodNode mn : (List<MethodNode>)cn.methods) {
			if (first) { first = false; } else { writer.write(",\n"); }
			writer.write("\t\t\tmethod(" + mn.access + ", " + "\"" + escape(mn.name) + "\", " + "\"" + escape(mn.desc) + "\", " + checkNull(mn.signature) + ", ");
			writeStrings(writer, (List<String>)mn.exceptions);
			writer.write(",\n\t\t\t\t");
			writeInstructions(writer, mn);
			writer.write(",\n\t\t\t");
			writeTryCatchBlocks(writer, mn);
			writer.write(",\n\t\t\t");
			writeLocalVariables(writer, mn);
			writer.write(")");
		}
		writer.write("\t\t]");
	}

	@SuppressWarnings("unchecked")
	private static void writeTryCatchBlocks(OutputStreamWriter writer, MethodNode mn) throws IOException {
		writer.write("[");
		boolean first = true;
		for (TryCatchBlockNode tn : (List<TryCatchBlockNode>)mn.tryCatchBlocks) {
			if (first) { first = false; } else { writer.write(", "); }
			if (tn.type != null) {
				writer.write("tryCatchBlock(" + getLabelIndex(tn.start) + ", " + getLabelIndex(tn.end) + ", " + getLabelIndex(tn.handler) + ", \"" + escape(tn.type) + "\")");
			} else {
				writer.write("finallyBlock(" + getLabelIndex(tn.start) + ", " + getLabelIndex(tn.end) + ", " + getLabelIndex(tn.handler) + ")");
			}
		}
		writer.write("]");
	}

	@SuppressWarnings("unchecked")
	private static void writeInstructions(OutputStreamWriter writer, MethodNode mn) throws IOException {
		writer.write("[");
		boolean first = true;
		for (AbstractInsnNode ai : (AbstractInsnNode[])mn.instructions.toArray()) {
			if (first) { first = false; } else { writer.write(", "); }
			if (ai instanceof FieldInsnNode) {
				FieldInsnNode n = ((FieldInsnNode)ai);
				writer.write("fieldRef(" + n.getOpcode() + ", \"" + escape(n.owner) + "\", \"" + escape(n.name) + "\", \"" + escape(n.desc) + "\")");
			} else if (ai instanceof IincInsnNode) {
				IincInsnNode n = ((IincInsnNode)ai);
				writer.write("increment(" + n.var + ", " + n.incr + ")");
			} else if (ai instanceof InsnNode) {
				InsnNode n = ((InsnNode)ai);
				writer.write("instruction(" + n.getOpcode() + ")");
			} else if (ai instanceof IntInsnNode) {
				IntInsnNode n = ((IntInsnNode)ai);
				writer.write("integer(" + n.getOpcode() + ", " + n.operand + ")");
			} else if (ai instanceof JumpInsnNode) {
				JumpInsnNode n = ((JumpInsnNode)ai);
				writer.write("jump(" + n.getOpcode() + ", " + getLabelIndex(n.label) + ")");
			} else if (ai instanceof LabelNode) {
				LabelNode n = ((LabelNode)ai);
				writer.write("label(" + getLabelIndex(n) + ")");
			} else if (ai instanceof LineNumberNode) {
				LineNumberNode n = ((LineNumberNode)ai);
				writer.write("lineNumber(" + n.line + ", " + getLabelIndex(n.start) + ")");
			} else if (ai instanceof VarInsnNode) {
				VarInsnNode n = ((VarInsnNode)ai);
				writer.write("localVariable(" + n.getOpcode() + ", " + n.var + ")");
			} else if (ai instanceof LdcInsnNode) {
				LdcInsnNode n = ((LdcInsnNode)ai);
				if (n.cst instanceof String) {
					writer.write("loadConstantString(\"" + escape((String)n.cst) + "\")");
				} else if (n.cst instanceof Integer) {
					writer.write("loadConstantInteger(" + n.cst + ")");
				} else if (n.cst instanceof Long) {
					writer.write("loadConstantLong(" + n.cst + ")");
				} else if (n.cst instanceof Float) {
					writer.write("loadConstantFloat(" + n.cst + ")");
				} else if (n.cst instanceof Double) {
					writer.write("loadConstantDouble(" + n.cst + ")");
				}
			} else if (ai instanceof LookupSwitchInsnNode) {
				LookupSwitchInsnNode n = ((LookupSwitchInsnNode)ai);
				writer.write("lookupSwitch(" + getLabelIndex(n.dflt) + ", [");
				boolean firstKey = true;
				for (Integer k : (List<Integer>)n.keys) {
					if (firstKey) { firstKey = false; } else { writer.write(", "); }
					writer.write(k);
				}
				writer.write("], [");
				boolean firstCase = true;
				for (LabelNode l : (List<LabelNode>)n.labels) {
					if (firstCase) { firstCase = false; } else { writer.write(", "); }
					writer.write(getLabelIndex(l));
				}
				writer.write("])");
			} else if (ai instanceof MethodInsnNode) {
				MethodInsnNode n = ((MethodInsnNode)ai);
				writer.write("method(" + n.getOpcode() + ", \"" + escape(n.owner) + "\", \"" + escape(n.name) + "\", \"" + escape(n.desc) + "\")");
			} else if (ai instanceof MultiANewArrayInsnNode) {
				MultiANewArrayInsnNode n = ((MultiANewArrayInsnNode)ai);
				writer.write("multiANewArray(\"" + escape(n.desc) + "\", " + n.dims + ")");
			} else if (ai instanceof TableSwitchInsnNode) {
				TableSwitchInsnNode n = ((TableSwitchInsnNode)ai);
				writer.write("tableSwitch(" + n.min + ", " + n.max + ", " + getLabelIndex(n.dflt) + ", [");
				boolean firstCase = true;
				for (LabelNode l : (List<LabelNode>)n.labels) {
					if (firstCase) { firstCase = false; } else { writer.write(", "); }
					writer.write(getLabelIndex(l));
				}
				writer.write("])");
			} else if (ai instanceof TypeInsnNode) {
				TypeInsnNode n = ((TypeInsnNode)ai);
				writer.write("\\type(" + n.getOpcode() + ", \"" + escape(n.desc) + "\")");
			} else {
				if (!(ai instanceof FrameNode)) {
					throw new RuntimeException("Error: Unsupported instruction encountered (" + ai.getClass() + ").");
				}
				first = true;
			}
		}
		writer.write("]");
	}

	private static void writeStrings(OutputStreamWriter writer, List<String> sl)
			throws IOException {
		writer.write("[");
		boolean first = true;
		for (String ex : sl) {
			if (first) { first = false; } else { writer.write(", "); }
			writer.write("\"" + escape(ex) + "\"");
		}
		writer.write("]");
	}

	@SuppressWarnings("unchecked")
	private static void writeLocalVariables(OutputStreamWriter writer, MethodNode mn)
			throws IOException {
		writer.write("[\n");
		boolean first = true;
		for (LocalVariableNode vn : (List<LocalVariableNode>)mn.localVariables) {
			if (first) { first = false; } else { writer.write(",\n"); }
			writer.write("\t\t\tlocalVariable(\"" + escape(vn.name) + "\", \"" + escape(vn.desc) + "\", " + checkNull(vn.signature) + ", " + getLabelIndex(vn.start) + ", " + getLabelIndex(vn.end) + ", " + vn.index + ")");
		}
		writer.write("]");
	}
	
	private static String checkNull(String s) {
		if (s == null) {
			return "\"\"";
		} else {
			return "\"" + escape(s) + "\"";
		}
	}
	
	private static String escape(String s) {
		return s.replace("<", "\\<").replace(">", "\\>");
	}

	private static int getLabelIndex(LabelNode ln) {
		if (!_labels.contains(ln)) {
			_labels.add(ln);
		}
		return _labels.indexOf(ln);
	}

}
