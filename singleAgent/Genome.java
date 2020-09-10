package singleAgent;

import java.util.ArrayList;


public class Genome {

    public int inputs;
    public int outputs;

    public int nextNode = 0;
    public int nextConnection = 0;

    public int biasNode;

    public ArrayList<NodeGene> nodes = new ArrayList<>();
    public ArrayList<ConnectionGene> genes = new ArrayList<>();

    public final float NODE_MUTATION_PROBABILITY = 0.01f;
    public final float CONNECTION_MUTATION_PROBABILITY = 0.1f;
    public final float WEIGHT_MUTATION_PROBABILITY = 0.5f;


    public Genome(int input, int output) {
        this.inputs = input;
        this.outputs = output;


        float d = 1 / ((float) outputs + 1);

        for (int i = 0; i < outputs; i++) {
            NodeGene nn = new NodeGene(i + inputs);
            nn.setType(NodeGene.TYPE.OUTPUT);


            nn.y = d * (i + 1);

            nodes.add(nn);

            nextNode++;
        }

        d = 1 / ((float) inputs + 1);

        for (int i = 0; i < inputs; i++) {
            NodeGene nn = new NodeGene(i);
            nn.setType(NodeGene.TYPE.INPUT);
            nodes.add(nn);


            nn.y = d * (i + 1);

            nextNode++;
        }

        nodes.add(new NodeGene(nextNode));
        biasNode = nextNode;
        nextNode++;
        nodes.get(biasNode).setType(singleAgent.NodeGene.TYPE.BIAS);
        nodes.get(biasNode).outputSum = 1;
        nodes.get(biasNode).y = d*inputs;

//        for(singleAgent.NodeGene n:nodes){
//            System.out.println(n.type.toString());
//            if(n.type == singleAgent.NodeGene.TYPE.INPUT)System.out.println("input");
//        }

    }

    public Genome(int inputs, int outputs, boolean clone) {
        this.inputs = inputs;
        this.outputs = outputs;
    }

    NodeGene getNode(int nodeNumber) {
        for (int i = 0; i < nodes.size(); i++) {
            if (nodes.get(i).number == nodeNumber) {
                return nodes.get(i);
            }
        }
        return null;
    }


    public float[] query(float[] input) {
        Calculator calc = new Calculator(nodes, genes);

        return calc.calculate(input);
    }

    private boolean exists(ConnectionGene con) {
        for (ConnectionGene c : genes) {
            if (c.nodeTo == con.nodeTo && c.nodeFrom == con.nodeFrom) return true;
        }
        return false;
    }

    public void mutate() {
        if (Math.random() < NODE_MUTATION_PROBABILITY) mutateNode();
        if (Math.random() < CONNECTION_MUTATION_PROBABILITY) mutateConnection();
        if (Math.random() < WEIGHT_MUTATION_PROBABILITY) mutateWeight();
    }

    public void mutateConnection() {
        if (nodes.size() == 0) return;
        for (int i = 0; i < 100; i++) {
            NodeGene n1 = nodes.get((int) (Math.random() * nodes.size()));
            NodeGene n2 = nodes.get((int) (Math.random() * nodes.size()));

            if (n1.type == n2.type && n1.type != NodeGene.TYPE.HIDDEN) continue;
            if (n1.x == n2.x) continue;

            ConnectionGene newCon;

            if (n1.x < n2.x) {
                newCon = new ConnectionGene(n1, n2, (float) Math.random() * 2 - 1, nextConnection);
            } else {
                newCon = new ConnectionGene(n2, n1, (float) Math.random() * 2 - 1, nextConnection);
            }

            if (exists(newCon)) continue;

            nextConnection++;

            genes.add(newCon);
            //System.out.println("connn");
            return;
        }
    }

    public void mutateNode() {
        if (genes.size() == 0) return;

        ConnectionGene con = genes.get((int) (Math.random() * genes.size()));
        con.isEnabled = false;

        NodeGene newNode = new NodeGene(nextNode);
        nextNode++;
        newNode.x = (con.nodeFrom.x + con.nodeTo.x) / 2;
        newNode.y = (float) ((con.nodeFrom.y + con.nodeTo.y) / 2 + (Math.random() * 2 - 1) / 50);
        newNode.setType(NodeGene.TYPE.HIDDEN);

        ConnectionGene con1 = new ConnectionGene(con.nodeFrom, newNode, con.weight, nextConnection);
        nextConnection++;

        ConnectionGene con2 = new ConnectionGene(newNode, con.nodeTo, 1f, nextConnection);
        nextConnection++;

        genes.add(con1);
        genes.add(con2);

        nodes.add(newNode);

        //System.out.println("Add - " + newNode.type + " number: " + newNode.number + " pos " + newNode.x + "-" + newNode.y);

    }

    public void mutateWeight() {
        if (genes.size() == 0) return;

        genes.get((int) (Math.random() * genes.size())).mutateWeight();
    }

    public Genome clone() {
        Genome clone = new Genome(inputs, outputs, true);

        for (NodeGene n : nodes) {
            clone.nodes.add(n.clone());
        }

        for (int i = 0; i < genes.size(); i++) {
            clone.genes.add(genes.get(i).clone(clone.getNode(genes.get(i).nodeFrom.number), clone.getNode(genes.get(i).nodeTo.number)));
        }


        return clone;
    }

    public static float map(float input, float input_start, float input_end, float output_start, float output_end) {
        return output_start + ((output_end - output_start) / (input_end - input_start)) * (input - input_start);
    }

}
