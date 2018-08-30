package com.nsv.guitarapp.repository;

import com.nsv.guitarapp.model.Model;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ModelJpaRepository extends JpaRepository<Model, Long> , ModelJpaRepositoryCustom{
    List<Model> findByPriceGreaterThanEqualAndPriceLessThanEqual(BigDecimal lowestPrice, BigDecimal highestPrice);
    List<Model> findByModelTypeNameIn(List<String> modelTypeNames);

    @Query("select m from Model m where m.price >= :lowest and m.price <= :highest and m.woodType like :wood")
    Page<Model> queryModelByLowestHighestAndWood(@Param("lowest") BigDecimal lowest,
                                                 @Param("highest") BigDecimal highest,
                                                 @Param("wood") String wood,
                                                 Pageable pageable);

    List<Model> findAllModelsByType(@Param("name") String name);

}