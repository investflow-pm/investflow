package com.mvp.crudmicroservice.stock.web.controller;

import com.mvp.crudmicroservice.stock.domain.Stock;
import com.mvp.crudmicroservice.stock.service.StockService;
import com.mvp.crudmicroservice.stock.web.dto.StockDto;
import com.mvp.crudmicroservice.stock.web.mapper.StockMapper;
import com.mvp.crudmicroservice.user.domain.exception.ResourceNotFoundException;
import com.mvp.crudmicroservice.user.domain.exception.StockAddingToUserException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/crud/stocks")
@RequiredArgsConstructor
public class StockController {

    /*
    TODO Написать ручки для получения StockDto
     с invest-microservice и сохранять их в бд
    */

}
