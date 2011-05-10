/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
*******************************************************************************/
package org.rascalmpl.library.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;

import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;

public class ShellExec {
	
	private static HashMap<IInteger, Process> runningProcesses = new HashMap<IInteger, Process>();
	private static IInteger processCounter = null;
	
	private final IValueFactory vf;

	public ShellExec(IValueFactory vf) {
		this.vf = vf;
	}

	public synchronized IInteger createProcess(IString processCommand) {
		return createProcessWithArgs(processCommand,vf.list(TypeFactory.getInstance().listType(TypeFactory.getInstance().stringType())));
	}

	public synchronized IInteger createProcessWithArgs(IString processCommand, IList arguments) {
		try {
			String[] args = new String[arguments.length()+1];
			args[0] = processCommand.getValue();
			for (int n = 0; n < arguments.length(); ++n) {
				if (arguments.get(n) instanceof IString) 
					args[n+1] = ((IString)arguments.get(n)).getValue();
				else
					throw RuntimeExceptionFactory.illegalArgument(arguments.get(n),null, "");
			}
			Process newProcess = Runtime.getRuntime().exec(args);
			if (processCounter == null) processCounter = vf.integer(0);
			processCounter = processCounter.add(vf.integer(1));
			runningProcesses.put(processCounter, newProcess);
			return processCounter;
		} catch (IOException e) {
			throw RuntimeExceptionFactory.javaException(e.toString(), null, e.getStackTrace().toString());
		}
	}

	public synchronized void killProcess(IInteger processId) {
		if (!runningProcesses.containsKey(processId))
			throw RuntimeExceptionFactory.illegalArgument(processId, null, null);
		Process runningProcess = runningProcesses.get(processId);
		runningProcess.destroy();
		runningProcesses.remove(processId);
		return;
	}
	
	public IString readFrom(IInteger processId) {
		if (!runningProcesses.containsKey(processId))
			throw RuntimeExceptionFactory.illegalArgument(processId, null, null);
		try {
			Process runningProcess = runningProcesses.get(processId);
			IString toReturn = vf.string("");
			InputStreamReader isr = new InputStreamReader(runningProcess.getInputStream());
			String line = "";
			while (isr.ready()) {
				line = line + (char)isr.read();
			}
			return vf.string(line);
		} catch (IOException e) {
			throw RuntimeExceptionFactory.javaException(e.toString(), null, e.getStackTrace().toString());
		}
	}

	public void writeTo(IInteger processId, IString msg) {
		if (!runningProcesses.containsKey(processId))
			throw RuntimeExceptionFactory.illegalArgument(processId, null, null);
		try {
			Process runningProcess = runningProcesses.get(processId);
			BufferedWriter buf = new BufferedWriter(new OutputStreamWriter(runningProcess.getOutputStream()));
			buf.write(msg.getValue());
			buf.flush();
		} catch (IOException e) {
			throw RuntimeExceptionFactory.javaException(e.toString(), null, e.getStackTrace().toString());
		}
	}

}
