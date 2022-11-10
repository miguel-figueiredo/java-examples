package org.example;

import static java.util.stream.Collectors.groupingBy;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public class GroupList<T> {

    public Collection<List<T>> group(List<T> integers, Function<T, Boolean> function){
        return integers.stream().collect(groupingBy(function)).values();
    }
}
