package br.com.viana.storage3praticaintegradora2.service;

import br.com.viana.storage3praticaintegradora2.model.Tutorial;

import java.util.List;

public interface ITutorial {
    Tutorial insert(Tutorial tutorial);
    List<Tutorial> get();
    Tutorial getById(long id);
    Tutorial update(long id, Tutorial tutorial);
    void delete();
    void deleteById(long id);
    List<Tutorial> getPublished();
    List<Tutorial> getByTitle(String title);
    Tutorial setPublished(long id);
}
