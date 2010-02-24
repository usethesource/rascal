/*
 * @(#)QuadTree.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */


package org.jhotdraw.standard;

import org.jhotdraw.framework.FigureEnumeration;
import org.jhotdraw.util.CollectionsFactory;

import java.awt.geom.Rectangle2D;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.io.Serializable;

/**
 * @author WMG (INIT Copyright (C) 2000 All rights reserved)
 * @version <$CURRENT_VERSION$>
 */
class QuadTree implements Serializable {

	//_________________________________________________________VARIABLES

	private Rectangle2D  _absoluteBoundingRectangle2D = new Rectangle2D.Double();
	private int          _nMaxTreeDepth;
	private Hashtable    _theHashtable = new Hashtable();
	private Hashtable    _outsideHashtable = new Hashtable();
	private QuadTree     _nwQuadTree;
	private QuadTree     _neQuadTree;
	private QuadTree     _swQuadTree;
	private QuadTree     _seQuadTree;

	//______________________________________________________CONSTRUCTORS

	public QuadTree(Rectangle2D absoluteBoundingRectangle2D) {
		this(2, absoluteBoundingRectangle2D);
	}

	public QuadTree(int nMaxTreeDepth, Rectangle2D
		absoluteBoundingRectangle2D) {
		_init(nMaxTreeDepth, absoluteBoundingRectangle2D);
	}

	//____________________________________________________PUBLIC METHODS

	public void add(Object anObject, Rectangle2D absoluteBoundingRectangle2D) {
		if (_nMaxTreeDepth == 1) {
			if (absoluteBoundingRectangle2D.intersects(_absoluteBoundingRectangle2D)) {
				_theHashtable.put(anObject, absoluteBoundingRectangle2D);
			}
			else {
				_outsideHashtable.put(anObject, absoluteBoundingRectangle2D);
			}
			return;
		}

		boolean bNW = absoluteBoundingRectangle2D.intersects(
			_nwQuadTree.getAbsoluteBoundingRectangle2D());

		boolean bNE = absoluteBoundingRectangle2D.intersects(
			_neQuadTree.getAbsoluteBoundingRectangle2D());

		boolean bSW = absoluteBoundingRectangle2D.intersects(
			_swQuadTree.getAbsoluteBoundingRectangle2D());

		boolean bSE = absoluteBoundingRectangle2D.intersects(
			_seQuadTree.getAbsoluteBoundingRectangle2D());

		int nCount = 0;

		if (bNW == true) {
			nCount++;
		}
		if (bNE == true) {
			nCount++;
		}
		if (bSW == true) {
			nCount++;
		}
		if (bSE == true) {
			nCount++;
		}

		if (nCount > 1) {
			_theHashtable.put(anObject, absoluteBoundingRectangle2D);
			return;
		}
		if (nCount == 0) {
			_outsideHashtable.put(anObject, absoluteBoundingRectangle2D);
			return;
		}

		if (bNW == true) {
			_nwQuadTree.add(anObject, absoluteBoundingRectangle2D);
		}
		if (bNE == true) {
			_neQuadTree.add(anObject, absoluteBoundingRectangle2D);
		}
		if (bSW == true) {
			_swQuadTree.add(anObject, absoluteBoundingRectangle2D);
		}
		if (bSE == true) {
			_seQuadTree.add(anObject, absoluteBoundingRectangle2D);
		}
	}

	public Object remove(Object anObject) {
		Object returnObject = _theHashtable.remove(anObject);
		if (returnObject != null) {
			return returnObject;
		}

		if (_nMaxTreeDepth > 1) {
			returnObject = _nwQuadTree.remove(anObject);
			if (returnObject != null) {
				return returnObject;
			}

			returnObject = _neQuadTree.remove(anObject);
			if (returnObject != null) {
				return returnObject;
			}

			returnObject = _swQuadTree.remove(anObject);
			if (returnObject != null) {
				return returnObject;
			}

			returnObject = _seQuadTree.remove(anObject);
			if (returnObject != null) {
				return returnObject;
			}
		}

		returnObject = _outsideHashtable.remove(anObject);
		if (returnObject != null) {
			return returnObject;
		}

		return null;
	}


	public void clear() {
		_theHashtable.clear();
		_outsideHashtable.clear();
		if (_nMaxTreeDepth > 1) {
			_nwQuadTree.clear();
			_neQuadTree.clear();
			_swQuadTree.clear();
			_seQuadTree.clear();
		}
	}

	public int getMaxTreeDepth() {
		return _nMaxTreeDepth;
	}
/*
	public FigureEnumeration getAll() {
		List l = CollectionsFactory.current().createList();
		l.add(_theHashtable.keySet());
		l.add(_outsideHashtable.keySet());

		if (_nMaxTreeDepth > 1) {
			l.add(_nwQuadTree.getAll().toList());
			l.add(_neQuadTree.getAll().toList());
			l.add(_swQuadTree.getAll().toList());
			l.add(_seQuadTree.getAll().toList());
		}

		return new FigureEnumerator(l);
	}
*/
	public FigureEnumeration getAllWithin(Rectangle2D r) {
		List l = CollectionsFactory.current().createList();
		for (Iterator ii = _outsideHashtable.keySet().iterator(); ii.hasNext(); ) {
			Object anObject = ii.next();
			Rectangle2D itsAbsoluteBoundingRectangle2D = (Rectangle2D)
			_outsideHashtable.get(anObject);

			if (itsAbsoluteBoundingRectangle2D.intersects(r)) {
				l.add(anObject);
			}
		}

		if (_absoluteBoundingRectangle2D.intersects(r)) {
			for(Iterator i = _theHashtable.keySet().iterator(); i.hasNext(); ) {
				Object anObject = i.next();
				Rectangle2D itsAbsoluteBoundingRectangle2D = (Rectangle2D)
				_theHashtable.get(anObject);

				if (itsAbsoluteBoundingRectangle2D.intersects(r)) {
					l.add(anObject);
				}
			}

			if (_nMaxTreeDepth > 1) {
				l.add(_nwQuadTree.getAllWithin(r));
				l.add(_neQuadTree.getAllWithin(r));
				l.add(_swQuadTree.getAllWithin(r));
				l.add(_seQuadTree.getAllWithin(r));
			}
		}

		return new FigureEnumerator(l);
	}

	public Rectangle2D getAbsoluteBoundingRectangle2D() {
		return _absoluteBoundingRectangle2D;
	}

	//___________________________________________________PRIVATE METHODS

	private void _init(int nMaxTreeDepth, Rectangle2D absoluteBoundingRectangle2D) {
		_absoluteBoundingRectangle2D.setRect(absoluteBoundingRectangle2D);
		_nMaxTreeDepth = nMaxTreeDepth;

		if (_nMaxTreeDepth > 1) {
			_nwQuadTree = new QuadTree(_nMaxTreeDepth-1,
			_makeNorthwest(absoluteBoundingRectangle2D));
			_neQuadTree = new QuadTree(_nMaxTreeDepth-1,
			_makeNortheast(absoluteBoundingRectangle2D));
			_swQuadTree = new QuadTree(_nMaxTreeDepth-1,
			_makeSouthwest(absoluteBoundingRectangle2D));
			_seQuadTree = new QuadTree(_nMaxTreeDepth-1,
			_makeSoutheast(absoluteBoundingRectangle2D));
		}
	}

	private Rectangle2D _makeNorthwest(Rectangle2D r) {
		return new Rectangle2D.Double(r.getX(), r.getY(), r.getWidth() / 2.0, r.getHeight() / 2.0);
	}

	private Rectangle2D _makeNortheast(Rectangle2D r) {
		return new Rectangle2D.Double(r.getX() + r.getWidth() / 2.0,
			r.getY(), r.getWidth() / 2.0, r.getHeight() / 2.0);
	}

	private Rectangle2D _makeSouthwest(Rectangle2D r) {
		return new Rectangle2D.Double(r.getX(), r.getY() + r.getHeight() / 2.0,
			r.getWidth() / 2.0, r.getHeight() / 2.0);
	}

	private Rectangle2D _makeSoutheast(Rectangle2D r) {
		return new Rectangle2D.Double(r.getX() + r.getWidth() / 2.0,
			r.getY() + r.getHeight() / 2.0, r.getWidth() / 2.0,
			r.getHeight() / 2.0);
	}

//_______________________________________________________________END

} //end of class QuadTree
