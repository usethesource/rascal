/** 
 * Copyright (c) 2017, Mauricio Verano Merino, Centrum Wiskunde & Informatica (CWI) 
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
package org.rascalmpl.library.util.notebook;

import java.io.IOException;
import de.neuland.jade4j.Jade4J;
import de.neuland.jade4j.exceptions.JadeCompilerException;
import de.neuland.jade4j.template.JadeTemplate;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValueFactory;

public class HTML {
    
    private final IValueFactory vf;
    
    public HTML(IValueFactory vf)
    {
        this.vf = vf;
    }
    
    public IString compile(IString loc){
        try {
            JadeTemplate template = Jade4J.getTemplate(loc.getValue());
            String html = Jade4J.render(template, null);
            return vf.string(html);
        }
        catch (JadeCompilerException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
