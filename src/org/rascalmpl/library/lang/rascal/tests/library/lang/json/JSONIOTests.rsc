module lang::rascal::tests::library::lang::json::JSONIOTests

import String;
import lang::json::IO;
import util::UUID;
import util::Maybe;
import IO;

loc targetFile = |memory://test-tmp/test-<"<uuidi()>">.json|;

bool jsonFeaturesSupported(value v) {
    for (/num r := v, size("<r>") > 10) {
         // json can only contain double precision numbers (doubles)
         // so let's ignore the cases where we get higher random numbers
        return false;
    }
    
    return true;
}    

bool writeRead(type[&T] returnType, &T dt) {
    if (!jsonFeaturesSupported(dt)) {
        return true;
    }
    json = toJSON(dt);
    return fromJSON(returnType, json) == dt;
}
	
// only single constructors supported for now
data DATA1 = data1(int n);
data DATA2 = data2(str n);
data DATA3 = data3(int n, str kw = "abc");
data Enum = x() | y() | z();
data DATA4 = data4(Enum e = x());

test bool jsonWithBool1(bool dt) = writeRead(#bool, dt);
test bool jsonWithInt1(int dt) = writeRead(#int, dt);
test bool jsonWithReal1(real dt) = writeRead(#real, dt);
test bool jsonWithRat1(rat dt) = writeRead(#rat, dt);
test bool jsonWithNum1(num dt) = writeRead(#num, dt);

test bool jsonWithLoc1(loc dt) = writeRead(#loc, dt);
test bool jsonWithStr1(str dt) = writeRead(#str, dt);
test bool jsonWithDatetime1(datetime dt) = writeRead(#datetime, dt);
test bool jsonWithList1(list[int] dt) = writeRead(#list[int], dt);
test bool jsonWithSet1(set[int] dt) = writeRead(#set[int], dt);
test bool jsonWithMap1(map[int, int] dt) = writeRead(#map[int,int], dt);
test bool jsonWithNode1(node  dt) = writeRead(#node, dt);

test bool jsonWithDATA11(DATA1 dt) = writeRead(#DATA1, dt);
test bool jsonWithDATA21(DATA2 dt) = writeRead(#DATA2, dt);

test bool jsonRandom1(value dt) = writeRead(#value, dt);

test bool json1() = writeRead(#DATA1, data1(123));
test bool json2() = writeRead(#DATA2, data2("123"));
test bool json3() = writeRead(#DATA3, data3(123,kw="123"));
test bool json4(Enum e) = writeRead(#DATA4, data4(e=e));

test bool originTracking() {
   ex2 = readJSON(#node, |std:///lang/rascal/tests/library/lang/json/glossary.json|, trackOrigins=true);   
   content = readFile(|std:///lang/rascal/tests/library/lang/json/glossary.json|);

   poss = [<x.src, x.line> | /node x := ex2, x.line?]; // every node has a .src field, otherwise this fails with an exception

   for (<loc p, int line> <- poss) {
      assert content[p.offset] == "{";                // all nodes start with a {
      assert content[p.offset + p.length - 1] == "}"; // all nodes end with a }
      assert p.begin.line == line;
   }

   return true;
}

data Cons = cons(str bla = "null");

test bool dealWithNull() {
    // use the default nulls map
    assert parseJSON(#map[str,value], "{\"bla\": null}") == ("bla":"null"());

    // using our own nulls map
    assert parseJSON(#map[str,value], "{\"bla\": null}", nulls=(#value:-1)) == ("bla":-1);

    // conflicting entries in the nulls maps: more specific goes first:
    assert parseJSON(#map[str,node], "{\"bla\": null}", nulls=(#value:-1, #node:"null"())) == ("bla":"null"());

    // the builtin Maybe interpreter with null
    assert parseJSON(#map[str,Maybe[str]], "{\"bla\": null}") == ("bla":nothing());

    // the builtin Maybe interpreter with non-null
    assert parseJSON(#map[str,Maybe[str]], "{\"bla\": \"foo\"}") == ("bla":just("foo"));

    // keyword parameters and null
    assert parseJSON(#Cons, "{\"bla\": \"foo\"}") == cons(bla="foo");
    assert parseJSON(#Cons, "{\"bla\": null}") == cons();

    return true;
}

data Example = example(Prop ex = F());

data Prop = T() | F() | and(Prop lhs, Prop rhs) | or(Prop lhs, Prop rhs);

str format(T()) = "true";
str format(F()) = "false";
str format(and(Prop p1, Prop p2)) = "<format(p1)> && <format(p2)>";
str format(or(Prop p1, Prop p2)) = "<format(p1)> || <format(p2)>";

Prop parse(type[Prop] _, "true") = T();
Prop parse(type[Prop] _, "false") = F();

test bool formattingToStringsTest() {
    ex1 = and(and(\T(), \F()),or(\T(), \F()));

    writeJSON(|memory://test-json/formatted.json|, example(ex=ex1), formatter=format);
    source = readFile(|memory://test-json/formatted.json|);

    assert source == "{\"ex\":\"true && false && true || false\"}";

    writeFile(|memory://test-json/printed.json|, "{\"ex\":\"true\"}");

    Example result = readJSON(#Example, |memory://test-json/printed.json|, parser=parse);

    assert result.ex == T();

    return true;
}

test bool explicitConstructorNames() {
    example = data4(e=z());
    json = asJSON(example, explicitConstructorNames=true);
    
    assert json == "{\"_constructor\":\"data4\",\"e\":{\"_constructor\":\"z\"}}";

    assert parseJSON(#DATA4, json, explicitConstructorNames=true) == example;

    // here we can't be sure to get z() back, but we will get some Enum
    assert data4(e=Enum _) := parseJSON(#DATA4, json, explicitConstructorNames=false);

    return true;
}

test bool explicitDataTypes() {
    example = data4(e=z());
    json = asJSON(example, explicitDataTypes=true);
    
    assert json == "{\"_constructor\":\"data4\",\"_type\":\"DATA4\",\"e\":{\"_constructor\":\"z\",\"_type\":\"Enum\"}}";

    // _constructor and _type must be the first fields
    assert parseJSON(#DATA4, json, explicitDataTypes=true) == example;

    // _type and _constructor may appear in a different order
    flippedJson = "{\"_type\":\"DATA4\",\"_constructor\":\"data4\",\"e\":{\"_constructor\":\"z\",\"_type\":\"Enum\"}}";
    assert parseJSON(#DATA4, flippedJson, explicitDataTypes=true) == example;

    // here we can't be sure to get z() back, but we will get some Enum
    assert data4(e=Enum _) := parseJSON(#DATA4, json, explicitDataTypes=false);

    return true;
}

test bool explicitConstructorNames() {
    example = data4(e=z());
    json = asJSON(example, explicitConstructorNames=true);
    
    assert json == "{\"_constructor\":\"data4\",\"e\":{\"_constructor\":\"z\"}}";

    assert parseJSON(#DATA4, json, explicitConstructorNames=true) == example;

    // here we can't be sure to get z() back, but we will get some Enum
    assert data4(e=Enum _) := parseJSON(#DATA4, json, explicitConstructorNames=false);

    return true;
}

test bool explicitDataTypes() {
    example = data4(e=z());
    json = asJSON(example, explicitDataTypes=true);
    
    assert json == "{\"_constructor\":\"data4\",\"_type\":\"DATA4\",\"e\":{\"_constructor\":\"z\",\"_type\":\"Enum\"}}";

    // _constructor and _type must be the first fields
    assert parseJSON(#DATA4, json, explicitDataTypes=true) == example;

    // _type and _constructor may appear in a different order
    flippedJson = "{\"_type\":\"DATA4\",\"_constructor\":\"data4\",\"e\":{\"_constructor\":\"z\",\"_type\":\"Enum\"}}";
    assert parseJSON(#DATA4, flippedJson, explicitDataTypes=true) == example;

    // here we can't be sure to get z() back, but we will get some Enum
    assert data4(e=Enum _) := parseJSON(#DATA4, json, explicitDataTypes=false);

    return true;
}