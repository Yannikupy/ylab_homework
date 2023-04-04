package io.ylab.intensive.lesson05.eventsourcing.api;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ApiApp {
    public static void main(String[] args) throws Exception {
        try (AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Config.class)) {
            applicationContext.start();
            PersonApi personApi = applicationContext.getBean(PersonApiImpl.class);
            personApi.savePerson(1L, "Yan", "Borisov", "Arturovich");
            personApi.savePerson(2L, "Artem", "Moskvin", "Aleksandrovich");
            System.out.println(personApi.findPerson(1L));
            System.out.println(personApi.findAll());
            personApi.deletePerson(1L);
        }
    }
}
