package org.rascalmpl.library.experiments.scm;


public interface ScmLogEntryHandler<E> {
	public void handleLogEntry(E entry);
}
