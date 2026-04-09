module BreakingChanges

import IO;
import Map;
import ValueIO;
import Location;
import Set;
import String;
import util::FileSystem;
import lang::rascalcore::check::RascalConfig;
import analysis::typepal::TModel;

map[loc,loc] breakingDiffs(TModel old, TModel new){
    missing = domain(old.logical2physical) - domain(new.logical2physical);
    return domainR(old.logical2physical, missing);
}

set[loc] getTPLs(loc repo)
    = find(repo, "tpl");

bool requireDetails(str path, set[str] details)
    = !isEmpty(details) && any(d <- details, contains(path, d));

set[loc] similar(loc d, TModel new){
    dpath = d.path;
    n = findLast(dpath, "$");
    if(n >=0){
        dpath = dpath[..n];
    }
    found = {};
    for(x <- new.logical2physical){
        if(startsWith(x.path, dpath)){
            found += x;
        }
    }
    return found;
}
void reportDiffs(map[loc,loc] diffs, TModel old, TModel new){
    for(d <- diffs){
        println("old <d>: <old.logical2physical[d]>");
        for(s <- similar(d, new)){
            println("new <s>: <new.logical2physical[s]>");
        }
    }
}
void breaking(loc old, loc new, set[str] details = {}){
    oldTPLs = getTPLs(old); oldRelTPLs = {relativize(old, tpl) | tpl <- oldTPLs};
    newTPLs = getTPLs(new); newRelTPLs = {relativize(new, tpl) | tpl <- newTPLs};
    set[str] removed = {};
    nchanges = 0;
    for(oldRelTPL <- oldRelTPLs){
        oldPath = oldRelTPL.path;
        tmOld = readBinaryValueFile(#TModel, old + oldPath);
        if(exists(new + oldPath)){
            tmNew = readBinaryValueFile(#TModel, new + oldPath);
            b = breakingDiffs(tmOld, tmNew);
            if(!isEmpty(b)){
                nchanges += size(b);
                println("- <oldPath> <size(b)> changes");
                if(requireDetails(oldPath, details)){
                    reportDiffs(b, tmOld, tmNew);
                }
            }
        } else {
            removed += oldPath;
            println("- <oldPath> was removed");
        }
    }
    println("\nSUMMARY
            '   Old: <old>, <size(oldTPLs)> tpls, 
            '   New: <new>, <size(newTPLs)> tpls
            '   Files in Old not in New: <size(removed)><for(r <- removed){>
            '       <r><}>
            '   New files in New: <size(newRelTPLs - oldRelTPLs)> <for(a <- newRelTPLs - oldRelTPLs){>
            '       <a.path><}>
            '   Breaking changes: <nchanges>");
}

void main(loc old = |mvn://org.rascalmpl--rascal--0.40.17|,
          loc new = |mvn://org.rascalmpl--rascal--0.41.0-RC54|,
          set[str] details = {"Content"}){
    breaking(old, new, details = details);
}