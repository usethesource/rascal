@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
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

