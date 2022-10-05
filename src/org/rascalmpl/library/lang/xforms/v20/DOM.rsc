@doc{
#### Synopsis

AST model for XForms v 2.0. 
}
@contributor{Tijs van der Storm - storm@cwi.nl (CWI)}
module lang::xforms::v20::DOM

import lang::xml::DOM;
  
str XFORMS_NAMESPACE = "http://www.w3.org/2002/xforms";

default str nameSpacePrefix() = "xf";

Namespace NS = namespace(nameSpacePrefix(), XFORMS_NAMESPACE);

Node action(value kids...) = element(NS, "action", values2nodes(kids));
Node alert(value kids...) = element(NS, "alert", values2nodes(kids));
Node \case(value kids...) = element(NS, "case", values2nodes(kids));
Node choices(value kids...) = element(NS, "choices", values2nodes(kids));
Node control(value kids...) = element(NS, "control", values2nodes(kids));
Node delete(value kids...) = element(NS, "delete", values2nodes(kids));
Node dispatch(value kids...) = element(NS, "dispatch", values2nodes(kids));
Node extension(value kids...) = element(NS, "extension", values2nodes(kids));
Node filename(value kids...) = element(NS, "filename", values2nodes(kids));
Node group(value kids...) = element(NS, "group", values2nodes(kids));
Node input(value kids...) = element(NS, "input", values2nodes(kids));
Node \insert(value kids...) = element(NS, "insert", values2nodes(kids));
Node instance(value kids...) = element(NS, "instance", values2nodes(kids));
Node itemset(value kids...) = element(NS, "itemset", values2nodes(kids));
Node label(value kids...) = element(NS, "label", values2nodes(kids));
Node mediatype(value kids...) = element(NS, "mediatype", values2nodes(kids));
Node message(value kids...) = element(NS, "message", values2nodes(kids));
Node model(value kids...) = element(NS, "model", values2nodes(kids));
Node output(value kids...) = element(NS, "output", values2nodes(kids));
Node range(value kids...) = element(NS, "range", values2nodes(kids));
Node rebuild(value kids...) = element(NS, "rebuild", values2nodes(kids));
Node recalculate(value kids...) = element(NS, "recalculate", values2nodes(kids));
Node refresh(value kids...) = element(NS, "refresh", values2nodes(kids));
Node repeat(value kids...) = element(NS, "repeat", values2nodes(kids));
Node reset(value kids...) = element(NS, "reset", values2nodes(kids));
Node revalidate(value kids...) = element(NS, "revalidate", values2nodes(kids));
Node secret(value kids...) = element(NS, "secret", values2nodes(kids));
Node select(value kids...) = element(NS, "select", values2nodes(kids));
Node select1(value kids...) = element(NS, "select1", values2nodes(kids));
Node setfocus(value kids...) = element(NS, "setfocus", values2nodes(kids));
Node setindex(value kids...) = element(NS, "setindex", values2nodes(kids));
Node setvalue(value kids...) = element(NS, "setvalue", values2nodes(kids));
Node submit(value kids...) = element(NS, "submit", values2nodes(kids));
Node \switch(value kids...) = element(NS, "switch", values2nodes(kids));
Node textarea(value kids...) = element(NS, "textarea", values2nodes(kids));
Node toggle(value kids...) = element(NS, "toggle", values2nodes(kids));
Node trigger(value kids...) = element(NS, "trigger", values2nodes(kids));
Node upload(value kids...) = element(NS, "upload", values2nodes(kids));
Node \value(value kids...) = element(NS, "value", values2nodes(kids));
Node xs(value kids...) = element(NS, "xs", values2nodes(kids));

// If an attribute name also exists as element, it is suffixed with _a

Node accept(value val) = attribute(NS, "accept", value2txt(val));
Node appearance(value val) = attribute(NS, "appearance", value2txt(val));
Node at(value val) = attribute(NS, "at", value2txt(val));
Node bind(value val) = attribute(NS, "bind", value2txt(val));
Node bubbles(value val) = attribute(NS, "bubbles", value2txt(val));
Node calculate(value val) = attribute(NS, "calculate", value2txt(val));
Node cancelable(value val) = attribute(NS, "cancelable", value2txt(val));
Node case_a(value val) = attribute(NS, "case", value2txt(val));
Node caseref(value val) = attribute(NS, "caseref", value2txt(val));
Node charset(value val) = attribute(NS, "charset", value2txt(val));
Node constraint(value val) = attribute(NS, "constraint", value2txt(val));
Node context(value val) = attribute(NS, "context", value2txt(val));
Node control_a(value val) = attribute(NS, "control", value2txt(val));
Node delay(value val) = attribute(NS, "delay", value2txt(val));
Node end(value val) = attribute(NS, "end", value2txt(val));
Node ev_default(value val) = attribute(NS, "ev_default", value2txt(val));
Node ev_event(value val) = attribute(NS, "ev_event", value2txt(val));
Node ev_phase(value val) = attribute(NS, "ev_phase", value2txt(val));
Node ev_propagate(value val) = attribute(NS, "ev_propagate", value2txt(val));
Node functions(value val) = attribute(NS, "functions", value2txt(val));
Node href(value val) = attribute(NS, "href", value2txt(val));
Node id(value val) = attribute(NS, "id", value2txt(val));
Node \if(value val) = attribute(NS, "if", value2txt(val));
Node incremental(value val) = attribute(NS, "incremental", value2txt(val));
Node index(value val) = attribute(NS, "index", value2txt(val));
Node indexref(value val) = attribute(NS, "indexref", value2txt(val));
Node inputmode(value val) = attribute(NS, "inputmode", value2txt(val));
Node iterate(value val) = attribute(NS, "iterate", value2txt(val));
Node level(value val) = attribute(NS, "level", value2txt(val));
Node mediatype(value val) = attribute(NS, "mediatype", value2txt(val));
Node method(value val) = attribute(NS, "method", value2txt(val));
Node model_a(value val) = attribute(NS, "model", value2txt(val));
Node name(value val) = attribute(NS, "name", value2txt(val));
Node nodeset(value val) = attribute(NS, "nodeset", value2txt(val));
Node number(value val) = attribute(NS, "number", value2txt(val));
Node origin(value val) = attribute(NS, "origin", value2txt(val));
Node override(value val) = attribute(NS, "override", value2txt(val));
Node p3ptype(value val) = attribute(NS, "p3ptype", value2txt(val));
Node position(value val) = attribute(NS, "position", value2txt(val));
Node readonly(value val) = attribute(NS, "readonly", value2txt(val));
Node ref(value val) = attribute(NS, "ref", value2txt(val));
Node relevant(value val) = attribute(NS, "relevant", value2txt(val));
Node repeat_a(value val) = attribute(NS, "repeat", value2txt(val));
Node required(value val) = attribute(NS, "required", value2txt(val));
Node resource(value val) = attribute(NS, "resource", value2txt(val));
Node schema(value val) = attribute(NS, "schema", value2txt(val));
Node selected(value val) = attribute(NS, "selected", value2txt(val));
Node selection(value val) = attribute(NS, "selection", value2txt(val));
Node show(value val) = attribute(NS, "show", value2txt(val));
Node signature(value val) = attribute(NS, "signature", value2txt(val));
Node \start(value val) = attribute(NS, "start", value2txt(val));
Node startindex(value val) = attribute(NS, "startindex", value2txt(val));
Node step(value val) = attribute(NS, "step", value2txt(val));
Node submission(value val) = attribute(NS, "submission", value2txt(val));
Node target(value val) = attribute(NS, "target", value2txt(val));
Node targetid(value val) = attribute(NS, "targetid", value2txt(val));
Node \type(value val) = attribute(NS, "type", value2txt(val));
Node value_a(value val) = attribute(NS, "value", value2txt(val));
Node version(value val) = attribute(NS, "version", value2txt(val));
Node \while(value val) = attribute(NS, "while", value2txt(val));
Node xpath(value val) = attribute(NS, "xpath", value2txt(val));

str value2txt(value v) = "<v>";

list[Node] values2nodes(list[value] vs) {
  return for (v <- vs) {
    if (Node n := v) {
      append n;
    }
    else {
      append charData("<v>");
    }
  }
}
