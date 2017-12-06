package engine.lighting;

import engine.render.Material;

public abstract class Light {
    /**
    * @author Jack Deadman
    */
    private Material onMaterial, offMaterial;
    private boolean isOn = true;

    public Light(Material material) {
        this.onMaterial = material;
        this.offMaterial = createOffMaterial();
    }

    private Material createOffMaterial() {
        Material material = new Material();
        material.setAmbient(0.0f, 0.0f, 0.0f);
        material.setDiffuse(0.0f, 0.0f, 0.0f);
        material.setSpecular(0.0f, 0.0f, 0.0f);
        return material;
    }

    public Material getMaterial() {
        return isOn ? onMaterial : offMaterial;
    }

    public void set(boolean isOn) {
        this.isOn = isOn;
    }

    public void setMaterial(Material material) {
        onMaterial = material;
    }

}
