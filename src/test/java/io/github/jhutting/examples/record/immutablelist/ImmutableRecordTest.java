package io.github.jhutting.examples.record.immutablelist;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ImmutableRecordTest {
    @Test
    @DisplayName("Mutable list supplied")
    void mutableList() {
        final List<String> reviews = new ArrayList<>();
        reviews.add("First review.");
        ImmutableRecord immutableRecord = new ImmutableRecord("The case of the forgotten reviews", reviews);

        assertThat(immutableRecord.reviews()).hasSize(1);

        assertThatThrownBy(() -> immutableRecord.reviews().add("Let me add a late review."))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasStackTraceContaining("ImmutableCollections");

        // but we can still simply add a review to the original List, yet it is not reflected inside the record
        reviews.add("This is only mutating the original list. Should have reviewed on time.");

        assertThat(reviews).hasSize(2);
        assertThat(immutableRecord.reviews()).hasSize(1);
        assertThat(immutableRecord.reviews().getLast()).isEqualTo("First review.");
    }

    @Test
    @DisplayName("Collections.unmodifiableList")
    void unmodifiableList() {
        final List<String> reviews = new ArrayList<>();
        reviews.add("First review.");
        List<String> unmodifiableList = Collections.unmodifiableList(reviews);
        ImmutableRecord immutableRecord = new ImmutableRecord("The case of the forgotten reviews", unmodifiableList);

        // You can add items to the original, and those are reflected in the Collections.unmodifiableList, but not in the record due to .copyOf in the constructor!
        reviews.add("Let me add a late review.");

        assertThat(unmodifiableList).hasSize(2);
        assertThat(unmodifiableList.getLast()).isEqualTo("Let me add a late review.");

        assertThat(immutableRecord.reviews()).hasSize(1);
        assertThat(immutableRecord.reviews().getLast()).isEqualTo("First review.");
    }

    @Test
    @DisplayName("Builder with mutable list")
    void builderMutableList() {
        ImmutableRecord.ImmutableRecordBuilder builder = new ImmutableRecord.ImmutableRecordBuilder("The case of the forgotten reviews");
        final List<String> reviews = new ArrayList<>();
        reviews.add("First review.");

        ImmutableRecord immutableRecord = builder.reviews(reviews).build();

        assertThat(immutableRecord.reviews()).hasSize(1);

        assertThatThrownBy(() -> immutableRecord.reviews().add("Let me add a late review."))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasStackTraceContaining("ImmutableCollections");

        // adding to the original list is also no longer reflected as `reviews` in the record is an immutable copy
        reviews.add("Let me add a late review.");
        assertThat(reviews).hasSize(2);
        assertThat(immutableRecord.reviews()).hasSize(1);
    }

    @Test
    @DisplayName("null as list supplied")
    void nulledList() {
        ImmutableRecord immutableRecord = new ImmutableRecord("The case of the forgotten reviews", null);

        assertThat(immutableRecord.reviews()).isEmpty();
    }
}
