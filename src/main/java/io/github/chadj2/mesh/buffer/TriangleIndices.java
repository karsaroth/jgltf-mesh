/*
 * Copyright (c) 2019, Chad Juliano, Kinetica DB Inc.
 *
 * SPDX-License-Identifier: MIT
 */

package io.github.chadj2.mesh.buffer;

/**
 * Serializer for triangle index primitives.
 *
 * @author Chad Juliano
 */
public abstract class TriangleIndices<T extends Number> extends ScalarBuffer<T> {
  public TriangleIndices(String _name) {
    super(_name);
  }

  @Override public final void add(T _primitive) {
    throw new UnsupportedOperationException("not implemented");
  }

  public abstract void add(int _v1, int _v2, int _v3) throws IllegalArgumentException;
}
