package net.medox.neonengine.audio.resourceManagement;

import java.nio.FloatBuffer;

import net.medox.neonengine.core.DataUtil;
import net.medox.neonengine.math.Vector3f;

import org.lwjgl.openal.AL10;

public class SourceData{
	private final int source;
	
	private final FloatBuffer sourcePosition;
	private final FloatBuffer sourceVelocity;
	
	public SourceData(){
		source = AL10.alGenSources();
		
		sourcePosition = DataUtil.createFloatBuffer(3).put(new float[]{0.0f, 0.0f, 0.0f});
		sourcePosition.flip();
		
		sourceVelocity = DataUtil.createFloatBuffer(3).put(new float[]{0.0f, 0.0f, 0.0f});
		sourceVelocity.flip();
	}
	
	public void setBuffer(SoundData data){
		AL10.alSourcei(source, AL10.AL_BUFFER, data.getID());
	}
	
	public void play(){
		AL10.alSourcePlay(source);
	}
	
	public void stop(){
		AL10.alSourceStop(source);
	}
	
	public void setPitch(float value){
		AL10.alSourcef(source, AL10.AL_PITCH, value);
	}
	
	public void setGain(float value){
		AL10.alSourcef(source, AL10.AL_GAIN, value);
	}
	
	public void setPosition(Vector3f value){
		sourcePosition.put(0, value.getX());
		sourcePosition.put(1, value.getY());
		sourcePosition.put(2, value.getZ());
        
		AL10.alSourcefv(source, AL10.AL_POSITION, sourcePosition);
	}
	
	public void setVelocity(Vector3f value){
		sourceVelocity.put(0, value.getX());
		sourceVelocity.put(1, value.getY());
		sourceVelocity.put(2, value.getZ());
        
		AL10.alSourcefv(source, AL10.AL_VELOCITY, sourceVelocity);
	}
	
	public void setLooping(boolean value){
		if(value){
			AL10.alSourcei(source, AL10.AL_LOOPING, AL10.AL_TRUE);
		}else{
			AL10.alSourcei(source, AL10.AL_LOOPING, AL10.AL_FALSE);
		}
	}
	
//	public void setMinGain(float value){
//		AL10.alSourcef(source, AL10.AL_MIN_GAIN, value);
//	}
//	
//	public void setMaxGain(float value){
//		AL10.alSourcef(source, AL10.AL_MAX_GAIN, value);
//	}
	
	public void setRolloffFactor(float value){
		AL10.alSourcef(source, AL10.AL_ROLLOFF_FACTOR, value);
	}
	
	public void dispose(){
		AL10.alDeleteSources(source);
	}
}
