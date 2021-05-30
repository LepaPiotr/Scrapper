package com.MongoApp.app.mongoRepos;

import com.MongoApp.app.entity.Search;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

public interface SearchRepository extends MongoRepository <Search, String> {

    public List<Search> findBySearchDateGreaterThanEqual(Date date);

}
