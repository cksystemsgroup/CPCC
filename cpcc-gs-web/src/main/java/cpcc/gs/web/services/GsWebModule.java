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

package cpcc.gs.web.services;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.commons.MappedConfiguration;
import org.apache.tapestry5.ioc.annotations.Startup;
import org.apache.tapestry5.ioc.services.cron.CronSchedule;
import org.apache.tapestry5.ioc.services.cron.PeriodicExecutor;

import cpcc.core.utils.VersionUtils;
import cpcc.rv.base.services.StateSynchronizer;

/**
 * This module is automatically included as part of the Tapestry IoC Registry, it's a good place to configure and extend
 * Tapestry, or to place your own service definitions.
 */
public final class GsWebModule
{
    private static final String VERSION_PROPERTIES = "version.properties";

    private GsWebModule()
    {
        // intentionally empty.
    }

    /**
     * @param configuration the application configuration.
     */
    public static void contributeFactoryDefaults(MappedConfiguration<String, Object> configuration)
    {
        // The application version number is incorporated into URLs for some
        // assets. Web browsers will cache assets because of the far future expires
        // header. If existing assets are changed, the version number should also
        // change, to force the browser to download new versions. This overrides Tapesty's default
        // (a random hexadecimal number), but may be further overridden by DevelopmentModule or
        // QaModule.
        configuration.override(SymbolConstants.APPLICATION_VERSION, VersionUtils.getVersion(VERSION_PROPERTIES));
    }

    /**
     * @param configuration the application configuration.
     */
    public static void contributeApplicationDefaults(MappedConfiguration<String, Object> configuration)
    {
        configuration.add(SymbolConstants.SUPPORTED_LOCALES, "en,de");
        configuration.add(SymbolConstants.MINIFICATION_ENABLED, "false");
        configuration.add(SymbolConstants.HMAC_PASSPHRASE, "Eith6Du9reeSa7aiaiweaM7oaCh6quae");
    }

    /**
     * @param executor the periodic executor service.
     * @param stateSync the state synchronization service.
     */
    @Startup
    public static void scheduleJobs(PeriodicExecutor executor, final StateSynchronizer stateSync)
    {
        // Taken from RealVehicleBaseModule to reduce network traffic.
        executor.addJob(new CronSchedule("* * * * * ?"), "Real Vehicle status update",
            stateSync::realVehicleStatusUpdate);
    }
}
