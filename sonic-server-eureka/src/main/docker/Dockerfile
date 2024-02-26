FROM sonicorg/sonic-server-base:v1.0.2

ADD /sonic-server-eureka/target/sonic-server-eureka.jar /
ENTRYPOINT ["java","-server","-XX:-UseGCOverheadLimit","-XX:+DisableExplicitGC","-XX:SurvivorRatio=1","-XX:LargePageSizeInBytes=128M","-XX:SoftRefLRUPolicyMSPerMB=0","-Djava.security.egd=file:/dev/./urandom","-jar","/sonic-server-eureka.jar"]
