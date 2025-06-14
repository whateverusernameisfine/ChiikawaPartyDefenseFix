package game;

import util.ImageLoader;

import javax.swing.*;
import java.awt.*;

public class HomeScreenPanel extends JPanel {
    private Image background;
    private JButton playButton;

    public HomeScreenPanel(JFrame frame) {
        setLayout(null);
        background = ImageLoader.load("HomePage.png"); // optional

        playButton = new JButton(new ImageIcon("src/images/TapToStart.png"));
        playButton.setBounds(435, 290, 480, 188);
        playButton.setBorderPainted(false);
        playButton.setContentAreaFilled(false);
        add(playButton);

        playButton.addActionListener(e -> {
            frame.setContentPane(new HomePanel(frame));
            frame.revalidate();
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (background != null) {
            g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
