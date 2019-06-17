package org.brijframework.jdbc.container;

import org.brijframework.asm.container.AbstractContainer;
import org.brijframework.data.container.DataContainer;
import org.brijframework.group.Group;
import org.brijframework.jdbc.group.JdbcGroup;
import org.brijframework.support.model.Assignable;
import org.brijframework.support.model.DepandOn;

@DepandOn(depand = DataContainer.class)
public class JdbcContainer extends AbstractContainer {

	private static JdbcContainer container;

	@Assignable
	public static JdbcContainer getContainer() {
		if (container == null) {
			container = new JdbcContainer();
		}
		return container;
	}

	@Override
	public Group load(Object groupKey) {
		Group group = get(groupKey);
		if (group == null) {
			group = new JdbcGroup(groupKey);
			this.add(groupKey, group);
			System.err.println("Group        : " + groupKey);
		}
		return group;
	}
}
