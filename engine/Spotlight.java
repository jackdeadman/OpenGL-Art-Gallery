package engine;
import gmaths.*;
import com.jogamp.common.nio.*;
import com.jogamp.opengl.*;

public class Spotlight {

  private Vec4 position;
  private Vec3 colour;
  private Vec3 attenuation;
  private Vec3 direction;
  private Material material;

  public Spotlight(Vec3 direction, Vec3 attenuation) {
    position = new Vec4(0f, 1f, 1f, 1f);
    this.colour = new Vec3(1.0f, 0.0f, 0.0f);
    this.attenuation = attenuation;
    this.direction = direction;

    material = new Material();
    material.setAmbient(0.2f, 0.2f, 0.2f);
    material.setDiffuse(0.2f, 0.2f, 0.2f);
    material.setSpecular(0.2f, 0.2f, 0.2f);
  }

  public float getCutOff() {
      return (float) Math.cos(Math.toRadians(12.5));
  }

  public float getOuterCutOff() {
      return (float) Math.cos(Math.toRadians(17.5));
  }

  public Vec3 getDirection() {
      return direction;
  }

  public void setDirection(Vec3 direction) {
      this.direction = direction;
  }

  public Material getMaterial() {
      return material;
  }

  public float getConstant() {
      return attenuation.x;
  }

  public float getLinear() {
      return attenuation.y;
  }

  public float getQuadratic() {
      return attenuation.z;
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

  public void setAttenuation(Vec3 v) {
    colour.x = v.x;
    colour.y = v.y;
    colour.z = v.z;
  }

  public void setColour(float x, float y, float z) {
    colour.x = x;
    colour.y = y;
    colour.z = z;
  }

  public Vec3 getPosition() {
    // Abstract implementation detail that the
    // position is stored as a vec4.
    return position.toVec3();
  }

  public Vec3 getColour() {
    return colour;
  }

  public Vec3 getAttenuation () {
    return attenuation;
  }

}
