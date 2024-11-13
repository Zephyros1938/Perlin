import com.zephyros1938.lib.PerlinNoise.PerlinNoise;
import com.zephyros1938.lib.math.Vector.Vector.Vector2;
import com.zephyros1938.lib.ConsoleColors.ConsoleColors;

public class Main {

    public static void main(String[] args) {

        double frequency = 1.1;
        double amplitude = 1.0;
        double sizeX = 100.0;
        double sizeY = 100.0;

        PerlinNoise P0 = new PerlinNoise(1);
        PerlinNoise P1 = new PerlinNoise(1);
        PerlinNoise P2 = new PerlinNoise(1);

        int[] acceptableSizes = { 0, 2, 4 };

        // Parse command-line arguments for frequency and amplitude
        if (args.length >= 2) {
            try {
                frequency = Double.parseDouble(args[0]);
                amplitude = Double.parseDouble(args[1]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Usage: java PerlinNoise <frequency> <amplitude> <size x> <size y>");
                return;
            }
        } else {
            System.out.println(
                    "Usage: \tjava PerlinNoise <frequency> <amplitude> <size x> <size y>\n\tEX: java PerlinNoise 1.1 2.0 10.0 10.0");
            System.out.println("Using default values: frequency = 1.1, amplitude = 1.0");
        }
        for (int size : acceptableSizes) {
            if (args.length != size) {
                try {
                    sizeX = Double.parseDouble(args[2]);
                    sizeY = Double.parseDouble(args[3]);
                } catch (Exception e) {
                    System.out.println(
                            "Invalid input. Usage: java PerlinNoise <frequency> <amplitude> <size x> <size y>");
                    return;
                }
            }
        }

        String output = new String();

        double incremental = 1.0;

        for (double y = 0.0; y < sizeY; y += incremental) {
            for (double x = 0.0; x < sizeX; x += incremental) {
                double xi = x * frequency;
                double yi = y * frequency;

                double N0 = P0.noise(xi, yi) * amplitude;
                double N1 = P1.noise(xi, yi) * amplitude;
                double N2 = P2.noise(xi, yi) * amplitude;

                N0 += 1.0;
                N0 /= 2.0;
                N1 += 1.0;
                N1 /= 2.0;
                N2 += 1.0;
                N2 /= 2.0;

                Integer C0 = (int) Math.round(255*(N0));
                Integer C1 = (int) Math.round(255*(N1));
                Integer C2 = (int) Math.round(255*(N2));

                output += "\033[48;2;" + C0 + ";" + C1 + ";" + C2 + "m  ";
            }
            output += ConsoleColors.RESET + '\n';
        }

        System.out.println(output);
    }

    public static double roundToPosition(double value, int decimalPlaces) {
        double scale = Math.pow(10, decimalPlaces);
        return Math.round(value * scale) / scale;
    }

    public static String clampDouble(double value) {
        int clampedValue = (int) Math.max(0, Math.min(255, value));

        return String.format("%d", clampedValue);
    }
}
