package hk.ebsl.mfms.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import hk.ebsl.mfms.dto.Location;
import hk.ebsl.mfms.manager.impl.LocationManagerImpl;

public class LocationTreeNode {

	public static final Logger logger = Logger.getLogger(LocationTreeNode.class);

	private Location location = null;

	private ArrayList<LocationTreeNode> children = null;

	private LocationTreeNode parent = null;

	private boolean privileged = true;

	public LocationTreeNode() {

	}

	public LocationTreeNode(Location location) {

		this.location = location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Location getLocation() {
		return location;
	}

	public void build(List<Integer> privilegedLocationKeyList) {

		// set location privileged

		if (privilegedLocationKeyList == null) {
			// if list is not supplied, use default
		} else {
			if (privilegedLocationKeyList.contains(location.getKey())) {
				privileged = true;
			} else {
				privileged = false;
			}
		}

		// build this node's children

		if (location == null) {
			// do nothing
			return;
		}

		// empty children
		if (children != null)
			children.clear();

		for (Location l : location.getChildren()) {
			if ("N".equals(l.getDeleted())) {
				LocationTreeNode node = new LocationTreeNode(l);
				this.add(node);
				// recursion
				node.build(privilegedLocationKeyList);
			}
		}

	}

	public boolean isRoot() {
		if (parent == null) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isLeaf() {
		if (children == null || children.size() == 0) {
			return true;
		} else {
			return false;
		}
	}

	public void add(LocationTreeNode node) {
		if (children == null) {
			children = new ArrayList<LocationTreeNode>();
		}
		children.add(node);
		node.setParent(this);
	}

	public String toString() {
		if (this.getLocation() != null) {
			return this.getLocation().getName();
		} else {
			return "";

		}
	}

	public LocationTreeNode getParent() {
		return parent;
	}

	public void setParent(LocationTreeNode parent) {
		this.parent = parent;
	}

	public List<LocationTreeNode> getChildren() {
		return children;
	}

	public int getLeafCount() {

		if (isLeaf())
			return 0;

		int count = 0;

		for (LocationTreeNode n : children) {
			if (n.isLeaf()) {
				count++;
			} else {
				// get child's leaf count
				count += n.getLeafCount();
			}
		}

		return count;
	}

	public boolean isPrivileged() {
		return privileged;
	}

	public boolean remove(LocationTreeNode node) {

		// remove node from its children

		if (node == null || children == null) {
			return false;
		} else {
			boolean result = children.remove(node);
			// result = true if node is in children list
			if (result == true) {
				/*
				 * Location's children is not removed so the full tree may be
				 * built again
				 */
				/*
				 * if (location != null && location.getChildren() != null) {
				 * location.getChildren().remove(node.getLocation()); }
				 */
				node.setParent(null);
			}
			return result;
		}
	}

	public void detach() {
		// remove itself from parent
		if (parent != null) {
			parent.remove(this);
		}
	}

	public void trim() {

		// trim unprivileged path

		// trim children
		if (children != null) {

			Iterator<LocationTreeNode> it = children.iterator();

			while (it.hasNext()) {

				LocationTreeNode node = (LocationTreeNode) it.next();
				// recursion
				node.trim();

				// check if this child has no children left
				if (node.isLeaf() && node.isPrivileged() == false) {
					// remove node
					it.remove();
					node.setParent(null);
				}

			}
		}
	}

	public LocationTreeNode getCurrentLocationTreeNode(String locationId) {

		if (locationId.equals(this.location.getKey().toString())) {
			return this;
		}

		List<LocationTreeNode> locationTreeNodeList = this.getChildren();

		LocationTreeNode currentLocationTreeNode = null;

		if (locationTreeNodeList != null) {
			for (int i = 0; i < locationTreeNodeList.size(); i++) {
				Location location = locationTreeNodeList.get(i).getLocation();
				if (currentLocationTreeNode == null) {
					if (locationId.equals(location.getKey().toString())) {
						currentLocationTreeNode = locationTreeNodeList.get(i);
						break;
					} else {
						currentLocationTreeNode = locationTreeNodeList.get(i).getCurrentLocationTreeNode(locationId);
					}
				} else {
					break;
				}
			}
		}

		return currentLocationTreeNode;

	}

	// TODO: add more functions if needed;

	// public List<LocationTreeNode>
	// getCurrentLocationTreeNodeList(LocationTreeNode locationTreeNode, int
	// parentId){
	//
	// }

}
