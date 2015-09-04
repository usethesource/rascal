package org.rascalmpl.shell.compiled;

import java.io.IOException;

public class CompiledRascalShellDelayed {
	public static void main(String[] args) {
		try {
			Thread.sleep(10_000);
			CompiledRascalShell.main(args);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
