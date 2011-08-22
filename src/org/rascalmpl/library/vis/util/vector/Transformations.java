package org.rascalmpl.library.vis.util.vector;

public final class Transformations {
	
	public static interface Transformation{
		void apply(TransformMatrix m);
		void unapply(TransformMatrix m);
	}
	
	public static class UnitTransformation implements Transformation{
		public static final UnitTransformation instance = new UnitTransformation();
		@Override
		public void apply(TransformMatrix m) {}
		@Override
		public void unapply(TransformMatrix m) {}
	}
	
	public static class Translation implements Transformation{
		double x, y;
		
		Translation(double x, double y){
			this.x = x;
			this.y = y;
		}

		@Override
		public void apply(TransformMatrix m) {
			m.translate(x, y);
		}

		@Override
		public void unapply(TransformMatrix m) {
			m.translate(-x,-y);
		}
	}
	
	public static class Rotation implements Transformation{
		double angle;

		Rotation(double angle){
			this.angle = angle;
		}
		
		@Override
		public void apply(TransformMatrix m) {
			m.rotate(angle);
		}

		@Override
		public void unapply(TransformMatrix m) {
			m.rotate(-angle);
		}		
	}
	
	public static class Scale implements Transformation{
		double x,y;
		
		Scale(double x, double y){
			this.x = x;
			this.y = y;
		}

		@Override
		public void apply(TransformMatrix m) {
			m.scale(x, y);
		}

		@Override
		public void unapply(TransformMatrix m) {
			m.scale(1.0/x, 1.0/y);
		}
	}
	
	public static class Combine implements Transformation{
		Transformation[] transformations;
		
		Combine(Transformation ...transformations){
			this.transformations = transformations;
		}

		@Override
		public void apply(TransformMatrix m) {
			for(int i = 0 ; i < transformations.length ; i++){
				transformations[i].apply(m);
			}
		}

		@Override
		public void unapply(TransformMatrix m) {
			for(int i = transformations.length-1 ; i >= 0 ; i--){
				transformations[i].unapply(m);
			}
		}
	}

}
