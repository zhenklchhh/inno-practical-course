package test;

import model.LinkedList;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Evgeniy Zaleshchenok
 */
class LinkedListTest {
    @Test
    void test_getSize() {
        LinkedList<Integer> list = new LinkedList<>();
        list.addLast(1);
        list.addLast(2);

        assertEquals(2, list.size());
    }

    @Test
    void test_addFirstInEmptyList() {
        LinkedList<Integer> list = new LinkedList<>();

        list.addFirst(10);

        assertEquals(1, list.size());
        assertEquals(10, list.getFirst());
        assertEquals(10, list.getLast());
    }

    @Test
    void test_addFirstInList() {
        LinkedList<Integer> list = new LinkedList<>();
        list.addLast(12);
        list.addLast(13);
        list.addLast(14);

        list.addFirst(12);

        assertEquals(4, list.size());
        assertEquals(12, list.getFirst());
    }

    @Test
    void test_addLastInEmptyList() {
        LinkedList<Integer> list = new LinkedList<>();

        list.addLast(10);

        assertEquals(1, list.size());
        assertEquals(10, list.getLast());
        assertEquals(10, list.getFirst());
    }

    @Test
    void test_addLastInList() {
        LinkedList<Integer> list = new LinkedList<>();
        list.addLast(12);
        list.addLast(13);
        list.addLast(14);

        assertEquals(3, list.size());
        assertEquals(14, list.getLast());
    }

    @Test
    void test_addInList_outOfBoundIndex(){
        LinkedList<Integer> list = new LinkedList<>();

        assertThrows(IndexOutOfBoundsException.class, () -> {
            list.add(2, 10);
        });
    }

    @Test
    void test_addInList(){
        LinkedList<Integer> list = new LinkedList<>();
        list.addLast(12);
        list.addLast(13);
        list.addLast(14);

        list.add(1, 10);

        assertEquals(4, list.size());
        assertEquals(10, list.remove(1));
    }

    @Test
    void test_getFirst(){
        LinkedList<Integer> list = new LinkedList<>();
        list.addLast(12);
        assertEquals(12, list.getFirst());
    }

    @Test
    void test_getFirts_EmptyList(){
        LinkedList<Integer> list = new LinkedList<>();
        assertNull(list.getFirst());
    }

    @Test
    void test_getLast(){
        LinkedList<Integer> list = new LinkedList<>();
        list.addLast(12);
        list.addLast(13);
        assertEquals(13, list.getLast());
    }

    @Test
    void test_getLast_EmptyList(){
        LinkedList<Integer> list = new LinkedList<>();
        assertNull(list.getLast());
    }

    @Test
    void test_get_OutOfBounds(){
        LinkedList<Integer> list = new LinkedList<>();
        assertThrows(IndexOutOfBoundsException.class, () -> {
            list.get(1);
        });
    }

    @Test
    void test_get(){
        LinkedList<Integer> list = new LinkedList<>();
        list.addLast(12);
        list.addLast(13);
        list.addLast(14);

        assertEquals(13, list.get(1));
    }

    @Test
    void test_removeFirst_EmptyList(){
        LinkedList<Integer> list = new LinkedList<>();

        assertNull(list.removeFirst());
    }

    @Test
    void test_removeFirst_oneElement(){
        LinkedList<Integer> list = new LinkedList<>();
        list.addLast(12);

        assertEquals(12, list.removeFirst());
        assertNull(list.getFirst());
        assertNull(list.getLast());
        assertEquals(0, list.size());
    }

    @Test
    void test_removeFirst(){
        LinkedList<Integer> list = new LinkedList<>();
        list.addLast(12);
        list.addLast(13);

        assertEquals(12, list.removeFirst());
        assertEquals(13, list.getFirst());
        assertEquals(13, list.getLast());
        assertEquals(1, list.size());
    }

    @Test
    void test_removeLast_EmptyList(){
        LinkedList<Integer> list = new LinkedList<>();

        assertNull(list.removeLast());
    }

    @Test
    void test_removeLast_oneElement(){
        LinkedList<Integer> list = new LinkedList<>();
        list.addLast(12);

        assertEquals(12, list.removeLast());
        assertNull(list.getFirst());
        assertNull(list.getLast());
    }

    @Test
    void test_removeLast(){
        LinkedList<Integer> list = new LinkedList<>();
        list.addLast(12);
        list.addLast(13);

        assertEquals(13, list.removeLast());
        assertEquals(12, list.getFirst());
        assertEquals(12, list.getLast());
    }

    @Test
    void test_remove_outOfBoundIndex(){
        LinkedList<Integer> list = new LinkedList<>();

        assertThrows(IndexOutOfBoundsException.class, () -> {
            list.remove(2);
        });
    }

    @Test
    void test_remove(){
        LinkedList<Integer> list = new LinkedList<>();
        list.addLast(12);
        list.addLast(13);
        list.addLast(14);

        assertEquals(13, list.remove(1));
        assertEquals(2, list.size());
    }
}