package com.example.medcare.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.medcare.dto.PersonCreationDTO;
import com.example.medcare.dto.PersonResponseDTO;
import com.example.medcare.mappers.PersonMapper;
import com.example.medcare.models.Person;
import com.example.medcare.services.PersonService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("person")
public class PersonController {

    @Autowired
    private PersonService service;

    @Autowired
    private PersonMapper mapper;
    
    @PostMapping("/create")
    public ResponseEntity<PersonResponseDTO> createPerson(@Valid @RequestBody PersonCreationDTO personCreationDto) {
        try {
            
            //retorna a entidade Salva
            Person entity = service.createPerson(personCreationDto);
            //CONSTRUÇÃO DA URI: Usa o ID do objeto salvo para criar o link do novo recurso
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(entity.getId())
                    .toUri();
            //Mapeando a entidade salva para o responseDto
            PersonResponseDTO responseDto = mapper.toDtoResponse(entity);
            //Retorna 201 created com a URI
            return ResponseEntity.created(location).body(responseDto);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonResponseDTO> findPersonById(@PathVariable long id) {
        
        //Pegando o objeto person do service
        Person entity = service.findPersonById(id);
        //Transformando em DTO de resposta
        PersonResponseDTO responseDto = mapper.toDtoResponse(entity);
        //Mandando o DTO
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/all")
    public ResponseEntity<List<PersonResponseDTO>> findAll() {
        //Pegando a lista de pessoas do service
        List<Person> persons = service.findAll();
        //Transformando a lista em responseDto
        List<PersonResponseDTO> personResponseDTOs = mapper.toDtoResponse(persons);
        //Retornando a lista
        return ResponseEntity.ok(personResponseDTOs);

    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonResponseDTO> updatePerson(@PathVariable long id,@Valid @RequestBody PersonCreationDTO person) {
        
        //Chama o service para executar o update
        Person updatedEntity = service.updatePerson(id, person);
        //Mapeia a entidade atualizada para o DTO de resposta
        PersonResponseDTO personResponseDTO = mapper.toDtoResponse(updatedEntity);
        //Retorna 200 OK e o corpo atualizado
        return ResponseEntity.ok(personResponseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable long id){
        service.deletePerson(id);

        return ResponseEntity.noContent().build();
    }

}
