/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Bert Lisser - Bert.Lisser@cwi.nl (CWI)
*******************************************************************************/
package org.rascalmpl.library.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.parser.gtd.util.ArrayList;

public class SystemAPI {
	private final static int STREAM_READ_SEGMENT_SIZE = 8192;

	private final IValueFactory values;

	public SystemAPI(IValueFactory values) {
		this.values = values;
		
	}
	
	public ISourceLocation resolveLoc(ISourceLocation loc, IEvaluatorContext ctx) {
		URI uri = _resolveLoc(loc, ctx);
		return ctx.getValueFactory().sourceLocation(uri);
	}
	
	private URI _resolveLoc(ISourceLocation loc, IEvaluatorContext ctx) {
		URI inputUri = loc.getURI();
		if (inputUri.getScheme().equals("http")) {
			return inputUri;
		}
		
		try {
			URI  resourceUri = ctx.getResolverRegistry().getResourceURI(inputUri);
			return resourceUri;	
		} catch (IOException e) {
			throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
		}
		
	}

	public IValue getSystemProperty(IString v) {
		return values.string(java.lang.System.getProperty(v.getValue()));
	}

	private IList readLines(Reader in, java.lang.String... regex_replacement)
			throws IOException {
		ArrayList<char[]> segments = new ArrayList<char[]>();

		// Gather segments.
		int nrOfWholeSegments = -1;
		int bytesRead;
		do {
			char[] segment = new char[STREAM_READ_SEGMENT_SIZE];
			bytesRead = in.read(segment, 0, STREAM_READ_SEGMENT_SIZE);
			segments.add(segment);
			nrOfWholeSegments++;
		} while (bytesRead == STREAM_READ_SEGMENT_SIZE);
		// Glue the segments together.
		char[] segment = segments.get(nrOfWholeSegments);
		char[] input;
		if (bytesRead != -1) {
			input = new char[(nrOfWholeSegments * STREAM_READ_SEGMENT_SIZE)
					+ bytesRead];
			System.arraycopy(segment, 0, input,
					(nrOfWholeSegments * STREAM_READ_SEGMENT_SIZE), bytesRead);
		} else {
			input = new char[(nrOfWholeSegments * STREAM_READ_SEGMENT_SIZE)];
		}
		for (int i = nrOfWholeSegments - 1; i >= 0; i--) {
			segment = segments.get(i);
			System.arraycopy(segment, 0, input, (i * STREAM_READ_SEGMENT_SIZE),
					STREAM_READ_SEGMENT_SIZE);
		}
		java.lang.String b = java.lang.String.valueOf(input);
		java.lang.String[] d = regex_replacement.length == 0 ? b.split("\n")
				: new java.lang.String[] { b };
		IListWriter r = values.listWriter();
		Pattern p = Pattern.compile("\\(\\w*\\)[ \t]*$");
		java.lang.String found = null;
		for (java.lang.String e : d) {
			if (regex_replacement.length == 8) {
				java.lang.String[] f = e.split(regex_replacement[0]);
				StringBuffer a = new StringBuffer();
				for (int i = 0; i < f.length; i++) {
					if (i % 2 != 0) {
						a.append(regex_replacement[1]);
						if (found!=null) {
							// System.err.println("found"+found);
							a.append(found);
							found = null;
						}
						a.append(regex_replacement[0]);
						java.lang.String q = f[i];
						q=q.replaceAll(regex_replacement[2],
								regex_replacement[3]);
						q=q.replaceAll(regex_replacement[4],
								regex_replacement[5]);
						q=q.replaceAll(regex_replacement[6],
								regex_replacement[7]);
						a.append(q);
						a.append(regex_replacement[0]);
						a.append(regex_replacement[1]);
					} else {
						Matcher m = p.matcher(f[i]);
						boolean z = m.find();
						if (z) {
							found = m.group();
							a.append(m.replaceFirst(""));
						    }
						else a.append(f[i]);
					    }
				}
				e = a.toString();
			}
			r.append(values.string(e));
		}
		return r.done();
	}

	public IValue getFileContent(IString g) {
		java.lang.String s = File.separator
				+ this.getClass().getCanonicalName().replaceAll("\\.",
						File.separator);
		s = s.substring(0, s.lastIndexOf(File.separatorChar));
		if (g != null)
			s += (File.separator + g.getValue());
		InputStreamReader a = new InputStreamReader(getClass()
				.getResourceAsStream(s));
		try {
			IList r = readLines(a);
			// System.out.println(r);
			return r;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public IValue getRascalFileContent(ISourceLocation g) {
		FileReader a = null;
		try {
			a = new FileReader(g.getURI().getPath());
			IList r = readLines(a, "`", "\"", "\"", "\\\\\"", "<", "\\\\<",
					">", "\\\\>");
			// System.out.println(((IString) r.get(0)).getValue());
			return r.get(0);
		} catch (FileNotFoundException fnfex) {
			throw RuntimeExceptionFactory.pathNotFound(g, null, null);
		} catch (IOException ioex) {
			throw RuntimeExceptionFactory.io(values.string(ioex.getMessage()),
					null, null);
		} finally {
			if (a != null) {
				try {
					a.close();
				} catch (IOException ioex) {
					throw RuntimeExceptionFactory.io(values.string(ioex
							.getMessage()), null, null);
				}
			}
		}
	}

	public IValue getLibraryPath(IString g) {
		try {
			java.lang.String s = File.separator
					+ this.getClass().getCanonicalName().replaceAll("\\.",
							File.separator);
			s = s.substring(0, s.lastIndexOf(File.separatorChar));
			if (g != null)
				s += (File.separator + g.getValue());
			IValue v = values.sourceLocation(getClass().getResource(s).toURI());
			return v;
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}
