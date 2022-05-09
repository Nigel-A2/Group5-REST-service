package com.group5.restservice.group5restservice;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.group5.model.Product;
import com.group5.model.ProductsSupplier;
import com.group5.model.Supplier;
import com.group5.restservice.group5restservice.logger.LogToFile;
import org.mariadb.jdbc.Driver;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.lang.reflect.Type;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

/**
 * API for handling Supplier resource related requests
 * @author Nate Penner
 * */
@Path("/supplier")
public class SupplierResource {
    /**
     * Constructor - needed to make sure SQL driver is registered
     * */
    public SupplierResource() {
        try {
            DriverManager.registerDriver(new Driver());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get a list of suppliers (and the products they supply)
     * @return json string data representing a supplier and the products they offer
     * */
    @Path("/list")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getSuppliersList() {
        EntityManager manager = Persistence.createEntityManagerFactory("default")
                .createEntityManager();

        // Get a list of all suppliers
        List<Supplier> suppliersList = manager.createQuery("select s from Supplier s order by s.supName", Supplier.class)
                .getResultList();

        // Build json array for the response
        JsonArray suppliersArray = new JsonArray();

        suppliersList.forEach(s -> {
            // put supplier info in the response for each supplier
            JsonObject supplier = new JsonObject();
            supplier.addProperty("id", s.getId());
            supplier.addProperty("supName", s.getSupName());

            JsonArray productsArrayList = new JsonArray();

            // Get products for this supplier
            List<ProductsSupplier> productsSuppliers = manager.createQuery(
                    "select ps from ProductsSupplier ps where ps.supplier = ?1",
                    ProductsSupplier.class
            ).setParameter(1, s.getId()).getResultList();

            // Get product info
            productsSuppliers.forEach(ps -> {
                Product product = manager.find(Product.class, ps.getProduct());
                JsonObject productObj = new JsonObject();
                productObj.addProperty("productId", product.getId());
                productObj.addProperty("prodName", product.getProdName());
                productsArrayList.add(productObj);
            });

            supplier.add("products", productsArrayList);

            suppliersArray.add(supplier);
        });

        // Convert to json string and return
        Type type = new TypeToken<List<Supplier>>(){}.getType();
        Gson gson = new Gson();
        return gson.toJson(suppliersArray);
    }

    /**
     * Create a new supplier
     * @param jsonString The new supplier data
     * @return A response string
     * */
    @Path("/create")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    public String createSupplier(String jsonString) {
        EntityManager manager = Persistence.createEntityManagerFactory("default")
                .createEntityManager();

        Gson gson = new Gson();

        // Convert the json string to a supplier object
        Supplier supplier = gson.fromJson(jsonString, Supplier.class);
        HashMap<String, Object> response = new HashMap<>();
        Type type = new TypeToken<HashMap<String, Object>>(){}.getType();

        // Check for duplicate supplier id
        if (manager.find(Supplier.class, supplier.getId()) != null) {
            response.put("statusCode", -1);
            response.put("message", "Supplier id already in use.");
            return gson.toJson(response, type);
        }

        // Try to save the new supplier to the database and set up an appropriate response
        try {
            manager.getTransaction().begin();
            manager.persist(supplier);
            if (!manager.contains(supplier)) {
                manager.getTransaction().rollback();
                response.put("statusCode", 0);
                response.put("message", "Failed to save supplier.");
            } else {
                manager.getTransaction().commit();
            }
        } catch (Exception e) {
            response.put("statusCode", 0);
            response.put("message", e.getMessage());
        }

        manager.close();

        // Succeeded
        response.put("statusCode", 1);
        response.put("message", "success");

        return gson.toJson(response, type);
    }

    /**
     * Updates a list of products for a supplier
     * @param jsonString the product list the supplier now offers
     * @return A response string
     * */
    @Path("/update")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    public String updateProduct(String jsonString) {
        Gson gson = new Gson();
        Type outputType = new TypeToken<HashMap<String, Object>>(){}.getType();

        // Default status code 1 if no problems occurred
        int statusCode = 1;

        // Response builder
        HashMap<String, Object> response = new HashMap<>();

        try {
            EntityManager manager = Persistence.createEntityManagerFactory("default")
                    .createEntityManager();

            // Parse the json string into an object
            JsonObject root = JsonParser.parseString(jsonString).getAsJsonObject();

            // Use the supplier id to get a supplier from the database
            int supplierId = root.get("id").getAsInt();
            Supplier supplier = manager.find(Supplier.class, supplierId);

            // get a list of products supplied by this supplier
            List<ProductsSupplier> supplierProducts = manager.createQuery(
                    "select ps from ProductsSupplier ps where ps.supplier=?1", ProductsSupplier.class
            ).setParameter(1, supplierId).getResultList();

            // get the list of products that this supplier will now provide (pulled from the json data)
            List<Product> receivedProducts = getReceivedProductsList(root.get("products").getAsJsonArray());

            // for debugging purposes
            HashMap<String, Boolean> removals = new LinkedHashMap<>();

            // Go through database product list for this supplier and remove each one not found in receivedProducts
            LogToFile.logToFile("Checking products supplied by supplier " + supplier.getSupName());
            for (ProductsSupplier sp : supplierProducts) {
                Product product = manager.find(Product.class, sp.getProduct());
                LogToFile.logToFile(supplier.getSupName() + " supplies " + product.getProdName());
                if (receivedProducts.stream().noneMatch(p -> Objects.equals(p.getId(), sp.getProduct()))) {
                    LogToFile.logToFile("That product is to be removed.");
                    try {
                        manager.getTransaction().begin();
                        ProductsSupplier merged = manager.merge(sp);
                        manager.remove(merged);
                        if (!manager.contains(sp)) {
                            manager.getTransaction().commit();
                            LogToFile.logToFile(product.getProdName() + " was removed.");
                            removals.put(product.getProdName(), true);
                        } else {
                            manager.getTransaction().rollback();
                            LogToFile.logToFile(product.getProdName() + " was not removed.");
                            statusCode = -1;
                            removals.put(product.getProdName(), false);
                        }
                    } catch (Exception e) {
                        statusCode = -1;
                        LogToFile.logToFile(product.getProdName() + " was not removed.");
                        LogToFile.logToFile(e.getMessage());
                    }
                } else {
                    LogToFile.logToFile("That product can stay");
                }
            }

            // Loop through received products and add them to the database unless they are already in the database
            receivedProducts.forEach(rp -> {
                HashMap<String, Object> map = new HashMap<>();
                map.put("id", 0);
                map.put("product", rp.getId());
                map.put("supplier", supplier.getId());

                Type mapType = new TypeToken<HashMap<String, Object>>(){}.getType();
                String psJsonString = gson.toJson(map, mapType);

                ProductsSupplier productsSupplier = gson.fromJson(JsonParser.parseString(psJsonString), ProductsSupplier.class);

                LogToFile.logToFile("Gonna add new product supplier:");
                LogToFile.logToFile("supplier id: "+productsSupplier.getSupplier());
                LogToFile.logToFile("product id: "+productsSupplier.getProduct());
                LogToFile.logToFile("productsupplier id: "+productsSupplier.getId());

                List<ProductsSupplier> checkExists = manager.createQuery("select ps from ProductsSupplier ps where ps.supplier=?1 and ps.product=?2", ProductsSupplier.class)
                        .setParameter(1, supplier.getId())
                        .setParameter(2, rp.getId())
                        .getResultList();

                // Only save the products supplier if it does not already exist in the database
                if (checkExists.size() < 1) {
                    // make the new product supplier persist
                    try {
                        manager.getTransaction().begin();
                        manager.persist(productsSupplier);
                        if (manager.contains(productsSupplier)) {
                            manager.getTransaction().commit();
                            LogToFile.logToFile("Successfully added to database.");
                        } else {
                            manager.getTransaction().rollback();
                            LogToFile.logToFile("Failed to add to database.");
                        }
                    } catch (Exception e) {
                        LogToFile.logToFile("Exception occurred. Failed to add to database.");
                        LogToFile.logToFile(e.getMessage());
                    }
                } else {
                    LogToFile.logToFile("That product supplier already exists. Not adding anything.");
                }
            });

            // response
            response.put("statusCode", statusCode);

        } catch (Exception e) {
            // response
            response.put("statusCode", statusCode);
            response.put("error", e.getMessage());
            response.put("stackTrace", e.getStackTrace());
        }
        return gson.toJson(response, outputType);
    }

    // Extract the new product list from the json array
    private List<Product> getReceivedProductsList(JsonArray array) {
        Gson gson = new Gson();
        List<Product> productList = new ArrayList<>();
        array.forEach(elem -> {
            JsonObject prodObj = elem.getAsJsonObject();
            productList.add(gson.fromJson(prodObj, Product.class));
        });
        return productList;
    }
}
