package entities;

import util.ImageLoader;
import java.awt.*;
import java.util.List;

public class Cherry extends Plant {
    private Image image;
    private Image explosionImage;
    private boolean exploded = false;
    private int explosionTimer = 0;
    private final int explodeDelay = 30; // 30 frames ≈ 1s

    public Cherry(int x, int y) {
        super(x, y);
        this.health = 9999; // cannot be damaged
        this.cost = 150;
        this.image = ImageLoader.load("Usagi.gif");
        this.explosionImage = ImageLoader.load("Boom.gif");
    }

    @Override
    public void update() {
        explosionTimer++;
        if (explosionTimer >= explodeDelay && !exploded) {
            exploded = true;
        }
    }

    public void affectZombies(List<Zombie> zombies) {
        if (!exploded) return;

        Rectangle explosionArea = new Rectangle(x - 80, y - 80, width + 160, height + 160);
        for (Zombie z : zombies) {
            if (z.getBounds().intersects(explosionArea) && !z.isDying()) {
                z.burn();
            }
        }
    }

    @Override
    public void draw(Graphics g) {
        if (!exploded) {
            g.drawImage(image, x, y, width, height, null);
        } else {
            g.drawImage(explosionImage, x - 40, y - 40, width + 80, height + 80, null);
        }
    }

    @Override
    public boolean isMarkedForRemoval() {
        return exploded && explosionTimer >= explodeDelay + 20; // remove after boom fades
    }
}
