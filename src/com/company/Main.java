package com.company;
import javax.swing.JFrame;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

public class Main {
    public static void main(String[] args) {
        Draw d = new Draw();
        d.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        d.setSize(1000, 1000);
        d.setVisible(true);
    }
}

class Draw extends JFrame {
    public Draw() {
        super("HahaGraph");
        GameTree tree = new GameTree(3, 6);
        tree.generate(tree.Root());
        tree.backwardInduction();

        mxGraph graph = tree.getGraph();
        Object parent = tree.getDefP();

        graph.getModel().beginUpdate();
        try {
            int move = 0;
            for (int i = tree.getDepth() - 1; i >= 0; i--) {
                int n = 0;
                for (GameTree.Node node : tree.getLayers().get(i)) {
                    n += 80 + 60 * (tree.getDepth() - 1 - i) ;
                    node.setCords(n + move,120 * i);
                    //Object v1 = node.getDraw();
                }
                move += n/16;
            }
            for (int i = 0; i < tree.getDepth(); i++) {
                for (GameTree.Node node : tree.getLayers().get(i)) {
                    if (!node.isTerminal()) {
                        for (GameTree.Node child : node.getChildren()) {
                            String str;
                            boolean flag = true;
                            for (int j = 0; j < node.getWin().length; j++) {
                                if (node.getWin()[j] != child.getWin()[j]) flag = false;
                            }
                            if (flag) str = "ROUNDED;strokeColor=red;fillColor=red";
                            else str = "ROUNDED;strokeColor=black;fillColor=black";
                            graph.insertEdge(parent, null, "", node.getDraw(), child.getDraw(), str);
                        }
                    }
                }
            }
            GameTree.Node node = tree.Root();
            while (!node.isTerminal()) {
                for (GameTree.Node child : node.getChildren()) {
                    String str;
                    boolean flag = true;
                    for (int j = 0; j < node.getWin().length; j++) {
                        if (node.getWin()[j] != child.getWin()[j]) flag = false;
                    }
                    if (flag) {
                        str = "ROUNDED;strokeColor=green;fillColor=green";
                        graph.insertEdge(parent, null, "", node.getDraw(), child.getDraw(), str);
                        node = child;
                    }
                }
            }
        } finally {
            graph.getModel().endUpdate();
        }
        mxGraphComponent graphComponent = new mxGraphComponent(graph);
        getContentPane().add(graphComponent);
    }

}

