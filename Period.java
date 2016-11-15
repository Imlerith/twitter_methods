package twitterset;

import java.time.Instant;

/**
 * Immutable datatype representing an interval starting from one datetime and
 * ending at a later datetime. The interval includes the endpoints.
 */
public class Period {

    private final Instant start;
    private final Instant end;
    /* Rep invariant: start <= end. */
    
    /**
     * Constructor of Period.
     * 
     * @param start
     *            starting date/time
     * @param end
     *            ending date/time. Requires end >= start.
     */
    public Period(Instant start, Instant end) {
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("requires start <= end");
        }
        this.start = start;
        this.end = end;
    }

    /**
     * @return the startpoint of the interval
     */
    public Instant getStart() {
        return start;
    }

    /**
     * @return the endpoint of the interval
     */
    public Instant getEnd() {
        return end;
    }


    @Override public String toString() {
        return "[" + this.getStart()
                + "..." + this.getEnd()
                + "]";
    }


    @Override public boolean equals(Object thatObject) {
        if (!(thatObject instanceof Period)) {
            return false;
        }

        Period that = (Period) thatObject;
        return this.start.equals(that.start) 
                && this.end.equals(that.end);
    }


    @Override public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + start.hashCode();
        result = prime * result + end.hashCode();
        return result;
    }

}
