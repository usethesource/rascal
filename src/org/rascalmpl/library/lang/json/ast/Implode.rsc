@license{
  Copyright (c) 2009-2013 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Mark Hills - Mark.Hills@cwi.nl (CWI)}
module lang::json::ast::Implode

import lang::json::\syntax::JSON;
import lang::json::ast::JSON;
import String;

alias AValue = lang::json::ast::JSON::Value;

alias CValue = lang::json::\syntax::JSON::Value;

public str removeEnds(str s) {
	return substring(substring(s,0,size(s)-1),1);
}

public AValue buildAST(start[JSONText] jt) = buildAST(jt.top);

public AValue buildAST((JSONText)`<Object \o>`) = object(convertObject(\o));
public AValue buildAST((JSONText)`<Array a>`) = array(convertArray(a));

public AValue convertValue((Value)`<Object \o>`) = object(convertObject(\o));
public AValue convertValue((Value)`<Array a>`) = array(convertArray(a));
public AValue convertValue((Value)`<IntegerLiteral il>`) = integer(toInt("<il>"));
public AValue convertValue((Value)`<RealLiteral rl>`) = float(toReal("<rl>"));
public AValue convertValue((Value)`<StringLiteral sl>`) = string(removeEnds("<sl>"));
public AValue convertValue((Value)`false`) = boolean(false);
public AValue convertValue((Value)`null`) = null();
public AValue convertValue((Value)`true`) = boolean(true);

public map[str,AValue] convertObject((Object)`{ < {Member ","}* ms > }`) {
	map[str,AValue] res = ( );
	for (`<StringLiteral memberName> : < lang::json::\syntax::JSON::Value memberValue>` <- ms) {
		mn = removeEnds("<memberName>");
		av = convertValue(memberValue);
		if (mn notin res) {
			res[mn] = av;
		} else {
			throw "Duplicate field <mn> in object";
		}
	}
	return res;
}

public list[AValue] convertArray((Array)`[ < {Value ","}* vs > ]`) = [ convertValue(v) | v <- vs ];
