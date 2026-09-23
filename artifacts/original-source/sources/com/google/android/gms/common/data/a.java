package com.google.android.gms.common.data;

import com.google.android.gms.internal.dm;
import java.util.Iterator;
import java.util.NoSuchElementException;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public final class a<T> implements Iterator<T> {
    private final DataBuffer<T> jg;
    private int jh = -1;

    public a(DataBuffer<T> dataBuffer) {
        this.jg = (DataBuffer) dm.e(dataBuffer);
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        return this.jh < this.jg.getCount() + (-1);
    }

    @Override // java.util.Iterator
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException("Cannot advance the iterator beyond " + this.jh);
        }
        DataBuffer<T> dataBuffer = this.jg;
        int i = this.jh + 1;
        this.jh = i;
        return dataBuffer.get(i);
    }

    @Override // java.util.Iterator
    public void remove() {
        throw new UnsupportedOperationException("Cannot remove elements from a DataBufferIterator");
    }
}
