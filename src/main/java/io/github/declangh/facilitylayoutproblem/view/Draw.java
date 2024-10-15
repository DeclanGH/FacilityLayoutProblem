package io.github.declangh.facilitylayoutproblem.view;

import io.github.declangh.facilitylayoutproblem.model.Floor;
import io.github.declangh.facilitylayoutproblem.model.Hole;
import io.github.declangh.facilitylayoutproblem.model.StationType;
import org.jetbrains.annotations.NotNull;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;

class Draw extends JFrame {
    public static final String PROJECT_TITLE = "Facility Layout Problem";

    public static final int DISPLAY_HEIGHT = 600;
    public static final int DISPLAY_WIDTH = 600;

    private final ArrayList<Floor> floorSnapshots;
    private int currSnapshotIdx = 0;
    private final JPanel floorGridPanel;

    protected Draw(ArrayList<Floor> floorSnapshots) {
        this.floorSnapshots = floorSnapshots;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle(PROJECT_TITLE);
        setSize(DISPLAY_WIDTH, DISPLAY_HEIGHT);
        setResizable(true);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        floorGridPanel = new JPanel();
        add(floorGridPanel, BorderLayout.CENTER);

        setVisible(true);

        startTimer();
    }

    private void startTimer() {
        Timer timer = new Timer(500, e -> {
            if (currSnapshotIdx < floorSnapshots.size()) {
                drawFloorSnapshot(floorSnapshots.get(currSnapshotIdx++));
            } else {
                ((Timer) e.getSource()).stop();
                System.out.println("Genetic algorithm has completed!");
            }
        });
        timer.setInitialDelay(0);
        timer.start();
    }

    private void drawFloorSnapshot(@NotNull final Floor floor) {
        floorGridPanel.removeAll();

        floorGridPanel.setLayout(new GridLayout(floor.getSideLength(), floor.getSideLength()));

        drawFloorGridPanel(floorGridPanel, floor);

        // button for info
        JButton infoButton = new JButton("i");
        infoButton.setFont(new Font("Arial", Font.BOLD, 15));
        infoButton.setPreferredSize(new Dimension(35, 35));

        infoButton.addActionListener(_ -> showLegend());

        // panel for info
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.add(infoButton, BorderLayout.EAST);
        add(infoPanel, BorderLayout.SOUTH);

        floorGridPanel.revalidate();
        floorGridPanel.repaint();
    }

    private void drawFloorGridPanel(JPanel floorGridPanel, Floor floor) {
        ArrayList<Hole> holes = floor.getHoles();

        for (Hole hole : holes) {
            JPanel holePanel = new JPanel();
            holePanel.setBorder(BorderFactory.createLineBorder(Color.WHITE,3));

            if (hole.isOccupied()) {
                holePanel.setBackground(hole.getStation().getColor());
                holePanel.add(new JLabel(hole.getStation().getIdAsString()));
            } else {
                holePanel.setBackground(Hole.COLOR);
            }
            floorGridPanel.add(holePanel);
        }
    }

    private void showLegend() {
        // panel for legend
        JPanel legendPanel = new JPanel();
        legendPanel.setLayout(new GridLayout(StationType.values().length, 1));

        for (StationType type : StationType.values()) {
            JPanel legendItem = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel colorLabel = new JLabel();
            colorLabel.setOpaque(true);
            colorLabel.setBackground(type.getColor());
            colorLabel.setPreferredSize(new Dimension(20, 20));

            JLabel textLabel = new JLabel(type.name());
            legendItem.add(colorLabel);
            legendItem.add(textLabel);

            legendPanel.add(legendItem);
        }

        JOptionPane.showMessageDialog(this, legendPanel, "Legend", JOptionPane.PLAIN_MESSAGE);
    }
}
