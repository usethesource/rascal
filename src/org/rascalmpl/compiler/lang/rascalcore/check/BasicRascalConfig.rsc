@license{
Copyright (c) 2018-2025, NWO-I CWI and Swat.engineering
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice,
this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice,
this list of conditions and the following disclaimer in the documentation
and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
POSSIBILITY OF SUCH DAMAGE.
}
@bootstrapParser
module lang::rascalcore::check::BasicRascalConfig

/*
    Basic configuration information as required by TypePal, including IdRole, PathRole, ScopeRole, DefInfo and the like.
    The checker itself is configure in RascalConfig.
*/
extend analysis::typepal::TypePal;

import lang::rascal::\syntax::Rascal;
import Location;
import util::SemVer;

data IdRole
    = moduleId()
    | functionId()
    | formalId()
    | keywordFormalId()
    | nestedFormalId()
    | patternVariableId()
    | moduleVariableId()
    | fieldId()
    | keywordFieldId()
    | labelId()
    | constructorId()
    | productionId()
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
public set[IdRole] variableRoles = formalRoles + {variableId(), moduleVariableId(), patternVariableId()};
public set[IdRole] inferrableRoles = formalRoles + {variableId(), moduleVariableId(), patternVariableId()};
public set[IdRole] keepInTModelRoles = dataOrSyntaxRoles + { moduleId(), constructorId(), functionId(),
                                                             fieldId(), keywordFieldId(), annoId(),
                                                             moduleVariableId(), productionId(), nonterminalId()
                                                           };
public set[IdRole] assignableRoles = variableRoles;

data PathRole
    = importPath()
    | extendPath()
    ;

data ScopeRole
    = //moduleScope()
     functionScope()
    //| conditionalScope()
    | replacementScope()
    | visitOrSwitchScope()
    //| boolScope()
    | loopScope()
    | orScope()
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

// Function md5Hash of source
data DefInfo(str md5 = "");

// Common Keyword fields for ADTs
data DefInfo(list[KeywordFormal] commonKeywordFields = []);

// Maintain allow before use: where variables may be used left (before) their definition
public str key_allow_use_before_def = "allow_use_before_def";

void storeAllowUseBeforeDef(Tree container, Tree allowedPart, Collector c){
    c.push(key_allow_use_before_def, <getLoc(container), getLoc(allowedPart)>);
}

void storeAllowUseBeforeDef(Tree container, list[Tree] allowedParts, Collector c){
    c.push(key_allow_use_before_def, <getLoc(container), cover([getLoc(allowed) | allowed <- allowedParts])>);
}

public str key_bom = "bill_of_materials";
public str key_current_module = "current_module";
public str key_pathconfig = "pathconfig";
public str key_grammar = "grammar";
public str key_ADTs = "ADTs";
public str key_common_keyword_fields = "CommonKeywordFields";

bool isValidRascalTplVersion(str version)
    = equalVersion(version, currentRascalTplVersion);

str getCurrentRascalTplVersion() = currentRascalTplVersion;

str currentRascalTplVersion = "2.0.0";

data TModel (
    str rascalTplVersion = "2.0.0"
);

// Define alias for TypePalConfig

alias RascalCompilerConfig = TypePalConfig;

// Add keyword parameters to a TypePalConfig to represent compiler settings
// Also see lang::rascalcore::check::RascalConfig

data TypePalConfig(
    str rascalTplVersion            = getCurrentRascalTplVersion(),

    // Control message level
    bool warnUnused                 = true,
    bool warnUnusedFormals          = true,
    bool warnUnusedVariables        = true,
    bool warnUnusedPatternFormals   = true,
    bool infoModuleChecked          = false,

    loc reloc                       = |noreloc:///|,  // Unused

    // Debugging options
    bool verbose                    = true,    // for each compiled module, module name and compilation time
    bool logImports                 = false,   // log all imported files
    bool logWrittenFiles            = false,   // log all files written by compiler
    bool logPathConfig              = false,    // log PathConfig that is used

    bool optimizeVisit              = true,     // Options for compiler developer
    bool enableAsserts              = true
);
