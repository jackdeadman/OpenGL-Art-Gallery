package scene;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.TitledBorder;
import java.util.*;
import models.*;
import engine.*;
import scene.guihelpers.*;
import scene.handpositions.*;

public class SceneControls extends JPanel {

    private HandConfiguration handConfig;
    private AnimationEngine<HandConfiguration> animator;

    private JPanel middle = new JPanel();

    private Lamp[] lamps;
    private DirectionalLight worldLight;
    private TimelineManager timelineManager;

    public void setLampModels(Lamp[] lamps) {
        this.lamps = lamps;
    }

    public void setWorldLight(DirectionalLight light) {
        worldLight = light;
    }

    public SceneControls(HandConfiguration handConfig) {
        this.handConfig = handConfig;
        setPreferredSize(new Dimension(300, 2000));
        buildPanel();
    }

    public void setAnimationEngine(AnimationEngine<HandConfiguration> animator) {
        timelineManager = new TimelineManager(animator);
        timelineManager.playFullAnimation();
        this.animator = animator;
    }

    private JPanel buildSection(String title) {
        JPanel section = new JPanel();
        TitledBorder titled = new TitledBorder(title);
        section.setBorder(titled);
        return section;
    }

    private void buildAnimationSection() {
        JPanel section = buildSection("Animation");

        ToggleButton playBtn = new ToggleButton(
                    "Pause",
                    "Play", false);

        playBtn.addToggleListener(e -> {
            boolean isOn = ((ToggleButton)(e.getSource())).getToggleState();
            if (isOn) {
                animator.startAnimation();
            } else {
                animator.pauseAnimation();
            }
        });

        JButton stopBtn = new JButton("Reset");
        stopBtn.addActionListener(e -> {
            animator.resetAnimation();
            playBtn.set(false);
        });

        JLabel time = new JLabel("100 / 1:30");

        section.add(playBtn);
        section.add(stopBtn);
        section.add(time);

        add(section);
    }

    private void buildHandPositionsSection() {
        JPanel section = buildSection("Hand Positions");
        section.setLayout(new GridLayout(2, 1));

        JPanel topSection = new JPanel(new GridLayout(1, -1));

        JButton btn1 = new JButton("A");
        btn1.addActionListener(e -> {
            animator.setTimeline((new LetterA()).getTimeline());
            animator.startAnimation();
        });

        JButton btn2 = new JButton("C");
        btn2.addActionListener(e -> {
            animator.setTimeline((new LetterC()).getTimeline());
            animator.startAnimation();
        });

        JButton btn3 = new JButton("K");
        btn3.addActionListener(e -> {
            animator.setTimeline((new LetterK()).getTimeline());
            animator.startAnimation();
        });

        JButton btn4 = new JButton("-- Love Gesture --");
        btn4.addActionListener(e -> {
            animator.setTimeline((new Love()).getTimeline());
            animator.startAnimation();
        });

        topSection.add(btn1);
        topSection.add(btn2);
        topSection.add(btn3);

        section.add(topSection);
        section.add(btn4);

        add(section);
    }

    private void buildWorldTogglesSection() {
        JPanel section = buildSection("World Toggles");
        section.setLayout(new GridLayout(-1, 1));

        ToggleButton button1 =  new ToggleButton("Lamp 1: 0n", "Lamp 1: Off");
        button1.addToggleListener(e -> {
            ToggleButton button = (ToggleButton) e.getSource();
            lamps[0].set(button.getToggleState());
        });

        ToggleButton button2 =  new ToggleButton("Lamp 2: 0n", "Lamp 2: Off");
        button2.addToggleListener(e -> {
            ToggleButton button = (ToggleButton) e.getSource();
            lamps[1].set(button.getToggleState());
        });

        // ToggleButton button3 =  new ToggleButton("Spotlight 0n", "Spotlight: Off");
        // button3.addToggleListener(e -> {
        //     ToggleButton button = (ToggleButton) e.getSource();
        //     spotlight.set(button.getToggleState());
        // });

        ToggleButton button4 =  new ToggleButton("World light: 0n", "World light: Off");
        button4.addToggleListener(e -> {
            ToggleButton button = (ToggleButton) e.getSource();
            worldLight.set(button.getToggleState());
        });

        section.add(button1);
        section.add(button2);
        // section.add(button3);
        section.add(button4);

        add(section);
    }

    private void buildCameraSection() {
        JPanel section = buildSection("Cameras");
        section.setLayout(new GridLayout(-1, 3));
        section.add(new JButton("Front"));
        section.add(new JButton("Back"));
        section.add(new JButton("Top"));

        add(section);
    }

    private void buildPanel() {
        // this.setLayout(new GridLayout(-1, 1));
        buildAnimationSection();
        buildHandPositionsSection();
        buildWorldTogglesSection();
        buildCameraSection();
        // buildFingerControls();
    }


}
