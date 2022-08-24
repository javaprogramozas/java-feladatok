package alapfeladatok;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class A20 {

    public static void main(String[] args) {
        List<Interval> intervals = new ArrayList<>();
        try (Scanner input = new Scanner(System.in)) {
            while (input.hasNextInt()) {
                int start = input.nextInt();
                int end = input.nextInt();
                Interval interval = new Interval(start, end);
                intervals.add(interval);
            }
        }

        System.out.println(intervals);

        System.out.println("Iteratív: " + iterativeSolution(intervals));
        System.out.println("Vermes: " + stackedSolution(intervals));
    }

    private static List<Interval> iterativeSolution(List<Interval> intervals) {
        List<Interval> results = new ArrayList<>(intervals);
        Collections.sort(results);
        int index = 0;
        while (index < results.size() - 1) {
            Interval first = results.get(index);
            Interval second = results.get(index + 1);
            if (first.overlapsWith(second)) {
                results.remove(index);
                results.remove(index);
                Interval merged = first.merge(second);
                results.add(index, merged);
            } else {
                index++;
            }
        }
        return results;
    }

    private static List<Interval> stackedSolution(List<Interval> intervals) {
        Deque<Interval> stack = new LinkedList<>(intervals);
        ArrayList<Interval> results = new ArrayList<>();

        while (stack.size() > 1) {
            Interval first = stack.pop();
            Interval second = stack.pop();
            if (first.overlapsWith(second)) {
                Interval merged = first.merge(second);
                stack.push(merged);
            } else {
                results.add(first);
                stack.push(second);
            }
        }
        results.add(stack.pop());
        return results;
    }

    private record Interval(int start, int end) implements Comparable<Interval> {

        @Override
        public String toString() {
            return String.format("[%d,%d]", start, end);
        }

        @Override
        public int compareTo(Interval other) {
            return Comparator.comparingInt(Interval::start)
                    .thenComparing(Interval::end)
                    .compare(this, other);
        }

        public boolean overlapsWith(Interval other) {
            return (this.start <= other.start && this.end >= other.start)
                    || (this.start > other.start && other.end >= this.start);
        }

        public Interval merge(Interval other) {
            if (!overlapsWith(other)) {
                throw new IllegalArgumentException("Other interval must overlap with this!");
            }
            return new Interval(Math.min(this.start, other.start), Math.max(this.end, other.end));
        }
    }
}
