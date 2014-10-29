/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jeroen van den Bos - Jeroen.van.den.Bos@cwi.nl (CWI)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.library.lang.jvm.transform;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValueFactory;
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
import org.rascalmpl.uri.FileURIResolver;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.values.ValueFactoryFactory;

public class Rascalify {

	private static final URIResolverRegistry _resolver;
	private static final ArrayList<LabelNode> _labels;
	
	static {
		_resolver = new URIResolverRegistry();
		_resolver.registerInputOutput(new FileURIResolver());
		_labels = new ArrayList<LabelNode>();
	}
	
	public Rascalify(IValueFactory values) {
		super();
	}

	@SuppressWarnings("unchecked")
	public static void deserializeToDisk(ISourceLocation source, ISourceLocation destination, IString moduleName) {
		try (InputStream inputStream = _resolver.getInputStream(source.getURI())) {
			ClassNode cn = new ClassNode();
      ClassReader cr = new ClassReader(inputStream);
			//cr.aaccept(cn, 0);
			
			OutputStreamWriter writer = new OutputStreamWriter(_resolver.getOutputStream(destination.getURI(), false));
			writer.write("module " + moduleName.getValue() + "\n\n");
			writer.write("import experiments::JVMBytecode::Opcodes;\n");
			writer.write("import experiments::JVMBytecode::SerializeClass;\n\n");
			writer.write("public Class generate" + moduleName.getValue() + "Class() {\n");
			writer.write("\treturn class(" + cn.version + ", " + cn.access + ", \"" + escape(cn.name) + "\", " + checkNull(cn.signature) + ", " + checkNull(cn.superName) + ",\n\t\t");
			writeStrings(writer, cn.interfaces);
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
			writer.write("\n\t\t\tfield(" + fn.access + ", \"" + escape(fn.name) + "\", \"" + escape(fn.desc) + "\", " + checkNull(fn.signature) + getValue(fn.value) + ")");
		}
		writer.write("\n\t\t]");
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
			writeStrings(writer, mn.exceptions);
			writer.write(",\n\t\t\t");
			writeInstructions(writer, mn);
			writer.write(",\n\t\t\t");
			writeTryCatchBlocks(writer, mn);
			writer.write(",\n\t\t\t");
			writeLocalVariables(writer, mn);
			writer.write(")");
		}
		writer.write("\n\t\t]");
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
		for (AbstractInsnNode ai : mn.instructions.toArray()) {
			if (first) { first = false; } else { writer.write(","); }
			if (ai instanceof FieldInsnNode) {
				FieldInsnNode n = ((FieldInsnNode)ai);
				writer.write("\n\t\t\t\tfieldRef(" + n.getOpcode() + ", \"" + escape(n.owner) + "\", \"" + escape(n.name) + "\", \"" + escape(n.desc) + "\")");
			} else if (ai instanceof IincInsnNode) {
				IincInsnNode n = ((IincInsnNode)ai);
				writer.write("\n\t\t\t\tincrement(" + n.var + ", " + n.incr + ")");
			} else if (ai instanceof InsnNode) {
				InsnNode n = ((InsnNode)ai);
				writer.write("\n\t\t\t\tinstruction(" + n.getOpcode() + ")");
			} else if (ai instanceof IntInsnNode) {
				IntInsnNode n = ((IntInsnNode)ai);
				writer.write("\n\t\t\t\tinteger(" + n.getOpcode() + ", " + n.operand + ")");
			} else if (ai instanceof JumpInsnNode) {
				JumpInsnNode n = ((JumpInsnNode)ai);
				writer.write("\n\t\t\t\tjump(" + n.getOpcode() + ", " + getLabelIndex(n.label) + ")");
			} else if (ai instanceof LabelNode) {
				LabelNode n = ((LabelNode)ai);
				writer.write("\n\t\t\t\tlabel(" + getLabelIndex(n) + ")");
			} else if (ai instanceof LineNumberNode) {
				LineNumberNode n = ((LineNumberNode)ai);
				writer.write("\n\t\t\t\tlineNumber(" + n.line + ", " + getLabelIndex(n.start) + ")");
			} else if (ai instanceof VarInsnNode) {
				VarInsnNode n = ((VarInsnNode)ai);
				writer.write("\n\t\t\t\tlocalVariable(" + n.getOpcode() + ", " + n.var + ")");
			} else if (ai instanceof LdcInsnNode) {
				LdcInsnNode n = ((LdcInsnNode)ai);
				if (n.cst instanceof String) {
					writer.write("\n\t\t\t\tloadConstantString(\"" + escape((String)n.cst) + "\")");
				} else if (n.cst instanceof Integer) {
					writer.write("\n\t\t\t\tloadConstantInteger(" + n.cst + ")");
				} else if (n.cst instanceof Long) {
					writer.write("\n\t\t\t\tloadConstantLong(" + n.cst + ")");
				} else if (n.cst instanceof Float) {
					writer.write("\n\t\t\t\tloadConstantFloat(" + n.cst + ")");
				} else if (n.cst instanceof Double) {
					writer.write("\n\t\t\t\tloadConstantDouble(" + n.cst + ")");
				}
			} else if (ai instanceof LookupSwitchInsnNode) {
				LookupSwitchInsnNode n = ((LookupSwitchInsnNode)ai);
				writer.write("\n\t\t\t\tlookupSwitch(" + getLabelIndex(n.dflt) + ", [");
				boolean firstKey = true;
				for (Integer k : (List<Integer>)n.keys) {
					if (firstKey) { firstKey = false; } else { writer.write(", "); }
					writer.write(k);
				}
				writer.write("], [");
				boolean firstCase = true;
				for (LabelNode l : (List<LabelNode>)n.labels) {
					if (firstCase) { firstCase = false; } else { writer.write(", "); }
					writer.write("" + getLabelIndex(l));
				}
				writer.write("])");
			} else if (ai instanceof MethodInsnNode) {
				MethodInsnNode n = ((MethodInsnNode)ai);
				writer.write("\n\t\t\t\tmethod(" + n.getOpcode() + ", \"" + escape(n.owner) + "\", \"" + escape(n.name) + "\", \"" + escape(n.desc) + "\")");
			} else if (ai instanceof MultiANewArrayInsnNode) {
				MultiANewArrayInsnNode n = ((MultiANewArrayInsnNode)ai);
				writer.write("\n\t\t\t\tmultiANewArray(\"" + escape(n.desc) + "\", " + n.dims + ")");
			} else if (ai instanceof TableSwitchInsnNode) {
				TableSwitchInsnNode n = ((TableSwitchInsnNode)ai);
				writer.write("\n\t\t\t\ttableSwitch(" + n.min + ", " + n.max + ", " + getLabelIndex(n.dflt) + ", [");
				boolean firstCase = true;
				for (LabelNode l : (List<LabelNode>)n.labels) {
					if (firstCase) { firstCase = false; } else { writer.write(", "); }
					writer.write("" + getLabelIndex(l));
				}
				writer.write("])");
			} else if (ai instanceof TypeInsnNode) {
				TypeInsnNode n = ((TypeInsnNode)ai);
				writer.write("\n\t\t\t\t\\type(" + n.getOpcode() + ", \"" + escape(n.desc) + "\")");
			} else {
				if (!(ai instanceof FrameNode)) {
					throw new RuntimeException("Error: Unsupported instruction encountered (" + ai.getClass() + ").");
				}
				first = true;
			}
		}
		writer.write("\n\t\t\t]");
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
		writer.write("[");
		boolean first = true;
		for (LocalVariableNode vn : (List<LocalVariableNode>)mn.localVariables) {
			if (first) { first = false; } else { writer.write(","); }
			writer.write("\n\t\t\t\tlocalVariable(\"" + escape(vn.name) + "\", \"" + escape(vn.desc) + "\", " + checkNull(vn.signature) + ", " + getLabelIndex(vn.start) + ", " + getLabelIndex(vn.end) + ", " + vn.index + ")");
		}
		writer.write("\n\t\t\t]");
	}
	
	private static String checkNull(String s) {
		if (s == null) {
			return "\"\"";
		}
		
		return "\"" + escape(s) + "\"";
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
