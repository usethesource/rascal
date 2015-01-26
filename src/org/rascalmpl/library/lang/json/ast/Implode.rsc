@license{
  Copyright (c) 2009-2015 CWI
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
import ParseTree;

private str removeEnds(str s) {
	return substring(substring(s,0,size(s)-1),1);
}

public JSON buildAST(start[JSONText] jt) = buildAST(jt.top);

public JSON buildAST((JSONText)`<Object obj>`) = object(convertObject(obj));
public JSON buildAST((JSONText)`<Array a>`) = array(convertArray(a));

private JSON convertValue((Value)`<Object obj>`) = object(convertObject(obj));
private JSON convertValue((Value)`<Array a>`) = array(convertArray(a));
private JSON convertValue((Value)`<IntegerLiteral il>`) = number(toReal("<il>"));
private JSON convertValue((Value)`<RealLiteral rl>`) = number(toReal("<rl>"));
private JSON convertValue((Value)`<StringLiteral sl>`) = string(removeEnds("<sl>"));
private JSON convertValue((Value)`false`) = boolean(false);
private JSON convertValue((Value)`null`) = null();
private JSON convertValue((Value)`true`) = boolean(true);

private map[str,JSON] convertObject((Object)`{ < {Member ","}* ms > }`) {
	map[str,JSON] res = ( );
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

private list[JSON] convertArray((Array)`[ < {Value ","}* vs > ]`) = [ convertValue(v) | v <- vs ];
