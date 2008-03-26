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
package com.google.gwt.maps.sample.maps.client;

import com.google.gwt.maps.client.InfoWindow;
import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.InfoWindowContent.InfoWindowTab;
import com.google.gwt.maps.client.event.InfoWindowListener;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * To create an info window, call the openInfoWindow method, passing it a
 * location and a DOM element to display. The following example code displays an
 * info window anchored to the center of the map with a simple "Hello, world"
 * message.
 */
public class InfoWindowDemo extends MapsDemo {

  private static final String TEST_DEFAULT = "Default Info Window";
  private static final String TEST_MAX_CONTENT = "Test maxContent";
  private static final String TEST_MAX_TITLE_CONTENT_WIDGET = "Test Maximized with Widgets";
  private static final String TEST_IMAGE = "Test adding an Image";
  private static final String TEST_NO_CLICK = "Test noClick";
  private static final String TEST_TABS = "Test with Tabs";
  private static final String TEST_INFO_WINDOW_LISTENER = "Test Info Window Listener";
  private static final String TEST_MAP_BLOWUP = "Test Map Blowup Info Window";

  private static HTML descHTML = null;
  private static final String descString = "<h2>InfoWindow Demos</h2>\n"
      + "<p>Tests the InfoWindow APIs</p>" + "<ol>\n" + " <li><b>"
      + TEST_DEFAULT
      + "</b>: Display an info window with a tree widget in "
      + "the center of the current viewport.  Click on the map outside the "
      + "and the InfoWindow will close.\n"
      + "Equivalent to Maps JavaScript API Example: "
      + "<a href=\"http://code.google.com/apis/maps/documentation/examples/map-infowindow.html\">"
      + "http://code.google.com/apis/maps/documentation/examples/map-infowindow.html</a></li>"
      + "<li><b>"
      + TEST_MAX_CONTENT
      + "</b>: Add a Maximized Title and Content to "
      + "the InfoWindow</li>\n"
      + "<li><b>"
      + TEST_MAX_TITLE_CONTENT_WIDGET
      + "</b>: Instead of text, use widgets"
      + "for the title and content when maximized.</li>\n"
      + "<li><b>"
      + TEST_IMAGE
      + "</b>: Use an image in the main InfoWindow.</li>"
      + "<li><b>"
      + TEST_NO_CLICK
      + "</b>: Turn on the 'noClick' setting so that the "
      + " InfoWindow won't close when you click on the map.</li>\n"
      + "<li><b>"
      + TEST_TABS
      + "</b>: Create an InfoWindow with three tabs.</li>"
      + "<li><b>"
      + TEST_INFO_WINDOW_LISTENER
      + "</b>: Register two "
      + "InfoWindowListeners</li>\n"
      + "<li><b>"
      + TEST_MAP_BLOWUP
      + "</b>: Display a Map Blowup inside an InfoWindow</li>" + "</ol>";

  public static MapsDemoInfo init() {
    return new MapsDemoInfo() {

      @Override
      public MapsDemo createInstance() {
        return new InfoWindowDemo();
      }

      @Override
      public HTML getDescriptionHTML() {
        if (descHTML == null) {
          descHTML = new HTML(descString);
        }
        return descHTML;
      }

      @Override
      public String getName() {
        return "Opening an Info Window";
      }
    };
  }

  private final MapWidget map;
  private final ListBox actionListBox;

  private InfoWindow info = null;

  public InfoWindowDemo() {

    VerticalPanel vertPanel = new VerticalPanel();
    vertPanel.setStyleName("hm-panel");

    map = new MapWidget(new LatLng(33.7814790, -84.3880580), 13);
    vertPanel.add(map);

    actionListBox = new ListBox();
    actionListBox.addItem(TEST_DEFAULT);
    actionListBox.addItem(TEST_IMAGE);
    actionListBox.addItem(TEST_NO_CLICK);
    actionListBox.addItem(TEST_TABS);
    actionListBox.addItem(TEST_MAX_CONTENT);
    actionListBox.addItem(TEST_MAX_TITLE_CONTENT_WIDGET);
    actionListBox.addItem(TEST_INFO_WINDOW_LISTENER);
    actionListBox.addItem(TEST_MAP_BLOWUP);

    actionListBox.addChangeListener(new ChangeListener() {
      public void onChange(Widget sender) {
        displayInfoWindow();
      }
    });

    map.setSize("500px", "450px");

    HorizontalPanel horizPanel = new HorizontalPanel();
    horizPanel.add(new Label("Choose Action:"));
    horizPanel.add(actionListBox);
    horizPanel.setSpacing(10);

    vertPanel.add(horizPanel);

    vertPanel.add(map);
    initWidget(vertPanel);
  }

  @Override
  public void onShow() {
    displayInfoWindow();
  }

  /**
   * Display one of the info Window test cases.
   */
  private void displayInfoWindow() {

    // pop down the existing info window.
    if (info != null) {
      info.close();
    }

    info = map.getInfoWindow();
    String selection = actionListBox.getItemText(actionListBox.getSelectedIndex());

    if (selection == null) {
      return;
    }

    InfoWindowContent content;

    if (selection.equals(TEST_MAX_CONTENT)) {

      // Demonstrate the use of the maxTitle and maxContent properties
      HTML htmlWidget = new HTML("<h1>ATTENTION PLEASE</h1>"
          + "<p> I have a few things to say to you (click maximize.)</p>");
      content = new InfoWindowContent(htmlWidget);
      content.setMaxContent("<p>Now is the time for all good men to come to the"
          + " aid of their country because we hold these truths to be self"
          + " evident, that I have a dream, that one day our children and our"
          + " children's children will tear down this wall!</p>"
          + "<p>Now is the time for all good men to come to the"
          + " aid of their country because we hold these truths to be self"
          + " evident, that I have a dream, that one day our children and our"
          + " children's children will tear down this wall!</p>");
      content.setMaxTitle("ATTENTION PLEASE");

    } else if (selection.equals(TEST_IMAGE)) {

      // An image that isn't loaded yet doesn't work well sometimes
      // Specify the width and height to work around this.
      HTML htmlWidget = new HTML(
          "<img src=\"boot.jpg\" width=\"235\" height=\"287\">");
      content = new InfoWindowContent(htmlWidget);
    } else if (selection.equals(TEST_NO_CLICK)) {

      // Demonstrates setting the info window to stay "sticky" and not
      // automatically close when the user clicks on the maps window.
      HTML htmlWidget = new HTML("<h1>STICKY INFO WINDOW</h1>"
          + "<p> Click if you must, you won't get rid of me that easily.</p>");
      content = new InfoWindowContent(htmlWidget);
      content.setNoCloseOnClick(true);
    } else if (selection.equals(TEST_TABS)) {

      // Display tabs in the InfoWindow
      content = displayInfoWindowTabs();
    } else if (selection.equals(TEST_MAX_TITLE_CONTENT_WIDGET)) {

      // Display the maximized content using widgets instead of strings.
      content = displayInfoWindowMaxWidget();
    } else if (selection.equals(TEST_INFO_WINDOW_LISTENER)) {

      // Display listeners when the info window is opened and closed.
      content = displayInfoWindowWithListeners();
    } else if (selection.equals(TEST_MAP_BLOWUP)) {

      // Display a Map Blowup Window
      content = new InfoWindowContent.MapBlowupContent();
    } else {

      // The default case
      Tree tree = new Tree();
      TreeItem foo = new TreeItem("Foo");
      tree.addItem(foo);
      TreeItem bar = new TreeItem("bar");
      foo.addItem(bar);
      bar.addItem("baz");
      bar.addItem("gwt");
      // max-height must be set in advance so info window is sized appropriately
      tree.setSize("217px", "104px");
      content = new InfoWindowContent(tree);
    }

    info.open(map.getCenter(), content);
  }

  private InfoWindowContent displayInfoWindowMaxWidget() {
    final InfoWindowContent content = new InfoWindowContent(
        "There's more to see (hit the maximize button)");
    content.setMaxTitle(new HTML("<i>Maximized Italic Boots</i>"));
    content.setMaxContent(new Image("boot.jpg"));
    return content;
  }

  private InfoWindowContent displayInfoWindowTabs() {

    InfoWindowTab tabs[] = new InfoWindowTab[3];

    tabs[0] = new InfoWindowTab("Tab 1", "<h1>Tab 1 Content</h1>");
    tabs[1] = new InfoWindowTab("Tab 2", "<h1>Tab 2 Content</h1>");
    tabs[2] = new InfoWindowTab("Tab 3", "<h1>Tab 3 Content</h1>");
    final InfoWindowContent content = new InfoWindowContent(tabs, 1);
    return content;
  }

  /**
   * Tests the addInfoWindowListener() method in the InfoWindowContent class.
   * 
   * @return an newly initialized content object to open an InfoWindow
   */
  private InfoWindowContent displayInfoWindowWithListeners() {
    InfoWindowContent content = new InfoWindowContent(
        "Expect 2 alerts on open and close.");
    content.addInfoWindowListener(new InfoWindowListener() {
      public void onInfoWindowClosed(MapWidget sender) {
        Window.alert("Closed InfoWindow (1)");
        if (sender == null) {
          Window.alert("null Map window!");
        }
      }

      public void onInfoWindowOpened(MapWidget sender) {
        Window.alert("Opened InfoWindow (1)");
        if (sender == null) {
          Window.alert("null Map window!");
        }
      }
    });
    content.addInfoWindowListener(new InfoWindowListener() {
      public void onInfoWindowClosed(MapWidget sender) {
        Window.alert("Closed InfoWindow (2)");
        if (sender == null) {
          Window.alert("null Map window!");
        }
      }

      public void onInfoWindowOpened(MapWidget sender) {
        Window.alert("Opened InfoWindow (2)");
        if (sender == null) {
          Window.alert("null Map window!");
        }
      }
    });
    return content;
  }
}
