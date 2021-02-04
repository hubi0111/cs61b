package tester;

import static org.junit.Assert.*;

import edu.princeton.cs.introcs.StdRandom;
import org.junit.Test;
import student.StudentArrayDeque;

public class TestArrayDequeEC {
    @Test
    /**
     * @source code from lab 3
     */
    public void testArrayDeque() {
        StudentArrayDeque<Integer> L = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> B = new ArrayDequeSolution<>();
        String message = "";

        int N = 1000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                B.addLast(randVal);
                message += "addLast(" + randVal + ")\n";
            } else if (operationNumber == 1) {
                // addFirst
                int randVal = StdRandom.uniform(0, 100);
                L.addFirst(randVal);
                B.addFirst(randVal);
                message += "addFirst(" + randVal + ")\n";
            } else if (operationNumber == 2) {
                // removeFirst
                if (L.isEmpty()) {
                    continue;
                }
                Integer x = L.removeFirst();
                Integer y = B.removeFirst();
                message += "removeFirst()\n";
                assertEquals(message, y, x);
            } else if (operationNumber == 3) {
                // removeLast
                if (L.isEmpty()) {
                    continue;
                }
                Integer x = L.removeLast();
                Integer y = B.removeLast();
                message += "removeLast()\n";
                assertEquals(message, y, x);
            }
            assertEquals(B.size(), L.size());
        }
    }
}
