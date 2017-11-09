package com.alice.emily.data.mongodb.support;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;

import javax.annotation.Nonnull;

/**
 * {@link MongoEntityInformation} implementation wrapping an existing {@link MongoEntityInformation} considering
 * {@link Persistable} types by delegating {@link #isNew(Object)} and {@link #getId(Object)} to the corresponding
 * {@link Persistable#isNew()} and {@link Persistable#getId()} implementations.
 *
 * @author Christoph Strobl
 * @author Oliver Gierke
 * @since 1.10
 */
@RequiredArgsConstructor
class PersistableMongoEntityInformation<T, ID> implements MongoEntityInformation<T, ID> {

    private final @NonNull MongoEntityInformation<T, ID> delegate;

    @Override
    public String getCollectionName() {
        return delegate.getCollectionName();
    }

    @Override
    public String getIdAttribute() {
        return delegate.getIdAttribute();
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean isNew(@Nonnull T t) {

        if (t instanceof Persistable) {
            return ((Persistable<ID>) t).isNew();
        }

        return delegate.isNew(t);
    }

    @Override
    @SuppressWarnings("unchecked")
    public ID getId(@Nonnull T t) {

        if (t instanceof Persistable) {
            return ((Persistable<ID>) t).getId();
        }

        return delegate.getId(t);
    }

    @Nonnull
    @Override
    public Class<ID> getIdType() {
        return delegate.getIdType();
    }

    @Nonnull
    @Override
    public Class<T> getJavaType() {
        return delegate.getJavaType();
    }
}