package com.tommwq.jet.event.id;

import java.util.Optional;

public interface IdentifiablePool<T extends Id, U extends Identifiable<T>> {
  boolean contains(T aId);

  void add(U aObject);

  void remove(U aObject);

  Optional<U> get(T aId);
}
