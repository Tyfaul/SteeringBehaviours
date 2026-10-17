
package ui;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Dimension;

public class MainWindow extends JFrame {

    private final int Width = 1000;
    private final int Height = 700;

    private final SimulationPanel SimulationPanel = new SimulationPanel();

    public MainWindow() {
        setTitle("Steering Behaviours");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setSize(Width, Height);
        setMinimumSize(new Dimension(600, 400));
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());
        add(SimulationPanel, BorderLayout.CENTER);
    }

    public void Initialize() {
        setVisible(true);
    }
}