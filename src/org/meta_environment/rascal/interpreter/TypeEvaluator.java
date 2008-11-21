package org.meta_environment.rascal.interpreter;

import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.meta_environment.rascal.ast.Expression;
import org.meta_environment.rascal.ast.NullASTVisitor;
import org.meta_environment.rascal.ast.BasicType.Bool;
import org.meta_environment.rascal.ast.BasicType.Double;
import org.meta_environment.rascal.ast.BasicType.Int;
import org.meta_environment.rascal.ast.BasicType.Loc;
import org.meta_environment.rascal.ast.BasicType.String;
import org.meta_environment.rascal.ast.BasicType.Tree;
import org.meta_environment.rascal.ast.BasicType.Value;
import org.meta_environment.rascal.ast.BasicType.Void;
import org.meta_environment.rascal.ast.BooleanLiteral.Lexical;
import org.meta_environment.rascal.ast.Expression.And;
import org.meta_environment.rascal.ast.Expression.List;
import org.meta_environment.rascal.ast.Expression.NonEmptySet;
import org.meta_environment.rascal.ast.IntegerLiteral.DecimalIntegerLiteral;
import org.meta_environment.rascal.ast.IntegerLiteral.HexIntegerLiteral;
import org.meta_environment.rascal.ast.IntegerLiteral.OctalIntegerLiteral;
import org.meta_environment.rascal.ast.Type.Basic;
import org.meta_environment.rascal.ast.Type.Function;

public class TypeEvaluator extends NullASTVisitor<Type> {
	private TypeFactory tf = TypeFactory.getInstance();

	@Override
	public Type visitIntegerLiteralDecimalIntegerLiteral(DecimalIntegerLiteral x) {
		return tf.integerType();
	}
	
	@Override
	public Type visitIntegerLiteralHexIntegerLiteral(HexIntegerLiteral x) {
		return tf.integerType();
	}
	
	@Override
	public Type visitIntegerLiteralOctalIntegerLiteral(OctalIntegerLiteral x) {
		return tf.integerType();
	}

	@Override
	public Type visitBooleanLiteralLexical(Lexical x) {
		return tf.boolType();
	}

	@Override
	public Type visitDoubleLiteralLexical(
			org.meta_environment.rascal.ast.DoubleLiteral.Lexical x) {
		return tf.doubleType();
	}

	@Override
	public Type visitExpressionAnd(And x) {
		return tf.boolType();
	}
	
	@Override
	public Type visitExpressionList(List x) {
		return elementType(x.getElements());
	}

	private Type elementType(java.util.List<Expression> elements) {
		Type elementType = tf.voidType();
		for (org.meta_environment.rascal.ast.Expression e : elements) {
			elementType = elementType.lub(e.accept(this));
		}
		return tf.listType(elementType);
	}
	
	@Override
	public Type visitExpressionNonEmptySet(NonEmptySet x) {
		return elementType(x.getElements());
	}
	
	@Override
	public Type visitTypeBasic(Basic x) {
		return x.getBasic().accept(this);
	}
	
	@Override
	public Type visitTypeFunction(Function x) {
		return x.getFunction().getType().accept(this);
	}
	
	@Override
	public Type visitBasicTypeBool(Bool x) {
		return tf.boolType();
	}
	
	@Override
	public Type visitBasicTypeDouble(Double x) {
		return tf.doubleType();
	}
	
	@Override
	public Type visitBasicTypeInt(Int x) {
		return tf.integerType();
	}
	
	@Override
	public Type visitBasicTypeLoc(Loc x) {
		return tf.sourceLocationType();
	}
	
	@Override
	public Type visitBasicTypeString(String x) {
		return tf.stringType();
	}
	
	@Override
	public Type visitBasicTypeTree(Tree x) {
		return tf.treeType();
	}
	
	@Override
	public Type visitBasicTypeValue(Value x) {
		return tf.valueType();
	}
	
	@Override
	public Type visitBasicTypeVoid(Void x) {
		return tf.voidType();
	}
}
