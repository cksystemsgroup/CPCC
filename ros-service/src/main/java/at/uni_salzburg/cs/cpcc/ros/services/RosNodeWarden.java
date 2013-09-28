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
package at.uni_salzburg.cs.cpcc.ros.services;

import java.net.URI;
import java.util.Map;

import at.uni_salzburg.cs.cpcc.ros.base.AbstractRosAdapter;

/**
 * RosNodeWarden
 */
public interface RosNodeWarden
{
    /**
     * @return the current master server URI.
     */
    URI getMasterURI();

    /**
     * @param masterURI the new master server URI.
     */
    void setMasterURI(URI masterURI);

    /**
     * @param node the ROS node to be added.
     */
    void addRosNode(AbstractRosAdapter node);

    /**
     * @param node the ROS node to be removed.
     */
    void removeRosNode(AbstractRosAdapter node);

    /**
     * @return the ROS adapter map
     */
    Map<String, AbstractRosAdapter> getAdapterMap();

}