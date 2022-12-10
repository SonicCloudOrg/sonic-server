FROM registry.cn-hongkong.aliyuncs.com/sonic-cloud/sonic-server-base:v1.0.0

ADD /sonic-server-controller/target/sonic-server-controller.jar /
["java","-server","-XX:-UseGCOverheadLimit","-XX:+DisableExplicitGC","-XX:SurvivorRatio=1","-XX:LargePageSizeInBytes=128M","-XX:SoftRefLRUPolicyMSPerMB=0","-Djava.security.egd=file:/dev/./urandom","--add-exports=java.naming/com.sun.jndi.ldap=ALL-UNNAMED","-jar","/sonic-server-controller.jar"]
