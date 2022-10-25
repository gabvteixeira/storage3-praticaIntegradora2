package br.com.viana.storage3praticaintegradora2.controller;

import br.com.viana.storage3praticaintegradora2.model.Tutorial;
import br.com.viana.storage3praticaintegradora2.service.ITutorial;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tutorials")
public class TutorialController {

    @Autowired
    private ITutorial service;

    @GetMapping
    public ResponseEntity<List<Tutorial>> get(@RequestParam Optional<String> title){
        if(title.isPresent()){
            return new ResponseEntity<>(this.service.getByTitle(title.get()), HttpStatus.OK);
        }

        return new ResponseEntity<>(this.service.get(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tutorial> getById(@PathVariable long id){
        return new ResponseEntity<>(this.service.getById(id), HttpStatus.OK);
    }

    @GetMapping("/published")
    public ResponseEntity<List<Tutorial>> getPublished(){
        return new ResponseEntity<>(this.service.getPublished(), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAll(){
        this.service.delete();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable long id){
        this.service.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tutorial> update(@PathVariable long id, @RequestBody Tutorial tutorial){
        Tutorial updatedTutorial = this.service.update(id, tutorial);
        return new ResponseEntity<>(updatedTutorial, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Tutorial> insert(@RequestBody Tutorial tutorial){
        Tutorial newTutorial = this.service.insert(tutorial);
        return new ResponseEntity<>(newTutorial, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/published")
    public ResponseEntity<Tutorial> setPublished(@PathVariable long id){
        Tutorial updatedTutorial = this.service.setPublished(id);
        return new ResponseEntity<>(updatedTutorial, HttpStatus.OK);
    }

}
