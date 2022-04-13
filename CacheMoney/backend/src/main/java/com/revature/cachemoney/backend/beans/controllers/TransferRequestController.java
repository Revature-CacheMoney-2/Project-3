package com.revature.cachemoney.backend.beans.controllers;

import com.revature.cachemoney.backend.beans.models.Transfer;
import com.revature.cachemoney.backend.beans.models.TransferRequest;
import com.revature.cachemoney.backend.beans.security.JwtUtil;
import com.revature.cachemoney.backend.beans.services.TransferRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("request")
public class TransferRequestController {
    private TransferRequestService transferRequestService;
    private JwtUtil jwtUtil;

    @Autowired
    public TransferRequestController(TransferRequestService transferRequestService, JwtUtil jwtUtil) {
        this.transferRequestService = transferRequestService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    public TransferRequest save(@RequestHeader(name = "token") String token,
                                @RequestHeader(name = "userId") Integer userId,
                                @RequestBody TransferRequest transferRequest) throws ResponseStatusException {
        if (this.jwtUtil.validateToken(token, userId)) {
            return this.transferRequestService.save(transferRequest);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Please provide a valid token");
        }
    }

    @GetMapping("source")
    public List<TransferRequest> findByRequestingUser(
            @RequestHeader(name = "token") String token,
            @RequestHeader(name = "userId") Integer userId) throws ResponseStatusException {
        if (this.jwtUtil.validateToken(token, userId)) {
            return this.transferRequestService.findByRequestingUser(userId);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Please provide a valid token");
        }
    }

    @GetMapping("destination")
    public List<TransferRequest> findByRequestedUser(
            @RequestHeader(name = "token") String token,
            @RequestHeader(name = "userId") Integer userId) throws ResponseStatusException {
        if (this.jwtUtil.validateToken(token, userId)) {
            return this.transferRequestService.findByRequestedUser(userId);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Please provide a valid token");
        }
    }

    @PostMapping("accept/{requestId}")
    public Transfer acceptTransfer(@PathVariable int requestId,
                                   @RequestHeader(name = "token") String token,
                                   @RequestHeader(name = "userId") Integer userId) throws ResponseStatusException {
        if (this.jwtUtil.validateToken(token, userId)) {
            return this.transferRequestService.acceptTransfer(requestId, userId);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Please provide a valid token");
        }
    }

    @GetMapping("delete/{requestId}")
    public void deleteTransfer(@PathVariable int requestId,
                               @RequestHeader(name = "token") String token,
                               @RequestHeader(name = "userId") Integer userId) throws ResponseStatusException {
        if (this.jwtUtil.validateToken(token, userId)) {
            this.transferRequestService.delete(requestId, userId);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Please provide a valid token");
        }
    }
}
