package com.example.demo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.Student;
@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
 
}