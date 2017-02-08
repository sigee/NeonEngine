package net.medox.neonengine.rendering;

import java.util.ArrayList;
import java.util.List;

import net.medox.neonengine.math.Matrix4f;

public class Joint{
	public final int index;
	public final String name;
	public final List<Joint> children;
	
	private final Matrix4f localBindTransform;
	
	private Matrix4f animatedTransform;
	private Matrix4f inverseBindTransform;
	
	public Joint(int index, String name, Matrix4f bindLocalTransform){
		this.index = index;
		this.name = name;
		this.localBindTransform = bindLocalTransform;
		
		children = new ArrayList<Joint>();
		
		animatedTransform = new Matrix4f();
		inverseBindTransform = new Matrix4f();
	}
	
	protected void calcInverseBindTransform(Matrix4f parentBindTransform){
		Matrix4f bindTransform = parentBindTransform.mul(localBindTransform);
//		inverseBindTransform = bindTransform.invert();//TODO add this
		
		for(Joint child : children){
			child.calcInverseBindTransform(bindTransform);
		}
	}
	
	public void addChild(Joint child){
		children.add(child);
	}
	
	public Matrix4f getAnimatedTransform(){
		return animatedTransform;
	}
	
	public Matrix4f getInverseBindTranform(){
		return inverseBindTransform;
	}
	
	public void setAnimationTransform(Matrix4f animationTransform){
		animatedTransform = animationTransform;
	}
}
