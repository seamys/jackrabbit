/*
 * Copyright 2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.jackrabbit.webdav.property;

import org.apache.log4j.Logger;
import org.apache.jackrabbit.webdav.xml.ElementIterator;
import org.apache.jackrabbit.webdav.xml.DomUtil;
import org.w3c.dom.Element;

import java.util.HashSet;
import java.util.Collection;
import java.util.Iterator;

/**
 * <code>DavPropertyNameSet</code> represents a Set of {@link DavPropertyName}
 * objects.
 */
public class DavPropertyNameSet extends PropContainer {

    private static Logger log = Logger.getLogger(DavPropertyNameSet.class);
    private final HashSet set = new HashSet();

    /**
     * Create a new empty set.
     */
    public DavPropertyNameSet() {
    }

    /**
     * Create a new <code>DavPropertyNameSet</code> with the given initial values.
     *
     * @param initialSet
     */
    public DavPropertyNameSet(DavPropertyNameSet initialSet) {
        addAll(initialSet);
    }

    /**
     * Create a new <code>DavPropertyNameSet</code> from the given DAV:prop
     * element.
     *
     * @param propElement
     * @throws IllegalArgumentException if the specified element is <code>null</code>
     * or is not a DAV:prop element.
     */
    public DavPropertyNameSet(Element propElement) {
        if (!DomUtil.matches(propElement, XML_PROP, NAMESPACE)) {
            throw new IllegalArgumentException("'DAV:prop' element expected.");
        }

        // fill the set
        ElementIterator it = DomUtil.getChildren(propElement);
        while (it.hasNext()) {
            add(DavPropertyName.createFromXml(it.nextElement()));
        }
    }

    /**
     * Adds the specified {@link DavPropertyName} object to this
     * set if it is not already present.
     *
     * @param propertyName element to be added to this set.
     * @return <tt>true</tt> if the set did not already contain the specified
     * element.
     */
    public boolean add(DavPropertyName propertyName) {
        return set.add(propertyName);
    }

    /**
     * Add the property names contained in the specified set to this set.
     *
     * @param propertyNames
     * @return true if the set has been modified by this call.
     */
    public boolean addAll(DavPropertyNameSet propertyNames) {
        return set.addAll(propertyNames.getContent());
    }

    /**
     * Removes the specified {@link DavPropertyName} object from this set.
     *
     * @param propertyName
     * @return true if the given property name could be removed.
     * @see HashSet#remove(Object)
     */
    public boolean remove(DavPropertyName propertyName) {
        return set.remove(propertyName);
    }

    /**
     * @return Iterator over all <code>DavPropertyName</code>s contained in this
     * set.
     */
    public DavPropertyNameIterator iterator() {
        return new PropertyNameIterator();
    }

    //------------------------------------------------------< PropContainer >---
    /**
     * @see PropContainer#contains(DavPropertyName)
     */
    public boolean contains(DavPropertyName name) {
        return set.contains(name);
    }

    /**
     * @param contentEntry NOTE that an instance of <code>DavPropertyName</code>
     * in order to successfully add the given entry.
     * @return true if contentEntry is an instance of <code>DavPropertyName</code>
     * that could be added to this set. False otherwise.
     * @see PropContainer#addContent(Object)
     */
    public boolean addContent(Object contentEntry) {
        if (contentEntry instanceof DavPropertyName) {
            return add((DavPropertyName)contentEntry);
        }
        log.debug("DavPropertyName object expected. Found: " + contentEntry.getClass().toString());
        return false;
    }

    /**
     * @see PropContainer#isEmpty()
     */
    public boolean isEmpty() {
        return set.isEmpty();
    }

    /**
     * @see PropContainer#getContentSize()
     */
    public int getContentSize() {
        return set.size();
    }

    /**
     * @see PropContainer#getContent()
     */
    public Collection getContent() {
        return set;
    }

    //--------------------------------------------------------< inner class >---
    private class PropertyNameIterator implements DavPropertyNameIterator {

        private Iterator iter;

        private PropertyNameIterator() {
            this.iter = set.iterator();
        }

        public DavPropertyName nextPropertyName() {
            return (DavPropertyName)iter.next();
        }

        public void remove() {
            iter.remove();
        }

        public boolean hasNext() {
            return iter.hasNext();
        }

        public Object next() {
            return iter.next();
        }
    }
}