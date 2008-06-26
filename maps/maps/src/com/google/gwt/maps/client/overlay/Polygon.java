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
package com.google.gwt.maps.client.overlay;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.maps.client.event.PolygonClickHandler;
import com.google.gwt.maps.client.event.PolygonRemoveHandler;
import com.google.gwt.maps.client.event.PolygonClickHandler.PolygonClickEvent;
import com.google.gwt.maps.client.event.PolygonRemoveHandler.PolygonRemoveEvent;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.impl.HandlerCollection;
import com.google.gwt.maps.client.impl.JsUtil;
import com.google.gwt.maps.client.impl.MapEvent;
import com.google.gwt.maps.client.impl.PolygonImpl;
import com.google.gwt.maps.client.impl.EventImpl.LatLngCallback;
import com.google.gwt.maps.client.impl.EventImpl.VoidCallback;
import com.google.gwt.maps.client.overlay.Overlay.ConcreteOverlay;

/**
 * This is a map overlay that draws a polygon on the map, using the vector
 * drawing facilities of the browser if they are available, or an image overlay
 * from Google servers otherwise. This is very similar to a
 * {@link com.google.gwt.maps.client.overlay.Polyline}, except that you can
 * additionally specify a fill color and opacity.
 */
public final class Polygon extends ConcreteOverlay {

  /**
   * Create a polygon from an array of polylines. Overlapping regions of the
   * polygons will be transparent.
   * 
   * @param polylines array of polylines to use as the outline for the polygon.
   * @return a new instance of Polygon.
   */
  public static Polygon fromEncoded(EncodedPolyline[] polylines) {
    return new Polygon(nativeFromEncoded(toJsArray(polylines)));
  }

  /**
   * Create a polygon from an array of polylines. Overlapping regions of the
   * polygons will be transparent.
   * 
   * @param polylines array of polylines to use as the outline for the polygon.
   * @param fill whether to fill in the polygon with the specified color.
   * @param color the color to use for the fill.
   * @param opacity Opacity to use for the fill.
   * @param outline <code>true</code>
   * @return a new instance of Polygon.
   */
  public static Polygon fromEncoded(EncodedPolyline[] polylines, boolean fill,
      String color, double opacity, boolean outline) {
    return new Polygon(nativeFromEncoded(toJsArray(polylines), fill, color,
        opacity, outline));
  }

  /**
   * Used to create a new Polygon by wrapping an existing GPolygon object. This
   * method is invoked by the JSIO library.
   * 
   * @param jsoPeer GPolygon object to wrap.
   * @return a new instance of Polygon.
   */
  @SuppressWarnings("unused")
  private static Polygon createPeer(JavaScriptObject jsoPeer) {
    return new Polygon(jsoPeer);
  }

  /**
   * This method is a little trick we can use in WebMode. EncodedPolyline is a
   * JSO subclass, and a JS array is really an object, so this JSNI method
   * basically just casts the array to a JSO. In Java things are different, so
   * this trick doesn't work - be sure to surround with a GWT.isScript() test.
   * 
   * @param array The array to pass into JavaScript.
   * @return a JsArray representing the input argument.
   */
  private static native JsArray<EncodedPolyline> nativeArrayToJavaScriptObject(
      EncodedPolyline[] array) /*-{
    return array;
  }-*/;

  private static native JavaScriptObject nativeFromEncoded(
      JsArray<EncodedPolyline> polylinesIn) /*-{
    return new $wnd.GPolygon.fromEncoded({polylines: polylinesIn});
  }-*/;

  private static native JavaScriptObject nativeFromEncoded(
      JsArray<EncodedPolyline> polylinesIn, boolean fillIn, String colorIn,
      double opacityIn, boolean outlineIn) /*-{
    return new $wnd.GPolygon.fromEncoded({polylines: polylinesIn, fill: fillIn, color: colorIn, opacity: opacityIn, outline: outlineIn});
  }-*/;

  private static JsArray<EncodedPolyline> toJsArray(EncodedPolyline[] array) {
    if (GWT.isScript()) {
      // This is the most efficient thing to do, and works in web mode
      return nativeArrayToJavaScriptObject(array);
    }

    // This is a workaround for hosted mode. Make a copy of the array.
    JsArray<EncodedPolyline> result = (JsArray<EncodedPolyline>) JavaScriptObject.createArray();
    for (int i = 0; i < array.length; ++i) {
      result.set(i, array[i]);
    }
    assert (array.length == result.length());
    return result;
  }

  private HandlerCollection<PolygonClickHandler> polygonClickHandlers;
  private HandlerCollection<PolygonRemoveHandler> polygonRemoveHandlers;

  /**
   * Create a Polygon from an array of points.
   * 
   * @param points the points to construct the polygon.
   */
  public Polygon(LatLng[] points) {
    super(PolygonImpl.impl.construct(JsUtil.toJsList(points)));
  }

  /**
   * Create a polygon from an array of points, specifying optional parameters.
   * 
   * @param points the points to construct the polygon.
   * @param strokeColor The line color, a string that contains the color in
   *          hexadecimal numeric HTML style, i.e. #RRGGBB.
   * @param strokeWeight The width of the line in pixels.
   * @param strokeOpacity The opacity of the line - a value between 0.0 and 1.0.
   * @param fillColor The fill color, a string that contains the color in
   *          hexadecimal numeric HTML style, i.e. #RRGGBB.
   * @param fillOpacity The opacity of the fill - a value between 0.0 and 1.0.
   */
  public Polygon(LatLng[] points, String strokeColor, int strokeWeight,
      double strokeOpacity, String fillColor, double fillOpacity) {
    super(PolygonImpl.impl.construct(JsUtil.toJsList(points), strokeColor,
        strokeWeight, strokeOpacity, fillColor, fillOpacity));
  }

  private Polygon(JavaScriptObject jsoPeer) {
    super(jsoPeer);
  }

  /**
   * This event is fired when the polygon is clicked. Note that this event also
   * subsequently triggers a "click" event on the map, where the polygon is
   * passed as the overlay argument within that event.
   * 
   * @param handler the handler to call when this event fires.
   */
  public void addPolygonClickHandler(final PolygonClickHandler handler) {
    maybeInitPolygonClickHandlers();

    polygonClickHandlers.addHandler(handler, new LatLngCallback() {
      @Override
      public void callback(LatLng latlng) {
        PolygonClickEvent e = new PolygonClickEvent(Polygon.this, latlng);
        handler.onClick(e);
      }
    });
  }

  /**
   * This event is fired when the polygon is removed from the map, using
   * {@link com.google.gwt.maps.client.MapWidget#removeOverlay} or
   * {@link com.google.gwt.maps.client.MapWidget#clearOverlays}.
   * 
   * @param handler the handler to call when this event fires.
   */
  public void addPolygonRemoveHandler(final PolygonRemoveHandler handler) {
    maybeInitPolygonRemoveHandlers();

    polygonRemoveHandlers.addHandler(handler, new VoidCallback() {
      @Override
      public void callback() {
        PolygonRemoveEvent e = new PolygonRemoveEvent(Polygon.this);
        handler.onRemove(e);
      }
    });
  }

  /**
   * Returns the position of the specified vertex in the polygon.
   * 
   * @param index the vertex to return.
   * @return the position of the specified vertex in the polygon.
   */
  public LatLng getVertex(int index) {
    return PolygonImpl.impl.getVertex(jsoPeer, index);
  }

  /**
   * Returns the number of verticies in the polygon.
   * 
   * @return the number of verticies in the polygon.
   */
  public int getVertexCount() {
    return PolygonImpl.impl.getVertexCount(jsoPeer);
  }

  /**
   * Returns true if the polygon is visible on the map.
   * 
   * @return true if the polygon is visible on the map.
   */
  public boolean isVisible() {
    return !PolygonImpl.impl.isHidden(jsoPeer);
  }

  /**
   * Removes a single handler of this map previously added with
   * {@link Polygon#addPolygonClickHandler(PolygonClickHandler)}.
   * 
   * @param handler the handler to remove
   */
  public void removePolygonClickHandler(PolygonClickHandler handler) {
    if (polygonClickHandlers != null) {
      polygonClickHandlers.removeHandler(handler);
    }
  }

  /**
   * Removes a single handler of this map previously added with
   * {@link Polygon#addPolygonRemoveHandler(PolygonRemoveHandler)}.
   * 
   * @param handler the handler to remove
   */
  public void removePolygonRemoveHandler(PolygonRemoveHandler handler) {
    if (polygonRemoveHandlers != null) {
      polygonRemoveHandlers.removeHandler(handler);
    }
  }

  /**
   * Returns <code>true</code> if this environment supports the
   * {@link Polygon#setVisible(boolean)} method.
   * 
   * @return true if setVisible(<code>false</code>) is supported.
   */
  public boolean supportsHide() {
    // TODO(zundel): after the polygon hide/show fix is in place (issue 101),
    // uncomment
    // return PolygonImpl.impl.supportsHide(jsoPeer);
    return false;
  }

  /**
   * Manually trigger the specified event on this object.
   * 
   * Note: The trigger() methods are provided for unit testing purposes only.
   * 
   * @param event an event to deliver to the handler.
   */
  void trigger(PolygonClickEvent event) {
    maybeInitPolygonClickHandlers();
    polygonRemoveHandlers.trigger(event.getLatLng());
  }

  /**
   * Manually trigger the specified event on this object.
   * 
   * Note: The trigger() methods are provided for unit testing purposes only.
   * 
   * @param event an event to deliver to the handler.
   */
  void trigger(PolygonRemoveEvent event) {
    maybeInitPolygonRemoveHandlers();
    polygonRemoveHandlers.trigger();
  }

  private void maybeInitPolygonClickHandlers() {
    if (polygonClickHandlers == null) {
      polygonClickHandlers = new HandlerCollection<PolygonClickHandler>(
          jsoPeer, MapEvent.CLICK);
    }
  }

  private void maybeInitPolygonRemoveHandlers() {
    if (polygonRemoveHandlers == null) {
      polygonRemoveHandlers = new HandlerCollection<PolygonRemoveHandler>(
          jsoPeer, MapEvent.REMOVE);
    }
  }

}