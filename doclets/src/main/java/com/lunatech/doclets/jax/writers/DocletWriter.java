/*
    Copyright 2009 Lunatech Research
    
    This file is part of jax-doclets.

    jax-doclets is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    jax-doclets is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with jax-doclets.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.lunatech.doclets.jax.writers;

import com.lunatech.doclets.jax.JAXConfiguration;
import com.lunatech.doclets.jax.Utils;
import com.sun.tools.doclets.formats.html.HtmlDocletWriter;

public class DocletWriter {

  private static final String VERSION = "0.10.0";

  protected HtmlDocletWriter writer;

  protected JAXConfiguration configuration;

  public DocletWriter(JAXConfiguration configuration, HtmlDocletWriter writer) {
    this.writer = writer;
    this.configuration = configuration;
  }

  public HtmlDocletWriter getWriter() {
    return writer;
  }

  public JAXConfiguration getConfiguration() {
    return configuration;
  }

  protected void open(String... tags) {
    for (String tag : tags)
      print("<" + tag + ">");
  }

  protected void close(String... tags) {
    for (String tag : tags)
      print("</" + tag + ">");
  }

  protected void tag(String... tags) {
    for (String tag : tags)
      print("<" + tag + "/>");
  }

  protected void around(String tag, String value) {
    open(tag);
    print(value);
    int space = tag.indexOf(' ');
    if (space > -1) {
      tag = tag.substring(0, space);
    }
    close(tag);
  }

  protected void printLink(boolean hasLink, String href, String text) {
    if (!hasLink)
      print(text);
    else
      around("a href='" + href + "'", text);
  }

  protected void printHeader(String title) {
    open("HTML");
    open("HEAD");
    around("TITLE", title);
    close("TITLE");
    tag("LINK REL='stylesheet' TYPE='text/css' HREF='" + writer.relativePath + "doclet.css' TITLE='Style'");
    String charset = configuration.parentConfiguration.charset;
    if (Utils.isEmptyOrNull(charset))
      charset = "UTF-8";
    print("<META http-equiv=\"Content-Type\" content=\"text/html; " + "charset=" + charset + "\">\n");
    printAdditionalHeader();
    close("HEAD");
    open("BODY");
    String msg = configuration.parentConfiguration.header;
    if (msg != null) {
      print(msg);
    }
  }

  protected void printAdditionalHeader() {}

  protected void printFooter() {
    String msg = configuration.parentConfiguration.footer;
    if (msg != null) {
      print(msg);
    }
    tag("hr");
    open("div class='footer'");
    print("Generated by <a href='http://www.lunatech-labs.com/open-source/jax-doclets'>Lunatech Labs jax-doclets</a> v" + VERSION);
    close("div");
    close("BODY");
    close("HTML");
  }

  protected void printMenu(String selected) {
    open("table class='menu'", "colgroup");
    tag("col", "col");
    close("colgroup");
    open("tbody", "tr");
    open("td class='NavBarCell1' colspan='2'");
    printTopMenu(selected);
    close("td", "tr");
    printThirdMenu();
    close("table");
  }

  protected void printThirdMenu() {}

  protected void printTopMenu(String selected) {
    open("table", "tbody", "tr");
    printMenuItem("Overview", writer.relativePath + "overview-summary.html", selected);
    printOtherMenuItems(selected);
    close("tr", "tbody", "table");
  }

  protected void printOtherMenuItems(String selected) {}

  protected void printMenuItem(String title, String href, String selected) {
    boolean isSelected = title.equals(selected);
    if (isSelected)
      open("th class='selected'");
    else
      open("th");
    if (href != null && !isSelected) {
      around("a href='" + href + "'", title);
    } else
      print(title);
    close("th");

  }

  protected void print(String str) {
    writer.write(str);
  }

  protected String escape(String str) {
    return str.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
  }
}
