package hk.ebsl.mfms.manager.impl;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import hk.ebsl.mfms.dao.PunchCardDao;
import hk.ebsl.mfms.dto.PunchCard;
import hk.ebsl.mfms.exception.MFMSException;
import hk.ebsl.mfms.manager.PunchCardManager;

public class PunchCardManagerImpl implements PunchCardManager {
	public static final Logger logger = Logger
			.getLogger(PatrolResultManagerImpl.class);

	private PunchCardDao punchCardDao;
	@Override
	@Transactional (rollbackFor={MFMSException.class})
	public void save(PunchCard punchCard) {
		punchCardDao.save(punchCard);

	}
	public PunchCardDao getPunchCardDao() {
		return punchCardDao;
	}
	public void setPunchCardDao(PunchCardDao punchCardDao) {
		this.punchCardDao = punchCardDao;
	}

}
