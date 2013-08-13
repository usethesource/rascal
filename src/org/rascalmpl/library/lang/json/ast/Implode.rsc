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

private str removeEnds(str s) {
	return substring(substring(s,0,size(s)-1),1);
}

//public lang::json::ast::JSON::Value buildAST(start[JSONText] jt) = buildAST2(jt.top);

public lang::json::ast::JSON::Value buildAST((JSONText)`<Object obj>`) = object(convertObject(obj));
public lang::json::ast::JSON::Value buildAST((JSONText)`<Array a>`) = array(convertArray(a));

private lang::json::ast::JSON::Value convertValue((Value)`<Object obj>`) = object(convertObject(obj));
private lang::json::ast::JSON::Value convertValue((Value)`<Array a>`) = array(convertArray(a));
private lang::json::ast::JSON::Value convertValue((Value)`<IntegerLiteral il>`) = integer(toInt("<il>"));
private lang::json::ast::JSON::Value convertValue((Value)`<RealLiteral rl>`) = float(toReal("<rl>"));
private lang::json::ast::JSON::Value convertValue((Value)`<StringLiteral sl>`) = string(removeEnds("<sl>"));
private lang::json::ast::JSON::Value convertValue((Value)`false`) = boolean(false);
private lang::json::ast::JSON::Value convertValue((Value)`null`) = null();
private lang::json::ast::JSON::Value convertValue((Value)`true`) = boolean(true);

private map[str,lang::json::ast::JSON::Value] convertObject((Object)`{ < {Member ","}* ms > }`) {
	map[str,lang::json::ast::JSON::Value] res = ( );
	for ((Member) `<StringLiteral memberName> : <Value memberValue>` <- ms) {
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

private list[lang::json::ast::JSON::Value] convertArray((Array)`[ < {Value ","}* vs > ]`) = [ convertValue(v) | v <- vs ];
