package org.rascalmpl.library.experiments.scm;

import java.util.Date;

import org.rascalmpl.interpreter.result.RascalFunction;

public abstract class AbstractScmConfiguration<E extends ScmLogEntryHandler<?>> {

	private final String repository;
	private final String workspacePath;
	private final String username;
	private final String password;
	private final Date start;
	private final Date end;
	
	protected AbstractScmConfiguration(String repositoryUrl, String workspacePath, 
		String username, String password, Date start, Date end) {
		
		this.repository = repositoryUrl;
		this.workspacePath = workspacePath;
		this.username = username;
		this.password = password;
		this.start = start;
		this.end = end;
	}
	
	public String getRepositoryUrl() {
		return repository;
	}
	
	public String getWorkspacePath() {
		return workspacePath;
	}

	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}

	public Date getStart() {
		return start;
	}

	public Date getEnd() {
		return end;
	}
	
	public abstract E createEntryHandler(RascalFunction callBack);

	@Override
	public String toString() {
		return "AbstractScmConfiguration [end=" + end + ", password="
				+ password + ", repository=" + repository + ", start=" + start
				+ ", username=" + username + ", workspacePath=" + workspacePath
				+ "]";
	}
}
