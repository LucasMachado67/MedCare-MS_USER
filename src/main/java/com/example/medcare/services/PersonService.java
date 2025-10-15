package com.example.medcare.services;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.medcare.dto.PersonCreationDTO;
import com.example.medcare.exceptions.CpfAlreadyExistsException;
import com.example.medcare.exceptions.EmailAlreadyExistsException;
import com.example.medcare.exceptions.InvalidCpfException;
import com.example.medcare.mappers.PersonMapper;
import com.example.medcare.models.Person;
import com.example.medcare.repositories.PersonRepository;
import com.example.medcare.utils.CpfValidatorUtils;

@Service
public class PersonService {

    @Autowired
    private PersonRepository repository;

    @Autowired
    private PersonMapper mapper;

    public Person createPerson(PersonCreationDTO dto){
        //Validação de CPF
        String cpfToValidate = dto.getCpf();
        
        //Validando se o email é unico
        if (repository.existsByEmail(dto.getEmail())) {
            throw new EmailAlreadyExistsException(""); 
        }
        
        // Validação matemática
        if (!CpfValidatorUtils.isValidCpf(cpfToValidate)) {
            throw new InvalidCpfException("O CPF é inválido. Verifique o formato ou os dígitos.");
        }

        // Validação de Unicidade (usando o CPF limpo)
        String cleanCpf = cpfToValidate.replaceAll("[^0-9]", "");
        if (repository.existsByCpf(cleanCpf)) {
            throw new CpfAlreadyExistsException(cleanCpf); 
        }


        Person person = mapper.toEntityCreation(dto);
        return repository.save(person);
    }

    public Person findPersonById(long id){
        return repository.findById(id).orElseThrow(() -> new NoSuchElementException("NOT FOUND"));
    }

    public List<Person> findAll(){
        return repository.findAll();
    }

    public Person updatePerson(long id, PersonCreationDTO personCreationDTO){
        Person existingPerson = findPersonById(id);

        mapper.updatePersonFromDto(personCreationDTO, existingPerson);

        return repository.save(existingPerson);
    }

    public void deletePerson(long id){
        Person personToDelete = repository.findById(id).get();

        repository.delete(personToDelete);
    }

    public Person findPersonByEmail(String email){
        return repository.findPersonByEmail(email);
    }

}
