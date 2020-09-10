package singleAgent;

import java.util.ArrayList;

public class NodeGene implements Comparable<NodeGene> {

    public enum TYPE {
        INPUT,
        HIDDEN,
        OUTPUT,
        BIAS
    }

    public int number;

    public TYPE type;

    public float inputSum = 0;
    public float outputSum = 0;

    public float x;
    public float y;

    public ArrayList<ConnectionGene> connections = new ArrayList<>();


    public NodeGene(int number) {
        this.number = number;
    }

    public void activate() {
        if (type != TYPE.INPUT && type != TYPE.BIAS) {
            outputSum = sigmoid(inputSum);
        }

        for (ConnectionGene connection : connections) {
            if (connection.isEnabled) {
                connection.nodeTo.inputSum += connection.weight * outputSum;
            }
        }
    }

    public float sigmoid(float x) {
        return (float) (1 / (1 + Math.exp(-x)));
    }

    public float step(float x) {
        return x > 0 ? 1f : 0f;
    }

    public void setConnections(ArrayList<ConnectionGene> conns) {
        for (ConnectionGene c : conns) {
            if (c.nodeFrom == this) connections.add(c);
        }
    }

    public void setType(TYPE type) {
//        if(type == null){
//            this.type = null;
//        }

        if (type == TYPE.INPUT) x = 0.1f;
        else if (type == TYPE.OUTPUT) x = 0.9f;
        else if (type == TYPE.BIAS) x = 0.1f;

        this.type = type;
    }

    public NodeGene clone() {
        NodeGene clone = new NodeGene(number);
        clone.setType(type);
        clone.x = x;
        clone.y = y;

        return clone;
    }

    @Override
    public int compareTo(NodeGene o) {
        return Float.compare(x, o.x);

    }

}
