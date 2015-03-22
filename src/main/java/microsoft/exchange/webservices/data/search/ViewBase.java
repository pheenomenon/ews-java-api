/*
 * The MIT License
 * Copyright (c) 2012 Microsoft Corporation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package microsoft.exchange.webservices.data.search;

import microsoft.exchange.webservices.data.EwsServiceXmlWriter;
import microsoft.exchange.webservices.data.PropertySet;
import microsoft.exchange.webservices.data.core.requests.ServiceRequestBase;
import microsoft.exchange.webservices.data.XmlAttributeNames;
import microsoft.exchange.webservices.data.attributes.EditorBrowsable;
import microsoft.exchange.webservices.data.enumerations.EditorBrowsableState;
import microsoft.exchange.webservices.data.enumerations.ServiceObjectType;
import microsoft.exchange.webservices.data.enumerations.XmlNamespace;
import microsoft.exchange.webservices.data.exceptions.ServiceValidationException;
import microsoft.exchange.webservices.data.exceptions.ServiceVersionException;
import microsoft.exchange.webservices.data.exceptions.ServiceXmlSerializationException;

import javax.xml.stream.XMLStreamException;

/**
 * Represents the base view class for search operations.
 */
@EditorBrowsable(state = EditorBrowsableState.Never)
public abstract class ViewBase {

  /**
   * The property set.
   */
  private PropertySet propertySet;

  /**
   * Initializes a new instance of the "ViewBase" class.
   */
  ViewBase() {
  }

  /**
   * Validates this view.
   *
   * @param request The request using this view.
   * @throws ServiceValidationException the service validation exception
   * @throws microsoft.exchange.webservices.data.exceptions.ServiceVersionException    the service version exception
   */
  public void internalValidate(ServiceRequestBase request)
      throws ServiceValidationException, ServiceVersionException {
    if (this.getPropertySet() != null) {
      this.getPropertySet().internalValidate();
      this.getPropertySet().validateForRequest(
          request,
          true /* summaryPropertiesOnly */);
    }
  }

  /**
   * Writes this view to XML.
   *
   * @param writer The writer
   * @throws ServiceXmlSerializationException the service xml serialization exception
   * @throws Exception                        the exception
   */
  protected void internalWriteViewToXml(EwsServiceXmlWriter writer)
      throws ServiceXmlSerializationException, Exception {
    Integer maxEntriesReturned = this.getMaxEntriesReturned();

    if (maxEntriesReturned != null) {
      writer.writeAttributeValue(XmlAttributeNames.MaxEntriesReturned,
          maxEntriesReturned);
    }
  }

  /**
   * Writes the search settings to XML.
   *
   * @param writer  The Writer
   * @param groupBy The group by clause.
   * @throws javax.xml.stream.XMLStreamException the xML stream exception
   * @throws microsoft.exchange.webservices.data.exceptions.ServiceXmlSerializationException    the service xml serialization exception
   */
  protected abstract void internalWriteSearchSettingsToXml(
      EwsServiceXmlWriter writer, Grouping groupBy)
      throws XMLStreamException, ServiceXmlSerializationException;

  /**
   * Writes OrderBy property to XML.
   *
   * @param writer The Writer
   * @throws javax.xml.stream.XMLStreamException the xML stream exception
   * @throws ServiceXmlSerializationException    the service xml serialization exception
   */
  public abstract void writeOrderByToXml(EwsServiceXmlWriter writer)
      throws XMLStreamException, ServiceXmlSerializationException;

  /**
   * Gets the name of the view XML element.
   *
   * @return TheXml Element name
   */
  protected abstract String getViewXmlElementName();

  /**
   * Gets the maximum number of items or folders the search operation should
   * return.
   *
   * @return The maximum number of items or folders that should be returned by
   * the search operation.
   */
  protected abstract Integer getMaxEntriesReturned();

  /**
   * Gets the type of service object this view applies to.
   *
   * @return A ServiceObjectType value.
   */
  protected abstract ServiceObjectType getServiceObjectType();

  /**
   * Writes the attributes to XML.
   *
   * @param writer The writer.
   * @throws microsoft.exchange.webservices.data.exceptions.ServiceXmlSerializationException the service xml serialization exception
   */
  public abstract void writeAttributesToXml(EwsServiceXmlWriter writer)
      throws ServiceXmlSerializationException;

  /**
   * Writes to XML.
   *
   * @param writer  The writer.
   * @param groupBy The group by clause.
   * @throws Exception the exception
   */
  public void writeToXml(EwsServiceXmlWriter writer, Grouping groupBy)
      throws Exception {
    this.getPropertySetOrDefault().writeToXml(writer,
        this.getServiceObjectType());
    writer.writeStartElement(XmlNamespace.Messages, this
        .getViewXmlElementName());
    this.internalWriteViewToXml(writer);
    writer.writeEndElement(); // this.GetViewXmlElementName()
    this.internalWriteSearchSettingsToXml(writer, groupBy);
  }

  /**
   * Gets the property set or the default.
   *
   * @return PropertySet
   */
  public PropertySet getPropertySetOrDefault() {
    if (this.getPropertySet() == null) {
      return PropertySet.getFirstClassProperties();
    } else {
      return this.getPropertySet();
    }
  }

  /**
   * Gets the property set. PropertySet determines which properties will be
   * loaded on found items. If PropertySet is null, all first class properties
   * are loaded on found items.
   *
   * @return the property set
   */
  public PropertySet getPropertySet() {
    return propertySet;
  }

  /**
   * Sets the property set. PropertySet determines which properties will be
   * loaded on found items. If PropertySet is null, all first class properties
   * are loaded on found items.
   *
   * @param propertySet The property set
   */
  public void setPropertySet(PropertySet propertySet) {
    this.propertySet = propertySet;
  }

}
