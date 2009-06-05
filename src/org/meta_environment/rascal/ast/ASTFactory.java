package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.INode;

public class ASTFactory {
private MappingCache<INode, Object> ambCache = new MappingCache<INode, Object>();
private MappingCache<INode, Object> sortCache = new MappingCache<INode, Object>();
private MappingCache<INode, Object> otherSortCache = new MappingCache<INode, Object>();
private MappingCache<INode, Object> lexCache = new MappingCache<INode, Object>();

public org.meta_environment.rascal.ast.Literal.String makeLiteralString(INode node, org.meta_environment.rascal.ast.StringLiteral stringLiteral) { 
org.meta_environment.rascal.ast.Literal.String x = (org.meta_environment.rascal.ast.Literal.String) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Literal.String(node, stringLiteral);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Literal.Real makeLiteralReal(INode node, org.meta_environment.rascal.ast.RealLiteral realLiteral) { 
org.meta_environment.rascal.ast.Literal.Real x = (org.meta_environment.rascal.ast.Literal.Real) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Literal.Real(node, realLiteral);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Literal.Integer makeLiteralInteger(INode node, org.meta_environment.rascal.ast.IntegerLiteral integerLiteral) { 
org.meta_environment.rascal.ast.Literal.Integer x = (org.meta_environment.rascal.ast.Literal.Integer) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Literal.Integer(node, integerLiteral);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Literal.Boolean makeLiteralBoolean(INode node, org.meta_environment.rascal.ast.BooleanLiteral booleanLiteral) { 
org.meta_environment.rascal.ast.Literal.Boolean x = (org.meta_environment.rascal.ast.Literal.Boolean) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Literal.Boolean(node, booleanLiteral);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Literal.Ambiguity makeLiteralAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Literal> alternatives) { 
org.meta_environment.rascal.ast.Literal.Ambiguity amb = (org.meta_environment.rascal.ast.Literal.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.Literal.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.Literal.RegExp makeLiteralRegExp(INode node, org.meta_environment.rascal.ast.RegExpLiteral regExpLiteral) { 
org.meta_environment.rascal.ast.Literal.RegExp x = (org.meta_environment.rascal.ast.Literal.RegExp) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Literal.RegExp(node, regExpLiteral);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Module.Ambiguity makeModuleAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Module> alternatives) { 
org.meta_environment.rascal.ast.Module.Ambiguity amb = (org.meta_environment.rascal.ast.Module.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.Module.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.Module.Default makeModuleDefault(INode node, org.meta_environment.rascal.ast.Header header, org.meta_environment.rascal.ast.Body body) { 
org.meta_environment.rascal.ast.Module.Default x = (org.meta_environment.rascal.ast.Module.Default) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Module.Default(node, header, body);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.ModuleActuals.Ambiguity makeModuleActualsAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.ModuleActuals> alternatives) { 
org.meta_environment.rascal.ast.ModuleActuals.Ambiguity amb = (org.meta_environment.rascal.ast.ModuleActuals.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.ModuleActuals.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.ModuleActuals.Default makeModuleActualsDefault(INode node, java.util.List<org.meta_environment.rascal.ast.Type> types) { 
org.meta_environment.rascal.ast.ModuleActuals.Default x = (org.meta_environment.rascal.ast.ModuleActuals.Default) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.ModuleActuals.Default(node, types);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.ImportedModule.Default makeImportedModuleDefault(INode node, org.meta_environment.rascal.ast.QualifiedName name) { 
org.meta_environment.rascal.ast.ImportedModule.Default x = (org.meta_environment.rascal.ast.ImportedModule.Default) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.ImportedModule.Default(node, name);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.ImportedModule.Renamings makeImportedModuleRenamings(INode node, org.meta_environment.rascal.ast.QualifiedName name, org.meta_environment.rascal.ast.Renamings renamings) { 
org.meta_environment.rascal.ast.ImportedModule.Renamings x = (org.meta_environment.rascal.ast.ImportedModule.Renamings) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.ImportedModule.Renamings(node, name, renamings);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.ImportedModule.Actuals makeImportedModuleActuals(INode node, org.meta_environment.rascal.ast.QualifiedName name, org.meta_environment.rascal.ast.ModuleActuals actuals) { 
org.meta_environment.rascal.ast.ImportedModule.Actuals x = (org.meta_environment.rascal.ast.ImportedModule.Actuals) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.ImportedModule.Actuals(node, name, actuals);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.ImportedModule.Ambiguity makeImportedModuleAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.ImportedModule> alternatives) { 
org.meta_environment.rascal.ast.ImportedModule.Ambiguity amb = (org.meta_environment.rascal.ast.ImportedModule.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.ImportedModule.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.ImportedModule.ActualsRenaming makeImportedModuleActualsRenaming(INode node, org.meta_environment.rascal.ast.QualifiedName name, org.meta_environment.rascal.ast.ModuleActuals actuals, org.meta_environment.rascal.ast.Renamings renamings) { 
org.meta_environment.rascal.ast.ImportedModule.ActualsRenaming x = (org.meta_environment.rascal.ast.ImportedModule.ActualsRenaming) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.ImportedModule.ActualsRenaming(node, name, actuals, renamings);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Renaming.Ambiguity makeRenamingAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Renaming> alternatives) { 
org.meta_environment.rascal.ast.Renaming.Ambiguity amb = (org.meta_environment.rascal.ast.Renaming.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.Renaming.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.Renaming.Default makeRenamingDefault(INode node, org.meta_environment.rascal.ast.Name from, org.meta_environment.rascal.ast.Name to) { 
org.meta_environment.rascal.ast.Renaming.Default x = (org.meta_environment.rascal.ast.Renaming.Default) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Renaming.Default(node, from, to);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Renamings.Ambiguity makeRenamingsAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Renamings> alternatives) { 
org.meta_environment.rascal.ast.Renamings.Ambiguity amb = (org.meta_environment.rascal.ast.Renamings.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.Renamings.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.Renamings.Default makeRenamingsDefault(INode node, java.util.List<org.meta_environment.rascal.ast.Renaming> renamings) { 
org.meta_environment.rascal.ast.Renamings.Default x = (org.meta_environment.rascal.ast.Renamings.Default) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Renamings.Default(node, renamings);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Import.Extend makeImportExtend(INode node, org.meta_environment.rascal.ast.ImportedModule module) { 
org.meta_environment.rascal.ast.Import.Extend x = (org.meta_environment.rascal.ast.Import.Extend) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Import.Extend(node, module);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Import.Ambiguity makeImportAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Import> alternatives) { 
org.meta_environment.rascal.ast.Import.Ambiguity amb = (org.meta_environment.rascal.ast.Import.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.Import.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.Import.Default makeImportDefault(INode node, org.meta_environment.rascal.ast.ImportedModule module) { 
org.meta_environment.rascal.ast.Import.Default x = (org.meta_environment.rascal.ast.Import.Default) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Import.Default(node, module);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.ModuleParameters.Ambiguity makeModuleParametersAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.ModuleParameters> alternatives) { 
org.meta_environment.rascal.ast.ModuleParameters.Ambiguity amb = (org.meta_environment.rascal.ast.ModuleParameters.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.ModuleParameters.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.ModuleParameters.Default makeModuleParametersDefault(INode node, java.util.List<org.meta_environment.rascal.ast.TypeVar> parameters) { 
org.meta_environment.rascal.ast.ModuleParameters.Default x = (org.meta_environment.rascal.ast.ModuleParameters.Default) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.ModuleParameters.Default(node, parameters);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Header.Parameters makeHeaderParameters(INode node, org.meta_environment.rascal.ast.QualifiedName name, org.meta_environment.rascal.ast.ModuleParameters params, org.meta_environment.rascal.ast.Tags tags, java.util.List<org.meta_environment.rascal.ast.Import> imports) { 
org.meta_environment.rascal.ast.Header.Parameters x = (org.meta_environment.rascal.ast.Header.Parameters) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Header.Parameters(node, name, params, tags, imports);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Header.Ambiguity makeHeaderAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Header> alternatives) { 
org.meta_environment.rascal.ast.Header.Ambiguity amb = (org.meta_environment.rascal.ast.Header.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.Header.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.Header.Default makeHeaderDefault(INode node, org.meta_environment.rascal.ast.QualifiedName name, org.meta_environment.rascal.ast.Tags tags, java.util.List<org.meta_environment.rascal.ast.Import> imports) { 
org.meta_environment.rascal.ast.Header.Default x = (org.meta_environment.rascal.ast.Header.Default) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Header.Default(node, name, tags, imports);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.RegExpLiteral.Ambiguity makeRegExpLiteralAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.RegExpLiteral> alternatives) { 
org.meta_environment.rascal.ast.RegExpLiteral.Ambiguity amb = (org.meta_environment.rascal.ast.RegExpLiteral.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.RegExpLiteral.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.RegExpLiteral.Lexical makeRegExpLiteralLexical(INode node, String string) { 
org.meta_environment.rascal.ast.RegExpLiteral.Lexical x = (org.meta_environment.rascal.ast.RegExpLiteral.Lexical) lexCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.RegExpLiteral.Lexical(node, string);
          lexCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.RegExpModifier.Ambiguity makeRegExpModifierAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.RegExpModifier> alternatives) { 
org.meta_environment.rascal.ast.RegExpModifier.Ambiguity amb = (org.meta_environment.rascal.ast.RegExpModifier.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.RegExpModifier.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.RegExpModifier.Lexical makeRegExpModifierLexical(INode node, String string) { 
org.meta_environment.rascal.ast.RegExpModifier.Lexical x = (org.meta_environment.rascal.ast.RegExpModifier.Lexical) lexCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.RegExpModifier.Lexical(node, string);
          lexCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Backslash.Ambiguity makeBackslashAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Backslash> alternatives) { 
org.meta_environment.rascal.ast.Backslash.Ambiguity amb = (org.meta_environment.rascal.ast.Backslash.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.Backslash.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.Backslash.Lexical makeBackslashLexical(INode node, String string) { 
org.meta_environment.rascal.ast.Backslash.Lexical x = (org.meta_environment.rascal.ast.Backslash.Lexical) lexCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Backslash.Lexical(node, string);
          lexCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.RegExp.Ambiguity makeRegExpAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.RegExp> alternatives) { 
org.meta_environment.rascal.ast.RegExp.Ambiguity amb = (org.meta_environment.rascal.ast.RegExp.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.RegExp.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.RegExp.Lexical makeRegExpLexical(INode node, String string) { 
org.meta_environment.rascal.ast.RegExp.Lexical x = (org.meta_environment.rascal.ast.RegExp.Lexical) lexCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.RegExp.Lexical(node, string);
          lexCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.NamedRegExp.Ambiguity makeNamedRegExpAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.NamedRegExp> alternatives) { 
org.meta_environment.rascal.ast.NamedRegExp.Ambiguity amb = (org.meta_environment.rascal.ast.NamedRegExp.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.NamedRegExp.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.NamedRegExp.Lexical makeNamedRegExpLexical(INode node, String string) { 
org.meta_environment.rascal.ast.NamedRegExp.Lexical x = (org.meta_environment.rascal.ast.NamedRegExp.Lexical) lexCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.NamedRegExp.Lexical(node, string);
          lexCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.NamedBackslash.Ambiguity makeNamedBackslashAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.NamedBackslash> alternatives) { 
org.meta_environment.rascal.ast.NamedBackslash.Ambiguity amb = (org.meta_environment.rascal.ast.NamedBackslash.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.NamedBackslash.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.NamedBackslash.Lexical makeNamedBackslashLexical(INode node, String string) { 
org.meta_environment.rascal.ast.NamedBackslash.Lexical x = (org.meta_environment.rascal.ast.NamedBackslash.Lexical) lexCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.NamedBackslash.Lexical(node, string);
          lexCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Formal.Ambiguity makeFormalAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Formal> alternatives) { 
org.meta_environment.rascal.ast.Formal.Ambiguity amb = (org.meta_environment.rascal.ast.Formal.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.Formal.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.Formal.TypeName makeFormalTypeName(INode node, org.meta_environment.rascal.ast.Type type, org.meta_environment.rascal.ast.Name name) { 
org.meta_environment.rascal.ast.Formal.TypeName x = (org.meta_environment.rascal.ast.Formal.TypeName) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Formal.TypeName(node, type, name);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Formals.Ambiguity makeFormalsAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Formals> alternatives) { 
org.meta_environment.rascal.ast.Formals.Ambiguity amb = (org.meta_environment.rascal.ast.Formals.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.Formals.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.Formals.Default makeFormalsDefault(INode node, java.util.List<org.meta_environment.rascal.ast.Formal> formals) { 
org.meta_environment.rascal.ast.Formals.Default x = (org.meta_environment.rascal.ast.Formals.Default) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Formals.Default(node, formals);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Parameters.VarArgs makeParametersVarArgs(INode node, org.meta_environment.rascal.ast.Formals formals) { 
org.meta_environment.rascal.ast.Parameters.VarArgs x = (org.meta_environment.rascal.ast.Parameters.VarArgs) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Parameters.VarArgs(node, formals);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Parameters.Ambiguity makeParametersAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Parameters> alternatives) { 
org.meta_environment.rascal.ast.Parameters.Ambiguity amb = (org.meta_environment.rascal.ast.Parameters.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.Parameters.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.Parameters.Default makeParametersDefault(INode node, org.meta_environment.rascal.ast.Formals formals) { 
org.meta_environment.rascal.ast.Parameters.Default x = (org.meta_environment.rascal.ast.Parameters.Default) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Parameters.Default(node, formals);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Expression.QualifiedName makeExpressionQualifiedName(INode node, org.meta_environment.rascal.ast.QualifiedName qualifiedName) { 
org.meta_environment.rascal.ast.Expression.QualifiedName x = (org.meta_environment.rascal.ast.Expression.QualifiedName) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Expression.QualifiedName(node, qualifiedName);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Expression.Location makeExpressionLocation(INode node, org.meta_environment.rascal.ast.URL url, org.meta_environment.rascal.ast.Expression offset, org.meta_environment.rascal.ast.Expression length, org.meta_environment.rascal.ast.Expression beginLine, org.meta_environment.rascal.ast.Expression beginColumn, org.meta_environment.rascal.ast.Expression endLine, org.meta_environment.rascal.ast.Expression endColumn) { 
org.meta_environment.rascal.ast.Expression.Location x = (org.meta_environment.rascal.ast.Expression.Location) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Expression.Location(node, url, offset, length, beginLine, beginColumn, endLine, endColumn);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Expression.Map makeExpressionMap(INode node, java.util.List<org.meta_environment.rascal.ast.Mapping> mappings) { 
org.meta_environment.rascal.ast.Expression.Map x = (org.meta_environment.rascal.ast.Expression.Map) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Expression.Map(node, mappings);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Expression.Tuple makeExpressionTuple(INode node, java.util.List<org.meta_environment.rascal.ast.Expression> elements) { 
org.meta_environment.rascal.ast.Expression.Tuple x = (org.meta_environment.rascal.ast.Expression.Tuple) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Expression.Tuple(node, elements);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Expression.Set makeExpressionSet(INode node, java.util.List<org.meta_environment.rascal.ast.Expression> elements) { 
org.meta_environment.rascal.ast.Expression.Set x = (org.meta_environment.rascal.ast.Expression.Set) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Expression.Set(node, elements);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Expression.List makeExpressionList(INode node, java.util.List<org.meta_environment.rascal.ast.Expression> elements) { 
org.meta_environment.rascal.ast.Expression.List x = (org.meta_environment.rascal.ast.Expression.List) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Expression.List(node, elements);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Expression.CallOrTree makeExpressionCallOrTree(INode node, org.meta_environment.rascal.ast.QualifiedName qualifiedName, java.util.List<org.meta_environment.rascal.ast.Expression> arguments) { 
org.meta_environment.rascal.ast.Expression.CallOrTree x = (org.meta_environment.rascal.ast.Expression.CallOrTree) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Expression.CallOrTree(node, qualifiedName, arguments);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Expression.Literal makeExpressionLiteral(INode node, org.meta_environment.rascal.ast.Literal literal) { 
org.meta_environment.rascal.ast.Expression.Literal x = (org.meta_environment.rascal.ast.Expression.Literal) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Expression.Literal(node, literal);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Expression.Any makeExpressionAny(INode node, java.util.List<org.meta_environment.rascal.ast.Expression> generators) { 
org.meta_environment.rascal.ast.Expression.Any x = (org.meta_environment.rascal.ast.Expression.Any) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Expression.Any(node, generators);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Expression.All makeExpressionAll(INode node, java.util.List<org.meta_environment.rascal.ast.Expression> generators) { 
org.meta_environment.rascal.ast.Expression.All x = (org.meta_environment.rascal.ast.Expression.All) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Expression.All(node, generators);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Expression.Comprehension makeExpressionComprehension(INode node, org.meta_environment.rascal.ast.Comprehension comprehension) { 
org.meta_environment.rascal.ast.Expression.Comprehension x = (org.meta_environment.rascal.ast.Expression.Comprehension) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Expression.Comprehension(node, comprehension);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Expression.EnumeratorWithStrategy makeExpressionEnumeratorWithStrategy(INode node, org.meta_environment.rascal.ast.Strategy strategy, org.meta_environment.rascal.ast.Expression pattern, org.meta_environment.rascal.ast.Expression expression) { 
org.meta_environment.rascal.ast.Expression.EnumeratorWithStrategy x = (org.meta_environment.rascal.ast.Expression.EnumeratorWithStrategy) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Expression.EnumeratorWithStrategy(node, strategy, pattern, expression);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Expression.Enumerator makeExpressionEnumerator(INode node, org.meta_environment.rascal.ast.Expression pattern, org.meta_environment.rascal.ast.Expression expression) { 
org.meta_environment.rascal.ast.Expression.Enumerator x = (org.meta_environment.rascal.ast.Expression.Enumerator) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Expression.Enumerator(node, pattern, expression);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Expression.NoMatch makeExpressionNoMatch(INode node, org.meta_environment.rascal.ast.Expression pattern, org.meta_environment.rascal.ast.Expression expression) { 
org.meta_environment.rascal.ast.Expression.NoMatch x = (org.meta_environment.rascal.ast.Expression.NoMatch) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Expression.NoMatch(node, pattern, expression);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Expression.Match makeExpressionMatch(INode node, org.meta_environment.rascal.ast.Expression pattern, org.meta_environment.rascal.ast.Expression expression) { 
org.meta_environment.rascal.ast.Expression.Match x = (org.meta_environment.rascal.ast.Expression.Match) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Expression.Match(node, pattern, expression);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Expression.Visit makeExpressionVisit(INode node, org.meta_environment.rascal.ast.Visit visit) { 
org.meta_environment.rascal.ast.Expression.Visit x = (org.meta_environment.rascal.ast.Expression.Visit) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Expression.Visit(node, visit);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Expression.Descendant makeExpressionDescendant(INode node, org.meta_environment.rascal.ast.Expression pattern) { 
org.meta_environment.rascal.ast.Expression.Descendant x = (org.meta_environment.rascal.ast.Expression.Descendant) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Expression.Descendant(node, pattern);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Expression.MultiVariable makeExpressionMultiVariable(INode node, org.meta_environment.rascal.ast.QualifiedName qualifiedName) { 
org.meta_environment.rascal.ast.Expression.MultiVariable x = (org.meta_environment.rascal.ast.Expression.MultiVariable) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Expression.MultiVariable(node, qualifiedName);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Expression.Anti makeExpressionAnti(INode node, org.meta_environment.rascal.ast.Expression pattern) { 
org.meta_environment.rascal.ast.Expression.Anti x = (org.meta_environment.rascal.ast.Expression.Anti) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Expression.Anti(node, pattern);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Expression.Guarded makeExpressionGuarded(INode node, org.meta_environment.rascal.ast.Type type, org.meta_environment.rascal.ast.Expression pattern) { 
org.meta_environment.rascal.ast.Expression.Guarded x = (org.meta_environment.rascal.ast.Expression.Guarded) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Expression.Guarded(node, type, pattern);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Expression.TypedVariableBecomes makeExpressionTypedVariableBecomes(INode node, org.meta_environment.rascal.ast.Type type, org.meta_environment.rascal.ast.Name name, org.meta_environment.rascal.ast.Expression pattern) { 
org.meta_environment.rascal.ast.Expression.TypedVariableBecomes x = (org.meta_environment.rascal.ast.Expression.TypedVariableBecomes) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Expression.TypedVariableBecomes(node, type, name, pattern);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Expression.VariableBecomes makeExpressionVariableBecomes(INode node, org.meta_environment.rascal.ast.Name name, org.meta_environment.rascal.ast.Expression pattern) { 
org.meta_environment.rascal.ast.Expression.VariableBecomes x = (org.meta_environment.rascal.ast.Expression.VariableBecomes) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Expression.VariableBecomes(node, name, pattern);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Expression.TypedVariable makeExpressionTypedVariable(INode node, org.meta_environment.rascal.ast.Type type, org.meta_environment.rascal.ast.Name name) { 
org.meta_environment.rascal.ast.Expression.TypedVariable x = (org.meta_environment.rascal.ast.Expression.TypedVariable) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Expression.TypedVariable(node, type, name);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Expression.Lexical makeExpressionLexical(INode node, String string) { 
org.meta_environment.rascal.ast.Expression.Lexical x = (org.meta_environment.rascal.ast.Expression.Lexical) lexCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Expression.Lexical(node, string);
          lexCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Expression.Or makeExpressionOr(INode node, org.meta_environment.rascal.ast.Expression lhs, org.meta_environment.rascal.ast.Expression rhs) { 
org.meta_environment.rascal.ast.Expression.Or x = (org.meta_environment.rascal.ast.Expression.Or) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Expression.Or(node, lhs, rhs);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Expression.And makeExpressionAnd(INode node, org.meta_environment.rascal.ast.Expression lhs, org.meta_environment.rascal.ast.Expression rhs) { 
org.meta_environment.rascal.ast.Expression.And x = (org.meta_environment.rascal.ast.Expression.And) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Expression.And(node, lhs, rhs);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Expression.Equivalence makeExpressionEquivalence(INode node, org.meta_environment.rascal.ast.Expression lhs, org.meta_environment.rascal.ast.Expression rhs) { 
org.meta_environment.rascal.ast.Expression.Equivalence x = (org.meta_environment.rascal.ast.Expression.Equivalence) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Expression.Equivalence(node, lhs, rhs);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Expression.Implication makeExpressionImplication(INode node, org.meta_environment.rascal.ast.Expression lhs, org.meta_environment.rascal.ast.Expression rhs) { 
org.meta_environment.rascal.ast.Expression.Implication x = (org.meta_environment.rascal.ast.Expression.Implication) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Expression.Implication(node, lhs, rhs);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Expression.IfDefinedOtherwise makeExpressionIfDefinedOtherwise(INode node, org.meta_environment.rascal.ast.Expression lhs, org.meta_environment.rascal.ast.Expression rhs) { 
org.meta_environment.rascal.ast.Expression.IfDefinedOtherwise x = (org.meta_environment.rascal.ast.Expression.IfDefinedOtherwise) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Expression.IfDefinedOtherwise(node, lhs, rhs);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Expression.IfThenElse makeExpressionIfThenElse(INode node, org.meta_environment.rascal.ast.Expression condition, org.meta_environment.rascal.ast.Expression thenExp, org.meta_environment.rascal.ast.Expression elseExp) { 
org.meta_environment.rascal.ast.Expression.IfThenElse x = (org.meta_environment.rascal.ast.Expression.IfThenElse) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Expression.IfThenElse(node, condition, thenExp, elseExp);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Expression.NonEquals makeExpressionNonEquals(INode node, org.meta_environment.rascal.ast.Expression lhs, org.meta_environment.rascal.ast.Expression rhs) { 
org.meta_environment.rascal.ast.Expression.NonEquals x = (org.meta_environment.rascal.ast.Expression.NonEquals) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Expression.NonEquals(node, lhs, rhs);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Expression.Equals makeExpressionEquals(INode node, org.meta_environment.rascal.ast.Expression lhs, org.meta_environment.rascal.ast.Expression rhs) { 
org.meta_environment.rascal.ast.Expression.Equals x = (org.meta_environment.rascal.ast.Expression.Equals) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Expression.Equals(node, lhs, rhs);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Expression.GreaterThanOrEq makeExpressionGreaterThanOrEq(INode node, org.meta_environment.rascal.ast.Expression lhs, org.meta_environment.rascal.ast.Expression rhs) { 
org.meta_environment.rascal.ast.Expression.GreaterThanOrEq x = (org.meta_environment.rascal.ast.Expression.GreaterThanOrEq) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Expression.GreaterThanOrEq(node, lhs, rhs);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Expression.GreaterThan makeExpressionGreaterThan(INode node, org.meta_environment.rascal.ast.Expression lhs, org.meta_environment.rascal.ast.Expression rhs) { 
org.meta_environment.rascal.ast.Expression.GreaterThan x = (org.meta_environment.rascal.ast.Expression.GreaterThan) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Expression.GreaterThan(node, lhs, rhs);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Expression.LessThanOrEq makeExpressionLessThanOrEq(INode node, org.meta_environment.rascal.ast.Expression lhs, org.meta_environment.rascal.ast.Expression rhs) { 
org.meta_environment.rascal.ast.Expression.LessThanOrEq x = (org.meta_environment.rascal.ast.Expression.LessThanOrEq) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Expression.LessThanOrEq(node, lhs, rhs);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Expression.LessThan makeExpressionLessThan(INode node, org.meta_environment.rascal.ast.Expression lhs, org.meta_environment.rascal.ast.Expression rhs) { 
org.meta_environment.rascal.ast.Expression.LessThan x = (org.meta_environment.rascal.ast.Expression.LessThan) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Expression.LessThan(node, lhs, rhs);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Expression.In makeExpressionIn(INode node, org.meta_environment.rascal.ast.Expression lhs, org.meta_environment.rascal.ast.Expression rhs) { 
org.meta_environment.rascal.ast.Expression.In x = (org.meta_environment.rascal.ast.Expression.In) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Expression.In(node, lhs, rhs);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Expression.NotIn makeExpressionNotIn(INode node, org.meta_environment.rascal.ast.Expression lhs, org.meta_environment.rascal.ast.Expression rhs) { 
org.meta_environment.rascal.ast.Expression.NotIn x = (org.meta_environment.rascal.ast.Expression.NotIn) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Expression.NotIn(node, lhs, rhs);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Expression.Subtraction makeExpressionSubtraction(INode node, org.meta_environment.rascal.ast.Expression lhs, org.meta_environment.rascal.ast.Expression rhs) { 
org.meta_environment.rascal.ast.Expression.Subtraction x = (org.meta_environment.rascal.ast.Expression.Subtraction) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Expression.Subtraction(node, lhs, rhs);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Expression.Addition makeExpressionAddition(INode node, org.meta_environment.rascal.ast.Expression lhs, org.meta_environment.rascal.ast.Expression rhs) { 
org.meta_environment.rascal.ast.Expression.Addition x = (org.meta_environment.rascal.ast.Expression.Addition) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Expression.Addition(node, lhs, rhs);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Expression.Intersection makeExpressionIntersection(INode node, org.meta_environment.rascal.ast.Expression lhs, org.meta_environment.rascal.ast.Expression rhs) { 
org.meta_environment.rascal.ast.Expression.Intersection x = (org.meta_environment.rascal.ast.Expression.Intersection) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Expression.Intersection(node, lhs, rhs);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Expression.Modulo makeExpressionModulo(INode node, org.meta_environment.rascal.ast.Expression lhs, org.meta_environment.rascal.ast.Expression rhs) { 
org.meta_environment.rascal.ast.Expression.Modulo x = (org.meta_environment.rascal.ast.Expression.Modulo) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Expression.Modulo(node, lhs, rhs);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Expression.Division makeExpressionDivision(INode node, org.meta_environment.rascal.ast.Expression lhs, org.meta_environment.rascal.ast.Expression rhs) { 
org.meta_environment.rascal.ast.Expression.Division x = (org.meta_environment.rascal.ast.Expression.Division) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Expression.Division(node, lhs, rhs);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Expression.Join makeExpressionJoin(INode node, org.meta_environment.rascal.ast.Expression lhs, org.meta_environment.rascal.ast.Expression rhs) { 
org.meta_environment.rascal.ast.Expression.Join x = (org.meta_environment.rascal.ast.Expression.Join) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Expression.Join(node, lhs, rhs);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Expression.Product makeExpressionProduct(INode node, org.meta_environment.rascal.ast.Expression lhs, org.meta_environment.rascal.ast.Expression rhs) { 
org.meta_environment.rascal.ast.Expression.Product x = (org.meta_environment.rascal.ast.Expression.Product) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Expression.Product(node, lhs, rhs);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Expression.Composition makeExpressionComposition(INode node, org.meta_environment.rascal.ast.Expression lhs, org.meta_environment.rascal.ast.Expression rhs) { 
org.meta_environment.rascal.ast.Expression.Composition x = (org.meta_environment.rascal.ast.Expression.Composition) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Expression.Composition(node, lhs, rhs);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Expression.SetAnnotation makeExpressionSetAnnotation(INode node, org.meta_environment.rascal.ast.Expression expression, org.meta_environment.rascal.ast.Name name, org.meta_environment.rascal.ast.Expression value) { 
org.meta_environment.rascal.ast.Expression.SetAnnotation x = (org.meta_environment.rascal.ast.Expression.SetAnnotation) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Expression.SetAnnotation(node, expression, name, value);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Expression.GetAnnotation makeExpressionGetAnnotation(INode node, org.meta_environment.rascal.ast.Expression expression, org.meta_environment.rascal.ast.Name name) { 
org.meta_environment.rascal.ast.Expression.GetAnnotation x = (org.meta_environment.rascal.ast.Expression.GetAnnotation) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Expression.GetAnnotation(node, expression, name);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Expression.TransitiveClosure makeExpressionTransitiveClosure(INode node, org.meta_environment.rascal.ast.Expression argument) { 
org.meta_environment.rascal.ast.Expression.TransitiveClosure x = (org.meta_environment.rascal.ast.Expression.TransitiveClosure) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Expression.TransitiveClosure(node, argument);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Expression.TransitiveReflexiveClosure makeExpressionTransitiveReflexiveClosure(INode node, org.meta_environment.rascal.ast.Expression argument) { 
org.meta_environment.rascal.ast.Expression.TransitiveReflexiveClosure x = (org.meta_environment.rascal.ast.Expression.TransitiveReflexiveClosure) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Expression.TransitiveReflexiveClosure(node, argument);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Expression.Negative makeExpressionNegative(INode node, org.meta_environment.rascal.ast.Expression argument) { 
org.meta_environment.rascal.ast.Expression.Negative x = (org.meta_environment.rascal.ast.Expression.Negative) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Expression.Negative(node, argument);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Expression.Negation makeExpressionNegation(INode node, org.meta_environment.rascal.ast.Expression argument) { 
org.meta_environment.rascal.ast.Expression.Negation x = (org.meta_environment.rascal.ast.Expression.Negation) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Expression.Negation(node, argument);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Expression.IsDefined makeExpressionIsDefined(INode node, org.meta_environment.rascal.ast.Expression argument) { 
org.meta_environment.rascal.ast.Expression.IsDefined x = (org.meta_environment.rascal.ast.Expression.IsDefined) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Expression.IsDefined(node, argument);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Expression.Subscript makeExpressionSubscript(INode node, org.meta_environment.rascal.ast.Expression expression, java.util.List<org.meta_environment.rascal.ast.Expression> subscripts) { 
org.meta_environment.rascal.ast.Expression.Subscript x = (org.meta_environment.rascal.ast.Expression.Subscript) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Expression.Subscript(node, expression, subscripts);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Expression.FieldProject makeExpressionFieldProject(INode node, org.meta_environment.rascal.ast.Expression expression, java.util.List<org.meta_environment.rascal.ast.Field> fields) { 
org.meta_environment.rascal.ast.Expression.FieldProject x = (org.meta_environment.rascal.ast.Expression.FieldProject) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Expression.FieldProject(node, expression, fields);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Expression.FieldAccess makeExpressionFieldAccess(INode node, org.meta_environment.rascal.ast.Expression expression, org.meta_environment.rascal.ast.Name field) { 
org.meta_environment.rascal.ast.Expression.FieldAccess x = (org.meta_environment.rascal.ast.Expression.FieldAccess) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Expression.FieldAccess(node, expression, field);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Expression.FieldUpdate makeExpressionFieldUpdate(INode node, org.meta_environment.rascal.ast.Expression expression, org.meta_environment.rascal.ast.Name key, org.meta_environment.rascal.ast.Expression replacement) { 
org.meta_environment.rascal.ast.Expression.FieldUpdate x = (org.meta_environment.rascal.ast.Expression.FieldUpdate) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Expression.FieldUpdate(node, expression, key, replacement);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Expression.ClosureCall makeExpressionClosureCall(INode node, org.meta_environment.rascal.ast.ClosureAsFunction closure, java.util.List<org.meta_environment.rascal.ast.Expression> arguments) { 
org.meta_environment.rascal.ast.Expression.ClosureCall x = (org.meta_environment.rascal.ast.Expression.ClosureCall) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Expression.ClosureCall(node, closure, arguments);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Expression.FunctionAsValue makeExpressionFunctionAsValue(INode node, org.meta_environment.rascal.ast.FunctionAsValue function) { 
org.meta_environment.rascal.ast.Expression.FunctionAsValue x = (org.meta_environment.rascal.ast.Expression.FunctionAsValue) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Expression.FunctionAsValue(node, function);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Expression.OperatorAsValue makeExpressionOperatorAsValue(INode node, org.meta_environment.rascal.ast.OperatorAsValue operator) { 
org.meta_environment.rascal.ast.Expression.OperatorAsValue x = (org.meta_environment.rascal.ast.Expression.OperatorAsValue) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Expression.OperatorAsValue(node, operator);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Expression.StepRange makeExpressionStepRange(INode node, org.meta_environment.rascal.ast.Expression first, org.meta_environment.rascal.ast.Expression second, org.meta_environment.rascal.ast.Expression last) { 
org.meta_environment.rascal.ast.Expression.StepRange x = (org.meta_environment.rascal.ast.Expression.StepRange) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Expression.StepRange(node, first, second, last);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Expression.Range makeExpressionRange(INode node, org.meta_environment.rascal.ast.Expression first, org.meta_environment.rascal.ast.Expression last) { 
org.meta_environment.rascal.ast.Expression.Range x = (org.meta_environment.rascal.ast.Expression.Range) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Expression.Range(node, first, last);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Expression.Bracket makeExpressionBracket(INode node, org.meta_environment.rascal.ast.Expression expression) { 
org.meta_environment.rascal.ast.Expression.Bracket x = (org.meta_environment.rascal.ast.Expression.Bracket) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Expression.Bracket(node, expression);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Expression.NonEmptyBlock makeExpressionNonEmptyBlock(INode node, java.util.List<org.meta_environment.rascal.ast.Statement> statements) { 
org.meta_environment.rascal.ast.Expression.NonEmptyBlock x = (org.meta_environment.rascal.ast.Expression.NonEmptyBlock) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Expression.NonEmptyBlock(node, statements);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Expression.VoidClosure makeExpressionVoidClosure(INode node, org.meta_environment.rascal.ast.Parameters parameters, java.util.List<org.meta_environment.rascal.ast.Statement> statements) { 
org.meta_environment.rascal.ast.Expression.VoidClosure x = (org.meta_environment.rascal.ast.Expression.VoidClosure) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Expression.VoidClosure(node, parameters, statements);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Expression.Ambiguity makeExpressionAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Expression> alternatives) { 
org.meta_environment.rascal.ast.Expression.Ambiguity amb = (org.meta_environment.rascal.ast.Expression.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.Expression.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.Expression.Closure makeExpressionClosure(INode node, org.meta_environment.rascal.ast.Type type, org.meta_environment.rascal.ast.Parameters parameters, java.util.List<org.meta_environment.rascal.ast.Statement> statements) { 
org.meta_environment.rascal.ast.Expression.Closure x = (org.meta_environment.rascal.ast.Expression.Closure) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Expression.Closure(node, type, parameters, statements);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.OperatorAsValue.NotIn makeOperatorAsValueNotIn(INode node) { 
org.meta_environment.rascal.ast.OperatorAsValue.NotIn x = (org.meta_environment.rascal.ast.OperatorAsValue.NotIn) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.OperatorAsValue.NotIn(node);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.OperatorAsValue.In makeOperatorAsValueIn(INode node) { 
org.meta_environment.rascal.ast.OperatorAsValue.In x = (org.meta_environment.rascal.ast.OperatorAsValue.In) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.OperatorAsValue.In(node);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.OperatorAsValue.Not makeOperatorAsValueNot(INode node) { 
org.meta_environment.rascal.ast.OperatorAsValue.Not x = (org.meta_environment.rascal.ast.OperatorAsValue.Not) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.OperatorAsValue.Not(node);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.OperatorAsValue.Or makeOperatorAsValueOr(INode node) { 
org.meta_environment.rascal.ast.OperatorAsValue.Or x = (org.meta_environment.rascal.ast.OperatorAsValue.Or) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.OperatorAsValue.Or(node);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.OperatorAsValue.And makeOperatorAsValueAnd(INode node) { 
org.meta_environment.rascal.ast.OperatorAsValue.And x = (org.meta_environment.rascal.ast.OperatorAsValue.And) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.OperatorAsValue.And(node);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.OperatorAsValue.GreaterThanOrEq makeOperatorAsValueGreaterThanOrEq(INode node) { 
org.meta_environment.rascal.ast.OperatorAsValue.GreaterThanOrEq x = (org.meta_environment.rascal.ast.OperatorAsValue.GreaterThanOrEq) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.OperatorAsValue.GreaterThanOrEq(node);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.OperatorAsValue.GreaterThan makeOperatorAsValueGreaterThan(INode node) { 
org.meta_environment.rascal.ast.OperatorAsValue.GreaterThan x = (org.meta_environment.rascal.ast.OperatorAsValue.GreaterThan) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.OperatorAsValue.GreaterThan(node);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.OperatorAsValue.LessThanOrEq makeOperatorAsValueLessThanOrEq(INode node) { 
org.meta_environment.rascal.ast.OperatorAsValue.LessThanOrEq x = (org.meta_environment.rascal.ast.OperatorAsValue.LessThanOrEq) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.OperatorAsValue.LessThanOrEq(node);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.OperatorAsValue.LessThan makeOperatorAsValueLessThan(INode node) { 
org.meta_environment.rascal.ast.OperatorAsValue.LessThan x = (org.meta_environment.rascal.ast.OperatorAsValue.LessThan) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.OperatorAsValue.LessThan(node);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.OperatorAsValue.NotEquals makeOperatorAsValueNotEquals(INode node) { 
org.meta_environment.rascal.ast.OperatorAsValue.NotEquals x = (org.meta_environment.rascal.ast.OperatorAsValue.NotEquals) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.OperatorAsValue.NotEquals(node);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.OperatorAsValue.Equals makeOperatorAsValueEquals(INode node) { 
org.meta_environment.rascal.ast.OperatorAsValue.Equals x = (org.meta_environment.rascal.ast.OperatorAsValue.Equals) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.OperatorAsValue.Equals(node);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.OperatorAsValue.Intersection makeOperatorAsValueIntersection(INode node) { 
org.meta_environment.rascal.ast.OperatorAsValue.Intersection x = (org.meta_environment.rascal.ast.OperatorAsValue.Intersection) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.OperatorAsValue.Intersection(node);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.OperatorAsValue.Division makeOperatorAsValueDivision(INode node) { 
org.meta_environment.rascal.ast.OperatorAsValue.Division x = (org.meta_environment.rascal.ast.OperatorAsValue.Division) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.OperatorAsValue.Division(node);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.OperatorAsValue.Product makeOperatorAsValueProduct(INode node) { 
org.meta_environment.rascal.ast.OperatorAsValue.Product x = (org.meta_environment.rascal.ast.OperatorAsValue.Product) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.OperatorAsValue.Product(node);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.OperatorAsValue.Subtraction makeOperatorAsValueSubtraction(INode node) { 
org.meta_environment.rascal.ast.OperatorAsValue.Subtraction x = (org.meta_environment.rascal.ast.OperatorAsValue.Subtraction) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.OperatorAsValue.Subtraction(node);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.OperatorAsValue.Ambiguity makeOperatorAsValueAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.OperatorAsValue> alternatives) { 
org.meta_environment.rascal.ast.OperatorAsValue.Ambiguity amb = (org.meta_environment.rascal.ast.OperatorAsValue.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.OperatorAsValue.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.OperatorAsValue.Addition makeOperatorAsValueAddition(INode node) { 
org.meta_environment.rascal.ast.OperatorAsValue.Addition x = (org.meta_environment.rascal.ast.OperatorAsValue.Addition) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.OperatorAsValue.Addition(node);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.FunctionAsValue.Ambiguity makeFunctionAsValueAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.FunctionAsValue> alternatives) { 
org.meta_environment.rascal.ast.FunctionAsValue.Ambiguity amb = (org.meta_environment.rascal.ast.FunctionAsValue.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.FunctionAsValue.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.FunctionAsValue.Default makeFunctionAsValueDefault(INode node, org.meta_environment.rascal.ast.Name name) { 
org.meta_environment.rascal.ast.FunctionAsValue.Default x = (org.meta_environment.rascal.ast.FunctionAsValue.Default) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.FunctionAsValue.Default(node, name);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Field.Index makeFieldIndex(INode node, org.meta_environment.rascal.ast.IntegerLiteral fieldIndex) { 
org.meta_environment.rascal.ast.Field.Index x = (org.meta_environment.rascal.ast.Field.Index) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Field.Index(node, fieldIndex);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Field.Ambiguity makeFieldAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Field> alternatives) { 
org.meta_environment.rascal.ast.Field.Ambiguity amb = (org.meta_environment.rascal.ast.Field.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.Field.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.Field.Name makeFieldName(INode node, org.meta_environment.rascal.ast.Name fieldName) { 
org.meta_environment.rascal.ast.Field.Name x = (org.meta_environment.rascal.ast.Field.Name) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Field.Name(node, fieldName);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.ClosureAsFunction.Ambiguity makeClosureAsFunctionAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.ClosureAsFunction> alternatives) { 
org.meta_environment.rascal.ast.ClosureAsFunction.Ambiguity amb = (org.meta_environment.rascal.ast.ClosureAsFunction.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.ClosureAsFunction.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.ClosureAsFunction.Evaluated makeClosureAsFunctionEvaluated(INode node, org.meta_environment.rascal.ast.Expression expression) { 
org.meta_environment.rascal.ast.ClosureAsFunction.Evaluated x = (org.meta_environment.rascal.ast.ClosureAsFunction.Evaluated) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.ClosureAsFunction.Evaluated(node, expression);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Command.Import makeCommandImport(INode node, org.meta_environment.rascal.ast.Import imported) { 
org.meta_environment.rascal.ast.Command.Import x = (org.meta_environment.rascal.ast.Command.Import) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Command.Import(node, imported);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Command.Declaration makeCommandDeclaration(INode node, org.meta_environment.rascal.ast.Declaration declaration) { 
org.meta_environment.rascal.ast.Command.Declaration x = (org.meta_environment.rascal.ast.Command.Declaration) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Command.Declaration(node, declaration);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Command.Statement makeCommandStatement(INode node, org.meta_environment.rascal.ast.Statement statement) { 
org.meta_environment.rascal.ast.Command.Statement x = (org.meta_environment.rascal.ast.Command.Statement) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Command.Statement(node, statement);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Command.Ambiguity makeCommandAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Command> alternatives) { 
org.meta_environment.rascal.ast.Command.Ambiguity amb = (org.meta_environment.rascal.ast.Command.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.Command.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.Command.Shell makeCommandShell(INode node, org.meta_environment.rascal.ast.ShellCommand command) { 
org.meta_environment.rascal.ast.Command.Shell x = (org.meta_environment.rascal.ast.Command.Shell) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Command.Shell(node, command);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.ShellCommand.History makeShellCommandHistory(INode node) { 
org.meta_environment.rascal.ast.ShellCommand.History x = (org.meta_environment.rascal.ast.ShellCommand.History) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.ShellCommand.History(node);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.ShellCommand.Edit makeShellCommandEdit(INode node, org.meta_environment.rascal.ast.Name name) { 
org.meta_environment.rascal.ast.ShellCommand.Edit x = (org.meta_environment.rascal.ast.ShellCommand.Edit) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.ShellCommand.Edit(node, name);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.ShellCommand.Quit makeShellCommandQuit(INode node) { 
org.meta_environment.rascal.ast.ShellCommand.Quit x = (org.meta_environment.rascal.ast.ShellCommand.Quit) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.ShellCommand.Quit(node);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.ShellCommand.Ambiguity makeShellCommandAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.ShellCommand> alternatives) { 
org.meta_environment.rascal.ast.ShellCommand.Ambiguity amb = (org.meta_environment.rascal.ast.ShellCommand.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.ShellCommand.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.ShellCommand.Help makeShellCommandHelp(INode node) { 
org.meta_environment.rascal.ast.ShellCommand.Help x = (org.meta_environment.rascal.ast.ShellCommand.Help) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.ShellCommand.Help(node);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.TagString.Ambiguity makeTagStringAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.TagString> alternatives) { 
org.meta_environment.rascal.ast.TagString.Ambiguity amb = (org.meta_environment.rascal.ast.TagString.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.TagString.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.TagString.Lexical makeTagStringLexical(INode node, String string) { 
org.meta_environment.rascal.ast.TagString.Lexical x = (org.meta_environment.rascal.ast.TagString.Lexical) lexCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.TagString.Lexical(node, string);
          lexCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.TagChar.Ambiguity makeTagCharAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.TagChar> alternatives) { 
org.meta_environment.rascal.ast.TagChar.Ambiguity amb = (org.meta_environment.rascal.ast.TagChar.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.TagChar.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.TagChar.Lexical makeTagCharLexical(INode node, String string) { 
org.meta_environment.rascal.ast.TagChar.Lexical x = (org.meta_environment.rascal.ast.TagChar.Lexical) lexCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.TagChar.Lexical(node, string);
          lexCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Tag.Empty makeTagEmpty(INode node, org.meta_environment.rascal.ast.Name name) { 
org.meta_environment.rascal.ast.Tag.Empty x = (org.meta_environment.rascal.ast.Tag.Empty) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Tag.Empty(node, name);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Tag.Ambiguity makeTagAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Tag> alternatives) { 
org.meta_environment.rascal.ast.Tag.Ambiguity amb = (org.meta_environment.rascal.ast.Tag.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.Tag.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.Tag.Default makeTagDefault(INode node, org.meta_environment.rascal.ast.Name name, org.meta_environment.rascal.ast.TagString contents) { 
org.meta_environment.rascal.ast.Tag.Default x = (org.meta_environment.rascal.ast.Tag.Default) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Tag.Default(node, name, contents);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Tags.Ambiguity makeTagsAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Tags> alternatives) { 
org.meta_environment.rascal.ast.Tags.Ambiguity amb = (org.meta_environment.rascal.ast.Tags.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.Tags.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.Tags.Default makeTagsDefault(INode node, java.util.List<org.meta_environment.rascal.ast.Tag> annotations) { 
org.meta_environment.rascal.ast.Tags.Default x = (org.meta_environment.rascal.ast.Tags.Default) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Tags.Default(node, annotations);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Bound.Default makeBoundDefault(INode node, org.meta_environment.rascal.ast.Expression expression) { 
org.meta_environment.rascal.ast.Bound.Default x = (org.meta_environment.rascal.ast.Bound.Default) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Bound.Default(node, expression);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Bound.Ambiguity makeBoundAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Bound> alternatives) { 
org.meta_environment.rascal.ast.Bound.Ambiguity amb = (org.meta_environment.rascal.ast.Bound.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.Bound.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.Bound.Empty makeBoundEmpty(INode node) { 
org.meta_environment.rascal.ast.Bound.Empty x = (org.meta_environment.rascal.ast.Bound.Empty) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Bound.Empty(node);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Statement.GlobalDirective makeStatementGlobalDirective(INode node, org.meta_environment.rascal.ast.Type type, java.util.List<org.meta_environment.rascal.ast.QualifiedName> names) { 
org.meta_environment.rascal.ast.Statement.GlobalDirective x = (org.meta_environment.rascal.ast.Statement.GlobalDirective) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Statement.GlobalDirective(node, type, names);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Statement.VariableDeclaration makeStatementVariableDeclaration(INode node, org.meta_environment.rascal.ast.LocalVariableDeclaration declaration) { 
org.meta_environment.rascal.ast.Statement.VariableDeclaration x = (org.meta_environment.rascal.ast.Statement.VariableDeclaration) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Statement.VariableDeclaration(node, declaration);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Statement.FunctionDeclaration makeStatementFunctionDeclaration(INode node, org.meta_environment.rascal.ast.FunctionDeclaration functionDeclaration) { 
org.meta_environment.rascal.ast.Statement.FunctionDeclaration x = (org.meta_environment.rascal.ast.Statement.FunctionDeclaration) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Statement.FunctionDeclaration(node, functionDeclaration);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Statement.Block makeStatementBlock(INode node, org.meta_environment.rascal.ast.Label label, java.util.List<org.meta_environment.rascal.ast.Statement> statements) { 
org.meta_environment.rascal.ast.Statement.Block x = (org.meta_environment.rascal.ast.Statement.Block) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Statement.Block(node, label, statements);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Statement.TryFinally makeStatementTryFinally(INode node, org.meta_environment.rascal.ast.Statement body, java.util.List<org.meta_environment.rascal.ast.Catch> handlers, org.meta_environment.rascal.ast.Statement finallyBody) { 
org.meta_environment.rascal.ast.Statement.TryFinally x = (org.meta_environment.rascal.ast.Statement.TryFinally) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Statement.TryFinally(node, body, handlers, finallyBody);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Statement.Try makeStatementTry(INode node, org.meta_environment.rascal.ast.Statement body, java.util.List<org.meta_environment.rascal.ast.Catch> handlers) { 
org.meta_environment.rascal.ast.Statement.Try x = (org.meta_environment.rascal.ast.Statement.Try) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Statement.Try(node, body, handlers);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Statement.Throw makeStatementThrow(INode node, org.meta_environment.rascal.ast.Expression expression) { 
org.meta_environment.rascal.ast.Statement.Throw x = (org.meta_environment.rascal.ast.Statement.Throw) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Statement.Throw(node, expression);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Statement.Insert makeStatementInsert(INode node, org.meta_environment.rascal.ast.Expression expression) { 
org.meta_environment.rascal.ast.Statement.Insert x = (org.meta_environment.rascal.ast.Statement.Insert) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Statement.Insert(node, expression);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Statement.AssertWithMessage makeStatementAssertWithMessage(INode node, org.meta_environment.rascal.ast.Expression expression, org.meta_environment.rascal.ast.StringLiteral message) { 
org.meta_environment.rascal.ast.Statement.AssertWithMessage x = (org.meta_environment.rascal.ast.Statement.AssertWithMessage) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Statement.AssertWithMessage(node, expression, message);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Statement.Assert makeStatementAssert(INode node, org.meta_environment.rascal.ast.Expression expression) { 
org.meta_environment.rascal.ast.Statement.Assert x = (org.meta_environment.rascal.ast.Statement.Assert) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Statement.Assert(node, expression);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Statement.Continue makeStatementContinue(INode node) { 
org.meta_environment.rascal.ast.Statement.Continue x = (org.meta_environment.rascal.ast.Statement.Continue) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Statement.Continue(node);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Statement.Return makeStatementReturn(INode node, org.meta_environment.rascal.ast.Return ret) { 
org.meta_environment.rascal.ast.Statement.Return x = (org.meta_environment.rascal.ast.Statement.Return) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Statement.Return(node, ret);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Statement.Fail makeStatementFail(INode node, org.meta_environment.rascal.ast.Fail fail) { 
org.meta_environment.rascal.ast.Statement.Fail x = (org.meta_environment.rascal.ast.Statement.Fail) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Statement.Fail(node, fail);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Statement.Break makeStatementBreak(INode node, org.meta_environment.rascal.ast.Break brk) { 
org.meta_environment.rascal.ast.Statement.Break x = (org.meta_environment.rascal.ast.Statement.Break) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Statement.Break(node, brk);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Statement.Assignment makeStatementAssignment(INode node, org.meta_environment.rascal.ast.Assignable assignable, org.meta_environment.rascal.ast.Assignment operator, org.meta_environment.rascal.ast.Expression expression) { 
org.meta_environment.rascal.ast.Statement.Assignment x = (org.meta_environment.rascal.ast.Statement.Assignment) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Statement.Assignment(node, assignable, operator, expression);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Statement.Visit makeStatementVisit(INode node, org.meta_environment.rascal.ast.Visit visit) { 
org.meta_environment.rascal.ast.Statement.Visit x = (org.meta_environment.rascal.ast.Statement.Visit) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Statement.Visit(node, visit);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Statement.Expression makeStatementExpression(INode node, org.meta_environment.rascal.ast.Expression expression) { 
org.meta_environment.rascal.ast.Statement.Expression x = (org.meta_environment.rascal.ast.Statement.Expression) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Statement.Expression(node, expression);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Statement.EmptyStatement makeStatementEmptyStatement(INode node) { 
org.meta_environment.rascal.ast.Statement.EmptyStatement x = (org.meta_environment.rascal.ast.Statement.EmptyStatement) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Statement.EmptyStatement(node);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Statement.Switch makeStatementSwitch(INode node, org.meta_environment.rascal.ast.Label label, org.meta_environment.rascal.ast.Expression expression, java.util.List<org.meta_environment.rascal.ast.Case> cases) { 
org.meta_environment.rascal.ast.Statement.Switch x = (org.meta_environment.rascal.ast.Statement.Switch) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Statement.Switch(node, label, expression, cases);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Statement.IfThen makeStatementIfThen(INode node, org.meta_environment.rascal.ast.Label label, java.util.List<org.meta_environment.rascal.ast.Expression> conditions, org.meta_environment.rascal.ast.Statement thenStatement, org.meta_environment.rascal.ast.NoElseMayFollow noElseMayFollow) { 
org.meta_environment.rascal.ast.Statement.IfThen x = (org.meta_environment.rascal.ast.Statement.IfThen) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Statement.IfThen(node, label, conditions, thenStatement, noElseMayFollow);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Statement.IfThenElse makeStatementIfThenElse(INode node, org.meta_environment.rascal.ast.Label label, java.util.List<org.meta_environment.rascal.ast.Expression> conditions, org.meta_environment.rascal.ast.Statement thenStatement, org.meta_environment.rascal.ast.Statement elseStatement) { 
org.meta_environment.rascal.ast.Statement.IfThenElse x = (org.meta_environment.rascal.ast.Statement.IfThenElse) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Statement.IfThenElse(node, label, conditions, thenStatement, elseStatement);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Statement.DoWhile makeStatementDoWhile(INode node, org.meta_environment.rascal.ast.Label label, org.meta_environment.rascal.ast.Statement body, org.meta_environment.rascal.ast.Expression condition) { 
org.meta_environment.rascal.ast.Statement.DoWhile x = (org.meta_environment.rascal.ast.Statement.DoWhile) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Statement.DoWhile(node, label, body, condition);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Statement.While makeStatementWhile(INode node, org.meta_environment.rascal.ast.Label label, org.meta_environment.rascal.ast.Expression condition, org.meta_environment.rascal.ast.Statement body) { 
org.meta_environment.rascal.ast.Statement.While x = (org.meta_environment.rascal.ast.Statement.While) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Statement.While(node, label, condition, body);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Statement.For makeStatementFor(INode node, org.meta_environment.rascal.ast.Label label, java.util.List<org.meta_environment.rascal.ast.Expression> generators, org.meta_environment.rascal.ast.Statement body) { 
org.meta_environment.rascal.ast.Statement.For x = (org.meta_environment.rascal.ast.Statement.For) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Statement.For(node, label, generators, body);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Statement.Ambiguity makeStatementAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Statement> alternatives) { 
org.meta_environment.rascal.ast.Statement.Ambiguity amb = (org.meta_environment.rascal.ast.Statement.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.Statement.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.Statement.Solve makeStatementSolve(INode node, java.util.List<org.meta_environment.rascal.ast.Declarator> declarations, org.meta_environment.rascal.ast.Bound bound, org.meta_environment.rascal.ast.Statement body) { 
org.meta_environment.rascal.ast.Statement.Solve x = (org.meta_environment.rascal.ast.Statement.Solve) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Statement.Solve(node, declarations, bound, body);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.NoElseMayFollow.Ambiguity makeNoElseMayFollowAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.NoElseMayFollow> alternatives) { 
org.meta_environment.rascal.ast.NoElseMayFollow.Ambiguity amb = (org.meta_environment.rascal.ast.NoElseMayFollow.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.NoElseMayFollow.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.NoElseMayFollow.Default makeNoElseMayFollowDefault(INode node) { 
org.meta_environment.rascal.ast.NoElseMayFollow.Default x = (org.meta_environment.rascal.ast.NoElseMayFollow.Default) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.NoElseMayFollow.Default(node);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Assignable.Constructor makeAssignableConstructor(INode node, org.meta_environment.rascal.ast.Name name, java.util.List<org.meta_environment.rascal.ast.Assignable> arguments) { 
org.meta_environment.rascal.ast.Assignable.Constructor x = (org.meta_environment.rascal.ast.Assignable.Constructor) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Assignable.Constructor(node, name, arguments);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Assignable.Tuple makeAssignableTuple(INode node, java.util.List<org.meta_environment.rascal.ast.Assignable> elements) { 
org.meta_environment.rascal.ast.Assignable.Tuple x = (org.meta_environment.rascal.ast.Assignable.Tuple) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Assignable.Tuple(node, elements);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Assignable.Annotation makeAssignableAnnotation(INode node, org.meta_environment.rascal.ast.Assignable receiver, org.meta_environment.rascal.ast.Name annotation) { 
org.meta_environment.rascal.ast.Assignable.Annotation x = (org.meta_environment.rascal.ast.Assignable.Annotation) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Assignable.Annotation(node, receiver, annotation);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Assignable.IfDefinedOrDefault makeAssignableIfDefinedOrDefault(INode node, org.meta_environment.rascal.ast.Assignable receiver, org.meta_environment.rascal.ast.Expression defaultExpression) { 
org.meta_environment.rascal.ast.Assignable.IfDefinedOrDefault x = (org.meta_environment.rascal.ast.Assignable.IfDefinedOrDefault) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Assignable.IfDefinedOrDefault(node, receiver, defaultExpression);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Assignable.FieldAccess makeAssignableFieldAccess(INode node, org.meta_environment.rascal.ast.Assignable receiver, org.meta_environment.rascal.ast.Name field) { 
org.meta_environment.rascal.ast.Assignable.FieldAccess x = (org.meta_environment.rascal.ast.Assignable.FieldAccess) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Assignable.FieldAccess(node, receiver, field);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Assignable.Subscript makeAssignableSubscript(INode node, org.meta_environment.rascal.ast.Assignable receiver, org.meta_environment.rascal.ast.Expression subscript) { 
org.meta_environment.rascal.ast.Assignable.Subscript x = (org.meta_environment.rascal.ast.Assignable.Subscript) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Assignable.Subscript(node, receiver, subscript);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Assignable.Ambiguity makeAssignableAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Assignable> alternatives) { 
org.meta_environment.rascal.ast.Assignable.Ambiguity amb = (org.meta_environment.rascal.ast.Assignable.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.Assignable.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.Assignable.Variable makeAssignableVariable(INode node, org.meta_environment.rascal.ast.QualifiedName qualifiedName) { 
org.meta_environment.rascal.ast.Assignable.Variable x = (org.meta_environment.rascal.ast.Assignable.Variable) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Assignable.Variable(node, qualifiedName);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Assignment.IfDefined makeAssignmentIfDefined(INode node) { 
org.meta_environment.rascal.ast.Assignment.IfDefined x = (org.meta_environment.rascal.ast.Assignment.IfDefined) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Assignment.IfDefined(node);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Assignment.Intersection makeAssignmentIntersection(INode node) { 
org.meta_environment.rascal.ast.Assignment.Intersection x = (org.meta_environment.rascal.ast.Assignment.Intersection) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Assignment.Intersection(node);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Assignment.Division makeAssignmentDivision(INode node) { 
org.meta_environment.rascal.ast.Assignment.Division x = (org.meta_environment.rascal.ast.Assignment.Division) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Assignment.Division(node);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Assignment.Product makeAssignmentProduct(INode node) { 
org.meta_environment.rascal.ast.Assignment.Product x = (org.meta_environment.rascal.ast.Assignment.Product) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Assignment.Product(node);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Assignment.Subtraction makeAssignmentSubtraction(INode node) { 
org.meta_environment.rascal.ast.Assignment.Subtraction x = (org.meta_environment.rascal.ast.Assignment.Subtraction) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Assignment.Subtraction(node);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Assignment.Addition makeAssignmentAddition(INode node) { 
org.meta_environment.rascal.ast.Assignment.Addition x = (org.meta_environment.rascal.ast.Assignment.Addition) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Assignment.Addition(node);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Assignment.Ambiguity makeAssignmentAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Assignment> alternatives) { 
org.meta_environment.rascal.ast.Assignment.Ambiguity amb = (org.meta_environment.rascal.ast.Assignment.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.Assignment.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.Assignment.Default makeAssignmentDefault(INode node) { 
org.meta_environment.rascal.ast.Assignment.Default x = (org.meta_environment.rascal.ast.Assignment.Default) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Assignment.Default(node);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Label.Default makeLabelDefault(INode node, org.meta_environment.rascal.ast.Name name) { 
org.meta_environment.rascal.ast.Label.Default x = (org.meta_environment.rascal.ast.Label.Default) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Label.Default(node, name);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Label.Ambiguity makeLabelAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Label> alternatives) { 
org.meta_environment.rascal.ast.Label.Ambiguity amb = (org.meta_environment.rascal.ast.Label.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.Label.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.Label.Empty makeLabelEmpty(INode node) { 
org.meta_environment.rascal.ast.Label.Empty x = (org.meta_environment.rascal.ast.Label.Empty) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Label.Empty(node);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Break.NoLabel makeBreakNoLabel(INode node) { 
org.meta_environment.rascal.ast.Break.NoLabel x = (org.meta_environment.rascal.ast.Break.NoLabel) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Break.NoLabel(node);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Break.Ambiguity makeBreakAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Break> alternatives) { 
org.meta_environment.rascal.ast.Break.Ambiguity amb = (org.meta_environment.rascal.ast.Break.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.Break.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.Break.WithLabel makeBreakWithLabel(INode node, org.meta_environment.rascal.ast.Name label) { 
org.meta_environment.rascal.ast.Break.WithLabel x = (org.meta_environment.rascal.ast.Break.WithLabel) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Break.WithLabel(node, label);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Fail.NoLabel makeFailNoLabel(INode node) { 
org.meta_environment.rascal.ast.Fail.NoLabel x = (org.meta_environment.rascal.ast.Fail.NoLabel) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Fail.NoLabel(node);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Fail.Ambiguity makeFailAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Fail> alternatives) { 
org.meta_environment.rascal.ast.Fail.Ambiguity amb = (org.meta_environment.rascal.ast.Fail.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.Fail.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.Fail.WithLabel makeFailWithLabel(INode node, org.meta_environment.rascal.ast.Name label) { 
org.meta_environment.rascal.ast.Fail.WithLabel x = (org.meta_environment.rascal.ast.Fail.WithLabel) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Fail.WithLabel(node, label);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Return.NoExpression makeReturnNoExpression(INode node) { 
org.meta_environment.rascal.ast.Return.NoExpression x = (org.meta_environment.rascal.ast.Return.NoExpression) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Return.NoExpression(node);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Return.Ambiguity makeReturnAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Return> alternatives) { 
org.meta_environment.rascal.ast.Return.Ambiguity amb = (org.meta_environment.rascal.ast.Return.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.Return.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.Return.WithExpression makeReturnWithExpression(INode node, org.meta_environment.rascal.ast.Expression expression) { 
org.meta_environment.rascal.ast.Return.WithExpression x = (org.meta_environment.rascal.ast.Return.WithExpression) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Return.WithExpression(node, expression);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Catch.Binding makeCatchBinding(INode node, org.meta_environment.rascal.ast.Expression pattern, org.meta_environment.rascal.ast.Statement body) { 
org.meta_environment.rascal.ast.Catch.Binding x = (org.meta_environment.rascal.ast.Catch.Binding) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Catch.Binding(node, pattern, body);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Catch.Ambiguity makeCatchAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Catch> alternatives) { 
org.meta_environment.rascal.ast.Catch.Ambiguity amb = (org.meta_environment.rascal.ast.Catch.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.Catch.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.Catch.Default makeCatchDefault(INode node, org.meta_environment.rascal.ast.Statement body) { 
org.meta_environment.rascal.ast.Catch.Default x = (org.meta_environment.rascal.ast.Catch.Default) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Catch.Default(node, body);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Declarator.Ambiguity makeDeclaratorAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Declarator> alternatives) { 
org.meta_environment.rascal.ast.Declarator.Ambiguity amb = (org.meta_environment.rascal.ast.Declarator.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.Declarator.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.Declarator.Default makeDeclaratorDefault(INode node, org.meta_environment.rascal.ast.Type type, java.util.List<org.meta_environment.rascal.ast.Variable> variables) { 
org.meta_environment.rascal.ast.Declarator.Default x = (org.meta_environment.rascal.ast.Declarator.Default) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Declarator.Default(node, type, variables);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.LocalVariableDeclaration.Dynamic makeLocalVariableDeclarationDynamic(INode node, org.meta_environment.rascal.ast.Declarator declarator) { 
org.meta_environment.rascal.ast.LocalVariableDeclaration.Dynamic x = (org.meta_environment.rascal.ast.LocalVariableDeclaration.Dynamic) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.LocalVariableDeclaration.Dynamic(node, declarator);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.LocalVariableDeclaration.Ambiguity makeLocalVariableDeclarationAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.LocalVariableDeclaration> alternatives) { 
org.meta_environment.rascal.ast.LocalVariableDeclaration.Ambiguity amb = (org.meta_environment.rascal.ast.LocalVariableDeclaration.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.LocalVariableDeclaration.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.LocalVariableDeclaration.Default makeLocalVariableDeclarationDefault(INode node, org.meta_environment.rascal.ast.Declarator declarator) { 
org.meta_environment.rascal.ast.LocalVariableDeclaration.Default x = (org.meta_environment.rascal.ast.LocalVariableDeclaration.Default) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.LocalVariableDeclaration.Default(node, declarator);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Mapping.Ambiguity makeMappingAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Mapping> alternatives) { 
org.meta_environment.rascal.ast.Mapping.Ambiguity amb = (org.meta_environment.rascal.ast.Mapping.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.Mapping.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.Mapping.Default makeMappingDefault(INode node, org.meta_environment.rascal.ast.Expression from, org.meta_environment.rascal.ast.Expression to) { 
org.meta_environment.rascal.ast.Mapping.Default x = (org.meta_environment.rascal.ast.Mapping.Default) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Mapping.Default(node, from, to);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Strategy.Innermost makeStrategyInnermost(INode node) { 
org.meta_environment.rascal.ast.Strategy.Innermost x = (org.meta_environment.rascal.ast.Strategy.Innermost) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Strategy.Innermost(node);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Strategy.Outermost makeStrategyOutermost(INode node) { 
org.meta_environment.rascal.ast.Strategy.Outermost x = (org.meta_environment.rascal.ast.Strategy.Outermost) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Strategy.Outermost(node);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Strategy.BottomUpBreak makeStrategyBottomUpBreak(INode node) { 
org.meta_environment.rascal.ast.Strategy.BottomUpBreak x = (org.meta_environment.rascal.ast.Strategy.BottomUpBreak) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Strategy.BottomUpBreak(node);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Strategy.BottomUp makeStrategyBottomUp(INode node) { 
org.meta_environment.rascal.ast.Strategy.BottomUp x = (org.meta_environment.rascal.ast.Strategy.BottomUp) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Strategy.BottomUp(node);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Strategy.TopDownBreak makeStrategyTopDownBreak(INode node) { 
org.meta_environment.rascal.ast.Strategy.TopDownBreak x = (org.meta_environment.rascal.ast.Strategy.TopDownBreak) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Strategy.TopDownBreak(node);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Strategy.Ambiguity makeStrategyAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Strategy> alternatives) { 
org.meta_environment.rascal.ast.Strategy.Ambiguity amb = (org.meta_environment.rascal.ast.Strategy.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.Strategy.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.Strategy.TopDown makeStrategyTopDown(INode node) { 
org.meta_environment.rascal.ast.Strategy.TopDown x = (org.meta_environment.rascal.ast.Strategy.TopDown) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Strategy.TopDown(node);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Comprehension.Map makeComprehensionMap(INode node, org.meta_environment.rascal.ast.Expression from, org.meta_environment.rascal.ast.Expression to, java.util.List<org.meta_environment.rascal.ast.Expression> generators) { 
org.meta_environment.rascal.ast.Comprehension.Map x = (org.meta_environment.rascal.ast.Comprehension.Map) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Comprehension.Map(node, from, to, generators);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Comprehension.List makeComprehensionList(INode node, java.util.List<org.meta_environment.rascal.ast.Expression> results, java.util.List<org.meta_environment.rascal.ast.Expression> generators) { 
org.meta_environment.rascal.ast.Comprehension.List x = (org.meta_environment.rascal.ast.Comprehension.List) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Comprehension.List(node, results, generators);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Comprehension.Ambiguity makeComprehensionAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Comprehension> alternatives) { 
org.meta_environment.rascal.ast.Comprehension.Ambiguity amb = (org.meta_environment.rascal.ast.Comprehension.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.Comprehension.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.Comprehension.Set makeComprehensionSet(INode node, java.util.List<org.meta_environment.rascal.ast.Expression> results, java.util.List<org.meta_environment.rascal.ast.Expression> generators) { 
org.meta_environment.rascal.ast.Comprehension.Set x = (org.meta_environment.rascal.ast.Comprehension.Set) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Comprehension.Set(node, results, generators);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Replacement.Conditional makeReplacementConditional(INode node, org.meta_environment.rascal.ast.Expression replacementExpression, java.util.List<org.meta_environment.rascal.ast.Expression> conditions) { 
org.meta_environment.rascal.ast.Replacement.Conditional x = (org.meta_environment.rascal.ast.Replacement.Conditional) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Replacement.Conditional(node, replacementExpression, conditions);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Replacement.Ambiguity makeReplacementAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Replacement> alternatives) { 
org.meta_environment.rascal.ast.Replacement.Ambiguity amb = (org.meta_environment.rascal.ast.Replacement.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.Replacement.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.Replacement.Unconditional makeReplacementUnconditional(INode node, org.meta_environment.rascal.ast.Expression replacementExpression) { 
org.meta_environment.rascal.ast.Replacement.Unconditional x = (org.meta_environment.rascal.ast.Replacement.Unconditional) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Replacement.Unconditional(node, replacementExpression);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.PatternWithAction.Arbitrary makePatternWithActionArbitrary(INode node, org.meta_environment.rascal.ast.Expression pattern, org.meta_environment.rascal.ast.Statement statement) { 
org.meta_environment.rascal.ast.PatternWithAction.Arbitrary x = (org.meta_environment.rascal.ast.PatternWithAction.Arbitrary) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.PatternWithAction.Arbitrary(node, pattern, statement);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.PatternWithAction.Ambiguity makePatternWithActionAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.PatternWithAction> alternatives) { 
org.meta_environment.rascal.ast.PatternWithAction.Ambiguity amb = (org.meta_environment.rascal.ast.PatternWithAction.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.PatternWithAction.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.PatternWithAction.Replacing makePatternWithActionReplacing(INode node, org.meta_environment.rascal.ast.Expression pattern, org.meta_environment.rascal.ast.Replacement replacement) { 
org.meta_environment.rascal.ast.PatternWithAction.Replacing x = (org.meta_environment.rascal.ast.PatternWithAction.Replacing) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.PatternWithAction.Replacing(node, pattern, replacement);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Case.Default makeCaseDefault(INode node, org.meta_environment.rascal.ast.Statement statement) { 
org.meta_environment.rascal.ast.Case.Default x = (org.meta_environment.rascal.ast.Case.Default) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Case.Default(node, statement);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Case.Ambiguity makeCaseAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Case> alternatives) { 
org.meta_environment.rascal.ast.Case.Ambiguity amb = (org.meta_environment.rascal.ast.Case.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.Case.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.Case.PatternWithAction makeCasePatternWithAction(INode node, org.meta_environment.rascal.ast.PatternWithAction patternWithAction) { 
org.meta_environment.rascal.ast.Case.PatternWithAction x = (org.meta_environment.rascal.ast.Case.PatternWithAction) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Case.PatternWithAction(node, patternWithAction);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Visit.GivenStrategy makeVisitGivenStrategy(INode node, org.meta_environment.rascal.ast.Strategy strategy, org.meta_environment.rascal.ast.Expression subject, java.util.List<org.meta_environment.rascal.ast.Case> cases) { 
org.meta_environment.rascal.ast.Visit.GivenStrategy x = (org.meta_environment.rascal.ast.Visit.GivenStrategy) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Visit.GivenStrategy(node, strategy, subject, cases);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Visit.Ambiguity makeVisitAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Visit> alternatives) { 
org.meta_environment.rascal.ast.Visit.Ambiguity amb = (org.meta_environment.rascal.ast.Visit.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.Visit.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.Visit.DefaultStrategy makeVisitDefaultStrategy(INode node, org.meta_environment.rascal.ast.Expression subject, java.util.List<org.meta_environment.rascal.ast.Case> cases) { 
org.meta_environment.rascal.ast.Visit.DefaultStrategy x = (org.meta_environment.rascal.ast.Visit.DefaultStrategy) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Visit.DefaultStrategy(node, subject, cases);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Visibility.Private makeVisibilityPrivate(INode node) { 
org.meta_environment.rascal.ast.Visibility.Private x = (org.meta_environment.rascal.ast.Visibility.Private) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Visibility.Private(node);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Visibility.Ambiguity makeVisibilityAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Visibility> alternatives) { 
org.meta_environment.rascal.ast.Visibility.Ambiguity amb = (org.meta_environment.rascal.ast.Visibility.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.Visibility.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.Visibility.Public makeVisibilityPublic(INode node) { 
org.meta_environment.rascal.ast.Visibility.Public x = (org.meta_environment.rascal.ast.Visibility.Public) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Visibility.Public(node);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Toplevel.DefaultVisibility makeToplevelDefaultVisibility(INode node, org.meta_environment.rascal.ast.Declaration declaration) { 
org.meta_environment.rascal.ast.Toplevel.DefaultVisibility x = (org.meta_environment.rascal.ast.Toplevel.DefaultVisibility) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Toplevel.DefaultVisibility(node, declaration);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Toplevel.Ambiguity makeToplevelAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Toplevel> alternatives) { 
org.meta_environment.rascal.ast.Toplevel.Ambiguity amb = (org.meta_environment.rascal.ast.Toplevel.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.Toplevel.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.Toplevel.GivenVisibility makeToplevelGivenVisibility(INode node, org.meta_environment.rascal.ast.Visibility visibility, org.meta_environment.rascal.ast.Declaration declaration) { 
org.meta_environment.rascal.ast.Toplevel.GivenVisibility x = (org.meta_environment.rascal.ast.Toplevel.GivenVisibility) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Toplevel.GivenVisibility(node, visibility, declaration);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Declaration.Tag makeDeclarationTag(INode node, org.meta_environment.rascal.ast.Kind kind, org.meta_environment.rascal.ast.Name name, org.meta_environment.rascal.ast.Tags tags, java.util.List<org.meta_environment.rascal.ast.Type> types) { 
org.meta_environment.rascal.ast.Declaration.Tag x = (org.meta_environment.rascal.ast.Declaration.Tag) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Declaration.Tag(node, kind, name, tags, types);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Declaration.Annotation makeDeclarationAnnotation(INode node, org.meta_environment.rascal.ast.Type annoType, org.meta_environment.rascal.ast.Type onType, org.meta_environment.rascal.ast.Name name, org.meta_environment.rascal.ast.Tags tags) { 
org.meta_environment.rascal.ast.Declaration.Annotation x = (org.meta_environment.rascal.ast.Declaration.Annotation) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Declaration.Annotation(node, annoType, onType, name, tags);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Declaration.Rule makeDeclarationRule(INode node, org.meta_environment.rascal.ast.Name name, org.meta_environment.rascal.ast.Tags tags, org.meta_environment.rascal.ast.PatternWithAction patternAction) { 
org.meta_environment.rascal.ast.Declaration.Rule x = (org.meta_environment.rascal.ast.Declaration.Rule) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Declaration.Rule(node, name, tags, patternAction);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Declaration.Variable makeDeclarationVariable(INode node, org.meta_environment.rascal.ast.Type type, java.util.List<org.meta_environment.rascal.ast.Variable> variables) { 
org.meta_environment.rascal.ast.Declaration.Variable x = (org.meta_environment.rascal.ast.Declaration.Variable) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Declaration.Variable(node, type, variables);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Declaration.Function makeDeclarationFunction(INode node, org.meta_environment.rascal.ast.FunctionDeclaration functionDeclaration) { 
org.meta_environment.rascal.ast.Declaration.Function x = (org.meta_environment.rascal.ast.Declaration.Function) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Declaration.Function(node, functionDeclaration);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Declaration.Data makeDeclarationData(INode node, org.meta_environment.rascal.ast.UserType user, org.meta_environment.rascal.ast.Tags tags, java.util.List<org.meta_environment.rascal.ast.Variant> variants) { 
org.meta_environment.rascal.ast.Declaration.Data x = (org.meta_environment.rascal.ast.Declaration.Data) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Declaration.Data(node, user, tags, variants);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Declaration.Alias makeDeclarationAlias(INode node, org.meta_environment.rascal.ast.UserType user, org.meta_environment.rascal.ast.Type base, org.meta_environment.rascal.ast.Tags tags) { 
org.meta_environment.rascal.ast.Declaration.Alias x = (org.meta_environment.rascal.ast.Declaration.Alias) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Declaration.Alias(node, user, base, tags);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Declaration.Ambiguity makeDeclarationAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Declaration> alternatives) { 
org.meta_environment.rascal.ast.Declaration.Ambiguity amb = (org.meta_environment.rascal.ast.Declaration.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.Declaration.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.Declaration.View makeDeclarationView(INode node, org.meta_environment.rascal.ast.Name view, org.meta_environment.rascal.ast.Name superType, org.meta_environment.rascal.ast.Tags tags, java.util.List<org.meta_environment.rascal.ast.Alternative> alts) { 
org.meta_environment.rascal.ast.Declaration.View x = (org.meta_environment.rascal.ast.Declaration.View) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Declaration.View(node, view, superType, tags, alts);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Alternative.Ambiguity makeAlternativeAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Alternative> alternatives) { 
org.meta_environment.rascal.ast.Alternative.Ambiguity amb = (org.meta_environment.rascal.ast.Alternative.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.Alternative.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.Alternative.NamedType makeAlternativeNamedType(INode node, org.meta_environment.rascal.ast.Name name, org.meta_environment.rascal.ast.Type type) { 
org.meta_environment.rascal.ast.Alternative.NamedType x = (org.meta_environment.rascal.ast.Alternative.NamedType) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Alternative.NamedType(node, name, type);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Variant.NillaryConstructor makeVariantNillaryConstructor(INode node, org.meta_environment.rascal.ast.Name name) { 
org.meta_environment.rascal.ast.Variant.NillaryConstructor x = (org.meta_environment.rascal.ast.Variant.NillaryConstructor) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Variant.NillaryConstructor(node, name);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Variant.Ambiguity makeVariantAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Variant> alternatives) { 
org.meta_environment.rascal.ast.Variant.Ambiguity amb = (org.meta_environment.rascal.ast.Variant.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.Variant.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.Variant.NAryConstructor makeVariantNAryConstructor(INode node, org.meta_environment.rascal.ast.Name name, java.util.List<org.meta_environment.rascal.ast.TypeArg> arguments) { 
org.meta_environment.rascal.ast.Variant.NAryConstructor x = (org.meta_environment.rascal.ast.Variant.NAryConstructor) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Variant.NAryConstructor(node, name, arguments);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.FunctionModifier.Ambiguity makeFunctionModifierAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.FunctionModifier> alternatives) { 
org.meta_environment.rascal.ast.FunctionModifier.Ambiguity amb = (org.meta_environment.rascal.ast.FunctionModifier.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.FunctionModifier.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.FunctionModifier.Java makeFunctionModifierJava(INode node) { 
org.meta_environment.rascal.ast.FunctionModifier.Java x = (org.meta_environment.rascal.ast.FunctionModifier.Java) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.FunctionModifier.Java(node);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.FunctionModifiers.Ambiguity makeFunctionModifiersAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.FunctionModifiers> alternatives) { 
org.meta_environment.rascal.ast.FunctionModifiers.Ambiguity amb = (org.meta_environment.rascal.ast.FunctionModifiers.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.FunctionModifiers.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.FunctionModifiers.List makeFunctionModifiersList(INode node, java.util.List<org.meta_environment.rascal.ast.FunctionModifier> modifiers) { 
org.meta_environment.rascal.ast.FunctionModifiers.List x = (org.meta_environment.rascal.ast.FunctionModifiers.List) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.FunctionModifiers.List(node, modifiers);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Signature.WithThrows makeSignatureWithThrows(INode node, org.meta_environment.rascal.ast.Type type, org.meta_environment.rascal.ast.FunctionModifiers modifiers, org.meta_environment.rascal.ast.Name name, org.meta_environment.rascal.ast.Parameters parameters, java.util.List<org.meta_environment.rascal.ast.Type> exceptions) { 
org.meta_environment.rascal.ast.Signature.WithThrows x = (org.meta_environment.rascal.ast.Signature.WithThrows) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Signature.WithThrows(node, type, modifiers, name, parameters, exceptions);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Signature.Ambiguity makeSignatureAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Signature> alternatives) { 
org.meta_environment.rascal.ast.Signature.Ambiguity amb = (org.meta_environment.rascal.ast.Signature.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.Signature.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.Signature.NoThrows makeSignatureNoThrows(INode node, org.meta_environment.rascal.ast.Type type, org.meta_environment.rascal.ast.FunctionModifiers modifiers, org.meta_environment.rascal.ast.Name name, org.meta_environment.rascal.ast.Parameters parameters) { 
org.meta_environment.rascal.ast.Signature.NoThrows x = (org.meta_environment.rascal.ast.Signature.NoThrows) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Signature.NoThrows(node, type, modifiers, name, parameters);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.FunctionDeclaration.Abstract makeFunctionDeclarationAbstract(INode node, org.meta_environment.rascal.ast.Signature signature, org.meta_environment.rascal.ast.Tags tags) { 
org.meta_environment.rascal.ast.FunctionDeclaration.Abstract x = (org.meta_environment.rascal.ast.FunctionDeclaration.Abstract) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.FunctionDeclaration.Abstract(node, signature, tags);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.FunctionDeclaration.Ambiguity makeFunctionDeclarationAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.FunctionDeclaration> alternatives) { 
org.meta_environment.rascal.ast.FunctionDeclaration.Ambiguity amb = (org.meta_environment.rascal.ast.FunctionDeclaration.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.FunctionDeclaration.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.FunctionDeclaration.Default makeFunctionDeclarationDefault(INode node, org.meta_environment.rascal.ast.Signature signature, org.meta_environment.rascal.ast.Tags tags, org.meta_environment.rascal.ast.FunctionBody body) { 
org.meta_environment.rascal.ast.FunctionDeclaration.Default x = (org.meta_environment.rascal.ast.FunctionDeclaration.Default) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.FunctionDeclaration.Default(node, signature, tags, body);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.FunctionBody.Ambiguity makeFunctionBodyAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.FunctionBody> alternatives) { 
org.meta_environment.rascal.ast.FunctionBody.Ambiguity amb = (org.meta_environment.rascal.ast.FunctionBody.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.FunctionBody.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.FunctionBody.Default makeFunctionBodyDefault(INode node, java.util.List<org.meta_environment.rascal.ast.Statement> statements) { 
org.meta_environment.rascal.ast.FunctionBody.Default x = (org.meta_environment.rascal.ast.FunctionBody.Default) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.FunctionBody.Default(node, statements);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Variable.Initialized makeVariableInitialized(INode node, org.meta_environment.rascal.ast.Name name, org.meta_environment.rascal.ast.Tags tags, org.meta_environment.rascal.ast.Expression initial) { 
org.meta_environment.rascal.ast.Variable.Initialized x = (org.meta_environment.rascal.ast.Variable.Initialized) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Variable.Initialized(node, name, tags, initial);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Variable.Ambiguity makeVariableAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Variable> alternatives) { 
org.meta_environment.rascal.ast.Variable.Ambiguity amb = (org.meta_environment.rascal.ast.Variable.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.Variable.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.Variable.UnInitialized makeVariableUnInitialized(INode node, org.meta_environment.rascal.ast.Name name, org.meta_environment.rascal.ast.Tags tags) { 
org.meta_environment.rascal.ast.Variable.UnInitialized x = (org.meta_environment.rascal.ast.Variable.UnInitialized) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Variable.UnInitialized(node, name, tags);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Kind.All makeKindAll(INode node) { 
org.meta_environment.rascal.ast.Kind.All x = (org.meta_environment.rascal.ast.Kind.All) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Kind.All(node);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Kind.Tag makeKindTag(INode node) { 
org.meta_environment.rascal.ast.Kind.Tag x = (org.meta_environment.rascal.ast.Kind.Tag) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Kind.Tag(node);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Kind.Anno makeKindAnno(INode node) { 
org.meta_environment.rascal.ast.Kind.Anno x = (org.meta_environment.rascal.ast.Kind.Anno) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Kind.Anno(node);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Kind.Alias makeKindAlias(INode node) { 
org.meta_environment.rascal.ast.Kind.Alias x = (org.meta_environment.rascal.ast.Kind.Alias) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Kind.Alias(node);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Kind.View makeKindView(INode node) { 
org.meta_environment.rascal.ast.Kind.View x = (org.meta_environment.rascal.ast.Kind.View) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Kind.View(node);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Kind.Data makeKindData(INode node) { 
org.meta_environment.rascal.ast.Kind.Data x = (org.meta_environment.rascal.ast.Kind.Data) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Kind.Data(node);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Kind.Variable makeKindVariable(INode node) { 
org.meta_environment.rascal.ast.Kind.Variable x = (org.meta_environment.rascal.ast.Kind.Variable) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Kind.Variable(node);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Kind.Rule makeKindRule(INode node) { 
org.meta_environment.rascal.ast.Kind.Rule x = (org.meta_environment.rascal.ast.Kind.Rule) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Kind.Rule(node);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Kind.Function makeKindFunction(INode node) { 
org.meta_environment.rascal.ast.Kind.Function x = (org.meta_environment.rascal.ast.Kind.Function) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Kind.Function(node);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Kind.Ambiguity makeKindAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Kind> alternatives) { 
org.meta_environment.rascal.ast.Kind.Ambiguity amb = (org.meta_environment.rascal.ast.Kind.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.Kind.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.Kind.Module makeKindModule(INode node) { 
org.meta_environment.rascal.ast.Kind.Module x = (org.meta_environment.rascal.ast.Kind.Module) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Kind.Module(node);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Comment.Ambiguity makeCommentAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Comment> alternatives) { 
org.meta_environment.rascal.ast.Comment.Ambiguity amb = (org.meta_environment.rascal.ast.Comment.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.Comment.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.Comment.Lexical makeCommentLexical(INode node, String string) { 
org.meta_environment.rascal.ast.Comment.Lexical x = (org.meta_environment.rascal.ast.Comment.Lexical) lexCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Comment.Lexical(node, string);
          lexCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.CommentChar.Ambiguity makeCommentCharAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.CommentChar> alternatives) { 
org.meta_environment.rascal.ast.CommentChar.Ambiguity amb = (org.meta_environment.rascal.ast.CommentChar.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.CommentChar.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.CommentChar.Lexical makeCommentCharLexical(INode node, String string) { 
org.meta_environment.rascal.ast.CommentChar.Lexical x = (org.meta_environment.rascal.ast.CommentChar.Lexical) lexCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.CommentChar.Lexical(node, string);
          lexCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Asterisk.Ambiguity makeAsteriskAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Asterisk> alternatives) { 
org.meta_environment.rascal.ast.Asterisk.Ambiguity amb = (org.meta_environment.rascal.ast.Asterisk.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.Asterisk.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.Asterisk.Lexical makeAsteriskLexical(INode node, String string) { 
org.meta_environment.rascal.ast.Asterisk.Lexical x = (org.meta_environment.rascal.ast.Asterisk.Lexical) lexCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Asterisk.Lexical(node, string);
          lexCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.StrChar.Lexical makeStrCharLexical(INode node, String string) { 
org.meta_environment.rascal.ast.StrChar.Lexical x = (org.meta_environment.rascal.ast.StrChar.Lexical) lexCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.StrChar.Lexical(node, string);
          lexCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.StrChar.Ambiguity makeStrCharAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.StrChar> alternatives) { 
org.meta_environment.rascal.ast.StrChar.Ambiguity amb = (org.meta_environment.rascal.ast.StrChar.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.StrChar.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.StrChar.newline makeStrCharnewline(INode node) { 
org.meta_environment.rascal.ast.StrChar.newline x = (org.meta_environment.rascal.ast.StrChar.newline) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.StrChar.newline(node);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.StrCon.Ambiguity makeStrConAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.StrCon> alternatives) { 
org.meta_environment.rascal.ast.StrCon.Ambiguity amb = (org.meta_environment.rascal.ast.StrCon.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.StrCon.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.StrCon.Lexical makeStrConLexical(INode node, String string) { 
org.meta_environment.rascal.ast.StrCon.Lexical x = (org.meta_environment.rascal.ast.StrCon.Lexical) lexCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.StrCon.Lexical(node, string);
          lexCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.SingleQuotedStrChar.Ambiguity makeSingleQuotedStrCharAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.SingleQuotedStrChar> alternatives) { 
org.meta_environment.rascal.ast.SingleQuotedStrChar.Ambiguity amb = (org.meta_environment.rascal.ast.SingleQuotedStrChar.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.SingleQuotedStrChar.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.SingleQuotedStrChar.Lexical makeSingleQuotedStrCharLexical(INode node, String string) { 
org.meta_environment.rascal.ast.SingleQuotedStrChar.Lexical x = (org.meta_environment.rascal.ast.SingleQuotedStrChar.Lexical) lexCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.SingleQuotedStrChar.Lexical(node, string);
          lexCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.SingleQuotedStrCon.Ambiguity makeSingleQuotedStrConAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.SingleQuotedStrCon> alternatives) { 
org.meta_environment.rascal.ast.SingleQuotedStrCon.Ambiguity amb = (org.meta_environment.rascal.ast.SingleQuotedStrCon.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.SingleQuotedStrCon.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.SingleQuotedStrCon.Lexical makeSingleQuotedStrConLexical(INode node, String string) { 
org.meta_environment.rascal.ast.SingleQuotedStrCon.Lexical x = (org.meta_environment.rascal.ast.SingleQuotedStrCon.Lexical) lexCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.SingleQuotedStrCon.Lexical(node, string);
          lexCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Symbol.Sort makeSymbolSort(INode node, org.meta_environment.rascal.ast.Name name) { 
org.meta_environment.rascal.ast.Symbol.Sort x = (org.meta_environment.rascal.ast.Symbol.Sort) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Symbol.Sort(node, name);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Symbol.CaseInsensitiveLiteral makeSymbolCaseInsensitiveLiteral(INode node, org.meta_environment.rascal.ast.SingleQuotedStrCon singelQuotedString) { 
org.meta_environment.rascal.ast.Symbol.CaseInsensitiveLiteral x = (org.meta_environment.rascal.ast.Symbol.CaseInsensitiveLiteral) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Symbol.CaseInsensitiveLiteral(node, singelQuotedString);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Symbol.Literal makeSymbolLiteral(INode node, org.meta_environment.rascal.ast.StrCon string) { 
org.meta_environment.rascal.ast.Symbol.Literal x = (org.meta_environment.rascal.ast.Symbol.Literal) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Symbol.Literal(node, string);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Symbol.CharacterClass makeSymbolCharacterClass(INode node, org.meta_environment.rascal.ast.CharClass charClass) { 
org.meta_environment.rascal.ast.Symbol.CharacterClass x = (org.meta_environment.rascal.ast.Symbol.CharacterClass) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Symbol.CharacterClass(node, charClass);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Symbol.Alternative makeSymbolAlternative(INode node, org.meta_environment.rascal.ast.Symbol lhs, org.meta_environment.rascal.ast.Symbol rhs) { 
org.meta_environment.rascal.ast.Symbol.Alternative x = (org.meta_environment.rascal.ast.Symbol.Alternative) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Symbol.Alternative(node, lhs, rhs);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Symbol.IterStarSep makeSymbolIterStarSep(INode node, org.meta_environment.rascal.ast.Symbol symbol, org.meta_environment.rascal.ast.StrCon sep) { 
org.meta_environment.rascal.ast.Symbol.IterStarSep x = (org.meta_environment.rascal.ast.Symbol.IterStarSep) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Symbol.IterStarSep(node, symbol, sep);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Symbol.IterSep makeSymbolIterSep(INode node, org.meta_environment.rascal.ast.Symbol symbol, org.meta_environment.rascal.ast.StrCon sep) { 
org.meta_environment.rascal.ast.Symbol.IterSep x = (org.meta_environment.rascal.ast.Symbol.IterSep) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Symbol.IterSep(node, symbol, sep);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Symbol.IterStar makeSymbolIterStar(INode node, org.meta_environment.rascal.ast.Symbol symbol) { 
org.meta_environment.rascal.ast.Symbol.IterStar x = (org.meta_environment.rascal.ast.Symbol.IterStar) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Symbol.IterStar(node, symbol);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Symbol.Iter makeSymbolIter(INode node, org.meta_environment.rascal.ast.Symbol symbol) { 
org.meta_environment.rascal.ast.Symbol.Iter x = (org.meta_environment.rascal.ast.Symbol.Iter) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Symbol.Iter(node, symbol);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Symbol.Optional makeSymbolOptional(INode node, org.meta_environment.rascal.ast.Symbol symbol) { 
org.meta_environment.rascal.ast.Symbol.Optional x = (org.meta_environment.rascal.ast.Symbol.Optional) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Symbol.Optional(node, symbol);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Symbol.Sequence makeSymbolSequence(INode node, org.meta_environment.rascal.ast.Symbol head, java.util.List<org.meta_environment.rascal.ast.Symbol> tail) { 
org.meta_environment.rascal.ast.Symbol.Sequence x = (org.meta_environment.rascal.ast.Symbol.Sequence) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Symbol.Sequence(node, head, tail);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Symbol.Ambiguity makeSymbolAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Symbol> alternatives) { 
org.meta_environment.rascal.ast.Symbol.Ambiguity amb = (org.meta_environment.rascal.ast.Symbol.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.Symbol.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.Symbol.Empty makeSymbolEmpty(INode node) { 
org.meta_environment.rascal.ast.Symbol.Empty x = (org.meta_environment.rascal.ast.Symbol.Empty) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Symbol.Empty(node);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Type.Bracket makeTypeBracket(INode node, org.meta_environment.rascal.ast.Type type) { 
org.meta_environment.rascal.ast.Type.Bracket x = (org.meta_environment.rascal.ast.Type.Bracket) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Type.Bracket(node, type);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Type.Selector makeTypeSelector(INode node, org.meta_environment.rascal.ast.DataTypeSelector selector) { 
org.meta_environment.rascal.ast.Type.Selector x = (org.meta_environment.rascal.ast.Type.Selector) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Type.Selector(node, selector);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Type.User makeTypeUser(INode node, org.meta_environment.rascal.ast.UserType user) { 
org.meta_environment.rascal.ast.Type.User x = (org.meta_environment.rascal.ast.Type.User) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Type.User(node, user);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Type.Variable makeTypeVariable(INode node, org.meta_environment.rascal.ast.TypeVar typeVar) { 
org.meta_environment.rascal.ast.Type.Variable x = (org.meta_environment.rascal.ast.Type.Variable) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Type.Variable(node, typeVar);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Type.Function makeTypeFunction(INode node, org.meta_environment.rascal.ast.FunctionType function) { 
org.meta_environment.rascal.ast.Type.Function x = (org.meta_environment.rascal.ast.Type.Function) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Type.Function(node, function);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Type.Structured makeTypeStructured(INode node, org.meta_environment.rascal.ast.StructuredType structured) { 
org.meta_environment.rascal.ast.Type.Structured x = (org.meta_environment.rascal.ast.Type.Structured) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Type.Structured(node, structured);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Type.Basic makeTypeBasic(INode node, org.meta_environment.rascal.ast.BasicType basic) { 
org.meta_environment.rascal.ast.Type.Basic x = (org.meta_environment.rascal.ast.Type.Basic) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Type.Basic(node, basic);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Type.Ambiguity makeTypeAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Type> alternatives) { 
org.meta_environment.rascal.ast.Type.Ambiguity amb = (org.meta_environment.rascal.ast.Type.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.Type.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.Type.Symbol makeTypeSymbol(INode node, org.meta_environment.rascal.ast.Symbol symbol) { 
org.meta_environment.rascal.ast.Type.Symbol x = (org.meta_environment.rascal.ast.Type.Symbol) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Type.Symbol(node, symbol);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.CharRange.Range makeCharRangeRange(INode node, org.meta_environment.rascal.ast.Character start, org.meta_environment.rascal.ast.Character end) { 
org.meta_environment.rascal.ast.CharRange.Range x = (org.meta_environment.rascal.ast.CharRange.Range) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.CharRange.Range(node, start, end);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.CharRange.Ambiguity makeCharRangeAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.CharRange> alternatives) { 
org.meta_environment.rascal.ast.CharRange.Ambiguity amb = (org.meta_environment.rascal.ast.CharRange.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.CharRange.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.CharRange.Character makeCharRangeCharacter(INode node, org.meta_environment.rascal.ast.Character character) { 
org.meta_environment.rascal.ast.CharRange.Character x = (org.meta_environment.rascal.ast.CharRange.Character) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.CharRange.Character(node, character);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.CharRanges.Bracket makeCharRangesBracket(INode node, org.meta_environment.rascal.ast.CharRanges ranges) { 
org.meta_environment.rascal.ast.CharRanges.Bracket x = (org.meta_environment.rascal.ast.CharRanges.Bracket) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.CharRanges.Bracket(node, ranges);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.CharRanges.Concatenate makeCharRangesConcatenate(INode node, org.meta_environment.rascal.ast.CharRanges lhs, org.meta_environment.rascal.ast.CharRanges rhs) { 
org.meta_environment.rascal.ast.CharRanges.Concatenate x = (org.meta_environment.rascal.ast.CharRanges.Concatenate) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.CharRanges.Concatenate(node, lhs, rhs);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.CharRanges.Ambiguity makeCharRangesAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.CharRanges> alternatives) { 
org.meta_environment.rascal.ast.CharRanges.Ambiguity amb = (org.meta_environment.rascal.ast.CharRanges.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.CharRanges.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.CharRanges.Range makeCharRangesRange(INode node, org.meta_environment.rascal.ast.CharRange range) { 
org.meta_environment.rascal.ast.CharRanges.Range x = (org.meta_environment.rascal.ast.CharRanges.Range) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.CharRanges.Range(node, range);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.OptCharRanges.Present makeOptCharRangesPresent(INode node, org.meta_environment.rascal.ast.CharRanges ranges) { 
org.meta_environment.rascal.ast.OptCharRanges.Present x = (org.meta_environment.rascal.ast.OptCharRanges.Present) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.OptCharRanges.Present(node, ranges);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.OptCharRanges.Ambiguity makeOptCharRangesAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.OptCharRanges> alternatives) { 
org.meta_environment.rascal.ast.OptCharRanges.Ambiguity amb = (org.meta_environment.rascal.ast.OptCharRanges.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.OptCharRanges.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.OptCharRanges.Absent makeOptCharRangesAbsent(INode node) { 
org.meta_environment.rascal.ast.OptCharRanges.Absent x = (org.meta_environment.rascal.ast.OptCharRanges.Absent) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.OptCharRanges.Absent(node);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.CharClass.Union makeCharClassUnion(INode node, org.meta_environment.rascal.ast.CharClass lhs, org.meta_environment.rascal.ast.CharClass rhs) { 
org.meta_environment.rascal.ast.CharClass.Union x = (org.meta_environment.rascal.ast.CharClass.Union) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.CharClass.Union(node, lhs, rhs);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.CharClass.Intersection makeCharClassIntersection(INode node, org.meta_environment.rascal.ast.CharClass lhs, org.meta_environment.rascal.ast.CharClass rhs) { 
org.meta_environment.rascal.ast.CharClass.Intersection x = (org.meta_environment.rascal.ast.CharClass.Intersection) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.CharClass.Intersection(node, lhs, rhs);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.CharClass.Difference makeCharClassDifference(INode node, org.meta_environment.rascal.ast.CharClass lhs, org.meta_environment.rascal.ast.CharClass rhs) { 
org.meta_environment.rascal.ast.CharClass.Difference x = (org.meta_environment.rascal.ast.CharClass.Difference) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.CharClass.Difference(node, lhs, rhs);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.CharClass.Complement makeCharClassComplement(INode node, org.meta_environment.rascal.ast.CharClass charClass) { 
org.meta_environment.rascal.ast.CharClass.Complement x = (org.meta_environment.rascal.ast.CharClass.Complement) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.CharClass.Complement(node, charClass);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.CharClass.Bracket makeCharClassBracket(INode node, org.meta_environment.rascal.ast.CharClass charClass) { 
org.meta_environment.rascal.ast.CharClass.Bracket x = (org.meta_environment.rascal.ast.CharClass.Bracket) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.CharClass.Bracket(node, charClass);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.CharClass.Ambiguity makeCharClassAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.CharClass> alternatives) { 
org.meta_environment.rascal.ast.CharClass.Ambiguity amb = (org.meta_environment.rascal.ast.CharClass.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.CharClass.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.CharClass.SimpleCharclass makeCharClassSimpleCharclass(INode node, org.meta_environment.rascal.ast.OptCharRanges optionalCharRanges) { 
org.meta_environment.rascal.ast.CharClass.SimpleCharclass x = (org.meta_environment.rascal.ast.CharClass.SimpleCharclass) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.CharClass.SimpleCharclass(node, optionalCharRanges);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.NumChar.Ambiguity makeNumCharAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.NumChar> alternatives) { 
org.meta_environment.rascal.ast.NumChar.Ambiguity amb = (org.meta_environment.rascal.ast.NumChar.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.NumChar.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.NumChar.Lexical makeNumCharLexical(INode node, String string) { 
org.meta_environment.rascal.ast.NumChar.Lexical x = (org.meta_environment.rascal.ast.NumChar.Lexical) lexCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.NumChar.Lexical(node, string);
          lexCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.ShortChar.Ambiguity makeShortCharAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.ShortChar> alternatives) { 
org.meta_environment.rascal.ast.ShortChar.Ambiguity amb = (org.meta_environment.rascal.ast.ShortChar.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.ShortChar.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.ShortChar.Lexical makeShortCharLexical(INode node, String string) { 
org.meta_environment.rascal.ast.ShortChar.Lexical x = (org.meta_environment.rascal.ast.ShortChar.Lexical) lexCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.ShortChar.Lexical(node, string);
          lexCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Character.Bottom makeCharacterBottom(INode node) { 
org.meta_environment.rascal.ast.Character.Bottom x = (org.meta_environment.rascal.ast.Character.Bottom) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Character.Bottom(node);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Character.EOF makeCharacterEOF(INode node) { 
org.meta_environment.rascal.ast.Character.EOF x = (org.meta_environment.rascal.ast.Character.EOF) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Character.EOF(node);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Character.Top makeCharacterTop(INode node) { 
org.meta_environment.rascal.ast.Character.Top x = (org.meta_environment.rascal.ast.Character.Top) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Character.Top(node);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Character.Short makeCharacterShort(INode node, org.meta_environment.rascal.ast.ShortChar shortChar) { 
org.meta_environment.rascal.ast.Character.Short x = (org.meta_environment.rascal.ast.Character.Short) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Character.Short(node, shortChar);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Character.Ambiguity makeCharacterAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Character> alternatives) { 
org.meta_environment.rascal.ast.Character.Ambiguity amb = (org.meta_environment.rascal.ast.Character.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.Character.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.Character.Numeric makeCharacterNumeric(INode node, org.meta_environment.rascal.ast.NumChar numChar) { 
org.meta_environment.rascal.ast.Character.Numeric x = (org.meta_environment.rascal.ast.Character.Numeric) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Character.Numeric(node, numChar);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.URLLiteral.Ambiguity makeURLLiteralAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.URLLiteral> alternatives) { 
org.meta_environment.rascal.ast.URLLiteral.Ambiguity amb = (org.meta_environment.rascal.ast.URLLiteral.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.URLLiteral.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.URLLiteral.Lexical makeURLLiteralLexical(INode node, String string) { 
org.meta_environment.rascal.ast.URLLiteral.Lexical x = (org.meta_environment.rascal.ast.URLLiteral.Lexical) lexCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.URLLiteral.Lexical(node, string);
          lexCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.URL.Ambiguity makeURLAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.URL> alternatives) { 
org.meta_environment.rascal.ast.URL.Ambiguity amb = (org.meta_environment.rascal.ast.URL.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.URL.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.URL.Default makeURLDefault(INode node, org.meta_environment.rascal.ast.URLLiteral urlliteral) { 
org.meta_environment.rascal.ast.URL.Default x = (org.meta_environment.rascal.ast.URL.Default) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.URL.Default(node, urlliteral);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Marker.Ambiguity makeMarkerAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Marker> alternatives) { 
org.meta_environment.rascal.ast.Marker.Ambiguity amb = (org.meta_environment.rascal.ast.Marker.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.Marker.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.Marker.Lexical makeMarkerLexical(INode node, String string) { 
org.meta_environment.rascal.ast.Marker.Lexical x = (org.meta_environment.rascal.ast.Marker.Lexical) lexCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Marker.Lexical(node, string);
          lexCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Rest.Ambiguity makeRestAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Rest> alternatives) { 
org.meta_environment.rascal.ast.Rest.Ambiguity amb = (org.meta_environment.rascal.ast.Rest.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.Rest.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.Rest.Lexical makeRestLexical(INode node, String string) { 
org.meta_environment.rascal.ast.Rest.Lexical x = (org.meta_environment.rascal.ast.Rest.Lexical) lexCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Rest.Lexical(node, string);
          lexCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Body.Toplevels makeBodyToplevels(INode node, java.util.List<org.meta_environment.rascal.ast.Toplevel> toplevels) { 
org.meta_environment.rascal.ast.Body.Toplevels x = (org.meta_environment.rascal.ast.Body.Toplevels) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Body.Toplevels(node, toplevels);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Body.Ambiguity makeBodyAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Body> alternatives) { 
org.meta_environment.rascal.ast.Body.Ambiguity amb = (org.meta_environment.rascal.ast.Body.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.Body.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.Body.Anything makeBodyAnything(INode node, org.meta_environment.rascal.ast.Marker marker, org.meta_environment.rascal.ast.Rest rest) { 
org.meta_environment.rascal.ast.Body.Anything x = (org.meta_environment.rascal.ast.Body.Anything) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Body.Anything(node, marker, rest);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.BasicType.Area makeBasicTypeArea(INode node) { 
org.meta_environment.rascal.ast.BasicType.Area x = (org.meta_environment.rascal.ast.BasicType.Area) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.BasicType.Area(node);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.BasicType.Loc makeBasicTypeLoc(INode node) { 
org.meta_environment.rascal.ast.BasicType.Loc x = (org.meta_environment.rascal.ast.BasicType.Loc) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.BasicType.Loc(node);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.BasicType.Void makeBasicTypeVoid(INode node) { 
org.meta_environment.rascal.ast.BasicType.Void x = (org.meta_environment.rascal.ast.BasicType.Void) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.BasicType.Void(node);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.BasicType.Node makeBasicTypeNode(INode node) { 
org.meta_environment.rascal.ast.BasicType.Node x = (org.meta_environment.rascal.ast.BasicType.Node) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.BasicType.Node(node);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.BasicType.Value makeBasicTypeValue(INode node) { 
org.meta_environment.rascal.ast.BasicType.Value x = (org.meta_environment.rascal.ast.BasicType.Value) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.BasicType.Value(node);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.BasicType.String makeBasicTypeString(INode node) { 
org.meta_environment.rascal.ast.BasicType.String x = (org.meta_environment.rascal.ast.BasicType.String) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.BasicType.String(node);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.BasicType.Real makeBasicTypeReal(INode node) { 
org.meta_environment.rascal.ast.BasicType.Real x = (org.meta_environment.rascal.ast.BasicType.Real) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.BasicType.Real(node);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.BasicType.Int makeBasicTypeInt(INode node) { 
org.meta_environment.rascal.ast.BasicType.Int x = (org.meta_environment.rascal.ast.BasicType.Int) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.BasicType.Int(node);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.BasicType.Ambiguity makeBasicTypeAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.BasicType> alternatives) { 
org.meta_environment.rascal.ast.BasicType.Ambiguity amb = (org.meta_environment.rascal.ast.BasicType.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.BasicType.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.BasicType.Bool makeBasicTypeBool(INode node) { 
org.meta_environment.rascal.ast.BasicType.Bool x = (org.meta_environment.rascal.ast.BasicType.Bool) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.BasicType.Bool(node);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.TypeArg.Named makeTypeArgNamed(INode node, org.meta_environment.rascal.ast.Type type, org.meta_environment.rascal.ast.Name name) { 
org.meta_environment.rascal.ast.TypeArg.Named x = (org.meta_environment.rascal.ast.TypeArg.Named) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.TypeArg.Named(node, type, name);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.TypeArg.Ambiguity makeTypeArgAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.TypeArg> alternatives) { 
org.meta_environment.rascal.ast.TypeArg.Ambiguity amb = (org.meta_environment.rascal.ast.TypeArg.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.TypeArg.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.TypeArg.Default makeTypeArgDefault(INode node, org.meta_environment.rascal.ast.Type type) { 
org.meta_environment.rascal.ast.TypeArg.Default x = (org.meta_environment.rascal.ast.TypeArg.Default) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.TypeArg.Default(node, type);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.StructuredType.Lex makeStructuredTypeLex(INode node, org.meta_environment.rascal.ast.TypeArg typeArg) { 
org.meta_environment.rascal.ast.StructuredType.Lex x = (org.meta_environment.rascal.ast.StructuredType.Lex) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.StructuredType.Lex(node, typeArg);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.StructuredType.Tuple makeStructuredTypeTuple(INode node, java.util.List<org.meta_environment.rascal.ast.TypeArg> arguments) { 
org.meta_environment.rascal.ast.StructuredType.Tuple x = (org.meta_environment.rascal.ast.StructuredType.Tuple) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.StructuredType.Tuple(node, arguments);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.StructuredType.Relation makeStructuredTypeRelation(INode node, java.util.List<org.meta_environment.rascal.ast.TypeArg> arguments) { 
org.meta_environment.rascal.ast.StructuredType.Relation x = (org.meta_environment.rascal.ast.StructuredType.Relation) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.StructuredType.Relation(node, arguments);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.StructuredType.Map makeStructuredTypeMap(INode node, org.meta_environment.rascal.ast.TypeArg first, org.meta_environment.rascal.ast.TypeArg second) { 
org.meta_environment.rascal.ast.StructuredType.Map x = (org.meta_environment.rascal.ast.StructuredType.Map) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.StructuredType.Map(node, first, second);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.StructuredType.Bag makeStructuredTypeBag(INode node, org.meta_environment.rascal.ast.TypeArg typeArg) { 
org.meta_environment.rascal.ast.StructuredType.Bag x = (org.meta_environment.rascal.ast.StructuredType.Bag) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.StructuredType.Bag(node, typeArg);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.StructuredType.Set makeStructuredTypeSet(INode node, org.meta_environment.rascal.ast.TypeArg typeArg) { 
org.meta_environment.rascal.ast.StructuredType.Set x = (org.meta_environment.rascal.ast.StructuredType.Set) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.StructuredType.Set(node, typeArg);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.StructuredType.Ambiguity makeStructuredTypeAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.StructuredType> alternatives) { 
org.meta_environment.rascal.ast.StructuredType.Ambiguity amb = (org.meta_environment.rascal.ast.StructuredType.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.StructuredType.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.StructuredType.List makeStructuredTypeList(INode node, org.meta_environment.rascal.ast.TypeArg typeArg) { 
org.meta_environment.rascal.ast.StructuredType.List x = (org.meta_environment.rascal.ast.StructuredType.List) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.StructuredType.List(node, typeArg);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.FunctionType.Ambiguity makeFunctionTypeAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.FunctionType> alternatives) { 
org.meta_environment.rascal.ast.FunctionType.Ambiguity amb = (org.meta_environment.rascal.ast.FunctionType.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.FunctionType.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.FunctionType.TypeArguments makeFunctionTypeTypeArguments(INode node, org.meta_environment.rascal.ast.Type type, java.util.List<org.meta_environment.rascal.ast.TypeArg> arguments) { 
org.meta_environment.rascal.ast.FunctionType.TypeArguments x = (org.meta_environment.rascal.ast.FunctionType.TypeArguments) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.FunctionType.TypeArguments(node, type, arguments);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.TypeVar.Bounded makeTypeVarBounded(INode node, org.meta_environment.rascal.ast.Name name, org.meta_environment.rascal.ast.Type bound) { 
org.meta_environment.rascal.ast.TypeVar.Bounded x = (org.meta_environment.rascal.ast.TypeVar.Bounded) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.TypeVar.Bounded(node, name, bound);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.TypeVar.Ambiguity makeTypeVarAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.TypeVar> alternatives) { 
org.meta_environment.rascal.ast.TypeVar.Ambiguity amb = (org.meta_environment.rascal.ast.TypeVar.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.TypeVar.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.TypeVar.Free makeTypeVarFree(INode node, org.meta_environment.rascal.ast.Name name) { 
org.meta_environment.rascal.ast.TypeVar.Free x = (org.meta_environment.rascal.ast.TypeVar.Free) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.TypeVar.Free(node, name);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.UserType.Parametric makeUserTypeParametric(INode node, org.meta_environment.rascal.ast.Name name, java.util.List<org.meta_environment.rascal.ast.Type> parameters) { 
org.meta_environment.rascal.ast.UserType.Parametric x = (org.meta_environment.rascal.ast.UserType.Parametric) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.UserType.Parametric(node, name, parameters);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.UserType.Ambiguity makeUserTypeAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.UserType> alternatives) { 
org.meta_environment.rascal.ast.UserType.Ambiguity amb = (org.meta_environment.rascal.ast.UserType.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.UserType.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.UserType.Name makeUserTypeName(INode node, org.meta_environment.rascal.ast.Name name) { 
org.meta_environment.rascal.ast.UserType.Name x = (org.meta_environment.rascal.ast.UserType.Name) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.UserType.Name(node, name);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.DataTypeSelector.Ambiguity makeDataTypeSelectorAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.DataTypeSelector> alternatives) { 
org.meta_environment.rascal.ast.DataTypeSelector.Ambiguity amb = (org.meta_environment.rascal.ast.DataTypeSelector.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.DataTypeSelector.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.DataTypeSelector.Selector makeDataTypeSelectorSelector(INode node, org.meta_environment.rascal.ast.Name sort, org.meta_environment.rascal.ast.Name production) { 
org.meta_environment.rascal.ast.DataTypeSelector.Selector x = (org.meta_environment.rascal.ast.DataTypeSelector.Selector) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.DataTypeSelector.Selector(node, sort, production);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.Name.Ambiguity makeNameAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Name> alternatives) { 
org.meta_environment.rascal.ast.Name.Ambiguity amb = (org.meta_environment.rascal.ast.Name.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.Name.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.Name.Lexical makeNameLexical(INode node, String string) { 
org.meta_environment.rascal.ast.Name.Lexical x = (org.meta_environment.rascal.ast.Name.Lexical) lexCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.Name.Lexical(node, string);
          lexCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.EscapedName.Ambiguity makeEscapedNameAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.EscapedName> alternatives) { 
org.meta_environment.rascal.ast.EscapedName.Ambiguity amb = (org.meta_environment.rascal.ast.EscapedName.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.EscapedName.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.EscapedName.Lexical makeEscapedNameLexical(INode node, String string) { 
org.meta_environment.rascal.ast.EscapedName.Lexical x = (org.meta_environment.rascal.ast.EscapedName.Lexical) lexCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.EscapedName.Lexical(node, string);
          lexCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.QualifiedName.Ambiguity makeQualifiedNameAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.QualifiedName> alternatives) { 
org.meta_environment.rascal.ast.QualifiedName.Ambiguity amb = (org.meta_environment.rascal.ast.QualifiedName.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.QualifiedName.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.QualifiedName.Default makeQualifiedNameDefault(INode node, java.util.List<org.meta_environment.rascal.ast.Name> names) { 
org.meta_environment.rascal.ast.QualifiedName.Default x = (org.meta_environment.rascal.ast.QualifiedName.Default) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.QualifiedName.Default(node, names);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.UnicodeEscape.Ambiguity makeUnicodeEscapeAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.UnicodeEscape> alternatives) { 
org.meta_environment.rascal.ast.UnicodeEscape.Ambiguity amb = (org.meta_environment.rascal.ast.UnicodeEscape.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.UnicodeEscape.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.UnicodeEscape.Lexical makeUnicodeEscapeLexical(INode node, String string) { 
org.meta_environment.rascal.ast.UnicodeEscape.Lexical x = (org.meta_environment.rascal.ast.UnicodeEscape.Lexical) lexCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.UnicodeEscape.Lexical(node, string);
          lexCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.DecimalIntegerLiteral.Ambiguity makeDecimalIntegerLiteralAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.DecimalIntegerLiteral> alternatives) { 
org.meta_environment.rascal.ast.DecimalIntegerLiteral.Ambiguity amb = (org.meta_environment.rascal.ast.DecimalIntegerLiteral.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.DecimalIntegerLiteral.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.DecimalIntegerLiteral.Lexical makeDecimalIntegerLiteralLexical(INode node, String string) { 
org.meta_environment.rascal.ast.DecimalIntegerLiteral.Lexical x = (org.meta_environment.rascal.ast.DecimalIntegerLiteral.Lexical) lexCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.DecimalIntegerLiteral.Lexical(node, string);
          lexCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.HexIntegerLiteral.Ambiguity makeHexIntegerLiteralAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.HexIntegerLiteral> alternatives) { 
org.meta_environment.rascal.ast.HexIntegerLiteral.Ambiguity amb = (org.meta_environment.rascal.ast.HexIntegerLiteral.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.HexIntegerLiteral.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.HexIntegerLiteral.Lexical makeHexIntegerLiteralLexical(INode node, String string) { 
org.meta_environment.rascal.ast.HexIntegerLiteral.Lexical x = (org.meta_environment.rascal.ast.HexIntegerLiteral.Lexical) lexCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.HexIntegerLiteral.Lexical(node, string);
          lexCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.OctalIntegerLiteral.Ambiguity makeOctalIntegerLiteralAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.OctalIntegerLiteral> alternatives) { 
org.meta_environment.rascal.ast.OctalIntegerLiteral.Ambiguity amb = (org.meta_environment.rascal.ast.OctalIntegerLiteral.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.OctalIntegerLiteral.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.OctalIntegerLiteral.Lexical makeOctalIntegerLiteralLexical(INode node, String string) { 
org.meta_environment.rascal.ast.OctalIntegerLiteral.Lexical x = (org.meta_environment.rascal.ast.OctalIntegerLiteral.Lexical) lexCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.OctalIntegerLiteral.Lexical(node, string);
          lexCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.DecimalLongLiteral.Ambiguity makeDecimalLongLiteralAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.DecimalLongLiteral> alternatives) { 
org.meta_environment.rascal.ast.DecimalLongLiteral.Ambiguity amb = (org.meta_environment.rascal.ast.DecimalLongLiteral.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.DecimalLongLiteral.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.DecimalLongLiteral.Lexical makeDecimalLongLiteralLexical(INode node, String string) { 
org.meta_environment.rascal.ast.DecimalLongLiteral.Lexical x = (org.meta_environment.rascal.ast.DecimalLongLiteral.Lexical) lexCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.DecimalLongLiteral.Lexical(node, string);
          lexCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.HexLongLiteral.Ambiguity makeHexLongLiteralAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.HexLongLiteral> alternatives) { 
org.meta_environment.rascal.ast.HexLongLiteral.Ambiguity amb = (org.meta_environment.rascal.ast.HexLongLiteral.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.HexLongLiteral.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.HexLongLiteral.Lexical makeHexLongLiteralLexical(INode node, String string) { 
org.meta_environment.rascal.ast.HexLongLiteral.Lexical x = (org.meta_environment.rascal.ast.HexLongLiteral.Lexical) lexCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.HexLongLiteral.Lexical(node, string);
          lexCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.OctalLongLiteral.Ambiguity makeOctalLongLiteralAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.OctalLongLiteral> alternatives) { 
org.meta_environment.rascal.ast.OctalLongLiteral.Ambiguity amb = (org.meta_environment.rascal.ast.OctalLongLiteral.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.OctalLongLiteral.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.OctalLongLiteral.Lexical makeOctalLongLiteralLexical(INode node, String string) { 
org.meta_environment.rascal.ast.OctalLongLiteral.Lexical x = (org.meta_environment.rascal.ast.OctalLongLiteral.Lexical) lexCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.OctalLongLiteral.Lexical(node, string);
          lexCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.RealLiteral.Ambiguity makeRealLiteralAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.RealLiteral> alternatives) { 
org.meta_environment.rascal.ast.RealLiteral.Ambiguity amb = (org.meta_environment.rascal.ast.RealLiteral.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.RealLiteral.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.RealLiteral.Lexical makeRealLiteralLexical(INode node, String string) { 
org.meta_environment.rascal.ast.RealLiteral.Lexical x = (org.meta_environment.rascal.ast.RealLiteral.Lexical) lexCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.RealLiteral.Lexical(node, string);
          lexCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.BooleanLiteral.Ambiguity makeBooleanLiteralAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.BooleanLiteral> alternatives) { 
org.meta_environment.rascal.ast.BooleanLiteral.Ambiguity amb = (org.meta_environment.rascal.ast.BooleanLiteral.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.BooleanLiteral.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.BooleanLiteral.Lexical makeBooleanLiteralLexical(INode node, String string) { 
org.meta_environment.rascal.ast.BooleanLiteral.Lexical x = (org.meta_environment.rascal.ast.BooleanLiteral.Lexical) lexCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.BooleanLiteral.Lexical(node, string);
          lexCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.SingleCharacter.Ambiguity makeSingleCharacterAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.SingleCharacter> alternatives) { 
org.meta_environment.rascal.ast.SingleCharacter.Ambiguity amb = (org.meta_environment.rascal.ast.SingleCharacter.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.SingleCharacter.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.SingleCharacter.Lexical makeSingleCharacterLexical(INode node, String string) { 
org.meta_environment.rascal.ast.SingleCharacter.Lexical x = (org.meta_environment.rascal.ast.SingleCharacter.Lexical) lexCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.SingleCharacter.Lexical(node, string);
          lexCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.CharacterLiteral.Ambiguity makeCharacterLiteralAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.CharacterLiteral> alternatives) { 
org.meta_environment.rascal.ast.CharacterLiteral.Ambiguity amb = (org.meta_environment.rascal.ast.CharacterLiteral.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.CharacterLiteral.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.CharacterLiteral.Lexical makeCharacterLiteralLexical(INode node, String string) { 
org.meta_environment.rascal.ast.CharacterLiteral.Lexical x = (org.meta_environment.rascal.ast.CharacterLiteral.Lexical) lexCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.CharacterLiteral.Lexical(node, string);
          lexCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.EscapeSequence.Ambiguity makeEscapeSequenceAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.EscapeSequence> alternatives) { 
org.meta_environment.rascal.ast.EscapeSequence.Ambiguity amb = (org.meta_environment.rascal.ast.EscapeSequence.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.EscapeSequence.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.EscapeSequence.Lexical makeEscapeSequenceLexical(INode node, String string) { 
org.meta_environment.rascal.ast.EscapeSequence.Lexical x = (org.meta_environment.rascal.ast.EscapeSequence.Lexical) lexCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.EscapeSequence.Lexical(node, string);
          lexCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.StringCharacter.Ambiguity makeStringCharacterAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.StringCharacter> alternatives) { 
org.meta_environment.rascal.ast.StringCharacter.Ambiguity amb = (org.meta_environment.rascal.ast.StringCharacter.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.StringCharacter.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.StringCharacter.Lexical makeStringCharacterLexical(INode node, String string) { 
org.meta_environment.rascal.ast.StringCharacter.Lexical x = (org.meta_environment.rascal.ast.StringCharacter.Lexical) lexCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.StringCharacter.Lexical(node, string);
          lexCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.StringLiteral.Ambiguity makeStringLiteralAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.StringLiteral> alternatives) { 
org.meta_environment.rascal.ast.StringLiteral.Ambiguity amb = (org.meta_environment.rascal.ast.StringLiteral.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.StringLiteral.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.StringLiteral.Lexical makeStringLiteralLexical(INode node, String string) { 
org.meta_environment.rascal.ast.StringLiteral.Lexical x = (org.meta_environment.rascal.ast.StringLiteral.Lexical) lexCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.StringLiteral.Lexical(node, string);
          lexCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.IntegerLiteral.OctalIntegerLiteral makeIntegerLiteralOctalIntegerLiteral(INode node, org.meta_environment.rascal.ast.OctalIntegerLiteral octal) { 
org.meta_environment.rascal.ast.IntegerLiteral.OctalIntegerLiteral x = (org.meta_environment.rascal.ast.IntegerLiteral.OctalIntegerLiteral) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.IntegerLiteral.OctalIntegerLiteral(node, octal);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.IntegerLiteral.HexIntegerLiteral makeIntegerLiteralHexIntegerLiteral(INode node, org.meta_environment.rascal.ast.HexIntegerLiteral hex) { 
org.meta_environment.rascal.ast.IntegerLiteral.HexIntegerLiteral x = (org.meta_environment.rascal.ast.IntegerLiteral.HexIntegerLiteral) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.IntegerLiteral.HexIntegerLiteral(node, hex);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.IntegerLiteral.Ambiguity makeIntegerLiteralAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.IntegerLiteral> alternatives) { 
org.meta_environment.rascal.ast.IntegerLiteral.Ambiguity amb = (org.meta_environment.rascal.ast.IntegerLiteral.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.IntegerLiteral.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.IntegerLiteral.DecimalIntegerLiteral makeIntegerLiteralDecimalIntegerLiteral(INode node, org.meta_environment.rascal.ast.DecimalIntegerLiteral decimal) { 
org.meta_environment.rascal.ast.IntegerLiteral.DecimalIntegerLiteral x = (org.meta_environment.rascal.ast.IntegerLiteral.DecimalIntegerLiteral) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.IntegerLiteral.DecimalIntegerLiteral(node, decimal);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.LongLiteral.OctalLongLiteral makeLongLiteralOctalLongLiteral(INode node, org.meta_environment.rascal.ast.OctalLongLiteral octalLong) { 
org.meta_environment.rascal.ast.LongLiteral.OctalLongLiteral x = (org.meta_environment.rascal.ast.LongLiteral.OctalLongLiteral) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.LongLiteral.OctalLongLiteral(node, octalLong);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.LongLiteral.HexLongLiteral makeLongLiteralHexLongLiteral(INode node, org.meta_environment.rascal.ast.HexLongLiteral hexLong) { 
org.meta_environment.rascal.ast.LongLiteral.HexLongLiteral x = (org.meta_environment.rascal.ast.LongLiteral.HexLongLiteral) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.LongLiteral.HexLongLiteral(node, hexLong);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
public org.meta_environment.rascal.ast.LongLiteral.Ambiguity makeLongLiteralAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.LongLiteral> alternatives) { 
org.meta_environment.rascal.ast.LongLiteral.Ambiguity amb = (org.meta_environment.rascal.ast.LongLiteral.Ambiguity) ambCache.get(node);
     if(amb == null){
     	amb = new org.meta_environment.rascal.ast.LongLiteral.Ambiguity(node, alternatives);
     	ambCache.putUnsafe(node, amb);
     }
     return amb; 
}
public org.meta_environment.rascal.ast.LongLiteral.DecimalLongLiteral makeLongLiteralDecimalLongLiteral(INode node, org.meta_environment.rascal.ast.DecimalLongLiteral decimalLong) { 
org.meta_environment.rascal.ast.LongLiteral.DecimalLongLiteral x = (org.meta_environment.rascal.ast.LongLiteral.DecimalLongLiteral) sortCache.get(node);
       if(x == null){
          x = new org.meta_environment.rascal.ast.LongLiteral.DecimalLongLiteral(node, decimalLong);
          sortCache.putUnsafe(node, x);
       }
       return x; 
}
}