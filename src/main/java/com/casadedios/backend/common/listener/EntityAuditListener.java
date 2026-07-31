package com.casadedios.backend.common.listener;

import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.time.Instant;

@Slf4j
public class EntityAuditListener {

    private static final String CREATED_AT_FIELD = "createdAt";
    private static final String UPDATED_AT_FIELD = "updatedAt";

    @PrePersist
    public void prePersist(Object entity) {
        Instant now = Instant.now();
        setFieldIfPresent(entity, CREATED_AT_FIELD, now);
        setFieldIfPresent(entity, UPDATED_AT_FIELD, now);
        log.debug("Creando entidad: {}", entity.getClass().getSimpleName());
    }

    @PreUpdate
    public void preUpdate(Object entity) {
        setFieldIfPresent(entity, UPDATED_AT_FIELD, Instant.now());
        log.debug("Actualizando entidad: {}", entity.getClass().getSimpleName());
    }

    @PostPersist
    public void postPersist(Object entity) {
        log.info("Entidad creada: {}", entity.getClass().getSimpleName());
    }

    @PostRemove
    public void postRemove(Object entity) {
        log.warn("Entidad eliminada: {}", entity.getClass().getSimpleName());
    }

    private void setFieldIfPresent(Object entity, String fieldName, Instant value) {
        try {
            BeanWrapper beanWrapper = new BeanWrapperImpl(entity);
            if (beanWrapper.isWritableProperty(fieldName)) {
                beanWrapper.setPropertyValue(fieldName, value);
            } else {
                log.debug("La entidad {} no tiene la propiedad '{}', se omite",
                        entity.getClass().getSimpleName(), fieldName);
            }
        } catch (Exception e) {
            log.error("No se pudo asignar la propiedad '{}' en {}",
                    fieldName, entity.getClass().getSimpleName(), e);
        }
    }
}

