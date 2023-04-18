package de.telran.todojpa.controller;

import de.telran.todojpa.entity.ToDo;
import de.telran.todojpa.repository.ToDoRepository;
import de.telran.todojpa.validation.ToDoValidationError;
import de.telran.todojpa.validation.ToDoValidationErrorBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ToDoController {

    private ToDoRepository<ToDo> repository;

    @Autowired
    public ToDoController(ToDoRepository<ToDo> repository) {

        this.repository = repository;
    }

    //find and get all ToDos
    @GetMapping("/todos")
    public ResponseEntity<Iterable<ToDo>> getTodos() {
        return ResponseEntity.ok(repository.findAll());
    }

    //find by id
    @GetMapping("/todo/{id}")
    public ResponseEntity<ToDo> getTodoById(@PathVariable String id) {
        Optional<ToDo> toDo = repository.findById(id);
//        if (toDo.isPresent()) {
//            return ResponseEntity.ok(toDo.get());
//        } else {
//            return ResponseEntity.notFound().build();
//        }
        return toDo.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    //find by key (id)
    @GetMapping("/todo")
    public ResponseEntity<ToDo> getTodoById2(@RequestParam String id) {
        Optional<ToDo> toDo = repository.findById(id);
        return toDo.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    //this method allows to create and update
    @RequestMapping(value = "/todo", method = {RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<?> createTodo(@Valid @RequestBody ToDo toDo, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(ToDoValidationErrorBuilder.fromBindingErrors(errors));
        }
        ToDo result = repository.save(toDo);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/todo/{id}")
    public ResponseEntity<?> deleteById(@PathVariable String id) {
        Optional<ToDo> optionalToDo = repository.findById(id);
        if (optionalToDo.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        repository.delete(optionalToDo.get());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/todo")
    public ResponseEntity<?> deleteToDo(@RequestBody ToDo toDo) {
        Optional<ToDo> optionalToDo = repository.findById(toDo.getId());
        if (optionalToDo.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        repository.delete(optionalToDo.get());
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ToDoValidationError handleException(Exception e) {
        return new ToDoValidationError((e.getMessage()));
    }

}
