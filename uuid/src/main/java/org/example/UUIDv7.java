package org.example;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.NoArgGenerator;
import com.fasterxml.uuid.UUIDGenerator;
import com.fasterxml.uuid.impl.TimeBasedEpochGenerator;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class UUIDv7 {

    private static final int SIZE = 100;

    public static void main(String[] args) {
        final TimeBasedEpochGenerator generator = Generators.timeBasedEpochGenerator();
//        final NoArgGenerator generator = Generators.timeBasedEpochRandomGenerator();
        final Set<UUID> uuids = new HashSet<>(SIZE);
        for (int i = 0; i < SIZE; i++) {
            final UUID uuid = generator.generate();
            System.out.println(uuid);
            uuids.add(uuid);

        }

        System.out.println("Generated " + uuids.size() + " UUIDs");

    }
}
