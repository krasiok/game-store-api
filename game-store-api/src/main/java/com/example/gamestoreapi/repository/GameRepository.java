package com.example.gamestoreapi.repository;

import com.example.gamestoreapi.model.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

    @Query("SELECT g FROM Game g WHERE " +
            "(:title IS NULL OR LOWER(g.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND " +
            "(:minPrice IS NULL OR g.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR g.price <= :maxPrice) AND " +
            "(:developerId IS NULL OR g.developer.id = :developerId)")
    List<Game> searchGames(
            @Param("title") String title,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("developerId") Long developerId
    );
}