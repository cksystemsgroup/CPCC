// This code is part of the JNavigator project.
//
// Copyright (c) 2009-2013 Clemens Krainer <clemens.krainer@gmail.com>
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

package cpcc.core.entities;

import java.io.Serializable;

import javax.persistence.Embeddable;

/**
 * This class implements a 3-tuple to describe a position in a polar coordinate system.
 */
@Embeddable
public class PolarCoordinate implements Serializable
{
    private static final long serialVersionUID = 2879155483936546557L;

    /**
     * The latitude value of this vector.
     */
    private double latitude;

    /**
     * The longitude value of this vector.
     */
    private double longitude;

    /**
     * The altitude value of this vector.
     */
    private double altitude;

    /**
     * Construct a null position vector, i.e. latitude, longitude and altitude are zero.
     */
    public PolarCoordinate()
    {
        latitude = 0;
        longitude = 0;
        altitude = 0;
    }

    /**
     * Construct a position vector as a copy of another position vector.
     * 
     * @param p the position vector to be copied.
     */
    public PolarCoordinate(PolarCoordinate p)
    {
        this.latitude = p.latitude;
        this.longitude = p.longitude;
        this.altitude = p.altitude;
    }

    /**
     * Construct a position vector with the given values for latitude, longitude and altitude.
     * 
     * @param latitude the latitude value.
     * @param longitude the longitude value.
     * @param altitude the altitude value.
     */
    public PolarCoordinate(double latitude, double longitude, double altitude)
    {
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
    }

    /**
     * Return the latitude value.
     * 
     * @return the latitude value.
     */
    public double getLatitude()
    {
        return latitude;
    }

    /**
     * Set the latitude value.
     * 
     * @param latitude the new latitude value
     */
    public void setLatitude(double latitude)
    {
        this.latitude = latitude;
    }

    /**
     * Return the longitude value.
     * 
     * @return the longitude value.
     */
    public double getLongitude()
    {
        return longitude;
    }

    /**
     * Set the longitude value.
     * 
     * @param longitude the new longitude value
     */
    public void setLongitude(double longitude)
    {
        this.longitude = longitude;
    }

    /**
     * Return the altitude value.
     * 
     * @return the altitude value.
     */
    public double getAltitude()
    {
        return altitude;
    }

    /**
     * Set the altitude value.
     * 
     * @param altitude the new altitude value
     */
    public void setAltitude(double altitude)
    {
        this.altitude = altitude;
    }

    /**
     * Copy the values of another PolarCoordinate vector to this vector.
     * 
     * @param p the other PolarCoordinate vector.
     */
    public void set(PolarCoordinate p)
    {
        latitude = p.latitude;
        longitude = p.longitude;
        altitude = p.altitude;
    }

    /**
     * {@inheritDoc}
     */
    public String toString()
    {
        return "(" + latitude + "\u00B0, " + longitude + "\u00B0, " + altitude + "m)";
    }

}
