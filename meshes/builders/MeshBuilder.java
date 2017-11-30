package meshes.builders;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;
import engine.*;
import engine.WorldConfiguration;
import gmaths.*;
import meshes.*;


/**

import java.util.ArrayList;
Helper class for creating meshes as the parameter list
is very long and often the default value is good enough

Example usage exaggerated usage...

Mesh backWall = new MeshBuilder(gl, TwoTriangles.class)
                .vertexShader(...)
                .fragmentShader(...)
                .multiLighting(true)
                .textures({ ..., ..., ... })
                .specularMaps({ ..., ..., ... })
                .addDiffuseMap({ ..., ..., ... })
                .addNormalMap({ ..., ..., ... })
                .build();


TwoTriangles back = new MeshBuilder<TwoTriangles>()
                    .

*
*/

public abstract class MeshBuilder {

    protected GL3 gl;

    // DEFAULTS
    protected String vertexShader = "shaders/vs_tt_05.txt";
    protected String fragmentShader = "shaders/fs_tt_05.txt";
    protected boolean useMultiLighting = true;
    protected boolean useSkyBoxViewMatrix = false;
    protected int[][] textures = new int[][] { new int[] {}};
    protected int[][] specularMaps = new int[][] { new int[] {}};
    protected int[][] diffuseMaps = new int[][] { new int[] {}};
    protected int[][] normalMaps = new int[][] { new int[] {}};

    public MeshBuilder(GL3 gl) {
        this.gl = gl;
    }

    public MeshBuilder vertexShader(String vertexShader) {
        this.vertexShader = vertexShader;
        return this;
    }

    public MeshBuilder fragmentShader(String fragmentShader) {
        this.fragmentShader = fragmentShader;
        return this;
    }

    public MeshBuilder useMultiLighting(boolean useMultiLighting) {
        this.useMultiLighting = useMultiLighting;
        return this;
    }

    public MeshBuilder textures(int[][] textures) {
        this.textures = textures;
        return this;
    }

    public MeshBuilder texture(int[] texture) {
        this.textures = new int[][] { texture };
        return this;
    }

    public MeshBuilder specularMaps(int[][] specularMaps) {
        this.specularMaps = specularMaps;
        return this;
    }

    public MeshBuilder specularMap(int[] specular) {
        this.specularMaps = new int[][] { specular };
        return this;
    }

    public MeshBuilder diffuseMaps(int[][] diffuseMaps) {
        this.diffuseMaps = diffuseMaps;
        return this;
    }

    public MeshBuilder diffuseMap(int[] texture) {
        this.textures = new int[][] { texture };
        return this;
    }

    public MeshBuilder normalMaps(int[][] normalMaps) {
        this.normalMaps = normalMaps;
        return this;
    }

    public MeshBuilder normalMaps(int[] normalMap) {
        this.normalMaps = new int[][] { normalMap };
        return this;
    }

    public abstract MeshImproved build();
}
