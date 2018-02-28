package hk.ebsl.mfms.manager.impl;

import org.springframework.transaction.annotation.Transactional;

import hk.ebsl.mfms.dao.SequenceNumberDao;
import hk.ebsl.mfms.dto.SequenceNumber;
import hk.ebsl.mfms.exception.MFMSException;
import hk.ebsl.mfms.manager.SequenceNumberManager;

public class SequenceNumberManagerImpl implements SequenceNumberManager {
	
	SequenceNumberDao sequenceNumberDao;

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public synchronized int getAndUpdatePatrolGroupNum() {
		// TODO Auto-generated method stub
		
		SequenceNumber seq = sequenceNumberDao.getPatrolResultGroupNum();
		
		if(seq != null){
			seq.setValue(seq.getValue()+1);
			sequenceNumberDao.saveSequenceNumber(seq);
			
			return seq.getValue()-1;
		}
		
		return -1;
	}

	public SequenceNumberDao getSequenceNumberDao() {
		return sequenceNumberDao;
	}

	public void setSequenceNumberDao(SequenceNumberDao sequenceNumberDao) {
		this.sequenceNumberDao = sequenceNumberDao;
	}
	
	
	
}
