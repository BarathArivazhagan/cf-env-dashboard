package com.barath.app.cloudfoundry.factory;

import org.cloudfoundry.client.CloudFoundryClient;
import org.cloudfoundry.doppler.DopplerClient;
import org.cloudfoundry.operations.CloudFoundryOperations;
import org.cloudfoundry.reactor.ConnectionContext;
import org.cloudfoundry.reactor.TokenProvider;
import org.cloudfoundry.uaa.UaaClient;

public interface CloudFoundryFactory {


   ConnectionContext getConnectionContext(String datacenter);

   TokenProvider getTokenProvider(String datacenter);

   CloudFoundryClient getCloudFoundryClient(String datacenter);

   DopplerClient getDopplerClient(String datacenter);

   UaaClient getUaaClient(String datacenter);

   CloudFoundryOperations getCloudFoundryOperations(String datacenter, String org, String space);

}
