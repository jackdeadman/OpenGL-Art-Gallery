package galleryscene.guihelpers;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;

public class ToggleButton extends JButton {
    /**
    * @author Jack Deadman
    */
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

    private ArrayList<ChangeListener> changeListeners = new ArrayList<>();

    public ToggleButton(String onMessage, String offMessage, boolean inital) {
        super(onMessage);
        this.onMessage = onMessage;
        this.offMessage = offMessage;
        on = inital;
        changeListeners = new ArrayList<>();

        setMessage();

        addActionListener(e -> {
            on = !on;
            setMessage();
            // Need to do it here to ensure the update occurs first
            for (ChangeListener listener: changeListeners) {
                listener.stateChanged(new ChangeEvent(this));
            }
        });
    }

    public void set(boolean on) {
        this.on = on;
        setMessage();
    }

    public ToggleButton(String onMessage, String offMessage) {
        this(onMessage, offMessage, true);
    }

    public boolean getToggleState() {
        return on;
    }

    public void addToggleListener(ChangeListener listener) {
        changeListeners.add(listener);
    }


}
