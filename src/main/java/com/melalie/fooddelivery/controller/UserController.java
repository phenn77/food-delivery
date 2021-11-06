package com.melalie.fooddelivery.controller;

import com.melalie.fooddelivery.model.response.TransactionByUserResponse;
import com.melalie.fooddelivery.service.GetTransactionsByUserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private GetTransactionsByUserService getTransactionsByUserService;

    public UserController(GetTransactionsByUserService getTransactionsByUserService) {
        this.getTransactionsByUserService = getTransactionsByUserService;
    }

    @Operation(description = "Retrieve User data based on USER_ID")
    @GetMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public TransactionByUserResponse getTransactionByUser(@PathVariable("userId") String userId) {
        return getTransactionsByUserService.execute(userId);
    }
}
