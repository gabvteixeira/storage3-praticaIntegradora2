package br.com.viana.storage3praticaintegradora2.service;

import br.com.viana.storage3praticaintegradora2.exception.InvalidParamException;
import br.com.viana.storage3praticaintegradora2.exception.NotFoundException;
import br.com.viana.storage3praticaintegradora2.model.Status;
import br.com.viana.storage3praticaintegradora2.model.Tutorial;
import br.com.viana.storage3praticaintegradora2.repository.TutorialRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
public class TutorialServiceTest {

    @InjectMocks
    private TutorialService service;

    @Mock
    private TutorialRepo repo;

    private Tutorial tutorial;
    private List<Tutorial> tutorials;

    private List<Tutorial> filteredTutorial;

    @BeforeEach
    public void setup(){
        this.tutorial = new Tutorial(1L,"Tutorial 1", "test", Status.DRAFT);
        this.tutorials = new ArrayList<>(){{
            add(new Tutorial(1L,"Tutorial 1", "test", Status.DRAFT));
            add(new Tutorial(2L,"Tutorial 2", "test", Status.DRAFT));
            add(new Tutorial(3L,"Tutorial 3", "test", Status.PUBLISHED));
        }};

        this.filteredTutorial = new ArrayList<>(){{
            add(new Tutorial(3L,"Tutorial 3", "test", Status.PUBLISHED));
        }};
    }

    @Test
    public void getById_returnTutorial_withCorrectId(){
        Mockito.when(this.repo.findById(anyLong()))
                .thenReturn(Optional.of(this.tutorial));
        Tutorial response = this.service.getById(this.tutorial.getId());

        assertThat(response).isNotNull();
        assertThat(response.getTitle()).isEqualTo(this.tutorial.getTitle());
        assertThat(response.getId()).isEqualTo(this.tutorial.getId());
        assertThat(response.getDescription()).isEqualTo(this.tutorial.getDescription());
        assertThat(response.getStatus()).isEqualTo(this.tutorial.getStatus());
    }

    @Test
    public void getById_returnNotFoundException_withIncorrectId(){
        final long invalidId = 999;

        Mockito.when(this.repo.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            this.service.getById(invalidId);
        });
    }

    @Test
    public void get_returnAllTutorials_whenHaveTutorials(){
        Mockito.when(this.repo.findAll())
                .thenReturn(this.tutorials);

        List<Tutorial> response = this.service.get();

        assertThat(response).isNotNull();
        assertThat(response.size()).isEqualTo(this.tutorials.size());
    }

    @Test
    public void getByTitle_returnAllTutorials_whenTitleIsNull(){
        final String title = null;

        Mockito.when(this.repo.findAll())
                .thenReturn(this.tutorials);

        List<Tutorial> response = this.service.getByTitle(title);

        assertThat(response).isNotNull();
        assertThat(response.size()).isEqualTo(this.tutorials.size());
    }

    @Test
    public void getByTitle_returnFilteredTutorials_whenTitleIsNotNull(){
        final String title = "3";
        Mockito.when(this.repo.findAll())
                .thenReturn(this.tutorials);

        Mockito.when(this.repo.findByTitleContaining(anyString()))
                .thenReturn(this.filteredTutorial);

        List<Tutorial> response = this.service.getByTitle(title);

        assertThat(response).isNotNull();
        assertThat(response.size()).isEqualTo(this.filteredTutorial.size());
        assertThat(response.get(0).getTitle()).isEqualTo(this.filteredTutorial.get(0).getTitle());
    }

    @Test
    public void getPublished_returnPublishedTutorials(){
        Mockito.when(this.repo.findPublished())
                .thenReturn(this.filteredTutorial);

        List<Tutorial> response = this.service.getPublished();

        assertThat(response).isNotNull();
        assertThat(response.size()).isEqualTo(this.filteredTutorial.size());
        assertThat(response.get(0).getStatus()).isEqualTo(this.filteredTutorial.get(0).getStatus());
    }

    @Test
    public void deleteById_returnNotFoundException_withInvalidId(){
        final long invalidId = 999;

        Mockito.when(this.repo.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            this.service.deleteById(invalidId);
        });
    }

    @Test
    public void insert_returnTutorial_whenHasValidRequest(){
        Tutorial newTutorial = new Tutorial(null,"Tutorial 4", "teste",Status.DRAFT);

        Mockito.when(this.repo.save(any()))
                .thenReturn(newTutorial);

        Tutorial response = this.service.insert(newTutorial);

        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(Status.DRAFT);
        assertThat(response.getTitle()).isNotNull();
    }

    @Test
    public void insert_returnInvalidParam_whenRequestisNull(){
        Tutorial newTutorial = null;
        assertThrows(InvalidParamException.class, () -> {
            this.service.insert(newTutorial);
        });
    }

    @Test
    public void insert_returnInvalidParam_whenHasRequestWithId(){
        Tutorial newTutorial = new Tutorial(4L,"Tutorial 4", "teste",Status.DRAFT);
        assertThrows(InvalidParamException.class, () -> {
            this.service.insert(newTutorial);
        });
    }

    @Test
    public void insert_returnInvalidParam_whenRequestHasNullTitle(){
        Tutorial newTutorial = new Tutorial(null,null, "teste",Status.DRAFT);
        assertThrows(InvalidParamException.class, () -> {
            this.service.insert(newTutorial);
        });
    }

    @Test
    public void setPublished_returnTutorial_whenUpdate(){
        Mockito.when(this.repo.findById(this.tutorial.getId()))
                .thenReturn(Optional.of(this.tutorial));

        BDDMockito.doAnswer((invocation) -> {
            this.tutorial.setStatus(Status.PUBLISHED);
            return this.tutorial;
                })
                .when(this.repo).save(this.tutorial);

        Tutorial response = this.service.setPublished(this.tutorial.getId());

        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(Status.PUBLISHED);
    }

    @Test
    public void setPublished_returnNotFoundException_withInvalidId(){
        final long invalidId = 999;

        Mockito.when(this.repo.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            this.service.setPublished(invalidId);
        });
    }

    @Test
    public void update_returnTutorial_whenUpdateWithSuccess(){
        Tutorial tutorialUpdate = new Tutorial(null, "update", "update", Status.REVIEW);

        Mockito.when(this.repo.findById(anyLong()))
                .thenReturn(Optional.of(this.tutorial));

        BDDMockito.doAnswer((invocation) -> new Tutorial(this.tutorial.getId(), tutorialUpdate.getTitle(),
                tutorialUpdate.getDescription(), tutorialUpdate.getStatus()))
                        .when(this.repo).save(this.tutorial);

        Tutorial response = this.service.update(this.tutorial.getId(), tutorialUpdate);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(this.tutorial.getId());
        assertThat(response.getTitle()).isEqualTo(tutorialUpdate.getTitle());
        assertThat(response.getStatus()).isEqualTo(tutorialUpdate.getStatus());
        assertThat(response.getDescription()).isEqualTo(tutorialUpdate.getDescription());
    }

    @Test
    public void update_returnNotFoundException_withInvalidId(){
        final long invalidId = 999;

        Mockito.when(this.repo.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            this.service.update(invalidId, this.tutorial);
        });
    }
}
