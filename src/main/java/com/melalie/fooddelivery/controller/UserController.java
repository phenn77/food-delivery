package com.melalie.fooddelivery.controller;

import com.melalie.fooddelivery.model.response.TransactionByUserResponse;
import com.melalie.fooddelivery.service.GetTransactionsByUserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private GetTransactionsByUserService getTransactionsByUserService;

    public UserController(GetTransactionsByUserService getTransactionsByUserService) {
        this.getTransactionsByUserService = getTransactionsByUserService;
    }

    @GetMapping("/{userId}")
    public TransactionByUserResponse getTransactionByUser(@PathVariable("userId") String userId) {
        return getTransactionsByUserService.execute(userId);
    }
}
