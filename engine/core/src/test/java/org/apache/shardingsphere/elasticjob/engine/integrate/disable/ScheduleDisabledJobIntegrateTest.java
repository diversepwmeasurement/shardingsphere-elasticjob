/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shardingsphere.elasticjob.engine.integrate.disable;

import org.apache.shardingsphere.elasticjob.api.JobConfiguration;
import org.apache.shardingsphere.elasticjob.engine.fixture.job.DetailedFooJob;
import org.apache.shardingsphere.elasticjob.engine.internal.schedule.JobRegistry;
import org.apache.shardingsphere.elasticjob.engine.internal.server.ServerStatus;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ScheduleDisabledJobIntegrateTest extends DisabledJobIntegrateTest {
    
    ScheduleDisabledJobIntegrateTest() {
        super(TestType.SCHEDULE);
    }
    
    @Override
    protected JobConfiguration getJobConfiguration(final String jobName) {
        return JobConfiguration.newBuilder(jobName, 3).cron("0/1 * * * * ?").shardingItemParameters("0=A,1=B,2=C")
                .jobListenerTypes("INTEGRATE-TEST", "INTEGRATE-DISTRIBUTE").disabled(true).overwrite(true).build();
    }
    
    @Test
    void assertJobRunning() {
        assertDisabledRegCenterInfo();
        setJobEnable();
        Awaitility.await().atMost(10L, TimeUnit.SECONDS).untilAsserted(() -> assertThat(((DetailedFooJob) getElasticJob()).isCompleted(), is(true)));
        assertEnabledRegCenterInfo();
    }
    
    private void setJobEnable() {
        getREGISTRY_CENTER().persist("/" + getJobName() + "/servers/" + JobRegistry.getInstance().getJobInstance(getJobName()).getServerIp(), ServerStatus.ENABLED.name());
    }
    
    private void assertEnabledRegCenterInfo() {
        assertTrue(getREGISTRY_CENTER().isExisted("/" + getJobName() + "/instances/" + JobRegistry.getInstance().getJobInstance(getJobName()).getJobInstanceId()));
        getREGISTRY_CENTER().remove("/" + getJobName() + "/leader/election");
        assertTrue(getREGISTRY_CENTER().isExisted("/" + getJobName() + "/sharding"));
    }
}