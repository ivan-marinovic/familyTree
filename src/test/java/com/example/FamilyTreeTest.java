package com.example;
import com.example.exception.CyclicRelationshipFoundException;
import com.example.exception.NoRootFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FamilyTreeTest {

    @Test
    public void testFamilyTreeWithValidInput() throws CyclicRelationshipFoundException, NoRootFoundException {
        String filePath = "src/test/resources/valid_input.txt";
        FamilyTree familyTree = new FamilyTree(filePath);
        Assertions.assertDoesNotThrow(familyTree::printFamilyTree);
    }

    @Test
    public void testInvalidFamilyTreeWithCyclicRelationship() {
        String filePath = "src/test/resources/cyclic_input.txt";
        assertThrows(CyclicRelationshipFoundException.class, () -> new FamilyTree(filePath));
    }

    @Test
    public void testFamilyTreeIfFilePathIsInvalid() {
        String filePath = "invalid/path/invalid.txt";
        assertThrows(IllegalArgumentException.class, () -> new FamilyTree(filePath));
    }

    @Test
    void testParentChildrenMap() throws CyclicRelationshipFoundException, NoRootFoundException {
        String filePath = "src/test/resources/valid_input.txt";
        FamilyTree familyTree = new FamilyTree(filePath);
        assertNotNull(familyTree.parentChildrenMap);
        assertFalse(familyTree.parentChildrenMap.isEmpty());
    }

    @Test
    void testCyclicRelationshipException() {
        String filePath = "src/test/resources/cyclic_input.txt";
        Exception exception = assertThrows(CyclicRelationshipFoundException.class, () -> {
            new FamilyTree(filePath);
        });
        String expectedMessage = "Cyclic relationship founded!";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testPrintFamilyTree_shouldReturnNoRootFound() throws NoRootFoundException {
        String filePath = "src/test/resources/empty_input.txt";
        assertThrows(NoRootFoundException.class, () -> {
            FamilyTree familyTree = new FamilyTree(filePath);
            familyTree.printFamilyTree();
        });

    }

}
