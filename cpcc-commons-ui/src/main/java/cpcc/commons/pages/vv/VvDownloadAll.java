// This code is part of the CPCC-NG project.
//
// Copyright (c) 2009-2016 Clemens Krainer <clemens.krainer@gmail.com>
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

package cpcc.commons.pages.vv;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

import org.apache.tapestry5.StreamResponse;

import cpcc.core.utils.DownloadStreamResponse;
import cpcc.vvrte.services.db.DownloadService;

/**
 * VvDownloadAll implementation.
 */
public class VvDownloadAll
{
    @Inject
    private DownloadService downloadService;

    StreamResponse onActivate() throws IOException
    {
        SimpleDateFormat sdf = new SimpleDateFormat("'all-vvs-'yyyyMMdd-HHmmss'.zip'");
        String fileName = sdf.format(new Date());
        return new DownloadStreamResponse("application/zip", downloadService.getAllVirtualVehicles(), fileName);
    }
}
