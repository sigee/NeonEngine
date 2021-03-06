package net.medox.neonengine.audio.audioLoading;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.lwjgl.openal.AL10;

import net.medox.neonengine.audio.audioLoading.oggLoading.OggInputStream;
import net.medox.neonengine.core.NeonEngine;

public class OGGSound{
	private ByteBuffer data;
	private int channels;
	private int samplerate;
	
	public OGGSound(String fileName){
		FileInputStream fin = null;
		try{
			fin = new FileInputStream(fileName);
		}catch(FileNotFoundException e){
			NeonEngine.throwError("Error: unable to read " + fileName);
		}
		
		try{
			create(new BufferedInputStream(fin));
		}catch(IOException e){
			NeonEngine.throwError("Error: unable to convert " + fileName);
		}
	}
	
	public IndexedSound toIndexedSound(){
		final IndexedSound result = new IndexedSound();
		
		result.setData(data);
		result.setFormat(channels > 1 ? AL10.AL_FORMAT_STEREO16 : AL10.AL_FORMAT_MONO16);
		result.setSamplerate(samplerate);
		
		data.clear();
		
		return result;
	}
	
	public void create(InputStream input) throws IOException{
		if(input == null){
			NeonEngine.throwError("Error: Failed to read OGG.");
		}
		
		final ByteArrayOutputStream dataout = new ByteArrayOutputStream();
		final OggInputStream oggInput = new OggInputStream(input);
		
		while(!oggInput.atEnd()){
			dataout.write(oggInput.read());
		}
		
		channels = oggInput.getChannels();
		samplerate = oggInput.getRate();
		
		oggInput.close();
		
		final byte[] data = dataout.toByteArray();
		this.data = ByteBuffer.allocateDirect(data.length);
		this.data.put(data);
		this.data.rewind();
	}
}
