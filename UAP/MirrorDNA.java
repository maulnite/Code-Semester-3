package UAP;

import java.util.Scanner;

class Node {
    char base;
    Node next;

    Node(char base) {
        this.base = base;
        this.next = null;
    }
}

class DNASequence {
    private Node head;

    public DNASequence() {
        this.head = null;
    }

    public void insert(char base) {
        Node newNode = new Node(base);
        if (head == null) {
            head = newNode;
        } else {
            Node temp = head;
            while (temp.next != null) {
                temp = temp.next;
            }
            temp.next = newNode;
        }
        System.out.println("basa " + base + " ditambahkan");
    }

    public void display() {
        if (head == null) {
            System.out.println("sekuens kosong");
            return;
        }

        Node temp = head;
        while (temp != null) {
            System.out.print(temp.base);
            if (temp.next != null) {
                System.out.print(" ");
            }
            temp = temp.next;
        }
        System.out.println();
    }

    // Method untuk memeriksa apakah palindrome (Mirror DNA)
    public void check() {
        if (head == null) {
            System.out.println("sekuens kosong");
            return;
        }

        // Strategi: Salin linked list ke String/Array untuk pengecekan mudah
        // tanpa mengubah struktur list asli.
        String sequenceStr = "";
        Node temp = head;
        while (temp != null) {
            sequenceStr += temp.base;
            temp = temp.next;
        }

        // Cek Palindrome menggunakan string yang sudah dibangun
        boolean isPalindrome = true;
        int len = sequenceStr.length();
        for (int i = 0; i < len / 2; i++) {
            if (sequenceStr.charAt(i) != sequenceStr.charAt(len - 1 - i)) {
                isPalindrome = false;
                break;
            }
        }

        if (isPalindrome) {
            System.out.println("mirror dna");
        } else {
            System.out.println("bukan mirror dna");
        }
    }

    public void reverse() {
        if (head == null) {
            System.out.println("sekuens kosong");
            return;
        }

        Node prev = null;
        Node current = head;
        Node next = null;

        while (current != null) {
            next = current.next; // Simpan node selanjutnya
            current.next = prev; // Balikkan pointer
            prev = current; // Geser prev maju
            current = next; // Geser current maju
        }
        head = prev; // Head baru adalah node terakhir yang diproses
        System.out.println("sekuens dibalik");
    }
}

public class MirrorDNA {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        if (in.hasNextInt()) {
            int Q = in.nextInt();
            in.nextLine(); // Konsumsi newline setelah angka Q

            DNASequence dna = new DNASequence();

            for (int i = 0; i < Q; i++) {
                String line = in.nextLine();
                String[] parts = line.split(" ");
                String command = parts[0];

                switch (command) {
                    case "INSERT":
                        char base = parts[1].charAt(0);
                        dna.insert(base);
                        break;
                    case "DISPLAY":
                        dna.display();
                        break;
                    case "CHECK":
                        dna.check();
                        break;
                    case "REVERSE":
                        dna.reverse();
                        break;
                }
            }
        }
        in.close();
    }
}