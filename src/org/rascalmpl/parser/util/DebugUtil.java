package org.rascalmpl.parser.util;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IValue;

public class DebugUtil {
    /**
     * Turn a production IConstructor into a string of the form "S -> E1 E2 ..."
     */
    public static String prodToString(IConstructor prod) {
        StringBuilder builder = new StringBuilder("'");

        IConstructor sort = (IConstructor) prod.get(0);
        builder.append(stripQuotes(String.valueOf(sort.get(0))));

        builder.append(" ->");

        if (prod.getName() == "prod") {
            IList children = (IList) prod.get(1);
            for (IValue child : children) {
                builder.append(" ");
                IConstructor conChild = (IConstructor) child;
                builder.append(stripQuotes(String.valueOf((conChild).get(0))));
            }
        } else {
            builder.append(" ");
            builder.append(prod.toString());
        }

        builder.append("'");

        return builder.toString();
    }

    private static String stripQuotes(String s) {
        if (s.charAt(0) == '"' && s.charAt(s.length()-1) == '"') {
            return s.substring(1, s.length()-1);
        }

        return s;
    }
}
