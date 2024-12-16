module MeasureParsing

import util::Reflective;
import util::Benchmark;
import lang::rascal::\syntax::Rascal;
import IO;
import ValueIO;
import lang::rascalcore::check::ModuleLocations;

data PathConfig(
    loc resources = |unknown:///|,
    loc generatedResources = |unknown:///|,
    loc generatedSources = |unknown:///|
);

void main(){
    pcfg =pathConfig(   
        srcs = [
                |project://rascal/src/org/rascalmpl/library|, 
                |std:///|,
                |project://rascal-core/src/org/rascalmpl/core/library|,
                //|project://rascal_eclipse/src/org/rascalmpl/eclipse/library|,
                |project://typepal/src|
                //|project://salix/src|
                ],
        bin = |project://rascal-core/target/test-classes|,
        generatedSources = |project://rascal-core/target/generated-test-sources|,
        resources = |project://rascal-core/target/generated-test-resources|,
        libs = []
    );
    parse_time = 0;
    read_time = 0;
    write_time = 0;
    for(int _ <- [1..10]){
        for(qualifiedModuleName <- ["Boolean", "Type", "ParseTree", "List"]){
            mloc = getRascalModuleLocation(qualifiedModuleName, pcfg);    
            ploc = |file:///tmp/<qualifiedModuleName>.parsetree|;        
            //println("*** parsing <qualifiedModuleName> from <mloc>");
            start_time = cpuTime();
            pt = parseModuleWithSpaces(mloc).top;
            end_time = cpuTime();
            parse_time += end_time - start_time;
            begin_time = start_time;
            writeBinaryValueFile(ploc, pt);
            end_time = cpuTime();
            write_time += end_time - start_time;
            begin_time = start_time;
            
            readBinaryValueFile(#Module, ploc);
            end_time = cpuTime();
            read_time += end_time - start_time;
        }
    }
    println("parse_time: <parse_time>");
    println("write_time: <write_time>");
    println("read_time:  <read_time>");
    
    println("parse_time / (write_time + read_time): <parse_time * 1.0 / (write_time + read_time)>");
}