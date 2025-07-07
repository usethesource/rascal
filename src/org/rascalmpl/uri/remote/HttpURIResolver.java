/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.uri.remote;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpClient.Version;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.Charset;
import java.time.Duration;
import java.time.Instant;
import java.util.Collections;

import org.rascalmpl.uri.FileAttributes;
import org.rascalmpl.uri.ISourceLocationInput;

import io.usethesource.vallang.ISourceLocation;

public class HttpURIResolver implements ISourceLocationInput {

	private final HttpClient httpClient = HttpClient.newBuilder()
		.version(Version.HTTP_2) // upgrade where possible
		.connectTimeout(Duration.ofSeconds(10))
		.followRedirects(Redirect.NORMAL) // follow redirects
		.build();

	@Override
	public InputStream getInputStream(ISourceLocation uri) throws IOException {
		try {
			var response = httpClient.send(
				HttpRequest.newBuilder(uri.getURI())
					.GET()
					.build()
				, BodyHandlers.ofInputStream());
			if (response.statusCode() >= 400) {
				throw new FileNotFoundException(""  + uri + " does not exist, server replied: " + response.statusCode());
			}
			return response.body();
		}
		catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			return InputStream.nullInputStream();
		}
	}

	@Override
	public String scheme() {
		return "http";
	}

	@Override
	public boolean exists(ISourceLocation uri) {
		try {
			sendHeadRequest(uri);
			return true;
		}
		catch (IOException e) {
			return false;
		}
	}

	@Override
	public boolean isDirectory(ISourceLocation uri) {
		return false;
	}

	@Override
	public boolean isFile(ISourceLocation uri) {
		return true;
	}

	@Override
	public long lastModified(ISourceLocation uri) {
		try {
			return extractLastModified(sendHeadRequest(uri));
		}
		catch (IOException e) {
			return 0L;
		}
	}

	@Override
	public long size(ISourceLocation uri) throws IOException {
		try {
			return extractSize(sendHeadRequest(uri));
		}
		catch (IOException e) {
			return 0L;
		}
	}

	private long extractLastModified(HttpHeaders headers) {
		return headers
			.firstValueAsLong("Last-Modified")
			.orElse(Instant.now().toEpochMilli());
	}

	private long extractSize(HttpHeaders headers) {
		return headers
			.firstValueAsLong("Content-Length")
			.orElse(0);
	}

	@Override
	public String[] list(ISourceLocation uri) {
		return new String[] { };
	}

	@Override
	public boolean supportsHost() {
		return true;
	}

	@Override
	public Charset getCharset(ISourceLocation uri) throws IOException {
		try {
			var encoding = sendHeadRequest(uri).firstValue("Content-Encoding").orElse(null);
			if (encoding != null && Charset.isSupported(encoding)) {
				return Charset.forName(encoding);
			}
			return null;
		}
		catch (IOException e) {
			return null;
		}
	}

	@Override
	public FileAttributes stat(ISourceLocation loc) throws IOException {
		var headers = sendHeadRequest(loc);
		return new FileAttributes(true, true
			, extractLastModified(headers)
			, extractLastModified(headers)
			, false
			, extractSize(headers)
		);
	}

	private HttpHeaders sendHeadRequest(ISourceLocation loc) throws IOException {
		try {
			var req = HttpRequest.newBuilder(loc.getURI())
				.method("HEAD", BodyPublishers.noBody())
				.header("Accept-Encoding", "identity") // disable compression of the body, to prevent incorrect sizes
				.build();
			var resp = httpClient.send(req, BodyHandlers.discarding());
			if (resp.statusCode() != 200) {
				throw new FileNotFoundException(""  + loc + " does not exist, server replied: " + resp.statusCode());
			}
			return resp.headers();
		}
		catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			return HttpHeaders.of(Collections.emptyMap(), (a,b) -> false);
		}
	}
}
