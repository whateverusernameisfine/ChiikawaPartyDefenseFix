package entities;

import game.GamePanel;
import util.ImageLoader;
import java.awt.*;

public class FootballZombie extends Zombie {
    private Image aliveImg;
    private Image dyingImg;


    public FootballZombie(int x, int y, GamePanel game) {
        super(x, y, game);
        this.health = 1100;  // tanky
        this.speed = 1.5;     // faster than normal
        this.avoidMud = true;
        aliveImg = ImageLoader.load("zombie_football.gif");
        dyingImg = ImageLoader.load("zombie_football_dying.gif");
    }

    @Override
    public int getScoreValue() {
        return 3;
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
