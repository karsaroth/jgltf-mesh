package io.github.chadj2.mesh.buffer;

import de.javagl.jgltf.impl.v2.Accessor;
import de.javagl.jgltf.impl.v2.BufferView;
import de.javagl.jgltf.impl.v2.GlTF;
import de.javagl.jgltf.impl.v2.MeshPrimitive;
import de.javagl.jgltf.model.GltfConstants;
import io.github.chadj2.mesh.MeshGltfWriter;

public abstract class ScalarBuffer<T extends Number> extends BufferBase<T> {
  public ScalarBuffer(String _name) {
    super(_name);
  }

  @Override
  protected Accessor addAccessor(GlTF _gltf, BufferView _bufferView) {
    Accessor _accessor = super.addAccessor(_gltf, _bufferView);
    _accessor.setType("SCALAR");
    _accessor.setMax(new Number[] {
        getMax() });

    _accessor.setMin(new Number[] {
        getMin() });
    _accessor.setComponentType(getComponentType());

    return _accessor;
  }

  public Accessor build(MeshGltfWriter _geoWriter, MeshPrimitive _meshPirimitive) {
    Accessor _accessor = super.buildBuffer(_geoWriter);
    if(_accessor == null) {
      return null;
    }

    int _accessorIdx = _geoWriter.getGltf().getAccessors().indexOf(_accessor);
    _meshPirimitive.setIndices(_accessorIdx);
    return _accessor;
  }

  @Override protected void setBufferViewTarget(BufferView _bufferView) {
    _bufferView.setTarget(GltfConstants.GL_ELEMENT_ARRAY_BUFFER);
  }

  protected abstract Integer getComponentType();
}
