import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.*;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.io.*;
import java.awt.Color;

public class grafikpanel extends JPanel implements ActionListener {

    // Creates a button
    JButton upload = new JButton();
    JButton save = new JButton();
    JButton monochrome = new JButton();
    JLabel photo = new JLabel();

    JLabel pixels = new JLabel();
    JButton rotate = new JButton();
    JButton reset = new JButton();
    Graphics2D g;
    BufferedImage picture = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);

    public grafikpanel() throws IOException {

        upload.setText("Upload an image"); // Title on the button
        upload.setBounds(new Rectangle(0, 0, 75, 20)); // placement and dimensions of button
        upload.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                upload_actionPerformed(e); // runs the method when button is clicked
            }
        });

        save.setText("Save the image"); // Title on the button
        save.setBounds(0, 0, 150, 32); // placement and dimensions of button
        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                save_actionPerformed(e); // runs the method when button is clicked
            }
        });

        rotate.setText("Flip"); //
        rotate.setBounds(0, 0, 160, 32);
        rotate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                rotate_actionPerformed(e); // runs the method when button is clicked
            }
        });

        monochrome.setText("Monochrome"); //
        monochrome.setBounds(0, 0, 160, 32);
        monochrome.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                monochrome_actionPerformed(e); // runs the method when button is clicked
            }
        });

        reset.setText("Reset"); // Title on the button
        reset.setBounds(0, 0, 150, 32); // placement and dimensions of button
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                reset_actionPerformed(e); // runs the method when button is clicked
            }
        });

        this.add(save);
        this.add(upload);
        this.add(rotate);
        this.add(monochrome);
        this.add(reset);
        this.repaint();

    }

    public void paintComponent(Graphics g) {

    }

    public void upload_actionPerformed(ActionEvent e) { // Method

        if (e.getSource() == upload) {

            JFileChooser file = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "JPG & png Images", "jpg", "png");
            file.setFileFilter(filter);

            if (file.showOpenDialog(getParent()) == JFileChooser.APPROVE_OPTION) {
                try {
                    photo.removeAll();
                    File path = file.getSelectedFile();
                    picture = ImageIO.read(path);
                    photo.setIcon(new ImageIcon(picture));
                    add(photo);

                    for (int y = 0; y < photo.getHeight(); y++) {
                        for (int x = 0; x < photo.getWidth(); x++) {
                            int pixel = picture.getRGB(x, y);
                            Color color = new Color(pixel, true);

                            int red = color.getRed();
                            int blue = color.getBlue();
                            int green = color.getGreen();
                            int alhpa = color.getAlpha();

                            System.out.println(
                                    "red = " + red + " blue = " + blue + " green = " + green + " alpha = " + alhpa);

                        }

                    }
                    photo.repaint();
                    this.repaint();

                } catch (IOException ioe) {
                    ioe.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error");
                }
            }

        }

    }

    public void save_actionPerformed(ActionEvent e) {
        if (e.getSource() == save) {
            JFileChooser save_file = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "JPG & png Images", "jpg", "png");
            save_file.setFileFilter(filter);

            if (save_file.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                try {
                    File path = save_file.getSelectedFile();
                    var ic = ((ImageIcon) photo.getIcon()).getImage();

                    ImageIO.write(imageToBufferedImage(ic), "JPG", path);
                } catch (IOException i) {
                    i.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error");
                }
            }
        }
    }

    public BufferedImage rotate_actionPerformed(ActionEvent e) {

        var ic = ((ImageIcon) photo.getIcon()).getImage();
        BufferedImage rotated90 = rotate(imageToBufferedImage(ic), 90.0d);
        photo.removeAll();
        photo.setIcon(new ImageIcon(rotated90));
        photo.repaint();
        return rotated90;
    }

    public static BufferedImage imageToBufferedImage(Image im) {
        BufferedImage bi = new BufferedImage(im.getWidth(null), im.getHeight(null), BufferedImage.TYPE_INT_RGB);
        Graphics bg = bi.getGraphics();
        bg.drawImage(im, 0, 0, null);
        bg.dispose();
        return bi;
    }

    public BufferedImage monochrome_actionPerformed(ActionEvent e) {
        var ic = ((ImageIcon) photo.getIcon()).getImage();

        BufferedImage result = imageToBufferedImage(ic);

        // get image width and height
        int width = ic.getWidth(null);
        int height = ic.getHeight(null);

        // convert to grayscale
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int p = result.getRGB(x, y);

                int a = (p >> 24) & 0xff;
                int r = (p >> 16) & 0xff;
                int g = (p >> 8) & 0xff;
                int b = p & 0xff;

                // calculate average
                int avg = (r + g + b) / 3;

                // replace RGB value with avg
                p = (a << 24) | (avg << 16) | (avg << 8) | avg;

                result.setRGB(x, y, p);
            }
        }

        photo.removeAll();
        photo.setIcon(new ImageIcon(result));
        photo.repaint();
        return result;
    }

    public static BufferedImage rotate(BufferedImage image, Double degrees) {
        // Calculate the new size of the image based on the angle of rotaion
        double radians = Math.toRadians(degrees);

        
        double sin = Math.abs(Math.sin(radians));
        double cos = Math.abs(Math.cos(radians));
        int newWidth = (int) Math.round(image.getWidth() * cos + image.getHeight() * sin);


        int newHeight = (int) Math.round(image.getWidth() * sin + image.getHeight() * cos);

        // Create a new image
        BufferedImage rotate = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);


        Graphics2D g2d = rotate.createGraphics();
        // Calculate the "anchor" point around which the image will be rotated
        int x = (newWidth - image.getWidth()) / 2;
        int y = (newHeight - image.getHeight()) / 2;
        // Transform the origin point around the anchor point
        AffineTransform at = new AffineTransform();
                  at.setToRotation(radians, x + (image.getWidth() / 2), y + (image.getHeight() / 2));
        at.translate(x, y);
        g2d.setTransform(at);
        // Paint the originl image
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();
        return rotate;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    public void reset_actionPerformed(ActionEvent e) {
        photo.setIcon(new ImageIcon(picture));
        photo.repaint();

    }

}