package org.rascalmpl.library.vis.swt.applet;

import static org.rascalmpl.library.vis.util.vector.Dimension.HOR_VER;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.ScrollBar;
import org.rascalmpl.library.vis.figure.Figure;
import org.rascalmpl.library.vis.figure.combine.Overlap;
import org.rascalmpl.library.vis.graphics.SWTGraphicsContext;
import org.rascalmpl.library.vis.swt.FigureExecutionEnvironment;
import org.rascalmpl.library.vis.util.FigureMath;
import org.rascalmpl.library.vis.util.vector.BoundingBox;
import org.rascalmpl.library.vis.util.vector.Coordinate;
import org.rascalmpl.library.vis.util.vector.Dimension;
import org.rascalmpl.library.vis.util.vector.Rectangle;
import org.rascalmpl.library.vis.util.vector.TransformMatrix;
import org.rascalmpl.library.vis.util.vector.TwoDimensional;

public class ViewPortHandler implements SelectionListener, ControlListener, PaintListener, IFigureChangedListener{
	
	public static final boolean DOUBLE_BUFFERED = true;
	public static final double MIN_SIZE = 50;
	public static BoundingBox scrollableMinSize; 
	public static BoundingBox scrollbarSize; // width of vertical scrollbar, height of horizontal
	private BoundingBox viewPortSize; // the size of the viewport (with the scrollbars, if enabled)
	private BoundingBox parentSize; // the size of the viewport (without the scrollbars)
	private Coordinate viewPortLocation;
	Coordinate zoom ;
	private TwoDimensional<Boolean> scrollBarsVisible;
	private TwoDimensional<ScrollBar> scrollBars;
	private ScrollBar horBar, verBar;
	private Figure figure;
	private FigureSWTApplet parent;
	private List<Overlap> overlapFigures; // this is silently mutated by the FigureSWTApplet
	private Image backbuffer;
	private SWTElementsVisibilityManager swtVisiblityMangager;
	private SWTZOrderManager zorderManager;
	private SWTGraphicsContext gc;
	private TransformMatrix topLevel;
	private Rectangle viewPortRectangle;
	
	public ViewPortHandler(FigureSWTApplet parent, List<Overlap> overlapFigures){
		this.parent = parent;
		this.figure = parent.getFigure();
		this.overlapFigures = overlapFigures;
		parentSize = new BoundingBox();
		viewPortLocation = new Coordinate(0,0);
		zoom = new Coordinate(1,1);
		viewPortSize = new BoundingBox();
		setScrollbars();
		horBar = parent.getHorizontalBar();
		verBar = parent.getVerticalBar();
		int horY = horBar == null ? 0 : horBar.getSize().y;
		int verX = verBar == null ? 0 : verBar.getSize().x;
		scrollbarSize = new BoundingBox(verX, horY);
		for(Dimension d: HOR_VER){
			if(scrollBars.get(d) != null){
				scrollBars.get(d).setVisible(false);
			}
		}
		if(horBar != null){
			horBar.addSelectionListener(this);
		}
		if(verBar != null){
			verBar.addSelectionListener(this);
		}
		scrollBarsVisible = new TwoDimensional<Boolean>(false, false);
		scrollableMinSize = new BoundingBox(MIN_SIZE + scrollbarSize.getX(), MIN_SIZE+ scrollbarSize.getY());
		swtVisiblityMangager = new SWTElementsVisibilityManager();
		zorderManager = new SWTZOrderManager(parent,overlapFigures);
		gc = new SWTGraphicsContext();
		topLevel = new TransformMatrix();
		viewPortRectangle = new Rectangle(viewPortLocation, viewPortSize);
	}
	
	private void setScrollbars(){
		if(horBar == null || horBar.isDisposed()){
			horBar = parent.getHorizontalBar();
		}
		if(verBar== null || verBar.isDisposed()){
			verBar = parent.getVerticalBar();
		}
		scrollBars = new TwoDimensional<ScrollBar>(horBar, verBar);
	}
	
	private void resetToMinSize(){
		for(Dimension d: HOR_VER){
			if(viewPortSize.get(d) < figure.minSize.get(d)){
				figure.size.set(d,figure.minSize.get(d));
			} else {
				figure.size.set(d,viewPortSize.get(d));
			}
		}
		Rectangle part = getViewPortRectangle();
		figure.resize(part,topLevel);
	}
	
	private void distributeExtraSize(){
		figure.size.set(viewPortSize);
		Rectangle part = getViewPortRectangle();
		figure.resize(part,topLevel);
	}
	
	private void  distributeSizeWidthDependsOnHeight(){
		figure.size.set(viewPortSize);
		Rectangle part = getViewPortRectangle();
		figure.resize(part,topLevel);
	}

	private Rectangle getViewPortRectangle() {
		viewPortRectangle.update();
		return viewPortRectangle;
	}
	
	private void setViewPortSize(){
		if(parent.isDisposed()) return;
		org.eclipse.swt.graphics.Rectangle s = parent.getClientArea();
		viewPortSize.set(s.width-1,s.height-1);
	}
	
	private void setScrollBarsVisiblity(){
		Point p = parent.getSize();
		parentSize.set(p.x,p.y);
		boolean fitsWidth = parentSize.getX() >=
				figure.getMinViewingSize().getX() ;
		boolean fitsHeight =  parentSize.getY() >= figure.getMinViewingSize().getY();
		if(fitsWidth && fitsHeight){
			scrollBarsVisible.set(false,false);
		} else {
			if(!fitsWidth){
				boolean fitsHeightWithHorizontalScrollBar = parentSize.getY() - scrollbarSize.getY() >= figure.getMinViewingSize().getY();
				scrollBarsVisible.set(true,!fitsHeightWithHorizontalScrollBar);
			} else { // !fitsHeight
				boolean fitsWidthWithVerticalScrollBar = parentSize.getX() - scrollbarSize.getX() >= figure.getMinViewingSize().getX();
				scrollBarsVisible.set(!fitsWidthWithVerticalScrollBar,true);
			}
		}
	}
	
	private void propagateScrollBarVisiblity(){
		setScrollbars();
		for(Dimension d : HOR_VER){
			ScrollBar bar = scrollBars.get(d);
			boolean shouldBeVisible =  scrollBarsVisible.get(d);
			if(bar != null && bar.isVisible() != shouldBeVisible){
				bar.setVisible(shouldBeVisible);
			}
		}
	}
	
	private void updateScrollBars(){
		setScrollbars();
		for(Dimension d : HOR_VER){
			ScrollBar bar = scrollBars.get(d);
			if(bar == null) {
				continue;
			}
			double diff = figure.size.get(d) - viewPortSize.get(d);
			viewPortLocation.setMinMax(d, 0, diff);
			bar.setMinimum(0);
			bar.setMaximum(FigureMath.ceil( figure.size.get(d)));
			bar.setIncrement(50);
			int selSize = FigureMath.floor(viewPortSize.get(d));
			bar.setPageIncrement(selSize);
			bar.setThumb(selSize);
			bar.setSelection((int)viewPortLocation.get(d));
		}
	}
	
	private void resizeWidthDependsOnHeight(){
		setScrollbars();
		setViewPortSize();
		if(viewPortSize.getX() == 0 || viewPortSize.getY() == 0 ) return;
		distributeSizeWidthDependsOnHeight();
		Dimension major =  figure.getMajorDimension();
		Dimension minor = major.other();
		if(scrollBars.get(minor) != null && figure.size.get(minor) > viewPortSize.get(minor) && !scrollBars.get(minor).isVisible()){
			scrollBars.get(minor).setVisible(true);
			scrollBarsVisible.set(minor,true);
		} else if(scrollBars.get(minor) != null  && figure.size.get(minor) <= viewPortSize.get(minor) && scrollBarsVisible.get(minor)){
			scrollBarsVisible.set(minor,false);
			scrollBars.get(minor).setVisible(false);
		}
		scrollBarsVisible.set(major,false);
		updateScrollBars();
		parent.notifyLayoutChanged();
		
	}
	
	
	private void resize(){
		if(figure.widthDependsOnHeight()){
			resizeWidthDependsOnHeight();
			return;
		}
		if(parent.isDisposed()) {
			System.out.printf("ignoring resize while parent is disposed\n");
			return;
		}
		
		setScrollbars();
		setScrollBarsVisiblity();
		if(horBar != null && horBar.isVisible() != scrollBarsVisible.getX() 
			|| verBar != null && verBar.isVisible() != scrollBarsVisible.getY()){
			propagateScrollBarVisiblity();
			//return; // we will get more resize events
		}
		setViewPortSize();
		if(viewPortSize.contains(figure.getMinViewingSize())){
			distributeExtraSize();
		} else {
			resetToMinSize();
		}
		Rectangle part = getViewPortRectangle();
		adjustOverlaps(part);
		updateScrollBars();
		parent.notifyLayoutChanged();
	}


	public void translateFromViewPortToFigure(Coordinate mouseLocation) {
		mouseLocation.add(viewPortLocation);
	}
	
	@Override
	public void controlMoved(ControlEvent e) {}

	@Override
	public void controlResized(ControlEvent e) {
		resize();
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		setScrollbars();
		for(Dimension d : HOR_VER){
			
			ScrollBar bar = scrollBars.get(d);
			
			if(bar != null){
				viewPortLocation.set(d,bar.getSelection());
				Rectangle part = getViewPortRectangle();
				adjustOverlaps(part);
			} else {
			}
		}
		parent.requestRedraw();
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {}

	@Override
	public void paintControl(PaintEvent e) {
		draw(e.gc);
		parent.animate();
	}
	

	
	public void draw(GC swtGC){
		if(viewPortSize.getX() <= 0 || viewPortSize.getY() <= 0){
			System.out.printf("NOT DRAWING %s\n",this);
			return;
		}
		long startTime = System.nanoTime();
		setBackBuffer();
		try{
			gc.setGC(new GC(backbuffer));
		} catch(IllegalArgumentException e){
			makeNewBackBuffer();
			gc.setGC(new GC(backbuffer));
		}
		
		gc.getGC().setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		
		Rectangle part = getViewPortRectangle();
		gc.getGC().fillRectangle(0, 0, FigureMath.ceil(part.getSize().getX()), FigureMath.ceil(part.getSize().getY()));
		gc.translate(-part.getLocation().getX(), -part.getLocation().getY());

		
		figure.draw(zoom, gc, part,swtVisiblityMangager.getVisibleSWTElementsVector());
		for(Overlap f : overlapFigures){
			if(f.over.overlapsWith(part)){
				f.over.draw(zoom, gc, part, swtVisiblityMangager.getVisibleSWTElementsVector());
			}
		}
		gc.translate(part.getLocation().getX(), part.getLocation().getY());
		

		
		gc.dispose();
		swtGC.drawImage(backbuffer, 0, 0);
	
		swtVisiblityMangager.makeOffscreenElementsInvisble();
		zorderManager.draw(part);
		
		if(FigureExecutionEnvironment.profile) {
			long rascalTime = parent.getCallBackEnv().getAndResetRascalTime();
			rascalTime/=1000000;
			long drawTime = System.nanoTime() - startTime;
			drawTime/=1000000;
			System.out.printf("Drawing (part) took %d rascalTime %d %f\n", drawTime,rascalTime,(double)rascalTime / (double) drawTime);
		}
	}

	public void adjustOverlaps(Rectangle part) {
		for(Overlap f : overlapFigures){
			if(f.innerFig.overlapsWith(part)){
				adjustOverlap(part,  f);
			}
		}
	}
	
	private void adjustOverlap(Rectangle part, Overlap f){
		if(!f.over.overlapsWith(part)){
			return;
		}
		boolean change = false;
		for(Dimension d : HOR_VER){
			if(f.desiredOverlapLocation.get(d) < part.getLocation().get(d)){
				f.over.globalLocation.set(d,part.getLocation().get(d));
				change = true;
			} else if(f.desiredOverlapLocation.get(d) + f.over.size.get(d) > part.getRightDown().get(d)){
				f.over.globalLocation.set(d,part.getRightDown().get(d) - f.over.size.get(d));
				change = true;
			} else {
				f.over.globalLocation.set(d,f.desiredOverlapLocation.get(d));
			}
		}
		if(change || !f.over.globalLocation.equals(f.desiredOverlapLocation)){
			f.over.updateGlobalLocation();
		}
	}

	

	private void setBackBuffer(){
		if(backbuffer == null || backbuffer.isDisposed() || backbuffer.getBounds().width != viewPortSize.getX() +1 || backbuffer.getBounds().height != viewPortSize.getY()+1){
			makeNewBackBuffer();
		}
	}
	
	private void makeNewBackBuffer(){
		if(backbuffer!=null){
			backbuffer.dispose();
		}
		backbuffer = new Image(parent.getDisplay(), FigureMath.ceil(viewPortSize.getX())+1, FigureMath.ceil(viewPortSize.getY())+1);
	}

	public void dispose() {
		if(backbuffer!=null) backbuffer.dispose();
		swtVisiblityMangager.dispose();
		zorderManager.dispose();
		for(Dimension d: HOR_VER){
			if(scrollBars.get(d) != null){
				scrollBars.get(d).dispose();
			}
		}
	}

	@Override
	public void notifyFigureChanged() {
		resize();
		zorderManager.notifyFigureChanged();
		
		parent.requestRedraw();
	}

	public Image getFigureImage() {
		return backbuffer;
	}

	public void beforeInitialise() {
		zorderManager.clearSWTOrder();	
	}
	
	public void writeScreenShot(OutputStream to){
		Image screenShot = new Image(parent.getDisplay(), (int)viewPortSize.getX()+1 ,(int)viewPortSize.getY()+1);
		GC gc = new GC(parent);
		gc.copyArea(screenShot, 0,0);
		gc.dispose();
		ImageLoader il = new ImageLoader();
		il.data = new ImageData[] {screenShot.getImageData()};
		il.save(to, SWT.IMAGE_PNG);
	}
	
	public void makeScreenShot() {
		FileDialog f = new FileDialog(parent.getShell(), SWT.SAVE);
		f.setText("Select where to save your screenshot.");
		String filepath = f.open();
		if(filepath == null){
			return;
		}

		
		if(!filepath.endsWith(".png")){
			filepath+=".png";
		}
		try{
			OutputStream to = new FileOutputStream(filepath);
			writeScreenShot(to);
			to.close();
		} catch(FileNotFoundException e){
			PrintWriter stdErr = this.parent.getCallBackEnv().getRascalContext().getStdErr();
			stdErr.printf("Could not write to " + filepath + "\n Reason " + e.getMessage());
		} catch (IOException e) {
			PrintWriter stdErr = this.parent.getCallBackEnv().getRascalContext().getStdErr();
			stdErr.printf("Could not write to " + filepath + "\n Reason " + e.getMessage());
		}
	}

	
	public void addSWTElement(Control c) {
		zorderManager.addSWTElement(c);
		
	}

	public void addAboveSWTElement(Figure fig) {
		zorderManager.addAboveSWTElement(fig);
		
	}
}
