/*
 * (c) Copyright 2007, 2008, 2009 Hewlett-Packard Development Company, LP
 * All rights reserved.
 * [See end of file]
 */

package com.hp.hpl.jena.sparql.pfunction;

import org.openjena.atlas.io.IndentedWriter ;

import com.hp.hpl.jena.graph.Node ;
import com.hp.hpl.jena.sparql.engine.ExecutionContext ;
import com.hp.hpl.jena.sparql.engine.QueryIterator ;
import com.hp.hpl.jena.sparql.expr.ExprList ;
import com.hp.hpl.jena.sparql.procedure.Procedure ;
import com.hp.hpl.jena.sparql.serializer.SerializationContext ;
import com.hp.hpl.jena.sparql.util.FmtUtils ;
import com.hp.hpl.jena.sparql.util.PrintSerializableBase ;

/** Adapter between property functions and server procedures
 *  When called, this wrapper reconstructs the usual property function calling conventions. */ 
public class ProcedurePF extends PrintSerializableBase implements Procedure
{
    private PropertyFunction propFunc ;
    private PropFuncArg subjArg ;
    private PropFuncArg objArg ;
    private Node pfNode ;

    public ProcedurePF(PropertyFunction propFunc, PropFuncArg subjArg, Node pfNode, PropFuncArg objArg)
    {
        this.propFunc = propFunc ;
        this.subjArg = subjArg ;
        this.pfNode = pfNode ;
        this.objArg = objArg ;
    }
    
    // Procedure interface
 
    public QueryIterator proc(QueryIterator input, ExecutionContext execCxt)
    {
        return propFunc.exec(input, subjArg, pfNode, objArg, execCxt) ;
    }

    public void build(Node procId, ExprList args, ExecutionContext execCxt)
    {}

    public void output(IndentedWriter out, SerializationContext sCxt)
    {
        out.print("ProcedurePF ["+FmtUtils.stringForNode(pfNode, sCxt)+"]") ;
        out.print("[") ;
        subjArg.output(out, sCxt) ;
        out.print("][") ;
        objArg.output(out, sCxt) ;
        out.print("]") ;
        out.println() ;
    }

}

/*
 * (c) Copyright 2007, 2008, 2009 Hewlett-Packard Development Company, LP
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. The name of the author may not be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */