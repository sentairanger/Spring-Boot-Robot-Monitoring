# Spring-Boot-Robot-Monitoring
This project uses Prometheus to monitor a robot using Spring Boot and Diozero

## Introduction

This Spring Boot application is an extension from the Spring Boot LED Monitoring application except it now uses Prometheus for observability and Grafana to display the data as the robot moves and blinks its eye. The project is divided into layers. I will explain each layer as I've done in previous projects and how it builds up to the final product. 

### Main application

The main application uses Maven and can be run using IntelliJ or can also be run in the terminal. This can be useful if making changes to your application. To run this application you can use the following:
1. Maven (Click [here](https://maven.apache.org/download.cgi))
2. IntelliJ (optional) (Click [here](https://www.jetbrains.com/idea/download/#section=linux))

In IntelliJ you must go to the maven section and go to Lifecycle and then click on Package. If using the terminal run `mvn package` from the same directory as the `pom.xml` file. After that you use the terminal to run the application with `java -jar -DPIGPIOD_HOST=<ip-address-of-pi> <jar-file-name>.jar`. If you are using the Vagrant file provided you can go to <vagrant-ip-address>:8080/home or localhost:8080 if you plan to run this locally. You can find the metrics under the `/actuator/prometheus` endpoint. Make sure to have Remote GPIO enabled with `sudo pigpiod` or use `sudo raspi-config` and then enable remote GPIO from interfaces. You can also enable this from the Raspberry Pi Configuration menu if using the desktop.

![robot](https://github.com/sentairanger/Spring-Boot-Robot-Monitoring/blob/main/images/robot.png)

### Docker container

After testing the application you can then build an image for the application. The Dockerfile is provided for use. Go into the same directory as the Dockerfile and prepare to build. Be sure to have docker installed. Click [here](https://docs.docker.com/get-docker/) to learn how to install. However, the Vagrant file also has docker installed by default so you can follow along. First build the image with `docker build -t spring-robot .`. Then you can test the application with `docker run -dp 8080:8080 spring-robot`. Then you can check  if it is running with `docker ps`. Access the application the same way as before. After you are finished you can stop the app with `docker stop <container-id>`. Next, you can tag the image with `docker tag <dockerhub-username>/spring-robot:tag`. Make sure to have a docker hub account first. Then create a repository under docker hub with the spring-monitoring name. Then log in with `docker login`. Then push with `docker push <dockerhub-username>/spring-robot:tag`. And you should have your container built.

### Kubernetes Cluster

I have provided a manifest file under the kubernetes directory as I've done in previous projects. This is the exact same manifest template but with slight changes. You can also use this exact mainfest and change it as you want. This is used to orchestrate the application. To run this cluster use the command `kubectl apply -f spring-robot.yaml`. This can be done using the Vagrant File or on a local machine or even the cloud. Then you can port-forward with `kubectl port-forward svc/java-led 8080`. If using a Vagrant machine, use `8080:8080`. There are three parts to this file:
1. Deployment: This is the main component used to deploy the cluster. As you can see prometheus scrape is set to true and the prometheus metrics endpoint is shown at `/actuator/prometheus`. And it has the port at 8080. 
2. Service: This is required to run the cluster at port 8080
3. Service Monitor: For prometheus to scrape the application this is required. There is a scrape interval that can be altered.

## Argo CD

Optionally it is possible to use Argo CD to deploy the cluster. I have an argo cd install script under the scripts directory to make it easier to install. I have provided a node port file and the main cluster manifest file as well. You can run them the same way using `kubectl apply -f <name-of-manifest-file>.yaml`. Once the node port is up and running if using vagrant you can access Argo CD with port 30007 or 30008. Then login as admin with the password you generated from the install script. Then run the other application and it should show up in the dashboard. Sync the application and you should see it sync.

![argocd](https://github.com/sentairanger/Spring-Boot-Robot-Monitoring/blob/main/images/argocd.png)

### Prometheus

Once the application is running make sure prometheus can see it. I have provided an install script to install both prometheus and grafana as I've done in other projects. So this will work similar to my previous projects. Once prometheus is installed you can check if your application is being scraped by prometheus by running the command `kubectl port-forward svc/prometheus-kube-prometheus-prometheus 9090 -n monitoring`. Make sure to change it as `9090:9090` if using the vagrant file. Then go to either localhost:9090 or <vagrant-ip-address>:9090. Click on status and then click on targets. You should see the applicaiton being scraped. Picture below shows the final result.

![prometheus](https://github.com/sentairanger/Spring-Boot-Robot-Monitoring/blob/main/images/prometheus.png)

### Grafana

Once prometheus is scraping the application you can mess around with the application by moving the robot around and blinking the LED and then access Grafana with the command `kubectl port-forward svc/prometheus-grafana 3000:80 -n monitoring`. Then go to `<vagrant-ip>:3000` or `localhost:3000`. Then you login with admin and the default password prom-operator. Change this for security reasons. Next, you can import the dashboard provided in the Grafana directory and the dashboard should appear. Note, if there is an issue with grafana starting up make sure to check your network and test if you can still connect.

![dashboard](https://github.com/sentairanger/Spring-Boot-Robot-Monitoring/blob/main/images/dashboard.png)


### Vagrant box

I have provided a vagrant file for use. Vagrant can be downloaded and installed from [here](https://www.vagrantup.com/downloads). Then make sure to have VirtualBox or VMWare installed. If using an Apple M1 or M2 Virtual box is currently not supported so it's best to find other options like Parallel or VMWare that will support these chips. I will update if there is any changes to that so stay tuned. I will also move this project to using Pi4J v2 once it is finalized. To create the virtual machine run the command `vagrant up`. Then login with `vagrant ssh`. To shutdown run `vagrant halt`.
