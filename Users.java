//To login as an admin enter the username: admin and the password: admin123
//store management system(shopping management system)
import java.io.*;
import java.util.*;

class Users {
    private String username;
    private String password;
    private boolean isAdmin;

    public Users(String username, String password, boolean isAdmin) {
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }
}

class Product {
    private int productId;
    private String name;
    private float price;
    private int quantity;

    public Product(int productId, String name, float price, int quantity) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public int getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public float getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setName(String name) {
        this.name = name;
    }
}

class StoreManagementSystem {
    private static final String USER_FILE = "users.txt";
    private static final String PRODUCT_FILE = "products.txt";
    private static List<Users> users = new ArrayList<>();
    private static List<Product> products = new ArrayList<>();
    private static final String DEFAULT_ADMIN_USERNAME = "admin";
    private static final String DEFAULT_ADMIN_PASSWORD = "admin123";

    public static void main(String[] args) {
        loadUsers();
        loadProducts();

        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Store Management System!");

        while (true) {
            System.out.println("1. Login");
            System.out.println("2. Sign Up");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    login(scanner);
                    break;
                case 2:
                    signUp(scanner);
                    break;
                case 3:
                    System.out.println("Exiting...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }

    private static void loadUsers() {
        // Load users from file
        try (Scanner scanner = new Scanner(new File(USER_FILE))) {
            while (scanner.hasNextLine()) {
                String[] userData = scanner.nextLine().split("\t");
                users.add(new Users(userData[0], userData[1], Boolean.parseBoolean(userData[2])));
            }
        } catch (FileNotFoundException e) {
            System.out.println("User file not found. Creating new file...");
        }

        // Check if default admin user exists, if not, create one
        boolean adminExists = false;
        for (Users user : users) {
            if (user.getUsername().equals(DEFAULT_ADMIN_USERNAME)) {
                adminExists = true;
                break;
            }
        }

        if (!adminExists) {
            users.add(new Users(DEFAULT_ADMIN_USERNAME, DEFAULT_ADMIN_PASSWORD, true));
            System.out.println("Default admin user created.");
        }
    }

    private static void saveUser(Users user) {
        try (FileWriter writer = new FileWriter(USER_FILE, true)) {
            writer.write(user.getUsername() + "\t" + user.getPassword() + "\t" + user.isAdmin() + "\n");
        } catch (IOException e) {
            System.out.println("Error saving user data.");
        }
    }

    private static void loadProducts() {
        // Load products from file
        try (Scanner scanner = new Scanner(new File(PRODUCT_FILE))) {
            while (scanner.hasNextLine()) {
                String[] productData = scanner.nextLine().split("\t");
                products.add(new Product(Integer.parseInt(productData[0]), productData[1],
                        Float.parseFloat(productData[2]), Integer.parseInt(productData[3])));
            }
        } catch (FileNotFoundException e) {
            System.out.println("Product file not found. Creating new file...");
        }
    }

    private static void saveProduct(Product product) {
        try (FileWriter writer = new FileWriter(PRODUCT_FILE, true)) {
            writer.write(product.getProductId() + "\t" + product.getName() + "\t" +
                    product.getPrice() + "\t" + product.getQuantity() + "\n");
        } catch (IOException e) {
            System.out.println("Error saving product data.");
        }
    }

    private static void login(Scanner scanner) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        Users loggedInUser = null;
        for (Users user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                loggedInUser = user;
                break;
            }
        }

        if (loggedInUser != null) {
            System.out.println("Login successful!");

            if (loggedInUser.isAdmin() || (username.equals(DEFAULT_ADMIN_USERNAME) && password.equals(DEFAULT_ADMIN_PASSWORD))) {
                adminMenu(scanner);
            } else {
                userMenu(scanner);
            }
        } else {
            System.out.println("Invalid username or password!");
        }
    }

    private static void signUp(Scanner scanner) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        boolean isAdmin = false; // Assume new users are regular users
        Users newUser = new Users(username, password, isAdmin);
        users.add(newUser);
        saveUser(newUser);
        System.out.println("User created successfully. You can now login.");
    }

    private static void adminMenu(Scanner scanner) {
        while (true) {
            System.out.println("Admin Menu:");
            System.out.println("1. Add Product");
            System.out.println("2. Modify Product");
            System.out.println("3. Remove Product");
            System.out.println("4. List All Users");
            System.out.println("5. Delete User");
            System.out.println("6. Logout");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    addProduct(scanner);
                    break;
                case 2:
                    modifyProduct(scanner);
                    break;
                case 3:
                    removeProduct(scanner);
                    break;
                case 4:
                    listAllUsers();
                    break;
                case 5:
                    deleteUser(scanner);
                    break;
                case 6:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }

    private static void addProduct(Scanner scanner) {
        System.out.print("Enter product ID: ");
        int productId = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter product name: ");
        String name = scanner.nextLine();
        System.out.print("Enter product price: ");
        float price = scanner.nextFloat();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter product quantity: ");
        int quantity = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        Product newProduct = new Product(productId, name, price, quantity);
        products.add(newProduct);
        saveProduct(newProduct);
        System.out.println("Product added successfully.");
    }

    private static void modifyProduct(Scanner scanner) {
        System.out.print("Enter the product ID to modify: ");
        int productId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        Product productToModify = null;
        for (Product product : products) {
            if (product.getProductId() == productId) {
                productToModify = product;
                break;
            }
        }

        if (productToModify != null) {
            System.out.println("Product found! Enter new details:");
            System.out.print("New product name (Enter to skip): ");
            String newName = scanner.nextLine();
            if (!newName.isEmpty()) {
                productToModify.setName(newName);
            }

            System.out.print("New product price (-1 to skip): ");
            float newPrice = scanner.nextFloat();
            scanner.nextLine(); // Consume newline
            if (newPrice != -1) {
                productToModify.setPrice(newPrice);
            }

            System.out.print("New product quantity (-1 to skip): ");
            int newQuantity = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            if (newQuantity != -1) {
                productToModify.setQuantity(newQuantity);
            }

            // Save modified product to file
            saveModifiedProduct(productToModify);
            System.out.println("Product modified successfully.");
        } else {
            System.out.println("Product not found!");
        }
    }

    private static void saveModifiedProduct(Product product) {
        // Rewrite the entire product file with the modified product
        try (PrintWriter writer = new PrintWriter(new FileWriter(PRODUCT_FILE))) {
            for (Product p : products) {
                if (p.getProductId() == product.getProductId()) {
                    writer.println(product.getProductId() + "\t" + product.getName() + "\t" +
                            product.getPrice() + "\t" + product.getQuantity());
                } else {
                    writer.println(p.getProductId() + "\t" + p.getName() + "\t" +
                            p.getPrice() + "\t" + p.getQuantity());
                }
            }
        } catch (IOException e) {
            System.out.println("Error saving modified product data.");
        }
    }


    private static void removeProduct(Scanner scanner) {
        System.out.print("Enter the product ID to remove: ");
        int productId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        Product productToRemove = null;
        for (Product product : products) {
            if (product.getProductId() == productId) {
                productToRemove = product;
                break;
            }
        }

        if (productToRemove != null) {
            products.remove(productToRemove);
            saveProductsToFile(); // Save updated product list to file
            System.out.println("Product removed successfully.");
        } else {
            System.out.println("Product not found!");
        }
    }
    private static void saveProductsToFile() {
        // Rewrite the entire product file with the updated product list
        try (PrintWriter writer = new PrintWriter(new FileWriter(PRODUCT_FILE))) {
            for (Product product : products) {
                writer.println(product.getProductId() + "\t" + product.getName() + "\t" +
                        product.getPrice() + "\t" + product.getQuantity());
            }
        } catch (IOException e) {
            System.out.println("Error saving product data.");
        }
    }


    private static void listAllUsers() {
        System.out.println("List of all users:");
        for (Users user : users) {
            System.out.println("Username: " + user.getUsername() + ", Admin: " + user.isAdmin());
        }
    }


    private static void deleteUser(Scanner scanner) {
        System.out.print("Enter the username of the user to delete: ");
        String usernameToDelete = scanner.nextLine();

        Users userToDelete = null;
        for (Users user : users) {
            if (user.getUsername().equals(usernameToDelete)) {
                userToDelete = user;
                break;
            }
        }

        if (userToDelete != null) {
            users.remove(userToDelete);
            saveUsersToFile(); // Save updated user list to file
            System.out.println("User deleted successfully.");
        } else {
            System.out.println("User not found!");
        }
    }

    private static void saveUsersToFile() {
        // Rewrite the entire user file with the updated user list
        try (PrintWriter writer = new PrintWriter(new FileWriter(USER_FILE))) {
            for (Users user : users) {
                writer.println(user.getUsername() + "\t" + user.getPassword() + "\t" + user.isAdmin());
            }
        } catch (IOException e) {
            System.out.println("Error saving user data.");
        }
    }


    private static void userMenu(Scanner scanner) {
        while (true) {
            System.out.println("User Menu:");
            System.out.println("1. Purchase Product");
            System.out.println("2. Search Product");
            System.out.println("3. List Available Products");
            System.out.println("4. Logout");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    purchaseProduct(scanner);
                    break;
                case 2:
                    searchProduct(scanner);
                    break;
                case 3:
                    listProducts();
                    break;
                case 4:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }

    private static void purchaseProduct(Scanner scanner) {
        System.out.print("Enter the product ID to purchase: ");
        int productId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        Product productToPurchase = null;
        for (Product product : products) {
            if (product.getProductId() == productId) {
                productToPurchase = product;
                break;
            }
        }

        if (productToPurchase != null) {
            System.out.println("Product found! Enter the quantity to purchase: ");
            int quantity = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (quantity <= productToPurchase.getQuantity()) {
                float totalCost = quantity * productToPurchase.getPrice();
                System.out.println("Total cost for " + quantity + " " + productToPurchase.getName() + ": $" + totalCost);

                System.out.println("Confirm purchase? (yes/no)");
                String confirmation = scanner.nextLine().toLowerCase();

                if (confirmation.equals("yes")) {
                    // Update product quantity
                    productToPurchase.setQuantity(productToPurchase.getQuantity() - quantity);
                    // Save updated product list to file
                    saveProductsToFile();

                    // Perform purchase transaction (you can customize this part based on your requirements)
                    System.out.println("Purchase successful! Thank you for shopping with us.");
                } else {
                    System.out.println("Purchase canceled.");
                }
            } else {
                System.out.println("Insufficient quantity available.");
            }
        } else {
            System.out.println("Product not found!");
        }
    }


    private static void searchProduct(Scanner scanner) {
        System.out.print("Enter the name of the product to search: ");
        String productName = scanner.nextLine();

        boolean found = false;
        for (Product product : products) {
            if (product.getName().equalsIgnoreCase(productName)) {
                System.out.println("Product found:");
                System.out.println("ID: " + product.getProductId());
                System.out.println("Name: " + product.getName());
                System.out.println("Price: $" + product.getPrice());
                System.out.println("Quantity available: " + product.getQuantity());
                found = true;
            }
        }

        if (!found) {
            System.out.println("Product not found!");
        }
    }

    private static void listProducts() {
        System.out.println("List of available products:");
        for (Product product : products) {
            System.out.println("ID: " + product.getProductId());
            System.out.println("Name: " + product.getName());
            System.out.println("Price: $" + product.getPrice());
            System.out.println("Quantity available: " + product.getQuantity());
            System.out.println("-------------------------");
        }
    }

}
