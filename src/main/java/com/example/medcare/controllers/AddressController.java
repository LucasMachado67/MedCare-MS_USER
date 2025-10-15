package com.example.medcare.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.medcare.dto.AddressDTO;
import com.example.medcare.dto.AddressResponseDTO;
import com.example.medcare.mappers.AddressMapper;
import com.example.medcare.models.Address;
import com.example.medcare.services.AddressService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("address")
public class AddressController {

    @Autowired
    private AddressService service;

    @Autowired
    private AddressMapper mapper;

    @PostMapping("/create")
    public ResponseEntity<AddressResponseDTO> createAddress(@Valid @RequestBody AddressDTO addressDTO) {
        try {
            //retornar a Entidade SALVA
            Address entityAddress = service.createAddress(addressDTO);
            //CONSTRUÇÃO DA URI: Usa o ID do objeto salvo para criar o link do novo recurso
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(entityAddress.getId())
                    .toUri();
            //Mapeando a Entidade salva para o responseDto
            AddressResponseDTO responseDTO = mapper.toDtoResponse(entityAddress);
            //Retorna 201 Created com a URI
            return ResponseEntity.created(location).body(responseDTO);

        } catch (Exception e) {
           return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<AddressResponseDTO> findAddressById(@PathVariable long id) {
        //Pegando o objeto address do service
        Address entity = service.findAddressById(id);
        //Transformando em DTO
        AddressResponseDTO responseDto = mapper.toDtoResponse(entity);
        //mandando o DTO
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/all")
    public ResponseEntity<List<AddressResponseDTO>> findAll() {

        //Pegando a lista de address com o service
        List<Address> addresses = service.findAll();
        //Transformando a lista em responseDto
        List<AddressResponseDTO> addressResponses = mapper.toDtoResponse(addresses);
        //Retornando response
        return ResponseEntity.ok(addressResponses);
        
    }

    @PutMapping("/{id}")
    public ResponseEntity<AddressResponseDTO> updateAddress(@PathVariable long id, @Valid @RequestBody AddressDTO addressDTO) {

        //Controller chama o Service para executar o update
        Address updatedEntity = service.updateAddress(id, addressDTO);
        //Mapeia a Entidade atualizada para o DTO de Resposta
        AddressResponseDTO responseDTO = mapper.toDtoResponse(updatedEntity);
        //Retorna 200 OK e o corpo atualizado
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable long id){

        service.deleteAddress(id);

        return ResponseEntity.noContent().build();
    }
    
    
    
}
