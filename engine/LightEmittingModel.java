package engine;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

public abstract class LightEmittingModel extends Model {
    /* Assume the model emits a point light */
    protected PointLight containedLight;

    protected void initialise(GL3 gl) {
        worldConfig.addPointLight(containedLight);
        super.initialise(gl);
    }

    public PointLight getContainedLight() {
        return containedLight;
    }

    public void setContainedLight(PointLight containedLight) {
        this.containedLight = containedLight;
    }

    public LightEmittingModel(WorldConfiguration worldConfig) {
        super(worldConfig);
    }
}
