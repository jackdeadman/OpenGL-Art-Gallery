package engine;
import com.jogamp.opengl.*;

public abstract class Model {

    protected Camera camera;
    protected Light light;

    Model(Camera camera, Light light) {
        this.camera = camera;
        this.light = light;
    }

    protected addMesh(String name, Mesh mesh) {
        mesh.setLight(light);
        mesh.setCamera(camera);
    }

    public void reshape(GL3 gl, Mat4 perspective) {
        // Loop and set
    }

    public abstract void draw(GL3 gl);

}
