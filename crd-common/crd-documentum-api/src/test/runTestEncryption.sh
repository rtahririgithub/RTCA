#!/usr/bin/ksh

export JAVA_HOME="/apps/infra/jdk/jdk160_24"

export PATH=${JAVA_HOME}/bin:${PATH}


export CLASSPATH=./:/work/users/custmgtdv01/documentumtest/test/config:/work/users/custmgtdv01/documentumtest/test/conf:./lib/commons-beanutils-1.7.0.jar:./lib/commons-codec-1.3.jar:./lib/commons-collections-3.2.jar:./lib/commons-digester-1.8.jar:./lib/commons-io-20030203.000550.jar:./lib/commons-lang-2.3.jar:./lib/commons-logging-1.1.jar:./lib/commons-validator-1.3.1.jar:./lib/frameworks5-core-1.11.jar:./lib/frameworks5-crypto-1.11.jar:./lib/frameworks5-documentum-1.11.jar:./lib/javaee-5_09.jar:./lib/jdom-1.0.jar:./lib/junit-3.8.1.jar:./lib/log4j-1.2.15.jar:./lib/spring-2.5.6.jar:./lib/telus-crd-documentum-api-1.1.jar:./lib/telusantext-1.3.jar:./lib/xalan-2.7.0.jar:./lib/xercesImpl-2.7.1.jar:./lib/xml-apis-2.7.0.jar:./com

java -classpath ${CLASSPATH} com.telus.credit.documentum.TestEncryption ${1} ${2}
