module lang::rascalcore::compile::Examples::Tst0
         
str job(str nam, str (void () step) block) {   
      return "";
}                
           
str newGenerate() {	
    return job("Generating parser;", str (void () worked) { 
        int uniqueItem = 1; // -1 and -2 are reserved by the SGTDBF implementation
        int newItem() { uniqueItem += 1; return uniqueItem; };

        int rewrite() = newItem();
        rewrite();
    
        return "";
   
        });      
}  