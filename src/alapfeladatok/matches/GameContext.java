package alapfeladatok.matches;

import java.util.StringJoiner;

public class GameContext {

    private int numberOfMatches;

    private int previousPick;

    private int round;

    public GameContext(int numberOfMatches) {
        this.numberOfMatches = numberOfMatches;
    }

    public int getNumberOfMatches() {
        return numberOfMatches;
    }

    public int getPreviousPick() {
        return previousPick;
    }

    public int getMaxPick() {
        return Math.min(3, numberOfMatches);
    }

    public int getRound() {
        return round;
    }

    public String getEcho(String name) {
        String possiblePicks = calculatePossiblePicks(getMaxPick());
        return String.format("%s: mennyit szeretnél elvenni? %s ", name, possiblePicks);
    }

    public boolean isNextRoundPossible() {
        return numberOfMatches > 0;
    }

    public void pick(int pick) {
        previousPick = pick;
        numberOfMatches -= pick;
        round++;
    }

    private String calculatePossiblePicks(int maxPick) {
        StringJoiner joiner = new StringJoiner(",", "[", "]");
        for (int i = 1; i <= maxPick; i++) {
            joiner.add(Integer.toString(i));
        }
        return joiner.toString();
    }
}
