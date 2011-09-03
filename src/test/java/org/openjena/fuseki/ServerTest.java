/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openjena.fuseki;

import java.util.Arrays ;

import org.junit.AfterClass ;
import org.junit.BeforeClass ;
import org.openjena.atlas.junit.BaseTest ;
import org.openjena.atlas.logging.Log ;
import org.openjena.fuseki.config.FusekiConfig ;
import org.openjena.fuseki.config.FusekiConfig.ServiceDesc ;
import org.openjena.fuseki.http.UpdateRemote ;
import org.openjena.fuseki.server.SPARQLServer ;

import com.hp.hpl.jena.sparql.core.DatasetGraph ;
import com.hp.hpl.jena.sparql.core.DatasetGraphFactory ;

/** Manage a server for testing.
 * <pre>
    \@BeforeClass public static void beforeClass() { ServerTest.allocServer() ; }
    \@AfterClass public static void afterClass() { ServerTest.freeServer() ; }
    </pre>
 */
public class ServerTest extends BaseTest
{
    // Abstraction that runs one server.
    // Inherit from this class to add starting/stopping a server.  
    
    public static final int port             = 3535 ;
    public static final String datasetPath   = "/dataset" ;
    public static final String serviceUpdate = "http://localhost:"+port+datasetPath+"/update" ; 
    public static final String serviceQuery  = "http://localhost:"+port+datasetPath+"/query" ; 
    public static final String serviceREST   = "http://localhost:"+port+datasetPath+"/data" ;
    
    private static int referenceCount = 0 ;
    private static SPARQLServer server = null ; 
    
    // If not inheriting from this class, call:
    
    @BeforeClass static public void allocServer()
    { 
        if ( referenceCount == 0 )
            serverStart() ;
        referenceCount ++ ;
    }
    
    @AfterClass static public void freeServer() 
    { 
        referenceCount -- ;
        if ( referenceCount == 0 )
            serverStop() ;
    }
    
    protected static void serverStart()
    {
        Log.logLevel(Fuseki.serverLog.getName(), org.apache.log4j.Level.WARN, java.util.logging.Level.WARNING) ;
        Log.logLevel(Fuseki.requestLog.getName(), org.apache.log4j.Level.WARN, java.util.logging.Level.WARNING) ;
        Log.logLevel("org.eclipse.jetty", org.apache.log4j.Level.WARN, java.util.logging.Level.WARNING) ;
        
        DatasetGraph dsg = DatasetGraphFactory.createMem() ;
        ServiceDesc sDesc = FusekiConfig.defaultConfiguration(datasetPath, dsg, true) ;
        SPARQLServer server = new SPARQLServer(null, port, Arrays.asList(sDesc) ) ;
        server.start() ;
    }
    
    protected static void serverStop()
    {
        server.stop() ;
        Log.logLevel(Fuseki.serverLog.getName(), org.apache.log4j.Level.INFO, java.util.logging.Level.INFO) ;
        Log.logLevel(Fuseki.requestLog.getName(), org.apache.log4j.Level.INFO, java.util.logging.Level.INFO) ;
        Log.logLevel("org.eclipse.jetty", org.apache.log4j.Level.INFO, java.util.logging.Level.INFO) ;
    }
    
    public static void resetServer()
    {
        UpdateRemote.executeClear(serviceUpdate) ;
    }
}