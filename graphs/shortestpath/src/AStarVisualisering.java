public class AStarVisualisering {

    static final int[][] RETNINGER = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    static void tegnRute(char fra, char til) {
        String[] kort = MinAStar.kort;
        int[] s = MinAStar.find(fra);
        int[] t = MinAStar.find(til);
        int længde = MinAStar.kortesteVej(kort, s[0], s[1], t[0], t[1]);
        boolean[][] rute = new boolean[kort.length][kort[0].length()];
        int r = s[0], k = s[1];
        int tilbage = længde;
        while (tilbage > 0) {
            for (int[] d : RETNINGER) {
                int nr = r + d[0], nk = k + d[1];
                if (nr < 0 || nr >= kort.length || nk < 0 || nk >= kort[0].length()) continue;
                char felt = kort[nr].charAt(nk);
                if (felt == '#' || felt == '~') continue;
                if (MinAStar.kortesteVej(kort, nr, nk, t[0], t[1]) == tilbage - 1) {
                    r = nr;
                    k = nk;
                    rute[r][k] = true;
                    tilbage--;
                    break;
                }
            }
        }
        System.out.println("Fra " + fra + " til " + til + ": " + længde + " skridt");
        for (int rr = 0; rr < kort.length; rr++) {
            StringBuilder sb = new StringBuilder("  ");
            for (int kk = 0; kk < kort[rr].length(); kk++) {
                char c = kort[rr].charAt(kk);
                if (c == fra || c == til) sb.append(c);
                else if (rute[rr][kk] && c == '.') sb.append('*');
                else sb.append(c);
                sb.append(' ');
            }
            System.out.println(sb);
        }
        System.out.println();
    }

    public static void main(String[] args) {
        MinAStar.main(new String[0]);
        System.out.println();
        System.out.println("E = EK Firskovvej, F = Frilandsmuseet, T = Taarbæk Skole, S = Strandvejen 329,");
        System.out.println("K = Karen Blixen Museet, # = kan ikke køres igennem, ~ = Øresund, * = ruten");
        System.out.println();
        tegnRute('E', 'T');
        tegnRute('E', 'K');
        tegnRute('T', 'S');
    }
}
