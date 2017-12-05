package galleryscene;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import models.*;
import engine.*;
import engine.animation.*;
import engine.lighting.*;
import galleryscene.guihelpers.*;

public class SceneControls extends JPanel {

    private HandConfiguration handConfig;
    private AnimationEngine<HandConfiguration> animator;

    private JPanel middle = new JPanel();

    // private Lamp[] lamps;
    private WorldConfiguration worldConfig;
    // private DirectionalLight worldLight;
    private TimelineManager timelineManager;
    private Camera camera;

    public SceneControls(HandConfiguration handConfig, WorldConfiguration worldConfig) {
        this.handConfig = handConfig;
        camera = worldConfig.getCamera();
        this.worldConfig = worldConfig;
        setPreferredSize(new Dimension(300, 2000));
        buildPanel();
    }

    public void setAnimationEngine(AnimationEngine<HandConfiguration> animator) {
        timelineManager = new TimelineManager(animator);
        this.animator = animator;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    private JPanel buildSection(String title) {
        JPanel section = new JPanel();
        TitledBorder titled = new TitledBorder(title);
        section.setBorder(titled);
        return section;
    }

    private ToggleButton playBtn;

    private void buildAnimationSection() {
        JPanel section = buildSection("Animation");

        playBtn = new ToggleButton("Pause","Play", false);

        playBtn.addToggleListener(e -> {
            boolean isOn = ((ToggleButton)(e.getSource())).getToggleState();
            if (isOn) {
                timelineManager.playFullAnimation();
            } else {
                animator.pauseAnimation();
            }
        });

        JButton stopBtn = new JButton("Reset");
        stopBtn.addActionListener(e -> {
            timelineManager.reset();
            playBtn.set(false);
        });

        section.add(playBtn);
        section.add(stopBtn);

        add(section);
    }

    private void buildHandPositionsSection() {
        JPanel section = buildSection("Hand Positions");
        section.setLayout(new GridLayout(2, 1));

        JPanel topSection = new JPanel(new GridLayout(1, -1));

        JButton btn1 = new JButton("A");
        btn1.addActionListener(e -> {
            timelineManager.playLetterA();
            playBtn.set(false);
        });

        JButton btn2 = new JButton("C");
        btn2.addActionListener(e -> {
            timelineManager.playLetterC();
            playBtn.set(false);
        });

        JButton btn3 = new JButton("K");
        btn3.addActionListener(e -> {
            timelineManager.playLetterK();
            playBtn.set(false);
        });

        JButton btn4 = new JButton("-- Love Gesture --");
        btn4.addActionListener(e -> {
            timelineManager.playLove();
            playBtn.set(false);
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
            worldConfig.getPointLight(0).set(button.getToggleState());
        });

        ToggleButton button2 =  new ToggleButton("Lamp 2: 0n", "Lamp 2: Off");
        button2.addToggleListener(e -> {
            ToggleButton button = (ToggleButton) e.getSource();
            worldConfig.getPointLight(1).set(button.getToggleState());
        });

        ToggleButton button3 =  new ToggleButton("Spotlight 0n", "Spotlight: Off");
        button3.addToggleListener(e -> {
            ToggleButton button = (ToggleButton) e.getSource();
            worldConfig.getSpotlight().set(button.getToggleState());
        });

        ToggleButton button4 =  new ToggleButton("World light: 0n", "World light: Off");
        button4.addToggleListener(e -> {
            ToggleButton button = (ToggleButton) e.getSource();
            worldConfig.getDirectionalLight().set(button.getToggleState());
        });

        section.add(button1);
        section.add(button2);
        section.add(button3);
        section.add(button4);

        add(section);
    }

    private void buildCameraSection() {
        JPanel section = buildSection("Cameras");
        section.setLayout(new GridLayout(-1, 3));

        JButton front = new JButton("Front");
        front.addActionListener(e -> {
            camera.setCamera(Camera.CameraType.X);
        });
        section.add(front);

        JButton side = new JButton("Side");
        side.addActionListener(e -> {
            camera.setCamera(Camera.CameraType.Z);
        });
        section.add(side);

        JButton top = new JButton("Top");
        top.addActionListener(e -> {
            camera.setCamera(Camera.CameraType.Y);
        });
        section.add(top);

        add(section);
    }

    private void buildPanel() {
        buildAnimationSection();
        buildHandPositionsSection();
        buildWorldTogglesSection();
        buildCameraSection();
    }


}
