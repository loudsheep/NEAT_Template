package singleAgent;

public class ConnectionGene {

    public NodeGene nodeTo;
    public NodeGene nodeFrom;

    public float weight;
    public boolean isEnabled = true;

    public int innovation;

    public ConnectionGene(NodeGene from, NodeGene to, float w, int innov) {
        nodeTo = to;
        nodeFrom = from;
        weight = w;
        innovation = innov;
    }

    public void mutateWeight() {
        if (Math.random() < 0.1) {
            weight = (float) (Math.random() * 2 - 1);
        } else {
            weight += (Math.random() * 2 - 1) / 50;
        }
    }

    public ConnectionGene clone(NodeGene nodeFrom, NodeGene nodeTo) {
        ConnectionGene clone = new ConnectionGene(nodeFrom, nodeTo, weight, innovation);
        clone.isEnabled = isEnabled;
        return clone;
    }

}
