// This code is part of the CPCC-NG project.
//
// Copyright (c) 2015 Clemens Krainer <clemens.krainer@gmail.com>
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

package cpcc.rv.base.services;

/**
 * Real vehicle base constants.
 */
public final class RealVehicleBaseConstants
{
    public static final String CONFIGURATION_UPDATE_CONNECTOR = "configuration.update";
    public static final String CONFIGURATION_UPDATE_PATH = "/rvbase/update/configuration";

    public static final String REAL_VEHICLE_STATUS_CONNECTOR = "vehicle.status";
    public static final String REAL_VEHICLE_STATUS_PATH = "/rvbase/update/status/pos-vvs-tsk-sen";

    public static final String JOB_QUEUE_NAME = "RV Base";

    public static final String RV_BASE_JOB_POOL_THREADS = "rv-base.job.pool.threads";
    public static final String RV_BASE_JOB_POOL_THREADS_DEFAULT = "5";

    public static final String JOB_MODE_CONFIG = "config";
    public static final String JOB_MODE_IMPORT = "import";
    public static final String JOB_MODE_STATUS = "status";
    public static final String JOB_MODE_INIT = "init";

    private RealVehicleBaseConstants()
    {
        // Intentionally empty.
    }
}
