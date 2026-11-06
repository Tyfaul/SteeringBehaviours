package ui;

import behaviours.SteeringManager;
import javax.swing.JLayeredPane;
import java.awt.Dimension;

public class SimulationPanel extends JLayeredPane {

    // Configurações da área de simulação
    private final int SimulationWidth = 1000;
    private final int SimulationHeight = 700;

    // Elementos da simulação
    private final DrawPanel DrawPanel = new DrawPanel();
    private final ObjectPanel ObjectPanel = new ObjectPanel();

    public SimulationPanel() {
        setPreferredSize(new Dimension(SimulationWidth, SimulationHeight));
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

    public SteeringManager GetSteeringManager() {
        return ObjectPanel.GetSteeringManager();
    }

    public ObjectPanel GetObjectPanel() {
        return ObjectPanel;
    }
}