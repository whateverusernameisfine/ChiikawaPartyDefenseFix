package util;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.HashMap;

public class ImageLoader {
    private static final HashMap<String, Image> imageCache = new HashMap<>();

    public static Image load(String name) {
        if (!imageCache.containsKey(name)) {
            try {
                URL url = ImageLoader.class.getResource("/images/" + name);
                if (url == null) {
                    System.err.println("[ImageLoader] Resource not found: /images/" + name);
                    return null;
                }
                Image image = new ImageIcon(url).getImage();
                if (image.getWidth(null) == -1) {
                    System.err.println("[ImageLoader] Failed to load image: /images/" + name);
                }
                imageCache.put(name, image);
            } catch (Exception e) {
                System.err.println("[ImageLoader] Exception loading image: /images/" + name);
                e.printStackTrace();
            }
        }
        return imageCache.get(name);
    }

    public static Image loadAndResize(String name, int w, int h) {
        String cacheKey = name + "@" + w + "x" + h;
        if (!imageCache.containsKey(cacheKey)) {
            Image original = load(name);
            if (original == null) return null;

            // ✅ If it's a .gif, return scaled version via ImageIcon to preserve animation
            if (name.toLowerCase().endsWith(".gif")) {
                Image scaledGif = new ImageIcon(
                        new ImageIcon(original).getImage().getScaledInstance(w, h, Image.SCALE_DEFAULT)
                ).getImage();
                imageCache.put(cacheKey, scaledGif);
                return scaledGif;
            }

            // ❌ For non-GIFs, resize to BufferedImage (as you already did)
            BufferedImage resized = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = resized.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.drawImage(original, 0, 0, w, h, null);
            g2.dispose();

            imageCache.put(cacheKey, resized);
        }
        return imageCache.get(cacheKey);
    }

}
