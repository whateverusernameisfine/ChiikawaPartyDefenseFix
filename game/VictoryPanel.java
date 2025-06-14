package game;

import util.ImageLoader;
import util.MapConfig;
import javax.swing.*;
import java.awt.*;

public class VictoryPanel extends JPanel {
    private final Image victoryImage;
    private final JButton backButton;
    private final JButton nextMapButton;
    private final int score;

    public VictoryPanel(GameFrame frame, int currentMap, int score) {
        setLayout(null);
        setBackground(Color.BLACK);

        this.score = score;
        victoryImage = ImageLoader.load("victory.png");

        // === Back Button ===
        backButton = new JButton(new ImageIcon(ImageLoader.load("back_button.png")));
        backButton.setBounds(400, 500, 140, 60);
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        add(backButton);
        backButton.addActionListener(e -> {
            frame.setContentPane(new MapSelectPanel(frame));
            frame.revalidate();
        });

        // === Next Map Button ===
        if (currentMap < 3) {
            nextMapButton = new JButton(new ImageIcon(ImageLoader.load("next.png")));
            nextMapButton.setBounds(400, 400, 150, 80);
            nextMapButton.setBorderPainted(false);
            nextMapButton.setContentAreaFilled(false);
            nextMapButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            final int nextMapId = currentMap + 1;

            nextMapButton.addActionListener(e -> {
                if (nextMapId == 2) {
                    frame.setContentPane(new Map2Panel(new MapConfig(2, new int[]{0, 0, 0})));
                } else if (nextMapId == 3) {
                    frame.setContentPane(new Map3Panel(new MapConfig(3, new int[]{0, 0, 0})));
                }
                frame.revalidate();
            });

            add(nextMapButton);
        } else {
            nextMapButton = null; // Still need to assign it because it's final
        }
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (victoryImage != null) {
            g.drawImage(victoryImage, 300, 100, 500, 250, this);
        }
        g.setFont(new Font("Arial", Font.BOLD, 28));
        g.setColor(Color.WHITE);
        g.drawString("Score: " + score, 420, 370);
    }
}
