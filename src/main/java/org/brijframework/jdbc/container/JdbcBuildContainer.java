package org.brijframework.jdbc.container;

import org.brijframework.group.Group;
import org.brijframework.jdbc.factories.build.JdbcBuilderFactory;
import org.brijframework.jdbc.group.JdbcGroup;
import org.brijframework.support.config.Assignable;
import org.brijframework.support.config.DepandOn;
import org.brijframework.util.reflect.InstanceUtil;
import org.brijframework.util.reflect.ReflectionUtils;

@DepandOn(depand=JdbcSourceContainer.class)
public class JdbcBuildContainer extends JdbcContainer{

	private static JdbcBuildContainer container;

	@Assignable
	public static JdbcBuildContainer getContainer() {
		if (container == null) {
			container = new JdbcBuildContainer();
		}
		return container;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void init() {
		try {
			ReflectionUtils.getClassListFromExternal().forEach(cls -> {
				if (JdbcBuilderFactory.class.isAssignableFrom(cls) && InstanceUtil.isAssignable(cls)) {
					register((Class<? extends JdbcBuilderFactory>) cls);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			ReflectionUtils.getClassListFromInternal().forEach(cls -> {
				if (JdbcBuilderFactory.class.isAssignableFrom(cls) && InstanceUtil.isAssignable(cls)) {
					register((Class<? extends JdbcBuilderFactory>) cls);
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
