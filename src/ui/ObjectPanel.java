package ui;

import agent.Arrow;
import agent.Obstacle;
import behaviours.SteeringManager;
import behaviours.Seek;
import behaviours.Flee;
import behaviours.Wander;
import behaviours.Arrive;
import behaviours.ObstacleAvoidance;
import behaviours.PathFollowing;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.BasicStroke;
import java.util.ArrayList;

public class ObjectPanel extends JPanel {
    // Configurações da flecha
    private Color ArrowColor = new Color(255, 124, 0);
    private int ArrowSize = 24;

    // Configurações do rastro
    private Color TrailColor = new Color(255, 124, 0);
    private float TrailThickness = 2.0f;


    // Configurações das cores dos comportamentos
    private Color SeekColor = new Color(0, 0, 255);
    private Color FleeColor = new Color(255, 0, 0);
    private Color ArriveColor = new Color(48, 255, 0);
    private final Color ObstacleColor = new Color(139, 90, 43);
    private Color PathColor = new Color(220, 0, 255);

    // Configurações dos obstáculos
    private int ObstacleSize = 40;

    // Configurações da atualização
    private final int UpdateInterval = 16;

    // Objetos e dados da simulação
    private final Arrow Arrow = new Arrow();
    private final Seek Seek = new Seek(Arrow);
    private final Flee Flee = new Flee(Arrow);
    private final Wander Wander = new Wander(Arrow);
    private final Arrive Arrive = new Arrive(Arrow);

    // Seek
    private int SeekPointX, SeekPointY;
    private boolean HasSeekPoint = false;

    // Flee
    private int FleePointX, FleePointY;
    private boolean HasFleePoint = false;

    // Arrive
    private int ArrivePointX, ArrivePointY;
    private boolean HasArrivePoint = false;

    private final ArrayList<Point> PathPoints = new ArrayList<>();
    private final PathFollowing PathFollowing = new PathFollowing(Arrow, PathPoints);
    private boolean PathFollowingActive = false;

    // Obstáculos
    private final ArrayList<Obstacle> Obstacles = new ArrayList<>();
    private final ObstacleAvoidance ObstacleAvoidance = new ObstacleAvoidance(Arrow, Obstacles);

    //Inicialização
    private final SteeringManager SteeringManager = new SteeringManager(Arrow, Seek, Flee, Wander, Arrive, ObstacleAvoidance, PathFollowing);
    private final ArrayList<Point> TrailPoints = new ArrayList<>();
    private long LastUpdateTime;

    public ObjectPanel() {
        setOpaque(false);
        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent Event) {
                PathPoints.add(Event.getPoint());
                repaint();
            }
        });
        LastUpdateTime = System.nanoTime();

        Timer Timer = new Timer(UpdateInterval, Event -> {
            long CurrentTime = System.nanoTime();
            double DeltaTime = (CurrentTime - LastUpdateTime) / 1_000_000_000.0;
            LastUpdateTime = CurrentTime;
            SteeringManager.Update(DeltaTime);

            //region Remoção por Collision
            // Remove o ponto quando o Seek termina
            if (HasSeekPoint && !Seek.GetIsActive()) HasSeekPoint = false;

            // Remove o ponto quando o Arrive termina
            if (HasArrivePoint && !Arrive.GetIsActive()) HasArrivePoint = false;
            //endregion

            Arrow.Update(DeltaTime, getWidth() - 1, getHeight() - 1);

            Point CurrentPoint = new Point((int) Arrow.GetPositionX(), (int) Arrow.GetPositionY());
            if (TrailPoints.isEmpty() || !TrailPoints.get(TrailPoints.size() - 1).equals(CurrentPoint)) TrailPoints.add(CurrentPoint);
            repaint();
        });

        //region Seek
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(javax.swing.KeyStroke.getKeyStroke("S"), "CreateSeek");
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(javax.swing.KeyStroke.getKeyStroke("s"), "CreateSeek");

        getActionMap().put("CreateSeek", new javax.swing.AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent Event) {
                java.awt.Point MousePosition = getMousePosition();
                SeekPointX = MousePosition.x;
                SeekPointY = MousePosition.y;
                HasSeekPoint = true;
                Seek.SetTarget(SeekPointX, SeekPointY);
                repaint();
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
                if (MousePosition != null) {
                    Obstacles.add(new Obstacle(MousePosition.x, MousePosition.y, ObstacleSize));
                    repaint();
                }
            }
        });
        //endregion

        //region Path Following
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(javax.swing.KeyStroke.getKeyStroke("P"), "TogglePath");
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(javax.swing.KeyStroke.getKeyStroke("p"), "TogglePath");

        getActionMap().put("TogglePath", new javax.swing.AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent Event) {
                PathFollowingActive = !PathFollowingActive;
                PathFollowing.Reset();
                SteeringManager.SetPathFollowingActive(PathFollowingActive);
                repaint();
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
            Point PreviousPoint = TrailPoints.get(i - 1), CurrentPoint = TrailPoints.get(i);
            G2.drawLine(PreviousPoint.x, PreviousPoint.y, CurrentPoint.x, CurrentPoint.y);
        }

        // Calcula a direção atual da flecha.
        double Angle = Math.atan2(Arrow.GetVelocityY(), Arrow.GetVelocityX());

        //Seek Draw
        if (HasSeekPoint) {G2.setColor(SeekColor);G2.fillRect(SeekPointX - 6, SeekPointY - 6, 12, 12);}

        //Flee Draw
        if (HasFleePoint) {G2.setColor(FleeColor);G2.fillRect(FleePointX - 6, FleePointY - 6, 12, 12);}

        // Arrive Draw
        if (HasArrivePoint) {G2.setColor(ArriveColor);G2.fillRect(ArrivePointX - 6, ArrivePointY - 6, 12, 12);}

        // Obstacles Draw
        G2.setColor(ObstacleColor);
        for (Obstacle Obstacle : Obstacles) {
            int Size = Obstacle.GetSize();
            G2.fillRect((int) (Obstacle.GetPositionX() - Size / 2.0), (int) (Obstacle.GetPositionY() - Size / 2.0), Size, Size);
        }

        // Path Following Draw
        G2.setColor(PathColor);
        G2.setStroke(new BasicStroke(3));
        for (int i = 0; i < PathPoints.size(); i++) {
            Point Point = PathPoints.get(i);
            G2.fillOval(Point.x - 5, Point.y - 5, 10, 10);
            if (i > 0) {
                Point Previous = PathPoints.get(i - 1);
                G2.drawLine(Previous.x, Previous.y, Point.x, Point.y);
            }
        }

        if (PathPoints.size() > 1) {
            Point First = PathPoints.get(0), Last = PathPoints.get(PathPoints.size() - 1);
            G2.drawLine(Last.x, Last.y, First.x, First.y);
        }

        // Posiciona e rotaciona o desenho.
        G2.translate(Arrow.GetPositionX(), Arrow.GetPositionY());
        G2.rotate(Angle);
        G2.setColor(ArrowColor);
        int[] PointsX = {-ArrowSize, 0, -ArrowSize};
        int[] PointsY = {-ArrowSize, 0, ArrowSize};
        G2.drawPolyline(PointsX, PointsY, 3);
        G2.dispose();
    }

    public void Reset() {
        //Bool
        HasSeekPoint = false;
        HasFleePoint = false;
        HasArrivePoint = false;
        PathFollowingActive = false;
        SteeringManager.SetPathFollowingActive(false);

        //Resets
        Arrow.Reset();
        Seek.Reset();
        Flee.Reset();
        Wander.Reset();
        Arrive.Reset();
        PathFollowing.Reset();

        //Clear
        TrailPoints.clear();
        Obstacles.clear();
        PathPoints.clear();

        repaint();
    }

    // Tamanho dos obstáculos
    public int GetObstacleSize() { return ObstacleSize; }
    public void SetObstacleSize(int ObstacleSize) { this.ObstacleSize = Math.max(5, ObstacleSize); }
    public SteeringManager GetSteeringManager() { return SteeringManager; }

    // Espessura do rastro
    public float GetTrailThickness() { return TrailThickness; }
    public void SetTrailThickness(float TrailThickness) {
        this.TrailThickness = Math.max(0.5f, TrailThickness);
        repaint();
    }

    // Cor do rastro
    public Color GetTrailColor() { return TrailColor; }
    public void SetTrailColor(Color TrailColor) {
        this.TrailColor = TrailColor;
        repaint();
    }
}