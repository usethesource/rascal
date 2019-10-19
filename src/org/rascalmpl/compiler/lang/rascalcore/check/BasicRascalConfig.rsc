@bootstrapParser
module lang::rascalcore::check::BasicRascalConfig

extend analysis::typepal::TypePal;
 
import lang::rascal::\syntax::Rascal;
import Location;

data IdRole
    = moduleId()
    | functionId()
    | formalId()
    | keywordFormalId()
    | nestedFormalId()
    | patternVariableId()
    | fieldId()
    | keywordFieldId()
    | labelId()
    | constructorId()
    | dataId()
    | aliasId()
    | annoId()
    | nonterminalId()
    | lexicalId()
    | layoutId()
    | keywordId()
    | typeVarId()
    ;

public set[IdRole] syntaxRoles = {aliasId(), nonterminalId(), lexicalId(), layoutId(), keywordId()};
public set[IdRole] dataOrSyntaxRoles = {dataId()} + syntaxRoles;
public set[IdRole] dataRoles = {aliasId(), dataId()}; 
public set[IdRole] outerFormalRoles = {formalId(), keywordFormalId()};
public set[IdRole] positionalFormalRoles = {formalId(), nestedFormalId()};
public set[IdRole] formalRoles = outerFormalRoles + {nestedFormalId()};
public set[IdRole] variableRoles = formalRoles + {variableId(), patternVariableId()};
public set[IdRole] inferrableRoles = formalRoles + {variableId(), patternVariableId()};
public set[IdRole] saveModuleRoles = dataOrSyntaxRoles + {constructorId(), functionId(), fieldId(), keywordFieldId(), annoId()} + variableRoles;

data PathRole
    = importPath()
    | extendPath()
    ;
    
data ScopeRole
    = moduleScope()
    | functionScope()
    | conditionalScope()
    | replacementScope()
    | visitOrSwitchScope()
    | boolScope()
    | loopScope()
    ;

data Vis
    = publicVis()
    | privateVis()
    | defaultVis()
    ;

data Modifier
    = javaModifier()
    | testModifier()
    | defaultModifier()
    ;

// Visibility information
data DefInfo(Vis vis = publicVis());

data DefInfo(bool canFail = false);

data DefInfo(map[str,str] tags = ());

// Function modifiers
data DefInfo(list[str] modifiers = []);

// Common Keyword fields for ADTs
data DefInfo(list[KeywordFormal] commonKeywordFields = []);

// Maintain allow before use: where variables may be used left (before) their definition
private str key_allow_use_before_def = "allow_use_before_def";

void storeAllowUseBeforeDef(Tree container, Tree allowedPart, Collector c){
    c.push(key_allow_use_before_def, <getLoc(container), getLoc(allowedPart)>);
}

void storeAllowUseBeforeDef(Tree container, list[Tree] allowedParts, Collector c){
    c.push(key_allow_use_before_def, <getLoc(container), union([getLoc(allowed) | allowed <- allowedParts])>);
}

public str key_bom = "bill_of_materials";
public str key_current_module = "current_module";

data TypePalConfig(
    bool logImports                 = false,
    bool classicReifier             = true,
    bool warnUnused                 = true,
    bool warnUnusedFormals          = true,
    bool warnUnusedVariables        = true,
    bool warnUnusedPatternFormals   = false,
    bool warnDeprecated             = false
);
