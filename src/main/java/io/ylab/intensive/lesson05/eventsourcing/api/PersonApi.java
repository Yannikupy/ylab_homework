package io.ylab.intensive.lesson05.eventsourcing.api;

import io.ylab.intensive.lesson05.eventsourcing.PersonLesson05;
import org.springframework.beans.factory.DisposableBean;

import java.util.List;

public interface PersonApi extends DisposableBean {
    void deletePerson(Long personId);

    void savePerson(Long personId, String firstName, String lastName, String middleName);

    PersonLesson05 findPerson(Long personId);

    List<PersonLesson05> findAll();

}
