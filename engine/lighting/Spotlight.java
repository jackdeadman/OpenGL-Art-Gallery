package engine.lighting;
import gmaths.*;
import engine.render.*;

// A spotlight is a specific version of a pointlight
public class Spotlight extends PointLight {
    /**
    * @author Jack Deadman
    */

    private Vec3 direction;

    // Should be in radians
    private float cutOff, outerCutOff;

    public Spotlight(Material material, Vec3 attenuation) {
        super(material, attenuation);
    }

    public void setCutOff(float cutOff) {
        this.cutOff = cutOff;
    }

    public void setOuterCutOff(float outerCutOff) {
        this.outerCutOff = outerCutOff;
    }

    public float getCutOff() {
      return cutOff;
  }
    public float getOuterCutOff() {
      return outerCutOff;
  }

    public Vec3 getDirection() {
      return direction;
  }
    public void setDirection(Vec3 direction) {
      this.direction = direction;
  }

}
