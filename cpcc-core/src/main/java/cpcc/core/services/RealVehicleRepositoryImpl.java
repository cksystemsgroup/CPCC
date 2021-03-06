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

package cpcc.core.services;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

import cpcc.core.entities.Parameter;
import cpcc.core.entities.RealVehicle;
import cpcc.core.entities.RealVehicleState;
import cpcc.core.entities.RealVehicleType;
import cpcc.core.services.jobs.TimeService;

/**
 * Real Vehicle Repository Implementation.
 */
public class RealVehicleRepositoryImpl implements RealVehicleRepository
{
    private static final int TOO_OLD_TO_REMEMBER = 86400000;

    private static final String LAST_UPDATE = "lastUpdate";
    private static final String TYPE = "type";
    private static final String DELETED = "deleted";
    private static final String ID = "id";
    private static final String REAL_VEHICLE_NAME = "name";
    private static final String REAL_VEHICLE_URL = "url";

    private Session session;
    private QueryManager qm;
    private TimeService timeService;
    private long connectionTimeout;

    /**
     * @param session the Hibernate {@link Session}
     * @param qm the query manager instance.
     * @param timeService the time service instance.
     */
    public RealVehicleRepositoryImpl(Session session, QueryManager qm, TimeService timeService)
    {
        this.session = session;
        this.qm = qm;
        this.timeService = timeService;
        this.connectionTimeout = 10000L;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<RealVehicle> findAllRealVehicles()
    {
        return session
            .createCriteria(RealVehicle.class)
            .addOrder(Property.forName(ID).asc())
            .list();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<RealVehicle> findAllActiveRealVehicles()
    {
        return session
            .createCriteria(RealVehicle.class)
            .add(Restrictions.eq(DELETED, Boolean.FALSE))
            .addOrder(Property.forName(ID).asc())
            .list();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<RealVehicle> findAllGroundStations()
    {
        return session
            .createCriteria(RealVehicle.class, "rv")
            .add(Restrictions.eq(TYPE, RealVehicleType.GROUND_STATION))
            .list();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<RealVehicle> findAllActiveRealVehiclesExceptOwn()
    {
        Parameter rvNameParam = qm.findParameterByName(Parameter.REAL_VEHICLE_NAME);
        if (rvNameParam == null)
        {
            return findAllActiveRealVehicles();
        }

        return session
            .createCriteria(RealVehicle.class)
            .add(Restrictions.not(Restrictions.eq(REAL_VEHICLE_NAME, rvNameParam.getValue())))
            .add(Restrictions.eq(DELETED, Boolean.FALSE))
            .list();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<RealVehicle> findAllRealVehiclesOrderByName()
    {
        return session
            .createCriteria(RealVehicle.class)
            .addOrder(Property.forName(REAL_VEHICLE_NAME).asc())
            .list();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public RealVehicle findRealVehicleByName(String name)
    {
        List<RealVehicle> rvList = session
            .createCriteria(RealVehicle.class)
            .add(Restrictions.eq(REAL_VEHICLE_NAME, name))
            .add(Restrictions.eq(DELETED, Boolean.FALSE))
            .list();

        return !rvList.isEmpty() ? rvList.get(0) : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RealVehicle findRealVehicleByUrl(String url)
    {
        return (RealVehicle) session
            .createCriteria(RealVehicle.class)
            .add(Restrictions.eq(REAL_VEHICLE_URL, url))
            .uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RealVehicle findRealVehicleById(Integer id)
    {
        return (RealVehicle) session
            .createCriteria(RealVehicle.class)
            .add(Restrictions.eq(ID, id))
            .uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RealVehicle findOwnRealVehicle()
    {
        Parameter rvNameParam = qm.findParameterByName(Parameter.REAL_VEHICLE_NAME);
        return rvNameParam != null ? findRealVehicleByName(rvNameParam.getValue()) : null;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<RealVehicleState> findAllRealVehicleStates()
    {
        return session
            .createCriteria(RealVehicleState.class)
            .list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RealVehicleState findRealVehicleStateById(int id)
    {
        return (RealVehicleState) session
            .createCriteria(RealVehicleState.class)
            .add(Restrictions.eq(ID, id))
            .uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void cleanupOldVehicleStates()
    {
        @SuppressWarnings("unchecked")
        List<RealVehicleState> oldRvStates = session
            .createCriteria(RealVehicleState.class)
            .add(Restrictions.le(LAST_UPDATE, new Date(timeService.currentTimeMillis() - TOO_OLD_TO_REMEMBER)))
            .list();

        oldRvStates.stream().forEach(x -> session.delete(x));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isRealVehicleConnected(int id)
    {
        RealVehicleState state = findRealVehicleStateById(id);
        return state != null && timeService.currentTimeMillis() - state.getLastUpdate().getTime() < connectionTimeout;
    }
}
