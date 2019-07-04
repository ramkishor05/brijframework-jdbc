package org.brijframework.jdbc.container;

import org.brijframework.group.Group;
import org.brijframework.jdbc.factories.model.JdbcMetaFactory;
import org.brijframework.jdbc.group.JdbcGroup;
import org.brijframework.support.config.Assignable;
import org.brijframework.support.config.DepandOn;
import org.brijframework.util.reflect.InstanceUtil;
import org.brijframework.util.reflect.ReflectionUtils;

@DepandOn(depand=JdbcSourceContainer.class)
public class JdbcMetaContainer  extends JdbcContainer {

	private static JdbcMetaContainer container;

	@Assignable
	public static JdbcMetaContainer getContainer() {
		if (container == null) {
			container = new JdbcMetaContainer();
		}
		return container;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void init() {
		try {
			ReflectionUtils.getClassListFromExternal().forEach(cls -> {
				if (JdbcMetaFactory.class.isAssignableFrom(cls) && InstanceUtil.isAssignable(cls)) {
					register((Class<? extends JdbcMetaFactory>) cls);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			ReflectionUtils.getClassListFromInternal().forEach(cls -> {
				if (JdbcMetaFactory.class.isAssignableFrom(cls) && InstanceUtil.isAssignable(cls)) {
					register((Class<? extends JdbcMetaFactory>) cls);
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
