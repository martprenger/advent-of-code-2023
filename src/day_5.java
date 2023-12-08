import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class day_5 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // i just added multi threading and finished it that way was too lazy to think of a smarter way
        String[] lines = ReadFile.getFile("day_5_star_1.txt");
        ArrayList<ArrayList<String>> input = new ArrayList<>();
        ArrayList<String> subArray = new ArrayList<>();

        for (String str : lines) {
            if (str.isEmpty()) {
                input.add(new ArrayList<>(subArray));
                subArray.clear();
            } else {
                subArray.add(str);
            }
        }
        // Add the remaining elements14 if any
        if (!subArray.isEmpty()) {
            input.add(new ArrayList<>(subArray));
        }

        ArrayList<ArrayList<ArrayList<Long>>> maps = new ArrayList<>();

        for (int i = 1; i < input.size(); i++) {

            ArrayList<String> tempMap = input.get(i);
            ArrayList<ArrayList<Long>> map = new ArrayList<>();

            for (int m = 1; m < tempMap.size(); m++) {
                String catagories = tempMap.get(m);

                map.add((ArrayList<Long>) Arrays.stream(
                                catagories.split(" "))
                        .map(Long::parseLong).collect(Collectors.toList()));
            }
            maps.add(map);
        }

        String[] inputs = input.get(0).get(0).split(": ")[1].split(" ");

        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        long lowest = Long.MAX_VALUE;
        Future<Long>[] futures = new Future[inputs.length / 2];

        for (int s = 0; s + 1 < inputs.length; s += 2) {
            long seedSource = Long.parseLong(inputs[s]);
            long seedRange = Long.parseLong(inputs[s + 1]);

            futures[s / 2] = executor.submit(() -> positionRange(seedSource, seedRange, maps));
        }

        for (Future<Long> future : futures) {
            Long temp = future.get();
            if (temp < lowest) lowest = temp;
        }

        executor.shutdown();

        System.out.println(lowest);
    }

    public static Long positionRange(Long seedStart, Long seedRange, ArrayList<ArrayList<ArrayList<Long>>> maps) {
        System.out.println("start range" + seedRange);
        long lowest = Long.MAX_VALUE;

        for (int r = 0; r < seedRange; r++) {
            Long seed = seedStart + r;
            for (ArrayList<ArrayList<Long>> map : maps) {
                for (ArrayList<Long> line : map) {
                    Long source = line.get(1);
                    Long range = line.get(2);
                    Long destination = line.get(0);

                    if ((seed >= source) && (seed < source + range)) {
                        seed = destination + (seed - source);
                        break;
                    }
                }
            }
            if (seed < lowest) lowest = seed;
        }
        System.out.println("range done: " + seedRange);
        return lowest;
    }
}
