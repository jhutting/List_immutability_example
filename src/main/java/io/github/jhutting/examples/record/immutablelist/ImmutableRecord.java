package io.github.jhutting.examples.record.immutablelist;

import java.util.ArrayList;
import java.util.List;

public record ImmutableRecord(String title, List<String> reviews) {
    public ImmutableRecord(String title, List<String> reviews) {
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        this.title = title;
        this.reviews = reviews == null ? List.of() : List.copyOf(reviews);
    }

    public static final class ImmutableRecordBuilder {
        private final String title;

        private List<String> reviews = new ArrayList<>();

        public ImmutableRecordBuilder(String title) {
            if (title == null || title.isBlank()) {
                throw new UnsupportedOperationException("Title needs to be present and not blank");
            }
            this.title = title;
        }

        public ImmutableRecordBuilder reviews(List<String> reviews) {
            this.reviews = reviews;
            return this;
        }

        public ImmutableRecordBuilder addReview(String review) {
            this.reviews.add(review);
            return this;
        }

        public ImmutableRecord build() {
            return new ImmutableRecord(title, reviews);
        }
    }
}
