/** 
 * Copyright (c) 2020, Mauricio Verano Merino, Centrum Wiskunde & Informatica (NWOi - CWI) 
 * All rights reserved. 
 *  
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met: 
 *  
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer. 
 *  
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution. 
 *  
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */ 
package org.rascalmpl.library.util.REPL;

import java.util.Set;


public class ExecutionGraph {
    
    private String currentNode;
    private Set<CustomNode> nodes;
    private Set<CustomEdge> edges;
    
    public ExecutionGraph(String current, Set<CustomNode> nodes, Set<CustomEdge> edges) {
        super();
        this.currentNode = current;
        this.nodes = nodes;
        this.edges = edges;
    }
    
    public static class CustomEdge {
        
        private String value;
        private String nodeU;
        private String nodeV;
        
        public CustomEdge(String nodeU, String nodeV, String value) {
            this.value = value;
            this.nodeU = nodeU;
            this.nodeV = nodeV;
        }
    }
    
    public static class CustomNode {

        private String input;
        private String result;
        private String hash;
        
        public String getInput() {
            return input;
        }

        public String getResult() {
            return result;
        }
        
        public String getHash() {
            return hash;
        }

        public CustomNode(String input, String result, String hash) {
            super();
            this.input = input;
            this.result = result;
            this.hash = hash;
        }
    }

}
