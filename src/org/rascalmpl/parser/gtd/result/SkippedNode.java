/*******************************************************************************
 * Copyright (c) 2009-2024 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
 *   * Pieter Olivier - Pieter.Olivier@swat.engineering
*******************************************************************************/
package org.rascalmpl.parser.gtd.result;

import java.net.URI;

import org.rascalmpl.unicode.UnicodeConverter;

/**
 * Result tree node that represents a skipped portion of the input sentence.
 */
public class SkippedNode extends AbstractNode {
	public static final int ID = 9;

	private final URI inputUri;
	private final int[] skippedChars;
	private final int offset;

	public SkippedNode(URI inputUri, int[] skippedChars, int offset) {
		super();

		this.inputUri = inputUri;
		this.skippedChars = skippedChars;
		this.offset = offset;
	}

	public int getTypeIdentifier(){
		return ID;
	}

	public URI getInputUri() {
		return inputUri;
	}

	public int[] getSkippedChars(){
		return skippedChars;
	}

	/**
	 * Returns the offset in the input string this node starts at.
	 */
	public int getOffset(){
		return offset;
	}

	public boolean isEmpty() {
		return (skippedChars.length == 0);
	}

	public boolean isNonterminalSeparator() {
		return false;
	}

	public int getLength() {
		return skippedChars.length;
	}

	@Override
	public String toString() {
		return "SkippedNode[skippedChars=" + UnicodeConverter.unicodeArrayToString(skippedChars) + ",offset=" + offset + "]";
	}
}
