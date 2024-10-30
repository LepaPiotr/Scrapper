package com.mongo.app.repository;

import com.mongo.app.entity.Search;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

public interface SearchRepository extends MongoRepository<Search, String> {

    List<Search> findBySearchDateGreaterThanEqual(Date date);

}
