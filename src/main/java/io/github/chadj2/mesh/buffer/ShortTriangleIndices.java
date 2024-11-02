/* 
 * Copyright (c) 2019, Chad Juliano, Kinetica DB Inc.
 * 
 * SPDX-License-Identifier: MIT
 */

package io.github.chadj2.mesh.buffer;

import java.nio.ByteBuffer;
import java.util.Collections;

import de.javagl.jgltf.impl.v2.BufferView;
import de.javagl.jgltf.impl.v2.GlTF;
import de.javagl.jgltf.model.GltfConstants;

/**
 * Serializer for triangle index primitives.
 * @author Chad Juliano
 */
public class ShortTriangleIndices extends TriangleIndices<Short> {
    
    public static final Integer MAX_INDEX = 65535;
    
    public ShortTriangleIndices(String _name) {
        super(_name);
    }

    @Override
    public Short getMin() {
        return Collections.min(this._list);
    }

    @Override
    public Short getMax() {
        return Collections.max(this._list);
    }
    
    public void add(int _v1, int _v2, int _v3) throws IllegalArgumentException {
        if(_v1 >= MAX_INDEX || _v2 >= MAX_INDEX || _v3 >= MAX_INDEX) {
            String msg = String.format("Trangle idex cannot exceed %d", MAX_INDEX);
            throw new IllegalArgumentException(msg);
        }
        this._list.add((short)_v1);
        this._list.add((short)_v2);
        this._list.add((short)_v3);
    }

    @Override
    protected Integer getComponentType() {
        return GltfConstants.GL_UNSIGNED_SHORT;
    }

    @Override
    protected void writeBuf(ByteBuffer _buffer) {
        for(short _s : this._list) {
            _buffer.putShort(_s);
        }
    }
}
