package net.medox.neonengine.rendering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.medox.neonengine.core.NeonEngine;
import net.medox.neonengine.core.Transform;
import net.medox.neonengine.physics.StaticMeshCollider;
import net.medox.neonengine.rendering.meshLoading.IndexedModel;
import net.medox.neonengine.rendering.meshLoading.OBJModel;
import net.medox.neonengine.rendering.resourceManagement.MeshData;

public class Mesh{
	private static final Map<String, MeshData> loadedModels = new HashMap<String, MeshData>();
	private static final List<MeshData> customModels = new ArrayList<MeshData>();
	
	private final String fileName;
	
	private MeshData resource;
	private boolean cleanedUp;
	
	public Mesh(String fileName){
		this.fileName = fileName;
		resource = loadedModels.get(fileName);
		
		if(resource == null){
			loadMesh(fileName);
			loadedModels.put(fileName, resource);
		}else{
			resource.addReference();
		}
	}
	
	public Mesh(String meshName, IndexedModel model){
		this.fileName = meshName;
		
		if(fileName.equals("")){
			model.calcRadius();
			
			resource = new MeshData(model);
			customModels.add(resource);
		}else{
			if(loadedModels.get(fileName) == null){
				model.calcRadius();
				
				resource = new MeshData(model);
				loadedModels.put(fileName, resource);
			}else{
				NeonEngine.throwError("Error: the mesh name:" + meshName + "is already in use.");
			}
		}
	}
	
	public boolean inFrustum(Transform transform, Camera camera){
		return resource.inFrustum(transform, camera);
	}
	
	public void cleanUp(){
		if(!cleanedUp){
			if(fileName.equals("")){
				resource.dispose();
				customModels.remove(resource);
			}else if(resource.removeReference()){
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
	
	private Mesh loadMesh(String fileName){
		final String[] splitArray = fileName.split("\\.");
		final String ext = splitArray[splitArray.length - 1];
		
		if(ext.equals("obj")){
			resource = new MeshData(new OBJModel("./res/models/" + fileName).toIndexedModel());
		}else{
			NeonEngine.throwError("Error: '" + ext + "' file format not supported for mesh data.");
		}
		
		return this;
	}
	
	public static void dispose(){
		for(final MeshData data : loadedModels.values()){
			data.dispose();
		}
		
		for(final MeshData data : customModels){
			data.dispose();
		}
	}
}
