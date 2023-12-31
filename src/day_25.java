import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class day_25 {
    public static void main(String[] args) {
        String[] input = ReadFile.getFile("day_25.txt");
        String[][] inputSplit = Arrays.stream(input).map(s -> s.split(": ")).toArray(String[][]::new);
        HashMap<String, Node> nodes = new HashMap<String, Node>();

        for (String[] line : inputSplit) {
            Node currentNode = nodes.computeIfAbsent(line[0], k -> new Node(line[0]));
            String[] neighbors = line[1].split(" ");
            for (String neighbor : neighbors) {
                Node neighborNode = nodes.computeIfAbsent(neighbor, Node::new);
                currentNode.addNeighbor(neighborNode);
                neighborNode.addNeighbor(currentNode);
            }
        }
        ArrayList<Node> nodesList = new ArrayList<>(nodes.values());

        int part1 = kargersAlgorithm(nodesList);
        System.out.println("part 1: " + part1);
    }

    public static int kargersAlgorithm(ArrayList<Node> nodesList) {
        int minCut = Integer.MAX_VALUE;
        ArrayList<Node> bestNodeList = new ArrayList<>();

        while(minCut > 3) {
            HashMap<String, Node> nodesMapCopy = new HashMap<>();
            for (Node node : nodesList) {
                nodesMapCopy.put(node.name, new Node(node.name));
            }
            for (Node node : nodesList) {
                Node nodeCopy = nodesMapCopy.get(node.name);
                for (Node neighbor : node.neighbors) {
                    nodeCopy.addNeighbor(nodesMapCopy.get(neighbor.name));
                }
            }
            ArrayList<Node> nodesListCopy = new ArrayList<>(nodesMapCopy.values());

            bestNodeList = contractEdges(nodesListCopy);
            if (bestNodeList.get(0).neighbors.toArray().length < minCut) {
                minCut = bestNodeList.get(0).neighbors.toArray().length;
            }
        }
        return bestNodeList.get(0).count * bestNodeList.get(1).count;
    }

    public static ArrayList<Node> contractEdges(ArrayList<Node> nodesList) {
        Random random = new Random();
        while (nodesList.size() > 2) {
            int randomIndex = random.nextInt(nodesList.size());
            Node node1 = nodesList.get(randomIndex);
            ArrayList<Node> neighbors = node1.neighbors;
            Node node2 = neighbors.get(random.nextInt(neighbors.size()));
            node1.count += node2.count;

            for (Node neighbor : new ArrayList<>(node2.neighbors)) {
                if (neighbor != node1) {
                    node1.addNeighbor(neighbor);
                    while (neighbor.neighbors.remove(node2)) {
                        neighbor.addNeighbor(node1);
                    }
                }
            }

            nodesList.remove(node2);

            node1.neighbors.removeIf(neighbor -> neighbor == node1);
            node1.neighbors.removeIf(neighbor -> neighbor == node2);
        }

        return nodesList;
    }

    public static class Node {
        String name;
        ArrayList<Node> neighbors;
        int count;

        public Node(String name) {
            this.name = name;
            this.neighbors = new ArrayList<Node>();
            count = 1;
        }

        public void addNeighbor(Node neighbor) {
            this.neighbors.add(neighbor);
        }

        public void removeNeighbor(Node neighbor) {
            this.neighbors.remove(neighbor);
        }

        @Override
        public String toString() {
            return this.name;
        }
    }
}
