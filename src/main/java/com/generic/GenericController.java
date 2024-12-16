package com.generic;

import com.utils.SearchRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Contrôleur générique pour gérer les APIs REST avec support des DTOs.
 *
 * @param <Entity>    Type de l'entité
 * @param <InputDTO>  Type du DTO pour les entrées (création/mise à jour)
 * @param <OutputDTO> Type du DTO pour les sorties (lecture)
 */
public abstract class GenericController<Entity, InputDTO, OutputDTO> {

    private final GenericService<Entity, InputDTO, OutputDTO> service;

    /**
     * Constructeur pour initialiser le service.
     *
     * @param service Service générique pour les entités
     */
    public GenericController(GenericService<Entity, InputDTO, OutputDTO> service) {
        this.service = service;
    }

    /**
     * API pour créer une nouvelle entité.
     *
     * @param dto DTO contenant les données de l'entité
     * @return DTO de sortie correspondant à l'entité créée
     */
    @PostMapping
    public ResponseEntity<OutputDTO> create(@RequestBody InputDTO dto) {
        OutputDTO output = service.create(dto);
        return ResponseEntity.ok(output);
    }

    /**
     * API pour récupérer une entité par ID.
     *
     * @param id Identifiant de l'entité
     * @return DTO de sortie si l'entité est trouvée
     */
    @GetMapping("/{id}")
    public ResponseEntity<OutputDTO> getById(@PathVariable Long id) {
        Optional<OutputDTO> output = service.getById(id);
        return output.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    /**
     * API pour récupérer toutes les entités.
     *
     * @return Iterable de DTOs de sortie
     */
    @GetMapping
    public ResponseEntity<Iterable<OutputDTO>> getAll() {
        Iterable<OutputDTO> outputs = service.getAll();
        return ResponseEntity.ok(outputs);
    }

    /**
     * API pour mettre à jour une entité existante.
     *
     * @param id  Identifiant de l'entité
     * @param dto DTO contenant les nouvelles données
     * @return DTO de sortie correspondant à l'entité mise à jour
     */
    @PutMapping("/{id}")
    public ResponseEntity<OutputDTO> update(@PathVariable Long id, @RequestBody InputDTO dto) {
        OutputDTO output = service.update(id, dto);
        return ResponseEntity.ok(output);
    }

    /**
     * API pour supprimer une entité par ID.
     *
     * @param id Identifiant de l'entité à supprimer
     * @return Une réponse sans contenu
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * API pour rechercher, filtrer et paginer des entités.
     *
     * @param searchRequest Objet contenant les critères de recherche, filtres et pagination
     * @return Une page contenant les DTOs de sortie correspondant aux résultats de la recherche
     */
    @PostMapping("/search")
    public ResponseEntity<Page<OutputDTO>> search(@RequestBody SearchRequest searchRequest) {
        // Récupérer les champs recherchables depuis la requête
        List<String> searchableFields = searchRequest.searchableFields();

        // Construire la spécification de recherche
        Specification<Entity> searchSpec = service.buildSearchSpecification(
                searchRequest.searchTerm(),
                searchableFields,
                searchRequest.filters()
        );

        // Appliquer la pagination et exécuter la recherche
        Page<OutputDTO> resultPage = service.search(
                searchSpec,
                PageRequest.of(searchRequest.page(), searchRequest.size())
        );

        // Retourner les résultats paginés
        return ResponseEntity.ok(resultPage);
    }
}
