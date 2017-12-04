package engine.render;
import com.jogamp.opengl.*;
import engine.lighting.*;
import engine.*;

public abstract class SpotLightEmittingModel extends Model {

    /* Assume the model emits a point light */
    protected PointLight containedLight;

    protected void initialise(GL3 gl) {
        worldConfig.addPointLight(containedLight);
        super.initialise(gl);
    }

    public Light getContainedLight() {
        return containedLight;
    }

    public void setContainedLight(PointLight containedLight) {
        this.containedLight = containedLight;
    }

    public SpotLightEmittingModel(WorldConfiguration worldConfig) {
        super(worldConfig);
    }
}
