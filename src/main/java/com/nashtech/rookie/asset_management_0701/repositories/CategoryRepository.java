package com.nashtech.rookie.asset_management_0701.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nashtech.rookie.asset_management_0701.dtos.responses.ReportResponse;
import com.nashtech.rookie.asset_management_0701.entities.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByName (String categoryName);

    Optional<Category> findByCode (String categoryCode);

    @Query("select new com.nashtech.rookie.asset_management_0701.dtos.responses.ReportResponse " +
            "(c.id, c.name, count(a.id)," +
            "sum(case when a.state = 'ASSIGNED' then 1 else 0 end)," +
            "sum(case when a.state = 'AVAILABLE' then 1 else 0 end) ," +
            "sum(case when a.state = 'NOT_AVAILABLE' then 1 else 0 end)," +
            "sum(case when a.state = 'WAITING_FOR_RECYCLE' then 1 else 0 end)," +
            "sum(case when a.state = 'RECYCLED' then 1 else 0 end)) " +
            "from Category c " +
            "join Asset a " +
            "on c.id = a.category.id " +
            "group by c.id, c.name")
    Page<ReportResponse> getReport (Pageable pageable);

}
