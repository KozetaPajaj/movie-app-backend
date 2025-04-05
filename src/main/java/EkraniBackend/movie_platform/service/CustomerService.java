package EkraniBackend.movie_platform.service;

import EkraniBackend.movie_platform.model.Customer;
import EkraniBackend.movie_platform.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Customer registerCustomer(Customer customer) {
        Optional<Customer> existingCustomer = customerRepository.findByEmail(customer.getEmail());
        if (existingCustomer.isPresent()) {
            throw new IllegalArgumentException("Email already in use");
        }
    
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        return customerRepository.save(customer);
    }
    

    public Optional<Customer> authenticate(String email, String rawPassword) {
        Optional<Customer> customerOpt = customerRepository.findByEmail(email);
        if (customerOpt.isPresent()) {
            Customer customer = customerOpt.get();
            if (passwordEncoder.matches(rawPassword, customer.getPassword())) {
                return Optional.of(customer);
            }
        }
        return Optional.empty();
    }

    public Optional<Customer> getCustomerById(String id) {
        return customerRepository.findById(id);
    }
}
