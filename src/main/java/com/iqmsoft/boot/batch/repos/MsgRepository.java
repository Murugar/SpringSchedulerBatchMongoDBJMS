package com.iqmsoft.boot.batch.repos;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.iqmsoft.boot.batch.MyMessage;

public interface MsgRepository extends MongoRepository<MyMessage, String> {

}
