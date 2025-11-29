/* 
 * 245150207111050 ORIE_ABYAN_MAULANA
 * 245150200111064 SULTHAN_RAFI_PUTRA_SUKMA
 * 245150207111049 HUBERT_CENDANA
 * 245150201111053 FARREL_BRILLIANT
*/

package ASD.Tugas.Tugas1;

import java.util.Scanner;

public class TowerDefense {
    static class LabyrinthSimulation {
        private int size;
        private boolean[][] walkable;
        private Point start, finish;
        private PriorityQueue results;

        public LabyrinthSimulation() {
            this.results = new PriorityQueue();
        }

        public void readInput(Scanner sc) {
            size = Integer.parseInt(sc.nextLine());
            walkable = new boolean[size][size];

            String[] areas = sc.nextLine().split(" ");
            for (int i = 0; i < areas.length; i++) {
                String[] coords = areas[i].split(",");
                int y = Integer.parseInt(coords[0]);
                int x = Integer.parseInt(coords[1]);
                walkable[y][x] = true;
                if (i == 0)
                    start = new Point(x, y);
                if (i == areas.length - 1)
                    finish = new Point(x, y);
            }

            int numParticipants = Integer.parseInt(sc.nextLine());

            for (int i = 0; i < numParticipants; i++) {
                String[] participantInfo = sc.nextLine().split(" ");
                String name = participantInfo[0];
                String[] strategy = new String[participantInfo.length - 1];
                System.arraycopy(participantInfo, 1, strategy, 0, strategy.length);
                Participant p = new Participant(name, strategy);
                solveForParticipant(p);
            }
        }

        private void solveForParticipant(Participant p) {
            int[][] pathMap = new int[size][size];
            boolean[][] visited = new boolean[size][size];
            Stack<Point> pathStack = new Stack<>();

            findPathRecursive(p, start, pathMap, visited, 1, pathStack);
        }

        private boolean findPathRecursive(Participant p, Point current, int[][] pathMap, boolean[][] visited, int step,
                Stack<Point> pathStack) {
            visited[current.y][current.x] = true;
            pathMap[current.y][current.x] = step;
            pathStack.push(current);

            if (current.equals(finish)) {
                int[][] finalPathMap = new int[size][size];
                for (int i = 0; i < size; i++) {
                    System.arraycopy(pathMap[i], 0, finalPathMap[i], 0, size);
                }
                results.add(new ParticipantResult(p.name, step, finalPathMap));
                return true;
            }

            for (String direction : p.moveStrategy) {
                Point nextMove = getNextPoint(current, direction);
                if (isMoveValid(nextMove, visited)) {
                    if (findPathRecursive(p, nextMove, pathMap, visited, step + 1, pathStack)) {
                        return true;
                    }
                }
            }

            pathMap[current.y][current.x] = 0;
            // visited[current.y][current.x] = false;
            pathStack.pop();
            return false;
        }

        private Point getNextPoint(Point current, String direction) {
            int newX = current.x;
            int newY = current.y;
            switch (direction) {
                case "UP":
                    newY++;
                    break;
                case "DOWN":
                    newY--;
                    break;
                case "RIGHT":
                    newX++;
                    break;
                case "LEFT":
                    newX--;
                    break;
            }
            return new Point(newX, newY);
        }

        private boolean isMoveValid(Point p, boolean[][] visited) {
            if (p.x < 0 || p.x >= size || p.y < 0 || p.y >= size) {
                return false;
            }
            return walkable[p.y][p.x] && !visited[p.y][p.x];
        }

        public void printResults(String header) {
            System.out.println(header);
            PriorityQueue tempQueueForPrinting = new PriorityQueue();
            ParticipantResult fastest = null;

            while (!results.isEmpty()) {
                ParticipantResult res = results.poll();
                if (fastest == null) {
                    fastest = res;
                }
                tempQueueForPrinting.add(res);

                System.out.println(res.name);
                printMap(res.pathMap);
            }

            PriorityQueue tempQueueForSummary = new PriorityQueue();

            while (!tempQueueForPrinting.isEmpty()) {
                ParticipantResult res = tempQueueForPrinting.poll();
                tempQueueForSummary.add(res);
                if (res == fastest) {
                    System.out.println("FASTEST " + res.name + " " + res.steps + " steps");
                } else {
                    System.out.println(res.name + " " + res.steps + " steps");
                }
            }

            while (!tempQueueForSummary.isEmpty()) {
                results.add(tempQueueForSummary.poll());
            }
        }

        private void printMap(int[][] pathMap) {
            for (int i = size - 1; i >= 0; i--) {
                for (int j = 0; j < size; j++) {
                    if (pathMap[i][j] > 0) {
                        System.out.printf("[%02d]", pathMap[i][j]);
                    } else if (walkable[i][j]) {
                        System.out.print("[OO]");
                    } else {
                        System.out.print("[XX]");
                    }
                }
                System.out.println();
            }
        }

        public int getFastestSteps() {
            if (results.isEmpty()) {
                return Integer.MAX_VALUE;
            }
            ParticipantResult fastest = results.peek();
            return fastest.steps;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        LabyrinthSimulation redTowerSim = new LabyrinthSimulation();
        redTowerSim.readInput(sc);

        LabyrinthSimulation blueTowerSim = new LabyrinthSimulation();
        blueTowerSim.readInput(sc);

        redTowerSim.printResults("BLUE TEAM ENTERED RED TOWER");
        System.out.println();

        blueTowerSim.printResults("RED TEAM ENTERED BLUE TOWER");
        System.out.println();

        int blueTeamFastest = redTowerSim.getFastestSteps();
        int redTeamFastest = blueTowerSim.getFastestSteps();

        if (blueTeamFastest < redTeamFastest) {
            System.out.println("BLUE TOWER TEAM IS WINNER");
            System.out.println(blueTeamFastest + " steps vs " + redTeamFastest + " steps");
        } else if (redTeamFastest < blueTeamFastest) {
            System.out.println("RED TOWER TEAM IS WINNER");
            System.out.println(redTeamFastest + " steps vs " + blueTeamFastest + " steps");
        } else {
            System.out.println("BOTH TEAMS ARE TIED, NO WINNER");
        }
        sc.close();
    }
}

// ADT CLASS

class Node<T> {
    T data;
    Node<T> next;

    public Node(T data) {
        this.data = data;
        this.next = null;
    }
}

class Stack<T> {
    private Node<T> top;
    private int size;

    public Stack() {
        top = null;
        size = 0;
    }

    public boolean isEmpty() {
        return top == null;
    }

    public int size() {
        return size;
    }

    public void push(T data) {
        Node<T> newNode = new Node<>(data);
        newNode.next = top;
        top = newNode;
        size++;
    }

    public T pop() {
        if (isEmpty()) {
            return null;
        }
        T data = top.data;
        top = top.next;
        size--;
        return data;
    }

    public T peek() {
        if (isEmpty()) {
            return null;
        }
        return top.data;
    }
}

class PriorityQueue {

    private class ResultNode {
        ParticipantResult data;
        ResultNode next;

        ResultNode(ParticipantResult data) {
            this.data = data;
        }
    }

    private ResultNode head;

    public PriorityQueue() {
        head = null;
    }

    public boolean isEmpty() {
        return head == null;
    }

    public void add(ParticipantResult newResult) {
        ResultNode newNode = new ResultNode(newResult);

        if (head == null || newResult.compareTo(head.data) < 0) {
            newNode.next = head;
            head = newNode;
        } else {
            ResultNode current = head;
            while (current.next != null && newResult.compareTo(current.next.data) >= 0) {
                current = current.next;
            }
            newNode.next = current.next;
            current.next = newNode;
        }
    }

    public ParticipantResult poll() {
        if (isEmpty()) {
            return null;
        }
        ParticipantResult data = head.data;
        head = head.next;
        return data;
    }

    public ParticipantResult peek() {
        if (isEmpty()) {
            return null;
        }
        return head.data;
    }
}

//

class Point {
    int x, y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Point point = (Point) obj;
        return x == point.x && y == point.y;
    }
}

class Participant {
    String name;
    String[] moveStrategy;

    public Participant(String name, String[] moveStrategy) {
        this.name = name;
        this.moveStrategy = moveStrategy;
    }
}

class ParticipantResult implements Comparable<ParticipantResult> {
    String name;
    int steps;
    int[][] pathMap;

    public ParticipantResult(String name, int steps, int[][] pathMap) {
        this.name = name;
        this.steps = steps;
        this.pathMap = pathMap;
    }

    @Override
    public int compareTo(ParticipantResult other) {
        if (this.steps != other.steps) {
            return Integer.compare(this.steps, other.steps);
        }
        return this.name.compareTo(other.name);
    }
}