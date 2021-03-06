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

import java.io.IOException;

import cpcc.vvrte.entities.VirtualVehicleState;
import cpcc.vvrte.services.js.JavascriptWorkerStateListener;
import cpcc.vvrte.services.task.TaskCompletionListener;

/**
 * VirtualVehicleLauncher
 */
public interface VirtualVehicleLauncher extends JavascriptWorkerStateListener, TaskCompletionListener
{
    /**
     * @param vehicleId the ID of the vehicle to launch.
     * @throws VirtualVehicleLaunchException thrown in case of errors.
     * @throws IOException thrown in case of errors.
     */
    void start(int vehicleId) throws VirtualVehicleLaunchException, IOException;

    /**
     * @param vehicleId the ID of the vehicle to launch.
     * @param newState the new vehicle state.
     * @throws VirtualVehicleLaunchException thrown in case of errors.
     * @throws IOException thrown in case of errors.
     */
    void stop(int vehicleId, VirtualVehicleState newState) throws VirtualVehicleLaunchException, IOException;

    /**
     * @param vehicleId the ID of the vehicle to launch.
     * @throws VirtualVehicleLaunchException thrown in case of errors.
     * @throws IOException thrown in case of errors.
     */
    void resume(int vehicleId) throws VirtualVehicleLaunchException, IOException;

    /**
     * @param vehicleId the ID of the vehicle.
     * @param newState the new vehicle state.
     */
    void stateChange(int vehicleId, VirtualVehicleState newState);

    /**
     * Handle unsuccessful migrations.
     */
    void handleStuckMigrations();
}
