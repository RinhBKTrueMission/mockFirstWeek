package com.example.mockfirstweek.endpoint;

import com.example.mockfirstweek.model.Product;
import com.example.mockfirstweek.reponsitory.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.Optional;
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Transactional
@RequestMapping(value = "api/employee")
public class EmployeeEndpoint {
    private final ProductRepository repo;

    public EmployeeEndpoint(ProductRepository repo) {
        this.repo = repo;
    }

    @PostAuthorize("hasRole('STAFF') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @GetMapping("")
    public ResponseEntity<Iterable<Product>> getAll(){
       return new ResponseEntity<>(repo.findAll(), HttpStatus.OK);
    }
    @PostAuthorize("hasRole('STAFF') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Product> getInfo(@PathVariable String id){
        Optional<Product> userOptional=repo.findById(id);
        return userOptional.map(product -> new ResponseEntity<>(product,HttpStatus.OK)).orElseGet(()->new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    @PostMapping(value = "")
    public ResponseEntity<Product> setInfo(@RequestBody Product  product){
        return new ResponseEntity<>(repo.save(product), HttpStatus.OK);
    }
    @PreAuthorize("hasRole('STAFF') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<Product> updateInfo(@PathVariable String id,@RequestBody Product  product){
        Optional<Product> userOptional=repo.findById(id);
        return userOptional.map(product1 -> {product.setCode(product1.getCode());
            return new ResponseEntity<>(repo.save(product),HttpStatus.OK);
        }).orElseGet(()->new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Product> deleteInfo(@PathVariable String id){
        Optional<Product> userOptional=repo.findById(id);
        return userOptional.map(product ->
        {
            repo.deleteById(product.getCode());
            return new ResponseEntity<>(product,HttpStatus.OK);

        }).orElseGet(()->new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
