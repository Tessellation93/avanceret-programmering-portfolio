import java.util.*;

public class MinAStar {

    record Felt(int r, int k, int g, int f) {}

    static int kortesteVej(String[] kort, int startR, int startK, int slutR, int slutK) {
        int[][] retninger = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        Map<String, Integer> g = new HashMap<>();
        Set<String> besøgt = new HashSet<>();
        PriorityQueue<Felt> kø = new PriorityQueue<>((a, b) -> a.f() - b.f());
        kø.add(new Felt(startR, startK, 0, Math.abs(startR - slutR) + Math.abs(startK - slutK)));
        g.put(startR + "," + startK, 0);
        while (!kø.isEmpty()) {
            Felt nu = kø.poll();
            if (besøgt.contains(nu.r() + "," + nu.k())) continue;
            if (nu.r() == slutR && nu.k() == slutK) return nu.g();
            besøgt.add(nu.r() + "," + nu.k());
            for (int[] d : retninger) {
                int nr = nu.r() + d[0];
                int nk = nu.k() + d[1];
                if (nr < 0 || nr >= kort.length || nk < 0 || nk >= kort[0].length()) continue;
                char felt = kort[nr].charAt(nk);
                if (felt == '#' || felt == '~') continue;
                int ny = nu.g() + 1;
                if (ny < g.getOrDefault(nr + "," + nk, Integer.MAX_VALUE)) {
                    g.put(nr + "," + nk, ny);
                    kø.add(new Felt(nr, nk, ny, ny + Math.abs(nr - slutR) + Math.abs(nk - slutK)));
                }
            }
        }
        return -1;
    }

    static final String[] kort = {
        "........K~",
        ".F.......~",
        ".........~",
        ".E.#####.~",
        "...#...#.~",
        ".###.#.#.~",
        "...#.#..T~",
        "...#...#.~",
        ".###.S...~",
        ".........."
    };

    static int[] find(char c) {
        for (int r = 0; r < kort.length; r++) {
            int k = kort[r].indexOf(c);
            if (k >= 0) return new int[]{r, k};
        }
        throw new RuntimeException("Fandt ikke " + c);
    }

    public static void main(String[] args) {
        tjek('E', 'T', 12);
        tjek('E', 'K', 10);
        tjek('E', 'F', 2);
        tjek('T', 'S', 5);
    }

    static void tjek(char a, char b, int forventet) {
        int[] s = find(a), t = find(b);
        int fik = kortesteVej(kort, s[0], s[1], t[0], t[1]);
        System.out.println((fik == forventet ? "OK    " : "FEJL  ") + a + " -> " + b
                + (fik == forventet ? "" : "   (fik " + fik + ", skulle være " + forventet + ")"));
    }
}
