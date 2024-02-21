/*
 * Copyright (C) 2024, Johan Hutting
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package io.github.jhutting.examples.record.immutablelist;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MutableRecordTest {

    @Test
    @DisplayName("Mutable list supplied")
    void mutableList() {
        final List<String> reviews = new ArrayList<>();
        reviews.add("First review.");
        MutableRecord mutableRecord = new MutableRecord("The case of the forgotten reviews", reviews);

        assertThat(mutableRecord.reviews()).hasSize(1);

        // despite the final keyword and being part of a record, the list inside the record is fully mutable
        mutableRecord.reviews().add("Let me add a late review.");

        assertThat(mutableRecord.reviews().getLast()).isEqualTo("Let me add a late review.");
    }

    @Test
    @DisplayName("Collections.immutableList")
    void unmodifiableList() {
        final List<String> reviews = new ArrayList<>();
        reviews.add("First review.");
        MutableRecord mutableRecord = new MutableRecord("The case of the forgotten reviews", Collections.unmodifiableList(reviews));

        assertThat(mutableRecord.reviews()).hasSize(1);

        // adding reviews to the record list will throw an exception, it is unmodifiable after all
        assertThatThrownBy(() -> mutableRecord.reviews().add("throw exception"))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasStackTraceContaining("UnmodifiableCollection");

        // but we can still simply add a review to the original List, the unmodifiableList is a pointer to this list resulting in an extra item
        reviews.add("Let me add a late review.");

        assertThat(mutableRecord.reviews().getLast()).isEqualTo("Let me add a late review.");
    }

    @Test
    @DisplayName("List.copyOf is truly immutable")
    void copyOf() {
        final List<String> reviews = new ArrayList<>();
        reviews.add("First review.");
        MutableRecord immutableRecord = new MutableRecord("The case of the forgotten reviews", List.copyOf(reviews));

        assertThat(immutableRecord.reviews()).hasSize(1);

        // adding reviews to the record list will throw an exception, it is immutable after all
        assertThatThrownBy(() -> immutableRecord.reviews().add("throw exception"))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasStackTraceContaining("ImmutableCollections");

        // but we can still simply add a review to the original List in the builder, yet it is no longer reflected in the record
        reviews.add("This is only mutating the original list. Should have reviewed on time.");

        assertThat(reviews).hasSize(2);
        assertThat(immutableRecord.reviews()).hasSize(1);
        assertThat(immutableRecord.reviews().getLast()).isEqualTo("First review.");
    }

    @Test
    @DisplayName("Builder with mutable list")
    void builderMutableList() {
        MutableRecord.MutableRecordBuilder builder = new MutableRecord.MutableRecordBuilder("The case of the forgotten reviews");
        final List<String> reviews = new ArrayList<>();
        reviews.add("First review.");

        MutableRecord mutableRecord = builder.reviews(reviews).build();

        assertThat(mutableRecord.reviews()).hasSize(1);

        // despite the final keyword and being part of a record, the list inside the record is fully mutable
        mutableRecord.reviews().add("Let me add a late review.");

        assertThat(mutableRecord.reviews()).hasSize(2);
        assertThat(mutableRecord.reviews().getLast()).isEqualTo("Let me add a late review.");
    }

    @Test
    @DisplayName("Builder with Collections.immutableList")
    void builderUnmodifiableList() {
        MutableRecord.MutableRecordBuilder builder = new MutableRecord.MutableRecordBuilder("The case of the forgotten reviews");
        final List<String> reviews = new ArrayList<>();
        reviews.add("First review.");

        MutableRecord mutableRecord = builder.reviews(reviews).buildWithUnmodifiableList();

        assertThat(mutableRecord.reviews()).hasSize(1);

        // adding reviews to the record list will throw an exception, it is unmodifiable after all
        assertThatThrownBy(() -> mutableRecord.reviews().add("throw exception"))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasStackTraceContaining("UnmodifiableCollection");

        // but we can still simply add a review to the original List, the unmodifiableList is a pointer to this list resulting in an extra item
        builder.addReview("Let me add a late review.");

        assertThat(mutableRecord.reviews()).hasSize(2);
        assertThat(mutableRecord.reviews().getLast()).isEqualTo("Let me add a late review.");
    }

    @Test
    @DisplayName("Builder with List.copyOf is truly immutable")
    void builderCopyOf() {
        MutableRecord.MutableRecordBuilder builder = new MutableRecord.MutableRecordBuilder("The case of the forgotten reviews");
        builder.addReview("First review.");
        MutableRecord immutableRecord = builder.buildWithCopyOf();

        assertThat(immutableRecord.reviews()).hasSize(1);

        // adding reviews to the record list will throw an exception, it is immutable after all
        assertThatThrownBy(() -> immutableRecord.reviews().add("throw exception"))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasStackTraceContaining("ImmutableCollections");

        // but we can still simply add a review to the original List in the builder, yet it is no longer reflected in the record
        builder.addReview("This is only mutating the original list. Should have reviewed on time.");

        assertThat(immutableRecord.reviews()).hasSize(1);
        assertThat(immutableRecord.reviews().getLast()).isEqualTo("First review.");
    }

    @Test
    @DisplayName("Builder with List.copyOf on an empty ArrayList also becomes immutable")
    void builderCopyOfEmpty() {
        MutableRecord.MutableRecordBuilder builder = new MutableRecord.MutableRecordBuilder("The case of the forgotten reviews");
        MutableRecord immutableRecord = builder.buildWithCopyOf();

        assertThat(immutableRecord.reviews()).isEmpty();

        // adding reviews to the record list will throw an exception, it is immutable after all
        assertThatThrownBy(() -> immutableRecord.reviews().add("throw exception"))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasStackTraceContaining("ImmutableCollections");

        // but we can still simply add a review to the original List in the builder, yet it is no longer reflected in the record
        builder.addReview("This is only mutating the original list. Should have reviewed on time.");

        assertThat(immutableRecord.reviews()).isEmpty();
    }
}
