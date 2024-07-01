package com.nashtech.rookie.asset_management_0701.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nashtech.rookie.asset_management_0701.services.returning_request.ReturningRequestService;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/v1/returning-requests")
@RequiredArgsConstructor
public class ReturningRequestController {

    private final ReturningRequestService returningRequestService;

}
