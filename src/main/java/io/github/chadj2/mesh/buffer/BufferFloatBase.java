/* 
 * Copyright (c) 2022, Chad Juliano, Kinetica DB Inc.
 * 
 * SPDX-License-Identifier: MIT
 */

package io.github.chadj2.mesh.buffer;

import de.javagl.jgltf.impl.v2.Accessor;
import de.javagl.jgltf.impl.v2.MeshPrimitive;
import io.github.chadj2.mesh.GltfWriter;

public abstract class BufferFloatBase<T> extends BufferBase<T> {
    
    protected T _min;
    protected T _max;
    protected final String _attrib;

    public BufferFloatBase(String _name, String _attrib) {
        super(String.format("%s-%s", _name, _attrib));
        this._attrib = _attrib;
    }

    public T getMin() { return this._min; }

    public T getMax() { return this._max; }
    
    @Override
    public Accessor build(GltfWriter _geoWriter, MeshPrimitive _meshPirimitive) {
        return buildAttrib(_geoWriter, _meshPirimitive, this._attrib);
    }

    public Accessor build(GltfWriter _geoWriter, GlTFMeshGpuInstancing _meshInstancing) {
        return buildAttrib(_geoWriter, _meshInstancing, this._attrib);
    }
    
    protected final Accessor buildAttrib(GltfWriter _geoWriter, GlTFMeshGpuInstancing _meshInstancing,
            String _attribute) {
        Accessor _accessor = buildBuffer(_geoWriter);
        if(_accessor == null) {
            return null;
        }
        
        int _accessorIdx = _geoWriter.getGltf().getAccessors().indexOf(_accessor);
        _meshInstancing.addAttributes(_attribute, _accessorIdx);
        return _accessor;
    }
}
