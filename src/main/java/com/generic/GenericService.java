package com.generic;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service générique pour gérer les opérations CRUD avec support des DTOs.
 *
 * @param <Entity>    Type de l'entité
 * @param <InputDTO>  Type du DTO pour les entrées (création/mise à jour)
 * @param <OutputDTO> Type du DTO pour les sorties (lecture)
 */
public abstract class GenericService<Entity, InputDTO, OutputDTO> {

    private final GenericRepository<Entity> repository;
    private final EntityMapper<Entity, InputDTO, OutputDTO> mapper;

    @Autowired
    private EntityManager entityManager;

    /**
     * Constructeur pour initialiser le repository.
     *
     * @param repository Repository générique pour les entités
     * @param mapper     Mapper pour convertir entre entités et DTOs
     */
    public GenericService(GenericRepository<Entity> repository, EntityMapper<Entity, InputDTO, OutputDTO> mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    /**
     * Crée une nouvelle entité à partir d'un DTO d'entrée.
     *
     * @param dto DTO contenant les informations de l'entité
     * @return DTO de sortie correspondant à l'entité créée
     */
    public OutputDTO create(InputDTO dto) {
        Entity entity = mapper.toEntity(dto);

        // Gère automatiquement les clés étrangères
        processForeignKeys(entity);

        Entity savedEntity = repository.save(entity); // Sauvegarde en base de données
        return mapper.toOutputDTO(savedEntity); // Conversion de l'entité sauvegardée en DTO de sortie
    }

    /**
     * Gère automatiquement les clés étrangères d'une entité en les associant à partir de leurs IDs.
     *
     * @param entity L'entité à traiter
     */
    private void processForeignKeys(Entity entity) {
        for (Field field : entity.getClass().getDeclaredFields()) {
            field.setAccessible(true);

            // Vérifie si le champ est une clé étrangère (par convention : type Long et nom se terminant par "Id")
            if (isForeignKeyField(field)) {
                try {
                    Object foreignKeyValue = field.get(entity); // Récupère la valeur de l'ID (ex: userId -> 1)
                    if (foreignKeyValue != null) {
                        // Détermine l'entité cible (ex: userId -> User.class)
                        Class<?> targetEntity = getTargetEntityFromField(field);

                        // Charge l'entité cible correspondante
                        Object relatedEntity = findRelatedEntityById(targetEntity, (Long) foreignKeyValue, targetEntity.getSimpleName());

                        // Associe l'entité cible à l'entité principale (ex: "setUser" pour "user")
                        String relatedFieldName = getRelatedFieldName(field.getName());
                        Field relatedField = entity.getClass().getDeclaredField(relatedFieldName);
                        relatedField.setAccessible(true);
                        relatedField.set(entity, relatedEntity);
                    }
                } catch (Exception e) {
                    throw new RuntimeException("Failed to process foreign key for field: " + field.getName(), e);
                }
            }
        }
    }

    /**
     * Vérifie si un champ représente une clé étrangère.
     * Par convention, une clé étrangère est un champ de type Long dont le nom se termine par "Id".
     *
     * @param field Le champ à vérifier
     * @return true si c'est une clé étrangère, sinon false
     */
    private boolean isForeignKeyField(Field field) {
        return field.getType().equals(Long.class) && field.getName().endsWith("Id");
    }

    /**
     * Détermine la classe de l'entité cible à partir du nom du champ.
     * Exemple : "userId" -> User.class
     *
     * @param field Le champ représentant la clé étrangère
     * @return La classe de l'entité cible
     */
    private Class<?> getTargetEntityFromField(Field field) {
        String entityName = field.getName().substring(0, field.getName().length() - 2); // Supprime "Id"
        try {
            return Class.forName("com.tsp.models." + capitalize(entityName)); // Adaptez le package si nécessaire
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Target entity class not found for field: " + field.getName(), e);
        }
    }

    /**
     * Détermine le nom du champ de relation à partir du nom du champ de clé étrangère.
     * Exemple : "userId" -> "user"
     *
     * @param foreignKeyFieldName Le nom du champ de clé étrangère
     * @return Le nom du champ de relation correspondant
     */
    private String getRelatedFieldName(String foreignKeyFieldName) {
        if (foreignKeyFieldName.endsWith("Id")) {
            return foreignKeyFieldName.substring(0, foreignKeyFieldName.length() - 2);
        }
        throw new IllegalArgumentException("Cannot determine related field name for: " + foreignKeyFieldName);
    }

    /**
     * Capitalise une chaîne (ex : "user" -> "User").
     *
     * @param str La chaîne à capitaliser
     * @return La chaîne capitalisée
     */
    private String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    // Méthodes existantes...
    public Optional<OutputDTO> getById(Long id) {
        return repository.findById(id).map(mapper::toOutputDTO);
    }

    public Iterable<OutputDTO> getAll() {
        return repository.findAll().stream()
                .map(mapper::toOutputDTO)
                .collect(Collectors.toList());
    }

    public OutputDTO update(Long id, InputDTO dto) {
        Optional<Entity> existingEntity = repository.findById(id);
        if (existingEntity.isEmpty()) {
            throw new IllegalArgumentException("Entity with ID " + id + " not found.");
        }

        Entity updatedEntity = mapper.toEntity(dto);
        processForeignKeys(updatedEntity); // Gère aussi les clés étrangères en mise à jour
        repository.save(updatedEntity);
        return mapper.toOutputDTO(updatedEntity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<OutputDTO> search(Specification<Entity> spec, Pageable pageable) {
        return repository.findAll(spec, pageable).map(mapper::toOutputDTO);
    }

    protected <R> R findRelatedEntityById(Class<R> entityType, Long id, String entityName) {
        if (id == null) {
            throw new IllegalArgumentException(entityName + " ID cannot be null.");
        }
        return entityManager.find(entityType, id);
    }

    /**
     * Construit une spécification de recherche basée sur un terme global, des champs spécifiques et des filtres.
     *
     * @param searchTerm       Terme global de recherche
     * @param searchableFields Champs dans lesquels effectuer la recherche globale
     * @param filters          Map de filtres spécifiques
     * @return Spécification combinant recherche globale et filtres
     */
    public Specification<Entity> buildSearchSpecification(String searchTerm, List<String> searchableFields, Map<String, Object> filters) {
        return (root, query, criteriaBuilder) -> {
            // Recherche globale : crée un prédicat de type OR
            Predicate globalSearchPredicate = criteriaBuilder.conjunction();
            if (searchTerm != null && !searchTerm.trim().isEmpty() && searchableFields != null && !searchableFields.isEmpty()) {
                globalSearchPredicate = criteriaBuilder.or(
                        searchableFields.stream()
                                .map(field -> criteriaBuilder.like(
                                        criteriaBuilder.lower(root.get(field)), // Champ recherché (en minuscules)
                                        "%" + searchTerm.toLowerCase() + "%" // Terme recherché entouré de wildcards
                                ))
                                .toArray(Predicate[]::new)
                );
            }

            // Filtres spécifiques : crée un prédicat de type AND
            Predicate filterPredicate = criteriaBuilder.conjunction();
            if (filters != null && !filters.isEmpty()) {
                filterPredicate = criteriaBuilder.and(
                        filters.entrySet().stream()
                                .filter(entry -> {
                                    // Vérifie si l'attribut existe dans l'entité pour éviter les erreurs
                                    try {
                                        root.get(entry.getKey());
                                        return true;
                                    } catch (IllegalArgumentException e) {
                                        return false; // Attribut inexistant
                                    }
                                })
                                .map(entry -> criteriaBuilder.equal(root.get(entry.getKey()), entry.getValue())) // Condition exacte
                                .toArray(Predicate[]::new)
                );
            }

            // Combine les prédicats de recherche globale et de filtres
            return criteriaBuilder.and(globalSearchPredicate, filterPredicate);
        };
    }
}
