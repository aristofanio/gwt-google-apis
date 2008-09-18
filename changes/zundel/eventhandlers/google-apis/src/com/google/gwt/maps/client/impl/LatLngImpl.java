/*
 * Copyright 2008 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.gwt.maps.client.impl;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.jsio.client.JSFlyweightWrapper;
import com.google.gwt.jsio.client.Constructor;
import com.google.gwt.maps.client.geom.LatLng;

/**
 * 
 */
public interface LatLngImpl extends JSFlyweightWrapper {

  @Constructor("$wnd.GLatLng")
  JavaScriptObject construct(double latitude, double longitude);

  @Constructor("$wnd.GLatLng")
  JavaScriptObject construct(double latitude, double longitude,
      boolean unbounded);

  double distanceFrom(JavaScriptObject jso, LatLng other);

  boolean equals(JavaScriptObject jso, LatLng other);

  double lat(JavaScriptObject jso);

  double latRadians(JavaScriptObject jso);

  double lng(JavaScriptObject jso);

  double lngRadians(JavaScriptObject jso);

  String toString(JavaScriptObject jso);

  String toUrlValue(JavaScriptObject jso, int precision);

}