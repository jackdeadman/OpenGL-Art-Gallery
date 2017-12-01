package galleryscene.shaderprograms;

import engine.*;
import engine.render.*;
import gmaths.*;
import com.jogamp.opengl.*;


public class OneTextureShader extends ShaderConfigurator {

    protected Material material;
    protected int[] texture;

    public OneTextureShader(GL3 gl, int[] texture, Material material) {
        super(gl, "shaders/correct/default.vs.glsl", "shaders/correct/one_texture.fs.glsl");
        this.material = material;
        this.texture = texture;
    }

    public OneTextureShader(GL3 gl, int[] texture) {
        this(gl, texture, null);
        this.material = createDefaultMaterial();
    }

    public void sendSendDataToTheGPU(GL3 gl, Mat4 perspective, Mat4 model, WorldConfiguration worldConfig) {
        shader.use(gl);

        Camera camera = worldConfig.getCamera();
        Mat4 mvpMatrix = Mat4.multiply(perspective,
            Mat4.multiply(camera.getViewMatrix(), model));


        shader.setFloatArray(gl, "model", model.toFloatArrayForGLSL());
        shader.setFloatArray(gl, "mvpMatrix", mvpMatrix.toFloatArrayForGLSL());
        shader.setVec3(gl, "viewPos", camera.getPosition());

        setupShaderForMultipleLighting(gl, worldConfig);

        shader.setVec3(gl, "material.ambient", material.getAmbient());
        shader.setVec3(gl, "material.diffuse", material.getDiffuse());
        shader.setVec3(gl, "material.specular", material.getSpecular());
        shader.setFloat(gl, "material.shininess", material.getShininess());

        shader.setInt(gl, "main_texture", 0);
        gl.glActiveTexture(GL.GL_TEXTURE0);
        gl.glBindTexture(GL.GL_TEXTURE_2D, texture[0]);

    }

    public void dispose(GL3 gl) {
        gl.glDeleteBuffers(1, texture, 0);
    }
}
