package org.rascalmpl.library.experiments.Compiler.RVM.ToJVM;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Function;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.MuPrimitive;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.OverloadedFunction;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContext;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalPrimitive;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Thrown;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Types;

public class BytecodeGenerator implements Opcodes {

	// Locations of the variables in a compiled RVM function.
	public static final int THIS = 0;
	public static final int CF = 1;
	public static final int SP = 2;
	public static final int STACK = 3;
	public static final int LBOOL = 4;
	public static final int LVAL = 5;
	public static final int LCOROUTINE = 6;
	public static final int LPREVFRAME = 7;
	public static final int EXCEPTION = 8;
	public static final int CS = 9;
	public static final int TS = 10;
	

	byte[] endCode = null;
	private ClassWriter cw = null;
	private MethodVisitor mv = null;
	private FieldVisitor fv = null;
	private String className = null;
	private String packageName = null;
	private String fullClassName = null;
	private String[] funcArray = null;
	private boolean emit = true;
	private boolean isCoroutine = false;

	private HashMap<String, Label> labelMap = new HashMap<String, Label>();
	private Set<String> catchTargetLabels = new HashSet<String>();
	private Map<String, ExceptionLine> catchTargets = new HashMap<String, ExceptionLine>() ;
	private Label[] hotEntryLabels = null;
	private Label exitLabel = null;

	//
	// Optimize some ocalls.
	//
	ArrayList<Function> functionStore;
	ArrayList<OverloadedFunction> overloadedStore;
	
	// Needed to find Types for the exception catch label code.
	RascalExecutionContext rex ;

	private Label getNamedLabel(String targetLabel) {
		Label lb = labelMap.get(targetLabel);
		if (lb == null) {
			lb = new Label();
			labelMap.put(targetLabel, lb);
		}
		return lb;
	}

	public BytecodeGenerator(String packageName2, String className2, ArrayList<Function> functionStore, ArrayList<OverloadedFunction> overloadedStore, RascalExecutionContext rex) {
		emit = true;

		fullClassName = packageName2 + "." + className2;
		fullClassName = fullClassName.replace('.', '/');
		this.functionStore = functionStore;
		this.overloadedStore = overloadedStore;
		this.rex = rex ;
	}

	public BytecodeGenerator() {
		emit = false;
	}

	void enableOutput(boolean flag) {
		emit = flag;
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

	public void emitClass(String pName, String cName) {
		if (!emit)
			return;

		this.className = cName;
		this.packageName = pName;
		this.fullClassName = packageName + "/" + className;
		this.fullClassName = "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RVMRunner";
		cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

		cw.visit(V1_7, ACC_PUBLIC + ACC_SUPER, fullClassName, null, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RVMRun", null);

		// Add constructor initialzing super.
		// Give it REX 15-5-2014
		mv = cw.visitMethod(ACC_PUBLIC, "<init>", "(Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RascalExecutionContext;)V", null, null);
		mv.visitCode();
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, 1);
		mv.visitMethodInsn(INVOKESPECIAL, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RVMRun", "<init>",
				"(Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RascalExecutionContext;)V");
		mv.visitInsn(RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();
	}
	public void emitMethod(Function f, boolean isCoroutine, int continuationPoints, String[] fromLabels, String[] toLabels, int[] fromSp, int[] types, String[] handlerLabels, boolean debug) {

		if (!emit)
			return;

		this.isCoroutine = isCoroutine;
		labelMap.clear(); // New set of labels.
		catchTargetLabels.clear(); 
		catchTargets.clear();
		
// Create statics to contain a functions constantStore and typeConstantStore (Why static?)		
		fv = cw.visitField(ACC_PUBLIC + ACC_STATIC, "cs_" + NameMangler.mangle(f.getName()), "[Ljava/lang/Object;", null, null);
		fv.visitEnd();
		fv = cw.visitField(ACC_PUBLIC + ACC_STATIC, "tcs_" + NameMangler.mangle(f.getName()), "[Ljava/lang/Object;", null, null);
		fv.visitEnd();
// TODO: create initialiser function to fill them. ( TODO: call them through constructor )
		mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "init_" + NameMangler.mangle(f.getName()), "()V", null, null);
		mv.visitCode();
		mv.visitInsn(RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();
		
// Create method		
		mv = cw.visitMethod(ACC_PUBLIC, NameMangler.mangle(f.getName()), "(Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;)Ljava/lang/Object;", null, null);
		mv.visitCode();
		
		emitExceptionTable(fromLabels, toLabels, fromSp, types, handlerLabels);
		
		mv.visitVarInsn(ALOAD, CF);
		mv.visitFieldInsn(GETFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame", "sp", "I");
		mv.visitVarInsn(ISTORE, SP);
		mv.visitVarInsn(ALOAD, CF);
		mv.visitFieldInsn(GETFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame", "stack", "[Ljava/lang/Object;");
		mv.visitVarInsn(ASTORE, STACK);

// Experimental local copies of the functions constantStore and typeConstantStore (Copy from static wise or not ?)
// Serialisation would be nice.	
// TODO : Fix the order problem needed for small optimizations.
//		if ( f.constantStore.length != 0 ) {
			mv.visitVarInsn(ALOAD, CF);
			mv.visitFieldInsn(GETFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame", "function", "Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Function;");
			mv.visitFieldInsn(GETFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Function", "constantStore", "[Lorg/eclipse/imp/pdb/facts/IValue;");
			mv.visitVarInsn(ASTORE, CS);	
//		}
//		if ( f.typeConstantStore.length != 0 ) {
			mv.visitVarInsn(ALOAD, CF);
			mv.visitFieldInsn(GETFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame", "function", "Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Function;");
			mv.visitFieldInsn(GETFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Function", "typeConstantStore", "[Lorg/eclipse/imp/pdb/facts/type/Type;");
			mv.visitVarInsn(ASTORE, TS);		
//		}
// end experiment
		
		if (continuationPoints != 0) {
			hotEntryLabels = new Label[continuationPoints + 1]; // Add entry 0
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
	}


	public void closeMethod() {
		if (!emit)
			return;

		// This label should never be reached, it is
		// placed to
		// keep JVM verifier happy. (Reason: code generated
		// by the compiler jumps sometimes to a non existing
		// location)
		if (exitLabel != null) {
			mv.visitLabel(exitLabel);
			mv.visitVarInsn(ALOAD, THIS);
			mv.visitFieldInsn(GETFIELD, fullClassName, "PANIC", "Lorg/eclipse/imp/pdb/facts/IString;");
			mv.visitInsn(ARETURN);
		}
		mv.visitMaxs(0, 0);
		mv.visitEnd();
	}

	public void emitJMP(String targetLabel) {
		if (!emit)
			return;
		Label lb = getNamedLabel(targetLabel);
		mv.visitJumpInsn(GOTO, lb);
	}

	public void emitJMPTRUE(String targetLabel, boolean debug) {
		if (!emit)
			return;
		Label lb = getNamedLabel(targetLabel);

		emitInlinePop(false); // pop part of jmp...

		mv.visitVarInsn(ALOAD, 3);
		mv.visitVarInsn(ILOAD, SP);
		mv.visitInsn(AALOAD);
		mv.visitMethodInsn(INVOKEINTERFACE, "org/eclipse/imp/pdb/facts/IBool", "getValue", "()Z");
		mv.visitJumpInsn(IFNE, lb);
	}

	public void emitJMPFALSE(String targetLabel, boolean debug) {
		if (!emit)
			return;

		Label lb = getNamedLabel(targetLabel);

		emitInlinePop(false); // pop part of jmp...

		mv.visitVarInsn(ALOAD, 3);
		mv.visitVarInsn(ILOAD, SP);
		mv.visitInsn(AALOAD);
		mv.visitMethodInsn(INVOKEINTERFACE, "org/eclipse/imp/pdb/facts/IBool", "getValue", "()Z");
		mv.visitJumpInsn(IFEQ, lb);
	}

	public void emitLabel(String targetLabel) {
		if (!emit)
			return;
		Label lb = getNamedLabel(targetLabel);
		mv.visitLabel(lb);
		
		ExceptionLine el = catchTargets.get(targetLabel) ;
		if ( el != null ) {
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
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, "exhaustHelper", "([Ljava/lang/Object;ILorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;)Ljava/lang/Object;");
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
					"([Ljava/lang/Object;ILorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;)Ljava/lang/Object;");
		} else {
			mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, "return1Helper",
					"([Ljava/lang/Object;ILorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;)Ljava/lang/Object;");
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
		mv.visitFieldInsn(GETFIELD, fullClassName, "NONE", "Lorg/eclipse/imp/pdb/facts/IString;");
		mv.visitInsn(ARETURN);
	}

	public void emitInlineFailreturn() {
		if (!emit)
			return;

		mv.visitVarInsn(ALOAD, THIS);
		mv.visitFieldInsn(GETFIELD, fullClassName, "FAILRETURN", "Lorg/eclipse/imp/pdb/facts/IString;");
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
			mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, NameMangler.mangle(funcArray[i]), "(Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;)Ljava/lang/Object;");
			mv.visitInsn(ARETURN);
		}
		mv.visitLabel(defaultlabel);

		// Function exit
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitFieldInsn(GETFIELD, fullClassName, "vf", "Lorg/eclipse/imp/pdb/facts/IValueFactory;");
		mv.visitInsn(ICONST_0);
		mv.visitMethodInsn(INVOKEINTERFACE, "org/eclipse/imp/pdb/facts/IValueFactory", "bool", "(Z)Lorg/eclipse/imp/pdb/facts/IBool;");
		mv.visitInsn(ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();
	}

	// Emit inlinefullClassName needs a rearangment of local variables.
	public void emitInlineGuard(int hotEntryPoint, boolean dcode) {
		if (!emit)
			return;

		mv.visitVarInsn(ALOAD, 1);
		emitIntValue(hotEntryPoint);
		
		mv.visitFieldInsn(PUTFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame", "hotEntryPoint", "I");
		mv.visitInsn(ACONST_NULL);
		mv.visitVarInsn(ASTORE, 6);
		mv.visitVarInsn(ALOAD, 1);
		mv.visitFieldInsn(GETFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame", "previousCallFrame", "Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;");
		mv.visitVarInsn(ASTORE, 7);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitVarInsn(ALOAD, 3);
		mv.visitVarInsn(ILOAD, 2);
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, "guardHelper", "([Ljava/lang/Object;I)Z");
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
		mv.visitMethodInsn(INVOKESPECIAL, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Coroutine", "<init>", "(Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;)V");
		mv.visitVarInsn(ASTORE, 6);
		mv.visitVarInsn(ALOAD, 6);
		mv.visitInsn(ICONST_1);
		mv.visitFieldInsn(PUTFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Coroutine", "isInitialized", "Z");
		mv.visitVarInsn(ALOAD, 6);
		mv.visitVarInsn(ALOAD, 1);
		mv.visitFieldInsn(PUTFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Coroutine", "entryFrame", "Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;");
		mv.visitVarInsn(ALOAD, 6);
		mv.visitVarInsn(ALOAD, 1);
		mv.visitMethodInsn(INVOKEVIRTUAL, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Coroutine", "suspend", "(Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;)V");
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
		mv.visitFieldInsn(GETFIELD, fullClassName, "NONE", "Lorg/eclipse/imp/pdb/facts/IString;");
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
		mv.visitFieldInsn(GETSTATIC, fullClassName, "Rascal_FALSE", "Lorg/eclipse/imp/pdb/facts/IBool;");
		mv.visitInsn(AASTORE);
		mv.visitVarInsn(ALOAD, 1);
		mv.visitVarInsn(ILOAD, 2);
		mv.visitFieldInsn(PUTFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame", "sp", "I");

		mv.visitVarInsn(ALOAD, THIS);
		mv.visitFieldInsn(GETFIELD, fullClassName, "NONE", "Lorg/eclipse/imp/pdb/facts/IString;");
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
	
	// Experimemtal local copy of constantStore and typeConstantStore probably need in final version.
	public void emitInlineLoadConOrType(int n, boolean conortype,  boolean debug) {
		if (!emit)	
			return;
		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);
		mv.visitIincInsn(SP, 1);
		
		if ( conortype )
			mv.visitVarInsn(ALOAD, CS);
		else
			mv.visitVarInsn(ALOAD, TS);
			
		emitIntValue(n);
		mv.visitInsn(AALOAD);
		mv.visitInsn(AASTORE);
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
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, "typeSwitchHelper", "([Ljava/lang/Object;I)I");

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
		mv.visitTypeInsn(CHECKCAST, "org/eclipse/imp/pdb/facts/IInteger");
		mv.visitMethodInsn(INVOKEINTERFACE, "org/eclipse/imp/pdb/facts/IInteger", "intValue", "()I");

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
			mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, "yield1Helper", "(Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;[Ljava/lang/Object;III)V");
		} else {
			emitIntValue(hotEntryPoint);
			mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, "yield0Helper", "(Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;[Ljava/lang/Object;II)V");
		}

		mv.visitVarInsn(ALOAD, CF);
		mv.visitFieldInsn(GETFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame", "previousCallFrame",
				"Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;");
		mv.visitJumpInsn(IFNONNULL, continueAt);
		mv.visitFieldInsn(GETSTATIC, fullClassName, "Rascal_TRUE", "Lorg/eclipse/imp/pdb/facts/IBool;");
		mv.visitInsn(ARETURN);

		mv.visitLabel(continueAt);
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitFieldInsn(GETFIELD, fullClassName, "YIELD", "Lorg/eclipse/imp/pdb/facts/IString;");
		mv.visitInsn(ARETURN);

		mv.visitLabel(hotEntryLabels[hotEntryPoint]);
	}

	public void emitPanicReturn() {
		if (!emit)
			return;
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitFieldInsn(GETFIELD, fullClassName, "PANIC", "Lorg/eclipse/imp/pdb/facts/IString;");
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

		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, "callHelper", "([Ljava/lang/Object;ILorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;III)Ljava/lang/Object;");
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitFieldInsn(GETFIELD, fullClassName, "YIELD", "Lorg/eclipse/imp/pdb/facts/IString;");
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Object", "equals", "(Ljava/lang/Object;)Z");
		mv.visitJumpInsn(IFEQ, l0);
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitFieldInsn(GETFIELD, fullClassName, "YIELD", "Lorg/eclipse/imp/pdb/facts/IString;");
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
				"([Ljava/lang/Object;ILorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;II)Ljava/lang/Object;");

		mv.visitVarInsn(ALOAD, THIS);
		mv.visitFieldInsn(GETFIELD, fullClassName, "YIELD", "Lorg/eclipse/imp/pdb/facts/IString;");
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Object", "equals", "(Ljava/lang/Object;)Z");
		mv.visitJumpInsn(IFEQ, l0);
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitFieldInsn(GETFIELD, fullClassName, "YIELD", "Lorg/eclipse/imp/pdb/facts/IString;");
		mv.visitInsn(ARETURN);

		mv.visitLabel(l0);
		
		mv.visitVarInsn(ALOAD, CF);
		mv.visitFieldInsn(GETFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame", "sp", "I");
		mv.visitVarInsn(ISTORE, SP);
	}

	/**
	 * Emits a inline version of the CallMUPrime instructions. Uses a direct call to the static enum execute method.
	 * 
	 */
	public void emitInlineCallMuPrime(MuPrimitive muprim, int arity, boolean debug) {
		if (!emit)
			return;

		mv.visitFieldInsn(GETSTATIC, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/MuPrimitive", muprim.name(),
				"Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/MuPrimitive;");

		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);

		emitIntValue(arity);

		mv.visitMethodInsn(INVOKEVIRTUAL, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/MuPrimitive", "execute", "([Ljava/lang/Object;II)I");
		mv.visitVarInsn(ISTORE, SP);
	}

	public void emitInlineCallPrime(RascalPrimitive prim, int arity, boolean debug) {
		if (!emit)
			return;
	
		mv.visitFieldInsn(GETSTATIC, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RascalPrimitive", prim.name(),
				"Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RascalPrimitive;");

		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);

		emitIntValue(arity);

		mv.visitVarInsn(ALOAD, CF);
		mv.visitMethodInsn(INVOKEVIRTUAL, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RascalPrimitive", "execute",
				"([Ljava/lang/Object;IILorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;)I");
		mv.visitVarInsn(ISTORE, SP);
	}

	public void emitInlineLoadBool(boolean b, boolean debug) {
		if (!emit)
			return;

		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);
		mv.visitIincInsn(SP, 1);
		if (b) {
			mv.visitFieldInsn(GETSTATIC, fullClassName, "Rascal_TRUE", "Lorg/eclipse/imp/pdb/facts/IBool;");
		} else {
			mv.visitFieldInsn(GETSTATIC, fullClassName, "Rascal_FALSE", "Lorg/eclipse/imp/pdb/facts/IBool;");
		}
		mv.visitInsn(AASTORE);		
	}

	public void emitCallWithArgsSS(String fname) {
		if (!emit)
			return;
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);
		
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, "([Ljava/lang/Object;I)I");
		mv.visitVarInsn(ISTORE, SP);
	}

	public void emitCallWithArgsSSI(String fname, int i, boolean dbg) {
		if (!emit)
			return;
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);
		emitIntValue(i);
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, "([Ljava/lang/Object;II)I");
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
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, "([Ljava/lang/Object;III)I");
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
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, "([Ljava/lang/Object;IIII)I");
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

		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, "([Ljava/lang/Object;ILorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;I)I");
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

		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, "([Ljava/lang/Object;ILorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;III)I");
		mv.visitVarInsn(ISTORE, SP);
	}

	public void emitVoidCallWithArgsSSI(String fname, int i, boolean dbg) {
		if (!emit)
			return;
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, 3);
		mv.visitVarInsn(ILOAD, SP);
		emitIntValue(i);
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, "([Ljava/lang/Object;II)V");
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

		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, "([Ljava/lang/Object;ILorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;II)I");
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

		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, "([Ljava/lang/Object;ILorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;IIZ)I");
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

		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, "([Ljava/lang/Object;ILorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;IIZ)V");
	}

	public void emitVoidCallWithArgsSSFII(String fname, int what, int pos, boolean dcode) {
		if (!emit)
			return;
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, 3); // Stack

		mv.visitVarInsn(ALOAD, THIS); // SP
		mv.visitFieldInsn(GETFIELD, fullClassName, "sp", "I");
		mv.visitVarInsn(ALOAD, 1); // CF

		emitIntValue(what); // I
		emitIntValue(pos); // I

		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, "([Ljava/lang/Object;ILorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;II)V");
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

		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, "([Ljava/lang/Object;ILorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;IIII)I");
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

		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, "([Ljava/lang/Object;ILorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;IIIII)I");
		mv.visitVarInsn(ISTORE, SP);
	}

	public void emitVoidCallWithArgsSS(String fname, boolean dcode) {
		if (!emit)
			return;
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, STACK); // Stack
		mv.visitVarInsn(ILOAD, SP); 
		
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, "([Ljava/lang/Object;I)V");
	}

	public void emitVoidCallWithArgsSSFI(String fname, int pos, boolean dcode) {
		if (!emit)
			return;
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, STACK); // Stack
		mv.visitVarInsn(ILOAD, SP); // SP
		mv.visitVarInsn(ALOAD, CF); // CF
		emitIntValue(pos); // I
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, "([Ljava/lang/Object;ILorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;I)V");
	}

	public void emitCallWithArgsSSF(String fname, boolean dcode) {
		if (!emit)
			return;
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, STACK); // Stack
		mv.visitVarInsn(ILOAD, SP); // Stack

		mv.visitVarInsn(ALOAD, CF); // CF

		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, "([Ljava/lang/Object;ILorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;)I");
		mv.visitVarInsn(ISTORE, SP);
	}

	/*
	 * emitOptimizedOcall emits a call to a full ocall implementation or: 1: There is only one funtion emit direct call 2: There is only a constructor call constructor
	 */
	public void emitOptimizedOcall(String fuid, int overloadedFunctionIndex, int arity, boolean dcode) {
		if (!emit)
			return;
		OverloadedFunction of = overloadedStore.get(overloadedFunctionIndex);
		int[] functions = of.getFuntions();
		if (functions.length == 1) {
			int[] ctors = of.getConstructors();
			if (ctors.length == 0) {
				Function fu = functionStore.get(functions[0]);
				if (of.getScopeFun() == null) {
					emitCallWithArgsSSFII("jvmOCALL", overloadedFunctionIndex, arity, dcode);
					// emitOcallSingle(NameMangler.mangle(fu.getName()), functions[0], arity);
				} else {
					// Nested function needs link to containing frame
					emitCallWithArgsSSFII("jvmOCALL", overloadedFunctionIndex, arity, dcode);
				}
			} else {
				// Has a constructor.
				emitCallWithArgsSSFII("jvmOCALL", overloadedFunctionIndex, arity, dcode);
			}
		} else {
			emitCallWithArgsSSFII("jvmOCALL", overloadedFunctionIndex, arity, dcode);
		}
	}

	private void emitOcallSingle(String funName, int fun, int arity) {
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitFieldInsn(GETFIELD, fullClassName, "cf", "Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;");
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitFieldInsn(GETFIELD, fullClassName, "functionStore", "Ljava/util/ArrayList;");

		emitIntValue(fun);

		mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/ArrayList", "get", "(I)Ljava/lang/Object;");
		mv.visitTypeInsn(CHECKCAST, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Function");
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitFieldInsn(GETFIELD, fullClassName, "root", "Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;");

		emitIntValue(arity);

		mv.visitVarInsn(ALOAD, THIS);
		mv.visitFieldInsn(GETFIELD, fullClassName, "sp", "I");
		mv.visitMethodInsn(
				INVOKEVIRTUAL,
				"org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame",
				"getFrame",
				"(Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Function;Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;II)Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;");
		mv.visitFieldInsn(PUTFIELD, fullClassName, "cf", "Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;");
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitFieldInsn(GETFIELD, fullClassName, "cf", "Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;");
		mv.visitFieldInsn(GETFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame", "stack", "[Ljava/lang/Object;");
		mv.visitFieldInsn(PUTFIELD, fullClassName, "stack", "[Ljava/lang/Object;");
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitFieldInsn(GETFIELD, fullClassName, "cf", "Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;");
		mv.visitFieldInsn(GETFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame", "sp", "I");
		mv.visitFieldInsn(PUTFIELD, fullClassName, "sp", "I");
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitFieldInsn(GETFIELD, fullClassName, "cf", "Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;");
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, funName, "(Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;)Ljava/lang/Object;");
		mv.visitInsn(POP);
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
		
		if ( concrete )
			mv.visitInsn(ICONST_1);
		else
			mv.visitInsn(ICONST_0);
			
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, "switchHelper", "([Ljava/lang/Object;IZ)I");
		mv.visitLookupSwitchInsn(trampolineLabel, intTable, switchTable);

		emitLabel(caseDefault + "_trampoline");

		// In case of default push RASCAL_FALSE on stack. Ask Paul why?.. done
		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);
		mv.visitIincInsn(SP, 1);
		mv.visitFieldInsn(GETSTATIC, fullClassName, "Rascal_FALSE", "Lorg/eclipse/imp/pdb/facts/IBool;");
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

		mv.visitVarInsn(ALOAD, CF);
		mv.visitVarInsn(ILOAD, SP);
		mv.visitMethodInsn(INVOKESTATIC, fullClassName, "debug" + ins, "(Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;I)V");
	}

	public void emitInlineLoadInt(int nval, boolean debug) {
		if (!emit)
			return;

		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);
		mv.visitIincInsn(SP, 1);
		emitIntValue(nval);
		mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;");
		mv.visitInsn(AASTORE);
	}

	private void emitExceptionTable(String[] fromLabels, String[] toLabels, int[] fromSp, int[] types, String[] handlerLabels) {
		if (!emit)
			return;

		int len = handlerLabels.length ;		
		for ( int i = 0 ; i < len ; i++ ) {
			Label fromLabel = getNamedLabel(fromLabels[i]) ;
			Label toLabel =   getNamedLabel(toLabels[i]) ;
			Label handlerLabel = getNamedLabel(handlerLabels[i]) ;

			catchTargetLabels.add(handlerLabels[i]) ;
			catchTargets.put(handlerLabels[i], new ExceptionLine(handlerLabels[i], fromSp[i], types[i])) ;
			
			//System.out.println("Exceptions :" + fromLabels[i] + " to " + toLabels[i] + " to " + handlerLabels[i] );
			
			mv.visitTryCatchBlock(fromLabel, toLabel, handlerLabel,  "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Thrown");
		}
	}
	
	public void emitCatchLabelEpilogue(int type, int newsp) {

// --- Code implements.		
//		catch(Thrown e) {
//			sp = newsp ;
//			stack[sp++] = e ;
//			if ( ! e.value.getType().isSubtypeOf(cf.function.typeConstantStore[type]) ) 
//				throw e ; 
//		}
//      .....		
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
		mv.visitFieldInsn(GETFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Thrown", "value", "Lorg/eclipse/imp/pdb/facts/IValue;");
		mv.visitMethodInsn(INVOKEINTERFACE, "org/eclipse/imp/pdb/facts/IValue", "getType", "()Lorg/eclipse/imp/pdb/facts/type/Type;");
		mv.visitVarInsn(ALOAD, CF);
		mv.visitFieldInsn(GETFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame", "function", "Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Function;");
		mv.visitFieldInsn(GETFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Function", "typeConstantStore", "[Lorg/eclipse/imp/pdb/facts/type/Type;");

		emitIntValue(type);

		mv.visitInsn(AALOAD);
		mv.visitMethodInsn(INVOKEVIRTUAL, "org/eclipse/imp/pdb/facts/type/Type", "isSubtypeOf", "(Lorg/eclipse/imp/pdb/facts/type/Type;)Z");
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
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, "thrownHelper", "(Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;[Ljava/lang/Object;I)Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Thrown;");
		mv.visitInsn(ATHROW);
			
	}

	public void emitInlineResetLocs(int positions, IValue constantValues, boolean debug) {
		if (!emit)
			return;

		IList il = (IList) constantValues ;
		for (IValue v : il) {
			int stackPos = ((IInteger) v).intValue();
			mv.visitVarInsn(ALOAD, STACK);
			emitIntValue(stackPos);
		}
		mv.visitInsn(ACONST_NULL);

		for ( int i = 1 ; i < il.length(); i++ ) {
			mv.visitInsn(DUP_X2);
			mv.visitInsn(AASTORE);
		}
		mv.visitInsn(AASTORE);
		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);
		mv.visitIincInsn(SP, 1);
		mv.visitFieldInsn(GETSTATIC, fullClassName, "Rascal_TRUE", "Lorg/eclipse/imp/pdb/facts/IBool;");
		mv.visitInsn(AASTORE);
	}
	
	private class ExceptionLine {
		String catchLabel;
		int    newsp ;
		int    type ;
		public ExceptionLine(String lbl, int sp, int type) {
			this.catchLabel = lbl;
			this.newsp = sp ;
			this.type = type ;
		}
	}

}
