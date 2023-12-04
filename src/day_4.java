import java.util.*;
import java.util.stream.Collectors;

public class day_4 {
    public static void main(String[] args) {
        String[] lines = ReadFile.getFile("day_4_star_1.txt");
        List<Card> cards = new ArrayList<>();

        for (String card : lines) {
            cards.add(new Card(card));
        }

        int rt = 0;

        for (int i = 0; i < cards.size(); i++) {
            Card card = cards.get(i);
            for (int j = 0; j < card.hits;j++) {
                cards.add(cards.get(card.index+j));
            }
            rt++;
        }
        System.out.println(rt);
    }

    static class Card {
        int index;
        int hits;
        public Card(String card) {
            int i1 = card.indexOf(':');
            int i2 = card.indexOf('|');

            index = Integer.parseInt(card.substring(5, i1).trim());
            List<Integer> left = Arrays.stream(card.substring(i1 + 1, i2).split(" ")).filter(t -> !"".equals(t))
                    .map(Integer::parseInt).collect(Collectors.toList());
            List<Integer> right = Arrays.stream(card.substring(i2 + 1).split(" ")).filter(t -> !"".equals(t))
                    .map(Integer::parseInt).collect(Collectors.toList());
            Set<Integer> hitsSet = new HashSet<>(left);
            hitsSet.retainAll(right);
            hits = hitsSet.size();
        }

    }

}

