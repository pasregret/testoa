package cn.itcast.dao;

import java.util.HashMap;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import cn.itcast.entity.Customer;
import cn.itcast.entity.Dict;

public class CustomerDaoImpl extends BaseDaoImpl<Customer> implements CustomerDao {

	
	
//
//	@SuppressWarnings("all")
//	@Override
//	public List<Customer> findAllPage(int begin, int pageSize) {
//		DetachedCriteria criteria = DetachedCriteria.forClass(Customer.class);
//		List<Customer> list =  (List<Customer>) this.getHibernateTemplate().findByCriteria(criteria, begin, pageSize);
//		return list;
//	}
//
	@SuppressWarnings("all")
	@Override
	public List<Customer> findConditain(Customer customer) {
	
		DetachedCriteria criteria = DetachedCriteria.forClass(Customer.class);
		criteria.add(Restrictions.like("custName", "%"+customer.getCustName()+"%"));
	
		List<Customer> list = 
				(List<Customer>) this.getHibernateTemplate().findByCriteria(criteria);
		return list;
	}
//多条件查询（推荐离线）
	@SuppressWarnings("unchecked")
	public List<Customer> findMoreCondition(Customer customer) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Customer.class);
	
		if(customer.getCustName()!=null && !"".equals(customer.getCustName())) {
		
			criteria.add(Restrictions.eq("custName", customer.getCustName()));
		}
		if(customer.getDictCusLevel().getDid()!=null && customer.getDictCusLevel().getDid()>0 ) {
			criteria.add(Restrictions.eq("dictCusLevel.did", customer.getDictCusLevel().getDid()));
		}
		if(customer.getCustSource()!=null && !"".equals(customer.getCustSource())) {
			criteria.add(Restrictions.eq("custSource", customer.getCustSource()));
		}
		
		return (List<Customer>) this.getHibernateTemplate().findByCriteria(criteria);
	}
	@SuppressWarnings("unchecked")
//
	public List<Dict> findAllDictLevel() {
		
		return (List<Dict>) this.getHibernateTemplate().find("from Dict");
	}
//
	public List<Customer> selectCustomerSource() {
		Session session = this.getSessionFactory().getCurrentSession();
	
		SQLQuery sqlQuery = session.createSQLQuery("SELECT COUNT(*) AS num,custSource FROM t_customer GROUP BY custSource");
		
	
		sqlQuery.setResultTransformer(Transformers.aliasToBean(HashMap.class));

		List list = sqlQuery.list();
		return list;
	}
//
	@SuppressWarnings("all")
	public List<Customer> selectCustomerLevel() {
		Session session = this.getSessionFactory().getCurrentSession();
		
		SQLQuery sqlQuery = session.createSQLQuery("SELECT c.num,d.dname FROM (SELECT COUNT(*) AS num,custLevel FROM t_customer GROUP BY custLevel) c , t_dict d WHERE c.custLevel=d.did");
		
	
		sqlQuery.setResultTransformer(Transformers.aliasToBean(HashMap.class));

		List list = sqlQuery.list();
		return list;
	}
	
	



	

}
