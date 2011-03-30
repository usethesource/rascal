package org.rascalmpl.interpreter.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class Shell{
	private final BufferedReader br;
	private final OutputStream out;
	
	public Shell(InputStream in, OutputStream out){
		super();
		
		this.br = new BufferedReader(new InputStreamReader(in));
		this.out = out;
	}
	
	public String readLine() throws IOException{
		return br.readLine();
	}
	
	public void print(String s) throws IOException{
		out.write(s.getBytes());
	}
}
