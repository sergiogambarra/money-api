package com.gambarra.money.api.resource;

import com.gambarra.money.api.event.ResourceCreatedEvent;
import com.gambarra.money.api.repository.CategoryRepository;
import com.gambarra.money.api.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/categorias")
public class CategoryResource {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ApplicationEventPublisher publisher;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_PESQUISAR_CATEGORIA') and #oauth2.hasScope('read')")
    public List<Category> index() {
        return categoryRepository.findAll();
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_CATEGORIA') and #oauth2.hasScope('write')")
    public ResponseEntity<Category> create(@Valid @RequestBody Category category, HttpServletResponse response) {
        Category categorySaved = categoryRepository.save(category);
        publisher.publishEvent(new ResourceCreatedEvent(this, response, categorySaved.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(categorySaved);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_PESQUISAR_CATEGORIA') and #oauth2.hasScope('read')")
    public Object show(@PathVariable Long id){
        Optional<Category> category = categoryRepository.findById(id);
        return category.isPresent() ? ResponseEntity.ok(category.get()) : ResponseEntity.notFound().build();
    }
    
}
