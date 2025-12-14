package fr.zenabkissir.pres;

import fr.zenabkissir.ext.DaoImplV2;
import fr.zenabkissir.metier.MetierImpl;

public class Pres1 {
    public static void main(String[] args) {
        DaoImplV2 d = new DaoImplV2();
        MetierImpl metier = new MetierImpl(d);
        System.out.println("Resultat : " + metier.calcul());
    }
}
