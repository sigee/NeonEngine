package net.medox.neonengine.physics;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.physics.bullet.dynamics.btTypedConstraint;

public abstract class Constraint{
	private static final List<Constraint> constraints = new ArrayList<Constraint>();
	
	private btTypedConstraint constraint;
	
	public Constraint(){
		constraints.add(this);
	}
	
	public void setConstraint(btTypedConstraint constraint){
		this.constraint = constraint;
	}
	
	public btTypedConstraint getConstraint(){
		return constraint;
	}
	
	@Override
	protected void finalize() throws Throwable{
		cleanUp();
		constraints.remove(this);
		
		super.finalize();
	}
	
	public void cleanUp(){
		constraint.dispose();
	}
	
	public static void dispose(){
		for(final Constraint data : constraints){
			data.cleanUp();
		}
	}
}
