package com.example.covider.database.building;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.covider.config.Config;
import com.example.covider.database.ManagerFactory;
import com.example.covider.model.building.Building;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;


public class BuildingManagerTest {
    BuildingManager buildingManager;

    @Before
    public void setup(){
        Context instrumentationContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Config.Change_Test();
//        instrumentationContext.deleteDatabase(Config.DATABASE_NAME);
        ManagerFactory.initialize(instrumentationContext);
        buildingManager = ManagerFactory.getBuildingManagerInstance();
    }

    @Test
    public void testInsertAndRetrieveBuildings(){
        buildingManager.addOrUpdateBuilding(new Building(1,"SAL"));
        buildingManager.addOrUpdateBuilding(new Building(2,"KAP"));
        buildingManager.addOrUpdateBuilding(new Building(4,"LVL"));

        Building buildingExpected = new Building(1, "SAL");

        Building buildingGetById = buildingManager.getBuildingById(buildingExpected.getId());
        assertEquals(buildingExpected, buildingGetById);

        Building buildingGetByName = buildingManager.getBuildingByName(buildingExpected.getName());
        assertEquals(buildingExpected, buildingGetByName);

        ArrayList<Building> buildingList = buildingManager.getBuildingList();
        assertEquals(buildingExpected, buildingList.get(0));

    }

    @Test
    public void testRequirements(){
        buildingManager.addOrUpdateBuilding(new Building(1,"SAL", "DUMMY"));
        Building buildingExpected = new Building(1, "SAL", "DUMMY");
        Building buildingGetById = buildingManager.getBuildingById(buildingExpected.getId());
        assertEquals(buildingExpected, buildingGetById);

        int id = buildingManager.addBuilding("Test");
        buildingGetById = buildingManager.getBuildingById(id);
        buildingExpected = new Building(id, "Test", Building.getDefaultRequirement());
        assertEquals(buildingExpected, buildingGetById);
    }

    @After
    public void clean(){
        Config.Change_Normal();
    }
}
