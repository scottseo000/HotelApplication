package service;

import model.Customer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CustomerService {

    private final Map<String, Customer> customers = new HashMap<String, Customer>();
    private static final CustomerService SINGLETON = new CustomerService();

    public static CustomerService getSingleton() {
        return SINGLETON;
    }

    public void addCustomer(String firstName, String lastName, String email) {
        customers.put(email, new Customer(firstName, lastName, email));
    }

    public Customer getCustomer(String customerEmail) {
        return customers.getOrDefault(customerEmail, null);
    }

    public Collection<Customer> getAllCustomers() {
        return customers.values();
    }
}
