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

package org.apache.shardingsphere.elasticjob.engine.internal.config;

import org.apache.shardingsphere.elasticjob.engine.internal.storage.JobNodePath;

/**
 * Configuration node.
 */
public final class ConfigurationNode {
    
    static final String ROOT = "config";
    
    private final JobNodePath jobNodePath;
    
    public ConfigurationNode(final String jobName) {
        jobNodePath = new JobNodePath(jobName);
    }
    
    /**
     * Judge is configuration root path or not.
     * 
     * @param path node path
     * @return is configuration root path or not
     */
    public boolean isConfigPath(final String path) {
        return jobNodePath.getConfigNodePath().equals(path);
    }
}