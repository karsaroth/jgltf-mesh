package io.github.chadj2.mesh;

import de.javagl.jgltf.impl.v2.MeshPrimitive;
import io.github.chadj2.mesh.buffer.BufferVecFloat2;
import io.github.chadj2.mesh.buffer.UIntLineIndices;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.vecmath.Point2f;

public class TreeBuilder extends TopologyBuilder {

  /** The indices keep track of connectivity between line vertices. */
  protected final List<Integer> _indicesList = new ArrayList<>();

  /**
   * @param _name Name of the glTF mesh node.
   */
  public TreeBuilder(String _name) {
    super(_name, TopologyMode.LINES);
  }

  @Override
  public void clear() { this._indicesList.clear(); }

  public void addLine(MeshVertex _vtx0, MeshVertex _vtx1) {
    // add indices
    this._indicesList.add(_vtx0.getIndex());
    this._indicesList.add(_vtx1.getIndex());
  }

  @Override
  protected void buildBuffers(MeshGltfWriter _geoWriter, MeshPrimitive _meshPrimitive) throws Exception {
    super.buildBuffers(_geoWriter, _meshPrimitive);

    if(this._indicesList.isEmpty()) {
      throw new Exception("Tree has no indices: " + this.getName());
    }

    BufferVecFloat2 _texCoords = new BufferVecFloat2(this.getName() + "-texCoords");

    for(MeshVertex _meshVertex : this._vertexList) {
      Point2f _texCoord = _meshVertex.getTexCoord();
      if(_texCoord != null) {
        _texCoords.add(_texCoord);
      }

      if(_texCoords.size() > 0 && _texCoord == null) {
        throw new Exception("Each Vertex must have a texCoord: " + _meshVertex);
      }
    }

    // copy triangles to the buffer
    UIntLineIndices indices  = new UIntLineIndices(this.getName());
    Iterator<Integer> iter = this._indicesList.iterator();
    while(iter.hasNext()) {
      indices.add(iter.next(), iter.next());
    }

    // flush all buffers to the primitive
    indices.build(_geoWriter, _meshPrimitive);
    _texCoords.buildAttrib(_geoWriter, _meshPrimitive, "TEXCOORD_0");

    this._indicesList.clear();
  }
}
