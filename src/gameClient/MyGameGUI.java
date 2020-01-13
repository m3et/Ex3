package gameClient;


import Server.Game_Server;
import Server.game_service;
import gameComponent.Fruit;
import gameComponent.Robot;
import graph.dataStructure.DGraph;
import graph.dataStructure.edge_data;
import graph.dataStructure.node_data;
import graph.utils.Point3D;
import graph.utils.Range;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.LinkedList;


public class MyGameGUI extends JFrame implements ActionListener, MouseListener, Runnable {
    /**
     * this is the screen parameters
     */
    private final int width = 1000;
    private final int height = 750;
    /**
     * range of node in game
     */
    private Range rangeX;
    private Range rangeY;
    /**
     * the graph in the game
     **/
    private DGraph graph;

    private final Double EPS = 0.001;

    private game_service game;
    private int level;                  //The level we are playing
    private BufferedImage gameLayout;   //Buffer for the graph

    /**
     * LinkList for the robots and fruits in the game
     */
    private LinkedList<Fruit> fruits;
    private LinkedList<Robot> robots;

    /**
     * INIT game
     **/
    public MyGameGUI() {
        initGUI();
    }

    /**
     * INIT the screen with all menu's and parameters
     */
    private void initGUI() {
        //set the window parameters
        this.setSize(width, height);
        this.setTitle("My Game");
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //ask for the level of the game

        level = askForLevel();
        game = Game_Server.getServer(level); // you have [0,23] games

        //init the game graph
        String graphStr = game.getGraph();
        this.graph = new DGraph();
        this.graph.init(graphStr);
        //set the points range of the graph
        setRangeX();
        setRangeY();
        //get the list of fruits
        getFruits();
        getRobots();

        statGame();





    }

    private void getFruits() {
        for (String s : game.getFruits()) {
            System.out.println(s);
            fruits.add(new Fruit(s));
        }
    }

    private void getRobots(){
        for (String s : game.getRobots()) {
            System.out.println(s);
            robots.add(new Robot(s));
        }

    }

    private edge_data findEdge(Point3D p){
        for (node_data node : graph.getV())
            for (edge_data edge : graph.getE(node.getKey())) {
                node_data src = graph.getNode(edge.getSrc());
                node_data dst = graph.getNode(edge.getDest());
                double dist = p.distance3D(src.getLocation()) + p.distance3D(dst.getLocation());
                if (Math.abs(src.getLocation().distance3D(dst.getLocation()) - dist) < EPS)
                    return edge;
            }
        System.out.println("No corresponding edge");
        return null;
    }


    private void statGame() {


    }

    /**
     * ask from play what level he wants to play
     * @return int number of chosen level to play
     */
    private int askForLevel() {
        JFrame frame = new JFrame();
        frame.setSize(400, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        ImageIcon icon = new ImageIcon("src/Utils/icon/icon.png");
        Image image = icon.getImage(); // transform it
        Image newimg = image.getScaledInstance(60, 60,  Image.SCALE_SMOOTH); // scale it the smooth way
        icon = new ImageIcon(newimg);  // transform it back

        Object[] possibilities = {
                "0","1","2","3","4","5","6",
                "7","8","9","10","11","12",
                "13","14","15","16","17","18",
                "19","20","21","22","23"};
        String s;
        do {
            s = (String)JOptionPane.showInputDialog(
                    frame,
                    "Choose level to play:\n",
                    "Level",
                    JOptionPane.PLAIN_MESSAGE,
                    icon,
                    possibilities,
                    "0");
            if (s == null)
                JOptionPane.showMessageDialog(this, "Please Choose a Level Number",
                        "ERROR", JOptionPane.ERROR_MESSAGE);
        }while (s == null);

        return Integer.parseInt(s);

    }

    public void paint(Graphics g){
        if (gameLayout == null)
            paintComponent(g);
        Graphics2D layoutCan = (Graphics2D)g;
        layoutCan.drawImage(gameLayout,null,0,0);



    }

    /**
     * paint a representation of a graph
     *
     * @param g - DGraph
     */
    public void paintComponent(Graphics g) {
        if (graph.nodeSize() == 0)
            return;

        gameLayout = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
        super.paint(g);

        DGraph graph = this.graph;

        Graphics2D graphics = gameLayout.createGraphics();

        for (node_data p : graph.getV()) {
            double xPixel = rescaleX(p.getLocation().x());
            double yPixel = rescaleY(p.getLocation().y());


            graphics.setColor(Color.BLUE);
            graphics.fillOval((int) xPixel - 5, (int) yPixel - 7, 15, 15);
            graphics.setColor(Color.black);
            g.setFont(new Font("TimesRoman", Font.BOLD, 15));
            graphics.drawString(String.valueOf(p.getKey()), (int) xPixel - 2, (int) yPixel - 8);
        }

        for (node_data p : graph.getV()) {
            if (graph.getE(p.getKey()) != null) {
                for (edge_data edge : graph.getE(p.getKey())) {

                    node_data src = graph.getNode(edge.getSrc());
                    node_data dest = graph.getNode(edge.getDest());

                    double srcXPixel = rescaleX(src.getLocation().x());
                    double srcYPixel = rescaleY(src.getLocation().y());
                    double destXPixel = rescaleX(dest.getLocation().x());
                    double destYPixel = rescaleY(dest.getLocation().y());

                    graphics.setColor(Color.RED);
                    graphics.setStroke(new BasicStroke(2));

                    Line2D line = new Line2D.Float(
                            (int) srcXPixel,
                            (int) srcYPixel,
                            (int) destXPixel,
                            (int) destYPixel);

                    graphics.setStroke(new BasicStroke((float) 2.5));
                    graphics.draw(line);


                    graphics.setColor(Color.yellow);
                    graphics.fillOval((int) ((srcXPixel * 10 + destXPixel * 2) / 12) - 5, (int) ((srcYPixel * 10 + destYPixel * 2) / 12) - 6, 10, 10);

                    graphics.setColor(Color.DARK_GRAY);
                    graphics.drawString(String.valueOf(edge.getWeight()), (int) ((srcXPixel * 2 + destXPixel) / 3) - 5, (int) ((srcYPixel * 2 + destYPixel) / 3) - 6);
                }
            }
        }
        Graphics2D layoutCan = (Graphics2D)g;
        layoutCan.drawImage(gameLayout,null,0,0);
    }

    /**
     *
     * @param data denote some data to be scaled
     * @param r_min the minimum of the range of your data
     * @param r_max the maximum of the range of your data
     * @param t_min the minimum of the range of your desired target scaling
     * @param t_max the maximum of the range of your desired target scaling
     * @return relative resolution based of given parameters
     */
    private double rescale(double data, double r_min, double r_max, double t_min, double t_max) {
        return ((data - r_min) / (r_max-r_min)) * (t_max - t_min) + t_min;
    }

    /**
     *
     * @param x,y - location of data
     * @return resolution of x/y with screen setting
     * using rescale method
     */
    private double rescaleX(double x) {
        return rescale(x,rangeX.get_min(),rangeX.get_max(),width*0.1,width - width*0.1);
    }
    private double rescaleY(double y) {
        return rescale(y,rangeY.get_min(),rangeY.get_max(),height*0.1,height - height*0.1);

    }


    @Override
    public void run() {

    }


    /**
     * set the RangeX of the graph Range[minX,maxX]
     * go over all nodes and find min,max X
     */
    private void setRangeX(){
        double max = Double.MIN_VALUE;
        double min = Double.MAX_VALUE;
        for(node_data node : graph.getV()) {
            if (node.getLocation().x() > max)
                max = node.getLocation().x();
            if (node.getLocation().x() < min)
                min = node.getLocation().x();
        }

        rangeX = new Range(min,max);
    }
    /**
     * set the RangeY of the graph Range[minY,maxY]
     * go over all nodes and find min,max Y
     */
    private void setRangeY(){
        double max = Double.MIN_VALUE;
        double min = Double.MAX_VALUE;
        for(node_data node : graph.getV()) {
            if (node.getLocation().y() > max)
                max = node.getLocation().y();
            if (node.getLocation().y() < min)
                min = node.getLocation().y();
        }

        rangeY = new Range(min,max);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

}
