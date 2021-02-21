package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
    // YOUR TESTS HERE
    @Test
    public void testThreeAddThreeRemove() {
        AListNoResizing<Integer> noBug = new AListNoResizing<>();
        BuggyAList<Integer> bug = new BuggyAList<>();

        noBug.addLast(4);
        noBug.addLast(5);
        noBug.addLast(6);

        bug.addLast(4);
        bug.addLast(5);
        bug.addLast(6);

        assertEquals(noBug.size(), bug.size());

        assertEquals(noBug.removeLast(), bug.removeLast());
        assertEquals(noBug.removeLast(), bug.removeLast());
        assertEquals(noBug.removeLast(), bug.removeLast());
    }

    @Test
    public void randomizedTest() {
        AListNoResizing<Integer> L = new AListNoResizing<>();
        BuggyAList<Integer> B = new BuggyAList<>();

        int N = 1000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                B.addLast(randVal);
            } else if (operationNumber == 1) {
                // size
                int size1 = L.size();
                int size2 = L.size();
            } else if (operationNumber == 2) {
                // getLast
                int size = L.size();
                if (size <= 0) {
                    continue;
                }
            } else if (operationNumber == 3) {
                // removeLast
                int size = L.size();
                if (size <= 0) {
                    continue;
                }
            }
        }
        assertEquals(L.getLast(), B.getLast());
    }
}
