This branch demonstrates backport of client-server JUnit tests to Tomcat 6
(TomcatBaseTest, TestTomcat).

Currently this serves as a proof a concept.
I expect to cherry-pick some or all of the features back to tc6.0.x/trunk.

Created: 2016-01-06 from r1723242.
Last catch-up merge: 2016-01-21, merged up to r1726051.


Current status / Completed:

 1. TomcatBaseTest, TestTomcat, Tomcat and some helper classes have been
 backported to Tomcat 6.

 Some methods are omitted / commented out.

 Tomcat class was moved from public java/ directory into test/ one. I
 consider this class as non-public API at this moment.


 2. Implemented support for starting Tomcat with a random port number
 (port number 0).

 Committed to tc6.0.x/trunk. (r1723545 + r1723551, BZ 52028).


 3. Implemented support for configuring a Context with a null docBase.

 Committed to tc6.0.x/trunk. (r1725061, BZ 57154).


 4. Implemented support for running the tests with Apache Ant.

 It can be used to test BIO, NIO and APR connectors.


 5. Fixed several issues that prevented sequential running of several
 instances of Tomcat in the same JVM.
 (Parallel running is not implemented. There is no intent to implement it.)

 Committed to tc6.0.x/trunk. (r1726003, r1726031, r1726035).


 6. There are several test classes that run successfully and prove the concept.

 TestTomcat, TestConnector, TestApplicationHttpRequest.

 More test classes are to follow.



Known issues / FIXME:

 None.


Further work / TODO:

 1. Backport other tests from Tomcat 7.                                     [In progress]


(Regarding BRANCH-README files - see Apache Subversion Community Guide
 http://subversion.apache.org/docs/community-guide/general.html#branch-policy
)
