package hk.ebsl.mfms.dao;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.activation.MimetypesFileTypeMap;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import hk.ebsl.mfms.dto.CauseCode;
import hk.ebsl.mfms.dto.DefectEmail;
import hk.ebsl.mfms.dto.DefectPhoto;
import hk.ebsl.mfms.dto.DefectVideo;
import hk.ebsl.mfms.dto.Tool;
import hk.ebsl.mfms.utility.FileBucket;
import hk.ebsl.mfms.web.controller.DefectController.ToolComparator;

public class DefectEmailDao extends BaseDao {

	public static final Logger logger = Logger.getLogger(DefectEmailDao.class);

	public void saveDefectEmail(DefectEmail defectEmail) {

		Session currentSession = getSession();
		currentSession.saveOrUpdate(defectEmail);

	}

	@SuppressWarnings("unchecked")
	public List<DefectEmail> searchDefectEmail(Integer defectKey, String email, String type, String result) {

		Criteria criteria = getDefaultCriteria().add(Restrictions.eq("defectKey", defectKey));

		if (email != null) {
			criteria.add(Restrictions.eq("email", email));
		}
		
		if (type != null) {
			criteria.add(Restrictions.eq("type", type));
		}
		
		if (result != null) {
			criteria.add(Restrictions.eq("result", result));
		}
		
		List<DefectEmail> list = criteria.list();
		
		return list;
	}

	private Criteria getDefaultCriteria() {

		Session currentSession = getSession();
		return currentSession.createCriteria(DefectEmail.class);
	}

}
