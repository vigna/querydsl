/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mysema.query.domain;

import org.junit.Test;

import com.mysema.query.annotations.PropertyType;
import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.annotations.QueryType;
import com.mysema.query.types.path.ComparablePath;
import com.mysema.query.types.path.DatePath;
import com.mysema.query.types.path.DateTimePath;
import com.mysema.query.types.path.SimplePath;
import com.mysema.query.types.path.TimePath;

public class QueryTypeTest extends AbstractTest {

    @QueryEntity
    public static class QueryTypeEntity {
        @QueryType(PropertyType.SIMPLE)
        public String stringAsSimple;

        @QueryType(PropertyType.COMPARABLE)
        public String stringAsComparable;

        @QueryType(PropertyType.DATE)
        public String stringAsDate;

        @QueryType(PropertyType.DATETIME)
        public String stringAsDateTime;

        @QueryType(PropertyType.TIME)
        public String stringAsTime;

        @QueryType(PropertyType.NONE)
        public String stringNotInQuerydsl;

    }

    @Test
    public void test() throws SecurityException, NoSuchFieldException{
        start(QQueryTypeTest_QueryTypeEntity.class, QQueryTypeTest_QueryTypeEntity.queryTypeEntity);
        match(SimplePath.class, "stringAsSimple");
        match(ComparablePath.class, "stringAsComparable");
        match(DatePath.class, "stringAsDate");
        match(DateTimePath.class, "stringAsDateTime");
        match(TimePath.class, "stringAsTime");
        assertMissing("stringNotInQuerydsl");
    }
}
