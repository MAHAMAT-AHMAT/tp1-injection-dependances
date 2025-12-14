package fr.zenabkissir.pres;
import fr.zenabkissir.dao.IDao;
import fr.zenabkissir.metier.IMetier;

import java.io.File;
import java.util.Scanner;

public class Pres2 {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(new File("config.txt"));

        String daoClassName = scanner.nextLine();
        Class cDao = Class.forName(daoClassName);
        IDao d = (IDao) cDao.newInstance();

        String metierClassName = scanner.nextLine();
        Class cMetier = Class.forName(metierClassName);
        IMetier metier = (IMetier) cMetier.getConstructor(IDao.class).newInstance(d);
        /*** IMetier metier = (IMetier) cMetier.getConstructor().newInstance();
         * Method setDao = cMetier.getDeclaredMethod("setDao", IDao.class);
         * setDao.invoke(metier, d);
         ***/

        System.out.println("Resultat : " + metier.calcul());
    }
}
