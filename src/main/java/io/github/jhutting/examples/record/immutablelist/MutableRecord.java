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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public record MutableRecord(String title, List<String> reviews) {
    public static final class MutableRecordBuilder {
        private final String title;

        private List<String> reviews = new ArrayList<>();

        public MutableRecordBuilder(String title) {
            if (title == null || title.isBlank()) {
                throw new UnsupportedOperationException("Title needs to be present and not blank");
            }
            this.title = title;
        }

        public MutableRecordBuilder reviews(List<String> reviews) {
            this.reviews = reviews;
            return this;
        }

        public MutableRecordBuilder addReview(String review) {
            this.reviews.add(review);
            return this;
        }

        public MutableRecord build() {
            // you could run some extra validations here as well, not just in the constructor.
            return new MutableRecord(title, reviews);
        }

        public MutableRecord buildWithUnmodifiableList() {
            return new MutableRecord(title, Collections.unmodifiableList(reviews));
        }

        public MutableRecord buildWithCopyOf() {
            return new MutableRecord(title, List.copyOf(reviews));
        }
    }
}
