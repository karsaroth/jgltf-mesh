package io.github.chadj2.mesh.buffer;

import de.javagl.jgltf.impl.v2.Accessor;
import de.javagl.jgltf.impl.v2.BufferView;
import de.javagl.jgltf.impl.v2.GlTF;
import de.javagl.jgltf.impl.v2.MeshPrimitive;
import de.javagl.jgltf.model.GltfConstants;
import io.github.chadj2.mesh.MeshGltfWriter;

import java.nio.ByteBuffer;

/**
 * Line indices that works for unsigned integers to the extent allowed by the Java implementation
 * of the glTF specification (buffer positions use int, not long in the underlying library). The
 * end result is that the maximum number of vertices that can be indexed is 2^31 - 1, even though
 * unsigned integers should allow for 2^32 - 1.
 */
public class UIntLineIndices extends ScalarBuffer<Integer> {
  public UIntLineIndices(String _name) {
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

  @Override public void add(Integer _primitive) {
    throw new UnsupportedOperationException("Not Implemented");
  }

  @Override protected void writeBuf(ByteBuffer byteBuffer) {
    for (int i : _list) {
      byteBuffer.putInt(i);
    }
  }

  @Override protected Integer getComponentType() {
    return GltfConstants.GL_UNSIGNED_INT;
  }

  /**
   * Add a line to the buffer, replaces the add method from the parent class.
   * @param _v1 The first vertex index
   * @param _v2 The second vertex index
   */
  public void add(int _v1, int _v2) {
    if (_v1 < 0 || _v2 < 0) {
      throw new IllegalArgumentException("Vertex indices must be positive");
    }
    _list.add(_v1);
    _list.add(_v2);
    var maxNewVertex = Math.max(_v1, _v2);
    var minNewVertex = Math.min(_v1, _v2);
    if (maxNewVertex > max || max == Integer.MAX_VALUE) {
      max = maxNewVertex;
    }
    if (minNewVertex < min || min == Integer.MIN_VALUE) {
      min = minNewVertex;
    }
  }

  /**
   * Creates the accessor for the buffer and links it to the mesh primitive.
   * @param _geoWriter The glTF writer to add the buffer to
   * @param _meshPrimitive The mesh primitive to add the indices to
   * @return The accessor for the buffer that was created
   */
  public Accessor build(MeshGltfWriter _geoWriter, MeshPrimitive _meshPrimitive) {
    Accessor _accessor = super.buildBuffer(_geoWriter);
    if(_accessor == null) {
      return null;
    }

    int _accessorIdx = _geoWriter.getGltf().getAccessors().indexOf(_accessor);
    _meshPrimitive.setIndices(_accessorIdx);
    return _accessor;
  }

  @Override
  protected BufferView addBufferView(GlTF _gltf, ByteBuffer _buffer) {
    BufferView _bufferView = super.addBufferView(_gltf, _buffer);
    _bufferView.setTarget(GltfConstants.GL_ELEMENT_ARRAY_BUFFER);
    return _bufferView;
  }

  @Override
  protected void setBufferViewByteStride(BufferView _bufferView) {
    _bufferView.setByteStride(Byte.BYTES);
  }
}
