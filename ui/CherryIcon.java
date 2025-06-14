package ui;

import game.*;
import java.awt.Point;


public class CherryIcon extends SidebarIcon {
    public CherryIcon(GamePanel panel) {
        super(panel,
                "active_cherry.png",
                "inactive_cherry.png",
                614, 0,
                150); // cost
    }

    @Override
    protected void handleDrop(Point dropPoint) {
        gamePanel.tryPlaceCherry(dropPoint);
    }
}
