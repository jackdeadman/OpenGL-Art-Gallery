package engine.render;
import com.jogamp.opengl.*;
import engine.lighting.*;
import engine.*;

public abstract class SpotLightEmittingModel extends Model {

    protected Spotlight containedLight;

    protected void initialise(GL3 gl) {
        worldConfig.setSpotlight(containedLight);
        super.initialise(gl);
    }

    public Spotlight getContainedLight() {
        return containedLight;
    }

    public void setContainedLight(Spotlight containedLight) {
        this.containedLight = containedLight;
    }

    public SpotLightEmittingModel(WorldConfiguration worldConfig) {
        super(worldConfig);
    }
}
