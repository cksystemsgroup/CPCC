/*
 * This code is part of the CPCC-NG project.
 *
 * Copyright (c) 2013 Clemens Krainer <clemens.krainer@gmail.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package at.uni_salzburg.cs.cpcc.vvrte.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;

import at.uni_salzburg.cs.cpcc.persistence.entities.Parameter;
import at.uni_salzburg.cs.cpcc.persistence.entities.RealVehicle;
import at.uni_salzburg.cs.cpcc.persistence.services.QueryManager;
import at.uni_salzburg.cs.cpcc.utilities.PolarCoordinate;
import at.uni_salzburg.cs.cpcc.vvrte.task.Task;

/**
 * VirtualVehicleMapperImpl
 */
public class VirtualVehicleMapperImpl implements VirtualVehicleMapper
{
    private QueryManager qm;
    private String rvName;
    private Map<String, RealVehicle> realVehicleMap;
    private Map<String, PolygonZone> areaOfOperationMap;

    /**
     * @param qm the query manager.
     */
    public VirtualVehicleMapperImpl(QueryManager qm)
    {
        this.qm = qm;
        init();
    }

    private void init()
    {
        Parameter rvNameParam = qm.findParameterByName(Parameter.REAL_VEHICLE_NAME);
        rvName = rvNameParam.getValue();

        Map<String, RealVehicle> rvMap = new HashMap<String, RealVehicle>();
        Map<String, PolygonZone> zoneMap = new HashMap<String, PolygonZone>();

        List<RealVehicle> realVehicleList = qm.findAllRealVehicles();

        for (RealVehicle realVehicle : realVehicleList)
        {
            String areaOfOperationString = realVehicle.getAreaOfOperation();

            JSONArray polygon = new JSONArray(areaOfOperationString);

            PolarCoordinate[] coordinates = new PolarCoordinate[polygon.length()];
            for (int k = 0, l = polygon.length(); k < l; ++k)
            {
                JSONObject point = (JSONObject) polygon.get(k);
                double lat = point.getDouble("lat");
                double lon = point.getDouble("lon");
                coordinates[k] = new PolarCoordinate(lat, lon, 0.0);
            }

            PolygonZone areaOfOperation = new PolygonZone(coordinates);

            rvMap.put(realVehicle.getName(), realVehicle);
            zoneMap.put(realVehicle.getName(), areaOfOperation);
        }

        realVehicleMap = rvMap;
        areaOfOperationMap = zoneMap;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VirtualVehicleMappingDecision findMappingDecision(Task task)
    {
        VirtualVehicleMappingDecision decision = new VirtualVehicleMappingDecision();
        decision.setTask(task);

        boolean migration = !areaOfOperationMap.get(rvName).isInside(task.getPosition());

        if (!migration)
        {
            RealVehicle realVehicle = realVehicleMap.get(rvName);
            migration = !realVehicle.getSensors().containsAll(task.getSensors());
        }

        decision.setMigration(migration);

        if (migration)
        {
            List<RealVehicle> destinationRealVehicles = new ArrayList<RealVehicle>();
            for (Entry<String, RealVehicle> entry : realVehicleMap.entrySet())
            {
                PolygonZone areaOfOperation = areaOfOperationMap.get(entry.getKey());
                if (!areaOfOperation.isInside(task.getPosition()))
                {
                    continue;
                }

                if (!entry.getValue().getSensors().containsAll(task.getSensors()))
                {
                    continue;
                }

                destinationRealVehicles.add(entry.getValue());
            }

            decision.setRealVehicles(destinationRealVehicles);
        }

        return decision;
    }

}