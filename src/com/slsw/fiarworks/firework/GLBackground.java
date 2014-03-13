package com.slsw.fiarworks.firework;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;

public class GLBackground
{
	//Background
	private final String vertexShaderCodeA =
        "attribute vec2 a_Position;" +
        "attribute vec2 a_TexCoordinate;" +
        "varying vec2 v_TexCoordinate;" +
        "void main() {" +
        "  vec4 position = vec4(a_Position, -0.9, 1.0);" +
        "  v_TexCoordinate = a_TexCoordinate;" +
        "  gl_Position = position;" +
        "}";

    private final String fragmentShaderCodeA =
        "precision mediump float;" +
        "uniform sampler2D u_TextureCam;" +
        "varying vec2 v_TexCoordinate;" +
        "void main() {" +
        "  gl_FragColor = texture2D(u_TextureCam, v_TexCoordinate);" +
        "}";

    //foreground
	private final String vertexShaderCodeB =
        "attribute vec2 a_Position;" +
        "attribute vec2 a_TexCoordinate;" +
        "varying vec2 v_TexCoordinate;" +
        "void main() {" +
        "  vec4 position = vec4(a_Position, 0.0, 1.0);" +
        "  v_TexCoordinate = a_TexCoordinate;" +
        "  gl_Position = position;" +
        "}";

    private final String fragmentShaderCodeB =
        "precision mediump float;" +
        "uniform sampler2D u_TextureCam;" +
        "uniform sampler2D u_TextureMask;" +
        "varying vec2 v_TexCoordinate;" +
        "void main() {" +
        "  if(texture2D(u_TextureMask, v_TexCoordinate).b > 0.5) {" +
        "    discard;" + 
        "  }" +
        "  gl_FragColor = texture2D(u_TextureCam, v_TexCoordinate);" +
        // "  gl_FragColor = vec4(1.0, 1.0, 1.0, 1.0);" +
        "}";

    static final float[] screenPos = {	-1.0f, -1.0f,
	   									-1.0f,  1.0f,
	   									 1.0f, -1.0f,
	   									 1.0f,  1.0f,
	   									 1.0f, -1.0f,
	   									-1.0f,  1.0f,
	   									};

	static final float[] screenTex = {	0.0f, 1.0f,
	   									0.0f,  0.0f,
	   									 1.0f, 1.0f,
	   									 1.0f,  0.0f,
	   									 1.0f, 1.0f,
	   									0.0f,  0.0f,
	   									};

    static int[] mfloatBufferPosHandle = new int[1];
    final int[] textureHandle = new int[2];
	static int mPosShaderLocA;
    static int mTexShaderLocA;
    static int mTextureCamUniformLocA;
    static int mTextureMaskUniformLocA;
	static int mProgramA;
    static int mPosShaderLocB;
    static int mTexShaderLocB;
    static int mTextureCamUniformLocB;
    static int mTextureMaskUniformLocB;
	static int mProgramB;

    static int mNumGeometryFloats; 


	ByteBuffer mByteBufferPos;
	ByteBuffer mByteBufferTex;

	GLBackground()
	{
		int vertexShaderA = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCodeA);
        int fragmentShaderA = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCodeA);

		int vertexShaderB = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCodeB);
        int fragmentShaderB = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCodeB);

		mProgramA = GLES20.glCreateProgram();             // create empty OpenGL Program
        GLES20.glAttachShader(mProgramA, vertexShaderA);   // add the vertex shader to program
        GLES20.glAttachShader(mProgramA, fragmentShaderA); // add the fragment shader to program
        GLES20.glLinkProgram(mProgramA);                  // create OpenGL program executables

        //error checking
        {
			mPosShaderLocA = GLES20.glGetAttribLocation(mProgramA, "a_Position");
			if(mPosShaderLocA == -1)
			{

				System.err.println("mPosShaderLocA is -1. This is bad.");

			}
			mTexShaderLocA = GLES20.glGetAttribLocation(mProgramA, "a_TexCoordinate");
			if(mTexShaderLocA == -1)
			{

				System.err.println("mTexShaderLocA is -1. This is bad.");
			}
			mTextureCamUniformLocA = GLES20.glGetUniformLocation(mProgramA, "u_TextureCam");
			if(mTextureCamUniformLocA == -1)
			{

				System.err.println("mTextureCamUniformLocA is -1. This is bad.");

			}
		}

		mProgramB = GLES20.glCreateProgram();             // create empty OpenGL Program
        GLES20.glAttachShader(mProgramB, vertexShaderB);   // add the vertex shader to program
        GLES20.glAttachShader(mProgramB, fragmentShaderB); // add the fragment shader to program
        GLES20.glLinkProgram(mProgramB);                  // create OpenGL program executables

		{
			mPosShaderLocB = GLES20.glGetAttribLocation(mProgramB, "a_Position");
			if(mPosShaderLocB == -1)
			{

				System.err.println("mPosShaderLocB is -1. This is bad.");
			}
			mTexShaderLocB = GLES20.glGetAttribLocation(mProgramB, "a_TexCoordinate");
			if(mTexShaderLocB == -1)
			{
				System.err.println("mTexShaderLocB is -1. This is bad.");
			}
			mTextureCamUniformLocB = GLES20.glGetUniformLocation(mProgramB, "u_TextureCam");
			if(mTextureCamUniformLocB == -1)
			{
				System.err.println("mTextureCamUniformLocB is -1. This is bad.");
			}
			mTextureMaskUniformLocB = GLES20.glGetUniformLocation(mProgramB, "u_TextureMask");
			if(mTextureMaskUniformLocB == -1)
			{
				System.err.println("mTextureMaskUniformLocB is -1. This is bad.");
			}
		}

		GLES20.glGenTextures(2, textureHandle, 0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[1]);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);

		mByteBufferPos = ByteBuffer.allocateDirect(12 * 4);
		mByteBufferPos.order(ByteOrder.nativeOrder());
		mByteBufferTex = ByteBuffer.allocateDirect(12 * 4);
		mByteBufferTex.order(ByteOrder.nativeOrder());


	}

	public void draw_background(Bitmap camera_image, Bitmap mask)
	{
		
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
 		GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, camera_image, 0);

 		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[1]);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
 		GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, mask, 0);

 		mNumGeometryFloats = screenPos.length;
		int numGeometryBytes = mNumGeometryFloats * 4;

		FloatBuffer floatBufferPos = mByteBufferPos.asFloatBuffer();
		floatBufferPos.put(screenPos);
		floatBufferPos.position(0);

		FloatBuffer floatBufferTex = mByteBufferTex.asFloatBuffer();
		floatBufferTex.put(screenTex);
		floatBufferTex.position(0);
		{
			GLES20.glUseProgram(mProgramA);

			GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);
			GLES20.glUniform1i(mTextureCamUniformLocA, 0);

			GLES20.glEnableVertexAttribArray(mPosShaderLocA);
			GLES20.glVertexAttribPointer(	mPosShaderLocA, 2,
											GLES20.GL_FLOAT, false,
											2*4, floatBufferPos);

			GLES20.glEnableVertexAttribArray(mTexShaderLocA);
			GLES20.glVertexAttribPointer(	mTexShaderLocA, 2,
											GLES20.GL_FLOAT, false,
											2*4, floatBufferTex);

			GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, mNumGeometryFloats / 2);

			GLES20.glDisableVertexAttribArray(mPosShaderLocA);
			GLES20.glDisableVertexAttribArray(mTexShaderLocA);
		}
	}
	public void draw_foreground(Bitmap camera_image, Bitmap mask)
	{

		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
 		GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, camera_image, 0);

 		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[1]);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
 		GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, mask, 0);

 		mNumGeometryFloats = screenPos.length;
		int numGeometryBytes = mNumGeometryFloats * 4;

		FloatBuffer floatBufferPos = mByteBufferPos.asFloatBuffer();
		floatBufferPos.put(screenPos);
		floatBufferPos.position(0);

		FloatBuffer floatBufferTex = mByteBufferTex.asFloatBuffer();
		floatBufferTex.put(screenTex);
		floatBufferTex.position(0);

		{
			GLES20.glUseProgram(mProgramB);

			GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);
			GLES20.glUniform1i(mTextureCamUniformLocB, 0);

			GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[1]);
			GLES20.glUniform1i(mTextureMaskUniformLocB, 1);

			GLES20.glEnableVertexAttribArray(mPosShaderLocB);
			GLES20.glVertexAttribPointer(	mPosShaderLocB, 2,
											GLES20.GL_FLOAT, false,
											2*4, floatBufferPos);

			GLES20.glEnableVertexAttribArray(mTexShaderLocB);
			GLES20.glVertexAttribPointer(	mTexShaderLocB, 2,
											GLES20.GL_FLOAT, false,
											2*4, floatBufferTex);

			GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, mNumGeometryFloats / 2);

			GLES20.glDisableVertexAttribArray(mPosShaderLocB);
			GLES20.glDisableVertexAttribArray(mTexShaderLocB);
		}
	}



	public static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        int[] isCompiled = new int[1];


        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, isCompiled, 0);
        if(isCompiled[0] == 0)
		{
			System.out.println("Shader did not compile");
        }
        
        return shader;
    }
}