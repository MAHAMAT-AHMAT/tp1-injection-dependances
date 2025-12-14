package fr.zenabkissir.pres;

import fr.zenabkissir.metier.IMetier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class PresSpringAnnotation {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext("fr.zenabkissir");
        IMetier metier = applicationContext.getBean(IMetier.class);
        System.out.println("Resultat : " + metier.calcul());
    }
}
