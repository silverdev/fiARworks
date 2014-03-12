
package com.slsw.fiarworks.firework;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.slsw.fiarworks.R;

/*
 * 
 */
public class GLFirework
{
	private final String vertexShaderCode =
		// This matrix member variable provides a hook to manipulate
		// the coordinates of the objects that use this vertex shader
		"uniform mat4 uMVPMatrix;" +
		"attribute vec2 vTexturePosIn;" +
		"attribute vec3 vPositionIn;" +
		"varying vec2 vTexturePos;" +
		"void main() {" +
		// the matrix must be included as a modifier of gl_Position
		"  vTexturePos = vTexturePosIn;" +
		"  vec4 position = vec4(vPositionIn, 1.0);" +
		"  gl_Position = uMVPMatrix * position;" +
		"}";

	private final String fragmentShaderCode =
		"precision mediump float;" +
		"uniform sampler2D u_TextureMask;" +
		"uniform sampler2D u_AlphaTexture;" +
		"uniform vec2 u_WindowSize;" +
		"varying vec2 vTexturePos;" +
		"void main() {" +
		"  if(texture2D(u_TextureMask, gl_FragCoord.xy / u_WindowSize.xy).b > 0.5) {" +
        "    discard;" + 
        "  }" +
		// "  gl_FragColor = vec4(1.0, 1.0, 0.0, 1.0);" +
		"  gl_FragColor = vec4(texture2D(u_AlphaTexture, vTexturePos).rgb, 1.0);" +
		"}";

	static final int POSITION_COORDS_PER_VERTEX = 3;
	static final int BYTES_PER_FLOAT = 4;
	static final int GEOMETERY_PER_VERTEX = 3;
	static final int VERTEX_PER_SPARK = 6;
	static final int MAX_SPARKS = 1000;

	final int[] textureHandle = new int[2];
	static int mPosShaderLoc;
	static int mTexShaderLoc;
	static int mColShaderLoc;
	static int mMVPMatrixLoc;
	static int mWindowSizeLoc;
	static int mTextureMaskUniformLoc;
	static int mAlphaTextureUniformLoc;

	static int mNumGeometryFloats;
	static int mNumTexGeometryFloats;
	static int mProgram;

	static int width;
	static int height;


	Bitmap spark_tex;
	ByteBuffer mByteBuffer;

	GLFirework(Context c)
	{
		int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
		int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

		mProgram = GLES20.glCreateProgram();             // create empty OpenGL Program
		GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
		GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
		GLES20.glLinkProgram(mProgram);                  // create OpenGL program executables
		//error checking
		{
			mPosShaderLoc = GLES20.glGetAttribLocation(mProgram, "vPositionIn");
			if(mPosShaderLoc == -1)
			{
				System.err.println("mPosShaderLoc is -1. This is bad.");
				
			}
			mTexShaderLoc = GLES20.glGetAttribLocation(mProgram, "vTexturePosIn");
			if(mTexShaderLoc == -1)
			{
				System.err.println("vTexturePosIn is -1. This is bad.");
			}
			mMVPMatrixLoc = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
			if(mMVPMatrixLoc == -1)
			{
				System.err.println("mMVPMatrixLoc is -1. This is bad.");
			}
			mTextureMaskUniformLoc = GLES20.glGetUniformLocation(mProgram, "u_TextureMask");
			if(mTextureMaskUniformLoc == -1)
			{
				System.err.println("mTextureMaskUniformLocB is -1. This is bad.");
			}
			mAlphaTextureUniformLoc = GLES20.glGetUniformLocation(mProgram, "u_AlphaTexture");
			if(mAlphaTextureUniformLoc == -1)
			{
				System.err.println("mAlphaTextureUniformLoc is -1. This is bad.");
			}
			mWindowSizeLoc = GLES20.glGetUniformLocation(mProgram, "u_WindowSize");
			if(mWindowSizeLoc == -1)
			{
				System.err.println("mWindowSizeLoc is -1. This is bad.");
			}
		}

		GLES20.glGenTextures(2, textureHandle, 0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[1]);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);

		//allocate a big buffer
		//we'll change the contents every frame
		mByteBuffer = ByteBuffer.allocateDirect(MAX_SPARKS * VERTEX_PER_SPARK * GEOMETERY_PER_VERTEX * BYTES_PER_FLOAT);
		mByteBuffer.order(ByteOrder.nativeOrder());
		// GLES20.glGenBuffers(1, mGeometryBufferHandle, 0);

		WindowManager wm = (WindowManager) c.getSystemService(c.WINDOW_SERVICE);	
		width = wm.getDefaultDisplay().getWidth();
		height = wm.getDefaultDisplay().getHeight();

		spark_tex = BitmapFactory.decodeResource(c.getResources(), R.drawable.particle);

	}

	static int printed = 0;
	public void updateFireworkAndDraw(float[] geometry, float[] tex_coord_geometry, float[] MVPMatrix, Bitmap mask)
	{
		//bind the mask as a texture
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);
 		GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, mask, 0);

 		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[1]);
 		GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, spark_tex, 0);

		//fill geometry buffer

		mNumGeometryFloats = geometry.length;
		int numGeometryBytes = mNumGeometryFloats * 4;

		FloatBuffer geometryBuffer = mByteBuffer.asFloatBuffer();

		geometryBuffer.put(geometry);
		geometryBuffer.position(0);

		//fill tex coord geometry buffer
		mNumTexGeometryFloats = tex_coord_geometry.length;
		int numTexGeometryBytes = mNumTexGeometryFloats * 4;

		FloatBuffer texGeometryBuffer = mByteBuffer.asFloatBuffer();

		texGeometryBuffer.put(tex_coord_geometry);
		texGeometryBuffer.position(0);


		// GLES20.glEnable();
		GLES20.glUseProgram(mProgram);

		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);
		GLES20.glUniform1i(mTextureMaskUniformLoc, 0);

		GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[1]);
		GLES20.glUniform1i(mAlphaTextureUniformLoc, 1);

		GLES20.glUniformMatrix4fv(mMVPMatrixLoc, 1, false, MVPMatrix, 0);

		float[] screenCoords = {(float)width, (float)height};
		GLES20.glUniform2fv(mWindowSizeLoc, 1, screenCoords, 0);

		GLES20.glEnableVertexAttribArray(mPosShaderLoc);
		GLES20.glVertexAttribPointer(   mPosShaderLoc, 3,
										GLES20.GL_FLOAT, false,
										3*4, geometryBuffer);

		GLES20.glEnableVertexAttribArray(mTexShaderLoc);
		GLES20.glVertexAttribPointer(   mTexShaderLoc, 2,
										GLES20.GL_FLOAT, false,
										2*4, texGeometryBuffer);


		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, mNumGeometryFloats / GEOMETERY_PER_VERTEX);


		GLES20.glDisableVertexAttribArray(mPosShaderLoc);
		GLES20.glDisableVertexAttribArray(mTexShaderLoc);
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

