package com.noprobit.servingwebcontent.controller;

import com.noprobit.servingwebcontent.domain.Person;
import com.noprobit.servingwebcontent.requests.UpdatePersonRequest;
import com.noprobit.servingwebcontent.service.OperatorService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/operators",
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
        consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class OperatorController {

    private OperatorService operatorService;

    @Autowired
    public OperatorController(OperatorService operatorService) {
        this.operatorService = operatorService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Person> getAllOperators() {
        return operatorService.getAllOperators();
    }

    @RequestMapping(method = RequestMethod.GET, value = "{key}")
    public Person getOperator(@PathVariable("key") String operatorKey) {
        Assert.hasText(operatorKey, "Key is missing");
        return operatorService.getOperatorByUuid(operatorKey);
    }

    @RequestMapping(method = RequestMethod.POST)
    public Person createOperator(@RequestBody Person operator) {
        return operatorService.createOperator(operator.getName(), operator.getEmail());
    }

    @RequestMapping(method = RequestMethod.PUT, value = "{key}")
    public Person updateOperator(@PathVariable("key") String operatorKey, @RequestBody UpdatePersonRequest updatePersonRequest) {
        Assert.hasText(operatorKey, "Key is missing");
        Assert.notNull(updatePersonRequest, "Request does not contain a Person to be modified");

        updatePersonRequest.setKey(operatorKey);
        return operatorService.updateOperator(updatePersonRequest);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "{key}")
    public void deleteOperator(@PathVariable("key") String operatorKey) {
        Assert.hasText(operatorKey, "Key is missing");
        operatorService.deleteOperatorByUuid(operatorKey);
    }
}
