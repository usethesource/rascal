module lang::rascalcore::compile::Examples::Tst1

layout Whitespace = [\ \t\n]*;

start syntax D = "d";
start syntax DS = D+;

<<<<<<< Upstream, based on branch 'master' of https://github.com/usethesource/rascal-core.git
value main() = (DS)`d d d`;
=======
value main() = (DS)`d d d`;
>>>>>>> 213075d Test case
