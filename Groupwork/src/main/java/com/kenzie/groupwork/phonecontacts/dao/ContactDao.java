package com.kenzie.groupwork.phonecontacts.dao;

import com.kenzie.groupwork.phonecontacts.model.Contact;
import com.kenzie.groupwork.phonecontacts.model.Name;
import com.kenzie.groupwork.phonecontacts.model.SortBy;
import com.kenzie.groupwork.phonecontacts.model.SortOrder;

import java.util.Comparator;
import java.util.SortedMap;
import java.util.TreeMap;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Class for accessing contacts data.
 * Note that we are making this class a Singleton since we're storing our contacts in-memory inside of this class in
 * TreeMaps. If this wasn't a Singleton, then every request would get a new copy of ContactDao with empty TreeMaps.
 */
@Singleton
public class ContactDao {

    private final SortedMap<Name, Contact> contactsSortedByFirstName;
    private final SortedMap<Name, Contact> contactsSortedByLastName;


    @Inject
    public ContactDao() {
        //sorting by first name
        Comparator<Name> byFirstName = Comparator.comparing(Name::getFirstName);
        this.contactsSortedByFirstName = new TreeMap<>(byFirstName);

        //sorting by last name
        Comparator<Name> byLastName = Comparator.comparing(Name::getLastName);
        this.contactsSortedByLastName = new TreeMap<>(byLastName);
    }


    /**
     * Saves the given contact.
     * @param contact to add.
     * @return The added contact.
     */
    public Contact addContact(Contact contact) {
        this.contactsSortedByFirstName.put(contact.getName(), contact);
        this.contactsSortedByLastName.put(contact.getName(), contact);
        return contact;
    }

    /**
     * Retrieves all contacts in the provided sort by order. If no sort parameters are given, returns the default
     * of sorting by last name in ascending order.
     * @param sortBy Attribute to sort contacts by, e.g. first name, last name.
     * @param sortOrder order to return contacts, either ascending or descending.
     * @return map of name to contacts in requested sorted order.
     */
    public SortedMap<Name, Contact> getContacts(SortBy sortBy, SortOrder sortOrder) {

        SortedMap<Name, Contact> selectedMap = sortBy == SortBy.FIRST_NAME ? // decide which map to use
                contactsSortedByFirstName : contactsSortedByLastName;


        if (sortOrder == SortOrder.DESCENDING) {// if DESCENDING return a reverse order map
            return new TreeMap<>(selectedMap).descendingMap();
        }


        return selectedMap;  // If ASCENDING return the map as is
    }

    /**
     * Removes all contacts.
     */
    public void clearContacts() {
        this.contactsSortedByFirstName.clear();
        this.contactsSortedByLastName.clear();
    }

    /**
     * Retrieves a portion of the contacts whose Name is equal to or after the given startKey based on the sorting
     * parameters.
     *
     * @param startKey The Name that should be the first contact in the returned Map
     * @param sortBy Attribute to sort contacts by, e.g. first name, last name.
     * @param sortOrder order to return contacts, either ascending or descending.
     * @return portion of contacts that includes startKey and all contacts after startKey based on requested sorted
     * order.
     */
    public SortedMap<Name, Contact> getContactsStartingAt(Name startKey, SortBy sortBy, SortOrder sortOrder) {

        SortedMap<Name, Contact> sortedContacts = getContacts(sortBy, sortOrder); // get all contacts in the sort order

        if (sortOrder == SortOrder.DESCENDING) {

            return sortedContacts.tailMap(startKey);// descending order works directly because its already descending
        } else {

            return sortedContacts.tailMap(startKey);  // For asending order use tailMap
        }

    }
}
