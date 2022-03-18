package com.example.covider.database.building;

import android.content.Context;


import static org.junit.Assert.*;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.covider.database.ManagerFactory;
import com.example.covider.model.building.Building;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;


public class BuildingManagerTest {
    BuildingManager buildingManager;

    @Before
    public void setup(){
        Context instrumentationContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
//        instrumentationContext.deleteDatabase("covider");
        ManagerFactory.initialize(instrumentationContext);
        buildingManager = ManagerFactory.getBuildingManagerInstance();
    }

    @Test
    public void testDefaultBuildings(){
        Building buildingExpected = new Building(1, "SAL");

        Building buildingGetById = buildingManager.getBuildingById(buildingExpected.getId());
        assertEquals(buildingExpected, buildingGetById);

        Building buildingGetByName = buildingManager.getBuildingByName(buildingExpected.getName());
        assertEquals(buildingExpected, buildingGetByName);

        ArrayList<Building> buildingList = buildingManager.getBuildingList();
        assertEquals(buildingExpected, buildingList.get(0));
        assertEquals(3, buildingList.size());

    }
}
