package org.rascalmpl.bridge;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * TODO: implement this annotation such that classes that use it get transparant access to the functions of a Rascal module via normal Java methods.
 */
@Retention(RetentionPolicy.SOURCE) 
@Target({ElementType.TYPE}) 
public @interface Rascal {
   public String value();
}
