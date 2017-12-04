package models;

import com.jogamp.opengl.*;

import engine.*;
import engine.render.*;
import engine.utils.*;
import engine.scenegraph.*;
import galleryscene.shaderprograms.*;
import gmaths.*;
import meshes.*;

public class Sky extends Model {

    private final String IMAGE_PATH = "textures/just_sky.jpg";

    private int[] skyTexture;
    private Mesh skyBox;
    private ShaderConfigurator textureProgram;
    private double startTime;

    public Sky(WorldConfiguration worldConfig) {
        super(worldConfig);
        startTime = System.currentTimeMillis();
    }

    protected void start(GL3 gl) {
        loadTextures(gl);
        loadMeshes(gl);
        buildSceneGraph();
    }

    private void loadTextures(GL3 gl) {
        skyTexture = TextureLibrary.loadTexture(gl, IMAGE_PATH,
                        GL.GL_MIRRORED_REPEAT, GL.GL_REPEAT,
                        GL.GL_LINEAR, GL.GL_LINEAR);
    }

    private void loadMeshes(GL3 gl) {
        textureProgram = new SkyBoxShader(gl, skyTexture);
        // textureProgram = new OneTextureShader(gl, skyTexture);
        // Change to a skybox shader
        skyBox = new TwoTrianglesNew(gl, textureProgram);
        skyBox.getShaderProgram().getShader().setFloat(gl, "offset", 0f);
        registerMesh(skyBox);
    }

    protected void onBeforeRender(GL3 gl) {
        double offset = ((System.currentTimeMillis()) - startTime);
        System.out.println(offset);
        offset = ((offset / 2.0) / 10000) % 2;
        System.out.println(offset );
        Shader shader = skyBox.getShaderProgram().getShader();
        shader.use(gl);
        shader.setFloat(gl, "offset", (float) offset);
    }

    private void buildSceneGraph() {
        MeshNode skyBoxShape = new MeshNode("TwoTriangles (SkyBox)", skyBox);

        TransformNode transformFrame = new TransformNode("",
            Mat4.multiplyVariable(
                Mat4Transform.translate(0f, 0f, -40f),
                Mat4Transform.rotateAroundX(90f),
                // Move it far away
                Mat4Transform.scale(30f, 30f, 30f)
            )
        );

        // Build graph
        SGNode root = new NameNode("Sky");
        root.addChild(transformFrame);
            transformFrame.addChild(skyBoxShape);
        root.update();
        setRoot(root);
    }
}
