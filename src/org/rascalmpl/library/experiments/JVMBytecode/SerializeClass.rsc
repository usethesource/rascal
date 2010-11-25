module experiments::JVMBytecode::SerializeClass

/*
 * TODO:
 * Add support for:
 * - Annotation methods
 * - Annotations (as annotations)
 * - Deserialize
*/

rule expandClass class(Version version, int access, str name, str signature, str superName, list[str] interfaces,
				   list[InnerClass] innerClasses, list[Field] fields, list[Method] methods) =>
				 class(version, access, name, signature, superName, interfaces, "", "", "", "", "",
				   innerClasses, fields, methods);
rule expandInnerClass innerClass(str name, int access) => innerClass(name, "", "", access);

data Class = class(Version version, int access, str name, str signature, str superName, list[str] interfaces,
				   str sourceFile, str sourceDebug, str outerClass, str outerMethod, str outerMethodDescription,
				   list[InnerClass] innerClasses, list[Field] fields, list[Method] methods)
		   | class(Version version, int access, str name, str signature, str superName, list[str] interfaces,
				   list[InnerClass] innerClasses, list[Field] fields, list[Method] methods);
data Version = version(int major, int minor);
data InnerClass = innerClass(str name, str outerName, str innerName, int access)
				| innerClass(str name, int access);
data Field = field(int access, str name, str description, str signature, value \value)
		   | field(int access, str name, str description, str signature);
data Method = method(int access, str name, str description, str signature, list[str] exceptions,
					 list[Instruction] instructions, list[TryCatchBlock] tryCatchBlocks,
					 list[LocalVariable] localVariables);
data Instruction = field(int opcode, str owner, str name, str description)
				 | increment(int index, int amount)
				 | instruction(int opcode)
				 | integer(int opcode, int operand)
				 | jump(int opcode, int labelIndex)
				 | label(int index)
				 | lineNumber(int line, int labelIndex)
				 | localVariable(int opcode, int index)
				 | loadConstantString(str \value)
				 | loadConstantInteger(int \value)
				 | loadConstantLong(int \value)
				 | loadConstantFloat(real \value)
				 | loadConstantDouble(real \value)
				 | lookupSwitch(int defaultLabelIndex, list[int] keys, list[int] cases)
				 | method(int opcode, str owner, str name, str description)
				 | multiANewArray(str description, int dimensions)
				 | tableSwitch(int minIndex, int maxIndex, int defaultLabelIndex, list[int] cases)
				 | \type(int opcode, str description);
data TryCatchBlock = tryCatchBlock(int startLabelIndex, int endLabelIndex, int handlerLabelIndex, str \type)
				   | finallyBlock(int startLabelIndex, int endLabelIndex, int handlerLabelIndex);
data LocalVariable = localVariableDeclaration(str name, str description, str signature, int startLabelIndex, int endLabelIndex, int index);

@javaClass{org.rascalmpl.library.experiments.JVMBytecode.SerializeClass}
public void java serialize(Class class, loc path)
throws PathNotFound(loc), IOError(str msg), JavaBytecodeError(str msg);
