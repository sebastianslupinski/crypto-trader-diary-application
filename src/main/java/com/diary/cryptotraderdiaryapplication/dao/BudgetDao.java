package com.diary.cryptotraderdiaryapplication.dao;

import com.diary.cryptotraderdiaryapplication.models.Budget;
import com.diary.cryptotraderdiaryapplication.models.Position;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public interface BudgetDao extends CrudRepository<Budget, Integer> {

    public List<Budget> findAll();
    public Budget findById(Integer id);

}
