package models.handparts;
import engine.*;
import engine.render.*;
import engine.scenegraph.*;
import engine.lighting.*;
import galleryscene.shaderprograms.*;
import meshes.*;
import gmaths.*;
import com.jogamp.opengl.*;

public class Ring extends Model {

    private Mesh ring;
    private Spotlight light;

    public Ring(WorldConfiguration worldConfig) {
        super(worldConfig);
        light = new Spotlight(new Vec3(0f, 1f, 0f), new Vec3(0.1f, 0.18f, 0.0112f));
        worldConfig.setSpotlight(light);
    }

    // OpenGL loaded
    protected void start(GL3 gl) {
        System.out.println("Loading ring");
        loadTextures(gl);
        loadMeshes(gl);
        buildSceneGraph(gl);
    }

    private void loadTextures(GL3 gl) {
    }

    private void loadMeshes(GL3 gl) {
        ring = new SphereNew(gl, new SolidColorShader(gl, new Vec3(1f, 1f, 1f)));

        registerMeshes(new Mesh[] { ring });
    }


    private void buildSceneGraph(GL3 gl) {
        System.out.println("Build scene graph......");
        System.out.println(worldConfig.getSpotlight());
        // SpotlightNode lightNode = new SpotlightNode("", light);
        MeshNode ringShape = new MeshNode("", ring);

        SGNode root = new NameNode("Ring");
        // root.addChild(lightNode);
             root.addChild(ringShape);
        root.update();
        setRoot(root);

    }

}
