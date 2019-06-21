package org.brijframework.jdbc.container;

import org.brijframework.asm.container.AbstractContainer;
import org.brijframework.group.Group;
import org.brijframework.jdbc.factories.JdbcFactory;
import org.brijframework.jdbc.group.JdbcGroup;
import org.brijframework.support.model.Assignable;
import org.brijframework.util.reflect.InstanceUtil;
import org.brijframework.util.reflect.ReflectionUtils;

public class JdbcContainer extends AbstractContainer {

	private static JdbcContainer container;

	@Assignable
	public static JdbcContainer getContainer() {
		if (container == null) {
			container = new JdbcContainer();
		}
		return container;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void init() {
		try {
			ReflectionUtils.getClassListFromExternal().forEach(cls -> {
				if (JdbcFactory.class.isAssignableFrom(cls) && InstanceUtil.isAssignable(cls)) {
					register((Class<? extends JdbcFactory>) cls);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			ReflectionUtils.getClassListFromInternal().forEach(cls -> {
				if (JdbcFactory.class.isAssignableFrom(cls) && InstanceUtil.isAssignable(cls)) {
					register((Class<? extends JdbcFactory>) cls);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
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
