package meshes.builders;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;
import engine.*;
import engine.WorldConfiguration;
import gmaths.*;
import meshes.*;

public class TwoTrianglesBuilder extends MeshBuilder {

    public TwoTrianglesBuilder(GL3 gl) {
        super(gl);
    }

    public MeshImproved build() {
        return new TwoTrianglesImproved(gl, vertexShader, fragmentShader,
            useMultiLighting, new Material(), textures, specularMaps,
            diffuseMaps, normalMaps);
    }

}
