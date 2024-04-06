package com.hundredcommits.messengerx.factories;

import com.hundredcommits.messengerx.domains.PersistentNotifyingEntity;
import com.hundredcommits.messengerx.repositories.PersistentNotifyingEntityRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class PersistentNotifyingEntityRepositoryFactoryImpl implements PersistentNotifyingEntityRepositoryFactory {

    private final Map<Class<? extends PersistentNotifyingEntity>, PersistentNotifyingEntityRepository<?>> repositoryMap;

    public PersistentNotifyingEntityRepositoryFactoryImpl(Map<Class<? extends PersistentNotifyingEntity>, PersistentNotifyingEntityRepository<?>> repositoryMap) {
        this.repositoryMap = repositoryMap;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends PersistentNotifyingEntity> PersistentNotifyingEntityRepository<T> getRepositoryForEntity(Class<T> entityType) {
        return (PersistentNotifyingEntityRepository<T>) repositoryMap.get(entityType);
    }

    public List<PersistentNotifyingEntityRepository<?>> getAllRepositories() {
        return new ArrayList<>(repositoryMap.values());
    }
}
