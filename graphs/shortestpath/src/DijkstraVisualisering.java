import java.util.*;

public class DijkstraVisualisering {

    static final String START = "Taarbæk Skole";
    static final String SLUT = "EK Firskovvej";

    static String km(int meter) {
        return String.format("%.1f km", meter / 1000.0).replace('.', ',');
    }

    public static void main(String[] args) {
        MinDijkstra.main(new String[0]);
        Map<String, List<MinDijkstra.Vej>> graf = MinDijkstra.graf;

        System.out.println();
        System.out.println("=== VEJNETTET ===");
        for (String sted : new TreeSet<>(graf.keySet())) {
            System.out.println(sted);
            for (MinDijkstra.Vej v : graf.get(sted)) {
                System.out.println("   -> " + v.til() + " (" + km(v.meter()) + ")");
            }
        }

        System.out.println();
        System.out.println("=== KORTESTE AFSTAND FRA " + START.toUpperCase() + " ===");
        List<String> steder = new ArrayList<>(graf.keySet());
        steder.sort(Comparator.comparingInt((String s) -> MinDijkstra.kortesteVej(graf, START, s)));
        for (String sted : steder) {
            int d = MinDijkstra.kortesteVej(graf, START, sted);
            System.out.println(String.format("%-22s %s", sted, km(d)));
        }

        System.out.println();
        System.out.println("=== RUTEN FRA " + START.toUpperCase() + " TIL " + SLUT.toUpperCase() + " ===");
        int total = MinDijkstra.kortesteVej(graf, START, SLUT);
        String nu = START;
        int tilbagelagt = 0;
        System.out.println(String.format("%-22s %s", nu, km(0)));
        while (!nu.equals(SLUT)) {
            int tilbage = MinDijkstra.kortesteVej(graf, nu, SLUT);
            for (MinDijkstra.Vej v : graf.get(nu)) {
                int rest = MinDijkstra.kortesteVej(graf, v.til(), SLUT);
                if (rest != -1 && v.meter() + rest == tilbage) {
                    tilbagelagt += v.meter();
                    nu = v.til();
                    System.out.println(String.format("%-22s %s", nu, km(tilbagelagt)));
                    break;
                }
            }
        }
        System.out.println();
        System.out.println("Samlet afstand: " + km(total));
    }
}
