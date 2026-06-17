package com.finance.project.controllerLayer.controllersREST.personControllers;

import com.finance.project.applicationLayer.applicationServices.personServices.CreatePersonTransactionService;
import com.finance.project.domainLayer.exceptions.InvalidArgumentsBusinessException;
import com.finance.project.domainLayer.exceptions.NotFoundArgumentsBusinessException;
import com.finance.project.dtos.dtos.*;
import com.finance.project.dtos.dtosAssemblers.CreatePersonTransactionDTOAssembler;
import com.finance.project.dtos.dtosAssemblers.DeletePersonTransactionDTOAssembler;
import com.finance.project.dtos.dtosAssemblers.UpdatePersonTransactionDTOAssembler;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

// S1-S4: Extract Constant — elimina literales duplicados en los 3 metodos
// M1: CrossOrigin restringido a origen del frontend (no wildcard)
@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class CreatePersonTransactionControllerREST {

    private static final String REL_SIBLINGS   = "siblings";
    private static final String REL_RECORDS    = "records";
    private static final String REL_ACCOUNTS   = "accounts";
    private static final String REL_CATEGORIES = "categories";

    @Autowired
    private CreatePersonTransactionService service;

    @PostMapping("/persons/{personEmail}/ledgers/records")
    public ResponseEntity<Object> createPersonTransaction(
            @Valid @RequestBody NewPersonTransactionInfoDTO info,
            @PathVariable final String personEmail) {
        try {
            CreatePersonTransactionDTO dto = CreatePersonTransactionDTOAssembler.createDTOFromPrimitiveTypes(
                    personEmail, info.getDenominationCategory(), info.getType(), info.getDescription(),
                    info.getAmount(), info.getDenominationAccountDeb(), info.getDenominationAccountCred(), info.getDate());
            PersonDTO result = service.createTransaction(dto);
            addHateoasLinks(result, personEmail);
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } catch (NotFoundArgumentsBusinessException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (InvalidArgumentsBusinessException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/persons/{personEmail}/ledgers/records/{transactionNumber}")
    public ResponseEntity<Object> updatePersonTransaction(
            @Valid @RequestBody NewPersonTransactionInfoDTO info,
            @PathVariable final String personEmail,
            @PathVariable final int transactionNumber) {
        try {
            UpdatePersonTransactionDTO dto = UpdatePersonTransactionDTOAssembler.createDTOFromPrimitiveTypes(
                    transactionNumber, personEmail, info.getDenominationCategory(), info.getType(),
                    info.getDescription(), info.getAmount(), info.getDenominationAccountDeb(), info.getDenominationAccountCred());
            PersonDTO result = service.updateTransaction(dto);
            addHateoasLinks(result, personEmail);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (NotFoundArgumentsBusinessException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (InvalidArgumentsBusinessException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // M9: DELETE retorna 204 NO_CONTENT en lugar de 200
    @DeleteMapping("/persons/{personEmail}/ledgers/records/{transactionNumber}")
    public ResponseEntity<Object> deletePersonTransaction(
            @PathVariable final String personEmail,
            @PathVariable final int transactionNumber) {
        try {
            DeletePersonTransactionDTO dto = DeletePersonTransactionDTOAssembler.createDTOFromPrimitiveTypes(
                    transactionNumber, personEmail);
            service.deleteTransaction(dto);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (NotFoundArgumentsBusinessException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (InvalidArgumentsBusinessException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // M3: Extract Method — bloque HATEOAS centralizado, elimina 12 lineas duplicadas x3
    // M7: Variables renombradas a camelCase (linkToSiblings vs link_to_siblings)
    private void addHateoasLinks(PersonDTO result, String personEmail) {
        Link linkToSiblings   = linkTo(methodOn(CreatePersonControllerREST.class).getPersonSiblings(personEmail)).withRel(REL_SIBLINGS);
        Link linkToRecords    = linkTo(methodOn(PersonSearchAccountRecordsControllerREST.class).searchPersonRecords("", "", "", personEmail)).withRel(REL_RECORDS);
        Link linkToAccounts   = linkTo(methodOn(CreatePersonControllerREST.class).getPersonAccounts(personEmail)).withRel(REL_ACCOUNTS);
        Link linkToCategories = linkTo(methodOn(CreatePersonControllerREST.class).getPersonCategories(personEmail)).withRel(REL_CATEGORIES);
        result.add(linkToSiblings);
        result.add(linkToRecords);
        result.add(linkToAccounts);
        result.add(linkToCategories);
    }
}
