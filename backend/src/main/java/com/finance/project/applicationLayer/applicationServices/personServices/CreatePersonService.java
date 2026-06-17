package com.finance.project.applicationLayer.applicationServices.personServices;

import com.finance.project.domainLayer.domainEntities.aggregates.person.*;
import com.finance.project.domainLayer.domainEntities.vosShared.*;
import com.finance.project.domainLayer.exceptions.InvalidArgumentsBusinessException;
import com.finance.project.domainLayer.exceptions.NotFoundArgumentsBusinessException;
import com.finance.project.domainLayer.repositoriesInterfaces.*;
import com.finance.project.dtos.dtos.*;
import com.finance.project.dtos.dtosAssemblers.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.finance.project.domainLayer.domainEntities.aggregates.account.Account;
import com.finance.project.domainLayer.domainEntities.aggregates.category.Category;
import com.finance.project.domainLayer.domainEntities.aggregates.group.Group;
import com.finance.project.domainLayer.domainEntities.aggregates.ledger.Ledger;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class CreatePersonService {

    private final IPersonRepository personRepository;
    private final ILedgerRepository ledgerRepository;
    private final ICategoryRepository categoryRepository;
    private final IAccountRepository accountRepository;
    private final IGroupRepository groupRepository;

    public static final String SUCCESS = "Account created and added";
    public static final String ADDRESS_ALREADY_EXIST = "Address already exists";
    public static final String MOTHER_ALREADY_EXIST = "Mother already exists";
    public static final String FATHER_ALREADY_EXIST = "Father already exists";
    public static final String SIBLING_ALREADY_EXIST = "Sibling already exists";
    public static final String ACCOUNT_ALREADY_EXIST = "Account already exists";
    public static final String CATEGORY_ALREADY_EXIST = "Category already exists";
    public static final String PERSON_DOES_NOT_EXIST = "Person does not exist";
    public static final String PERSON_ALREADY_EXIST = "Person already exists";
    public static final String ACCOUNT_DOES_NOT_EXIST = "Account does not exist";
    public static final String CATEGORY_DOES_NOT_EXIST = "Category does not exist";
    public static final String LEDGER_DOES_NOT_EXIST = "Ledger does not exist";
    public static final String TRANSACTION_ALREADY_EXIST = "Transaction already exist";

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    public CreatePersonService(IPersonRepository personRepository,
                               ILedgerRepository ledgerRepository,
                               ICategoryRepository categoryRepository,
                               IAccountRepository accountRepository,
                               IGroupRepository groupRepository) {
        this.personRepository = personRepository;
        this.ledgerRepository = ledgerRepository;
        this.categoryRepository = categoryRepository;
        this.accountRepository = accountRepository;
        this.groupRepository = groupRepository;
    }


    // -------------------------------------------------------------------------
    // Person CRUD
    // -------------------------------------------------------------------------

    public PersonDTO createPerson(CreatePersonDTO createPersonDTO) {
        PersonID personID = PersonID.createPersonID(createPersonDTO.getEmail());

        if (personRepository.findById(personID).isPresent()) {
            throw new InvalidArgumentsBusinessException(PERSON_ALREADY_EXIST);
        }

        Person newPerson = Person.createPerson(
                createPersonDTO.getEmail(),
                createPersonDTO.getName(),
                createPersonDTO.getBirthdate(),
                createPersonDTO.getBirthplace());

        Person saved = personRepository.save(newPerson);
        return toPersonDTO(saved);
    }

    public CreatePersonDTO createAndSavePerson(CreatePersonDTO createPersonDTO) {
        Person newPerson = Person.createPerson(
                createPersonDTO.getEmail(),
                createPersonDTO.getName(),
                LocalDate.parse(createPersonDTO.getBirthdate().toString()),
                createPersonDTO.getBirthplace());

        Person saved = personRepository.save(newPerson);

        return CreatePersonDTOAssembler.createDTOFromPrimitiveTypes(
                saved.getEmail().getEmail(),
                saved.getName().getName(),
                saved.getBirthdate().getBirthdate().format(DATE_FORMATTER),
                saved.getBirthplace().getBirthplace());
    }


    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    public PersonDTO getPersonByEmail(PersonEmailDTO personEmailDTO) {
        Person person = findPersonOrThrow(personEmailDTO.getEmail());
        return toPersonDTO(person);
    }

    @Transactional
    public TransactionsDTO getPersonLedger(PersonEmailDTO personEmailDTO) {
        Person person = findPersonOrThrow(personEmailDTO.getEmail());
        Ledger ledger = findLedgerOrThrow(person.getLedgerID());
        List<TransactionDTOout> transactions = ledger.getRecordsAsDTO();
        return TransactionsDTOAssembler.createDTOFromPrimitiveTypes(transactions);
    }

    @Transactional
    public AccountsDTO getPersonAccounts(PersonEmailDTO personEmailDTO) {
        Person person = findPersonOrThrow(personEmailDTO.getEmail());

        List<AccountDTO> accountsDTO = new ArrayList<>();
        for (AccountID accountID : person.getListOfAccounts()) {
            Account account = accountRepository
                    .findById(personEmailDTO.getEmail(), accountID.getDenomination().getDenomination())
                    .orElseThrow(() -> new NotFoundArgumentsBusinessException(ACCOUNT_DOES_NOT_EXIST));
            accountsDTO.add(AccountDTOAssembler.createDTOFromPrimitiveTypes(
                    account.getAccountID().getDenomination().getDenomination(),
                    account.getDescription().getDescription()));
        }

        return AccountsDTOAssembler.createDTOFromDomainObject(accountsDTO);
    }

    @Transactional
    public CategoriesDTO getPersonCategories(PersonEmailDTO personEmailDTO) {
        Person person = findPersonOrThrow(personEmailDTO.getEmail());
        return CategoriesDTOAssembler.createDTOFromDomainObject(person.getListOfCategories());
    }

    public SiblingsDTO getPersonSiblings(PersonEmailDTO personEmailDTO) {
        Person person = findPersonOrThrow(personEmailDTO.getEmail());
        return SiblingsDTOAssembler.createDTOFromDomainObject(person.getListOfSiblings());
    }

    @Transactional
    public List<GroupDTO> getPersonGroups(PersonEmailDTO personEmailDTO) {
        PersonID personID = PersonID.createPersonID(personEmailDTO.getEmail());
        findPersonOrThrow(personEmailDTO.getEmail());

        List<GroupDTO> result = new ArrayList<>();
        for (Group group : groupRepository.findAll()) {
            if (group.getAllMembers().contains(personID)) {
                result.add(GroupDTOAssembler.createDTOFromDomainObject(
                        group.getGroupID().getDenomination(),
                        group.getDescription(),
                        group.getDateOfCreation()));
            }
        }
        return result;
    }


    // -------------------------------------------------------------------------
    // Mutations
    // -------------------------------------------------------------------------

    @Transactional
    public boolean addAddressToPerson(PersonID id, Address address) {
        Person person = findPersonByIDOrThrow(id);
        boolean added = person.addAddress(
                address.getStreet(), address.getDoorNumber(),
                address.getPostCode(), address.getCity(), address.getCountry());
        if (!added) {
            throw new NotFoundArgumentsBusinessException(ADDRESS_ALREADY_EXIST);
        }
        return personRepository.addAndSaveAddress(person);
    }

    @Transactional
    public boolean addMotherToPerson(PersonID id, PersonID motherID) {
        Person person = findPersonByIDOrThrow(id);
        findPersonByIDOrThrow(motherID);
        boolean added = person.addMother(motherID);
        if (!added) {
            throw new NotFoundArgumentsBusinessException(MOTHER_ALREADY_EXIST);
        }
        return personRepository.addAndSaveMother(person);
    }

    @Transactional
    public boolean addFatherToPerson(PersonID id, PersonID fatherID) {
        Person person = findPersonByIDOrThrow(id);
        findPersonByIDOrThrow(fatherID);
        boolean added = person.addFather(fatherID);
        if (!added) {
            throw new NotFoundArgumentsBusinessException(FATHER_ALREADY_EXIST);
        }
        return personRepository.addAndSaveFather(person);
    }

    @Transactional
    public boolean addSiblingToPerson(PersonID personID, PersonID siblingID) {
        Person person = findPersonByIDOrThrow(personID);
        findPersonByIDOrThrow(siblingID);
        boolean added = person.addSibling(siblingID);
        if (!added) {
            throw new NotFoundArgumentsBusinessException(SIBLING_ALREADY_EXIST);
        }
        return personRepository.addAndSaveSibling(person, siblingID);
    }

    @Transactional
    public boolean addCategoryToPerson(CreatePersonCategoryDTO dto) {
        PersonID personID = PersonID.createPersonID(dto.getEmail());
        findPersonByIDOrThrow(personID);

        if (categoryRepository.findById(personID.getEmail().getEmail(), dto.getDenomination()).isPresent()) {
            throw new NotFoundArgumentsBusinessException(CATEGORY_ALREADY_EXIST);
        }

        Person person = findPersonByIDOrThrow(personID);
        boolean added = person.addCategory(CategoryID.createCategoryID(dto.getDenomination(), personID));
        if (!added) {
            throw new NotFoundArgumentsBusinessException(CATEGORY_ALREADY_EXIST);
        }
        return personRepository.addAndSaveCategory(person);
    }

    @Transactional
    public boolean addAccountToPerson(CreatePersonAccountDTO dto) {
        PersonID personID = PersonID.createPersonID(dto.getEmail());
        findPersonByIDOrThrow(personID);

        if (accountRepository.findById(personID.getEmail().getEmail(), dto.getDenomination()).isPresent()) {
            throw new NotFoundArgumentsBusinessException(ACCOUNT_ALREADY_EXIST);
        }

        Person person = findPersonByIDOrThrow(personID);
        boolean added = person.addAccount(AccountID.createAccountID(dto.getDenomination(), personID));
        if (!added) {
            throw new NotFoundArgumentsBusinessException(ACCOUNT_ALREADY_EXIST);
        }
        personRepository.addAndSaveAccount(person, dto.getDescription());
        return true;
    }

    @Transactional
    public boolean addPersonTransaction(CreatePersonTransactionDTO dto) {
        PersonID personID = PersonID.createPersonID(dto.getEmail());
        Person person = findPersonByIDOrThrow(personID);

        String emailStr = personID.getEmail().getEmail();
        CategoryID categoryID = CategoryID.createCategoryID(dto.getDenominationCategory(), personID);
        AccountID creditAccountID = AccountID.createAccountID(dto.getDenominationAccountCred(), personID);
        AccountID debitAccountID = AccountID.createAccountID(dto.getDenominationAccountDeb(), personID);

        if (!categoryRepository.findById(emailStr, dto.getDenominationCategory()).isPresent()) {
            throw new NotFoundArgumentsBusinessException(CATEGORY_DOES_NOT_EXIST);
        }
        if (!accountRepository.findById(emailStr, dto.getDenominationAccountDeb()).isPresent()) {
            throw new NotFoundArgumentsBusinessException(ACCOUNT_DOES_NOT_EXIST);
        }
        if (!accountRepository.findById(emailStr, dto.getDenominationAccountCred()).isPresent()) {
            throw new NotFoundArgumentsBusinessException(ACCOUNT_DOES_NOT_EXIST);
        }

        Ledger ledger = findLedgerOrThrow(person.getLedgerID());
        LocalDate date = LocalDate.parse(dto.getDate(), DATE_FORMATTER);

        boolean added = ledger.createAndAddTransactionWithDate(
                categoryID, dto.getType(), dto.getDescription(),
                dto.getAmount(), date, debitAccountID, creditAccountID);

        if (added) {
            ledgerRepository.addAndSaveTransaction(ledger);
            return true;
        }
        throw new InvalidArgumentsBusinessException(TRANSACTION_ALREADY_EXIST);
    }


    // -------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------

    private Person findPersonOrThrow(String email) {
        PersonID personID = PersonID.createPersonID(email);
        return personRepository.findById(personID)
                .orElseThrow(() -> new NotFoundArgumentsBusinessException(PERSON_DOES_NOT_EXIST));
    }

    private Person findPersonByIDOrThrow(PersonID personID) {
        return personRepository.findById(personID)
                .orElseThrow(() -> new NotFoundArgumentsBusinessException(PERSON_DOES_NOT_EXIST));
    }

    private Ledger findLedgerOrThrow(LedgerID ledgerID) {
        return ledgerRepository.findById(ledgerID)
                .orElseThrow(() -> new NotFoundArgumentsBusinessException(LEDGER_DOES_NOT_EXIST));
    }

    private PersonDTO toPersonDTO(Person person) {
        return PersonDTOAssembler.createDTOFromDomainObject(
                person.getPersonID().getEmail(),
                person.getName(),
                person.getBirthdate(),
                person.getBirthplace(),
                person.getFather(),
                person.getMother());
    }
}
