package br.com.viana.storage3praticaintegradora2.service;

import br.com.viana.storage3praticaintegradora2.exception.InvalidParamException;
import br.com.viana.storage3praticaintegradora2.exception.NotFoundException;
import br.com.viana.storage3praticaintegradora2.model.Status;
import br.com.viana.storage3praticaintegradora2.model.Tutorial;
import br.com.viana.storage3praticaintegradora2.repository.TutorialRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class TutorialService implements ITutorial {

    @Autowired
    private TutorialRepo repo;

    @Override
    public Tutorial insert(Tutorial tutorial) {
        if(tutorial == null){
            throw new InvalidParamException("Tutorial can not be null");
        }

        if(tutorial.getTitle() == null){
            throw new InvalidParamException("Title can not be null");
        }

        if(tutorial.getId() != null){
            throw new InvalidParamException("Tutorial can not have an id");
        }

        tutorial.setStatus(Status.DRAFT);
        return this.repo.save(tutorial);
    }

    @Override
    public List<Tutorial> get() {
        return this.repo.findAll();
    }

    @Override
    public Tutorial getById(long id) {
        Optional<Tutorial> optionalTutorial = this.repo.findById(id);

        if(optionalTutorial.isEmpty()){
            throw new NotFoundException("Tutorial not found");
        }

        return optionalTutorial.get();
    }

    @Override
    public Tutorial update(long id, Tutorial tutorial) {
        Optional<Tutorial> optionalTutorial = this.repo.findById(id);

        if(optionalTutorial.isEmpty()) {
            throw new NotFoundException("Tutorial not found");
        }

        Tutorial newTutorial = optionalTutorial.get();

        if(tutorial.getTitle() != null){
            newTutorial.setTitle(tutorial.getTitle());
        }

        if(tutorial.getDescription() != null){
            newTutorial.setDescription(tutorial.getDescription());
        }

        if(tutorial.getStatus() != null) {
            newTutorial.setStatus(tutorial.getStatus());
        }

        return this.repo.save(newTutorial);
    }

    @Override
    public void delete() {
        this.repo.deleteAll();
    }

    @Override
    public void deleteById(long id) {
        Optional<Tutorial> optionalTutorial = this.repo.findById(id);
        if(optionalTutorial.isEmpty()){
            throw new NotFoundException("Tutorial not found");
        }

        this.repo.deleteById(id);
    }

    @Override
    public List<Tutorial> getPublished() {
        List<Tutorial> tutorials = this.repo.findAll();
        List<Tutorial> publishedTutorials =  tutorials.stream()
                .filter(tutorial -> tutorial.getStatus() == Status.PUBLISHED)
                .collect(Collectors.toList());

        return publishedTutorials;
    }

    @Override
    public List<Tutorial> getByTitle(String title) {
        List<Tutorial> tutorials = this.repo.findAll();

        if(title == null) {
            return tutorials;
        }

        List<Tutorial> filteredTutorials = this.repo.findByTitleContaining(title);

        return filteredTutorials;
    }

    @Override
    public Tutorial setPublished(long id) {
        Optional<Tutorial> optionalTutorial = this.repo.findById(id);

        if(optionalTutorial.isEmpty()) {
            throw new NotFoundException("Tutorial not found");
        }

        Tutorial newTutorial = optionalTutorial.get();
        newTutorial.setStatus(Status.PUBLISHED);

        return this.repo.save(newTutorial);
    }
}
