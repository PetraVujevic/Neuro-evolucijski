import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class GA {
    static NeuronskaMreza mreza = new NeuronskaMreza("2x6x4x3");
    static SetPodataka podaci = new SetPodataka();
    static double[] parametri = new double[mreza.brojParametara()];
    static Random random = new Random();
    // static double[] parametri = { 0.125, 1, 0.25, 2, 0.375, 1, 0.25, 2,
    // 0.625, 1, 0.25, 2, 0.875, 1, 0.25, 2, 0.125, 1, 0.75, 2, 0.375, 1, 0.75,
    // 2, 0.625, 1, 0.75, 2, 0.875, 1, 0.75, 2, 0, 1, 0, 0, 1, 0, 0,
    // 1, 0, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 0 };

    public static void main(String[] args) {
        nauciParametreMrezePomocuGA();

        // testiranje mreze
        int brojKorektnih = 0;
        int brojPogresnih = 0;
        for (int k = 0; k < podaci.brojPodataka(); k++) {
            Uzorak uzorak = podaci.dohvatiPodatak(k);
            double[] izlazMreze = mreza.izracunajIzlaz(parametri, uzorak.ulaz);
            int[] dobiveniIzlaz = new int[izlazMreze.length];
            for (int i = 0; i < izlazMreze.length; i++) {
                if (izlazMreze[i] < 0.5) {
                    dobiveniIzlaz[i] = 0;
                } else {
                    dobiveniIzlaz[i] = 1;
                }
            }
            int[] ocekivaniIzlaz = uzorak.izlaz;

            boolean isti = true;
            System.out.print("Ocekivani izaz: ");
            for (int i = 0; i < ocekivaniIzlaz.length; i++) {
                System.out.print(ocekivaniIzlaz[i] + " ");
            }
            System.out.print("\nDobiveni izlaz: ");
            for (int i = 0; i < dobiveniIzlaz.length; i++) {
                System.out.print(dobiveniIzlaz[i] + " ");
                isti &= (dobiveniIzlaz[i] == ocekivaniIzlaz[i]);
            }
            System.out.println();
            if (isti) {
                brojKorektnih++;
            } else {
                brojPogresnih++;
            }
            System.out.println();
        }
        System.out.println("\nBroj korektno klasificiranih uzoraka: "
                + brojKorektnih);
        System.out.println("\nBroj pogresno klasificiranih uzoraka: "
                + brojPogresnih);
    }

    static void nauciParametreMrezePomocuGA() {
        int vel_pop = 100;
        int broj_iteracija = 30000;
        int mortalitet = 20;
        double pm = 1. / 100;
        // vjerojatnost primjene prvog operatora mutacije
        double v1 = 0.6;
        Random random = new Random();
        // stvori pocetnu populaciju
        ArrayList<Jedinka> populacija = new ArrayList<Jedinka>();
        for (int i = 0; i < vel_pop; i++) {
            populacija.add(Jedinka.slucajnoGenerirajJedinku(random,
                    mreza.brojParametara()));
        }

        // evaluiraj populaciju
        for (int i = 0; i < vel_pop; i++) {
            Jedinka jedinka = populacija.get(i);
            jedinka.kazna = mreza.izracunajPogresku(podaci, jedinka.geni);
        }

        Jedinka najbolja = null;

        for (int generacija = 1; generacija <= broj_iteracija; generacija++) {
            for (int j = 0; j < mortalitet; j++) {
                // odaberi slucajne jedinke za 3-turnirsku selekciju
                ArrayList<Jedinka> turnir = new ArrayList<Jedinka>();
                for (int k = 0; k < 3; k++) {
                    int index = random.nextInt(populacija.size());
                    turnir.add(populacija.get(index));
                }

                // krizaj dvije najbolje jedinke, slucajno odaberi nacin
                // krizanja
                Collections.sort(turnir);

                int nacinKrizanja = random.nextInt(3);
                Jedinka r1 = turnir.get(0);
                Jedinka r2 = turnir.get(1);
                Jedinka dijete;
                if (nacinKrizanja == 0) {
                    dijete = potpunaAritmetickaRekombinacija(r1, r2);
                } else if (nacinKrizanja == 1) {
                    dijete = jednostavnaAritmetickaRekombinacija(r1, r2);
                } else {
                    dijete = jednostrukaAritmetickaRekombinacija(r1, r2);
                }

                // mutiraj dijete, slucajno odaberi nacin mutacije
                double operatorMutacije = random.nextDouble();
                if (operatorMutacije <= v1) {
                    dijete = mutiraj1(dijete, pm);
                } else {
                    dijete = mutiraj2(dijete, pm);
                }

                // evaluiraj dijete
                dijete.kazna = mreza.izracunajPogresku(podaci, dijete.geni);

                // zamijeni dijete s najgorom jedinkom
                int indexNajgora = populacija.indexOf(turnir.get(2));
                populacija.set(indexNajgora, dijete);
            }

            Collections.sort(populacija);

            if (najbolja == null || najbolja.kazna > populacija.get(0).kazna) {
                najbolja = populacija.get(0);
                System.out.println("\nGeneracija " + generacija + ": \n"
                        + najbolja);
                parametri = najbolja.geni;
            }
        }
    }

    private static Jedinka mutiraj1(Jedinka dijete, double pm) {
        double[] geni = new double[dijete.geni.length];
        for (int i = 0; i < dijete.geni.length; i++) {
            double r = random.nextDouble();
            if (r <= pm)
                geni[i] = dijete.geni[i] + random.nextGaussian();
            else
                geni[i] = dijete.geni[i];
        }
        return new Jedinka(geni);
    }

    // jednostavna mutacija
    private static Jedinka mutiraj2(Jedinka dijete, double pm) {
        double[] geni = new double[dijete.geni.length];
        for (int i = 0; i < dijete.geni.length; i++) {
            double r = random.nextDouble();
            if (r <= pm)
                geni[i] = random.nextGaussian();
            else
                geni[i] = dijete.geni[i];
        }
        return new Jedinka(geni);
    }

    static private Jedinka potpunaAritmetickaRekombinacija(Jedinka r1,
            Jedinka r2) {
        double[] geni = new double[r1.geni.length];
        for (int i = 0; i < r1.geni.length; i++) {
            geni[i] = (r1.geni[i] + r2.geni[i]) / 2;
        }
        Jedinka dijete = new Jedinka(geni);
        return dijete;
    }

    static private Jedinka jednostavnaAritmetickaRekombinacija(Jedinka r1,
            Jedinka r2) {
        double[] geni = new double[r1.geni.length];
        int indeks = random.nextInt(geni.length);
        int j = random.nextInt(2);
        for (int i = 0; i < indeks; i++) {
            geni[i] = j == 0 ? r1.geni[i] : r2.geni[i];
        }
        for (int i = indeks; i < geni.length; i++) {
            geni[i] = (r1.geni[i] + r2.geni[i]) / 2;
        }

        Jedinka dijete = new Jedinka(geni);
        return dijete;
    }

    static private Jedinka jednostrukaAritmetickaRekombinacija(Jedinka r1,
            Jedinka r2) {
        double[] geni = new double[r1.geni.length];
        double indeks = random.nextInt(r1.geni.length);
        int j = random.nextInt(2);
        for (int i = 0; i < indeks; i++) {
            if (i == indeks) {
                geni[i] = (r1.geni[i] + r2.geni[i]) / 2;
            } else {
                geni[i] = j == 0 ? r1.geni[i] : r2.geni[i];
            }
        }

        Jedinka dijete = new Jedinka(geni);
        return dijete;
    }
}
