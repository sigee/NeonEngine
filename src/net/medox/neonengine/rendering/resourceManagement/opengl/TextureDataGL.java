package net.medox.neonengine.rendering.resourceManagement.opengl;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import net.medox.neonengine.core.CoreEngine;
import net.medox.neonengine.core.Util;
import net.medox.neonengine.rendering.resourceManagement.TextureData;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBFramebufferObject;
import org.lwjgl.opengl.EXTTextureFilterAnisotropic;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class TextureDataGL extends TextureData{
//	public static int bound;
	
	private final int textureTarget;
	private final int numTextures;
	
	private int[] textureID;
	private int frameBuffer;
	private int renderBuffer;
	private int width;
	private int height;
	
	public TextureDataGL(int textureTarget, int width, int height, int numTextures, ByteBuffer[] data, int[] filters, int[] internalFormat, int[] format, int[] type, boolean clamp, int[] attachments){
		super(textureTarget, width, height, numTextures, data, filters, internalFormat, format, type, clamp, attachments);
		
		textureID = new int[numTextures];
		this.textureTarget = textureTarget;
		this.numTextures = numTextures;
		
		if(CoreEngine.PROFILING_SET_2x2_TEXTURE == 0){
			this.width = width;
			this.height = height;
		}else{
			this.width = 2;
			this.height = 2;
		}
		
		frameBuffer = 0;
		renderBuffer = 0;
		
		initTextures(data, filters, internalFormat, format, type, clamp);
		initRenderTargets(attachments);
	}
	
	@Override
	public void bind(int samplerSlot){
//		if(bound != textureID[0]){
			assert(samplerSlot >= 0 && samplerSlot <= 31);
			GL13.glActiveTexture(GL13.GL_TEXTURE0 + samplerSlot);
			GL11.glBindTexture(textureTarget, textureID[0]);
//			
//			bound = textureID[0];
//		}
	}
	
	@Override
	public void bind(int samplerSlot, int id){
//		if(bound != textureID[id]){
			assert(samplerSlot >= 0 && samplerSlot <= 31);
			GL13.glActiveTexture(GL13.GL_TEXTURE0 + samplerSlot);
			GL11.glBindTexture(textureTarget, textureID[id]);
//			
//			bound = textureID[id];
//		}
	}
	
	@Override
    public void bindAsRenderTarget(){
    	GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    	ARBFramebufferObject.glBindFramebuffer(ARBFramebufferObject.GL_FRAMEBUFFER, frameBuffer);
    	
    	if(CoreEngine.PROFILING_SET_1x1_VIEWPORT == 0){
    		GL11.glViewport(0, 0, width, height);
    	}else{
    		GL11.glViewport(0, 0, 1, 1);
    	}
    }
	
	@Override
	public int getWidth(){
		return width;
	}
	
	@Override
	public int getHeight(){
		return height;
	}
	
	public void initTextures(ByteBuffer[] data, int[] filters, int[] internalFormat, int[] format, int[] type, boolean clamp){
		for(int i = 0; i < numTextures; i++){
			textureID[i] = GL11.glGenTextures();
			
			GL11.glBindTexture(textureTarget, textureID[i]);

			GL11.glTexParameterf(textureTarget, GL11.GL_TEXTURE_MIN_FILTER, filters[i]);
			GL11.glTexParameterf(textureTarget, GL11.GL_TEXTURE_MAG_FILTER, filters[i]);
			
			if(clamp){
				GL11.glTexParameterf(textureTarget, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
				GL11.glTexParameterf(textureTarget, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
				
//				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, ARBShadow.GL_TEXTURE_COMPARE_MODE_ARB, ARBShadow.GL_COMPARE_R_TO_TEXTURE_ARB);
//				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, ARBShadow.GL_TEXTURE_COMPARE_FUNC_ARB, GL11.GL_LEQUAL);
			}

			GL11.glTexImage2D(textureTarget, 0, internalFormat[i], width, height, 0, format[i], type[i], data[i]);
			
			if(filters[i] == GL11.GL_NEAREST_MIPMAP_NEAREST ||
					filters[i] == GL11.GL_NEAREST_MIPMAP_LINEAR ||
					filters[i] == GL11.GL_LINEAR_MIPMAP_NEAREST ||
					filters[i] == GL11.GL_LINEAR_MIPMAP_LINEAR){
				GL30.glGenerateMipmap(textureTarget);
//				float maxAnisotropy = GL11.glGetFloat(EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT);
//				glTexParameterf(textureTarget, EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT, Util.clamp(0.0f, 8.0f, maxAnisotropy)/*Clamp(0.0f, 8.0f, maxAnisotropy)*/);
				GL11.glTexParameterf(textureTarget, EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT, Util.clamp(0.0f, 8.0f, GL11.glGetFloat(EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT))/*Clamp(0.0f, 8.0f, maxAnisotropy)*/);
			}else{
				GL11.glTexParameteri(textureTarget, GL12.GL_TEXTURE_BASE_LEVEL, 0);
				GL11.glTexParameteri(textureTarget, GL12.GL_TEXTURE_MAX_LEVEL, 0);
			}
		}
	}
	
	public void initRenderTargets(int[] attachments){
		if(attachments == null){
			return;
		}

		final IntBuffer drawBuffers = BufferUtils.createIntBuffer(numTextures);
//		IntBuffer.allocate(32);      //32 is the max number of bound textures in OpenGL
		assert(numTextures <= 32);            //Assert to be sure no buffer overrun should occur

		boolean hasDepth = false;
		for(int i = 0; i < numTextures; i++){
			if(attachments[i] == ARBFramebufferObject.GL_DEPTH_ATTACHMENT){
				drawBuffers.put(i, GL11.GL_NONE);
				hasDepth = true;
			}else{
				drawBuffers.put(i, attachments[i]); 
				
			}

			if(attachments[i] == GL11.GL_NONE){
				continue;
			}

			if(frameBuffer == 0){
				frameBuffer = ARBFramebufferObject.glGenFramebuffers();
//				glGenFramebuffers(1, frameBuffer);
				ARBFramebufferObject.glBindFramebuffer(ARBFramebufferObject.GL_FRAMEBUFFER, frameBuffer);
			}

			ARBFramebufferObject.glFramebufferTexture2D(ARBFramebufferObject.GL_FRAMEBUFFER, attachments[i], textureTarget, textureID[i], 0);
		}

		if(frameBuffer == 0){
			return;
		}

		if(!hasDepth){
			renderBuffer = ARBFramebufferObject.glGenRenderbuffers();
//			glGenRenderbuffers(1, renderBuffer);
			ARBFramebufferObject.glBindRenderbuffer(ARBFramebufferObject.GL_RENDERBUFFER, renderBuffer);
			ARBFramebufferObject.glRenderbufferStorage(ARBFramebufferObject.GL_RENDERBUFFER, GL11.GL_DEPTH_COMPONENT, width, height);
			ARBFramebufferObject.glFramebufferRenderbuffer(ARBFramebufferObject.GL_FRAMEBUFFER, ARBFramebufferObject.GL_DEPTH_ATTACHMENT, ARBFramebufferObject.GL_RENDERBUFFER, renderBuffer);
		}

		GL20.glDrawBuffers(/*numTextures, */drawBuffers);
		
//		glDrawBuffer(GL_NONE);
//		glReadBuffer(GL_NONE);

//		if(glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE){
//			std::cerr << "Framebuffer creation failed!" << std::endl;
//			assert(false);
//		}
		
		if(ARBFramebufferObject.glCheckFramebufferStatus(ARBFramebufferObject.GL_FRAMEBUFFER) != ARBFramebufferObject.GL_FRAMEBUFFER_COMPLETE){
//			System.out.println("not completed...");
			assert(false);
		}

		ARBFramebufferObject.glBindFramebuffer(ARBFramebufferObject.GL_FRAMEBUFFER, 0);
	}
	
	@Override
	public int getID(){
		return textureID[0];
	}
	
	@Override
	public void dispose(){
//		if(textureID != null){
//			GL11.glDeleteTextures(textureID);
			for(int i = 0; i < numTextures; i++){
				GL15.glDeleteBuffers(textureID[i]);
			}
//		}
//		if(frameBuffer != null){
			GL30.glDeleteFramebuffers(frameBuffer);
//		}
//		if(renderBuffer != null){
			GL30.glDeleteRenderbuffers(renderBuffer);
//		}
	}
}