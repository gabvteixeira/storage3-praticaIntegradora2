package br.com.viana.storage3praticaintegradora2.repository;

import br.com.viana.storage3praticaintegradora2.model.Tutorial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TutorialRepo extends JpaRepository<Tutorial, Long> {
    List<Tutorial> findByTitleContaining(String title);
}
