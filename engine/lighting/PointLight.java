package engine.lighting;
import gmaths.*;
import engine.render.*;

public class PointLight implements Light {

  private Vec4 position;
  private Vec3 colour;
  private Vec3 attenuation;
  private Material material;
  private Material onMaterial;
  private Material offMaterial;

  public PointLight(Material material, Vec3 attenuation) {
    position = new Vec4(0f, 0f, 0f, 1f);

    this.attenuation = attenuation;

    offMaterial = new Material();
    offMaterial.setAmbient(0.0f, 0.0f, 0.0f);
    offMaterial.setDiffuse(0.0f, 0.0f, 0.0f);
    offMaterial.setSpecular(0.0f, 0.0f, 0.0f);

    this.material = material;
    this.onMaterial = material;;
  }

  public Material getMaterial() {
      return material;
  }

  public void set(boolean isOn) {
      material = isOn ? onMaterial : offMaterial;
  }

  public void setMaterial(Material mat) {
      material = mat;
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
