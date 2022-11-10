package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

class GroupListTest {

    @Test
    void groupByOddAndEven() {
        List<Integer> integers = IntStream.rangeClosed(1, 5).boxed().toList();

        Collection<List<Integer>> result = new GroupList<Integer>().group(integers, i -> i % 2 == 0);

        assertIterableEquals(List.of(List.of(1, 3, 5), List.of(2, 4)), result);
    }

    @Test
    void groupContiguous() {
        List<Integer> integers = List.of(1, 2);

        List<List<Integer>> reduce = integers.stream().reduce(new ArrayList<List<Integer>>(), (List<List<Integer>> l, Integer i) -> {
            l.add(List.of(i));
            return l;
        }, (l1, l2) -> Stream.concat(l1.stream(), l2.stream()).toList());

        System.out.println(reduce);
    }
}