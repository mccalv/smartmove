package com.closertag.smartmove.server.content.persistence.hibernate;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.property.ChainedPropertyAccessor;
import org.hibernate.property.PropertyAccessor;
import org.hibernate.property.PropertyAccessorFactory;
import org.hibernate.property.Setter;
import org.hibernate.transform.ResultTransformer;

import com.closertag.smartmove.server.content.domain.Item;

/**
 * Result transformer that allows to transform a result to a user specified
 * class which will be populated via setter methods or fields matching the alias
 * names.
 * 
 * <pre>
 * List resultWithAliasedBean = s.createCriteria(Enrolment.class).createAlias(
 * 		&quot;student&quot;, &quot;st&quot;).createAlias(&quot;course&quot;, &quot;co&quot;).setProjection(
 * 		Projections.projectionList().add(
 * 				Projections.property(&quot;co.description&quot;), &quot;courseDescription&quot;))
 * 		.setResultTransformer(
 * 				new AliasToBeanResultTransformer(StudentDTO.class)).list();
 * StudentDTO dto = (StudentDTO) resultWithAliasedBean.get(0);
 * </pre>
 * 
 * @author max
 * 
 */
public class ItemResultTrasformer implements ResultTransformer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8947011130111215982L;
	private final Class resultClass;
	private Setter[] setters;
	private PropertyAccessor propertyAccessor;

	public ItemResultTrasformer() {
		
		this.resultClass = Item.class;
		propertyAccessor = new ChainedPropertyAccessor(new PropertyAccessor[] {
				PropertyAccessorFactory.getPropertyAccessor(this.resultClass, null),
				PropertyAccessorFactory.getPropertyAccessor("field") });
	}

	public Object transformTuple(Object[] tuple, String[] aliases) {
		Object result;

		try {
			if (setters == null) {
				setters = new Setter[aliases.length];
				for (int i = 0; i < aliases.length; i++) {
					
					String alias = StringUtils.replace(aliases[i],"item","");
					 alias = StringUtils.replace(alias,"_this.","");
					if (alias != null) {
						setters[i] = propertyAccessor.getSetter(resultClass,
								alias);
					}
				}
			}
			result = resultClass.newInstance();

			for (int i = 0; i < aliases.length; i++) {
				if (setters[i] != null) {
					setters[i].set(result, tuple[i], null);
				}
			}
		} catch (InstantiationException e) {
			throw new HibernateException("Could not instantiate resultclass: "
					+ resultClass.getName());
		} catch (IllegalAccessException e) {
			throw new HibernateException("Could not instantiate resultclass: "
					+ resultClass.getName());
		}

		return result;
	}

	public List transformList(List collection) {
		return collection;
	}

}