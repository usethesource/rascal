module lang::rascalcore::check::tests::usedef::Util

import IO;
import List;
import Location;
import String;
import lang::rascalcore::check::Checker;
import lang::rascalcore::check::TestConfigs;
import util::Reflective;
import util::UUID;

data Expect
    = use(str id, int rank)
    | def(str id, int rank)
    | useDef(str id, value useRank, value defRank)
    ;

bool testUseDef(str moduleBody, list[Expect] expects) {
    str moduleName = "_" + replaceAll(uuid().authority, "-", "_");
    return testUseDef((moduleName: moduleBody), expects);
}

bool testUseDef(map[str, str] moduleBodies, list[Expect] expects) {
    loc testUuid = uuid();
    loc testDir = |memory://<testUuid.authority>/src|;
    
    // Write modules
    list[str] moduleNames = [s | str s <- moduleBodies];
    list[loc] testFiles = [];
    for (str moduleName <- moduleNames) {
        loc l = testDir + "<moduleName>.rsc";
        str s = "module <moduleName>\n<moduleBodies[moduleName]>";
        writeFile(l, s);
        testFiles += l;
    }

    // Compile modules
    PathConfig pathConfig = getDefaultTestingPathConfig();
    pathConfig.srcs += [testDir];
    ModuleStatus moduleStatus = rascalTModelForLocs(testFiles, rascalCompilerConfig(pathConfig)[assertValidDefines = true][assertValidUseDef = true], dummy_compile1);
    map[str, Module] pts = (s: pt | str s <- moduleNames, <_, pt, _> := getModuleParseTree(s, moduleStatus));
    map[str, TModel] tms = (s: tm | str s <- moduleNames, <_, tm, _> := getTModelForModule(s, moduleStatus));

    for (str s <- moduleNames) {
        assert [] == [m | m <- tms[s].messages, m is error] : "<tms[s].messages>";
    }

    // Compute rankings
    map[str, map[str, list[loc]]] rankings = (s: () | str s <- moduleNames);
    for (str moduleName <- moduleNames) {
        rankings += (moduleName: ());
        
        void add(Tree t) {
            str id = "<t>";
            rankings[moduleName] += id in rankings[moduleName] ? () : (id: []);
            rankings[moduleName][id] += t.src;
        }

        top-down-break visit (pts[moduleName]) {
            case Name t: add(t);
            // case QualifiedName t: add(t);
            case Nonterminal t: add(t);
            case NonterminalLabel t: add(t);
        }
    }

    // Computer ranks
    map[loc, tuple[str moduleName, str id, int rank]] ranks = ();
    for (str moduleName <- moduleNames, str id <- rankings[moduleName], int rank <- [1..size(rankings[moduleName][id]) + 1]) {
        loc l = rankings[moduleName][id][rank - 1];
        ranks[l] = <moduleName, id, rank>;
    }

    // Define auxiliary functions
    loc toPhysical(loc l, TModel tm)
        = l in tm.logical2physical ? tm.logical2physical[l] : l; 
    
    str getModuleName(loc l, TModel tm) {
        l = toPhysical(l, tm);
        map[loc, str] moduleNames = (toPhysical(scope, tm): scope.path[1..] | loc scope <- tm.scopes, "rascal+module" == scope.scheme);
        for (loc moduleLoc <- moduleNames) {
            if (isContainedIn(l, moduleLoc)) {
                return moduleNames[moduleLoc];
            }
        }
        assert false;
    }

    str defaultModuleName() {
        assert [_] := moduleNames;
        return moduleNames[0];
    }

    // Add actuals
    list[Expect] actuals = [];
    for (str s <- moduleNames, <loc useLoc, loc defLoc> <- tms[s].useDef) {
        TModel tm = tms[s];
        useLoc = toPhysical(useLoc, tm);
        defLoc = toPhysical(defLoc, tm);
        value useRank = useLoc in ranks ? <getModuleName(useLoc, tm), ranks[useLoc].rank> : "unranked";
        value defRank = defLoc in ranks ? <getModuleName(defLoc, tm), ranks[defLoc].rank> : "unranked";
        actuals += useDef(ranks[useLoc].id, useRank, defRank);
    }

    // Update actuals and expects
    if ([_] := moduleNames) {
        actuals = visit (actuals) {
            case Expect e: useDef(_, <_, int i>, _) => e[useRank = i]
        }
        actuals = visit (actuals) {
            case Expect e: useDef(_, _, <_, int i>) => e[defRank = i]
        }
        expects = visit (expects) {
            case Expect e: useDef(_, <_, int i>, _) => e[useRank = i]
        }
        expects = visit (expects) {
            case Expect e: useDef(_, _, <_, int i>) => e[defRank = i]
        }
    }

    actuals = sort(actuals);
    expects = sort(expects);

    // Compare expects and actuals
    for (Expect e <- expects) {
        if (e notin actuals) {
            str message = "Expected: `<e>`. Actual: `<iprintToString(actuals)>`.";
            println("[ERROR] <message>");
            assert false : message;
        }
    }

    // Check expectations
    // top-down-break visit (expects) {
    //     case Expect e: use(_, _): {
    //         str moduleName = !e.moduleName? && [str s] := moduleNames ? s : e.moduleName;
    //         assert any(
    //             Use u <- tms[moduleName].uses,
    //             e.id == u.id,
    //             0 <= e.rank - 1 && e.rank - 1 < size(rankings[e.id][moduleName]),
    //             rankings[e.id][moduleName][e.rank - 1] == convert(u.occ, tms[moduleName].logical2physical)
    //         ) : "Expected: `<e>`. Actual: `<tms[moduleName].uses>`.";
    //     }

    //     case Expect e: def(_, _): {
    //         str moduleName = !e.moduleName? && [str s] := moduleNames ? s : e.moduleName;
    //         assert any(
    //             Define d <- tms[moduleName].defines,
    //             e.id == d.id,
    //             0 <= e.rank - 1 && e.rank - 1 < size(rankings[e.id][moduleName]),
    //             rankings[e.id][moduleName][e.rank - 1] == convert(d.defined, tms[moduleName].logical2physical)
    //         ) : "Expected: `<e>`. Actual: `<tms[moduleName].defines>`.";
    //     }

    //     case Expect e: useDef(_, _, _): {
    //         str useModuleName = !e.useModuleName? && [str s] := moduleNames ? s : e.useModuleName;
    //         str defModuleName = !e.defModuleName? && [str s] := moduleNames ? s : e.defModuleName;
    //         assert any(
    //             <loc useLoc, loc defLoc> <- tms[useModuleName].useDef,
    //             0 <= e.useRank - 1 && e.useRank - 1 < size(rankings[useModuleName][e.id]),
    //             0 <= e.defRank - 1 && e.defRank - 1 < size(rankings[defModuleName][e.id]),
    //             rankings[useModuleName][e.id][e.useRank - 1] == convert(useLoc, tms[useModuleName].logical2physical),
    //             rankings[defModuleName][e.id][e.defRank - 1] == convert(defLoc, tms[defModuleName].logical2physical)
    //         ) : "Expected `<e>`. Actual: `<breakpoint(actuals)>`.";
    //     }
    // } 

    remove(testDir);
    return true;
}

&T breakpoint(&T v) {
    return v;
}
