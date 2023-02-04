package collections.framework;

import java.io.Serializable;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

public class Range<C extends Comparable> implements Serializable {

    private final C lowerBound;
    private final C upperBound;
    private final boolean lowerBoundInclusive;
    private final boolean upperBoundInclusive;

    private Range(C lowerBound, C upperBound, boolean lowerBoundInclusive, boolean upperBoundInclusive) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.lowerBoundInclusive = lowerBoundInclusive;
        this.upperBoundInclusive = upperBoundInclusive;
        if (lowerBound != null && upperBound != null) {
            if (lowerBound.compareTo(upperBound) > 0)
                throw new IllegalArgumentException("Invalid Range: [(" + lowerBound + ") > (" + upperBound + ")]");

            if (!lowerBoundInclusive && !upperBoundInclusive && Objects.equals(lowerBound, upperBound))
                throw new IllegalArgumentException("Invalid Range: (" + lowerBound + ", " + upperBound + ")");
        }
    }

    public boolean contains(C value) {

        requireNonNull(value);

        if (lowerBound != null) {
            if (lowerBoundInclusive && value.compareTo(lowerBound) < 0) {
                return false;
            }

            if (!lowerBoundInclusive && value.compareTo(lowerBound) <= 0) {
                return false;
            }
        }
        if (upperBound != null) {
            if (upperBoundInclusive && value.compareTo(upperBound) > 0) {
                return false;
            }

            if (!upperBoundInclusive && value.compareTo(upperBound) >= 0) {
                return false;
            }
        }

        return true;
    }

    public boolean encloses(Range<C> other) {

        requireNonNull(other);

        if (upperBound == other.upperBound
                && lowerBound == other.lowerBound
                && upperBoundInclusive == other.upperBoundInclusive
                && lowerBoundInclusive == other.lowerBoundInclusive) {
            return true;
        }

        if (upperBound == null && lowerBound == null) {
            return true;
        }

        if (other.upperBound == null) {
            if (other.lowerBound == null || upperBound != null) {
                return false;
            }
            return this.contains(other.lowerBound);
        } else if (other.lowerBound == null) {
            if (lowerBound != null) {
                return false;
            }

            return this.contains(other.upperBound);
        } else {
            if (upperBound == null) {
                return this.contains(other.lowerBound);
            }
            if (lowerBound == null) {
                return this.contains(other.upperBound);
            }
        }

        if (other.upperBoundInclusive &&
                this.contains(other.upperBound) &&
                other.lowerBoundInclusive &&
                this.contains(other.lowerBound))
            return true;

        if (!other.upperBoundInclusive &&
                this.upperBound.compareTo(other.upperBound) > 0 &&
                other.lowerBoundInclusive &&
                this.contains(other.lowerBound))
            return true;

        if (other.upperBoundInclusive &&
                this.contains(other.upperBound) &&
                !other.lowerBoundInclusive &&
                this.lowerBound.compareTo(other.lowerBound) < 0)
            return true;

        if (!other.upperBoundInclusive &&
                this.upperBound.compareTo(other.upperBound) >= 0 &&
                !other.lowerBoundInclusive &&
                this.lowerBound.compareTo(other.lowerBound) <= 0)
            return true;

        return false;
    }

    public Range<C> intersection(Range<C> other) {

        if (this.encloses(other)) {
            return other;
        }
        if (other.encloses(this)) {
            return this;
        }

        if (lowerBound == null) {
            if (this.contains(other.lowerBound)) {
                return new Range<>(other.lowerBound, upperBound, other.lowerBoundInclusive, upperBoundInclusive);
            }
            if (upperBound.compareTo(other.lowerBound) == 0 && lowerBoundInclusive && upperBoundInclusive) {
                return new Range<>(upperBound, upperBound, true, true);
            } else {
                return new Range<>(other.upperBound, other.upperBound, false, true);
            }
        }

        if (other.lowerBound == null) {
            if (other.contains(lowerBound)) {
                return new Range<>(lowerBound, other.upperBound, lowerBoundInclusive, other.upperBoundInclusive);
            }
            if (other.upperBound.compareTo(lowerBound) == 0 && lowerBoundInclusive && upperBoundInclusive) {
                return new Range<>(lowerBound, lowerBound, true, true);
            } else {
                return new Range<>(other.upperBound, other.upperBound, false, true);
            }
        }

        if (upperBound == null) {
            if (this.contains(other.upperBound)) {
                return new Range<>(lowerBound, other.upperBound, lowerBoundInclusive, other.upperBoundInclusive);
            }
            if (lowerBound.compareTo(other.upperBound) == 0 && lowerBoundInclusive && upperBoundInclusive) {
                return new Range<>(lowerBound, lowerBound, true, true);
            } else {
                return new Range<>(other.upperBound, other.upperBound, false, true);
            }
        }
        if (other.upperBound == null) {
            if (other.contains(upperBound)) {
                return new Range<>(other.lowerBound, upperBound, other.lowerBoundInclusive, upperBoundInclusive);
            }
            if (other.lowerBound.compareTo(upperBound) == 0 && lowerBoundInclusive && upperBoundInclusive) {
                return new Range<>(other.lowerBound, other.lowerBound, true, true);
            } else {
                return new Range<>(other.lowerBound, other.lowerBound, false, true);
            }
        }

        if (other.contains(this.lowerBound)) {
            return intersection(this, other);
        } else if (other.contains(this.upperBound)) {
            return intersection(other, this);
        } else {
            return new Range<>(other.upperBound, other.upperBound, false, true);
        }
    }

    private Range<C> intersection(Range<C> a, Range<C> b) {
        return new Range<>(a.lowerBound, b.upperBound, a.lowerBoundInclusive, b.upperBoundInclusive);
    }

    public Range<C> span(Range<C> other) {
        if (this.encloses(other) || other.isEmpty()) {
            return this;
        }
        if (other.encloses(this) || this.isEmpty()) {
            return other;
        }
        if (lowerBound == null) {
            return span(other, this);
        }
        if (other.lowerBound == null) {
            return span(this, other);
        }

        if (upperBound == null) {
            return span(this, other);
        }
        if (other.upperBound == null) {
            return span(other, this);
        }

        if (other.contains(this.lowerBound)) {
            return span(this, other);
        } else if (other.contains(this.upperBound)) {
            return span(other, this);
        } else if (other.lowerBound.compareTo(this.upperBound) >= 0) {
            return span(other, this);
        } else {
            return span(this, other);
        }
    }

    private Range<C> span(Range<C> a, Range<C> b) {
        return new Range<>(b.lowerBound, a.upperBound, b.lowerBoundInclusive, a.upperBoundInclusive);
    }

    public boolean isEmpty() {
        return lowerBoundInclusive != upperBoundInclusive &&
                (lowerBound != null && lowerBound.equals(upperBound) ||
                        (lowerBound == null && upperBound == null));
    }

    public static <C extends Comparable> Range<C> open(C a, C b) {
        return new Range<>(requireNonNull(a),
                requireNonNull(b),
                false,
                false);
    }

    public static <C extends Comparable> Range<C> closed(C a, C b) {
        return new Range<>(requireNonNull(a),
                requireNonNull(b),
                true,
                true);
    }

    public static <C extends Comparable> Range<C> openClosed(C a, C b) {
        return new Range<>(requireNonNull(a),
                requireNonNull(b),
                false,
                true);
    }

    public static <C extends Comparable> Range<C> closedOpen(C a, C b) {
        return new Range<>(requireNonNull(a),
                requireNonNull(b),
                true,
                false);
    }

    public static <C extends Comparable> Range<C> greaterThan(C a) {
        return new Range<>(requireNonNull(a),
                null,
                false,
                false);
    }

    public static <C extends Comparable> Range<C> atLeast(C a) {
        return new Range<>(requireNonNull(a),
                null,
                true,
                false);
    }

    public static <C extends Comparable> Range<C> lessThan(C a) {
        return new Range<>(null,
                requireNonNull(a),
                false,
                false);
    }

    public static <C extends Comparable> Range<C> atMost(C a) {
        return new Range(null,
                requireNonNull(a),
                false,
                true);
    }

    public static <C extends Comparable> Range<C> all() {
        return new Range(null,
                null,
                false,
                false);
    }

    @Override
    public String toString() {

        char l = lowerBoundInclusive ? '[' : '(';
        char u = upperBoundInclusive ? ']' : ')';

        String lValue = lowerBound == null ? "-INF" : lowerBound.toString();
        String uValue = upperBound == null ? "INF" : upperBound.toString();

        if (isEmpty()) {
            return "EMPTY";
        }
        return l + lValue +
                ", " + uValue + u;
    }
}

