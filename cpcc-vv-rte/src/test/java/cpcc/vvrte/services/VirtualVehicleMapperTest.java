// This code is part of the CPCC-NG project.
//
// Copyright (c) 2013 Clemens Krainer <clemens.krainer@gmail.com>
//
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software Foundation,
// Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.

package cpcc.vvrte.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import cpcc.core.entities.Parameter;
import cpcc.core.entities.PolarCoordinate;
import cpcc.core.entities.RealVehicle;
import cpcc.core.entities.RealVehicleType;
import cpcc.core.entities.SensorDefinition;
import cpcc.core.entities.SensorType;
import cpcc.core.entities.SensorVisibility;
import cpcc.core.services.QueryManager;
import cpcc.core.services.RealVehicleRepository;
import cpcc.vvrte.base.VirtualVehicleMappingDecision;
import cpcc.vvrte.entities.Task;
import cpcc.vvrte.services.VirtualVehicleMapperImpl;

/**
 * VirtualVehicleMapperTest
 */
public class VirtualVehicleMapperTest
{
    private static final String GS_01 = "gs01";
    private static final String REAL_VEHICLE_ONE_NAME = "rv001";
    private static final String REAL_VEHICLE_TWO_NAME = "rv002";
    private static final String REAL_VEHICLE_THREE_NAME = "rv003";

    @SuppressWarnings("serial")
    private static final SensorDefinition altimeter = new SensorDefinition()
    {
        {
            setId(1);
            setDescription("Altimeter");
            setLastUpdate(new Date(1));
            setParameters(null);
            setType(SensorType.ALTIMETER);
            setVisibility(SensorVisibility.ALL_VV);
            setMessageType("std_msgs/Float32");
        }
    };

    @SuppressWarnings("serial")
    private static final SensorDefinition barometer = new SensorDefinition()
    {
        {
            setId(1);
            setDescription("Barometer");
            setLastUpdate(new Date(2));
            setParameters(null);
            setType(SensorType.BAROMETER);
            setVisibility(SensorVisibility.ALL_VV);
            setMessageType("std_msgs/Float32");
        }
    };

    @SuppressWarnings("serial")
    private static final SensorDefinition co2Sensor = new SensorDefinition()
    {
        {
            setId(1);
            setDescription("CO2");
            setLastUpdate(new Date(3));
            setParameters(null);
            setType(SensorType.CO2);
            setVisibility(SensorVisibility.ALL_VV);
            setMessageType("std_msgs/Float32");
        }
    };

    @SuppressWarnings("serial")
    private static final SensorDefinition gpsReceiver = new SensorDefinition()
    {
        {
            setId(1);
            setDescription("GPS");
            setLastUpdate(new Date(4));
            setParameters(null);
            setType(SensorType.GPS);
            setVisibility(SensorVisibility.ALL_VV);
            setMessageType("sensor_msgs/NavSatFix");
        }
    };

    private static final String AREA_OF_OPERATION_RV1 =
        "{\"type\":\"FeatureCollection\",\"features\":["
            + "{\"type\":\"Feature\",\"properties\":{\"type\":\"depot\"},"
            + "\"geometry\":{\"type\":\"Point\",\"coordinates\":[47.5,13.5]}},"
            + "{\"type\":\"Feature\",\"properties\":{\"minAlt\":20,\"maxAlt\":50},"
            + "\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[13,47],[14,47],[14,48],[13,48],[13,48]]]}}]}";

    private static final String AREA_OF_OPERATION_RV2 =
        "{\"type\":\"FeatureCollection\",\"features\":["
            + "{\"type\":\"Feature\",\"properties\":{\"type\":\"depot\"},"
            + "\"geometry\":{\"type\":\"Point\",\"coordinates\":[47.5,14.5]}},"
            + "{\"type\":\"Feature\",\"properties\":{\"minAlt\":20,\"maxAlt\":50},"
            + "\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[14,47],[15,47],[15,48],[14,48],[14,47]]]}}]}";

    private Parameter gsName1;
    private RealVehicle gs01;
    private Parameter rvName1;
    private RealVehicle realVehicle1;
    private Parameter rvName2;
    private RealVehicle realVehicle2;
    private Parameter rvName3;
    private RealVehicle realVehicle3;
    private QueryManager qm;
    private VirtualVehicleMapperImpl sut;
    private RealVehicleRepository realVehicleRepository;
    private Logger logger;

    @BeforeMethod
    public void setUp() throws JsonParseException, JsonMappingException, IOException
    {
        logger = mock(Logger.class);

        gs01 = mock(RealVehicle.class);
        when(gs01.toString()).thenReturn(GS_01);
        when(gs01.getName()).thenReturn(GS_01);
        when(gs01.getType()).thenReturn(RealVehicleType.GROUND_STATION);

        realVehicle1 = mock(RealVehicle.class);
        when(realVehicle1.toString()).thenReturn(REAL_VEHICLE_ONE_NAME);
        when(realVehicle1.getName()).thenReturn(REAL_VEHICLE_ONE_NAME);
        when(realVehicle1.getAreaOfOperation()).thenReturn(AREA_OF_OPERATION_RV1);
        when(realVehicle1.getSensors()).thenReturn(Arrays.asList(altimeter, barometer, co2Sensor));

        realVehicle2 = mock(RealVehicle.class);
        when(realVehicle2.toString()).thenReturn(REAL_VEHICLE_TWO_NAME);
        when(realVehicle2.getName()).thenReturn(REAL_VEHICLE_TWO_NAME);
        when(realVehicle2.getAreaOfOperation()).thenReturn(AREA_OF_OPERATION_RV2);
        when(realVehicle2.getSensors()).thenReturn(Arrays.asList(altimeter, barometer, co2Sensor));

        realVehicle3 = mock(RealVehicle.class);
        when(realVehicle3.toString()).thenReturn(REAL_VEHICLE_THREE_NAME);
        when(realVehicle3.getName()).thenReturn(REAL_VEHICLE_THREE_NAME);
        when(realVehicle3.getAreaOfOperation()).thenReturn(AREA_OF_OPERATION_RV2);

        gsName1 = new Parameter();
        gsName1.setValue(GS_01);

        rvName1 = new Parameter();
        rvName1.setValue(REAL_VEHICLE_ONE_NAME);

        rvName2 = new Parameter();
        rvName2.setValue(REAL_VEHICLE_TWO_NAME);

        rvName3 = new Parameter();
        rvName3.setValue(REAL_VEHICLE_THREE_NAME);

        qm = mock(QueryManager.class);
        when(qm.findParameterByName(Parameter.REAL_VEHICLE_NAME)).thenReturn(rvName1);

        realVehicleRepository = mock(RealVehicleRepository.class);
        when(realVehicleRepository.findRealVehicleByName(GS_01)).thenReturn(gs01);
        when(realVehicleRepository.findRealVehicleByName(REAL_VEHICLE_ONE_NAME)).thenReturn(realVehicle1);
        when(realVehicleRepository.findRealVehicleByName(REAL_VEHICLE_TWO_NAME)).thenReturn(realVehicle2);
        when(realVehicleRepository.findRealVehicleByName(REAL_VEHICLE_THREE_NAME)).thenReturn(realVehicle3);
        when(realVehicleRepository.findAllRealVehicles())
            .thenReturn(Arrays.asList(gs01, realVehicle1, realVehicle2, realVehicle3));
        when(realVehicleRepository.findOwnRealVehicle()).thenReturn(realVehicle1);
        when(realVehicleRepository.findAllActiveRealVehiclesExceptOwn())
            .thenReturn(Arrays.asList(gs01, realVehicle2, realVehicle3));

        sut = new VirtualVehicleMapperImpl(logger, realVehicleRepository);
    }

    @DataProvider
    public Object[][] tasksThatCauseMigrationDataProvider()
    {
        return new Object[][]{
            new Object[]{47.9, -13.8, 10.0, Arrays.asList(altimeter, barometer, co2Sensor)},
        };
    }

    @Test(dataProvider = "tasksThatCauseMigrationDataProvider")
    public void shouldDecideForMigratingOfVirtualVehicles(double latitude, double longitude, double altitude,
        List<SensorDefinition> sensors)
    {
        PolarCoordinate pos = mock(PolarCoordinate.class);
        when(pos.getLatitude()).thenReturn(latitude);
        when(pos.getLongitude()).thenReturn(longitude);
        when(pos.getAltitude()).thenReturn(altitude);

        Task task = mock(Task.class);
        when(task.getPosition()).thenReturn(pos);
        when(task.getSensors()).thenReturn(sensors);

        VirtualVehicleMappingDecision decision = sut.findMappingDecision(task);

        assertThat(decision).isNotNull();
        assertThat(decision.isMigration()).isTrue();
        assertThat(decision.getRealVehicles()).containsExactly(gs01);
    }

    @DataProvider
    public Object[][] tasksThatNotCauseMigrationDataProvider()
    {
        return new Object[][]{
            new Object[]{47.9, 13.8, 10.0, Arrays.asList(altimeter, barometer, co2Sensor)},
        };
    }

    @Test(dataProvider = "tasksThatNotCauseMigrationDataProvider")
    public void shouldDecideForNotMigratingOfVirtualVehicles(double latitude, double longitude, double altitude,
        List<SensorDefinition> sensors)
    {
        PolarCoordinate pos = mock(PolarCoordinate.class);
        when(pos.getLatitude()).thenReturn(latitude);
        when(pos.getLongitude()).thenReturn(longitude);
        when(pos.getAltitude()).thenReturn(altitude);

        Task task = mock(Task.class);
        when(task.getPosition()).thenReturn(pos);
        when(task.getSensors()).thenReturn(sensors);

        VirtualVehicleMappingDecision decision = sut.findMappingDecision(task);

        assertThat(decision).isNotNull();
        assertThat(decision.isMigration()).isFalse();
    }

    @DataProvider
    public Object[][] tasksThatCauseNoMigrationBecauseOfSensorsDataProvider()
    {
        return new Object[][]{
            new Object[]{new PolarCoordinate(47.9, 13.8, 10.0), Arrays.asList(altimeter, barometer, co2Sensor)},
            new Object[]{new PolarCoordinate(47.9, 13.8, 10.0), Arrays.asList(altimeter, co2Sensor, barometer)},
            new Object[]{new PolarCoordinate(47.9, 13.8, 10.0), Arrays.asList(barometer, co2Sensor, altimeter)},
            new Object[]{new PolarCoordinate(47.9, 13.8, 10.0), Arrays.asList(barometer, altimeter, co2Sensor)},
            new Object[]{new PolarCoordinate(47.9, 13.8, 10.0), Arrays.asList(co2Sensor, altimeter, barometer)},
            new Object[]{new PolarCoordinate(47.9, 13.8, 10.0), Arrays.asList(co2Sensor, barometer, altimeter)}
        };
    }

    @Test(dataProvider = "tasksThatCauseNoMigrationBecauseOfSensorsDataProvider")
    public void shouldDecideForNoMigrationBecauseOfSensors(PolarCoordinate position, List<SensorDefinition> sensors)
    {
        Task task = mock(Task.class);
        when(task.getPosition()).thenReturn(position);
        when(task.getSensors()).thenReturn(sensors);

        VirtualVehicleMappingDecision decision = sut.findMappingDecision(task);

        assertThat(decision).isNotNull();
        assertThat(decision.isMigration()).isFalse();
    }

    @DataProvider
    public Object[][] tasksThatCauseMigrationBecauseOfSensorsDataProvider()
    {
        return new Object[][]{
            new Object[]{47.9, 13.8, 10.0, Arrays.asList(altimeter, barometer, co2Sensor, gpsReceiver)},
        };
    }

    @Test(dataProvider = "tasksThatCauseMigrationBecauseOfSensorsDataProvider")
    public void shouldDecideForMigrationBecauseOfSensors(double latitude, double longitude, double altitude,
        List<SensorDefinition> sensors)
    {
        PolarCoordinate pos = mock(PolarCoordinate.class);
        when(pos.getLatitude()).thenReturn(latitude);
        when(pos.getLongitude()).thenReturn(longitude);
        when(pos.getAltitude()).thenReturn(altitude);

        Task task = mock(Task.class);
        // when(task.getPosition()).thenReturn(new PolarCoordinate(latitude, longitude, altitude));
        when(task.getPosition()).thenReturn(pos);
        when(task.getSensors()).thenReturn(sensors);

        VirtualVehicleMappingDecision decision = sut.findMappingDecision(task);

        assertThat(decision).isNotNull();
        assertThat(decision.isMigration()).isTrue();
        assertThat(decision.getRealVehicles()).containsExactly(gs01);
    }

    @DataProvider
    public Object[][] tasksThatCauseMigrationBecauseOfPositionDataProvider()
    {
        return new Object[][]{
            new Object[]{47.9, 14.1, 10.0, Arrays.asList(altimeter, barometer, co2Sensor)},
            new Object[]{47.8, 14.2, 10.0, Arrays.asList(altimeter, co2Sensor, barometer)},
            new Object[]{47.7, 14.3, 10.0, Arrays.asList(barometer, co2Sensor, altimeter)},
            new Object[]{47.6, 14.4, 10.0, Arrays.asList(barometer, altimeter, co2Sensor)},
            new Object[]{47.2, 14.5, 10.0, Arrays.asList(co2Sensor, altimeter, barometer)},
            new Object[]{47.1, 14.6, 10.0, Arrays.asList(co2Sensor, barometer, altimeter)}
        };
    }

    @Test(dataProvider = "tasksThatCauseMigrationBecauseOfPositionDataProvider")
    public void shouldDecideForMigrationBecauseOfPosition(double latitude, double longitude, double altitude,
        List<SensorDefinition> sensors)
    {
        PolarCoordinate pos = mock(PolarCoordinate.class);
        when(pos.getLatitude()).thenReturn(latitude);
        when(pos.getLongitude()).thenReturn(longitude);
        when(pos.getAltitude()).thenReturn(altitude);

        Task task = mock(Task.class);
        when(task.getPosition()).thenReturn(pos);
        when(task.getSensors()).thenReturn(sensors);

        VirtualVehicleMappingDecision decision = sut.findMappingDecision(task);

        assertThat(decision).isNotNull();
        assertThat(decision.isMigration()).isTrue();
        assertThat(decision.getRealVehicles())
            .overridingErrorMessage("Expected migration to real vehicle %s", rvName2.getValue())
            .isNotNull()
            .containsExactly(realVehicle2);
        assertThat(decision.getRealVehicles().get(0).getName()).isNotNull().isEqualTo(rvName2.getValue());
    }

    @Test
    public void shouldHandleUnconfiguredRealVehicleName() throws JsonParseException, JsonMappingException, IOException
    {
        RealVehicleRepository realVehicleRepository2 = mock(RealVehicleRepository.class);
        Task task = mock(Task.class);

        VirtualVehicleMapperImpl localSut = new VirtualVehicleMapperImpl(logger, realVehicleRepository2);
        localSut.findMappingDecision(task);

        verifyZeroInteractions(task);
    }

    @Test
    public void shouldHandleUnconfiguredRealVehicleAreaOfOperation()
        throws JsonParseException, JsonMappingException, IOException
    {
        RealVehicle realVehicle3 = mock(RealVehicle.class);

        RealVehicleRepository realVehicleRepository2 = mock(RealVehicleRepository.class);
        when(realVehicleRepository2.findOwnRealVehicle()).thenReturn(realVehicle3);

        Task task = mock(Task.class);

        VirtualVehicleMapperImpl localSut = new VirtualVehicleMapperImpl(logger, realVehicleRepository2);
        VirtualVehicleMappingDecision actual = localSut.findMappingDecision(task);

        assertThat(actual.isMigration()).isTrue();
        assertThat(actual.getRealVehicles()).isEmpty();

        verify(realVehicle3).getAreaOfOperation();
    }

    @Test
    public void shouldHandleBrokenRealVehicleAreaOfOperation()
        throws JsonParseException, JsonMappingException, IOException
    {
        RealVehicle realVehicle3 = mock(RealVehicle.class);
        when(realVehicle3.getAreaOfOperation()).thenReturn("buggerit!");

        RealVehicleRepository rvRepo2 = mock(RealVehicleRepository.class);
        when(rvRepo2.findOwnRealVehicle()).thenReturn(realVehicle3);

        Task task = mock(Task.class);

        VirtualVehicleMapperImpl localSut = new VirtualVehicleMapperImpl(logger, rvRepo2);
        VirtualVehicleMappingDecision actual = localSut.findMappingDecision(task);

        assertThat(actual.isMigration()).isTrue();
        assertThat(actual.getRealVehicles()).isEmpty();

        verify(realVehicle3).getAreaOfOperation();
    }
}
