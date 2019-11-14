package at.dalex.grape.graphics.shader;

import at.dalex.grape.graphics.shader.UniformUtil.UniformLoader;

import static org.lwjgl.opengl.GL20.*;

/**
 * This class was written by dalex on 14.10.2017.
 * You are not permitted to edit this file.
 *
 * @author dalex
 */

public abstract class ShaderProgram {

    public int programID;
    private int vertexShaderID;
    private int fragmentShaderID;

    public abstract void getAllUniformLocations();
    public abstract void bindAttributes();

    private UniformLoader uniformLoader;
    
    public ShaderProgram(String vertexCode, String fragmentCode) {
        vertexShaderID = loadShader(vertexCode, GL_VERTEX_SHADER);
        fragmentShaderID = loadShader(fragmentCode, GL_FRAGMENT_SHADER);

        programID = glCreateProgram();
        glAttachShader(programID, vertexShaderID);
        glAttachShader(programID, fragmentShaderID);
        bindAttributes();
        glLinkProgram(programID);
        glValidateProgram(programID);

        uniformLoader = new UniformLoader(programID);
        
        start();
        getAllUniformLocations();
        stop();
    }

    public void start() {
        glUseProgram(programID);
    }

    public void stop() {
        glUseProgram(0);
    }

    protected void bindAttribute(int attribute, String variableName) {
        glBindAttribLocation(programID, attribute, variableName);
    }

    private static int loadShader(String shaderCode, int type) {
        int shaderID = glCreateShader(type);
        glShaderSource(shaderID, shaderCode);
        glCompileShader(shaderID);
        return shaderID;
    }

    public UniformLoader getUniformLoader() {
    	return this.uniformLoader;
    }
    
    public void cleanUp() {
        stop();
        glDetachShader(programID, vertexShaderID);
        glDetachShader(programID, fragmentShaderID);
        glDeleteShader(vertexShaderID);
        glDeleteShader(fragmentShaderID);
        glDeleteProgram(programID);
    }
}
