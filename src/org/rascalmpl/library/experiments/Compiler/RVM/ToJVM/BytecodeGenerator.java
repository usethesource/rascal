package org.rascalmpl.library.experiments.Compiler.RVM.ToJVM;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.xml.bind.DatatypeConverter;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CompilerError;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Function;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.MuPrimitive;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.OverloadedFunction;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalPrimitive;
import org.rascalmpl.value.IInteger;
import org.rascalmpl.value.IList;
import org.rascalmpl.value.IMap;
import org.rascalmpl.value.IString;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.io.StandardTextWriter;

public class BytecodeGenerator implements Opcodes {

	// Locations of the variables in a compiled RVM function.
	
	// Common local variables
	public static final int THIS = 0;
	public static final int CF = 1;				// Current frame
	public static final int SP = 2;				// RVM stack pointer: int sp
	public static final int STACK = 3;			// RVM stack: Object stack[]
	
	// Local variables in coroutines
	
	public static final int LBOOL = 4;			// Boolean variable used in guard code
	public static final int LVAL = 5;			// Local IValue
	public static final int LCOROUTINE = 6;		// Local corotuine instance
	public static final int LPREVFRAME = 7;		// 
	public static final int EXCEPTION = 8;
	
	// Only needed when function needed constant store or type store
	public static final int CS = 9;				// Constant store
	public static final int TS = 10;			// Type constant store
	
	public static final int TMPOBJECT = 11;		// Scratch location

	byte[] endCode = null;
	
	private ClassWriter cw = null;
	private MethodVisitor mv = null;
	private FieldVisitor fv = null;
	private String className = null;
	private String packageName = null;
	private String fullClassName = null;

	private String[] funcArray = null;			// List of compiler-generated function names
	private boolean emit = true;				// TODO: remove

	// Administration per function
	private HashMap<String, Label> labelMap = new HashMap<String, Label>();
	private Set<String> catchTargetLabels = new HashSet<String>();			
	private Set<String> storeCreators = new HashSet<String>();
	private Map<String, ExceptionLine> catchTargets = new HashMap<String, ExceptionLine>();
	private Label[] hotEntryLabels = null;		// entry labels for coroutines
	private Label exitLabel = null;				// special case for labels without code
												// TODO: peephole optimizer now removes them; can disappear?

	ArrayList<Function> functionStore;
	ArrayList<OverloadedFunction> overloadedStore;
	Map<String, Integer> functionMap;
	Map<String, Integer> constructorMap;
	Map<String, Integer> resolver;

	private Label getNamedLabel(String targetLabel) {
		Label lb = labelMap.get(targetLabel);
		if (lb == null) {
			lb = new Label();
			labelMap.put(targetLabel, lb);
		}
		return lb;
	}

	public BytecodeGenerator(ArrayList<Function> functionStore, ArrayList<OverloadedFunction> overloadedStore,
			Map<String, Integer> functionMap, Map<String, Integer> constructorMap, Map<String, Integer> resolver) {
		emit = true;

		this.functionStore = functionStore;
		this.overloadedStore = overloadedStore;
		this.functionMap = functionMap;
		this.resolver = resolver;
		this.constructorMap = constructorMap;
	}

	public BytecodeGenerator() {
		emit = false;
	}

	void enableOutput(boolean flag) {
		emit = flag;
	}

	static final int CHUNKSIZE = 1024 ; // test value.
	private void emitStringValue(String str) {
		int len = str.length() ;
		int chunks = len / CHUNKSIZE ;
		
		int i = 0 ;
		if ( len > CHUNKSIZE ) { 
			for ( i = 0 ; i < chunks ; i++) {
				mv.visitLdcInsn(str.substring(i * CHUNKSIZE, i* CHUNKSIZE + CHUNKSIZE));	
			}
			if ( len > (chunks * CHUNKSIZE) ) {
				/// final partial chunk
				mv.visitLdcInsn(str.substring(i*CHUNKSIZE, len));					
			}
			else {
				chunks-- ;  // No partial chunk perfect fit.
			}
			while ( chunks != 0 ) {
				mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "concat", "(Ljava/lang/String;)Ljava/lang/String;", false);
				chunks-- ;
			}
		}
		else {
			mv.visitLdcInsn(str);				
		}
	}
	
	private void emitIntValue(int value) {
		switch (value) {
		case 0:
			mv.visitInsn(ICONST_0);
			return;
		case 1:
			mv.visitInsn(ICONST_1);
			return;
		case 2:
			mv.visitInsn(ICONST_2);
			return;
		case 3:
			mv.visitInsn(ICONST_3);
			return;
		case 4:
			mv.visitInsn(ICONST_4);
			return;
		case 5:
			mv.visitInsn(ICONST_5);
			return;
		}
		if (value >= -128 && value <= 127)
			mv.visitIntInsn(BIPUSH, value); // 8 bit
		else if (value >= -32768 && value <= 32767)
			mv.visitIntInsn(SIPUSH, value); // 16 bit
		else
			mv.visitLdcInsn(new Integer(value)); // REST
	}

	public static String anySerialize(Object o) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(o);
		oos.close();
		return DatatypeConverter.printBase64Binary(baos.toByteArray());
	}

	public static Object anyDeserialize(String s) throws IOException, ClassNotFoundException {
		ByteArrayInputStream bais = new ByteArrayInputStream(DatatypeConverter.parseBase64Binary(s));
		ObjectInputStream ois = new ObjectInputStream(bais);
		Object o = ois.readObject();
		ois.close();
		return o;
	}

	/*
	 * Generate the constructor method for the generated class
	 */
	public void emitConstructor(OverloadedFunction[] overloadedStore) {
		if (!emit)
			return;

		mv = cw.visitMethod(ACC_PUBLIC, "<init>",
				"(Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RVMExecutable;Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RascalExecutionContext;)V",
				null, null);

		mv.visitCode();
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, 1); // rvmExec
		mv.visitVarInsn(ALOAD, 2); // rex
		mv.visitMethodInsn(INVOKESPECIAL, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RVMRun", "<init>",
				"(Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RVMExecutable;Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RascalExecutionContext;)V",false);

		// Serialize/Deserialize overloadedStore.
		try {
			mv.visitVarInsn(ALOAD, THIS);
			
			emitStringValue(anySerialize(overloadedStore));		
			
			mv.visitMethodInsn(INVOKESTATIC, fullClassName, "anyDeserialize", "(Ljava/lang/String;)Ljava/lang/Object;",false);
			mv.visitTypeInsn(CHECKCAST, "[Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/OverloadedFunction;");
			mv.visitFieldInsn(PUTFIELD, fullClassName, "overloadedStore", "[Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/OverloadedFunction;");
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (String fname : storeCreators) {
			mv.visitVarInsn(ALOAD, THIS);
			mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, "()V",false);
		}

		mv.visitInsn(RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();
	}

	/*
	 * Generate the complete class
	 */
	public void emitClass(String pName, String cName) {
		if (!emit)
			return;

		this.className = cName;
		this.packageName = pName;
		this.fullClassName = packageName.replace('.', '/') + "/" + className;
		//this.fullClassName = "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RVMRunner";
		cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

		cw.visit(V1_8, ACC_PUBLIC + ACC_SUPER, fullClassName, null, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RVMRun", null);
	}

	/*
	 * Generate constant and/or type stores when present (per function)
	 */
	public void emitStoreInitializer(Function f) {

		// Create the statics to contain a functions constantStore and typeConstantStore (Why static?)
		if (f.constantStore.length > 0) {
			fv = cw.visitField(ACC_PUBLIC + ACC_STATIC, "cs_" + NameMangler.mangle(f.getName()), "[Ljava/lang/Object;", null, null);
			fv.visitEnd();
		}

		if (f.typeConstantStore.length > 0) {
			fv = cw.visitField(ACC_PUBLIC + ACC_STATIC, "tcs_" + NameMangler.mangle(f.getName()), "[Ljava/lang/Object;", null, null);
			fv.visitEnd();
		}

		if (f.constantStore.length == 0 && f.typeConstantStore.length == 0)
			return;

		// TODO: create initialiser function to fill them. ( TODO: call them through constructor )
		mv = cw.visitMethod(ACC_PUBLIC, "init_" + NameMangler.mangle(f.getName()), "()V", null, null);
		mv.visitCode();

		mv.visitTypeInsn(NEW, "org/rascalmpl/value/io/StandardTextReader");
		mv.visitInsn(DUP);
		mv.visitMethodInsn(INVOKESPECIAL, "org/rascalmpl/value/io/StandardTextReader", "<init>", "()V",false);
		mv.visitVarInsn(ASTORE, 1);

		if (f.constantStore.length > 0) {
			emitIntValue(f.constantStore.length);
			mv.visitTypeInsn(ANEWARRAY, "java/lang/Object");
			mv.visitFieldInsn(PUTSTATIC, fullClassName, "cs_" + NameMangler.mangle(f.getName()), "[Ljava/lang/Object;");
		}
		Label l0 = new Label();
		Label l1 = new Label();	
		Label l2 = new Label();
		Label l3 = new Label();

		mv.visitTryCatchBlock(l0, l1, l2, "java/lang/Exception");

		mv.visitLabel(l0);

		StringWriter w = null;
		for (int i = 0; i < f.constantStore.length; i++) {
			mv.visitFieldInsn(GETSTATIC, fullClassName, "cs_" + NameMangler.mangle(f.getName()), "[Ljava/lang/Object;");
			emitIntValue(i);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, fullClassName, "vf", "Lorg/rascalmpl/value/IValueFactory;");
			mv.visitTypeInsn(NEW, "java/io/StringReader");
			mv.visitInsn(DUP);

			w = new StringWriter();
			try {
				new StandardTextWriter().write(f.constantStore[i], w);
				// System.err.println(w.toString().length() + " = " + w.toString());
				emitStringValue(w.toString());
			} catch (Exception e) {
			}

			mv.visitMethodInsn(INVOKESPECIAL, "java/io/StringReader", "<init>", "(Ljava/lang/String;)V",false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "org/rascalmpl/value/io/StandardTextReader", "read",
					"(Lorg/rascalmpl/value/IValueFactory;Ljava/io/Reader;)Lorg/rascalmpl/value/IValue;",false);
			mv.visitInsn(AASTORE);
		}
		/* */mv.visitInsn(NOP); // so there is no empty try block
		mv.visitLabel(l1);

		mv.visitJumpInsn(GOTO, l3);

		mv.visitLabel(l2);
		mv.visitVarInsn(ASTORE, 2);
		mv.visitFieldInsn(GETSTATIC, "java/lang/System", "err", "Ljava/io/PrintStream;");
		mv.visitVarInsn(ALOAD, 2);
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Exception", "getMessage", "()Ljava/lang/String;",false);
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V",false);

		mv.visitLabel(l3);

		mv.visitInsn(RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		// Force function to be called in the constructor.
		storeCreators.add("init_" + NameMangler.mangle(f.getName()));
	}

	/*
	 * Generate a method for each Rascal function
	 */
	public void emitMethod(Function f, boolean debug) {

		if (!emit)
			return;

		labelMap.clear(); // New set of labels.
		catchTargetLabels.clear();
		catchTargets.clear();

		// Create method
		mv = cw.visitMethod(ACC_PUBLIC, NameMangler.mangle(f.getName()), "(Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;)Ljava/lang/Object;", null, null);
		mv.visitCode();

		emitExceptionTable(f.fromLabels, f.toLabels, f.fromSPsCorrected, f.types, f.handlerLabels);

		/*
		 * Fetch sp, stack from current frame and assign to local variable
		 */
		mv.visitVarInsn(ALOAD, CF);
		mv.visitFieldInsn(GETFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame", "sp", "I");
		mv.visitVarInsn(ISTORE, SP);
		mv.visitVarInsn(ALOAD, CF);
		mv.visitFieldInsn(GETFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame", "stack", "[Ljava/lang/Object;");
		mv.visitVarInsn(ASTORE, STACK);

		/*
		 * Fetch constant store from current frame and (when present) assign to local variable
		 */
		if (f.constantStore.length != 0) {
			mv.visitVarInsn(ALOAD, CF);
			mv.visitFieldInsn(GETFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame", "function",
					"Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Function;");
			mv.visitFieldInsn(GETFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Function", "constantStore", "[Lorg/rascalmpl/value/IValue;");
			mv.visitVarInsn(ASTORE, CS);
		}
		/*
		 * Fetch type store from current frame and (when present) assign to local variable
		 */

		if (f.typeConstantStore.length != 0) {
			mv.visitVarInsn(ALOAD, CF);
			mv.visitFieldInsn(GETFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame", "function",
					"Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Function;");
			mv.visitFieldInsn(GETFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Function", "typeConstantStore", "[Lorg/rascalmpl/value/type/Type;");
			mv.visitVarInsn(ASTORE, TS);
		}

		/*
		 * Handle coroutine
		 */
		if (f.continuationPoints != 0) {
			hotEntryLabels = new Label[f.continuationPoints + 1]; // Add entry 0
			exitLabel = new Label();

			for (int i = 0; i < hotEntryLabels.length; i++)
				hotEntryLabels[i] = new Label();

			mv.visitCode();
			mv.visitVarInsn(ALOAD, CF);
			mv.visitFieldInsn(GETFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame", "hotEntryPoint", "I");
			mv.visitTableSwitchInsn(0, hotEntryLabels.length - 1, exitLabel, hotEntryLabels);

			mv.visitLabel(hotEntryLabels[0]); // Start at 'address' 0
		} else {
			exitLabel = null;
		}

		f.codeblock.genByteCode(this, debug);

		if (exitLabel != null) {
			mv.visitLabel(exitLabel);
			mv.visitVarInsn(ALOAD, THIS);
			mv.visitFieldInsn(GETFIELD, fullClassName, "PANIC", "Lorg/rascalmpl/value/IString;");
			mv.visitInsn(ARETURN);
		}
		mv.visitMaxs(0, 0);
		mv.visitEnd();
	}

//	/*
//	 * End of method generation for a single Rascal function
//	 */
//	public void closeMethod() {
//		if (!emit)
//			return;
//
//		// This label should never be reached, it is
//		// placed to
//		// keep JVM verifier happy. (Reason: code generated
//		// by the compiler jumps sometimes to a non existing
//		// location)
//		if (exitLabel != null) {
//			mv.visitLabel(exitLabel);
//			mv.visitVarInsn(ALOAD, THIS);
//			mv.visitFieldInsn(GETFIELD, fullClassName, "PANIC", "Lorg/rascalmpl/value/IString;");
//			mv.visitInsn(ARETURN);
//		}
//		mv.visitMaxs(0, 0);
//		mv.visitEnd();
//	}

	public void emitJMP(String targetLabel) {
		if (!emit)
			return;
		Label lb = getNamedLabel(targetLabel);
		mv.visitJumpInsn(GOTO, lb);
	}

	public void emitJMPTRUEorFALSE(boolean tf, String targetLabel, boolean debug) {
		if (!emit)
			return;
		Label target = getNamedLabel(targetLabel);

		mv.visitIincInsn(SP, -1);
		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);
		mv.visitInsn(AALOAD);
		mv.visitMethodInsn(INVOKEINTERFACE, "org/rascalmpl/value/IBool", "getValue", "()Z",true);
		if (tf)
			mv.visitJumpInsn(IFNE, target);
		else
			mv.visitJumpInsn(IFEQ, target);
	}

	public void emitLabel(String targetLabel) {
		if (!emit)
			return;
		Label lb = getNamedLabel(targetLabel);
		mv.visitLabel(lb);

		ExceptionLine el = catchTargets.get(targetLabel);
		if (el != null) {
			emitCatchLabelEpilogue(el.type, el.newsp);
		}
	}

	public byte[] finalizeCode() {
		if (!emit)
			return null;
		if (endCode == null) {
			cw.visitEnd();
			endCode = cw.toByteArray();
		}
		return endCode;
	}

	public void emitInlineExhaust(boolean dcode) {
		if (!emit)
			return;
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);
		mv.visitVarInsn(ALOAD, CF);
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, "exhaustHelper", "([Ljava/lang/Object;ILorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;)Ljava/lang/Object;",false);
		mv.visitInsn(ARETURN);
	}

	public void emitInlineReturn(int wReturn, boolean debug) {
		if (!emit)
			return;

		Label normalReturn = new Label();
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);
		mv.visitVarInsn(ALOAD, CF);

		if (wReturn == 0) {
			mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, "return0Helper",
					"([Ljava/lang/Object;ILorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;)Ljava/lang/Object;",false);
		} else {
			emitIntValue(wReturn);
			mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, "return1Helper",
					"([Ljava/lang/Object;ILorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;I)Ljava/lang/Object;",false);
		}

		mv.visitVarInsn(ASTORE, LVAL);

		mv.visitVarInsn(ALOAD, CF);
		mv.visitFieldInsn(GETFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame", "previousCallFrame",
				"Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;");
		mv.visitJumpInsn(IFNONNULL, normalReturn);
		mv.visitVarInsn(ALOAD, LVAL);
		mv.visitInsn(ARETURN);

		mv.visitLabel(normalReturn);
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitFieldInsn(GETFIELD, fullClassName, "NONE", "Lorg/rascalmpl/value/IString;");
		mv.visitInsn(ARETURN);
	}

	public void emitInlineFailreturn() {
		if (!emit)
			return;

		mv.visitVarInsn(ALOAD, THIS);
		mv.visitFieldInsn(GETFIELD, fullClassName, "FAILRETURN", "Lorg/rascalmpl/value/IString;");
		mv.visitInsn(ARETURN);
	}

	public void emitDynDispatch(int numberOfFunctions) {
		if (!emit)
			return;
		funcArray = new String[numberOfFunctions];
	}

	public void emitDynCaLL(String fname, Integer value) {
		if (!emit)
			return;
		funcArray[value] = fname;
	}

	public void emitDynFinalize() {
		if (!emit)
			return;
		int nrFuncs = funcArray.length;
		Label[] caseLabels = new Label[nrFuncs];

		for (int i = 0; i < nrFuncs; i++) {
			caseLabels[i] = new Label();
		}
		Label defaultlabel = new Label();

		mv = cw.visitMethod(ACC_PUBLIC, "dynRun", "(ILorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;)Ljava/lang/Object;", null, null);
		mv.visitCode();

		// Case switch on int at loc 1 (java stack)
		mv.visitVarInsn(ILOAD, 1);
		mv.visitTableSwitchInsn(0, nrFuncs - 1, defaultlabel, caseLabels);
		for (int i = 0; i < nrFuncs; i++) {
			mv.visitLabel(caseLabels[i]);
			mv.visitVarInsn(ALOAD, THIS);
			mv.visitVarInsn(ALOAD, 2); // BEWARE: CF in second argument differs from generated functions.
			mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, NameMangler.mangle(funcArray[i]), "(Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;)Ljava/lang/Object;",false);
			mv.visitInsn(ARETURN);
		}
		mv.visitLabel(defaultlabel);

		// Function exit
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitFieldInsn(GETFIELD, fullClassName, "vf", "Lorg/rascalmpl/value/IValueFactory;");
		mv.visitInsn(ICONST_0);
		mv.visitMethodInsn(INVOKEINTERFACE, "org/rascalmpl/value/IValueFactory", "bool", "(Z)Lorg/rascalmpl/value/IBool;",true);
		mv.visitInsn(ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();
	}

	public void emitInlineGuard(int hotEntryPoint, boolean dcode) {
		if (!emit)
			return;

		mv.visitVarInsn(ALOAD, 1);
		emitIntValue(hotEntryPoint);

		mv.visitFieldInsn(PUTFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame", "hotEntryPoint", "I");
		mv.visitInsn(ACONST_NULL);
		mv.visitVarInsn(ASTORE, 6);
		mv.visitVarInsn(ALOAD, 1);
		mv.visitFieldInsn(GETFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame", "previousCallFrame",
				"Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;");
		mv.visitVarInsn(ASTORE, 7);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitVarInsn(ALOAD, 3);
		mv.visitVarInsn(ILOAD, 2);
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, "guardHelper", "([Ljava/lang/Object;I)Z",false);
		mv.visitVarInsn(ISTORE, 4);
		mv.visitVarInsn(ALOAD, 1);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, fullClassName, "cccf", "Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;");
		Label l0 = new Label();
		mv.visitJumpInsn(IF_ACMPNE, l0);
		mv.visitVarInsn(ILOAD, 4);
		Label l1 = new Label();
		mv.visitJumpInsn(IFEQ, l1);
		mv.visitTypeInsn(NEW, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Coroutine");
		mv.visitInsn(DUP);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, fullClassName, "cccf", "Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;");
		mv.visitMethodInsn(INVOKESPECIAL, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Coroutine", "<init>",
				"(Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;)V",false);
		mv.visitVarInsn(ASTORE, 6);
		mv.visitVarInsn(ALOAD, 6);
		mv.visitInsn(ICONST_1);
		mv.visitFieldInsn(PUTFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Coroutine", "isInitialized", "Z");
		mv.visitVarInsn(ALOAD, 6);
		mv.visitVarInsn(ALOAD, 1);
		mv.visitFieldInsn(PUTFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Coroutine", "entryFrame",
				"Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;");
		mv.visitVarInsn(ALOAD, 6);
		mv.visitVarInsn(ALOAD, 1);
		mv.visitMethodInsn(INVOKEVIRTUAL, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Coroutine", "suspend",
				"(Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;)V",false);
		mv.visitLabel(l1);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitInsn(ACONST_NULL);
		mv.visitFieldInsn(PUTFIELD, fullClassName, "cccf", "Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;");
		mv.visitVarInsn(ALOAD, 1);
		mv.visitIincInsn(2, -1);
		mv.visitVarInsn(ILOAD, 2);
		mv.visitFieldInsn(PUTFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame", "sp", "I");
		mv.visitVarInsn(ALOAD, 7);
		mv.visitFieldInsn(GETFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame", "stack", "[Ljava/lang/Object;");
		mv.visitVarInsn(ALOAD, 7);
		mv.visitInsn(DUP);
		mv.visitFieldInsn(GETFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame", "sp", "I");
		mv.visitInsn(DUP_X1);
		mv.visitInsn(ICONST_1);
		mv.visitInsn(IADD);
		mv.visitFieldInsn(PUTFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame", "sp", "I");
		mv.visitVarInsn(ILOAD, 4);
		Label l2 = new Label();
		mv.visitJumpInsn(IFEQ, l2);
		mv.visitVarInsn(ALOAD, 6);
		Label l3 = new Label();
		mv.visitJumpInsn(GOTO, l3);
		mv.visitLabel(l2);
		mv.visitFieldInsn(GETSTATIC, fullClassName, "exhausted", "Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Coroutine;");
		mv.visitLabel(l3);
		mv.visitInsn(AASTORE);

		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, fullClassName, "NONE", "Lorg/rascalmpl/value/IString;");
		mv.visitInsn(ARETURN);
		mv.visitLabel(l0);
		mv.visitVarInsn(ILOAD, 4);
		Label l4 = new Label();
		mv.visitJumpInsn(IFNE, l4);
		mv.visitVarInsn(ALOAD, 7);
		mv.visitFieldInsn(GETFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame", "stack", "[Ljava/lang/Object;");
		mv.visitVarInsn(ALOAD, 7);
		mv.visitInsn(DUP);
		mv.visitFieldInsn(GETFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame", "sp", "I");
		mv.visitInsn(DUP_X1);
		mv.visitInsn(ICONST_1);
		mv.visitInsn(IADD);
		mv.visitFieldInsn(PUTFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame", "sp", "I");
		mv.visitFieldInsn(GETSTATIC, fullClassName, "Rascal_FALSE", "Lorg/rascalmpl/value/IBool;");
		mv.visitInsn(AASTORE);
		mv.visitVarInsn(ALOAD, 1);
		mv.visitVarInsn(ILOAD, 2);
		mv.visitFieldInsn(PUTFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame", "sp", "I");

		mv.visitVarInsn(ALOAD, THIS);
		mv.visitFieldInsn(GETFIELD, fullClassName, "NONE", "Lorg/rascalmpl/value/IString;");
		mv.visitInsn(ARETURN);
		mv.visitLabel(l4);
		mv.visitIincInsn(SP, -1);

		mv.visitLabel(hotEntryLabels[hotEntryPoint]);
	}

	public void emitInlineLoadLocN(int n, boolean debug) {
		if (!emit)
			return;

		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);
		mv.visitIincInsn(SP, 1);
		mv.visitVarInsn(ALOAD, STACK);
		emitIntValue(n);
		mv.visitInsn(AALOAD);
		mv.visitInsn(AASTORE);
	}

	// Experimemtal local copy of constantStore and typeConstantStore probably needed in final version.
	public void emitInlineLoadConOrType(int n, boolean conOrType, boolean debug) {
		if (!emit)
			return;
		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);
		mv.visitIincInsn(SP, 1);

		if (conOrType)
			mv.visitVarInsn(ALOAD, CS);
		else
			mv.visitVarInsn(ALOAD, TS);

		emitIntValue(n);
		mv.visitInsn(AALOAD);
		mv.visitInsn(AASTORE);
	}
	
	public void emitInlinePushConOrType(int n, boolean conOrType, boolean debug) { 
		// TODO
	}

	public void emitInlinePop(boolean debug) {
		if (!emit)
			return;
		mv.visitIincInsn(SP, -1);
	}

	public void emitInlineStoreLoc(int loc, boolean debug) {
		if (!emit)
			return;

		mv.visitVarInsn(ALOAD, STACK);
		emitIntValue(loc);
		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);
		mv.visitInsn(ICONST_1);
		mv.visitInsn(ISUB);
		mv.visitInsn(AALOAD);
		mv.visitInsn(AASTORE);
	}

	public void emitInlineTypeSwitch(IList labels, boolean dcode) {
		if (!emit)
			return;

		Label[] switchTable;

		int nrLabels = labels.length();
		switchTable = new Label[nrLabels];
		int lcount = 0;
		for (IValue vlabel : labels) {
			String label = ((IString) vlabel).getValue();
			switchTable[lcount++] = getNamedLabel(label);
		}

		if (exitLabel == null)
			exitLabel = new Label();

		mv.visitIincInsn(SP, -1);
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, "typeSwitchHelper", "([Ljava/lang/Object;I)I",false);

		mv.visitTableSwitchInsn(0, nrLabels - 1, exitLabel, switchTable);
	}

	public void emitHotEntryJumpTable(int continuationPoints, boolean debug) {
		if (!emit)
			return;

		hotEntryLabels = new Label[continuationPoints + 1]; // Add default 0
															// entry point.
	}

	public void emitInlineJmpIndexed(IList labels, boolean dcode) {
		if (!emit)
			return;

		Label[] switchTable;

		int nrLabels = labels.length();
		switchTable = new Label[nrLabels];

		int lcount = 0;
		for (IValue vlabel : labels) {
			String label = ((IString) vlabel).getValue();
			switchTable[lcount++] = getNamedLabel(label);
		}

		if (exitLabel == null)
			exitLabel = new Label();

		mv.visitVarInsn(ALOAD, STACK);

		mv.visitVarInsn(ALOAD, THIS);
		mv.visitInsn(DUP);
		mv.visitFieldInsn(GETFIELD, fullClassName, "sp", "I");
		mv.visitInsn(ICONST_1);
		mv.visitInsn(ISUB);
		mv.visitInsn(DUP_X1);
		mv.visitFieldInsn(PUTFIELD, fullClassName, "sp", "I");
		mv.visitInsn(AALOAD);
		mv.visitTypeInsn(CHECKCAST, "org/rascalmpl/value/IInteger");
		mv.visitMethodInsn(INVOKEINTERFACE, "org/rascalmpl/value/IInteger", "intValue", "()I",true);

		mv.visitTableSwitchInsn(0, nrLabels - 1, exitLabel, switchTable);
	}

	public void emitInlineYield(int arity, int hotEntryPoint, boolean debug) {
		if (!emit)
			return;

		Label continueAt = new Label();

		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, CF);
		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);

		if (arity > 0) {
			emitIntValue(arity);
			emitIntValue(hotEntryPoint);
			mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, "yield1Helper", "(Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;[Ljava/lang/Object;III)V",false);
		} else {
			emitIntValue(hotEntryPoint);
			mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, "yield0Helper", "(Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;[Ljava/lang/Object;II)V",false);
		}

		mv.visitVarInsn(ALOAD, CF);
		mv.visitFieldInsn(GETFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame", "previousCallFrame",
				"Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;");
		mv.visitJumpInsn(IFNONNULL, continueAt);
		mv.visitFieldInsn(GETSTATIC, fullClassName, "Rascal_TRUE", "Lorg/rascalmpl/value/IBool;");
		mv.visitInsn(ARETURN);

		mv.visitLabel(continueAt);
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitFieldInsn(GETFIELD, fullClassName, "YIELD", "Lorg/rascalmpl/value/IString;");
		mv.visitInsn(ARETURN);

		mv.visitLabel(hotEntryLabels[hotEntryPoint]);
	}

	public void emitPanicReturn() {
		if (!emit)
			return;
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitFieldInsn(GETFIELD, fullClassName, "PANIC", "Lorg/rascalmpl/value/IString;");
		mv.visitInsn(ARETURN);
	}

	public void emitEntryLabel(int continuationPoint) {
		mv.visitLabel(hotEntryLabels[continuationPoint]);
	}

	public void emitInlineCall(int functionIndex, int arity, int continuationPoint, boolean debug) {
		if (!emit)
			return;

		Label l0 = new Label();

		emitEntryLabel(continuationPoint);

		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);
		mv.visitVarInsn(ALOAD, CF);

		emitIntValue(functionIndex);
		emitIntValue(arity);
		emitIntValue(continuationPoint);

		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, "callHelper", "([Ljava/lang/Object;ILorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;III)Ljava/lang/Object;",false);
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitFieldInsn(GETFIELD, fullClassName, "YIELD", "Lorg/rascalmpl/value/IString;");
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Object", "equals", "(Ljava/lang/Object;)Z",false);
		mv.visitJumpInsn(IFEQ, l0);
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitFieldInsn(GETFIELD, fullClassName, "YIELD", "Lorg/rascalmpl/value/IString;");
		mv.visitInsn(ARETURN);

		mv.visitLabel(l0);
		mv.visitVarInsn(ALOAD, CF);
		mv.visitFieldInsn(GETFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame", "sp", "I");
		mv.visitVarInsn(ISTORE, SP);
	}

	public void emitInlineCalldyn(int arity, int continuationPoint, boolean debug) {
		if (!emit)
			return;

		Label l0 = new Label();

		emitEntryLabel(continuationPoint);

		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);
		mv.visitVarInsn(ALOAD, CF);

		emitIntValue(arity);
		emitIntValue(continuationPoint);

		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, "calldynHelper",
				"([Ljava/lang/Object;ILorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;II)Ljava/lang/Object;",false);

		mv.visitVarInsn(ALOAD, THIS);
		mv.visitFieldInsn(GETFIELD, fullClassName, "YIELD", "Lorg/rascalmpl/value/IString;");
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Object", "equals", "(Ljava/lang/Object;)Z",false);
		mv.visitJumpInsn(IFEQ, l0);
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitFieldInsn(GETFIELD, fullClassName, "YIELD", "Lorg/rascalmpl/value/IString;");
		mv.visitInsn(ARETURN);

		mv.visitLabel(l0);

		mv.visitVarInsn(ALOAD, CF);
		mv.visitFieldInsn(GETFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame", "sp", "I");
		mv.visitVarInsn(ISTORE, SP);
	}
	
	/**
	 * Emits a inline version of the CallMUPrim0 instruction. Uses a direct call to the static enum execute method.
	 * 
	 */
	public void emitInlineCallMuPrim0(MuPrimitive muprim, boolean debug) {
		if (!emit)
			return;

		mv.visitVarInsn(ALOAD, STACK);		// stack
		mv.visitVarInsn(ILOAD, SP);			// sp
		
		mv.visitFieldInsn(GETSTATIC, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/MuPrimitive", muprim.name(),
				"Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/MuPrimitive;");
	
		mv.visitMethodInsn(INVOKEVIRTUAL, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/MuPrimitive", "execute0", "()Ljava/lang/Object;",false);
		mv.visitInsn(AASTORE);				// stack[sp] = callMuPrim0()
		
		mv.visitIincInsn(SP, 1);			// sp += 1
	}
	
	/**
	 * Emits a inline version of the CallMUPrim1 instruction. Uses a direct call to the static enum execute method.
	 * 
	 */
	public void emitInlineCallMuPrim1(MuPrimitive muprim, boolean debug) {
		if (!emit)
			return;

		mv.visitIincInsn(SP, -1);			// sp -= 1
		mv.visitVarInsn(ALOAD, STACK);		// stack
		mv.visitVarInsn(ILOAD, SP);			// sp
		
		mv.visitFieldInsn(GETSTATIC, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/MuPrimitive", muprim.name(),
				"Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/MuPrimitive;");
		
		mv.visitVarInsn(ALOAD, STACK);		// arg_1
		mv.visitVarInsn(ILOAD, SP);
		mv.visitInsn(AALOAD);
		
		mv.visitMethodInsn(INVOKEVIRTUAL, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/MuPrimitive", "execute1", "(Ljava/lang/Object;)Ljava/lang/Object;",false);
		
		mv.visitInsn(AASTORE);				// stack[sp] = callMuPrim1(arg_1)
		mv.visitIincInsn(SP, 1);			// sp += 1
	}
	
	/**
	 * Emits a inline version of the CallMUPrim1 instruction. Uses a direct call to the static enum execute method.
	 * 
	 */
	public void emitInlineCallMuPrim2(MuPrimitive muprim, boolean debug) {
		if (!emit)
			return;

		mv.visitIincInsn(SP, -2);			// sp -= 2
		mv.visitVarInsn(ALOAD, STACK);		// stack
		mv.visitVarInsn(ILOAD, SP);			// sp
		
		mv.visitFieldInsn(GETSTATIC, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/MuPrimitive", muprim.name(),
				"Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/MuPrimitive;");
		
		mv.visitVarInsn(ALOAD, STACK);		// arg_2
		mv.visitVarInsn(ILOAD, SP);
		mv.visitInsn(AALOAD);
		
		mv.visitIincInsn(SP, 1);
		
		mv.visitVarInsn(ALOAD, STACK);		// arg_1
		mv.visitVarInsn(ILOAD, SP);
		mv.visitInsn(AALOAD);
		
		mv.visitMethodInsn(INVOKEVIRTUAL, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/MuPrimitive", "execute2", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",false);
		
		mv.visitInsn(AASTORE);				// stack[sp] = callMuPrim2(arg_2, arg_1)
	}

	/**
	 * Emits a inline version of the CallMUPrim instructions. Uses a direct call to the static enum execute method.
	 * 
	 */
	public void emitInlineCallMuPrimN(MuPrimitive muprim, int arity, boolean debug) {
		if (!emit)
			return;

		mv.visitFieldInsn(GETSTATIC, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/MuPrimitive", muprim.name(),
				"Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/MuPrimitive;");

		mv.visitVarInsn(ALOAD, STACK);		// stack
		mv.visitVarInsn(ILOAD, SP);			// sp

		emitIntValue(arity);				// arity

		mv.visitMethodInsn(INVOKEVIRTUAL, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/MuPrimitive", "executeN", "([Ljava/lang/Object;II)I",false);
		mv.visitVarInsn(ISTORE, SP);		// sp = callMuPrimN(stach, sp, arity)
	}
	
	/**
	 * Emits a inline version of the CallPrim0 instruction. Uses a direct call to the static enum execute method.
	 * 
	 */
	public void emitInlineCallPrim0(RascalPrimitive prim, boolean debug) {
		if (!emit)
			return;
		
		mv.visitVarInsn(ALOAD, STACK);		// stack
		mv.visitVarInsn(ILOAD, SP);			// sp
		
		mv.visitFieldInsn(GETSTATIC, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RascalPrimitive", prim.name(),
				"Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RascalPrimitive;");
		
		mv.visitVarInsn(ALOAD, CF);			// currentFrame
		
		mv.visitVarInsn(ALOAD, 0);			// rex
		mv.visitFieldInsn(GETFIELD, fullClassName, "rex", "Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RascalExecutionContext;");

		mv.visitMethodInsn(INVOKEVIRTUAL, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RascalPrimitive", "execute0",
				"(Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RascalExecutionContext;)Ljava/lang/Object;",false);
		
		mv.visitInsn(AASTORE);				// stack[sp] callPrim0()
		mv.visitIincInsn(SP, 1);			// sp += 1
	}
	
	/**
	 * Emits a inline version of the CallPrim1 instruction. Uses a direct call to the static enum execute method.
	 * 
	 */
	public void emitInlineCallPrim1(RascalPrimitive prim, boolean debug) {
		if (!emit)
			return;
		
		mv.visitIincInsn(SP, -1);
		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);
		
		mv.visitFieldInsn(GETSTATIC, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RascalPrimitive", prim.name(),
				"Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RascalPrimitive;");
		
		mv.visitVarInsn(ALOAD, STACK);	// arg_1
		mv.visitVarInsn(ILOAD, SP);
		mv.visitInsn(AALOAD);
		
		mv.visitVarInsn(ALOAD, CF);		// currentFrame

		mv.visitVarInsn(ALOAD, 0);		// rex
		mv.visitFieldInsn(GETFIELD, fullClassName, "rex", "Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RascalExecutionContext;");

		mv.visitMethodInsn(INVOKEVIRTUAL, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RascalPrimitive", "execute1",
				"(Ljava/lang/Object;Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RascalExecutionContext;)Ljava/lang/Object;",false);
		
		mv.visitInsn(AASTORE);
		mv.visitIincInsn(SP, 1);
	}
	
	/**
	 * Emits a inline version of the CallPrim1 instruction. Uses a direct call to the static enum execute method.
	 * 
	 */
	public void emitInlineCallPrim2(RascalPrimitive prim, boolean debug) {
		if (!emit)
			return;
		
		mv.visitIincInsn(SP, -2);
		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);
		
		mv.visitFieldInsn(GETSTATIC, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RascalPrimitive", prim.name(),
				"Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RascalPrimitive;");
		
		mv.visitVarInsn(ALOAD, STACK);		// arg_2
		mv.visitVarInsn(ILOAD, SP);
		mv.visitInsn(AALOAD);
		
		mv.visitIincInsn(SP, 1);
		
		mv.visitVarInsn(ALOAD, STACK);		// arg_1
		mv.visitVarInsn(ILOAD, SP);
		mv.visitInsn(AALOAD);
		
		mv.visitVarInsn(ALOAD, CF);			// currentFrame

		mv.visitVarInsn(ALOAD, 0);			// rex
		mv.visitFieldInsn(GETFIELD, fullClassName, "rex", "Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RascalExecutionContext;");
		
		mv.visitMethodInsn(INVOKEVIRTUAL, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RascalPrimitive", "execute2",
				"(Ljava/lang/Object;Ljava/lang/Object;Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RascalExecutionContext;)Ljava/lang/Object;",false);
		
		mv.visitInsn(AASTORE);
	}
	
	/**
	 * Emits a inline version of the CallPrimN instructions. Uses a direct call to the static enum execute method.
	 * 
	 */
	public void emitInlineCallPrimN(RascalPrimitive prim, int arity, boolean debug) {
		if (!emit)
			return;

		mv.visitFieldInsn(GETSTATIC, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RascalPrimitive", prim.name(),
				"Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RascalPrimitive;");

		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);

		emitIntValue(arity);

		mv.visitVarInsn(ALOAD, CF);
		
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, fullClassName, "rex", "Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RascalExecutionContext;");
		
		mv.visitMethodInsn(INVOKEVIRTUAL, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RascalPrimitive", "executeN",
				"([Ljava/lang/Object;IILorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RascalExecutionContext;)I",false);
		mv.visitVarInsn(ISTORE, SP);
	}

	public void emitInlineLoadBool(boolean b, boolean debug) {
		if (!emit)
			return;

		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);
		mv.visitIincInsn(SP, 1);
		if (b) {
			mv.visitFieldInsn(GETSTATIC, fullClassName, "Rascal_TRUE", "Lorg/rascalmpl/value/IBool;");
		} else {
			mv.visitFieldInsn(GETSTATIC, fullClassName, "Rascal_FALSE", "Lorg/rascalmpl/value/IBool;");
		}
		mv.visitInsn(AASTORE);
	}

	public void emitCallWithArgsSS(String fname) {
		if (!emit)
			return;
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);

		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, "([Ljava/lang/Object;I)I",false);
		mv.visitVarInsn(ISTORE, SP);
	}

	public void emitCallWithArgsSSI(String fname, int i, boolean dbg) {
		if (!emit)
			return;
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);
		emitIntValue(i);
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, "([Ljava/lang/Object;II)I",false);
		mv.visitVarInsn(ISTORE, SP);
	}

	public void emitCallWithArgsSSII(String fname, int i, int j, boolean dbg) {
		if (!emit)
			return;
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);
		emitIntValue(i);
		emitIntValue(j);
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, "([Ljava/lang/Object;III)I",false);
		mv.visitVarInsn(ISTORE, SP);
	}

	public void emitCallWithArgsSSIII(String fname, int pos1, int type, int pos2) {
		if (!emit)
			return;
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, 3);
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitFieldInsn(GETFIELD, fullClassName, "sp", "I");
		emitIntValue(pos1);
		emitIntValue(type);
		emitIntValue(pos2);
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, "([Ljava/lang/Object;IIII)I",false);
		mv.visitVarInsn(ISTORE, SP);
	}

	public void emitCallWithArgsSSFI(String fname, int i, boolean dbg) {
		if (!emit)
			return;
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, STACK); // Stack
		mv.visitVarInsn(ILOAD, SP); // Stack
		mv.visitVarInsn(ALOAD, CF); // CF

		emitIntValue(i); // I

		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, "([Ljava/lang/Object;ILorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;I)I",false);
		mv.visitVarInsn(ISTORE, SP);
	}

	public void emitCallWithArgsSSFIII(String fname, int i, int j, int k, boolean dbg) {
		if (!emit)
			return;
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, STACK); // Stack
		mv.visitVarInsn(ILOAD, SP); // Stack
		mv.visitVarInsn(ALOAD, CF); // CF

		emitIntValue(i); // I
		emitIntValue(j); // I
		emitIntValue(k); // I

		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, "([Ljava/lang/Object;ILorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;III)I",false);
		mv.visitVarInsn(ISTORE, SP);
	}

	public void emitVoidCallWithArgsSSI(String fname, int i, boolean dbg) {
		if (!emit)
			return;
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, 3);
		mv.visitVarInsn(ILOAD, SP);
		emitIntValue(i);
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, "([Ljava/lang/Object;II)V",false);
	}

	public void emitCallWithArgsSSFII(String fname, int i, int j, boolean dcode) {
		if (!emit)
			return;
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, STACK); // Stack
		mv.visitVarInsn(ILOAD, SP); // SP
		mv.visitVarInsn(ALOAD, 1); // CF

		emitIntValue(i); // I
		emitIntValue(j); // I

		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, "([Ljava/lang/Object;ILorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;II)I",false);
		mv.visitVarInsn(ISTORE, SP);
	}

	public void emitCallWithArgsSSFIIZ(String fname, int what, int pos, boolean b, boolean dcode) {
		if (!emit)
			return;
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, STACK); // Stack
		mv.visitVarInsn(ILOAD, SP); // Stack
		mv.visitVarInsn(ALOAD, CF); // CF

		emitIntValue(what); // I
		emitIntValue(pos); // I

		if (b)
			mv.visitInsn(ICONST_1);
		else
			mv.visitInsn(ICONST_0);

		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, "([Ljava/lang/Object;ILorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;IIZ)I",false);
		mv.visitVarInsn(ISTORE, SP);
	}

	public void emitVoidCallWithArgsSSFIIZ(String fname, int what, int pos, boolean b, boolean dcode) {
		if (!emit)
			return;
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);

		mv.visitVarInsn(ALOAD, CF);

		emitIntValue(what); // I
		emitIntValue(pos); // I

		if (b)
			mv.visitInsn(ICONST_1);
		else
			mv.visitInsn(ICONST_0);

		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, "([Ljava/lang/Object;ILorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;IIZ)V",false);
	}

	public void emitVoidCallWithArgsSSFII(String fname, int what, int pos, boolean dcode) {
		if (!emit)
			return;
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, STACK); 
		mv.visitVarInsn(ILOAD, SP); 
		mv.visitVarInsn(ALOAD, CF); 

		emitIntValue(what); // I
		emitIntValue(pos); // I

		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, "([Ljava/lang/Object;ILorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;II)V",false);
	}

	public void emitCallWithArgsSSFIIII(String fname, int methodName, int className2, int parameterTypes, int reflect, boolean dcode) {
		if (!emit)
			return;
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, STACK); // Stack
		mv.visitVarInsn(ILOAD, SP);

		mv.visitVarInsn(ALOAD, 1); // CF

		emitIntValue(methodName); // I
		emitIntValue(className2); // I
		emitIntValue(parameterTypes); // I
		emitIntValue(reflect); // I

		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, "([Ljava/lang/Object;ILorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;IIII)I",false);
		mv.visitVarInsn(ISTORE, SP);
	}

	public void emitCallWithArgsSSFIIIII(String fname, int methodName, int className, int parameterTypes, int keywordTypes, int reflect, boolean dcode) {
		if (!emit)
			return;
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, STACK); // Stack
		mv.visitVarInsn(ILOAD, SP);
		mv.visitVarInsn(ALOAD, CF); // CF

		emitIntValue(methodName); // I
		emitIntValue(className); // I
		emitIntValue(parameterTypes); // I
		emitIntValue(keywordTypes); // I
		emitIntValue(reflect); // I

		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, "([Ljava/lang/Object;ILorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;IIIII)I",false);
		mv.visitVarInsn(ISTORE, SP);
	}

	public void emitVoidCallWithArgsSS(String fname, boolean dcode) {
		if (!emit)
			return;
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, STACK); // Stack
		mv.visitVarInsn(ILOAD, SP);

		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, "([Ljava/lang/Object;I)V",false);
	}

	public void emitVoidCallWithArgsSSFI(String fname, int pos, boolean dcode) {
		if (!emit)
			return;
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, STACK); // Stack
		mv.visitVarInsn(ILOAD, SP); // SP
		mv.visitVarInsn(ALOAD, CF); // CF
		emitIntValue(pos); // I
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, "([Ljava/lang/Object;ILorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;I)V",false);
	}

	public void emitCallWithArgsSSF(String fname, boolean dcode) {
		if (!emit)
			return;
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, STACK); // Stack
		mv.visitVarInsn(ILOAD, SP); // Stack

		mv.visitVarInsn(ALOAD, CF); // CF

		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, "([Ljava/lang/Object;ILorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;)I",false);
		mv.visitVarInsn(ISTORE, SP);
	}

	/*
	 * emitOptimizedOcall emits a call to a full ocall implementation or: 
	 * 1: There is only one function emit direct call  (DONE) 
	 * 2: There is only a constructor call constructor (TODO)
	 * *: Rest call full ocall implementation
	 */
	public void emitOptimizedOcall(String fuid, int overloadedFunctionIndex, int arity, boolean dcode) {
		if (!emit)
			return;
		OverloadedFunction of = overloadedStore.get(overloadedFunctionIndex);
		int[] functions = of.getFunctions();
		if (functions.length == 1) {
			int[] ctors = of.getConstructors();
			if (ctors.length == 0) {
				Function fu = functionStore.get(functions[0]);
				if (of.getScopeFun().equals("")) {
					emitOcallSingle(NameMangler.mangle(fu.getName()), functions[0], arity);
				} else {
					// Nested function needs link to containing frame
					emitCallWithArgsSSFII("jvmOCALL", overloadedFunctionIndex, arity, dcode);
				}
			} else {
				// Has a constructor.
				emitCallWithArgsSSFII("jvmOCALL", overloadedFunctionIndex, arity, dcode);
			}
		} else {
			if ( functions.length == 0 )  System.err.println("Optimize OCALL for call to single constructor!!");
			emitCallWithArgsSSFII("jvmOCALL", overloadedFunctionIndex, arity, dcode);
		}
	}

	private void emitOcallSingle(String funName, int fun, int arity) {

		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, CF);
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitFieldInsn(GETFIELD, fullClassName, "functionStore", "Ljava/util/ArrayList;");

		emitIntValue(fun);

		mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/ArrayList", "get", "(I)Ljava/lang/Object;",false);

		mv.visitTypeInsn(CHECKCAST, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Function");
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, fullClassName, "root", "Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;");

		emitIntValue(arity);
		mv.visitVarInsn(ILOAD, SP);

		mv.visitMethodInsn(
				INVOKEVIRTUAL,
				"org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame",
				"getFrame",
				"(Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Function;Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;II)Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;",false);
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, funName, "(Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;)Ljava/lang/Object;",false);
		mv.visitInsn(POP);

		mv.visitVarInsn(ALOAD, CF);
		mv.visitFieldInsn(GETFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame", "sp", "I");
		mv.visitVarInsn(ISTORE, SP);
	}

	public void dump(String loc) {
		if (!emit)
			return;
		if (endCode == null)
			finalizeCode();
		try {
			FileOutputStream fos = new FileOutputStream(loc);
			fos.write(endCode);
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void emitInlineSwitch(IMap caseLabels, String caseDefault, boolean concrete, boolean debug) {
		if (!emit)
			return;

		Map<Integer, String> jumpBlock = new TreeMap<Integer, String>(); // This map is sorted on its keys.

		int nrLabels = caseLabels.size();

		Label[] switchTable = new Label[nrLabels];
		int[] intTable = new int[nrLabels];

		for (IValue vlabel : caseLabels) {
			jumpBlock.put(((IInteger) vlabel).intValue(), ((IString) caseLabels.get(vlabel)).getValue());
		}

		nrLabels = 0;
		for (Map.Entry<Integer, String> entry : jumpBlock.entrySet()) {
			intTable[nrLabels] = entry.getKey();
			switchTable[nrLabels++] = getNamedLabel(entry.getValue());
		}

		Label trampolineLabel = getNamedLabel(caseDefault + "_trampoline");

		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, STACK);
		mv.visitIincInsn(SP, -1);
		mv.visitVarInsn(ILOAD, SP);

		if (concrete)
			mv.visitInsn(ICONST_1);
		else
			mv.visitInsn(ICONST_0);

		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, "switchHelper", "([Ljava/lang/Object;IZ)I",false);
		mv.visitLookupSwitchInsn(trampolineLabel, intTable, switchTable);

		emitLabel(caseDefault + "_trampoline");

		// In case of default push RASCAL_FALSE on stack. Ask Paul why?.. done
		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);
		mv.visitIincInsn(SP, 1);
		mv.visitFieldInsn(GETSTATIC, fullClassName, "Rascal_FALSE", "Lorg/rascalmpl/value/IBool;");
		mv.visitInsn(AASTORE);

		emitJMP(caseDefault);
	}

	public void emitGetSpfromFrame() {
		if (!emit)
			return;

		mv.visitVarInsn(ALOAD, CF);
		mv.visitFieldInsn(GETFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame", "sp", "I");
		mv.visitVarInsn(ISTORE, SP);
	}

	public void emitPutSpInFrame() {
		if (!emit)
			return;

		mv.visitVarInsn(ALOAD, CF);
		mv.visitVarInsn(ILOAD, SP);
		mv.visitFieldInsn(PUTFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame", "sp", "I");
	}

	public void emitDebugCall(String ins) {
		if (!emit)
			return;
		mv.visitLdcInsn(ins);
		mv.visitVarInsn(ALOAD, CF);
		mv.visitVarInsn(ILOAD, SP);
		mv.visitMethodInsn(INVOKESTATIC, fullClassName, "debug" + ins, "(Ljava/lang/String;Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;I)V",false);
	}

	public void emitInlineLoadInt(int nval, boolean debug) {
		if (!emit)
			return;

		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);
		mv.visitIincInsn(SP, 1);
		emitIntValue(nval);
		mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;",false);
		mv.visitInsn(AASTORE);
	}

	private void emitExceptionTable(String[] fromLabels, String[] toLabels, int[] fromSp, int[] types, String[] handlerLabels) {
		if (!emit)
			return;

		int len = handlerLabels.length;
		for (int i = 0; i < len; i++) {
			Label fromLabel = getNamedLabel(fromLabels[i]);
			Label toLabel = getNamedLabel(toLabels[i]);
			Label handlerLabel = getNamedLabel(handlerLabels[i]);

			catchTargetLabels.add(handlerLabels[i]);
			catchTargets.put(handlerLabels[i], new ExceptionLine(handlerLabels[i], fromSp[i], types[i]));

			// System.out.println("Exceptions :" + fromLabels[i] + " to " + toLabels[i] + " to " + handlerLabels[i] );

			mv.visitTryCatchBlock(fromLabel, toLabel, handlerLabel, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Thrown");
		}
	}

	public void emitCatchLabelEpilogue(int type, int newsp) {

		// --- Code implements.
		// catch(Thrown e) {
		// sp = newsp ;
		// stack[sp++] = e ;
		// if ( ! e.value.getType().isSubtypeOf(cf.function.typeConstantStore[type]) )
		// throw e ;
		// }
		// .....
		if (!emit)
			return;
		Label noReThrow = new Label();

		mv.visitVarInsn(ASTORE, EXCEPTION);

		emitIntValue(newsp);

		mv.visitVarInsn(ISTORE, SP);
		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);
		mv.visitIincInsn(SP, 1);
		mv.visitVarInsn(ALOAD, EXCEPTION);
		mv.visitInsn(AASTORE);
		mv.visitVarInsn(ALOAD, EXCEPTION);
		mv.visitFieldInsn(GETFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Thrown", "value", "Lorg/rascalmpl/value/IValue;");
		mv.visitMethodInsn(INVOKEINTERFACE, "org/rascalmpl/value/IValue", "getType", "()Lorg/rascalmpl/value/type/Type;",true);
		mv.visitVarInsn(ALOAD, CF);
		mv.visitFieldInsn(GETFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame", "function",
				"Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Function;");
		mv.visitFieldInsn(GETFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Function", "typeConstantStore", "[Lorg/rascalmpl/value/type/Type;");

		emitIntValue(type);

		mv.visitInsn(AALOAD);
		mv.visitMethodInsn(INVOKEVIRTUAL, "org/rascalmpl/value/type/Type", "isSubtypeOf", "(Lorg/rascalmpl/value/type/Type;)Z",false);
		mv.visitJumpInsn(IFNE, noReThrow);
		mv.visitVarInsn(ALOAD, EXCEPTION);
		mv.visitInsn(ATHROW);
		mv.visitLabel(noReThrow);

	}

	public void emitInlineThrow(boolean debug) {
		if (!emit)
			return;

		mv.visitVarInsn(ALOAD, CF);
		mv.visitIincInsn(SP, -1);
		mv.visitVarInsn(ILOAD, SP);
		mv.visitFieldInsn(PUTFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame", "sp", "I");
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, CF);
		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, "thrownHelper",
				"(Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;[Ljava/lang/Object;I)Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Thrown;",false);
		mv.visitInsn(ATHROW);

	}
	
	public void emitInlineResetLoc(int position, boolean debug) {
		if (!emit)
			return;
		
		mv.visitVarInsn(ALOAD, STACK);
		emitIntValue(position);
		mv.visitInsn(ACONST_NULL);
		mv.visitInsn(AASTORE);
	}

	public void emitInlineResetLocs(int positions, IValue constantValues, boolean debug) {
		if (!emit)
			return;

		IList il = (IList) constantValues;
		for (IValue v : il) {
			int stackPos = ((IInteger) v).intValue();
			mv.visitVarInsn(ALOAD, STACK);
			emitIntValue(stackPos);
		}
		mv.visitInsn(ACONST_NULL);

		for (int i = 1; i < il.length(); i++) {
			mv.visitInsn(DUP_X2);
			mv.visitInsn(AASTORE);
		}
		mv.visitInsn(AASTORE);
//		mv.visitVarInsn(ALOAD, STACK);
//		mv.visitVarInsn(ILOAD, SP);
//		mv.visitIincInsn(SP, 1);
//		mv.visitFieldInsn(GETSTATIC, fullClassName, "Rascal_TRUE", "Lorg/rascalmpl/value/IBool;");
//		mv.visitInsn(AASTORE);
	}

	private class ExceptionLine {
		@SuppressWarnings("unused")
		String catchLabel;
		int newsp;
		int type;

		public ExceptionLine(String lbl, int sp, int type) {
			this.catchLabel = lbl;
			this.newsp = sp;
			this.type = type;
		}
	}

	public void emitInlineCheckArgTypeAndCopy(int pos1, int type, int pos2, boolean debug) {
		if (!emit)
			return;
		Label l1 = new Label();
		Label l5 = new Label();

		mv.visitVarInsn(ALOAD, STACK);

		/* sourceLoc */
		emitIntValue(pos1);

		mv.visitInsn(AALOAD);
//		mv.visitTypeInsn(CHECKCAST, "org/rascalmpl/value/IValue");
		mv.visitMethodInsn(INVOKEINTERFACE, "org/rascalmpl/value/IValue", "getType", "()Lorg/rascalmpl/value/type/Type;",true);
		mv.visitVarInsn(ALOAD, TS);

		/* type */
		emitIntValue(type);

		mv.visitInsn(AALOAD);
		mv.visitMethodInsn(INVOKEVIRTUAL, "org/rascalmpl/value/type/Type", "isSubtypeOf", "(Lorg/rascalmpl/value/type/Type;)Z",false);
		mv.visitJumpInsn(IFEQ, l1);
		mv.visitVarInsn(ALOAD, STACK);

		/* toloc */
		emitIntValue(pos2);

		mv.visitVarInsn(ALOAD, STACK);

		/* sourceLoc */
		emitIntValue(pos1);

		mv.visitInsn(AALOAD);
		mv.visitInsn(AASTORE);
		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);
		mv.visitIincInsn(SP, 1);
		mv.visitFieldInsn(GETSTATIC, fullClassName, "Rascal_TRUE", "Lorg/rascalmpl/value/IBool;");
		mv.visitInsn(AASTORE);
		mv.visitJumpInsn(GOTO, l5);
		mv.visitLabel(l1);
		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);
		mv.visitIincInsn(SP, 1);
		mv.visitFieldInsn(GETSTATIC, fullClassName, "Rascal_FALSE", "Lorg/rascalmpl/value/IBool;");
		mv.visitInsn(AASTORE);
		mv.visitLabel(l5);
	}

	public void buildClass(String packageName, String className, boolean debug) {

		emitClass(packageName,className);

		for (Function f : functionStore) {
			emitMethod(f, debug);
			//System.out.println(f.toString() );
		}

		
		// All functions are created create int based dispatcher
		emitDynDispatch(functionMap.size());
		for (Map.Entry<String, Integer> e : functionMap.entrySet()) {
			String fname = e.getKey();
			emitDynCaLL(fname, e.getValue());
		}
		emitDynFinalize();

		OverloadedFunction[] overloadedStoreV2 = overloadedStore.toArray(new OverloadedFunction[overloadedStore.size()]);
		emitConstructor(overloadedStoreV2);
	}

	public String finalName() {
		return fullClassName;
	}

	public void emitInlineLoadEmptyKwMap(boolean debug) {
		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);
		mv.visitTypeInsn(NEW, "java/util/HashMap");
		mv.visitInsn(DUP);
		mv.visitMethodInsn(INVOKESPECIAL, "java/util/HashMap", "<init>", "()V",false);
		mv.visitInsn(AASTORE);
		mv.visitIincInsn(SP, 1);
	}

	public void emitInlineCheckMemo(boolean debug) {
		throw new CompilerError("CheckMemo not implemented");
	}

	public void emitInlineValueSubtype(int type, boolean debug) {
		throw new CompilerError("ValueSubtype not implemented");
	}

	public void emitInlineVisit(boolean debug) {
		throw new CompilerError("Visit not implemented");
	}

	public void emitInlinePopAccu(boolean debug) {
		// TODO Auto-generated method stub
		
	}

	public void emitInlinePushAccu(boolean debug) {
		// TODO Auto-generated method stub
		
	}

	public void emitInlinePushLoc(int pos, boolean debug) {
		// TODO Auto-generated method stub
		
	}
}
