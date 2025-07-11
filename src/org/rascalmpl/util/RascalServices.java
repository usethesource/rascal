/*
 * Copyright (c) 2018-2025, NWO-I CWI and Swat.engineering
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.rascalmpl.util;

import org.rascalmpl.library.lang.rascal.syntax.RascalParser;
import org.rascalmpl.library.util.ParseErrorRecovery;
import org.rascalmpl.parser.Parser;
import org.rascalmpl.parser.gtd.result.out.DefaultNodeFlattener;
import org.rascalmpl.parser.gtd.util.StackNodeIdDispenser;
import org.rascalmpl.parser.uptr.UPTRNodeFactory;
import org.rascalmpl.parser.uptr.action.NoActionExecutor;
import org.rascalmpl.parser.uptr.recovery.ToTokenRecoverer;
import org.rascalmpl.values.IRascalValueFactory;
import org.rascalmpl.values.parsetrees.ITree;

import io.usethesource.vallang.ISourceLocation;

public class RascalServices {
    private static final IRascalValueFactory VF = IRascalValueFactory.getInstance();
    private static final ParseErrorRecovery RECOVERY = new ParseErrorRecovery(VF);
    public static final int MAX_AMB_DEPTH = 2;
    public static final int MAX_RECOVERY_ATTEMPTS = 50;
    public static final int MAX_RECOVERY_TOKENS = 3;

    public static ITree parseRascalModule(ISourceLocation loc, char[] input) {
        // TODO: Which of these objects are stateless and can be reused?

        // Parse
        RascalParser parser = new RascalParser();
        ITree tree = parser.parse(
            Parser.START_MODULE, loc.getURI(), input, MAX_AMB_DEPTH,
            new NoActionExecutor(),
            new DefaultNodeFlattener<>(),
            new UPTRNodeFactory(true),
            new ToTokenRecoverer(loc.getURI(), parser, new StackNodeIdDispenser(parser), MAX_RECOVERY_ATTEMPTS, MAX_RECOVERY_TOKENS));

        // We pre-emptively disambiguate parse errors here to avoid performance issues.
        // Parse forests with parse errors can be very large, and Rascal is currently
        // not well equipped to handle such large forests. For instance visits and deep matches
        // can take a long time until we implement memoization in those constructs.
        // In the future we will remove this automatic disambiguation so language developers are free
        // to handle parse errors as they see fit.
        return (ITree) RECOVERY.disambiguateParseErrors(tree, VF.bool(true));
    }
}
