package org.example.immutables;

import org.immutables.value.Value;

@Value.Immutable
public abstract class FoobarValue {
  public abstract int foo();
}