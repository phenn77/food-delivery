package com.melalie.fooddelivery.controller;

import com.melalie.fooddelivery.model.request.UserRequest;
import com.melalie.fooddelivery.model.response.TransactionByUserResponse;
import com.melalie.fooddelivery.model.response.UserByDateResponse;
import com.melalie.fooddelivery.service.GetTransactionByUserService;
import com.melalie.fooddelivery.service.GetUsersByDateService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    private GetTransactionByUserService getTransactionsByUserService;
    private GetUsersByDateService getUsersByDateService;

    public UserController(GetTransactionByUserService getTransactionsByUserService, GetUsersByDateService getUsersByDateService) {
        this.getTransactionsByUserService = getTransactionsByUserService;
        this.getUsersByDateService = getUsersByDateService;
    }

    @Operation(
            summary = "Retrieve User data based on ID or Name",
            description = "Will return 1 data if used ID as search parameter, meanwhile, if used NAME as search parameters, it will return list of user in which the name has the value (wildcard search)")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public TransactionByUserResponse getTransactionByUser(
            @RequestParam(value = "userId", required = false) String userId,
            @RequestParam(value = "name", required = false) String name) throws Exception {
        return getTransactionsByUserService.execute(userId, name);
    }

    @Operation(
            summary = "Retrieve User data based on ID or Name",
            description = "Will return 1 data if used ID as search parameter, meanwhile, if used NAME as search parameters, it will return list of user in which the name has the value (wildcard search)")
    @PostMapping(value = "/transaction", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserByDateResponse getUsersTransaction(@Valid @RequestBody UserRequest request) throws Exception {
        return getUsersByDateService.execute(request);
    }
}
