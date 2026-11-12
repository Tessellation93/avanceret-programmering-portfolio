import java.util.*;

public class MinDijkstra {

    record Punkt(String sted, int afstand) {}

    static int kortesteVej(Map<String, List<Vej>> graf, String start, String slut) {
        Map<String, Integer> afstand = new HashMap<>();
        Set<String> besøgt = new HashSet<>();
        PriorityQueue<Punkt> kø = new PriorityQueue<>((a, b) -> a.afstand() - b.afstand());
        kø.add(new Punkt(start, 0));
        afstand.put(start, 0);
	while (!kø.isEmpty()) {
            Punkt nu = kø.poll();
            if (besøgt.contains(nu.sted())) continue;
            if (nu.sted().equals(slut)) return nu.afstand();
            besøgt.add(nu.sted());
            for (Vej v : graf.getOrDefault(nu.sted(), List.of())) {
                int ny = nu.afstand() + v.meter();
                if (ny < afstand.getOrDefault(v.til(), Integer.MAX_VALUE)) {
                    afstand.put(v.til(), ny);
                    kø.add(new Punkt(v.til(), ny));
                }
            }
            
        }
        return -1;
    }

    record Vej(String til, int meter) {}

    static final Map<String, List<Vej>> graf = new HashMap<>();

    static void vej(String a, String b, int meter) {
        graf.computeIfAbsent(a, k -> new ArrayList<>()).add(new Vej(b, meter));
        graf.computeIfAbsent(b, k -> new ArrayList<>()).add(new Vej(a, meter));
    }

    public static void main(String[] args) {
        vej("Taarbæk Skole", "Taarbæk Havn", 300);
        vej("Taarbæk Havn", "Taarbæk", 400);
        vej("Taarbæk", "Klampenborg Station", 2000);
        vej("Taarbæk", "Lyngby Station", 6000);
        vej("Taarbæk", "Frilandsmuseet", 5500);
        vej("Taarbæk", "Karen Blixen Museet", 8000);
        vej("Klampenborg Station", "Lyngby Station", 7000);
        vej("Klampenborg Station", "Strandvejen 329", 1000);
        vej("Lyngby Station", "EK Firskovvej", 2500);
        vej("Lyngby Station", "Frilandsmuseet", 2000);
        vej("Lyngby Station", "Karen Blixen Museet", 11000);
        vej("EK Firskovvej", "Frilandsmuseet", 3500);
        graf.get("Taarbæk Havn").add(new Vej("Strandvejen 329", 1500));

        tjek("Taarbæk Skole", "EK Firskovvej", 9200);
        tjek("Taarbæk Havn", "Strandvejen 329", 1500);
        tjek("Strandvejen 329", "Taarbæk Havn", 3400);
    }

    static void tjek(String start, String slut, int forventet) {
        int fik = kortesteVej(graf, start, slut);
        System.out.println((fik == forventet ? "OK    " : "FEJL  ") + start + " -> " + slut
                + (fik == forventet ? "" : "   (fik " + fik + ", skulle være " + forventet + ")"));
    }
}
