/*
 * @(#)ContentProducerRegistry.java
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
import java.io.Serializable;
import java.net.URL;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import org.jhotdraw.util.Storable;
import org.jhotdraw.util.StorableInput;
import org.jhotdraw.util.StorableOutput;

/**
 * ContentProducerRegistry acts as a repository for ContentProducers. It allows
 * for registries to be organized in a hierarchy, so that a specific producer
 * request will travel upwards in the hierarchy until eventually a suitable
 * producer is found.<br>
 * Producers are registered associated with the class of the object they are suitable
 * to produce contents for, but ContentProducerRegistry allows for class hierarchy
 * searches. What this means is that for two classes A and B, B being a subclass
 * of A, if a producer is registered for class B it will be used, otherwise the
 * producer registered for class A will be used instead. ContentProducerRegistry will
 * always select the super class closest in the class hierarchy, so if several
 * producers are registered for classes in a derivation hierarchy, the producer
 * registered for the class closest to the requested class will be selected.
 *
 * @author  Eduardo Francos - InContext
 * @created 7 mai 2002
 * @version <$CURRENT_VERSION$>
 */
public class ContentProducerRegistry implements Serializable, Storable {

	/** producers registered with this registry */
	private Hashtable fContentProducers = new Hashtable();

	/** parent registry for hierarchical searches */
	private transient ContentProducerRegistry fParent;

	/** Application global producers */
	private static ContentProducerRegistry fDefaultRegistry =
			new ContentProducerRegistry(null);

	// initialize the application wide default content producers
	static {
		fDefaultRegistry.registerContentProducer(URL.class, new URLContentProducer());
	}

	/**Constructor for the ContentProducerRegistry object */
	public ContentProducerRegistry() {
		setParent(fDefaultRegistry);
	}

	/**
	 *Constructor for the ContentProducerRegistry object
	 *
	 * @param parent  the parent for this producer
	 */
	public ContentProducerRegistry(ContentProducerRegistry parent) {
		setParent(parent);
	}

	/**
	 * Sets the autonomous attribute of the ContentProducerRegistry object.
	 * Similar to setting the parent to null<br>
	 * An autonomous registry does not have a parent hierarchy so resolution of
	 * search requests stop do not propagate.
	 *
	 * @see   #setParent(ContentProducerRegistry)
	 */
	public void setAutonomous() {
		setParent(null);
	}

	/**
	 * Gets the autonomous status of the ContentProducerRegistry object
	 *
	 * @return   The autonomous value
	 */
	public boolean isAutonomous() {
		return (getParent() == null);
	}

	/**
	 * Sets the parent attribute of the ContentProducerRegistry object
	 *
	 * @param newParent  The new parent value
	 * @see              #setAutonomous()
	 */
	public void setParent(ContentProducerRegistry newParent) {
		fParent = newParent;
	}

	/**
	 * Gets the parent attribute of the ContentProducerRegistry object
	 *
	 * @return   The parent value
	 */
	public ContentProducerRegistry getParent() {
		return fParent;
	}

	/**
	 * Registers an application global producer
	 *
	 * @param producer     the registered producer
	 * @param targetClass  The class associated with the producer
	 * @return             the previous producer registered for the target class
	 */
	public static ContentProducer registerDefaultContentProducer(Class targetClass, ContentProducer producer) {
		return fDefaultRegistry.registerContentProducer(targetClass, producer);
	}

	/**
	 * Unregisters ie: removes a registered producer for a target class.<br>
	 * A check is made to ensure the removed producer is the current producer
	 * for the target class. If not, the request is ignored.
	 *
	 * @param producer     the producer to unregister
	 * @param targetClass  the target class
	 */
	public static void unregisterDefaultContentProducer(Class targetClass, ContentProducer producer) {
		fDefaultRegistry.unregisterContentProducer(targetClass, producer);
	}

	/**
	 * Gets the ContentProducer attribute of the HTMLTextAreaFigure object
	 *
	 * @param targetClass  the target class
	 * @return             the previous producer registered for the target class
	 */
	public static ContentProducer getDefaultContentProducer(Class targetClass) {
		return fDefaultRegistry.getContentProducer(targetClass);
	}

	/**
	 * Gets the exact application global Producer for the target class, ie:
	 * no class hierarchy search
	 *
	 * @param targetClass  the target class
	 * @return             The producer
	 */
	public static ContentProducer getExactDefaultContentProducer(Class targetClass) {
		return fDefaultRegistry.getExactContentProducer(targetClass);
	}

	/**
	 * Registers a producer
	 *
	 * @param producer     the producer to register
	 * @param targetClass  the target class
	 * @return             the previous producer registered for the target class
	 */
	public ContentProducer registerContentProducer(Class targetClass, ContentProducer producer) {
		ContentProducer previousProducer = getContentProducer(targetClass);
		fContentProducers.put(targetClass, producer);
		return previousProducer;
	}

	/**
	 * Unregisters a producer
	 *
	 * @param producer     the producer to unregister
	 * @param targetClass  the target class
	 */
	public void unregisterContentProducer(Class targetClass, ContentProducer producer) {
		// first check that the producer being removed is the current producer
		// for the target class
		ContentProducer currentProducer = getContentProducer(targetClass);
		if (currentProducer == producer) {
			fContentProducers.remove(targetClass);
		}
	}

	/**
	 * Finds the most appropriate producer for the target class. Will search
	 * first for exact producers, ie: no class hierarchy search, then if none found,
	 * will do a class compatible search
	 *
	 * @param targetClass  The target class
	 * @return             The producer
	 */
	public ContentProducer getContentProducer(Class targetClass) {
		// first try an exact producer
		ContentProducer producer = getExactContentProducer(targetClass);
		if (producer != null) {
			return producer;
		}

		// none defined, try finding one for the nearest super class
		// of the target class. Note this shouldn't return null because there is
		// a default producer defined for the Object class
		return getSuperClassContentProducer(targetClass, null);
	}

	/**
	 * Finds the exact producer for the target class, ie: no class hierarchy search
	 *
	 * @param targetClass  The target class
	 * @return             The producer
	 */
	public ContentProducer getExactContentProducer(Class targetClass) {
		// first try our own
		ContentProducer producer = (ContentProducer)fContentProducers.get(targetClass);
		if (producer != null) {
			return producer;
		}

		// none defined, try our parent's
		// unless we are autonomous (ie: we have no parent)
		if (!this.isAutonomous()) {
			return getParent().getExactContentProducer(targetClass);
		}

		// none defined
		return null;
	}

	/**
	 * Gets the producers for the closest super class of the target class
	 *
	 * @param targetClass  The target class
	 * @return             The producer
	 */
	protected ContentProducer getSuperClassContentProducer(Class targetClass, Class closestClass) {
		Map.Entry entry = null;
		Class entryClass = null;
		ContentProducer closestProducer = null;

		Iterator iter = fContentProducers.entrySet().iterator();
		while (iter.hasNext()) {
			entry = (Map.Entry)iter.next();
			entryClass = (Class)entry.getKey();
			if (entryClass.isAssignableFrom(targetClass)) {
				if (closestClass != null && closestClass.isAssignableFrom(entryClass)) {
					closestClass = entryClass;
					closestProducer = (ContentProducer)entry.getValue();
				}
			}
		}

		// whether we found one or not ask our parent to see if a more
		// exact producer is defined
		// unless we are autonomous (ie: we have no parent)
		if (!this.isAutonomous()) {
			ContentProducer parentProducer =
					getParent().getSuperClassContentProducer(targetClass, closestClass);
			if (parentProducer != null) {
				closestProducer = parentProducer;
			}
		}

		return closestProducer;
	}

	/**
	 * Storable write support
	 *
	 * @param dw  the storable output
	 */
	public void write(StorableOutput dw) {
		dw.writeInt(fContentProducers.size());
		Map.Entry producerEntry;
		Iterator iter = fContentProducers.entrySet().iterator();
		while (iter.hasNext()) {
			producerEntry = (Map.Entry)iter.next();
			dw.writeString(((Class)producerEntry.getKey()).getName());
			dw.writeStorable((Storable)producerEntry.getKey());
		}
	}

	/**
	 * Storable inoput support
	 *
	 * @param dr               storable input
	 * @exception IOException  thrown by called methods
	 */
	public void read(StorableInput dr) throws IOException {
		// read the default content producers, count first
		int prodCount = dr.readInt();
		String prodClass;
		ContentProducer producer;
		for (int cnt = 0; cnt < prodCount; cnt++) {
			prodClass = dr.readString();
			producer = (ContentProducer)dr.readStorable();
			try {
				registerContentProducer(Class.forName(prodClass), producer);
			}
			catch (ClassNotFoundException ex) {
				// the class does not exist in this application
				// cannot do much about it so ignore it, the entities of
				// this class will get their toString() value instead
			}
		}

	}
}
