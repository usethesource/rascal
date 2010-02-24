/*
 * @(#)URLContentProducer.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.jhotdraw.contrib.html;

import java.io.IOException;

import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import org.jhotdraw.util.StorableInput;
import org.jhotdraw.util.StorableOutput;

/**
 * URLContentProducer produces string contents from URLs.<br>
 * Anything the <code>URL.openStream()</code> method can get can be retrieved
 * by this producer, that includes resources, local files, web documents,
 * web queries, FTP files, and you name it.<br>
 * It can either be specific if set for a specific URL, or generic, retrieving
 * any URL passed to the getContents method.
 *
 * @author  Eduardo Francos - InContext
 * @created 4 mai 2002
 * @version <$CURRENT_VERSION$>
 * @todo    should we cache the contents for specific URLs? this can
 * accelerate things a lot for static documents, but for dynamic ones it
 * will complicate things. If cached then if must be in a DisposableResourceHolder
 */
public class URLContentProducer extends FigureDataContentProducer
		 implements Serializable {

	/** the specific URL */
	private URL fURL;

	/**
	 * Constructor for the URLContentProducer object
	 */
	public URLContentProducer() { }

	/**
	 *Constructor for the URLContentProducer object
	 *
	 * @param url  the specific URL
	 */
	public URLContentProducer(URL url) {
		setURL(url);
	}

	/**
	 * Retrieves the contents of the URL pointed object
	 *
	 * @param context       the calling client context
	 * @param ctxAttrName   the attribute name that led to this being called
	 * @param ctxAttrValue  the value of the URL attribute
	 * @return              the contents of the URL pointed object as a string
	 */
	public Object getContent(ContentProducerContext context, String ctxAttrName, Object ctxAttrValue) {
		try {
			// if we have our own URL then use it
			// otherwise use the one supplied
			URL url = (getURL() != null) ? new URL(getURL().toExternalForm()) : new URL(((URL)ctxAttrValue).toExternalForm());

			InputStream reader = url.openStream();
			int available = reader.available();
			byte contents[] = new byte[available];
			reader.read(contents, 0, available);
			reader.close();
			return new String(contents);
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return ex.toString();
		}
	}

	/**
	 * Writes the storable
	 *
	 * @param dw  the storable output
	 */
	public void write(StorableOutput dw) {
		super.write(dw);
		dw.writeBoolean((getURL() != null));
		if (getURL() != null) {
			dw.writeString(getURL().toExternalForm());
		}
	}

	/**
	 * Writes the storable
	 *
	 * @param dr               the storable input
	 * @exception IOException  thrown by called methods
	 */
	public void read(StorableInput dr) throws IOException {
		super.read(dr);
		boolean hasURL = dr.readBoolean();
		if (hasURL) {
			setURL(new URL(dr.readString()));
		}
	}

	public URL getURL() {
		return fURL;
	}

	protected void setURL(URL newURL) {
		fURL = newURL;
	}
}
