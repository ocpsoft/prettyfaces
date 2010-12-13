/*
 * PrettyFaces is an OpenSource JSF library to create bookmarkable URLs.
 * Copyright (C) 2010 - Lincoln Baxter, III <lincoln@ocpsoft.com> This program
 * is free software: you can redistribute it and/or modify it under the terms of
 * the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version. This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details. You should have received a copy of the GNU Lesser General
 * Public License along with this program. If not, see the file COPYING.LESSER
 * or visit the GNU website at <http://www.gnu.org/licenses/>.
 */

package com.ocpsoft.pretty.faces.config.rewrite;

/**
 * Enumeration describing different methods of Inbound Redirecting
 * 
 * @author Lincoln Baxter, III <lincoln@ocpsoft.com>
 */
public enum Redirect {
    PERMANENT(301), TEMPORARY(302), CHAIN;

    private final int status;

    private Redirect()
    {
        status = -1;
    }

    private Redirect(final int status)
    {
        this.status = status;
    }

    public int getStatus()
    {
        return status;
    }
}
