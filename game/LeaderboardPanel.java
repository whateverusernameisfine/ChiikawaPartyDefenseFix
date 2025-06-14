package game;

import util.ImageLoader;

import javax.swing.*;
import java.awt.*;

public class LeaderboardPanel extends JPanel {
    private Image backImage;
    public LeaderboardPanel(JFrame frame) {
        setLayout(null);

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
        setLayout(new BorderLayout());
        JLabel label = new JLabel("Leaderboard Coming Soon", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 32));
        add(label, BorderLayout.CENTER);
    }
}
