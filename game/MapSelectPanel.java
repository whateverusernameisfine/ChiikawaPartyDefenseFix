package game;

import util.MapConfig;
import util.ImageLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MapSelectPanel extends JPanel {
    private Image backImage;
    private Image backgroundImage;

    public MapSelectPanel(JFrame frame) {
        setLayout(null);
        backgroundImage = ImageLoader.load("stageSelectionBg.png");

        // === Back Button ===
        backImage = ImageLoader.load("back_button.png");
        JButton backButton = new JButton(new ImageIcon(backImage));
        backButton.setBounds(900, 570, 150, 80); // bottom-right
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        add(backButton);

        backButton.addActionListener(e -> {
            frame.setContentPane(new HomePanel(frame));
            frame.revalidate();
        });

        // === Map 1 Button ===
        JLabel map1Button = new JLabel(new ImageIcon(ImageLoader.load("map3.jpg")));
        map1Button.setBounds(100, 160, 260, 389);
        map1Button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        map1Button.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                MapConfig config = new MapConfig(1, new int[]{0, 1, 5}); // Easy
                frame.setContentPane(new Map1Panel(config));
                frame.revalidate();
            }
        });
        add(map1Button);

// === Map 2 Button ===
        JLabel map2Button = new JLabel(new ImageIcon(ImageLoader.load("map3.jpg")));
        map2Button.setBounds(400, 160, 260, 389);
        map2Button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        map2Button.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                MapConfig config = new MapConfig(2, new int[]{0, 0, 0}); // Medium
                frame.setContentPane(new Map2Panel(config));
                frame.revalidate();
            }
        });
        add(map2Button);

// === Map 3 Button ===
        JLabel map3Button = new JLabel(new ImageIcon(ImageLoader.load("map3.jpg")));
        map3Button.setBounds(700, 160, 260, 389);
        map3Button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        map3Button.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                MapConfig config = new MapConfig(3, new int[]{0, 0, 0}); // Hard
                frame.setContentPane(new Map3Panel(config));
                frame.revalidate();
            }
        });
        add(map3Button);

        JLabel scoreLabel = new JLabel("Total Score: " + ((GameFrame) frame).getTotalScore());
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 20));
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setBounds(30, 30, 300, 30);
        add(scoreLabel);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 32));
        g.drawString("Choose a Map", 430, 100);
    }
}
