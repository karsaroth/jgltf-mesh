package io.github.chadj2.mesh.buffer;

import de.javagl.jgltf.model.GltfConstants;

import java.nio.ByteBuffer;

/**
 * Triangle indices that works for unsigned integers to the extent allowed by the Java implementation
 * of the glTF specification (buffer positions use int, not long in the underlying library). The
 * end result is that the maximum number of vertices that can be indexed is 2^31 - 1, even though
 * unsigned integers should allow for 2^32 - 1.
 */
public class UIntTriangleIndices extends TriangleIndices<Integer> {

  public UIntTriangleIndices(String _name) {
    super(_name);
  }

  int max = Integer.MAX_VALUE;
  int min = Integer.MIN_VALUE;

  @Override public Integer getMin() {
    return min;
  }

  @Override public Integer getMax() {
    return max;
  }

  /**
   * Add a triangle to the buffer, replaces the add method from the parent class.
   * @param _v1 The first vertex index
   * @param _v2 The second vertex index
   * @param _v3 The third vertex index
   */
  @Override
  public void add(int _v1, int _v2, int _v3) {
    if (_v1 < 0 || _v2 < 0 || _v3 < 0) {
      throw new IllegalArgumentException("Vertex indices must be positive");
    }
    _list.add(_v1);
    _list.add(_v2);
    _list.add(_v3);
    var maxNewVertex = Math.max(_v1, Math.max(_v2, _v3));
    var minNewVertex = Math.min(_v1, Math.min(_v2, _v3));
    if (maxNewVertex > max || max == Integer.MAX_VALUE) {
      max = maxNewVertex;
    }
    if (minNewVertex < min || min == Integer.MIN_VALUE) {
      min = minNewVertex;
    }
  }

  @Override protected void writeBuf(ByteBuffer byteBuffer) {
    for (int i : _list) {
      byteBuffer.putInt(i);
    }
  }

  @Override protected Integer getComponentType() {
    return GltfConstants.GL_UNSIGNED_INT;
  }

  @Override public void clear() {
    super.clear();
    max = Integer.MAX_VALUE;
    min = Integer.MIN_VALUE;
  }
}
