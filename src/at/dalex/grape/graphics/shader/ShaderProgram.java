package at.dalex.grape.graphics.shader;

import at.dalex.grape.graphics.shader.UniformUtil.UniformLoader;
import at.dalex.grape.info.Logger;
import at.dalex.grape.resource.FileContentReader;

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

    public ShaderProgram(String vertexFile, String fragmentFile) {
        System.out.println("\n------ Creating new Shader: " + this.getClass().getSimpleName());
        vertexShaderID = loadShader(vertexFile, GL_VERTEX_SHADER);
        fragmentShaderID = loadShader(fragmentFile, GL_FRAGMENT_SHADER);

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

    private int loadShader(String shaderSource, int type) {
        String shaderCode = FileContentReader.readFile(shaderSource);
        String shaderType = (type == GL_VERTEX_SHADER ? "[vsh]" : "[fsh]");
        System.out.printf("%s ==> Shader Source: %-50s", shaderType, shaderSource);

        int shaderID = glCreateShader(type);
        glShaderSource(shaderID, shaderCode);
        glCompileShader(shaderID);
        if (glGetShaderi(shaderID, GL_COMPILE_STATUS) == GL_FALSE) {
            System.out.printf("FAIL");

            System.err.println("\n\nAn error occured while compiling the shader!");
            System.err.println("Error-Log: \n" + glGetShaderInfoLog(shaderID));
        }
        else System.out.printf("SUCCESS\n");
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
