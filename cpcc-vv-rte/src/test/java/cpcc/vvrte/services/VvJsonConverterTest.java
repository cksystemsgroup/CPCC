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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;

import org.apache.tapestry5.json.JSONArray;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import cpcc.core.entities.SensorDefinition;
import cpcc.core.entities.SensorType;
import cpcc.core.entities.SensorVisibility;
import cpcc.core.services.CoreJsonConverter;
import cpcc.core.services.CoreJsonConverterImpl;
import cpcc.vvrte.entities.VirtualVehicle;
import cpcc.vvrte.entities.VirtualVehicleState;
import cpcc.vvrte.task.Task;

public class VvJsonConverterTest
{
    private VvJsonConverter converter;
    private VirtualVehicle vv1 = mock(VirtualVehicle.class);
    private VirtualVehicle vv2 = mock(VirtualVehicle.class);
    private VirtualVehicle vv3 = mock(VirtualVehicle.class);
    private VirtualVehicle vv4 = mock(VirtualVehicle.class);

    private SensorDefinition s1 = mock(SensorDefinition.class);
    private SensorDefinition s2 = mock(SensorDefinition.class);
    private SensorDefinition s3 = mock(SensorDefinition.class);
    private SensorDefinition s4 = mock(SensorDefinition.class);

    private Task task1 = mock(Task.class);
    private Task task2 = mock(Task.class);
    private Task task3 = mock(Task.class);
    private Task task4 = mock(Task.class);

    @BeforeMethod
    public void setUp()
    {
        CoreJsonConverter pjc = new CoreJsonConverterImpl();
        converter = new VvJsonConverterImpl(pjc);

        when(vv1.getName()).thenReturn("vv1");
        when(vv1.getUuid()).thenReturn("19a43d42-669a-11e3-a337-df369887df3e");
        when(vv1.getState()).thenReturn(VirtualVehicleState.RUNNING);

        when(vv2.getName()).thenReturn("vv2");
        when(vv2.getUuid()).thenReturn("1d50b380-669a-11e3-9008-471c9c51252f");
        when(vv2.getState()).thenReturn(VirtualVehicleState.DEFECTIVE);

        when(vv3.getName()).thenReturn("vv3");
        when(vv3.getUuid()).thenReturn("2088ea36-669a-11e3-b371-13460b0b688a");
        when(vv3.getState()).thenReturn(VirtualVehicleState.FINISHED);

        when(vv4.getName()).thenReturn("vv4");
        when(vv4.getUuid()).thenReturn("235a7d38-669a-11e3-8672-5be62c412e2e");
        when(vv4.getState()).thenReturn(VirtualVehicleState.MIGRATING);

        when(s1.getId()).thenReturn(1);
        when(s1.getDescription()).thenReturn("Altimeter");
        when(s1.getType()).thenReturn(SensorType.ALTIMETER);
        when(s1.getMessageType()).thenReturn("std_msgs/Float32");
        when(s1.getVisibility()).thenReturn(SensorVisibility.ALL_VV);
        when(s1.getParameters()).thenReturn("random=10:35");
        when(s1.getLastUpdate()).thenReturn(new Date(1000001));

        when(s2.getId()).thenReturn(2);
        when(s2.getDescription()).thenReturn("Barometer");
        when(s2.getType()).thenReturn(SensorType.BAROMETER);
        when(s2.getMessageType()).thenReturn("std_msgs/Float32");
        when(s2.getVisibility()).thenReturn(SensorVisibility.NO_VV);
        when(s2.getParameters()).thenReturn("random=1050:1080");
        when(s2.getLastUpdate()).thenReturn(new Date(2000002));

        when(s3.getId()).thenReturn(3);
        when(s3.getDescription()).thenReturn("Belly Mounted Camera 640x480");
        when(s3.getType()).thenReturn(SensorType.CAMERA);
        when(s3.getMessageType()).thenReturn("sensor_msgs/Image");
        when(s3.getVisibility()).thenReturn(SensorVisibility.ALL_VV);
        when(s3.getParameters()).thenReturn("width=640 height=480 yaw=0 down=1.571 alignment=north");
        when(s3.getLastUpdate()).thenReturn(new Date(3000003));

        when(s4.getId()).thenReturn(4);
        when(s4.getDescription()).thenReturn("GPS");
        when(s4.getType()).thenReturn(SensorType.GPS);
        when(s4.getMessageType()).thenReturn("sensor_msgs/NavSatFix");
        when(s4.getVisibility()).thenReturn(SensorVisibility.PRIVILEGED_VV);
        when(s4.getParameters()).thenReturn("");
        when(s4.getLastUpdate()).thenReturn(new Date(4000004));

        when(task1.getLatitude()).thenReturn(47.1234);
        when(task1.getLongitude()).thenReturn(13.4321);
        when(task1.getAltitude()).thenReturn(10.0);
        when(task1.getTolerance()).thenReturn(10.1);
        when(task1.getSensors()).thenReturn(Arrays.asList(s1));

        when(task2.getLatitude()).thenReturn(47.2345);
        when(task2.getLongitude()).thenReturn(13.5432);
        when(task2.getAltitude()).thenReturn(20.0);
        when(task2.getTolerance()).thenReturn(3.7);
        when(task2.getSensors()).thenReturn(Arrays.asList(s2, s1));

        when(task3.getLatitude()).thenReturn(47.3456);
        when(task3.getLongitude()).thenReturn(13.6543);
        when(task3.getAltitude()).thenReturn(30.0);
        when(task3.getTolerance()).thenReturn(7.9);
        when(task3.getSensors()).thenReturn(Arrays.asList(s3, s2, s1));

        when(task4.getLatitude()).thenReturn(47.4567);
        when(task4.getLongitude()).thenReturn(13.7654);
        when(task4.getAltitude()).thenReturn(40.0);
        when(task4.getTolerance()).thenReturn(1.6);
        when(task4.getSensors()).thenReturn(Arrays.asList(s4, s3, s2, s1));
    }

    @DataProvider
    public Object[][] vehiclesDataProvider()
    {
        return new Object[][]{
            new Object[]{
                new VirtualVehicle[]{}, "[]"
            },
            new Object[]{
                new VirtualVehicle[]{vv1}, "["
                    + "{\"name\":\"vv1\",\"state\":\"RUNNING\",\"uuid\":\"19a43d42-669a-11e3-a337-df369887df3e\"}"
                    + "]"
            },
            new Object[]{
                new VirtualVehicle[]{vv1, vv2}, "["
                    + "{\"name\":\"vv1\",\"state\":\"RUNNING\",\"uuid\":\"19a43d42-669a-11e3-a337-df369887df3e\"},"
                    + "{\"name\":\"vv2\",\"state\":\"DEFECTIVE\",\"uuid\":\"1d50b380-669a-11e3-9008-471c9c51252f\"}"
                    + "]"
            },
            new Object[]{
                new VirtualVehicle[]{vv1, vv2, vv3}, "["
                    + "{\"name\":\"vv1\",\"state\":\"RUNNING\",\"uuid\":\"19a43d42-669a-11e3-a337-df369887df3e\"},"
                    + "{\"name\":\"vv2\",\"state\":\"DEFECTIVE\",\"uuid\":\"1d50b380-669a-11e3-9008-471c9c51252f\"},"
                    + "{\"name\":\"vv3\",\"state\":\"FINISHED\",\"uuid\":\"2088ea36-669a-11e3-b371-13460b0b688a\"}"
                    + "]"
            },
            new Object[]{
                new VirtualVehicle[]{vv1, vv2, vv3, vv4}, "["
                    + "{\"name\":\"vv1\",\"state\":\"RUNNING\",\"uuid\":\"19a43d42-669a-11e3-a337-df369887df3e\"},"
                    + "{\"name\":\"vv2\",\"state\":\"DEFECTIVE\",\"uuid\":\"1d50b380-669a-11e3-9008-471c9c51252f\"},"
                    + "{\"name\":\"vv3\",\"state\":\"FINISHED\",\"uuid\":\"2088ea36-669a-11e3-b371-13460b0b688a\"},"
                    + "{\"name\":\"vv4\",\"state\":\"MIGRATING\",\"uuid\":\"235a7d38-669a-11e3-8672-5be62c412e2e\"}"
                    + "]"
            },
        };
    }

    @Test(dataProvider = "vehiclesDataProvider")
    public void shouldConvertVehicles(VirtualVehicle[] vehicles, String expectedJsonString) throws JSONException
    {
        JSONArray result = converter.toJsonArray(vehicles);

        JSONAssert.assertEquals(expectedJsonString, result.toString(true), false);
    }

    @DataProvider
    public Object[][] tasksDataProvider()
    {
        return new Object[][]{
            new Object[]{
                new Task[]{}, "[]"
            },
            new Object[]{
                new Task[]{task1}, "["
                    + "{\"sensors\":["
                    + "{\"id\":\"1\","
                    + "\"visibility\":\"ALL_VV\","
                    + "\"lastUpdate\":1000001,"
                    + "\"description\":\"Altimeter\","
                    + "\"parameters\":\"random=10:35\","
                    + "\"messageType\":\"std_msgs/Float32\",\"type\":\"ALTIMETER\",\"deleted\":false}],"
                    + "\"position\":{\"lon\":\"13.43210000\",\"alt\":\"10.000\",\"lat\":\"47.12340000\"},"
                    + "\"tolerance\":\"10.1\"}"
                    + "]"
            },
            new Object[]{
                new Task[]{task2, task3}, "["
                    + "{\"sensors\":["
                    + "{\"id\":\"2\","
                    + "\"visibility\":\"NO_VV\","
                    + "\"lastUpdate\":2000002,"
                    + "\"description\":\"Barometer\","
                    + "\"parameters\":\"random=1050:1080\","
                    + "\"messageType\":\"std_msgs/Float32\","
                    + "\"type\":\"BAROMETER\",\"deleted\":false},"
                    + "{\"id\":\"1\","
                    + "\"visibility\":\"ALL_VV\","
                    + "\"lastUpdate\":1000001,"
                    + "\"description\":\"Altimeter\","
                    + "\"parameters\":\"random=10:35\","
                    + "\"messageType\":\"std_msgs/Float32\","
                    + "\"type\":\"ALTIMETER\",\"deleted\":false}],"
                    + "\"position\":{\"lon\":\"13.54320000\",\"alt\":\"20.000\",\"lat\":\"47.23450000\"},"
                    + "\"tolerance\":\"3.7\"},"

                    + "{\"sensors\":["
                    + "{\"id\":\"3\","
                    + "\"visibility\":\"ALL_VV\","
                    + "\"lastUpdate\":3000003,"
                    + "\"description\":\"Belly Mounted Camera 640x480\","
                    + "\"parameters\":\"width=640 height=480 yaw=0 down=1.571 alignment=north\","
                    + "\"messageType\":\"sensor_msgs/Image\","
                    + "\"type\":\"CAMERA\",\"deleted\":false},"
                    + "{\"id\":\"2\","
                    + "\"visibility\":\"NO_VV\","
                    + "\"lastUpdate\":2000002,"
                    + "\"description\":\"Barometer\","
                    + "\"parameters\":\"random=1050:1080\","
                    + "\"messageType\":\"std_msgs/Float32\","
                    + "\"type\":\"BAROMETER\",\"deleted\":false},"
                    + "{\"id\":\"1\","
                    + "\"visibility\":\"ALL_VV\","
                    + "\"lastUpdate\":1000001,"
                    + "\"description\":\"Altimeter\","
                    + "\"parameters\":\"random=10:35\","
                    + "\"messageType\":\"std_msgs/Float32\","
                    + "\"type\":\"ALTIMETER\",\"deleted\":false}],"
                    + "\"position\":{\"lon\":\"13.65430000\",\"alt\":\"30.000\",\"lat\":\"47.34560000\"},"
                    + "\"tolerance\":\"7.9\"}"

                    + "]"
            },
            new Object[]{
                new Task[]{task3, task4, task1}, "["
                    + "{\"sensors\":["
                    + "{\"id\":\"3\","
                    + "\"visibility\":\"ALL_VV\","
                    + "\"lastUpdate\":3000003,"
                    + "\"description\":\"Belly Mounted Camera 640x480\","
                    + "\"parameters\":\"width=640 height=480 yaw=0 down=1.571 alignment=north\","
                    + "\"messageType\":\"sensor_msgs/Image\","
                    + "\"type\":\"CAMERA\",\"deleted\":false},"
                    + "{\"id\":\"2\","
                    + "\"visibility\":\"NO_VV\","
                    + "\"lastUpdate\":2000002,"
                    + "\"description\":\"Barometer\","
                    + "\"parameters\":\"random=1050:1080\","
                    + "\"messageType\":\"std_msgs/Float32\","
                    + "\"type\":\"BAROMETER\",\"deleted\":false},"
                    + "{\"id\":\"1\","
                    + "\"visibility\":\"ALL_VV\","
                    + "\"lastUpdate\":1000001,"
                    + "\"description\":\"Altimeter\","
                    + "\"parameters\":\"random=10:35\","
                    + "\"messageType\":\"std_msgs/Float32\","
                    + "\"type\":\"ALTIMETER\",\"deleted\":false}],"
                    + "\"position\":{\"lon\":\"13.65430000\",\"alt\":\"30.000\",\"lat\":\"47.34560000\"},"
                    + "\"tolerance\":\"7.9\"},"

                    + "{\"sensors\":["
                    + "{\"id\":\"4\","
                    + "\"visibility\":\"PRIVILEGED_VV\","
                    + "\"lastUpdate\":4000004,"
                    + "\"description\":\"GPS\","
                    + "\"parameters\":\"\","
                    + "\"messageType\":\"sensor_msgs/NavSatFix\","
                    + "\"type\":\"GPS\",\"deleted\":false},"
                    + "{\"id\":\"3\","
                    + "\"visibility\":\"ALL_VV\","
                    + "\"lastUpdate\":3000003,"
                    + "\"description\":\"Belly Mounted Camera 640x480\","
                    + "\"parameters\":\"width=640 height=480 yaw=0 down=1.571 alignment=north\","
                    + "\"messageType\":\"sensor_msgs/Image\","
                    + "\"type\":\"CAMERA\",\"deleted\":false},"
                    + "{\"id\":\"2\","
                    + "\"visibility\":\"NO_VV\","
                    + "\"lastUpdate\":2000002,"
                    + "\"description\":\"Barometer\","
                    + "\"parameters\":\"random=1050:1080\","
                    + "\"messageType\":\"std_msgs/Float32\""
                    + ",\"type\":\"BAROMETER\",\"deleted\":false},"
                    + "{\"id\":\"1\","
                    + "\"visibility\":\"ALL_VV\","
                    + "\"lastUpdate\":1000001,"
                    + "\"description\":\"Altimeter\","
                    + "\"parameters\":\"random=10:35\","
                    + "\"messageType\":\"std_msgs/Float32\","
                    + "\"type\":\"ALTIMETER\",\"deleted\":false}],"
                    + "\"position\":{\"lon\":\"13.76540000\",\"alt\":\"40.000\",\"lat\":\"47.45670000\"},"
                    + "\"tolerance\":\"1.6\"},"

                    + "{\"sensors\":["
                    + "{\"id\":\"1\","
                    + "\"visibility\":\"ALL_VV\","
                    + "\"lastUpdate\":1000001,"
                    + "\"description\":\"Altimeter\","
                    + "\"parameters\":\"random=10:35\","
                    + "\"messageType\":\"std_msgs/Float32\","
                    + "\"type\":\"ALTIMETER\",\"deleted\":false}],"
                    + "\"position\":{\"lon\":\"13.43210000\",\"alt\":\"10.000\",\"lat\":\"47.12340000\"},"
                    + "\"tolerance\":\"10.1\"}"

                    + "]"
            },
            new Object[]{
                new Task[]{task4, task1, task2, task3}, "["

                    + "{\"sensors\":["
                    + "{\"id\":\"4\","
                    + "\"visibility\":\"PRIVILEGED_VV\","
                    + "\"lastUpdate\":4000004,"
                    + "\"description\":\"GPS\","
                    + "\"parameters\":\"\","
                    + "\"messageType\":\"sensor_msgs/NavSatFix\","
                    + "\"type\":\"GPS\",\"deleted\":false},"
                    + "{\"id\":\"3\","
                    + "\"visibility\":\"ALL_VV\","
                    + "\"lastUpdate\":3000003,"
                    + "\"description\":\"Belly Mounted Camera 640x480\","
                    + "\"parameters\":\"width=640 height=480 yaw=0 down=1.571 alignment=north\","
                    + "\"messageType\":\"sensor_msgs/Image\","
                    + "\"type\":\"CAMERA\",\"deleted\":false},"
                    + "{\"id\":\"2\","
                    + "\"visibility\":\"NO_VV\","
                    + "\"lastUpdate\":2000002,"
                    + "\"description\":\"Barometer\","
                    + "\"parameters\":\"random=1050:1080\","
                    + "\"messageType\":\"std_msgs/Float32\""
                    + ",\"type\":\"BAROMETER\",\"deleted\":false},"
                    + "{\"id\":\"1\","
                    + "\"visibility\":\"ALL_VV\","
                    + "\"lastUpdate\":1000001,"
                    + "\"description\":\"Altimeter\","
                    + "\"parameters\":\"random=10:35\","
                    + "\"messageType\":\"std_msgs/Float32\","
                    + "\"type\":\"ALTIMETER\",\"deleted\":false}],"
                    + "\"position\":{\"lon\":\"13.76540000\",\"alt\":\"40.000\",\"lat\":\"47.45670000\"},"
                    + "\"tolerance\":\"1.6\"},"

                    + "{\"sensors\":["
                    + "{\"id\":\"1\","
                    + "\"visibility\":\"ALL_VV\","
                    + "\"lastUpdate\":1000001,"
                    + "\"description\":\"Altimeter\","
                    + "\"parameters\":\"random=10:35\","
                    + "\"messageType\":\"std_msgs/Float32\","
                    + "\"type\":\"ALTIMETER\",\"deleted\":false}],"
                    + "\"position\":{\"lon\":\"13.43210000\",\"alt\":\"10.000\",\"lat\":\"47.12340000\"},"
                    + "\"tolerance\":\"10.1\"},"

                    + "{\"sensors\":["
                    + "{\"id\":\"2\","
                    + "\"visibility\":\"NO_VV\","
                    + "\"lastUpdate\":2000002,"
                    + "\"description\":\"Barometer\","
                    + "\"parameters\":\"random=1050:1080\","
                    + "\"messageType\":\"std_msgs/Float32\","
                    + "\"type\":\"BAROMETER\",\"deleted\":false},"
                    + "{\"id\":\"1\","
                    + "\"visibility\":\"ALL_VV\","
                    + "\"lastUpdate\":1000001,"
                    + "\"description\":\"Altimeter\","
                    + "\"parameters\":\"random=10:35\","
                    + "\"messageType\":\"std_msgs/Float32\","
                    + "\"type\":\"ALTIMETER\",\"deleted\":false}],"
                    + "\"position\":{\"lon\":\"13.54320000\",\"alt\":\"20.000\",\"lat\":\"47.23450000\"},"
                    + "\"tolerance\":\"3.7\"},"

                    + "{\"sensors\":["
                    + "{\"id\":\"3\","
                    + "\"visibility\":\"ALL_VV\","
                    + "\"lastUpdate\":3000003,"
                    + "\"description\":\"Belly Mounted Camera 640x480\","
                    + "\"parameters\":\"width=640 height=480 yaw=0 down=1.571 alignment=north\","
                    + "\"messageType\":\"sensor_msgs/Image\","
                    + "\"type\":\"CAMERA\",\"deleted\":false},"
                    + "{\"id\":\"2\","
                    + "\"visibility\":\"NO_VV\","
                    + "\"lastUpdate\":2000002,"
                    + "\"description\":\"Barometer\","
                    + "\"parameters\":\"random=1050:1080\","
                    + "\"messageType\":\"std_msgs/Float32\","
                    + "\"type\":\"BAROMETER\",\"deleted\":false},"
                    + "{\"id\":\"1\","
                    + "\"visibility\":\"ALL_VV\","
                    + "\"lastUpdate\":1000001,"
                    + "\"description\":\"Altimeter\","
                    + "\"parameters\":\"random=10:35\","
                    + "\"messageType\":\"std_msgs/Float32\","
                    + "\"type\":\"ALTIMETER\",\"deleted\":false}],"
                    + "\"position\":{\"lon\":\"13.65430000\",\"alt\":\"30.000\",\"lat\":\"47.34560000\"},"
                    + "\"tolerance\":\"7.9\"}"

                    + "]"
            },
        };
    }

    @Test(dataProvider = "tasksDataProvider")
    public void shouldConvertTasks(Task[] tasks, String expectedJsonString) throws JSONException
    {
        JSONArray result = converter.toJsonArray(tasks);

        JSONAssert.assertEquals(expectedJsonString, result.toString(true), false);
    }
}
