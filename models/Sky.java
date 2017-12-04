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

    public final String IMAGE_PATH = "textures/final/sky.jpg";
    public final double ANIMATION_SPEED = 0.05;

    private Mesh skyBox;
    private int[] skyTexture;
    private ShaderConfigurator textureProgram;

    // Timing logic is done here as well as in the main animation engine as
    // they are completely independent i.e, when you pause the animation
    // time still passes in the world.
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
        // Have the texture nicely repeat. Need to mirror as
        // the texure does not naturally repeat nicely.
        skyTexture = TextureLibrary.loadTexture(gl, IMAGE_PATH,
                        GL.GL_MIRRORED_REPEAT, GL.GL_REPEAT,
                        GL.GL_LINEAR, GL.GL_LINEAR);
    }

    private void updateSkyOffset(GL3 gl, float offset) {
        Shader shader = skyBox.getShaderProgram().getShader();
        shader.use(gl);
        shader.setFloat(gl, "offset", offset);
    }

    private void loadMeshes(GL3 gl) {
        // Special shader which gives the illusion the sky is
        // really far away.
        textureProgram = new SkyBoxShader(gl, skyTexture);
        skyBox = new TwoTrianglesNew(gl, textureProgram);

        // Default the offset to 0 at the start, just in case.
        updateSkyOffset(gl, 0.0f);
        registerMesh(skyBox);
    }

    protected void onBeforeRender(GL3 gl) {
        double offset = ((System.currentTimeMillis()) - startTime);
        // Mod 2 because we want to use the mirrored wrap too
        offset = ((offset * ANIMATION_SPEED) / 1000) % 2;
        updateSkyOffset(gl, (float) offset);
    }

    private void buildSceneGraph() {
        MeshNode skyBoxShape = new MeshNode("TwoTriangles (SkyBox)", skyBox);

        TransformNode transformFrame = new TransformNode(
            "Scale(30f, 30f, 30f); rotateAroundX(90 degrees); Translate(0f, 0f, -40f)",
            Mat4.multiplyVariable(
                // Move it far away from screen
                Mat4Transform.translate(0f, 0f, -40f),
                Mat4Transform.rotateAroundX(90f),
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
