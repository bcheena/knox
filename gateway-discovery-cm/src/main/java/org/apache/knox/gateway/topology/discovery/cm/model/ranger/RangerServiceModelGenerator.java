/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.apache.knox.gateway.topology.discovery.cm.model.ranger;

import com.cloudera.api.swagger.model.ApiConfigList;
import com.cloudera.api.swagger.model.ApiRole;
import com.cloudera.api.swagger.model.ApiService;
import com.cloudera.api.swagger.model.ApiServiceConfig;
import org.apache.knox.gateway.topology.discovery.cm.ServiceModel;
import org.apache.knox.gateway.topology.discovery.cm.model.AbstractServiceModelGenerator;

import java.util.Locale;

public class RangerServiceModelGenerator extends AbstractServiceModelGenerator {
  public static final String SERVICE      = "RANGER";
  public static final String SERVICE_TYPE = "RANGER";
  public static final String ROLE_TYPE    = "RANGER_ADMIN";

  static final String SSL_ENABLED = "ssl_enabled";
  static final String HTTP_PORT   = "ranger_service_http_port";
  static final String HTTPS_PORT  = "ranger_service_https_port";

  @Override
  public String getService() {
    return SERVICE;
  }

  @Override
  public String getServiceType() {
    return SERVICE_TYPE;
  }

  @Override
  public String getRoleType() {
    return ROLE_TYPE;
  }

  @Override
  public ServiceModel.Type getModelType() {
    return ServiceModel.Type.API;
  }

  @Override
  public ServiceModel generateService(ApiService service,
                                 ApiServiceConfig serviceConfig,
                                 ApiRole role,
                                 ApiConfigList roleConfig, ApiServiceConfig coreSettingsConfig) {
    String hostname = role.getHostRef().getHostname();
    String scheme;
    String port;
    String sslEnabled = getRoleConfigValue(roleConfig, SSL_ENABLED);
    if(Boolean.parseBoolean(sslEnabled)) {
      scheme = "https";
      port = getServiceConfigValue(serviceConfig, HTTPS_PORT);
    } else {
      scheme = "http";
      port = getServiceConfigValue(serviceConfig, HTTP_PORT);
    }

    ServiceModel model = createServiceModel(String.format(Locale.getDefault(), "%s://%s:%s", scheme, hostname, port));
    model.addServiceProperty(HTTP_PORT, getServiceConfigValue(serviceConfig, HTTP_PORT));
    model.addServiceProperty(HTTPS_PORT, getServiceConfigValue(serviceConfig, HTTPS_PORT));
    model.addRoleProperty(getRoleType(), SSL_ENABLED, sslEnabled);

    return model;
  }

}
