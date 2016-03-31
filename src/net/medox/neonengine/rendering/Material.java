package net.medox.neonengine.rendering;

import java.util.HashMap;
import java.util.Map;

import net.medox.neonengine.core.Util;
import net.medox.neonengine.math.Vector3f;

public class Material{
	public static final Texture DEFAULT_DIFFUSE_TEXTURE = Util.createDefaultDiffuseMap();
	public static final Texture DEFAULT_NORMAL_MAP_TEXTURE = Util.createDefaultNormalMap();
//	public static final Texture DEFAULT_DISPLACEMENT_MAP_TEXTURE = Util.createDefaultDisplacementMap();
	public static final Texture DEFAULT_SPECULAR_MAP_TEXTURE = Util.createDefaultSpecularMap();
	public static final Texture DEFAULT_GLOW_MAP_TEXTURE = Util.createDefaultGlowMap();
	
//	public static final Texture defaultCubeMap = Util.createDefaultCubeMap();
	
	public static final float DEFAULT_SPECULAR_INTENSITY = 0;
	public static final float DEFAULT_SPECULAR_POWER = 0;
	
//	public static final float DEFAULT_DISP_MAP_SCALE = 0.0f;
//	public static final float DEFAULT_DISP_MAP_OFFSET = 0.0f;
	
	private final Map<String, Texture> textureMap;
	private final Map<String, CubeMap> cubeMapMap;
	private final Map<String, Vector3f> vector3fMap;
	private final Map<String, Float> floatMap;
	
	public Material(){
		textureMap = new HashMap<String, Texture>();
		cubeMapMap = new HashMap<String, CubeMap>();
		vector3fMap = new HashMap<String, Vector3f>();
		floatMap = new HashMap<String, Float>();
				
		setTexture("diffuse", DEFAULT_DIFFUSE_TEXTURE);
		setTexture("normalMap", DEFAULT_NORMAL_MAP_TEXTURE);
//		setTexture("dispMap", DEFAULT_DISPLACEMENT_MAP_TEXTURE);
		setTexture("specMap", DEFAULT_SPECULAR_MAP_TEXTURE);
		setTexture("glowMap", DEFAULT_GLOW_MAP_TEXTURE);
		
		setFloat("specularIntensity", DEFAULT_SPECULAR_INTENSITY);
		setFloat("specularPower", DEFAULT_SPECULAR_POWER);
		
//		/*float baseBias = defaultDispMapScale/2.0f;*/
//		setFloat("dispMapScale", DEFAULT_DISP_MAP_SCALE);
//		/*setFloat("dispMapBias", -baseBias + baseBias * defaultDispMapOffset);*/
//		setFloat("dispMapBias", DEFAULT_DISP_MAP_OFFSET);
	}
	
	public void setDiffuseMap(Texture texture){
		setTexture("diffuse", texture);
	}
	
	public void setNormalMap(Texture texture){
		setTexture("normalMap", texture);
	}
	
	public void setSpecularMap(Texture texture){
		setTexture("specMap", texture);
	}
	
	public void setGlowMap(Texture texture){
		setTexture("glowMap", texture);
	}
	
	public void setSpecularIntensity(float value){
		setFloat("specularIntensity", value);
	}
	
	public void setSpecularPower(float value){
		setFloat("specularPower", value);
	}
	
//	@Override
//	public void setFloat(String name, float floatValue){
//		if(name.equals("dispMapBias")){
////			float dispMapOffset = floatValue;
//			final float baseBias = getFloat("dispMapScale")/2.0f;
//			
//			floatValue = -baseBias + baseBias * /*dispMapOffset*/floatValue;
//		}else if(name.equals("dispMapScale")){
//			final float dispMapScale = getFloat("dispMapScale");
////			float dispMapBias = getFloat("dispMapBias");
//			
////			float dispMapOffset = (dispMapBias + (dispMapScale/2))/(dispMapScale/2);
////			float newDispMapBias = -(floatValue/2) + (floatValue/2) * dispMapOffset;
//			
//			super.setFloat("dispMapBias", /*newDispMapBias*/-(floatValue/2) + (floatValue/2) * ((getFloat("dispMapBias") + (dispMapScale/2))/(dispMapScale/2)));
//		}
//		
//		super.setFloat(name, floatValue);
//	}
	
	public void setTexture(String name, Texture texture){
		textureMap.remove(name);
		textureMap.put(name, texture);
	}
	
	public void setCubeMap(String name, CubeMap cubeMap){
		cubeMapMap.remove(name);
		cubeMapMap.put(name, cubeMap);
	}
	
	public void setVector3f(String name, Vector3f vector3f){
		vector3fMap.remove(name);
		vector3fMap.put(name, vector3f);
	}
	
	public void setFloat(String name, float floatValue){
		floatMap.remove(name);
		floatMap.put(name, floatValue);
	}
	
	public Texture getTexture(String name){
		final Texture result = textureMap.get(name);
		
		return result == null ? Material.DEFAULT_DIFFUSE_TEXTURE : result;
	}
	
	public CubeMap getCubeMap(String name){
		final CubeMap result = cubeMapMap.get(name);
		
		return result == null ? null : result;
	}
	
	public Vector3f getVector3f(String name){
		final Vector3f result = vector3fMap.get(name);
		
		return result == null ? new Vector3f(0, 0, 0) : result;
	}
	
	public float getFloat(String name){
		final Float result = floatMap.get(name);
		
		return result == null ? 0 : result;
	}
}
