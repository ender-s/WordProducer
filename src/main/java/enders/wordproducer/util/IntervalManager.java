package enders.wordproducer.util;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IntervalManager
{

    private List<Interval> intervals;
    private boolean merged;

    private static final class Interval implements Comparable<Interval>
    {
        private BigInteger lowerBound;
        private BigInteger upperBound;

        private Interval(BigInteger lowerBound, BigInteger upperBound)
        {
            if (upperBound.compareTo(lowerBound) < 0)
            {
                throw new RuntimeException(
                        String.format("Invalid interval!: [%d, %d]: upper bound must be greater than or" +
                                "equal to lower bound", lowerBound, upperBound));
            }
            this.lowerBound = lowerBound;
            this.upperBound = upperBound;
        }

        private void expand(Interval interval)
        {
            BigInteger lowerBound = interval.lowerBound;
            BigInteger upperBound = interval.upperBound;
            if (!lowerBound.equals(this.upperBound.add(BigInteger.ONE)))
            {
                throw new RuntimeException(
                        String.format("Invalid expansion interval!: [%d, %d]: lower bound must be " +
                                        "current upper bound (%d) + 1",
                                lowerBound, upperBound, this.upperBound));
            }

            if (upperBound.compareTo(lowerBound) < 0)
            {
                throw new RuntimeException(
                        String.format("Invalid expansion interval!: [%d, %d]: upper bound must be greater than or" +
                                "equal to lower bound", lowerBound, upperBound));
            }

            this.upperBound = upperBound;
        }

        private BigInteger getCount()
        {
            return upperBound.subtract(lowerBound).add(BigInteger.ONE);
        }

        @Override
        public int compareTo(Interval interval)
        {
            if (lowerBound.compareTo(interval.lowerBound) > 0) return 1;
            if (lowerBound.compareTo(interval.lowerBound) < 0) return -1;
            if (upperBound.compareTo(interval.upperBound) > 0) return 1;
            if (upperBound.compareTo(interval.upperBound) < 0) return -1;
            return 0;
        }
    }

    public IntervalManager()
    {
        intervals = new ArrayList<>();
    }

    public void addInterval(BigInteger lowerBound, BigInteger upperBound)
    {
        merged = false;
        intervals.add(new Interval(lowerBound, upperBound));
    }

    public void mergeIntervals()
    {
        Collections.sort(intervals);
        Interval expandingInterval = intervals.get(0);
        for (int i = 1; i < intervals.size(); i++)
        {
            expandingInterval.expand(intervals.get(i));
        }
        intervals = new ArrayList<>();
        intervals.add(expandingInterval);
        merged = true;
    }

    public BigInteger getCount()
    {
        if (!merged)
        {
            mergeIntervals();
        }
        return intervals.get(0).getCount();
    }

}
