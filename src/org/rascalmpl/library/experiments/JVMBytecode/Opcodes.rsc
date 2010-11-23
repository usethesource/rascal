module experiments::JVMBytecode::Opcodes

// The following values were copied directly from the ASM library's
// org.objectweb.asm.Opcodes interface, hence the inclusion of its
// license agreement.

/***
 * ASM: a very small and fast Java bytecode manipulation framework
 * Copyright (c) 2000-2007 INRIA, France Telecom
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the copyright holders nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

    // access flags

    public int ACC_PUBLIC = 0x0001; // class, field, method
    public int ACC_PRIVATE = 0x0002; // class, field, method
    public int ACC_PROTECTED = 0x0004; // class, field, method
    public int ACC_STATIC = 0x0008; // field, method
    public int ACC_FINAL = 0x0010; // class, field, method
    public int ACC_SUPER = 0x0020; // class
    public int ACC_SYNCHRONIZED = 0x0020; // method
    public int ACC_VOLATILE = 0x0040; // field
    public int ACC_BRIDGE = 0x0040; // method
    public int ACC_VARARGS = 0x0080; // method
    public int ACC_TRANSIENT = 0x0080; // field
    public int ACC_NATIVE = 0x0100; // method
    public int ACC_INTERFACE = 0x0200; // class
    public int ACC_ABSTRACT = 0x0400; // class, method
    public int ACC_STRICT = 0x0800; // method
    public int ACC_SYNTHETIC = 0x1000; // class, field, method
    public int ACC_ANNOTATION = 0x2000; // class
    public int ACC_ENUM = 0x4000; // class(?) field inner

    // ASM specific pseudo access flags

    public int ACC_DEPRECATED = 0x20000; // class, field, method

    // types for NEWARRAY

    public int T_BOOLEAN = 4;
    public int T_CHAR = 5;
    public int T_FLOAT = 6;
    public int T_DOUBLE = 7;
    public int T_BYTE = 8;
    public int T_SHORT = 9;
    public int T_INT= 10;
    public int T_LONG = 11;

    // stack map frame types

    /**
     * Represents an expanded frame. See {@link ClassReader#EXPAND_FRAMES}.
     */
    public int F_NEW = -1;

    /**
     * Represents a compressed frame with complete frame data.
     */
    public int F_FULL = 0;

    /**
     * Represents a compressed frame where locals are the same as the locals in
     * the previous frame, except that additional 1-3 locals are defined, and
     * with an empty stack.
     */
    public int F_APPEND = 1;

    /**
     * Represents a compressed frame where locals are the same as the locals in
     * the previous frame, except that the last 1-3 locals are absent and with
     * an empty stack.
     */
    public int F_CHOP = 2;

    /**
     * Represents a compressed frame with exactly the same locals as the
     * previous frame and with an empty stack.
     */
    public int F_SAME = 3;

    /**
     * Represents a compressed frame with exactly the same locals as the
     * previous frame and with a single value on the stack.
     */
    public int F_SAME1 = 4;

    /** 
     * Represents a owner of an invokedynamic call.
     */
    str INVOKEDYNAMIC_OWNER = "java/lang/dyn/Dynamic";
    
    // opcodes // visit method (- = idem)

    public int NOP = 0; // visitInsn
    public int ACONST_NULL = 1; // -
    public int ICONST_M1 = 2; // -
    public int ICONST_0 = 3; // -
    public int ICONST_1 = 4; // -
    public int ICONST_2 = 5; // -
    public int ICONST_3 = 6; // -
    public int ICONST_4 = 7; // -
    public int ICONST_5 = 8; // -
    public int LCONST_0 = 9; // -
    public int LCONST_1 = 10; // -
    public int FCONST_0 = 11; // -
    public int FCONST_1 = 12; // -
    public int FCONST_2 = 13; // -
    public int DCONST_0 = 14; // -
    public int DCONST_1 = 15; // -
    public int BIPUSH = 16; // visitIntInsn
    public int SIPUSH = 17; // -
    public int LDC = 18; // visitLdcInsn
    // public int LDC_W = 19; // -
    // public int LDC2_W = 20; // -
    public int ILOAD = 21; // visitVarInsn
    public int LLOAD = 22; // -
    public int FLOAD = 23; // -
    public int DLOAD = 24; // -
    public int ALOAD = 25; // -
    // public int ILOAD_0 = 26; // -
    // public int ILOAD_1 = 27; // -
    // public int ILOAD_2 = 28; // -
    // public int ILOAD_3 = 29; // -
    // public int LLOAD_0 = 30; // -
    // public int LLOAD_1 = 31; // -
    // public int LLOAD_2 = 32; // -
    // public int LLOAD_3 = 33; // -
    // public int FLOAD_0 = 34; // -
    // public int FLOAD_1 = 35; // -
    // public int FLOAD_2 = 36; // -
    // public int FLOAD_3 = 37; // -
    // public int DLOAD_0 = 38; // -
    // public int DLOAD_1 = 39; // -
    // public int DLOAD_2 = 40; // -
    // public int DLOAD_3 = 41; // -
    // public int ALOAD_0 = 42; // -
    // public int ALOAD_1 = 43; // -
    // public int ALOAD_2 = 44; // -
    // public int ALOAD_3 = 45; // -
    public int IALOAD = 46; // visitInsn
    public int LALOAD = 47; // -
    public int FALOAD = 48; // -
    public int DALOAD = 49; // -
    public int AALOAD = 50; // -
    public int BALOAD = 51; // -
    public int CALOAD = 52; // -
    public int SALOAD = 53; // -
    public int ISTORE = 54; // visitVarInsn
    public int LSTORE = 55; // -
    public int FSTORE = 56; // -
    public int DSTORE = 57; // -
    public int ASTORE = 58; // -
    // public int ISTORE_0 = 59; // -
    // public int ISTORE_1 = 60; // -
    // public int ISTORE_2 = 61; // -
    // public int ISTORE_3 = 62; // -
    // public int LSTORE_0 = 63; // -
    // public int LSTORE_1 = 64; // -
    // public int LSTORE_2 = 65; // -
    // public int LSTORE_3 = 66; // -
    // public int FSTORE_0 = 67; // -
    // public int FSTORE_1 = 68; // -
    // public int FSTORE_2 = 69; // -
    // public int FSTORE_3 = 70; // -
    // public int DSTORE_0 = 71; // -
    // public int DSTORE_1 = 72; // -
    // public int DSTORE_2 = 73; // -
    // public int DSTORE_3 = 74; // -
    // public int ASTORE_0 = 75; // -
    // public int ASTORE_1 = 76; // -
    // public int ASTORE_2 = 77; // -
    // public int ASTORE_3 = 78; // -
    public int IASTORE = 79; // visitInsn
    public int LASTORE = 80; // -
    public int FASTORE = 81; // -
    public int DASTORE = 82; // -
    public int AASTORE = 83; // -
    public int BASTORE = 84; // -
    public int CASTORE = 85; // -
    public int SASTORE = 86; // -
    public int POP = 87; // -
    public int POP2 = 88; // -
    public int DUP = 89; // -
    public int DUP_X1 = 90; // -
    public int DUP_X2 = 91; // -
    public int DUP2 = 92; // -
    public int DUP2_X1 = 93; // -
    public int DUP2_X2 = 94; // -
    public int SWAP = 95; // -
    public int IADD = 96; // -
    public int LADD = 97; // -
    public int FADD = 98; // -
    public int DADD = 99; // -
    public int ISUB = 100; // -
    public int LSUB = 101; // -
    public int FSUB = 102; // -
    public int DSUB = 103; // -
    public int IMUL = 104; // -
    public int LMUL = 105; // -
    public int FMUL = 106; // -
    public int DMUL = 107; // -
    public int IDIV = 108; // -
    public int LDIV = 109; // -
    public int FDIV = 110; // -
    public int DDIV = 111; // -
    public int IREM = 112; // -
    public int LREM = 113; // -
    public int FREM = 114; // -
    public int DREM = 115; // -
    public int INEG = 116; // -
    public int LNEG = 117; // -
    public int FNEG = 118; // -
    public int DNEG = 119; // -
    public int ISHL = 120; // -
    public int LSHL = 121; // -
    public int ISHR = 122; // -
    public int LSHR = 123; // -
    public int IUSHR = 124; // -
    public int LUSHR = 125; // -
    public int IAND = 126; // -
    public int LAND = 127; // -
    public int IOR = 128; // -
    public int LOR = 129; // -
    public int IXOR = 130; // -
    public int LXOR = 131; // -
    public int IINC = 132; // visitIincInsn
    public int I2L = 133; // visitInsn
    public int I2F = 134; // -
    public int I2D = 135; // -
    public int L2I = 136; // -
    public int L2F = 137; // -
    public int L2D = 138; // -
    public int F2I = 139; // -
    public int F2L = 140; // -
    public int F2D = 141; // -
    public int D2I = 142; // -
    public int D2L = 143; // -
    public int D2F = 144; // -
    public int I2B = 145; // -
    public int I2C = 146; // -
    public int I2S = 147; // -
    public int LCMP = 148; // -
    public int FCMPL = 149; // -
    public int FCMPG = 150; // -
    public int DCMPL = 151; // -
    public int DCMPG = 152; // -
    public int IFEQ = 153; // visitJumpInsn
    public int IFNE = 154; // -
    public int IFLT = 155; // -
    public int IFGE = 156; // -
    public int IFGT = 157; // -
    public int IFLE = 158; // -
    public int IF_ICMPEQ = 159; // -
    public int IF_ICMPNE = 160; // -
    public int IF_ICMPLT = 161; // -
    public int IF_ICMPGE = 162; // -
    public int IF_ICMPGT = 163; // -
    public int IF_ICMPLE = 164; // -
    public int IF_ACMPEQ = 165; // -
    public int IF_ACMPNE = 166; // -
    public int GOTO = 167; // -
    public int JSR = 168; // -
    public int RET = 169; // visitVarInsn
    public int TABLESWITCH = 170; // visiTableSwitchInsn
    public int LOOKUPSWITCH = 171; // visitLookupSwitch
    public int IRETURN = 172; // visitInsn
    public int LRETURN = 173; // -
    public int FRETURN = 174; // -
    public int DRETURN = 175; // -
    public int ARETURN = 176; // -
    public int RETURN = 177; // -
    public int GETSTATIC = 178; // visitFieldInsn
    public int PUTSTATIC = 179; // -
    public int GETFIELD = 180; // -
    public int PUTFIELD = 181; // -
    public int INVOKEVIRTUAL = 182; // visitMethodInsn
    public int INVOKESPECIAL = 183; // -
    public int INVOKESTATIC = 184; // -
    public int INVOKEINTERFACE = 185; // -
    public int INVOKEDYNAMIC = 186; // -
    public int NEW = 187; // visitTypeInsn
    public int NEWARRAY = 188; // visitIntInsn
    public int ANEWARRAY = 189; // visitTypeInsn
    public int ARRAYLENGTH = 190; // visitInsn
    public int ATHROW = 191; // -
    public int CHECKCAST = 192; // visitTypeInsn
    public int INSTANCEOF = 193; // -
    public int MONITORENTER = 194; // visitInsn
    public int MONITOREXIT = 195; // -
    // public int WIDE = 196; // NOT VISITED
    public int MULTIANEWARRAY = 197; // visitMultiANewArrayInsn
    public int IFNULL = 198; // visitJumpInsn
    public int IFNONNULL = 199; // -
    // public int GOTO_W = 200; // -
    // public int JSR_W = 201; // -
