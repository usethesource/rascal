/*
 * @(#)SerializationStorageFormat.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.util;

import org.jhotdraw.framework.*;
import org.jhotdraw.standard.StandardDrawing;

import java.io.*;
import java.util.*;
import java.util.List;
import java.awt.*;

import javax.jdo.*;
import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;


/**
 * @author Wolfram Kaiser <mrfloppy@users.sourceforge.net>
 * @version <$CURRENT_VERSION$>
 */
public class JDOStorageFormat extends StandardStorageFormat {

	private Map pms;

	/**
	 * Create a SerialzationStorageFormat for storing and restoring Drawings.
	 */
	public JDOStorageFormat() {
		super();
		pms = CollectionsFactory.current().createMap();
		// close database connection when application exits
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				Iterator iter = pms.values().iterator();
//				while (iter.hasNext()) {
//					((PersistenceManager)iter.next()).close();
//				}
			}
		});
	}

	/**
	 * Factory method to create the file extension recognized by the FileFilter for this
	 * SerializationStorageFormat. The SerializationStorageFormat has the file extension "ser"
	 * (e.g. my_picasso.ser).
	 *
	 * @return new file extension
	 */
	protected String createFileExtension() {
		return "j2";
	}

	/**
	 * Factory method to create a file description for the file type when displaying the
	 * associated FileFilter.
	 *
	 * @return new file description
	 */
	public String createFileDescription() {
		return "Database (" + getFileExtension() + ")";
	}

	/**
	 * @see org.jhotdraw.util.StorageFormat#isRestoreFormat()
	 */
	public boolean isRestoreFormat() {
		return true;
	}

	/**
	 * @see org.jhotdraw.util.StorageFormat#isStoreFormat()
	 */
	public boolean isStoreFormat() {
		return true;
	}

	/**
	 * Store a Drawing under a given name. The name should be valid with regard to the FileFilter
	 * that means, it should already contain the appropriate file extension.
	 *
	 * @param fileName file name of the Drawing under which it should be stored
	 * @param storeDrawing drawing to be saved
	 */
	public String store(String fileName, Drawing storeDrawing) throws IOException {
		PersistenceManager pm = getPersistenceManager(fileName);
		String drawingName = null;

		Drawing txnDrawing = crossTxnBoundaries(storeDrawing);
		endTransaction(pm, false);

		startTransaction(pm);
		try {
			Extent extent = pm.getExtent(StandardDrawing.class, true);
			DrawingListModel listModel = new DrawingListModel(extent.iterator());
			drawingName = showStoreDialog(listModel, storeDrawing);
			if (drawingName != null) {
				storeDrawing.setTitle(drawingName);
				txnDrawing.setTitle(drawingName);
				pm.makePersistent(txnDrawing);
			}
		}
		finally {
			endTransaction(pm, (drawingName != null));
		}

		// there must be always a transaction running
		startTransaction(pm);
		return drawingName;
	}

	/**
	 * Restore a Drawing from a file with a given name. The name must be should with regard to the
	 * FileFilter that means, it should have the appropriate file extension.
	 *
	 * @param fileName of the file in which the Drawing has been saved
	 * @return restored Drawing
	 */
	public synchronized Drawing restore(String fileName) throws IOException {
		PersistenceManager pm = getPersistenceManager(fileName);

		endTransaction(pm, false);
		startTransaction(pm);
		Drawing restoredDrawing  = null;

		try {
			Extent extent = pm.getExtent(StandardDrawing.class, true);
			DrawingListModel listModel = new DrawingListModel(extent.iterator());
			Drawing txnDrawing = showRestoreDialog(listModel);
			if (txnDrawing != null) {
//				pm.retrieve(txnDrawing);
//				retrieveAll(pm, (StandardDrawing)txnDrawing);
//				restoredDrawing = crossTxnBoundaries(txnDrawing);
				restoredDrawing = txnDrawing;
			}
		}
		finally {
			endTransaction(pm, false);
		}

		// there must be always a transaction running
		startTransaction(pm);
		return restoredDrawing;
	}

	private void retrieveAll(PersistenceManager pm, Figure figure) {
		pm.retrieve(figure);
		FigureEnumeration fe = figure.figures();
		while (fe.hasNextFigure()) {
			retrieveAll(pm, fe.nextFigure());
		}
	}

	private Drawing crossTxnBoundaries(Drawing originalDrawing) {
		return (Drawing)((StandardDrawing)originalDrawing).clone();
//		return originalDrawing;
	}

	private synchronized PersistenceManager getPersistenceManager(String fileName) {
		PersistenceManager pm = (PersistenceManager)pms.get(fileName);
		if (pm == null) {
			pm = createPersistenceManagerFactory(fileName).getPersistenceManager();
			pms.put(fileName, pm);
		}
		return pm;
	}

	private PersistenceManagerFactory createPersistenceManagerFactory(String dbFileName) {
		Properties pmfProps = new Properties();

        pmfProps.put(
            "javax.jdo.PersistenceManagerFactoryClass",
            "com.poet.jdo.PersistenceManagerFactories" );
        pmfProps.put(
            "javax.jdo.option.ConnectionURL",
            "fastobjects://LOCAL/MyBase.j1" );
        final PersistenceManagerFactory pmf =
            JDOHelper.getPersistenceManagerFactory( pmfProps );

		return pmf;
	}

	private static void startTransaction(PersistenceManager pm) {
		if (!pm.currentTransaction().isActive()) {
			pm.currentTransaction().begin();
		}
	}

	private static void endTransaction(PersistenceManager pm, boolean doCommit) {
		if (pm.currentTransaction().isActive()) {
			if (doCommit) {
				pm.currentTransaction().commit();
			}
			else {
				pm.currentTransaction().rollback();
			}
		}
	}

	private String showStoreDialog(ListModel listModel, Drawing storeDrawing) {
		final String msgString = "Specify a name for the drawing";

		final JTextField nameTextField = new JTextField(storeDrawing.getTitle());
		final JList dataList = new JList(listModel);
		final JScrollPane dbContentScrollPane = new JScrollPane(dataList);
		Object[] guiComponents = {msgString, dbContentScrollPane, nameTextField};

		dataList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		dataList.setValueIsAdjusting(true);
		dataList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
				nameTextField.setText(dataList.getSelectedValue().toString());
			}
		});

		final JOptionPane optionPane = new JOptionPane(
			guiComponents,
			JOptionPane.PLAIN_MESSAGE,
			JOptionPane.OK_CANCEL_OPTION);

		final JDialog dialog = optionPane.createDialog(null, "Restore a drawing from the database");
//		dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
//		dialog.addWindowListener(new WindowAdapter() {
//			public void windowClosing(WindowEvent we) {
//				System.exit(0);
//			}
//		});
		dialog.setVisible(true);
		if ((optionPane.getValue() != null) && (optionPane.getValue().equals(new Integer(JOptionPane.OK_OPTION)))) {
			return nameTextField.getText();
		}
		else {
			return null;
		}
	}

	private Drawing showRestoreDialog(DrawingListModel listModel) {
		final String msgString = "Select a drawing";

		final JList dataList = new JList(listModel);
		final JScrollPane dbContentScrollPane = new JScrollPane(dataList);
		Object[] guiComponents = {msgString, dbContentScrollPane};

		dataList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		dataList.setValueIsAdjusting(true);

		final JOptionPane optionPane = new JOptionPane(
			guiComponents,
			JOptionPane.PLAIN_MESSAGE,
			JOptionPane.OK_CANCEL_OPTION);

		final JDialog dialog = optionPane.createDialog(null, "Restore a drawing from the database");
		dialog.setVisible(true);
		if ((optionPane.getValue() != null)
				&& (optionPane.getValue().equals(new Integer(JOptionPane.OK_OPTION)))
				&& (dataList.getSelectedIndex() >= 0)
				&& (dataList.getSelectedIndex() < dataList.getModel().getSize())) {
			return listModel.getDrawingAt(dataList.getSelectedIndex());
		}
		else {
			return null;
		}
	}

	static class DrawingListModel extends AbstractListModel {
		private List myList;

		DrawingListModel(Iterator iter) {
			myList = CollectionsFactory.current().createList();
			while (iter.hasNext()) {
				Object o = iter.next();
				System.out.println("extent: " + o + " .. " + ((Drawing)o).getTitle());
				myList.add(o);
			}
		}

		public Object getElementAt(int index) {
			return getDrawingAt(index).getTitle();
		}

		protected Drawing getDrawingAt(int index) {
			return ((Drawing)myList.get(index));
		}

		public int getSize() {
			return myList.size();
		}
	}

	static class DrawingSelector extends JDialog {
		DrawingSelector() {
			init();
		}

		private void init() {
			setTitle("Select Drawing");
			getContentPane().setLayout(new BorderLayout());
			getContentPane().add(new JLabel("Database content"), BorderLayout.NORTH);
			setSize(200, 200);
		}
	}

	public static void main(String[] args) {
		DrawingSelector frame = new DrawingSelector();
		try {
			Drawing newDrawing = new StandardDrawing();
			newDrawing.setTitle("TestDrawingName" + new Random(System.currentTimeMillis()).nextLong());
			new JDOStorageFormat().store("base.j2", newDrawing);
			System.exit(0);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
//		frame.setVisible(true);
	}
}
