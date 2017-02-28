#! /usr/bin/env bash
#
# Builds and makes packages. Tested on CentOS 5.8.
#

set -eux

# Acquire and locally install JDK 1.5.  Hey, Tomcat 6 is ancient.
if [ ! -d jdk1.5.0_22 ]; then
    # Get JDK 1.5, it installs in jdk1.5.0_22/
    wget http://mirror.mtv.cloudera.com/tomcat6/jdk-1_5_0_22-linux-amd64.bin
    chmod 755 jdk-1_5_0_22-linux-amd64.bin
    ./jdk-1_5_0_22-linux-amd64.bin
fi

#     # Since I moved the downloaded libs into the repo, I don't need this
#     # here any more since we don't download anything. Downloads were
#     # super flaky, and might disappear from the internet one day...
#
#     # Get and install a root cert that jkd1.5 doesn't have
#     JJLS=jdk1.5.0_22/jre/lib/security
#     wget http://mirror.mtv.cloudera.com/tomcat6/dst.pem
#     cp ${JJLS}/cacerts jssecacerts
#     chmod 644 jssecacerts
#     keytool -import -alias dst -keystore jssecacerts \
# 	-file dst.pem -storepass changeit -noprompt
#     cp jssecacerts ${JJLS}/
#
#     # Turn off DHE to avoid key size issues (ugh)
#     chmod 644 ${JJLS}/java.security
#     echo "jdk.tls.disabledAlgorithms=SSLv3, DHE, DH" >> ${JJLS}/java.security

export JAVA_HOME=$(pwd)/jdk1.5.0_22
export PATH=${JAVA_HOME}/bin:${PATH}

# Ant 1.8 is fortunately in our toolchain.
[ -d /opt/toolchain/apache-ant-1.8.2 ] || exit 1
export ANT_HOME=/opt/toolchain/apache-ant-1.8.2
export PATH=${ANT_HOME}/bin:${PATH}

ant download
ant
ant -f extras.xml
ant -f dist.xml -Dskip.installer=true release
