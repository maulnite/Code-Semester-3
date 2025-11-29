package ASD.Tugas.Tugas2;

/*
 * 245150200111064 Sulthan Rafi Putra Sukma
 * 245150207111049 Hubert Cendana
 * 245150207111050 Orie Abyan Maulana
 * 245150201111053 Farrel Brilliant
 */

import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Collections;

class TreeNode {
    String tagName;
    String id;
    String text;
    List<TreeNode> children;
    TreeNode parent;

    boolean isTextNode;

    public TreeNode(String tagName, String id, TreeNode parent) {
        this.tagName = tagName;
        this.id = id;
        this.children = new ArrayList<>();
        this.parent = parent;
        this.isTextNode = false;
        this.text = null;
    }

    public TreeNode(String text, TreeNode parent) {
        this.tagName = "#text";
        this.id = null;
        this.children = null;
        this.parent = parent;
        this.isTextNode = true;
        this.text = text;
    }

    public String getIdentifier() {
        return this.id != null ? this.id : "";
    }
}

class HTMLTree {
    TreeNode root;
    HashMap<String, TreeNode> idMap;

    public HTMLTree() {
        root = new TreeNode("html", "html", null);
        idMap = new HashMap<>();
        idMap.put("html", root);
    }

    // --- SELECTOR ---
    public List<TreeNode> findNodes(String selector) {
        List<TreeNode> results = new ArrayList<>();

        if (selector.startsWith("#")) {
            String idToFind = selector.substring(1);
            TreeNode node = idMap.get(idToFind);
            if (node != null) {
                results.add(node);
            }
        } else {
            findNodesByTagRecursive(root, selector, results);
        }

        Collections.sort(results, (n1, n2) -> n1.getIdentifier().compareTo(n2.getIdentifier()));
        return results;
    }

    private void findNodesByTagRecursive(TreeNode current, String tag, List<TreeNode> results) {
        if (current.tagName.equals(tag)) {
            results.add(current);
        }
        if (current.children != null) {
            for (TreeNode child : current.children) {
                if (!child.isTextNode) {
                    findNodesByTagRecursive(child, tag, results);
                }
            }
        }
    }

    // --- ADD COMMANDS ---
    public void addTag(String selector, String tagNameChild, String idChild) {
        List<TreeNode> parents = findNodes(selector);

        if (parents.isEmpty()) {
            System.out.println("tidak ditemukan " + selector);
            return;
        }

        for (int i = 0; i < parents.size(); i++) {
            TreeNode parent = parents.get(i);
            String currentId;

            if (parents.size() > 1) {
                currentId = idChild + (i + 1);
            } else {
                currentId = idChild;
            }

            if (idMap.containsKey(currentId)) {
                System.out.println(currentId + " sudah ada");
                continue;
            }

            TreeNode newNode = new TreeNode(tagNameChild, currentId, parent);
            parent.children.add(newNode);
            idMap.put(currentId, newNode);

            System.out.println("tambah <" + tagNameChild + " id=\"" + currentId + "\"> pada " + parent.id);
        }
    }

    public void addText(String selector, String textContent) {
        List<TreeNode> parents = findNodes(selector);

        if (parents.isEmpty()) {
            System.out.println("tidak ditemukan " + selector);
            return;
        }

        for (TreeNode parent : parents) {
            TreeNode newTextNode = new TreeNode(textContent, parent);
            parent.children.add(newTextNode);
            System.out.println("tambah teks \"" + textContent + "\" pada " + parent.id);
        }
    }

    // --- DELETE COMMAND ---
    public void deleteNodes(String selector) {
        List<TreeNode> targets = findNodes(selector);

        if (targets.isEmpty()) {
            System.out.println("tidak ditemukan " + selector);
            return;
        }

        for (TreeNode target : targets) {
            if (target.id.equals("html")) {
                continue;
            }
            deletePostOrderRecursive(target);
            if (target.parent != null && target.parent.children != null) {
                target.parent.children.remove(target);
            }
            target.parent = null;
        }
    }

    private void deletePostOrderRecursive(TreeNode node) {
        if (node.isTextNode)
            return;

        if (node.children != null) {
            List<TreeNode> childrenCopy = new ArrayList<>(node.children);
            for (TreeNode child : childrenCopy) {
                deletePostOrderRecursive(child);
            }
            node.children.clear();
        }

        idMap.remove(node.id);
        System.out.println("hapus " + node.id);
    }

    // --- PRINT COMMAND ---
    public void printTree(String selector, int maxDepth) {
        List<TreeNode> targets = findNodes(selector);

        if (targets.isEmpty()) {
            System.out.println("tidak ditemukan " + selector);
            return;
        }

        for (TreeNode target : targets) {
            printPreOrderRecursive(target, 0, maxDepth);
        }
    }

    private void printPreOrderRecursive(TreeNode node, int currentDepth, int maxDepth) {
        if (maxDepth != -1 && currentDepth > maxDepth) {
            return;
        }

        StringBuilder indent = new StringBuilder();
        for (int i = 0; i < currentDepth; i++) {
            indent.append("---");
        }

        if (node.isTextNode) {
            System.out.println(indent + node.text);
            return;
        }

        System.out.print(indent + "<" + node.tagName + " id=\"" + node.id + "\">");

        boolean singleLine = false;

        if (node.children != null && node.children.size() == 1 && node.children.get(0).isTextNode) {
            System.out.print(node.children.get(0).text);
            singleLine = true;
        }

        if (singleLine) {
            System.out.println("</" + node.tagName + ">");
            return;
        }

        System.out.println();

        if (node.children != null) {
            for (TreeNode child : node.children) {
                printPreOrderRecursive(child, currentDepth + 1, maxDepth);
            }
        }

        System.out.println(indent + "</" + node.tagName + ">");
    }

    // --- SEARCH COMMAND ---
    public void searchNode(String selector) {
        List<TreeNode> targets = findNodes(selector);

        if (targets.isEmpty()) {
            System.out.println("tidak ditemukan " + selector);
            return;
        }

        for (TreeNode target : targets) {
            List<TreeNode> path = new ArrayList<>();
            TreeNode temp = target;
            while (temp != null) {
                path.add(0, temp);
                temp = temp.parent;
            }

            for (int i = 0; i < path.size(); i++) {
                TreeNode node = path.get(i);
                StringBuilder indent = new StringBuilder();
                for (int j = 0; j < i; j++) {
                    indent.append("---");
                }

                if (node.isTextNode) {
                    System.out.println(indent + node.text);
                } else {
                    System.out.println(indent + "<" + node.tagName + " id=\"" + node.id + "\">");
                }
            }
            System.out.println();
        }
    }

    // --- CHILDREN_COUNT COMMAND ---
    public void countDescendants(String selector) {
        List<TreeNode> targets = findNodes(selector);

        if (targets.isEmpty()) {
            System.out.println("tidak ditemukan " + selector);
            return;
        }

        for (TreeNode target : targets) {
            long count = countDescendantsRecursive(target) - 1;
            System.out.println(count);
        }
    }

    private long countDescendantsRecursive(TreeNode node) {
        long count = 1;

        if (node.children != null) {
            for (TreeNode child : node.children) {
                if (child.isTextNode)
                    continue;

                count += countDescendantsRecursive(child);
            }
        }
        return count;
    }
}

public class T2_ASD_TIF_E_OrieAbyanMaulana {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        HTMLTree tree = new HTMLTree();

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if (line.trim().isEmpty())
                continue;

            String[] parts = line.split(";");
            String command = parts[0].trim();

            try {
                switch (command) {
                    case "ADD":
                        String checkType = parts[1].trim();
                        if (checkType.equals("TEXT")) {
                            String selector = parts[2].trim();
                            String textContent = parts[3].trim();
                            tree.addText(selector, textContent);
                        } else {
                            String selector = parts[1].trim();
                            String tagNameChild = parts[2].trim();
                            String idChild = parts[3].trim();
                            tree.addTag(selector, tagNameChild, idChild);
                        }
                        break;

                    case "DELETE":
                        String deleteSelector = parts[1].trim();
                        tree.deleteNodes(deleteSelector);
                        break;

                    case "PRINT":
                        String printSelector = parts[1].trim();
                        int maxDepth = Integer.parseInt(parts[2].trim());
                        tree.printTree(printSelector, maxDepth);
                        break;

                    case "SEARCH":
                        String searchSelector = parts[1].trim();
                        tree.searchNode(searchSelector);
                        break;

                    case "CHILDREN_COUNT":
                        String countSelector = parts[1].trim();
                        tree.countDescendants(countSelector);
                        break;
                }
            } catch (Exception e) {
            }
        }
        sc.close();
    }
}