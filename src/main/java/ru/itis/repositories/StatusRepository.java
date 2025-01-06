package ru.itis.repositories;

import ru.itis.models.Status;

public interface StatusRepository extends CrudRepository<Status>{
    Status findById(Long id) throws Exception;

}
