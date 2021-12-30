# 建立Hello API

## 主要功能：回傳該主機的hostname 以及 IP Address

`HelloController.java`
``` csharp
          InetAddress addr = InetAddress.getLocalHost();
            String hostname =addr.getHostName().toString();//獲得本機名稱
            obj.put("hostname",hostname);

            InetAddress Addresses[] = InetAddress.getAllByName(hostname);
            ArrayList<java.lang.String> ip_array = new ArrayList<>();

            JSONObject ipObject = new JSONObject();

            int i=1;
            for(InetAddress address1 : Addresses){
                ip_array.add(address1.getHostAddress());
                ipObject.put("add"+i,address1.getHostAddress());
                i++;
            }
            //obj.put("ip_list",ip_array);
            obj.put("ip_list",ipObject);
```
`application.yml`

``` csharp
server:
  port: 9990

spring:
  applicaiton:
    name: hello-service
```

`pom.xml`

``` csharp
   <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <fork>true</fork>
                    <mainClass>my.demo.hello.HelloApplication</mainClass>
                </configuration>
                <executions>
                    <execution>
                        <goals>
                            <goal>repackage</goal>
                        </goals>
                    </execution>
                </executions>
                <version>2.1.4.RELEASE</version>
            </plugin>
        </plugins>
    </build>

```

## 預計執行結果

``` csharp
{

    "hostname":"Kevinteki-MacBook-Air.local",
    "ip_list":{
        "add5":"fe80:0:0:0:1491:548c:dff0:d87%4",
        "add2":"192.168.0.10",
        "add1":"127.0.0.1",
        "add4":"fe80:0:0:0:0:0:0:1%1",
        "add3":"0:0:0:0:0:0:0:1"
    }

}

```




# 產生該API服務的Docker Image

## 撰寫Dockerfile

``` csharp
FROM java:8
VOLUME /tmp
ADD HelloHostService-1.0-SNAPSHOT.jar hello.jar
ENTRYPOINT ["java","-jar","/hello.jar"]
EXPOSE 9990
```

## 建立 Docker Image

```% docker build -t hello . --no-cache```

```% docker images ```


## 上傳Docker Image

```% docker login ```
```% docker image ls ```

``` csharp
REPOSITORY  TAG     IMAGE ID        CREATED         SIZE
hello       latest  a96c28aff74f    5 minutes ago   1.01GB

```

## 加註Tag
`% docker tag a96c28aff74f kkwen/hello`

## 上傳 Docker Image 至 Docker Hub
`% docker push kkwen922/hello`

## 在Docker Hub網站檢查push結果
![](https://i.imgur.com/MykILTc.png)


# Kubernetes

## 建立YAML檔案
` hello.yaml`

```
apiVersion: v1
kind: Pod
metadata:
  name: my-hello
  labels:
    app: hello 
spec:
  containers:
  - name: pod-hello
    image: kkwen922/hello
    ports:
    - containerPort: 9990 
```


## 創建Pod

`% kubectl create -f hello.yaml`

## 檢查創建Pod結果(顯示目前所有Pods資訊)

`% kubectl get pods`


```
NAME        READY    STATUS    RESTARTS    AGES
my-hello    1/1      Running   0           4m2s      
```

## 創建服務
`% kubectl expose pod my-hello --type=NodePort --name=hello-service`

## 檢查創建服務結果
`%kubectl get services`

```
NAME                TYPE        CLUSTER-IP       EXTERNAL-IP   PORT(S)          AGE
hello-service       NodePort    10.107.72.33     <none>        9990:32297/TCP   3h39m
kubernetes          ClusterIP   10.96.0.1        <none>        443/TCP          5d23h
my-zipkin-service   NodePort    10.104.236.245   <none>        9411:31213/TCP   24h
```
## 創建多個PODs

## 建立YAML

`deployment.yaml`
```
apiVersion: apps/v1
kind: Deployment
metadata:
  name: hello-deployment
spec:
  replicas: 3
  template:
    metadata:
      labels:
        app: hello 
    spec:
      containers:
        - name: pod-hello 
          image: kkwen922/hello
          ports:
            - containerPort: 9990 
  selector:
    matchLabels:
      app: hello 

```

## 創建多個Pods指令
`% kubectl create -f deployment.yaml`

## 檢查結果
`% kubectl get pods`

```
NAME                                READY   STATUS    RESTARTS      AGE
hello-deployment-85f449d8c4-5jrhd   1/1     Running   1 (17m ago)   3h35m
hello-deployment-85f449d8c4-qfdgz   1/1     Running   1 (17m ago)   3h35m
hello-deployment-85f449d8c4-qfx88   1/1     Running   1             3h35m
my-hello                            1/1     Running   1 (17m ago)   3h47m
my-zipkin                           1/1     Running   2             24h

```

## 檢查servies

`% kubectl get services`
```
NAME                TYPE        CLUSTER-IP       EXTERNAL-IP   PORT(S)          AGE
hello-service       NodePort    10.107.72.33     <none>        9990:32297/TCP   3h46m
kubernetes          ClusterIP   10.96.0.1        <none>        443/TCP          5d23h
my-zipkin-service   NodePort    10.104.236.245   <none>        9411:31213/TCP   24h

```

## LoadBalance機制確認

藉由多次呼叫 Hello API的回傳結果，確認K8s是否有達到Load Balance

```
http://192.168.0.32:32297/hello/1
```

```
回傳結果之一：
{"hostname":"hello-deployment-85f449d8c4-qfdgz","ip_list":{"add1":"10.244.1.13"}}
```

```
回傳結果之二：
{"hostname":"hello-deployment-85f449d8c4-qfx88","ip_list":{"add1":"10.244.1.22"}}
```

