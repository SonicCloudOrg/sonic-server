FROM sonicorg/sonic-server-base:v1.0.2

ADD /sonic-server-gateway/target/sonic-server-gateway.jar /
ENTRYPOINT ["java","-server","-XX:-UseGCOverheadLimit","-XX:+DisableExplicitGC","-XX:SurvivorRatio=1","-XX:LargePageSizeInBytes=128M","-XX:SoftRefLRUPolicyMSPerMB=0","-Djava.security.egd=file:/dev/./urandom","-jar","/sonic-server-gateway.jar"]
