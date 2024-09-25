package io.github.declangh.facilitylayoutproblem.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.atomic.AtomicInteger;

public class Panel extends JPanel implements ActionListener {

    //  500 milliseconds to draw twice a second
    public static final int DRAW_INTERVAL = 500;

    private final AtomicInteger generation;
    private final Timer timer;

    public Panel() {
        this.generation = new AtomicInteger(0);
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
        graphics2D.setColor(Color.BLACK);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.drawString("Generation: " + generation.getAndIncrement(), 10, 20);
    }


    /*public void paint(Graphics g) {
        g.drawRect(10,10,10,10);
    }*/
}
