package com.melalie.fooddelivery.service;

import com.melalie.fooddelivery.model.projection.UserTransaction;
import com.melalie.fooddelivery.model.request.UserRequest;
import com.melalie.fooddelivery.model.response.TopUsersResponse;
import com.melalie.fooddelivery.repository.UserPurchaseRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.melalie.fooddelivery.util.constant.CommonUtils.formatDate;

@Log4j2
@Service
public class GetTopUsersService {

    private UserPurchaseRepository userPurchaseRepository;

    public GetTopUsersService(UserPurchaseRepository userPurchaseRepository) {
        this.userPurchaseRepository = userPurchaseRepository;
    }

    public TopUsersResponse execute(UserRequest request) throws Exception {
        if (Objects.isNull(request.getTotalUser())) {
            log.error("Total User is needed.");

            throw new Exception("Payload not complete.");
        }

        return TopUsersResponse.builder()
                .users(retrieveUsersTransactions(request))
                .build();
    }

    private List<TopUsersResponse.UserTrx> retrieveUsersTransactions(UserRequest request) throws Exception {
        var fromDate = formatDate(request.getFromDate());
        var toDate = formatDate(request.getToDate());

        var trx = userPurchaseRepository.getUsersTransaction(fromDate, toDate, request.getTotalUser());

        return trx
                .stream()
                .map(data -> TopUsersResponse.UserTrx.builder()
                        .name(data.getName())
                        .totalTransaction(data.getTotalTransaction())
                        .totalAmount(data.getTotalAmount().setScale(2, RoundingMode.DOWN))
                        .build())
                .collect(Collectors.toList());
    }
}
