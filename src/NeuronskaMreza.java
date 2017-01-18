public class NeuronskaMreza {
    int[] arhitektura;
    double[] neuroni;

    public NeuronskaMreza(String arhitektura) {
        int brojNeurona = 0;
        String[] slojevi = arhitektura.split("x");
        this.arhitektura = new int[slojevi.length];
        for (int i = 0; i < slojevi.length; i++) {
            int velicinaSloja = Integer.parseInt(slojevi[i]);
            brojNeurona += velicinaSloja;
            this.arhitektura[i] = velicinaSloja;
        }
        neuroni = new double[brojNeurona];
    }

    public int brojParametara() {
        int brojParametara = 0;
        for (int slojIndex = 1; slojIndex < arhitektura.length; slojIndex++) {
            int slojVel = arhitektura[slojIndex];
            int prethodniSlojVel = arhitektura[slojIndex - 1];
            if (slojIndex == 1) {
                brojParametara += slojVel * prethodniSlojVel * 2;
            } else if (slojIndex > 1) {
                brojParametara += slojVel * (prethodniSlojVel + 1);
            }
        }
        return brojParametara;
    }

    // calcOutput
    // parametri imaju redom spremljene parametre:
    // prvi sloj: w1 s1 w2 s2 (za svaki neuron)
    // svi ostali slojevi : w0 w1 ... w<velicinaSloja> (za svaki neuron)
    double[] izracunajIzlaz(double[] parametri, double[] ulaz) {
        // indeks neurona za koji racunamo izlaz
        int neuron_i = 0;
        // indeks parametra koji nam trenutno treba
        int parametar_i = 0;
        // ulazni sloj nista ne racuna, vec stavlja dobiveni uzorak na izlaz
        neuroni[neuron_i++] = ulaz[0];
        neuroni[neuron_i++] = ulaz[1];

        // neuroni tipa 1
        for (int i = 0; i < arhitektura[1]; i++) {
            double sum = 0;
            sum += Math.abs(neuroni[0] - parametri[parametar_i++])
                    / Math.abs(parametri[parametar_i++]);
            sum += Math.abs(neuroni[1] - parametri[parametar_i++])
                    / Math.abs(parametri[parametar_i++]);
            neuroni[neuron_i++] = 1 / (1 + sum);
        }

        // neuroni tipa 2
        for (int k = 2; k < arhitektura.length; k++) {
            int neuronPrethodniSloj_i = neuron_i - arhitektura[k - 1];
            for (int j = 0; j < arhitektura[k]; j++) {
                double net = 0;
                net += parametri[parametar_i++];
                int np_i = neuronPrethodniSloj_i;
                for (int i = 0; i < arhitektura[k - 1]; i++) {
                    net += parametri[parametar_i++] * neuroni[np_i++];
                }
                neuroni[neuron_i++] = sigm(net);
            }
        }

        double[] izlaz = new double[3];
        izlaz[2] = neuroni[--neuron_i];
        izlaz[1] = neuroni[--neuron_i];
        izlaz[0] = neuroni[--neuron_i];
        return izlaz;
    }

    private double sigm(double net) {
        return 1.0 / (1.0 + Math.pow(Math.E, -net));
    }

    // calcError
    double izracunajPogresku(SetPodataka podaci, double[] parametri) {
        double mse = 0;
        double sum = 0;

        for (int s = 0; s < podaci.brojPodataka(); s++) {
            for (int o = 0; o < 3; o++) {
                double t_so = podaci.dohvatiPodatak(s).izlaz[o];
                double y_so = izracunajIzlaz(parametri,
                        podaci.dohvatiPodatak(s).ulaz)[o];
                sum += Math.pow(t_so - y_so, 2);
            }
        }

        mse = sum / podaci.brojPodataka();
        return mse;
    }
}
