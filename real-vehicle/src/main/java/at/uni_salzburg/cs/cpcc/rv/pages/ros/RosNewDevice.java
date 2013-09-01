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
package at.uni_salzburg.cs.cpcc.rv.pages.ros;

import static org.apache.tapestry5.EventConstants.PREPARE;
import static org.apache.tapestry5.EventConstants.SUCCESS;

import javax.inject.Inject;
import javax.validation.Valid;

import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import org.apache.tapestry5.ioc.Messages;

import at.uni_salzburg.cs.cpcc.rv.entities.Device;
import at.uni_salzburg.cs.cpcc.rv.entities.DeviceType;
import at.uni_salzburg.cs.cpcc.rv.pages.Configuration;
import at.uni_salzburg.cs.cpcc.rv.services.DeviceTypeSelectHelpers;
import at.uni_salzburg.cs.cpcc.rv.services.QueryManager;

/**
 * RosNewDevice
 */
public class RosNewDevice
{
    private final static String ERROR_TOPIC_ALREADY_USED = "error.topic.already.used";
    private final static String ERROR_TOPIC_MUST_START_WITH_SLASH = "error.topic.must.start.with.slash";
    
    @Inject
    private QueryManager qm;

    @Inject
    private Messages messages;
    
    @Valid
    @Property
    private Device device;

    @Component(id = "form")
    private Form form;

    @OnEvent(PREPARE)
    void createDevice()
    {
        device = new Device();
    }
    
    @OnEvent(SUCCESS)
    @CommitAfter
    Object newDevice()
    {
        qm.saveOrUpdate(device);
        qm.saveOrUpdateMappingAttributes(device);

        return Configuration.class;
    }
    
    public SelectModel getDeviceTypeNameSelectModel() 
    {           
        return DeviceTypeSelectHelpers.selectModel(qm.findAllDeviceTypes());
    }           
            
    public ValueEncoder<DeviceType> getDeviceTypeNameEncoder()
    {
        return new DeviceTypeSelectHelpers(qm).valueEncoder();
    }
    
    void onValidateFromForm()
    {
        Device dev = qm.findDeviceByTopicRoot(device.getTopicRoot());
        
        if (dev != null)
        {
            String msg = messages.get(ERROR_TOPIC_ALREADY_USED);
            form.recordError(String.format(msg, device.getTopicRoot()));
        }
        
        if (!device.getTopicRoot().startsWith("/"))
        {
            String msg = messages.get(ERROR_TOPIC_MUST_START_WITH_SLASH);
            form.recordError(msg);
        }
    }
}
