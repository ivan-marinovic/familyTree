package com.example;

import com.example.exception.CyclicRelationshipFoundException;
import com.example.exception.NoRootFoundException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class FamilyTree {

    final Map<String, List<String>> parentChildrenMap;

    public FamilyTree(String filePath) throws CyclicRelationshipFoundException {
        this.parentChildrenMap = new HashMap<>();

        readInputFile(filePath);
        checkForCyclicRelationship();
    }

    void readInputFile(String filePath) {

        try{
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;

            while ((line = reader.readLine()) != null) {

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split("\\s+");

                if (parts.length != 2) {
                    throw new IllegalArgumentException("Invalid input format: " + line);
                }

                String child = parts[0];
                String parent = parts[1];

                if (parentChildrenMap.containsKey(parent)) {
                    parentChildrenMap.get(parent).add(child);
                } else {
                    List<String> children = new ArrayList<>();
                    children.add(child);
                    parentChildrenMap.put(parent, children);
                }

                if (!parentChildrenMap.containsKey(child)) {
                    parentChildrenMap.put(child, new ArrayList<>());
                }

            }
            reader.close();

        } catch (IOException e) {
            throw new IllegalArgumentException("Incorrect file path: " + filePath);
        }

    }

    private boolean hasCycle(String parent, Map<String, List<String>> map, Set<String> visited, Set<String> recursionStack) {
        visited.add(parent);
        recursionStack.add(parent);

        if (map.containsKey(parent)) {
            for (String child : map.get(parent)) {
                if (!visited.contains(child)) {
                    if (hasCycle(child, map, visited, recursionStack)) {
                        return true;
                    }
                } else if (recursionStack.contains(child)) {
                    return true;
                }
            }
        }

        recursionStack.remove(parent);
        return false;
    }

    void checkForCyclicRelationship() throws CyclicRelationshipFoundException {
        for (String parent : parentChildrenMap.keySet()) {
            Set<String> visited = new HashSet<>();
            Set<String> recursionStack = new HashSet<>();

            if (hasCycle(parent, parentChildrenMap, visited, recursionStack)) {
                throw new CyclicRelationshipFoundException("Cyclic relationship founded!");
            }
        }
    }

    void printIndent(int levelOfNode) {
        for (int i = 0; i < levelOfNode; i++) {
            System.out.print("    ");
        }
    }

    private void printFamilyTreeHelper(String parent, int levelOfNode) {

        printIndent(levelOfNode);
        System.out.println(parent);

        for (String child : parentChildrenMap.get(parent)) {
            printFamilyTreeHelper(child, levelOfNode + 1);
        }
    }

    void printFamilyTree() throws NoRootFoundException {

        Set<String> roots = new HashSet<>(parentChildrenMap.keySet());
        for (String parent : parentChildrenMap.keySet()) {
            for (String child : parentChildrenMap.get(parent)) {
                roots.remove(child);
            }
        }

        if (roots.isEmpty()) {
            throw new NoRootFoundException("No root found in file!");
        }

        for (String root : roots) {
            printFamilyTreeHelper(root, 0);
        }
    }

    public static void main(String[] args) throws NoRootFoundException, CyclicRelationshipFoundException {

        FamilyTree familyTree = new FamilyTree("src/main/resources/input.txt");
        familyTree.printFamilyTree();
    }
}
