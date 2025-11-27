module lang::rascalcore::compile::Examples::CheckerBug

import IO;
import List;
import util::FileSystem;
import util::Reflective;
import lang::rascalcore::check::Checker;

set[loc] checkAndGetTPLs(loc srcFile, RascalCompilerConfig ccfg) {
  msgs = check([srcFile], ccfg);
  iprintln(msgs);
  msgs = [program(l, errors) | program(loc l, allMsgs) <- msgs, errors := {m | m <- allMsgs, m is error}, errors !:= {}];
  if (msgs != []) {
    print("Errors: ");
    iprintln(msgs);
  }

  return find(ccfg.typepalPathConfig.bin, "tpl");
}

void main(loc birdCoreDir = |file:///Users/paulklint/git/bird/bird-core|) {
    // simplified path config from second-level run
    PathConfig pcfg = pathConfig(
        projectRoot = birdCoreDir
      , srcs = [birdCoreDir + "src/main/rascal"
                // ,|file:///Users/paulklint/git/rascal/src/org/rascalmpl/library|
                // ,|file:///Users/paulklint/git/typepal/src/|
      ]
      , libs = [
        //0.41.0-RC54 error
        //0.40.3-RC3 error
        //0.40.17 ok

        |mvn://org.rascalmpl--rascal--0.41.0-RC54|,
        |mvn://org.rascalmpl--typepal--0.15.1|
      ]
      , bin = birdCoreDir + "/target/classes"
      , resources = []
    );


    ccfg = rascalCompilerConfig(pcfg);

    // clean
    remove(pcfg.bin);
    println("Checking `lang::bird::Syntax`...");
    syntaxTPLs = checkAndGetTPLs(birdCoreDir + "/src/main/rascal/lang/bird/Syntax.rsc", ccfg);
    print("TPLs: ");
    iprintln(syntaxTPLs);

    println();

    // clean
    remove(pcfg.bin);
    println("Checking `lang::bird::Checker`...");
    checkerTPLs = checkAndGetTPLs(birdCoreDir + "/src/main/rascal/lang/bird/Checker.rsc", ccfg);
    print("TPLs: ");
    iprintln(checkerTPLs);
}
