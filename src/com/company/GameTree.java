package com.company;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import com.mxgraph.view.mxGraph;

public class GameTree {
    private mxGraph Graph;
    private Object dParent;
    private Node Root;
    private int Depth;
    private Random Rnd;
    private int Players;
    private int Count;
    private ArrayList<ArrayList<Node>> Layers;
    private HashSet<Node> Terminal;
    public GameTree(int players, int depth) {
        this.Graph = new mxGraph();
        this.dParent = Graph.getDefaultParent();
        this.Players = players;
        this.Depth = depth;
        this.Root = new Node();
        this.Count = 0;
        this.Terminal = new HashSet<Node>();
        this.Rnd = new Random();
        this.Layers = new ArrayList<ArrayList<Node>>(Depth);
        for (int i = 0; i < Depth; i++){
            Layers.add(new ArrayList<Node>());
        }
        Layers.get(0).add(Root);
    }

    public void generate(Node node) {

        addNode(node);
        addNode(node);
        if (node.getLayer() == Depth - 2) {
            for (Node child: node.getChildren()) {
                child.randWin();
                Terminal.add(child);
            }
            return;
        }
        for (Node child: node.getChildren()) {
            generate(child);
        }
    }

    public void backwardInduction() {
        for (int i = Depth - 2; i >= 0; i--) {

            for (Node parent : Layers.get(i)) {
                if (parent.equals(Root)) {
                    System.out.println("Мы в корне!");
                }
                Node childL = parent.getChild(0);
                Node childR = parent.getChild(1);
                if (childL.getWin()[parent.getPlayer()] >= childR.getWin()[parent.getPlayer()]){
                    parent.setWin(childL.getWin());
                }
                else parent.setWin(childR.getWin());

            }
        }

    }

    public void addNode(Node parent) {
        Count++;
        Node child = new Node(parent);
        Layers.get(child.getLayer()).add(child);
    }
    public HashSet<Node> getTerminal(){
        return this.Terminal;
    }
    public ArrayList<ArrayList<Node>> getLayers(){
        return this.Layers;
    }
    public Node Root() {
        return Root;
    }
    public int getCount() {
        return Count;
    }
    public int getDepth() {
        return Depth;
    }
    public mxGraph getGraph() {
        return Graph;
    }
    public Object getDefP() {
        return dParent;
    }
    //-------------Класс узлов--------------------------------
    class Node {
        private ArrayList<Node> Children;
        private Node Parent;
        private int Layer;
        private int ID;
        private int x, y;
        private boolean isTerminal;
        //private Object draw;
        private int Player;
        private int[] Win;
        private void newChild(Node child) {
            Children.add(child);
        }


        public Node() {
            this.Parent = null;
            this.Children = new ArrayList<Node>();
            this.Layer = 0;
            this.Player = 0;
            this.ID = 0;
            this.isTerminal = true;
            this.Win = new int[Players];
        }

        public Node(Node parentNode) {
            this.Parent = parentNode;
            this.Children = new ArrayList<Node>();
            this.Layer = parentNode.getLayer() + 1;
            this.Player = this.Layer % Players;
            this.ID = Count;
            this.isTerminal = true;
            this.Win = new int[Players];
            parentNode.newChild(this);
            parentNode.isTerminal = false;
        }

        public Object getDraw() {
            String str = (this.getPlayer() + 1) + "\n" + this.getWinStr();
            return Graph.insertVertex(dParent, null, str, x, y, 60, 60);
        }
        public void randWin() {
            for (int i = 0; i < Win.length; i++) {
                Win[i] = Rnd.nextInt(51);
            }
        }
        public boolean isRoot() {
            return (this.getParent() == null);
        }

        public int getPlayer() {
            return this.Player;
        }
        public int getLayer() {
            return this.Layer;
        }
        public boolean Equals(Node node) {
            if (this.ID == node.ID) return true;
            else return false;
        }
        public int getID() {
            return this.ID;
        }
        public int[] getWin() {
            return Win;
        }
        public String getWinStr(){
            String str = "(";
            for (int i = 0; i < Win.length; i++) {
                if (i < Win.length - 1) str = str + Win[i] + ", ";
                else str = str + Win[i];
            }
            str += ")";
            return str;
        }
        public void setWin(int[] arr) {
            for (int i = 0; i < this.Win.length; i++) {
                this.Win[i] = arr[i];
            }
        }
        public void setCords(int xx, int yy) {
            this.x = xx;
            this.y = yy;
        }
        public boolean isTerminal(){
            return this.isTerminal;
        }
        public Node getParent() {
            return this.Parent;
        }
        public ArrayList<Node> getChildren() {
            return this.Children;
        }
        public Node getChild(int i) {
            return this.Children.get(i);
        }
    }

}
