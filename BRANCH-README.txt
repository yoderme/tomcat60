This branch demonstrates backport of client-server JUnit tests to Tomcat 6
(TomcatBaseTest, TestTomcat).

Currently this serves as a proof a concept.
I expect to cherry-pick some or all of the features back to tc6.0.x/trunk.

Created: 2016-01-06 from r1723242.
Last catch-up merge: 2016-01-16, merged up to r1724907.


Current status / Completed:

 1. TomcatBaseTest, TestTomcat, Tomcat and some helper classes have been
 backported to Tomcat 6.

 Some methods are omitted / commented out.

 Tomcat class was moved from public java/ directory into test/ one. I
 consider this class as non-public API at this moment.


 2. Implemented support for starting Tomcat with a random port number
 (port number 0).

 Committed to tc6.0.x/trunk. (r1723545 + r1723551, BZ 52028).


 3. Implemented support for running the tests with Apache Ant.

 It can be used to test BIO, NIO and APR connectors.


 4. There are several test classes that run successfully and prove the concept.

 TestTomcat, TestConnector, TestApplicationHttpRequest.

 More test classes are to follow.



Known issues / FIXME:

 1. Tomcat 6 has class org.apache.catalina.ServerFactory that contains a
    singleton reference to a Server instance. This field has to be cleared
    after a test run.

    Current workaround in TomcatBaseTest.tearDown() is to use reflection to
    clean up the ServerFactory.server field.

    The TomcatBaseTest.setUp() method uses reflection to assert that the
    ServerFactory.server field has been cleared before the test run.

    I expect to add necessary access methods to ServerFactory class.
    Discussed in http://tomcat.markmail.org/thread/ko7ip7obvyaftwe4         [Not Started]


Further work / TODO:

 1. Backport support for running with a null docBase (without docBase).     [Not Started]

    In Tomcat 7 this is implemented by
      r1681953 (2015-05-27, BZ 57154)

 2. Backport other tests from Tomcat 7.                                     [In progress]


 3. All calls to method Tomcat.silence(host, contextPath) are               [Not Started]
    commented-out. It is likely that the log output is more verbose
    than it is in the same tests in Tomcat 7.

    This is OK for now, can be adjusted later.


(Regarding BRANCH-README files - see Apache Subversion Community Guide
 http://subversion.apache.org/docs/community-guide/general.html#branch-policy
)
