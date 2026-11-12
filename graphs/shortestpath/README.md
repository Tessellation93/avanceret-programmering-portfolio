# Portfolio: Grafer og korteste vej på mine egne veje

Dijkstra og A* på kort over steder, jeg selv kender: Taarbæk, Lyngby og stykket op langs Strandvejen.

## Hvem har lavet hvad

| Fil | Hvem |
|---|---|
| `src/MinDijkstra.java` | Dijkstra-algoritmen (`kortesteVej`) er skrevet af mig. Vejnettet og testene i `main` kom som en skabelon fra AI. |
| `src/MinAStar.java` | A*-algoritmen (`kortesteVej`) er skrevet af mig. Kortet og testene i `main` kom som en skabelon fra AI. |
| `src/DijkstraVisualisering.java` | Lavet med AI-hjælp (Claude). Kalder min Dijkstra og printer vejnettet, afstande og ruten. |
| `src/AStarVisualisering.java` | Lavet med AI-hjælp (Claude). Kalder min A* og tegner kortet med ruten. |
| `output/` | Output fra en kørsel af de to visualiseringer. |

Valget af steder og afstandene er mit eget. Afstandene er omtrentlige skøn.

## Stederne

| Sted | Hvad det er |
|---|---|
| Taarbæk Skole | ved havnen i Taarbæk |
| Taarbæk Havn / Taarbæk | knudepunkter i Taarbæk |
| EK Firskovvej | min skole, Firskovvej 18 i Lyngby |
| Lyngby Station | knudepunktet mod Lyngby |
| Klampenborg Station | knudepunktet mod syd |
| Strandvejen 329 | et sted på Strandvejen |
| Frilandsmuseet | Kongevejen 100 |
| Karen Blixen Museet | Rungsted Strandvej 111 |

## Sådan kører du det

Kræver Java 17 eller nyere.

```
javac -encoding UTF-8 -d out src/*.java
java -cp out DijkstraVisualisering
java -cp out AStarVisualisering
```

## 1. Dijkstra: vægtet og rettet vejnet

9 steder og 12 veje. De fleste veje kan køres begge veje og er lagt ind som to veje, men én vej er en envejsvej: fra Taarbæk Havn kan man køre direkte til Strandvejen 329 (1,5 km), men ikke tilbage. Retningen betyder noget, så grafen er rettet. Vejene har forskellig længde, så grafen er vægtet.

Resultat fra Taarbæk Skole til EK Firskovvej:

```
Taarbæk Skole -> Taarbæk Havn -> Taarbæk -> Lyngby Station -> EK Firskovvej
Samlet afstand: 9,2 km
```

### Sådan virker min algoritme
1. Start har afstand 0 og lægges i en prioritetskø, som altid giver det sted med mindst afstand først.
2. Jeg tager et sted ud af køen. Er jeg allerede færdig med det, springer jeg det over. Er det målet, returnerer jeg afstanden.
3. Ellers markerer jeg stedet som besøgt og kigger på alle veje derfra. Er afstanden til naboen via dette sted kortere end den, jeg kendte, gemmer jeg den og lægger naboen i køen.
4. Findes målet ikke, returnerer jeg -1.

Køen kan indeholde gamle, dårligere afstande til samme sted. De bliver sprunget over, når de kommer frem, fordi stedet da allerede er besøgt.

Kompleksitet: O((V + E) log V). `log V` kommer fra prioritetskøen.

### Visualiseringen
`DijkstraVisualisering` printer vejnettet, afstanden fra Taarbæk Skole til alle steder (sorteret) og den fundne rute med løbende afstand. Så kan man se, at ruten ikke nødvendigvis er den med færrest veje, men den med færrest kilometer.

## 2. A* på et skematisk kort

Et skematisk 10x10 kort (nord er op, Øresund mod øst). `#` er områder, man ikke kan køre igennem, `~` er Øresund. Hvert skridt koster 1.

```
  . . . . . . . . K ~
  . F . . . . . . . ~
  . . . . . . . . . ~
  . E . # # # # # . ~
  . . . # . . . # . ~
  . # # # . # . # . ~
  . . . # . # . . T ~
  . . . # . . . # . ~
  . # # # . S . . . ~
  . . . . . . . . . .
```

E = EK Firskovvej, F = Frilandsmuseet, T = Taarbæk Skole, S = Strandvejen 329, K = Karen Blixen Museet.

### Heuristikken
Jeg bruger Manhattan-afstand til målet: `|række-forskel| + |kolonne-forskel|`. Den overvurderer aldrig den rigtige afstand på et grid med fire retninger, og derfor finder A* stadig den korteste vej. Køen sorteres efter `f = g + h`, hvor `g` er skridt gået og `h` er gættet på resten.

### Sådan virker min algoritme
Den er bygget som min Dijkstra, men på et gitter og med et gæt:
1. Start lægges i en prioritetskø med 0 skridt og et gæt på afstanden til målet.
2. Køen giver altid det felt med lavest `f = g + h` først. Er feltet allerede besøgt, springes det over. Er det målet, returnerer jeg antal skridt.
3. Ellers kigger jeg på felterne op, ned, venstre og højre. Mure, vand og felter uden for kortet springes over. For de andre regner jeg skridt til naboen ud. Er det mindre end det, jeg kendte, gemmer jeg det og lægger naboen i køen med `f`.
4. Findes der ingen vej, returnerer jeg -1.

### Resultat
Fra EK Firskovvej til Taarbæk Skole er der **12 skridt**, fra EK Firskovvej til Karen Blixen Museet 10 og fra Taarbæk Skole til Strandvejen 329 5. Visualiseringen tegner ruterne med `*`.

### Forskellen på Dijkstra og A*
- Dijkstra sorterer efter `g`. A* sorterer efter `g + h`.
- Er `h` altid 0, er A* præcis Dijkstra.
- A* skal bruge et mål og et gæt. Dijkstra kan finde afstande til alle steder.
- A* finder kun garanteret den korteste vej, hvis heuristikken aldrig overvurderer.
