/** 
 * Copyright (c) 2016, Davy Landman, Centrum Wiskunde & Informatica (CWI) 
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
package org.rascalmpl.value.io.binary.wire;

import java.io.Closeable;
import java.io.IOException;
import java.util.Map;


/**
 * StAX-style reader for the Wire format.
 */
public interface IWireInputStream extends Closeable {

    int MESSAGE_START = 0;
    int FIELD = 1;
    int MESSAGE_END = 2;

    /**
     * Advances to the next start of a message, field in a message, or end of a message.<br/>
     * It will read both the identifier of the field/message, and in case of a field, also read the value.<br/>
     * Except in case of an field with an nested message, this message is not read.
     * @return The current position in the file (either {@linkplain IWireInputStream#MESSAGE_START}, {@linkplain IWireInputStream#FIELD}, or {@linkplain IWireInputStream#MESSAGE_END})
     * @throws IOException
     */
    int next() throws IOException;

    /**
     * @return The current position in the file (either {@linkplain IWireInputStream#MESSAGE_START}, {@linkplain IWireInputStream#FIELD}, or {@linkplain IWireInputStream#MESSAGE_END})
     */
    int current();

    /**
     * The current read message id. <br/>
     * Note: normally you can use this to keep track of which message you are in, however, if you are using nested messages (not adviced), this won't work.
     * @return the message id
     */
    int message();
    /**
     * The current read field id
     * @return the field id
     */
    int field();
    /**
     * The current field type, see {@link FieldKind} for the different kind of field types.<br/>
     * Normally, the value of the field is also read, for type of {@linkplain FieldKind#NESTED} we do not read the nested message, and a call to {@linkplain next} is needed to move to 
     * 
     * @return
     */
    int getFieldType();

    /**
     * get the integer value, only valid if {@linkplain #getFieldType()} is {@linkplain FieldKind#INT}
     * @return
     */
    int getInteger();
    /**
     * get the integer value, only valid if {@linkplain #getFieldType()} is {@linkplain FieldKind#STRING}
     * @return
     */
    String getString();
    /**
     * get the integer value, only valid if {@linkplain #getFieldType()} is {@linkplain FieldKind#BYTES}
     * @return
     */
    byte[] getBytes();


    /**
     * get the type of the repeated value, only valid if {@linkplain #getFieldType()} is {@linkplain FieldKind#REPEATED} <br />
     * <br />
     * Note that if the repeated type is {@linkplain FieldKind#NESTED}, the caller has to take care of reading {@linkplain #getRepeatedLength()} messages.
     * @return
     */
    int getRepeatedType();
    /**
     * get the arity of the repeated value, only valid if {@linkplain #getFieldType()} is {@linkplain FieldKind#REPEATED}
     * @return
     */
    int getRepeatedLength();
    /**
     * get string array, only valid if {@linkplain #getFieldType()} is {@linkplain FieldKind#REPEATED} and {@linkplain #getRepeatedType()} is {@link FieldKind.Repeated#STRINGS}
     * @return
     */
    String[] getStrings();
    /**
     * get int array, only valid if {@linkplain #getFieldType()} is {@linkplain FieldKind#REPEATED} and {@linkplain #getRepeatedType()} is {@link FieldKind.Repeated#STRINGS}
     * @return
     */
    int[] getIntegers();
    

    /**
     * skip the current message, also takes care to skip nested messages
     * @throws IOException
     */
    void skipMessage() throws IOException;

    default void skipNestedField() throws IOException {
        if (getFieldType() == FieldKind.NESTED) {
            next(); // go into the message
            skipMessage();
        }
        else if (getFieldType() == FieldKind.REPEATED && getRepeatedType() == FieldKind.NESTED) {
            int repeated = getRepeatedLength();
            for (int i = 0; i < repeated; i++ ){
                next(); // go into the message
                skipMessage();                            
            }
        }
    }
}
