module analysis::m3::ConvertToOldAST

import lang::java::jdt::JDT;
import analysis::m3::JDT;
import analysis::m3::AST;
extend lang::java::jdt::JavaADT;
import util::ValueUI;
import lang::java::jdt::Java;
import List;
import IO;
import Node;
import String;
import Set;
import ValueIO;

data AstNode = notProcessedYet();

private set[loc] crawl(loc dir, str suffix) {
	res = {};
  	for(str entry <- listEntries(dir)){
    	loc sub = dir + entry;   
      	if(isDirectory(sub)) {
        	res += crawl(sub, suffix);
      	} else {
          	if(endsWith(entry, suffix)) { 
            	res += {sub}; 
			}
		}
	};
	return res;
}

public bool testAstForFile(loc file, bool debug = true) {
	try AstNode oldAST = lang::java::jdt::JDT::createAstFromFile(file, false);
	catch : {println("Couldn\'t work with file: <file>\nMoving on..."); return true;}
	Declaration cuNewJDT = analysis::m3::JDT::createAstFromFile(file, false);
	
	AstNode transformedAST = transform(cuNewJDT);
	AstNode tm = visit(oldAST) {
		case AstNode n => delAnnotations(n)
	};
	
	str tAST = toString(transformedAST);
	str oAST = toString(tm);
	
	//text(transformedAST);
	//text(tm);
	
	for (int i <- [0..size(tAST)])
		if (tAST[i] != oAST[i]) {
			if (debug) {
				println("Character: <i>");
				try println("got <tAST[i]> should have been <oAST[i]>");
				catch: "Index out of bounds";
			}
		}
	
	return transformedAST == tm;
}

public bool testAstForProject(loc project) {
println("Testing for project : <project.authority>");
	list[bool] result = [];
	set[loc] readProjects = {};
	readProjects = readTextValueFile(#set[loc], |home:///Desktop/okayFiles.txt|);
	if (project in readProjects)
		return true;
	set[loc] files = crawl(project, ".java");
	if (size(files) > 0) {
		for (loc f <- files) {
			b = testAstForFile(f, debug = false);
			if (b) {
				result += b;
			}
			else {
			println(f);
				throw "not continuing";
				}
		}
	} else {
		println("no files to check");
	}
	readProjects += project;
	writeTextValueFile(|home:///Desktop/okayFiles.txt|, readProjects);
	return all(bool n <- result);
}

@javaClass{org.rascalmpl.library.experiments.m3.internal.JDT}
public java set[loc] getProjects();

public void testAstForAll() {
	for(loc project <- getProjects())
		testAstForProject(project);
}

private AstNode transform(Declaration d: \compilationUnit(list[Declaration] imports, list[Declaration] types)) =
	compilationUnit(none(), transform(imports), transform(types));

private AstNode transform(Declaration d: \compilationUnit(Declaration package, list[Declaration] imports, list[Declaration] types)) =
	compilationUnit(some(transform(package)), transform(imports), transform(types));
	
private AstNode transform(Declaration d: \enum(str name, list[Type] implements, list[Declaration] constants, list[Declaration] body)) =
	enumDeclaration(d@modifiers? ? transformModifier(d@modifiers) : [], d@modifiers? ? transformAnno(d@modifiers) : [], name, transform(implements), transform(constants), transform(body));

private AstNode transform(Declaration d: \enumConstant(str name, list[Expression] arguments, Declaration class)) =
	enumConstantDeclaration(d@modifiers? ? transformModifier(d@modifiers) : [], d@modifiers? ? transformAnno(d@modifiers) : [], name, transform(arguments), some(transform(class)));
	
private AstNode transform(Declaration d: \enumConstant(str name, list[Expression] arguments)) =
	enumConstantDeclaration(d@modifiers? ? transformModifier(d@modifiers) : [], d@modifiers? ? transformAnno(d@modifiers) : [], name, transform(arguments), none());
	
private AstNode transform(Declaration d: \class(str name, list[Type] extends, list[Type] implements, list[Declaration] body)) = 
	typeDeclaration(d@modifiers? ? transformModifier(d@modifiers) : [], d@modifiers? ? transformAnno(d@modifiers) : [], "class", name, d@typeParameters? ? transform(d@typeParameters) : [], extends == [] ? none() : some(transform(head(extends))), transform(implements), transform(body));

private AstNode transform(Declaration d: \class(list[Declaration] body)) =
	anonymousClassDeclaration(transform(body));

private AstNode transform(Declaration d: \interface(str name, list[Type] extends, list[Type] implements, list[Declaration] body)) = 
	typeDeclaration(d@modifiers? ? transformModifier(d@modifiers) : [], d@modifiers? ? transformAnno(d@modifiers) : [], "interface", name, d@typeParameters? ? transform(d@typeParameters) : [], extends == [] ? none() : some(transform(head(extends))), transform(implements), transform(body));

private AstNode transform(Declaration d: \field(Type \type, list[Expression] fragments)) =
	fieldDeclaration(d@modifiers? ? transformModifier(d@modifiers) : [], d@modifiers? ? transformAnno(d@modifiers) : [], transform(\type), transform(fragments));
	
private AstNode transform(Declaration d: \initializer(Statement initializerBody)) =
	initializer(d@modifiers? ? transformModifier(d@modifiers) : [], d@modifiers? ? transformAnno(d@modifiers) : [], transform(initializerBody));
	
private AstNode transform(Declaration d: \method(Type \return, str name, list[Declaration] parameters, list[Expression] exceptions, Statement impl)) =
	methodDeclaration(d@modifiers? ? transformModifier(d@modifiers) : [], d@modifiers? ? transformAnno(d@modifiers) : [], d@typeParameters? ? transform(d@typeParameters) : [], some(transform(\return)), name, transform(parameters), transform(exceptions), some(transform(impl)));

private AstNode transform(Declaration d: \method(Type \return, str name, list[Declaration] parameters, list[Expression] exceptions)) =
	methodDeclaration(d@modifiers? ? transformModifier(d@modifiers) : [], d@modifiers? ? transformAnno(d@modifiers) : [], d@typeParameters? ? transform(d@typeParameters) : [], some(transform(\return)), name, transform(parameters), transform(exceptions), none());
	
private AstNode transform(Declaration d: \method(str name, list[Declaration] parameters, list[Expression] exceptions, Statement impl)) =
	methodDeclaration(d@modifiers? ? transformModifier(d@modifiers) : [], d@modifiers? ? transformAnno(d@modifiers) : [], d@typeParameters? ? transform(d@typeParameters) : [], none(), name, transform(parameters), transform(exceptions), some(transform(impl)));
	
private AstNode transform(Declaration d: \import(str name)) =
		importDeclaration(name, d@modifiers? ? Modifiers::\static() in d@modifiers : false, d@modifiers? ? \onDemand() in d@modifiers : false);

private AstNode transform(Declaration d: \package(str name)) =
	packageDeclaration(name, d@modifiers? ? transformAnno(d@modifiers) : []);
	
private AstNode transform(Declaration d: \typeParameter(str name, list[Type] extendsList)) =
	typeParameter(name, transform(extendsList));
	
private AstNode transform(Declaration d: \annotationType(str name, list[Declaration] body)) =
	annotationTypeDeclaration(d@modifiers? ? transformModifier(d@modifiers) : [], d@modifiers? ? transformAnno(d@modifiers) : [], name, transform(body));

private AstNode transform(Declaration d: \annotationTypeMember(Type \type, str name)) =
	annotationTypeMemberDeclaration(d@modifiers? ? transformModifier(d@modifiers) : [], d@modifiers? ? transformAnno(d@modifiers) : [], transform(\type), name, none());

private AstNode transform(Declaration d: \annotationTypeMember(Type \type, str name, Expression defaultBlock)) =
	annotationTypeMemberDeclaration(d@modifiers? ? transformModifier(d@modifiers) : [], d@modifiers? ? transformAnno(d@modifiers) : [], transform(\type), name, some(transform(defaultBlock)));
	
private AstNode transform(Declaration d: \parameter(Type \type, str name, int extraDimensions)) = 
	singleVariableDeclaration(name, d@modifiers? ? transformModifier(d@modifiers) : [], d@modifiers? ? transformAnno(d@modifiers) : [], transform(\type), none(), false);
	
private AstNode transform(Declaration d: \vararg(Type \type, str name)) =
	singleVariableDeclaration(name, d@modifiers? ? transformModifier(d@modifiers) : [], d@modifiers? ? transformAnno(d@modifiers) : [], transform(\type), none(), true);
	
private AstNode transform(Expression e: \arrayAccess(Expression array, Expression index)) =
	arrayAccess(transform(array), transform(index));
	
private AstNode transform(Expression e: \newArray(Type \type, list[Expression] dimensions, Expression init)) =
	arrayCreation(transform(\type), transform(dimensions), some(transform(init)));

private AstNode transform(Expression e: \newArray(Type \type, list[Expression] dimensions)) =
	arrayCreation(transform(\type), transform(dimensions), none());

private AstNode transform(Expression e: \arrayInitializer(list[Expression] elements)) =
	arrayInitializer(transform(elements));
	
private AstNode transform(Expression e: \assignment(Expression lhs, str operator, Expression rhs)) =
	assignment(transform(lhs), transform(rhs));
	
private AstNode transform(Expression e: \cast(Type \type, Expression expression)) =
	castExpression(transform(\type), transform(expression));

private AstNode transform(Expression e: \char(str charValue)) =
	characterLiteral(charValue);

private AstNode transform(Expression e: \newObject(Expression expr, Type \type, list[Expression] args, Declaration class)) =
	classInstanceCreation(some(transform(expr)), transform(\type), e@typeParameters? ? transform(e@typeParameters) : [], transform(args), some(transform(class)));
	
private AstNode transform(Expression e: \newObject(Expression expr, Type \type, list[Expression] args)) =
	classInstanceCreation(some(transform(expr)), transform(\type), e@typeParameters? ? transform(e@typeParameters) : [], transform(args), none());
	
private AstNode transform(Expression e: \newObject(Type \type, list[Expression] args, Declaration class)) =
	classInstanceCreation(none(), transform(\type), e@typeParameters? ? transform(e@typeParameters) : [], transform(args), some(transform(class)));
	
private AstNode transform(Expression e: \newObject(Type \type, list[Expression] args)) =
	classInstanceCreation(none(), transform(\type), e@typeParameters? ? transform(e@typeParameters) : [], transform(args), none());
	
private AstNode transform(Expression e: \conditional(Expression expression, Expression thenBranch, Expression elseBranch)) =
	conditionalExpression(transform(expression), transform(thenBranch), transform(elseBranch));
	
private AstNode transform(Expression e: \fieldAccess(false, Expression expression, str name)) =
	fieldAccess(transform(expression), name);

private AstNode transform(Expression e: \fieldAccess(false, str name)) =
	notProcessedYet();
	
private AstNode transform(Expression e: \fieldAccess(true, Expression expression, str name)) =
	superFieldAccess(some(transform(expression)), name);

private AstNode transform(Expression e: \fieldAccess(true, str name)) =
	superFieldAccess(none(), name);

private AstNode transform(Expression e: \instanceof(Expression leftSide, Type rightSide)) =
	instanceofExpression(transform(leftSide), transform(rightSide));

private AstNode transform(Expression e: \methodCall(false, str name, list[Expression] arguments)) =
	methodInvocation(none(), e@typeParameters? ? transform(e@typeParameters) : [], name, transform(arguments));

private AstNode transform(Expression e: \methodCall(false, Expression receiver, str name, list[Expression] arguments)) =
	methodInvocation(some(transform(receiver)), e@typeParameters? ? transform(e@typeParameters) : [], name, transform(arguments));

private AstNode transform(Expression e: \methodCall(true, str name, list[Expression] arguments)) =
	superMethodInvocation(none(), e@typeParameters? ? transform(e@typeParameters) : [], name, transform(arguments));

private AstNode transform(Expression e: \methodCall(true, Expression receiver, str name, list[Expression] arguments)) =
	superMethodInvocation(some(transform(receiver)), e@typeParameters? ? transform(e@typeParameters) : [], name, transform(arguments));

private AstNode transform(Expression e: \null()) =
	nullLiteral();

private AstNode transform(Expression e: \number(str numberValue)) =
	numberLiteral(numberValue);
	
private AstNode transform(Expression e: \boolean(bool boolValue)) =
	booleanLiteral(boolValue);
	
private AstNode transform(Expression e: \string(str stringValue)) =
	stringLiteral(stringValue);

private AstNode transform(Expression e: \type(Type \type)) =
	typeLiteral(transform(\type));
	
private AstNode transform(Expression e: \variable(str name, int extraDimensions)) =
	variableDeclarationFragment(name, none());
	
private AstNode transform(Expression e: \variable(str name, int extraDimensions, Expression initializer)) =
	variableDeclarationFragment(name, some(transform(initializer)));
	
private AstNode transform(Expression e: \bracket(Expression expression)) =
	parenthesizedExpression(transform(expression));
	
private AstNode transform(Expression e: \this()) =
	thisExpression(none());

private AstNode transform(Expression e: \this(Expression \this)) =
	thisExpression(some(transform(\this)));

// Doesn't take expression for some reason
private AstNode transform(Expression e: \declaration(Declaration d: \variables(Type \type, *\variable))) =
	variableDeclarationExpression(d@modifiers? ? transformModifier(d@modifiers) : [], d@modifiers? ? transformAnno(d@modifiers) : [], transform(\type), transform(\variable));

private AstNode transform(Expression e: \variable(str name, int extraDimensions)) =
	variableDeclarationFragment(name, none());
	
private AstNode transform(Expression e: \variable(str name, int extraDimensions, Expression initializer)) =
	variableDeclarationFragment(name, some(transform(initializer)));

private AstNode transform(Expression e: \infix(Expression lhs, str operator, Expression rhs, list[Expression] extendedOperands)) =
	infixExpression(operator, transform(lhs), transform(rhs), transform(extendedOperands));

private AstNode transform(Expression e: \postfix(Expression operand, str operator)) =
	postfixExpression(transform(operand), operator);
	
private AstNode transform(Expression e: \prefix(str operator, Expression operand)) =
	prefixExpression(transform(operand), operator);
	
private AstNode transform(Expression e: \simpleName(str name)) =
	simpleName(name);

private AstNode transform(Expression e: \qualifier(Expression qualified, Expression simpleName)) =
	qualifiedName(transform(qualified), simpleName.name);
	
private AstNode transform(Expression e: \markerAnnotation(str typeName)) =
	markerAnnotation(typeName);
	
private AstNode transform(Expression e: \normalAnnotation(str typeName, list[Expression] memberValuePairs)) =
	normalAnnotation(typeName, transform(memberValuePairs));
	
private AstNode transform(Expression e: \memberValuePair(str name, Expression \value)) =
	memberValuePair(name, transform(\value));
	
private AstNode transform(Expression e: \singleMemberAnnotation(str typeName, Expression \value)) =
	singleMemberAnnotation(typeName, transform(\value));
	
private AstNode transform(Statement s: \assert(Expression expression)) =
	assertStatement(transform(expression), none());
	
private AstNode transform(Statement s: \assert(Expression expression, Expression message)) =
	assertStatement(transform(expression), some(transform(message)));

private AstNode transform(Statement s: \block(list[Statement] statements)) =
	block(transform(statements));
	
private AstNode transform(Statement s: \break()) =
	breakStatement(none());

private AstNode transform(Statement s: \break(str label)) =
	breakStatement(some(label));
	
private AstNode transform(Statement s: \continue()) =
	continueStatement(none());

private AstNode transform(Statement s: \continue(str label)) =
	continueStatement(some(label));
	
private AstNode transform(Statement s: \do(Statement body, Expression condition)) =
	doStatement(transform(body), transform(condition));

private AstNode transform(Statement s: \empty()) =
	emptyStatement();

private AstNode transform(Statement s: \foreach(Declaration parameter, Expression collection, Statement body)) =
	enhancedForStatement(transform(parameter), transform(collection), transform(body));
	
private AstNode transform(Statement s: \for(list[Expression] initializers, Expression condition, list[Expression] updaters, Statement body)) =
	forStatement(transform(initializers), some(transform(condition)), transform(updaters), transform(body));

private AstNode transform(Statement s: \for(list[Expression] initializers, list[Expression] updaters, Statement body)) =
	forStatement(transform(initializers), none(), transform(updaters), transform(body));
	
private AstNode transform(Statement s: \if(Expression condition, Statement thenBranch)) =
	ifStatement(transform(condition), transform(thenBranch), none());

private AstNode transform(Statement s: \if(Expression condition, Statement thenBranch, Statement elseBranch)) =
	ifStatement(transform(condition), transform(thenBranch), some(transform(elseBranch)));
	
private AstNode transform(Statement s: \label(str name, Statement body)) =
	labeledStatement(name, transform(body));
	
private AstNode transform(Statement s: \return(Expression expression)) =
	returnStatement(some(transform(expression)));
	
private AstNode transform(Statement s: \return()) =
	returnStatement(none());
	
private AstNode transform(Statement s: \switch(Expression expression, list[Statement] statements)) =
	switchStatement(transform(expression), transform(statements));

private AstNode transform(Statement s: \case(Expression expression)) =
	switchCase(false, some(transform(expression)));

private AstNode transform(Statement s: \defaultCase()) =
	switchCase(true, none());
	
private AstNode transform(Statement s: \synchronized(Expression lock, Statement body)) =
	synchronizedStatement(transform(lock), transform(body));
	
private AstNode transform(Statement s: \throw(Expression expression)) =
	throwStatement(transform(expression));
	
private AstNode transform(Statement s: \try(Statement body, list[Statement] catchClauses)) =
	tryStatement(transform(body), transform(catchClauses), none());

private AstNode transform(Statement s: \try(Statement body, list[Statement] catchClauses, Statement \finally)) =
	tryStatement(transform(body), transform(catchClauses), some(transform(\finally)));
	
private AstNode transform(Statement s: \catch(Declaration exception, Statement body)) =
	catchClause(transform(exception), transform(body));

private AstNode transform(Statement s: \while(Expression condition, Statement body)) =
	whileStatement(transform(condition), transform(body));
	
private AstNode transform(Statement s: \expression(Expression stmt)) =
	expressionStatement(transform(stmt));

private AstNode transform(Statement s: \constructorCall(false, Expression expr, list[Expression] arguments)) =
	notProcessedYet();

private AstNode transform(Statement s: \constructorCall(false, list[Expression] arguments)) =
	constructorInvocation(s@typeParameters? ? transform(s@typeParameters) : [], transform(arguments));

private AstNode transform(Statement s: \constructorCall(true, Expression expr, list[Expression] arguments)) =
	superConstructorInvocation(some(transform(expr)), s@typeParameters? ? transform(s@typeParameters) : [], transform(arguments));
	
private AstNode transform(Statement s: \constructorCall(true, list[Expression] arguments)) =
	superConstructorInvocation(none(), s@typeParameters? ? transform(s@typeParameters) : [], transform(arguments));
	
private AstNode transform(Statement s: \declaration(Declaration d: \variables(Type \type, list[Expression] \variable))) =
	variableDeclarationStatement(d@modifiers? ? transformModifier(d@modifiers) : [], d@modifiers? ? transformAnno(d@modifiers) : [], transform(\type), transform(\variable));
	
	private AstNode transform(Statement s: \declaration(Declaration d)) =
		typeDeclarationStatement(transform(d));
	
private AstNode transform(Type t: \arrayType(Type \type)) =
	arrayType(transform(\type));
	
private AstNode transform(Type t: \parameterizedType(Type \type)) =
	parameterizedType(transform(\type), t@typeParameters? ? transform(t@typeParameters) : []);

private AstNode transform(Type t: \qualifier(Type qualifier, Expression simpleName)) =
	qualifiedType(transform(qualifier), simpleName.name);
	
private AstNode transform(Type t: \simpleType(str name)) =
	simpleType(name);
	
private AstNode transform(Type t: \unionType(list[Type] types)) =
	unionType(transform(types));
	
private AstNode transform(Type t: \wildcard()) =
	wildcardType(none(), none());
	
private AstNode transform(Type t: \upperbound(Type \type)) =
	wildcardType(some(transform(\type)), some("extends"));

private AstNode transform(Type t: \lowerbound(Type \type)) =
	wildcardType(some(transform(\type)), some("super"));
	
private AstNode transform(Type t: \int()) =
	primitiveType(\int());
	
private AstNode transform(Type t: \short()) =
	primitiveType(short());
	
private AstNode transform(Type t: \long()) =
	primitiveType(long());
	
private AstNode transform(Type t: \float()) =
	primitiveType(float());
	
private AstNode transform(Type t: \double()) =
	primitiveType(double());
	
private AstNode transform(Type t: \char()) =
	primitiveType(char());
	
//private AstNode transform(Type t: \string()) =
//	primitiveType(string());
	
private AstNode transform(Type t: \byte()) =
	primitiveType(byte());
	
private AstNode transform(Type t: \void()) =
	primitiveType(\void());
	
private AstNode transform(Type t: \boolean()) =
	primitiveType(boolean());

private default AstNode transform(Declaration d) {
	println("Declaration: <d>");
	return notProcessedYet();
}	

private default AstNode transform(Expression e) {
	println("Expression : <e>"); 
	if (e.decl? )
		transform(e.decl);
	return notProcessedYet();
}

private default AstNode transform(Statement s) {
	println("Statement: <s>"); 
	return notProcessedYet();
}

private default AstNode transform(Type t) {
	println("Type: <t>"); 
	return notProcessedYet();
}

private list[AstNode] transform(list[Declaration] d) = [transform(decl) | Declaration decl <- d];
private list[AstNode] transform(list[Expression] e) = [transform(exp) | Expression exp <- e];
private list[AstNode] transform(list[Statement] s) = [transform(stmt) | Statement stmt <- s];
private list[AstNode] transform(list[Type] t) = [transform(typ) | Type typ <- t];
private list[Modifier] transformModifier(list[Modifiers] m) = [transform(modif) | Modifiers modif <- m, !(annotation(_) := modif)];
private list[AstNode] transformAnno(list[Modifiers] m) = [transform(e) | annotation(Expression e) <- m];

private Modifier transform(Modifiers m: \public()) = \public();
private Modifier transform(Modifiers m: \private()) = \private();
private Modifier transform(Modifiers m: \protected()) = \protected();
private Modifier transform(Modifiers m: \friendly()) = \friendly();
private Modifier transform(Modifiers m: \static()) = \static();
private Modifier transform(Modifiers m: \final()) = \final();
private Modifier transform(Modifiers m: \synchronized()) = \synchronized();
private Modifier transform(Modifiers m: \transient()) = \transient();
private Modifier transform(Modifiers m: \abstract()) = \abstract();
private Modifier transform(Modifiers m: \native()) = \native();
private Modifier transform(Modifiers m: \volatile()) = \volatile();
private Modifier transform(Modifiers m: \strictfp()) = \strictfp();
private Modifier transform(Modifiers m: \deprecated()) = \deprecated();
