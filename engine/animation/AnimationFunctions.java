package engine.animation;

public class AnimationFunctions {

    public static AnimationFunction linear() {
        return new AnimationFunction() {
            public float run(float t) {
                return t;
            }
        };
    }
/*
    public static AnimationFunction sin() {
        return new AnimationFunction() {
            run(float t) {
                return Math.sin(t);
            }
        };
    }

    public static AnimationFunction cos() {
        return new AnimationFunction() {
            run(float t) {
                return Math.cos(t);
            }
        };
    }

    public static AnimationFunction cubicBezier() {
        return new AnimationFunction() {
            run(float t) {
                return Math.cos(t);
            }
        };
    }
*/
}
