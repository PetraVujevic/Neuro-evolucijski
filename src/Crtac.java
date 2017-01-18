import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

@SuppressWarnings("serial")
public class Crtac extends JFrame {

    class MyPanel extends JPanel {
        public MyPanel() {
            super.setPreferredSize(new Dimension(800, 800));
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            for (int i = 0; i < x.size(); i++) {
                int xx = (int) (x.get(i) * 750);
                int yy = (int) (y.get(i) * 750);
                g.setColor(colors.get(i));
                g.fillOval(xx, yy, 10, 10);
                DecimalFormat df = new DecimalFormat("#.##");

                g.drawString(
                        "(" + df.format(x.get(i)) + "," + df.format(y.get(i))
                                + ")", xx, yy);
            }
        }
    }

    JPanel panel;
    ArrayList<Double> x = new ArrayList<Double>();
    ArrayList<Double> y = new ArrayList<Double>();
    ArrayList<Color> colors = new ArrayList<Color>();

    public Crtac() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(800, 800);
        panel = new MyPanel();
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(panel, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        Crtac crtac = new Crtac();
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                crtac.setVisible(true);
            }
        });
        try {
            Scanner sc = new Scanner(Paths.get("tocke"));
            while (sc.hasNextLine()) {
                double x = sc.nextDouble();
                double y = sc.nextDouble();
                int b0 = sc.nextInt();
                int b1 = sc.nextInt();
                sc.nextInt();
                crtac.x.add(x);
                crtac.y.add(y);
                Color color;
                if (b0 == 1) {
                    color = Color.BLUE;
                } else if (b1 == 1) {
                    color = Color.RED;
                } else {
                    color = Color.ORANGE;
                }
                crtac.colors.add(color);

            }
            crtac.repaint();
            sc.close();
        } catch (IOException e) {
            System.out.println("Ne mogu procitati 'tocke'");
        }
    }
}
