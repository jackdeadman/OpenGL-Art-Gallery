package scene;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.TitledBorder;
import java.util.*;
import models.*;
import engine.*;

class ToggleButton extends JButton {

    private String onMessage, offMessage;
    private boolean on = true;

    private void setMessage() {
        if (on) {
            setText(onMessage);
            setBackground(new Color(127, 219, 255));
            setForeground(new Color(0, 73, 102));
        } else {
            setText(offMessage);
            setBackground(new Color(221, 221, 221));
            setForeground(new Color(0, 0, 0));
        }
    }

    private ArrayList<ChangeListener> changeListeners123 = new ArrayList<>();

    public ToggleButton(String onMessage, String offMessage) {
        super(onMessage);
        this.onMessage = onMessage;
        this.offMessage = offMessage;
        changeListeners123 = new ArrayList<>();

        setMessage();

        addActionListener(e -> {
            on = !on;
            setMessage();
            // Need to do it here to ensure the update occurs first
            for (ChangeListener listener: changeListeners123) {
                listener.stateChanged(new ChangeEvent(this));
            }
        });
    }

    public boolean getToggleState() {
        return on;
    }

    public void addToggleListener(ChangeListener listener) {
        changeListeners123.add(listener);
    }


}

class LabelSlider extends JPanel {

    private JLabel label = new JLabel();
    private JSlider slider;

    public LabelSlider(int orientation, int min, int max, int step) {
        slider = new JSlider(orientation, min, max, step);

        add(label);
        add(slider);
    }

    public void addChangeListener(ChangeListener listener) {
        slider.addChangeListener(e -> {
            JSlider slider = (JSlider)(e.getSource());
            String value = String.valueOf(slider.getValue());
            label.setText(value);
            listener.stateChanged(new ChangeEvent(this));
        });
    }

    public void setValue(int value) {
        label.setText(String.valueOf(value));
        slider.setValue(value);
    }

    public int getValue() {
        return slider.getValue();
    }

}


public class SceneControls extends JPanel {

    private HandConfiguration handConfig;
    private Object worldConfig;

    private final char PLAY_CHARACTER = '\u25B6';
    private final char PAUSE_CHARACTER = '\u23F8';
    private final char STOP_CHARACTER = '\uu23F9';
    private JPanel middle = new JPanel();


    private final int JOINT_MIN = 0, JOINT_MAX = 100;
    private final int NUM_JOINTS = 3;
    private final int NUM_FINGERS = 4;

    private Lamp[] lamps;
    private DirectionalLight worldLight;

    public void setLampModels(Lamp[] lamps) {
        this.lamps = lamps;
    }

    public void setWorldLight(DirectionalLight light) {
        worldLight = light;
    }

    // fingerSliders[fingerNumber][paramNumber]
    // 3 for the additional finger rotations
    private LabelSlider[][] fingerSliders = new LabelSlider[NUM_FINGERS][NUM_JOINTS + 3];
    private HashMap<LabelSlider, int[]> sliderToIndexMap = new HashMap<>();

    private class SliderListener implements ChangeListener {
        public void stateChanged(ChangeEvent e) {
            LabelSlider slider = (LabelSlider)(e.getSource());
            int value = slider.getValue();
            float percentage = value / Float.valueOf(JOINT_MAX);
            int[] indices = sliderToIndexMap.get(slider);
            handConfig.getFingerValues()[indices[0]][indices[1]] = percentage;
        }
    }

    public SceneControls(HandConfiguration handConfig, Object worldConfig) {
        this.handConfig = handConfig;
        this.worldConfig = worldConfig;

        setPreferredSize(new Dimension(300, 2000));
        buildPanel();
        setSliders();
    }

    public void setSliders() {
        for (int i=0; i<NUM_FINGERS; ++i) {
            for (int j=0; j<NUM_JOINTS+3; ++j) {
                LabelSlider slider = fingerSliders[i][j];
                slider.setValue(
                    (int)(handConfig.getFingerValues()[i][j] * 100.0)
                );
            }
        }
    }

    private JPanel buildSection(String title) {
        JPanel section = new JPanel();
        TitledBorder titled = new TitledBorder(title);
        section.setBorder(titled);
        return section;
    }

    private void buildAnimationSection() {
        JPanel section = buildSection("Animation");
        JButton playBtn = new JButton(String.valueOf(PLAY_CHARACTER));
        JButton pauseBtn = new JButton(String.valueOf(PAUSE_CHARACTER));

        JLabel time = new JLabel("100 / 1:30");

        section.add(playBtn);
        section.add(pauseBtn);
        section.add(time);

        add(section);
    }

    private void buildHandPositionsSection() {
        JPanel section = buildSection("Hand Positions");
        section.setLayout(new GridLayout(2, 1));

        JPanel topSection = new JPanel(new GridLayout(1, -1));

        JButton btn1 = new JButton("A");
        JButton btn2 = new JButton("C");
        JButton btn3 = new JButton("K");
        JButton btn4 = new JButton("Something complex");

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

        ToggleButton button3 =  new ToggleButton("Spotlight: 0n", "Spotlight: Off");
        button3.addToggleListener(e -> {
            ToggleButton button = (ToggleButton) e.getSource();
            worldLight.set(button.getToggleState());
        });

        section.add(button1);
        section.add(button2);
        section.add(button3);

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

    private JPanel buildFingerControl(String title, int fingerNumber) {
        JPanel section = buildSection(title);
        JPanel sliderSection = new JPanel(new GridLayout(-1, 1));
        ChangeListener listener = new SliderListener();

        for (int i=1; i<NUM_JOINTS+1; ++i) {
            sliderSection.add(new Label("Joint " + i));
            LabelSlider jointAngle = new LabelSlider(JSlider.HORIZONTAL,
                                          JOINT_MIN, JOINT_MAX, 0);

            // Create mapping both ways
            sliderToIndexMap.put(jointAngle, new int[] { fingerNumber, i-1});
            fingerSliders[fingerNumber][i-1] = jointAngle;

            jointAngle.addChangeListener(listener);
            sliderSection.add(jointAngle);
        }

        sliderSection.add(new Label("Turn"));

        LabelSlider jointAngleX = new LabelSlider(JSlider.HORIZONTAL,
                                      JOINT_MIN, JOINT_MAX, 0);

        jointAngleX.addChangeListener(listener);
        sliderSection.add(jointAngleX);
        sliderToIndexMap.put(jointAngleX, new int[] { fingerNumber, NUM_JOINTS});
        fingerSliders[fingerNumber][NUM_JOINTS] = jointAngleX;

        LabelSlider jointAngleY = new LabelSlider(JSlider.HORIZONTAL,
                                      JOINT_MIN, JOINT_MAX, 0);

        jointAngleY.addChangeListener(listener);
        sliderSection.add(jointAngleY);
        sliderToIndexMap.put(jointAngleY, new int[] { fingerNumber, NUM_JOINTS+1});
        fingerSliders[fingerNumber][NUM_JOINTS+1] = jointAngleY;

        LabelSlider jointAngleZ = new LabelSlider(JSlider.HORIZONTAL,
                                      JOINT_MIN, JOINT_MAX, 0);

        jointAngleZ.addChangeListener(listener);
        sliderSection.add(jointAngleZ);
        sliderToIndexMap.put(jointAngleZ, new int[] { fingerNumber, NUM_JOINTS+2});
        fingerSliders[fingerNumber][NUM_JOINTS+2] = jointAngleZ;

        section.add(sliderSection);

        return section;
    }

    private void buildFingerControls() {
        JPanel section = buildSection("Fingers");
        section.setLayout(new GridLayout(-1, 1));

        for (int i=1; i<NUM_FINGERS+1; ++i) {
            JPanel fingerSection = buildFingerControl("Finger " + i, i-1);
            section.add(fingerSection);
        }

        add(section);
    }

    private void buildPanel() {
        // this.setLayout(new GridLayout(-1, 1));
        buildAnimationSection();
        buildHandPositionsSection();
        buildWorldTogglesSection();
        buildCameraSection();
        buildFingerControls();
    }


}
