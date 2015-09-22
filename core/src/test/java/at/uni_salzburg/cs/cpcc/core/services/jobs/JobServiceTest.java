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

package at.uni_salzburg.cs.cpcc.core.services.jobs;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;

import org.apache.tapestry5.hibernate.HibernateSessionManager;
import org.apache.tapestry5.ioc.ServiceResources;
import org.hibernate.Session;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.slf4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import at.uni_salzburg.cs.cpcc.core.entities.Job;
import at.uni_salzburg.cs.cpcc.core.entities.JobStatus;

public class JobServiceTest
{
    private static final String QUEUE_NAME_01 = "queueName01";
    private static final String QUEUE_NAME_02 = "queueName02";

    private TimeService timeService;
    private JobRepository jobRepository;
    private Date createdDate1;
    private Date createdDate2;
    private Session session;
    private HibernateSessionManager sessionManager;
    private Job existingJob;
    private Job failingJob;
    private JobService sut;
    private String parameters01;
    private String parameters02;
    private JobQueue jobQueue01;
    private JobQueue jobQueue02;
    private ServiceResources serviceResources;
    private Logger logger;

    @BeforeMethod
    public void setUp() throws JobExecutionException
    {
        parameters01 = "parameters01";
        parameters02 = "parameters02";

        createdDate1 = mock(Date.class);
        createdDate2 = mock(Date.class);

        session = mock(Session.class);

        sessionManager = mock(HibernateSessionManager.class);
        when(sessionManager.getSession()).thenReturn(session);

        timeService = mock(TimeService.class);

        jobRepository = mock(JobRepository.class);

        logger = mock(Logger.class);

        existingJob = mock(Job.class);
        when(existingJob.getQueueName()).thenReturn(QUEUE_NAME_01);
        when(existingJob.getParameters()).thenReturn(parameters01);

        failingJob = mock(Job.class);
        when(failingJob.getQueueName()).thenReturn(QUEUE_NAME_02);
        when(failingJob.getParameters()).thenReturn(parameters02);

        jobQueue01 = mock(JobQueue.class);
        doThrow(JobExecutionException.class).when(jobQueue01).execute(failingJob);

        jobQueue02 = mock(JobQueue.class);
        doThrow(JobExecutionException.class).when(jobQueue02).execute(failingJob);

        sut = new JobServiceImpl(serviceResources, sessionManager, jobRepository, timeService, logger);
    }

    @Test
    public void shouldAddJob() throws JobCreationException
    {
        when(timeService.newDate()).thenAnswer(new Answer<Date>()
        {
            int counter = 0;
            Date[] dateList = new Date[]{createdDate1};

            @Override
            public Date answer(InvocationOnMock invocation) throws Throwable
            {
                return counter < dateList.length ? dateList[counter++] : null;
            }
        });

        sut.addJobQueue(QUEUE_NAME_01, jobQueue01);

        sut.addJob(QUEUE_NAME_01, parameters01);

        verify(jobRepository).findOtherRunningJob(QUEUE_NAME_01, parameters01);

        ArgumentCaptor<Job> argument = ArgumentCaptor.forClass(Job.class);
        verify(session).save(argument.capture());
        Job actual = argument.getValue();

        assertThat(actual.getCreated())
            .describedAs("Job queued time")
            .isEqualTo(createdDate1);

        assertThat(actual.getStatus())
            .describedAs("Job status")
            .isEqualTo(JobStatus.CREATED);

        assertThat(actual.getParameters())
            .describedAs("Job parameters")
            .isEqualTo(parameters01);

        assertThat(actual.getQueueName())
            .describedAs("Job queue name")
            .isEqualTo(QUEUE_NAME_01);
    }

    @Test
    public void shouldAddJobWithData() throws JobCreationException
    {
        when(timeService.newDate()).thenAnswer(new Answer<Date>()
        {
            int counter = 0;
            Date[] dateList = new Date[]{createdDate1, createdDate2};

            @Override
            public Date answer(InvocationOnMock invocation) throws Throwable
            {
                return counter < dateList.length ? dateList[counter++] : null;
            }
        });

        sut.addJobQueue(QUEUE_NAME_01, jobQueue01);

        byte[] data1 = new byte[]{1, 2, 3};
        byte[] data2 = new byte[]{3, 2, 1};

        sut.addJob(QUEUE_NAME_01, parameters01, data1);
        sut.addJob(QUEUE_NAME_01, parameters01, data2);

        InOrder inOrder = Mockito.inOrder(timeService, jobQueue01, jobRepository, session);

        inOrder.verify(jobRepository).findOtherRunningJob(QUEUE_NAME_01, parameters01);

        ArgumentCaptor<Job> argument1 = ArgumentCaptor.forClass(Job.class);
        inOrder.verify(session).save(argument1.capture());
        Job actual1 = argument1.getValue();

        assertThat(actual1.getCreated())
            .describedAs("Job queued time")
            .isEqualTo(createdDate1);

        assertThat(actual1.getStatus())
            .describedAs("Job status")
            .isEqualTo(JobStatus.CREATED);

        assertThat(actual1.getParameters())
            .describedAs("Job parameters")
            .isEqualTo(parameters01);

        assertThat(actual1.getQueueName())
            .describedAs("Job queue name")
            .isEqualTo(QUEUE_NAME_01);

        assertThat(actual1.getData())
            .describedAs("Data One")
            .isEqualTo(data1);

        ArgumentCaptor<Job> argument2 = ArgumentCaptor.forClass(Job.class);
        inOrder.verify(session).save(argument2.capture());
        Job actual2 = argument2.getValue();

        assertThat(actual2.getCreated())
            .describedAs("Job queued time")
            .isEqualTo(createdDate2);

        assertThat(actual2.getStatus())
            .describedAs("Job status")
            .isEqualTo(JobStatus.CREATED);

        assertThat(actual2.getParameters())
            .describedAs("Job parameters")
            .isEqualTo(parameters01);

        assertThat(actual2.getQueueName())
            .describedAs("Job queue name")
            .isEqualTo(QUEUE_NAME_01);

        assertThat(actual2.getData())
            .describedAs("Data Two")
            .isEqualTo(data2);
    }

    @Test
    public void shouldAddJobIfNotExists() throws JobCreationException
    {
        when(timeService.newDate()).thenAnswer(new Answer<Date>()
        {
            int counter = 0;
            Date[] dateList = new Date[]{createdDate1};

            @Override
            public Date answer(InvocationOnMock invocation) throws Throwable
            {
                return counter < dateList.length ? dateList[counter++] : null;
            }
        });

        sut.addJobQueue(QUEUE_NAME_01, jobQueue01);

        sut.addJobIfNotExists(QUEUE_NAME_01, parameters01);

        verify(jobRepository).findOtherRunningJob(QUEUE_NAME_01, parameters01);

        ArgumentCaptor<Job> argument = ArgumentCaptor.forClass(Job.class);
        verify(session).save(argument.capture());
        Job actual = argument.getValue();

        assertThat(actual.getCreated())
            .describedAs("Job queued time")
            .isEqualTo(createdDate1);

        assertThat(actual.getStatus())
            .describedAs("Job status")
            .isEqualTo(JobStatus.CREATED);

        assertThat(actual.getParameters())
            .describedAs("Job parameters")
            .isEqualTo(parameters01);

        assertThat(actual.getQueueName())
            .describedAs("Job queue name")
            .isEqualTo(QUEUE_NAME_01);

        verifyZeroInteractions(logger);
    }

    @Test
    public void shouldNotAddJobIfExists() throws JobCreationException
    {
        Job existingJob = mock(Job.class);

        when(jobRepository.findOtherRunningJob(QUEUE_NAME_01, parameters01)).thenReturn(Arrays.asList(existingJob));

        when(timeService.newDate()).thenAnswer(new Answer<Date>()
        {
            int counter = 0;
            Date[] dateList = new Date[]{createdDate1};

            @Override
            public Date answer(InvocationOnMock invocation) throws Throwable
            {
                return counter < dateList.length ? dateList[counter++] : null;
            }
        });

        sut.addJobQueue(QUEUE_NAME_01, jobQueue01);

        sut.addJobIfNotExists(QUEUE_NAME_01, parameters01);

        verify(jobRepository).findOtherRunningJob(QUEUE_NAME_01, parameters01);

        verify(logger)
            .info("Job already executing in queue '" + QUEUE_NAME_01 + "', parameters='" + parameters01 + "'");
    }

    @Test
    public void shouldThrowJobCreationExceptionIfJobIsAlreadyActive() throws JobCreationException
    {
        when(jobRepository.findOtherRunningJob(QUEUE_NAME_01, parameters01)).thenReturn(Arrays.asList(existingJob));

        sut.addJobQueue(QUEUE_NAME_01, jobQueue01);

        catchException(sut).addJob(QUEUE_NAME_01, parameters01);

        assertThat(caughtException())
            .describedAs("Thrown exception")
            .isInstanceOf(JobCreationException.class);

        assertThat(caughtException().getMessage())
            .describedAs("Exception message")
            .isEqualTo("Job already executing: queue=queueName01  params=parameters01");
    }

    @Test
    public void shouldDoNothingIfThereAreNoQueuedJobs() throws JobExecutionException
    {
        verifyZeroInteractions(session, sessionManager, timeService);

        sut.executeJobs();

        verify(jobRepository).findNextScheduledJobs();
    }

    @Test
    public void shouldExecuteSucceedingQueuedJobs() throws JobExecutionException
    {
        when(jobRepository.findNextScheduledJobs()).thenReturn(Arrays.asList(existingJob));

        sut.addJobQueue(QUEUE_NAME_01, jobQueue01);
        sut.addJobQueue(QUEUE_NAME_02, jobQueue02);

        sut.executeJobs();

        verify(jobQueue01).setServiceResources(serviceResources);
        verify(jobQueue02).setServiceResources(serviceResources);

        verify(existingJob).getQueueName();
        verify(jobQueue01).execute(existingJob);
        verifyZeroInteractions(jobQueue02);
    }

    @Test
    public void shouldThrowExceptionIfJobQueueIsNotRegistered() throws JobCreationException
    {
        catchException(sut).addJob(QUEUE_NAME_01, parameters01);

        assertThat(caughtException())
            .describedAs("Thrown exception")
            .isInstanceOf(JobCreationException.class);

        assertThat(caughtException().getMessage())
            .describedAs("Exception message")
            .isEqualTo("Queue " + QUEUE_NAME_01 + " not registered!");
    }

    @Test
    public void shouldResetJobs()
    {
        sut.resetJobs();

        verify(jobRepository).resetJobs();
    }

    @Test
    public void shouldRemoveOldJobs()
    {
        sut.removeOldJobs();

        verify(jobRepository).removeOldJobs();
    }
}
