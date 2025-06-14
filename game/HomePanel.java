package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomePanel extends JPanel {
    private JButton mapButton;
    private JButton leaderboardButton;
    private JButton galleryButton;

    public HomePanel(JFrame frame) {
        setLayout(null);
        setBackground(Color.DARK_GRAY);

        // === Map Button ===
        mapButton = new JButton("Choose Map");
        mapButton.setBounds(400, 250, 200, 60);
        mapButton.setFont(new Font("Arial", Font.BOLD, 18));
        mapButton.setFocusPainted(false);
        add(mapButton);

        mapButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setContentPane(new MapSelectPanel(frame));
                frame.revalidate();
            }
        });

        // === Leaderboard Button ===
        leaderboardButton = new JButton("Leaderboard");
        leaderboardButton.setBounds(400, 350, 200, 60);
        leaderboardButton.setFont(new Font("Arial", Font.BOLD, 18));
        leaderboardButton.setFocusPainted(false);
        add(leaderboardButton);

        leaderboardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setContentPane(new LeaderboardPanel(frame)); // Create this later
                frame.revalidate();
            }
        });

        galleryButton = new JButton("Gallery");
        galleryButton.setBounds(400, 450, 200, 60);
        galleryButton.setFont(new Font("Arial", Font.BOLD, 18));
        galleryButton.setFocusPainted(false);
        add(galleryButton);

        galleryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setContentPane(new GalleryPanel(frame)); // Create this later
                frame.revalidate();
            }
        });

        JLabel scoreLabel = new JLabel("Total Score: " + ((GameFrame) frame).getTotalScore());
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 20));
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setBounds(30, 30, 300, 30);
        add(scoreLabel);
    }
}
