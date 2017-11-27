package scene;
import scene.handpositions.*;
import engine.*;

public class TimelineManager {
    private Timeline<HandConfiguration> letterA = (new LetterA()).getTimeline();
    private Timeline<HandConfiguration> letterC = (new LetterC()).getTimeline();
    private Timeline<HandConfiguration> letterK = (new LetterK()).getTimeline();
    private Timeline<HandConfiguration> love = (new Love()).getTimeline();
    private Timeline<HandConfiguration> neutral = (new Neutral()).getTimeline();
    private AnimationEngine animator;

    public TimelineManager(AnimationEngine animator) {
        animator.setTimeline(neutral);
        this.animator = animator;
    }

    public void reset() {
        animator.setTimeline(neutral);
        animator.resetAnimation();
    }

    public void playFullAnimation() {
        Timeline<HandConfiguration> fullAnimation = new Timeline<HandConfiguration>();
        fullAnimation.setStart((HandConfiguration) animator.getAnimationState());
        fullAnimation.extend(letterA);
        fullAnimation.extend(letterC);
        fullAnimation.extend(letterK);
        fullAnimation.extend(love);
        animator.setTimeline(fullAnimation);
        animator.startAnimation();
    }

    private Timeline<HandConfiguration> returnToDefaultBeforePlay(Timeline<HandConfiguration> letter) {
        Timeline timeline = new Timeline<HandConfiguration>();
        timeline.setStart(animator.getAnimationState());
        // timeline.addKeyFrame(new HandConfiguration(), 2000);
        timeline.extend(letter);
        return timeline;
    }

    public void playLetterA() {
        animator.setTimeline(returnToDefaultBeforePlay(letterA));
        animator.startAnimation();
    }

    public void playLetterC() {
        animator.setTimeline(returnToDefaultBeforePlay(letterC));
        animator.startAnimation();
    }

    public void playLetterK() {
        animator.setTimeline(returnToDefaultBeforePlay(letterK));
        animator.startAnimation();
    }

    public void playLove() {
        animator.setTimeline(returnToDefaultBeforePlay(love));
        animator.startAnimation();
    }

}
