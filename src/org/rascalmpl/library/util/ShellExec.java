/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
*******************************************************************************/
package org.rascalmpl.library.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.rascalmpl.exceptions.RuntimeExceptionFactory;
import org.rascalmpl.uri.URIResolverRegistry;

import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IMap;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.ITuple;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;

public class ShellExec {
	private static Map<IInteger, Process> runningProcesses = new HashMap<>();
	private static Map<IInteger, InputStreamReader> processInputStreams = new HashMap<>();
	private static Map<IInteger, BufferedReader> processErrorStreams = new HashMap<>();
	private static Map<IInteger, OutputStreamWriter> processOutputStreams = new HashMap<>();
	private static IInteger processCounter = null;
	
	private final IValueFactory vf;

	public ShellExec(IValueFactory vf) {
		this.vf = vf;
	}

	public IInteger createProcess(IString processCommand, ISourceLocation workingDir, IList arguments, IMap envVars) {
		return createProcessInternal(processCommand,arguments,envVars,workingDir);
	}

	private IString toString(IValue o) {
		try {
			if (o.getType().isSourceLocation()) {
				ISourceLocation p = URIResolverRegistry.getInstance().logicalToPhysical((ISourceLocation) o);

				if (!"file".equals(p.getScheme())) {
					throw RuntimeExceptionFactory.illegalArgument(o,
						"only file:/// URI are supported or logical schemes that derived from file");
				}

				return vf.string(new File(p.getURI()).getAbsolutePath());
			}
			else {
				return vf.string(o.toString());
			}
		}
		catch (IOException e) {
			throw RuntimeExceptionFactory.io(e.getMessage());
		}
	}

	public IInteger createProcess(ISourceLocation processCommand, ISourceLocation workingDir, IList arguments, IMap envVars) {
		try {
			processCommand = URIResolverRegistry.getInstance().logicalToPhysical(processCommand);

			arguments = arguments.stream().map(v -> toString(v)).collect(vf.listWriter());
			envVars = envVars.stream().map(t -> vf.tuple(((ITuple) t).get(0), toString(((ITuple) t).get(1)))).collect(vf.mapWriter());

			if ("file".equals(processCommand.getScheme())) {
				return createProcessInternal(toString(processCommand), arguments, envVars, workingDir);	
			}
			else {
				throw RuntimeExceptionFactory.illegalArgument(processCommand, "createProcess only supports the file:/// scheme for command arguments, or logical schemes derived from it.");
			}
		}
		catch (IOException e) {
			throw RuntimeExceptionFactory.io(e.getMessage());
		}
	}
	
	public IBool isAlive(IInteger pid) {
	    Process p = runningProcesses.get(pid);
        return p != null ? vf.bool(p.isAlive()) : vf.bool(false);
	}

	public IBool isZombie(IInteger pid) {
        Process p = runningProcesses.get(pid);
        return vf.bool(p != null && !p.isAlive());
    }

	public IInteger exitCode(IInteger pid) {
		Process p = runningProcesses.get(pid);

		if (p == null) {
			throw RuntimeExceptionFactory.illegalArgument(pid, "unknown process");
		}

		while (true) {
			try {
				return vf.integer(p.waitFor());
			}
			catch (InterruptedException e) {
				continue;
			}
		}
	}
	
	private synchronized IInteger createProcessInternal(IString processCommand, IList arguments, IMap envVars, ISourceLocation workingDir) {
		try {
			// Build the arg array using the command and the command arguments passed in the arguments list
			List<String> args = new ArrayList<String>();
			args.add(processCommand.getValue());
			if(arguments != null) {
			    for (int n = 0; n < arguments.length(); ++n) {
				    if (arguments.get(n) instanceof IString) 
					    args.add(((IString)arguments.get(n)).getValue());
				    else
					    throw RuntimeExceptionFactory.illegalArgument(arguments.get(n),null, null);
			    }
			}
			ProcessBuilder pb = new ProcessBuilder(args);
			
			// Built the environment var map using the envVars map
			Map<String,String> vars = new HashMap<String,String>();
			if (envVars != null && envVars.size() > 0) { 
				for (IValue varKey : envVars) {
					if (varKey instanceof IString) {
						IString strKey = (IString) varKey;
						IValue varVal = envVars.get(varKey);
						if (varVal instanceof IString) {
							IString strVal = (IString) varVal;
							vars.put(strKey.getValue(),  strVal.getValue());
						} else {
							throw RuntimeExceptionFactory.illegalArgument(varVal,null, null);
						}
					} else {
						throw RuntimeExceptionFactory.illegalArgument(varKey,null, null);
					}
				}
			}
			Map<String,String> currentEnv = pb.environment();
			try {
				for (String strKey : vars.keySet()) {
					currentEnv.put(strKey, vars.get(strKey));
				}
			} catch (UnsupportedOperationException uoe) {
				throw RuntimeExceptionFactory.permissionDenied(vf.string("Modifying environment variables is not allowed on this machine."), null, null);
			} catch (IllegalArgumentException iae) {
				throw RuntimeExceptionFactory.permissionDenied(vf.string("Modifying environment variables is not allowed on this machine."), null, null);
			}
			
			File cwd = null;
			if (workingDir != null && workingDir.getScheme().equals("file")) {
				cwd = new File(workingDir.getPath());
				pb.directory(cwd);
			}
			
			Process newProcess = pb.start();

			if (processCounter == null) {
				processCounter = vf.integer(0);
			}
			processCounter = processCounter.add(vf.integer(1));
			runningProcesses.put(processCounter, newProcess);
			return processCounter;
		} catch (IOException e) {
			throw RuntimeExceptionFactory.io(e.getMessage());
		}
	}

	public synchronized void killProcess(IInteger processId, IBool force) {
		if (!runningProcesses.containsKey(processId))
			throw RuntimeExceptionFactory.illegalArgument(processId, null, null);

		if (processInputStreams.containsKey(processId)) {
			try {
				processInputStreams.get(processId).close();
			} catch (IOException e) {
				// eat it, we are just throwing it away anyway
			} finally {
				processInputStreams.remove(processId);
			}
		}
		
		if (processErrorStreams.containsKey(processId)) {
			try {
				processErrorStreams.get(processId).close();
			} catch (IOException e) {
				// eat it, we are just throwing it away anyway
			} finally {
				processErrorStreams.remove(processId);
			}
		}

		if (processOutputStreams.containsKey(processId)) {
			try {
				processOutputStreams.get(processId).close();
			} catch (IOException e) {
				// eat it, we are just throwing it away anyway
			} finally {
				processOutputStreams.remove(processId);
			}
		}

		Process runningProcess = runningProcesses.get(processId);
		
		if (runningProcess.isAlive()) {
		    if (force.getValue()) {
		        runningProcess.destroyForcibly();
		    }
		    else {
		        runningProcess.destroy();    
		    }
		}
		
		new Thread("zombie process clean up") {
		    public void run() {
				while (true) {
					try {
						runningProcess.waitFor();
						runningProcesses.remove(processId);
						return;
					}
					catch (InterruptedException e) {
						// may happen occasionally 
						// and we should go back to waiting for
						// the process to end
					}
				}
		    };
		}.start();
		 
		
		return;
	}
	
	public synchronized IString readFrom(IInteger processId) {
		if (!runningProcesses.containsKey(processId))
			throw RuntimeExceptionFactory.illegalArgument(processId, null, null);
		try {
			Process runningProcess = runningProcesses.get(processId);
			InputStreamReader isr = null;
			if (processInputStreams.containsKey(processId)) {
				isr = processInputStreams.get(processId);
			} else {
				isr = new InputStreamReader(runningProcess.getInputStream());
				processInputStreams.put(processId, isr);
			}
			 
			StringBuffer line = new StringBuffer();
			while (isr.ready()) {
				line.append((char)isr.read());
			}
			return vf.string(line.toString());
		} catch (IOException e) {
			throw RuntimeExceptionFactory.javaException(e, null, null);
		}
	}
	
	public synchronized IString readWithWait(IInteger processId, IInteger wait) {
      if (!runningProcesses.containsKey(processId))
        throw RuntimeExceptionFactory.illegalArgument(processId, null, null);

      try {
          Process runningProcess = runningProcesses.get(processId);
          InputStreamReader isr = null;
          if (processInputStreams.containsKey(processId)) {
              isr = processInputStreams.get(processId);
          } else {
              isr = new InputStreamReader(runningProcess.getInputStream());
              processInputStreams.put(processId, isr);
          }
           
          StringBuffer line = new StringBuffer();
          int nrOfWaits = 0;
          while (nrOfWaits < 2) {
            if (isr.ready()) {
              nrOfWaits = 0;
              line.append((char)isr.read());
            } else {
              Thread.sleep(wait.intValue());
              nrOfWaits++;
            }
          }
          return vf.string(line.toString());
      } catch (IOException | InterruptedException e) {
          throw RuntimeExceptionFactory.javaException(e, null, null);
      }
    }
	
	public synchronized IString readFromErr(IInteger processId) {
		if (!runningProcesses.containsKey(processId))
			throw RuntimeExceptionFactory.illegalArgument(processId, null, null);
		try {
			Process runningProcess = runningProcesses.get(processId);
			BufferedReader isr = null;
			if (processErrorStreams.containsKey(processId)) {
				isr = processErrorStreams.get(processId);
			} else {
				isr = new BufferedReader(new InputStreamReader(runningProcess.getErrorStream()));
				processErrorStreams.put(processId, isr);
			}
			StringBuffer line = new StringBuffer();
			while (isr.ready()) {
				line.append((char)isr.read());
			}
			return vf.string(line.toString());
		} catch (IOException e) {
			throw RuntimeExceptionFactory.javaException(e, null, null);
		}
	}
	
	public synchronized IString readLineFromErr(IInteger processId, IInteger wait, IInteger maxTries) {
        if (!runningProcesses.containsKey(processId))
            throw RuntimeExceptionFactory.illegalArgument(processId, null, null);
        try {
            Process runningProcess = runningProcesses.get(processId);
            BufferedReader isr = null;
            if (processErrorStreams.containsKey(processId)) {
                isr = processErrorStreams.get(processId);
            } else {
                isr = new BufferedReader(new InputStreamReader(runningProcess.getErrorStream()));
                processErrorStreams.put(processId, isr);
            }

            // wait until there is input first
            for (long max = maxTries.longValue(); !isr.ready() && max > 0; max--) {
                try {
                    Thread.sleep(wait.longValue());
                }
                catch (InterruptedException e) {
                    // it happens
                }
            }
            
            // block until we have an entire line
            return vf.string(isr.readLine());
        } catch (IOException e) {
            throw RuntimeExceptionFactory.javaException(e, null, null);
        }
    }
	
	public synchronized IString readEntireStream(IInteger processId) {
		if (!runningProcesses.containsKey(processId)) {
			throw RuntimeExceptionFactory.illegalArgument(processId, null, null);
		}
		
		try (BufferedReader br 
				= new BufferedReader(
						new InputStreamReader(
								runningProcesses.get(processId).getInputStream()))) {
			StringBuffer lines = new StringBuffer();
			String line = "";
			while (null != (line = br.readLine())) {
				lines.append(line);
				lines.append('\n');
			}
			
			return vf.string(lines.toString());
		} catch (IOException e) {
			throw RuntimeExceptionFactory.javaException(e, null, null);
		}
	}

	public synchronized IString readEntireErrStream(IInteger processId) {
		if (!runningProcesses.containsKey(processId)) {
			throw RuntimeExceptionFactory.illegalArgument(processId, null, null);
		}
		
		try (BufferedReader br 
				= new BufferedReader(
						new InputStreamReader(
								runningProcesses.get(processId).getErrorStream()))) {
			StringBuffer lines = new StringBuffer();
			String line = "";
			while (null != (line = br.readLine())) {
				lines.append(line);
				lines.append('\n');
			}
			
			return vf.string(lines.toString());
		} catch (IOException e) {
			throw RuntimeExceptionFactory.javaException(e, null, null);
		}
	}

	public void writeTo(IInteger processId, IString msg) {
		if (!runningProcesses.containsKey(processId))
			throw RuntimeExceptionFactory.illegalArgument(processId, null, null);
		try {
			Process runningProcess = runningProcesses.get(processId);
			OutputStreamWriter osw = null;
			if (processOutputStreams.containsKey(processId)) {
				osw = processOutputStreams.get(processId);
			} else {
				osw = new OutputStreamWriter(runningProcess.getOutputStream());
				processOutputStreams.put(processId, osw);
			}
			osw.append(msg.getValue());
			osw.flush();
		} catch (IOException e) {
			throw RuntimeExceptionFactory.javaException(e, null, null);
		}
	}

}
