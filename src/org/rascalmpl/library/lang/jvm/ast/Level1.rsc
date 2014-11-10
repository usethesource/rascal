@license{
  Copyright (c) 2009-2013 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jeroen van den Bos - Jeroen.van.den.Bos@cwi.nl (CWI)}
module lang::jvm::ast::Level1

/*
 * TODO:
 * Add support for:
 * - Annotation methods
 * - Annotations (as annotations)
 * - Deserialize
*/

public Class class(int version, set[ClassModifier] modifiers, str name, str signature, str superName, list[str] interfaces,
				   list[InnerClass] innerClasses, list[Field] fields, list[Method] methods) =
				 class(version, modifiers, name, signature, superName, interfaces, "", "", "", "", "",
				   innerClasses, fields, methods);

data PrimitiveTypeDescriptor = boolean() | char() | byte() | short() | \int() | float() | long() | double();
data TypeDescriptor = primitive(PrimitiveTypeDescriptor \type) | object(str internalName) | array(TypeDescriptor \td);

data MethodDescription = methodDescriptor(list[TypeDescriptor] arguments, TypeDescriptor returnType);

data TypeSignature = primitive(PrimitiveTypeDescriptor \type) | fieldType(FieldTypeSignature sig);

data FieldTypeSignature = classType(ClassTypeSignature class) | array(TypeSignature \type) | typeVar(str name);

data ClassTypeSignature = topLevel(str internalName, list[TypeArg] args)
                        | inner(ClassTypeSignature class, str name, list[TypeArg] args);

data TypeArg = \type(FieldTypeSignature fieldType)
             | wildcard()
             | extends(FieldTypeSignature fieldType)
             | super(FieldTypeSignature fieldType);

data Class = class(int version, set[ClassModifier] modifiers, str name, str signature, str superName, list[str] interfaces,
				   str sourceFile, str sourceDebug, str outerClass, str outerMethod, str outerMethodDescription,
				   list[InnerClass] innerClasses, list[Field] fields, list[Method] methods)
		   | class(int version, set[ClassModifier] modifiers, str name, str signature, str superName, list[str] interfaces,
				   list[InnerClass] innerClasses, list[Field] fields, list[Method] methods);

data AccessModifier = \public() | \private() | protected();
data ClassModifier = access(AccessModifier access) | final() | super() | interface() | abstract() | synthetic() | annotation() | enum() | deprecated();

data InnerClass = innerClass(str name, str outerName, str innerName, set[ClassModifier] modifiers)
				| innerClass(str name, set[ClassModifier] modifiers);

data Field = field(set[FieldModifier] modifiers, str name, str description, str signature, value \value)
		   | field(set[FieldModifier] modifiers, str name, str description, str signature);

data FieldModifier = access(AccessModifier access) | static() | final() | volatile() | transient() | synthetic() | enum();

data Method = method(set[MethodModifier] modifiers, str name, str description, str signature, list[str] exceptions,
					 list[Instruction] instructions, list[TryCatchBlock] tryCatchBlocks,
					 list[LocalVariable] localVariables);

data MethodModifier = access(AccessModifier access) | static() | final() | synchronized() | bridge() | varags() | native() | abstract() | strict() | synthetic();

data Instruction = getStatic(str owner, str name, str description)
                 | putStatic(str owner, str name, str description)
                 | getField(str owner, str name, str description)
                 | putField(str owner, str name, str description)

				 | increment(int index, int amount)

				 | nop()
				 | aConstNull()
				 | iConstM1()
				 | iConst0()
				 | iConst1()
				 | iConst2()
				 | iConst3()
				 | iConst4()
				 | iConst5()
				 | lConst0()
				 | lConst1()
				 | fConst0()
				 | fConst1()
				 | fConst2()
				 | dConst0()
				 | dConst1()
				 | iaLoad()
				 | laLoad()
				 | faLoad()
				 | daLoad()
				 | aaLoad()
				 | baLoad()
				 | caLoad()
				 | saLoad()
				 | iaStore()
				 | laStore()
				 | faStore()
				 | daStore()
				 | aaStore()
				 | baStore()
				 | caStore()
				 | saStore()
				 | pop()
				 | pop2()
				 | dup()
				 | dupX1()
				 | dupX2()
				 | dup2()
				 | dup2X1()
				 | dup2X2()
				 | swap()
				 | iAdd()
				 | lAdd()
				 | fAdd()
				 | dAdd()
				 | iSub()
				 | lSub()
				 | fSub()
				 | dSub()
				 | iMul()
				 | lMul()
				 | fMul()
				 | dMul()
				 | iDiv()
				 | lDiv()
				 | fDiv()
				 | dDiv()
				 | iRem()
				 | lRem()
				 | fRem()
				 | dRem()
				 | iNeg()
				 | lNeg()
				 | fNeg()
				 | dNeg()
				 | iShl()
				 | lShl()
				 | iShr()
				 | lShr()
				 | iuShr()
				 | luShr()
				 | iAnd()
				 | lAnd()
				 | iOr()
				 | lOr()
				 | iXor()
				 | lXor()
				 | i2l()
				 | i2f()
				 | i2d()
				 | l2i()
				 | l2f()
				 | l2d()
				 | f2i()
				 | f2l()
				 | f2d()
				 | d2i()
				 | d2l()
				 | d2f()
				 | i2b()
				 | i2c()
				 | i2s()
				 | lCmp()
				 | fCmpL()
				 | fCmpG()
				 | dCmpL()
				 | dCmpH()
				 | iReturn()
				 | lReturn()
				 | fReturn()
				 | dReturn()
				 | aReturn()
				 | \return()
				 | arrayLength()
				 | aThrow()
				 | monitorEnter()
				 | monitorExit()				 

				 | biPush(int operand)
				 | siPush(int operand)
				 | newArray(int operand)
				 
				 | ifEq(int label)
				 | ifNe(int label)
				 | ifLt(int label)
				 | ifGe(int label)
				 | ifGt(int label)
				 | ifLe(int label)
				 | ifICmpEq(int label)
				 | ifICmpNe(int label)
				 | ifICmpLt(int label)
				 | ifICmpGe(int label)
				 | ifICmpGt(int label)
				 | ifICmpLe(int label)
				 | ifACmpEq(int label)
				 | ifACmpNe(int label)
				 | goto(int label)
				 | jsr(int label)
				 | ifNull(int label)
				 | ifNonNull(int label)

				 | label(int index)

				 | lineNumber(int line, int labelIndex)

				 | localVariable(int opcode, int index)
				 | iLoad(int index)
				 | lLoad(int index)
				 | fLoad(int index)
				 | dLoad(int index)
				 | aLoad(int index)
				 | iStore(int index)
				 | lStore(int index)
				 | fStore(int index)
				 | dStore(int index)
				 | aStore(int index)
				 | ret(int index)

				 | ldcString(str string)
				 | ldcInt(int \int)
				 | ldcLong(int long)
				 | ldcFloat(real float)
				 | ldcDouble(real double)

				 | lookupSwitch(int defaultLabel, list[int] keys, list[int] cases)

				 | invokeVirtual(str owner, str name, str description)
				 | invokeSpecial(str owner, str name, str description)
				 | invokeStatic(str owner, str name, str description)
				 | invokeInterface(str owner, str name, str description)
				 | invokeDynamic(str owner, str name, str description)

				 | multiANewArray(str description, int dimensions)

				 | tableSwitch(int min, int max, int defaultLabel, list[int] cases)
				 
				 | new(str internalName)
				 | aNewArray(str internalName)
				 | checkCast(str internalName)
				 | instanceOf(str internalName);

data TryCatchBlock = tryCatchBlock(int startLabelIndex, int endLabelIndex, int handlerLabelIndex, str \type)
				   | finallyBlock(int startLabelIndex, int endLabelIndex, int handlerLabelIndex);

data LocalVariable = localVariable(str name, str description, str signature, int startLabelIndex, int endLabelIndex, int index);
