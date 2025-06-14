package entities;

import game.GamePanel;
import util.DijkstraPathfinder;
import util.ImageLoader;
import java.awt.*;
import java.awt.Point;
import java.util.List;

public abstract class Zombie {
    private GamePanel game;

    protected double x, y;
    protected double drawX;
    protected double drawY;
    protected int width = 80;
    protected int height = 100;

    protected int health;
    public double speed;
    public double originalSpeed;
    private List<Point> path = null;
    private int pathIndex = 0;

    protected boolean isDying = false;
    protected boolean isColliding = false;
    protected boolean avoidMud; // false = will walk through mud
    protected int dyingTimer = 0;
    protected int attackCooldown = 0;
    protected Plant targetPlant = null;

    protected int defaultDyingDuration = 15;
    protected int customDyingDuration = 80;
    protected boolean burnt = false;
    protected Image burntImage;

    public Zombie(int x, int y, GamePanel game) {
        this.x = x;
        this.y = y;
        this.drawX = x;
        this.drawY = y;
        this.game = game;
        this.speed = 5;
        this.originalSpeed = this.speed;
        this.burntImage = ImageLoader.load("Burnt_Zombie.gif");
    }

    public void update(List<Plant> plants) {
        if (isDying) return;

        // 🔹 Adjust speed based on mud
        if (isOnMud()) {
            speed = originalSpeed * 0.5;
        } else {
            speed = originalSpeed;
        }

        // 🔹 1. Handle plant collision BEFORE movement
        isColliding = false;
        targetPlant = null;
        for (Plant plant : plants) {
            if (Math.abs(plant.getY() - this.y) <= 30 &&
                    plant.getBounds().intersects(this.getBounds()) &&
                    plant.isAlive()) {
                isColliding = true;
                targetPlant = plant;
                break;
            }
        }

        if (isColliding && targetPlant != null) {
            attackCooldown++;
            if (attackCooldown >= 30) {
                targetPlant.takeDamage(30);
                System.out.println("Zombie attacks! " + targetPlant.name + " health: " + targetPlant.health);
                attackCooldown = 0;
                if (targetPlant.isDead()) {
                    targetPlant.startDying();
                }
            }
            return; // ⛔ DO NOT MOVE if attacking
        }

        // 🔹 2. Path-following logic
        if (path != null && pathIndex < path.size()) {
            followPath();
            return;
        }

        // 🔹 3. Fallback: No path, no collision
        path = null;
        pathIndex = 0;

        if (x > 0) {
            ObstacleTypeAhead ahead = checkAhead(game.getObstacles(), game.getColumns(), game.getRows());
            if (ahead == ObstacleTypeAhead.ROCK || ahead == ObstacleTypeAhead.MUD) {
                System.out.println("Zombie: Obstacle ahead, recalculating path...");
                calculatePathToLeftEdge();
                return; // 🔁 Wait for next frame to start moving via path
            }
        }

        // 🔹 4. Move freely (only if not dying/pathing/colliding)
        x -= speed;
        drawX = (int) x;
        drawY = (int) y;
    }


    private boolean isOnMud() {
        // Use the zombie's center, not top-left corner
        int centerX = (int) (x + width / 2);
        int centerY = (int) (y + height / 2);

        Point currentGrid = game.pixelToGrid(centerX, centerY);
        if (currentGrid == null) return false;

        for (Obstacle obs : game.getObstacles()) {
            if (obs instanceof ObstacleMud &&
                    obs.getGridX() == currentGrid.x &&
                    obs.getGridY() == currentGrid.y) {
                return true;
            }
        }
        return false;
    }



    public void calculatePathToLeftEdge() {
        Point start = game.pixelToGrid((int) x, (int) y);
        if (start == null) {
            System.out.println("Zombie: Invalid start position.");
            return;
        }

        int[][] costGrid = game.buildCostGrid(this); // ✅ Declare once

        Point bestGoal = null;
        List<Point> bestPath = null;
        int bestCost = Integer.MAX_VALUE;

        for (int row = 0; row < costGrid.length; row++) {
            Point goalCandidate = new Point(0, row);
            List<Point> candidatePath = DijkstraPathfinder.findPath(start, goalCandidate, costGrid);

            if (candidatePath != null && !candidatePath.isEmpty()) {
                int totalCost = candidatePath.size();  // Optional: you could sum real tile costs
                if (totalCost < bestCost) {
                    bestCost = totalCost;
                    bestPath = candidatePath;
                    bestGoal = goalCandidate;
                }
            }
        }

        if (bestPath != null) {
            this.path = bestPath;
            this.pathIndex = 0;
            System.out.println("Zombie: Path to leftmost tile at " + bestGoal + " (length: " + bestPath.size() + ")");
        } else {
            System.out.println("Zombie: No path to left column found.");
            this.path = null;
        }
    }


    public void followPath() {
        if (path == null || pathIndex >= path.size()) {
            System.out.println("Zombie: No path or finished path");
            return;
        }

        Point grid = path.get(pathIndex);
        Point target = game.gridToPixel(grid.x, grid.y);

        double dx = target.x - x;
        double dy = target.y - y;
        double dist = Math.hypot(dx, dy);

        if (dist < 3.0) {
            pathIndex++;
            System.out.println("Zombie: Reached step, moving to next");
        } else {
            x += speed * dx / dist;
            y += speed * dy / dist;
            drawX = (int) x;
            drawY = (int) y;
        }

        // ✅ When done with all path steps, reset
        if (pathIndex >= path.size()) {
            path = null;
            pathIndex = 0;
            System.out.println("Zombie: Path complete, ready to move freely.");
            return;
        }
    }



    public enum ObstacleTypeAhead {
        CLEAR, MUD, ROCK
    }

    public ObstacleTypeAhead checkAhead(List<Obstacle> obstacles, int[] columns, int[] rows) {
        Point currentGrid = findClosestGrid((int)x, (int)y, columns, rows);
        int nextCol = currentGrid.x - 1;
        int row = currentGrid.y;

        if (nextCol < 0) return ObstacleTypeAhead.CLEAR;

        for (Obstacle obs : obstacles) {
            if (obs.getGridX() == nextCol && obs.getGridY() == row) {
                if (obs instanceof ObstacleRock) return ObstacleTypeAhead.ROCK;
                if (obs instanceof ObstacleMud) return ObstacleTypeAhead.MUD;
            }
        }
        return ObstacleTypeAhead.CLEAR;
    }

    private Point findClosestGrid(int x, int y, int[] columns, int[] rows) {
        int col = 0, row = 0;
        for (int i = 0; i < columns.length; i++) {
            if (x >= columns[i]) col = i;
        }
        for (int j = 0; j < rows.length; j++) {
            if (y >= rows[j]) row = j;
        }
        return new Point(col, row);
    }


    public boolean isDead() {
        return health <= 0 && !isDying;
    }

    public void startDying() {
        startDying(defaultDyingDuration);
    }

    public void startDying(int duration) {
        isDying = true;
        dyingTimer = 0;
        customDyingDuration = duration;
    }

    public void updateDying() {
        if (isDying) dyingTimer++;
    }

    public boolean isMarkedForRemoval() {
        return isDying && dyingTimer >= customDyingDuration;
    }

    public boolean isDying() {
        return isDying;
    }

    public void burn() {
        this.burnt = true;
        this.health = 0;
        this.startDying(80);
    }

    public void takeDamage(int amount) {
        health -= amount;
    }

    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, width, height);
    }

    public int getX() { return (int) x; }
    public int getY() { return (int) y; }
    public int getHealth() { return health; }
    public int getScoreValue() { return 1; }

    public abstract void draw(Graphics g);
}
