/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.catalina.startup;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import org.apache.catalina.Host;
import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleEvent;
import org.apache.catalina.LifecycleListener;
import org.apache.catalina.Wrapper;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.core.StandardHost;
import org.apache.catalina.deploy.ContextEnvironment;
import org.apache.catalina.deploy.ContextResourceLink;
import org.apache.catalina.ha.context.ReplicatedContext;
import org.apache.catalina.realm.GenericPrincipal;
import org.apache.catalina.realm.RealmBase;
import org.apache.tomcat.util.buf.ByteChunk;

public class TestTomcat extends TomcatBaseTest {

    /**
     * Simple servlet to test in-line registration.
     */
    public static class HelloWorld extends HttpServlet {

        private static final long serialVersionUID = 1L;

        @Override
        public void doGet(HttpServletRequest req, HttpServletResponse res)
                throws IOException {
            res.getWriter().write("Hello world");
        }
    }

    /**
     * Simple servlet to test the default session manager.
     */
    public static class HelloWorldSession extends HttpServlet {

        private static final long serialVersionUID = 1L;

        @Override
        public void doGet(HttpServletRequest req, HttpServletResponse res)
                throws IOException {
            HttpSession s = req.getSession(true);
            s.getId();
            res.getWriter().write("Hello world");
        }
    }

//FIXME
//    /**
//     * Simple servlet to test JNDI
//     */
//    public static class HelloWorldJndi extends HttpServlet {
//
//        private static final long serialVersionUID = 1L;
//
//        private static final String JNDI_ENV_NAME = "test";
//
//        @Override
//        public void doGet(HttpServletRequest req, HttpServletResponse res)
//                throws IOException {
//
//            String name = null;
//
//            try {
//                Context initCtx = new InitialContext();
//                Context envCtx = (Context) initCtx.lookup("java:comp/env");
//                name = (String) envCtx.lookup(JNDI_ENV_NAME);
//            } catch (NamingException e) {
//                throw new IOException(e);
//            }
//
//            res.getWriter().write("Hello, " + name);
//        }
//    }
//
//    /**
//     * Servlet that tries to obtain a URL for WEB-INF/web.xml
//     */
//    public static class GetResource extends HttpServlet {
//
//        private static final long serialVersionUID = 1L;
//
//        @Override
//        public void doGet(HttpServletRequest req, HttpServletResponse res)
//        throws IOException {
//            URL url = req.getServletContext().getResource("/WEB-INF/web.xml");
//
//            res.getWriter().write("The URL obtained for /WEB-INF/web.xml was ");
//            if (url == null) {
//                res.getWriter().write("null");
//            } else {
//                res.getWriter().write(url.toString() + "\n");
//                res.getWriter().write("The first 20 characters of that resource are:\n");
//
//                // Read some content from the resource
//                URLConnection conn = url.openConnection();
//
//                InputStream is = null;
//                Reader reader = null;
//                char cbuf[] = new char[20];
//                int read = 0;
//                try {
//                    is = conn.getInputStream();
//                    reader = new InputStreamReader(is);
//                    while (read < 20) {
//                        int len = reader.read(cbuf, read, cbuf.length - read);
//                        res.getWriter().write(cbuf, read, len);
//                        read = read + len;
//                    }
//                } finally {
//                    if (reader != null) {
//                        try { reader.close(); } catch(IOException ioe) {/*Ignore*/}
//                    }
//                    if (is != null) {
//                        try { is.close(); } catch(IOException ioe) {/*Ignore*/}
//                    }
//                }
//
//
//            }
//
//
//        }
//    }
//
//    /**
//     * Simple servlet to test initialization of servlet instances.
//     */
//    private static class InitCount extends HttpServlet {
//
//        private static final long serialVersionUID = 1L;
//
//        private AtomicInteger callCount = new AtomicInteger(0);
//
//        @Override
//        public void init() throws ServletException {
//            super.init();
//            callCount.incrementAndGet();
//        }
//
//        @Override
//        protected void doGet(HttpServletRequest req, HttpServletResponse resp)
//                throws ServletException, IOException {
//            resp.setContentType("text/plain");
//            resp.getWriter().print("OK");
//        }
//
//        public int getCallCount() {
//            return callCount.intValue();
//        }
//    }
//
//
//    /**
//     * Simple Realm that uses a configurable {@link Map} to link user names and
//     * passwords.
//     */
//    public static final class MapRealm extends RealmBase {
//        private Map<String,String> users = new HashMap<String,String>();
//        private Map<String,List<String>> roles =
//            new HashMap<String,List<String>>();
//
//        public void addUser(String username, String password) {
//            users.put(username, password);
//        }
//
//        public void addUserRole(String username, String role) {
//            List<String> userRoles = roles.get(username);
//            if (userRoles == null) {
//                userRoles = new ArrayList<String>();
//                roles.put(username, userRoles);
//            }
//            userRoles.add(role);
//        }
//
//        @Override
//        protected String getName() {
//            return "MapRealm";
//        }
//
//        @Override
//        protected String getPassword(String username) {
//            return users.get(username);
//        }
//
//        @Override
//        protected Principal getPrincipal(String username) {
//            return new GenericPrincipal(username, getPassword(username),
//                    roles.get(username));
//        }
//
//    }

    /**
     * Start tomcat with a single context and one
     * servlet - all programmatic, no server.xml or
     * web.xml used.
     *
     * @throws Exception
     */
    @Test
    public void testProgrammatic() throws Exception {
        Tomcat tomcat = getTomcatInstance();

        // // No file system docBase required
        // org.apache.catalina.Context ctx = tomcat.addContext("", null);

        // Must have a real docBase - just use temp
        // FIXME: Implement support for null docBase (r1681953)
        org.apache.catalina.Context ctx = tomcat.addContext("",
                System.getProperty("java.io.tmpdir"));

        // You can customize the context by calling
        // its API

        Tomcat.addServlet(ctx, "myServlet", new HelloWorld());
        ctx.addServletMapping("/", "myServlet");

        tomcat.start();

        ByteChunk res = getUrl("http://localhost:" + getPort() + "/");
        assertEquals("Hello world", res.toString());
    }

    @Test
    public void testSingleWebapp() throws Exception {
        Tomcat tomcat = getTomcatInstance();

        File appDir = new File(getBuildDirectory(), "webapps/examples");

        tomcat.addWebapp(null, "/examples", appDir.getAbsolutePath());

        tomcat.start();

        ByteChunk res = getUrl("http://localhost:" + getPort() +
                "/examples/servlets/servlet/HelloWorldExample");
        String text = res.toString();
        assertTrue(text, text.indexOf("<h1>Hello World!</h1>") > 0);
    }
}
