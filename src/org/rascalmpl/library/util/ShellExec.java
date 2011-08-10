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
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;

import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;

public class ShellExec {
	
	private static HashMap<IInteger, Process> runningProcesses = new HashMap<IInteger, Process>();
	private static IInteger processCounter = null;
	
	private final IValueFactory vf;

	public ShellExec(IValueFactory vf) {
		this.vf = vf;
	}

	public IInteger createProcess(IString processCommand) {
		return createProcessInternal(processCommand,null,null,null);
	}

	public IInteger createProcess(IString processCommand, ISourceLocation workingDir) {
		return createProcessInternal(processCommand,null,null,workingDir);
	}

	public IInteger createProcess(IString processCommand, IList arguments) {
		return createProcessInternal(processCommand,arguments,null,null);
	}

	public IInteger createProcess(IString processCommand, IList arguments, ISourceLocation workingDir) {
		return createProcessInternal(processCommand,arguments,null,workingDir);
	}

	public IInteger createProcess(IString processCommand, IMap envVars) {
		return createProcessInternal(processCommand,null,envVars,null);
	}

	public IInteger createProcess(IString processCommand, IMap envVars, ISourceLocation workingDir) {
		return createProcessInternal(processCommand,null,envVars,workingDir);
	}

	public IInteger createProcess(IString processCommand, IList arguments, IMap envVars) {
		return createProcessInternal(processCommand,arguments,envVars,null);
	}

	public IInteger createProcess(IString processCommand, IList arguments, IMap envVars, ISourceLocation workingDir) {
		return createProcessInternal(processCommand,arguments,envVars,workingDir);
	}

	private synchronized IInteger createProcessInternal(IString processCommand, IList arguments, IMap envVars, ISourceLocation workingDir) {
		try {
			// Build the arg array using the command and the command arguments passed in the arguments list
			String[] args = null;
			if (arguments != null && arguments.length() > 0) {
				args = new String[arguments.length()+1];
				args[0] = processCommand.getValue();
				for (int n = 0; n < arguments.length(); ++n) {
					if (arguments.get(n) instanceof IString) 
						args[n+1] = ((IString)arguments.get(n)).getValue();
					else
						throw RuntimeExceptionFactory.illegalArgument(arguments.get(n),null, "");
				}
			} else {
				args = new String[1];
				args[0] = processCommand.getValue();
			}
			
			// Built the environment var map using the envVars map
			String[] vars = null;
			if (envVars != null && envVars.size() > 0) { 
				vars = new String[envVars.size()];
				int keyCount = 0;
				for (IValue varKey : envVars) {
					if (varKey instanceof IString) {
						IString strKey = (IString) varKey;
						IValue varVal = envVars.get(varKey);
						if (varVal instanceof IString) {
							IString strVal = (IString) varVal;
							vars[keyCount] = strKey + " = " + strVal;
						} else {
							throw RuntimeExceptionFactory.illegalArgument(varVal,null, "");
						}
					} else {
						throw RuntimeExceptionFactory.illegalArgument(varKey,null, "");
					}
					keyCount++;
				}
			}
			
			File cwd = null;
			if (workingDir != null) {
				cwd = new File(workingDir.getURI().getPath());
			}
			
			Process newProcess = Runtime.getRuntime().exec(args, vars, cwd);
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
	
	public synchronized IString readFrom(IInteger processId) {
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
