package fr.zenabkissir.dao;

import org.springframework.stereotype.Repository;

@Repository("d")
public class DaoImpl implements IDao {
    @Override
    public double getData() {
        System.out.println("Version Base de Données");
        double t = 34;
        return t;
    }
}
