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

import java.util.Arrays;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import cpcc.core.entities.RealVehicle;
import cpcc.vvrte.base.VirtualVehicleMappingDecision;
import cpcc.vvrte.entities.Task;

/**
 * VirtualVehicleMappingDecisionTest
 */
public class VirtualVehicleMappingDecisionTest
{
    private VirtualVehicleMappingDecision decision;

    @BeforeMethod
    public void setUp()
    {
        decision = new VirtualVehicleMappingDecision();
    }

    @DataProvider
    public Object[][] mappingDataProvider()
    {
        return new Object[][]{
            new Object[]{false, mock(Task.class), new RealVehicle[]{mock(RealVehicle.class)}},
            new Object[]{false, mock(Task.class), new RealVehicle[]{mock(RealVehicle.class)}},
            new Object[]{true, mock(Task.class), new RealVehicle[]{mock(RealVehicle.class)}},
            new Object[]{true, mock(Task.class), new RealVehicle[]{mock(RealVehicle.class)}},
        };
    }

    @Test(dataProvider = "mappingDataProvider")
    public void shouldStoreData(boolean migration, Task task, RealVehicle[] realVehicle)
    {
        decision.setMigration(migration);
        decision.setTask(task);
        decision.setRealVehicles(Arrays.asList(realVehicle));

        assertThat(decision.isMigration()).isEqualTo(migration);
        assertThat(decision.getTask()).isNotNull().isEqualTo(task);
        assertThat(decision.getRealVehicles()).isNotNull().containsExactly(realVehicle);
    }
}
