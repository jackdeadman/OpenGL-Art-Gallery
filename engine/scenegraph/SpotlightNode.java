package engine.scenegraph;
import gmaths.*;
import engine.lighting.*;

public class SpotlightNode extends SGNode {

    private Spotlight light;
    private Vec4 startingDirection;
    private Vec4 startingPosition;

    public SpotlightNode(String str, Spotlight light) {
        super(str);
        this.light = light;

        Vec3 direction = light.getDirection();
        // 4d vectors should have 0 for homogeneous coord.
        startingDirection = new Vec4(direction.x, direction.y, direction.z, 0);

        // Points should have 1 for homogeneous coord.
        Vec3 position = light.getPosition();
        startingPosition = new Vec4(position.x, position.y, direction.z, 1);
    }

    protected void update(Mat4 t) {
        super.update(t);

        light.setPosition(
                Mat4.multiply(worldTransform, startingPosition).toVec3());
        light.setDirection(
                Mat4.multiply(worldTransform, startingDirection).toVec3());
    }

}
