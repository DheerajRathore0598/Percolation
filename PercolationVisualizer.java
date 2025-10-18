import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class PercolationVisualizer extends JPanel {
    private final Percolation perc;
    private final int n;
    private final int gridSize = 600;
    private final int statusBarHeight = 50;
    private final int cellSize;

    public PercolationVisualizer(Percolation perc, int n) {
        this.perc = perc;
        this.n = n;
        this.cellSize = gridSize / n;
        setPreferredSize(new Dimension(gridSize, gridSize + statusBarHeight));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int opened = 0;
        for (int row = 1; row <= n; row++) {
            for (int col = 1; col <= n; col++) {
                if (perc.isFull(row, col)) {
                    g.setColor(Color.CYAN);
                    opened++;
                }
                else if (perc.isOpen(row, col)) {
                    g.setColor(Color.WHITE);
                    opened++;
                }
                else {
                    g.setColor(Color.BLACK);
                }
                g.fillRect((col - 1) * cellSize, (row - 1) * cellSize, cellSize, cellSize);
                g.setColor(Color.GRAY);
                g.drawRect((col - 1) * cellSize, (row - 1) * cellSize, cellSize, cellSize);
            }
        }

        // status bar text BELOW the grid
        g.setColor(Color.BLACK);
        g.setFont(new Font("SansSerif", Font.BOLD, 16));

        // left side: open sites
        g.drawString(opened + " open sites", 20, gridSize + 30);

        // right side: percolation status
        String status = perc.percolates() ? "Percolates" : "Does not percolate";
        g.drawString(status, gridSize - 200, gridSize + 30);
    }

    public static void main(String[] args) throws FileNotFoundException {
        if (args.length == 0) {
            System.out.println("Usage: java PercolationVisualizer <inputfile>");
            return;
        }

        Scanner sc = new Scanner(new File(args[0]));
        int n = sc.nextInt();
        Percolation perc = new Percolation(n);

        JFrame frame = new JFrame("Percolation Visualizer");
        PercolationVisualizer panel = new PercolationVisualizer(perc, n);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        while (sc.hasNext()) {
            int i = sc.nextInt();
            int j = sc.nextInt();
            perc.open(i, j);
            panel.repaint();
            try {
                Thread.sleep(100);
            }
            catch (InterruptedException ignored) {
            }
        }
    }
}
