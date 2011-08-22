package org.rascalmpl.library.vis.properties;

import org.rascalmpl.library.vis.util.FigureColorUtils;

public class CombinedProperty <PropType> extends  PropertyValue<PropType> {
	
	public static enum Combine{
		PLUS{ 
			public Object execute(Object l,Object r){
				double ld = ((Number)l).doubleValue();
				double rd = ((Number)r).doubleValue();
				double val = ld + rd;
				if(l instanceof Integer && r instanceof Integer){
					return (int)val;
				} else {
					return val;
				}
			}
		},
		MUL { 
			public Object execute(Object l,Object r){
				double ld = ((Number)l).doubleValue();
				double rd = ((Number)r).doubleValue();
				System.out.printf("%f %f becomes %f\n\n", ld,rd,ld*rd);
				double val = ld * rd;
				if(l instanceof Integer && r instanceof Integer){
					return (int)val;
				} else {
					return val;
				}
			}
		},
		AND{
			public Object execute(Object l,Object r){
				boolean lb = ((Boolean)l);
				boolean rb = ((Boolean)r);
				return lb && rb;
			}
		},
		OR  {
			public Object execute(Object l,Object r){
				boolean lb = ((Boolean)l);
				boolean rb = ((Boolean)r);
				return lb || rb;
			}
		},
		INTERPOLATECOLOR {
			public Object execute(Object l,Object r){
				int lb = ((Integer)l);
				int rb = ((Integer)r);
				return FigureColorUtils.myLerpColor(lb,rb,0.5f);
			}
		},
		FIRST{ 
			public Object execute(Object l,Object r){
				return l;
			}
		},
		LAST {
			public Object execute(Object l,Object r){
				return r;
			}
		};
		
		public abstract Object execute(Object l,Object r);
		
	}
	
	PropertyValue<PropType> l,r; 
	Combine combine;
	
	CombinedProperty(PropertyValue<PropType> l,PropertyValue<PropType> r,Combine combine){
		this.l = l;
		this.r = r;
		this.combine = combine;
	}

	@SuppressWarnings("unchecked")
	@Override
	public PropType getValue() {
		return (PropType)combine.execute(l.getValue(),r.getValue());
	}
	
	

}
