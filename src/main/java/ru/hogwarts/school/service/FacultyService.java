package ru.hogwarts.school.service;

import ru.hogwarts.school.model.Faculty;

import java.util.Collection;

public interface FacultyService {
    Faculty createFaculty(Faculty faculty);

    Faculty getFaculty(long id);

    Faculty editFaculty(Faculty faculty);

    Faculty deleteFaculty(long id);

    Collection<Faculty> filterByColor(String color);
}
