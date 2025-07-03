package com.thanhnt.productservice.domain.repository;

import com.thanhnt.productservice.domain.logs.LogDetail;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogDetailRepository extends MongoRepository<LogDetail, String> {}
