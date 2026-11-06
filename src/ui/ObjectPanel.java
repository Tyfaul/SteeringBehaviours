package ui;

import agent.Arrow;
import agent.Obstacle;

import behaviours.SteeringManager;
import behaviours.Seek;
import behaviours.Flee;
import behaviours.Wander;
import behaviours.Arrive;
import behaviours.ObstacleAvoidance;

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
    private Color ArrowColor = new Color(255, 0, 0);
    private int ArrowSize = 24;

    // Configurações do rastro
    private Color TrailColor = new Color(255, 124, 0);
    private float TrailThickness = 2.0f;

    // Configurações dos obstáculos
    private int ObstacleSize = 40;
    private final Color ObstacleColor = new Color(139, 90, 43);

    // Configurações da atualização
    private final int UpdateInterval = 16;

    // Objetos e dados da simulação
    private final Arrow Arrow = new Arrow();
    private final Seek Seek = new Seek(Arrow);
    private final Flee Flee = new Flee(Arrow);
    private final Wander Wander = new Wander(Arrow);
    private final Arrive Arrive = new Arrive(Arrow);

    // Seek
    private int SeekPointX;
    private int SeekPointY;
    private boolean HasSeekPoint = false;

    // Flee
    private int FleePointX;
    private int FleePointY;
    private boolean HasFleePoint = false;

    // Arrive
    private int ArrivePointX;
    private int ArrivePointY;
    private boolean HasArrivePoint = false;

    // Obstáculos
    private final ArrayList<Obstacle> Obstacles = new ArrayList<>();

    private final ObstacleAvoidance ObstacleAvoidance =
            new ObstacleAvoidance(Arrow, Obstacles);

    private final SteeringManager SteeringManager =
            new SteeringManager(Arrow, Seek, Flee, Wander, Arrive, ObstacleAvoidance);

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

        //Timers
        LastUpdateTime = System.nanoTime();

        Timer Timer = new Timer(UpdateInterval, Event -> {
            long CurrentTime = System.nanoTime();
            double DeltaTime = (CurrentTime - LastUpdateTime) / 1_000_000_000.0;
            LastUpdateTime = CurrentTime;

            SteeringManager.Update(DeltaTime);

            //region Remoção por Collision
            // Remove o ponto quando o Seek termina
            if (HasSeekPoint && !Seek.GetIsActive()) {
                HasSeekPoint = false;
            }
            // Remove o ponto quando o Arrive termina
            if (HasArrivePoint && !Arrive.GetIsActive()) {
                HasArrivePoint = false;
            }
            //endregion

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
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(javax.swing.KeyStroke.getKeyStroke("S"), "CreateSeek");
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(javax.swing.KeyStroke.getKeyStroke("s"), "CreateSeek");

        getActionMap().put("CreateSeek", new javax.swing.AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent Event) {
                java.awt.Point MousePosition = getMousePosition();

                if (MousePosition != null) {
                    Obstacles.add(new Obstacle(MousePosition.x, MousePosition.y, ObstacleSize));
                    repaint();
                }
            }
        });
        //endregion

        //region Flee
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(javax.swing.KeyStroke.getKeyStroke("F"), "CreateFlee");
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(javax.swing.KeyStroke.getKeyStroke("f"), "CreateFlee");

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

        //region Arrive
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(javax.swing.KeyStroke.getKeyStroke("A"), "CreateArrive");
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(javax.swing.KeyStroke.getKeyStroke("a"), "CreateArrive");

        getActionMap().put("CreateArrive", new javax.swing.AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent Event) {
                Point MousePosition = getMousePosition();

                if (MousePosition != null) {
                    ArrivePointX = MousePosition.x;
                    ArrivePointY = MousePosition.y;

                    HasArrivePoint = true;
                    Arrive.SetTarget(ArrivePointX, ArrivePointY);
                    repaint();
                }
            }
        });
//endregion

        //region Obstacle
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(javax.swing.KeyStroke.getKeyStroke("O"), "CreateObstacle");
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(javax.swing.KeyStroke.getKeyStroke("o"), "CreateObstacle");

        getActionMap().put("CreateObstacle", new javax.swing.AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent Event) {
                Point MousePosition = getMousePosition();

                if (MousePosition != null) {Obstacles.add(new Obstacle(MousePosition.x, MousePosition.y, ObstacleSize));
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

        G2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Desenha o rastro.
        G2.setColor(TrailColor);
        G2.setStroke(new BasicStroke(TrailThickness));

        for (int i = 1; i < TrailPoints.size(); i++) {
            Point PreviousPoint = TrailPoints.get(i - 1);
            Point CurrentPoint = TrailPoints.get(i);

            G2.drawLine(PreviousPoint.x, PreviousPoint.y, CurrentPoint.x, CurrentPoint.y);
        }

        // Calcula a direção atual da flecha.
        double Angle = Math.atan2(Arrow.GetVelocityY(), Arrow.GetVelocityX());

        //Seek Draw
        if (HasSeekPoint) {
            G2.setColor(Color.BLUE);
            G2.fillRect(SeekPointX - 6, SeekPointY - 6, 12, 12);
        }

        //Flee Draw
        if (HasFleePoint) {
            G2.setColor(Color.RED);
            G2.fillRect(FleePointX - 6, FleePointY - 6, 12, 12);
        }

        // Arrive Draw
        if (HasArrivePoint) {
            G2.setColor(Color.MAGENTA);
            G2.fillRect(ArrivePointX - 6, ArrivePointY - 6, 12, 12);
        }

        // Obstacles Draw
        G2.setColor(ObstacleColor);

        for (Obstacle Obstacle : Obstacles) {
            int Size = Obstacle.GetSize();
            G2.fillRect((int) (Obstacle.GetPositionX() - Size / 2.0), (int) (Obstacle.GetPositionY() - Size / 2.0), Size, Size);
        }

        // Posiciona e rotaciona o desenho.
        G2.translate(Arrow.GetPositionX(), Arrow.GetPositionY());
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
        //Bool
        HasSeekPoint = false;
        HasFleePoint = false;
        HasArrivePoint = false;

        //Resets
        Arrow.Reset();
        Seek.Reset();
        Flee.Reset();
        Wander.Reset();
        Arrive.Reset();

        //Clear
        TrailPoints.clear();
        Obstacles.clear();
        //Paint
        repaint();
    }

    // Tamanho dos obstáculos
    public int GetObstacleSize() {return ObstacleSize;}
    public void SetObstacleSize(int ObstacleSize) {this.ObstacleSize = Math.max(5, ObstacleSize);}

    public SteeringManager GetSteeringManager() {return SteeringManager;}

    // Espessura do rastro
    public float GetTrailThickness() {return TrailThickness;}

    public void SetTrailThickness(float TrailThickness) {
        this.TrailThickness = Math.max(0.5f, TrailThickness);
        repaint();
    }

    // Cor do rastro
    public Color GetTrailColor() {return TrailColor;}

    public void SetTrailColor(Color TrailColor) {
        this.TrailColor = TrailColor;
        repaint();
    }
}