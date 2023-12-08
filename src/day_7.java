import javax.swing.text.PlainDocument;
import java.util.ArrayList;
import java.util.Collections;

public class day_7 {
    enum Level {
        Fiveofakind,
        Fourofakind,
        Fullhouse,
        Threeofakind,
        Twopair,
        Onepair,
        Highcard;

        public boolean isHigherThan(Level otherLevel) {
            return this.ordinal() > otherLevel.ordinal();
        }
    }

    public static void main(String[] args) {
        String[] lines = ReadFile.getFile("day_7.txt");

        ArrayList<Player> players = new ArrayList<>();

        for (String line : lines) {
            Player test = new Player(line);
            players.add(test);
        }
        Collections.sort(players);
        Collections.reverse(players);

        int rt = 0;
        for (int i = 1; i <= players.size(); i++) {
            rt += i * players.get(i-1).bet;
            System.out.println(players.get(i-1));
        }
        System.out.println(rt);


    }

    public static class Player implements Comparable<Player> {
        char[] cards = new char[] {'A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', 'J'};
        public int jokers = 0;
        public String hand;
        public int bet;
        public Level level;

        public Player(String bet) {
            String[] temp = bet.split(" ");
            this.hand = temp[0];
            this.bet = Integer.parseInt(temp[1]);
            this.level = getLevel();
        }

        private Level getLevel() {
            for (char j : hand.toCharArray()) {
                if (j == 'J') jokers++;
            }

            ArrayList<Integer> values = new ArrayList<>();
            for (char caracter : cards){
                if (caracter == 'J') break;
                int cardCount = getCardCount(caracter);
                values.add(cardCount);
            }

            int maxJokerValue = 5 - jokers;

            if (values.contains(maxJokerValue)) {
                return Level.Fiveofakind;
            } else if (values.contains(maxJokerValue - 1)) {
                return Level.Fourofakind;
            } else if (values.contains(maxJokerValue - 2)) {
                values.remove(values.indexOf(maxJokerValue - 2));
                if (values.contains(2)) {
                    return Level.Fullhouse;
                } else {
                    return Level.Threeofakind;
                }
            } else if (values.contains(maxJokerValue - 3)) {
                values.remove(values.indexOf(maxJokerValue - 3));
                if (values.contains(2)) {
                    return Level.Twopair;
                } else {
                    return Level.Onepair;
                }
            }

            return Level.Highcard;
        }

        private int getCardCount(char card) {
            return (int) hand.chars().filter(ch -> ch == card).count();
        }

        @Override
        public String toString() {
            return  "hand: " + hand +
                    ", jokers: " + jokers +
                    ", level: " + level +
                    ", bet: " + bet + '\'';
        }

        private int getIndex(char card) {
            for (int i = 0; i < cards.length; i++) {
                if (cards[i] == card) {
                    return i;
                }
            }
            return -1; // Return some default value for cards not found in 'cards'
        }

        private boolean isHigherHand(String hand) {
            char[] myCards = this.hand.toCharArray();
            char[] hisCards = hand.toCharArray();

            for (int i = 0; i < Math.min(myCards.length, hisCards.length); i++) {
                int myIndex = getIndex(myCards[i]);
                int hisIndex = getIndex(hisCards[i]);

                if (myIndex > hisIndex) {
                    return true; // My card has a higher index
                } else if (myIndex < hisIndex) {
                    return false; // His card has a higher index
                }
            }

            return false;
        }

        private boolean isLowerHand(String hand) {
            char[] hisCards = this.hand.toCharArray();
            char[] myCards = hand.toCharArray();

            for (int i = 0; i < Math.min(myCards.length, hisCards.length); i++) {
                int myIndex = getIndex(myCards[i]);
                int hisIndex = getIndex(hisCards[i]);

                if (myIndex > hisIndex) {
                    return true; // My card has a higher index
                } else if (myIndex < hisIndex) {
                    return false; // His card has a higher index
                }
            }

            return false;
        }

        @Override
        public int compareTo(Player other) {
            if (this.level.isHigherThan(other.level)) {
                return 1;
            } else if (other.level.isHigherThan(this.level)) {
                return -1;
            } else {
                if (isHigherHand(other.hand)) {
                    return 1;
                } else if (isLowerHand(other.hand)){
                    return -1;
                } else {
                    return 0;
                }
            }
        }
    }
}
