module experiments::GrammarTools::Grammars

import experiments::GrammarTools::Grammar;

public Grammar G1 = grammar(nt("E"),
{
<nt("E"), [nt("E"), t("*"), nt("B")]>,
<nt("E"), [nt("E"), t("+"), nt("B")]>,
<nt("E"), [nt("B")]>,
<nt("B"), [t("0")]>,
<nt("B"), [t("1")]>
});

public Grammar G2 = grammar(nt("E"),
{
<nt("E"),  [nt("T"), nt("E1")]>,
<nt("E1"), [t("+"), nt("T"), nt("E1")]>,
<nt("E1"), []>,
<nt("T"),  [nt("F"), nt("T1")]>,
<nt("T1"), [t("*"), nt("F"), nt("T1")]>,
<nt("T1"), []>,
<nt("F"),  [t("("), nt("E"), t(")")]>,
<nt("F"),  [t("id")]>
});

