package scene;
import scene.handpositions.*;
import engine.*;

public class TimelineManager {

    private Timeline<HandConfiguration> fullAnimation;

    private Timeline<HandConfiguration> letterA = (new LetterA()).getTimeline();
    private Timeline<HandConfiguration> letterC = (new LetterC()).getTimeline();
    private Timeline<HandConfiguration> letterK = (new LetterK()).getTimeline();
    private Timeline<HandConfiguration> love = (new Love()).getTimeline();
    // private Timeline<HandConfiguration> default = (new LetterA()).getTimeline();
    private AnimationEngine animator;

    public TimelineManager(AnimationEngine animator) {
        this.animator = animator;

        fullAnimation = new Timeline<HandConfiguration>();
        // Full animation is simply all the animations
        // one after eachother.
        fullAnimation.extend(letterA);
        fullAnimation.extend(letterC);
        fullAnimation.extend(letterK);
        fullAnimation.extend(love);
    }

    public void playFullAnimation() {
        animator.setTimeline(fullAnimation);
        animator.startAnimation();
    }

}
