package com.diary.cryptotraderdiaryapplication.dao;

import com.diary.cryptotraderdiaryapplication.models.Position;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.Id;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public interface PositionDao extends CrudRepository<Position, Integer>{

    public List<Position> findAll();

    public Position findById(Integer id);

    public List<Position> findByOpen(boolean open);
}
