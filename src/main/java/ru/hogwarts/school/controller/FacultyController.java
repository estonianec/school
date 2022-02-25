package ru.hogwarts.school.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;

@RestController
@RequestMapping("faculty")
public class FacultyController {
    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }
    @PostMapping
    public Faculty createFaculty(@RequestBody Faculty faculty) {
        return facultyService.createFaculty(faculty);
    }
    @PutMapping
    public ResponseEntity<Faculty> editFaculty(@RequestBody Faculty faculty) {
        Faculty entity = facultyService.editFaculty(faculty);
        if (entity == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(entity);
    }
    @GetMapping("{id}")
    public ResponseEntity<Faculty> getFaculty(@PathVariable("id") long id) {
        Faculty entity = facultyService.getFaculty(id);
        if (entity == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(entity);
    }
    @DeleteMapping("{id}")
    public ResponseEntity<Faculty> deleteFaculty(@PathVariable("id") long id) {
        facultyService.deleteFaculty(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/", params = {"filterByColor"})
    public ResponseEntity<Collection<Faculty>> filterByColor(@RequestParam(value = "filterByColor", required = false) String color) {
        Collection<Faculty> filteredFaculties = facultyService.filterByColor(color);
        if (filteredFaculties.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(filteredFaculties);
    }

    @GetMapping(value = "/", params = {"colorOrName"})
    public ResponseEntity<Collection<Faculty>> findByColorIgnoreCaseOrNameIgnoreCase(@RequestParam(value = "colorOrName", required = false) String colorOrName) {
        Collection<Faculty> filteredFaculties = facultyService.findByColorIgnoreCaseOrNameIgnoreCase(colorOrName, colorOrName);
        if (filteredFaculties.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(filteredFaculties);
    }

    @GetMapping(value = "/", params = {"filterByColor", "colorOrName"})
    public ResponseEntity<Collection<Faculty>> filterByColorAndFindByColorIgnoreCaseOrNameIgnoreCase(@RequestParam(value = "filterByColor", required = false) String color, @RequestParam(value = "colorOrName", required = false) String colorOrName) {
        Collection<Faculty> filteredFaculties = facultyService.filterByColor(color);
        if (filteredFaculties.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(filteredFaculties);
    }
}
