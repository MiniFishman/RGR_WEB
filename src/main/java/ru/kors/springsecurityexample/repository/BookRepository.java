package ru.kors.springsecurityexample.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kors.springsecurityexample.models.Books;


@Repository
public interface BookRepository extends JpaRepository<Books, Integer> {
}
