This branch demonstrates backport of client-server JUnit tests to Tomcat 6
(TomcatBaseTest, TestTomcat).

Currently this serves as a proof a concept.
I expect to cherry-pick some or all of the features back to tc6.0.x/trunk.

Created: 2016-01-06 from r1723242.
Last catch-up merge: 2016-01-15, merged up to r1724810.


Current status / Completed:

 1. TomcatBaseTest, TestTomcat, Tomcat and some helper classes have been
 backported to Tomcat 6.

 Some methods are omitted / commented out.

 Tomcat class was moved from public java/ directory into test/ one. I
 consider this class as non-public API at this moment.


 2. There are several test classes that run successfully and prove the concept.

 TestTomcat, TestConnector.

 Caveat: The testSingleWebapp() test assumes that examples web application
 has been built and copied into output\build\webapps\examples\

 Make sure to run Ant build before running the test from within an IDE.


 3. Implemented support for starting Tomcat with a random port number (port
 number 0).

 Committed to tc6.0.x/trunk. (r1723545 + r1723551, BZ 52028).

 4. Implemented support for running the tests with Apache Ant.

 Note: running the tests with APR connector is not usable.
 The JVM crashes. See FIXME items below.


Known issues / FIXME:

 1. I am using org.apache.catalina.startup.Embedded class as the tool that
    runs the server instance. This class is the server (extends
    StandardService).

    I am using org.apache.catalina.startup.Tomcat class as a helper class
    used to configure the server.

    This is a quick proof of concept. I expect to reorganise this so that
    Tomcat class gets the central role and maintains (wraps) a server
    instance.                                                               [Started]


 2. Tomcat 6 has class org.apache.catalina.ServerFactory that contains a
    singleton reference to a Server instance. This field has to be cleared
    after a test run.

    Current workaround in TomcatBaseTest.tearDown() is to use reflection to
    clean up the ServerFactory.server field.

    The TomcatBaseTest.setUp() method uses reflection to assert that the
    ServerFactory.server field has been cleared before the test run.

    I expect to add necessary access methods to ServerFactory class.
    Discussed in http://tomcat.markmail.org/thread/ko7ip7obvyaftwe4         [Not Started]


 3. The Engine has no assigned name. Logs print [null] as the name.

    The defaultHost attribute on Engine has not been set. A warning is
    printed in the logs:

    org.apache.catalina.connector.MapperListener registerEngine
    WARNING: Unknown default host: null

    I expect to fix this along with API review (item 1. above)              [Not Started]

 4. Running tests with Ant using APR connector

    It looks that Embedded.start() does not send INIT_EVENT to
    AprLifecycleListener.

    A quick fix added: an explicit call to
      listener.lifecycleEvent(new LifecycleEvent(server, Lifecycle.INIT_EVENT));

    I think this goes away with test class API review (item 1. above)

 5. Running tests with Ant using APR connector crashes

    I have seen this stacktrace

    [junit] jan 09, 2016 10:29:58 AM org.apache.coyote.http11.Http11AprProtocol destroy
    [junit] INFO: Stopping Coyote HTTP/1.1 on http-127.0.0.1-auto-1-58371
    [junit] Exception in thread "http-127.0.0.1-auto-1-1" java.lang.NullPointerException
    [junit] 	at org.apache.tomcat.util.net.AprEndpoint.getPoller(AprEndpoint.java:367)
    [junit] 	at org.apache.tomcat.util.net.AprEndpoint$Worker.run(AprEndpoint.java:1764)
    [junit] 	at java.lang.Thread.run(Thread.java:745)

    or a JVM crash

    #  EXCEPTION_ACCESS_VIOLATION (0xc0000005) at pc=0x10006eb6, pid=4664, tid=2380

    Stack: [0x05ad0000,0x05b20000],  sp=0x05b1f3f4,  free space=316k
    Native frames: (J=compiled Java code, j=interpreted, Vv=VM code, C=native code)
    C  [tcnative-1.dll+0x6eb6]
    j  org.apache.tomcat.jni.Poll.poll(JJ[JZ)I+0
    j  org.apache.tomcat.util.net.AprEndpoint$Poller.run()V+320
    v  ~StubRoutines::call_stub
    V  [jvm.dll+0x1429aa]
    <system libraries...>
    
    Java frames: (J=compiled Java code, j=interpreted, Vv=VM code)
    j  org.apache.tomcat.jni.Poll.poll(JJ[JZ)I+0
    j  org.apache.tomcat.util.net.AprEndpoint$Poller.run()V+320
    v  ~StubRoutines::call_stub

    The cause is unknown.
    I suspect that the poller thread does not stop properly.


Further work / TODO:

 6. Backport support for running with a null docBase (without docBase).     [Not Started]

    In Tomcat 7 this is implemented by
      r1681953 (2015-05-27, BZ 57154)

 7. Backport other tests from Tomcat 7.                                     [In progress]



(Regarding BRANCH-README files - see Apache Subversion Community Guide
 http://subversion.apache.org/docs/community-guide/general.html#branch-policy
)
