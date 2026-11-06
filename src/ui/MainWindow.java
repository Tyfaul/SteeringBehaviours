package ui;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.AbstractAction;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class MainWindow extends JFrame {

    // Configurações da janela
    private final int Width = 1920;
    private final int Height = 1080;
    private final int MinimumWidth = 600;
    private final int MinimumHeight = 400;

    // Elementos da interface
    private final SimulationPanel SimulationPanel = new SimulationPanel();

    private final SettingsPanel SettingsPanel = new SettingsPanel(
            SimulationPanel.GetSteeringManager(),
            SimulationPanel.GetObjectPanel()
    );

    private final JButton ResetButton = new JButton("Reset");

    public MainWindow() {

        setTitle("Steering Behaviours");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setSize(Width, Height);
        setMinimumSize(new Dimension(MinimumWidth, MinimumHeight));
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        JPanel BottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        BottomPanel.add(ResetButton);

        add(SimulationPanel, BorderLayout.CENTER);
        add(SettingsPanel, BorderLayout.EAST);
        add(BottomPanel, BorderLayout.SOUTH);

        ResetButton.addActionListener(Event -> ResetSimulation());

        // Configura a tecla R para executar o reset
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_R, 0), "ResetSimulation");

        getRootPane().getActionMap().put("ResetSimulation", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent Event) {
                ResetSimulation();
            }
        });
    }

    private void ResetSimulation() {
        SimulationPanel.Reset();
    }

    public void Initialize() {
        setVisible(true);
    }
}