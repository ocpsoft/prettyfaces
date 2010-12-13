/*
 * Copyright 2010 Lincoln Baxter, III
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ocpsoft.pretty.util;

import java.net.URLDecoder;

import com.ocpsoft.pretty.PrettyException;

/**
 * Utility class for HTML form encoding. This class contains methods for
 * converting a String to the application/x-www-form-urlencoded MIME format.
 * 
 * @author lb3
 */
public class HTTPDecoder
{
    public String decode(final String value)
    {
        String result = value;
        if (value != null)
        {
            try
            {
                result = URLDecoder.decode(value, "UTF-8");
            }
            catch (Exception e)
            {
                throw new PrettyException("Could not URLDecode value <" + value + ">", e);
            }
        }
        return result;
    }
}
