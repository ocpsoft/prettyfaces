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
package com.ocpsoft.pretty.faces.config.mapping;

import com.ocpsoft.pretty.faces.config.types.ConvertElement;
import com.ocpsoft.pretty.faces.util.StringUtils;

/**
 * @author Christian Kaltepoth <christian@kaltepoth.de>
 */
public class PathConverter
{
   private int index;
   private String converterId;

   public PathConverter()
   {
      // nothing
   }

   /**
    * Creates a path converter from the supplied JAXB object
    * 
    * @param converterElement The JAXB object
    */
   public PathConverter(ConvertElement converterElement)
   {
      index = converterElement.getIndex();
      if (StringUtils.isNotBlank(converterElement.getConverterId()))
      {
         converterId = converterElement.getConverterId().trim();
      }
   }

   public int getIndex()
   {
      return index;
   }

   public void setIndex(int index)
   {
      this.index = index;
   }

   public String getConverterId()
   {
      return converterId;
   }

   public void setConverterId(String converterId)
   {
      this.converterId = converterId;
   }

   @Override
   public int hashCode()
   {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((converterId == null) ? 0 : converterId.hashCode());
      result = prime * result + index;
      return result;
   }

   @Override
   public boolean equals(Object obj)
   {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      PathConverter other = (PathConverter) obj;
      if (converterId == null) {
         if (other.converterId != null)
            return false;
      }
      else if (!converterId.equals(other.converterId))
         return false;
      if (index != other.index)
         return false;
      return true;
   }

   @Override
   public String toString()
   {
      return "PathConverter [index=" + index + ", converterId=" + converterId + "]";
   }

}
