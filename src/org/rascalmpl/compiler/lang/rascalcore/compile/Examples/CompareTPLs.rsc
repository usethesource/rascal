module lang::rascalcore::compile::Examples::CompareTPLs

import IO;
import List;
import Location;
import Set;
import ValueIO;
import util::FileSystem;
import util::Monitor;

import analysis::typepal::TModel;
import lang::rascalcore::check::LogicalLocations;

str JOB = "Comparing TPLs";

void main() {
    compareTPLs();
}

rel[loc, loc, loc] compareTPLs() = job(JOB, rel[loc, loc, loc](void(str, int) step) {
    /*
        Preconditions
        1. Make sure the right Rascal release JAR is present in the Maven repository.
        2. Compile the same Rascal release locally and copy the JARs to the local target folder.
    */
    loc localTarget = |home:///swat/projects/Rascal/rascal/targetBackup/relocatedClasses|;
    loc remoteTarget = |mvn://org.rascalmpl--rascal--0.41.3-RC8|;

    rel[loc, loc, loc] differentLocations = {};
    step("Finding local TPLs", 1);
    allTPLs = sort(find(localTarget, "tpl"), byPathLength);
    jobTodo(JOB, work=size(allTPLs));

    for (tpl <- allTPLs) {
        relTplPath = relativize(localTarget, tpl);
        step("Comparing <relTplPath>", 1);
        differentLocations += toSet(compareTPL(relTplPath, localTarget, remoteTarget));
    }

    step("Computing statistics", 1);

    set[str] filesWithDiffs = {l.parent.path | l <- differentLocations<0>};
    set[loc] defs = differentLocations<0>;

    println("Number of tested TPLs: <size(allTPLs)>");
    println("Found <size(defs)> different locations in <size(filesWithDiffs)> files.");;

    print("Kinds of different locations: ");
    iprintln({l.scheme | l <- differentLocations<0>});

    return differentLocations;
}, totalWork=2);

bool byPathLength(loc a, loc b) = a.path < b.path;

lrel[loc, loc, loc] compareTPL(loc relTplPath, loc localTargetDir, loc unixTargetDir) {
    loc localTplPath = resolve(localTargetDir, relTplPath);
    loc unixTplPath = resolve(unixTargetDir, relTplPath);

    if (!exists(localTplPath)) {
        throw "Local TPL <localTplPath> does not exist";
    }
    if (!exists(unixTplPath)) {
        throw "Unix TPL <unixTplPath> does not exist";
    }

    localTpl = readBinaryValueFile(#TModel, localTplPath);
    unixTpl = readBinaryValueFile(#TModel, unixTplPath);
    
    differentDefs = [<ll, ul, localTpl.logical2physical[ll]> | <ll, ul> <- difference(localTpl.defines.defined, unixTpl.defines.defined)];
    if ([_, *_] := differentDefs) {
        println("Differences in defs of <relTplPath> (\<Local logical, Unix logical, local physical\>): ");
        iprintln(differentDefs);
        println();
    }

    return differentDefs;
}

lrel[loc, loc] difference(set[loc] lLocs, set[loc] uLocs) =
    [<l, u> | <l, u> <- pairs, !isEqualModuloNewlines(l, u)]
    when lrel[loc, loc] pairs := zip2(sort(lLocs, lessThan), sort(uLocs, lessThan));

bool isEqualModuloNewlines(loc localLoc, loc unixLoc) = isRascalLogicalLoc(localLoc)
    ? isEqualLogicalModuloNewlines(localLoc, unixLoc)
    : isEqualPhysicalModuloNewlines(localLoc, unixLoc);

bool isEqualLogicalModuloNewlines(loc localLoc, loc unixLoc) = localLoc == unixLoc;

bool isEqualPhysicalModuloNewlines(loc localLoc, loc unixLoc) {
    if (localLoc.uri != unixLoc.uri) {
        throw "URIs not equal: <localLoc> vs. <unixLoc>";
    }

    if (!localLoc.begin?) {
        // Cannot say anything sensible about newlines without line information
        return true;
    }

    if (localLoc.begin.line == localLoc.end.line) {
        // Single line
        return localLoc.length == unixLoc.length
            && localLoc.begin == unixLoc.begin
            && localLoc.end == unixLoc.end;
    }

    // Multi line
    return localLoc.begin == unixLoc.begin
        && localLoc.end == unixLoc.end;
}

bool lessThan(loc a, loc b) = a.offset? && a.uri == b.uri
    ? a.offset < b.offset
    : a.uri < b.uri;

bool lessThan(tuple[&A, &B] a, tuple[&A, &B] b) = a<0> != b<0>
    ? lessThan(a<0>, b<0>)
    : lessThan(a<1>, b<1>);
