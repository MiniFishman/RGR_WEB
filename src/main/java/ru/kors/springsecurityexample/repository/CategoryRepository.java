package ru.kors.springsecurityexample.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kors.springsecurityexample.models.Categories;

@Repository
public interface CategoryRepository extends JpaRepository<Categories, Integer> {
}
