package at.dalex.grape.graphics.postprocessing;

import at.dalex.grape.graphics.shader.ShaderProgram;

public abstract class PostProcessor {

    private ShaderProgram shaderProgram;
    private boolean isEnabled;

    public PostProcessor(ShaderProgram shaderProgram) {
        this.shaderProgram = shaderProgram;
    }

    public ShaderProgram getShaderProgram() {
        return shaderProgram;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }
}
