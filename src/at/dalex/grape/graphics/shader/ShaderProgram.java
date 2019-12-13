package at.dalex.grape.graphics.shader;

import at.dalex.grape.graphics.shader.UniformUtil.UniformLoader;
import at.dalex.grape.resource.FileContentReader;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL43.GL_COMPUTE_SHADER;

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
        createShader(
                new ShaderInfo(GL_VERTEX_SHADER,    vertexFile),
                new ShaderInfo(GL_FRAGMENT_SHADER,  fragmentFile)
        );
    }

    public ShaderProgram(String computeFile) {
        createShader(new ShaderInfo(GL_COMPUTE_SHADER, computeFile));
    }

    protected void createShader(ShaderInfo... shaderInfos) {
        System.out.println("\n------ Creating new Shader: " + this.getClass().getSimpleName());
        programID = glCreateProgram();
        for (ShaderInfo shaderInfo : shaderInfos) {
            int shaderId = loadShader(shaderInfo.shaderFile, shaderInfo.shaderType);
            glAttachShader(programID, shaderId);
        }

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
        shaderCode = appendGLSLConstants(shaderCode);
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

    private String appendGLSLConstants(String shaderCode) {
        //Pre-Multiplied constants to reduce calculations in the shaders
        ArrayList<String> shaderConstants = new ArrayList<>();
        shaderConstants.add("#version 460");
        shaderConstants.add("#define MATH_PI 3.1415926535897932384626433832795");
        shaderConstants.add("#define MATH_HALF_PI 1.57079632679489661923132169163975");
        shaderConstants.add("#define MATH_2_PI 6.283185307179586476925286766559");

        StringBuilder stringBuilder = new StringBuilder();
        for (String constant : shaderConstants) {
            stringBuilder.append(constant);
            stringBuilder.append("\n");
        }
        stringBuilder.append(shaderCode);
        return stringBuilder.toString();
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

    private static class ShaderInfo {

        //Public members for easy access
        //Unable to be changed from outside, as they're constants anyway.
        final String shaderFile;
        final int shaderType;

        ShaderInfo(int shaderType, String shaderFile) {
            this.shaderType = shaderType;
            this.shaderFile = shaderFile;
        }
    }
}
