module lang::rascal::tests::library::lang::json::Issue2633

import lang::json::IO;
import IO;

data OuterData = outer(str name, str \type, list[Nested] nested_abc);
data Nested = nested(str \type, map[str, value] property__1, map[value, value] property__2);

test bool failsOnCertainJSON() {
    str input = "{
                '  \"name\": \"TESTING_  Adding one more character to this data causes the test to fail\",
                '  \"type\": \"type_abcd\",
                '  \"nested_abc\": [
                '    {
                '      \"type\": \"line\",
                '      \"property__1\": { \"y\": { \"value\": 2, \"absolute\": false } },
                '      \"property__2\": {}
                '    },
                '    {
                '      \"type\": \"line\",
                '      \"property__1\": { \"y\": { \"value\": 2, \"absolute\": false } },
                '      \"property__2\": {}
                '    },
                '    {
                '      \"type\": \"line\",
                '      \"property__1\": { \"y\": { \"value\": 2, \"absolute\": false } },
                '      \"property__2\": {}
                '    },
                '    {
                '      \"type\": \"line\",
                '      \"property__1\": { \"y\": { \"value\": 2, \"absolute\": false } },
                '      \"property__2\": {}
                '    },
                '    {
                '      \"type\": \"line\",
                '      \"property__1\": { \"y\": { \"value\": 2, \"absolute\": false } },
                '      \"property__2\": {}
                '    },
                '    {
                '      \"type\": \"line\",
                '      \"property__1\": { \"y\": { \"value\": 2, \"absolute\": false } },
                '      \"property__2\": {}
                '      },
                '    {
                '      \"type\": \"line\",
                '      \"property__1\": { \"y\": { \"value\": 2, \"absolute\": false } },
                '      \"property__2\": {}
                '    }
                '  ]
                '}
                '";

    // Fails when the original data above is increased by even one character (e.g. the name, or changing a 2 to 20, or even adding a meaningless space anywhere)
    parsedVFD = parseJSON(#OuterData, input, trackOrigins=true, lenient=true);
    println("Parsed VisualFormData (After)");
    iprintln(parsedVFD);

    return true;
}