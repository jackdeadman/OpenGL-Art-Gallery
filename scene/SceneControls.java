package scene;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;

public class SceneControls extends JPanel {

    private Object handConfig;
    private Object worldConfig;

    private final char PLAY_CHARACTER = '\u25B6';
    private final char PAUSE_CHARACTER = '\u23F8';
    private final char STOP_CHARACTER = '\uu23F9';

    public SceneControls(Object handConfig, Object worldConfig) {
        this.handConfig = handConfig;
        this.worldConfig = worldConfig;

        setPreferredSize(new Dimension(300, 0));
        buildPanel();
    }

    private JPanel buildSection(String title) {
        JPanel section = new JPanel();
        TitledBorder titled = new TitledBorder(title);
        section.setBorder(titled);
        return section;

    }

    private void buildAnimationSection() {
        JPanel section = buildSection("Animation");
        section.setLayout(new GridLayout(1, 3));
        section.setPreferredSize(new Dimension(300, 100));

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
        section.setLayout(new GridLayout(3, 1));

        JPanel topSection = new JPanel(new GridLayout(1, 3));

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
        // section.setLayout(new GridLayout(-1, 2));

        section.add(new JButton("Turn on Lamp 1"));
        section.add(new JButton("Turn on Lamp 2"));
        section.add(new JButton("Turn on World Light"));

        add(section);
    }

    private void buildCameraSection() {
        JPanel section = buildSection("Cameras");
        section.add(new JButton("Front"));
        section.add(new JButton("Back"));
        section.add(new JButton("Top"));

        add(section);
    }

    private void buildPanel() {
        this.setLayout(new GridLayout(-1, 1));
        buildAnimationSection();
        buildHandPositionsSection();
        buildWorldTogglesSection();
        buildCameraSection();
    }

}
