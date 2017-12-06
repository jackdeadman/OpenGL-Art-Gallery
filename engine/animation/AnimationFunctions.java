package engine.animation;

// Collection of animation functions
public class AnimationFunctions {
    /**
    * @author Jack Deadman
    */
    // All the functions assume an input value t between 0 and 1

    public static AnimationFunction linear() {
        return new AnimationFunction() {
            public float run(float t) {
                return t;
            }
        };
    }

    public static AnimationFunction sin() {
        return new AnimationFunction() {
            public float run(float t) {
                return (float)Math.sin(t * Math.PI / 2.0f);
            }
        };
    }

    public static AnimationFunction cos() {
        return new AnimationFunction() {
            public float run(float t) {
                double t2 = (Math.PI/2.0 - (t/2 * Math.PI));
                return (float)Math.cos(t2);
            }
        };
    }
}
