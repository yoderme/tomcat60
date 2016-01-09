This branch demonstrates backport of client-server JUnit tests to Tomcat 6
(TomcatBaseTest, TestTomcat).

Currently this serves as a proof a concept.
I expect to cherry-pick some or all of the features back to tc6.0.x/trunk.

Created: 2016-01-06 from r1723242.
Last catch-up merge: 2016-01-09, merged up to r1723827.


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


 3. Support for starting Tomcat with a random port number (port number 0)
 has been implemented. (r1723545 + r1723551, BZ 52028).


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



Further work / TODO:

 4. Add support for running the tests with Ant.

  *  Add <target name="test"> to the main build.xml file.                   [Not Started]

     tomcat6-testing branch has some incomplete attempt at implementing this.
     See https://svn.apache.org/viewvc/tomcat/tc6.0.x/branches/tomcat6-testing/BRANCH-diff.diff?view=markup


  *  Drop useless test/build.xml file.                                      [Not Started]

  *  Update BUILDING.txt.                                                   [Not Started]

 5. Backport support for running with a null docBase (without docBase).     [Not Started]

    In Tomcat 7 this is implemented by
      r1681953 (2015-05-27, BZ 57154)

 6. Backport other tests from Tomcat 7.                                     [In progress]



(Regarding BRANCH-README files - see Apache Subversion Community Guide
 http://subversion.apache.org/docs/community-guide/general.html#branch-policy
)
