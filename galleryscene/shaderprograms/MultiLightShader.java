package galleryscene.shaderprograms;

import com.jogamp.opengl.util.glsl.ShaderProgram;
import engine.Camera;
import engine.WorldConfiguration;
import engine.render.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import com.jogamp.opengl.*;
import gmaths.Mat4;

/**
Generic Multilight shader which additional textures can be
attached to.
*/
public class MultiLightShader extends ShaderConfigurator {

    private class TexturePair {
        public String name;
        public int[] texture;
        public TexturePair(String name, int[] texture) {
            this.name = name;
            this.texture = texture;
        }
    }

    private ArrayList<TexturePair> textures = new ArrayList<>();
    // Optional
    private Material material;

    public MultiLightShader(GL3 gl, String vs, String fs) {
        super(gl, vs, fs);
        material = createDefaultMaterial();
    }

    public MultiLightShader(GL3 gl, String fs) {
        this(gl, "shaders/correct/default.vs.glsl", fs);
    }

    public void addTexture(String name, int[] texture) {
        textures.add(new TexturePair(name, texture));
    }

    public void setMaterial(Material material) {
        this.material = material;
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

        if (material != null) {
            shader.setVec3(gl, "material.ambient", material.getAmbient());
            shader.setVec3(gl, "material.diffuse", material.getDiffuse());
            shader.setVec3(gl, "material.specular", material.getSpecular());
            shader.setFloat(gl, "material.shininess", material.getShininess());
        }

        for (int i=0; i<textures.size(); ++i) {
            TexturePair pair = textures.get(i);

            shader.setInt(gl, pair.name, i);
            gl.glActiveTexture(GL.GL_TEXTURE0 + i);
            gl.glBindTexture(GL.GL_TEXTURE_2D, pair.texture[0]);
        }
    }

    public void dispose(GL3 gl) {
        System.out.println("Disposing");
        for (int i=0; i<textures.size(); ++i) {
            TexturePair pair = textures.get(i);
            gl.glDeleteBuffers(1, pair.texture, 0);
        }
    }
}
