package StandardLibrary;

import java.util.Iterator;import java.util.Map.Entry;

import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IValue;

public class TEMP {

	private IValue arb(IMap m)
	{
	   int i = 0;
	   int k = random.nextInt(m.size());
	   Iterator iter = m.entryIterator();
	  
	   while(iter.hasNext()){
	      if(i == k){
	      	return (IValue) ((Entry) iter.next()).getKey();
	      }
	      iter.next();
	      i++;
	   }
	   return null;
	} 
	
}
