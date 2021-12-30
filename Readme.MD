###################################################
# Docker Network
###################################################
% docker network ls

% docker network inspect bridge
==>subnet:172.17.0.0/16
==>gateway:172.17.0.1
==>containers:{}

% apt-get install bridge-utils
% brctl show
% docker run it alpine sh


###################################################
#
###################################################
% docker ps
% docker run postgres:9.6
==>download / pull complete and run

##################################################
Docker Command
##################################################
% docker pull redis
% docker images

% docker run -d redis       ==>  starts new container with a command
% docker ps                 ==>
% docker stop "container Id"
% docker start "container Id"

#####################
# Dockerfile
####################

FROM java:8
VOLUME /tmp
ADD zipkin-server-2.12.9-exec.jar zipkin.jar
ENTRYPOINT ["java","-jar","/zipkin.jar"]
EXPOSE 9411


###################
# DockerImage build
###################
% docker build -t zipkin . --no-cache

% docker images

==>
itpi@itpi-NIS-Q170J:~/kkCloud/zipkin$ docker images
REPOSITORY      TAG       IMAGE ID       CREATED             SIZE
kkwen922/demo   latest    a96c28aff74f   About an hour ago   1.01GB
zipkin          latest    a96c28aff74f   About an hour ago   1.01GB
config          latest    50e269ea4aab   3 hours ago         1.01GB


#######################
# Docker Login
#######################
% docker login
username:kkwen922
password:Ghost+3849

% docker image ls

==>
REPOSITORY   TAG       IMAGE ID       CREATED         SIZE
zipkin       latest    a96c28aff74f   5 minutes ago   1.01GB

% docker tag a96c28aff74f kkwen922/demo

==>
REPOSITORY      TAG       IMAGE ID       CREATED         SIZE
kkwen922/demo   latest    a96c28aff74f   7 minutes ago   1.01GB

% docker push kkwen922/demo

#############################
# K8s
############################
% vi zipkin.yaml

apiVersion: v1
kind: Pod
metadata:
  name: my-zipkin
  labels:
    app: zipkin
spec:
  containers:
  - name: pod-zipkin
    image: kkwen922/demo
    ports:
    - containerPort: 9411


% kubectl create -f zipkin.yaml

==>
itpi@master-node:~/kkCloud$ kubectl create -f zipkin.yaml
pod/my-zipkin created


% kubectl get pods

==>
itpi@master-node:~/kkCloud$ kubectl get pods
NAME        READY   STATUS    RESTARTS   AGE
my-zipkin   1/1     Running   0          4m2s


%

==>
Name:         my-zipkin
Namespace:    default
Priority:     0
Node:         worker01/192.168.0.35
Start Time:   Mon, 27 Dec 2021 11:53:13 +0800
Labels:       app=zipkin
Annotations:  <none>
Status:       Running
IP:           10.244.1.8
IPs:
  IP:  10.244.1.8
Containers:
  pod-zipkin:
    Container ID:   docker://c3fb18f16974cb92d7ba81139c44c12826316da3c1b168cacaa4365b4d7ada95
    Image:          kkwen922/demo
    Image ID:       docker-pullable://kkwen922/demo@sha256:35ee19e5e1c0205db51c2ca680f061e1efb9234c54cab09152b707cfc10aeaae
    Port:           9411/TCP
    Host Port:      0/TCP
    State:          Running
      Started:      Tue, 28 Dec 2021 14:00:20 +0800
    Ready:          True
    Restart Count:  0
    Environment:    <none>
    Mounts:
      /var/run/secrets/kubernetes.io/serviceaccount from kube-api-access-mxzr8 (ro)
Conditions:
  Type              Status
  Initialized       True
  Ready             True
  ContainersReady   True
  PodScheduled      True
Volumes:
  kube-api-access-mxzr8:
    Type:                    Projected (a volume that contains injected data from multiple sources)
    TokenExpirationSeconds:  3607
    ConfigMapName:           kube-root-ca.crt
    ConfigMapOptional:       <nil>
    DownwardAPI:             true
QoS Class:                   BestEffort
Node-Selectors:              <none>
Tolerations:                 node.kubernetes.io/not-ready:NoExecute op=Exists for 300s
                             node.kubernetes.io/unreachable:NoExecute op=Exists for 300s
Events:
  Type    Reason     Age    From               Message
  ----    ------     ----   ----               -------
  Normal  Scheduled  11m    default-scheduler  Successfully assigned default/my-zipkin to worker01
  Normal  Pulling    26h    kubelet            Pulling image "kkwen922/demo"
  Normal  Pulled     9m1s   kubelet            Successfully pulled image "kkwen922/demo" in 2m44.918097256s
  Normal  Created    8m33s  kubelet            Created container pod-zipkin
  Normal  Started    8m31s  kubelet            Started container pod-zipkin

################
# 單機
################
% kubectl port-forward my-zipkin 9411:9411

#################
# 建立K8s Service
##################

% kubectl expose pod my-zipkin --type=NodePort --name=my-zipkin-service

==>
itpi@master-node:~/kkCloud$ kubectl expose pod my-zipkin --type=NodePort --name=my-zipkin-service
service/my-zipkin-service exposed


% kubectl get services

==>
itpi@master-node:~/kkCloud$ kubectl get services
NAME                TYPE        CLUSTER-IP       EXTERNAL-IP   PORT(S)          AGE
kubernetes          ClusterIP   10.96.0.1        <none>        443/TCP          4d22h
my-zipkin-service   NodePort    10.104.236.245   <none>        9411:31213/TCP   28s


%





