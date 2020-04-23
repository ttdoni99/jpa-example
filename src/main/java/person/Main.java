package person;

import com.github.javafaker.Faker;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.ZoneId;
import java.util.List;
import lombok.*;

public class Main {

    private static EntityManagerFactory databaseFactory;
    private static EntityManager database;
    private static Faker faker;

    public static void main(String[] args) {
        int szemelyekSZama=1000;
        databaseFactory = Persistence.createEntityManagerFactory("jpa-example");
        database = databaseFactory.createEntityManager();
        faker = new Faker();
        try {
            database.getTransaction().begin();

            for (int i = 0; i < szemelyekSZama; i ++) {
                database.persist(randomPerson());
            }
            database.getTransaction().commit();



        } finally {
            database.close();
            databaseFactory.close();
        }
    }

    public static Person randomPerson() {
        Person ujSzemely = new Person();
        ujSzemely.setName(faker.name().fullName());
        com.github.javafaker.Address randomCim = faker.address();
        ujSzemely.setAddress(Address.builder()
                .city(randomCim.city())
                .streetAddress(randomCim.streetAddress())
                .country(randomCim.country())
                .state(randomCim.state())
                .zip(randomCim.zipCode())
                .build());
        ujSzemely.setEmail(faker.internet().emailAddress());
        ujSzemely.setGender(faker.options().option(Person.Gender.FEMALE, Person.Gender.MALE));
        ujSzemely.setProfession(faker.company().profession());
        ujSzemely.setDob(faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        System.out.println(ujSzemely);
        return ujSzemely;
    }
}
