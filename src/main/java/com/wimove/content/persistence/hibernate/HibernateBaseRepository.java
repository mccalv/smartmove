/*
 * Copyright (C) 2008-2009  CiaoTech S.r.l (PnoGroup)
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * Created on 7 May 2009
 * @author mccalv
 * @version $Id$
 */
package com.wimove.content.persistence.hibernate;

import java.io.Serializable;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Required;

/**
 * A base HibernateRepository used just for injecting the session factory.
 * 
 * @author mccalv
 * @param <T>
 * 
 */
public abstract class HibernateBaseRepository {
	protected SessionFactory sessionFactory;

	/**
	 * @param sessionFactory
	 *            the sessionFactory to set
	 */
	@Required
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@SuppressWarnings("unchecked")
	protected  <T> T get(Class clazz,
			Serializable id) {
		return (T)sessionFactory.getCurrentSession().get(clazz, id);

	}

}
