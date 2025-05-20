package fr.baretto.Entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CarrierTest {

    private Carrier carrier;
    private UUID carrierId;
    private String code;
    private String name;
    private Set<Warehouse> warehouses;

    @BeforeEach
    void setUp() {
        carrierId = UUID.randomUUID();
        code = "TEST-CARRIER";
        name = "Test Carrier";
        warehouses = new HashSet<>();
        
        Warehouse warehouse = new Warehouse();
        warehouse.setName("WH1");
        warehouse.setCode("WH1-CODE");
        warehouse.setAddress("Address 1");
        warehouse.setMarketplace("Marketplace 1");
        warehouse.setId(UUID.randomUUID());
        warehouses.add(warehouse);
        
        carrier = new Carrier();
        carrier.setId(carrierId);
        carrier.setCode(code);
        carrier.setName(name);
        carrier.setIncludedWarehouses(warehouses);
    }

    @Test
    void constructor_WithCodeAndName_ShouldInitializeCorrectly() {
        Carrier newCarrier = new Carrier(code, name);

        assertEquals(code, newCarrier.getCode());
        assertEquals(name, newCarrier.getName());
        assertNotNull(newCarrier.getIncludedWarehouses());
        assertTrue(newCarrier.getIncludedWarehouses().isEmpty());
    }

    @Test
    void gettersAndSetters_ShouldWorkCorrectly() {
        // Test ID
        assertEquals(carrierId, carrier.getId());
        UUID newId = UUID.randomUUID();
        carrier.setId(newId);
        assertEquals(newId, carrier.getId());

        // Test Code
        assertEquals(code, carrier.getCode());
        String newCode = "NEW-CARRIER";
        carrier.setCode(newCode);
        assertEquals(newCode, carrier.getCode());

        // Test Name
        assertEquals(name, carrier.getName());
        String newName = "New Carrier";
        carrier.setName(newName);
        assertEquals(newName, carrier.getName());

        // Test Warehouses
        assertEquals(warehouses, carrier.getIncludedWarehouses());
        Set<Warehouse> newWarehouses = new HashSet<>();
        carrier.setIncludedWarehouses(newWarehouses);
        assertEquals(newWarehouses, carrier.getIncludedWarehouses());
    }

    @Test
    void addWarehouse_ShouldAddWarehouseToSet() {
        Warehouse newWarehouse = new Warehouse();
        newWarehouse.setName("NEW-WH");
        newWarehouse.setCode("NEW-WH-CODE");
        newWarehouse.setAddress("New Address");
        newWarehouse.setMarketplace("New Marketplace");
        newWarehouse.setId(UUID.randomUUID());
        newWarehouse.getCarriers().add(carrier);
        carrier.getIncludedWarehouses().add(newWarehouse);

        assertEquals(2, carrier.getIncludedWarehouses().size());
        assertTrue(carrier.getIncludedWarehouses().contains(newWarehouse));
    }

    @Test
    void removeWarehouse_ShouldRemoveWarehouseFromSet() {
        Warehouse warehouseToRemove = warehouses.iterator().next();
        carrier.getIncludedWarehouses().remove(warehouseToRemove);

        assertTrue(carrier.getIncludedWarehouses().isEmpty());
        assertFalse(carrier.getIncludedWarehouses().contains(warehouseToRemove));
    }
} 