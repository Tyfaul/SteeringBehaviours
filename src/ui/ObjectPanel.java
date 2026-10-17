package ui;

import javax.swing.Timer;
import agent.Arrow;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.RenderingHints;

public class ObjectPanel extends JPanel {

    //Criação da Flecha
    private final Arrow Arrow = new Arrow(500, 350, 0, 50);

    private long LastUpdateTime;

    public ObjectPanel() {
        setOpaque(false);

        LastUpdateTime = System.nanoTime();

        Timer Timer = new Timer(16, Event -> {
            long CurrentTime = System.nanoTime();
            double DeltaTime = (CurrentTime - LastUpdateTime) / 1_000_000_000.0;
            LastUpdateTime = CurrentTime;

            Arrow.Update(DeltaTime, getWidth() - 1, getHeight() - 1);
            repaint();
        });
        Timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D G2 = (Graphics2D) g.create();

        G2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        G2.setColor(Color.ORANGE);

        double Angle = Math.atan2( //Cálculo de ângulo
                Arrow.GetVelocityY(),
                Arrow.GetVelocityX()
        );

        G2.translate(
                Arrow.GetPositionX(),
                Arrow.GetPositionY()
        );

        G2.rotate(Angle);

        int ArrowSize = 24;

        int[] PointsX = {
                -ArrowSize,
                0,
                -ArrowSize
        };

        int[] PointsY = {
                -ArrowSize,
                0,
                ArrowSize
        };

        G2.drawPolyline(PointsX, PointsY, 3);

        G2.dispose();
    }
}