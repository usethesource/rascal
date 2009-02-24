package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Expression extends AbstractAST { 
  public java.util.List<org.meta_environment.rascal.ast.Statement> getStatements() { throw new UnsupportedOperationException(); } public boolean hasStatements() { return false; } public boolean isNonEmptyBlock() { return false; } static public class NonEmptyBlock extends Expression {
/* "{" statements:Statement+ "}" -> Expression {cons("NonEmptyBlock")} */
	private NonEmptyBlock() { }
	/*package*/ NonEmptyBlock(INode node, java.util.List<org.meta_environment.rascal.ast.Statement> statements) {
		this.node = node;
		this.statements = statements;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitExpressionNonEmptyBlock(this);
	}

	public boolean isNonEmptyBlock() { return true; }

	public boolean hasStatements() { return true; }

private java.util.List<org.meta_environment.rascal.ast.Statement> statements;
	public java.util.List<org.meta_environment.rascal.ast.Statement> getStatements() { return statements; }
	private void $setStatements(java.util.List<org.meta_environment.rascal.ast.Statement> x) { this.statements = x; }
	public NonEmptyBlock setStatements(java.util.List<org.meta_environment.rascal.ast.Statement> x) { 
		NonEmptyBlock z = new NonEmptyBlock();
 		z.$setStatements(x);
		return z;
	}	
} static public class Ambiguity extends Expression {
  private final java.util.List<org.meta_environment.rascal.ast.Expression> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Expression> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.meta_environment.rascal.ast.Expression> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitExpressionAmbiguity(this);
  }
} public org.meta_environment.rascal.ast.Visit getVisit() { throw new UnsupportedOperationException(); } public boolean hasVisit() { return false; } public boolean isVisit() { return false; } static public class Visit extends Expression {
/* visit:Visit -> Expression {cons("Visit")} */
	private Visit() { }
	/*package*/ Visit(INode node, org.meta_environment.rascal.ast.Visit visit) {
		this.node = node;
		this.visit = visit;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitExpressionVisit(this);
	}

	public boolean isVisit() { return true; }

	public boolean hasVisit() { return true; }

private org.meta_environment.rascal.ast.Visit visit;
	public org.meta_environment.rascal.ast.Visit getVisit() { return visit; }
	private void $setVisit(org.meta_environment.rascal.ast.Visit x) { this.visit = x; }
	public Visit setVisit(org.meta_environment.rascal.ast.Visit x) { 
		Visit z = new Visit();
 		z.$setVisit(x);
		return z;
	}	
} public abstract <T> T accept(IASTVisitor<T> visitor); public org.meta_environment.rascal.ast.Literal getLiteral() { throw new UnsupportedOperationException(); }
public boolean hasLiteral() { return false; }
public boolean isLiteral() { return false; }
static public class Literal extends Expression {
/* literal:Literal -> Expression {cons("Literal")} */
	private Literal() { }
	/*package*/ Literal(INode node, org.meta_environment.rascal.ast.Literal literal) {
		this.node = node;
		this.literal = literal;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitExpressionLiteral(this);
	}

	public boolean isLiteral() { return true; }

	public boolean hasLiteral() { return true; }

private org.meta_environment.rascal.ast.Literal literal;
	public org.meta_environment.rascal.ast.Literal getLiteral() { return literal; }
	private void $setLiteral(org.meta_environment.rascal.ast.Literal x) { this.literal = x; }
	public Literal setLiteral(org.meta_environment.rascal.ast.Literal x) { 
		Literal z = new Literal();
 		z.$setLiteral(x);
		return z;
	}	
} public org.meta_environment.rascal.ast.QualifiedName getQualifiedName() { throw new UnsupportedOperationException(); } public java.util.List<org.meta_environment.rascal.ast.Expression> getArguments() { throw new UnsupportedOperationException(); } public boolean hasQualifiedName() { return false; } public boolean hasArguments() { return false; } public boolean isCallOrTree() { return false; }
static public class CallOrTree extends Expression {
/* qualifiedName:QualifiedName "(" arguments:{Expression ","}* ")" -> Expression {cons("CallOrTree")} */
	private CallOrTree() { }
	/*package*/ CallOrTree(INode node, org.meta_environment.rascal.ast.QualifiedName qualifiedName, java.util.List<org.meta_environment.rascal.ast.Expression> arguments) {
		this.node = node;
		this.qualifiedName = qualifiedName;
		this.arguments = arguments;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitExpressionCallOrTree(this);
	}

	public boolean isCallOrTree() { return true; }

	public boolean hasQualifiedName() { return true; }
	public boolean hasArguments() { return true; }

private org.meta_environment.rascal.ast.QualifiedName qualifiedName;
	public org.meta_environment.rascal.ast.QualifiedName getQualifiedName() { return qualifiedName; }
	private void $setQualifiedName(org.meta_environment.rascal.ast.QualifiedName x) { this.qualifiedName = x; }
	public CallOrTree setQualifiedName(org.meta_environment.rascal.ast.QualifiedName x) { 
		CallOrTree z = new CallOrTree();
 		z.$setQualifiedName(x);
		return z;
	}
	private java.util.List<org.meta_environment.rascal.ast.Expression> arguments;
	public java.util.List<org.meta_environment.rascal.ast.Expression> getArguments() { return arguments; }
	private void $setArguments(java.util.List<org.meta_environment.rascal.ast.Expression> x) { this.arguments = x; }
	public CallOrTree setArguments(java.util.List<org.meta_environment.rascal.ast.Expression> x) { 
		CallOrTree z = new CallOrTree();
 		z.$setArguments(x);
		return z;
	}	
} public java.util.List<org.meta_environment.rascal.ast.Expression> getElements() { throw new UnsupportedOperationException(); } public boolean hasElements() { return false; } public boolean isList() { return false; }
static public class List extends Expression {
/* "[" elements:{Expression ","}* "]" -> Expression {cons("List")} */
	private List() { }
	/*package*/ List(INode node, java.util.List<org.meta_environment.rascal.ast.Expression> elements) {
		this.node = node;
		this.elements = elements;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitExpressionList(this);
	}

	public boolean isList() { return true; }

	public boolean hasElements() { return true; }

private java.util.List<org.meta_environment.rascal.ast.Expression> elements;
	public java.util.List<org.meta_environment.rascal.ast.Expression> getElements() { return elements; }
	private void $setElements(java.util.List<org.meta_environment.rascal.ast.Expression> x) { this.elements = x; }
	public List setElements(java.util.List<org.meta_environment.rascal.ast.Expression> x) { 
		List z = new List();
 		z.$setElements(x);
		return z;
	}	
} public boolean isSet() { return false; }
static public class Set extends Expression {
/* "{" elements:{Expression ","}* "}" -> Expression {cons("Set")} */
	private Set() { }
	/*package*/ Set(INode node, java.util.List<org.meta_environment.rascal.ast.Expression> elements) {
		this.node = node;
		this.elements = elements;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitExpressionSet(this);
	}

	public boolean isSet() { return true; }

	public boolean hasElements() { return true; }

private java.util.List<org.meta_environment.rascal.ast.Expression> elements;
	public java.util.List<org.meta_environment.rascal.ast.Expression> getElements() { return elements; }
	private void $setElements(java.util.List<org.meta_environment.rascal.ast.Expression> x) { this.elements = x; }
	public Set setElements(java.util.List<org.meta_environment.rascal.ast.Expression> x) { 
		Set z = new Set();
 		z.$setElements(x);
		return z;
	}	
} public boolean isTuple() { return false; }
static public class Tuple extends Expression {
/* "<" elements:{Expression ","}+ ">" -> Expression {cons("Tuple")} */
	private Tuple() { }
	/*package*/ Tuple(INode node, java.util.List<org.meta_environment.rascal.ast.Expression> elements) {
		this.node = node;
		this.elements = elements;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitExpressionTuple(this);
	}

	public boolean isTuple() { return true; }

	public boolean hasElements() { return true; }

private java.util.List<org.meta_environment.rascal.ast.Expression> elements;
	public java.util.List<org.meta_environment.rascal.ast.Expression> getElements() { return elements; }
	private void $setElements(java.util.List<org.meta_environment.rascal.ast.Expression> x) { this.elements = x; }
	public Tuple setElements(java.util.List<org.meta_environment.rascal.ast.Expression> x) { 
		Tuple z = new Tuple();
 		z.$setElements(x);
		return z;
	}	
} 
public java.util.List<org.meta_environment.rascal.ast.Mapping> getMappings() { throw new UnsupportedOperationException(); }
public boolean hasMappings() { return false; }
public boolean isMap() { return false; }
static public class Map extends Expression {
/* "(" mappings:{Mapping ","}* ")" -> Expression {cons("Map")} */
	private Map() { }
	/*package*/ Map(INode node, java.util.List<org.meta_environment.rascal.ast.Mapping> mappings) {
		this.node = node;
		this.mappings = mappings;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitExpressionMap(this);
	}

	public boolean isMap() { return true; }

	public boolean hasMappings() { return true; }

private java.util.List<org.meta_environment.rascal.ast.Mapping> mappings;
	public java.util.List<org.meta_environment.rascal.ast.Mapping> getMappings() { return mappings; }
	private void $setMappings(java.util.List<org.meta_environment.rascal.ast.Mapping> x) { this.mappings = x; }
	public Map setMappings(java.util.List<org.meta_environment.rascal.ast.Mapping> x) { 
		Map z = new Map();
 		z.$setMappings(x);
		return z;
	}	
} public org.meta_environment.rascal.ast.Expression getFilename() { throw new UnsupportedOperationException(); } public boolean hasFilename() { return false; } public boolean isFileLocation() { return false; }
static public class FileLocation extends Expression {
/* "file" "(" filename:Expression ")" -> Expression {cons("FileLocation")} */
	private FileLocation() { }
	/*package*/ FileLocation(INode node, org.meta_environment.rascal.ast.Expression filename) {
		this.node = node;
		this.filename = filename;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitExpressionFileLocation(this);
	}

	public boolean isFileLocation() { return true; }

	public boolean hasFilename() { return true; }

private org.meta_environment.rascal.ast.Expression filename;
	public org.meta_environment.rascal.ast.Expression getFilename() { return filename; }
	private void $setFilename(org.meta_environment.rascal.ast.Expression x) { this.filename = x; }
	public FileLocation setFilename(org.meta_environment.rascal.ast.Expression x) { 
		FileLocation z = new FileLocation();
 		z.$setFilename(x);
		return z;
	}	
} public org.meta_environment.rascal.ast.Expression getAreaExpression() { throw new UnsupportedOperationException(); } public boolean hasAreaExpression() { return false; }
public boolean isAreaInFileLocation() { return false; }
static public class AreaInFileLocation extends Expression {
/* "area-in-file" "(" filename:Expression "," 
                     areaExpression:Expression ")" -> Expression {cons("AreaInFileLocation")} */
	private AreaInFileLocation() { }
	/*package*/ AreaInFileLocation(INode node, org.meta_environment.rascal.ast.Expression filename, org.meta_environment.rascal.ast.Expression areaExpression) {
		this.node = node;
		this.filename = filename;
		this.areaExpression = areaExpression;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitExpressionAreaInFileLocation(this);
	}

	public boolean isAreaInFileLocation() { return true; }

	public boolean hasFilename() { return true; }
	public boolean hasAreaExpression() { return true; }

private org.meta_environment.rascal.ast.Expression filename;
	public org.meta_environment.rascal.ast.Expression getFilename() { return filename; }
	private void $setFilename(org.meta_environment.rascal.ast.Expression x) { this.filename = x; }
	public AreaInFileLocation setFilename(org.meta_environment.rascal.ast.Expression x) { 
		AreaInFileLocation z = new AreaInFileLocation();
 		z.$setFilename(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Expression areaExpression;
	public org.meta_environment.rascal.ast.Expression getAreaExpression() { return areaExpression; }
	private void $setAreaExpression(org.meta_environment.rascal.ast.Expression x) { this.areaExpression = x; }
	public AreaInFileLocation setAreaExpression(org.meta_environment.rascal.ast.Expression x) { 
		AreaInFileLocation z = new AreaInFileLocation();
 		z.$setAreaExpression(x);
		return z;
	}	
} 
public org.meta_environment.rascal.ast.Expression getBeginLine() { throw new UnsupportedOperationException(); }
	public org.meta_environment.rascal.ast.Expression getBeginColumn() { throw new UnsupportedOperationException(); }
	public org.meta_environment.rascal.ast.Expression getEndLine() { throw new UnsupportedOperationException(); }
	public org.meta_environment.rascal.ast.Expression getEndColumn() { throw new UnsupportedOperationException(); }
	public org.meta_environment.rascal.ast.Expression getOffset() { throw new UnsupportedOperationException(); }
	public org.meta_environment.rascal.ast.Expression getLength() { throw new UnsupportedOperationException(); }
public boolean hasBeginLine() { return false; }
	public boolean hasBeginColumn() { return false; }
	public boolean hasEndLine() { return false; }
	public boolean hasEndColumn() { return false; }
	public boolean hasOffset() { return false; }
	public boolean hasLength() { return false; }
public boolean isArea() { return false; }
static public class Area extends Expression {
/* "area" "(" beginLine:Expression "," 
             beginColumn:Expression "," 
             endLine:Expression "," 
             endColumn:Expression "," 
             offset:Expression "," 
             length:Expression ")" -> Expression {cons("Area")} */
	private Area() { }
	/*package*/ Area(INode node, org.meta_environment.rascal.ast.Expression beginLine, org.meta_environment.rascal.ast.Expression beginColumn, org.meta_environment.rascal.ast.Expression endLine, org.meta_environment.rascal.ast.Expression endColumn, org.meta_environment.rascal.ast.Expression offset, org.meta_environment.rascal.ast.Expression length) {
		this.node = node;
		this.beginLine = beginLine;
		this.beginColumn = beginColumn;
		this.endLine = endLine;
		this.endColumn = endColumn;
		this.offset = offset;
		this.length = length;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitExpressionArea(this);
	}

	public boolean isArea() { return true; }

	public boolean hasBeginLine() { return true; }
	public boolean hasBeginColumn() { return true; }
	public boolean hasEndLine() { return true; }
	public boolean hasEndColumn() { return true; }
	public boolean hasOffset() { return true; }
	public boolean hasLength() { return true; }

private org.meta_environment.rascal.ast.Expression beginLine;
	public org.meta_environment.rascal.ast.Expression getBeginLine() { return beginLine; }
	private void $setBeginLine(org.meta_environment.rascal.ast.Expression x) { this.beginLine = x; }
	public Area setBeginLine(org.meta_environment.rascal.ast.Expression x) { 
		Area z = new Area();
 		z.$setBeginLine(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Expression beginColumn;
	public org.meta_environment.rascal.ast.Expression getBeginColumn() { return beginColumn; }
	private void $setBeginColumn(org.meta_environment.rascal.ast.Expression x) { this.beginColumn = x; }
	public Area setBeginColumn(org.meta_environment.rascal.ast.Expression x) { 
		Area z = new Area();
 		z.$setBeginColumn(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Expression endLine;
	public org.meta_environment.rascal.ast.Expression getEndLine() { return endLine; }
	private void $setEndLine(org.meta_environment.rascal.ast.Expression x) { this.endLine = x; }
	public Area setEndLine(org.meta_environment.rascal.ast.Expression x) { 
		Area z = new Area();
 		z.$setEndLine(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Expression endColumn;
	public org.meta_environment.rascal.ast.Expression getEndColumn() { return endColumn; }
	private void $setEndColumn(org.meta_environment.rascal.ast.Expression x) { this.endColumn = x; }
	public Area setEndColumn(org.meta_environment.rascal.ast.Expression x) { 
		Area z = new Area();
 		z.$setEndColumn(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Expression offset;
	public org.meta_environment.rascal.ast.Expression getOffset() { return offset; }
	private void $setOffset(org.meta_environment.rascal.ast.Expression x) { this.offset = x; }
	public Area setOffset(org.meta_environment.rascal.ast.Expression x) { 
		Area z = new Area();
 		z.$setOffset(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Expression length;
	public org.meta_environment.rascal.ast.Expression getLength() { return length; }
	private void $setLength(org.meta_environment.rascal.ast.Expression x) { this.length = x; }
	public Area setLength(org.meta_environment.rascal.ast.Expression x) { 
		Area z = new Area();
 		z.$setLength(x);
		return z;
	}	
} public boolean isQualifiedName() { return false; }
static public class QualifiedName extends Expression {
/* qualifiedName:QualifiedName -> Expression {cons("QualifiedName")} */
	private QualifiedName() { }
	/*package*/ QualifiedName(INode node, org.meta_environment.rascal.ast.QualifiedName qualifiedName) {
		this.node = node;
		this.qualifiedName = qualifiedName;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitExpressionQualifiedName(this);
	}

	public boolean isQualifiedName() { return true; }

	public boolean hasQualifiedName() { return true; }

private org.meta_environment.rascal.ast.QualifiedName qualifiedName;
	public org.meta_environment.rascal.ast.QualifiedName getQualifiedName() { return qualifiedName; }
	private void $setQualifiedName(org.meta_environment.rascal.ast.QualifiedName x) { this.qualifiedName = x; }
	public QualifiedName setQualifiedName(org.meta_environment.rascal.ast.QualifiedName x) { 
		QualifiedName z = new QualifiedName();
 		z.$setQualifiedName(x);
		return z;
	}	
} public org.meta_environment.rascal.ast.Type getType() { throw new UnsupportedOperationException(); } public org.meta_environment.rascal.ast.Name getName() { throw new UnsupportedOperationException(); } public boolean hasType() { return false; } public boolean hasName() { return false; } public boolean isTypedVariable() { return false; }
static public class TypedVariable extends Expression {
/* type:Type name:Name -> Expression {cons("TypedVariable")} */
	private TypedVariable() { }
	/*package*/ TypedVariable(INode node, org.meta_environment.rascal.ast.Type type, org.meta_environment.rascal.ast.Name name) {
		this.node = node;
		this.type = type;
		this.name = name;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitExpressionTypedVariable(this);
	}

	public boolean isTypedVariable() { return true; }

	public boolean hasType() { return true; }
	public boolean hasName() { return true; }

private org.meta_environment.rascal.ast.Type type;
	public org.meta_environment.rascal.ast.Type getType() { return type; }
	private void $setType(org.meta_environment.rascal.ast.Type x) { this.type = x; }
	public TypedVariable setType(org.meta_environment.rascal.ast.Type x) { 
		TypedVariable z = new TypedVariable();
 		z.$setType(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Name name;
	public org.meta_environment.rascal.ast.Name getName() { return name; }
	private void $setName(org.meta_environment.rascal.ast.Name x) { this.name = x; }
	public TypedVariable setName(org.meta_environment.rascal.ast.Name x) { 
		TypedVariable z = new TypedVariable();
 		z.$setName(x);
		return z;
	}	
} public org.meta_environment.rascal.ast.Expression getCondition() { throw new UnsupportedOperationException(); } public org.meta_environment.rascal.ast.Expression getThenExp() { throw new UnsupportedOperationException(); } public org.meta_environment.rascal.ast.Expression getElseExp() { throw new UnsupportedOperationException(); } public boolean hasCondition() { return false; } public boolean hasThenExp() { return false; } public boolean hasElseExp() { return false; } public boolean isIfThenElse() { return false; } static public class IfThenElse extends Expression {
/* condition:Expression "?" thenExp:Expression ":" 
                           elseExp:Expression -> Expression {right, cons("IfThenElse")} */
	private IfThenElse() { }
	/*package*/ IfThenElse(INode node, org.meta_environment.rascal.ast.Expression condition, org.meta_environment.rascal.ast.Expression thenExp, org.meta_environment.rascal.ast.Expression elseExp) {
		this.node = node;
		this.condition = condition;
		this.thenExp = thenExp;
		this.elseExp = elseExp;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitExpressionIfThenElse(this);
	}

	public boolean isIfThenElse() { return true; }

	public boolean hasCondition() { return true; }
	public boolean hasThenExp() { return true; }
	public boolean hasElseExp() { return true; }

private org.meta_environment.rascal.ast.Expression condition;
	public org.meta_environment.rascal.ast.Expression getCondition() { return condition; }
	private void $setCondition(org.meta_environment.rascal.ast.Expression x) { this.condition = x; }
	public IfThenElse setCondition(org.meta_environment.rascal.ast.Expression x) { 
		IfThenElse z = new IfThenElse();
 		z.$setCondition(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Expression thenExp;
	public org.meta_environment.rascal.ast.Expression getThenExp() { return thenExp; }
	private void $setThenExp(org.meta_environment.rascal.ast.Expression x) { this.thenExp = x; }
	public IfThenElse setThenExp(org.meta_environment.rascal.ast.Expression x) { 
		IfThenElse z = new IfThenElse();
 		z.$setThenExp(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Expression elseExp;
	public org.meta_environment.rascal.ast.Expression getElseExp() { return elseExp; }
	private void $setElseExp(org.meta_environment.rascal.ast.Expression x) { this.elseExp = x; }
	public IfThenElse setElseExp(org.meta_environment.rascal.ast.Expression x) { 
		IfThenElse z = new IfThenElse();
 		z.$setElseExp(x);
		return z;
	}	
} public org.meta_environment.rascal.ast.Expression getPattern() { throw new UnsupportedOperationException(); } public org.meta_environment.rascal.ast.Expression getExpression() { throw new UnsupportedOperationException(); } public boolean hasPattern() { return false; } public boolean hasExpression() { return false; } public boolean isMatch() { return false; }
static public class Match extends Expression {
/* pattern:Expression ":=" expression:Expression -> Expression {non-assoc, cons("Match")} */
	private Match() { }
	/*package*/ Match(INode node, org.meta_environment.rascal.ast.Expression pattern, org.meta_environment.rascal.ast.Expression expression) {
		this.node = node;
		this.pattern = pattern;
		this.expression = expression;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitExpressionMatch(this);
	}

	public boolean isMatch() { return true; }

	public boolean hasPattern() { return true; }
	public boolean hasExpression() { return true; }

private org.meta_environment.rascal.ast.Expression pattern;
	public org.meta_environment.rascal.ast.Expression getPattern() { return pattern; }
	private void $setPattern(org.meta_environment.rascal.ast.Expression x) { this.pattern = x; }
	public Match setPattern(org.meta_environment.rascal.ast.Expression x) { 
		Match z = new Match();
 		z.$setPattern(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Expression expression;
	public org.meta_environment.rascal.ast.Expression getExpression() { return expression; }
	private void $setExpression(org.meta_environment.rascal.ast.Expression x) { this.expression = x; }
	public Match setExpression(org.meta_environment.rascal.ast.Expression x) { 
		Match z = new Match();
 		z.$setExpression(x);
		return z;
	}	
} public boolean isNoMatch() { return false; }
static public class NoMatch extends Expression {
/* pattern:Expression "!:=" expression:Expression -> Expression {non-assoc, cons("NoMatch")} */
	private NoMatch() { }
	/*package*/ NoMatch(INode node, org.meta_environment.rascal.ast.Expression pattern, org.meta_environment.rascal.ast.Expression expression) {
		this.node = node;
		this.pattern = pattern;
		this.expression = expression;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitExpressionNoMatch(this);
	}

	public boolean isNoMatch() { return true; }

	public boolean hasPattern() { return true; }
	public boolean hasExpression() { return true; }

private org.meta_environment.rascal.ast.Expression pattern;
	public org.meta_environment.rascal.ast.Expression getPattern() { return pattern; }
	private void $setPattern(org.meta_environment.rascal.ast.Expression x) { this.pattern = x; }
	public NoMatch setPattern(org.meta_environment.rascal.ast.Expression x) { 
		NoMatch z = new NoMatch();
 		z.$setPattern(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Expression expression;
	public org.meta_environment.rascal.ast.Expression getExpression() { return expression; }
	private void $setExpression(org.meta_environment.rascal.ast.Expression x) { this.expression = x; }
	public NoMatch setExpression(org.meta_environment.rascal.ast.Expression x) { 
		NoMatch z = new NoMatch();
 		z.$setExpression(x);
		return z;
	}	
} public org.meta_environment.rascal.ast.Expression getLhs() { throw new UnsupportedOperationException(); } public org.meta_environment.rascal.ast.Expression getRhs() { throw new UnsupportedOperationException(); } public boolean hasLhs() { return false; } public boolean hasRhs() { return false; } public boolean isEquals() { return false; } static public class Equals extends Expression {
/* lhs:Expression "==" rhs:Expression -> Expression {left, cons("Equals")} */
	private Equals() { }
	/*package*/ Equals(INode node, org.meta_environment.rascal.ast.Expression lhs, org.meta_environment.rascal.ast.Expression rhs) {
		this.node = node;
		this.lhs = lhs;
		this.rhs = rhs;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitExpressionEquals(this);
	}

	public boolean isEquals() { return true; }

	public boolean hasLhs() { return true; }
	public boolean hasRhs() { return true; }

private org.meta_environment.rascal.ast.Expression lhs;
	public org.meta_environment.rascal.ast.Expression getLhs() { return lhs; }
	private void $setLhs(org.meta_environment.rascal.ast.Expression x) { this.lhs = x; }
	public Equals setLhs(org.meta_environment.rascal.ast.Expression x) { 
		Equals z = new Equals();
 		z.$setLhs(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Expression rhs;
	public org.meta_environment.rascal.ast.Expression getRhs() { return rhs; }
	private void $setRhs(org.meta_environment.rascal.ast.Expression x) { this.rhs = x; }
	public Equals setRhs(org.meta_environment.rascal.ast.Expression x) { 
		Equals z = new Equals();
 		z.$setRhs(x);
		return z;
	}	
} public boolean isValueProducer() { return false; }
static public class ValueProducer extends Expression {
/* pattern:Expression "<-" expression:Expression -> Expression {prefer, cons("ValueProducer")} */
	private ValueProducer() { }
	/*package*/ ValueProducer(INode node, org.meta_environment.rascal.ast.Expression pattern, org.meta_environment.rascal.ast.Expression expression) {
		this.node = node;
		this.pattern = pattern;
		this.expression = expression;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitExpressionValueProducer(this);
	}

	public boolean isValueProducer() { return true; }

	public boolean hasPattern() { return true; }
	public boolean hasExpression() { return true; }

private org.meta_environment.rascal.ast.Expression pattern;
	public org.meta_environment.rascal.ast.Expression getPattern() { return pattern; }
	private void $setPattern(org.meta_environment.rascal.ast.Expression x) { this.pattern = x; }
	public ValueProducer setPattern(org.meta_environment.rascal.ast.Expression x) { 
		ValueProducer z = new ValueProducer();
 		z.$setPattern(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Expression expression;
	public org.meta_environment.rascal.ast.Expression getExpression() { return expression; }
	private void $setExpression(org.meta_environment.rascal.ast.Expression x) { this.expression = x; }
	public ValueProducer setExpression(org.meta_environment.rascal.ast.Expression x) { 
		ValueProducer z = new ValueProducer();
 		z.$setExpression(x);
		return z;
	}	
} 
public org.meta_environment.rascal.ast.Strategy getStrategy() { throw new UnsupportedOperationException(); } public boolean hasStrategy() { return false; } public boolean isValueProducerWithStrategy() { return false; }
static public class ValueProducerWithStrategy extends Expression {
/* strategy:Strategy pattern:Expression "<-" expression:Expression -> Expression {prefer, cons("ValueProducerWithStrategy")} */
	private ValueProducerWithStrategy() { }
	/*package*/ ValueProducerWithStrategy(INode node, org.meta_environment.rascal.ast.Strategy strategy, org.meta_environment.rascal.ast.Expression pattern, org.meta_environment.rascal.ast.Expression expression) {
		this.node = node;
		this.strategy = strategy;
		this.pattern = pattern;
		this.expression = expression;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitExpressionValueProducerWithStrategy(this);
	}

	public boolean isValueProducerWithStrategy() { return true; }

	public boolean hasStrategy() { return true; }
	public boolean hasPattern() { return true; }
	public boolean hasExpression() { return true; }

private org.meta_environment.rascal.ast.Strategy strategy;
	public org.meta_environment.rascal.ast.Strategy getStrategy() { return strategy; }
	private void $setStrategy(org.meta_environment.rascal.ast.Strategy x) { this.strategy = x; }
	public ValueProducerWithStrategy setStrategy(org.meta_environment.rascal.ast.Strategy x) { 
		ValueProducerWithStrategy z = new ValueProducerWithStrategy();
 		z.$setStrategy(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Expression pattern;
	public org.meta_environment.rascal.ast.Expression getPattern() { return pattern; }
	private void $setPattern(org.meta_environment.rascal.ast.Expression x) { this.pattern = x; }
	public ValueProducerWithStrategy setPattern(org.meta_environment.rascal.ast.Expression x) { 
		ValueProducerWithStrategy z = new ValueProducerWithStrategy();
 		z.$setPattern(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Expression expression;
	public org.meta_environment.rascal.ast.Expression getExpression() { return expression; }
	private void $setExpression(org.meta_environment.rascal.ast.Expression x) { this.expression = x; }
	public ValueProducerWithStrategy setExpression(org.meta_environment.rascal.ast.Expression x) { 
		ValueProducerWithStrategy z = new ValueProducerWithStrategy();
 		z.$setExpression(x);
		return z;
	}	
} 
public org.meta_environment.rascal.ast.Comprehension getComprehension() { throw new UnsupportedOperationException(); }
public boolean hasComprehension() { return false; }
public boolean isComprehension() { return false; }
static public class Comprehension extends Expression {
/* comprehension:Comprehension -> Expression {cons("Comprehension")} */
	private Comprehension() { }
	/*package*/ Comprehension(INode node, org.meta_environment.rascal.ast.Comprehension comprehension) {
		this.node = node;
		this.comprehension = comprehension;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitExpressionComprehension(this);
	}

	public boolean isComprehension() { return true; }

	public boolean hasComprehension() { return true; }

private org.meta_environment.rascal.ast.Comprehension comprehension;
	public org.meta_environment.rascal.ast.Comprehension getComprehension() { return comprehension; }
	private void $setComprehension(org.meta_environment.rascal.ast.Comprehension x) { this.comprehension = x; }
	public Comprehension setComprehension(org.meta_environment.rascal.ast.Comprehension x) { 
		Comprehension z = new Comprehension();
 		z.$setComprehension(x);
		return z;
	}	
} public java.util.List<org.meta_environment.rascal.ast.Expression> getGenerators() { throw new UnsupportedOperationException(); } public boolean hasGenerators() { return false; } public boolean isAll() { return false; }
static public class All extends Expression {
/* "all" "(" generators:{Expression ","}+ ")" -> Expression {cons("All")} */
	private All() { }
	/*package*/ All(INode node, java.util.List<org.meta_environment.rascal.ast.Expression> generators) {
		this.node = node;
		this.generators = generators;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitExpressionAll(this);
	}

	public boolean isAll() { return true; }

	public boolean hasGenerators() { return true; }

private java.util.List<org.meta_environment.rascal.ast.Expression> generators;
	public java.util.List<org.meta_environment.rascal.ast.Expression> getGenerators() { return generators; }
	private void $setGenerators(java.util.List<org.meta_environment.rascal.ast.Expression> x) { this.generators = x; }
	public All setGenerators(java.util.List<org.meta_environment.rascal.ast.Expression> x) { 
		All z = new All();
 		z.$setGenerators(x);
		return z;
	}	
} public boolean isAny() { return false; }
static public class Any extends Expression {
/* "any" "(" generators:{Expression ","}+ ")" -> Expression {cons("Any")} */
	private Any() { }
	/*package*/ Any(INode node, java.util.List<org.meta_environment.rascal.ast.Expression> generators) {
		this.node = node;
		this.generators = generators;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitExpressionAny(this);
	}

	public boolean isAny() { return true; }

	public boolean hasGenerators() { return true; }

private java.util.List<org.meta_environment.rascal.ast.Expression> generators;
	public java.util.List<org.meta_environment.rascal.ast.Expression> getGenerators() { return generators; }
	private void $setGenerators(java.util.List<org.meta_environment.rascal.ast.Expression> x) { this.generators = x; }
	public Any setGenerators(java.util.List<org.meta_environment.rascal.ast.Expression> x) { 
		Any z = new Any();
 		z.$setGenerators(x);
		return z;
	}	
} public org.meta_environment.rascal.ast.Parameters getParameters() { throw new UnsupportedOperationException(); } public boolean hasParameters() { return false; } public boolean isClosure() { return false; }
static public class Closure extends Expression {
/* type:Type parameters:Parameters "{" statements:Statement+ "}" -> Expression {cons("Closure")} */
	private Closure() { }
	/*package*/ Closure(INode node, org.meta_environment.rascal.ast.Type type, org.meta_environment.rascal.ast.Parameters parameters, java.util.List<org.meta_environment.rascal.ast.Statement> statements) {
		this.node = node;
		this.type = type;
		this.parameters = parameters;
		this.statements = statements;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitExpressionClosure(this);
	}

	public boolean isClosure() { return true; }

	public boolean hasType() { return true; }
	public boolean hasParameters() { return true; }
	public boolean hasStatements() { return true; }

private org.meta_environment.rascal.ast.Type type;
	public org.meta_environment.rascal.ast.Type getType() { return type; }
	private void $setType(org.meta_environment.rascal.ast.Type x) { this.type = x; }
	public Closure setType(org.meta_environment.rascal.ast.Type x) { 
		Closure z = new Closure();
 		z.$setType(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Parameters parameters;
	public org.meta_environment.rascal.ast.Parameters getParameters() { return parameters; }
	private void $setParameters(org.meta_environment.rascal.ast.Parameters x) { this.parameters = x; }
	public Closure setParameters(org.meta_environment.rascal.ast.Parameters x) { 
		Closure z = new Closure();
 		z.$setParameters(x);
		return z;
	}
	private java.util.List<org.meta_environment.rascal.ast.Statement> statements;
	public java.util.List<org.meta_environment.rascal.ast.Statement> getStatements() { return statements; }
	private void $setStatements(java.util.List<org.meta_environment.rascal.ast.Statement> x) { this.statements = x; }
	public Closure setStatements(java.util.List<org.meta_environment.rascal.ast.Statement> x) { 
		Closure z = new Closure();
 		z.$setStatements(x);
		return z;
	}	
} public boolean isVoidClosure() { return false; }
static public class VoidClosure extends Expression {
/* parameters:Parameters "{" statements:Statement+ "}" -> Expression {cons("VoidClosure")} */
	private VoidClosure() { }
	/*package*/ VoidClosure(INode node, org.meta_environment.rascal.ast.Parameters parameters, java.util.List<org.meta_environment.rascal.ast.Statement> statements) {
		this.node = node;
		this.parameters = parameters;
		this.statements = statements;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitExpressionVoidClosure(this);
	}

	public boolean isVoidClosure() { return true; }

	public boolean hasParameters() { return true; }
	public boolean hasStatements() { return true; }

private org.meta_environment.rascal.ast.Parameters parameters;
	public org.meta_environment.rascal.ast.Parameters getParameters() { return parameters; }
	private void $setParameters(org.meta_environment.rascal.ast.Parameters x) { this.parameters = x; }
	public VoidClosure setParameters(org.meta_environment.rascal.ast.Parameters x) { 
		VoidClosure z = new VoidClosure();
 		z.$setParameters(x);
		return z;
	}
	private java.util.List<org.meta_environment.rascal.ast.Statement> statements;
	public java.util.List<org.meta_environment.rascal.ast.Statement> getStatements() { return statements; }
	private void $setStatements(java.util.List<org.meta_environment.rascal.ast.Statement> x) { this.statements = x; }
	public VoidClosure setStatements(java.util.List<org.meta_environment.rascal.ast.Statement> x) { 
		VoidClosure z = new VoidClosure();
 		z.$setStatements(x);
		return z;
	}	
} public boolean isBracket() { return false; }
static public class Bracket extends Expression {
/* "(" expression:Expression ")" -> Expression {cons("Bracket"), bracket} */
	private Bracket() { }
	/*package*/ Bracket(INode node, org.meta_environment.rascal.ast.Expression expression) {
		this.node = node;
		this.expression = expression;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitExpressionBracket(this);
	}

	public boolean isBracket() { return true; }

	public boolean hasExpression() { return true; }

private org.meta_environment.rascal.ast.Expression expression;
	public org.meta_environment.rascal.ast.Expression getExpression() { return expression; }
	private void $setExpression(org.meta_environment.rascal.ast.Expression x) { this.expression = x; }
	public Bracket setExpression(org.meta_environment.rascal.ast.Expression x) { 
		Bracket z = new Bracket();
 		z.$setExpression(x);
		return z;
	}	
} public org.meta_environment.rascal.ast.Expression getFirst() { throw new UnsupportedOperationException(); } public org.meta_environment.rascal.ast.Expression getLast() { throw new UnsupportedOperationException(); } public boolean hasFirst() { return false; } public boolean hasLast() { return false; } public boolean isRange() { return false; }
static public class Range extends Expression {
/* "[" first:Expression ".."  last:Expression "]" -> Expression {cons("Range")} */
	private Range() { }
	/*package*/ Range(INode node, org.meta_environment.rascal.ast.Expression first, org.meta_environment.rascal.ast.Expression last) {
		this.node = node;
		this.first = first;
		this.last = last;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitExpressionRange(this);
	}

	public boolean isRange() { return true; }

	public boolean hasFirst() { return true; }
	public boolean hasLast() { return true; }

private org.meta_environment.rascal.ast.Expression first;
	public org.meta_environment.rascal.ast.Expression getFirst() { return first; }
	private void $setFirst(org.meta_environment.rascal.ast.Expression x) { this.first = x; }
	public Range setFirst(org.meta_environment.rascal.ast.Expression x) { 
		Range z = new Range();
 		z.$setFirst(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Expression last;
	public org.meta_environment.rascal.ast.Expression getLast() { return last; }
	private void $setLast(org.meta_environment.rascal.ast.Expression x) { this.last = x; }
	public Range setLast(org.meta_environment.rascal.ast.Expression x) { 
		Range z = new Range();
 		z.$setLast(x);
		return z;
	}	
} public org.meta_environment.rascal.ast.Expression getSecond() { throw new UnsupportedOperationException(); } public boolean hasSecond() { return false; } public boolean isStepRange() { return false; }
static public class StepRange extends Expression {
/* "[" first:Expression "," second:Expression ".." last:Expression "]" -> Expression {cons("StepRange")} */
	private StepRange() { }
	/*package*/ StepRange(INode node, org.meta_environment.rascal.ast.Expression first, org.meta_environment.rascal.ast.Expression second, org.meta_environment.rascal.ast.Expression last) {
		this.node = node;
		this.first = first;
		this.second = second;
		this.last = last;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitExpressionStepRange(this);
	}

	public boolean isStepRange() { return true; }

	public boolean hasFirst() { return true; }
	public boolean hasSecond() { return true; }
	public boolean hasLast() { return true; }

private org.meta_environment.rascal.ast.Expression first;
	public org.meta_environment.rascal.ast.Expression getFirst() { return first; }
	private void $setFirst(org.meta_environment.rascal.ast.Expression x) { this.first = x; }
	public StepRange setFirst(org.meta_environment.rascal.ast.Expression x) { 
		StepRange z = new StepRange();
 		z.$setFirst(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Expression second;
	public org.meta_environment.rascal.ast.Expression getSecond() { return second; }
	private void $setSecond(org.meta_environment.rascal.ast.Expression x) { this.second = x; }
	public StepRange setSecond(org.meta_environment.rascal.ast.Expression x) { 
		StepRange z = new StepRange();
 		z.$setSecond(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Expression last;
	public org.meta_environment.rascal.ast.Expression getLast() { return last; }
	private void $setLast(org.meta_environment.rascal.ast.Expression x) { this.last = x; }
	public StepRange setLast(org.meta_environment.rascal.ast.Expression x) { 
		StepRange z = new StepRange();
 		z.$setLast(x);
		return z;
	}	
} 
public org.meta_environment.rascal.ast.OperatorAsValue getOperator() { throw new UnsupportedOperationException(); }
public boolean hasOperator() { return false; }
public boolean isOperatorAsValue() { return false; }
static public class OperatorAsValue extends Expression {
/* operator:OperatorAsValue -> Expression {cons("OperatorAsValue")} */
	private OperatorAsValue() { }
	/*package*/ OperatorAsValue(INode node, org.meta_environment.rascal.ast.OperatorAsValue operator) {
		this.node = node;
		this.operator = operator;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitExpressionOperatorAsValue(this);
	}

	public boolean isOperatorAsValue() { return true; }

	public boolean hasOperator() { return true; }

private org.meta_environment.rascal.ast.OperatorAsValue operator;
	public org.meta_environment.rascal.ast.OperatorAsValue getOperator() { return operator; }
	private void $setOperator(org.meta_environment.rascal.ast.OperatorAsValue x) { this.operator = x; }
	public OperatorAsValue setOperator(org.meta_environment.rascal.ast.OperatorAsValue x) { 
		OperatorAsValue z = new OperatorAsValue();
 		z.$setOperator(x);
		return z;
	}	
} 
public org.meta_environment.rascal.ast.FunctionAsValue getFunction() { throw new UnsupportedOperationException(); }
public boolean hasFunction() { return false; }
public boolean isFunctionAsValue() { return false; }
static public class FunctionAsValue extends Expression {
/* function:FunctionAsValue -> Expression {cons("FunctionAsValue")} */
	private FunctionAsValue() { }
	/*package*/ FunctionAsValue(INode node, org.meta_environment.rascal.ast.FunctionAsValue function) {
		this.node = node;
		this.function = function;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitExpressionFunctionAsValue(this);
	}

	public boolean isFunctionAsValue() { return true; }

	public boolean hasFunction() { return true; }

private org.meta_environment.rascal.ast.FunctionAsValue function;
	public org.meta_environment.rascal.ast.FunctionAsValue getFunction() { return function; }
	private void $setFunction(org.meta_environment.rascal.ast.FunctionAsValue x) { this.function = x; }
	public FunctionAsValue setFunction(org.meta_environment.rascal.ast.FunctionAsValue x) { 
		FunctionAsValue z = new FunctionAsValue();
 		z.$setFunction(x);
		return z;
	}	
} 
public org.meta_environment.rascal.ast.ClosureAsFunction getClosure() { throw new UnsupportedOperationException(); } public boolean hasClosure() { return false; } public boolean isClosureCall() { return false; }
static public class ClosureCall extends Expression {
/* closure:ClosureAsFunction "(" arguments:{Expression ","}* ")" -> Expression {cons("ClosureCall")} */
	private ClosureCall() { }
	/*package*/ ClosureCall(INode node, org.meta_environment.rascal.ast.ClosureAsFunction closure, java.util.List<org.meta_environment.rascal.ast.Expression> arguments) {
		this.node = node;
		this.closure = closure;
		this.arguments = arguments;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitExpressionClosureCall(this);
	}

	public boolean isClosureCall() { return true; }

	public boolean hasClosure() { return true; }
	public boolean hasArguments() { return true; }

private org.meta_environment.rascal.ast.ClosureAsFunction closure;
	public org.meta_environment.rascal.ast.ClosureAsFunction getClosure() { return closure; }
	private void $setClosure(org.meta_environment.rascal.ast.ClosureAsFunction x) { this.closure = x; }
	public ClosureCall setClosure(org.meta_environment.rascal.ast.ClosureAsFunction x) { 
		ClosureCall z = new ClosureCall();
 		z.$setClosure(x);
		return z;
	}
	private java.util.List<org.meta_environment.rascal.ast.Expression> arguments;
	public java.util.List<org.meta_environment.rascal.ast.Expression> getArguments() { return arguments; }
	private void $setArguments(java.util.List<org.meta_environment.rascal.ast.Expression> x) { this.arguments = x; }
	public ClosureCall setArguments(java.util.List<org.meta_environment.rascal.ast.Expression> x) { 
		ClosureCall z = new ClosureCall();
 		z.$setArguments(x);
		return z;
	}	
} public org.meta_environment.rascal.ast.Name getKey() { throw new UnsupportedOperationException(); }
	public org.meta_environment.rascal.ast.Expression getReplacement() { throw new UnsupportedOperationException(); } public boolean hasKey() { return false; }
	public boolean hasReplacement() { return false; }
public boolean isFieldUpdate() { return false; }
static public class FieldUpdate extends Expression {
/* expression:Expression "[" key:Name "->" replacement:Expression "]" -> Expression {cons("FieldUpdate")} */
	private FieldUpdate() { }
	/*package*/ FieldUpdate(INode node, org.meta_environment.rascal.ast.Expression expression, org.meta_environment.rascal.ast.Name key, org.meta_environment.rascal.ast.Expression replacement) {
		this.node = node;
		this.expression = expression;
		this.key = key;
		this.replacement = replacement;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitExpressionFieldUpdate(this);
	}

	public boolean isFieldUpdate() { return true; }

	public boolean hasExpression() { return true; }
	public boolean hasKey() { return true; }
	public boolean hasReplacement() { return true; }

private org.meta_environment.rascal.ast.Expression expression;
	public org.meta_environment.rascal.ast.Expression getExpression() { return expression; }
	private void $setExpression(org.meta_environment.rascal.ast.Expression x) { this.expression = x; }
	public FieldUpdate setExpression(org.meta_environment.rascal.ast.Expression x) { 
		FieldUpdate z = new FieldUpdate();
 		z.$setExpression(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Name key;
	public org.meta_environment.rascal.ast.Name getKey() { return key; }
	private void $setKey(org.meta_environment.rascal.ast.Name x) { this.key = x; }
	public FieldUpdate setKey(org.meta_environment.rascal.ast.Name x) { 
		FieldUpdate z = new FieldUpdate();
 		z.$setKey(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Expression replacement;
	public org.meta_environment.rascal.ast.Expression getReplacement() { return replacement; }
	private void $setReplacement(org.meta_environment.rascal.ast.Expression x) { this.replacement = x; }
	public FieldUpdate setReplacement(org.meta_environment.rascal.ast.Expression x) { 
		FieldUpdate z = new FieldUpdate();
 		z.$setReplacement(x);
		return z;
	}	
} public org.meta_environment.rascal.ast.Name getField() { throw new UnsupportedOperationException(); } public boolean hasField() { return false; }
public boolean isFieldAccess() { return false; }
static public class FieldAccess extends Expression {
/* expression:Expression "." field:Name -> Expression {cons("FieldAccess")} */
	private FieldAccess() { }
	/*package*/ FieldAccess(INode node, org.meta_environment.rascal.ast.Expression expression, org.meta_environment.rascal.ast.Name field) {
		this.node = node;
		this.expression = expression;
		this.field = field;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitExpressionFieldAccess(this);
	}

	public boolean isFieldAccess() { return true; }

	public boolean hasExpression() { return true; }
	public boolean hasField() { return true; }

private org.meta_environment.rascal.ast.Expression expression;
	public org.meta_environment.rascal.ast.Expression getExpression() { return expression; }
	private void $setExpression(org.meta_environment.rascal.ast.Expression x) { this.expression = x; }
	public FieldAccess setExpression(org.meta_environment.rascal.ast.Expression x) { 
		FieldAccess z = new FieldAccess();
 		z.$setExpression(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Name field;
	public org.meta_environment.rascal.ast.Name getField() { return field; }
	private void $setField(org.meta_environment.rascal.ast.Name x) { this.field = x; }
	public FieldAccess setField(org.meta_environment.rascal.ast.Name x) { 
		FieldAccess z = new FieldAccess();
 		z.$setField(x);
		return z;
	}	
} public java.util.List<org.meta_environment.rascal.ast.Field> getFields() { throw new UnsupportedOperationException(); } public boolean hasFields() { return false; }
public boolean isFieldProject() { return false; }
static public class FieldProject extends Expression {
/* expression:Expression "<" fields:{Field ","}+ ">" -> Expression {cons("FieldProject")} */
	private FieldProject() { }
	/*package*/ FieldProject(INode node, org.meta_environment.rascal.ast.Expression expression, java.util.List<org.meta_environment.rascal.ast.Field> fields) {
		this.node = node;
		this.expression = expression;
		this.fields = fields;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitExpressionFieldProject(this);
	}

	public boolean isFieldProject() { return true; }

	public boolean hasExpression() { return true; }
	public boolean hasFields() { return true; }

private org.meta_environment.rascal.ast.Expression expression;
	public org.meta_environment.rascal.ast.Expression getExpression() { return expression; }
	private void $setExpression(org.meta_environment.rascal.ast.Expression x) { this.expression = x; }
	public FieldProject setExpression(org.meta_environment.rascal.ast.Expression x) { 
		FieldProject z = new FieldProject();
 		z.$setExpression(x);
		return z;
	}
	private java.util.List<org.meta_environment.rascal.ast.Field> fields;
	public java.util.List<org.meta_environment.rascal.ast.Field> getFields() { return fields; }
	private void $setFields(java.util.List<org.meta_environment.rascal.ast.Field> x) { this.fields = x; }
	public FieldProject setFields(java.util.List<org.meta_environment.rascal.ast.Field> x) { 
		FieldProject z = new FieldProject();
 		z.$setFields(x);
		return z;
	}	
} public java.util.List<org.meta_environment.rascal.ast.Expression> getSubscripts() { throw new UnsupportedOperationException(); } public boolean hasSubscripts() { return false; }
public boolean isSubscript() { return false; }
static public class Subscript extends Expression {
/* expression:Expression "[" subscripts: {Expression ","}+"]" -> Expression {cons("Subscript")} */
	private Subscript() { }
	/*package*/ Subscript(INode node, org.meta_environment.rascal.ast.Expression expression, java.util.List<org.meta_environment.rascal.ast.Expression> subscripts) {
		this.node = node;
		this.expression = expression;
		this.subscripts = subscripts;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitExpressionSubscript(this);
	}

	public boolean isSubscript() { return true; }

	public boolean hasExpression() { return true; }
	public boolean hasSubscripts() { return true; }

private org.meta_environment.rascal.ast.Expression expression;
	public org.meta_environment.rascal.ast.Expression getExpression() { return expression; }
	private void $setExpression(org.meta_environment.rascal.ast.Expression x) { this.expression = x; }
	public Subscript setExpression(org.meta_environment.rascal.ast.Expression x) { 
		Subscript z = new Subscript();
 		z.$setExpression(x);
		return z;
	}
	private java.util.List<org.meta_environment.rascal.ast.Expression> subscripts;
	public java.util.List<org.meta_environment.rascal.ast.Expression> getSubscripts() { return subscripts; }
	private void $setSubscripts(java.util.List<org.meta_environment.rascal.ast.Expression> x) { this.subscripts = x; }
	public Subscript setSubscripts(java.util.List<org.meta_environment.rascal.ast.Expression> x) { 
		Subscript z = new Subscript();
 		z.$setSubscripts(x);
		return z;
	}	
} public org.meta_environment.rascal.ast.Expression getArgument() { throw new UnsupportedOperationException(); } public boolean hasArgument() { return false; } public boolean isNegation() { return false; }
static public class Negation extends Expression {
/* "!" argument:Expression -> Expression {cons("Negation")} */
	private Negation() { }
	/*package*/ Negation(INode node, org.meta_environment.rascal.ast.Expression argument) {
		this.node = node;
		this.argument = argument;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitExpressionNegation(this);
	}

	public boolean isNegation() { return true; }

	public boolean hasArgument() { return true; }

private org.meta_environment.rascal.ast.Expression argument;
	public org.meta_environment.rascal.ast.Expression getArgument() { return argument; }
	private void $setArgument(org.meta_environment.rascal.ast.Expression x) { this.argument = x; }
	public Negation setArgument(org.meta_environment.rascal.ast.Expression x) { 
		Negation z = new Negation();
 		z.$setArgument(x);
		return z;
	}	
} public boolean isNegative() { return false; }
static public class Negative extends Expression {
/* "-" argument:Expression -> Expression {cons("Negative")} */
	private Negative() { }
	/*package*/ Negative(INode node, org.meta_environment.rascal.ast.Expression argument) {
		this.node = node;
		this.argument = argument;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitExpressionNegative(this);
	}

	public boolean isNegative() { return true; }

	public boolean hasArgument() { return true; }

private org.meta_environment.rascal.ast.Expression argument;
	public org.meta_environment.rascal.ast.Expression getArgument() { return argument; }
	private void $setArgument(org.meta_environment.rascal.ast.Expression x) { this.argument = x; }
	public Negative setArgument(org.meta_environment.rascal.ast.Expression x) { 
		Negative z = new Negative();
 		z.$setArgument(x);
		return z;
	}	
} public boolean isTransitiveReflexiveClosure() { return false; }
static public class TransitiveReflexiveClosure extends Expression {
/* argument:Expression "*" -> Expression {cons("TransitiveReflexiveClosure")} */
	private TransitiveReflexiveClosure() { }
	/*package*/ TransitiveReflexiveClosure(INode node, org.meta_environment.rascal.ast.Expression argument) {
		this.node = node;
		this.argument = argument;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitExpressionTransitiveReflexiveClosure(this);
	}

	public boolean isTransitiveReflexiveClosure() { return true; }

	public boolean hasArgument() { return true; }

private org.meta_environment.rascal.ast.Expression argument;
	public org.meta_environment.rascal.ast.Expression getArgument() { return argument; }
	private void $setArgument(org.meta_environment.rascal.ast.Expression x) { this.argument = x; }
	public TransitiveReflexiveClosure setArgument(org.meta_environment.rascal.ast.Expression x) { 
		TransitiveReflexiveClosure z = new TransitiveReflexiveClosure();
 		z.$setArgument(x);
		return z;
	}	
} public boolean isTransitiveClosure() { return false; }
static public class TransitiveClosure extends Expression {
/* argument:Expression "+" -> Expression {cons("TransitiveClosure")} */
	private TransitiveClosure() { }
	/*package*/ TransitiveClosure(INode node, org.meta_environment.rascal.ast.Expression argument) {
		this.node = node;
		this.argument = argument;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitExpressionTransitiveClosure(this);
	}

	public boolean isTransitiveClosure() { return true; }

	public boolean hasArgument() { return true; }

private org.meta_environment.rascal.ast.Expression argument;
	public org.meta_environment.rascal.ast.Expression getArgument() { return argument; }
	private void $setArgument(org.meta_environment.rascal.ast.Expression x) { this.argument = x; }
	public TransitiveClosure setArgument(org.meta_environment.rascal.ast.Expression x) { 
		TransitiveClosure z = new TransitiveClosure();
 		z.$setArgument(x);
		return z;
	}	
} public boolean isGetAnnotation() { return false; }
static public class GetAnnotation extends Expression {
/* expression:Expression "@" name:Name -> Expression {cons("GetAnnotation")} */
	private GetAnnotation() { }
	/*package*/ GetAnnotation(INode node, org.meta_environment.rascal.ast.Expression expression, org.meta_environment.rascal.ast.Name name) {
		this.node = node;
		this.expression = expression;
		this.name = name;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitExpressionGetAnnotation(this);
	}

	public boolean isGetAnnotation() { return true; }

	public boolean hasExpression() { return true; }
	public boolean hasName() { return true; }

private org.meta_environment.rascal.ast.Expression expression;
	public org.meta_environment.rascal.ast.Expression getExpression() { return expression; }
	private void $setExpression(org.meta_environment.rascal.ast.Expression x) { this.expression = x; }
	public GetAnnotation setExpression(org.meta_environment.rascal.ast.Expression x) { 
		GetAnnotation z = new GetAnnotation();
 		z.$setExpression(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Name name;
	public org.meta_environment.rascal.ast.Name getName() { return name; }
	private void $setName(org.meta_environment.rascal.ast.Name x) { this.name = x; }
	public GetAnnotation setName(org.meta_environment.rascal.ast.Name x) { 
		GetAnnotation z = new GetAnnotation();
 		z.$setName(x);
		return z;
	}	
} public org.meta_environment.rascal.ast.Expression getValue() { throw new UnsupportedOperationException(); } public boolean hasValue() { return false; }
public boolean isSetAnnotation() { return false; }
static public class SetAnnotation extends Expression {
/* expression:Expression "@" "(" name:Name ":" value: Expression ")" -> Expression {cons("SetAnnotation")} */
	private SetAnnotation() { }
	/*package*/ SetAnnotation(INode node, org.meta_environment.rascal.ast.Expression expression, org.meta_environment.rascal.ast.Name name, org.meta_environment.rascal.ast.Expression value) {
		this.node = node;
		this.expression = expression;
		this.name = name;
		this.value = value;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitExpressionSetAnnotation(this);
	}

	public boolean isSetAnnotation() { return true; }

	public boolean hasExpression() { return true; }
	public boolean hasName() { return true; }
	public boolean hasValue() { return true; }

private org.meta_environment.rascal.ast.Expression expression;
	public org.meta_environment.rascal.ast.Expression getExpression() { return expression; }
	private void $setExpression(org.meta_environment.rascal.ast.Expression x) { this.expression = x; }
	public SetAnnotation setExpression(org.meta_environment.rascal.ast.Expression x) { 
		SetAnnotation z = new SetAnnotation();
 		z.$setExpression(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Name name;
	public org.meta_environment.rascal.ast.Name getName() { return name; }
	private void $setName(org.meta_environment.rascal.ast.Name x) { this.name = x; }
	public SetAnnotation setName(org.meta_environment.rascal.ast.Name x) { 
		SetAnnotation z = new SetAnnotation();
 		z.$setName(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Expression value;
	public org.meta_environment.rascal.ast.Expression getValue() { return value; }
	private void $setValue(org.meta_environment.rascal.ast.Expression x) { this.value = x; }
	public SetAnnotation setValue(org.meta_environment.rascal.ast.Expression x) { 
		SetAnnotation z = new SetAnnotation();
 		z.$setValue(x);
		return z;
	}	
} public boolean isComposition() { return false; }
static public class Composition extends Expression {
/* lhs:Expression "o" rhs:Expression -> Expression {cons("Composition"), left} */
	private Composition() { }
	/*package*/ Composition(INode node, org.meta_environment.rascal.ast.Expression lhs, org.meta_environment.rascal.ast.Expression rhs) {
		this.node = node;
		this.lhs = lhs;
		this.rhs = rhs;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitExpressionComposition(this);
	}

	public boolean isComposition() { return true; }

	public boolean hasLhs() { return true; }
	public boolean hasRhs() { return true; }

private org.meta_environment.rascal.ast.Expression lhs;
	public org.meta_environment.rascal.ast.Expression getLhs() { return lhs; }
	private void $setLhs(org.meta_environment.rascal.ast.Expression x) { this.lhs = x; }
	public Composition setLhs(org.meta_environment.rascal.ast.Expression x) { 
		Composition z = new Composition();
 		z.$setLhs(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Expression rhs;
	public org.meta_environment.rascal.ast.Expression getRhs() { return rhs; }
	private void $setRhs(org.meta_environment.rascal.ast.Expression x) { this.rhs = x; }
	public Composition setRhs(org.meta_environment.rascal.ast.Expression x) { 
		Composition z = new Composition();
 		z.$setRhs(x);
		return z;
	}	
} public boolean isProduct() { return false; }
static public class Product extends Expression {
/* lhs:Expression "*" rhs:Expression -> Expression {cons("Product"), left} */
	private Product() { }
	/*package*/ Product(INode node, org.meta_environment.rascal.ast.Expression lhs, org.meta_environment.rascal.ast.Expression rhs) {
		this.node = node;
		this.lhs = lhs;
		this.rhs = rhs;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitExpressionProduct(this);
	}

	public boolean isProduct() { return true; }

	public boolean hasLhs() { return true; }
	public boolean hasRhs() { return true; }

private org.meta_environment.rascal.ast.Expression lhs;
	public org.meta_environment.rascal.ast.Expression getLhs() { return lhs; }
	private void $setLhs(org.meta_environment.rascal.ast.Expression x) { this.lhs = x; }
	public Product setLhs(org.meta_environment.rascal.ast.Expression x) { 
		Product z = new Product();
 		z.$setLhs(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Expression rhs;
	public org.meta_environment.rascal.ast.Expression getRhs() { return rhs; }
	private void $setRhs(org.meta_environment.rascal.ast.Expression x) { this.rhs = x; }
	public Product setRhs(org.meta_environment.rascal.ast.Expression x) { 
		Product z = new Product();
 		z.$setRhs(x);
		return z;
	}	
} public boolean isDivision() { return false; }
static public class Division extends Expression {
/* lhs:Expression "/" rhs:Expression -> Expression {cons("Division"), left} */
	private Division() { }
	/*package*/ Division(INode node, org.meta_environment.rascal.ast.Expression lhs, org.meta_environment.rascal.ast.Expression rhs) {
		this.node = node;
		this.lhs = lhs;
		this.rhs = rhs;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitExpressionDivision(this);
	}

	public boolean isDivision() { return true; }

	public boolean hasLhs() { return true; }
	public boolean hasRhs() { return true; }

private org.meta_environment.rascal.ast.Expression lhs;
	public org.meta_environment.rascal.ast.Expression getLhs() { return lhs; }
	private void $setLhs(org.meta_environment.rascal.ast.Expression x) { this.lhs = x; }
	public Division setLhs(org.meta_environment.rascal.ast.Expression x) { 
		Division z = new Division();
 		z.$setLhs(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Expression rhs;
	public org.meta_environment.rascal.ast.Expression getRhs() { return rhs; }
	private void $setRhs(org.meta_environment.rascal.ast.Expression x) { this.rhs = x; }
	public Division setRhs(org.meta_environment.rascal.ast.Expression x) { 
		Division z = new Division();
 		z.$setRhs(x);
		return z;
	}	
} public boolean isModulo() { return false; }
static public class Modulo extends Expression {
/* lhs:Expression "%" rhs:Expression -> Expression {cons("Modulo"), left} */
	private Modulo() { }
	/*package*/ Modulo(INode node, org.meta_environment.rascal.ast.Expression lhs, org.meta_environment.rascal.ast.Expression rhs) {
		this.node = node;
		this.lhs = lhs;
		this.rhs = rhs;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitExpressionModulo(this);
	}

	public boolean isModulo() { return true; }

	public boolean hasLhs() { return true; }
	public boolean hasRhs() { return true; }

private org.meta_environment.rascal.ast.Expression lhs;
	public org.meta_environment.rascal.ast.Expression getLhs() { return lhs; }
	private void $setLhs(org.meta_environment.rascal.ast.Expression x) { this.lhs = x; }
	public Modulo setLhs(org.meta_environment.rascal.ast.Expression x) { 
		Modulo z = new Modulo();
 		z.$setLhs(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Expression rhs;
	public org.meta_environment.rascal.ast.Expression getRhs() { return rhs; }
	private void $setRhs(org.meta_environment.rascal.ast.Expression x) { this.rhs = x; }
	public Modulo setRhs(org.meta_environment.rascal.ast.Expression x) { 
		Modulo z = new Modulo();
 		z.$setRhs(x);
		return z;
	}	
} public boolean isIntersection() { return false; }
static public class Intersection extends Expression {
/* lhs:Expression "&" rhs:Expression -> Expression {cons("Intersection"), left} */
	private Intersection() { }
	/*package*/ Intersection(INode node, org.meta_environment.rascal.ast.Expression lhs, org.meta_environment.rascal.ast.Expression rhs) {
		this.node = node;
		this.lhs = lhs;
		this.rhs = rhs;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitExpressionIntersection(this);
	}

	public boolean isIntersection() { return true; }

	public boolean hasLhs() { return true; }
	public boolean hasRhs() { return true; }

private org.meta_environment.rascal.ast.Expression lhs;
	public org.meta_environment.rascal.ast.Expression getLhs() { return lhs; }
	private void $setLhs(org.meta_environment.rascal.ast.Expression x) { this.lhs = x; }
	public Intersection setLhs(org.meta_environment.rascal.ast.Expression x) { 
		Intersection z = new Intersection();
 		z.$setLhs(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Expression rhs;
	public org.meta_environment.rascal.ast.Expression getRhs() { return rhs; }
	private void $setRhs(org.meta_environment.rascal.ast.Expression x) { this.rhs = x; }
	public Intersection setRhs(org.meta_environment.rascal.ast.Expression x) { 
		Intersection z = new Intersection();
 		z.$setRhs(x);
		return z;
	}	
} public boolean isAddition() { return false; }
static public class Addition extends Expression {
/* lhs:Expression "+" rhs:Expression -> Expression {cons("Addition"), left} */
	private Addition() { }
	/*package*/ Addition(INode node, org.meta_environment.rascal.ast.Expression lhs, org.meta_environment.rascal.ast.Expression rhs) {
		this.node = node;
		this.lhs = lhs;
		this.rhs = rhs;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitExpressionAddition(this);
	}

	public boolean isAddition() { return true; }

	public boolean hasLhs() { return true; }
	public boolean hasRhs() { return true; }

private org.meta_environment.rascal.ast.Expression lhs;
	public org.meta_environment.rascal.ast.Expression getLhs() { return lhs; }
	private void $setLhs(org.meta_environment.rascal.ast.Expression x) { this.lhs = x; }
	public Addition setLhs(org.meta_environment.rascal.ast.Expression x) { 
		Addition z = new Addition();
 		z.$setLhs(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Expression rhs;
	public org.meta_environment.rascal.ast.Expression getRhs() { return rhs; }
	private void $setRhs(org.meta_environment.rascal.ast.Expression x) { this.rhs = x; }
	public Addition setRhs(org.meta_environment.rascal.ast.Expression x) { 
		Addition z = new Addition();
 		z.$setRhs(x);
		return z;
	}	
} public boolean isSubtraction() { return false; }
static public class Subtraction extends Expression {
/* lhs:Expression "-" rhs:Expression -> Expression {cons("Subtraction"), left} */
	private Subtraction() { }
	/*package*/ Subtraction(INode node, org.meta_environment.rascal.ast.Expression lhs, org.meta_environment.rascal.ast.Expression rhs) {
		this.node = node;
		this.lhs = lhs;
		this.rhs = rhs;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitExpressionSubtraction(this);
	}

	public boolean isSubtraction() { return true; }

	public boolean hasLhs() { return true; }
	public boolean hasRhs() { return true; }

private org.meta_environment.rascal.ast.Expression lhs;
	public org.meta_environment.rascal.ast.Expression getLhs() { return lhs; }
	private void $setLhs(org.meta_environment.rascal.ast.Expression x) { this.lhs = x; }
	public Subtraction setLhs(org.meta_environment.rascal.ast.Expression x) { 
		Subtraction z = new Subtraction();
 		z.$setLhs(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Expression rhs;
	public org.meta_environment.rascal.ast.Expression getRhs() { return rhs; }
	private void $setRhs(org.meta_environment.rascal.ast.Expression x) { this.rhs = x; }
	public Subtraction setRhs(org.meta_environment.rascal.ast.Expression x) { 
		Subtraction z = new Subtraction();
 		z.$setRhs(x);
		return z;
	}	
} public boolean isNotIn() { return false; }
static public class NotIn extends Expression {
/* lhs:Expression "notin" rhs:Expression -> Expression {non-assoc, cons("NotIn")} */
	private NotIn() { }
	/*package*/ NotIn(INode node, org.meta_environment.rascal.ast.Expression lhs, org.meta_environment.rascal.ast.Expression rhs) {
		this.node = node;
		this.lhs = lhs;
		this.rhs = rhs;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitExpressionNotIn(this);
	}

	public boolean isNotIn() { return true; }

	public boolean hasLhs() { return true; }
	public boolean hasRhs() { return true; }

private org.meta_environment.rascal.ast.Expression lhs;
	public org.meta_environment.rascal.ast.Expression getLhs() { return lhs; }
	private void $setLhs(org.meta_environment.rascal.ast.Expression x) { this.lhs = x; }
	public NotIn setLhs(org.meta_environment.rascal.ast.Expression x) { 
		NotIn z = new NotIn();
 		z.$setLhs(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Expression rhs;
	public org.meta_environment.rascal.ast.Expression getRhs() { return rhs; }
	private void $setRhs(org.meta_environment.rascal.ast.Expression x) { this.rhs = x; }
	public NotIn setRhs(org.meta_environment.rascal.ast.Expression x) { 
		NotIn z = new NotIn();
 		z.$setRhs(x);
		return z;
	}	
} public boolean isIn() { return false; }
static public class In extends Expression {
/* lhs:Expression "in" rhs:Expression -> Expression {non-assoc, cons("In")} */
	private In() { }
	/*package*/ In(INode node, org.meta_environment.rascal.ast.Expression lhs, org.meta_environment.rascal.ast.Expression rhs) {
		this.node = node;
		this.lhs = lhs;
		this.rhs = rhs;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitExpressionIn(this);
	}

	public boolean isIn() { return true; }

	public boolean hasLhs() { return true; }
	public boolean hasRhs() { return true; }

private org.meta_environment.rascal.ast.Expression lhs;
	public org.meta_environment.rascal.ast.Expression getLhs() { return lhs; }
	private void $setLhs(org.meta_environment.rascal.ast.Expression x) { this.lhs = x; }
	public In setLhs(org.meta_environment.rascal.ast.Expression x) { 
		In z = new In();
 		z.$setLhs(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Expression rhs;
	public org.meta_environment.rascal.ast.Expression getRhs() { return rhs; }
	private void $setRhs(org.meta_environment.rascal.ast.Expression x) { this.rhs = x; }
	public In setRhs(org.meta_environment.rascal.ast.Expression x) { 
		In z = new In();
 		z.$setRhs(x);
		return z;
	}	
} public boolean isIfDefined() { return false; }
static public class IfDefined extends Expression {
/* lhs:Expression "=?" rhs:Expression -> Expression {left, cons("IfDefined")} */
	private IfDefined() { }
	/*package*/ IfDefined(INode node, org.meta_environment.rascal.ast.Expression lhs, org.meta_environment.rascal.ast.Expression rhs) {
		this.node = node;
		this.lhs = lhs;
		this.rhs = rhs;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitExpressionIfDefined(this);
	}

	public boolean isIfDefined() { return true; }

	public boolean hasLhs() { return true; }
	public boolean hasRhs() { return true; }

private org.meta_environment.rascal.ast.Expression lhs;
	public org.meta_environment.rascal.ast.Expression getLhs() { return lhs; }
	private void $setLhs(org.meta_environment.rascal.ast.Expression x) { this.lhs = x; }
	public IfDefined setLhs(org.meta_environment.rascal.ast.Expression x) { 
		IfDefined z = new IfDefined();
 		z.$setLhs(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Expression rhs;
	public org.meta_environment.rascal.ast.Expression getRhs() { return rhs; }
	private void $setRhs(org.meta_environment.rascal.ast.Expression x) { this.rhs = x; }
	public IfDefined setRhs(org.meta_environment.rascal.ast.Expression x) { 
		IfDefined z = new IfDefined();
 		z.$setRhs(x);
		return z;
	}	
} public boolean isLessThan() { return false; }
static public class LessThan extends Expression {
/* lhs:Expression "<" rhs:Expression -> Expression {non-assoc, cons("LessThan")} */
	private LessThan() { }
	/*package*/ LessThan(INode node, org.meta_environment.rascal.ast.Expression lhs, org.meta_environment.rascal.ast.Expression rhs) {
		this.node = node;
		this.lhs = lhs;
		this.rhs = rhs;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitExpressionLessThan(this);
	}

	public boolean isLessThan() { return true; }

	public boolean hasLhs() { return true; }
	public boolean hasRhs() { return true; }

private org.meta_environment.rascal.ast.Expression lhs;
	public org.meta_environment.rascal.ast.Expression getLhs() { return lhs; }
	private void $setLhs(org.meta_environment.rascal.ast.Expression x) { this.lhs = x; }
	public LessThan setLhs(org.meta_environment.rascal.ast.Expression x) { 
		LessThan z = new LessThan();
 		z.$setLhs(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Expression rhs;
	public org.meta_environment.rascal.ast.Expression getRhs() { return rhs; }
	private void $setRhs(org.meta_environment.rascal.ast.Expression x) { this.rhs = x; }
	public LessThan setRhs(org.meta_environment.rascal.ast.Expression x) { 
		LessThan z = new LessThan();
 		z.$setRhs(x);
		return z;
	}	
} public boolean isLessThanOrEq() { return false; }
static public class LessThanOrEq extends Expression {
/* lhs:Expression "<=" rhs:Expression -> Expression {non-assoc, cons("LessThanOrEq")} */
	private LessThanOrEq() { }
	/*package*/ LessThanOrEq(INode node, org.meta_environment.rascal.ast.Expression lhs, org.meta_environment.rascal.ast.Expression rhs) {
		this.node = node;
		this.lhs = lhs;
		this.rhs = rhs;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitExpressionLessThanOrEq(this);
	}

	public boolean isLessThanOrEq() { return true; }

	public boolean hasLhs() { return true; }
	public boolean hasRhs() { return true; }

private org.meta_environment.rascal.ast.Expression lhs;
	public org.meta_environment.rascal.ast.Expression getLhs() { return lhs; }
	private void $setLhs(org.meta_environment.rascal.ast.Expression x) { this.lhs = x; }
	public LessThanOrEq setLhs(org.meta_environment.rascal.ast.Expression x) { 
		LessThanOrEq z = new LessThanOrEq();
 		z.$setLhs(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Expression rhs;
	public org.meta_environment.rascal.ast.Expression getRhs() { return rhs; }
	private void $setRhs(org.meta_environment.rascal.ast.Expression x) { this.rhs = x; }
	public LessThanOrEq setRhs(org.meta_environment.rascal.ast.Expression x) { 
		LessThanOrEq z = new LessThanOrEq();
 		z.$setRhs(x);
		return z;
	}	
} public boolean isGreaterThan() { return false; }
static public class GreaterThan extends Expression {
/* lhs:Expression ">" rhs:Expression -> Expression {non-assoc, cons("GreaterThan")} */
	private GreaterThan() { }
	/*package*/ GreaterThan(INode node, org.meta_environment.rascal.ast.Expression lhs, org.meta_environment.rascal.ast.Expression rhs) {
		this.node = node;
		this.lhs = lhs;
		this.rhs = rhs;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitExpressionGreaterThan(this);
	}

	public boolean isGreaterThan() { return true; }

	public boolean hasLhs() { return true; }
	public boolean hasRhs() { return true; }

private org.meta_environment.rascal.ast.Expression lhs;
	public org.meta_environment.rascal.ast.Expression getLhs() { return lhs; }
	private void $setLhs(org.meta_environment.rascal.ast.Expression x) { this.lhs = x; }
	public GreaterThan setLhs(org.meta_environment.rascal.ast.Expression x) { 
		GreaterThan z = new GreaterThan();
 		z.$setLhs(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Expression rhs;
	public org.meta_environment.rascal.ast.Expression getRhs() { return rhs; }
	private void $setRhs(org.meta_environment.rascal.ast.Expression x) { this.rhs = x; }
	public GreaterThan setRhs(org.meta_environment.rascal.ast.Expression x) { 
		GreaterThan z = new GreaterThan();
 		z.$setRhs(x);
		return z;
	}	
} public boolean isGreaterThanOrEq() { return false; }
static public class GreaterThanOrEq extends Expression {
/* lhs:Expression ">=" rhs:Expression -> Expression {non-assoc, cons("GreaterThanOrEq")} */
	private GreaterThanOrEq() { }
	/*package*/ GreaterThanOrEq(INode node, org.meta_environment.rascal.ast.Expression lhs, org.meta_environment.rascal.ast.Expression rhs) {
		this.node = node;
		this.lhs = lhs;
		this.rhs = rhs;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitExpressionGreaterThanOrEq(this);
	}

	public boolean isGreaterThanOrEq() { return true; }

	public boolean hasLhs() { return true; }
	public boolean hasRhs() { return true; }

private org.meta_environment.rascal.ast.Expression lhs;
	public org.meta_environment.rascal.ast.Expression getLhs() { return lhs; }
	private void $setLhs(org.meta_environment.rascal.ast.Expression x) { this.lhs = x; }
	public GreaterThanOrEq setLhs(org.meta_environment.rascal.ast.Expression x) { 
		GreaterThanOrEq z = new GreaterThanOrEq();
 		z.$setLhs(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Expression rhs;
	public org.meta_environment.rascal.ast.Expression getRhs() { return rhs; }
	private void $setRhs(org.meta_environment.rascal.ast.Expression x) { this.rhs = x; }
	public GreaterThanOrEq setRhs(org.meta_environment.rascal.ast.Expression x) { 
		GreaterThanOrEq z = new GreaterThanOrEq();
 		z.$setRhs(x);
		return z;
	}	
} public boolean isNonEquals() { return false; }
static public class NonEquals extends Expression {
/* lhs:Expression "!=" rhs:Expression -> Expression {left, cons("NonEquals")} */
	private NonEquals() { }
	/*package*/ NonEquals(INode node, org.meta_environment.rascal.ast.Expression lhs, org.meta_environment.rascal.ast.Expression rhs) {
		this.node = node;
		this.lhs = lhs;
		this.rhs = rhs;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitExpressionNonEquals(this);
	}

	public boolean isNonEquals() { return true; }

	public boolean hasLhs() { return true; }
	public boolean hasRhs() { return true; }

private org.meta_environment.rascal.ast.Expression lhs;
	public org.meta_environment.rascal.ast.Expression getLhs() { return lhs; }
	private void $setLhs(org.meta_environment.rascal.ast.Expression x) { this.lhs = x; }
	public NonEquals setLhs(org.meta_environment.rascal.ast.Expression x) { 
		NonEquals z = new NonEquals();
 		z.$setLhs(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Expression rhs;
	public org.meta_environment.rascal.ast.Expression getRhs() { return rhs; }
	private void $setRhs(org.meta_environment.rascal.ast.Expression x) { this.rhs = x; }
	public NonEquals setRhs(org.meta_environment.rascal.ast.Expression x) { 
		NonEquals z = new NonEquals();
 		z.$setRhs(x);
		return z;
	}	
} public boolean isImplication() { return false; }
static public class Implication extends Expression {
/* lhs:Expression "==>" rhs:Expression -> Expression {right, cons("Implication")} */
	private Implication() { }
	/*package*/ Implication(INode node, org.meta_environment.rascal.ast.Expression lhs, org.meta_environment.rascal.ast.Expression rhs) {
		this.node = node;
		this.lhs = lhs;
		this.rhs = rhs;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitExpressionImplication(this);
	}

	public boolean isImplication() { return true; }

	public boolean hasLhs() { return true; }
	public boolean hasRhs() { return true; }

private org.meta_environment.rascal.ast.Expression lhs;
	public org.meta_environment.rascal.ast.Expression getLhs() { return lhs; }
	private void $setLhs(org.meta_environment.rascal.ast.Expression x) { this.lhs = x; }
	public Implication setLhs(org.meta_environment.rascal.ast.Expression x) { 
		Implication z = new Implication();
 		z.$setLhs(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Expression rhs;
	public org.meta_environment.rascal.ast.Expression getRhs() { return rhs; }
	private void $setRhs(org.meta_environment.rascal.ast.Expression x) { this.rhs = x; }
	public Implication setRhs(org.meta_environment.rascal.ast.Expression x) { 
		Implication z = new Implication();
 		z.$setRhs(x);
		return z;
	}	
} public boolean isEquivalence() { return false; }
static public class Equivalence extends Expression {
/* lhs:Expression "<==>" rhs:Expression -> Expression {right, cons("Equivalence")} */
	private Equivalence() { }
	/*package*/ Equivalence(INode node, org.meta_environment.rascal.ast.Expression lhs, org.meta_environment.rascal.ast.Expression rhs) {
		this.node = node;
		this.lhs = lhs;
		this.rhs = rhs;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitExpressionEquivalence(this);
	}

	public boolean isEquivalence() { return true; }

	public boolean hasLhs() { return true; }
	public boolean hasRhs() { return true; }

private org.meta_environment.rascal.ast.Expression lhs;
	public org.meta_environment.rascal.ast.Expression getLhs() { return lhs; }
	private void $setLhs(org.meta_environment.rascal.ast.Expression x) { this.lhs = x; }
	public Equivalence setLhs(org.meta_environment.rascal.ast.Expression x) { 
		Equivalence z = new Equivalence();
 		z.$setLhs(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Expression rhs;
	public org.meta_environment.rascal.ast.Expression getRhs() { return rhs; }
	private void $setRhs(org.meta_environment.rascal.ast.Expression x) { this.rhs = x; }
	public Equivalence setRhs(org.meta_environment.rascal.ast.Expression x) { 
		Equivalence z = new Equivalence();
 		z.$setRhs(x);
		return z;
	}	
} public boolean isAnd() { return false; }
static public class And extends Expression {
/* lhs:Expression "&&" rhs:Expression -> Expression {left, cons("And")} */
	private And() { }
	/*package*/ And(INode node, org.meta_environment.rascal.ast.Expression lhs, org.meta_environment.rascal.ast.Expression rhs) {
		this.node = node;
		this.lhs = lhs;
		this.rhs = rhs;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitExpressionAnd(this);
	}

	public boolean isAnd() { return true; }

	public boolean hasLhs() { return true; }
	public boolean hasRhs() { return true; }

private org.meta_environment.rascal.ast.Expression lhs;
	public org.meta_environment.rascal.ast.Expression getLhs() { return lhs; }
	private void $setLhs(org.meta_environment.rascal.ast.Expression x) { this.lhs = x; }
	public And setLhs(org.meta_environment.rascal.ast.Expression x) { 
		And z = new And();
 		z.$setLhs(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Expression rhs;
	public org.meta_environment.rascal.ast.Expression getRhs() { return rhs; }
	private void $setRhs(org.meta_environment.rascal.ast.Expression x) { this.rhs = x; }
	public And setRhs(org.meta_environment.rascal.ast.Expression x) { 
		And z = new And();
 		z.$setRhs(x);
		return z;
	}	
} public boolean isOr() { return false; }
static public class Or extends Expression {
/* lhs:Expression "||" rhs:Expression -> Expression {left, cons("Or")} */
	private Or() { }
	/*package*/ Or(INode node, org.meta_environment.rascal.ast.Expression lhs, org.meta_environment.rascal.ast.Expression rhs) {
		this.node = node;
		this.lhs = lhs;
		this.rhs = rhs;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitExpressionOr(this);
	}

	public boolean isOr() { return true; }

	public boolean hasLhs() { return true; }
	public boolean hasRhs() { return true; }

private org.meta_environment.rascal.ast.Expression lhs;
	public org.meta_environment.rascal.ast.Expression getLhs() { return lhs; }
	private void $setLhs(org.meta_environment.rascal.ast.Expression x) { this.lhs = x; }
	public Or setLhs(org.meta_environment.rascal.ast.Expression x) { 
		Or z = new Or();
 		z.$setLhs(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Expression rhs;
	public org.meta_environment.rascal.ast.Expression getRhs() { return rhs; }
	private void $setRhs(org.meta_environment.rascal.ast.Expression x) { this.rhs = x; }
	public Or setRhs(org.meta_environment.rascal.ast.Expression x) { 
		Or z = new Or();
 		z.$setRhs(x);
		return z;
	}	
} static public class Lexical extends Expression {
	private String string;
	/*package*/ Lexical(INode node, String string) {
		this.node = node;
		this.string = string;
	}
	public String getString() {
		return string;
	}

 	public <T> T accept(IASTVisitor<T> v) {
     		return v.visitExpressionLexical(this);
  	}
}
}