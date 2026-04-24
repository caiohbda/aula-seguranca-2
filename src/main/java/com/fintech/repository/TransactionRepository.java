package com.fintech.repository;

import com.fintech.model.Account;
import com.fintech.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t WHERE t.sender = :account OR t.receiver = :account ORDER BY t.createdAt DESC")
    List<Transaction> findAllByAccount(@Param("account") Account account);
}
