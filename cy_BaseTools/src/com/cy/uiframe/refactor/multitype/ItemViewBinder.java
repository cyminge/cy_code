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

import com.cy.uiframe.refactor.recyclerview.GioneeRecyclerAdapter;
import com.cy.uiframe.refactor.recyclerview.GioneeRecyclerViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/***
 * @author drakeet
 */
public abstract class ItemViewBinder<T, VH extends GioneeRecyclerViewHolder> {

    public GioneeRecyclerAdapter adapter;


    public abstract @NonNull VH onCreateViewHolder(@NonNull View view);

    public abstract void onBindViewHolder(@NonNull VH holder, @NonNull T item);

    protected final int getPosition(@NonNull final ViewHolder holder) {
        return holder.getAdapterPosition();
    }

    protected final @NonNull GioneeRecyclerAdapter getAdapter() {
        if (adapter == null) {
            throw new IllegalStateException("ItemViewBinder " + this + " not attached to MultiTypeAdapter. " +
                "You should not call the method before registering the binder.");
        }
        return adapter;
    }

    public long getItemId(@NonNull T item) {
        return RecyclerView.NO_ID;
    }

    public void onViewRecycled(@NonNull VH holder) {}


    public boolean onFailedToRecycleView(@NonNull VH holder) {
        return false;
    }

    public void onViewAttachedToWindow(@NonNull VH holder) {}

    public void onViewDetachedFromWindow(@NonNull VH holder) {}
}
