package ru.hogwarts.school.service;

import ru.hogwarts.school.model.Faculty;

import java.util.Collection;
import java.util.Optional;

public interface FacultyService {
    Faculty createFaculty(Faculty faculty);

    Faculty getFaculty(long id);

    Faculty editFaculty(Faculty faculty);

    void deleteFaculty(long id);

    Collection<Faculty> filterByColor(String color);

    Collection<Faculty> findByColorIgnoreCaseOrNameIgnoreCase(String color, String name);

    Optional<String> getLongestNameOfFaculties();
}
