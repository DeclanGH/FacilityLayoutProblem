package io.github.declangh.facilitylayoutproblem.view;

import io.github.declangh.facilitylayoutproblem.model.Floor;
import io.github.declangh.facilitylayoutproblem.model.Hole;

import javax.swing.SwingUtilities;
import java.util.ArrayList;

public class Display {
    private final ArrayList<Floor> floorSnapshots;

    public Display() {
        this.floorSnapshots = new ArrayList<>();
    }

    public synchronized void submitFloor(Floor floor, String threadName, int generation) {
        if (generation == floorSnapshots.size()) {
            floorSnapshots.add(floor);
            int count = 0;
            for (Hole hole : floor.getHoles()) {
                if (hole.isOccupied()) count += 1;
            }
            System.out.println(count);
            System.out.println(threadName + " | Generation: " + generation + " |  fitness score: "
                    + floor.getFitnessScore() + " | display size: " + floorSnapshots.size());
        }
    }

    public void showSnapshots() {
        SwingUtilities.invokeLater(() -> new Draw(floorSnapshots));
    }
}
