module IO

public void java println(value V...)
@java-imports{import java.io.File;}
{
   IList argList = (IList) V;
   for(int i = 0; i < argList.length(); i++){
   	System.out.print(argList.get(i).toString());
   }
   System.out.println();
   return;
}

public list[str] java readFile(str filename)
throws file_not_found(str msg), read_error(str msg)
@java-imports
{
	import java.io.File; 
	import java.io.FileReader;
	import java.io.BufferedReader;
	import java.io.IOException;
	import java.io.FileNotFoundException;
}
{
  IList res = null;
  try {
  	BufferedReader in = new BufferedReader(new FileReader(filename.getValue()));
  	String line;
  	
  	IListWriter w = types.listType(types.stringType()).writer(values);
  	do {
  		line = in.readLine();
  		if(line != null){
  			w.append(values.string(line));
  		}
  	} while (line != null);
  	in.close();
  	res =  w.done();
  }
    catch (FileNotFoundException e){
  	throw new RascalException(values, "file_not_found");
  }
  catch (IOException e){
    throw new RascalException(values, "read_error");
  }

  return res;
}


