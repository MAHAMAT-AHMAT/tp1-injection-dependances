# Projet d'Injection de Dépendances avec Spring Framework

## Description du Projet

L’objectif de ce projet est de démontrer les techniques d’injection de dépendances en utilisant le framework Spring, à travers quatre approches de gestion des dépendances entre les couches DAO et Métier, mettant en évidence la transition du couplage fort vers le couplage faible.

##  Architecture du Projet

Le projet suit une architecture en couches avec les packages suivants :

```
src/main/java/fr/zenabkissir/
├── dao/           # Couche d'accès aux données
├── ext/           # Extension avec implémentation alternative
├── metier/        # Couche métier (business logic)
└── pres/          # Couche présentation (points d'entrée)
```

## Structure Détaillée des Fichiers

### Configuration Maven

#### `pom.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>fr.zenabkissir</groupId>
    <artifactId>tp1-injection-dependances</artifactId>
    <version>1.0-SNAPSHOT</version>

    <properties>
        <maven.compiler.source>23</maven.compiler.source>
        <maven.compiler.target>23</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>
    <dependencies>
        <!-- https://mvnrepository.com/artifact/org.springframework/spring-core -->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-core</artifactId>
            <version>6.2.11</version>
        </dependency>
        <!-- https://mvnrepository.com/artifact/org.springframework/spring-context -->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
            <version>6.2.11</version>
        </dependency>
        <!-- https://mvnrepository.com/artifact/org.springframework/spring-beans -->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-beans</artifactId>
            <version>6.2.11</version>
        </dependency>
    </dependencies>
</project>
```

Fichier de configuration Maven définissant les dépendances Spring Framework et la version Java 21.

### 🔧 Fichiers de Configuration

#### `config.txt`

```
fr.zenabkissir.ext.DaoImplV2
fr.zenabkissir.metier.MetierImpl
```

Configuration Spring XML définissant les beans et leurs dépendances avec injection par constructeur.

### Couche DAO (Data Access Object)

#### `src/main/java/fr/zenabkissir/dao/IDao.java`

```java
package fr.zenabkissir.dao;

public interface IDao {
    double getData();
}
```

Interface définissant le contrat d'accès aux données.

#### `src/main/java/fr/zenabkissir/dao/DaoImpl.java`

```java
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
```

Implémentation DAO pour base de données avec annotation `@Repository`. Retourne la valeur 34.

#### `src/main/java/fr/zenabkissir/ext/DaoImplV2.java`

```java
package fr.zenabkissir.ext;

import fr.zenabkissir.dao.IDao;
import org.springframework.stereotype.Repository;

@Repository("d2")
public class DaoImplV2 implements IDao {
    @Override
    public double getData() {
        System.out.println("Version Capteurs...");
        double t = 12;
        return t;
    }
}
```

Implémentation DAO alternative pour capteurs avec annotation `@Repository`. Retourne la valeur 12.

### Couche Métier

#### `src/main/java/fr/zenabkissir/metier/IMetier.java`

```java
package fr.zenabkissir.metier;

public interface IMetier {
    double calcul();
}
```

Interface de la couche métier définissant la méthode de calcul.

#### `src/main/java/fr/zenabkissir/metier/MetierImpl.java`

```java
package fr.zenabkissirmetier;

import fr.zenabkissirdao.IDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("metier")
public class MetierImpl implements IMetier {
    private IDao dao;

    public MetierImpl(@Qualifier("d") IDao dao) {
        this.dao = dao;
    }

    @Override
    public double calcul() {
        double t = dao.getData();
        double res = t * 12 * Math.PI / 2 * Math.cos(t);
        return res;
    }

    public void setDao(IDao dao) {
        this.dao = dao;
    }
}
```

Implémentation de la logique métier avec injection par constructeur et `@Qualifier` pour spécifier quelle implémentation
DAO utiliser.

### Couche Présentation

#### `src/main/java/fr/zenabkissir/pres/Pres1.java`

```java
package fr.zenabkissir.pres;

import fr.zenabkissirext.DaoImplV2;
import fr.zenabkissirmetier.MetierImpl;

public class Pres1 {
    public static void main(String[] args) {
        DaoImplV2 d = new DaoImplV2();
        MetierImpl metier = new MetierImpl(d);
        System.out.println("Resultat : " + metier.calcul());
    }
}
```

**Approche 1 : Instanciation Statique** - Création manuelle des objets avec couplage fort. La classe est fortement
couplée à `DaoImplV2` et `MetierImpl`.

#### `src/main/java/fr/zenabkissir/pres/Pres2.java`

```java
package fr.zenabkissirpres;

import fr.zenabkissirdao.IDao;
import fr.zenabkissirmetier.IMetier;

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

        System.out.println("Resultat : " + metier.calcul());
    }
}
```

**Approche 2 : Instanciation Dynamique** - Utilisation de la réflexion Java pour instancier dynamiquement les classes à
partir du fichier de configuration. Couplage faible grâce à l'utilisation des interfaces.

#### `src/main/java/fr/zenabkissir/pres/PresSpringXML.java`

```java
package fr.zenabkissirpres;

import fr.zenabkissirmetier.IMetier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class PresSpringXML {
    public static void main(String[] args) {
        ApplicationContext springContext = new ClassPathXmlApplicationContext("config.xml");
        IMetier metier = springContext.getBean(IMetier.class);
        System.out.println("Resultat : " + metier.calcul());
    }
}
```

**Approche 3 : Configuration Spring XML** - Utilisation du contexte Spring avec configuration XML pour l'injection de
dépendances.

#### `src/main/java/fr/zenabkissir/pres/PresSpringAnnotation.java`

```java
package fr.zenabkissirpres;

import fr.zenabkissirmetier.IMetier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class PresSpringAnnotation {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext("fr.zenabkissir");
        IMetier metier = applicationContext.getBean(IMetier.class);
        System.out.println("Resultat : " + metier.calcul());
    }
}
```

**Approche 4 : Configuration Spring par Annotations** - Utilisation des annotations Spring pour la configuration
automatique et l'injection de dépendances.

## Comment Exécuter le Projet

### Prérequis

- Java 21+
- Maven 3.6+

### Compilation

```bash
mvn clean compile
```

### Exécution des différentes approches

1. **Instanciation Statique** :

```bash
java -cp target/classes fr.zenabkissirpres.Pres1
```

2. **Instanciation dynamique** :

```bash
java -cp target/classes fr.zenabkissirpres.Pres2
```

3. **Spring XML** :

```bash
java -cp target/classes:target/dependency/* fr.zenabkissirpres.PresSpringXML
```

4. **Spring Annotations** :

```bash
java -cp target/classes:target/dependency/* fr.zenabkissirpres.PresSpringAnnotation
```

##  Changement d'Implémentation

### Pour Spring XML (`config.xml`)

```xml
<!-- Changer de DaoImplV2 vers DaoImpl -->
<bean id="d" class="fr.zenabkissirdao.DaoImpl"></bean>
```

### Pour Spring Annotations

```java
// Modifier le @Qualifier dans MetierImpl.java
public MetierImpl(@Qualifier("d2") IDao dao);
```

### Pour l'Instanciation Dynamique (`config.txt`)

```
fr.zenabkissir.dao.DaoImpl
fr.zenabkissir.metier.MetierImpl
```

### Pour l'Instanciation Statique (Pres1)

```java
// Modification directe du code source nécessaire
DaoImpl d = new DaoImpl(); // au lieu de DaoImplV2
```
