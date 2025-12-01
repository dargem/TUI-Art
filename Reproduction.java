
import java.util.Map.Entry;
import java.util.TreeMap;

public class Reproduction {

    public static void main(String[] args) {
        testNaN();
        testUnderflow();
    }

    private static void testNaN() {
        System.out.println("Testing NaN...");
        TreeMap<Double, String> map = new TreeMap<>();
        map.put(0.0, "Vertical");
        map.put(1.57, "Horizontal"); // PI/2

        double search_angle = Double.NaN;
        
        // Simulate CharacterRetriever logic
        TreeMap<Double, Double> closeness_scores = new TreeMap<>();
        double total_closeness_score = 0;
        double CLOSENESS_SCALAR = 20;

        for (Double angle : map.keySet()) {
            double offset = Math.abs(angle - search_angle); // NaN
            offset = (offset == 0.0) ? 1e-12 : offset; // NaN
            offset = Math.pow(offset, CLOSENESS_SCALAR); // NaN

            final double score = 1.0 / offset; // NaN
            closeness_scores.put(angle, score);
            total_closeness_score += score; // NaN
        }

        TreeMap<Double, String> probs = new TreeMap<>();
        double accumulated_probability = 0;
        for (Entry<Double, Double> entry : closeness_scores.entrySet()) {
            final double score = entry.getValue();
            final double probability = score / total_closeness_score; // NaN
            accumulated_probability += probability; // NaN
            probs.put(accumulated_probability, map.get(entry.getKey()));
        }

        System.out.println("Probs keys: " + probs.keySet());
        // Check what we get for a random number
        double percent = 0.5;
        Entry<Double, String> entry = probs.ceilingEntry(percent);
        System.out.println("Result for 0.5: " + (entry == null ? "null" : entry.getValue()));
    }

    private static void testUnderflow() {
        System.out.println("\nTesting Underflow...");
        TreeMap<Double, String> map = new TreeMap<>();
        map.put(0.0, "Vertical");
        map.put(1.57, "Horizontal");

        double search_angle = 0.0 + 1e-16; // Very close to 0
        
        TreeMap<Double, Double> closeness_scores = new TreeMap<>();
        double total_closeness_score = 0;
        double CLOSENESS_SCALAR = 20;

        for (Double angle : map.keySet()) {
            double offset = Math.abs(angle - search_angle);
            System.out.println("Angle: " + angle + ", Offset: " + offset);
            offset = (offset == 0.0) ? 1e-12 : offset;
            double pow = Math.pow(offset, CLOSENESS_SCALAR);
            System.out.println("Pow: " + pow);

            final double score = 1.0 / pow;
            System.out.println("Score: " + score);
            
            closeness_scores.put(angle, score);
            total_closeness_score += score;
        }
        System.out.println("Total Score: " + total_closeness_score);
    }
}
