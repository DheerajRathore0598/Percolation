import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class InteractivePercolationVisualizer extends JPanel {
    private final Percolation perc;
    private final int n;
    private final int gridSize = 600;
    private final int statusBarHeight = 50;
    private final int cellSize;

    public InteractivePercolationVisualizer(Percolation perc, int n) {
        this.perc = perc;
        this.n = n;
        this.cellSize = gridSize / n;
        setPreferredSize(new Dimension(gridSize, gridSize + statusBarHeight));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int col = e.getX() / cellSize + 1;       // 1-indexed
                int row = n - (e.getY() / cellSize);     // flip y
                if (row >= 1 && row <= n && col >= 1 && col <= n) {
                    if (!perc.isOpen(row, col)) {
                        System.out.println(row + " " + col);
                        perc.open(row, col);
                        repaint();
                    }
                }
            }
        });
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

        g.setColor(Color.BLACK);
        g.setFont(new Font("SansSerif", Font.PLAIN, 14));
        g.drawString(opened + " open sites", 20, gridSize + 30);
        String status = perc.percolates() ? "percolates" : "does not percolate";
        g.drawString(status, gridSize - 200, gridSize + 30);
    }

    public static void main(String[] args) {
        int n = 10;
        if (args.length == 1) n = Integer.parseInt(args[0]);

        System.out.println(n);
        Percolation perc = new Percolation(n);

        JFrame frame = new JFrame("Interactive Percolation Visualizer");
        InteractivePercolationVisualizer panel = new InteractivePercolationVisualizer(perc, n);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
