
package ui;

import javax.swing.JLayeredPane;
import java.awt.Dimension;

public class SimulationPanel extends JLayeredPane {

    private final DrawPanel DrawPanel = new DrawPanel();
    private final ObjectPanel ObjectPanel = new ObjectPanel();

    public SimulationPanel() {
        setPreferredSize(new Dimension(1000, 700));
        setOpaque(true);

        add(DrawPanel, Integer.valueOf(0));
        add(ObjectPanel, Integer.valueOf(1));
    }

    @Override
    public void doLayout() {
        DrawPanel.setBounds(0, 0, getWidth(), getHeight());
        ObjectPanel.setBounds(0, 0, getWidth(), getHeight());
    }

    public void Reset() {
        ObjectPanel.Reset();
    }
}