package org.rascalmpl.library.vis.interaction;

import java.awt.event.MouseEvent;

import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.result.OverloadedFunctionResult;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.FigurePApplet;
import org.rascalmpl.library.vis.properties.IPropertyManager;

import processing.core.PApplet;
import processing.core.PConstants;

public class EnterTrigger extends Figure {
	
	protected String tname;
	
	StringBuffer currentInput;
	private int cursor;
	
	private static boolean debug = false;
	private final int carretHeight = 2;
	private final String carret = "\u2038";

	private boolean doubleClicked = false;
	IValueFactory vf;
	protected Type[] argtypes = new Type[] {TypeFactory.getInstance().stringType()};
	protected  IValue[] argvals = new IValue[1];
	
	private OverloadedFunctionResult validator;

	public EnterTrigger(FigurePApplet fpa, IPropertyManager properties, IString tname, IString initial, IValue validator, IEvaluatorContext ctx) {
		super(fpa, properties, ctx);
		this.tname = tname.getValue();
		currentInput = new StringBuffer(initial.getValue());
	
		vf = ctx.getValueFactory();
		if(validator.getType().isExternalType() && (validator instanceof OverloadedFunctionResult)){
			this.validator = (OverloadedFunctionResult) validator;
		} else
			 RuntimeExceptionFactory.illegalArgument(validator, ctx.getCurrentAST(), ctx.getStackTrace());
		cursor = currentInput.length();
		updateControl();
	}
	
	@Override
	public
	void bbox(){
		applyFontProperties();
		
		height = getHeightProperty() + 2 * carretHeight;
		width = getWidthProperty();
		if(debug){
			System.err.printf("strControl.bbox: font=%s, ascent=%f, descent=%f\n", fpa.getFont(), fpa.textAscent(), fpa.textDescent() );
			System.err.printf("strControl.bbox: txt=\"%s\", width=%f, height=%f angle =%f\n", currentInput, width, height, getTextAngleProperty());
		}
	}
	
	@Override
	public
	void draw(float left, float top) {
		this.setLeft(left);
		this.setTop(top);
		
		if(doubleClicked){
			fpa.fill(192, 90);
			fpa.stroke(0);
			fpa.strokeWeight(0);
			fpa.rect(left, top, width, height);
		}
		
		if(!isValid()){
			fpa.fill(0XFFFA8072, 80); // Salmon
			fpa.stroke(0);
			fpa.strokeWeight(0);
			fpa.rect(left, top, width, height);
		}
		
		applyProperties();
		applyFontProperties();
	
		if(debug)System.err.printf("strControl.draw: %s, doubleClicked=%b, font=%s, left=%f, top=%f, width=%f, height=%f\n", currentInput, doubleClicked, fpa.getFont(), left, top, width, height);
		if(height > 0 && width > 0){

			fpa.textAlign(PConstants.CENTER,PConstants.CENTER);
	
			String s = currentInput.toString();
			float carretX = left + width/2 - fpa.textWidth(s)/2 + fpa.textWidth(currentInput.substring(0, cursor));
			
			fpa.text(s, left + width/2, top + height/2 - 2);
			fpa.text(carret, carretX, top + height/2 - 1);

		}
	}
	
	@Override
	public boolean mousePressed(int mouseX, int mouseY, MouseEvent e){
		if(mouseInside(mouseX, mouseY)){
			fpa.registerFocus(this);
			if(debug)System.err.printf("StrControl.mousePressed: %d, %d, clickCnt=%d\n", mouseX, mouseY,  e.getClickCount());
			
			String s = currentInput.toString();
			float tw = fpa.textWidth(s);
			float start = getLeft() + width/2 - tw/2;
			float end = getLeft() + width/2 + tw/2;
			if(mouseX < start)
				cursor = 0;
			else if(mouseX > end)
				cursor = s.length();
			else {
				if(debug)System.err.printf("tw=%f, mouseDiff=%f, oldCursor=%d, ", tw, mouseX - getLeft(), cursor);
				cursor = FigurePApplet.round(s.length() * (mouseX - start) / tw);
				if(debug)System.err.printf("newCursor=%d\n", cursor);
			}
			doubleClicked = e.getClickCount() > 1;
			return true;
		}
		doubleClicked = false;
		return false;
	}
	
	@Override
	public boolean keyPressed(int key, int keyCode) {
		if(debug)System.err.println("strControl, key = " + key);
		if (key == PApplet.ENTER || key == PApplet.RETURN) {
			updateControl();
		} else if (key == PApplet.BACKSPACE) {
			if(doubleClicked){
				currentInput.delete(0, currentInput.length());
				cursor = 0;
				doubleClicked = false;
			} else
			if(cursor > 0){ 
				if(cursor == currentInput.length())
					currentInput.deleteCharAt(cursor-1);
				else
					currentInput.deleteCharAt(cursor);
				cursor--;
			}
		} else if(key == PApplet.DELETE){
			if(cursor < currentInput.length())
				currentInput.deleteCharAt(cursor);
			doubleClicked = false;
				
		} else if(key == PApplet.CODED){
				if(keyCode == PApplet.LEFT && cursor > 0)
						cursor--;
				else if(keyCode == PApplet.RIGHT && cursor < currentInput.length())
					cursor++;
			} 
		else {
			if(doubleClicked){
				currentInput.delete(0, currentInput.length());
				cursor = 0;
				doubleClicked = false;
			}
			currentInput = currentInput.insert(cursor, Character.toString((char) key));
			cursor++;
		}
		
		if(debug)System.err.println("currentInput = " + currentInput);
		return true;
	}
	
	boolean isValid(){
		String s = currentInput.toString();
		argvals[0] = vf.string(s);
		Result<IValue> validationValue = validator.call(argtypes, argvals);
		return validationValue.getValue().isEqual(vf.bool(true));
	}
	
	void updateControl(){
		fpa.setStrTrigger(tname, currentInput.toString());
	}
	
}
