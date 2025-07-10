package org.llm_links.controller;

import org.llm_links.model.Entity;
import org.llm_links.repository.EntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller // Marks this class as a Spring MVC controller
//@RequiredArgsConstructor // Lombok: Generates a constructor with required (final) fields
public class EntityController {

    private final EntityRepository entityRepository;

    public EntityController(EntityRepository entityRepository) {
        this.entityRepository = entityRepository;
    }



    // Handles GET requests to the root path ("/").  Displays the form.
    @GetMapping("/")
    public String showForm(Model model) {
        model.addAttribute("entity", new Entity()); // Create an empty Entity object for the form
        return "form"; // Return the name of the Thymeleaf template (form.html)
    }

    // Handles POST requests to "/save".  Saves the entity to Redis.
    @PostMapping("/save")
    public String saveEntity(@ModelAttribute Entity entity) {
        // Generate a UUID for the ID if it's not already set
        if (entity.getId() == null || entity.getId().isEmpty()) {
            entity.setId(UUID.randomUUID().toString());
        }
        entityRepository.save(entity);
        return "redirect:/entities"; // Redirect to the entities list page after saving
    }


    // Handles GET requests to "/entities".  Displays the list of entities.
    @GetMapping("/entities")
    public String listEntities(Model model) {
        Iterable<Entity> entities = entityRepository.findAll(); // Get all entities from Redis
        model.addAttribute("entities", entities); // Add the list of entities to the model
        return "entities"; // Return the name of the Thymeleaf template (entities.html)
    }

    // Optional:  Delete entity by ID
    @GetMapping("/delete/{id}")
    public String deleteEntity(@PathVariable String id) {
        entityRepository.deleteById(id);
        return "redirect:/entities";
    }
}
