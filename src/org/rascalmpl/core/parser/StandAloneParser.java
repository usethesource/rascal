package org.rascalmpl.core.parser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.util.jar.Attributes;

public class StandAloneParser {
	private static final String BAD_JAR = "Jar file not correctly set up";
	private static final String EXPECTED_ARG = "Expected argument to option";
	private static final String NO_PARSER = "Parser not found in jar file";
	private static String rascalPath = null;
	private static String startSymbol = null;
	private static String input = null;
	/**
	 * @param args
	 * @throws Throwable 
	 */
	public static void main(String[] args) throws Throwable {
		URL resource = StandAloneParser.class.getResource("StandAloneParser.class");

		if(resource.getProtocol().equals("jar")) {
			URLConnection conn = resource.openConnection();
			if(conn instanceof JarURLConnection) {
				Attributes mainAttributes = ((JarURLConnection) conn).getMainAttributes();
				String clsName = mainAttributes.getValue("X-Rascal-Saved-Class");
				if(clsName == null)
					fatal(BAD_JAR, "No saved parser class");

				parseOptions(args);

				String classPath = System.getenv("CLASSPATH");
				String[] split = classPath != null ? classPath.split(System.getProperty("path.separator")) : new String[0];

				URL[] urls = new URL[split.length + 1 + (rascalPath != null ? 1 : 0)];
				int i = 0;

				if(rascalPath != null)
					urls[i++] = new URL("file", null, rascalPath);

				for(String path : split) {
					urls[i++] = new URL("file", null, path);
				}
				urls[i++] = ((JarURLConnection) conn).getJarFileURL();

				// System.out.println("URLs: " + Arrays.toString(urls));

				try(URLClassLoader loader = new URLClassLoader(urls, null)) {
					Class<?> clazz = loader.loadClass(clsName);

					if(startSymbol == null) {
						// attempt to guess start symbol from available classes
						for(Class<?> f : clazz.getDeclaredClasses()) {
							if(f.getSimpleName().startsWith("start__")) {
								startSymbol = f.getSimpleName();
								System.err.println("Using start symbol " + startSymbol);
								break;
							}
						}
					}
					if(startSymbol == null)
						fatal("Start symbol required", "--start STARTSYMBOL");


					Class<?> INodeFlattener = loader.loadClass("org.rascalmpl.core.parser.gtd.result.out.INodeFlattener");
					Class<?> INodeConstructorFactory = loader.loadClass("org.rascalmpl.core.parser.gtd.result.out.INodeConstructorFactory");
					Class<?> DefaultNodeFlattener = loader.loadClass("org.rascalmpl.core.parser.gtd.result.out.DefaultNodeFlattener");
					Class<?> UPTRNodeFactory = loader.loadClass("org.rascalmpl.core.parser.uptr.UPTRNodeFactory");
					Class<?> ParseError = loader.loadClass("org.rascalmpl.core.parser.gtd.exception.ParseError");
					Class<?> UndeclaredNonTerminalException = loader.loadClass("org.rascalmpl.core.parser.gtd.exception.UndeclaredNonTerminalException");
					
					
					Method method = clazz.getMethod("parse", String.class, URI.class, char[].class, INodeFlattener, INodeConstructorFactory);
					URI inputURI;
					if(input != null)
						inputURI = new URI("file", input, null);
					else
						inputURI = new URI("stdin", "//", null);

					try {
						Object invoke = method.invoke(clazz.newInstance(), startSymbol, inputURI, getInput(), DefaultNodeFlattener.newInstance(), UPTRNodeFactory.newInstance());
						System.out.println(invoke);
					}
					catch(InvocationTargetException e) {
						Throwable cause = e.getCause();
						if(ParseError.isInstance(cause)) {
							fatal(cause.toString(), null);
						}
						else if(UndeclaredNonTerminalException.isInstance(cause)) {
							fatal(cause.getMessage(), "(perhaps start symbol doesn't exist?)");
						}
						throw e.getCause();
					}
				}
				catch(NoSuchMethodException | ClassNotFoundException e) {
					fatal(NO_PARSER, e.getMessage());
				}
			}
			else {
				fatal(BAD_JAR, "Not running from a JAR file");
			}
		}
		else {
			fatal(BAD_JAR, "Not running from a JAR file");
		}

	}

	private static char[] getInput() {
		InputStream inputStream = null;
		try {
			try {
				if(input != null) {
					inputStream = new FileInputStream(input);
				}
				else {
					inputStream = System.in;
				}

				StringBuilder builder = new StringBuilder();
				char[] buffer = new char[8192];

				InputStreamReader reader = new InputStreamReader(inputStream);
				while(true) {
					int n = reader.read(buffer);
					if(n == -1)
						break;
					builder.append(buffer, 0, n);
				}
				return builder.toString().toCharArray();
			} 
			finally {
				if(inputStream != null && inputStream != System.in)
					inputStream.close();
			}
		}
		catch (FileNotFoundException e) {
			fatal("Input file not found", input);
		}
		catch (IOException e) {
			fatal("I/O error reading input file", e.getMessage());
		}
		return null; // can't happen
	}

	private static void parseOptions(String[] args) {
		int i = 0;
		while(i < args.length) {
			switch(args[i]) {
			case "-r":
			case "--rascal":
				if(++i < args.length) {
					rascalPath = args[i++];
				}
				else {
					fatal(EXPECTED_ARG, "rascal path");
				}
				break;
			case "-s":
			case "--start":
				if(++i < args.length) {
					startSymbol = args[i++];
				}
				else {
					fatal(EXPECTED_ARG, "start symbol");
				}
				break;
			case "-h":
			case "--help":
				System.err.println("usage: java -jar parserJarFile [options] inputFile");
				System.err.println("options:");
				System.err.println("    -r, --rascal PATH       give path to Rascal installation (either jar or dir)");
				System.err.println("    -s, --start NAME        name of start symbol");
				System.err.println("    -h, --help              print help");
				System.exit(0);
				break;
			default:
				if(input != null) {
					fatal("name of input file already given", args[i]);
				}
				else {
					input = args[i++];
				}
				break;
			}
		}

	}

	private static void fatal(String msg1, String msg2) {
		if(msg2 == null)
			System.err.println(msg1);
		else
			System.err.println(msg1 + ": " + msg2);
		System.exit(1);
	}

}
