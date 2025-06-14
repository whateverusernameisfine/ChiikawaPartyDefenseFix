package entities;

import game.GamePanel;
import util.ImageLoader;
import java.awt.*;

public class NormalZombie extends Zombie {
    private Image aliveImg;
    private Image dyingImg;

    public NormalZombie(int x, int y, GamePanel game) {
        super(x, y, game);
        this.health = 190;
        this.speed = 5;
        this.avoidMud = false;
        aliveImg = ImageLoader.load("zombie_normal.gif");
        dyingImg = ImageLoader.load("zombie_normal_dying.gif"); // only load once
    }

    @Override
    public void draw(Graphics g) {
        Image img = isDying ? dyingImg : aliveImg;
        if (isDying && burnt) {
            g.drawImage(burntImage, (int) drawX, (int) drawY, width, height, null);
        } else {
            g.drawImage(img, (int) drawX, (int) drawY, width, height, null);
        }
    }

}
