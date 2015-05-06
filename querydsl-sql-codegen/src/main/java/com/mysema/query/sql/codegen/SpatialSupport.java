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
package com.mysema.query.sql.codegen;

import java.util.Map;
import java.util.Set;

import org.geolatte.geom.Geometry;
import org.geolatte.geom.GeometryCollection;
import org.geolatte.geom.LineString;
import org.geolatte.geom.LinearRing;
import org.geolatte.geom.MultiLineString;
import org.geolatte.geom.MultiPoint;
import org.geolatte.geom.MultiPolygon;
import org.geolatte.geom.Point;
import org.geolatte.geom.Polygon;
import org.geolatte.geom.PolyHedralSurface;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mysema.codegen.model.ClassType;
import com.mysema.codegen.model.Type;
import com.mysema.query.codegen.AbstractModule;
import com.mysema.query.codegen.CodegenModule;
import com.mysema.query.codegen.TypeMappings;
import com.mysema.query.spatial.path.GeometryCollectionPath;
import com.mysema.query.spatial.path.GeometryPath;
import com.mysema.query.spatial.path.LineStringPath;
import com.mysema.query.spatial.path.LinearRingPath;
import com.mysema.query.spatial.path.MultiLineStringPath;
import com.mysema.query.spatial.path.MultiPointPath;
import com.mysema.query.spatial.path.MultiPolygonPath;
import com.mysema.query.spatial.path.PointPath;
import com.mysema.query.spatial.path.PolygonPath;
import com.mysema.query.spatial.path.PolyhedralSurfacePath;
import com.mysema.query.sql.Configuration;
import com.mysema.query.sql.spatial.RelationalPathSpatial;

/**
 * @author tiwe
 *
 */
public final class SpatialSupport {

    private static void registerTypes(Configuration configuration) {
        // mysql & postgres
        configuration.registerType("geometry", Geometry.class);
        configuration.registerType("point", Point.class);
        configuration.registerType("linestring", LineString.class);
        configuration.registerType("polygon", Polygon.class);
        configuration.registerType("multipoint", MultiPoint.class);
        configuration.registerType("multilinestring", MultiLineString.class);
        configuration.registerType("multipolygon", MultiPolygon.class);
        configuration.registerType("geometrycollection", GeometryCollection.class);
        // teradata
        configuration.registerType("sysudtlib.st_geometry", Geometry.class);
    }

    private static void registerTypes(TypeMappings typeMappings) {
        Map<Class<?>, Class<?>> mappings = Maps.newHashMap();
        mappings.put(GeometryCollection.class, GeometryCollectionPath.class);
        mappings.put(Geometry.class, GeometryPath.class);
        mappings.put(LinearRing.class, LinearRingPath.class);
        mappings.put(LineString.class, LineStringPath.class);
        mappings.put(MultiLineString.class, MultiLineStringPath.class);
        mappings.put(MultiPoint.class, MultiPointPath.class);
        mappings.put(MultiPolygon.class, MultiPolygonPath.class);
        mappings.put(Point.class, PointPath.class);
        mappings.put(Polygon.class, PolygonPath.class);
        mappings.put(PolyHedralSurface.class, PolyhedralSurfacePath.class);
        for (Map.Entry<Class<?>, Class<?>> entry : mappings.entrySet()) {
            Type type = new ClassType(entry.getKey());
            typeMappings.register(type, new ClassType(entry.getValue(), type));
        }
    }

    private static void addImports(AbstractModule module) {
        Set<String> imports = module.get(Set.class, CodegenModule.IMPORTS);
        String packageName = GeometryPath.class.getPackage().getName();
        if (imports.isEmpty()) {
            imports = ImmutableSet.of(packageName);
        } else {
            Set<String> old = imports;
            imports = Sets.newHashSet();
            imports.addAll(old);
            imports.add(packageName);
        }
        module.bind(CodegenModule.IMPORTS, imports);
    }

    public static void addSupport(AbstractModule module) {
        module.bindInstance(SQLCodegenModule.ENTITYPATH_TYPE, RelationalPathSpatial.class);
        registerTypes(module.get(Configuration.class));
        registerTypes(module.get(TypeMappings.class));
        addImports(module);
    }

    private SpatialSupport() {}

}
