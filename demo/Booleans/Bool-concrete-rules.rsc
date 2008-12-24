module Bool-conc-rules

import languages/Booleans/syntax;

rule a1 true & <Bool B2>   => <B2>
rule a2 false & <Bool B2>  => false

rule o1 true | true        => true
rule o2 true | false       => true
rule o3 false | true       => true
rule o4 false | false      => false