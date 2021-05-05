package com.MongoApp.app.mongoRepos;

import com.MongoApp.app.entity.Search;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface Searchrepository extends MongoRepository <Search, String> {

}
