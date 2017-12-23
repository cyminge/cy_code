/*
 * Copyright 2016 drakeet. https://github.com/drakeet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cy.uiframe.refactor.multitype;

import java.util.ArrayList;
import java.util.List;

import android.support.annotation.NonNull;

/**
 * An List implementation of TypePool.
 *
 * @author drakeet
 */
public class MultiTypePool implements TypePool {

    private @NonNull
    final List<Class<?>> mClasses;
    private @NonNull
    final List<ItemViewBinder<?, ?>> mBinders;
    private @NonNull
    final List<Integer> mLayoutId;

    public MultiTypePool() {
        this.mClasses = new ArrayList<>();
        this.mBinders = new ArrayList<>();
        this.mLayoutId = new ArrayList<>();
    }

    public MultiTypePool(int initialCapacity) {
        this.mClasses = new ArrayList<>(initialCapacity);
        this.mBinders = new ArrayList<>(initialCapacity);
        this.mLayoutId = new ArrayList<>(initialCapacity);
    }

    public MultiTypePool(
        @NonNull List<Class<?>> classes,
        @NonNull List<ItemViewBinder<?, ?>> binders,
        @NonNull List<Integer> layoutId) {
        this.mClasses = classes;
        this.mBinders = binders;
        this.mLayoutId = layoutId;
    }

    @Override
    public <T> void register(
        @NonNull Class<? extends T> clazz,
        @NonNull ItemViewBinder<T, ?> binder,
        @NonNull int layoutId) {
        mClasses.add(clazz);
        mBinders.add(binder);
        mLayoutId.add(layoutId);
    }

    @Override
    public boolean unregister(@NonNull Class<?> clazz) {
        boolean removed = false;
        while (true) {
            int index = mClasses.indexOf(clazz);
            if (index != -1) {
                mClasses.remove(index);
                mBinders.remove(index);
                removed = true;
            } else {
                break;
            }
        }
        return removed;
    }

    @Override
    public int size() {
        return mClasses.size();
    }

    @Override
    public int firstIndexOf(@NonNull final Class<?> clazz) {
        int index = mClasses.indexOf(clazz);
        if (index != -1) {
            return index;
        }
        for (int i = 0; i < mClasses.size(); i++) {
            if (mClasses.get(i).isAssignableFrom(clazz)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public @NonNull
    Class<?> getClass(int index) {
        return mClasses.get(index);
    }

    @Override
    public @NonNull
    ItemViewBinder<?, ?> getItemViewBinder(int index) {
        return mBinders.get(index);
    }

    @NonNull
    @Override
    public int getLayoutId(int index) {
        return mLayoutId.get(index);
    }
}
