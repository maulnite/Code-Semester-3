package UAP;

import java.util.Scanner;

public class LoketAkademik {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        if (sc.hasNextInt()) {
            int n = sc.nextInt(); // Kapasitas antrean
            int q = sc.nextInt(); // Jumlah perintah

            StudentQueue queue = new StudentQueue(n);
            BST arsip = new BST();

            for (int i = 0; i < q; i++) {
                String command = sc.next();

                switch (command) {
                    case "ARRIVE":
                        String nim = sc.next();
                        String nama = sc.next();
                        queue.enqueue(nim, nama);
                        break;

                    case "SERVE":
                        Pair student = queue.dequeue();
                        if (student != null) {
                            System.out.println("dilayani " + student.nim + " " + student.nama);
                            arsip.insert(student.nim, student.nama);
                        }
                        break;

                    case "STATUS":
                        queue.printStatus();
                        break;

                    case "ARCHIVE":
                        arsip.printInorder();
                        break;

                    case "LEVEL":
                        arsip.printLevelOrder();
                        break;
                }
            }
        }
        sc.close();
    }
}

// Class sederhana untuk menampung data Mahasiswa
class Pair {
    String nim, nama;

    Pair(String nim, String nama) {
        this.nim = nim;
        this.nama = nama;
    }
}

// Queue berbasis Array untuk antrean mahasiswa
class StudentQueue {
    String[] nim;
    String[] nama;
    int cap;
    int size;

    public StudentQueue(int cap) {
        this.cap = cap;
        this.nim = new String[cap];
        this.nama = new String[cap];
        this.size = 0;
    }

    // Masuk antrean
    public void enqueue(String nimMasuk, String namaMasuk) {
        if (size == cap) {
            System.out.println("antrian penuh");
        } else {
            nim[size] = nimMasuk;
            nama[size] = namaMasuk;
            size++;
        }
    }

    // Keluar antrean (dilayani)
    public Pair dequeue() {
        if (size == 0) {
            System.out.println("antrian kosong");
            return null;
        } else {
            // Ambil data paling depan (index 0)
            Pair p = new Pair(nim[0], nama[0]);

            // Geser elemen ke kiri
            for (int i = 0; i < size - 1; i++) {
                nim[i] = nim[i + 1];
                nama[i] = nama[i + 1];
            }

            size--;
            return p;
        }
    }

    // Cetak status antrean
    public void printStatus() {
        if (size == 0) {
            System.out.println("kosong");
        } else {
            for (int i = 0; i < size; i++) {
                System.out.print(nim[i] + " " + nama[i]);
                if (i < size - 1)
                    System.out.print(" ");
            }
            System.out.println();
        }
    }
}

// Class BST untuk Arsip
class BST {

    // Inner class Node untuk BST
    static class Node {
        String nim, nama;
        Node left, right;

        Node(String nim, String nama) {
            this.nim = nim;
            this.nama = nama;
        }
    }

    Node root;

    public BST() {
        root = null;
    }

    public void insert(String nim, String nama) {
        root = insertRec(root, nim, nama);
    }

    private Node insertRec(Node root, String nim, String nama) {
        if (root == null) {
            return new Node(nim, nama);
        }
        // Bandingkan String NIM
        if (nim.compareTo(root.nim) < 0) {
            root.left = insertRec(root.left, nim, nama);
        } else if (nim.compareTo(root.nim) > 0) {
            root.right = insertRec(root.right, nim, nama);
        }
        return root;
    }

    // Traversal Inorder (Recursive) untuk ARCHIVE
    public void printInorder() {
        if (root == null) {
            System.out.println("arsip kosong");
        } else {
            System.out.print("arsip");
            inorderRec(root);
            System.out.println();
        }
    }

    private void inorderRec(Node root) {
        if (root != null) {
            inorderRec(root.left);
            System.out.print(" " + root.nim + " " + root.nama);
            inorderRec(root.right);
        }
    }

    // Traversal Level Order menggunakan Queue untuk LEVEL
    public void printLevelOrder() {
        if (root == null) {
            System.out.println("arsip kosong");
            return;
        }

        // Buat queue khusus Node BST. Kapasitas 1000 (sesuai max Q)
        NodeQueue q = new NodeQueue(1001);
        q.add(root);

        System.out.print("level");
        while (!q.isEmpty()) {
            Node current = q.poll();
            System.out.print(" " + current.nim + " " + current.nama);

            if (current.left != null)
                q.add(current.left);
            if (current.right != null)
                q.add(current.right);
        }
        System.out.println();
    }
}

class NodeQueue {
    BST.Node[] arr;
    int cap;
    int size;
    int head;
    int tail;

    public NodeQueue(int cap) {
        this.cap = cap;
        this.arr = new BST.Node[cap];
        this.size = 0;
        this.head = 0;
        this.tail = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void add(BST.Node node) {
        if (size < cap) {
            arr[tail] = node;
            tail = (tail + 1) % cap; // Circular logic (optional, but good practice)
            size++;
        }
    }

    public BST.Node poll() {
        if (isEmpty())
            return null;
        BST.Node node = arr[head];
        head = (head + 1) % cap;
        size--;
        return node;
    }
}