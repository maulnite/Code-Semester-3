package PRAKTIKUM_ASD.LiveCoding.Bab12;

import java.util.Scanner;

class Sensor {
    String id;
    int value;

    Sensor(String id, int value) {
        this.id = id;
        this.value = value;
    }
}

class Node {
    Sensor data;
    Node next;

    Node(Sensor data) {
        this.data = data;
        this.next = null;
    }
}

public class Main {

    static Node head = null;

    static void addSensor(String id, int value) {
        // isi sendiri
        if (head == null) {
            head = new Node(new Sensor(id, value));
        } else {
            Node current = head;
            while (current.next != null) {
                if (current.data.id.equals(id)) {
                    return;
                }
                current = current.next;
            }
            if (current.data.id.equals(id)) {
                System.out.println("Sensor " + id + " sudah ada");
                return;
            }
            current.next = new Node(new Sensor(id, value));
        }
        System.out.println("Sensor " + id + " ditambahkan dengan nilai " + value);
    }

    static void updateSensor(String id, int value) {
        // isi sendiri
        Node current = head;
        while (current != null) {
            if (current.data.id.equals(id)) {
                current.data.value = value;
                System.out.println("Sensor " + id + " diperbarui menjadi " + value);
                return;
            }
            current = current.next;
        }
        System.out.println("Sensor " + id + " tidak ditemukan");
    }

    static void deleteSensor(String id) {
        // isi sendiri
        if (head == null) {
            System.out.println("Sensor " + id + " tidak ditemukan");
            return;
        }
        if (head.data.id.equals(id)) {
            head = head.next;
            System.out.println("Sensor " + id + " dihapus");
            return;
        }
        Node current = head;
        while (current.next != null) {
            if (current.next.data.id.equals(id)) {
                current.next = current.next.next;
                System.out.println("Sensor " + id + " dihapus");
                return;
            }
            current = current.next;
        }
    }

    static void printSensors() {
        // isi sendiri
        if (head == null) {
            System.out.println("Tidak ada sensor");
            return;
        }
        Node current = head;
        while (current != null) {
            System.out.println(current.data.id + " " + current.data.value);
            current = current.next;
        }
    }

    static void sortAsc() {
        // isi sendiri
        for (Node i = head; i != null; i = i.next) {
            for (Node j = head; j.next != null; j = j.next) {
                if (j.data.value > j.next.data.value) {
                    Sensor temp = j.data;
                    j.data = j.next.data;
                    j.next.data = temp;
                }
            }
        }
        System.out.println("Data sensor diurutkan secara ASC");
    }

    static void sortDesc() {
        // isi sendiri
        for (Node i = head; i != null; i = i.next) {
            for (Node j = head; j.next != null; j = j.next) {
                if (j.data.value < j.next.data.value) {
                    Sensor temp = j.data;
                    j.data = j.next.data;
                    j.next.data = temp;
                }
            }
        }
        System.out.println("Data sensor diurutkan secara DESC");
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (sc.hasNext()) {
            String cmd = sc.next();

            if (cmd.equals("STOP")) {
                break;
            }

            if (cmd.equals("ADD")) {
                addSensor(sc.next(), sc.nextInt());
            } else if (cmd.equals("UPDATE")) {
                updateSensor(sc.next(), sc.nextInt());
            } else if (cmd.equals("DELETE")) {
                deleteSensor(sc.next());
            } else if (cmd.equals("PRINT")) {
                printSensors();
            } else if (cmd.equals("SORT")) {
                String mode = sc.next();
                if (mode.equals("ASC"))
                    sortAsc();
                else
                    sortDesc();
            }
        }
        sc.close();
    }
}