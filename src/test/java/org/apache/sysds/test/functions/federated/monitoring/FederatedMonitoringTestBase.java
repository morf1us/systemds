/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.sysds.test.functions.federated.monitoring;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.sysds.runtime.controlprogram.federated.monitoring.models.BaseEntityModel;
import org.apache.sysds.test.functions.federated.multitenant.MultiTenantTestBase;
import org.junit.After;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public abstract class FederatedMonitoringTestBase extends MultiTenantTestBase {
	protected Process monitoringProcess;
	private int monitoringPort;

	private static final String WORKER_MAIN_PATH = "/workers";

	@Override
	public abstract void setUp();

	// ensure that the processes are killed - even if the test throws an exception
	@After
	public void stopMonitoringProcesses() {
		if (monitoringProcess != null) {
			monitoringProcess.destroyForcibly();
		}
	}

	/**
	 * Start federated backend monitoring processes on available port
	 *
	 * @return
	 */
	protected void startFedMonitoring(String[] addArgs) {
		monitoringPort = getRandomAvailablePort();
		monitoringProcess = startLocalFedMonitoring(monitoringPort, addArgs);
	}

	protected List<HttpResponse<?>> addWorkers(int numWorkers) {
		String uriStr = String.format("http://localhost:%d%s", monitoringPort, WORKER_MAIN_PATH);

		List<HttpResponse<?>> responses = new ArrayList<>();
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			for (int i = 0; i < numWorkers; i++) {
				String requestBody = objectMapper
					.writerWithDefaultPrettyPrinter()
					.writeValueAsString(new BaseEntityModel((i + 1L), "Worker", "localhost"));
				var client = HttpClient.newHttpClient();
				var request = HttpRequest.newBuilder(URI.create(uriStr))
					.header("accept", "application/json")
					.POST(HttpRequest.BodyPublishers.ofString(requestBody))
					.build();
				responses.add(client.send(request, HttpResponse.BodyHandlers.ofString()));
			}

			return responses;
		}
		catch (IOException | InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	protected HttpResponse<?> getWorkers() {
		String uriStr = String.format("http://localhost:%d%s", monitoringPort, WORKER_MAIN_PATH);

		try {
			var client = HttpClient.newHttpClient();
			var request = HttpRequest.newBuilder(URI.create(uriStr))
				.header("accept", "application/json")
				.GET().build();
			return client.send(request, HttpResponse.BodyHandlers.ofString());
		}
		catch (IOException | InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}
