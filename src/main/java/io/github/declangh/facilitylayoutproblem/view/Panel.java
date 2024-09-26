package io.github.declangh.facilitylayoutproblem.view;

import io.github.declangh.facilitylayoutproblem.model.Floor;
import io.github.declangh.facilitylayoutproblem.model.Hole;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Panel extends JPanel implements ActionListener {

    //  500 milliseconds to draw twice a second
    public static final int DRAW_INTERVAL = 500;

    private final Floor floor;
    private final Timer timer;

    public Panel(Floor floor) {
        this.floor = floor;
        this.timer = new Timer(DRAW_INTERVAL, this) ;
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    @Override
    public void paintComponent(final Graphics graphics) {
        super.paintComponent(graphics);
        final Graphics2D graphics2D = (Graphics2D) graphics;

        graphics2D.setColor(Color.WHITE);
        graphics2D.fillRect(0, 0, getWidth(), getHeight());

        ArrayList<ArrayList<Hole>> floorGrid = floor.getHoles();

        int numOfRows = floorGrid.size();
        int numOfColumns = floorGrid.getFirst().size();

        int holeWidth = getWidth() / numOfColumns;
        int holeHeight = getHeight() / numOfRows;

        //graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (int row = 0; row < numOfRows; row++) {
            for (int col = 0; col < numOfColumns; col++) {
                Hole hole = floorGrid.get(row).get(col);

                if (hole.isOccupied()) {
                    graphics2D.setColor(hole.getStation().getColor());
                } else {
                    graphics2D.setColor(Color.WHITE);
                }

                int x = col * holeWidth;
                int y = row * holeHeight;
                graphics2D.fillRect(x, y, holeWidth, holeHeight);
                graphics2D.setColor(Color.WHITE);
                graphics2D.drawRect(x, y, holeWidth, holeHeight);
            }
        }
    }


    /*public void paint(Graphics g) {
        g.drawRect(10,10,10,10);
    }*/
}
