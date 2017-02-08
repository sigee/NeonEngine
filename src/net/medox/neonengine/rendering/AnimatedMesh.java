package net.medox.neonengine.rendering;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.medox.neonengine.core.NeonEngine;
import net.medox.neonengine.core.Transform;
import net.medox.neonengine.math.Matrix4f;
import net.medox.neonengine.physics.StaticMeshCollider;
import net.medox.neonengine.rendering.resourceManagement.AnimatedMeshData;

public class AnimatedMesh{
	private static final Map<String, AnimatedMeshData> loadedModels = new ConcurrentHashMap<String, AnimatedMeshData>();
	
	private final String fileName;
	
	private AnimatedMeshData resource;
	private boolean cleanedUp;
	
	
	private final Joint rootJoint;
	private final int jointCount;
	
	public AnimatedMesh(String fileName){
		this.fileName = fileName;
		resource = loadedModels.get(fileName);
		
		if(resource == null){
			loadMesh(fileName);
			loadedModels.put(fileName, resource);
		}else{
			resource.addReference();
		}
		
		rootJoint = null;//TODO Not final
		jointCount = 0;//TODO Not final
		
		rootJoint.calcInverseBindTransform(new Matrix4f());
	}
	
	public Joint getRootJoint(){
		return rootJoint;
	}
	
	public void doAnimation(){//TODO add this
		
	}
	
	public void update(){
		
	}
	
	public Matrix4f[] getJointTransforms(){
		Matrix4f[] jointMatrices = new Matrix4f[jointCount];
		addJointsToArray(rootJoint, jointMatrices);
		return jointMatrices;
	}
	
	private void addJointsToArray(Joint headJoint, Matrix4f[] jointMatrices){
		jointMatrices[headJoint.index] = headJoint.getAnimatedTransform();
		
		for(Joint childJoint : headJoint.children){
			addJointsToArray(childJoint, jointMatrices);
		}
	}
	
	public boolean inFrustum(Transform transform, Camera camera){
		return resource.inFrustum(transform, camera);
	}
	
	public void cleanUp(){
		if(!cleanedUp){
			if(resource.removeReference()){
				resource.dispose();
				loadedModels.remove(fileName);
			}
			
			cleanedUp = true;
		}
	}
	
	@Override
	protected void finalize() throws Throwable{
		cleanUp();
		
		super.finalize();
	}
	
	public void draw(){
		resource.draw();
	}
	
	public StaticMeshCollider generateCollider(){
		return resource.generateCollider();
	}
	
	private AnimatedMesh loadMesh(String fileName){
		final String[] splitArray = fileName.split("\\.");
		final String ext = splitArray[splitArray.length - 1];
		
		if(ext.equals("dae")){
//			resource = new MeshData(new OBJModel("./res/models/" + fileName).toIndexedModel());//TODO add this
		}else{
			NeonEngine.throwError("Error: '" + ext + "' file format not supported for animated mesh data.");
		}
		
		return this;
	}
	
	public static void dispose(){
		for(final AnimatedMeshData data : loadedModels.values()){
			data.dispose();
		}
	}
}
