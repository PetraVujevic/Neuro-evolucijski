import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class SetPodataka {
    private ArrayList<Uzorak> uzorci = new ArrayList<Uzorak>();

    public SetPodataka() {
        Scanner sc = null;
        try {
            sc = new Scanner(Paths.get("tocke"));
        } catch (IOException e) {
            System.out
                    .println("Ne mogu procitati podatke iz datoteke 'tocke'.");
        }
        while (sc.hasNextLine()) {
            Uzorak uzorak = new Uzorak();
            uzorak.ulaz[0] = sc.nextDouble();
            uzorak.ulaz[1] = sc.nextDouble();
            uzorak.izlaz[0] = sc.nextInt();
            uzorak.izlaz[1] = sc.nextInt();
            uzorak.izlaz[2] = sc.nextInt();
            uzorci.add(uzorak);
        }
    }

    public int brojPodataka() {
        return uzorci.size();
    }

    public Uzorak dohvatiPodatak(int i) {
        return uzorci.get(i);
    }
}

class Uzorak {
    double[] ulaz;
    int[] izlaz;

    public Uzorak() {
        ulaz = new double[2];
        izlaz = new int[3];
    }
}
