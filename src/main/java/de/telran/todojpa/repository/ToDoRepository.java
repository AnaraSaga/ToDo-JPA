package de.telran.todojpa.repository;

import de.telran.todojpa.entity.ToDo;
import org.springframework.data.repository.CrudRepository;

public interface ToDoRepository<T> extends CrudRepository<ToDo, String>{

}
