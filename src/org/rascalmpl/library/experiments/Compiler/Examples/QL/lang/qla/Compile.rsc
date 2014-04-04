module experiments::Compiler::Examples::QL::lang::qla::Compile

import experiments::Compiler::Examples::QL::lang::qla::AST;
import experiments::Compiler::Examples::QL::lang::qla::Expr2JS;
import Set;
import List;
import String;
import IO;

private loc HTML_TEMPLATE = |project://QL-LWC14/resources/qltemplate.html|;

str form2html(Form f, loc jsPath) 
  = replaceAll(
       replaceAll(
         replaceAll(tmpl, 
           "__TITLE__", "<f.name>"),
           "__FORM_JS__", jsPath.file),
           "__ROOT__", "../output")
  when tmpl := readFile(HTML_TEMPLATE);

str form2js(Form f) =
  "$(document).ready(function () {
  '  var form = new QLrt.FormWidget({ name: \"<f.name>\", submitCallback: persist});
  '  <for (q <- f.body) {>
  '  <question2decl(q, "form")>
  '  <}>
  '  $(\'#QL-content\').append(form.domElement());
  '  form.activate();
  '  function persist(data) {
  '     localStorage[\'QL-persist\'] = JSON.stringify(data);
  '  }
  '});";

str question2decl(qc:ifThen(e, q), str parent) 
  = "var <nameFor(qc)> = <cond2group(e, parent)>;
    '<question2decl(q, nameFor(qc))>";

str question2decl(qc:ifThenElse(e, q1, q2), str parent) 
  = "var <nameFor(qc)> = <cond2group(e, parent)>;
    '<question2decl(q1, nameFor(qc))>
    'var <nameFor(qc)>Else = <cond2group(not(e), parent)>;
    '<question2decl(q2, nameFor(qc) + "Else")>";

str question2decl(Question::group(qs), str parent)
  = ( "" | it + "\n" + question2decl(q, parent) | q <- qs );

str question2decl(question(l, v, t), str parent)
  = question2widget(l, v, t, parent, "");

str question2decl(computed(l, v, t, e), str parent)
  = question2widget(l, v, t, parent, exp2lazyValue(e));

str question2widget(str l, Id v, QType t, str parent, str e)
  = "var <v.name> = new QLrt.SimpleFormElementWidget({
    '  name: \"<v.name>\", 
    '  label: <l>,
    '  valueWidget: new QLrt.<type2widget(t)>(<e>) 
    '}).appendTo(<parent>);";

str cond2group(Expr e, str parent)
  = "new QLrt.ConditionalGroupWidget(<exp2lazyValue(e)>).appendTo(<parent>)";
    
str exp2lazyValue(Expr e) 
  = "new QLrt.LazyValue(
    '  function () { return [<ps>]; },
    '  function (<ps>) { return <expr2js(e)>; }
    ')"
  when str ps := expParams(e);
    

str nameFor(Question q) = "q<q@location.offset>";

str type2widget(QType::boolean()) = "BooleanValueWidget";
str type2widget(QType::money())   = "MoneyValueWidget";
str type2widget(QType::string())  = "StringValueWidget";
str type2widget(QType::integer()) = "IntegerValueWidget";

list[str] freeVars(Expr e) =  [ x.name | /Id x := e ];

str expParams(Expr e) = intercalate(", ", freeVars(e));
  