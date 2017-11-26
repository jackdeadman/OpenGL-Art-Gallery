package engine;
import gmaths.*;

public class SpotlightNode extends SGNode {

    private Spotlight light;
    private Vec4 worldPosition;
    private Vec4 startingDirection;

    public SpotlightNode(String str, Spotlight light) {
        super(str);
        this.light = light;

        Vec3 direction = light.getDirection();
        // 4d vectors should have 0 for homogeneous coord
        startingDirection = new Vec4(direction.x, direction.y, direction.z, 0);
    }

    protected void update(Mat4 t) {
        super.update(t);
        Vec4 worldPoint = Mat4.multiply(worldTransform, new Vec4());
        light.setPosition(worldPoint.toVec3());
        System.out.println("startingDirection");
        System.out.println(Mat4.multiply(worldTransform, startingDirection).toVec3());
        light.setDirection(
            Mat4.multiply(worldTransform, startingDirection).toVec3());
    }

}
