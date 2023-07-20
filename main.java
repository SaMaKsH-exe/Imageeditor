import java.awt.*;
import java.io.IOException;
import javax.swing.*;

public class main extends grafikpanel {
    public static void main(String[] args) throws IOException {

        grafikpanel panel = new grafikpanel();
        JFrame window = new JFrame("Photoshop");
        window.add(panel);
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        window.setSize(500, 500);
        window.setVisible(true);
        Color c = new Color(35, 35, 35);

        window.setBackground(c);
    }
}
