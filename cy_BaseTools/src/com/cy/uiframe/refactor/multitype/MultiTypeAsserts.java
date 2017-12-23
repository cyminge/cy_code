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

import java.util.List;

import com.cy.uiframe.refactor.recyclerview.GioneeRecyclerAdapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

/**
 * @author drakeet
 */
public final class MultiTypeAsserts {

    private MultiTypeAsserts() {
        throw new AssertionError();
    }

    @SuppressWarnings("unchecked")
    public static void assertAllRegistered(@NonNull GioneeRecyclerAdapter adapter, @NonNull List<?> items)
        throws BinderNotFoundException, IllegalArgumentException, IllegalAccessError {

        if (items.isEmpty()) {
            throw new IllegalArgumentException("Your Items/List is empty.");
        }
        for (int i = 0; i < items.size(); i++) {
            adapter.indexInTypesOf(i, items.get(0));
        }
        /* All passed. */
    }

    public static void assertHasTheSameAdapter(@NonNull RecyclerView recyclerView, @NonNull GioneeRecyclerAdapter adapter)
        throws IllegalArgumentException, IllegalAccessError {
        if (recyclerView.getAdapter() == null) {
            throw new IllegalAccessError("The assertHasTheSameAdapter() method must " +
                "be placed after recyclerView.setAdapter()");
        }
        if (recyclerView.getAdapter() != adapter) {
            throw new IllegalArgumentException(
                "Your recyclerView's adapter is not the sample with the argument adapter.");
        }
    }
}
