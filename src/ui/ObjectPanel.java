
package ui;

import agent.Arrow;
import behaviours.Seek;
import behaviours.Flee;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Point;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.awt.BasicStroke;


public class ObjectPanel extends JPanel {
    // Configurações da flecha
    private Color ArrowColor = Color.RED;
    private int ArrowSize = 24;

    // Configurações do rastro
    private Color TrailColor = Color.ORANGE;
    private float TrailThickness = 2.0f;

    // Configurações da atualização
    private final int UpdateInterval = 16;

    // Objetos e dados da simulação
    private final Arrow Arrow = new Arrow();
    private final Seek Seek = new Seek(Arrow);
    private final Flee Flee = new Flee(Arrow);

    // Seek
    private int SeekPointX;
    private int SeekPointY;
    private boolean HasSeekPoint = false;

    // Flee
    private int FleePointX;
    private int FleePointY;
    private boolean HasFleePoint = false;

    private final ArrayList<Point> TrailPoints = new ArrayList<>();

    private long LastUpdateTime;

    public ObjectPanel() {
        setOpaque(false);

        /*addMouseListener(new java.awt.event.MouseAdapter(){
            @Override
            public void mousePressed(java.awt.event.MouseEvent Event){
                Seek.SetTarget(Event.getX(), Event.getY());
            }
        });*/

        LastUpdateTime = System.nanoTime();

        Timer Timer = new Timer(UpdateInterval, Event -> {
            long CurrentTime = System.nanoTime();
            double DeltaTime = (CurrentTime - LastUpdateTime) / 1_000_000_000.0;
            LastUpdateTime = CurrentTime;

            if(Seek.GetIsActive()){
                Seek.Update(DeltaTime);
            }
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

        //region Seek
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(
                javax.swing.KeyStroke.getKeyStroke("S"), "CreateSeek"
        );
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(
                javax.swing.KeyStroke.getKeyStroke("s"), "CreateSeek"
        );

        getActionMap().put("CreateSeek", new javax.swing.AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent Event) {
                java.awt.Point MousePosition = getMousePosition();

                if (MousePosition != null) {
                    SeekPointX = MousePosition.x;
                    SeekPointY = MousePosition.y;

                    HasSeekPoint = true;
                    Seek.SetTarget(SeekPointX, SeekPointY);

                    repaint();
                }
            }
        });
        //endregion

        //region Flee
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(
                javax.swing.KeyStroke.getKeyStroke("F"), "CreateFlee"
        );

        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(
                javax.swing.KeyStroke.getKeyStroke("f"), "CreateFlee"
        );

        getActionMap().put("CreateFlee", new javax.swing.AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent Event) {
                Point MousePosition = getMousePosition();

                if (MousePosition != null) {
                    FleePointX = MousePosition.x;
                    FleePointY = MousePosition.y;

                    HasFleePoint = true;
                    Flee.SetTarget(FleePointX, FleePointY);

                    repaint();
                }
            }
        });
        //endregion

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
        G2.setColor(TrailColor);
        G2.setStroke(new BasicStroke(TrailThickness));

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

        //Seek Draw
        if (HasSeekPoint) {
            G2.setColor(Color.BLUE);
            G2.fillRect(SeekPointX - 6, SeekPointY - 6, 12, 12);
        }

        // Posiciona e rotaciona o desenho.
        G2.translate(
                Arrow.GetPositionX(),
                Arrow.GetPositionY()
        );

        G2.rotate(Angle);

        G2.setColor(ArrowColor);

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
        HasSeekPoint = false;

        Arrow.Reset();
        Seek.Reset();
        TrailPoints.clear();
        repaint();
    }
}