package wazoo.filtering;

import java.util.*;

public class AhoCorasick {
    private TrieNode root;

    public AhoCorasick() {
        root = new TrieNode();
    }

    public void addKeyword(String keyword) {
        TrieNode node = root;
        for (char c : keyword.toCharArray()) {
            node = node.children.computeIfAbsent(c, k -> new TrieNode());
        }
        node.outputs.add(keyword);
    }

    public void buildFailureLinks() {
        Queue<TrieNode> queue = new LinkedList<>();
        root.fail = root;

        for (TrieNode node : root.children.values()) {
            node.fail = root;
            queue.add(node);
        }

        while (!queue.isEmpty()) {
            TrieNode current = queue.poll();

            for (Map.Entry<Character, TrieNode> entry : current.children.entrySet()) {
                char c = entry.getKey();
                TrieNode child = entry.getValue();
                queue.add(child);

                TrieNode failNode = current.fail;
                while (failNode != root && !failNode.children.containsKey(c)) {
                    failNode = failNode.fail;
                }
                if (failNode.children.containsKey(c)) {
                    child.fail = failNode.children.get(c);
                } else {
                    child.fail = root;
                }
                child.outputs.addAll(child.fail.outputs);
            }
        }
    }

    public Set<String> search(String text) {
        Set<String> result = new HashSet<>();
        TrieNode node = root;

        for (char c : text.toCharArray()) {
            while (node != root && !node.children.containsKey(c)) {
                node = node.fail;
            }
            if (node.children.containsKey(c)) {
                node = node.children.get(c);
            } else {
                node = root;
            }
            result.addAll(node.outputs);
        }

        return result;
    }
}