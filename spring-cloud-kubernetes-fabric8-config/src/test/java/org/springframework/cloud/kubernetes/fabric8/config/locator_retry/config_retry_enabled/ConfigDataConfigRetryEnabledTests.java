/*
 * Copyright 2013-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cloud.kubernetes.fabric8.config.locator_retry.config_retry_enabled;

import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.server.mock.EnableKubernetesMockClient;
import io.fabric8.kubernetes.client.server.mock.KubernetesMockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.mockito.internal.util.MockUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.kubernetes.commons.KubernetesNamespaceProvider;
import org.springframework.cloud.kubernetes.commons.config.ConfigDataRetryableConfigMapPropertySourceLocator;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.mockito.Mockito.spy;

/**
 * @author Isik Erhan
 */

@TestPropertySource(properties = "spring.config.import=kubernetes:")
@EnableKubernetesMockClient
class ConfigDataConfigRetryEnabledTests extends ConfigRetryEnabled {

	private static KubernetesMockServer mockServer;

	private static KubernetesClient mockClient;

	@MockitoBean
	private KubernetesNamespaceProvider namespaceProvider;

	@BeforeAll
	static void setup() {
		setup(mockClient, mockServer);
	}

	@Autowired
	ConfigDataRetryableConfigMapPropertySourceLocator propertySourceLocator;

	@BeforeEach
	void beforeEach() {
		psl = propertySourceLocator;
		// latest Mockito does not allow to do something like Mockito.spy(spy)
		// so this works around that
		if (!MockUtil.isSpy(propertySourceLocator.getConfigMapPropertySourceLocator())) {
			verifiablePsl = spy(propertySourceLocator.getConfigMapPropertySourceLocator());
			propertySourceLocator.setConfigMapPropertySourceLocator(verifiablePsl);
		}
		else {
			verifiablePsl = propertySourceLocator.getConfigMapPropertySourceLocator();
		}
	}

	@AfterEach
	void afterEach() {
		Mockito.reset(verifiablePsl);
	}

}
