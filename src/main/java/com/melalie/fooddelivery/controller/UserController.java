package com.melalie.fooddelivery.controller;

import com.melalie.fooddelivery.model.response.TransactionByUserResponse;
import com.melalie.fooddelivery.service.GetTransactionByUserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private GetTransactionByUserService getTransactionsByUserService;

    public UserController(GetTransactionByUserService getTransactionsByUserService) {
        this.getTransactionsByUserService = getTransactionsByUserService;
    }

    @Operation(summary = "Retrieve User data based on ID or Name")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public TransactionByUserResponse getTransactionByUser(
            @RequestParam(value = "userId", required = false) String userId,
            @RequestParam(value = "name", required = false) String name) {
        return getTransactionsByUserService.execute(userId, name);
    }
}
