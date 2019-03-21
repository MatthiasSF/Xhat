package client;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

/**
 * Klassen lagrar en anvÃ¤ndares kontakter, grupper och konversationer.
 *
 */
public class Data {
	private HashMap<String, Contact> contacts = new HashMap<>();
	private HashMap<String, GroupChat> groups = new HashMap<>();
	
	public Data() {
		//read from disk if we want
	}
	
	public void addContact(Contact contact) {
		contacts.put(contact.getName(), contact);
	}
	
	public Contact getContact(String contactName) {
		return contacts.get(contactName);
	}
	//Bugg04: Kontakt går ej att radera. 
	/** 
	 * Clear the list of contacts entirely. 
	 * This works out okay since we iterate through it all anyways. 
	 * A prettier solution would be to pass in a username and remove just that one.
	 * In practice it probably is about the same amount of computing. 
	 */
	public void removeAllContacts() {
	    contacts.clear();
	}
	
	public boolean isContact(String contactName) {
		return contacts.containsKey(contactName);
	}
	
	public Collection<Contact> getContacts() {
		return contacts.values();
	}
	
	public Set<String> getContactKeys() {
		return contacts.keySet();
	}
	
	public void addGroup(String groupName, String groupId) {
		groups.put(groupId, new GroupChat(groupName, groupId));
	}
	
	public GroupChat getGroup(String groupId) {
		return groups.get(groupId);
	}
	
	public void clearGroups() {
		groups.clear();
	}
	
	public Collection<GroupChat> getGroups() {
		return groups.values();
	}
	
}
