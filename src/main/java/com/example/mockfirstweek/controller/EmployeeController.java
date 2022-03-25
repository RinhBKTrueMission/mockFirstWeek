package com.example.mockfirstweek.controller;

import com.example.mockfirstweek.model.Product;
import com.example.mockfirstweek.reponsitory.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.Optional;

@RestController
@Transactional
@RequestMapping(value = "api/employee")
public class EmployeeController {
    @Autowired
    private  ProductRepository repo;
    @GetMapping("/getall")
    public ResponseEntity<Iterable<Product>> getAll(){
       return new ResponseEntity<>(repo.findAll(), HttpStatus.OK);
    }
    @GetMapping("/getinfo/{id}")
    public ResponseEntity<Product> getInfo(@PathVariable String id){
        Optional<Product> userOptional=repo.findById(id);
        return userOptional.map(product -> new ResponseEntity<>(product,HttpStatus.OK)).orElseGet(()->new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
