package com.tananushka.taskmanager.service;

import com.tananushka.taskmanager.model.DatabaseSequence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class SequenceGeneratorService {

   private final MongoOperations mongoOperations;

   @Autowired
   public SequenceGeneratorService(MongoOperations mongoOperations) {
      this.mongoOperations = mongoOperations;
   }

   public int generateSequence(String seqName) {
      DatabaseSequence counter = mongoOperations.findAndModify(
            Query.query(Criteria.where("_id").is(seqName)),
            new Update().inc("seq", 1),
            FindAndModifyOptions.options().returnNew(true).upsert(true),
            DatabaseSequence.class);
      return !Objects.isNull(counter) ? counter.getSeq() : 1;
   }
}
