package fr.zenabkissir.dao;

public class DaoImpl implements IDao {
    @Override
    public double getData() {
        System.out.println("Version Base de Données");
        double t = 34;
        return t;
    }
}
