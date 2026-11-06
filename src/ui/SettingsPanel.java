package ui;

import behaviours.SteeringManager;

import javax.swing.*;
import java.awt.*;
import java.util.Locale;
import java.util.function.DoubleConsumer;

public class SettingsPanel extends JPanel {

    private final double WeightStep = 0.05;

    private final SteeringManager SteeringManager;
    private final ObjectPanel ObjectPanel;

    public SettingsPanel(
            SteeringManager SteeringManager,
            ObjectPanel ObjectPanel) {

        this.SteeringManager = SteeringManager;
        this.ObjectPanel = ObjectPanel;

        setPreferredSize(new Dimension(350, 700));
        setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel Title = new JLabel("Configurações");
        Title.setFont(new Font("Arial", Font.BOLD, 20));

        add(Title);
        add(Box.createVerticalStrut(25));

        // Pesos
        JLabel WeightTitle = new JLabel("Pesos dos comportamentos");
        WeightTitle.setFont(new Font("Arial", Font.BOLD, 14));

        add(WeightTitle);
        add(Box.createVerticalStrut(15));

        add(CreateWeightRow("Arrive", SteeringManager.GetArriveWeight(), SteeringManager::SetArriveWeight));
        add(Box.createVerticalStrut(10));

        add(CreateWeightRow("Flee", SteeringManager.GetFleeWeight(), SteeringManager::SetFleeWeight));
        add(Box.createVerticalStrut(10));

        add(CreateWeightRow("Seek", SteeringManager.GetSeekWeight(), SteeringManager::SetSeekWeight));
        add(Box.createVerticalStrut(10));

        add(CreateWeightRow("Wander", SteeringManager.GetWanderWeight(), SteeringManager::SetWanderWeight));
        add(Box.createVerticalStrut(25));

        add(CreateWeightRow("Obstacle", SteeringManager.GetObstacleAvoidanceWeight(), Value -> { //Mesmo de Path Following
            SteeringManager.SetObstacleAvoidanceWeight(Value);
            if (Value == 0) {ObjectPanel.repaint();}}));

        add(CreateWeightRow("Path Following", SteeringManager.GetPathFollowingWeight(), Value -> {
            SteeringManager.SetPathFollowingWeight(Value);
            if (Value == 0) {ObjectPanel.repaint();}}));
        add(Box.createVerticalStrut(25));

        // Movimento
        JLabel MovementTitle = new JLabel("Movimento");
        MovementTitle.setFont(new Font("Arial", Font.BOLD, 14));

        add(MovementTitle);
        add(Box.createVerticalStrut(15));

        add(CreateValueRow("Velocidade", SteeringManager.GetMaxSpeed(), 10, 0, 1000, SteeringManager::SetMaxSpeed));
        add(Box.createVerticalStrut(10));

        add(CreateValueRow("Raio", SteeringManager.GetInfluenceRadius(), 10, 0, 1000, SteeringManager::SetInfluenceRadius));
        add(Box.createVerticalStrut(25));

        // Obstáculos
        JLabel ObstacleTitle = new JLabel("Obstáculos");
        ObstacleTitle.setFont(new Font("Arial", Font.BOLD, 14));

        add(ObstacleTitle);
        add(Box.createVerticalStrut(15));

        add(CreateValueRow("Tamanho", ObjectPanel.GetObstacleSize(), 5, 5, 200, Value -> ObjectPanel.SetObstacleSize((int) Value)));
        add(Box.createVerticalStrut(25));

        // Aparência
        JLabel AppearanceTitle = new JLabel("Aparência");
        AppearanceTitle.setFont(new Font("Arial", Font.BOLD, 14));

        add(AppearanceTitle);
        add(Box.createVerticalStrut(15));

        add(CreateValueRow("Espessura", ObjectPanel.GetTrailThickness(), 0.5, 0.5, 20, Value -> ObjectPanel.SetTrailThickness((float) Value)));
        add(Box.createVerticalStrut(10));

        add(CreateColorRow());
        add(Box.createVerticalGlue());
    }

    private JPanel CreateWeightRow(String Name, double InitialValue, DoubleConsumer Setter) {
        return CreateValueRow(Name, InitialValue, WeightStep, 0, 1, Setter);
    }

    private JPanel CreateValueRow(String Name, double InitialValue, double Step, double Minimum, double Maximum, DoubleConsumer Setter) {

        JPanel Row = new JPanel(new GridBagLayout());
        Row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));

        GridBagConstraints Constraints = new GridBagConstraints();
        Constraints.gridy = 0;
        Constraints.insets = new Insets(0, 2, 0, 2);

        JLabel NameLabel = new JLabel(Name);
        NameLabel.setPreferredSize(new Dimension(110, 30)); //Espaço dos nomes

        Constraints.gridx = 0;
        Constraints.anchor = GridBagConstraints.WEST;
        Row.add(NameLabel, Constraints);

        double[] CurrentValue = {InitialValue};

        JLabel ValueLabel = new JLabel(FormatValue(CurrentValue[0]));
        ValueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        ValueLabel.setPreferredSize(new Dimension(45, 30));

        JButton DecreaseButton = new JButton("←");
        JButton IncreaseButton = new JButton("→");

        Dimension ButtonSize = new Dimension(30, 30);

        DecreaseButton.setPreferredSize(ButtonSize);
        IncreaseButton.setPreferredSize(ButtonSize);

        DecreaseButton.setMargin(new Insets(0, 0, 0, 0));
        IncreaseButton.setMargin(new Insets(0, 0, 0, 0));

        DecreaseButton.addActionListener(Event -> {
            CurrentValue[0] = Math.max(
                    Minimum, CurrentValue[0] - Step
            );
            Setter.accept(CurrentValue[0]);
            ValueLabel.setText(FormatValue(CurrentValue[0]));
        });

        IncreaseButton.addActionListener(Event -> {
            CurrentValue[0] = Math.min(
                    Maximum, CurrentValue[0] + Step
            );
            Setter.accept(CurrentValue[0]);
            ValueLabel.setText(FormatValue(CurrentValue[0]));
        });

        Constraints.gridx = 1;
        Row.add(DecreaseButton, Constraints);

        Constraints.gridx = 2;
        Row.add(ValueLabel, Constraints);

        Constraints.gridx = 3;
        Row.add(IncreaseButton, Constraints);

        return Row;
    }

    private JPanel CreateColorRow() {

        JPanel Row = new JPanel(new BorderLayout(5, 0));
        Row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));

        JLabel NameLabel = new JLabel("Cor do rastro");

        JButton ColorButton = new JButton("Escolher");
        ColorButton.setBackground(ObjectPanel.GetTrailColor());
        ColorButton.setOpaque(true);

        ColorButton.addActionListener(Event -> {
            Color SelectedColor = JColorChooser.showDialog(this, "Escolher cor do rastro", ObjectPanel.GetTrailColor());

            if (SelectedColor != null) {
                ObjectPanel.SetTrailColor(SelectedColor);
                ColorButton.setBackground(SelectedColor);
            }
        });

        Row.add(NameLabel, BorderLayout.WEST);
        Row.add(ColorButton, BorderLayout.CENTER);
        return Row;
    }

    private String FormatValue(double Value) {
        return String.format(Locale.US, "%.1f", Value);
    }
}