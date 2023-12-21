import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

public class day_20 {
    public static long low = 0;
    public static long high = 0;


    public static void main(String[] args) {
        String[] input = ReadFile.getFile("day_20.txt");

        Module broadcaster;


        Map<String, Module> modulesMap = new HashMap<>();
        ArrayList<String[]> connections = new ArrayList<>();

        Module output = new Module("output");
        modulesMap.put("output", output);

        for (String line : input) {
            String[] split = line.split(" -> ");
            if (split[0].equals("broadcaster")) {
                broadcaster = new Module("broadcaster");
                modulesMap.put("broadcaster", broadcaster);
                connections.add(new String[] {"broadcaster", split[1]});
            } else {
                String name = split[0].substring(1);
                modulesMap.put(name, makeModule(name, split[0].charAt(0)));
                connections.add(new String[]{name, split[1]});
            }
        }

        for (String[] connection : connections) {
            String[] connectionsOfModule = connection[1].split(", ");
            Module temp1 = modulesMap.get(connection[0]);
            for (String connectionOfModule : connectionsOfModule) {
                Module temp2 = modulesMap.get(connectionOfModule);
                if (temp2 == null) {
                    temp2 = new Module(connectionOfModule);
                    modulesMap.put(connectionOfModule, temp2);
                }
                temp1.connect(temp2);
                if (temp2.getType() == '&') {
                    temp2.addInput(temp1);
                }
            }
        }

        broadcaster = modulesMap.get("broadcaster");
        Module button = new Button("button", false);
        button.connect(broadcaster);
        Queue<Send> sends = new LinkedList<>();

        // part 2
        // find all the modules that are connect to the previous module of rx
        // go trough map
        Module[] whatch = new Module[0];
        for (Map.Entry<String, Module> entry : modulesMap.entrySet()) {
            String rx = entry.getKey();
            if (rx.equals("gf")) {
                whatch = entry.getValue().getInputs();
                break;
            }
        }

        HashMap<Module, Integer> count = new HashMap<>();
        // put all modules in hashmap
        for (Map.Entry<String, Module> entry : modulesMap.entrySet()) {
            count.put(entry.getValue(), 0);
        }
        HashMap<Module, Integer> previous = new HashMap<>();
        HashMap<Module, Integer> toLCM = new HashMap<>();



        int i = 0;
        while (true) {
            sends.addAll(button.sendPulse());

            while (!sends.isEmpty()) {
                Send send = sends.poll();
                if (send.getPulse().equals("low")) {
                    Module x = send.getReceiver();
                    if (previous.containsKey(x) && (count.get(x) == 2) && (Arrays.stream(whatch).anyMatch(x::equals))) {
                        toLCM.put(x, i - previous.get(x));
                    }
                    previous.put(x, i);
                    count.put(x, count.get(x) + 1);
                }
                if (toLCM.size()==whatch.length) {
                    System.out.println("lcm: " + findLCM(toLCM));
                    System.exit(0);
                }
                send.send();
                sends.addAll(send.getReceiver().sendPulse());
            }
            if (i == 1000) {
                System.out.println("low: " + low);
                System.out.println("high: " + high);
                System.out.println("total: " + (low * high));
            }
            i++;
        }
    }

    public static long lcm(long[] nums) {
        long lcm = 1;
        for (long num : nums) {
            lcm = (lcm * num) / gcd(lcm, num);
        }
        return lcm;
    }

    public static long gcd(long a, long b) {
        while (b > 0) {
            long temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    // Integrate this method into your main method or where needed
    public static BigInteger findLCM(HashMap<Module, Integer> toLCM) {
        BigInteger rt = BigInteger.valueOf(1);

        for (Map.Entry<Module, Integer> entry : toLCM.entrySet()) {
            BigInteger x = BigInteger.valueOf(entry.getValue());
            BigInteger gcd = rt.gcd(x);

            rt = (rt.multiply(x)).divide(gcd);
        }

        return rt;
    }
    public static Module makeModule(String name, char type) {
        if (type == '%') return new FlipFlop(name);
        if (type == '&') return new Conjunction(name);
        return new Module(name);
    }

    public static class Send {
        private Module receiver;
        private boolean pulse;
        private Module sender;

        public Send(Module receiver, boolean pulse, Module sender) {
            this.receiver = receiver;
            this.pulse = pulse;
            this.sender = sender;
        }

        public void send() {
            if (pulse) high++;
            else low++;
            receiver.revceivePulse(pulse, sender);
        }

        public Module getReceiver() {
            return receiver;
        }

        public Module getSender() {
            return sender;
        }

        public String getPulse() {
            return pulse ? "high" : "low";
        }
    }

    public static class Button extends Module {
        private boolean state;
        public Button(String name, boolean state) {
            super(name);
            this.state = state;
        }
        @Override
        public ArrayList<Send> sendPulse() {
            ArrayList<Send> sends = new ArrayList<>();
            for (Module module : super.connectedModules) {
                sends.add(new Send(module, state, this));
            }
            return sends;
        }
        @Override
        public void revceivePulse(boolean pulse, Module module) {
        }
        @Override
        public void addInput(Module module) {
        }
        @Override
        public boolean getState() {
            return state;
        }
        @Override
        public String getName() {
            return super.getName();
        }

        @Override
        public String toString() {
            StringBuilder connections = new StringBuilder();
            for (Module module : super.connectedModules) {
                connections.append(module.getName()).append(", ");
            }

            return "name: " + super.name + ", type: button, state: " + state + ", conections: " + connections.toString();
         }

        @Override
        public char getType() {
            return 'B';
        }
    }
    public static class Conjunction extends Module {

        HashMap<Module, Boolean> inputs = new HashMap<>();
        public Conjunction(String name) {
            super(name);
        }
        @Override
        public ArrayList<Send> sendPulse() {
            boolean pulse = false;
            for (Map.Entry<Module, Boolean> entry : inputs.entrySet()) {
                if (!entry.getValue()) {
                    pulse = true;
                    break;
                }
            }

            ArrayList<Send> sends = new ArrayList<>();
            for (Module module : super.connectedModules) {
                sends.add(new Send(module, pulse, this));
            }
            return sends;
        }
        @Override
        public Module[] getInputs() {
            return inputs.keySet().toArray(new Module[inputs.size()]);
        }
        @Override
        public void revceivePulse(boolean pulse, Module module) {
            inputs.put(module, pulse);
        }
        @Override
        public void addInput(Module module) {
            inputs.put(module, false);
        }
        @Override
        public boolean getState() {
            boolean state = true;
            for (Map.Entry<Module, Boolean> entry : inputs.entrySet()) {
                if (!entry.getValue()) {
                    state = false;
                    break;
                }
            }
            return state;
        }
        @Override
        public String getName() {
            return super.getName();
        }

        @Override
        public String toString() {
            StringBuilder connections = new StringBuilder();
            for (Module module : super.connectedModules) {
                connections.append(module.getName()).append(", ");
            }

            boolean state = true;
            for (Map.Entry<Module, Boolean> entry : inputs.entrySet()) {
                if (!entry.getValue()) {
                    state = false;
                    break;
                }
            }

            return "name: " + super.name + ", type: Conjunction, state: " + state + ", conections: " + connections.toString();
        }

        @Override
        public char getType() {
            return '&';
        }
    }

    public static class FlipFlop extends Module {
        public boolean state;
        public boolean active = false;
        public FlipFlop(String name) {
            super(name);
            this.state = false;
        }
        @Override
        public void revceivePulse(boolean pulse, Module module) {

            if (!pulse) {
                state = !state;
                active = true;
            }
            else active = false;
        }
        @Override
        public ArrayList<Send> sendPulse() {
            ArrayList<Send> sends = new ArrayList<>();
            if (!active) return sends;
            for (Module module : super.connectedModules) {
                sends.add(new Send(module, this.state, this));
            }
            return sends;
        }
        @Override
        public boolean getActive() {
            return active;
        }
        @Override
        public boolean getState() {
            return state;
        }
        @Override
        public String getName() {
            return super.getName();
        }

        @Override
        public String toString() {
            StringBuilder connections = new StringBuilder();
            for (Module module : super.connectedModules) {
                connections.append(module.getName()).append(", ");
            }

            return "name: " + super.name + ", type: flip flop, state: " + state + ", conections: " + connections.toString();
        }
        @Override
        public char getType() {
            return '%';
        }
    }

    public static class Module {
        private boolean state2 = false;
        private final String name;
        private ArrayList<Module> connectedModules = new ArrayList<>();
        public Module(String name) {
            this.name = name;
        }
        public String getName() {
            return name;
        }
        public Module[] getConnections() {
            return connectedModules.toArray(new Module[connectedModules.size()]);
        }
        public void connect(Module module) {
            connectedModules.add(module);
        }
        public void revceivePulse(boolean pulse, Module module) {
            this.state2 = pulse;
        }
        public ArrayList<Send> sendPulse() {
            ArrayList<Send> sends = new ArrayList<>();

            for (Module module : connectedModules) {
                sends.add(new Send(module, this.state2, this));
            }
            return sends;
        }
        public void addInput(Module module) {
        }
        public boolean getState() {
            return state2;
        }
        public char getType() {
            return ' ';
        }

        public Module[] getInputs() {
            return new Module[0];
        }

        public boolean getActive() {
            return true;
        }

        @Override
        public String toString() {
            StringBuilder connections = new StringBuilder();
            for (Module module : this.connectedModules) {
                connections.append(module.getName()).append(", ");
            }

            return "name: " + this.name + ", type: normal, state: " + this.state2 + " conections: " + connections.toString();
        }
    }
}
