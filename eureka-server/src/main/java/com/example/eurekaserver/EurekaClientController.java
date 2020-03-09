/*package com.example.eurekaserver;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

public class EurekaClientController {



   private EurekaClient eurekaClient;

    public EurekaClientController(EurekaClient eurekaClient) {
        this.eurekaClient = eurekaClient;
    }


    @GetMapping(value = "/clients")
    public List<InstanceInfo> doRequest() {
        Application application
                = eurekaClient.getApplication("Countries-Service");
        return application.getInstances();

//        InstanceInfo instanceInfo = application.getInstances().get(0);
//        String hostname = instanceInfo.getHostName();
//        int port = instanceInfo.getPort();
//        // ...
    }
}*/
