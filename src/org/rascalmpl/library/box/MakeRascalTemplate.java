package org.rascalmpl.library.box;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

public class MakeRascalTemplate {
	
	static private Display screen = Display.getCurrent() == null ? new Display()
	: Display.getCurrent();

	final private Shell shell;

	private String outputFile, outputDir = System.getProperty("user.home")
			+ File.separatorChar + "asfix"+ File.separatorChar+"templates";

	private URI getFileName() {
		FileDialog dialog = new FileDialog(shell);
		String defaultDir = System.getProperty("DEFAULTDIR");
		if (defaultDir != null)
			dialog.setFilterPath(defaultDir);
		dialog.setFileName("Modules.sdf");
		String fileName = dialog.open();
		if (fileName == null) {
			System.err.println("Canceled");
			System.exit(0);
		}
		try {
			URI r = new URI("file", fileName, null);
			System.err.println("uri:" + r);
			return r;
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}
	
	MakeRascalTemplate() {
		shell = new Shell(screen);
		shell.setLayout(new FillLayout());
		URI uri = null;
		while (uri==null) uri = getFileName();
		File f = new File(uri.getPath());
		outputFile = f.getName().substring(0, f.getName().lastIndexOf('.'))+".rsc";
		final File dir = new File(outputDir);
		dir.mkdir();
		System.err.println("Can write:"+dir+" "+dir.canWrite()+" "+outputFile);
		if (dir.canWrite()) {
			IString v = (IString) new MakeBox().toSrc(uri);
			File of = new File(dir, outputFile);
			System.out.println(v.getValue());
			try {
				PrintStream p = new PrintStream(of);
				p.print(v.getValue());
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
	}
	
	public static void main(String[] args) {
		new MakeRascalTemplate();
	}

}
