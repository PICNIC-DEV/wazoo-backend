package wazoo.filtering;

import java.util.*;

public class TrieNode {
    Map<Character, TrieNode> children = new HashMap<>();
    TrieNode fail;
    Set<String> outputs = new HashSet<>();
}
