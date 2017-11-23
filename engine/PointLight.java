package engine;
import gmaths.*;
import
java.nio.*;
import com.jogamp.common.nio.*;
import com.jogamp.opengl.*;

public class PointLight {

  private Vec4 position;
  private Vec3 colour;
  private Vec3 attenuation;

  public Light(GL3 gl, Vec3 colour, Vec3 attenuation) {
    position = new Vec4(0f, 0f, 0f, 1f);
    this.colour = colour;
    this.attenuation = attenuation;
  }

  public void setPosition(Vec3 v) {
    position.x = v.x;
    position.y = v.y;
    position.z = v.z;
  }

  public void setPosition(float x, float y, float z) {
    position.x = x;
    position.y = y;
    position.z = z;
  }

  public void setColour(Vec3 v) {
    colour.x = v.x;
    colour.y = v.y;
    colour.z = v.z;
  }

  public void setColour(float x, float y, float z) {
    colour.x = x;
    colour.y = y;
    colour.z = z;
  }

  public Vec4 getPosition() {
    return position;
  }

  public Vec3 getColour() {
    return colour;
  }

}
