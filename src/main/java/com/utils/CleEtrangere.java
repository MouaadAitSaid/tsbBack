package com.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation pour marquer un champ comme clé étrangère dans une entité.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CleEtrangere {
    Class<?> targetEntity(); // Classe de l'entité liée
    String entityName() default ""; // Nom de l'entité pour les messages d'erreur
}
