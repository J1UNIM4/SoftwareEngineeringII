package com.finance.project.applicationLayer.applicationServices.personServices;

import com.finance.project.applicationLayer.ErrorMessages;
import com.finance.project.domainLayer.exceptions.InvalidArgumentsBusinessException;
import com.finance.project.domainLayer.exceptions.NotFoundArgumentsBusinessException;
import com.finance.project.domainLayer.repositoriesInterfaces.ICategoryRepository;
import com.finance.project.domainLayer.repositoriesInterfaces.IPersonRepository;
import com.finance.project.dtos.dtosAssemblers.PersonDTOAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.finance.project.dtos.dtos.CreatePersonCategoryDTO;
import com.finance.project.dtos.dtos.PersonDTO;
import com.finance.project.domainLayer.domainEntities.aggregates.person.Person;
import com.finance.project.domainLayer.domainEntities.vosShared.CategoryID;
import com.finance.project.domainLayer.domainEntities.vosShared.PersonID;

import java.util.Optional;


@Service
public class CreatePersonCategoryService {

    @Autowired
    private IPersonRepository personRepository;
    @Autowired
    private ICategoryRepository categoryRepository;


    public CreatePersonCategoryService(IPersonRepository personRepository, ICategoryRepository categoryRepository) {
        this.personRepository = personRepository;
        this.categoryRepository = categoryRepository;
    }


    // Return messages

    public final static String SUCCESS               = ErrorMessages.CATEGORY_CREATED;
    public final static String CATEGORY_ALREADY_EXIST = ErrorMessages.CATEGORY_ALREADY_EXISTS;
    public final static String PERSON_DOES_NOT_EXIST = ErrorMessages.PERSON_DOES_NOT_EXIST;


    public PersonDTO createCategory(CreatePersonCategoryDTO createPersonCategoryDTO) {
        Person person;

        PersonID personID = PersonID.createPersonID(createPersonCategoryDTO.getEmail());
        Optional<Person> optPerson = personRepository.findById(personID);

        if (!optPerson.isPresent()) {

            throw new NotFoundArgumentsBusinessException(PERSON_DOES_NOT_EXIST);

        } else {
            person = optPerson.get();

            CategoryID categoryID = CategoryID.createCategoryID(createPersonCategoryDTO.getDenomination(), personID);
            boolean categoryExists = categoryRepository.existsById(categoryID);

            if (categoryExists) {

                throw new InvalidArgumentsBusinessException(CATEGORY_ALREADY_EXIST);

            } else {
                person.addCategory(categoryID);
                personRepository.addAndSaveCategory(person);
            }
        }
        return PersonDTOAssembler.createDTOFromDomainObject(
                person.getPersonID().getEmail(), person.getName(),
                person.getBirthdate(), person.getBirthplace(),
                person.getFather(), person.getMother());
    }
}
