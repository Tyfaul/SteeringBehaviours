
package ui;

import agent.Arrow;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Point;
import java.awt.RenderingHints;
import java.util.ArrayList;

public class ObjectPanel extends JPanel {

    private final Arrow Arrow = new Arrow(500, 350, 150, 0);
    private final ArrayList<Point> TrailPoints = new ArrayList<>();

    private long LastUpdateTime;

    public ObjectPanel() {
        setOpaque(false);

        LastUpdateTime = System.nanoTime();

        Timer Timer = new Timer(16, Event -> {
            long CurrentTime = System.nanoTime();
            double DeltaTime = (CurrentTime - LastUpdateTime) / 1_000_000_000.0;
            LastUpdateTime = CurrentTime;

            Arrow.Update(DeltaTime, getWidth() - 1, getHeight() - 1);

            Point CurrentPoint = new Point(
                    (int) Arrow.GetPositionX(),
                    (int) Arrow.GetPositionY()
            );

            if (TrailPoints.isEmpty()
                    || !TrailPoints.get(TrailPoints.size() - 1).equals(CurrentPoint)) {
                TrailPoints.add(CurrentPoint);
            }

            // Mantém o histórico limitado, caso seja necessário no futuro.
            // if (TrailPoints.size() > 300) {
            //     TrailPoints.remove(0);
            // }

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

        // Desenha o rastro.
        G2.setColor(Color.RED);

        for (int i = 1; i < TrailPoints.size(); i++) {
            Point PreviousPoint = TrailPoints.get(i - 1);
            Point CurrentPoint = TrailPoints.get(i);

            G2.drawLine(
                    PreviousPoint.x,
                    PreviousPoint.y,
                    CurrentPoint.x,
                    CurrentPoint.y
            );
        }

        // Calcula a direção atual da flecha.
        double Angle = Math.atan2(
                Arrow.GetVelocityY(),
                Arrow.GetVelocityX()
        );

        // Posiciona e rotaciona o desenho.
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

    public void Reset() {
        Arrow.Reset();
        TrailPoints.clear();
        repaint();
    }
}