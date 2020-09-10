package singleAgent;

import java.util.ArrayList;
import java.util.Comparator;

public class Calculator {

    private ArrayList<NodeGene> inputNodes = new ArrayList<>();
    private ArrayList<NodeGene> hiddenNodes = new ArrayList<>();
    private ArrayList<NodeGene> outputNodes = new ArrayList<>();
    private NodeGene biasNodes;


    public Calculator(ArrayList<NodeGene> nodes, ArrayList<ConnectionGene> connections) {

        for (NodeGene n : nodes) {
            if (n.type == NodeGene.TYPE.INPUT) inputNodes.add(n);
                //else if (n.type == singleAgent.NodeGene.TYPE.BIAS) biasNodes.add(n);
            else if (n.type == NodeGene.TYPE.HIDDEN) hiddenNodes.add(n);
            else if (n.type == NodeGene.TYPE.OUTPUT) outputNodes.add(n);
            else if (n.type == NodeGene.TYPE.BIAS) biasNodes = n;

            n.setConnections(connections);
        }

        //System.out.println(inputNodes.size() + "-" + hiddenNodes.size() + "-" + outputNodes.size());// + "-" + biasNodes.size());


        hiddenNodes.sort(new Comparator<NodeGene>() {
            @Override
            public int compare(NodeGene o1, NodeGene o2) {
                return o1.compareTo(o2);
            }
        });

    }


    public float[] calculate(float[] input) {
        if (input.length != inputNodes.size())
            throw new RuntimeException("Inputs don't match input nodes: size=" + inputNodes.size() + ", provided=" + input.length);

        for (int i = 0; i < inputNodes.size(); i++) {
            inputNodes.get(i).outputSum = input[i] + biasNodes.outputSum;
            inputNodes.get(i).activate();
        }

        for (NodeGene n : hiddenNodes) {
            n.outputSum += biasNodes.outputSum;
            n.activate();
        }

        float[] output = new float[outputNodes.size()];

        for (int i = 0; i < outputNodes.size(); i++) {
            outputNodes.get(i).activate();
            output[i] = outputNodes.get(i).outputSum;
        }

        return output;
    }

}
