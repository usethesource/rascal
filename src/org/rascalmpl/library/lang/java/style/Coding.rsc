module lang::java::style::Coding

import analysis::m3::Core;
import lang::java::m3::Core;
import lang::java::m3::AST;
import Message;
import String;

import lang::java::jdt::m3::Core;		// Java specific modules
import lang::java::jdt::m3::AST;

import IO;

/*
ArrayTrailingComma				TBD
AvoidInlineConditionals			TBD
CovariantEquals					TBD
EmptyStatement					TBD
EqualsAvoidNull					TBD
EqualsHashCode					TBD
FinalLocalVariable				TBD
HiddenField						TBD
IllegalInstantiation			TBD
IllegalToken					TBD
IllegalTokenText				TBD
InnerAssignment					TBD
MagicNumber						TBD
MissingSwitchDefault			TBD
ModifiedControlVariable			TBD
RedundantThrows					TBD
SimplifyBooleanExpression		TBD
SimplifyBooleanReturn			TBD
StringLiteralEquality			TBD
NestedForDepth					TBD
NestedIfDepth					TBD
NestedTryDepth					TBD
NoClone							TBD
NoFinalizer						TBD
SuperClone						TBD
SuperFinalize					TBD
IllegalCatch					TBD
IllegalThrows					TBD
PackageDeclaration				TBD
JUnitTestCase					TBD
ReturnCount						TBD
IllegalType						TBD
DeclarationOrder				TBD
ParameterAssignment				TBD
ExplicitInitialization			TBD
DefaultComesLast				TBD
MissingCtor						TBD
FallThrough						TBD
MultipleStringLiterals			TBD
MultipleVariableDeclarations	TBD
RequireThis						TBD
UnnecessaryParentheses			TBD
OneStatementPerLine				TBD
*/