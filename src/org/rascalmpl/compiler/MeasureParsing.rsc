@license{
Copyright (c) 2018-2025, NWO-I CWI, Swat.engineering and Paul Klint
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice,
this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice,
this list of conditions and the following disclaimer in the documentation
and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
POSSIBILITY OF SUCH DAMAGE.
}
module MeasureParsing

import util::Reflective;
import util::Benchmark;
import lang::rascal::\syntax::Rascal;
import IO;
import ValueIO;
import lang::rascalcore::check::ModuleLocations;

import  lang::rascalcore::check::TestConfigs;

void main(){
    pcfg = getAllSrcPathConfig();
    start_time = cpuTime();
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
    println("Total time: <(cpuTime() - start_time)/1000000> ms");
}