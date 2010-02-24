/*
 * @(#)StorableInput.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.util;

import java.io.*;
import java.awt.Color;
import java.util.List;

/**
 * An input stream that can be used to resurrect Storable objects.
 * StorableInput preserves the object identity of the stored objects.
 *
 * @see Storable
 * @see StorableOutput
 *
 * @version <$CURRENT_VERSION$>s
 */
public class StorableInput {

	private StreamTokenizer fTokenizer;
	private List            fMap;

	/**
	 * Initializes a Storable input with the given input stream.
	 */
	public StorableInput(InputStream stream) {
		Reader r = new BufferedReader(new InputStreamReader(stream));
		fTokenizer = new StreamTokenizer(r);
		// include inner class separate in class names
		fTokenizer.wordChars('$', '$');
		fMap = CollectionsFactory.current().createList();
	}

	/**
	 * Reads and resurrects a Storable object from the input stream.
	 */
	public Storable readStorable() throws IOException {
		Storable storable;
		String s = readString();

		if (s.equals("NULL")) {
			return null;
		}

		if (s.equals("REF")) {
			int ref = readInt();
			return retrieve(ref);
		}

		storable = (Storable) makeInstance(s);
		map(storable);
		storable.read(this);
		return storable;
	}

	/**
	 * Reads a string from the input stream.
	 */
	public String readString() throws IOException {
		int token = fTokenizer.nextToken();
		if (token == StreamTokenizer.TT_WORD || token == '"') {
			return fTokenizer.sval;
		}

		String msg = "String expected in line: " + fTokenizer.lineno();
		throw new IOException(msg);
	}

	/**
	 * Reads an int from the input stream.
	 */
	public int readInt() throws IOException {
		int token = fTokenizer.nextToken();
		if (token == StreamTokenizer.TT_NUMBER) {
			return (int) fTokenizer.nval;
		}

		String msg = "Integer expected in line: " + fTokenizer.lineno();
		IOException exception =  new IOException(msg);
		exception.printStackTrace();
		throw new IOException(msg);
	}

	/**
	 * Reads an int from the input stream.
	 */
	public long readLong() throws IOException {
		long token = fTokenizer.nextToken();
		if (token == StreamTokenizer.TT_NUMBER) {
			return (long)fTokenizer.nval;
		}
		String msg = "Long expected in line: " + fTokenizer.lineno();
		IOException exception =  new IOException(msg);
		//exception.printStackTrace();
		throw exception;
	}

	/**
	 * Reads a color from the input stream.
	 */
	public Color readColor() throws IOException {
		return new Color(readInt(), readInt(), readInt());
	}

	/**
	 * Reads a double from the input stream.
	 */
	public double readDouble() throws IOException {
		int token = fTokenizer.nextToken();
		if (token == StreamTokenizer.TT_NUMBER) {
			return fTokenizer.nval;
		}

		String msg = "Double expected in line: " + fTokenizer.lineno();
		throw new IOException(msg);
	}

	/**
	 * Reads a boolean from the input stream.
	 */
	public boolean readBoolean() throws IOException {
		int token = fTokenizer.nextToken();
		if (token == StreamTokenizer.TT_NUMBER) {
			return ((int) fTokenizer.nval) == 1;
		}

		String msg = "Integer expected in line: " + fTokenizer.lineno();
		throw new IOException(msg);
	}

	private Object makeInstance(String className) throws IOException {
		try {
			Class cl = Class.forName(className);
			return cl.newInstance();
		}
		catch (NoSuchMethodError e) {
			throw new IOException("Class " + className
				+ " does not seem to have a no-arg constructor");
		}
		catch (ClassNotFoundException e) {
			throw new IOException("No class: " + className);
		}
		catch (InstantiationException e) {
			throw new IOException("Cannot instantiate: " + className);
		}
		catch (IllegalAccessException e) {
			throw new IOException("Class (" + className + ") not accessible");
		}
	}

	private void map(Storable storable) {
		if (!fMap.contains(storable)) {
			fMap.add(storable);
		}
	}

	private Storable retrieve(int ref) {
		return (Storable)fMap.get(ref);
	}
}
