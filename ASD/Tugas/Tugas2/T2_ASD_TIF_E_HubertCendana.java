package ASD.Tugas.Tugas2;
/*
 * 245150200111064 Sulthan Rafi Putra Sukma
 * 245150207111049 Hubert Cendana
 * 245150207111050 Orie Abyan Maulana
 * 245150201111053 Farrel Brilliant
 */

import java.util.*;

public class T2_ASD_TIF_E_HubertCendana {

    static class HtmlElement {
        String tagName;
        String id;
        String textContent;
        HtmlElement parent;
        List<HtmlElement> children;

        public HtmlElement(String tagName, String id) {
            this.tagName = tagName;
            this.id = id;
            this.textContent = null;
            this.children = new ArrayList<>();
            this.parent = null;
        }

        public void addChild(HtmlElement child) {
            child.parent = this;
            this.children.add(child);
        }

        public void removeChild(HtmlElement child) {
            this.children.remove(child);
            child.parent = null;
        }

        public String getId() {
            return id == null ? "" : id;
        }

        public boolean isTextNode() {
            return textContent != null;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        HtmlElement root = new HtmlElement("html", "html");

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty())
                continue;

            String[] parts = line.split(";\\s*");
            String command = parts[0];

            try {
                switch (command) {
                    case "ADD":
                        if (parts[1].equals("TEXT")) {
                            handleAddText(root, parts[2], parts[3]);
                        } else {
                            handleAddElement(root, parts[1], parts[2], parts[3]);
                        }
                        break;
                    case "DELETE":
                        handleDelete(root, parts[1]);
                        break;
                    case "PRINT":
                        handlePrint(root, parts[1], Integer.parseInt(parts[2]));
                        break;
                    case "SEARCH":
                        handleSearch(root, parts[1]);
                        break;
                    case "CHILDREN_COUNT":
                        handleChildrenCount(root, parts[1]);
                        break;
                }
            } catch (Exception e) {
            }
        }
        scanner.close();
    }

    private static void handleAddElement(HtmlElement root, String selector, String tagName, String childId) {
        List<HtmlElement> parents = findElementsBySelector(root, selector);

        if (parents.isEmpty()) {
            System.out.println("tidak ditemukan " + selector);
            return;
        }

        parents.sort(Comparator.comparing(HtmlElement::getId));
        boolean multipleParents = parents.size() > 1;
        int counter = 1;

        for (HtmlElement parent : parents) {
            String currentChildId = childId;
            if (multipleParents) {
                currentChildId = childId + counter;
                counter++;
            }

            if (findElementById(root, currentChildId) != null) {
                System.out.println(currentChildId + " sudah ada");
                continue;
            }

            HtmlElement newChild = new HtmlElement(tagName, currentChildId);
            parent.addChild(newChild);
            System.out.println("tambah <" + tagName + " id=\"" + currentChildId + "\"> pada " + parent.id);
        }
    }

    private static void handleAddText(HtmlElement root, String selector, String textContent) {
        List<HtmlElement> parents = findElementsBySelector(root, selector);

        if (parents.isEmpty()) {
            System.out.println("tidak ditemukan " + selector);
            return;
        }

        parents.sort(Comparator.comparing(HtmlElement::getId));

        for (HtmlElement parent : parents) {
            HtmlElement textNode = new HtmlElement("text", null);
            textNode.textContent = textContent;
            parent.addChild(textNode);
            System.out.println("tambah teks \"" + textContent + "\" pada " + parent.id);
        }
    }

    private static void handleDelete(HtmlElement root, String selector) {
        List<HtmlElement> targets = findElementsBySelector(root, selector);

        if (targets.isEmpty()) {
            System.out.println("tidak ditemukan " + selector);
            return;
        }

        targets.sort(Comparator.comparing(HtmlElement::getId));

        for (HtmlElement target : targets) {
            if (target == root)
                continue;
            printDeleteLogPostOrder(target);
            if (target.parent != null) {
                target.parent.removeChild(target);
            }
        }
    }

    private static void printDeleteLogPostOrder(HtmlElement node) {
        List<HtmlElement> childrenCopy = new ArrayList<>(node.children);
        for (HtmlElement child : childrenCopy) {
            printDeleteLogPostOrder(child);
        }
        if (!node.isTextNode()) {
            System.out.println("hapus " + node.id);
        }
    }

    private static void handlePrint(HtmlElement root, String selector, int maxDepth) {
        List<HtmlElement> targets = findElementsBySelector(root, selector);

        if (targets.isEmpty()) {
            System.out.println("tidak ditemukan " + selector);
            return;
        }

        targets.sort(Comparator.comparing(HtmlElement::getId));

        for (HtmlElement target : targets) {
            printNodeRecursive(target, 0, maxDepth);
        }
    }

    private static void printNodeRecursive(HtmlElement node, int currentDepth, int maxDepth) {
        StringBuilder indent = new StringBuilder();
        for (int i = 0; i < currentDepth; i++)
            indent.append("---");

        if (currentDepth > maxDepth)
            return;

        if (node.isTextNode()) {
            System.out.println(indent + node.textContent);
            return;
        }

        System.out.print(indent + "<" + node.tagName);
        if (node.id != null)
            System.out.print(" id=\"" + node.id + "\"");

        System.out.print(">");

        boolean hasChildren = !node.children.isEmpty();
        boolean isInlineText = false;
        if (node.children.size() == 1 && node.children.get(0).isTextNode()) {
            isInlineText = true;
        }

        if (isInlineText) {
            System.out.print(node.children.get(0).textContent);
            System.out.println("</" + node.tagName + ">");
        } else {
            System.out.println();

            if (hasChildren && currentDepth < maxDepth) {
                for (HtmlElement child : node.children) {
                    printNodeRecursive(child, currentDepth + 1, maxDepth);
                }
            }

            System.out.println(indent + "</" + node.tagName + ">");
        }
    }

    private static void handleSearch(HtmlElement root, String selector) {
        List<HtmlElement> targets = findElementsBySelector(root, selector);

        if (targets.isEmpty()) {
            System.out.println("tidak ditemukan " + selector);
            return;
        }

        targets.sort(Comparator.comparing(HtmlElement::getId));

        for (HtmlElement target : targets) {
            printPathToRoot(target);
        }
    }

    private static void printPathToRoot(HtmlElement target) {
        List<HtmlElement> path = new ArrayList<>();
        HtmlElement curr = target;
        while (curr != null) {
            path.add(curr);
            curr = curr.parent;
        }
        Collections.reverse(path);

        for (int i = 0; i < path.size(); i++) {
            HtmlElement node = path.get(i);
            StringBuilder indent = new StringBuilder();
            for (int j = 0; j < i; j++)
                indent.append("---");

            System.out.print(indent + "<" + node.tagName + " id=\"" + node.id + "\">");

            if (i == path.size() - 1) {
                System.out.println("</" + node.tagName + ">");
            } else {
                System.out.println();
            }
        }
        for (int i = path.size() - 2; i >= 0; i--) {
            HtmlElement node = path.get(i);
            StringBuilder indent = new StringBuilder();
            for (int j = 0; j < i; j++)
                indent.append("---");
            System.out.println(indent + "</" + node.tagName + ">");
        }
    }

    private static void handleChildrenCount(HtmlElement root, String selector) {
        List<HtmlElement> targets = findElementsBySelector(root, selector);

        if (targets.isEmpty()) {
            System.out.println("tidak ditemukan " + selector);
            return;
        }

        targets.sort(Comparator.comparing(HtmlElement::getId));

        for (HtmlElement target : targets) {
            int count = countDescendantsElementsOnly(target);
            System.out.println(count);
        }
    }

    private static int countDescendantsElementsOnly(HtmlElement node) {
        int count = 0;
        for (HtmlElement child : node.children) {
            if (!child.isTextNode()) {
                count++;
                count += countDescendantsElementsOnly(child);
            }
        }
        return count;
    }

    private static List<HtmlElement> findElementsBySelector(HtmlElement root, String selector) {
        List<HtmlElement> results = new ArrayList<>();

        if (selector.startsWith("#")) {
            String targetId = selector.substring(1);
            HtmlElement res = findElementById(root, targetId);
            if (res != null)
                results.add(res);
        } else {
            findAllElementsByTag(root, selector, results);
        }
        return results;
    }

    private static HtmlElement findElementById(HtmlElement node, String id) {
        if (node.id != null && node.id.equals(id)) {
            return node;
        }
        for (HtmlElement child : node.children) {
            HtmlElement res = findElementById(child, id);
            if (res != null)
                return res;
        }
        return null;
    }

    private static void findAllElementsByTag(HtmlElement node, String tagName, List<HtmlElement> results) {
        if (node.tagName != null && node.tagName.equals(tagName)) {
            results.add(node);
        }
        for (HtmlElement child : node.children) {
            findAllElementsByTag(child, tagName, results);
        }
    }
}