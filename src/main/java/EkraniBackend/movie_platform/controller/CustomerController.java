package EkraniBackend.movie_platform.controller;

import EkraniBackend.movie_platform.model.Customer;
import EkraniBackend.movie_platform.service.CustomerService;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public ResponseEntity<List<Customer>> getAllUsers() {
        List<Customer> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Customer customer) {
        if (customer.getFirstName() == null || customer.getLastName() == null ||
                customer.getEmail() == null || customer.getPassword() == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Missing required fields"));
        }

        if (!customer.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid email format"));
        }

        try {
            Customer createdCustomer = customerService.registerCustomer(customer);
            return ResponseEntity
                    .ok(Map.of("message", "User registered successfully", "userId", createdCustomer.getId()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("message", "Error registering user"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> loginData) {
        String email = loginData.get("email");
        String password = loginData.get("password");

        if (email == null || password == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email and password are required"));
        }

        Optional<Customer> customer = customerService.authenticate(email, password);
        if (customer.isPresent()) {
            Customer loggedInCustomer = customer.get();
            Map<String, Object> response = Map.of(
                    "message", "Login successful",
                    "userId", loggedInCustomer.getId(),
                    "firstName", loggedInCustomer.getFirstName(),
                    "lastName", loggedInCustomer.getLastName(),
                    "email", loggedInCustomer.getEmail());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(401).body(Map.of("message", "Invalid email or password"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomerById(@PathVariable String id) {
        if (!ObjectId.isValid(id)) {
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid customer ID format"));
        }

        Optional<Customer> customer = customerService.getCustomerById(id);
        if (customer.isPresent()) {
            return ResponseEntity.ok(customer.get());
        } else {
            return ResponseEntity.status(404).body(Map.of("message", "User not found"));
        }
    }

}
