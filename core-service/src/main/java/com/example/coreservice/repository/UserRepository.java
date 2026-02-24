package com.example.coreservice.repository;

import com.example.coreservice.entity.UserEntity;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<UserEntity, String> {

    /**
     * Optimized query to find a user by username using aggregation.
     * Demonstrates how to encapsulate optimized queries in the repository layer.
     */
    @Aggregation(pipeline = {
            "{ '$match': { 'username': ?0 } }",
            "{ '$limit': 1 }"
    })
    UserEntity findByUsernameOptimized(String username);

    /**
     * Demonstration of preventing N+1 equivalent in MongoDB by using $lookup
     * (e.g., if users had roles in a separate collection).
     */
    @Aggregation(pipeline = {
            "{ '$lookup': { 'from': 'roles', 'localField': 'roleIds', 'foreignField': '_id', 'as': 'roles' } }",
            "{ '$project': { 'username': 1, 'roles': 1 } }"
    })
    List<UserEntity> findAllWithDetailsOptimized();
}
