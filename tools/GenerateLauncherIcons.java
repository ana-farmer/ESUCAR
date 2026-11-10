import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.nio.file.*;
import javax.imageio.ImageIO;

/** Render Android launcher resources from the original emblem. */
class GenerateLauncherIcons {
    static BufferedImage original;
    static BufferedImage render(int size, double fraction, boolean background, boolean round) {
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        if (round) g.clip(new Ellipse2D.Double(0, 0, size, size));
        if (background) { g.setColor(new Color(0x007328)); g.fillRect(0, 0, size, size); }
        int h = (int)Math.round(size * fraction), w = (int)Math.round(h * 353.0 / 452.0);
        int x = (size - w) / 2, y = (size - h) / 2;
        Path2D shield = new Path2D.Double();
        shield.moveTo(x, y);
        shield.lineTo(x + w, y);
        shield.lineTo(x + w, y + h * 355.0 / 452.0);
        shield.lineTo(x + w * 176.0 / 353.0, y + h);
        shield.lineTo(x, y + h * 355.0 / 452.0);
        shield.closePath();
        g.clip(shield);
        g.drawImage(original, x, y, x + w, y + h, 58, 13, 411, 465, null);
        g.dispose();
        return image;
    }
    static void save(BufferedImage image, Path path) throws Exception {
        Files.createDirectories(path.getParent());
        ImageIO.write(image, "png", path.toFile());
    }
    public static void main(String[] args) throws Exception {
        Path root = Paths.get(args.length == 0 ? "." : args[0]).toAbsolutePath().normalize();
        Path res = root.resolve("app/src/main/res");
        original = ImageIO.read(res.resolve("drawable-nodpi/esucar_logo.jpg").toFile());
        // Fits the central 66dp safe circle of the adaptive 108dp canvas.
        save(render(432, 50.0 / 108.0, false, false), res.resolve("drawable-nodpi/ic_launcher_foreground.png"));
        String[] densities = {"mdpi", "hdpi", "xhdpi", "xxhdpi", "xxxhdpi"};
        int[] sizes = {48, 72, 96, 144, 192};
        for (int i = 0; i < sizes.length; i++) {
            Path folder = res.resolve("mipmap-" + densities[i]);
            for (boolean round : new boolean[]{false, true}) {
                String name = round ? "ic_launcher_round" : "ic_launcher";
                save(render(sizes[i], .72, true, round), folder.resolve(name + ".png"));
                Files.deleteIfExists(folder.resolve(name + ".webp"));
            }
        }
        save(render(192, .72, true, true), root.resolve("output/launcher-icon.png"));
    }
}
