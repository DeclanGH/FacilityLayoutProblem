package io.github.declangh.facilitylayoutproblem.view;

import javax.swing.*;
import java.awt.*;

public class Display extends JFrame {

    public static final String PROJECT_TITLE = "Facility Layout Problem";

    public static final int DISPLAY_HEIGHT = 600;
    public static final int DISPLAY_WIDTH = 600;

    public Display() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle(PROJECT_TITLE);
        setSize(DISPLAY_WIDTH, DISPLAY_HEIGHT);
        setResizable(true);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(1, 1, 1, 1));
    }

    public void turnOn() {
        Panel panel = new Panel();
        add(panel);

        setVisible(true);
    }
}
