package com.flysafe;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/*JpaRepository is provided by SpringBoot. Spring automatically generated SQL,
and we don't have to write manual SQL statements.
We have to pass the Entity class and the id.
 */
@Repository
  public interface SeatRepository extends JpaRepository<Seat, Long> {
    Optional<Seat> findByRowAndCol(int row, int col);

    /*
     * When we call the above method, Spring fires a SQL query.
     * H2 database finds exactly one matching row. Hibernate extracts the columns and builds a Seat object
       in java memory.
     * Spring passes this object to the above method.

    You get a non-null Empty box if row doesn't exist and a box with your seat of if it exists.
    This eliminates writing conditional statements.
     */
}


